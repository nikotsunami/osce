package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

/**
 * @author dk
 *
 */
public class OscePlace extends Place {
	
	private String token;

	public OscePlace(){
		Log.debug("OscePlace");
		this.token = "OscePlace";
	}
	
	public OscePlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<OscePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("OscePlace.Tokenizer");
			this.requests = requests;
		}

		public OscePlace getPlace(String token) {
			Log.debug("OscePlace.Tokenizer.getPlace");
			return new OscePlace(token);
		}

		public String getToken(OscePlace place) {
			Log.debug("OscePlace.Tokenizer.getToken");
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
