package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class OsceDetailsPlace extends OsMaDetailsPlace {
	private String token = "OsceDetailsPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;

	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public OsceDetailsPlace(){
	}
	
	public OsceDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public OsceDetailsPlace(Operation operation) {
		this.operation = operation;
	}

	public OsceDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		this.token = "OsceDetailsPlace";
		this.operation = operation;
		proxyId = stableId;
	}

	public Operation getOperation() {
		return operation;
	}

	public OsceDetailsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<OsceDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("OsceDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public OsceDetailsPlace getPlace(String token) {
			Log.debug("OsceDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new OsceDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new OsceDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new OsceDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.CREATE);
			}

			return new OsceDetailsPlace(token);
		}

		public String getToken(OsceDetailsPlace place) {
			Log.debug("OsceDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.DETAILS;
			}
			if (Operation.EDIT == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.EDIT;
			}
			if (Operation.CREATE == place.getOperation()) {
				return /*place.getProxyId() +*/ SEPARATOR + Operation.CREATE.toString();
			}

			return place.getToken();
		}
	}
}
