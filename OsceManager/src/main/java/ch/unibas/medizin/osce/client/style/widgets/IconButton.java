package ch.unibas.medizin.osce.client.style.widgets;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;

public class IconButton extends Button {
	private static final String ICON_HTML_OPEN = "<span class=\"ui-icon ui-icon-";
	private static final String ICON_HTML_CLOSE = "\"></span>";
	private String icon = "bullet";
	private String text = "";
	
	public IconButton() {
		super();
	}
	
	public IconButton(String html) {
		super(html);
	}
	
	public IconButton(String html, ClickListener listener) {
		super(html, listener);
	}
	
	public void setIcon(String iconName) {
		this.icon = iconName;
		text = getText();
		construct();
	}
	
	public void setText(String text) {
		this.text = text;
		construct();
	}
	
	private void construct() {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant(ICON_HTML_OPEN + icon + ICON_HTML_CLOSE + text);
		setHTML(builder.toSafeHtml());
	}
}
