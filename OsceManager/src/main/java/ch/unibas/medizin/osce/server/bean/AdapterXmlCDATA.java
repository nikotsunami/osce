package ch.unibas.medizin.osce.server.bean;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdapterXmlCDATA extends XmlAdapter<String, String> {
 
    @Override
    public String marshal(String value) throws Exception {
        return "<![CDATA[" + value + "]]>";
    }
    @Override
    public String unmarshal(String value) throws Exception {
        return value;
    }
 
}