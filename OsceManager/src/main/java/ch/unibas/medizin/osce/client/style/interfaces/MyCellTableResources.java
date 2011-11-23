package ch.unibas.medizin.osce.client.style.interfaces;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.cellview.client.CellTable;

public interface MyCellTableResources extends CellTable.Resources {
	@Source({CellTable.Style.DEFAULT_CSS, "MyCellTable.css"})
	CellTable.Style cellTableStyle();
	
	@Source("images/ui-bg_glass_75_dadada_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
	ImageResource cellTableHeaderBackground();
	
	@Source("images/ui-bg_glass_75_dadada_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
	ImageResource cellTableFooterBackground();
	
	@Source("images/ui-bg_glass_75_f1b532_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
	ImageResource cellTableSelectedBackground();
}
