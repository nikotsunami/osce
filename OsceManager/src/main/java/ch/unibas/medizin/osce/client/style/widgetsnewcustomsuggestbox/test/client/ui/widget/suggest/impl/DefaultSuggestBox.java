
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl;

import java.util.ArrayList;

import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.AbstractSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.SuggestOracle;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.SuggestOracle.Request;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.SuggestPossibilitiesCallBack;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.SuggestTextBoxWidget;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.SuggestTextBoxWidgetImpl;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultValueRenderer;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;


public class DefaultSuggestBox<T, W extends EventHandlingValueHolderItem<T>> extends AbstractSuggestBox<T, W> {

	@SuppressWarnings("rawtypes")
	interface SuggestBoxUiBinder extends UiBinder<Widget, DefaultSuggestBox> {
	}

	private static SuggestBoxUiBinder	uiBinder = GWT.create(SuggestBoxUiBinder.class);

	protected @UiField
	SuggestTextBoxWidgetImpl<T, W>		textField;

	protected int	suggestionMaxCount	= 10;
	protected SuggestOracle<T>				suggestOracle;

	
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private final class CallBackHandler implements SuggestOracle.Callback {
		private SuggestPossibilitiesCallBack<T>	innerCallBack;

		@SuppressWarnings("unchecked")
		public void onSuggestionsReady(SuggestOracle.Request request, SuggestOracle.Response response) {
			innerCallBack.setPossibilities(new ArrayList<T>(response.getSuggestions()));
			// display.setMoreSuggestions(response.hasMoreSuggestions(),
			// response.getMoreSuggestionsCount());
			// display.showSuggestions(SuggestBox.this,
			// response.getSuggestions(),
			// oracle.isDisplayStringHTML(),
			// isAutoSelectEnabled(),
			// suggestionCallback);
		}

		public SuggestPossibilitiesCallBack<T> getInnerCallBack() {
			return innerCallBack;
		}

		public void setInnerCallBack(SuggestPossibilitiesCallBack<T> innerCallBack) {
			this.innerCallBack = innerCallBack;
		}

	};

	private CallBackHandler	callback	= new CallBackHandler();

	public DefaultSuggestBox() {
		this(null);
		//System.out.println("Call Constructor1");
		this.setWidth(200);
		//textField.addStyleName("addBorder");
	}

	public DefaultSuggestBox(String defaultText) {
		this(defaultText, new DefaultSuggestOracle<T>());
		//System.out.println("Call Constructor2");
		textField.addStyleName("customControls");
		this.setWidth(200);
	}

	@SuppressWarnings("unchecked")
	public DefaultSuggestBox(String defaultText, SuggestOracle<T> suggestOracle) {
		initWidget(uiBinder.createAndBindUi(this));
		init(defaultText);		
		this.suggestOracle = suggestOracle;
		suggestOracle.setSuggestBox((AbstractSuggestBox<T, EventHandlingValueHolderItem<T>>) this);
		//System.out.println("Call Constructor3");
		this.setWidth(130);
	}

	// ------------------ default event handling -----------------------
	@UiHandler("textField")
	public void onKeyUp(KeyUpEvent keyUpEvent) {
		super.onKeyUp(keyUpEvent);
	}

	@UiHandler("textField")
	public void onBlur(BlurEvent event) {
		super.onBlur(event);
	}

	@UiHandler("textField")
	public void onDoubleClick(DoubleClickEvent event) {
		super.onDoubleClick(event);
	}

	// -------------------------- end.

	@Override
	protected void computeFiltredPossibilities(final String text, final SuggestPossibilitiesCallBack<T> suggestPossibilitiesCallBack) {
		Request request = new Request(text, suggestionMaxCount);
		callback.setInnerCallBack(suggestPossibilitiesCallBack);
		suggestOracle.requestSuggestions(request, callback);
	}

	@Override
	protected boolean fillValue(T t, boolean commit) {
		// get the typed text length before updating the field with the selected
		// value
		int startIndex = textField.getText().length();
		// now safely update the value
		if (commit || strictMode) {
			super.fillValue(t, commit);
			if (!commit && !strictMode) {
				textField.setSelectionRange(startIndex, textField.getText().length() - startIndex);
			}
			return true;
		} else {
			// text.setSelectionRange(0, text.getText().length());
			return false;
		}
	}

	@Override
	public SuggestTextBoxWidgetImpl<T, W> getTextField() {
		return textField;
	}

	public void setTextField(SuggestTextBoxWidget<T, W> textField) {
		this.textField = (SuggestTextBoxWidgetImpl<T, W>) textField;
	}

	public SuggestOracle<T> getSuggestOracle() {
		return suggestOracle;
	}

	public void setSuggestOracle(SuggestOracle<T> suggestOracle) {
		this.suggestOracle = suggestOracle;
	}

	public int getPropositionsMaxCount() {
		return suggestionMaxCount;
	}

	public void setPropositionsMaxCount(int propositionsMaxCount) {
		this.suggestionMaxCount = propositionsMaxCount;
	}
	
	public void setWidth(int i)
	{
		String s =(i+5)+"px";
		String s1 =i+"px";
		String s2 =(i-2)+"px";
		
		this.getTextField().advancedTextBox.setWidth(s1);
		Log.info("Offset Width1: " + this.scrollPanel.getOffsetWidth());
		this.scrollPanel.setWidth(s);
		Log.info("Offset Width: " + this.scrollPanel.getOffsetWidth());
		DefaultValueRenderer.widthValue=s2;
		this.setRendererWidth(s2);
		//this.textField.setWidth(i);	
		
	}

}
