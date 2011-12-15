package ch.unibas.medizin.osce.client.style.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * Class aiding in customization of the TabPanel.
 * @author michael
 */
public class TabPanelHelper {
	/**
	 * Moves the TabBar of the given panel down, below the content.
	 * @param panel The TabPanel to be modified
	 */
	public static void moveTabBarToBottom(TabPanel panel) {
		// reorder the Tab- and Content-Panels
		Node tabTable = panel.getElement().getFirstChild();
		Node contentPanel = tabTable.getLastChild();
		tabTable.removeChild(contentPanel);
		tabTable.insertFirst(contentPanel);
	}
	
	/**
	 * Hack to reorder z-index layering of tabs in tabbar so that the leftmost tab is on top.
	 * @param panel
	 */
	public static void reorderTabs(TabPanel panel) {
		Element tabBar = panel.getTabBar().getElement();
		int numChildren = panel.getTabBar().getTabCount();
		for (int i=0; i < numChildren; i++) {
			Element tab = (Element)tabBar.getChild(0).getChild(0).getChild(i+1).getChild(0);
			tab.setAttribute("style", tab.getAttribute("style") + "; z-index: " + (numChildren - i) + ";");
		}
	}
}
