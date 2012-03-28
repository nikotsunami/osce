package ch.unibas.medizin.osce.client.style.widgets;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

public class IntegerBox extends TextBox {
	int _value;
	int _minValue = Integer.MIN_VALUE;
	int _maxValue = Integer.MAX_VALUE;
	
	public IntegerBox() {
		super();
		addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				cancelKey();
//				if ()
			}
		});
	}
	
	public void setValue(Integer value) {
		if (value > _maxValue) {
			value = _maxValue;
		} else if (value < _minValue) {
			value = _minValue;
		}
		
		_value = value;
		super.setText("" + _value);
	}
	
	public void setValue(String value) {
		if (value == null) {
			if (_minValue <= 0) {
				value = "0";
			} else {
				value = "" + _minValue;
			}
		}
		setText(value);
	}
	
	public void setText(String text) {
		try {
			int value = Integer.parseInt(text);
			setValue(value);
		} catch (NumberFormatException e) {}
	}
	
	public int getIntegerValue() {
		return _value;
	}
	
	public void setMaxValue(Integer maxValue) {
		_maxValue = maxValue;
	}
	
	public void setMinValue(Integer minValue) {
		_minValue = minValue;
	}
	
	public int getMaxValue() {
		return _maxValue;
	}
	
	public int getMinValue() {
		return _minValue;
	}
}
