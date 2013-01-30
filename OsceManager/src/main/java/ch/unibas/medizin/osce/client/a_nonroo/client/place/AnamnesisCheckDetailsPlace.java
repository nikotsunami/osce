package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class AnamnesisCheckDetailsPlace extends OsMaDetailsPlace {
	private String token = "AnamnesisCheckDetailsPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	private static String titleId = null;
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public AnamnesisCheckDetailsPlace(){
	}
	
	public AnamnesisCheckDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}
	
	public AnamnesisCheckDetailsPlace(Operation operation) {
		this.operation = operation;
	}

    public AnamnesisCheckDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		this.operation = operation;
		proxyId = stableId;
	}
    
    public AnamnesisCheckDetailsPlace(Operation operation, String titleId) {
		this.operation = operation;
		this.titleId = titleId;
	}

	public String getTitleId() {
		return titleId;
	}
	
	public Operation getOperation() {
		return operation;
	}
	
	public AnamnesisCheckDetailsPlace(String token){
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return "AnamnesisCheckDetailsPlace";
	}

	/**
	 * Tokenizer.
	 */

	public static class Tokenizer implements PlaceTokenizer<AnamnesisCheckDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("AnamnesisCheckDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public AnamnesisCheckDetailsPlace getPlace(String token) {
			Log.debug("AnamnesisCheckDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			
			GWT.log(">>>>>>>>>>>>>>>>>>>>>>>>in AnamnesisCheckDetailsPlace titleId = "+titleId);

			if (Operation.DETAILS == operation) {
				return new AnamnesisCheckDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new AnamnesisCheckDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				String titleId = String.valueOf(bits[2]);
				return new AnamnesisCheckDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.CREATE, titleId);
			}
			if (Operation.NEW == operation) {
				return new AnamnesisCheckDetailsPlace(requests.getProxyId(bits[0]), Operation.NEW);
			}

			return new AnamnesisCheckDetailsPlace(token);
		}

		public String getToken(AnamnesisCheckDetailsPlace place) {
			Log.debug("AnamnesisCheckDetailsPlace.Tokenizer.getToken");
			if (Operation.DETAILS == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.DETAILS;
			}
			if (Operation.EDIT == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.EDIT;
			}
			if (Operation.CREATE == place.getOperation()) {
				return /*place.getProxyId() + */ SEPARATOR + Operation.CREATE.toString() + SEPARATOR + titleId;
			}
			if (Operation.NEW == place.getOperation()) {
				return place.getProxyId() + SEPARATOR + Operation.NEW;
			}
        
			return place.getToken();
		}
	}


	

}
