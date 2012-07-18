/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.ui.SpecialisationProxyRenderer;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class RoleViewImpl extends Composite implements RoleView {

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
	public IconButton exportButton;
	
	@UiField(provided = true)
	public SimplePager pager;
	
	@UiField(provided = true)
	public CellTable<RoleTopicProxy> table;
//	
	
	@UiField
	 TextBox topicName;

	
//	@UiField(provided = true)
//    ValueListBox<StudyYears> traitTypeBox1 = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
//	
//	
	@UiField(provided = true)
	ListBox slots_till_change=new ListBox();
	
//	@UiField(provided = true)
//	ListBox SpecialisationId=new ListBox();
//	
//	
	@UiField(provided = true)
    ValueListBox<StudyYears> studyYearBox = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());

	
//	ValueListBox<Specialisation> c=new ValueListBox<Specialisation>(new SpecialisationProxyRenderer());
	
	@UiField(provided = true)
	ValueListBox<SpecialisationProxy> SpecialisationBox = new ValueListBox<SpecialisationProxy>(SpecialisationProxyRenderer.instance());
	
	private boolean addBoxesShown = true;   
	
	
	
	@UiField
	public IconButton newButton;
//	
	//spec end
	
//	@UiField
//	HasClickHandlers showSubview;
	
	@UiField
	SimplePanel detailsPanel;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	private RoleFilterViewTooltipImpl  filterPanel;
	
	//private StandardizedPatientFilterViewImpl filterPanel; 
	

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
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<RoleTopicProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		
		
		filterPanel = new RoleFilterViewTooltipImpl();
		filterPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				Log.info("filter panel close");
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
		
		
		for(int i=1;i<=12;i++)
		{
			slots_till_change.addItem(i+"");
		}
		
		
		
		studyYearBox.setValue(StudyYears.values()[0]);
		studyYearBox.setAcceptableValues(Arrays.asList(StudyYears.values()));
		
		
		//spec  end
		
		init();
		//spec  start
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		exportButton.setText(constants.export());
		newButton.setText(constants.addRoleTopic());

		//spec  end
		
		
		// Highlight onViolation
		
		roleTopicMap=new HashMap<String, Widget>();
		roleTopicMap.put("name", topicName);
		roleTopicMap.put("description", topicName);
		roleTopicMap.put("studyYear", studyYearBox);
		roleTopicMap.put("slotsUntilChange", slots_till_change);
		roleTopicMap.put("specialisation",SpecialisationBox);
		
		
		// E Highlight onViolation
	}

	//spec start
	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {
		
		delegate.newClicked(topicName.getValue(), slots_till_change.getValue(slots_till_change.getSelectedIndex()),SpecialisationBox.getValue() ,studyYearBox.getValue());
		topicName.setValue("");
	}
	//spec end
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		
		//spec start
		Log.info("set data in column for cell tabel");
		paths.add("name");
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
		}, constants.topic());
		
		
		
		paths.add("specialisation");
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
	
	@Override
	public CellTable<RoleTopicProxy> getTable() {
		return table;
	}

	
	
	
	public ValueListBox<SpecialisationProxy> gets() {
		return SpecialisationBox;
	}
	
	@Override
	public void setSpecialisationBoxValues(List<SpecialisationProxy> values) {
		boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {
			//hideAddBoxes();
			return;
		}
		
	
		SpecialisationBox.setAcceptableValues(values);
		
	}
	
	private void setAddBoxesShown(boolean show) {
		if (addBoxesShown  == show) {
			return;
		}
		
		SpecialisationBox.setVisible(show);
	//	scarAddButton.setVisible(show);
		addBoxesShown = show;
		
	}
	private void showAddBoxes() {
		setAddBoxesShown(true);
	}
	
	private void hideAddBoxes() {
		setAddBoxesShown(false);
	}

	public ValueListBox<SpecialisationProxy> getSpecialisationBox() {
		return SpecialisationBox;
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
	
}
