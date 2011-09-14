package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

/**
 * @author dk
 *
 */
public class ScarPlace extends Place {
	
	private String token;

	public ScarPlace(){
		Log.debug("ScarPlace");
		this.token = "ScarPlace";
	}
	
	public ScarPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<ScarPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ScarPlace.Tokenizer");
			this.requests = requests;
		}

		public ScarPlace getPlace(String token) {
			Log.debug("ScarPlace.Tokenizer.getPlace");
			return new ScarPlace(token);
		}

		public String getToken(ScarPlace place) {
			Log.debug("ScarPlace.Tokenizer.getToken");
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
