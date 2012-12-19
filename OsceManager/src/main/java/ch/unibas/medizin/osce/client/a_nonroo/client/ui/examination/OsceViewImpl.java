/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.activity.OsceEditActivity;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResourcesNoSortArrow;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class OsceViewImpl extends Composite implements  OsceView, RecordChangeHandler, MenuClickHandler {

	private static OsceViewUiBinder uiBinder = GWT
			.create(OsceViewUiBinder.class);

	interface OsceViewUiBinder extends UiBinder<Widget, OsceViewImpl> {
	}

	private Delegate delegate;
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	@UiField
	SplitLayoutPanel splitLayoutPanel;

	@UiField
	Button newButton;
	

	@UiField
	ScrollPanel mainScrollPanel;

	@UiField
	SimplePanel detailsPanel;

	/*@UiField (provided = true)
	SimplePager pager;
*/
	//cell table changes
/*	@UiField (provided = true)
	CellTable<OsceProxy> table;
*/
	@UiField (provided = true)
	AdvanceCellTable<OsceProxy> table;

//	cell table changes
	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	int widthSize=1225,decreaseSize=0;
	Timer timer;
	
	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.newClicked();
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
	public OsceViewImpl() {
		
		//cell tbale changes
		/*CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<OsceProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);*/
		CellTable.Resources tableResources = GWT.create(MyCellTableResourcesNoSortArrow.class);
		table = new AdvanceCellTable<OsceProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		//cell tbale changes
	/*	SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
*/
		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		newButton.setText(constants.addOsce());
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
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
		//spec start add tabel data
		
		paths.add("OSCE");
		paths.add("");
		table.addColumn(new TextColumn<OsceProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				if(object.getStudyYear()==null) {
					return " ";
				}
				String s= " " + new EnumRenderer<StudyYears>().render(object.getStudyYear()) 
						+ "." + new EnumRenderer<Semesters>().render(OsceEditActivity.semester.getSemester());
				if(object.getIsRepeOsce()==true) {
					s += " " + constants.repe();
				}
				//String s=""+object.getStudyYear().ordinal();
				return renderer.render(s);
			}
		}, constants.osce());
		
		paths.add("maxNumberStudents");
		paths.add("");
		table.addColumn(new TextColumn<OsceProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				if(object.getMaxNumberStudents()==null)
				{
					return " ";
				}
				else
				{
				return renderer.render(object.getMaxNumberStudents().toString());
				}
			}
		}, constants.osceMaxStudents());
		
		paths.add("numberCourses");
		paths.add("");
		table.addColumn(new TextColumn<OsceProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				if(object.getNumberCourses()==null)
				{
					return " ";
				}
				else
				{
				return renderer.render(object.getNumberCourses().toString());
				}
			}
		}, constants.osceMaxCircuits());
		
		paths.add("postLength");
		paths.add("");
		table.addColumn(new TextColumn<OsceProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				if(object.getPostLength()==null)
				{
					return " ";
				}
				else
				{
				return renderer.render(object.getPostLength().toString() + " " + constants.min());
				}
			}
		}, constants.osceStationLength());
		
		
		paths.add("osceBreak");
		paths.add("");
		table.addColumn(new TextColumn<OsceProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				String min="";
				if(object.getShortBreak()!=null)
				{
					min=min+object.getShortBreak();
					
				}
				if(object.getLongBreak()!=null)
				{
					min=min+"/"+object.getLongBreak();
				}
				else
				{
					min=min+"/-";
				}
				
				if(object.getLunchBreak()!=null)
				{
					min=min+"/"+object.getLunchBreak();
				}
				else
				{
					min=min+"/-";
				}
				
				min=min + " " + constants.min();
				return min;
				//return renderer.render(object.getShortBreak().toString()+"/"+object.getLongBreak().toString()+"/"+object.getLunchBreak().toString()+" min");
			}
		}, constants.osceBreak());
		
		
		
		//spec end 
		/*paths.add("OSCE");
		table.addColumn(new TextColumn<OsceProxy>() {

			Renderer<java.lang.Long> renderer = new AbstractRenderer<java.lang.Long>() {

				public String render(java.lang.Long obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				return renderer.render(object.getId());
			}
		}, "OSCE");
		paths.add("max");
		table.addColumn(new TextColumn<OsceProxy>() {

			Renderer<String> renderer = new AbstractRenderer<String>() {

				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				return renderer.render(object.getStudyYear().toString());
			}
		}, constants.studyYear());
		paths.add("preName");
		table.addColumn(new TextColumn<OsceProxy>() {

			Renderer<Integer> renderer = new AbstractRenderer<Integer>() {

				public String render(Integer obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				return renderer.render(object.getMaxNumberStudents());
			}
		}, constants.maxStudents());
		paths.add("email");
		table.addColumn(new TextColumn<OsceProxy>() {

			Renderer<Integer> renderer = new AbstractRenderer<Integer>() {

				public String render(Integer obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				return renderer.render(object.getNumberCourses());
			}
		}, constants.maxCircuits());
		//	        paths.add("responsibilities");
		//	        table.addColumn(new TextColumn<OsceProxy>() {
		//
		//	            Renderer<java.util.Set> renderer = ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.ResponsibilityProxyRenderer.instance());
		//
		//	            @Override
		//	            public String getValue(OsceProxy object) {
		//	                return renderer.render(object.getResponsibilities());
		//	            }
		//	        }, "Responsibilities");
*/	}

	@Override
	public CellTable<OsceProxy> getTable() {
		return table;
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

		/*splitLayoutPanel.animate(150000);
//		widthSize = 1200;
//		decreaseSize = 0;
//		splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		if (isDetailPlace) {

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
			widthSize = 1225;
			decreaseSize = 0;
			splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		}*/
//		splitLayoutPanel.setWidgetSize(westPanel, OsMaConstant.WIDTH_SIZE - OsMaConstant.WIDTH_MIN );
		ResolutionSettings.setSplitLayoutPanelAnimation(splitLayoutPanel);
		splitLayoutPanel.animate(OsMaConstant.ANIMATION_TIME);	
	}
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

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
