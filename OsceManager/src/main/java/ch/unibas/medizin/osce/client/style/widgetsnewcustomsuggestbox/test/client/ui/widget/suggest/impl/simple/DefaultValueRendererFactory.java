
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple;

import java.util.Map;

import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.ValueRendererFactory;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.param.Option;

import com.google.gwt.text.shared.Renderer;


public class DefaultValueRendererFactory<T, W extends EventHandlingValueHolderItem<T>> implements ValueRendererFactory<T, W> {

	
	@SuppressWarnings("unchecked")
	@Override
	public W createValueRenderer(T value, String filterText, Map<String, Option<?>> options) {
		//W toReturn = (W) new DefaultValueRenderer<T>(value, filterText, BooleanOption.isEnabled(DefaultOptions.CASE_SENSITIVE.name(), options), this);
		//W toReturn = (W) new DefaultValueRenderer<T>(value, filterText, BooleanOption.isEnabled("CASE_SENSITIVE", options), this);
		W toReturn = (W) new DefaultValueRenderer<T>(value, filterText, false, this);
		toReturn.setStyleName(EventHandlingValueHolderItem.ITEM_DEFAULT_STYLE);
		return toReturn;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public W createValueRenderer(T value, String filterText, Map<String, Option<?>> options,Renderer<T> renderer) {
		//Window.alert(DefaultOptions.CASE_SENSITIVE.name());
		//W toReturn = (W) new DefaultValueRenderer<T>(value, filterText, BooleanOption.isEnabled(DefaultOptions.CASE_SENSITIVE.name(), options), this,renderer);		
		//W toReturn = (W) new DefaultValueRenderer<T>(value, filterText, BooleanOption.isEnabled("CASE_SENSITIVE", options), this,renderer);
		W toReturn = (W) new DefaultValueRenderer<T>(value, filterText, false, this,renderer);
		toReturn.setStyleName(EventHandlingValueHolderItem.ITEM_DEFAULT_STYLE);
		return toReturn;
	}

	@Override
	public ValueRendererFactory.ListRenderer<T, W> createListRenderer() {
		DefaultListRenderer<T, W> defaultListRenderer = new DefaultListRenderer<T, W>();
		defaultListRenderer.setSpacing(0);
		return defaultListRenderer;
	}

}
