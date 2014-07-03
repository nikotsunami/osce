package ch.unibas.medizin.osce.client.style.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class IntegerBox extends CheckedTextBox {
	private String lastGoodValue = "";
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
	
	private class IntegerCheckHandler implements ValueChangeHandler<String> {

		/**
		 * this method is used to check whether the new value in 
		 * the TextBox is a valid integer and is within the 
		 * predefined size boundaries (min, max, unsigned).
		 */
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			if (event.getValue().length() == 0 
					|| (event.getValue().equals("-") && min < 0)) {
				lastGoodValue = event.getValue();
			} else {
				try {
					int value = Integer.parseInt(event.getValue());
					if ((value > max && max > 0) || (value < min && min <= 0)) {
						IntegerBox.this.setValue(lastGoodValue);
					} else {
						lastGoodValue = event.getValue();
					}
				} catch (NumberFormatException e) {
					IntegerBox.this.setValue(lastGoodValue);
				}
			}
		}
	}
	
	public IntegerBox() {
		super();
		this.addValueChangeHandler(new IntegerCheckHandler());
	}
	
	protected IntegerBox(Element element) {
		super(element);
		this.addValueChangeHandler(new IntegerCheckHandler());
	}
		
	/**
	 * Set the highest number allowed
	 * @param max
	 */
	public void setMax(int max) {
		this.max = max;
		try {
			if (getValue().length() > 0 
					&& !getValue().equals("-")
					&& Integer.parseInt(getValue()) > max) {
				setValue("" + max);
			}
		} catch (NumberFormatException e) {}
	}
	
	/**
	 * Set the lowest number allowed
	 * @param min
	 */
	public void setMin(int min) {
		this.min = min;
		try {
			if (getValue().length() > 0 
					&& !getValue().equals("-")
					&& Integer.parseInt(getValue()) < min) {
				setValue("" + min);
			}
		} catch (NumberFormatException e) {}
	}
}
