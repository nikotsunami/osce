/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author rahul
 *
 */
@RemoteServiceRelativePath("rotationrefresh")
public interface RotationRefreshService extends RemoteService {
	Map<String, String> schedulePostpone(Long osceDayId);	
	Map<String, String> scheduleEarlier(Long osceDayId);
}
