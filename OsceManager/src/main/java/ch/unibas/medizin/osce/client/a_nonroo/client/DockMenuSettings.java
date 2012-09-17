package ch.unibas.medizin.osce.client.a_nonroo.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SplitLayoutPanel;


public class DockMenuSettings {
	
	public static int getRightWidgetMarginLeft(){
		return (OsMaMainNav.getMenuStatus() == 0) ? 20 : 0;
	}
	
	public static int getRightWidgetWidth(){
		
		int menuOpenWidgetSize = (int)(Window.getClientWidth()*0.79);
		int menuCloseWidgetSize = (int) (Window.getClientWidth()*0.93);
		
		Log.info(" menuOpenWidgetSize == " + menuOpenWidgetSize);
		Log.info(" menuCloseWidgetSize == " + menuCloseWidgetSize);
		
		return (OsMaMainNav.getMenuStatus() == 0) ? menuCloseWidgetSize : menuOpenWidgetSize;
	}
	
	public static int getSplitLayoutPanelLeft(){
		return (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
	}
//	public static void setSplitLayoutPanelPosition(SplitLayoutPanel splitLayoutPanel){}
	public static void setSplitLayoutPanelPosition(SplitLayoutPanel splitLayoutPanel, boolean isInt){
		
		Log.info(" In DockMenuSettings Window.getClientWidth() == "+Window.getClientWidth());
		
		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		int openSplitSize = Window.getClientWidth() - (Window.getClientWidth()/6);
//		int menuOpenSplitSize = Window.getClientWidth() - (Window.getClientWidth()/6);
//		int menuCloseSplitSize = Window.getClientWidth() - (Window.getClientWidth()/50);
		
		int openSplitSize = (int) (Window.getClientWidth()*0.55);
		int menuOpenSplitSize = (int) (Window.getClientWidth()*0.80);
		int menuCloseSplitSize = (int) (Window.getClientWidth()*0.95);
		
		Log.info("openSplitSize == "+openSplitSize);
		
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
		
		if(isInt){
			
			Log.info("in IS-INIT");
			
			if(OsMaMainNav.getMenuStatus() == 0)
				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),menuCloseSplitSize);
			else
				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), menuOpenSplitSize);
		}
		
		Log.info("Window.getClientWidth() == "+Window.getClientWidth());
		
		Log.info("menu closeSize "+ menuCloseSplitSize);
		Log.info("menu openSize "+ menuOpenSplitSize);
		
		if(splitLayoutPanel.getWidget(0).getOffsetWidth() >= openSplitSize){
			Log.info("Window.getClientWidth() == "+Window.getClientWidth());
			if(OsMaMainNav.getMenuStatus() == 0)
				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),menuCloseSplitSize);
			else
				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), menuOpenSplitSize);
		}
		
		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
	
		
	}
}