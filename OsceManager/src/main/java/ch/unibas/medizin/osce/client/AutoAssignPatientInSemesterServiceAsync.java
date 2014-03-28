package ch.unibas.medizin.osce.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
/**
 * 
 * @author manish
 *
 */
public interface AutoAssignPatientInSemesterServiceAsync {

	void autoAssignPatientInSemester(Long semesterId,AsyncCallback<Void> callback);

}
