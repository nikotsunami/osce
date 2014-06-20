
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget;






import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;



public class AdvancedTextBox extends TextBox implements HasDoubleClickHandlers {
	private static final String DEFAULT_TEXT_STYLE = "eu-nextstreet-AdvancedTextBoxDefaultText";
	private static final String MANDATORY_TEXT_STYLE = "eu-nextstreet-AdvancedTextBoxMandatoryText"; // Button Click
	private static final String ERROR_TEXT_STYLE = "eu-nextstreet-AdvancedTextBoxErrorText";
	private static final String READ_ONLY_TEXT_STYLE = "eu-nextstreet-AdvancedTextBoxReadOnlyText";
	//protected Validator<String> validator;
	protected String defaultText;
	protected String defaultTextStyle;
	protected String errorTextStyle;
	protected String mandatoryTextStyle;
	protected String readOnlyTextStyle;
	protected boolean mandatory;
	protected Widget representer;
	/*
	protected UIHandler uiHandler = new UIHandler() {

		@Override
		public void removeStyleName(String style) {
		}

		@Override
		public void removeError() {
			setTitle("");
		}

		@Override
		public void handleTextStyles() {
		}

		@Override
		public void handleError(ValidationException error) {
			setTitle(error.getMessage());
		}

		@Override
		public void handleDefaultText() {
		}

		@Override
		public void addStyleName(String style) {
		}

		
	};
	
	*/

	public AdvancedTextBox() {		
		this(null);
		//Log.info("Constructor1");
	}

	public AdvancedTextBox(final String defautText) {
		//Log.info("Constructor2");
		this.defaultText = defautText;
		addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				String text = AdvancedTextBox.this.getText();
				if (AdvancedTextBox.this.defaultText == null
						|| !AdvancedTextBox.this.defaultText.equals(text)) {
					setSelectionRange(0, text.length());
				} else {
					AdvancedTextBox.super.setText("");
				}
			}
		});

		addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				handleDefaultText();
			}
		});

		addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				//handleTextStyles();
			}
		});
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(
			final DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		handleDefaultText();
	}

	/**
	 * If the box text is empty fills it with the default value
	 * 
	 */
	protected void handleDefaultText() {
		if (defaultText != null && defaultText.length() > 0) {
			boolean empty = isEmptyTextField();
			if (empty && !isReadOnly()) {
				super.setText(defaultText);
			}
		}
		/*if (uiHandler != null)
			uiHandler.handleDefaultText();
*/
	//	handleTextStyles();
	}

	
	protected boolean isEmptyTextField() {
		String text = getText();
		return text == null || text.trim().length() == 0;
	}

	
	public boolean isEmpty() {
		return isEmptyTextField()
				|| (defaultText == null ? true : defaultText.equals(getText()));
	}

	
	/*protected void handleTextStyles() {
		if (isReadOnly()) {
			addStyleName(getReadOnlyTextStyle());
		} else {
			String text = getTextValue();
			ValidationException error = null;
			if (validator != null) {
				try {
					validator.validate(text);
				} catch (ValidationException ex) {
					error = ex;
				}
			}
			if (error == null) {
				if (isEmptyTextField() || text.trim().length() == 0) {
				//	addStyleName(getTextStyle());
				} else {
				//	removeStyleName(getTextStyle());
				}
			//	removeStyleName(getErrorTextStyle());
			//	removeError();
			} else {
				//removeStyleName(getTextStyle());
			//	addStyleName(getErrorTextStyle());
			//	handleError(error);
			}
		}
		if (uiHandler != null)
			uiHandler.handleTextStyles();

	}*/

	
	/*
	protected void handleError(ValidationException error) {
		if (uiHandler != null)
			uiHandler.handleError(error);
	}

	
	protected void removeError() {
		if (uiHandler != null)
			uiHandler.removeError();
	}
*/
	

	
	public String getTextValue() {
		String text = super.getText();
		if (text.trim().equals(defaultText))
			return "";
		return text;
	}

	public void setDefaultText(String text) {
		defaultText = text;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (defaultText != null && isEmptyTextField()) {
			setText(defaultText);
		}
	}

	
	/*public Validator<String> getValidator() {
		return validator;
	}

	public void setValidator(Validator<String> validator) {
		this.validator = validator;
		handleTextStyles();
	}*/

	public String getReadOnlyTextStyle() {
		if (readOnlyTextStyle == null)
			return READ_ONLY_TEXT_STYLE;
		return readOnlyTextStyle;
	}

	public void setReadOnlyTextStyle(String readOnlyTextStyle) {
		this.readOnlyTextStyle = readOnlyTextStyle;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		removeStyleName(getReadOnlyTextStyle());
		super.setReadOnly(readOnly);
	//	handleTextStyles();
		if (readOnly) {
			String text = getText();
			if (defaultText != null && text != null && defaultText.equals(text))
				setText("");
		} else {
			handleDefaultText();
		}
	}

	@Override
	public void removeStyleName(String style) {
		super.removeStyleName(style);
		/*if (uiHandler != null)
			uiHandler.removeStyleName(style);*/
	}

	@Override
	public void addStyleName(String style) {
		super.addStyleName(style);
		/*if (uiHandler != null)
			uiHandler.addStyleName(style);*/
	}

	/*
	public UIHandler getUiHandler() {
		return uiHandler;
	}

	public void setUiHandler(UIHandler uiHandler) {
		this.uiHandler = uiHandler;
	}

*/	
	public Widget getRepresenter() {
		return representer;
	}

	public void setRepresenter(Widget representer) {
		this.representer = representer;
	}

}
