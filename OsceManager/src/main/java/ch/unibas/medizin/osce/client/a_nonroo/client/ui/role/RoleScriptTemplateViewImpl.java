package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResourcesNoSortArrow;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RoleScriptTemplateViewImpl extends Composite implements RoleScriptTemplateView, RecordChangeHandler, MenuClickHandler {

	
	private static RoleScriptTemplateViewUiBinder uiBinder = GWT
			.create(RoleScriptTemplateViewUiBinder.class);

	interface RoleScriptTemplateViewUiBinder extends UiBinder<Widget, RoleScriptTemplateViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;
	
	// Violation Changes Highlight
		Map<String, Widget> addTemplateMap;
		// E Violation Changes Highlight
	
	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	
	@UiField
	public SimplePanel detailsPanel;
	
	private Presenter presenter;
	

	
	@UiField (provided = true)
	SimplePager pager;
	
	//cell table
	/*
	@UiField (provided = true)
	CellTable<RoleTemplateProxy> table;
	*/
	@UiField (provided = true)
	AdvanceCellTable<RoleTemplateProxy> table;
	//cell table
	List<String> paths=new ArrayList<String>();
	
	@UiField
	TextBox newBodypart;
	
	@UiField
	IconButton newButton;
	
	// Issue Role Module
	int top =0, left=0;
	
	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	int widthSize=1225,decreaseSize=0;
	Timer timer;
	
	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {
		// Violation Changes Highlight

				/*if(newBodypart.getValue()==null || newBodypart.getValue()=="" || newBodypart.getValue().startsWith(" "))
					Window.alert("Please Enter Appropriate Value For Role Template");
				else
					delegate.newClicked(newBodypart.getValue());
				newBodypart.setValue("");*/
				
				delegate.newClicked(newBodypart.getValue());
				newBodypart.setValue("");
				// E Violation Changes Highlight
	}
	
	@UiField(provided=true)
	QuickSearchBox searchBox;
	
	//@UiField (provided = true)
	//QuickSearchBox searchBox;
	
	
	
	public RoleScriptTemplateViewImpl() {
		// TODO Auto-generated constructor stub
		//cell table chages
		/*CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<RoleTemplateProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		*/
		CellTable.Resources tableResources = GWT.create(MyCellTableResourcesNoSortArrow.class);
		table = new AdvanceCellTable<RoleTemplateProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		//cell table changes 
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));

		newButton.setText(constants.addRoleScriptTemplate());
		
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		
		// Violation Changes Highlight
				addTemplateMap=new HashMap<String, Widget>();
				addTemplateMap.put("templateName",newBodypart);
				
				// E Violation Changes Highlight
	}
	
	public String[] getPathsString() {
		return paths.toArray(new String[paths.size()]);
	}
	
	public List<String> getPaths() {
		return paths;
	}
	
	public void init()
	{
		newBodypart.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		        	newButtonClicked(null);
		    }
		});

		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
//		int splitLeft = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//
//		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+splitLeft+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
		
		// Issue Role Module
				table.addDomHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						top = event.getClientY()-50;
						left = event.getClientX();
						
						Log.info("top : "+top);
						Log.info("left : "+left);
					}
				}, ClickEvent.getType());		
				// E Issue Role Module
		
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		paths.add("templateName");
		paths.add("");
		table.addColumn(new TextColumn<RoleTemplateProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleTemplateProxy object) {
				return renderer.render(object.getTemplateName());
			}
		}, "Role Template");
		
		paths.add("date_created");
		paths.add("");
		table.addColumn(new TextColumn<RoleTemplateProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<java.util.Date> renderer = new AbstractRenderer<java.util.Date>() {

				public String render(java.util.Date obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleTemplateProxy object) {
				return DateTimeFormat.getShortDateFormat().format(object.getDate_cretaed());
			}
		}, constants.dateCreated());
		paths.add("date_edited");
		paths.add("");
		table.addColumn(new TextColumn<RoleTemplateProxy>() {

			{
				this.setSortable(true);
			}
			Renderer<java.util.Date> renderer = new AbstractRenderer<java.util.Date>() {

				public String render(java.util.Date obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleTemplateProxy object) {
				return DateTimeFormat.getShortDateFormat().format(object.getDate_edited());
			}
		}, constants.dateEdited());
		//Edit Button
					// Issue Role Module
		addColumn(new ActionCell<RoleTemplateProxy>(
						OsMaConstant.EDIT_ICON, new ActionCell.Delegate<RoleTemplateProxy>() {
							public void execute(final RoleTemplateProxy roleTemplate) {
												
								delegate.editClicked(roleTemplate,left,top);
													
							}
						}), "", new GetValue<RoleTemplateProxy>() {
					public RoleTemplateProxy getValue(RoleTemplateProxy roleTemplate) {
						return roleTemplate;
					}
				}, null);
		// E Issue Role Module
		addColumn(new ActionCell<RoleTemplateProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<RoleTemplateProxy>() {
					public void execute(final RoleTemplateProxy roleTemplate) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						/*if(Window.confirm("wirklich löschen?"))
							delegate.deleteClicked(roleTemplate);*/
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();									
									Log.info("yes click");
									delegate.deleteClicked(roleTemplate);
									return;

										}
									});

							dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									Log.info("no click");
									return;
									
								}
							});
						// E: Issue Role
					}
				}), "", new GetValue<RoleTemplateProxy>() {
			public RoleTemplateProxy getValue(RoleTemplateProxy roleTemplate) {
				return roleTemplate;
			}
		}, null);

		table.addColumnStyleName(1, "iconCol");
		table.addColumnStyleName(2, "iconCol");
		table.addColumnStyleName(3, "iconCol");
		table.addColumnStyleName(4, "iconCol");
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}
	public void setDetailPanel(boolean isDetailPlace) {

//		splitLayoutPanel.setWidgetSize(westPanel, OsMaConstant.WIDTH_SIZE - OsMaConstant.WIDTH_MIN );
		ResolutionSettings.setSplitLayoutPanelAnimation(splitLayoutPanel);
		splitLayoutPanel.animate(OsMaConstant.ANIMATION_TIME);	
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	private static interface GetValue<C> {
		C getValue(RoleTemplateProxy contact);
	}

	private List<AbstractEditableCell<?, ?>> editableCells;

	@Override
	public CellTable<RoleTemplateProxy> getTable() {
		return table;
	}

	/**
	 * Add a column with a header.
	 *
	 * @param <C> the cell type
	 * @param cell the cell used to render the column
	 * @param headerText the header string
	 * @param getter the value getter for the cell
	 */
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<RoleTemplateProxy, C> fieldUpdater) {
		Column<RoleTemplateProxy, C> column = new Column<RoleTemplateProxy, C>(cell) {
			@Override
			public C getValue(RoleTemplateProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column);
	}
	// Violation Changes Highlight
	@Override
	public Map getAadTemplateMap() {
		// TODO Auto-generated method stub
		return this.addTemplateMap;
	}
	// E Violation Changes Highlight
	
	// by spec
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
	// by spec

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());		
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,false);
//		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(splitLayoutPanel.getWidget(0).getOffsetWidth() >= 1220){
//			
//			if(OsMaMainNav.getMenuStatus() == 0)
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//			else
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
//		}
			
	}

}
