
package ch.unibas.medizin.osce.server.importanswer;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class StudentAnswer {

    private Examanswers examanswers;
    private String publicKey;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The examanswers
     */
    public Examanswers getExamanswers() {
        return examanswers;
    }

    /**
     * 
     * @param examanswers
     *     The examanswers
     */
    public void setExamanswers(Examanswers examanswers) {
        this.examanswers = examanswers;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
    
    public String getPublicKey() {
		return publicKey;
	}
    
    public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

}
