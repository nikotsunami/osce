package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * @author dk
 *
 */
public class LogPlace extends OsMaPlace {
	
	private String token;

	public LogPlace(){
		Log.debug("LogPlace");
		this.token = "LogPlace";
	}
	
	public LogPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<LogPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("LogPlace.Tokenizer");
			this.requests = requests;
		}

		public LogPlace getPlace(String token) {
			Log.debug("LogPlace.Tokenizer.getPlace");
			return new LogPlace(token);
		}

		public String getToken(LogPlace place) {
			Log.debug("LogPlace.Tokenizer.getToken");
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
