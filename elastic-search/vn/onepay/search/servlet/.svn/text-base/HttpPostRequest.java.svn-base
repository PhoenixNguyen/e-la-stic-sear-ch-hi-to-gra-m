package vn.onepay.search.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import vn.onepay.common.MapUtil;
import vn.onepay.search.elastic.ElasticSearch;
import vn.onepay.search.entities.CardCdr;
import vn.onepay.service.ServiceFinder;

@SuppressWarnings("serial")
public class HttpPostRequest extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ElasticSearch elasticSearch = ServiceFinder.getContext(req).getBean("elasticSearch", ElasticSearch.class);
		
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		//response
    	JSONObject jsonObject = new JSONObject();
    	
		System.out.println("Welcome");
		//query = ?id=541a49460cf2560687b68af199&amount=50000&merchant=changic&paymentProvider=ahha_c_15_1&app_code=bnc&pin=8592515236179&serial=54429823652&type=viettel&status=00&message=01|Kiem tra thanh cong&timestamp=1409479229528&extractStatus=true
		
		Map<String, String> params = MapUtil.httpRequestParameterToMap(req);
		String id = StringUtils.trimToEmpty(params.get("id"));
		String amount = StringUtils.trimToEmpty(params.get("amount"));
	    String merchant = StringUtils.trimToEmpty(params.get("merchant"));
	    String paymentProvider = StringUtils.trimToEmpty(params.get("paymentProvider"));
	    String app_code = StringUtils.trimToEmpty(params.get("app_code"));
	    String pin = StringUtils.trimToEmpty(params.get("pin"));
	    String serial = StringUtils.trimToEmpty(params.get("serial"));
	    String type = StringUtils.trimToEmpty(params.get("type"));
	    String status = StringUtils.trimToEmpty(params.get("status"));
	    String message = StringUtils.trimToEmpty(params.get("message"));

	    String timestamp = StringUtils.trimToEmpty(params.get("timestamp"));
	    String extractStatus  = StringUtils.trimToEmpty(params.get("extractStatus"));
	    
	    //Check valid
	    if(checkValidate(Arrays.asList(new String []{id, amount, merchant, paymentProvider, app_code, 
	    		pin, serial, type, status, message, timestamp, extractStatus}))){
	    
		    Date timestampValue = null;
		    int amountValue = 0;
		    boolean extractStatusValue = false;
		    
		    try{
		    	amountValue = Integer.parseInt(amount);
		    	
		    	timestampValue = new Date(Long.parseLong(timestamp));
		    	
		    	extractStatusValue = Boolean.parseBoolean(extractStatus);
		    	
		    	CardCdr cardCdr = new CardCdr(id, amountValue, merchant, paymentProvider, app_code, 
			    		pin, serial, type, status, message, timestampValue, extractStatusValue);
		    	
		    	if(elasticSearch.checkIndex(CardCdr.class)){
					if(!elasticSearch.exist(cardCdr.getId(), CardCdr.class)){
						elasticSearch.index(cardCdr.getId(), cardCdr);
						System.out.print("indexed");
						
						jsonObject.put("status", "Đẩy dữ liệu thành công");
				    	out.append(jsonObject.toString());
					}
					else{
						System.out.print("existed");
						jsonObject.put("status", "Đã tồn tại thẻ");
				    	out.append(jsonObject.toString());
					}
				}
		    	
		    	
		    	
		    	
		    }
		    catch(Exception e){
		    	e.printStackTrace();
		    	resp.sendError(403);
		    }
		    out.flush();
			out.close();
	    }
	    else{
	    	try {
				jsonObject.put("status", "Query sai định dạng");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	out.append(jsonObject.toString());
	    	out.flush();
			out.close();
			
	    	//resp.sendError(403);
	    }
	    
	}
	
	private boolean checkValidate(List<String> params) {
		// TODO Auto-generated method stub
		if(params.contains(""))
			return false;
		else
			return true;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
