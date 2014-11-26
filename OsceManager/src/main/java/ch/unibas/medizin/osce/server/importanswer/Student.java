
package ch.unibas.medizin.osce.server.importanswer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Student {

    private String id;
    private List<Answer> answers = new ArrayList<Answer>();
    private Notes notes;
    private List<AudioNote> audioNotes = new ArrayList<AudioNote>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The answers
     */
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     * 
     * @param answers
     *     The answers
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    /**
     * 
     * @return
     *     The notes
     */
    public Notes getNotes() {
        return notes;
    }

    /**
     * 
     * @param notes
     *     The notes
     */
    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    /**
     * 
     * @return
     *     The audioNotes
     */
    public List<AudioNote> getAudioNotes() {
        return audioNotes;
    }

    /**
     * 
     * @param audioNotes
     *     The audioNotes
     */
    public void setAudioNotes(List<AudioNote> audioNotes) {
        this.audioNotes = audioNotes;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
