package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

public class ExportOscePlace extends OsMaPlace {

	private String token;
	public SemesterProxy semesterProxy;
	public HandlerManager handlerManager;

	public ExportOscePlace(){
		Log.debug("ExportOscePlace");
		this.token = "ExportOscePlace";
	}
	
	public ExportOscePlace(String token){
		this.token = token;
	}
	
	public ExportOscePlace(String token, HandlerManager handler, SemesterProxy proxy){
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

	public static class Tokenizer implements PlaceTokenizer<ExportOscePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ExportOscePlace.Tokenizer");
			this.requests = requests;
		}

		public ExportOscePlace getPlace(String token) {
			Log.debug("ExportOscePlace.Tokenizer.getPlace");
			return new ExportOscePlace(token);
		}

		public String getToken(ExportOscePlace place) {
			Log.debug("ExportOscePlace.Tokenizer.getToken");
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
