
package ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.ValueRendererFactory;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.UIObject;


public class DefaultValueRenderer<T> extends HTML implements
		EventHandlingValueHolderItem<T> {
	private static final String ITEM_HOVER = "eu-nextstreet-SuggestItemHover";
	private static final String MATCHING_STRING = "eu-nextstreet-SuggestMatchingString";
	public static final String SELECTED = "eu-nextstreet-SuggestItemSelected";
	protected T value;
	protected boolean caseSensitive;
	protected ValueRendererFactory<T, ?> valueRendererFactory;
	public static String widthValue="130px"; 
	
	//int i=0;
private Renderer<T> renderer;
	
	public void setRenderer(Renderer<T> renderer)
	{
		this.renderer = renderer;
	}
	
	
	
	public DefaultValueRenderer(T value, String filterText,
			boolean caseSensitive, ValueRendererFactory<T, ?> valueRendererFactory) {
		Log.info("Const1");
		this.value = value;
		this.caseSensitive = caseSensitive;
		fillHtml(value, filterText, caseSensitive);
		this.valueRendererFactory = valueRendererFactory;	
		//i=0;
	}
	
	public DefaultValueRenderer(T value, String filterText,
			boolean caseSensitive, ValueRendererFactory<T, ?> valueRendererFactory,Renderer<T> renderer) {
		Log.info("Const2");
		this.value = value;
		this.caseSensitive = caseSensitive;
		this.renderer=renderer;
		fillHtml(value, filterText, caseSensitive);
		this.valueRendererFactory = valueRendererFactory;			
	}
	//int[] tempLength;
	
	//int cnt=-1;
	protected void fillHtml(T value, String filterText, boolean caseSensitive) 
	{		
		String html = toString(value);		
		html = highlightMatchingSequence(html, filterText, caseSensitive);		
		setHTML(html);		
		Log.info("Get Width Value: " + widthValue);
		setWidth(widthValue);
		//Log.info("Before I Is: " + i);
		Log.info("html.length() " + html.length());				
		/*if(i<html.length())
		{
			i=html.length();
			Log.info("I Is: " + i);
			Log.info("HTML.Length Is: " + html.length());
		}
		Log.info("Maximum I Is: " + i);*/
		String s=(html.length()+20)+"px";
		
		//Log.info("Max Length Set Popup:" + s);
		//setWidth(s);
		
		//cnt++;		
		//tempLength[cnt]=html.length();
		//Log.info("tempLength " + tempLength[cnt]);
		/*setWidth("auto");*/		
		/*setWidth("130px");*/
	}
	

	
	public String toString(T value) 
	{
		//return value.toString();
		return renderer.render(value);
		
	}

	protected String highlightMatchingSequence(String html, String filterText,
			boolean caseSensitive) {
		/*return HtmlUtil.highlightMatchingSequence(html, filterText, caseSensitive,
				MATCHING_STRING);*/
		return highlightMatchingSequence(html, filterText, caseSensitive,
		MATCHING_STRING);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public void hover(boolean hover) {
		if (hover)
			addStyleName(ITEM_HOVER);
		else
			removeStyleName(ITEM_HOVER);
	}

	@Override
	public void setSelected(boolean focused) {
		if (focused)
			addStyleName(SELECTED);
		else
			removeStyleName(SELECTED);
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	@Override
	public UIObject getUiObject() {
		return this;
	}

	@Override
	public ValueRendererFactory<T, ?> getValueRendererFactory() {
		return valueRendererFactory;
	}
	
	//test 
	public  String highlightMatchingSequence(String html, String filterText, boolean caseSensitive,
			String matchingStringStyle) {
		if (caseSensitive) {
			html = html.replace(filterText, "<span class='" + matchingStringStyle + "'>" + filterText + "</span>");

		} else {
			String startSequence = "###start###";
			String endSequence = "###end###";
			String temp = html.toLowerCase()
				.replace(filterText.toLowerCase(), startSequence + filterText + endSequence);
			int firstIndex = temp.indexOf(startSequence);
			int lastIndex = temp.indexOf(endSequence) - startSequence.length();
			if (firstIndex > -1) {
				html = html.substring(0, firstIndex) + "<span class='" + matchingStringStyle + "'>"
					+ html.substring(firstIndex, lastIndex) + "</span>"
					+ html.substring(firstIndex + filterText.length());
			}

		}
		return html;
	}
	//test
}
