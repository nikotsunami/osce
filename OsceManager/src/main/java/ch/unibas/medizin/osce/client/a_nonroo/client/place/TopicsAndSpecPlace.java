package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class TopicsAndSpecPlace extends OsMaPlace{
	
	private String token;
	
	public TopicsAndSpecPlace() {
		Log.debug("TopicsAndSpecPlace");
		this.token = "TopicsAndSpecPlace";
	}
	
	public TopicsAndSpecPlace(String token)
	{
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

	public static class Tokenizer implements PlaceTokenizer<TopicsAndSpecPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("TopicsAndSpecPlace.Tokenizer");
			this.requests = requests;
		}

		public TopicsAndSpecPlace getPlace(String token) {
			Log.debug("TopicsAndSpecPlace.Tokenizer.getPlace");
			return new TopicsAndSpecPlace(token);
		}

		public String getToken(TopicsAndSpecPlace place) {
			Log.debug("LogPTopicsAndSpecPlacelace.Tokenizer.getToken");
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
