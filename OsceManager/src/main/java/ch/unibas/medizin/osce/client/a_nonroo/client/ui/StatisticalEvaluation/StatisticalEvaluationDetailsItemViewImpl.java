package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StatisticalEvaluationDetailsItemViewImpl  extends Composite implements StatisticalEvaluationDetailsItemView{

	private static StatisticalEvaluationDetailsItemViewImplUiBinder uiBinder = GWT.create(StatisticalEvaluationDetailsItemViewImplUiBinder.class);

	interface StatisticalEvaluationDetailsItemViewImplUiBinder extends UiBinder<Widget, StatisticalEvaluationDetailsItemViewImpl> {
	}

	private Delegate delegate;
	
	OsceConstants constants = GWT.create(OsceConstants.class);
	
	private ChecklistQuestionProxy checklistQuestionProxy;
	
	private AnswerProxy answerProxy;
	
	public AnswerProxy getAnswerProxy() {
		return answerProxy;
	}

	public void setAnswerProxy(AnswerProxy answerProxy) {
		this.answerProxy = answerProxy;
	}

	@UiField
	TableElement sequenceHeader;
	
	//@UiField
	//Image minMaxImage;
	
	@UiField
	Label sequenceLbl;
	
	
	@UiField
	HorizontalPanel postDataHP;
	
	@UiField
	Label sumPerSequenceLbl;
	
	public Label getSumPerSequenceLbl() {
		return sumPerSequenceLbl;
	}

	public HorizontalPanel getPostDataHP() {
		return postDataHP;
	}
	


	public Label getSequenceLbl() {
		return sequenceLbl;
	}
	
	public TableElement getSequenceHeader() {
		return sequenceHeader;
	}

	public ChecklistQuestionProxy getChecklistQuestionProxy() {
		return checklistQuestionProxy;
	}

	public void setChecklistQuestionProxy(
			ChecklistQuestionProxy checklistQuestionProxy) {
		this.checklistQuestionProxy = checklistQuestionProxy;
	}

	public StatisticalEvaluationDetailsItemViewImpl() 
	{
		Log.info("Call StatisticalEvaluationDetailsItemViewImpl");
		initWidget(uiBinder.createAndBindUi(this));
		
		
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public Widget asWidget() {
		return this;
	}
	
	public Label createPostDataLabel()
	{
		Label l=new Label();
		l.addStyleName("postData");
		l.setWordWrap(true);
		return l;
	}
}
