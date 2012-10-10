/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResourcesNoSortArrow;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.TraitTypes;
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
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class ScarViewImpl extends Composite implements ScarView, RecordChangeHandler, MenuClickHandler {

	private static ScarViewUiBinder uiBinder = GWT
			.create(ScarViewUiBinder.class);

	interface ScarViewUiBinder extends UiBinder<Widget, ScarViewImpl> {
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;

	@UiField
	SplitLayoutPanel splitLayoutPanel;

	@UiField (provided = true)
	QuickSearchBox searchBox;

	@UiField
	TextBox newBodypart;
	
	@UiField(provided = true)
    ValueListBox<TraitTypes> traitTypeBox = new ValueListBox<TraitTypes>(new EnumRenderer<TraitTypes>());

	@UiField
	Button newButton;
	
	@UiField (provided = true)
	SimplePager pager;

	
	//celltable changes start
	/*@UiField (provided = true)
	CellTable<ScarProxy> table;
*/
	@UiField (provided = true)
	AdvanceCellTable<ScarProxy> table;

	//celltable changes end
	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	ScarEditPopupView scarPopupView;
	
	int left1 = 0;
	int top = 0;

	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {
		Log.info("CAll newButtonClicked");
		delegate.newClicked(traitTypeBox.getValue(), newBodypart.getValue());
		newBodypart.setValue("");
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
	Map<String, Widget> scarMap;
	Map<String, Widget> scarEditMap;
	// E Highlight onViolation
	
	public ScarViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResourcesNoSortArrow.class);
		//celltable changes start
		//table = new CellTable<ScarProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		table = new AdvanceCellTable<ScarProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		//cell atble changes end
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		traitTypeBox.setValue(TraitTypes.values()[0]);
		traitTypeBox.setAcceptableValues(Arrays.asList(TraitTypes.values()));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		newButton.setText(constants.addTrait());
		
		// Highlight onViolation
		scarMap=new HashMap<String, Widget>();
		scarMap.put("bodypart",newBodypart );
		scarMap.put("traitType", traitTypeBox);
		// E Highlight onViolation
		
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		newBodypart.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		        	newButtonClicked(null);
		    }
		});

		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
//		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		
//		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
		
		table.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				left1 = event.getClientX();
				top = event.getClientY();
				
			}
		}, ClickEvent.getType());

		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
		paths.add("trait_type");
		table.addColumn(new TextColumn<ScarProxy>() {
			Renderer<TraitTypes> renderer = new EnumRenderer<TraitTypes>();

			@Override
			public String getValue(ScarProxy object) {
				return renderer.render(object.getTraitType());
			}
		}, constants.type());
		paths.add("bodypart");
		table.addColumn(new TextColumn<ScarProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(ScarProxy object) {
				return renderer.render(object.getBodypart());
			}
		}, constants.location());
		
		addColumn(new ActionCell<ScarProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<ScarProxy>() {
					public void execute(ScarProxy scar) {
						showEditScarPopup(scar);
					}
				}), "", new GetValue<ScarProxy>() {
			public ScarProxy getValue(ScarProxy scar) {
				return scar;
			}
		}, null);
		
		addColumn(new ActionCell<ScarProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<ScarProxy>() {
					public void execute(final ScarProxy scar) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						/*if(Window.confirm("wirklich löschen?"))
							delegate.deleteClicked(scar);*/
						
						final MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						messageConfirmationDialogBox.showYesNoDialog("wirklich löschen?");
						
						messageConfirmationDialogBox.getYesBtn().addClickHandler(new ClickHandler() {					
							@Override
							public void onClick(ClickEvent event) {
								messageConfirmationDialogBox.hide();
								delegate.deleteClicked(scar);				
							}
						});
						
						messageConfirmationDialogBox.getNoBtnl().addClickHandler(new ClickHandler() {					
							@Override
							public void onClick(ClickEvent event) {
												
							}
						});
					}
				}), "", new GetValue<ScarProxy>() {
			public ScarProxy getValue(ScarProxy scar) {
				return scar;
			}
		}, null);
		
		table.addColumnStyleName(2, "iconCol");
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
			final GetValue<C> getter, FieldUpdater<ScarProxy, C> fieldUpdater) {
		Column<ScarProxy, C> column = new Column<ScarProxy, C>(cell) {
			@Override
			public C getValue(ScarProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column);
	}
	
	/**
	 * Get a cell value from a record.
	 *
	 * @param <C> the cell type
	 */
	private static interface GetValue<C> {
		C getValue(ScarProxy contact);
	}

	private List<AbstractEditableCell<?, ?>> editableCells;

	@Override
	public CellTable<ScarProxy> getTable() {
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
	// Highlight onViolation

@Override
public Map getScarMap() 
{	
	return this.scarMap;
}
// E Highlight onViolation

//by spec
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
	
	public void showEditScarPopup(final ScarProxy scar)
	{
		scarPopupView = new ScarEditPopupViewImpl();
		
		((ScarEditPopupViewImpl)scarPopupView).setAnimationEnabled(true);
		
		scarEditMap=new HashMap<String, Widget>();
		scarEditMap.put("bodypart",scarPopupView.getLocationTxtBox());
		scarEditMap.put("traitType", scarPopupView.getTraitTypeBox());
		
		scarPopupView.getTraitTypeBox().setValue(scar.getTraitType());
		
		scarPopupView.getLocationTxtBox().setText(scar.getBodypart());
		
		((ScarEditPopupViewImpl)scarPopupView).setWidth("160px");	
			
		RootPanel.get().add(((ScarEditPopupViewImpl)scarPopupView));
		
		/*	// Highlight onViolation
		checklistOptionMap=new HashMap<String, Widget>();
		checklistOptionMap.put("optionName", optionPopup.getTopicTxtBox());
		checklistOptionMap.put("name", optionPopup.getTopicTxtBox());
		checklistOptionMap.put("value", optionPopup.getDescriptionTxtBox());
			// E Highlight onViolation
			*/	
		scarPopupView.getOkBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
					
				if(scarPopupView.getLocationTxtBox().getValue()=="")
				{
				}	
				else
				{
					//delegate.saveCheckListTopic(optionPopup.getTopicTxtBox().getValue(),optionPopup.getDescriptionTxtBox().getValue());
					delegate.updateClicked(scarPopupView.getLocationTxtBox().getValue(), scarPopupView.getTraitTypeBox().getValue(), scar);
					(((ScarEditPopupViewImpl)scarPopupView)).hide(true);
					
					scarPopupView.getLocationTxtBox().setText("");
					scarPopupView.getTraitTypeBox().setValue(TraitTypes.values()[0]);
				}
			}
		});

		// Issue Role V1 
		scarPopupView.getCancelBtn().addClickHandler(new ClickHandler() 
		{				
			@Override
			public void onClick(ClickEvent event) 
			{
				(((ScarEditPopupViewImpl)scarPopupView)).hide(true);					
				scarPopupView.getLocationTxtBox().setText("");
				scarPopupView.getTraitTypeBox().setValue(TraitTypes.values()[0]);
			}
		});	
		// E: Issue Role V1
		
		(((ScarEditPopupViewImpl)scarPopupView)).setPopupPosition(left1, top);
		//(((ScarEditPopupViewImpl)scarPopupView)).getElement().getStyle().setZIndex(2);
		(((ScarEditPopupViewImpl)scarPopupView)).show();
	}

	@Override
	public Map getScarEditMap() {
		// TODO Auto-generated method stub
		return this.scarEditMap;
	}
}
