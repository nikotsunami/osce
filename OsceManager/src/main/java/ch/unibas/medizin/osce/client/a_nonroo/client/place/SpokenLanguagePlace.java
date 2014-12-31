package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class SpokenLanguagePlace extends OsMaPlace {
	
	
	private String token;

	public SpokenLanguagePlace(){
		this.token = "SpokenLanguagePlace";
	}
	
	public SpokenLanguagePlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<SpokenLanguagePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("SystemStartPlace.Tokenizer");
			this.requests = requests;
		}

		public SpokenLanguagePlace getPlace(String token) {
			Log.debug("SystemStartPlace.Tokenizer.getPlace");
			return new SpokenLanguagePlace(token);
		}

		public String getToken(SpokenLanguagePlace place) {
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
