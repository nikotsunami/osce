package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ProfessionPlace extends OsMaPlace {
	
	
	private String token;

	public ProfessionPlace(){
		this.token = "ProfessionPlace";
	}
	
	public ProfessionPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<ProfessionPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ProfessionPlace.Tokenizer");
			this.requests = requests;
		}

		public ProfessionPlace getPlace(String token) {
			Log.debug("ProfessionPlace.Tokenizer.getPlace");
			return new ProfessionPlace(token);
		}

		public String getToken(ProfessionPlace place) {
			Log.debug("ProfessionPlace.Tokenizer.getToken");
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
