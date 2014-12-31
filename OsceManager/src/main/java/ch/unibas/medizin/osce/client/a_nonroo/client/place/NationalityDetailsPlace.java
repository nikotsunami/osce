package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class NationalityDetailsPlace extends OsMaDetailsPlace {
	private String token = "NationalityDetailsPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public NationalityDetailsPlace(){
	}
	
	public NationalityDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
		
	}

    public NationalityDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		this.token = "NationalityDetailsPlace";
		this.operation = operation;
		proxyId = stableId;
	}

	public Operation getOperation() {
		return operation;
	}
	
	public NationalityDetailsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<NationalityDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("NationalityDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public NationalityDetailsPlace getPlace(String token) {
			Log.debug("NationalityDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new NationalityDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new NationalityDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new NationalityDetailsPlace(requests.getProxyId(bits[0]), Operation.CREATE);
			}

			return new NationalityDetailsPlace(token);
		}

		public String getToken(NationalityDetailsPlace place) {
			Log.debug("NationalityDetailsPlace.Tokenizer.getToken");
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
