
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;

import com.google.gwt.event.dom.client.ChangeEvent;


public class SuggestChangeEvent<T, W extends EventHandlingValueHolderItem<T>> extends
		ChangeEvent {
	protected AbstractSuggestBox<T, W> source;
	protected T selection;
	protected boolean selected;
	protected String text;

	public SuggestChangeEvent(AbstractSuggestBox<T, W> suggestBox, T selection) {
		this.source = suggestBox;
		this.selection = selection;
		this.selected = true;
	}

	public SuggestChangeEvent(AbstractSuggestBox<T, W> suggestBox, String text) {
		this.source = suggestBox;
		this.text = text;
		this.selected = false;
	}

	public T getSelection() {
		return selection;
	}

	public void setSelection(T selection) {
		this.selection = selection;
	}

	public AbstractSuggestBox<T, W> getSource() {
		return source;
	}

	public void setSource(AbstractSuggestBox<T, W> source) {
		this.source = source;
	}

	
	public boolean isSelected() {
		return selected;
	}

}
