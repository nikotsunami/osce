package ch.unibas.medizin.osce.server;

import ch.unibas.medizin.osce.client.a_nonroo.client.dmzsync.DMZSyncService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DMZSyncServiceImpl extends RemoteServiceServlet implements DMZSyncService {

	
	@Override
	public void pushToDMZ(Integer standardizedPatientId){
		System.err.println(" pushToDMZ(Integer standardizedPatientId) " + standardizedPatientId);
	}

	@Override
	public void pullFromDMZ(Integer standardizedPatientId){
		System.err.println(" pullFromDMZ(Integer standardizedPatientId) " + standardizedPatientId);		
	}


}
