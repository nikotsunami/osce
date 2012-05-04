package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class TopicsAndSpecDetailsPlace extends Place implements HasOperationOnProxy {
	private String token;
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}
	public TopicsAndSpecDetailsPlace(){
		this.token = "TopicsAndSpecDetailsPlace";
	}
	public TopicsAndSpecDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public TopicsAndSpecDetailsPlace(Operation operation) {
		this.operation = operation;
	}

	public TopicsAndSpecDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		this.operation = operation;
		proxyId = stableId;
	}
	
	public Operation getOperation() {
		return operation;
	}

	public TopicsAndSpecDetailsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<TopicsAndSpecDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("TopicsAndSpecDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public TopicsAndSpecDetailsPlace getPlace(String token) {
			Log.debug("TopicsAndSpecDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new TopicsAndSpecDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new TopicsAndSpecDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new TopicsAndSpecDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.CREATE);
			}

			return new TopicsAndSpecDetailsPlace(token);
		}

		public String getToken(TopicsAndSpecDetailsPlace place) {
			Log.debug("TopicsAndSpecDetailsPlace.Tokenizer.getToken");
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
