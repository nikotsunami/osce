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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TrainingViewImpl extends Composite implements TrainingView {

	private static TrainingViewImplUiBinder uiBinder = GWT.create(TrainingViewImplUiBinder.class);
	
	interface TrainingViewImplUiBinder extends UiBinder<Widget, TrainingViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
	
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
		
	private Delegate delegate;
	
	@UiField
	HorizontalPanel sstartTimeRoleNameHp;
	
	@UiField
	Label startTimeLabel;
	
	@UiField
	Label roleNameLabel;
	
	@UiField
	Label endTimeLabel;
	
	@UiField
	VerticalPanel showSuggestionButtonPanel;
	
	@UiField
	IconButton showSuggestionsBtn;
	
	@UiField
	VerticalPanel trainingPanel;
	
	private Date startTimeDate;
	
	private Date endTimeDate ;
	
	private StandardizedRoleProxy standardizedRoleProxy;

	private TrainingProxy trainingProxy;
	
	public TrainingViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("showSuggestionsBtn")
	public void showSuggestionsButtonClicked(ClickEvent event){
		Log.info("Show suggestions button clicked");
		delegate.showRemainingSuggestions(this.startTimeDate);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}
	public Label getStartTimeLabel() {
		return startTimeLabel;
	}
	public void setStartTimeLabel(Label startTimeLabel) {
		this.startTimeLabel = startTimeLabel;
	}
	public Date getStartTimeDate() {
		return startTimeDate;
	}
	
	@Override
	public void setStartTimeDate(Date startTimeDate) {
		this.startTimeDate = startTimeDate;
	}
	
	@Override
	public StandardizedRoleProxy getStandardizedRoleProxy() {
		return standardizedRoleProxy;
	}
	@Override
	public void setStandardizedRoleProxy(StandardizedRoleProxy standardizedRoleProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
	}
	public Date getEndTimeDate() {
		return endTimeDate;
	}
	@Override
	public void setEndTimeDate(Date endTimeDate) {
		this.endTimeDate = endTimeDate;
	}
	@Override
	public void setValue() {
		Log.info("setting start and endtime value");
		
		this.startTimeDate = trainingProxy.getTimeStart();
		this.endTimeDate=trainingProxy.getTimeEnd();
		this.standardizedRoleProxy=trainingProxy.getStandardizedRole();
		
		String startHours =startTimeDate.getHours() < 10 ? "0" + String.valueOf(startTimeDate.getHours()): String.valueOf(startTimeDate.getHours());
		String startMinutes =startTimeDate.getMinutes() < 10? "0"+ String.valueOf(startTimeDate.getMinutes()) : String.valueOf(startTimeDate.getMinutes());
		startTimeLabel.setText(startHours+ ":" + startMinutes );
		String endHours =endTimeDate.getHours() < 10 ? "0" + String.valueOf(endTimeDate.getHours()): String.valueOf(endTimeDate.getHours());
		String endMinutes=endTimeDate.getMinutes() < 10? "0"+ String.valueOf(endTimeDate.getMinutes()) : String.valueOf(endTimeDate.getMinutes());
		endTimeLabel.setText(endHours + ":" + endMinutes);
		roleNameLabel.setText(standardizedRoleProxy.getShortName());
	}
	@Override
	public void setTrainingProxy(TrainingProxy trainingProxy) {
	
		this.trainingProxy = trainingProxy;
		
	}
	@Override
	public IconButton getShowSuggestionsBtn() {
		return showSuggestionsBtn;
	}
	
	@Override
	public VerticalPanel getTrainingPanel() {
		return trainingPanel;
	}
	
	
}
