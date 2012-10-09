package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class StatisticalEvaluationPlace extends OsMaPlace {

	private String token;
	
	
	public static SemesterProxy semesterProxy;
	public static HandlerManager handler;


	public StatisticalEvaluationPlace(){
		Log.debug("StatisticalEvaluationPlace.StatisticalEvaluationPlace");
		this.token = "StatisticalEvaluationPlace";
	}

	public StatisticalEvaluationPlace(String token){
		this.token = token;
	}
	// Module10 Create plans
		public StatisticalEvaluationPlace(String token,HandlerManager handler,SemesterProxy semesterProxy)
		{
			Log.info("Call Place Constructor(3args)");
			Log.info("~Get Semester : " + semesterProxy.getCalYear() + ": In StatisticalEvaluationPlace Constrcutor");
			Log.info("this token--"+this.token+"   token--"+token);
			//this.token = "StatisticalEvaluationPlace";
			this.token = token;
			this.semesterProxy=semesterProxy;
			this.handler=handler;
			
		}
		// E Module10 Create plans

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Tokenizer.
	 */
	public static class Tokenizer implements PlaceTokenizer<StatisticalEvaluationPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("StatisticalEvaluationPlace.Tokenizer");
			this.requests = requests;
		}

		public StatisticalEvaluationPlace getPlace(String token) {
			Log.debug("StatisticalEvaluationPlace.Tokenizer.getPlace");
			return new StatisticalEvaluationPlace(token);
		}

		public String getToken(StatisticalEvaluationPlace place) {
			Log.debug("StatisticalEvaluationPlace.Tokenizer.getToken");
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
