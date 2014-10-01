package vn.onepay.search.controller;

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
import org.springframework.data.elasticsearch.core.facet.result.Term;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import vn.onepay.account.model.Account;
import vn.onepay.card.charging.entities.ChargeStatus;
import vn.onepay.search.elastic.ElasticSearch;
import vn.onepay.web.secure.controllers.AbstractProtectedController;

public class CardCdrController extends AbstractProtectedController{

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
		Account account = (Account)request.getSession().getAttribute("account_logined");
		
		model.put("status", Arrays.asList(new Integer[]{0, 1, 2}));
		
		//Option param 
		String key = StringUtils.trimToEmpty(request.getParameter("key"));
		String reservation = StringUtils.trimToEmpty(request.getParameter("reservation"));
		String statusStr = StringUtils.trimToEmpty(request.getParameter("status_filter"));
		
		//(KEY PARAMS*)
		@SuppressWarnings("unchecked")
		Map<String , String> fieldMaps = new LinkedMap();
		fieldMaps.put("merchant", "Merchant");
		fieldMaps.put("paymentProvider", "Nhà cung cấp");
		fieldMaps.put("type", "Loại thẻ");
		fieldMaps.put("amount", "Mệnh giá");
		fieldMaps.put("status", "Trạng thái");
		model.put("fieldMaps", fieldMaps);
		
		//( KEY FIELDS*)
		List<String> fields = new ArrayList<String>();
		fields.add("merchant");
		fields.add("paymentProvider");
		fields.add("type");
		fields.add("amount");
		fields.add("status");
		
		List<String> terms = new ArrayList<String>();
		//Auto param 
		for(String field : fieldMaps.keySet()){
			
			String param = StringUtils.trimToEmpty(request.getParameter(field));
			terms.add(param);
		}
		
	    //(Regex Search*)
	    @SuppressWarnings("unchecked")
		Map<String , List<String>> keywords = new LinkedMap();
	    
	    if(!key.equals("")){
	    	String operator = "_operator_regex";
	    	
	    	keywords.put("merchant" + operator, Arrays.asList(new String[]{key}) );
	    	keywords.put("type" + operator, Arrays.asList(new String[]{key}) );
	    	keywords.put("serial" + operator, Arrays.asList(new String[]{key}) );
	    	keywords.put("paymentProvider" + operator, Arrays.asList(new String[]{key}) );
	    }
	    
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
	    //Operator IN: Status filter
	    List<String> statusList = new ArrayList<String>();
	    int status = -1;
	    
	    if(!statusStr.equals("") && StringUtils.isNumeric(statusStr)){
	    	status = Integer.parseInt(statusStr);
	    	
	    	//success
		    if(status == 0)
		    	statusList = Arrays.asList(new String[]{"00"});
		    else
		    	//error
		    	if(status == 1)
			    	statusList.addAll(ChargeStatus.ALL_CHARGING_ERROR_STATUS);
		    	else
		    		//wrong
				    if(status == 2)
				    	statusList.addAll(ChargeStatus.ALL_CHARGING_WRONG_STATUS);
		    
		}
	    System.out.println("status: " + status + " " + statusList);
	    if(statusList != null && statusList.size() > 0){
	    	String operator = "_operator_in";
    		keywords.put("status" + operator, statusList);
	    }
	    
	    //Operator TIME RANGE: filter with time reservation
	    String fromDateStr = "";
		String toDateStr = "";
		if(!reservation.equals("")){
			try{
				String[] reservationArr = reservation.split("-");
				fromDateStr = StringUtils.trimToEmpty(reservationArr[0]);
				toDateStr = StringUtils.trimToEmpty(reservationArr[1]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(!fromDateStr.equals("") || !toDateStr.equals("") ){
		    List<String> timeRanges = Arrays.asList(new String[]{fromDateStr, toDateStr}); //"06/08/2014", "07/08/2014"
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
	    
		int offset = 0;
		int page = 0;
	      if (StringUtils.isNumeric(request.getParameter("d-49520-p"))) {
	    	  offset = Integer.parseInt(request.getParameter("d-49520-p"));
	    	  page = Integer.parseInt(request.getParameter("d-49520-p")) - 1;
	        if (offset > 0) {
	          offset = (offset - 1) * this.limit;
	        }
	      }
	    
	      //(List facets*)
		  List<List<Term>> termLists = new ArrayList<List<Term>>();
		  //List Object
	      List<vn.onepay.search.entities.CardCdr> cardCdrList = new ArrayList<vn.onepay.search.entities.CardCdr>();
	      //Count
	      int count = 0;
	      
	      //Facet size to view
	      int facetSize = 20;
	      
	      if(elasticSearch.checkIndex(vn.onepay.search.entities.CardCdr.class)){
	    	    count = elasticSearch.count(fields, terms, keywords, facetSize, vn.onepay.search.entities.CardCdr.class);
	    	    termLists = elasticSearch.getFacets(fields, terms, keywords, facetSize, vn.onepay.search.entities.CardCdr.class);
				cardCdrList = elasticSearch.search(fields, terms, keywords, sorts, page, limit, facetSize, vn.onepay.search.entities.CardCdr.class);
				
				
	      }
	    
	    System.out.println("COUNT: "+count);
	    
		model.put("total", count);
		
		//(*)
		//push to layout
		@SuppressWarnings("unchecked")
		Map<String , List<Term>> facetsMap = new LinkedMap();
		int k = 0;
		for(String field : fieldMaps.keySet()){
			
			if(termLists.size() > k)
				facetsMap.put(field, termLists.get(k));
			
			k++;
		}

		model.put("facetsMap", facetsMap);
			
		model.put("pagesize", Integer.valueOf(this.limit));
	    model.put("offset", Integer.valueOf(offset));
	    model.put("list", cardCdrList);
	      
	    Date end = new Date();
	    Long duration = end.getTime() - start.getTime();
	    Long timeHandleTotal = TimeUnit.MILLISECONDS.toMillis(duration);
	      
        model.put("timeHandleTotal", timeHandleTotal);
	      
		return new ModelAndView(getWebView(), "model", model);
	}

}
