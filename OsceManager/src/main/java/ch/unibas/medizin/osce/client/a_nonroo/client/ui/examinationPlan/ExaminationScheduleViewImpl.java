/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class ExaminationScheduleViewImpl extends Composite implements ExaminationScheduleView, MenuClickHandler {

	private static ExaminationScheduleViewUiBinder uiBinder = GWT
			.create(ExaminationScheduleViewUiBinder.class);

	interface ExaminationScheduleViewUiBinder extends UiBinder<Widget, ExaminationScheduleViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	@UiField
	public TabPanel osceTabPanel;
	
	
	
	
	@UiField
	public HTMLPanel containerHTMLPanel;

	public TabPanel getOsceTabPanel() {
		return osceTabPanel;
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
	public ExaminationScheduleViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		
		int panelMarginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		int panelWidth = ResolutionSettings.getRightWidgetWidth();
		int height = ResolutionSettings.getRightWidgetHeight();
		containerHTMLPanel.getElement().setAttribute("style", "margin-left: "+panelMarginLeft+"px; width: "+panelWidth+"px; height: "+height+"px;");
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
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
		int panelMarginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		int panelWidth = ResolutionSettings.getRightWidgetWidth();
		int height = ResolutionSettings.getRightWidgetHeight();
		containerHTMLPanel.getElement().setAttribute("style", "margin-left: "+panelMarginLeft+"px; width: "+panelWidth+"px; height: "+height+"px;");
	}
}
