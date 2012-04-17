package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class AnamnesisFormDetailsPlace extends Place  implements HasOperationOnProxy {
	private String token;
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public AnamnesisFormDetailsPlace(){
		
		this.token = "SystemStartPlace";
	}
	public AnamnesisFormDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
		
	}

    public AnamnesisFormDetailsPlace(EntityProxyId<?> stableId, Operation operation) {

		this.operation = operation;
		proxyId = stableId;
	}

	public Operation getOperation() {
		return operation;
	}
	
	public AnamnesisFormDetailsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<AnamnesisFormDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("AnamnesisFormDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public AnamnesisFormDetailsPlace getPlace(String token) {
			Log.debug("AnamnesisFormDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new AnamnesisFormDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new AnamnesisFormDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new AnamnesisFormDetailsPlace(requests.getProxyId(bits[0]), Operation.CREATE);
			}

			return new AnamnesisFormDetailsPlace(token);
		}

		public String getToken(AnamnesisFormDetailsPlace place) {
			Log.debug("AnamnesisFormDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.DETAILS;
			}
			if (Operation.EDIT == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.EDIT;
			}
			if (Operation.CREATE == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.CREATE;
			}

        
			return place.getToken();
		}
	}


	

}
