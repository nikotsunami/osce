
package ch.unibas.medizin.osce.server.importanswer;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Signature {

    private String signaturePath;
    private String timestamp;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The signaturePath
     */
    public String getSignaturePath() {
        return signaturePath;
    }

    /**
     * 
     * @param signaturePath
     *     The signaturePath
     */
    public void setSignaturePath(String signaturePath) {
        this.signaturePath = signaturePath;
    }

    /**
     * 
     * @return
     *     The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 
     * @param timestamp
     *     The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
