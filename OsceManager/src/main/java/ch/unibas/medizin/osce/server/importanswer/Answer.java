
package ch.unibas.medizin.osce.server.importanswer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Answer {

    private AnswerOption answerOption;
    private String questionId;
    private List<Criteria> criterias = new ArrayList<Criteria>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The answerOption
     */
    public AnswerOption getAnswerOption() {
        return answerOption;
    }

    /**
     * 
     * @param answerOption
     *     The answerOption
     */
    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }

    /**
     * 
     * @return
     *     The questionId
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * 
     * @param questionId
     *     The questionId
     */
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    /**
     * 
     * @return
     *     The criterias
     */
    public List<Criteria> getCriterias() {
        return criterias;
    }

    /**
     * 
     * @param criterias
     *     The criterias
     */
    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
