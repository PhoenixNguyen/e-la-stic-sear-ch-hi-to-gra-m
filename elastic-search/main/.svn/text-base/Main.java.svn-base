package main;
import java.util.ArrayList;
import java.util.List;

import main.repositories.CardCdrService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import vn.onepay.card.dao.CardCdrDAO;
import vn.onepay.card.model.CardCdr;
//import vn.onepay.search.elastic.ElasticSearch;


public class Main {
	public static void main(String [] args){
		Main main = new Main();
		
		//Use template
		//main.cardIndexTemplate();
		
		//Use Repository
		main.cardIndexRepository();
		
	}

	private void cardIndexRepository() {
		@SuppressWarnings("resource")
		ApplicationContext ctx1 = new ClassPathXmlApplicationContext("/main/elastic-repository-config.xml");
		CardCdrService cardCdrService = (CardCdrService)ctx1.getBean("cardCdrService");
		ElasticsearchTemplate elasticsearchTemplate = (ElasticsearchTemplate) ctx1.getBean("elasticsearchTemplate");
		
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/main/mongo-config.xml");
		CardCdrDAO cardCdrDAO= (CardCdrDAO) ctx.getBean("cardCdrDAO");
		List<CardCdr> cardCdrList = cardCdrDAO.findAllCardCdrs();
	  	  
	  	if(cardCdrList == null || cardCdrList.size() == 0){
  		  return;
  	    }
	  	
	  	System.out.println("Size: " + cardCdrList.size());
	  	
	  	//delete all data
	  	cardCdrService.deleteAll();
	  	
	  	//delete index
	  	elasticsearchTemplate.deleteIndex(vn.onepay.search.entities.CardCdr.class);
	  	
	    //INDEX
	    if(!elasticsearchTemplate.indexExists(vn.onepay.search.entities.CardCdr.class)){
    	  System.out.println("Dang danh chi muc ...");
    	  
    	  List<vn.onepay.search.entities.CardCdr> objList = new ArrayList<vn.onepay.search.entities.CardCdr>();
    	  List<String> ids = new ArrayList<String>();
    	  for(CardCdr card : cardCdrList){
    		  ids.add(card.getId());
    		  objList.add(new vn.onepay.search.entities.CardCdr(card.getId(), card.getAmount(), card.getMerchant(), card.getPaymentProvider(),
    				  card.getApp_code(), card.getPin(), card.getSerial(), card.getType(), 
    				  card.getStatus(), card.getMessage(), card.getTimestamp(), card.getExtractStatus()));
    		  
	  	  }
    	  
    	  //cardCdrService.bulkSave(objList); too much
    	  int MAX = 100000;
    	  int times = objList.size()/MAX;
    	  for(int i = 0; i <= times; i++){
    		  
    		  if( i != times){
    			  System.out.println("Danh chi muc lan " + (i + 1) + " tu " + i * MAX + " den " + (MAX*(i+1) - 1) + " ...");
    			  cardCdrService.bulkSave(objList.subList(i * MAX, MAX*(i+1)));
    		  }
    		  else{
    			  System.out.println("Danh chi muc lan " + (i + 1) + " tu " + i * MAX + " den " + (objList.size() - 1) + " ...");
    			  cardCdrService.bulkSave(objList.subList(i * MAX, objList.size()));
    		  }
    		  
    		  System.out.println("		Hoan thanh lan " + (i + 1));
    	  }
    	  
    	  System.out.println("Hoan thanh tat ca");
	    }
	    else{
    	  System.out.println("Da ton tai chi muc");
        }
	}

//	private void cardIndexTemplate() {
//		ApplicationContext ctx1 = new ClassPathXmlApplicationContext("/vn/onepay/search/resources/elastic-config.xml");
//		ElasticSearch elasticSearch = (ElasticSearch) ctx1.getBean("elasticSearch");
//		
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("/main/mongo-config.xml");
//		CardCdrDAO cardCdrDAO= (CardCdrDAO) ctx.getBean("cardCdrDAO");
//		
//		List<CardCdr> cardCdrList = cardCdrDAO.findAllCardCdrs();
//  	  
//	  	  if(cardCdrList != null){
//	  		  System.out.println("Size: " + cardCdrList.size());
//	  	  }
//	  	  
//		  elasticSearch.deleteIndex(vn.onepay.search.entities.CardCdr.class);
//	      //INDEX
//	      if(!elasticSearch.checkIndex(vn.onepay.search.entities.CardCdr.class) ){
//	    	  System.out.println("Dang danh chi muc ...");
//	    	  //cardCdrElasticSearch.deleteIndex();
//	    	  //List<CardCdr> cardCdrList = cardCdrDAO.findAllCardCdrs();
//	    	  
//	    	  if(cardCdrList != null){
//	    		  System.out.println("Size: " + cardCdrList.size());
//	    	  }
//	    	  if(cardCdrList == null || cardCdrList.size() == 0){
//	    		  return;
//	    	  }
//	    	  
//	    	  List<vn.onepay.search.entities.CardCdr> objList = new ArrayList<vn.onepay.search.entities.CardCdr>();
//	    	  List<String> ids = new ArrayList<String>();
//	    	  for(CardCdr card : cardCdrList){
//	    		  ids.add(card.getId());
//	    		  objList.add(new vn.onepay.search.entities.CardCdr(card.getId(), card.getAmount(), card.getMerchant(), card.getPaymentProvider(),
//	    				  card.getApp_code(), card.getPin(), card.getSerial(), card.getType(), 
//	    				  card.getStatus(), card.getMessage(), card.getTimestamp(), card.getExtractStatus()));
//	    		  
//	    	  }
//	    	  
//	    	  for(int i = 0; i <= objList.size()/20000; i++){
//	    		  System.out.println("Danh chi muc lan " + (i + 1));
//	    		  elasticSearch.bulkIndex(ids, objList.subList(i, 20000*(i+1)));
//	    	  }
//	    	  
//	    	  
//	    	  System.out.println("Hoan thanh");
//	      }
//	      else{
//	    	  System.out.println("Da ton tai chi muc");
//	      }
//		
//	}
}
