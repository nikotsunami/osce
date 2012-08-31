package ch.unibas.medizin.osce.client.style.widgets;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import java.util.List;

import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class SimpleShowErrorDialogBox extends DialogBox {
	
	private DialogBox dialogBox = null;
	private VerticalPanel dialogContents = null;
	private Button closeButton = null;
	
		
	public SimpleShowErrorDialogBox(String title,List<String> errorMsgs,String closeButtonName){
		init(errorMsgs);
		setTitle(title);
		
		setCloseButtonName(closeButtonName);
	}
	
	/**
	 * Initialize the dialog box
	 */
	private void init(List<String> errorMsgs){
		dialogBox = new DialogBox();
		
		
		// Create a table to layout the content
		dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);
		
		// Add a close button at the bottom of the dialog
		closeButton = new Button();
		closeButton.addClickHandler(new ClickHandler() {
			  public void onClick(ClickEvent event) {
				dialogBox.hide();
			  }
		});
		setBodyErrorMessages(errorMsgs);
		
		dialogContents.add(closeButton);

		dialogContents.setCellHorizontalAlignment(
			  closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
		
		dialogContents.setCellVerticalAlignment(
			  closeButton, HasVerticalAlignment.ALIGN_BOTTOM);

		
		
	}
	
	/**
	 * set the DialogBox title 
	 */
	public void setTitle(String title){
		dialogBox.setText(title);
	}
	
	/**
	 * set the close button title
	 */
	public void setCloseButtonName(String name){
		closeButton.setText(name);
	}
	
	/**
	 * set a list of error messages which you want to show for user
	 */
	public void setBodyErrorMessages(List<String> messages){
		for(String message : messages){
			// Add some text to the top of the dialog
			HTML details = new HTML(message+"<br/><br/>");
			dialogContents.add(details);
			dialogContents.setCellHorizontalAlignment(
				details, HasHorizontalAlignment.ALIGN_LEFT);
		}
	}
	
	/**
	 * show the simpleShowErrorDialogBox 
	 */
	public void show(){
		dialogBox.center();
		dialogBox.show();
	}
	
}
