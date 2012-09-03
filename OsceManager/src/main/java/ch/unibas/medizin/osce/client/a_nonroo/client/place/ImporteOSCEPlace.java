package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ImporteOSCEPlace extends OsMaPlace {

	private String token;

	public ImporteOSCEPlace(){
		Log.debug("ImporteOSCEPlace");
		this.token = "ImporteOSCEPlace";
	}
	
	public ImporteOSCEPlace(String token){
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static class Tokenizer implements PlaceTokenizer<ImporteOSCEPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ImporteOSCEPlace.Tokenizer");
			this.requests = requests;
		}

		public ImporteOSCEPlace getPlace(String token) {
			Log.debug("ImporteOSCEPlace.Tokenizer.getPlace");
			return new ImporteOSCEPlace(token);
		}

		public String getToken(ImporteOSCEPlace place) {
			Log.debug("ImporteOSCEPlace.Tokenizer.getToken");
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
