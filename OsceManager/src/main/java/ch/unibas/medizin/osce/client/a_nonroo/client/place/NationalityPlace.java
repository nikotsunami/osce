package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class NationalityPlace extends Place {
	
	
	private String token;

	public NationalityPlace(){
		Log.debug("NationalityPlace");
		this.token = "NationalityPlace";
	}
	
	public NationalityPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<NationalityPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("NationalityPlace.Tokenizer");
			this.requests = requests;
		}

		public NationalityPlace getPlace(String token) {
			Log.debug("NationalityPlace.Tokenizer.getPlace");
			return new NationalityPlace(token);
		}

		public String getToken(NationalityPlace place) {
			Log.debug("NationalityPlace.Tokenizer.getToken");
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
