/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author rahul
 *
 */
public interface RotationRefreshServiceAsync  {

	void schedulePostpone(Long osceDayId,AsyncCallback<Map<String, String>> asyncCallback);	
	void scheduleEarlier(Long osceDayId,AsyncCallback<Map<String, String>> asyncCallback); 
}
