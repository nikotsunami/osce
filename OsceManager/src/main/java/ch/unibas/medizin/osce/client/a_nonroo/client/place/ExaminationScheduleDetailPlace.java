package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ExaminationScheduleDetailPlace extends OsMaDetailsPlace{
	
	private String token = "ExaminationScheduleDetailPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	
	public ExaminationScheduleDetailPlace()
	{
		
	}
	
	public ExaminationScheduleDetailPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);

	}
	
	public ExaminationScheduleDetailPlace(Operation operation) {
		this.operation = operation;
	}

	public ExaminationScheduleDetailPlace(EntityProxyId<?> stableId, Operation operation) {
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

	public ExaminationScheduleDetailPlace(String token){
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
	public static class Tokenizer implements PlaceTokenizer<ExaminationScheduleDetailPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ExaminationScheduleDetailPlace.Tokenizer");
			this.requests = requests;
		}

		@Override
		public ExaminationScheduleDetailPlace getPlace(String token) {
			Log.debug("ExaminationScheduleDetailPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			if (Operation.DETAILS == operation) {
				return new ExaminationScheduleDetailPlace(/*requests.getProxyId(bits[0]), */Operation.DETAILS);
			}
/*			if (Operation.EDIT == operation) {
				return new CircuitDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new CircuitDetailsPlace(requests.getProxyId(bits[0]), Operation.CREATE);
			}
*/
			return new ExaminationScheduleDetailPlace(token);

		}

		@Override
		public String getToken(ExaminationScheduleDetailPlace place) {
			Log.debug("ExaminationScheduleDetailPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return /*place.getProxyId() + */SEPARATOR + Operation.DETAILS.toString();
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
