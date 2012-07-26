package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;

import java.util.Map;

import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.param.Option;

import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.IsWidget;


public interface ValueRendererFactory<T, W extends ValueHolderItem<T>> {
	public static interface ListRenderer<T, W> extends IsWidget {

		int getWidgetCount();

		void clear();

		void add(W item);

		W getAt(int index);

	}

	W createValueRenderer(T value, String filterText, Map<String, Option<?>> options);
	
	W createValueRenderer(T value, String filterText, Map<String, Option<?>> options,Renderer<T> renderer);

	ListRenderer<T, W> createListRenderer();

}
