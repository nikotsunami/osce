/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class StudentsViewImpl extends Composite implements StudentsView, MenuClickHandler {

	private static StudentsViewUiBinder uiBinder = GWT
			.create(StudentsViewUiBinder.class);

	interface StudentsViewUiBinder extends UiBinder<Widget, StudentsViewImpl> {
	}

	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	public ImageResource icon1 = uiIcons.triangle1West(); 
	public ImageResource icon2=  uiIcons.triangle1East();
		
	Unit u=Unit.PX;
	
	/*@UiField(provided=true)
	ScrolledTabLayoutPanel scrollpanel=new ScrolledTabLayoutPanel(40L, u, icon1, icon2);
	*/
	@UiField(provided=true)
	ScrolledTabLayoutPanel studentTabPanel1=new ScrolledTabLayoutPanel(40L, u, icon1, icon2);
	
	@UiField
	HorizontalPanel horizontalStudentTabPanel1;
	
	@UiField
	HorizontalPanel containerPanel;
	
	
	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
/*
	@UiField
	TabPanel studentTabPanel;
	*/
	
	
	/*@UiField
	HorizontalPanel test1;
	*/
	/*
	@UiField
	TabPanel firstcheck;
	*/
	/*@UiField
	SimplePanel name;*/
	
	public SimplePanel studentDetailPanel = new SimplePanel();
	
	public Button b=new Button("click");
	
	public static int hpwidth=0;
	
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
	public StudentsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();

		
		
	
		
		//test1.add(scrollpanel);
		studentTabPanel1.setHeight("500px");
		//studentTabPanel1.setWidth("800px");
		horizontalStudentTabPanel1.addStyleName("horizontalPanelStyle");
		
		IconButton b=new IconButton();
		b.setText("test");
		b.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Log.info("button click");
			}
		});
		
		
		
		
		
		Log.info("image resources1--"+icon1);
		Log.info("image resources2--"+icon2);
		
		
	
		
		
		
		
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		
		
		int panelMarginLeft = ResolutionSettings.getRightWidgetMarginLeft();		
		containerPanel.getElement().setAttribute("style", "margin-left: "+panelMarginLeft+"px; width : 600px;");

	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*@Override
	public TabPanel getStudentTabPanel(){
		//return this.studentTabPanel;
		return null;
		
	}*/
	
	/*@Override
	public SimplePanel getNamePanel(){
		return this.name;
		
	}*/
	
	@Override
	public SimplePanel getStudentDetailPanel(){
		return this.studentDetailPanel;
		
	}

	@Override
	public ScrolledTabLayoutPanel getStudentTabPanel1() {
		// TODO Auto-generated method stub
		return studentTabPanel1;
	}
	

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
		int panelMarginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		containerPanel.getElement().setAttribute("style", "margin-left: "+panelMarginLeft+"px; width : 600px;");
	}

}
