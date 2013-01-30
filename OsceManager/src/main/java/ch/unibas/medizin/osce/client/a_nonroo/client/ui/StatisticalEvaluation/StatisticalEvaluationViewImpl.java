/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class StatisticalEvaluationViewImpl extends Composite implements StatisticalEvaluationView,MenuClickHandler {

	private static StatisticalEvaluationViewUiBinder uiBinder = GWT
			.create(StatisticalEvaluationViewUiBinder.class);

	interface StatisticalEvaluationViewUiBinder extends UiBinder<Widget, StatisticalEvaluationViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	public ImageResource icon1 = uiIcons.triangle1West(); 
	public ImageResource icon2=  uiIcons.triangle1East();
	Unit u=Unit.PX;
	
	@UiField
	HTMLPanel mainHTMLPanel;

	@UiField
	HorizontalPanel horizontalStatisticalEvaluationTabPanel;
	
	
	@UiField(provided=true)
	ScrolledTabLayoutPanel statisticalEvaluationTabPanel=new ScrolledTabLayoutPanel(40L, u, icon1, icon2);
	
	//ScrolledTab Changes end
	
	
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
	public StatisticalEvaluationViewImpl() 
	{
		Log.info("Call StatisticalEvaluationViewImpl");
		initWidget(uiBinder.createAndBindUi(this));
		horizontalStatisticalEvaluationTabPanel.add(statisticalEvaluationTabPanel);
		
//		circuitTabPanel.setHeight("720px");
		//circuitTabPanel.setWidth("1240px");
		horizontalStatisticalEvaluationTabPanel.addStyleName("horizontalPanelStyle");
		init();
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() 
	{
		int marginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		int width = ResolutionSettings.getRightWidgetWidth();
		int height = ResolutionSettings.getRightWidgetHeight();
		Log.info("height =============\\\\\\"+height);
		Log.info("width =============\\\\\\"+width);
		Log.info("DockMenuSettings.getRightWidgetWidth()  == = = =" + ResolutionSettings.getRightWidgetWidth());
		mainHTMLPanel.getElement().setAttribute("style", "margin-left: "+marginLeft+"px; width : "+width+"px;height : "+height+"px;");
		
		statisticalEvaluationTabPanel.setHeight(height+"px");
	
		
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
	public ScrolledTabLayoutPanel getEvaluationTab(){
		return this.statisticalEvaluationTabPanel;
		
	}

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
		int marginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		int width = ResolutionSettings.getRightWidgetWidth();
		int height = ResolutionSettings.getRightWidgetHeight();
		
		Log.info("width =============\\\\\\"+width);
		
		mainHTMLPanel.getElement().setAttribute("style", "margin-left: "+marginLeft+"px; width : "+width+"px;height : "+height+"px;");
	}
}
