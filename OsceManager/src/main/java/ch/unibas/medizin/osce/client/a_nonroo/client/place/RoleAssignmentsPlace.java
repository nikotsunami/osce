package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * @author dk
 *
 */
public class RoleAssignmentsPlace extends OsMaPlace {
	
	private String token;

	public RoleAssignmentsPlace(){
		Log.debug("RoleAssignmentsPlace");
		this.token = "RoleAssignmentsPlace";
	}
	
	public RoleAssignmentsPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<RoleAssignmentsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("RoleAssignmentsPlace.Tokenizer");
			this.requests = requests;
		}

		public RoleAssignmentsPlace getPlace(String token) {
			Log.debug("RoleAssignmentsPlace.Tokenizer.getPlace");
			return new RoleAssignmentsPlace(token);
		}

		public String getToken(RoleAssignmentsPlace place) {
			Log.debug("RoleAssignmentsPlace.Tokenizer.getToken");
			return place.getToken();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		return true;
	}
}
