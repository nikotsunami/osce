package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ProfessionDetailsPlace extends Place {
	
	
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

	public ProfessionDetailsPlace(){
		
		this.token = "SystemStartPlace";
	}
	public ProfessionDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
		
	}

    public ProfessionDetailsPlace(EntityProxyId<?> stableId, Operation operation) {

		this.operation = operation;
		proxyId = stableId;
	}

	public Operation getOperation() {
		return operation;
	}
	
	public ProfessionDetailsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<ProfessionDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ProfessionDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public ProfessionDetailsPlace getPlace(String token) {
			Log.debug("ProfessionDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new ProfessionDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new ProfessionDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new ProfessionDetailsPlace(requests.getProxyId(bits[0]), Operation.CREATE);
			}

			return new ProfessionDetailsPlace(token);
		}

		public String getToken(ProfessionDetailsPlace place) {
			Log.debug("ProfessionDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + ProfessionDetailsPlace.Operation.DETAILS;
			}
			if (Operation.EDIT == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + ProfessionDetailsPlace.Operation.EDIT;
			}
			if (Operation.CREATE == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + ProfessionDetailsPlace.Operation.CREATE;
			}

        
			return place.getToken();
		}
	}


	

}
