package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

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
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl extends Composite implements RoleDetailsChecklistSubViewChecklistCriteriaItemView{

private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label criteriaLbl;
	
	//Spec

	@UiField
	public AbsolutePanel roleCriteriaAP;
	
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
	
	public RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl> {
	}
	
	@UiHandler("delete")
	public void deleteOption(ClickEvent event)
	{
		/*if(Window.confirm("are you sure you want to delete this criteria?"))*/
		final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
		 dialogBox.showConfirmationDialog("are you sure you want to delete this criteria?");
		 
		 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();							
				Log.info("ok click");	
				return;
					}
				});

			delegate.deleteCriteria(this);
	}

	
}
