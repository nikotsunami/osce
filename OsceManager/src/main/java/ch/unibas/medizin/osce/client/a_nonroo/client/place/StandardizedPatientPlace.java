package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class StandardizedPatientPlace extends Place {
	
	
	private String token;

	public StandardizedPatientPlace(){
		Log.debug("SystemStartPlace.SystemStartPlace");
		this.token = "SystemStartPlace";
	}
	
	public StandardizedPatientPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<StandardizedPatientPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("SystemStartPlace.Tokenizer");
			this.requests = requests;
		}

		public StandardizedPatientPlace getPlace(String token) {
			Log.debug("SystemStartPlace.Tokenizer.getPlace");
			return new StandardizedPatientPlace(token);
		}

		public String getToken(StandardizedPatientPlace place) {
			Log.debug("SystemStartPlace.Tokenizer.getToken");
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
