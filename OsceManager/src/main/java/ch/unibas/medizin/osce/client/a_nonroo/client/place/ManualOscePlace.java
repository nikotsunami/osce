package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ManualOscePlace extends OsMaPlace {

	private String token;
	public SemesterProxy semesterProxy;
	public HandlerManager handlerManager;

	public ManualOscePlace(){
		Log.debug("ManualOscePlace");
		this.token = "ManualOscePlace";
	}
	
	public ManualOscePlace(String token){
		this.token = token;
	}
	
	public ManualOscePlace(String token, HandlerManager handler, SemesterProxy proxy){
		this.token = token;
		this.semesterProxy = proxy;
		this.handlerManager = handler;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static class Tokenizer implements PlaceTokenizer<ManualOscePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ManualOscePlace.Tokenizer");
			this.requests = requests;
		}

		public ManualOscePlace getPlace(String token) {
			Log.debug("ManualOscePlace.Tokenizer.getPlace");
			return new ManualOscePlace(token);
		}

		public String getToken(ManualOscePlace place) {
			Log.debug("ManualOscePlace.Tokenizer.getToken");
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
