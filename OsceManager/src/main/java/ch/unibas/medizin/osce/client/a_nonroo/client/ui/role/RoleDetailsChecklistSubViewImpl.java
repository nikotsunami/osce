package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;





import java.util.ArrayList;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientMediaSubViewImpl;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.shared.OsMaConstant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistSubViewImpl  extends Composite 
implements RoleDetailsChecklistSubView {
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private Presenter presenter;
	
	@UiField
	SpanElement displayRenderer;
	
	@UiField(provided=true)
	CellTable<String> table;

	public CellTable<String> getTable() {
		return table;
	}

	public void setTable(CellTable<String> table) {
		this.table = table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		
		this.delegate=delegate;
	}

	public RoleDetailsChecklistSubViewImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<String>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(BINDER.createAndBindUi(this));
		
		
		table.addColumn(new TextColumn<String>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(String object) {
				return renderer.render(object);
			}
		}, "type");
		
		ArrayList<String> list=new ArrayList<String>();
		list.add("milan");
		table.setRowCount(1);
		table.setRowData(list);
		displayRenderer.setInnerText("CheckList Item 1");
	}
	
	interface Binder extends UiBinder<Widget, RoleDetailsChecklistSubViewImpl> {
	}
	
	
}
