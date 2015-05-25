/**
 * Popup class to show first name and name values in popup
 * @author Manish
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class NamePrenamePopupViewImpl  extends PopupPanel  implements NamePrenamePopupView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	//private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label preNameLbl;
	
	@UiField
	Label preNameValue;
	
	@UiField
	Label nameLbl;
	
	@UiField 
	Label nameValue;
	//Commented below code as I hiding poup on mouse out. Will use this if required
	/*@UiField
	Button cancelButton;

	@UiHandler("cancelButton")
	public void cancelButtonClicked(ClickEvent event){
		Log.info("cancel button clicked");
		this.hide();
	}*/
	
	interface Binder extends UiBinder<Widget, NamePrenamePopupViewImpl> {
	}
	public Label getNameLbl() {
		return nameLbl;
	}
	public Label getNameValue() {
		return nameValue;
	}
	
	public NamePrenamePopupViewImpl()
	{
		super(true);
		add(BINDER.createAndBindUi(this));
		this.setAnimationEnabled(true);
	}
	
	
	public void setDelegate(Delegate delegate) {
		//this.delegate = delegate;
	}
	
	

	/**
	 * showing popup
	 */
	@Override
	public void showPopup(){
		Log.info("showing popup");
		super.show();
	}
	/**
	 * hiding popup
	 */
	@Override
	public void hidePopup(){
		Log.info("hiding popup");
		super.hide();
	}

	/**
	 * setting pre-name value
	 * @param prename
	 */
	@Override
	public void setPrenameValue(String prename){
		preNameValue.setText(prename);
	}
	/**
	 * setting name value
	 * @param name
	 */
	@Override
	public void setNameValue(String name){
		nameValue.setText(name);
	}
	/**
	 * setting prename and name value
	 * @param preName
	 * @param name
	 */
	@Override
	public void setPreNameAndName(String preName,String name){
		if(preName==null || preName.isEmpty()){
			setPrenameValue(constants.notAssigned());
		}else{
			setPrenameValue(preName);
		}
		if(name==null || name.isEmpty()){
			setNameValue(constants.notAssigned());
		}else{
			setNameValue(name);
		}
	}
	
	public void setPopupPosition(int left,int top){
		super.setPopupPosition(left, top);
	}
	@Override
	public boolean isShowingPopup() {
		return super.isShowing();
	}
}
