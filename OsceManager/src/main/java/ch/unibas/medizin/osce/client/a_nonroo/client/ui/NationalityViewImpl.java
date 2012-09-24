/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class NationalityViewImpl extends Composite implements  NationalityView, RecordChangeHandler, MenuClickHandler {

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends UiBinder<Widget, NationalityViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	@UiField
	SplitLayoutPanel splitLayoutPanel;
	
	@UiField (provided = true)
	QuickSearchBox searchBox;
	
	@UiField
	TextBox newNationality;
	
    @UiField
    Button newButton;
    
//    @UiField
//    SimplePanel detailsPanel;
    
    int left = 0;
	int top = 0;
    
    @UiField (provided = true)
    SimplePager pager;
    
    @UiField (provided = true)
    CellTable<NationalityProxy> table;

    protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
    
	//spec popup
	public EditPopView editPopView;
    
    @UiHandler ("newButton")
    public void newButtonClicked(ClickEvent event) {
    	
    	 // Highlight onViolation
    	nationalityNewMap=new HashMap<String, Widget>();
    	nationalityNewMap.put("nationality",newNationality);	
    	nationalityNewMap.put("standardizedpatients",newNationality);		
		 // E Highlight onViolation
    	delegate.newClicked(newNationality.getValue());
    	newNationality.setValue("");
    	
    }

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
    // Highlight onViolation
    Map<String, Widget> nationalityMap;
    Map<String, Widget> nationalityNewMap;
    // E Highlight onViolation
   
    
	public NationalityViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<NationalityProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		newButton.setText(constants.addNationality());
		
		
	}
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		newNationality.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		        	newButtonClicked(null);
		    }
		});
		
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
//		int splitLeft = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+splitLeft+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);

		table.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				left = event.getClientX();
				top = event.getClientY();
				
			}
		}, ClickEvent.getType());
		
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
//        paths.add("id");
//        table.addColumn(new TextColumn<NationalityProxy>() {
//
//            Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {
//
//                public String render(java.lang.Long obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(NationalityProxy object) {
//                return renderer.render(object.getId());
//            }
//        }, "Id");
//        paths.add("version");
//        table.addColumn(new TextColumn<NationalityProxy>() {
//
//            Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
//
//                public String render(java.lang.Integer obj) {
//                    return obj == null ? "" : String.valueOf(obj);
//                }
//            };
//
//            @Override
//            public String getValue(NationalityProxy object) {
//                return renderer.render(object.getVersion());
//            }
//        }, "Version");
		paths.add("nationality");
		table.addColumn(new TextColumn<NationalityProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(NationalityProxy object) {
				return renderer.render(object.getNationality());
			}
		}, constants.nationality());
//        paths.add("standardizedPatient");
//        table.addColumn(new TextColumn<NationalityProxy>() {
//
//            Renderer<java.util.Set> renderer = ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientProxyRenderer.instance());
//
//            @Override
//            public String getValue(NationalityProxy object) {
//                return renderer.render(object.getStandardizedpatients());
//            }
//        }, "Standardized Patient");
		addColumn(new ActionCell<NationalityProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<NationalityProxy>() {
					public void execute(NationalityProxy nation) {
						showEditPopUp(nation);
					}
				}), "", new GetValue<NationalityProxy>() {
			public NationalityProxy getValue(NationalityProxy nation) {
				return nation;
			}
		}, null);
		
		addColumn(new ActionCell<NationalityProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<NationalityProxy>() {
					public void execute(final NationalityProxy nation) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						messageConfirmationDialogBox.showYesNoDialog("wirklich lÃ¶schen?");
						
						messageConfirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {					
							@Override
							public void onClick(ClickEvent event) {
								messageConfirmationDialogBox.hide();
								delegate.deleteClicked(nation);				
							}
						});
						
						messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {					
							@Override
							public void onClick(ClickEvent event) {
												
							}
						});
					}
				}), "", new GetValue<NationalityProxy>() {
			public NationalityProxy getValue(NationalityProxy nation) {
				return nation;
			}
		}, null);

		table.addColumnStyleName(1, "iconCol");
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
			final GetValue<C> getter, FieldUpdater<NationalityProxy, C> fieldUpdater) {
		Column<NationalityProxy, C> column = new Column<NationalityProxy, C>(cell) {
			@Override
			public C getValue(NationalityProxy object) {
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
		C getValue(NationalityProxy contact);
	}

	private List<AbstractEditableCell<?, ?>> editableCells;

	@Override
	public CellTable<NationalityProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

//	@Override
//	public SimplePanel getDetailsPanel() {
//		return detailsPanel;
//	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	//spec popup
	public void showEditPopUp(final NationalityProxy nation)
	{
		editPopView = new EditPopViewImpl();
		((EditPopViewImpl)editPopView).setAnimationEnabled(true);
		((EditPopViewImpl)editPopView).setWidth("200px");
		((EditPopViewImpl)editPopView).getEditTextbox().setValue(nation.getNationality());
		RootPanel.get().add(((EditPopViewImpl)editPopView));
		
		 // Highlight onViolation
		nationalityMap=new HashMap<String, Widget>();
		 nationalityMap.put("nationality",((EditPopViewImpl)editPopView).getEditTextbox());	
		 nationalityMap.put("standardizedpatients",((EditPopViewImpl)editPopView).getEditTextbox());		
		 // E Highlight onViolation
		 
		editPopView.getOkBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				// Highlight onViolation
				/*if (((EditPopViewImpl)editPopView).getEditTextbox().getValue().equals(""))
				{
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
					messageConfirmationDialogBox.showConfirmationDialog("Enter Correct Value");
				}
				else
				{*/
				// E Highlight onViolation
					delegate.updateClicked(nation, ((EditPopViewImpl)editPopView).getEditTextbox().getValue());
					((EditPopViewImpl)editPopView).getEditTextbox().setValue("");
				// Highlight onViolation
				/*	((EditPopViewImpl)editPopView).hide(true);
				}*/
				// E Highlight onViolation
			}
		});
		
		editPopView.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				((EditPopViewImpl)editPopView).getEditTextbox().setValue("");
				((EditPopViewImpl)editPopView).hide(true);
			}
		});
		
		((EditPopViewImpl)editPopView).setPopupPosition(left-150, top - 50);
		((EditPopViewImpl)editPopView).show();
	}

	@Override
	public EditPopView getEditPopupView() {
		// TODO Auto-generated method stub
		return editPopView;
	}

	// Highlight onViolation
	@Override
	public Map getNationalityMap() 
	{	
		return this.nationalityMap;
	}

	@Override
	public Map getNationalityNewMap() {
		return this.nationalityNewMap;
	}
	// E Highlight onViolation

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
