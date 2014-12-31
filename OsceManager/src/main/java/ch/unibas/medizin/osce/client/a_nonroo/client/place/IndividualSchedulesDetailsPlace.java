package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class IndividualSchedulesDetailsPlace extends OsMaDetailsPlace {
	
	private String token = "IndividualSchedulesDetailsPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	public IndividualSchedulesDetailsPlace(){
	}

	public IndividualSchedulesDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public IndividualSchedulesDetailsPlace(Operation operation) {
		this.operation = operation;
	}

	public IndividualSchedulesDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		Log.info("Call IndividualSchedulesDetailsPlace");
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

	public IndividualSchedulesDetailsPlace(String token){
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
	
	/**
	 * Tokenizer.
	 */
	public static class Tokenizer implements PlaceTokenizer<IndividualSchedulesDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("IndividualSchedulesDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		@Override
		public IndividualSchedulesDetailsPlace getPlace(String token) {
			Log.debug("IndividualSchedulesDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new IndividualSchedulesDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
/*			if (Operation.EDIT == operation) {
				return new CircuitDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new CircuitDetailsPlace(requests.getProxyId(bits[0]), Operation.CREATE);
			}
*/
			return new IndividualSchedulesDetailsPlace(token);

		}

		@Override
		public String getToken(IndividualSchedulesDetailsPlace place) {
			Log.debug("CircuitDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.DETAILS.toString();
			}
/*			if (Operation.EDIT == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.EDIT;
			}
			if (Operation.CREATE == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.CREATE.toString();
			}
*/
			return place.getToken();
		}
	}
}
