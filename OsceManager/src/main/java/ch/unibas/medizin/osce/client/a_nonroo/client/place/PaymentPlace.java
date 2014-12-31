package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class PaymentPlace extends OsMaPlace {

	private String token;
	public SemesterProxy semesterProxy;
	public HandlerManager handlerManager;

	public PaymentPlace(){
		Log.debug("PaymentPlace");
		this.token = "PaymentPlace";
	}
	
	public PaymentPlace(String token){
		this.token = token;
	}
	
	public PaymentPlace(String token, HandlerManager handler, SemesterProxy proxy){
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

	public static class Tokenizer implements PlaceTokenizer<PaymentPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("PaymentPlace.Tokenizer");
			this.requests = requests;
		}

		public PaymentPlace getPlace(String token) {
			Log.debug("PaymentPlace.Tokenizer.getPlace");
			return new PaymentPlace(token);
		}

		public String getToken(PaymentPlace place) {
			Log.debug("PaymentPlace.Tokenizer.getToken");
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
