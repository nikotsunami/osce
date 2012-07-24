package ch.unibas.medizin.osce.client.a_nonroo.client;

import ch.unibas.medizin.osce.shared.Locale;

import com.google.gwt.user.client.ui.IsWidget;

public interface OsMaHeader extends IsWidget {
	public interface Delegate {
		public void changeLocale(Locale locale);
		public void changeRecordValue(String val);
	}
	
	public void setDelegate(Delegate delegate);
}
