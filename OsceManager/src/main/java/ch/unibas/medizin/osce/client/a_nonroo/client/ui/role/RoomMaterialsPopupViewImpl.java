package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Collection;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class RoomMaterialsPopupViewImpl extends PopupPanel implements RoomMaterialsPopupView{
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public StandardizedRoleProxy standardizedRoleProxy;
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, RoomMaterialsPopupViewImpl> {
	}
	
	
	@UiField(provided = true)
	FocusableValueListBox<MaterialUsedFromTypes> used_from = new FocusableValueListBox<MaterialUsedFromTypes>(
			new EnumRenderer<MaterialUsedFromTypes>());

	@UiField
	IntegerBox materialCount;

	@UiField
	Button saveRoomMaterial;
	
	@UiField
	Button cancel;
	
	@UiField(provided = true)
	ValueListBox<MaterialListProxy> materialList = new ValueListBox<MaterialListProxy>(
			new AbstractRenderer<MaterialListProxy>() {
				public String render(MaterialListProxy obj) {
					return obj == null ? "" : String.valueOf(obj.getName());
				}
			});
	
	public void setProxy(StandardizedRoleProxy standardizedRoleProxy)
	{
		this.standardizedRoleProxy=standardizedRoleProxy;
	}
	
	/*public RoomMaterialsPopupViewImpl() 
	{
		super(true);
		add(BINDER.createAndBindUi(this));
		used_from.setAcceptableValues(java.util.Arrays.asList(MaterialUsedFromTypes.values()));		
	}*/
	public RoomMaterialsPopupViewImpl(StandardizedRoleProxy standardizedRoleProxy)
	{		
		super(true);		
		add(BINDER.createAndBindUi(this));
		used_from.setAcceptableValues(java.util.Arrays.asList(MaterialUsedFromTypes.values()));		
		//this.standardizedRoleProxy=standardizedRoleProxy;	
	
	}
	
	@Override
	public void setMaterialListPickerValues(Collection<MaterialListProxy> values) {
		materialList.setAcceptableValues(values);
	}
	
	@UiHandler("saveRoomMaterial")
	public void saveRoomMaterialClicked(ClickEvent event)
	{
		if (materialList.getValue() == null	|| materialList.getValue().toString() == "") 
		{
			/*Window.confirm("Please enter a value for Room material list");*/
			
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Success");
			 dialogBox.showConfirmationDialog("Please enter a value for Room material list");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
					return;
						}
					});

			
			
//E: Issue Role
			return;
		}
		if (materialCount.getValue() == null || materialCount.getValue().toString() == "") 
		{
			/*Window.confirm("Please enter a value for Material Count number");*/
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Success");
			 dialogBox.showConfirmationDialog("Please enter a value for Material Count number");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
					return;
						}
					});

			
//E: Issue Role
			return;
		}
		if (used_from.getValue() == null || used_from.getValue().toString() == "") 
		{
		/*	Window.confirm("Please enter a value for \"For who\"");*/
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Success");
			 dialogBox.showConfirmationDialog("Please enter a value used for");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
					return;
						}
					});

			
//E: Issue Role
			return;
		}
		
		Log.info("saveRoomMaterial Clicked");
		Log.info("="+materialCount.getValue()+"="+used_from.getValue()+"="+materialList.getValue().getName());
		
		Log.info("Stand.Role.Proxy: " + this.standardizedRoleProxy.getShortName());
		delegate.newUsedMaterialButtonClicked(materialCount.getValue(),used_from.getValue(), this.standardizedRoleProxy,materialList.getValue());
		hide();
		
	}
	
	@UiHandler("cancel")
	public void cancelClicked(ClickEvent event)
	{
		Log.info("cancel Clicked");		
		hide();
	}
	
}
