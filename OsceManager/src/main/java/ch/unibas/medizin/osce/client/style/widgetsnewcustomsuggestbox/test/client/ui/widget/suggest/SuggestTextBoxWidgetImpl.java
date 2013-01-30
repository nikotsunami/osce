package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.AdvancedTextBox;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.GestureChangeHandler;
import com.google.gwt.event.dom.client.GestureEndHandler;
import com.google.gwt.event.dom.client.GestureStartHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;



public class SuggestTextBoxWidgetImpl<T, W extends EventHandlingValueHolderItem<T>>
		extends Composite implements SuggestTextBoxWidget<T, W> {
	/** the text field style name */
	private static final String SUGGEST_FIELD = "eu-nextstreet-SuggestField";
	private static final String SUGGEST_FIELD_TOP = "eu-nextstreet-SuggestField-top";
	private static final String SUGGEST_FIELD_BOTTOM = "eu-nextstreet-SuggestField-bottom";
	private static final String SUGGEST_FIELD_LEFT = "eu-nextstreet-SuggestField-left";
	private static final String SUGGEST_FIELD_RIGHT = "eu-nextstreet-SuggestField-right";
	public static int widthValue1=0;
	
	/** the main panel */
	protected DockPanel panel = new DockPanel();
	/** the main component */
	public AdvancedTextBox advancedTextBox = new AdvancedTextBox();
	public IconButton arrowButton =new IconButton();
	
	/** the suggest box that contains this widget */
	protected AbstractSuggestBox<T, W> representer;

	
	protected int buttonWidth = 16;

	/** any value change is notified to this list of listeners */
	protected ChangeEventHandlerHolder<T, ChangeEvent> valueChangeEventHandlerHolder = new ChangeEventHandlerHolder<T, ChangeEvent>() {

		@Override
		protected ChangeEvent changedValue(T param) {
			return new SuggestChangeEvent<T, W>(representer, param);
		}

	};

	/** any value change is notified to this list of listeners */
	protected ChangeEventHandlerHolder<String, ChangeEvent> textChangeEventHandlerHolder = new ChangeEventHandlerHolder<String, ChangeEvent>() {

		@Override
		protected ChangeEvent changedValue(String param) {
			return new SuggestChangeEvent<T, W>(representer, param);
		}

	};

	/** left of the text box */
	protected SimplePanel left = new SimplePanel();
	/** right of the text box */
	protected SimplePanel right = new SimplePanel();
	/** top of the text box */
	protected SimplePanel top = new SimplePanel();
	/** bottom of the text box */
	protected SimplePanel bottom = new SimplePanel();

	public SuggestTextBoxWidgetImpl() {
		initWidget(panel);
		initPanels();
		addMouseDownHandler(this);
		addMouseMoveHandler(this);
		addMouseOutHandler(this);		
		arrowButton.setVisible(false);
		/*arrowButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Log.info("arrow click");
				
			}
		});*/
		
	}




	
	@Override
	public void setValue(T value) {
		advancedTextBox.setText(representer.toString(value));
		valueChangeEventHandlerHolder.fireChangeOccured(value);
	}

	
	@Override
	public void setText(String value) {
		advancedTextBox.setText(value);
		textChangeEventHandlerHolder.fireChangeOccured(value);
	}

	private void initPanels() {
		panel.add(advancedTextBox, DockPanel.CENTER);
		panel.add(top, DockPanel.NORTH);
		top.setStyleName(SUGGEST_FIELD_TOP);
		panel.add(bottom, DockPanel.SOUTH);
		bottom.setStyleName(SUGGEST_FIELD_BOTTOM);
		advancedTextBox.setStyleName(SUGGEST_FIELD);
		//advancedTextBox.setWidth("10px");
		left.setStyleName(SUGGEST_FIELD_LEFT);
		panel.add(left, DockPanel.WEST);
		right.setStyleName(SUGGEST_FIELD_RIGHT);
		panel.add(right, DockPanel.EAST);
		arrowButton.setIcon("triangle-1-s");
		panel.add(arrowButton,DockPanel.EAST);
	}

	
	
	
	public void setLeftWidget(IsWidget left) {
		this.left.setWidget(left);
	}

	
	
	public void setRightWidget(IsWidget right) {
		this.right.setWidget(right);
	}

	
	public void setTopWidget(IsWidget top) {
		this.top.setWidget(top);
	}
	

	
	public void setBottomWidget(IsWidget bottom) {
		this.bottom.setWidget(bottom);
	}

	
	
	@Override
	public AbstractSuggestBox<T, W> getRepresenter() {
		return representer;
	}

	@Override
	public void setRepresenter(AbstractSuggestBox<T, W> abstractSuggestBox) {
		this.representer = abstractSuggestBox;
	}

	/** Sets the default text */
	@Override
	public void setDefaultText(String defaultText) {
		advancedTextBox.setDefaultText(defaultText);
	}

	/** delegates the focus to the text field */
	@Override
	public void setFocus(boolean b) {
		advancedTextBox.setFocus(b);
	}

	/** gets the text value */
	@Override
	public String getTextValue() {
		return advancedTextBox.getTextValue();
	}

	/** Enables or disables the edition */
	@Override
	public void setEnabled(boolean enabled) {
		advancedTextBox.setEnabled(enabled);
	}

	/** returns the default text */
	
	/*
	@Override
	public String getDefaultText() {
		return advancedTextBox.getDefaultText();
	}
*/
	/** delegates to the text box */
	@Override
	public void setSelectionRange(int i, int length) {
		advancedTextBox.setSelectionRange(i, length);
	}

	/*@Override
	public Validator<String> getValidator() {
		return advancedTextBox.getValidator();
	}

	@Override
	public void setValidator(Validator<String> validator) {
		advancedTextBox.setValidator(validator);
	}*/

	

	@Override
	public String getText() {
		return advancedTextBox.getText();
	}

	// -------------------------- delegate events -----------------------
	public HandlerRegistration addAttachHandler(Handler handler) {
		return advancedTextBox.addAttachHandler(handler);
	}

	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return advancedTextBox.addChangeHandler(handler);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return advancedTextBox.addValueChangeHandler(handler);
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return advancedTextBox.addDoubleClickHandler(handler);
	}

	
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return advancedTextBox.addBlurHandler(handler);
	}
	

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return advancedTextBox.addClickHandler(handler);
	}
	
	/*public HandlerRegistration addClickHandler(ClickHandler handler) {
		return arrowButton.addClickHandler(handler);
	}*/

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return advancedTextBox.addFocusHandler(handler);
	}

	public HandlerRegistration addGestureChangeHandler(
			GestureChangeHandler handler) {
		return advancedTextBox.addGestureChangeHandler(handler);
	}

	public HandlerRegistration addGestureEndHandler(GestureEndHandler handler) {
		return advancedTextBox.addGestureEndHandler(handler);
	}

	public HandlerRegistration addGestureStartHandler(GestureStartHandler handler) {
		return advancedTextBox.addGestureStartHandler(handler);
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return advancedTextBox.addKeyDownHandler(handler);
	}

	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return advancedTextBox.addKeyPressHandler(handler);
	}

	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return advancedTextBox.addKeyUpHandler(handler);
	}

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return advancedTextBox.addMouseDownHandler(handler);
	}

/*	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return arrowButton.addMouseDownHandler(handler);
	}
*/
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return advancedTextBox.addMouseMoveHandler(handler);
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return advancedTextBox.addMouseOutHandler(handler);
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return advancedTextBox.addMouseOverHandler(handler);
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return advancedTextBox.addMouseUpHandler(handler);
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return advancedTextBox.addMouseWheelHandler(handler);
	}

	
	
	// --------------------- other features ----------------------

	public int getButtonWidth() {
		return buttonWidth;
	}

	public void setButtonWidth(int buttonWidth) {
		this.buttonWidth = buttonWidth;
	}

	
	
	@UiHandler("textField")
	public void onMouseDown(MouseDownEvent event) {
			Log.info("click");
			
			
			//chane for click
	//	Window.alert("onclick");
			
		int interval = advancedTextBox.getAbsoluteLeft()
				+ advancedTextBox.getOffsetWidth() - event.getClientX();
		
		if (interval < buttonWidth) {
			if (representer.isShowingSuggestList()) {
				
				representer.hideSuggestList(false);
				
				
			} else {
				
				//representer.recomputePopupContent(KeyCodes.KEY_DOWN);
				
				representer.recomputeAllPopupContent(KeyCodes.KEY_DOWN,null);
				representer.highlightSelectedValue();
			}
			
		}
			
			/*if (representer.isShowingSuggestList()) {
				representer.hideSuggestList(false);
				
			} else {
				//representer.recomputePopupContent(KeyCodes.KEY_DOWN);
				representer.recomputeAllPopupContent(KeyCodes.KEY_DOWN,null);
				representer.highlightSelectedValue();
				
			}*/
			
			
	}
		
		
		/*@UiHandler("arrowButton")
		 public void onClick(ClickEvent event) {
        	Log.info("button click");
        	
        	
          
         }*/

	

		
	@UiHandler("textField")
	public void onMouseMove(MouseMoveEvent event) {
		int mousePosition = event.getX();
		representer.mouseOnButton(mousePosition > (advancedTextBox.getOffsetWidth() - buttonWidth));
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		representer.mouseOnButton(false);
	}

	// --------------------- layout widgets accessors -------------------------
	public DockPanel getPanel() {
		return panel;
	}

	public void setPanel(DockPanel panel) {
		this.panel = panel;
	}

	public AdvancedTextBox getAdvancedTextBox() {
		return advancedTextBox;
	}

	public void setAdvancedTextBox(AdvancedTextBox advancedTextBox) {
		this.advancedTextBox = advancedTextBox;
	}

	public SimplePanel getLeft() {
		return left;
	}

	public void setLeft(SimplePanel left) {
		this.left = left;
	}

	public SimplePanel getRight() {
		return right;
	}

	public void setRight(SimplePanel right) {
		this.right = right;
	}

	public SimplePanel getTop() {
		return top;
	}

	public void setTop(SimplePanel top) {
		this.top = top;
	}

	public SimplePanel getBottom() {
		return bottom;
	}

	public void setBottom(SimplePanel bottom) {
		this.bottom = bottom;
	}




/*
	@Override
	public void setValidator(Validator<String> validator) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public Validator<String> getValidator() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	

	// ------------------------------------------------------- end.
}
