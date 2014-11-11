package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.style.widgets.NumberSpinner;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatisticalEvaluationDetailsItemViewImpl  extends Composite implements StatisticalEvaluationDetailsItemView{

	private static StatisticalEvaluationDetailsItemViewImplUiBinder uiBinder = GWT.create(StatisticalEvaluationDetailsItemViewImplUiBinder.class);

	interface StatisticalEvaluationDetailsItemViewImplUiBinder extends UiBinder<Widget, StatisticalEvaluationDetailsItemViewImpl> {
	}
	
	private OscePostProxy oscePostProxy;
	
	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}

	public void setOscePostProxy(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
	}

	private Delegate delegate;
	
	OsceConstants constants = GWT.create(OsceConstants.class);
	
	private ChecklistQuestionProxy checklistQuestionProxy;
	
	private DoctorProxy doctorProxy;
	
	private ChecklistItemProxy checklistItemProxy;
	
	@UiField
	 ToggleButton onOffButton;
	
	 PopupPanel addPointPopup;
	
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
	
	NumberSpinner addPoint;
	
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

	/*public ChecklistQuestionProxy getChecklistQuestionProxy() {
		return checklistQuestionProxy;
	}

	public void setChecklistQuestionProxy(
			ChecklistQuestionProxy checklistQuestionProxy) {
		this.checklistQuestionProxy = checklistQuestionProxy;
	}*/

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
		
		//delegate.onOffButtonClicked(checklistQuestionProxy.getId(),onOffButton.isDown());
		delegate.onOffButtonClicked(checklistItemProxy.getId(),onOffButton.isDown());
	}
	
	public NumberSpinner createAddPointButton()
	{
		addPoint = new NumberSpinner();
		return addPoint;
		/*Button addPointBtn=new Button();
		addPointBtn.setText(constants.addPoint());
		
		addPointBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("createAddPointButton");
				showPopup();
				
			}
		});
		
		return addPointBtn;*/
	}
	
	public void showPopup()
	{
		Log.info("showPopup");
		//final PopupPanel popup=new PopupPanel(true);
		
		if(addPointPopup==null)
		{
			addPointPopup=new PopupPanel(true);
		
		
		HorizontalPanel hp1=new HorizontalPanel();
		
		Label addPointLbl=new Label(constants.addPoint()+":");
		//final IntegerBox addPointTxtBox=new IntegerBox();
		final NumberSpinner addPointTxtBox = new NumberSpinner();
		
		/*if(!((Label)postDataHP.getWidget(6)).getText().equalsIgnoreCase("-"))
		{
			minTxtBox.setText(((Label)postDataHP.getWidget(6)).getText());
		}*/
		hp1.setSpacing(3);
		hp1.add(addPointLbl);
		hp1.add(addPointTxtBox);
		
/*		HorizontalPanel hp2=new HorizontalPanel();
		Label maxLbl=new Label(constants.max());
		final IntegerBox maxTxtBox=new IntegerBox();
		if(!((Label)postDataHP.getWidget(7)).getText().equalsIgnoreCase("-"))
		{
			maxTxtBox.setText(((Label)postDataHP.getWidget(7)).getText());
		}
		hp2.setSpacing(3);
		hp2.add(maxLbl);
		hp2.add(maxTxtBox);*/
		
		HorizontalPanel hp3=new HorizontalPanel();
		Button saveBtn=new Button();
		saveBtn.setText(constants.save());
		hp3.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hp3.setSpacing(3);
		hp3.add(saveBtn);
		
		saveBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(addPointTxtBox.getValue() == null)
				{
					MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog(constants.minMaxNotNull());
					
					return;
				}
				
				if (!isNumber(addPointTxtBox.getValue().toString()))
				{
					MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog(constants.addPointErr());
					
					return;
				}
				
				delegate.setAddPoint(oscePostProxy,doctorProxy,addPointTxtBox.getValue());
				//((Label)postDataHP.getWidget(6)).setText(minTxtBox.getText());
				//((Label)postDataHP.getWidget(7)).setText(maxTxtBox.getText());
				addPointPopup.hide();
			}
		});
		
		VerticalPanel vp=new VerticalPanel();
		vp.setSpacing(5);
		vp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		vp.add(hp1);
		//vp.add(hp2);
		vp.add(hp3);
		
		addPointPopup.add(vp);
		}
		addPointPopup.setPopupPosition(fourthColumnHP.getWidget(0).getAbsoluteLeft()-70, fourthColumnHP.getWidget(0).getAbsoluteTop()-110);
		addPointPopup.show();
	}
	
	public static boolean isNumber(String value) {
		
		  try  
		  {  
		    Integer.parseInt(value);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  catch (Exception e) {
			  return false;
		  }
		  
		  return true;  
	}

	public NumberSpinner getAddPoint() {
		return addPoint;
	}

	public void setAddPoint(NumberSpinner addPoint) {
		this.addPoint = addPoint;
	}

	public ChecklistItemProxy getChecklistItemProxy() {
		return checklistItemProxy;
	}
	
	public void setChecklistItemProxy(ChecklistItemProxy checklistItemProxy) {
		this.checklistItemProxy = checklistItemProxy;
	}
	
}
