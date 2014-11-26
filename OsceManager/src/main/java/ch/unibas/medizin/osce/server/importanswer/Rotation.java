
package ch.unibas.medizin.osce.server.importanswer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Rotation {

    private String stationId;
    private Signature signature;
    private String rotationId;
    private List<Student> students = new ArrayList<Student>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The stationId
     */
    public String getStationId() {
        return stationId;
    }

    /**
     * 
     * @param stationId
     *     The stationId
     */
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    /**
     * 
     * @return
     *     The signature
     */
    public Signature getSignature() {
        return signature;
    }

    /**
     * 
     * @param signature
     *     The signature
     */
    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    /**
     * 
     * @return
     *     The rotationId
     */
    public String getRotationId() {
        return rotationId;
    }

    /**
     * 
     * @param rotationId
     *     The rotationId
     */
    public void setRotationId(String rotationId) {
        this.rotationId = rotationId;
    }

    /**
     * 
     * @return
     *     The students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * 
     * @param students
     *     The students
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
