package ch.unibas.medizin.osce.server.service;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdapterCDATA extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String val) throws Exception {
		System.out.println("val 1" + val);
		return val; 
	}

	@Override
	public String marshal(String val) throws Exception {
		System.out.println("val " + val);
		return "<![CDATA[" + val + "]]>";
	}

}
