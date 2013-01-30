package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class AnamnesisFormPlace extends OsMaPlace {
	
	
	private String token;

	public AnamnesisFormPlace(){
		this.token = "AnamnesisFormPlace";
	}
	
	public AnamnesisFormPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<AnamnesisFormPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("AnamnesisFormPlace.Tokenizer");
			this.requests = requests;
		}

		public AnamnesisFormPlace getPlace(String token) {
			Log.debug("AnamnesisFormPlace.Tokenizer.getPlace");
			return new AnamnesisFormPlace(token);
		}

		public String getToken(AnamnesisFormPlace place) {
			Log.debug("AnamnesisFormPlace.Tokenizer.getToken");
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
