package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class PlanSPTrainingPlace extends OsMaPlace {
	
	private String token;

	public SemesterProxy semesterProxy;
	
	public PlanSPTrainingPlace(){
		Log.debug("PlanSPTrainingPlace");
		this.token = "RoomPlace";
	}
	
	public PlanSPTrainingPlace(String token){
		this.token = token;
	}

	public HandlerManager handler;
	public PlanSPTrainingPlace(String token, HandlerManager handler,SemesterProxy semesterProxy) 
	{
		Log.info("~Get Semester : " + semesterProxy.getCalYear() + ": In Plan SP training place Constrcutor");
		this.semesterProxy=semesterProxy;
		this.handler=handler;
		this.token = token;					
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public static class Tokenizer implements PlaceTokenizer<PlanSPTrainingPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("PlanSPTrainingPlace.Tokenizer");
			this.requests = requests;
		}

		public PlanSPTrainingPlace getPlace(String token) {
			Log.debug("PlanSPTrainingPlace.Tokenizer.getPlace");
			return new PlanSPTrainingPlace(token);
		}

		public String getToken(PlanSPTrainingPlace place) {
			Log.debug("PlanSPTrainingPlace.Tokenizer.getToken");
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
