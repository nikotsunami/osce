package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.LearningObjectiveData;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;

public class PaymentViewImpl extends Composite implements PaymentView, RecordChangeHandler, MenuClickHandler {

	private static PaymentViewImplUiBinder uiBinder = GWT
			.create(PaymentViewImplUiBinder.class);

	interface PaymentViewImplUiBinder extends UiBinder<Widget, PaymentViewImpl> {
	}
	
	@UiField
	IconButton exportButton;
	
	@UiField
	IconButton printButton;
	
	@UiField
	SplitLayoutPanel splitLayoutPanel;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public Delegate delegate;
	
	public Presenter presenter;
	
	@UiField (provided = true)
    SimplePager pager;
    
    @UiField (provided = true)
    CellTable<StandardizedPatientProxy> table;
    
    protected Set<String> paths = new HashSet<String>();
    
    public MultiSelectionModel<StandardizedPatientProxy> multiselectionModel;

	public PaymentViewImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<StandardizedPatientProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		exportButton.setText(constants.export());
		printButton.setText(constants.print());
		
		init();
		
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
	}
	
	public void init()
	{
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
		
		ProvidesKey<StandardizedPatientProxy> keyProvider1 = ((AbstractHasData<StandardizedPatientProxy>) table).getKeyProvider();
		multiselectionModel=new MultiSelectionModel<StandardizedPatientProxy>(keyProvider1);
	
		Column<StandardizedPatientProxy, Boolean> checkColumn = new Column<StandardizedPatientProxy, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(StandardizedPatientProxy object) {
				// Get the value from the selection model.
				return multiselectionModel.isSelected(object);
				}
		};
		
		table.setSelectionModel(multiselectionModel,DefaultSelectionEventManager.<StandardizedPatientProxy> createCheckboxManager());
		
		CheckboxCell checkboxCell = new CheckboxCell(true, false);
		
		Header<Boolean> hdr = new Header<Boolean>(checkboxCell) {
			@Override
			public Boolean getValue() {
				for (StandardizedPatientProxy stdPat : table.getVisibleItems())
				{
					if (!table.getSelectionModel().isSelected(stdPat))
					{
						return false;
					}
				}
				
				return table.getVisibleItems().size() > 0;
			}
		};
		
		hdr.setUpdater(new ValueUpdater<Boolean>() {
			
			@Override
			public void update(Boolean value) {
				for (StandardizedPatientProxy stdPat : table.getVisibleItems())
				{
					table.getSelectionModel().setSelected(stdPat, value);
				}
			}
		});
				
		table.addColumn(checkColumn, hdr);
		
		table.setColumnWidth(checkColumn, 40, Unit.PX);
		
		paths.add("name");
		table.addColumn(new TextColumn<StandardizedPatientProxy>() {
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StandardizedPatientProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.name());
		
		paths.add("prename");
		table.addColumn(new TextColumn<StandardizedPatientProxy>() {
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StandardizedPatientProxy object) {
				return renderer.render(object.getPreName());
			}
		}, constants.preName());
	}

	@UiHandler("exportButton")
	void onClick(ClickEvent e) {
		delegate.exportButtonClicked();
	}
	
	
	@UiHandler("printButton")
	void onPrintButtonClick(ClickEvent e) {
		delegate.printButtonClicked();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter systemStartActivity) {
		this.presenter = systemStartActivity;
	}

	public CellTable<StandardizedPatientProxy> getTable() {
		return table;
	}

	public void setTable(CellTable<StandardizedPatientProxy> table) {
		this.table = table;
	}

	public Set<String> getPaths() {
		return paths;
	}

	public void setPaths(Set<String> paths) {
		this.paths = paths;
	}

	public MultiSelectionModel<StandardizedPatientProxy> getMultiselectionModel() {
		return multiselectionModel;
	}

	public void setMultiselectionModel(
			MultiSelectionModel<StandardizedPatientProxy> multiselectionModel) {
		this.multiselectionModel = multiselectionModel;
	}

	@Override
	public void onRecordChange(RecordChangeEvent event) {
		int pagesize = 0;

		if (event.getRecordValue() == "ALL") {
			pagesize = table.getRowCount();
			OsMaConstant.TABLE_PAGE_SIZE = pagesize;
		} else {
			pagesize = Integer.parseInt(event.getRecordValue());
		}

		table.setPageSize(pagesize);
	}

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		OsMaMainNav.setMenuStatus(event.getMenuStatus());		
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,false);
	}	
}
