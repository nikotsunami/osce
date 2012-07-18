package ch.unibas.medizin.osce.client.a_nonroo.client.receiver;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.util.OSCEReceiverPopupViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public abstract class OSCEReceiver<T> extends Receiver<T> 
{
	// SPEC START =	
	OSCEReceiverPopupViewImpl osceReceiverPopupView=null;
	Map<String, Widget> localViewMap;
// SPEC END =
	
	// Constraint Violation
	StringBuffer errorBuffor;
	StringBuffer violationMessageBuffor;

	public OSCEReceiver()
	{
		Log.info("Call OSCEReceiver Constructor()");
	}
	
	public OSCEReceiver(Map viewMap)
	{
		Log.info("Call OSCEReceiver Constructor(Map)");
		Log.info("Size of map is: " + viewMap.size());
		localViewMap=new HashMap<String, Widget>();
		localViewMap=viewMap;
		
		if(localViewMap==null)
		  {
			  Log.info("Map Null from constructor");
		  }
		  else
		  {
			  Log.info("Map Not Null  from constructor");
			  
			  Iterator<String> tempIterator=localViewMap.keySet().iterator();
			  while(tempIterator.hasNext())
			  {
				  Log.info("Remove... Highlight");	
				  localViewMap.get(tempIterator.next()).removeStyleName("higlight_onViolation");
			  }
		  }
	}
	
/*public OSCEReceiver(Widget view) 
{
	
	Log.info("Call OSCE RECEIVER..." + view.getAbsoluteTop());
}*/
	
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
	t.schedule(10000);

}

@Override
public abstract void onSuccess(T response);

  public void onViolation(Set<Violation> errors) 
  {
	  Log.info("Call onViolation");		  
	  errorBuffor=new StringBuffer();
	  violationMessageBuffor=new StringBuffer();
	  // Constraint Violation
	  if (!errors.isEmpty()) 
	  {		      			  
		  Iterator<Violation> iter = errors.iterator();
		  Log.info("Test Method: "  + errors.contains(errors.iterator().next().getPath()));
		  while (iter.hasNext()) 
		  {				
			  
			  	String str=iter.next().getPath();
			  	errorBuffor.append("Please specify appropriate value of " + str + "<br>");
			  	violationMessageBuffor.append(str+",");				  				 
		  }
		  showMessage(errorBuffor.toString());

		  // Violation Changes Highlight
		  
		  if(localViewMap==null)
		  {
			  Log.info("Map Null");
		  }
		  else
		  {
			  Log.info("Map Not Null");
			  
			 /* Iterator<String> tempIterator=localViewMap.keySet().iterator();
			  while(tempIterator.hasNext())
			  {
				  Log.info("Remove... Highlight");	
				  localViewMap.get(tempIterator.next()).removeStyleName("higlight_onViolation");
			  }*/
			  
			  
			  for (String retval: violationMessageBuffor.toString().split(","))
				{
			       
				   Log.info("Local Map Size is: " + localViewMap.size()); 
			        Log.info("*"+retval+"*");
				  	Log.info("Violated Value: " + retval);			        
				  	Log.info(" localViewMap.get(retval) Widget" +  localViewMap.get(retval.trim()));			         
			         
			         Iterator<String> iterator=localViewMap.keySet().iterator();
			         
			         		         
			        while(iterator.hasNext())
			        {
			        	  
			        	if(retval.equals(iterator.next()))
				         {			        	
				        	 Log.info("Violation... Highlight");	
				        	 localViewMap.get(retval).addStyleName("higlight_onViolation");			        	 		        	 	 	        
				         }
				         else
				         {
				        	 Log.info("Successful No Violation...");	       
				         }
			        }
				}
		  }
			// E Violation Changes Highlight
		  
		  
		  
		  
	  }
  }
		  	
}
