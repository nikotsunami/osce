package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class AnamnesisCheckTitleDetailsPlace extends OsMaDetailsPlace {
	
	private String token = "AnamnesisCheckTitleDetailsPlace";
	private static final String SEPARATOR = "!";
	private EntityProxyId<?> proxyId;
	private Operation operation = null;
	private static String titleId = null;
	
	public EntityProxyId<?> getProxyId() {
		return proxyId;
	}

	public AnamnesisCheckTitleDetailsPlace(){
	}
	
	public AnamnesisCheckTitleDetailsPlace(EntityProxyId<?> record) {
		this(record, Operation.DETAILS);
	}
	
	public AnamnesisCheckTitleDetailsPlace(Operation operation) {
		this.operation = operation;
	}
	public AnamnesisCheckTitleDetailsPlace(Operation operation,String titleId) {
		this.operation = operation;
		this.titleId=titleId;
	}
	
	

    public AnamnesisCheckTitleDetailsPlace(EntityProxyId<?> stableId, Operation operation) {
		this.operation = operation;
		proxyId = stableId;
	}
    

    public String getTitleId() {
		return titleId;
	}
    
	public Operation getOperation() {
		return operation;
	}
	
	public AnamnesisCheckTitleDetailsPlace(String token){
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
		return "AnamnesisCheckTitleDetailsPlace";
	}

	/**
	 * Tokenizer.
	 */

	public static class Tokenizer implements PlaceTokenizer<AnamnesisCheckTitleDetailsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			System.err.println("AnamnesisCheckTitleDetailsPlace.Tokenizer");
			this.requests = requests;
		}

		public AnamnesisCheckTitleDetailsPlace getPlace(String token) {
			System.err.println("AnamnesisCheckTitleDetailsPlace.Tokenizer.getPlace");
			String bits[] = token.split(SEPARATOR);
			Operation operation = Operation.valueOf(bits[1]);
			

			if (Operation.DETAILS == operation) {
				return new AnamnesisCheckTitleDetailsPlace(requests.getProxyId(bits[0]), Operation.DETAILS);
			}
			if (Operation.EDIT == operation) {
				return new AnamnesisCheckTitleDetailsPlace(requests.getProxyId(bits[0]), Operation.EDIT);
			}
			if (Operation.CREATE == operation) {
				return new AnamnesisCheckTitleDetailsPlace(/*requests.getProxyId(bits[0]), */Operation.CREATE);
			}
			if (Operation.NEW == operation) {
				return new AnamnesisCheckTitleDetailsPlace(requests.getProxyId(bits[0]), Operation.NEW);
			}

			return new AnamnesisCheckTitleDetailsPlace(token);
		}

		public String getToken(AnamnesisCheckTitleDetailsPlace place) {
			System.err.println("AnamnesisCheckTitleDetailsPlace.Tokenizer.getToken");
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
