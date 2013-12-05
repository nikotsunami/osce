package ch.unibas.medizin.osce.client.a_nonroo.client.ui.util;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class OSCEReceiverPopupViewImpl extends DialogBox
{	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	VerticalPanel panel = new VerticalPanel();
	HorizontalPanel HPbtn=new HorizontalPanel();
	Button btnOk=new Button();
	private DialogBox dialogBox;
	
	
	public OSCEReceiverPopupViewImpl() {
		dialogBox=this;
		super.getCaption().asWidget().addStyleName("confirmbox");
		super.setText(constants.violationMessage());
		Log.info("Call OSCEReceiverPopupViewImpl() Constructor");		
		
		this.add(panel);
		//center();		
		setGlassEnabled(true);	
		setAnimationEnabled(true);
		setAutoHideEnabled(true);	
		//setWidth("500px");
		//setVisible(false);
		this.hide();
		HPbtn.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);	
		btnOk.setText(constants.okBtn());
		btnOk.addStyleName("marginTop15");
		HPbtn.add(btnOk);
		this.getElement().getStyle().setZIndex(1);
		btnOk.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();				
			}
		});
		
	}

	public void showMessage(String message) {
		Log.info("Call showMessage" + message);		
		
	
		panel.setWidth("500px");
		panel.add(new HTML(message));		
		panel.add(HPbtn);

		
		
		
		display();
	}

	public void showHTMLMessage(HTML message) {
		Log.info("Call showMessage" + message);		
		
		panel.setWidth("500px");
		panel.add(message);		
		panel.add(HPbtn);
		
		display();
	}
	private void display() {		
		Log.info("display() call....." + panel.getWidgetCount());				
		//dialogBox.setVisible(true);		
		dialogBox.show();	
		dialogBox.center();

	}
	/*
	 * public void setDelegate(Delegate delegate) { this.delegate = delegate; }
	 */

	public Button getBtnOk() {
		return btnOk;
	}

	
}
