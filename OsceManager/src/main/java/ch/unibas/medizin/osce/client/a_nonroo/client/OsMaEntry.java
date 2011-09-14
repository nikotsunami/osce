package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.client.a_nonroo.client.ioc.OsMaInjector;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class OsMaEntry implements EntryPoint {

	
	  /**
	   * This field gets compiled out when <code>log_level=OFF</code>, or any <code>log_level</code>
	   * higher than <code>DEBUG</code>.
	   */
	  private long startTimeMillis;
	  
	  final private OsMaInjector injectorWrapper = GWT.create(OsMaInjector.class);

	  
	@Override
	public void onModuleLoad() {
		
		
		 /*
	     * Install an UncaughtExceptionHandler which will produce <code>FATAL</code> log messages
	     */
	    Log.setUncaughtExceptionHandler();

	    // use deferred command to catch initialization exceptions in onModuleLoad2
	    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	      @Override
	      public void execute() {
	        onModuleLoad2();
	      }
	    });


	}
	  private void onModuleLoad2() {
		    /*
		     * Use a <code>if (Log.isDebugEnabled()) {...}</code> guard to ensure that
		     * <code>System.currentTimeMillis()</code> is compiled out when <code>log_level=OFF</code>, or
		     * any <code>log_level</code> higher than <code>DEBUG</code>.
		     */
		    if (Log.isDebugEnabled()) {
		      startTimeMillis = System.currentTimeMillis();
		    }

		    /*
		     * No guards necessary. Code will be compiled out when <code>log_level=OFF</code>
		     */
		    Log.debug("This is a 'DEBUG' test message");
		    Log.info("This is a 'INFO' test message");
		    Log.warn("This is a 'WARN' test message");


		    /*
		     * Again, we need a guard here, otherwise <code>log_level=OFF</code> would still produce the
		     * following useless JavaScript: <pre> var durationSeconds, endTimeMillis; endTimeMillis =
		     * currentTimeMillis_0(); durationSeconds = (endTimeMillis - this$static.startTimeMillis) /
		     * 1000.0; </pre>
		     */
		    if (Log.isDebugEnabled()) {
		      long endTimeMillis = System.currentTimeMillis();
		      float durationSeconds = (endTimeMillis - startTimeMillis) / 1000F;
		      Log.debug("Duration: " + durationSeconds + " seconds");
		    }
		    
		    
		    
		    injectorWrapper.getOsceApp().run();
		  }
		

}
