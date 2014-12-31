package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class StudentManagementDetailsPlace extends OsMaDetailsPlace {
	
	private String token = "StudentManagementDetailsPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	public StudentManagementDetailsPlace(){
	}

	public StudentManagementDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}

	public StudentManagementDetailsPlace(Operation operation) {
		this.operation = operation;
	}

	public StudentManagementDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		Log.info("Call StudentManagementDetailsPlace");
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

	public StudentManagementDetailsPlace(String token){
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
	public static class Tokenizer implements PlaceTokenizer<StudentManagementDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("StudentManagementDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		@Override
		public StudentManagementDetailsPlace getPlace(String token) {
			Log.debug("StudentManagementDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new StudentManagementDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
/*			if (Operation.EDIT == operation) {
				return new CircuitDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new CircuitDetailsPlace(requests.getProxyId(bits[0]), Operation.CREATE);
			}
*/
			return new StudentManagementDetailsPlace(token);

		}

		@Override
		public String getToken(StudentManagementDetailsPlace place) {
			Log.debug("StudentManagementDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.DETAILS;
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
