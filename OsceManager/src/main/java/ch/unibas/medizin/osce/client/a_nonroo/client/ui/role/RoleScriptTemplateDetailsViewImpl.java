package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ItemDefination;
import ch.unibas.medizin.osce.shared.OsMaConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleScriptTemplateDetailsViewImpl extends Composite implements
		RoleScriptTemplateDetailsView {

	
	private static RoleScriptTemplateDetailsViewImplUiBinder uiBinder = GWT
			.create(RoleScriptTemplateDetailsViewImplUiBinder.class);

	interface RoleScriptTemplateDetailsViewImplUiBinder extends
			UiBinder<Widget, RoleScriptTemplateDetailsViewImpl> {

	}

	// Violation Changes Highlight
			Map<String, Widget> viewMap;
		// E Violation Changes Highlight
			
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private Presenter presenter;
	private Delegate delegate;
	
	// Violation Changes Highlight
	public RoleBaseTableItemViewImpl roleBaseTableItemViewImpl;
	// E Violation Changes Highlight
	
	@UiField
	TextBox ItemNameText;

	@UiField(provided = true)
	ValueListBox<ItemDefination> ItemtypeDD = new ValueListBox<ItemDefination>(new EnumRenderer<ItemDefination>());

	@UiField
	IconButton AddItem;
	
	@UiField
	VerticalPanel TableItem;
	
//	@UiField
//	RoleBaseTableItemViewImpl roleBaseTableItemViewImpl;
//	

	// Violation Changes Highlight
	@Override
	public RoleBaseTableItemViewImpl getRoleBaseTableItemViewImpl() {
		return roleBaseTableItemViewImpl;
	}
	// E Violation Changes Highlight
	
	@Override
	public VerticalPanel getTableItem() {
		return TableItem;
	}
	
	@UiField(provided = true)
	CellTable<RoleBaseItemProxy>table;
	
	@UiHandler("AddItem")
	public void addItemClickHandler(ClickEvent event) {
		System.out.println("selected Item Defination index value.."+ ItemtypeDD.getValue());
		// Violation Changes Highlight
		
				/*if(ItemNameText.getValue()==null || ItemNameText.getValue()=="" || ItemNameText.getValue().startsWith(" "))
				{
					Window.alert("Please Enter appropriate value for Role Base Item");
					// Issue Role
					 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
					 dialogBox.showConfirmationDialog("Please Enter appropriate value for Role Base Item");
					 
					 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();							
							Log.info("ok click");	
							return;
								}
							});

					
					
			//E: Issue Role
				}
				else
				{
					delegate.newClicked(ItemNameText.getValue(),ItemtypeDD.getValue());
					ItemNameText.setText("");	
				}*/
				
				delegate.newClicked(ItemNameText.getValue(),ItemtypeDD.getValue());
				ItemNameText.setText("");	
				// E Violation Changes Highlight
		
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public RoleScriptTemplateDetailsViewImpl() {

		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<RoleBaseItemProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		
		ItemtypeDD.setValue(ItemDefination.values()[0]);
		ItemtypeDD.setAcceptableValues(Arrays.asList(ItemDefination.values()));
		
		init();
		// todo
		AddItem.setText(constants.addItem());
		
		// Violation Changes Highlight
				viewMap=new HashMap<String, Widget>();
				viewMap.put("item_name", ItemNameText);
				viewMap.put("item_defination", ItemtypeDD);
				// E Violation Changes Highlight
				
				roleBaseTableItemViewImpl=new RoleBaseTableItemViewImpl();
	}
	
	protected ArrayList<String> paths = new ArrayList<String>();
	public void init(){
		
		
		
		paths.add("deleted");
		table.addColumn(new TextColumn<RoleBaseItemProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleBaseItemProxy object) {
				return renderer.render(object.getItem_name());
				
			}
		}, constants.deletedItemName());
		
		addColumn(new ActionCell<RoleBaseItemProxy>(
				/*OsMaConstant.EDIT_ICON */new SafeHtml() {
					
					@Override
					public String asString() {
						
						return "Restore";
					}
				}, new ActionCell.Delegate<RoleBaseItemProxy>() {
					public void execute(RoleBaseItemProxy roleBaseItem) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						//if(Window.confirm("wirklich löschen?"))
						delegate.restoreButtonClicked(roleBaseItem);
						
					}
				}), "", new GetValue<RoleBaseItemProxy>() {
			public RoleBaseItemProxy getValue(RoleBaseItemProxy roletopic) {
				return roletopic;
			}
		}, null);
		table.addColumnStyleName(1, "iconCol");
		
	}
	
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<RoleBaseItemProxy, C> fieldUpdater) {
		Column<RoleBaseItemProxy, C> column = new Column<RoleBaseItemProxy, C>(cell) {
			@Override
			public C getValue(RoleBaseItemProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column, headerText);
	}
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	private static interface GetValue<C> {
		
		C getValue(RoleBaseItemProxy object);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter StandardizedPatientActivity) {
		this.presenter = StandardizedPatientActivity;
	}



	@Override
	public void setValue(RoleTemplateProxy proxy) {
		
		//Label lbl=new Label("Helllo Base Item");
	   //TableItem.add(lbl);
	
	//	roleBaseTableItemViewImpl.set
		
	}

	public Widget asWidget() {
		return this;
	}
	@Override
	public CellTable<RoleBaseItemProxy> getTable() {
		return table;
	}

	@Override
	public Delegate getDelegate() {

		return delegate;
	}

	@Override
	public void addRoleBaseWidget(RoleBaseItemProxy rolebaseItem) {
		System.out.println("Adding Widget");
		RoleBaseTableItemViewImpl roleBaseTableItemViewImpl=new RoleBaseTableItemViewImpl();
		roleBaseTableItemViewImpl.baseItemHeaderLable.setText(rolebaseItem.getItem_name());
		System.out.println("rolebaseItem.id : " + rolebaseItem.getId());			
		roleBaseTableItemViewImpl.setValue(rolebaseItem);
		
		TableItem.add(roleBaseTableItemViewImpl);
	}
	
	// Violation Changes Highlight

		@Override
		public Map getViewMap() {
			// TODO Auto-generated method stub
			Log.info("Call getViewMap....");
			return this.viewMap;
		}
		// E Violation Changes Highlight		
}
