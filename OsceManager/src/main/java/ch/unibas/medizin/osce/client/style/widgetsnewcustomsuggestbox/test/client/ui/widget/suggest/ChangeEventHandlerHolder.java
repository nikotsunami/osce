
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;

public abstract class ChangeEventHandlerHolder<P, E extends ChangeEvent> extends Composite {
	protected List<ChangeHandler> changeHandlerList = new ArrayList<ChangeHandler>();

	public void addHandler(ChangeHandler changeHandler) {
		changeHandlerList.add(changeHandler);
	}

	public void removeHandler(ChangeHandler changeHandler) {
		changeHandlerList.remove(changeHandler);
	}

	private void changeOccured(E changeEvent) {
		for (ChangeHandler changeHandler : changeHandlerList) {
			changeHandler.onChange(changeEvent);
		}
	}

	
	protected void fireChangeOccured(P param) {
		E changeEvent = changedValue(param);
		changeOccured(changeEvent);
	}

	
	protected abstract E changedValue(P param);
}
