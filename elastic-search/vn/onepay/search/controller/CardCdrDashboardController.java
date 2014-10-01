package vn.onepay.search.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.facet.result.IntervalUnit;
import org.springframework.data.elasticsearch.core.facet.result.Term;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import vn.onepay.account.model.Account;
import vn.onepay.card.charging.entities.ChargeStatus;
import vn.onepay.search.elastic.ElasticSearch;
import vn.onepay.web.secure.controllers.AbstractProtectedController;

public class CardCdrDashboardController extends AbstractProtectedController{
	
	public static String ALL_STATUS = "allStatus";
	public static String SUCCESS_STATUS = "successStatus";
	public static String WRONG_STATUS = "wrongStatus";
	public static String ERROR_STATUS = "errorStatus";
	
	public ElasticSearch elasticSearch;
	public ElasticSearch getElasticSearch() {
		return elasticSearch;
	}

	public void setElasticSearch(ElasticSearch elasticSearch) {
		this.elasticSearch = elasticSearch;
	}

	private int limit;
	public void setLimit(int limit) {
	    this.limit = limit;
	}
	
	@Override
	protected ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {
		Date start = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Account account = (Account)request.getSession().getAttribute("account_logined");
		
		model.put("status", Arrays.asList(new Integer[]{0, 1, 2}));
		
		//Option param 
		String time_search = StringUtils.trimToEmpty(request.getParameter("time_search"));
		
		//(KEY PARAMS*)
		@SuppressWarnings("unchecked")
		Map<String , String> fieldMaps = new LinkedMap();
		fieldMaps.put("type", "Loại thẻ");
		fieldMaps.put("paymentProvider", "Nhà cung cấp");
		fieldMaps.put("merchant", "Merchant");
		fieldMaps.put("status", "Trạng thái");
		model.put("fieldMaps", fieldMaps);
		
		//( KEY FIELDS*)
		List<String> fields = new ArrayList<String>();
		fields.add("type");
		fields.add("paymentProvider");
		fields.add("merchant");
		fields.add("status");
		
		//terms and fields have a size is equals
		List<String> terms = new ArrayList<String>();
		
	    //(Regex Search*)
	    @SuppressWarnings("unchecked")
		Map<String , List<String>> keywords = new LinkedMap();
	  //Filter histograms
	    List<String> filter_merchant = null;
	    List<String> filter_provider = null;
	    List<String> filter_card_type = null;
	    
	    if(request.getParameterValues("filter_merchant") != null){
	    	String merchantParam= StringUtils.trimToEmpty(request.getParameter("filter_merchant"));
	    	filter_merchant = Arrays.asList(new String[]{merchantParam});
	    }
	    if(request.getParameterValues("filter_provider") != null)
	    	filter_provider = Arrays.asList(request.getParameterValues("filter_provider"));
	    if(request.getParameterValues("filter_card_type") != null)
	    	filter_card_type = Arrays.asList(request.getParameterValues("filter_card_type"));
	    
	  /*//Auto param 
  		for(String field : fieldMaps.keySet()){
  			
  			String param = StringUtils.trimToEmpty(request.getParameter(field));
  			terms.add(param);
  		}*/
	    
	    //click on facet
	    if(StringUtils.isNotBlank(request.getParameter("type"))){
			terms.add(StringUtils.trimToEmpty(request.getParameter("type")));
	    }
	    else{
	    	terms.add("");
		    //Operator IN:
		    if(filter_card_type != null && filter_card_type.size() > 0){
		    	String operator = "_operator_in";
		    	keywords.put("type" + operator, filter_card_type);
		    }
	    }
	    //click on facet
	    if(StringUtils.isNotBlank(request.getParameter("paymentProvider"))){
			terms.add(StringUtils.trimToEmpty(request.getParameter("paymentProvider")));
	    }
	    else{
	    	terms.add("");
		    //Operator IN:
		    if(filter_provider != null && filter_provider.size() > 0){
		    	String operator = "_operator_in";
		    	keywords.put("paymentProvider" + operator, filter_provider);
		    }
	    }
	    //click on facet
	    if(StringUtils.isNotBlank(request.getParameter("merchant"))){
			terms.add(StringUtils.trimToEmpty(request.getParameter("merchant")));
	    }
	    else{
	    	terms.add("");
	    	//Operator Regex:
		    if(filter_merchant != null && filter_merchant.size() > 0){
		    	String operator = "_operator_regex";
		    	keywords.put("merchant" + operator, filter_merchant );
		    }
	    }
	    //status
	    terms.add("");
	    //End Filter histograms
	    
	    //Operator IN: Merchant manager permissions
	    List<String> merchants = null;
	    if(account.isOperator())
	    	merchants = null;
	    else
	    	if(account.isMerchantManager())
		    	merchants = Arrays.asList(new String[]{account.getUsername(), "mwork", "tritueviet", "dungtt"});
	    	else
			    if(account.isMerchant())
			    	merchants = Arrays.asList(new String[]{account.getUsername()});
			    else
			    	merchants = Arrays.asList(new String[]{""});
	    
	    if(merchants != null && merchants.size() > 0){
	    	String operator = "_operator_in";
    		keywords.put("merchant" + operator, merchants);
	    }
	    
	    //Operator time range:
	    if(!time_search.equals("")){
		    List<String> timeRanges = Arrays.asList(new String[]{time_search, time_search}); //"06/08/2014", "07/08/2014"
		    if(timeRanges != null && timeRanges.size() > 0){
		    	String operator = "_operator_time_range";
		    	keywords.put("timestamp" + operator, timeRanges);
		    }
		}else{
			String today = df.format(new Date());
			System.out.println("today: "+today);
			
			List<String> timeRanges = Arrays.asList(new String[]{today, today});
		    if(timeRanges != null && timeRanges.size() > 0){
		    	String operator = "_operator_time_range";
		    	keywords.put("timestamp" + operator, timeRanges);
		    }
		}
	    
	    //(Sort *)
	    @SuppressWarnings("unchecked")
		Map<String , SortOrder> sorts = new LinkedMap();
	    sorts.put("timestamp", SortOrder.DESC);
	    sorts.put("amount", SortOrder.ASC);
	    
	      //(List facets*)
		  List<List<Term>> termLists = new ArrayList<List<Term>>();
		  //list all facets
		  List<List<Term>> termAllLists = new ArrayList<List<Term>>();
		  
		  @SuppressWarnings("unchecked")
		  Map<String , List<IntervalUnit>> statusHistogramMap = new LinkedMap();
		  //field filter
		  List<String> fieldHistogram = new ArrayList<String>(); 
		  fieldHistogram.add(ALL_STATUS);
		  fieldHistogram.add(SUCCESS_STATUS);
		  fieldHistogram.add(WRONG_STATUS);
		  fieldHistogram.add(ERROR_STATUS);
		  
	      //Count
	      int count = 0;
	      
	      //Facet size to view
	      int facetSize = 20;
	      
	      if(elasticSearch.checkIndex(vn.onepay.search.entities.CardCdr.class)){
	    	    count = elasticSearch.count(fields, terms, keywords, facetSize, vn.onepay.search.entities.CardCdr.class);
	    	    termAllLists = elasticSearch.getFacets(fields, null, null, facetSize, vn.onepay.search.entities.CardCdr.class);
	    	    
	    	    termLists = elasticSearch.getFacets(fields, terms, keywords, facetSize, vn.onepay.search.entities.CardCdr.class);
				
	    	    //get histogram
	    	    String field = "timestamp";
	    	    String fieldFilter = "status";
	    	    String operator = "_operator_in";
	    	    for(String status : fieldHistogram){
	    	    	if(status.equalsIgnoreCase(ALL_STATUS)){
	    	    		statusHistogramMap.put(status, elasticSearch.getHistogramFacet(field, fields, terms, keywords, facetSize, vn.onepay.search.entities.CardCdr.class));
	    	    	}else
	    	    		if(status.equalsIgnoreCase(SUCCESS_STATUS)){
	    	        		keywords.put(fieldFilter + operator, Arrays.asList(new String[]{ChargeStatus.SUCCESS_STATUS}));
	    	        		statusHistogramMap.put(status, elasticSearch.getHistogramFacet(field, fields, terms, keywords, facetSize, vn.onepay.search.entities.CardCdr.class));
		    	    	}else
		    	    		if(status.equalsIgnoreCase(WRONG_STATUS)){
		    	    			keywords.put(fieldFilter + operator, ChargeStatus.ALL_CHARGING_WRONG_STATUS);
		    	    			statusHistogramMap.put(status, elasticSearch.getHistogramFacet(field, fields, terms, keywords, facetSize, vn.onepay.search.entities.CardCdr.class));
			    	    	}else
			    	    		if(status.equalsIgnoreCase(ERROR_STATUS)){
			    	    			keywords.put(fieldFilter + operator, ChargeStatus.ALL_CHARGING_ERROR_STATUS);
			    	    			statusHistogramMap.put(status, elasticSearch.getHistogramFacet(field, fields, terms, keywords, facetSize, vn.onepay.search.entities.CardCdr.class));
				    	    	}
	    	    	
	    	    }
				
	      }
	    
		//(*)
		//push to layout
		@SuppressWarnings("unchecked")
		Map<String , List<Term>> facetsMap = new LinkedMap();
		@SuppressWarnings("unchecked")
		Map<String , List<Term>> facetAllsMap = new LinkedMap();
		
		int k = 0;
		for(String field : fieldMaps.keySet()){
			//filter
			if(termLists.size() > k)
				facetsMap.put(field, termLists.get(k));
			
			//All
			if(termAllLists.size() > k)
				facetAllsMap.put(field, termAllLists.get(k));
			
			k++;
		}
		
		System.out.println("COUNT: "+count);
		model.put("total", count);
		model.put("facetsMap", facetsMap);
		model.put("facetAllsMap", facetAllsMap);
		model.put("statusHistogramMap", statusHistogramMap);
		
	    Date end = new Date();
	    Long duration = end.getTime() - start.getTime();
	    Long timeHandleTotal = TimeUnit.MILLISECONDS.toMillis(duration);
	      
        model.put("timeHandleTotal", timeHandleTotal);
	      
		return new ModelAndView(getWebView(), "model", model);
	}

}
