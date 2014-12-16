package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.List;

import ch.unibas.medizin.osce.shared.OscePostWiseQuestion;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.google.gwt.requestfactory.shared.ValueProxy;

@ProxyFor(value = OscePostWiseQuestion.class)
public interface OscePostWiseQuestionProxy extends ValueProxy {

	public Long getOscePostId();
	
	public List<StatisticalEvaluationQuestionProxy> getQuestionList();
}
