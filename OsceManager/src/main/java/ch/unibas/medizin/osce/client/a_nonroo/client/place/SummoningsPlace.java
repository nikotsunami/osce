package ch.unibas.medizin.osce.client.a_nonroo.client.place;


import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class SummoningsPlace extends OsMaPlace {

	private String token;
	
	public HandlerManager handlerManager;
	public SemesterProxy semesterProxy;

	public SummoningsPlace(){
		Log.debug("SummoningsPlace.SummoningsPlace");
		this.token = "SummoningsPlace";
	}

	public SummoningsPlace(String token){
		this.token = token;
	}
	
	public SummoningsPlace(String token,HandlerManager handler){
		this.handlerManager = handler;
		this.token = token;
	}
	
	public SummoningsPlace(String token,HandlerManager handler,SemesterProxy semesterProxy){
		Log.info("Semester Proxy: "+semesterProxy.getCalYear());
		this.semesterProxy = semesterProxy;
		this.handlerManager = handler;
		this.token = token;
	}
	
	public SemesterProxy getSemesterProxy() {
		return semesterProxy;
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
	public static class Tokenizer implements PlaceTokenizer<SummoningsPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("SummoningsPlace.Tokenizer");
			this.requests = requests;
		}

		public SummoningsPlace getPlace(String token) {
			Log.debug("SummoningsPlace.Tokenizer.getPlace");
			return new SummoningsPlace(token);
		}

		public String getToken(SummoningsPlace place) {
			Log.debug("SummoningsPlace.Tokenizer.getToken");
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
