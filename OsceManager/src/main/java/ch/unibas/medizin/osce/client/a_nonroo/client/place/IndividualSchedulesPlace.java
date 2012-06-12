package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class IndividualSchedulesPlace extends OsMaPlace {

	private String token;

	public IndividualSchedulesPlace(){
		Log.debug("ExaminationSchedulePlace.ExaminationSchedulePlace");
		this.token = "IndividualSchedulesPlace";
	}

	public IndividualSchedulesPlace(String token){
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
	public static class Tokenizer implements PlaceTokenizer<IndividualSchedulesPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ExaminationSchedulePlace.Tokenizer");
			this.requests = requests;
		}

		public IndividualSchedulesPlace getPlace(String token) {
			Log.debug("ExaminationSchedulePlace.Tokenizer.getPlace");
			return new IndividualSchedulesPlace(token);
		}

		public String getToken(IndividualSchedulesPlace place) {
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
