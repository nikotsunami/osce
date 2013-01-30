package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.widgets.DisclosurePanel;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	
	public Label headerLabel = new Label();

	@UiField
	DisclosurePanel mainDisclosurePanel;
	//@UiField (provided = true)
	public CellTable<DoctorProxy> table;
	

	
	public ClinicSubViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<DoctorProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		//System.out.println("RIGHT WIDTH : " + ResolutionSettings.getRightWidgetWidth());
		mainDisclosurePanel.setWidth(countWidth() + "px");
		
		init();
		loadMainDisclosurePanel();
	}

	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}
	
	public void loadMainDisclosurePanel()
	{
		HorizontalPanel panel = new HorizontalPanel(){
			public void onBrowserEvent(Event event) {
				switch (DOM.eventGetType(event)) {
				case Event.ONCLICK:
					// Prevent link default action.
					DOM.eventPreventDefault(event);
					mainDisclosurePanel.setOpen(!mainDisclosurePanel.isOpen());
				}
			}
		};
		
		HorizontalPanel headerPanel1 = new HorizontalPanel(){
			public void onBrowserEvent(Event event) {
				switch (DOM.eventGetType(event)) {
				case Event.ONCLICK:
					// Prevent link default action.
					DOM.eventPreventDefault(event);
					mainDisclosurePanel.setOpen(!mainDisclosurePanel.isOpen());
				}
			}
		};
		
		
		headerPanel1.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		
		headerPanel1.setWidth(countWidth() + "px");
		headerPanel1.setHeight("25px");
		headerPanel1.sinkEvents(Event.ONCLICK);
		
		final HorizontalPanel iconImagePanel = new HorizontalPanel();		
		iconImagePanel.addStyleName("rightIcon");
		
		iconImagePanel.setWidth("15px");
		headerPanel1.add(iconImagePanel);
		headerLabel.addStyleName("clinicDtlHeaderLabel");
		headerPanel1.add(headerLabel);

		
		panel.add(headerPanel1);
				
		OpenHandler<DisclosurePanel> openHandler1 = new OpenHandler<DisclosurePanel>() {

			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				iconImagePanel.removeStyleName("rightIcon");
				iconImagePanel.addStyleName("downIcon");
				mainDisclosurePanel.setOpen(true);
			}
		};
		
		CloseHandler<DisclosurePanel> closeHandler1 = new CloseHandler<DisclosurePanel>() {

			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				iconImagePanel.removeStyleName("downIcon");
				iconImagePanel.addStyleName("rightIcon");
				mainDisclosurePanel.setOpen(false);
			}
		};
		
		mainDisclosurePanel.addOpenHandler(openHandler1);
		mainDisclosurePanel.addCloseHandler(closeHandler1);
		
		mainDisclosurePanel.setHeader(panel);
		
		HorizontalPanel tableContent = new HorizontalPanel();
		table.setWidth(countWidth() + "px");
		table.addStyleName("clinicDetailTable");
		tableContent.add(table);
		tableContent.setWidth(countWidth() + "px");
		mainDisclosurePanel.setContent(tableContent);
	}

	public void init() {

		// bugfix to avoid hiding of all panels (maybe there is a better lution...?!)
		//DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

		//		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		//		
		//table.addStyleName("");
		
		paths.add("name");
		TextColumn<DoctorProxy> nameCol = new TextColumn<DoctorProxy>() {

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
		};
		
		table.addColumn(nameCol, constants.doctors());
		table.setColumnWidth(nameCol, "100px");


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
				String value = "";
				
				if (object.getAssignments() != null)
				{
					Iterator<AssignmentProxy> itr = object.getAssignments().iterator();
					
					while (itr.hasNext())
					{
						AssignmentProxy assignment = itr.next();
						
						value = assignment.getOsceDay().getOsce().getName();
						
						if (!oscelist.equals(""))
						{
							if (!oscelist.contains(value))
							{
								oscelist = oscelist + ", " + value;
							}
						}
						else
						{
							oscelist = value;
						}
					}
				}
				
				return renderer.render(oscelist);
			}
		}, constants.osce());

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


	public Label getHeaderLabel() {
		return headerLabel;
	}


	public void setHeaderLabel(Label headerLabel) {
		this.headerLabel = headerLabel;
	}
	
	public int countWidth()
	{
		int width = ResolutionSettings.getRightWidgetWidth() / 2;
		width = width - 40;
		
		return width;
	}

}
