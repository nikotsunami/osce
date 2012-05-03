package ch.unibas.medizin.osce.client.style.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface UiStyles extends ClientBundle {
	
	public interface UiCss extends CssResource {
		public String backgroundActive();
		public String backgroundRegular();
		public String backgroundHilight();
		public String alignRight();
		public String horizontalSpacing();
	}

	@Source("UiStyles.css")
	public UiCss uiCss();
	
	@Source("images/ui-bg_glass_65_d3ae50_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	public ImageResource backgroundActive();
	
	@Source("images/ui-bg_glass_75_dadada_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	public ImageResource backgroundRegular();
	
	@Source("images/ui-bg_glass_75_f1b532_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	public ImageResource backgroundHilight();
}
