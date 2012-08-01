package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OscePostViewImpl  extends Composite implements OscePostView{

	private static OscePostViewImplUiBinder uiBinder = GWT
			.create(OscePostViewImplUiBinder.class);

	interface OscePostViewImplUiBinder extends UiBinder<Widget, OscePostViewImpl> {
	}

	private Delegate delegate;
	
	@UiField
	Label oscePostLbl;
	
	@UiField
	VerticalPanel studentSlotsVP;
	
	@UiField
	FocusPanel oscePostPanel;
	
	PopupView popupView;

	public FocusPanel getOscePostPanel() {
		return oscePostPanel;
	}

	public VerticalPanel getStudentSlotsVP() {
		return studentSlotsVP;
	}

	public VerticalPanel getSpSlotsVP() {
		return spSlotsVP;
	}

	public VerticalPanel getExaminerVP() {
		return examinerVP;
	}

	@UiField
	VerticalPanel spSlotsVP;
	
	@UiField
	VerticalPanel examinerVP;
	
	
	private OscePostProxy oscePostProxy;
	
	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}

	public void setOscePostProxy(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
	}

	public Label getOscePostLbl() {
		return oscePostLbl;
	}

	public OscePostViewImpl()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@UiHandler("oscePostPanel")
	public void oscePostPanelClicked(ClickEvent event)
	{
		Log.info("oscePostPanel Clicked");
		showOscePostPopupView();
		
	}
	public void showOscePostPopupView()
	{
		if(popupView == null)
		{
			popupView=new PopupViewImpl();
			popupView.createOscePostPopupView();
			
			((PopupViewImpl)popupView).setAnimationEnabled(true);
		
			
			
			
			//((PopupViewImpl)popupView).setWidth("150px");

		
			RootPanel.get().add(((PopupViewImpl)popupView));
			
			
			((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop()-130);
			
			popupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					((PopupViewImpl)popupView).hide();
					
					
				}
			});
			
			
			//setDAta
			popupView.getNameValue().setText(oscePostProxy.getOscePostBlueprint().getPostType().toString());
			popupView.getStartTimeValue().setText(oscePostProxy.getStandardizedRole().getRoleTopic().getName());
			
		}
		
		((PopupViewImpl)popupView).show();
		
	}
}
