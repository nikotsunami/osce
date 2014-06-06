package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SingleDaySelectedViewImpl extends Composite implements SingleDaySelectedView {

	private static SingleDaySelectedViewImplUiBinder uiBinder = GWT.create(SingleDaySelectedViewImplUiBinder.class);
	
	interface SingleDaySelectedViewImplUiBinder extends UiBinder<Widget, SingleDaySelectedViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	@UiField
	IconButton proposeTrainigDay;
	
	@UiField
	IconButton removeTrainigDay;
	
	@UiField
	IconButton proposeOsceDay;
	
	@UiField
	IconButton removeOsceDay;
	
	@UiField
	IconButton scheduleTraining;
	
	
	
	@UiField
	VerticalPanel buttonPanel;

	@UiField
	VerticalPanel labelPanel;
	
	@UiField
	Label msgLabel;
	
	Date date;
	
	
	public SingleDaySelectedViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler(value={"proposeTrainigDay","proposeOsceDay","scheduleTraining","removeTrainigDay","removeOsceDay"})
	public void mouseDownEventOfAllButtons(MouseDownEvent event)
	{
		event.preventDefault();
		event.stopPropagation();
	}
	
	@UiHandler("proposeTrainigDay")
	public void proposeTrainingDayButtonClicked(ClickEvent event){
		Log.info("propose Training Day Button Clicked");
		delegate.proposeTrainingDateClicked();
	}
	
	@UiHandler("proposeOsceDay")
	public void proposeOSceDayButtonClicked(ClickEvent event){
		Log.info("propose osce Day Button Clicked");
		delegate.proposeOsceDayButtonClicked();
	}
	
	@UiHandler("scheduleTraining")
	public void scheduleTrainingButtonClicked(ClickEvent event){
		Log.info("schedule Training Button Clicked");
		delegate.scheduleTraingClicked(event);
	}

	@UiHandler("removeTrainigDay")
	public void removeTrainingButtonClicked(ClickEvent event){
		Log.info("Remove Training day Button Clicked");
		delegate.removeTrainingDayButtonClicked();
	}
	
	@UiHandler("removeOsceDay")
	public void removeOsceButtonClicked(ClickEvent event){
		Log.info("remove osce day Button Clicked");
		 delegate.removeOsceDayButtonClicked();
	}

	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public IconButton getProposedTrainingDayButton() {
		return proposeTrainigDay;
		
	}

	@Override
	public IconButton getRemoveProposedTrainingDaButton() {
		return removeTrainigDay;
		
	}

	@Override
	public IconButton getProposedOsceDayButton() {
		return proposeOsceDay;
		
	}

	@Override
	public IconButton getRemoveProposedOsceDaybutton() {
		return removeOsceDay;
		
	}

	
}
