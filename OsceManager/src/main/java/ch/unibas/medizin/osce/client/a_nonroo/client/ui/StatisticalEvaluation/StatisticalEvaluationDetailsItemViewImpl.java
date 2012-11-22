package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import org.hibernate.type.IntegerType;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatisticalEvaluationDetailsItemViewImpl  extends Composite implements StatisticalEvaluationDetailsItemView{

	private static StatisticalEvaluationDetailsItemViewImplUiBinder uiBinder = GWT.create(StatisticalEvaluationDetailsItemViewImplUiBinder.class);

	interface StatisticalEvaluationDetailsItemViewImplUiBinder extends UiBinder<Widget, StatisticalEvaluationDetailsItemViewImpl> {
	}

	private Delegate delegate;
	
	OsceConstants constants = GWT.create(OsceConstants.class);
	
	private ChecklistQuestionProxy checklistQuestionProxy;
	
	private DoctorProxy doctorProxy;
	
	@UiField
	 ToggleButton onOffButton;
	
	
	
	public DoctorProxy getDoctorProxy() {
		return doctorProxy;
	}
	
	public void setDoctorProxy(DoctorProxy doctorProxy) {
		this.doctorProxy = doctorProxy;
	}

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
	
	@UiField
	HorizontalPanel fourthColumnHP;
	
	public HorizontalPanel getFourthColumnHP() {
		return fourthColumnHP;
	}

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
		onOffButton.setStylePrimaryName("green-ToggleButton");
		
	}
	
	public ToggleButton getOnOffButton() {
		return onOffButton;
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
	
	@UiHandler("onOffButton")
	public void onOffButtonClickHandler(ClickEvent event)
	{
		Log.info("onOffButtonClickHandler");
		
		delegate.onOffButtonClicked(checklistQuestionProxy.getId(),onOffButton.isDown());
	}
	
	public Button createAddPointButton()
	{
		Button addPointBtn=new Button();
		addPointBtn.setText(constants.addPoint());
		
		addPointBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("createAddPointButton");
				showPopup();
				
			}
		});
		
		return addPointBtn;
	}
	
	public void showPopup()
	{
		Log.info("showPopup");
		final PopupPanel popup=new PopupPanel(true);
		
		HorizontalPanel hp1=new HorizontalPanel();
		
		Label minLbl=new Label(constants.minmum());
		final IntegerBox minTxtBox=new IntegerBox();
		if(!((Label)postDataHP.getWidget(6)).getText().equalsIgnoreCase("-"))
		{
			minTxtBox.setText(((Label)postDataHP.getWidget(6)).getText());
		}
		hp1.setSpacing(3);
		hp1.add(minLbl);
		hp1.add(minTxtBox);
		
		HorizontalPanel hp2=new HorizontalPanel();
		Label maxLbl=new Label(constants.max());
		final IntegerBox maxTxtBox=new IntegerBox();
		if(!((Label)postDataHP.getWidget(7)).getText().equalsIgnoreCase("-"))
		{
			maxTxtBox.setText(((Label)postDataHP.getWidget(7)).getText());
		}
		hp2.setSpacing(3);
		hp2.add(maxLbl);
		hp2.add(maxTxtBox);
		
		HorizontalPanel hp3=new HorizontalPanel();
		Button saveBtn=new Button();
		saveBtn.setText(constants.save());
		hp3.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hp3.setSpacing(3);
		hp3.add(saveBtn);
		
		saveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(minTxtBox.getText().equals("") || maxTxtBox.getText().equals(""))
				{
					MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog(constants.minMaxNotNull());
					
					return;
				}
				((Label)postDataHP.getWidget(6)).setText(minTxtBox.getText());
				((Label)postDataHP.getWidget(7)).setText(maxTxtBox.getText());
				popup.hide();
			}
		});
		
		VerticalPanel vp=new VerticalPanel();
		vp.setSpacing(5);
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.add(hp1);
		vp.add(hp2);
		vp.add(hp3);
		
		popup.add(vp);
		popup.setPopupPosition(fourthColumnHP.getWidget(0).getAbsoluteLeft()-70, fourthColumnHP.getWidget(0).getAbsoluteTop()-110);
		popup.show();
	}
}
