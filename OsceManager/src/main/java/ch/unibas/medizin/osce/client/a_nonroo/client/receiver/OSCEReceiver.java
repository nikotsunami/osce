package ch.unibas.medizin.osce.client.a_nonroo.client.receiver;


import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

@SuppressWarnings("deprecation")
public abstract class OSCEReceiver<T> extends Receiver<T> 
{
	// SPEC START =	
		OSCEReceiverPopupViewImpl osceReceiverPopupView=null;
		//static String errorType;
	// SPEC END =
	
	@Override
	public void onFailure(ServerFailure error) 
	{
		Log.error(error.getMessage());	
		showMessage(error.getMessage());
		//final String errorMsg=error.getMessage();		
		//final HorizontalPanel hp=new HorizontalPanel();
		/*osceReceiverPopupView=new OSCEReceiverPopupViewImpl();
		osceReceiverPopupView.setTitle(error.getExceptionType());*/
		//errorType=error.getExceptionType();
	}
	
	public void showMessage(String error)
	{
		
		final String errorMsg=error;
		Log.info("Error Message" + errorMsg);	
		
		osceReceiverPopupView=new OSCEReceiverPopupViewImpl();
		osceReceiverPopupView.setAnimationEnabled(true);
		osceReceiverPopupView.center();
		osceReceiverPopupView.setGlassEnabled(true);
	
		osceReceiverPopupView.lblErrMessage.setWordWrap(true);
		osceReceiverPopupView.lblErrMessage.setText(errorMsg);
		
		//osceReceiverPopupView.lblTitle.setText(errorType);
		/*osceReceiverPopupView.lblErrMessage.setWidth("500px");*/							
						
		RootPanel.get().add(osceReceiverPopupView);
		osceReceiverPopupView.show();
		
					
				
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
		  if (!errors.isEmpty()) 
		  {
		      onFailure(new ServerFailure("The call failed on the server due to a ConstraintViolation"));
		  }
	  }

}
