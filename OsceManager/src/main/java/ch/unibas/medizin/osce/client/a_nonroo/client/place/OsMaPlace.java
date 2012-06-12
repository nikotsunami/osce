package ch.unibas.medizin.osce.client.a_nonroo.client.place;

import com.google.gwt.place.shared.Place;

public abstract class OsMaPlace extends Place {
	public abstract String getToken();
	
	public abstract void setToken(String token);
}
