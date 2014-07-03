package ch.unibas.medizin.osce.client.style.widgets;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Class defining a standard single-line {@link TextBox} which
 * fires a value change-event whenever something is entered, cut or pasted
 * into / from the parent TextBox.
 * @author mwagner
 *
 */

public class CheckedTextBox extends TextBox {
	
	protected class KeyPressToValueChangeConverter implements KeyPressHandler {

		/** 
		 * Fire a ValueChangeEvent on pressing any key 
		 **/ 
		@Override
		public void onKeyPress(KeyPressEvent event) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				
                @Override
                public void execute() {
                    ValueChangeEvent.fire(CheckedTextBox.this, getText());
                }

            });
		}
	}
	
	public CheckedTextBox() {
		super();
		sinkEvents(Event.ONPASTE);
		addCutHandler(this.getElement());
		this.addKeyPressHandler(new KeyPressToValueChangeConverter());
	}
	
	protected CheckedTextBox(Element element) {
		super(element);
		sinkEvents(Event.ONPASTE);
		addCutHandler(this.getElement());
		this.addKeyPressHandler(new KeyPressToValueChangeConverter());
	}
	
	@Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
            case Event.ONPASTE:
                fireValueChangeEvent();
                break;
        }
    }
	
	private native void addCutHandler(Element elementID)
    /*-{
        var temp = this;  // hack to hold on to 'this' reference
        elementID.oncut = function(e) {
            temp.@ch.unibas.medizin.osce.client.style.widgets.IntegerBox::fireValueChangeEvent()();
        }
    }-*/;
		
	protected void fireValueChangeEvent() {
		Log.debug("fireValueChangeEvent()");
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
            @Override
            public void execute() {
                ValueChangeEvent.fire(CheckedTextBox.this, getText());
            }

        });
	}
}
