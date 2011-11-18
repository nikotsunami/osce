package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class AnamnesisCheckPlace extends Place {
	
	
	private String token;

	public AnamnesisCheckPlace(){
		Log.debug("AnamnesisCheckPlace.AnamnesisCheckPlace");
		this.token = "SystemStartPlace";
	}
	
	public AnamnesisCheckPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<AnamnesisCheckPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("AnamnesisCheckPlace.Tokenizer");
			this.requests = requests;
		}

		public AnamnesisCheckPlace getPlace(String token) {
			Log.debug("AnamnesisCheckPlace.Tokenizer.getPlace");
			return new AnamnesisCheckPlace(token);
		}

		public String getToken(AnamnesisCheckPlace place) {
			Log.debug("AnamnesisCheckPlace.Tokenizer.getToken");
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
