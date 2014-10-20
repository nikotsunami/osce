package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;



import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class IndividualSuggestionForTrainingViewImpl extends Composite implements IndividualSuggestionForTrainingView {

	private static individualSuggestionForTrainingViewImplUiBinder uiBinder = GWT.create(individualSuggestionForTrainingViewImplUiBinder.class);
	
	interface individualSuggestionForTrainingViewImplUiBinder extends UiBinder<Widget, IndividualSuggestionForTrainingViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
		
	private Delegate delegate;
	
	@UiField
	HTMLPanel mainHtmlPanel;
	
	@UiField
	HorizontalPanel viewPanel;
	
	
	@UiField
	Label roleNameLabel;
	
	@UiField
	TextBox startTimeTextBox;
	
	@UiField
	TextBox endTimeTextBox;
	
	@UiField
	IconButton scheduleTrainingButton;
	
	private StandardizedRoleProxy standardizedRoleProxy;
	
	private Date startTimeDate;
	
	private Date endTimeDate;

	private TrainingProxy trainingProxy;

	private boolean isAfternoon;
	
	public IndividualSuggestionForTrainingViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
	}

	public StandardizedRoleProxy getStandardizedRoleProxy() {
		return standardizedRoleProxy;
	}

	@Override
	public void setValue() {
		if(isAfternoon){
			startTimeTextBox.setValue("13:00");
			endTimeTextBox.setValue("17:00");
		}else{
			startTimeTextBox.setValue("08:00");
			endTimeTextBox.setValue("12:00");
		}
		
		roleNameLabel.setText(standardizedRoleProxy.getShortName() + " - " + standardizedRoleProxy.getLongName());
	}

	@Override
	public void setIsAfternoon(boolean isAfternoon) {
		this.isAfternoon=isAfternoon;
		
	}
	
	@Override
	public void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
	}

	@UiHandler("scheduleTrainingButton")
	public void scheduleTrainingButtonClicked(ClickEvent event){
		Log.info("schedule Training Button Clicked");
		boolean isBindTrainingToSuggestion=true;
		delegate.scheduleTrainingOfSuggestedRole(startTimeTextBox.getValue(),endTimeTextBox.getValue(),standardizedRoleProxy,isBindTrainingToSuggestion);
	}
	public Date getStartTimeDate() {
		return startTimeDate;
	}

	public void setStartTime(Date startTimeDate) {
		this.startTimeDate = startTimeDate;
	}

	public Date getEndTimeDate() {
		return endTimeDate;
	}

	public void setEndTime(Date endTimeDate) {
		this.endTimeDate = endTimeDate;
	}


	public void setTrainingProxy(TrainingProxy training) {
		this.trainingProxy=training;
		
	}

	public TrainingProxy getTrainingProxy() {
		return this.trainingProxy;
		
	}
}
