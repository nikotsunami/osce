package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * @author dk
 *
 */
public class OscePlace extends OsMaPlace {
	
	private String token;

	// G: SPEC START =
	
	public SemesterProxy semesterProxy;
	
	// G: SPEC END =

	public OscePlace(){
		Log.debug("OscePlace");
		this.token = "OscePlace";
	}
	
	public OscePlace(String token){
		this.token = token;
	}

	// G: SPEC START =
	
		/*public OscePlace(String token, SemesterProxy value) 
			{
				//Log.info("~Get Semester : " + value.getCalYear() + ": In RolePlace Constrcutor");
				this.token = token;
				this.semesterProxy=value;		
			}*/
			public HandlerManager handler;
			public OscePlace(String token, HandlerManager handler,SemesterProxy semesterProxy) 
			{
				Log.info("~Get Semester : " + semesterProxy.getCalYear() + ": In RolePlace Constrcutor");
				this.semesterProxy=semesterProxy;
				this.handler=handler;
				this.token = token;					
			}
			
			// G: SPEC End =
		
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Tokenizer.
	 */

	public static class Tokenizer implements PlaceTokenizer<OscePlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("OscePlace.Tokenizer");
			this.requests = requests;
		}

		public OscePlace getPlace(String token) {
			Log.debug("OscePlace.Tokenizer.getPlace");
			return new OscePlace(token);
		}

		public String getToken(OscePlace place) {
			Log.debug("OscePlace.Tokenizer.getToken");
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
