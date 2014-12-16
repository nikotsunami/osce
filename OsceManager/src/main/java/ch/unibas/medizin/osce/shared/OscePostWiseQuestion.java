package ch.unibas.medizin.osce.shared;

import java.util.List;

public class OscePostWiseQuestion {
	
	Long oscePostId;
	
	List<StatisticalEvaluationQuestion> questionList;
	
	public Long getOscePostId() {
		return oscePostId;
	}
	
	public void setOscePostId(Long oscePostId) {
		this.oscePostId = oscePostId;
	}
	
	public List<StatisticalEvaluationQuestion> getQuestionList() {
		return questionList;
	}
	
	public void setQuestionList(List<StatisticalEvaluationQuestion> questionList) {
		this.questionList = questionList;
	}
}
