package vn.onepay.search.elastic.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.facet.request.HistogramFacetRequestBuilder;
import org.springframework.data.elasticsearch.core.facet.request.TermFacetRequestBuilder;
import org.springframework.data.elasticsearch.core.facet.result.HistogramResult;
import org.springframework.data.elasticsearch.core.facet.result.IntervalUnit;
import org.springframework.data.elasticsearch.core.facet.result.Term;
import org.springframework.data.elasticsearch.core.facet.result.TermResult;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import vn.onepay.search.elastic.ElasticSearch;

public class ElasticSearchImpl implements ElasticSearch{
	public static String QUERY_FACET = "facet";
	public static String QUERY_HISTOGRAM = "histogram";
	public static long INTERVAL_TIME = 6*60*1000;
	
	ElasticsearchTemplate elasticsearchTemplate;
	
	public ElasticsearchTemplate getElasticsearchTemplate() {
		return elasticsearchTemplate;
	}

	public void setElasticsearchTemplate(ElasticsearchTemplate elasticsearchTemplate) {
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	@Override
	public <T> List<List<Term>> getFacets(List<String> fields, List<String> terms, Map<String , List<String>> keywords, int facetSize, Class<T> clazz) {
		
		List<List<Term>> termLists = new ArrayList<List<Term>>();
		
		if(fields == null)
			return termLists;
		
		
			int i = 0;
			for(String f : fields){
				if(terms == null){
					int countAll = countAllData(clazz);
					termLists.add(i , getFacets(f, fields, terms, keywords, countAll, clazz));
				}
				else{
					if(!terms.get(i).equals("")){
						
						termLists.add(i, Arrays.asList(new Term(terms.get(i), getTotalRecord(fields, terms, keywords, facetSize, clazz))));
						//termLists.get(i).add(new Term(terms.get(i), 0));
						
					}
					else{
						List<String> termTemps = new ArrayList<String>();
						termTemps.addAll(terms);
						termTemps.set(i, "");
						
						termLists.add(i , getFacets(f, fields, termTemps, keywords, facetSize, clazz));
						
						//test
		//				if(termLists.get(i).size() > 0)
		//					System.out.println(termLists.get(i).get(0).getTerm() + " " +termLists.get(i).get(0).getCount());
					}
				}
				i++;
			}
		
		return termLists;
	}
	
	
	private <T> List<Term> getFacets(String field, List<String> fields, List<String> terms,  Map<String , List<String>> keywords, int facetSize, Class<T> clazz){
		
        TermResult facet = (TermResult) elasticsearchTemplate.queryForPage(queryString(field, fields, terms, keywords, null, 0, 0, facetSize, QUERY_FACET),  clazz).getFacet(field);
        
        return facet.getTerms();
        
	}
	
	public <T> List<IntervalUnit> getHistogramFacet(String field, List<String> fields, List<String> terms,  Map<String , List<String>> keywords, int facetSize, Class<T> clazz){
		HistogramResult facet = (HistogramResult) elasticsearchTemplate.queryForPage(queryString(field, fields, terms, keywords, null, 0, 0, facetSize, QUERY_HISTOGRAM), clazz).getFacet(field);
		
		List<IntervalUnit> unitList =  facet.getIntervalUnit();
		// For each entry
		//SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		/*for (IntervalUnit entry : unitList) {
			System.out.println("key: " + df.format(new Date(entry.getKey()))+ " count: " + entry.getCount());
		       // Key (X-Axis)
		       // Doc count (Y-Axis)
		}*/
		
        return unitList;
        
	}

	public <T> List<T> search(List<String> fields, List<String> terms, Map<String , List<String>> keywords, Map<String , SortOrder> sorts, int page, int size, int facetSize, Class<T> clazz){
		
		Iterable<T> resultIterable = elasticsearchTemplate.queryForPage(queryString("", fields, terms, keywords, sorts, page, size, facetSize, QUERY_FACET),  clazz);
		
		List<T> resultList = new ArrayList<T>();
		
		CollectionUtils.addAll(resultList, resultIterable.iterator());
		
		return resultList;
		
	}
	
	public <T> int count(List<String> fields, List<String> terms, Map<String , List<String>> keywords, int facetSize, Class<T> clazz){
		//return elasticsearchTemplate.count(queryString("", fields, terms, keywords, sorts, 0, 0),  clazz);
		
		return getTotalRecord(fields, terms, keywords, facetSize, clazz);
		
	}
	
	private SearchQuery queryString(String field, List<String> fields, List<String> terms, Map<String , List<String>> keywords, Map<String , SortOrder> sorts, int page, int size, int facetSize, String typeQuery) {
		
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(matchAllQuery());
		
		//And
		List<FilterBuilder> filterBuildersAnd = new ArrayList<FilterBuilder>();
		if(terms != null){
			int i = 0;
			for(String f : fields){
				if(StringUtils.isNotBlank(terms.get(i))){
					filterBuildersAnd.add(FilterBuilders.termFilter(f, terms.get(i)) );
				}
				i++;
			}
		}
		//Regex
		List<FilterBuilder> filterBuildersRegex = new ArrayList<FilterBuilder>();
		
		//In
		@SuppressWarnings("unchecked")
		Map<String, List<String>> inMap = new LinkedMap();
		@SuppressWarnings("unchecked")
		Map<String, List<String>> timeRangeMap = new LinkedMap();
		
		if(keywords != null){
			Set<String> keys = keywords.keySet();
			if(keys.size() > 0)
				for(String key : keys){
					String startKey[] = key.split("_operator_");
					
					String keyField = startKey[0];
					String operator = startKey[1];
					
					List<String> values = keywords.get(key);
					System.out.println(keyField + " " + values);
					
					if(values != null && values.size() > 0){
						if(operator.equals("regex"))
							filterBuildersRegex.add(FilterBuilders.regexpFilter(keyField, ".*" +values.get(0) + ".*"));
						else
							if(operator.equals("in")){
								inMap.put(keyField, values);
							}
							else
								if(operator.equals("time_range")){
									timeRangeMap.put(keyField, values);
								}
					}
				}
		}
		
		//In
		if(inMap != null && inMap.size() > 0){
			List<FilterBuilder> filterBuildersIn = new ArrayList<FilterBuilder>();
			for(String key : inMap.keySet()){
				System.out.println(key + " " + inMap.get(key));
				filterBuildersIn.add(FilterBuilders.inFilter(key, inMap.get(key)));
			}
			filterBuildersAnd.addAll(filterBuildersIn);
		}
		
		//Time range
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if(timeRangeMap != null && timeRangeMap.size() > 0){
			for(String key : timeRangeMap.keySet()){
				System.out.println("Time Range: size = " +timeRangeMap.size() + " " + key + " " + timeRangeMap.get(key));
				try {
					String from = timeRangeMap.get(key).get(0);
					String to = timeRangeMap.get(key).get(1);
					
					Date fromDate = null;
					Date toDate = null;
					
					if(!from.equals(""))
						fromDate = df.parse(from);
					if(!to.equals("")){
						toDate = df.parse(to);
						Calendar c = Calendar.getInstance();
						c.setTime(toDate);
						c.add(Calendar.DAY_OF_MONTH, 1);
						
						toDate = c.getTime();
					}
					if(fromDate != null && toDate != null)
						filterBuildersAnd.add(FilterBuilders.rangeFilter(key).from(fromDate.getTime()).to(toDate.getTime()) );
					else
						if(fromDate != null)
							filterBuildersAnd.add(FilterBuilders.rangeFilter(key).from(fromDate.getTime()) );
						else
							if(toDate != null)
								filterBuildersAnd.add(FilterBuilders.rangeFilter(key).to(toDate.getTime()) );
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//or with regexs
		filterBuildersAnd.add(FilterBuilders.orFilter(filterBuildersRegex.toArray(new FilterBuilder[filterBuildersRegex.size()])));
		
		//And all
		queryBuilder.withFilter(FilterBuilders.andFilter(filterBuildersAnd.toArray(new FilterBuilder[filterBuildersAnd.size()]) ));
		
		//Sorts
		if(sorts != null)
			for(String fie : sorts.keySet()){
				queryBuilder.withSort(new FieldSortBuilder(fie).ignoreUnmapped(true).order(sorts.get(fie)));
			}
		
		if(size != 0)
			queryBuilder.withPageable(new PageRequest(page, size));
		
		if(!field.equals("")){
			if(typeQuery.equalsIgnoreCase(QUERY_FACET)){
				System.out.println("QUERY_FACET _______________ ");
				queryBuilder.withFacet(new TermFacetRequestBuilder(field).applyQueryFilter().fields(field).ascCount().size(facetSize).build());
			}
			if(typeQuery.equalsIgnoreCase(QUERY_HISTOGRAM)){
				System.out.println("QUERY_HISTOGRAM _______________ ");
				queryBuilder.withFacet(new HistogramFacetRequestBuilder(field).field(field).interval(INTERVAL_TIME).applyQueryFilter().build());
			}
		}
		return queryBuilder.build();
	}
	
	@Override
	public <T> boolean checkIndex(Class<T> clazz) {
		
		return elasticsearchTemplate.indexExists(clazz);
	}
	
	public <T> boolean deleteIndex(Class<T> clazz){
		
		if(elasticsearchTemplate.indexExists(clazz))
			return elasticsearchTemplate.deleteIndex(clazz);
		return false;
	}
	public <T> String remove(Class<T> clazz, String id){
		
		if(elasticsearchTemplate.indexExists(clazz))
			return elasticsearchTemplate.delete(clazz, id);
		return "";
	}

	public <T> void bulkIndex(List<String> idList, List<T> objectList){
		if(objectList == null)
			return;
		
		List<IndexQuery> indexQuerys = new ArrayList<IndexQuery>();
		
		int i = 0;
		for(T value : objectList){
			IndexQuery  query = new IndexQueryBuilder().withId(idList.get(i)).withObject(value).build();
			indexQuerys.add(query);
			
			i ++;
		}
		
		elasticsearchTemplate.bulkIndex(indexQuerys);
	}
	
	public <T> boolean exist(String id, Class<T> clazz){
		if(elasticsearchTemplate.queryForObject(new CriteriaQuery(Criteria.where("id").is(id)), clazz) == null){
			return false;
		}
		else
			return true;
	}
	
	public <T> String index(String id, T object){
		if(object == null)
			return "null value";
		
		return elasticsearchTemplate.index(new IndexQueryBuilder().withId(id).withObject(object).build());
	}
	
	private <T> int countAllData(Class<T> clazz){
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(matchAllQuery());
		
		return (int)elasticsearchTemplate.count(queryBuilder.build(), clazz);
		
	}
	private <T> int getTotalRecord(List<String> fields, List<String> terms, Map<String , List<String>> keywords, int facetSize,Class<T> clazz){
		
		if(fields.size() == 0)
			return 0;
		int countAll = countAllData(clazz);
		System.out.println("countAll: " + countAll);
		
		int i = 0;
		int total[] = new int[fields.size()];
		
		int termCompare = 0;
		for(String te : terms){
			if(!te.equals(""))
				termCompare++;
		}
		
		if(termCompare == 0){
			int fieldCompare = 0;
			//not choose
			for(String field : fields){
				List<String> termTemps = new ArrayList<String>();
				termTemps.addAll(terms);
				termTemps.set(i, "");
				
				List<Term> termForFields = getFacets(field, fields, termTemps, keywords, countAll, clazz);
				
				//get count
				int tmp = 0;
				if(terms.get(i).equalsIgnoreCase("") && termForFields.size() > 0){
					for(Term term : termForFields){
						tmp += term.getCount();
					}
					total[i] = tmp;
					fieldCompare ++;
				}
				
				i++;
			}
			if(fieldCompare > 0){
				return NumberUtils.max(total);
			}
		}
		else
			//Choose any one
			for(String field : fields){
				List<String> termTemps = new ArrayList<String>();
				termTemps.addAll(terms);
				termTemps.set(i, "");
				
				List<Term> termForFields = getFacets(field, fields, termTemps, keywords, countAll, clazz);
				
				//get count
				int tmp = 0;
				if(terms.get(i).equalsIgnoreCase("") && termForFields.size() > 0){
					for(Term term : termForFields){
						tmp += term.getCount();
					}
					return tmp;
				}
				
				i++;
			}
		
		
		//Else all chosen
		int count = 0;
		List<Term> rest = getFacets(fields.get(0), fields, terms, keywords, countAll, clazz);
		for(Term term : rest){
			count += term.getCount();
		}
		
		return count;
	}

	public <T> long count(SearchQuery query, Class<T> clazz) {
		long count = elasticsearchTemplate.count(query, clazz);
		
		System.out.println("COUNT Query = " + count);
		return count;
	}
}
