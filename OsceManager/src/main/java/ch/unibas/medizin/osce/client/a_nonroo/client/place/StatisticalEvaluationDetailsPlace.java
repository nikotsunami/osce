package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class StatisticalEvaluationDetailsPlace extends OsMaDetailsPlace {
	
	private String token = "IndividualSchedulesDetailsPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	public StatisticalEvaluationDetailsPlace(){
	}

	public StatisticalEvaluationDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public StatisticalEvaluationDetailsPlace(Operation operation) {
		this.operation = operation;
	}

	public StatisticalEvaluationDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		Log.info("Call StatisticalEvaluationDetailsPlace");
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

	public StatisticalEvaluationDetailsPlace(String token){
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
	public static class Tokenizer implements PlaceTokenizer<StatisticalEvaluationDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("StatisticalEvaluationDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		@Override
		public StatisticalEvaluationDetailsPlace getPlace(String token) {
			Log.debug("StatisticalEvaluationDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new StatisticalEvaluationDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
/*			if (Operation.EDIT == operation) {
				return new CircuitDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new CircuitDetailsPlace(requests.getProxyId(bits[0]), Operation.CREATE);
			}
*/
			return new StatisticalEvaluationDetailsPlace(token);

		}

		@Override
		public String getToken(StatisticalEvaluationDetailsPlace place) {
			Log.debug("StatisticalEvaluationDetailsPlace.Tokenizer.getToken");
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
