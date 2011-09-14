package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class DoctorDetailsPlace extends Place {
	
	
	private String token;
	
	public enum Operation {
		DETAILS, EDIT, CREATE
	}
	private static final String SEPARATOR = "!";


	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public DoctorDetailsPlace(){
		
		this.token = "SystemStartPlace";
	}
	public DoctorDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
		
	}

    public DoctorDetailsPlace(EntityProxyId<?> stableId, Operation operation) {

		this.operation = operation;
		proxyId = stableId;
	}

	public Operation getOperation() {
		return operation;
	}
	
	public DoctorDetailsPlace(String token){
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Tokenizer.
	 */

	public static class Tokenizer implements PlaceTokenizer<DoctorDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("DoctorDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public DoctorDetailsPlace getPlace(String token) {
			Log.debug("DoctorDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new DoctorDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new DoctorDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new DoctorDetailsPlace(requests.getProxyId(bits[0]), Operation.CREATE);
			}

			return new DoctorDetailsPlace(token);
		}

		public String getToken(DoctorDetailsPlace place) {
			Log.debug("DoctorDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + DoctorDetailsPlace.Operation.DETAILS;
			}
			if (Operation.EDIT == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + DoctorDetailsPlace.Operation.EDIT;
			}
			if (Operation.CREATE == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + DoctorDetailsPlace.Operation.CREATE;
			}

        
			return place.getToken();
		}
	}


	

}
