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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleTrainingViewImpl extends PopupPanel implements ScheduleTrainingView {

	private static ScheduleTrainingViewImplUiBinder uiBinder = GWT.create(ScheduleTrainingViewImplUiBinder.class);
	
	interface ScheduleTrainingViewImplUiBinder extends UiBinder<Widget, ScheduleTrainingViewImpl> {
	
	}
	
	@UiField
	Label dateLabel;
	
	@UiField
	HTMLPanel trainingHtmlPanel;
	
	@UiField
	VerticalPanel scheduledTrainingsPanel;
	
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
	
	@UiField
	CheckBox ignoreTrainingBlock;
	
	@UiField
	Image bottomAttow;
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
		
	private Delegate delegate;
	
	public ScheduleTrainingViewImpl() {
		
		setWidget(uiBinder.createAndBindUi(this));
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		
		this.bottomAttow.setUrl("osMaEntry/gwt/unibas/images/bottomarrow.png");
	}

	@UiHandler("cancelButton")
	public void cancelButtonClicked(ClickEvent event){
		Log.info("cancel button clicked");
		this.startTimeBox.setValue("");
		this.endTimeBox.setValue("");
		this.standardizedRoleSuggestionBox.setSelected(null);
		this.hide();
		delegate.cancelButtonClickedOfPopup();
	}
	
	@UiHandler("schedultButton")
	public void scheduleButtonClicked(ClickEvent event){
		Log.info("schedule button clicked");
		this.hide();
		boolean isBindTrainingTosuggestion=false;
		delegate.schedultTrainingWithGivenData(startTimeBox.getValue(),endTimeBox.getValue(),standardizedRoleSuggestionBox.getSelected(),isBindTrainingTosuggestion);
		
		this.startTimeBox.setValue("");
		this.endTimeBox.setValue("");
		this.standardizedRoleSuggestionBox.setSelected(null);
	}
	
	@UiHandler("ignoreTrainingBlock")
	public void ignoreTrainingBlockCheckBoxClicked(ClickEvent event){
		delegate.ignoreTrainingBlock();
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
	public void setValueOfIgnoreTrainingBlockCheckBox(boolean value){
		ignoreTrainingBlock.setValue(value);
	}

	public HTMLPanel getTrainingHtmlPanel() {
		return trainingHtmlPanel;
	}
}
