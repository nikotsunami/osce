package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceViewImpl extends Composite implements ManualOsceView, MenuClickHandler {

	private static ManualOsceViewImplUiBinder uiBinder = GWT.create(ManualOsceViewImplUiBinder.class);
	
	interface ManualOsceViewImplUiBinder extends UiBinder<Widget, ManualOsceViewImpl> {
	}
	
	private Delegate delegate;
	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	
	public ImageResource icon1 = uiIcons.triangle1West(); 
	
	public ImageResource icon2=  uiIcons.triangle1East();
	
	Unit unit = Unit.PX;

	@UiField(provided=true)
	ScrolledTabLayoutPanel osceTabPanel = new ScrolledTabLayoutPanel(40L, unit, icon1, icon2);
	
	@UiField
	HTMLPanel mainHTMLPanel;
	
	public ManualOsceViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		mainHTMLPanel.add(osceTabPanel);
		mainHTMLPanel.addStyleName("horizontalPanelStyle");
		init();
	}

	public void init(){
		int marginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		int width = ResolutionSettings.getRightWidgetWidth();
		int height = ResolutionSettings.getRightWidgetHeight();
		
		mainHTMLPanel.setWidth(width + "px");
		mainHTMLPanel.setHeight(height + "px");
		
		mainHTMLPanel.getElement().setAttribute("style", "margin-left: "+marginLeft+"px; width : "+width+"px;height : "+height+"px;");
		
		osceTabPanel.setWidth(width + "px");
		osceTabPanel.setHeight(height + "px");
	}
		
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		int panelMarginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		mainHTMLPanel.getElement().setAttribute("style", "margin-left: "+panelMarginLeft+"px; width : " + ResolutionSettings.getRightWidgetWidth() + "px; height : " + ResolutionSettings.getRightWidgetHeight() + "px;");
	}

	public ScrolledTabLayoutPanel getOsceTabPanel() {
		return osceTabPanel;
	}
	
	public int getViewWidth()
	{
		return ResolutionSettings.getRightWidgetWidth();
	}
	
	public int getViewHeight()
	{
		return ResolutionSettings.getRightWidgetHeight();
	}
	
	public HTMLPanel getMainHTMLPanel() {
		return mainHTMLPanel;
	}
}
