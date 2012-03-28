package ch.unibas.medizin.osce.client.style.widgets;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TextBox;

public class QuickSearchBox extends TextBox {
	private final Timer _timer = new EntryTimer();
	private final Delegate _delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private String _description = constants.searchField();
	private int _timeout = OsMaConstant.ENTRY_TIMEOUT_MS;
	
	private String _searchString;
	
	public interface Delegate {
		public void performAction();
	}
	
	private class EntryTimer extends Timer {
		@Override
		public void run() {
			if (_searchString.equals(getText())) {
				_delegate.performAction();
			} else {
				schedule(_timeout);
			}
		}
	}
	
	public QuickSearchBox(Delegate delegate, int timeout) {
		this(delegate);
		_timeout = timeout;
	}
	
	public QuickSearchBox(Delegate delegate, String description) {
		this(delegate);
		_description = description;
	}
	
	public QuickSearchBox(Delegate delegate, int timeout, String description) {
		this(delegate);
		_timeout = timeout;
		_description = description;
	}
	
	public QuickSearchBox(Delegate delegate) {
		super();
		if (delegate == null) throw new IllegalArgumentException("QuickSearchBox.Delegate delegate must not be null");
		
		_delegate = delegate;
		setText(_description);
		addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				scheduleAction(event);
			}
		});
		
		addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				clearDescription();
			}
		});
		
		addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				showDescription();
			}
		});
	}

	private void scheduleAction(KeyUpEvent event) {
		_searchString = super.getText().trim();

		switch (event.getNativeKeyCode()) {
		case KeyCodes.KEY_ENTER:
		case KeyCodes.KEY_TAB:
			_timer.cancel();
			_delegate.performAction();
			break;
		case KeyCodes.KEY_ALT:
		case KeyCodes.KEY_CTRL:
		case KeyCodes.KEY_DOWN:
		case KeyCodes.KEY_END:
		case KeyCodes.KEY_ESCAPE:
		case KeyCodes.KEY_HOME:
		case KeyCodes.KEY_LEFT:
		case KeyCodes.KEY_PAGEDOWN:
		case KeyCodes.KEY_PAGEUP:
		case KeyCodes.KEY_RIGHT:
		case KeyCodes.KEY_SHIFT:
		case KeyCodes.KEY_UP:
			// do nothing
			break;
		default:
			_timer.schedule(_timeout);
		}
	}
	
	private void clearDescription() {
		if (super.getText().equals(_description)) {
			setText("");
		}
	}
	
	private void showDescription() {
		if (super.getText().isEmpty()) {
			setText(_description);
		}
	}
	
	public String getValue() {
		return getText();
	}
	
	public String getText() {
		if (super.getText().equals(_description)) {
			return "";
		}
		return super.getText();
	}
	
	public void setDescription(String description) {
		_description = description;
	}
}
