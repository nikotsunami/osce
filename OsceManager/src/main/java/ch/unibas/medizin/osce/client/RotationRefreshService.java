/**
 * 
 */
package ch.unibas.medizin.osce.client;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;

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
