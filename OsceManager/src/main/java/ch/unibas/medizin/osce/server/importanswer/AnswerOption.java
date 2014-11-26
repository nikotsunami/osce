
package ch.unibas.medizin.osce.server.importanswer;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class AnswerOption {

    private String checklistOptionValue;
    private String checklistOptionId;
    private String timestamp;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The checklistOptionValue
     */
    public String getChecklistOptionValue() {
        return checklistOptionValue;
    }

    /**
     * 
     * @param checklistOptionValue
     *     The checklistOptionValue
     */
    public void setChecklistOptionValue(String checklistOptionValue) {
        this.checklistOptionValue = checklistOptionValue;
    }

    /**
     * 
     * @return
     *     The checklistOptionId
     */
    public String getChecklistOptionId() {
        return checklistOptionId;
    }

    /**
     * 
     * @param checklistOptionId
     *     The checklistOptionId
     */
    public void setChecklistOptionId(String checklistOptionId) {
        this.checklistOptionId = checklistOptionId;
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
