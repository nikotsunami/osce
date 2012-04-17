package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ClinicPlace extends Place {
	
	private String token;

	public ClinicPlace(){
		Log.debug("ClinicPlace.ClinicPlace");
		this.token = "ClinicPlace";
	}
	
	public ClinicPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<ClinicPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ClinicPlace.Tokenizer");
			this.requests = requests;
		}

		public ClinicPlace getPlace(String token) {
			Log.debug("ClinicPlace.Tokenizer.getPlace");
			return new ClinicPlace(token);
		}

		public String getToken(ClinicPlace place) {
			Log.debug("ClinicPlace.Tokenizer.getToken");
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
		// reload list after deleting an item
		if(token != null){
			if(/*token.contains("ClinicPlace!DELETED") ||*/ token.contains("ClinicPlace!CANCEL")){
				return false;
			}
		}

		return true;
	}
}
