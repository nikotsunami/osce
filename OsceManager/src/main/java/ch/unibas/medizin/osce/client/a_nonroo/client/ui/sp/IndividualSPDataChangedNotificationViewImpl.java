package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

/**
 * This class is providing view for the individual sps who changed data.
 * @author manishp
 */

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class IndividualSPDataChangedNotificationViewImpl extends VerticalPanel  implements IndividualSPDataChangedNotificationView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	interface Binder extends UiBinder<Widget, IndividualSPDataChangedNotificationViewImpl> {
	}
	
	private IndividualSPDataChangedNotificationViewImpl  individualSPDataChangedNotificationViewImpl;
	
	
	@UiField
	Label individualSPDataChangedHeaderLabel;
	
	@UiField
	IconButton reviewChangesButton;
	
	
	public IndividualSPDataChangedNotificationViewImpl()
	{
		
		individualSPDataChangedNotificationViewImpl=this;
		add(BINDER.createAndBindUi(this));
		
		individualSPDataChangedHeaderLabel.setText(constants.individualSPDataChangeMsg());
		reviewChangesButton.setText(constants.reviewChanges());
		
		init();
		
		addClickHandlerOfButtons();
		
	}
	
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	/**
	 * This method initializing table that is used to show data to user.
	 */
	public void init() {

		
	}
	/**
	 * This method is used to add click handler of all buttons.
	 */
	private void addClickHandlerOfButtons() {
		
		reviewChangesButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Review change button clicked");
				delegate.reviewButtonClicked();
			}
		});
		
	}

	
}
