package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

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
	}

	@UiField
    IconButton addBasicData;
    @UiField
    IconButton addScar;
    @UiField
    IconButton addAnamnesis;
    @UiField
    IconButton addLanguage;

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
	
	@UiField (provided = true)
	CellTable<AdvancedSearchCriteriaProxy> table;
	
	@UiField (provided = true)
	SimplePager pager;

	private Delegate delegate;
	

//	@UiHandler("button")
//	void onClick(ClickEvent e) {
//		Window.alert("Hello!");
//	}

	
	 public void init() {
		 
	       
	    /*    table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

	            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

	                public String render(java.lang.Long obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getId());
	            }
	        }, "Id");
	      
	        table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

	            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {

	                public String render(java.lang.Integer obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getVersion());
	            }
	        }, "Version");*/
		 
		 table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

	            Renderer<ch.unibas.medizin.osce.shared.BindType> renderer = new AbstractRenderer<ch.unibas.medizin.osce.shared.BindType>() {

	                public String render(ch.unibas.medizin.osce.shared.BindType obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getBindType());
	            }
	        }, constants.bindType());
		 
	        
	        table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

	            Renderer<ch.unibas.medizin.osce.shared.PossibleFields> renderer = new AbstractRenderer<ch.unibas.medizin.osce.shared.PossibleFields>() {

	                public String render(ch.unibas.medizin.osce.shared.PossibleFields obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getField());
	            }
	        }, constants.field());
	        
	       
	        
	        table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

	            Renderer<ch.unibas.medizin.osce.shared.Comparison2> renderer = new AbstractRenderer<ch.unibas.medizin.osce.shared.Comparison2>() {

	                public String render(ch.unibas.medizin.osce.shared.Comparison2 obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getComparation());
	            }
	        }, constants.comparison());
	        
	        table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getValue());
	            }
	        }, constants.value());
	    }


	@Override
	public CellTable<AdvancedSearchCriteriaProxy> getTable() {
		// TODO Auto-generated method stub
		return table;
	}


	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		
	}

}
