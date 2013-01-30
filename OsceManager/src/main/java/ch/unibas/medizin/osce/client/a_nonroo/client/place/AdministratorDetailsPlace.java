package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class AdministratorDetailsPlace extends OsMaDetailsPlace {
	private String token;
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public AdministratorDetailsPlace(){
		this.token = "AdministratorDetailsPlace";
	}
	public AdministratorDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}
	public AdministratorDetailsPlace(Operation operation) {
		this.operation = operation;
	}

    public AdministratorDetailsPlace(EntityProxyId<?> stableId, Operation operation) {

		this.operation = operation;
		proxyId = stableId;
	}

	public Operation getOperation() {
		return operation;
	}
	
	public AdministratorDetailsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<AdministratorDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("AdministratorDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public AdministratorDetailsPlace getPlace(String token) {
			Log.debug("AdministratorDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new AdministratorDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new AdministratorDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new AdministratorDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.CREATE);
			}

			return new AdministratorDetailsPlace(token);
		}

		public String getToken(AdministratorDetailsPlace place) {
			Log.debug("AdministratorDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.DETAILS;
			}
			if (Operation.EDIT == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.EDIT;
			}
			if (Operation.CREATE == place.getOperation()) {
				return /*place.getProxyId() + */SEPARATOR + Operation.CREATE.toString();
			}

        
			return place.getToken();
		}
	}
}
