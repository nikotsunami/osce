package ch.unibas.medizin.osce.client.a_nonroo.client.ui.util;


import org.apache.bcel.classfile.Constant;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.client.style.widgets.tooltip.TooltipPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

//public class SemesterPopupViewImpl extends DialogBox implements SemesterPopupView
public class OSCEReceiverPopupViewImpl extends TooltipPanel implements OSCEReceiverPopupView {
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
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	VerticalPanel panel = new VerticalPanel();
	HorizontalPanel HPbtn=new HorizontalPanel();
	Button btnOk=new Button();
	
	public OSCEReceiverPopupViewImpl() {
		Log.info("Call SemesterPopupViewImpl() Constructor");
		
		this.setWidget(panel);
		this.center();		
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setGlassEnabled(true);	
		setWidth("300px");
	}
	
	public OSCEReceiverPopupViewImpl(UIObject target) {		
		super(target, Position.BELOW, Align.RIGHT);
		Log.info("OSCEReceiverPopupViewImpl()");				
		setWidget(uiBinder.createAndBindUi(this));
//		setPopupPositionAndShow();
		show();
	}

	public void showMessage(String message) {
		Log.info("Call showMessage" + message);		
		
		HPbtn.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		HPbtn.add(btnOk);
		
		panel.add(new HTML(message));						
		panel.add(HPbtn);
		
		btnOk.setText(constants.okBtn());
		btnOk.addStyleName("marginTop15");
		
		btnOk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		display();
	}

	private void display() {		
		Log.info("display() call....." + panel.getWidgetCount());				
		setVisible(true);
		this.setGlassEnabled(true);
		this.show();		

	}
	/*
	 * public void setDelegate(Delegate delegate) { this.delegate = delegate; }
	 */

}
