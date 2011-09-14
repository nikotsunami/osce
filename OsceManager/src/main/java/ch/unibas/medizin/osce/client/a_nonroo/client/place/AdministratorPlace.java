package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class AdministratorPlace extends Place {
	
	
	private String token;

	public AdministratorPlace(){
		Log.debug("AdministratorPlace.AdministratorPlace");
		this.token = "AdministratorPlace";
	}
	
	public AdministratorPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<AdministratorPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("AdministratorPlace.Tokenizer");
			this.requests = requests;
		}

		public AdministratorPlace getPlace(String token) {
			Log.debug("AdministratorPlace.Tokenizer.getPlace");
			return new AdministratorPlace(token);
		}

		public String getToken(AdministratorPlace place) {
			Log.debug("AdministratorPlace.Tokenizer.getToken");
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
		//bei Löschaktion neu laden
		if(token!=null){
			if(token.contains("AdministratorPlace!DELETED")||token.contains("AdministratorPlace!CANCEL")){
				return false;
			}
		}

		

		return true;
	}
}
