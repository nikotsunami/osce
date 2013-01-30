package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsMaPlace;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Cookies;

public class UserPlaceSettings {
	private OsMaPlace place;
	private HashMap<String, String> placeSettings;
	
	public UserPlaceSettings(OsMaPlace place) {
		this.place = place;
		loadPlaceSettings();
	}
	
	public void setValue(String key, boolean value) {
		setValue(key, (value) ? "1" : "0");
	}
	
	public void setValue(String key, int value) {
		setValue(key, "" + value);
	}
	
	public void setValue(String key, String value) {
		placeSettings.put(key.trim(), value.trim());
	}
	
	public void flush() {
		if (placeSettings.isEmpty()) {
			return;
		}
		
		Iterator<String> i = placeSettings.keySet().iterator();
		StringBuilder sb = new StringBuilder();
		while (i.hasNext()) {
			String key = i.next();
			String value = placeSettings.get(key);
			sb.append(key);
			sb.append("=");
			sb.append(value);
			sb.append(";");
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		
		// cookie, valid 30 days
		Log.debug("trying to set cookie " + place.getToken() + " [" + sb.toString() + "]");
		Cookies.setCookie(place.getToken(), sb.toString(), new Date(System.currentTimeMillis() + 2592000000L));
	}
	
	public boolean getBooleanValue(String key) {
		String strValue = getValue(key);
		if (strValue != null) { 
			try {
				return (Integer.parseInt(strValue) == 0) ? false : true;
			} catch (NumberFormatException ex) {}
		}
		return false;
	}
	
	public int getIntValue(String key) {
		String strValue = getValue(key);
		try {
			return Integer.parseInt(strValue);
		} catch (NumberFormatException ex) {}
		return -1;
	}
	
	public String getValue(String key) {
		return placeSettings.get(key);
	}
	
	public boolean hasSettings() {
		if (placeSettings.isEmpty())
			return false;
		return true;
	}
	
	private void loadPlaceSettings() {
		Log.debug("trying to fetch cookie " + place.getToken());
		String cookie = Cookies.getCookie(place.getToken());

		placeSettings = new HashMap<String,String>();
		if (cookie != null) {
			String[] kvPairs = cookie.split(";");
			for (String kvPair : kvPairs) {
				String[] tokens = kvPair.split("=");
				if (tokens.length > 1) {
					String key = tokens[0].trim();
					String value = tokens[1].trim();
					placeSettings.put(key, value);
				}
			}
		}
	}
}
