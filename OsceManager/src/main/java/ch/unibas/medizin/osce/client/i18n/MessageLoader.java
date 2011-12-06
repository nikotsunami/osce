package ch.unibas.medizin.osce.client.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Resourcen müssten in Classpath hinzugefügt werden.
 * @author michael
 *
 */
public class MessageLoader {
	private ResourceBundle messages = null;
	private static MessageLoader instance;
	
	private MessageLoader() {
		messages = ResourceBundle.getBundle("Messages");
	}
	
	public static MessageLoader getInstance() {
		if (instance == null)
			instance = new MessageLoader();
		return instance;
	}
	
	public void setLocale(String language, String country) {
		try {
			messages = ResourceBundle.getBundle("Messages", new Locale(language, country));
		} catch (MissingResourceException ex) {
			Log.error("Messages for locale " + language + "_" + country + " not found, using default bundle instead.");
			messages = ResourceBundle.getBundle("Messages");
		}
	}
	
	public ResourceBundle getMessages() {
		if (messages == null) {
			Log.error("Set a locale first via setLocale(language, country)");
			return null;
		}
		return messages;
	}
}