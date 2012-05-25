package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class RoleScriptTemplateDetailsPlace extends OsMaDetailsPlace{
	private String token;
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}
	public RoleScriptTemplateDetailsPlace(){
		this.token = "SystemStartPlace";
	}
	public RoleScriptTemplateDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public RoleScriptTemplateDetailsPlace(Operation operation) {
		this.operation = operation;
	}

	public RoleScriptTemplateDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		this.operation = operation;
		proxyId = stableId;
	}
	
	public Operation getOperation() {
		return operation;
	}

	public RoleScriptTemplateDetailsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<RoleScriptTemplateDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("RoleScriptTemplateDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public RoleScriptTemplateDetailsPlace getPlace(String token) {
			Log.debug("RoleScriptTemplateDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new RoleScriptTemplateDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new RoleScriptTemplateDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new RoleScriptTemplateDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.CREATE);
			}

			return new RoleScriptTemplateDetailsPlace(token);
		}

		public String getToken(RoleScriptTemplateDetailsPlace place) {
			Log.debug("RoleScriptTemplateDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return /*place.getProxyId() + */SEPARATOR + Operation.DETAILS.toString();
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
