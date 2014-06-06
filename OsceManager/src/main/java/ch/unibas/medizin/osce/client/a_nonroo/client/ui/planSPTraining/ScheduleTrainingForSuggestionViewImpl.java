package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleTrainingForSuggestionViewImpl extends PopupPanel implements ScheduleTrainingForSuggestionView {

	private static ScheduleTrainingForSuggestionViewImplUiBinder uiBinder = GWT.create(ScheduleTrainingForSuggestionViewImplUiBinder.class);
	
	interface ScheduleTrainingForSuggestionViewImplUiBinder extends UiBinder<Widget, ScheduleTrainingForSuggestionViewImpl> {
	
	}
	
	@UiField
	HTMLPanel trainingHtmlPanel;
	
	
	@UiField
	Label dateLabel;
	
	@UiField
	Label scheduledTrainingLbl;
	
	
	@UiField
	VerticalPanel scheduledTrainingsPanel;
	
	@UiField
	Label suggestedTrainingMorningLbl;
	
	@UiField
	VerticalPanel suggestedTrainingForMorningPanel;
	
	@UiField
	IconButton showSuggestionForMorning;
	
	@UiField
	IconButton hideSuggestionForMorning;
	
	@UiField
	Label suggestedTrainingAfternoonLbl;
	
	@UiField
	VerticalPanel suggestedTrainingForAfternoonPanel;
	
	@UiField
	IconButton showSuggestionForAfternoon;
	
	@UiField
	IconButton hideSuggestionForAfternoon;
	
	/*@UiField
	Label overrideTrainingLbl;
	
	@UiField
	VerticalPanel overrideSuggestionsPanel;*/
	
	@UiField
	HorizontalPanel scheduleTrainingHP;
	
	@UiField
	TextBox startTimeBox;
	
	@UiField
	TextBox endTimeBox;
	
	@UiField
	DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> standardizedRoleSuggestionBox;
	
	@UiField
	HorizontalPanel buttonHP;
	
	@UiField
	IconButton cancelButton;
	
	@UiField
	IconButton schedultButton;
	
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
		
	private Delegate delegate;
	
	public ScheduleTrainingForSuggestionViewImpl() {
		
		setWidget(uiBinder.createAndBindUi(this));
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		
		suggestedTrainingMorningLbl.setText(constants.suggestedTraining() + "- " + constants.morning());
		suggestedTrainingAfternoonLbl.setText(constants.suggestedTraining() + " - " +constants.afterNoon());
		
	}

	@UiHandler("cancelButton")
	public void cancelButtonClicked(ClickEvent event){
		Log.info("cancel button clicked");
		this.startTimeBox.setValue("");
		this.endTimeBox.setValue("");
		this.standardizedRoleSuggestionBox.setSelected(null);
		this.hide();
		delegate.cancelButtonClickedOfPopupWhenSuggestionShowing();
	}
	
	@UiHandler("schedultButton")
	public void scheduleButtonClicked(ClickEvent event){
		Log.info("schedule button clicked");
		this.hide();
		boolean isBindTrainingToSuggestion=false;
		delegate.schedultTrainingWithGivenDataWhenSuggestionIsShown(startTimeBox.getValue(),endTimeBox.getValue(),standardizedRoleSuggestionBox.getSelected(),isBindTrainingToSuggestion);
		
		this.startTimeBox.setValue("");
		this.endTimeBox.setValue("");
		this.standardizedRoleSuggestionBox.setSelected(null);
	}
	
	@UiHandler("showSuggestionForMorning")
	public void showSuggestionForMorningButtonClicked(ClickEvent event){
		this.showSuggestionForMorning.setVisible(false);
		this.hideSuggestionForMorning.setVisible(true);
		
		delegate.showSuggestionsForMorningButtonClicked();
	}
	@UiHandler("hideSuggestionForMorning")
	public void hideSuggestionForMorningButtonClicked(ClickEvent event){
		this.showSuggestionForMorning.setVisible(true);
		
		this.hideSuggestionForMorning.setVisible(false);
		
		this.suggestedTrainingForMorningPanel.clear();
	}
	
	@UiHandler("showSuggestionForAfternoon")
	public void showSuggestionForAfternoonButtonClicked(ClickEvent event){
		this.showSuggestionForAfternoon.setVisible(false);
		this.hideSuggestionForAfternoon.setVisible(true);
		
		delegate.showSuggestionsForAfternoonButtonClicked();
	}
	
	@UiHandler("hideSuggestionForAfternoon")
	public void hideSuggestionForAfternoon(ClickEvent event){
		this.showSuggestionForAfternoon.setVisible(true);
		
		this.hideSuggestionForAfternoon.setVisible(false);
		
		this.suggestedTrainingForAfternoonPanel.clear();
	}
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}

	@Override
	public VerticalPanel getScheduledTrainingsPanel() {
		return scheduledTrainingsPanel;
	}

	@Override
	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestionBox() {
		return this.standardizedRoleSuggestionBox;
	}

	@Override
	public void setDateValue(String formatedDate) {
		Log.info("setting data on date Label");
		this.dateLabel.setText(formatedDate);
	}

	@Override
	public VerticalPanel getSuggestedTraingsForMorningPanel() {
		return this.suggestedTrainingForMorningPanel;
	}

	@Override
	public VerticalPanel getSuggestedTraingsForAfternoonPanel() {
		return this.suggestedTrainingForAfternoonPanel;
	}

	@Override
	public void setStartTimeValue(Date startTimeDate) {
	
		String startHours =startTimeDate.getHours() < 10 ? "0" + String.valueOf(startTimeDate.getHours()): String.valueOf(startTimeDate.getHours());
		
		String startMinutes =startTimeDate.getMinutes() < 10? "0"+ String.valueOf(startTimeDate.getMinutes()) : String.valueOf(startTimeDate.getMinutes());
		
		this.startTimeBox.setValue(startHours+ ":" + startMinutes );
	}
	
	@Override
	public void setEndTimeValue(Date endTimeDate) {

		String endHours =endTimeDate.getHours() < 10 ? "0" + String.valueOf(endTimeDate.getHours()): String.valueOf(endTimeDate.getHours());
		
		String endMinutes=endTimeDate.getMinutes() < 10? "0"+ String.valueOf(endTimeDate.getMinutes()) : String.valueOf(endTimeDate.getMinutes());
		
		this.endTimeBox.setValue(endHours + ":" + endMinutes);
	}

	@Override
	public IconButton getShowSuugestionForMorningButton() {
		return showSuggestionForMorning;
	}

	@Override
	public IconButton getShowSuugestionForAfternoonButton() {
		return showSuggestionForAfternoon;
	}
	
	public HTMLPanel getTrainingHtmlPanel() {
		return trainingHtmlPanel;
	}

	/*@Override
	public VerticalPanel getOverrideTrainingsPanel() {
		return overrideSuggestionsPanel;
	}
*/
	@Override
	public Label getScheduleTrainingLabel() {
		return scheduledTrainingLbl;
	}

	@Override
	public IconButton getHideSuggestionForMorningButton() {
		return this.hideSuggestionForMorning;
	}

	@Override
	public IconButton getHideSuggestionForAfternoonButton() {
		return this.hideSuggestionForAfternoon;
	}
	
}
