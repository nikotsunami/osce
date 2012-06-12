package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ExaminationSchedulePlace extends OsMaPlace {

	private String token;

	public ExaminationSchedulePlace(){
		Log.debug("ExaminationSchedulePlace.ExaminationSchedulePlace");
		this.token = "ExaminationSchedulePlace";
	}

	public ExaminationSchedulePlace(String token){
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
	public static class Tokenizer implements PlaceTokenizer<ExaminationSchedulePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ExaminationSchedulePlace.Tokenizer");
			this.requests = requests;
		}

		public ExaminationSchedulePlace getPlace(String token) {
			Log.debug("ExaminationSchedulePlace.Tokenizer.getPlace");
			return new ExaminationSchedulePlace(token);
		}

		public String getToken(ExaminationSchedulePlace place) {
			Log.debug("ExaminationSchedulePlace.Tokenizer.getToken");
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
