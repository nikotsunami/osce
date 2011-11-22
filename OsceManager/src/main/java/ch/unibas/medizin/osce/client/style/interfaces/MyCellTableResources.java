package ch.unibas.medizin.osce.client.style.interfaces;
import com.google.gwt.user.cellview.client.CellTable;

public interface MyCellTableResources extends CellTable.Resources {
	@Source({CellTable.Style.DEFAULT_CSS, "MyCellTable.css"})
	MyCellTableStyle cellTableStyle();
}
