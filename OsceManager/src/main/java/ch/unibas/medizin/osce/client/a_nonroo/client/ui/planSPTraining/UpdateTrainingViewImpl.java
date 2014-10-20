package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UpdateTrainingViewImpl extends Composite implements UpdateTrainingView {

	private static UpdateTrainingViewImplUiBinder uiBinder = GWT.create(UpdateTrainingViewImplUiBinder.class);
	
	interface UpdateTrainingViewImplUiBinder extends UiBinder<Widget, UpdateTrainingViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
	
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
		
	private Delegate delegate;
	
	@UiField
	HTMLPanel mainHtmlPanel;
	
	@UiField
	HorizontalPanel viewPanel;
	
	@UiField
	VerticalPanel startEndTimeLabelPanel;
	
	@UiField
	Label startTimeLabel;
	
	@UiField
	Label endTimeLabel;
	
	@UiField
	Label roleNameLabel;
	
	@UiField
	IconButton editTrainingButton;
	
	@UiField
	IconButton deleteTrainingButton;
	
	private StandardizedRoleProxy standardizedRoleProxy;
	
	private Date startTimeDate;
	
	private Date endTimeDate;

	private TrainingProxy trainingProxy;
	
	public UpdateTrainingViewImpl() {
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
	public void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
	}

	public Date getStartTimeDate() {
		return startTimeDate;
	}

	@Override
	public void setStartTime(Date startTimeDate) {
		this.startTimeDate = startTimeDate;
	}

	public Date getEndTimeDate() {
		return endTimeDate;
	}

	@Override
	public void setEndTime(Date endTimeDate) {
		this.endTimeDate = endTimeDate;
	}

	@Override
	public void setValue() {
		this.startTimeDate = trainingProxy.getTimeStart();
		
		this.endTimeDate=trainingProxy.getTimeEnd();
		
		this.standardizedRoleProxy=trainingProxy.getStandardizedRole();
		
		Log.info("setting start and endtime value");
		
		String startHours =startTimeDate.getHours() < 10 ? "0" + String.valueOf(startTimeDate.getHours()): String.valueOf(startTimeDate.getHours());
		
		String startMinutes =startTimeDate.getMinutes() < 10? "0"+ String.valueOf(startTimeDate.getMinutes()) : String.valueOf(startTimeDate.getMinutes());
		
		startTimeLabel.setText(startHours+ ":" + startMinutes );
		
		String endHours =endTimeDate.getHours() < 10 ? "0" + String.valueOf(endTimeDate.getHours()): String.valueOf(endTimeDate.getHours());
		
		String endMinutes=endTimeDate.getMinutes() < 10? "0"+ String.valueOf(endTimeDate.getMinutes()) : String.valueOf(endTimeDate.getMinutes());
		
		endTimeLabel.setText(endHours + ":" + endMinutes);
		
		roleNameLabel.setText(standardizedRoleProxy.getShortName() + " - " + standardizedRoleProxy.getLongName());
		
	}

	@UiHandler("editTrainingButton")
	public void updateTrainingButtonClicked(ClickEvent event ){
		delegate.updateTrainingButtonClicked(this);
	}
	
	@UiHandler("deleteTrainingButton")
	public void deleteTrainingButtonClicked(ClickEvent event){
		delegate.deleteTrainingButtonClicked(trainingProxy);
	}
	
	@Override
	public void setTrainingProxy(TrainingProxy training) {
		this.trainingProxy=training;
		
	}

	public TrainingProxy getTrainingProxy() {
		return this.trainingProxy;
		
	}

	
}
