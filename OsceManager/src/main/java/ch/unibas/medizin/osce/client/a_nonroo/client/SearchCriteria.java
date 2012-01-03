package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class SearchCriteria implements Serializable {
	
	private List <String> fields;
	private List <Integer> comparisons;
	private List <String> values;
	
	public SearchCriteria() {
		fields = new ArrayList<String>();
		comparisons = new ArrayList<Integer>();
		values = new ArrayList<String>();
	}
	
	public void clean() {
		fields.clear();
		comparisons.clear();
		values.clear();
	}
	
	public void add(String field, int comparison, String value) {
		fields.add(field);
		comparisons.add(comparison);
		values.add(value);
	}
	
	public List<String> getFields() {
		return fields;
	}
	
	public List<Integer> getComparisons() {
		return comparisons;		
	}
	
	public List<String> getValues() {
		return values;
	}
	
}
