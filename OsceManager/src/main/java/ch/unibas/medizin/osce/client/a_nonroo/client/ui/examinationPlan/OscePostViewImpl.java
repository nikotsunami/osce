package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OscePostViewImpl  extends Composite implements OscePostView{

	private static OscePostViewImplUiBinder uiBinder = GWT
			.create(OscePostViewImplUiBinder.class);

	interface OscePostViewImplUiBinder extends UiBinder<Widget, OscePostViewImpl> {
	}

	OsceConstants constants = GWT.create(OsceConstants.class);
	
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
	
	private CourseProxy courseProxy;
	
	private OscePostRoomProxy oscePostRoomProxy;
	
	//Added for OMS-158.
	private OsceDayProxy osceDayProxy;
	
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
		//changed for OMS-158. Now we are also showing room of break on click of oscePostPanel.
		showOscePostPopupView();
	}
	//changed for OMS-158.
	/**
	 * showing osce post popup
	 */
	public void showOscePostPopupView()
	{
		if(popupView == null)
		{
			popupView=new PopupViewImpl();
			
			if(oscePostProxy!=null){
				popupView.createOscePostPopupView();
			}else{
				popupView.createPopupToshowReserveRoomOfSP();
			}
			((PopupViewImpl)popupView).setAnimationEnabled(true);
			
			
			//((PopupViewImpl)popupView).setWidth("150px");
		
			//RootPanel.get().add(((PopupViewImpl)popupView));
			
			popupView.getOkButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					//validate Entered Data
					((PopupViewImpl)popupView).hide();
				}
			});
		}
			//changed for OMS-158.
		if(oscePostProxy!=null){
			//clicked on station label
			OsceConstantsWithLookup enumConstants = GWT.create(OsceConstantsWithLookup.class);
				
			//setData
			popupView.getNameValue().setText(enumConstants.getString(oscePostProxy.getOscePostBlueprint().getPostType().name()));
				
			if(oscePostProxy.getStandardizedRole() !=null)
			popupView.getStartTimeValue().setText(oscePostProxy.getStandardizedRole().getRoleTopic().getName());
				
			delegate.retrieveRoomNo(oscePostProxy,courseProxy,popupView);
			
			((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop()-195);
				
		}else{
			//clicked on SP break
			if(oscePostLbl.getText().equals(constants.exaPlanBreakPost()) && osceDayProxy!=null){
				
				if(osceDayProxy.getReserveSPRoom()!=null){
					popupView.getEndTimeValue().setText(osceDayProxy.getReserveSPRoom().getRoomNumber());
				}else{
					popupView.getEndTimeValue().setText(constants.notAssigned());
				}
				((PopupViewImpl)popupView).setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop()-114);
				((PopupViewImpl)popupView).show();
			}
		}
			
		
	
		
	}

	public CourseProxy getCourseProxy() {
		return courseProxy;
	}

	public void setCourseProxy(CourseProxy courseProxy) {
		this.courseProxy = courseProxy;
	}

	public OscePostRoomProxy getOscePostRoomProxy() {
		return oscePostRoomProxy;
	}
		
	public void setOscePostRoomProxy(OscePostRoomProxy oscePostRoomProxy) {
		this.oscePostRoomProxy = oscePostRoomProxy;
	}

	@Override
	public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
		this.osceDayProxy=osceDayProxy;
		
	}

	
}
