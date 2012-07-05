package ch.unibas.medizin.osce.client.a_nonroo.client.receiver;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.google.gwt.editor.client.EditorError;
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
public abstract class OSCEReceiver<T> extends Receiver<T> 
{
	// SPEC START =	
		OSCEReceiverPopupViewImpl osceReceiverPopupView=null;
	// SPEC END =
		
		// Constraint Violation
		StringBuffer errorBuffor;
	
	@Override
	public void onFailure(ServerFailure error) 
	{
		Log.error(error.getMessage());	
		showMessage(error.getMessage());
	}
	
	public void showMessage(String error)
	{
		
		final String errorMsg=error;
		Log.info("Error Message" + errorMsg);	
		
		String[] errorMsgLst;
		osceReceiverPopupView=new OSCEReceiverPopupViewImpl();
		osceReceiverPopupView.showMessage(error);
		
		Timer t = new Timer() 
		{		
			@Override
			public void run() 
			{				
				osceReceiverPopupView.hide();
				osceReceiverPopupView=null;
			}
				
		};
		t.schedule(5000);
	
	}

	@Override
	public abstract void onSuccess(T response);
	
	  public void onViolation(Set<Violation> errors) 
	  {
		  errorBuffor=new StringBuffer();
		  // Constraint Violation
		  if (!errors.isEmpty()) 
		  {		      			  
			  Iterator<Violation> iter = errors.iterator();
			 
			  while (iter.hasNext()) 
			  {				
				  errorBuffor.append("Please specify appropriate value of " + iter.next().getPath() + "<br>");				  
			  }
			  showMessage(errorBuffor.toString());
		  }
	  }
}
