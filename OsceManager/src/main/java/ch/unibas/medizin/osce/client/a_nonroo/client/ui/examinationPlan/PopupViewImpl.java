package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.io.IOException;
import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class PopupViewImpl  extends PopupPanel  implements PopupView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label examinerNameLbl;
	
	public ValueListBox<Date> getEndTimeListBox() {
		return endTimeListBox;
	}
	public Button getOkButton() {
		return okButton;
	}
	public Button getCancelButton() {
		return cancelButton;
	}
	
	@UiField
	Button saveBtn;
	
	
	
	public Button getSaveBtn() {
		return saveBtn;
	}
	@UiField
	IconButton edit;
	
	@UiField
	Label examinerNameValue;
	
	public Label getExaminerNameValue() {
		return examinerNameValue;
	}
	public IconButton getEdit() {
		return edit;
	}
	
	@UiField
	IconButton clearButton;

	public IconButton getClearButton() {
		return clearButton;
	}

	@UiField(provided = true)
	ValueListBox<Date> endTimeListBox=new ValueListBox<Date>(new Renderer<Date>() {

		@Override
		public String render(Date object) {
			if(object==null)
				return "";
			else
				return DateTimeFormat.getShortDateTimeFormat().format(object);
		}

		@Override
		public void render(Date object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	@UiField
	IconButton okButton;
	
	@UiField
	Button cancelButton;
	
	
	
	@UiField(provided = true)
	public SuggestBox examinerSuggestionBox =  //new SuggestBox(keywordoracle);
			new SuggestBox(
					new ProxySuggestOracle<DoctorProxy>(
							new AbstractRenderer<DoctorProxy>() {
								@Override
								public String render(DoctorProxy object) {
									return object.getPreName() +" "+ object.getName();
								}
							}// ));
							, ",;:. \t?!_-/\\"));
	
	public void setExaminerSuggestionBox(SuggestBox examinerSuggestionBox) {
		this.examinerSuggestionBox = examinerSuggestionBox;
	}

	@UiField
	Label nameLbl;
	
	@UiField 
	Label nameValue;
	
	public Label getExaminerNameLbl() {
		return examinerNameLbl;
	}
	public SuggestBox getExaminerSuggestionBox() {
		return examinerSuggestionBox;
	}
	public Label getNameLbl() {
		return nameLbl;
	}
	public Label getNameValue() {
		return nameValue;
	}
	public Label getStartTimeLbl() {
		return startTimeLbl;
	}
	public Label getStartTimeValue() {
		return startTimeValue;
	}
	public Label getEndTimeLbl() {
		return endTimeLbl;
	}
	public Label getEndTimeValue() {
		return endTimeValue;
	}

	@UiField
	Label startTimeLbl;
	
	@UiField
	Label startTimeValue;
	
	@UiField
	Label endTimeLbl;
	
	@UiField
	Label endTimeValue;
	
	@UiField
	IntegerBox breakDuration;
	
	//by spec change[
	
	@UiField
	Label exchangeStudLbl;
	
	@UiField
	Label exchangeSPLbl;
	
	@UiField
	DefaultSuggestBox<StudentProxy, EventHandlingValueHolderItem<StudentProxy>> exchangeStudentListBox;
	
	@UiField
	DefaultSuggestBox<PatientInRoleProxy, EventHandlingValueHolderItem<PatientInRoleProxy>> exchangeSpListBox;
	
	//by spec change]	
	
	public IntegerBox getBreakDuration() {
		return breakDuration;
	}
	
	public PopupViewImpl()
	{
		super(true);
		add(BINDER.createAndBindUi(this));
	}
	
	public void createEditBreakDurationPopupView()
	{
		examinerNameLbl.setVisible(true);
		examinerNameLbl.setText(constants.breakDuration());
		examinerNameValue.setVisible(true);
		examinerSuggestionBox.removeFromParent();
		edit.removeFromParent();
		
		
		nameLbl.setVisible(true);
		nameValue.removeFromParent();
		nameValue.setVisible(true);
		breakDuration.setVisible(true);
		nameLbl.setText(constants.newBreakDuration());
		
		startTimeLbl.removeFromParent();	
		startTimeValue.removeFromParent();	
	
		
		endTimeLbl.removeFromParent();	
		endTimeValue.removeFromParent();	
		
		//endTimeValue.removeFromParent();
		endTimeListBox.removeFromParent();
		
		
		saveBtn.removeFromParent();
		
		okButton.setVisible(true);
		cancelButton.setVisible(true);	
		okButton.setText(constants.okBtn());
		okButton.setIcon("check");
		cancelButton.setText(constants.cancel());
		
		//by spec change[
		exchangeSPLbl.setVisible(false);
		exchangeSpListBox.setVisible(false);
		exchangeStudLbl.setVisible(false);
		exchangeStudentListBox.setVisible(false);
		
		exchangeSPLbl.removeFromParent();
		exchangeSpListBox.removeFromParent();
		exchangeStudLbl.removeFromParent();
		exchangeStudentListBox.removeFromParent();
		//by spec change]
		
		clearButton.removeFromParent();
	}
	
	public void createSPPopupView()
	{
		examinerNameLbl.removeFromParent();
		examinerSuggestionBox.removeFromParent();
		edit.removeFromParent();
		
		nameLbl.setVisible(true);
		nameValue.setVisible(true);
		nameLbl.setText(constants.spName());
		
		startTimeLbl.setVisible(true);		
		startTimeValue.setVisible(true);
		startTimeLbl.setText(constants.circuitStart());
		
		endTimeLbl.setVisible(true);
		endTimeValue.setVisible(true);
		endTimeLbl.setText(constants.circuitEndTime());
		//endTimeValue.removeFromParent();
		endTimeListBox.removeFromParent();
		
		okButton.setVisible(true);
		okButton.setText(constants.close());
		cancelButton.setVisible(false);
		saveBtn.removeFromParent();
		breakDuration.removeFromParent();
		
		//by spec change[
		exchangeSPLbl.setVisible(false);
		exchangeSpListBox.setVisible(false);
		exchangeStudLbl.setVisible(false);
		exchangeStudentListBox.setVisible(false);
		
		exchangeSPLbl.removeFromParent();
		exchangeSpListBox.removeFromParent();
		exchangeStudLbl.removeFromParent();
		exchangeStudentListBox.removeFromParent();
		//by spec change]
		clearButton.removeFromParent();
	}
	public void createOscePostPopupView()
	{
		examinerNameLbl.removeFromParent();
		examinerSuggestionBox.removeFromParent();
		edit.removeFromParent();
		
		nameLbl.setVisible(true);
		nameValue.setVisible(true);
		nameLbl.setText(constants.postType());
		
		startTimeLbl.setVisible(true);		
		startTimeValue.setVisible(true);
		startTimeLbl.setText(constants.roleTopic());
		
		okButton.setVisible(true);
		okButton.setText(constants.close());
		cancelButton.setVisible(false);
		endTimeLbl.removeFromParent();
		endTimeListBox.removeFromParent();
		endTimeValue.removeFromParent();
		saveBtn.removeFromParent();
		breakDuration.removeFromParent();
		
		//by spec change[
		exchangeSPLbl.setVisible(false);
		exchangeSpListBox.setVisible(false);
		exchangeStudLbl.setVisible(false);
		exchangeStudentListBox.setVisible(false);
		
		exchangeSPLbl.removeFromParent();
		exchangeSpListBox.removeFromParent();
		exchangeStudLbl.removeFromParent();
		exchangeStudentListBox.removeFromParent();
		//by spec change]
		clearButton.removeFromParent();
	}
	public void createExaminerInfoPopupView()
	{
		examinerNameLbl.setVisible(true);
		examinerNameLbl.setText(constants.examinerName());
		examinerSuggestionBox.setVisible(false);
		examinerNameValue.setVisible(true);
		edit.setVisible(true);
		saveBtn.setVisible(false);
		
		nameLbl.setVisible(false);
		nameValue.setVisible(false);
		
		//nameLbl.setText(constants.examinerName());
		
		startTimeLbl.setVisible(true);		
		startTimeValue.setVisible(true);
		startTimeLbl.setText(constants.circuitStart());
		
		okButton.setVisible(true);
		okButton.setText(constants.close());
		cancelButton.setVisible(false);
		
		endTimeLbl.setVisible(true);
		endTimeListBox.removeFromParent();
		endTimeValue.setVisible(true);
		endTimeLbl.setText(constants.circuitEndTime());
		breakDuration.removeFromParent();
		
		//by spec change[
		exchangeSPLbl.setVisible(false);
		exchangeSpListBox.setVisible(false);
		exchangeStudLbl.setVisible(false);
		exchangeStudentListBox.setVisible(false);
		
		exchangeSPLbl.removeFromParent();
		exchangeSpListBox.removeFromParent();
		exchangeStudLbl.removeFromParent();
		exchangeStudentListBox.removeFromParent();
		//by spec change]
		
		clearButton.setText(constants.clear());
		clearButton.setVisible(true);
		
	}
	
	public void createExaminerAssignPopupView() {
		
		//enable
		examinerNameLbl.setVisible(true);
		examinerSuggestionBox.setVisible(true);
		startTimeLbl.setVisible(true);
		
		startTimeValue.setVisible(true);
		endTimeLbl.setVisible(true);
		endTimeListBox.setVisible(true);
		okButton.setVisible(true);
		cancelButton.setVisible(true);
		
		startTimeLbl.setText(constants.circuitStart());
		examinerNameLbl.setText(constants.examinerName());
		endTimeLbl.setText(constants.circuitEndTime());
		
		okButton.setText(constants.okBtn());
		okButton.setIcon("check");
		cancelButton.setText(constants.cancel());
		
		//set visible false
		
		edit.removeFromParent();
		nameLbl.removeFromParent();
		nameValue.removeFromParent();
		endTimeValue.removeFromParent();
		saveBtn.removeFromParent();
		
		breakDuration.removeFromParent();
		
		//by spec change[
		exchangeSPLbl.setVisible(false);
		exchangeSpListBox.setVisible(false);
		exchangeStudLbl.setVisible(false);
		exchangeStudentListBox.setVisible(false);
		
		exchangeSPLbl.removeFromParent();
		exchangeSpListBox.removeFromParent();
		exchangeStudLbl.removeFromParent();
		exchangeStudentListBox.removeFromParent();
		//by spec change]
		clearButton.removeFromParent();
	}	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, PopupViewImpl> {
	}

	//by spec change[
	public Label getExchangeStudLbl() {
		return exchangeStudLbl;
	}
	public void setExchangeStudLbl(Label exchangeStudLbl) {
		this.exchangeStudLbl = exchangeStudLbl;
	}
	public Label getExchangeSPLbl() {
		return exchangeSPLbl;
	}
	public void setExchangeSPLbl(Label exchangeSPLbl) {
		this.exchangeSPLbl = exchangeSPLbl;
	}
	public DefaultSuggestBox<StudentProxy, EventHandlingValueHolderItem<StudentProxy>> getExchangeStudentListBox() {
		return exchangeStudentListBox;
	}
	public void setExchangeStudentListBox(
			DefaultSuggestBox<StudentProxy, EventHandlingValueHolderItem<StudentProxy>> exchangeStudentListBox) {
		this.exchangeStudentListBox = exchangeStudentListBox;
	}
	public DefaultSuggestBox<PatientInRoleProxy, EventHandlingValueHolderItem<PatientInRoleProxy>> getExchangeSpListBox() {
		return exchangeSpListBox;
	}
	public void setExchangeSpListBox(
			DefaultSuggestBox<PatientInRoleProxy, EventHandlingValueHolderItem<PatientInRoleProxy>> exchangeSpListBox) {
		this.exchangeSpListBox = exchangeSpListBox;
	}
	
	public void createExchangeStudentPopupView()
	{
		examinerNameLbl.removeFromParent();
		examinerSuggestionBox.removeFromParent();
		edit.removeFromParent();
		
		nameLbl.setVisible(true);
		nameValue.setVisible(true);
		nameLbl.setText(constants.spName());
		
		startTimeLbl.setVisible(true);		
		startTimeValue.setVisible(true);
		startTimeLbl.setText(constants.circuitStart());
		
		endTimeLbl.setVisible(true);
		endTimeValue.setVisible(true);
		endTimeLbl.setText(constants.circuitEndTime());
		//endTimeValue.removeFromParent();
		endTimeListBox.removeFromParent();
		
		exchangeStudLbl.setVisible(true);
		exchangeStudLbl.setText(constants.students());
		
		exchangeStudentListBox.setVisible(true);
		
		okButton.setVisible(true);
		okButton.setText(constants.exchange());
		cancelButton.setVisible(false);
		saveBtn.removeFromParent();
		breakDuration.removeFromParent();
		
		disableContextMenu();
		clearButton.removeFromParent();
	}
	
	public void createExchangeSPPopupView()
	{
		examinerNameLbl.removeFromParent();
		examinerSuggestionBox.removeFromParent();
		edit.removeFromParent();
		
		nameLbl.setVisible(true);
		nameValue.setVisible(true);
		nameLbl.setText(constants.spName());
		
		startTimeLbl.setVisible(true);		
		startTimeValue.setVisible(true);
		startTimeLbl.setText(constants.circuitStart());
		
		endTimeLbl.setVisible(true);
		endTimeValue.setVisible(true);
		endTimeLbl.setText(constants.circuitEndTime());
		//endTimeValue.removeFromParent();
		endTimeListBox.removeFromParent();
		
		exchangeSPLbl.setVisible(true);
		exchangeSPLbl.setText(constants.standardizedPatient());
		
		exchangeSpListBox.setVisible(true);
		
		okButton.setVisible(true);
		okButton.setText(constants.exchange());
		cancelButton.setVisible(false);
		saveBtn.removeFromParent();
		breakDuration.removeFromParent();
		
		//by spec change[
		exchangeStudLbl.setVisible(false);
		exchangeStudentListBox.setVisible(false);
		
		exchangeStudLbl.removeFromParent();
		exchangeStudentListBox.removeFromParent();
		//by spec change]
		clearButton.removeFromParent();
		
		disableContextMenu();
	}
	
	private void disableContextMenu()
	{
		this.addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
	}
	//by spec change]
}
