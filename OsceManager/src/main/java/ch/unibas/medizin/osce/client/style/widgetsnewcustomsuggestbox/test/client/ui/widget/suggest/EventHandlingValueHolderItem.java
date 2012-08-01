
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.UIObject;


public interface EventHandlingValueHolderItem<T> extends ValueHolderItem<T>,
		HasAllMouseHandlers, HasClickHandlers {
	

public static final String ITEM_DEFAULT_STYLE = "eu-nextstreet-SuggestItem";

	
	public T getValue();

	
	public void setValue(T value);

	
	public void setSelected(boolean selected);

	
	public void hover(boolean hover);

	
	public void setStyleName(String item);

	public UIObject getUiObject();

	
	ValueRendererFactory<T, ?> getValueRendererFactory();
}
