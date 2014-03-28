package ch.unibas.medizin.osce.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
/**
 * 
 * @author manish
 *
 */
@RemoteServiceRelativePath("autoAssignPatientInSemster")
public interface AutoAssignPatientInSemesterService extends RemoteService {

	void autoAssignPatientInSemester(Long semesterId);
}
