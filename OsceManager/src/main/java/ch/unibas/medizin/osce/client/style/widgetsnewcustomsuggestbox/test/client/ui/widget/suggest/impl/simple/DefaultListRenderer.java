
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple;

import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.ValueRendererFactory.ListRenderer;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;




public class DefaultListRenderer<T, W extends EventHandlingValueHolderItem<T>> extends
		VerticalPanel implements ListRenderer<T, W> {

	@Override
	public void add(W item) {
		super.add((Widget) item);
	}

	@SuppressWarnings("unchecked")
	public W getAt(int index) {
		return (W) super.getWidget(index);
	}
}
