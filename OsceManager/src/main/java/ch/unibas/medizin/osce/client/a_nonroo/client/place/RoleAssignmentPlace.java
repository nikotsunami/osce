package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.requestfactory.shared.RequestFactory;

@SuppressWarnings("deprecation")
public class RoleAssignmentPlace extends OsMaPlace {

	private String token;
	public SemesterProxy semesterProxy;
	public HandlerManager handler;

	public RoleAssignmentPlace() {
		Log.debug("SPRoleAssignmentPlace.SPRoleAssignmentPlace");
		this.token = "SPRoleAssignmentPlace";
	}

	public RoleAssignmentPlace(String token, HandlerManager handler,
			SemesterProxy semesterProxy) {
		Log.info("~Get Semester : " + semesterProxy.getCalYear()
				+ ": In SPRoleAssignmentPlace Constrcutor");
		this.semesterProxy = semesterProxy;
		this.handler = handler;
		this.token = token;
	}

	public RoleAssignmentPlace(String token) {
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
	public static class Tokenizer implements
			PlaceTokenizer<RoleAssignmentPlace> {
		private final RequestFactory requests;

		public Tokenizer(RequestFactory requests) {
			Log.debug("SPRoleAssignmentPlace.Tokenizer");
			this.requests = requests;
		}

		public RoleAssignmentPlace getPlace(String token) {
			Log.debug("SPRoleAssignmentPlace.Tokenizer.getPlace");
			return new RoleAssignmentPlace(token);
		}

		public String getToken(RoleAssignmentPlace place) {
			Log.debug("SPRoleAssignmentPlace.Tokenizer.getToken");
			return place.getToken();
		}

		public RequestFactory getRequests() {
			return requests;
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
