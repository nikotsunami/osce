package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
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
	FocusableValueListBox<MaterialUsedFromTypes> used_from = 
			new FocusableValueListBox<MaterialUsedFromTypes>(new EnumRenderer<MaterialUsedFromTypes>());

	@UiField
	IntegerBox materialCount;

	@UiField
	Button saveRoomMaterial;
	
	@UiField
	Button cancel;
	
	@UiField
	Label name;
	
	@UiField
	Label number;
	
	@UiField
	Label useFor;
	
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	public DefaultSuggestBox<MaterialListProxy, EventHandlingValueHolderItem<MaterialListProxy>> materialList;

	
	/*@UiField(provided = true)
	ValueListBox<MaterialListProxy> materialList = new ValueListBox<MaterialListProxy>(
			new AbstractRenderer<MaterialListProxy>() {
				public String render(MaterialListProxy obj) {
					return obj == null ? "" : String.valueOf(obj.getName());
				}
			});*/
	
	//Issue # 122 : Replace pull down with autocomplete.
	
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
		name.setText(constants.roomMaterialName() + ":");
		number.setText(constants.roomMaterialNumber() + ":");
		useFor.setText(constants.roomMaterialUser() + ":");
	}
	
	//Issue # 122 : Replace pull down with autocomplete.
	@Override
	public void setMaterialListPickerValues(List<MaterialListProxy> values) {
	//public void setMaterialListPickerValues(Collection<MaterialListProxy> values) {
		
		
			//materialList.setAcceptableValues(values);
		

			DefaultSuggestOracle<MaterialListProxy> suggestOracle1 = (DefaultSuggestOracle<MaterialListProxy>) materialList.getSuggestOracle();
			suggestOracle1.setPossiblilities(values);
			
			
			materialList.setSuggestOracle(suggestOracle1);
			
			//materialList.setRenderer(new MaterialListProxyRenderer());
			materialList.setRenderer(new AbstractRenderer<MaterialListProxy>() {

				@Override
				public String render(MaterialListProxy object) {
					// TODO Auto-generated method stub
					if(object!=null)
					return object.getName()+" ";
					else
						return "";
				}
			});

		//Issue # 122 : Replace pull down with autocomplete.
	}
	
	@UiHandler("saveRoomMaterial")
	public void saveRoomMaterialClicked(ClickEvent event)
	{
		Log.info("Call saveRoomMaterialClicked");

		// Highlight onViolation
		/*if (materialList.getValue() == null	|| materialList.getValue().toString() == "") 
		{
			Window.confirm("Please enter a value for Room material list");
			
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
			Window.confirm("Please enter a value for Material Count number");
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
			Window.confirm("Please enter a value for \"For who\"");
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
		}*/
		// E Highlight onViolation
				
		//Log.info("="+materialCount.getValue()+"="+used_from.getValue()+"="+materialList.getValue().getName());		
		//Log.info("Stand.Role.Proxy: " + this.standardizedRoleProxy.getShortName());
		//Issue # 122 : Replace pull down with autocomplete.
		//delegate.newUsedMaterialButtonClicked(materialCount.getValue(),used_from.getValue(), this.standardizedRoleProxy,materialList.getValue());
		delegate.newUsedMaterialButtonClicked(materialCount.getValue(),used_from.getValue(), this.standardizedRoleProxy,materialList.getSelected());
		//Issue # 122 : Replace pull down with autocomplete.
		// Highlight onViolation
		//hide();
		// E Highlight onViolation
		
	}
	
	@UiHandler("cancel")
	public void cancelClicked(ClickEvent event)
	{
		Log.info("cancel Clicked");		
		hide();
	}
	
}
