package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

/**
 * This class is providing view for the individual sps who sent edit data request.
 * @author manishp
 */
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

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

public class SPEditRequestNotificationViewImpl extends PopupPanel  implements SPEditRequestNotificationView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	interface Binder extends UiBinder<Widget, SPEditRequestNotificationViewImpl> {
	}
	
	private SPEditRequestNotificationViewImpl  spEditRequestViewImpl;
	
	@UiField(provided = true)
	public AdvanceCellTable<StandardizedPatientProxy> table;
		
	@UiField
	Label headerLabel;
	
	@UiField
	Label allowEditLabel;
	
	@UiField
	IconButton allowButton;
	
	@UiField
	IconButton denyButton;
	
	@UiField
	IconButton decideLaterButton;
	
	@UiField(provided = true)
	public SimplePager pager;
	
	public SPEditRequestNotificationViewImpl()
	{
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<StandardizedPatientProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		spEditRequestViewImpl=this;
		add(BINDER.createAndBindUi(this));
		
		headerLabel.setText(constants.spsSendEditRequest());
		allowEditLabel.setText(constants.allowUserToEditData());
		
		init();
		
		addClickHandlerOfButtons();
		
		spEditRequestViewImpl.setGlassEnabled(true);
		spEditRequestViewImpl.setPopupPosition(432,95);
		spEditRequestViewImpl.addStyleName("popupPanelWidth");
		spEditRequestViewImpl.hide();
	}
	
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

	/**
	 * This method initializing table that is used to show data to user.
	 */
	public void init() {

		table.addColumn(new TextColumn<StandardizedPatientProxy>() {
			
			{ this.setSortable(false); 
			
			}	

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StandardizedPatientProxy object) {
				return renderer.render(object.getPreName());
			}
		}, constants.preName());

		table.addColumn(new TextColumn<StandardizedPatientProxy>() {
			{ 
				this.setSortable(false); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StandardizedPatientProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.name());
		
	}
	/**
	 * This method is used to add click handler of all buttons.
	 */
	private void addClickHandlerOfButtons() {
		allowButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Allow sps edit data button clicked");
				delegate.allowEditRequestButtonClicked();
				
			}
		});
		
		denyButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("Deny sps edit data button clicked");
				delegate.denyEditRequestButtonClicked();
				
			}
		});
		
		decideLaterButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("decide later button clicked");
				spEditRequestViewImpl.hide();
				delegate.showAllSpsWhoEditedData();
			}
		});
	}


	public AdvanceCellTable<StandardizedPatientProxy> getTable() {
		return table;
	}


	public void setButtonText(int totalSPs) {
		if(totalSPs==1){
			allowButton.setText(constants.allow());
			denyButton.setText(constants.deny());
			decideLaterButton.setText(constants.decideLater());
		}else if(totalSPs > 1 ){
			allowButton.setText(constants.allowAllSps());
			denyButton.setText(constants.denyAllSps());
			decideLaterButton.setText(constants.individuallyDecide());
		}
		
	}
	
}
