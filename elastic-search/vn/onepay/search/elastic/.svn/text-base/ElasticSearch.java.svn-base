package vn.onepay.search.elastic;

import java.util.List;
import java.util.Map;

import org.elasticsearch.search.facet.histogram.HistogramFacet.Entry;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.facet.result.IntervalUnit;
import org.springframework.data.elasticsearch.core.facet.result.Term;

public interface ElasticSearch {
	
	public <T> boolean checkIndex(Class<T> clazz);
	public <T> boolean deleteIndex(Class<T> clazz);
	public <T> String remove(Class<T> clazz, String id);
	
	public <T> void index(String id, T object);
	public <T> void bulkIndex(List<String> idList, List<T> objectList);
	
	public <T> List<List<Term>> getFacets(List<String> fields, List<String> terms, Map<String , List<String>> keywords, int facetSize, Class<T> clazz);
	
	public <T> List<IntervalUnit> getHistogramFacet(String field, List<String> fields, List<String> terms,  Map<String , List<String>> keywords, int facetSize, Class<T> clazz);
	
	public <T> List<T> search(List<String> fields, List<String> terms, Map<String , List<String>> keywords, Map<String , SortOrder> sorts,int page, int size, int facetSize, Class<T> clazz);
	public <T> int count(List<String> fields, List<String> terms, Map<String , List<String>> keywords, int facetSize, Class<T> clazz);
}
