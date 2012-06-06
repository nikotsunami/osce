package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.ArrayList;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.TraitTypes;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;

public class StandartizedPatientAdvancedSearchSubViewImpl extends Composite
		implements StandartizedPatientAdvancedSearchSubView {

	private static StandartizedPatientAdvancedSearchSubViewImplUiBinder uiBinder = GWT
			.create(StandartizedPatientAdvancedSearchSubViewImplUiBinder.class);

	interface StandartizedPatientAdvancedSearchSubViewImplUiBinder extends
			UiBinder<Widget, StandartizedPatientAdvancedSearchSubViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	public StandartizedPatientAdvancedSearchSubViewImpl() {
		table = new CellTable<AdvancedSearchCriteriaProxy>(OsMaConstant.TABLE_PAGE_SIZE, (CellTable.Resources) GWT.create(MyCellTableResources.class));
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, (SimplePager.Resources) GWT.create(MySimplePagerResources.class), true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		addBasicData.setText(constants.basicFilter());
		addScar.setText(constants.traits());
		addAnamnesis.setText(constants.anamnesisValues());
		addLanguage.setText(constants.languages());
		addNationality.setText(constants.nationality());
	}

	@UiField
    IconButton addBasicData;
    @UiField
    IconButton addScar;
    @UiField
    IconButton addAnamnesis;
    @UiField
    IconButton addLanguage;
    @UiField
    IconButton addNationality;

	@UiHandler("addBasicData")
	void onAddBasicDataClick(ClickEvent e) {
		delegate.addBasicCriteriaClicked(addBasicData);
	}
	
	@UiHandler("addScar")
	void onAddScarClick(ClickEvent e) {
		delegate.addScarCriteriaClicked(addScar);
	}
	
	@UiHandler("addAnamnesis")
	void onAddAnamnesisClick(ClickEvent e) {
		delegate.addAnamnesisCriteriaClicked(addAnamnesis);
	}
	
	@UiHandler("addLanguage")
	void onAddLanguageClick(ClickEvent e) {
		delegate.addLanguageCriteriaClicked(addLanguage);
	}
	
	@UiHandler("addNationality")
	void onAddNationalityClick(ClickEvent e) {
		delegate.addNationalityCriteriaClicked(addNationality);
	}
	
	@UiField (provided = true)
	CellTable<AdvancedSearchCriteriaProxy> table;
	
	@UiField (provided = true)
	SimplePager pager;

	private Delegate delegate;
	
	//Assignment : F [
	protected ArrayList<String> paths = new ArrayList<String>();
	StandardizedRoleProxy standardizedRoleProxy;
	//]Assignment : F
	
	 public void init() {
		 table.addColumn(new Column<AdvancedSearchCriteriaProxy, SafeHtml>(new SafeHtmlCell()) {
			 @Override
			 public SafeHtml getValue(AdvancedSearchCriteriaProxy criterion) {
				 switch (criterion.getField()) {
				 case NATIONALITY:
					 return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-flag\"></span>").toSafeHtml();
				 case LANGUAGE:
					 return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-comment\"></span>").toSafeHtml();
				 case ANAMNESIS:
					 return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-pencil\"></span>").toSafeHtml();
				 case SCAR:
					 return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-search\"></span>").toSafeHtml();
				 default:
					 return new SafeHtmlBuilder().appendHtmlConstant("<span class=\"ui-icon ui-icon-wrench\"></span>").toSafeHtml();
				 }
			 }			 
		 });
		 
		 table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {
			 Renderer<BindType> renderer = new EnumRenderer<BindType>();

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getBindType());
	            }
	        }, constants.bindType());

		 table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

			 public String getValue(AdvancedSearchCriteriaProxy criterion) {
				 return criterion.getShownValue();
			 }
		 }, constants.criterion());
	        
	        ActionCell.Delegate<AdvancedSearchCriteriaProxy> deleteDelegate = new ActionCell.Delegate<AdvancedSearchCriteriaProxy>() {
				@Override
				public void execute(AdvancedSearchCriteriaProxy object) {
					delegate.deleteAdvancedSearchCriteria(object);
				}
			};
	        
	        table.addColumn(new IdentityColumn<AdvancedSearchCriteriaProxy>(new ActionCell<AdvancedSearchCriteriaProxy>(OsMaConstant.DELETE_ICON, deleteDelegate)));
			table.addColumnStyleName(table.getColumnCount() - 1, "iconCol");
			
			table.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {
				
				@Override
				public void onRowCountChange(RowCountChangeEvent event) {
					if (event.getNewRowCount() > 0) {
						table.setVisible(true);
						pager.setVisible(true);
					} else {
						table.setVisible(false);
						pager.setVisible(false);
					}
				}
			});
			
			table.setVisible(false);
			pager.setVisible(false);
	    }

	@Override
	public CellTable<AdvancedSearchCriteriaProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}
	
	//Assignment : F[
	@Override
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}
	
	@Override
	public void setValue(StandardizedRoleProxy standardizedRoleProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
	}
	
	public StandardizedRoleProxy getStRoleProxy() {
		return this.standardizedRoleProxy;
	}
//	]Assignment : F 

}
