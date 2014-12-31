package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class ImportObjectiveViewPlace extends OsMaPlace {

	private String token;
	
	public HandlerManager handler;
	
	public ImportObjectiveViewPlace()
	{
		Log.info("ImportObjectiveViewPlace");
		this.token = "ImportObjectiveViewPlace";
	}
	
	public ImportObjectiveViewPlace(String token)
	{
		Log.info("ImportObjectiveViewPlace");
		this.token = token;
	}
	
	public ImportObjectiveViewPlace(String token,HandlerManager handler){
		this.handler=handler;
		this.token = token;	
	}
	
	@Override
	public String getToken() {
		return token;
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}
	
	public static class Tokenizer implements PlaceTokenizer<ImportObjectiveViewPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("ImportObjectiveViewPlace.Tokenizer");
			this.requests = requests;
		}

		public ImportObjectiveViewPlace getPlace(String token) {
			Log.debug("ImportObjectiveViewPlace.Tokenizer.getPlace");
			return new ImportObjectiveViewPlace(token);
		}

		public String getToken(ImportObjectiveViewPlace place) {
			Log.debug("ImportObjectiveViewPlace.Tokenizer.getToken");
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
