package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * @author dk
 *
 */
public class RolePlace extends OsMaPlace {
	
	private String token;

	public RolePlace(){
		Log.debug("RolePlace");
		this.token = "RolePlace";
	}
	
	public RolePlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<RolePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("RolePlace.Tokenizer");
			this.requests = requests;
		}

		public RolePlace getPlace(String token) {
			Log.debug("RolePlace.Tokenizer.getPlace");
			return new RolePlace(token);
		}

		public String getToken(RolePlace place) {
			Log.debug("RolePlace.Tokenizer.getToken");
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
