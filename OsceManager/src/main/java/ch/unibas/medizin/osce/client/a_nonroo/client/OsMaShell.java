/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author nikotsunami
 *
 */
public class OsMaShell extends Composite  {
	

	private static OsceShellUiBinder uiBinder = GWT
			.create(OsceShellUiBinder.class);

	interface OsceShellUiBinder extends UiBinder<Widget, OsMaShell> {
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
	
	@UiField
	DockLayoutPanel dockPanel;
	public OsMaShell() {
		initWidget(uiBinder.createAndBindUi(this));
		DOM.setElementAttribute(dockPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px;");
	}

	
	
//	@UiField
//	SimplePanel details;
//	@UiField
//	DivElement error;
//	@UiField
//	LoginWidget loginWidget;
	@UiField
	SimplePanel master;
	@UiField
	NotificationMole mole;
	@UiField
	SimplePanel headerPanel;
	
	/**
	 * 
	 * @return panel with language selector, login/logout-button, breadcrumb navigation
	 */
	public SimplePanel getHeaderPanel() {
		return headerPanel;
	}
	
//	/**
//	 * @return the panel to hold the details
//	 */
//	public SimplePanel getDetailsPanel() {
//		return details;
//	}

//	/**
//	 * @return the login widget
//	 */
//	public LoginWidget getLoginWidget() {
//		return loginWidget;
//	}

	/**
	 * @return the panel to hold the master list
	 */
	public SimplePanel getMainPanel() {
		return master;
	}

	/**
	 * @return the notification mole for loading feedback
	 */
	public NotificationMole getMole() {
		return mole;
	}
	
	@UiField //(provided = true)
	SimplePanel mainNav;

	public void setNavigation(OsMaMainNav nav) {
		mainNav.add(nav);
		
	}

	public void setHeader(OsMaHeader header) {
		headerPanel.add(header);
	}
}
