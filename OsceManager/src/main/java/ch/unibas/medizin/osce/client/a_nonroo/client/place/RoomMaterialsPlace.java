package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class RoomMaterialsPlace extends OsMaPlace{
	
	private String token;
	
	public RoomMaterialsPlace() {
		Log.debug("RoomMaterialsPlace");
		this.token = "RoomMaterialsPlace";
	}
	
	public RoomMaterialsPlace(String token)
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

	public static class Tokenizer implements PlaceTokenizer<RoomMaterialsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("RoomMaterialsPlace.Tokenizer");
			this.requests = requests;
		}

		public RoomMaterialsPlace getPlace(String token) {
			Log.debug("RoomMaterialsPlace.Tokenizer.getPlace");
			return new RoomMaterialsPlace(token);
		}

		public String getToken(RoomMaterialsPlace place) {
			Log.debug("RoomMaterialsPlace.Tokenizer.getToken");
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
