package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.richtext.RichTextToolbar;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleBaseTableItemValueViewImpl extends Composite implements
RoleBaseTableItemValueView {

	private static RoleBaseTableItemValueViewImplUiBinder uiBinder = GWT
			.create(RoleBaseTableItemValueViewImplUiBinder.class);

	interface RoleBaseTableItemValueViewImplUiBinder extends
			UiBinder<Widget, RoleBaseTableItemValueViewImpl> {

	}

	private MajorMinorPopupPanelViewImpl popupPanel;
	private boolean minorClick=false;
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private Presenter presenter;
	private Delegate delegate;
	
	// Issue Role V1
	
		int top=0,left=0;
		
		// E: Issue Role V1

	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	@UiField
	public DisclosurePanel roleBaseItemDisclosurePanel;
	
	@UiField(provided=true)
	public RichTextToolbar toolbar;
	
	@UiField(provided=true)
	public RichTextArea description;
	
//	@UiField 
//	HTMLPanel dynamicItemTable;
	
/*	@UiField
	Image arrow;
*/	
	
//	@UiField
//	public RoleBaseTableAccessViewImpl roleBaseAccessViewImpl;
//	
	@Override
	public RoleBaseTableAccessViewImpl getRoleBaseTableAccessViewImpl() {
		return null;
	}
	private  RoleBaseItemProxy roleBasedItemProxy;
	
//	public ViewType screen=ViewType.role_template;
	
	@UiField
	public HorizontalPanel accessDataPanel;

	
	@Override
	public HorizontalPanel getAccessDataPanel() {
		return accessDataPanel;
	}
	
	@UiField
	public Label baseItemHeaderLable;
	
	@UiField(provided = true)
	public CellTable<RoleTableItemValueProxy> table;
	
//	@UiField
//	Label hello;
	
	
	
	
	
//	@UiField
//	public Label accessLabel1;
//	
	
	@UiField
	com.google.gwt.user.client.ui.Image arrow;
	
	
	@UiField
	public IconButton addRichTextAreaValue;
	
	@UiField
	public IconButton saveRichTextArea;
	
	
	@UiHandler("addRichTextAreaValue")
	public void addRichTextAreaValueEdit(ClickEvent event){
	//role template spec
		saveRichTextArea.setVisible(true);
		description.setEnabled(true);
		//delegate.addRichTextAreaValue(roleBasedItemProxy,description);
	}
	
	
	
	@UiHandler("saveRichTextArea")
	public void addRichTextAreaValue(ClickEvent event){
	//role template spec
		//int majorOrMinor=getMajorMinorChange();
		//Log.info("Major or minar click--"+majorOrMinor);
		showPopup();
	//	delegate.addRichTextAreaValue(roleBasedItemProxy,description);
	//	saveRichTextArea.setVisible(false);
	}
	

	
	@UiHandler("arrow")
	void handleClick(ClickEvent e) {
		if(roleBaseItemDisclosurePanel.isOpen()) {
			roleBaseItemDisclosurePanel.setOpen(false);
			arrow.setResource(uiIcons.triangle1East());
		}
		else {
			roleBaseItemDisclosurePanel.setOpen(true);
			arrow.setResource(uiIcons.triangle1South());
		}
	   
	  }
	/*
	public RoleBaseTableItemValueViewImpl(ViewType viewType) {

		screen=viewType;
		System.out.println("ENUM VALUE: " +screen.name());
		//this.screen = screen;
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<RoleTableItemValueProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		
		description = new RichTextArea();
		description.setSize("100%", "14em");
		toolbar = new RichTextToolbar(description);
		toolbar.setWidth("100%");
		initWidget(uiBinder.createAndBindUi(this));
		
		init();
		// todo
		AccessButton.setText("access");
		AddSubItem.setText("Add Subitem");
		
	}
	
*/	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public RoleBaseTableItemValueViewImpl() {

		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<RoleTableItemValueProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		
		description = new RichTextArea();
		description.setSize("100%", "14em");
		description.setEnabled(false);
		toolbar = new RichTextToolbar(description);
		description.setEnabled(false);
		toolbar.setWidth("100%");
		initWidget(uiBinder.createAndBindUi(this));
		saveRichTextArea.setVisible(false);
		//toolbar description
		description.setEnabled(false);
		
		init();
		// todo
		//AddSubItem.setStyleName("padding=10");
		
	//	accessLabel1=new Label();
	}
	
	public void setValue(RoleBaseItemProxy roleBaseItemProxy)
	{		
		this.roleBasedItemProxy = roleBaseItemProxy;
		baseItemHeaderLable.setText(roleBaseItemProxy.getItem_name());
		System.out.println("Set value rolebaseItem.id : " + this.roleBasedItemProxy.getId());	
	}

	protected ArrayList<String> paths = new ArrayList<String>();
	public void init(){
		
		// Issue Role V1
		
		table.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Call Click Evenet" );
					left=event.getClientX();
					top=event.getClientY();
					Log.info("Call Check Table Click Position: X=" + left + "Y=" + top );
				
			}
		}, ClickEvent.getType());
		
		// E: Issue Role V1
		
		paths.add("item_name");
		table.addColumn(new TextColumn<RoleTableItemValueProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleTableItemValueProxy object) {
				return renderer.render(object.getRoleTableItem().getItemName());
				
			}
		},"Item Name");
		
				
//			paths.add("item_value");
		TextColumn<RoleTableItemValueProxy> slotColumn=new TextColumn<RoleTableItemValueProxy>() {
				{ this.setSortable(true); }

				 Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
					 
					            public String render(java.lang.String obj) {
					              return obj == null ? "" : String.valueOf(obj);
					          }
					
				};

				@Override
				public String getValue(RoleTableItemValueProxy object) {
					return object.getValue();
					
				}
			};
			
			table.setColumnWidth(slotColumn, "210px");
			table.addColumn(slotColumn,"Item Value");
			
			
			addColumn(new ActionCell<RoleTableItemValueProxy>(
					OsMaConstant.EDIT_ICON, new ActionCell.Delegate<RoleTableItemValueProxy>() {
						public void execute(final RoleTableItemValueProxy roleTableItemValueProxy) {
						//Window.alert("Edit clicked");
						//delegate.addRoleScriptTableItemValue(roleTableItemValueProxy,roleBasedItemProxy.getId(),table,left,top);
							// Issue Role V1
							delegate.addRoleScriptTableItemValue(roleTableItemValueProxy,roleBasedItemProxy.getId(),table,left,top);
							// E: Issue Role V1
						}
					}), "", new GetValue<RoleTableItemValueProxy>() {
				public RoleTableItemValueProxy getValue(RoleTableItemValueProxy roleTableItem) {
					return roleTableItem;
				}
			}, null);
			table.addColumnStyleName(1, "iconCol");

			
		}	
		
		
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<RoleTableItemValueProxy, C> fieldUpdater) {
		Column<RoleTableItemValueProxy, C> column = new Column<RoleTableItemValueProxy, C>(cell) {
			@Override
			public C getValue(RoleTableItemValueProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column, headerText);
	}
		
	
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter StandardizedPatientActivity) {
		this.presenter = StandardizedPatientActivity;
	}

	private List<AbstractEditableCell<?, ?>> editableCells;

	/*@Override
	public void setValue(RoleTemplateProxy proxy) {
		
	}*/
	
	private static interface GetValue<C> {
		
		C getValue(RoleTableItemValueProxy object);
	}

	public Widget asWidget() {
		return this;
	}
	
	

	@Override
	public Delegate getDelegate() {

		return delegate;
	}

	@Override
	public void setBaseItemMidifiedValue(String value) {
		
		baseItemHeaderLable.setText(value);
	}

	@Override
	public CellTable<RoleTableItemValueProxy> getTable() {
		return this.table;
	}
//	public Label getLabel(){
//		return this.accessLabel1;
//	}

	
	@Override
	public String getDescriptionContent() {
		return description.getHTML();
	}

	@Override
	public void setDescriptionContent(String description) {
		this.description.setHTML(description);
	}
	
	

	public int getMajorMinorChange() {
		showPopup();
		if(minorClick)
			return 0;
		else
			return 1;
	}
	
	public void showPopup()
	{
		Log.info("Major Minor Popup");
		
			
			//popupPanel=new PopupPanel();
			popupPanel=new MajorMinorPopupPanelViewImpl();
			popupPanel.setWidth("300px");
			//popupPanel.setHeight("350px");
			popupPanel.setAnimationEnabled(true);
			popupPanel.setPopupPosition(500, 300);
			popupPanel.setGlassEnabled(true);
			
			DecoratorPanel dp=new DecoratorPanel();
			
			VerticalPanel vp=new VerticalPanel();
			Label minorLbl=new Label("Minor Change ?");
			Label majorLbl=new Label("Major Change ?");
			
			
			Button minorBtn=new Button("Minor Change");
			Button majorBtn=new Button("Major Change");
			
			minorBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
				
					minorClick=true;
					
					delegate.addRichTextAreaValue(roleBasedItemProxy,description,0);
					saveRichTextArea.setVisible(false);
					popupPanel.hide();
					
					
				}
			});
			
			majorBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					
				
					minorClick=false;
					delegate.addRichTextAreaValue(roleBasedItemProxy,description,1);
					saveRichTextArea.setVisible(false);
					
					popupPanel.hide();
					
					
					
					
				}
			});
			
			
			//minor.add(minorLbl);
			popupPanel.minor.add(minorBtn);
			
			
			
			
			//major.add(majorLbl);
			popupPanel.major.add(majorBtn);
			popupPanel.minor.setSpacing(20);
			popupPanel.major.setSpacing(20);
			
			//dp.setSize("300px", "100px");
			
			//dp.add(minor);
			
			
			//vp.add(dp);
			//vp.add(major);
			
			//vp.setSpacing(35);
			
			//popupPanel.add(vp);
			
			//RootPanel.get().add(popupPanel);
			
			popupPanel.show();
			
		
	}
	
	

}
