

package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;


public interface ValueHolderItem<T> extends IsWidget {
	public static final String ITEM_DEFAULT_STYLE = "eu-nextstreet-SuggestItem";

	
	public T getValue();

	
	public void setValue(T value);

	
	public void setSelected(boolean selected);

	
	public void hover(boolean hover);

	
	public void setStyleName(String item);

	public UIObject getUiObject();

	
	ValueRendererFactory<T, ?> getValueRendererFactory();

}
