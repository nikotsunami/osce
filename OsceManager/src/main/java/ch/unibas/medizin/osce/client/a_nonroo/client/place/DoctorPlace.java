package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class DoctorPlace extends Place {
	
	
	private String token;

	public DoctorPlace(){
		Log.debug("DoctorPlace");
		this.token = "DoctorPlace";
	}
	
	public DoctorPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<DoctorPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("DoctorPlace.Tokenizer");
			this.requests = requests;
		}

		public DoctorPlace getPlace(String token) {
			Log.debug("DoctorPlace.Tokenizer.getPlace");
			return new DoctorPlace(token);
		}

		public String getToken(DoctorPlace place) {
			Log.debug("DoctorPlace.Tokenizer.getToken");
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
			if(token.contains("DoctorPlace!DELETED") || token.contains("DoctorPlace!CANCEL")){
				return false;
			}
		}

		return true;
	}
}
