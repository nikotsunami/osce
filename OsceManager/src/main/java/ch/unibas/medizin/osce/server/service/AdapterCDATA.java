package ch.unibas.medizin.osce.server.service;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdapterCDATA extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String val) throws Exception {
		return val; 
	}

	@Override
	public String marshal(String val) throws Exception {
		return "<![CDATA[" + val + "]]>";
	}

}
