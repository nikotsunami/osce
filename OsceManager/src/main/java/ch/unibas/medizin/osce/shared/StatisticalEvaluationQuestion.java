package ch.unibas.medizin.osce.shared;

public class StatisticalEvaluationQuestion {

	Long questionId;
	
	String questionText;
	
	Boolean isRegressionItem;
	
	public Long getQuestionId() {
		return questionId;
	}
	
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	
	public String getQuestionText() {
		return questionText;
	}
	
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	public Boolean getIsRegressionItem() {
		return isRegressionItem;
	}
	
	public void setIsRegressionItem(Boolean isRegressionItem) {
		this.isRegressionItem = isRegressionItem;
	}
}
