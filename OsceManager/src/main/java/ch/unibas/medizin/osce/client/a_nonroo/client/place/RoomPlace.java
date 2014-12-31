package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * @author dk
 *
 */
public class RoomPlace extends OsMaPlace {
	
	private String token;

	public RoomPlace(){
		Log.debug("RoomPlace");
		this.token = "RoomPlace";
	}
	
	public RoomPlace(String token){
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

	public static class Tokenizer implements PlaceTokenizer<RoomPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("RoomPlace.Tokenizer");
			this.requests = requests;
		}

		public RoomPlace getPlace(String token) {
			Log.debug("RoomPlace.Tokenizer.getPlace");
			return new RoomPlace(token);
		}

		public String getToken(RoomPlace place) {
			Log.debug("RoomPlace.Tokenizer.getToken");
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
