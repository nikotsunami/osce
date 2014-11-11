package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.shared.StatisticalEvaluationQuestion;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.google.gwt.requestfactory.shared.ValueProxy;

@ProxyFor(value = StatisticalEvaluationQuestion.class)
public interface StatisticalEvaluationQuestionProxy extends ValueProxy {

	public Long getQuestionId();
	
	public String getQuestionText();
	
	public Boolean getIsRegressionItem();
}
