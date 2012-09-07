package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class BellSchedulePlace extends OsMaPlace {

	private String token;
	public SemesterProxy semesterProxy;
	public HandlerManager handler;

	public BellSchedulePlace() {
		Log.debug("BellSchedulePlace.BellSchedulePlace");
		this.token = "BellSchedulePlace";
	}

	public BellSchedulePlace(String token) {
		this.token = token;
	}

	public BellSchedulePlace(String token, HandlerManager handler,
			SemesterProxy semesterProxy) {
		this.semesterProxy = semesterProxy;
		this.handler = handler;
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
	public static class Tokenizer implements PlaceTokenizer<BellSchedulePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("BellSchedulePlace.Tokenizer");
			this.requests = requests;
		}

		public BellSchedulePlace getPlace(String token) {
			Log.debug("BellSchedulePlace.Tokenizer.getPlace");
			return new BellSchedulePlace(token);
		}

		public String getToken(BellSchedulePlace place) {
			Log.debug("BellSchedulePlace.Tokenizer.getToken");
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
