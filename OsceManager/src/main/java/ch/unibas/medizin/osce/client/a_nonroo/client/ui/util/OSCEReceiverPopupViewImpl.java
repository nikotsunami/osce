package ch.unibas.medizin.osce.client.a_nonroo.client.ui.util;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

//public class SemesterPopupViewImpl extends DialogBox implements SemesterPopupView
public class OSCEReceiverPopupViewImpl extends PopupPanel implements OSCEReceiverPopupView
{
//	private static final Binder BINDER = GWT.create(Binder.class);
	
private static OSCEReceiverPopupViewImplUiBinder uiBinder = GWT.create(OSCEReceiverPopupViewImplUiBinder.class);
	
	interface OSCEReceiverPopupViewImplUiBinder extends UiBinder<Widget, OSCEReceiverPopupViewImpl> {
	}
	
	private Delegate delegate;
	
	@UiField
	public Label lblTitle;
	
	@UiField
	public Label lblErrMessage;
	
	@UiField
	public Button btnOk;
	
	@UiHandler("btnOk")
	public void btnOkClicked(ClickEvent event)
	{
		Log.info("Ok Button Clicked.");
		hide();
	}
	
	
	public OSCEReceiverPopupViewImpl() 
	{		
		Log.info("Call SemesterPopupViewImpl() Constructor");				
		setWidget(uiBinder.createAndBindUi(this));			
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		//setWidth("300px");
		//setHeight("150px");
		center();
		//setGlassEnabled(true);	
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
}
