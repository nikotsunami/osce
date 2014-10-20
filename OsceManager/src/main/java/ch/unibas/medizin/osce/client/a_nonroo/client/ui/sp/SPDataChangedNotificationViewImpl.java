package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

/**
 * This class is providing view for the sps who changed data.
 * @author manishp
 */

import ch.unibas.medizin.osce.client.managed.request.SpStandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SPDataChangedNotificationViewImpl extends PopupPanel  implements SPDataChangedNotificationView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	interface Binder extends UiBinder<Widget, SPDataChangedNotificationViewImpl> {
	}
	
	private SPDataChangedNotificationViewImpl  spDataChangedNotificationViewImpl;
	
	@UiField(provided = true)
	public AdvanceCellTable<SpStandardizedPatientProxy> table;
		
	@UiField
	Label headerLabel;
	
	@UiField
	Label reviewMsgLabel;
	
	@UiField
	IconButton reviewChangesButton;
	
	@UiField
	IconButton reviewLaterButton;
	
	@UiField(provided = true)
	public SimplePager pager;
	
	public SPDataChangedNotificationViewImpl()
	{
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<SpStandardizedPatientProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		spDataChangedNotificationViewImpl=this;
		add(BINDER.createAndBindUi(this));
		
		headerLabel.setText(constants.spChangedData());
		reviewMsgLabel.setText(constants.wantToReviewData());
		reviewChangesButton.setText(constants.reviewChanges());
		reviewLaterButton.setText(constants.reviewLater());
		
		init();
		
		addClickHandlerOfButtons();
		
		spDataChangedNotificationViewImpl.setGlassEnabled(true);
		spDataChangedNotificationViewImpl.setPopupPosition(432,95);
		spDataChangedNotificationViewImpl.addStyleName("popupPanelWidth");
		spDataChangedNotificationViewImpl.hide();
	}
	
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	/**
	 * This method initializing table that is used to show data to user.
	 */
	public void init() {

		table.addColumn(new TextColumn<SpStandardizedPatientProxy>() {
			
			{ this.setSortable(false); 
			
			}	

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(SpStandardizedPatientProxy object) {
				return renderer.render(object.getPreName());
			}
		}, constants.preName());

		table.addColumn(new TextColumn<SpStandardizedPatientProxy>() {
			{ 
				this.setSortable(false); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(SpStandardizedPatientProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.name());
		
	}
	/**
	 * This method is used to add click handler of all buttons.
	 */
	private void addClickHandlerOfButtons() {
		
		reviewChangesButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Review changes button clicked");
				delegate.reviewChangeButtonClicked();
			}
		});
		
		reviewLaterButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Review Later Button clicked");
				spDataChangedNotificationViewImpl.hide();
			}
		});
	}


	public AdvanceCellTable<SpStandardizedPatientProxy> getTable() {
		return table;
	}

	public void showView(boolean isToShow){
		if(isToShow){
			spDataChangedNotificationViewImpl.show();
		}else{
			spDataChangedNotificationViewImpl.hide();	
		}
	}
}
