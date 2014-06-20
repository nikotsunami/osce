package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ManualOsceDetailsPlace extends OsMaDetailsPlace {
	
	private String token = "ManualOsceDetailsPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	public ManualOsceDetailsPlace(){
	}

	public ManualOsceDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public ManualOsceDetailsPlace(Operation operation) {
		this.operation = operation;
	}

	public ManualOsceDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		this.operation = operation;
		proxyId = stableId;
	}

	@Override
	public Operation getOperation() {
		return operation;
	}

	@Override
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public ManualOsceDetailsPlace(String token){
		this.token = token;
	}
	@Override
	public String getToken() {
		return token;
	}

	@Override
	public void setToken(String token) {
		this.token=token;
	}
	
	public static class Tokenizer implements PlaceTokenizer<ManualOsceDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ManualOsceDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		@Override
		public ManualOsceDetailsPlace getPlace(String token) {
			Log.debug("ManualOsceDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new ManualOsceDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			return new ManualOsceDetailsPlace(token);

		}

		@Override
		public String getToken(ManualOsceDetailsPlace place) {
			Log.debug("CircuitDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.DETAILS.toString();
			}

			return place.getToken();
		}
	}
}
