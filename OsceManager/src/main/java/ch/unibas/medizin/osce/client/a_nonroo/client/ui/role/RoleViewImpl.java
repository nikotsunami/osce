/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResourcesNoSortArrow;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class RoleViewImpl extends Composite implements RoleView, RecordChangeHandler, MenuClickHandler {

//	private static RoleViewUiBinder uiBinder = GWT
//			.create(RoleViewUiBinder.class);

	private static RoleViewImplUiBinder uiBinder = GWT
			.create(RoleViewImplUiBinder.class);
	
	interface RoleViewImplUiBinder extends UiBinder<Widget, RoleViewImpl> {
	}

	
	// Highlight onViolation
		Map<String, Widget> roleTopicMap;
	// E Highlight onViolation
	//spec  start
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	//spec  end
	
	private Delegate delegate;
	
	@UiField
	SplitLayoutPanel splitLayoutPanel;
	
	//spec start
	@UiField (provided = true)
	public QuickSearchBox searchBox;
	
	@UiField
	public IconButton filterButton;

	@UiField
	public IconButton clearButton;
	
	
	@UiField
	public IconButton exportButton;
	
	@UiField(provided = true)
	public SimplePager pager;
	
	//cell table changes start
	/*@UiField(provided = true)
	public CellTable<RoleTopicProxy> table;*/
	@UiField(provided = true)
	public AdvanceCellTable<RoleTopicProxy> table;
	/*cell table changes end*/
//	
	
	/*@UiField
	 TextBox topicName;
*/
	/*@UiField
	 MySuggestionBoxDemo suggestdemo;
	
	@UiField
	public DefaultIconedSuggestBox checkdemo ;
	*/
	/*@UiField
	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> defaultsuggest;
	*/
	/*@UiField
	CustomSuggestBox cSuggestBox;//=new CustomSuggestBox(s);

*/	
//	@UiField(provided = true)
//    ValueListBox<StudyYears> traitTypeBox1 = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
//	
//	
	/*@UiField(provided = true)
	ListBox slots_till_change=new ListBox();*/
	
//	@UiField(provided = true)
//	ListBox SpecialisationId=new ListBox();
//	
//	
	/*@UiField(provided = true)
    ValueListBox<StudyYears> studyYearBox = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());*/

	
//	ValueListBox<Specialisation> c=new ValueListBox<Specialisation>(new SpecialisationProxyRenderer());
	
	//Issue # 122 : Replace pull down with autocomplete.

	/*@UiField
	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> SpecialisationBox;*/
/*	@UiField(provided = true)
	ValueListBox<SpecialisationProxy> SpecialisationBox = new ValueListBox<SpecialisationProxy>(new SpecialisationProxyRenderer());
*/
	
	//Issue # 122 : Replace pull down with autocomplete.

	
	private boolean addBoxesShown = true;   
	
	
	
	@UiField
	public IconButton newButton;
	
	
	
//	
	//spec end
	
//	@UiField
//	HasClickHandlers showSubview;
	
	@UiField
	SimplePanel detailsPanel;

	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	int widthSize=OsMaConstant.WIDTH_SIZE,decreaseSize=0;
	Timer timer;
	
	
	/*protected Set<String> paths = new HashSet<String>();*/
	//cell table changes
	List<String> paths=new ArrayList<String>();
	//cell table changes
	private Presenter presenter;
	
	private RoleFilterViewTooltipImpl  filterPanel;
	
	//private StandardizedPatientFilterViewImpl filterPanel;
	
	//issue
	RoleAddPopupView popupView;
	

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * 
	 *
	 * implement HasHTML instead of HasText.
	 */
	
	@UiHandler("clearButton")
	void onClick(ClickEvent e) {
		Log.info("clear button call");
		//Window.alert("clear call");
		searchBox.setText("");
		delegate.performSearch(searchBox.getValue(), getSearchFilters(),getTableFilters(),getWhereFilters());
		
		
		
	}
	
	@UiHandler("filterButton")
	public void filterButtonHover(MouseOverEvent event) {
//System.out.println("Mouse over");
		Log.info("filter panel call");
			showFilterPanel((Widget) event.getSource());
	}
	
	
	
	private void showFilterPanel(Widget eventSource) {
		int x = eventSource.getAbsoluteLeft();
		int y = eventSource.getAbsoluteTop();
		filterPanel.setPopupPosition(x, y);
		filterPanel.show();
		//Log.info(filterPanel.getSpecialisationBox().getValue());
		
	}
	

	
	public List<String> getSearchFilters() {
		return filterPanel.getFilters();
	}
	
	
	public List<String> getTableFilters() {
		return filterPanel.getTableFilters();
	}
	
	
	public List<String> getWhereFilters() {
		return filterPanel.getWhereFilters();
	}
	

	
	public String getQuery() {
		return searchBox.getValue();
	}
	
	@Override
	public void updateSearch() {
		String q = searchBox.getValue();
		delegate.performSearch(q, getSearchFilters(),getTableFilters(),getWhereFilters());
		
	}

	public RoleViewImpl() {
		
		//spec  start
		
		//cell table changes start
		/*CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<RoleTopicProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);*/
		CellTable.Resources tableResources = GWT.create(MyCellTableResourcesNoSortArrow.class);
		table = new AdvanceCellTable<RoleTopicProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		//cell table changes end
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		
		
		filterPanel = new RoleFilterViewTooltipImpl();
		filterPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				Log.info("filter panel close");
				if(filterPanel.startDate.isDatePickerShowing())
					filterPanel.startDate.hideDatePicker();
				
				if (filterPanel.endDate.isDatePickerShowing())
					filterPanel.endDate.hideDatePicker();
				
				if (filterPanel.selectionChanged()) {
					filterPanel.clearSelectionChanged();
					//delegate.performSearch(searchBox.getValue(), getSearchFilters());
					delegate.performSearch(searchBox.getValue(), getSearchFilters(),getTableFilters(),getWhereFilters());
				}
			}
			
		});

		
//		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				//delegate.performSearch(searchBox.getValue(), getSearchFilters());
				delegate.performSearch(searchBox.getValue(), getSearchFilters(),getTableFilters(),getWhereFilters());
			}
		});
		
		
		initWidget(uiBinder.createAndBindUi(this));
		
		
		//	studyYearBox.setValue(StudyYears.valueOf(""+0));
		
		
		/*for(int i=1;i<=12;i++)
		{
			slots_till_change.addItem(i+"");
		}
		
		
		
		studyYearBox.setValue(StudyYears.values()[0]);
		studyYearBox.setAcceptableValues(Arrays.asList(StudyYears.values()));*/
		
		
		//spec  end
		
		init();
		//spec  start
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		exportButton.setText(constants.export());
		newButton.setText(constants.addRoleTopic());

		//spec  end
		
		
		// Highlight onViolation
		
		roleTopicMap=new HashMap<String, Widget>();
		/*roleTopicMap.put("name", topicName);
		roleTopicMap.put("description", topicName);*/
		/*roleTopicMap.put("studyYear", studyYearBox);
		roleTopicMap.put("slotsUntilChange", slots_till_change);
		//Issue # 122 : Replace pull down with autocomplete.
		//roleTopicMap.put("specialisation",SpecialisationBox);
		roleTopicMap.put("specialisation",SpecialisationBox.getTextField().advancedTextBox);*/
		//Issue # 122 : Replace pull down with autocomplete.


		
		
		// E Highlight onViolation
	}

	//spec start
	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {
		
		popupView = new RoleAddPopupViewImpl();
			
		((RoleAddPopupViewImpl)popupView).setGlassEnabled(false);
	
		((RoleAddPopupViewImpl)popupView).setWidth("480px");
	
		((RoleAddPopupViewImpl)popupView).getNewButton().addClickHandler(new ClickHandler() {
				
			@Override
			public void onClick(ClickEvent event) {
				delegate.newClicked(((RoleAddPopupViewImpl)popupView).getTopicName().getValue(), ((RoleAddPopupViewImpl)popupView).getSlots_till_change().getValue(((RoleAddPopupViewImpl)popupView).getSlots_till_change().getSelectedIndex()), ((RoleAddPopupViewImpl)popupView).getSpecialisationBox().getSelected(), ((RoleAddPopupViewImpl)popupView).getStudyYearBox().getValue());
				((RoleAddPopupViewImpl)popupView).getTopicName().setValue("");
				((RoleAddPopupViewImpl)popupView).hide();
			}
		});
		
		
		
		delegate.getAllSpecialisation(popupView, event.getClientX(), event.getClientY());
	
		//Issue # 122 : Replace pull down with autocomplete.
		//delegate.newClicked(topicName.getValue(), slots_till_change.getValue(slots_till_change.getSelectedIndex()),SpecialisationBox.getValue() ,studyYearBox.getValue());
		//delegate.newClicked(topicName.getValue(), slots_till_change.getValue(slots_till_change.getSelectedIndex()),SpecialisationBox.getSelected() ,studyYearBox.getValue());
		//Issue # 122 : Replace pull down with autocomplete.
		//topicName.setValue("");
	}
	//spec end
	
	public List<String> getPaths() {
		return paths;
	}

	public void init() {

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
		
		//spec start
		Log.info("set data in column for cell tabel");
		paths.add("name");
		paths.add("");
		table.addColumn(new TextColumn<RoleTopicProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleTopicProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.roleTopic());
		
		
		
		paths.add("specialisation");
		paths.add("");
		table.addColumn(new TextColumn<RoleTopicProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};


			@Override
			public String getValue(RoleTopicProxy object) {
				String strStanderdizedRole = "";
			
				if(object.getStandardizedRoles()!=null) {
					for (StandardizedRoleProxy role : object.getStandardizedRoles()) {
						// Boolean object has to be checked for null value!
						if(role.getActive() != null && role.getActive().booleanValue() == true) {
							strStanderdizedRole = strStanderdizedRole + role.getShortName() + ", ";
						}
					}
				}
				if(strStanderdizedRole!="")
				{
					
						strStanderdizedRole = strStanderdizedRole.substring(0, strStanderdizedRole.length() - 2); 
				}

				
			
				
				return renderer.render(strStanderdizedRole);
				
			}
		}, constants.aviableRole());
		//spec end
		
		
		
		// TODO implement this!
	}
	


	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}
	public void setDetailPanel(boolean isDetailPlace) {
//		splitLayoutPanel.setWidgetSize(westPanel, OsMaConstant.WIDTH_SIZE - OsMaConstant.WIDTH_MIN );
		ResolutionSettings.setSplitLayoutPanelAnimation(splitLayoutPanel);
		splitLayoutPanel.animate(OsMaConstant.ANIMATION_TIME);		
		
//		widthSize = 1200;
//		decreaseSize = 0;
//		splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		/*if (isDetailPlace) {

			timer = new Timer() {
				@Override
				public void run() {
					if (decreaseSize <= 705) {
						splitLayoutPanel.setWidgetSize(westPanel, 1225
								- decreaseSize);
						decreaseSize += 5;
					} else {
						timer.cancel();
					}
				}
			};
			timer.schedule(1);
			timer.scheduleRepeating(1);

		} else {
			widthSize = OsMaConstant.WIDTH_SIZE;
			decreaseSize = 0;
			splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		}*/
	}
	
	
	
	@Override
	public CellTable<RoleTopicProxy> getTable() {
		return table;
	}

	
	
	
	public ValueListBox<SpecialisationProxy> gets() {
		//Issue # 122 : Replace pull down with autocomplete.
		//return SpecialisationBox;
		return null;
		//Issue # 122 : Replace pull down with autocomplete.
		
		
	}
	
	@Override
	public void setSpecialisationBoxValues(List<SpecialisationProxy> values) {
		/*boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {
			//hideAddBoxes();
			return;
		}
		
	List<Object> objectValue=new ArrayList<Object>();
	objectValue.addAll(values);
	
	//Issue # 122 : Replace pull down with autocomplete.
		//SpecialisationBox.setAcceptableValues(values);
	
	DefaultSuggestOracle<SpecialisationProxy> suggestOracle1 = (DefaultSuggestOracle<SpecialisationProxy>) SpecialisationBox.getSuggestOracle();
	suggestOracle1.setPossiblilities(values);
	SpecialisationBox.setSuggestOracle(suggestOracle1);
	//SpecialisationBox.setRenderer(new SpecialisationProxyRenderer());
	SpecialisationBox.setRenderer(new AbstractRenderer<SpecialisationProxy>() {

		@Override
		public String render(SpecialisationProxy object) {
			// TODO Auto-generated method stub
			if(object!=null)
			{
			return object.getName();
			}
			else
			{
				return "";
			}
		}
	});
	//SpecialisationBox.setRendererWidth("100px");
	SpecialisationBox.setWidth(120);
	
		//Issue # 122 : Replace pull down with autocomplete.
	
		DefaultSuggestOracle<SpecialisationProxy> suggestOracle1 = (DefaultSuggestOracle<SpecialisationProxy>) defaultsuggest.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);


		defaultsuggest.setSuggestOracle(suggestOracle1);
		
		First Way to set renderer
		 * defaultsuggest.setRenderer(new AbstractRenderer<SpecialisationProxy>() {

			@Override
			public String render(SpecialisationProxy object) {
				// TODO Auto-generated method stub
				return object.getName();
			}
		});

		//second way to set renderer
		defaultsuggest.setRenderer(new SpecialisationProxyRenderer());
		
		
		*/
		
	}
	
	
	
	private void setAddBoxesShown(boolean show) {
		/*if (addBoxesShown  == show) {
			return;
		}
		
		SpecialisationBox.setVisible(show);
	//	scarAddButton.setVisible(show);
		addBoxesShown = show;*/
		
	}
	private void showAddBoxes() {
		setAddBoxesShown(true);
	}
	
	private void hideAddBoxes() {
		setAddBoxesShown(false);
	}

	public ValueListBox<SpecialisationProxy> getSpecialisationBox() {
		//Issue # 122 : Replace pull down with autocomplete.
		//return SpecialisationBox;
		return null;
		//Issue # 122 : Replace pull down with autocomplete.
	}

	@Override
	public ListBox getListBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueListBox<SpecialisationProxy> getSpecialisationBoxValues() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public RoleFilterViewTooltipImpl getFilter() {
		// TODO Auto-generated method stub
		return this.filterPanel;
	}

	

	@Override
	public void setKeywordAutocompleteValue(List<KeywordProxy> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuthorAutocompleteValue(List<DoctorProxy> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReviewerAutocompleteValue(List<DoctorProxy> values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSpecialisationAutocompleteValue(
			List<SpecialisationProxy> values) {
		// TODO Auto-generated method stub
		
	}
	
	// Highlight onViolation
	@Override
	public Map getRoleTopicMap()
	{
		return this.roleTopicMap;
	}
	//E Highlight onViolation
	
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
//			int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//			
//			DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//			
//			if(splitLayoutPanel.getWidget(0).getOffsetWidth() >= 1220){
//				
//				if(OsMaMainNav.getMenuStatus() == 0)
//					splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//				else
//					splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
//			}
				
		}

}
