package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandartizedPatientAdvancedSearchSubView.Delegate;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class StandartizedPatientAdvancedSearchSubViewImpl extends Composite
		implements StandartizedPatientAdvancedSearchSubView {

	private static StandartizedPatientAdvancedSearchSubViewImplUiBinder uiBinder = GWT
			.create(StandartizedPatientAdvancedSearchSubViewImplUiBinder.class);

	interface StandartizedPatientAdvancedSearchSubViewImplUiBinder extends
			UiBinder<Widget, StandartizedPatientAdvancedSearchSubViewImpl> {
	}

	public StandartizedPatientAdvancedSearchSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}


	
	@UiField
	CellTable<AdvancedSearchCriteriaProxy> table;
	
//	@UiField (provided = true)
//	SimplePager pager;

	private Delegate delegate;
	

//	@UiHandler("button")
//	void onClick(ClickEvent e) {
//		Window.alert("Hello!");
//	}

	
	 public void init() {
	       
	        table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

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
	        }, "Version");
	        
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
	        }, "Field");
	        
	        table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getBindType());
	            }
	        }, "Bind Type");
	        
	        table.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

	            Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

	                public String render(java.lang.String obj) {
	                    return obj == null ? "" : String.valueOf(obj);
	                }
	            };

	            @Override
	            public String getValue(AdvancedSearchCriteriaProxy object) {
	                return renderer.render(object.getComparation());
	            }
	        }, "Comparation");
	        
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
	        }, "Value");
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
