package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class AnamnesisCheckDetailsPlace extends Place {
	
	
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

	public AnamnesisCheckDetailsPlace(){
		
		this.token = "SystemStartPlace";
	}
	public AnamnesisCheckDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}
	
	public AnamnesisCheckDetailsPlace(Operation operation) {
		this.operation = operation;
	}

    public AnamnesisCheckDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		this.operation = operation;
		proxyId = stableId;
	}

	public Operation getOperation() {
		return operation;
	}
	
	public AnamnesisCheckDetailsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<AnamnesisCheckDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("AnamnesisCheckDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public AnamnesisCheckDetailsPlace getPlace(String token) {
			Log.debug("AnamnesisCheckDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new AnamnesisCheckDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new AnamnesisCheckDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new AnamnesisCheckDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.CREATE);
			}

			return new AnamnesisCheckDetailsPlace(token);
		}

		public String getToken(AnamnesisCheckDetailsPlace place) {
			Log.debug("AnamnesisFormDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + AnamnesisCheckDetailsPlace.Operation.DETAILS;
			}
			if (Operation.EDIT == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + AnamnesisCheckDetailsPlace.Operation.EDIT;
			}
			if (Operation.CREATE == place.getOperation()) {
				return /*place.getProxyId() + SEPARATOR + */AnamnesisCheckDetailsPlace.Operation.CREATE.toString();
			}

        
			return place.getToken();
		}
	}


	

}
