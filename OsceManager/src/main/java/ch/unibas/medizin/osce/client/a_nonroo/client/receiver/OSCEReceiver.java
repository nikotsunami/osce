package ch.unibas.medizin.osce.client.a_nonroo.client.receiver;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.log.ErrorLog;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.log.ErrorMessage;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.log.ErrorSeverity;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

@SuppressWarnings("deprecation")
public abstract class OSCEReceiver<T> extends Receiver<T> {
	// SPEC START =	
		OSCEReceiverPopupViewImpl osceReceiverPopupView=null;
	// SPEC END =
		
		// Constraint Violation
		StringBuffer errorBuffor;
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private ErrorLog errorLog = ErrorLog.getInstance();

	@Override
	public abstract void onSuccess(T response);
	
	@Override
	public void onFailure(ServerFailure error) 
	{
		Log.error(error.getMessage());	
		showMessage(error.getMessage());
	}
	
//	public void showMessage(String error)
//	{
//		
//		final String errorMsg=error;
//		Log.info("Error Message" + errorMsg);	
//		
//		String[] errorMsgLst;
//		osceReceiverPopupView=new OSCEReceiverPopupViewImpl();
//		osceReceiverPopupView.showMessage(error);
//		
//		Timer t = new Timer() 
//		{		
//			@Override
//			public void run() 
//			{				
//				osceReceiverPopupView.hide();
//				osceReceiverPopupView=null;
//			}
//				
//		};
//		t.schedule(5000);
//	
//	}

//	  public void onViolation(Set<Violation> errors) 
//	  {
//		  errorBuffor=new StringBuffer();
//		  // Constraint Violation
//		  if (!errors.isEmpty()) 
//		  {		      			  
//			  Iterator<Violation> iter = errors.iterator();
//			 
//			  while (iter.hasNext()) 
//			  {				
//				  errorBuffor.append("Please specify appropriate value of " + iter.next().getPath() + "<br>");				  
//			  }
//			  showMessage(errorBuffor.toString());
//		  }
//	  }
	
	public void onViolation(Set<Violation> errors) {
		if (!errors.isEmpty()) {
			Iterator<Violation> iter = errors.iterator();
			String message = "";
			while(iter.hasNext()){
				Violation vio = iter.next();
				vio.getPath();
				message += vio.getMessage() + "\n";
			}
			Log.warn(" in StandardizedPatient -" + message);
			showMessage(message);
//			onFailure(new ServerFailure("The call failed on the server due to a ConstraintViolation"));
		}
	}
	
	public void showMessage(String error) {
		ErrorSeverity severity;
		String message = error;
		Log.info("Error Message: " + message);
		
		severity = ErrorSeverity.ERROR;
		message = error;
		
		errorLog.addMessage(severity, message);
		
//		osceReceiverPopupView = new OSCEReceiverPopupViewImpl();
//		osceReceiverPopupView.show();
//	
//		osceReceiverPopupView.lblErrMessage.setWordWrap(true);
//		osceReceiverPopupView.lblErrMessage.setText(errorMsg);
//		
//		//osceReceiverPopupView.lblTitle.setText(errorType);
//		/*osceReceiverPopupView.lblErrMessage.setWidth("500px");*/							
//						
//		RootPanel.get().add(osceReceiverPopupView);
//		osceReceiverPopupView.show();
//		
//		Timer t = new Timer() {		
//			@Override
//			public void run() {				
//				osceReceiverPopupView.hide();
//				osceReceiverPopupView=null;
//			}
//		};
		//t.schedule(2500);
	}

	private String parseMessage(String message) {
		if (message.indexOf("must match") > 0) {
			return "INVALID_FORMAT";
			//return constants.invalidFormat();
		}
		return "";
	}
}
