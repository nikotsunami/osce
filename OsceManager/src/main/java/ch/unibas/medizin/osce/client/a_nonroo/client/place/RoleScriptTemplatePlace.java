package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class RoleScriptTemplatePlace extends OsMaPlace{
	
	private String token;
	
	public RoleScriptTemplatePlace() {
		Log.debug("RoleScriptTemplatePlace");
		this.token = "RoleScriptTemplatePlace";
	}
	
	public RoleScriptTemplatePlace(String token)
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

	public static class Tokenizer implements PlaceTokenizer<RoleScriptTemplatePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("RoleScriptTemplatePlace.Tokenizer");
			this.requests = requests;
		}

		public RoleScriptTemplatePlace getPlace(String token) {
			Log.debug("TopicsAndSpecPlace.Tokenizer.getPlace");
			return new RoleScriptTemplatePlace(token);
		}

		public String getToken(RoleScriptTemplatePlace place) {
			Log.debug("RoleScriptTemplatePlace.Tokenizer.getToken");
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
