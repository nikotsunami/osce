package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class SummoningsPlace extends Place {

	private String token;

	public SummoningsPlace(){
		Log.debug("SummoningsPlace.SummoningsPlace");
		this.token = "SummoningsPlace";
	}

	public SummoningsPlace(String token){
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
	public static class Tokenizer implements PlaceTokenizer<SummoningsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("SummoningsPlace.Tokenizer");
			this.requests = requests;
		}

		public SummoningsPlace getPlace(String token) {
			Log.debug("SummoningsPlace.Tokenizer.getPlace");
			return new SummoningsPlace(token);
		}

		public String getToken(SummoningsPlace place) {
			Log.debug("SummoningsPlace.Tokenizer.getToken");
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
