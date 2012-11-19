package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.List;

import ch.unibas.medizin.osce.shared.MapEnvelop;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.google.gwt.requestfactory.shared.ValueProxy;

@ProxyFor(value = MapEnvelop.class)
public interface MapEnvelopProxy extends ValueProxy{
	public String getKey();
	
	public List<String> getValue();
	
	//public List<String> get(String key);
	
	//public List<String> getKeys();
	
	//public List<List<String>> getValues();
	
	//public List<MapEnvelopKeyValuePair> getMaps();
}
