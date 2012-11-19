package ch.unibas.medizin.osce.shared;

import java.util.ArrayList;
import java.util.List;

public  class MapEnvelop {

	//List<MapEnvelopKeyValuePair> maps=new ArrayList<MapEnvelopKeyValuePair>();
	
	String key;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	List<String> value;

	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}
	/*List<String> keys=new ArrayList<String>();
	
	public List<String> getKeys() {
		return keys;
	}
	public void setKeys(List<String> keys) {
		this.keys = keys;
	}
	public List<List<String>> getValues() {
		return values;
	}
	public void setValues(List<List<String>> values) {
		this.values = values;
	}
	List<List<String>> values=new ArrayList<List<String>>();
	
	
	*/
	
/*	public List<MapEnvelopKeyValuePair> getMaps() {
		return maps;
	}
*/
	public void put(String key,List<String> value)
	{
		this.key=key;
		this.value=value;
		
		
		//maps.add(map);
		//keys.add(key);
		//values.add(value);
	}
	
/*	public List<String> get(String key)
	{
		for(int i=0;i<keys.size();i++)
		{
			if(keys.get(i).equalsIgnoreCase(key))
			{
				return values.get(i);
			}
			
		}
		
		return null;
	}
	*/
}
