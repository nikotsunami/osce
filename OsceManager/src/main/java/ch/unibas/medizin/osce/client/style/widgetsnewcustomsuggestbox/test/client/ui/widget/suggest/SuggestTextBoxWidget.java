package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;



import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.ui.IsWidget;


public interface SuggestTextBoxWidget<T, W extends EventHandlingValueHolderItem<T>> extends
		IsWidget, MouseDownHandler, MouseMoveHandler, MouseOutHandler,
		HasKeyUpHandlers, HasKeyDownHandlers, HasDoubleClickHandlers,
		HasBlurHandlers {

	public abstract String getText();

	
/*
	public abstract void setValidator(Validator<String> validator);

	public abstract Validator<String> getValidator();*/

	public abstract void setSelectionRange(int i, int length);



	public abstract void setEnabled(boolean enabled);

	public abstract String getTextValue();

	public abstract void setFocus(boolean b);

	public abstract void setDefaultText(String defaultText);

	public abstract void setRepresenter(
			AbstractSuggestBox<T, W> abstractSuggestBox);

	public abstract AbstractSuggestBox<T, W> getRepresenter();

	public abstract void setText(String value);

	public abstract void setValue(T value);

	public abstract void setStyleName(String suggestField);

	// ------------------- used to position the popup ------------------

	public abstract int getAbsoluteLeft();

	public abstract int getAbsoluteTop();

	public abstract int getOffsetHeight();

	// ------------------- style handling -------------------
	public abstract void addStyleName(String suggestFieldHover);

	public abstract void removeStyleName(String suggestFieldHover);




}