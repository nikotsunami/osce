package ch.unibas.medizin.osce.client.style.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.cellview.client.CellTable;

public interface AnamnesisTableResources extends CellTable.Resources {
	public interface Style extends CellTable.Style {
		String cellTableEvenYesRow();
		String cellTableOddYesRow();
		String cellTableEvenNoRow();
		String cellTableOddNoRow();
	}
	@Source({CellTable.Style.DEFAULT_CSS, "AnamnesisTable.css"})
	Style cellTableStyle();
	
	@Source("images/ui-bg_glass_75_dadada_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
	ImageResource cellTableHeaderBackground();
	
	@Source("images/ui-bg_glass_75_dadada_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
	ImageResource cellTableFooterBackground();
	
	@Source("images/ui-bg_glass_75_f1b532_1x400.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
	ImageResource cellTableSelectedBackground();
	
	@Source("images/loading.gif")
	ImageResource cellTableLoading();
}
