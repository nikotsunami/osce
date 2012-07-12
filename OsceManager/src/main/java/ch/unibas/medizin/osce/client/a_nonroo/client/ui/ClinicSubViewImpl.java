package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ClinicSubViewImpl extends Composite implements ClinicSubView {

	private static ClinicSubViewImplUiBinder uiBinder = GWT
			.create(ClinicSubViewImplUiBinder.class);

	interface ClinicSubViewImplUiBinder extends
			UiBinder<Widget, ClinicSubViewImpl> {
	}
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	private ClinicSubView clinicSubView;
	
	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	
	@UiField (provided = true)
	CellTable<DoctorProxy> table;
	

	
	public ClinicSubViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<DoctorProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}
public void init() {
		
		// bugfix to avoid hiding of all panels (maybe there is a better lution...?!)
		//DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
//		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
//		
		paths.add("name");
		table.addColumn(new TextColumn<DoctorProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(DoctorProxy object) {
			//	Log.info("get value here "+ object.getStudent().getName());
				return renderer.render(object.getName());
			
			}
		}, "DoctorName");
		
		
		paths.add("Prename");
		
		table.addColumn(new TextColumn<DoctorProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(DoctorProxy object) {
				String oscelist="";
				Iterator<OscePostBlueprintProxy> iteratorOscePostBlueprint = null;
				if(object.getSpecialisation()!=null && object.getSpecialisation().getOscePostBlueprint()!= null){
				
			iteratorOscePostBlueprint=object.getSpecialisation().getOscePostBlueprint().iterator();
			
				while(iteratorOscePostBlueprint.hasNext())
				{
					OscePostBlueprintProxy proxy=iteratorOscePostBlueprint.next();
					if(object.getSpecialisation().getOscePostBlueprint().size()==1)
					{
						oscelist=proxy.getOsce().getName();
					}
					else if(!iteratorOscePostBlueprint.hasNext())
					{
						oscelist=proxy.getOsce().getName();
					}
					else
						oscelist=proxy.getOsce().getName()+",";
				}
				}
				return renderer.render(oscelist);
			}
		}, "OsceName");
		
}
private <C> void addColumn(Cell<C> cell, String headerText,
		final GetValue<C> getter, FieldUpdater<DoctorProxy, C> fieldUpdater) {
	Column<DoctorProxy, C> column = new Column<DoctorProxy, C>(cell) {
		@Override
		public C getValue(DoctorProxy object) {
			return getter.getValue(object);
		}
	};
	column.setFieldUpdater(fieldUpdater);
	if (cell instanceof AbstractEditableCell<?, ?>) {
		editableCells.add((AbstractEditableCell<?, ?>) cell);
	}
	table.addColumn(column, headerText);
}

/**
 * Get a cell value from a record.
 *
 * @param <C> the cell type
 */
private static interface GetValue<C> {
	C getValue(DoctorProxy contact);
}

private List<AbstractEditableCell<?, ?>> editableCells;

@Override
public CellTable<DoctorProxy> getTable() {
	return table;
}

@Override
public void setDelegate(Delegate delegate) {
	this.delegate = delegate;
}


@Override
public void setPresenter(Presenter presenter) {
	this.presenter = presenter;
}

}
