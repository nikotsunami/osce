package ch.unibas.medizin.osce.client.style.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;

public class NumberSpinner extends Composite {

	private Integer RATE = 1;
	private IntegerBox integerBox;
	
	public NumberSpinner()
	{
		this(0);
	}
	
	public NumberSpinner(Integer defaultValue)
	{
		AbsolutePanel absolutePanel = new AbsolutePanel();
		initWidget(absolutePanel);
		absolutePanel.setSize("55px", "25px");
		
		integerBox = new IntegerBox();
		absolutePanel.add(integerBox, 0, 0);
		integerBox.setSize("30px", "22px");
		integerBox.setValue(defaultValue);
		
		
		
		Button upButton = new Button();
		//upButton.setIcon("ui-icon ui-icon-triangle-1-n");
		upButton.setHTML("<span class=\"numberSpinnerUpIcon\"></span>");
		
		upButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setValue(getValue() + RATE);
			}
		});
		
		absolutePanel.add(upButton, 35, 0);
		upButton.setSize("15px", "12px");
		
		Button downButton = new Button();
		//downButton.setIcon("ui-icon ui-icon-triangle-1-s");
		downButton.setHTML("<span class=\"numberSpinnerDownIcon\"></span>");
				
		downButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				/*if (getValue() == 0)
					return;*/
				
				setValue(getValue() - RATE);							
			}
		});
		
		absolutePanel.add(downButton, 35, 12);
		downButton.setSize("15px", "12px");
	}
	
	public Integer getValue()
	{
		return integerBox.getValue() == null ? 0 : (isNumber(integerBox.getValue().toString()) ? integerBox.getValue() : 0);
	}
	
	public void setValue(Integer defaultValue)
	{
		integerBox.setValue(defaultValue);
	}
	
	public void setRate(Integer rate)
	{
		this.RATE = rate;
	}
	
	public static boolean isNumber(String value) {
		
	  try  
	  {  
	    Integer.parseInt(value);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  catch (Exception e) {
		  return false;
	  }
	  
	  return true;  
	}
}
