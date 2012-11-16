package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class StudentManagementPlace extends OsMaPlace {

	private String token;
	public SemesterProxy semesterProxy;
	public HandlerManager handlerManager;

	public StudentManagementPlace(){
		Log.debug("StudentManagementPlace");
		this.token = "StudentManagementPlace";
	}
	
	public StudentManagementPlace(String token){
		this.token = token;
	}
	
	public StudentManagementPlace(String token, HandlerManager handler){
		this.token = token;
		this.handlerManager = handler;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static class Tokenizer implements PlaceTokenizer<StudentManagementPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("StudentManagementPlace.Tokenizer");
			this.requests = requests;
		}

		public StudentManagementPlace getPlace(String token) {
			Log.debug("StudentManagementPlace.Tokenizer.getPlace");
			return new StudentManagementPlace(token);
		}

		public String getToken(StudentManagementPlace place) {
			Log.debug("StudentManagementPlace.Tokenizer.getToken");
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
