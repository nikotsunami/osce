package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.Date;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MultipleDaySelectedViewImpl extends Composite implements MultipleDaySelectedView {

	private static MultipleDaySelectedViewImplUiBinder uiBinder = GWT.create(MultipleDaySelectedViewImplUiBinder.class);
	
	interface MultipleDaySelectedViewImplUiBinder extends UiBinder<Widget, MultipleDaySelectedViewImpl> {
	}
		
	private Delegate delegate;
	
	@UiField
	IconButton proposeTrainigDays;
	
	@UiField
	IconButton proposeOsceDays;
	
	@UiField
	VerticalPanel buttonPanel;

	Date date;
	
	public MultipleDaySelectedViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler(value={"proposeTrainigDays","proposeOsceDays"})
	public void mouseDownEventOfAllButtons(MouseDownEvent event)
	{
		event.preventDefault();
		event.stopPropagation();
	}
	
	@UiHandler("proposeTrainigDays")
	public void proposeTrainingDayButtonClicked(ClickEvent event){
		Log.info("propose Training Days Button Clicked");
		delegate.proposeMultipleDaysAsTrainingDaysButtonClicked();
	}
	
	@UiHandler("proposeOsceDays")
	public void proposeOSceDayButtonClicked(ClickEvent event){
		Log.info("propose osce Days Button Clicked");
		delegate.proposeMultipleDaysAsOsceDaysButtonClicked();
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

	/*@Override
	public IconButton getProposedTrainingDaysButton() {
		return proposeTrainigDays;
		
	}
	
	@Override
	public IconButton getProposedOsceDaysButton() {
		return proposeOsceDays;
		
	}
*/

	
}
