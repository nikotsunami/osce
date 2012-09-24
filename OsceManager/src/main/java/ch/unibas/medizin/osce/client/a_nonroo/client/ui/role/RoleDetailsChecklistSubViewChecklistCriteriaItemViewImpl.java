package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.Validator;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl extends Composite implements RoleDetailsChecklistSubViewChecklistCriteriaItemView{

private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	// SPEC Change
	private MessageConfirmationDialogBox confirmationDialogBox;
	
	@UiField
	Label criteriaLbl;
	
	//Spec
	public RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl roleDetailsChecklistSubViewChecklistCriteriaItemViewImpl;

	@UiField
	public AbsolutePanel roleCriteriaAP;
	
	public CriteriaPopupView criteriaPopup;
	
	Map<String, Widget> checklistCriteriaMap;
	
	@Override
	public AbsolutePanel getRoleCriteriaAP()
	{
		Log.info("Impl Drag getRoleCriteriaAP Called");
		return this.roleCriteriaAP;
	}
	
	@UiField
	HorizontalPanel roleCriteriaHP;
	
	
	PickupDragController dragController;
	
	public PickupDragController getDragController() {
		Log.info("Impl Drag controller Called");
		return dragController;
	}
	
	HorizontalPanelDropController dropController;
	
	public HorizontalPanel getRoleCriteriaHP(){
		Log.info("Impl Drag getRoleCriteriaHP Called");
		return this.roleCriteriaHP;
	}
	
	//End
	public ChecklistCriteriaProxy proxy;
	
	public ChecklistCriteriaProxy getProxy() {
		return proxy;
	}

	public void setProxy(ChecklistCriteriaProxy proxy) {
		this.proxy = proxy;
	}

	public Label getCriteriaLbl() {
		return criteriaLbl;
	}

	public void setCriteriaLbl(Label criteriaLbl) {
		this.criteriaLbl = criteriaLbl;
	}

	@UiField
	IconButton delete;
	
	//issue
	@UiField
	IconButton edit;
	
	public RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		this.roleDetailsChecklistSubViewChecklistCriteriaItemViewImpl = this;
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl> {
	}
	
	//issue
	@UiHandler("edit")
	public void editOption(ClickEvent event)
	{
		criteriaPopup=new CriteriaPopupViewImpl();
		
		
		((CriteriaPopupViewImpl)criteriaPopup).setAnimationEnabled(true);
	
		((CriteriaPopupViewImpl)criteriaPopup).setWidth("100px");

		criteriaPopup.getCriteriaTxtBox().setText(roleDetailsChecklistSubViewChecklistCriteriaItemViewImpl.getProxy().getCriteria());
	
		RootPanel.get().add(((CriteriaPopupViewImpl)criteriaPopup));

		// Highlight onViolation			
		checklistCriteriaMap=new HashMap<String, Widget>();
		checklistCriteriaMap.put("criteria",criteriaPopup.getCriteriaTxtBox());
		checklistCriteriaMap.put("checklistQuestion",criteriaPopup.getCriteriaTxtBox());
		// E Highlight onViolation
		
		criteriaPopup.getOkBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				// SPEC Change
				
				if(Validator.isNotNull(criteriaPopup.getCriteriaTxtBox().getValue()))
				{
					delegate.updateCriteria(criteriaPopup.getCriteriaTxtBox().getValue(),roleDetailsChecklistSubViewChecklistCriteriaItemViewImpl);
				
					((CriteriaPopupViewImpl)criteriaPopup).hide(true);
			
					((CriteriaPopupViewImpl)criteriaPopup).criteriaTxtBox.setValue("");
				}
				else
				{
					confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					confirmationDialogBox.showConfirmationDialog(constants.requiredFields());
				}
			}
		});
	
		((CriteriaPopupViewImpl)criteriaPopup).setPopupPosition(this.edit.getAbsoluteLeft()-50, this.edit.getAbsoluteTop()-85);
		((CriteriaPopupViewImpl)criteriaPopup).show();
	}
	
	@UiHandler("delete")
	public void deleteOption(ClickEvent event)
	{
		/*if(Window.confirm("are you sure you want to delete this criteria?"))*/
		final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(
				constants.warning());
		dialogBox.showYesNoDialog(constants.criteriadelete());

		dialogBox.getYesBtn().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				delegate.deleteCriteria(roleDetailsChecklistSubViewChecklistCriteriaItemViewImpl);
				Log.info("ok click");
				return;
			}
		});

		dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

	
	}
}
