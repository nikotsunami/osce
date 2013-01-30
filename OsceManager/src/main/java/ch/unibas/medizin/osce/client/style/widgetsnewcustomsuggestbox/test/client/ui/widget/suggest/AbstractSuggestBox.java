
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.ValueRendererFactory.ListRenderer;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestionPopup;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultValueRenderer;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultValueRendererFactory;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.param.Option;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;



public abstract class AbstractSuggestBox<T, W extends EventHandlingValueHolderItem<T>> extends
		ChangeEventHandlerHolder<Boolean, SuggestChangeEvent<T, W>>  {

	private static final String						SUGGEST_FIELD_COMP		= "eu-nextstreet-SuggestFieldComp";
	private static final String						SUGGEST_FIELD					= "eu-nextstreet-SuggestFieldDetail";
	
	private static final String						SUGGEST_FIELD_HOVER		= "eu-nextstreet-SuggestFieldHover";
	private static final String						SUGGEST_BOX_LOADING		= "eu-nextstreet-AdvancedTextBoxDefaultText-loading";


	// private static SuggestBoxUiBinder uiBinder = GWT
	// .create(SuggestBoxUiBinder.class);
	protected T	selected;
	protected String			typed;
	protected Map<String, Option<?>>			options								= new HashMap<String, Option<?>>();
	public SuggestWidget<T>						suggestWidget					= new DefaultSuggestionPopup<T>();
	public ScrollPanel									scrollPanel						= new ScrollPanel();
	protected ListRenderer<T, W>					listRenderer;
	protected boolean											strictMode;
	//protected StringFormulator<T>					stringFormulator			= new DefaultStringFormulator<T>();
	protected DefaultStringFormulator<T>					stringFormulator			= new DefaultStringFormulator<T>();
	protected int													selectedIndex					= -1;

	private boolean												recomputePopupContent	= true;
	/**
	 * Specifies if enter is hit multiple times with same value, whether it
	 * generates a change event for each
	 */
	private boolean												multipleChangeEvent;
	private boolean												fireChangeOnBlur;

	protected ValueRendererFactory<T, W>	valueRendererFactory;
	protected List<T>											currentPossibilities;

	// @SuppressWarnings("rawtypes")
	// interface SuggestBoxUiBinder extends UiBinder<Widget, AbstractSuggestBox> {
	// }
	
	
	public class DefaultStringFormulator<T> {
		
		
		public String toString(T t) {
			return t.toString();
		}
		
		

	}
	
	
	private Renderer<T> renderer;
	
	public void setRenderer(Renderer<T> renderer)
	{
		this.renderer = renderer;
	}

	public AbstractSuggestBox() {
		this(null);
	}

	public AbstractSuggestBox(String defaultText) {
		
	}

	/**
	 * This method must be called in the implementation's constructor
	 * 
	 * @param defaultText
	 *          the defalt text. Can be null
	 */
	protected void init(String defaultText) {
		//setStyleName(SUGGEST_FIELD_COMP);
		Log.info("Call Set Style");
		addStyleName(SUGGEST_FIELD_COMP);
		getTextField().setRepresenter(this);
		//getTextField().setStyleName(SUGGEST_FIELD);
		getTextField().setDefaultText(defaultText);
		 scrollPanel.setSize("125px", "150px"); 
		suggestWidget.setWidget(scrollPanel);
		
		//suggestWidget.addStyleName("style.standardTable");
		
		setValueRendererFactory(new DefaultValueRendererFactory<T, W>());
	}

	

	
	// @UiHandler("textField")
	public void onDoubleClick(DoubleClickEvent event) {
		doubleClicked(event);
	}

	/**
	 * Default blur event handling
	 * 
	 * @param event
	 */
	// @UiHandler("textField")
	public void onBlur(BlurEvent event) {
		new Timer() {
			@Override
			public void run() {
				String currentText = getText();
				if (typed == null || !typed.equals(currentText)) {
					if (strictMode) {
						setText("");
						valueTyped("");
					} else {
						valueTyped(currentText);
					}
				} else if (fireChangeOnBlur) {
					valueSelected(selected);
					fireChangeOnBlur = false;
				}
				if (currentText.trim().length() == 0)
					setText("");
			}
		}.schedule(200);
		if (isShowingSuggestList()) {
			new Timer() {
				@Override
				public void run() {
					hideSuggestList(false);
				}
			}.schedule(300);
		}
	}

	/**
	 * Default keyboard events handling
	 * 
	 * @param keyUpEvent
	 */
	// @UiHandler("textField")
	public void onKeyUp(KeyUpEvent keyUpEvent) {
		final int keyCode = keyUpEvent.getNativeKeyCode();

		if (keyCode == KeyCodes.KEY_TAB || keyCode == KeyCodes.KEY_ALT || keyCode == KeyCodes.KEY_CTRL || keyCode == KeyCodes.KEY_SHIFT
				|| keyCode == KeyCodes.KEY_HOME || keyCode == KeyCodes.KEY_END) {
			return;
		} else if (keyCode == KeyCodes.KEY_UP || keyCode == KeyCodes.KEY_DOWN || keyCode == KeyCodes.KEY_LEFT || keyCode == KeyCodes.KEY_RIGHT) {
			// don't recompute if only navigating
			handleKeyNavigation(keyCode);
			recomputePopupContent = false;
		} else if (keyCode == KeyCodes.KEY_DELETE || keyCode == KeyCodes.KEY_BACKSPACE) {
			recomputePopupContent = true;

		} else if (keyCode == KeyCodes.KEY_DELETE || keyCode == KeyCodes.KEY_BACKSPACE) {
			selectedIndex = -1;
		} else {
			recomputePopupContent = true;
		}
		if (recomputePopupContent) {
			SuggestPossibilitiesCallBack<T> callBack = new SuggestPossibilitiesCallBack<T>() {

				@Override
				public void setPossibilities(List<T> possibilities) {
					// the value was already set by the previous handler
					if (possibilities.size() == 1)
						return;

					EventHandlingValueHolderItem<T> popupWidget = getItemAt(selectedIndex);
					if (popupWidget != null && selectedIndex != -1)
						popupWidget.setSelected(false);
					int widgetCount = listRenderer.getWidgetCount();

					if (widgetCount == 0)
						return;

					if (keyCode == KeyCodes.KEY_ENTER) {
						if (isShowingSuggestList()) {
							if (popupWidget != null) {
								T t = popupWidget.getValue();
								fillValue(t, true);
							} else {
								// value not in list, this enter means OK.
								valueTyped(getText());
							}
							hideSuggestList();
						} else {
							if (multipleChangeEvent) {
								// popup is not visible, this enter means OK (check if the
								// value was entered from the list or from text).
								if (selected == null)
									valueTyped(getText());
								else
									fillValue(selected, multipleChangeEvent);
							}
						}
					} else if (keyCode == KeyCodes.KEY_ESCAPE) {
						hideSuggestList();
					} else if (keyCode == KeyCodes.KEY_TAB) {

					} else {
						recomputePopupContent = true;
						if (strictMode) {
							final StringBuffer reducingText = new StringBuffer(getText());
							recomputePopupContent(keyCode, new SuggestPossibilitiesCallBack<T>() {

								@Override
								public void setPossibilities(List<T> possibilities) {
									if (reducingText.length() > 1 && possibilities.size() < 1) {
										// FIXME should optimize by remembering the last valid entry
										// (that has at least one possibility)
										reducingText.setLength(reducingText.length() - 1);
										setText(reducingText.toString());
										recomputePopupContent(keyCode, this);
									}
								}
							});
						}
					}
				}
			};
			recomputePopupContent(keyCode, callBack);
		}

	}

	
	protected void handleKeyNavigation(int keyCode) {
		if (currentPossibilities == null || !isShowingSuggestList()) {
			recomputePopupContent(keyCode);
		} else {
			int widgetCount = currentPossibilities.size();
			if (widgetCount == 0)
				return;
			int newSelectedIndex;
			if (keyCode == KeyCodes.KEY_DOWN) {
				newSelectedIndex = (selectedIndex + 1) % widgetCount;
				highlightSelectedValue(selectedIndex, newSelectedIndex);
			} else if (keyCode == KeyCodes.KEY_UP) {
				newSelectedIndex = (selectedIndex - 1) % widgetCount;
				if (newSelectedIndex < 0)
					newSelectedIndex += widgetCount;

				highlightSelectedValue(selectedIndex, newSelectedIndex);
			}
		}
	}

	
	protected boolean isShowingSuggestList() {
		return suggestWidget.isShowing();
	}

	
	protected void highlightSelectedValue(int oldSelectedIndex, int newSelectedIndex) {
		if (oldSelectedIndex != -1) {
			EventHandlingValueHolderItem<T> oldPopupWidget = getItemAt(oldSelectedIndex);
			oldPopupWidget.setSelected(false);
		}
		EventHandlingValueHolderItem<T> newPopupWidget = getItemAt(newSelectedIndex);

		if (newPopupWidget != null) {
			newPopupWidget.setSelected(true);
			UIObject uiObject = newPopupWidget.getUiObject();
			assert (uiObject != null);
			scrollPanel.ensureVisible(uiObject);
		}
		selectedIndex = newSelectedIndex;
	}

	
	protected void hideSuggestList(boolean resteSelectedIndex) {
		suggestWidget.hide();
		if (resteSelectedIndex)
			selectedIndex = -1;
	}

	private void hideSuggestList() {
		hideSuggestList(true);
	}

	
	protected void recomputePopupContent(final int keyCode) {
		recomputePopupContent(keyCode, null);
	}

	protected void recomputePopupContent(final int keyCode, final SuggestPossibilitiesCallBack<T> callBack) {
		
		//if (isReadOnly())
		//	return;

		String textValue = getText();
		SuggestPossibilitiesCallBack<T> suggestPossibilitiesCallBack = new SuggestPossibilitiesCallBack<T>() {

			@Override
			public void setPossibilities(List<T> possibilities) {
				getTextField().removeStyleName(SUGGEST_BOX_LOADING);

				AbstractSuggestBox.this.setPossibilities(keyCode, possibilities);
				if (callBack != null)
					callBack.setPossibilities(possibilities);
			}
		};
		getTextField().addStyleName(SUGGEST_BOX_LOADING);

		computeFiltredPossibilities(textValue, suggestPossibilitiesCallBack);
	}
	
	
	protected void recomputeAllPopupContent(final int keyCode,  final SuggestPossibilitiesCallBack<T> callBack) {
		
		//if (isReadOnly())
		//	return;

		String textValue = "";
		SuggestPossibilitiesCallBack<T> suggestPossibilitiesCallBack = new SuggestPossibilitiesCallBack<T>() {

			@Override
			public void setPossibilities(List<T> possibilities) {
				getTextField().removeStyleName(SUGGEST_BOX_LOADING);

				AbstractSuggestBox.this.setPossibilities(keyCode, possibilities);
				if (callBack != null)
					callBack.setPossibilities(possibilities);
			}
		};
		getTextField().addStyleName(SUGGEST_BOX_LOADING);

		computeFiltredPossibilities(textValue, suggestPossibilitiesCallBack);
		
	}

	protected void setPossibilities(int keyCode, List<T> possibilities) {
		this.currentPossibilities = possibilities;
		if (possibilities.size() > 0) {
			listRenderer.clear();
			if (possibilities.size() == 1) {
				// laisse l'utilisateur effacer les valeurs
				if (keyCode != KeyCodes.KEY_BACKSPACE && keyCode != KeyCodes.KEY_LEFT && keyCode != KeyCodes.KEY_RIGHT) {
					fillValue(possibilities.get(0), false);
				}
			}

			constructPopupContent(possibilities);

			showSuggestList();
		} else {
			hideSuggestList();
		}
	}

	protected void showSuggestList() {
		suggestWidget.adjustPosition(getTextField().getAbsoluteLeft(), getTextField().getAbsoluteTop() + getTextField().getOffsetHeight());
		highlightSelectedValue(-1, selectedIndex);
		suggestWidget.show();
	}

	protected void constructPopupContent(List<T> possibilities) {
		int size = possibilities.size();
		for (int i = 0; i < size; i++) {
			final T t = possibilities.get(i);
			String currentText = getText();
			if (checkSelected(t, currentText))
				selectedIndex = i;

			final W currentLabel = createValueRenderer(t, currentText);
			((DefaultValueRenderer<T>)currentLabel).setWidth(rendererWidth);			
			
			listRenderer.add(currentLabel);
			currentLabel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent clickEvent) {
					itemClicked(t);
				}
			});

			class MouseHoverhandler implements MouseOverHandler, MouseOutHandler {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					currentLabel.hover(true);
				}

				@Override
				public void onMouseOut(MouseOutEvent event) {
					currentLabel.hover(false);
				}
			}

			MouseHoverhandler hoverhandler = new MouseHoverhandler();
			currentLabel.addMouseOverHandler(hoverhandler);
			currentLabel.addMouseOutHandler(hoverhandler);
		}
		//System.out.println("Assigning width " );
		//setRendererWidth("250px");
	}

	String rendererWidth = "200px";
	public void setRendererWidth(String width)
	{
		rendererWidth = width;
		/*System.out.println("setRendererWidth : " + width);
		if(listRenderer != null)
		{
			System.out.println("Renderer is not null : " + listRenderer.getWidgetCount() );
			int count = listRenderer.getWidgetCount();
			for(int index=0;index<count;index++)
			{
				W w = listRenderer.getAt(index);
				((DefaultValueRenderer<T>)w).setWidth(width);
			}
		}*/
		
	}

	protected boolean checkSelected(final T item, String currentText) {
		String value = toString(item);
		boolean found = value.equals(currentText);
		return found;
	}

	private W createValueRenderer(final T t, String value) {
	//	System.out.println("sankit : " + value);
		//final W currentLabel = valueRendererFactory.createValueRenderer(t, value, getOptions());
		final W currentLabel = valueRendererFactory.createValueRenderer(t, value, getOptions(),renderer);
		
		return currentLabel;				
	}

	/**
	 * returns the set options
	 * 
	 * @return the set options
	 */
	public Map<String, Option<?>> getOptions() {
		return options;
	}

	
	public String toString(final T t) {
		//return stringFormulator.toString(t);
		return renderer.render(t);
	}

	private EventHandlingValueHolderItem<T> getItemAt(int index) {
		if (index != -1 && listRenderer.getWidgetCount() > index)
			return (EventHandlingValueHolderItem<T>) listRenderer.getAt(index);
		return null;
	}

	
	protected boolean fillValue(final T t, boolean commit) {
		getTextField().setText(toString(t));
		hideSuggestList();
		getTextField().setFocus(true);
		selected = t;
		typed = toString(t);
		if (commit) {
			valueSelected(t);
		} else {
			// the change is not notified immediately since we just suggest the
			// value t. We keep a flag to know there was no notification
			fireChangeOnBlur = true;
		}
		return true;
	}

	
	public void valueSelected(T value) {
		fireChangeOccured(true);
	}

	
	public void valueTyped(String value) {
		selected = null;
		// if (defautText != null && defautText.equals(value))
		// value = "";
		typed = value;
		fireChangeOccured(false);
	}

	
	public T getSelected() {
		return selected;
	}

	
	public String getTyped() {
		return typed;
	}

	@Override
	protected SuggestChangeEvent<T, W> changedValue(Boolean selected) {
		if (selected)
			return new SuggestChangeEvent<T, W>(this, getSelected());
		return new SuggestChangeEvent<T, W>(this, getText());
	}

	
	public String getText() {
		return getTextField().getTextValue();
	}

	public void setText(String str) {
		getTextField().setText(str);
		typed = str;
	}

	
	public void computeSelected(String text) {
		computeFiltredPossibilities(text, new SuggestPossibilitiesCallBack<T>() {

			@Override
			public void setPossibilities(List<T> possibilities) {
				if (possibilities.size() == 1) {
					setSelectedValue(possibilities.get(0));
				} else {
					setSelectedValue(null);
				}
			}
		});
	}

	protected void setSelectedValue(T selected) {
		this.selected = selected;
	}

	public boolean isEmpty() {
		return getText().length() == 0;
	}

	public void setEnabled(boolean enabled) {
		getTextField().setEnabled(enabled);
	}

	public void setFocus(boolean focus) {
		getTextField().setFocus(focus);
	}

	protected abstract void computeFiltredPossibilities(String text, SuggestPossibilitiesCallBack<T> suggestPossibilitiesCallBack);

	
	public ValueRendererFactory<T, ? extends EventHandlingValueHolderItem<T>> getValueRendererFactory() {
		return valueRendererFactory;
	}

	
	public void setValueRendererFactory(ValueRendererFactory<T, W> valueRendererFactory) {
		this.valueRendererFactory = valueRendererFactory;
		if (listRenderer != null) {
			listRenderer.clear();
			scrollPanel.clear();
		}
		listRenderer = valueRendererFactory.createListRenderer();
		scrollPanel.add((Widget) listRenderer);		
		System.out.println();
	}

	public SuggestWidget<T> getSuggestWidget() {
		return suggestWidget;
	}

	
	public void setSuggestWidget(SuggestWidget<T> suggestWidget) {
		this.suggestWidget = suggestWidget;
	}

	public Object getOption(String key) {
		return options.get(key);
	}

	public Option<?> putOption(Option<?> option) {
		return options.put(option.getKey(), option);
	}

	public Option<?> removeOption(String key) {
		return options.remove(key);
	}

	public Option<?> removeOption(Option<?> option) {
		return removeOption(option.getKey());
	}

	
	public boolean isMultipleChangeEvent() {
		return multipleChangeEvent;
	}

	public void setMultipleChangeEvent(boolean multipleChangeEvent) {
		this.multipleChangeEvent = multipleChangeEvent;
	}

	public boolean isStrictMode() {
		return strictMode;
	}

	public void setStrictMode(boolean strictMode) {
		this.strictMode = strictMode;
	}

	/*
	public String getDefaultText() {
		return getTextField().getDefaultText();
	}
*/
	public void setDefaultText(String text) {
		this.getTextField().setDefaultText(text);
	}

	
	
	protected void mouseOnButton(boolean onButton) {
		if (onButton /*&& !isReadOnly()*/)
			getTextField().addStyleName(SUGGEST_FIELD_HOVER);
		else
			getTextField().removeStyleName(SUGGEST_FIELD_HOVER);
	}

	
	protected void doubleClicked(DoubleClickEvent event) {
		this.getTextField().setSelectionRange(0, getText().length());
		recomputePopupContent(KeyCodes.KEY_RIGHT);
	}

	
	protected void itemClicked(T t) {
		fillValue(t, true);
	}

	/*public Validator<String> getValidator() {
		return getTextField().getValidator();
	}

	public void setValidator(Validator<String> validator) {
		getTextField().setValidator(validator);
	}
*/
	
	public void setSelected(T selected) {
		this.selected = selected;
		setText(toString(selected));
	}

	public abstract SuggestTextBoxWidget<T, W> getTextField();

	public void highlightSelectedValue() {
		highlightSelectedValue(-1, selectedIndex);
	}

	/*public StringFormulator<T> getStringFormulator() {
		return stringFormulator;
	}
*/
	public DefaultStringFormulator<T> getStringFormulator() {
		return stringFormulator;
	}
	
	/*public void setStringFormulator(StringFormulator<T> stringFormulator) {
		this.stringFormulator = stringFormulator;
	}*/

	public void setStringFormulator(DefaultStringFormulator<T> stringFormulator) {
		this.stringFormulator = stringFormulator;
	}
}
