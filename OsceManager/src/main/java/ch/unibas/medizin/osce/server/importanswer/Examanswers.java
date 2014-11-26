
package ch.unibas.medizin.osce.server.importanswer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Examanswers {

    private List<Rotation> rotations = new ArrayList<Rotation>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The rotations
     */
    public List<Rotation> getRotations() {
        return rotations;
    }

    /**
     * 
     * @param rotations
     *     The rotations
     */
    public void setRotations(List<Rotation> rotations) {
        this.rotations = rotations;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
