package ch.unibas.medizin.osce.client.a_nonroo.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SplitLayoutPanel;


public class ResolutionSettings {
	
	public static int getMainContainerWidth(){
		return Window.getClientWidth() - 10;
	}
	
	public static int getRightWidgetMarginLeft(){
		return (OsMaMainNav.getMenuStatus() == 0) ? 20 : 0;
	}
	
	public static int getRightWidgetHeight(){
		return Window.getClientHeight() - 40;
	}
	
	public static int getRightWidgetWidth(){
		
//		int menuOpenWidgetSize = (int)(Window.getClientWidth()*0.70);
//		int menuCloseWidgetSize = (int) (Window.getClientWidth()*0.94);
//		
//		Log.info(" menuOpenWidgetSize == " + menuOpenWidgetSize);
//		Log.info(" menuCloseWidgetSize == " + menuCloseWidgetSize);
		
//		return (OsMaMainNav.getMenuStatus() == 0) ? menuCloseWidgetSize : menuOpenWidgetSize;
		return Window.getClientWidth() - getSplitLayoutPanelLeft()-5;
	}
	
	public static int getSplitLayoutPanelLeft(){
		return (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
	}
	public static void setSplitLayoutPanelAnimation(SplitLayoutPanel splitLayoutPanel){
		
		int width = splitLayoutPanel.getWidget(0).getOffsetWidth();
		int minWidth = width/2;
		
		Log.info("(splitLayoutPanel.getOffsetWidth()/2) == ="+(splitLayoutPanel.getOffsetWidth()/2));
		Log.info("minWidth == ="+minWidth);
		Log.info("width == ="+width);
		
		if((width - (splitLayoutPanel.getOffsetWidth()/2)) > 100)
			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),width - minWidth);
		
	}
	public static void setSplitLayoutPanelPosition(SplitLayoutPanel splitLayoutPanel, boolean isInit){
		
		Log.info(" In DockMenuSettings Window.getClientWidth() == "+Window.getClientWidth());
		
		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		int openSplitSize = Window.getClientWidth() - (Window.getClientWidth()/6);
//		int menuOpenSplitSize = Window.getClientWidth() - (Window.getClientWidth()/6);
//		int menuCloseSplitSize = Window.getClientWidth() - (Window.getClientWidth()/50);
		
//		int halfScreen = (int) (Window.getClientWidth()*0.5);
		
		int halfScreen = (int) (getMainContainerWidth()*0.5);
		
//		int menuOpenSplitSize = (int) (Window.getClientWidth()*0.87);
//		int menuCloseSplitSize = (int) (Window.getClientWidth()*0.95);
		
//		int openSplitSize = Window.getClientWidth()-left;
		
		int openSplitSize = getMainContainerWidth()-left+2;
		
		Log.info("halfScreen == "+halfScreen);
		
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px; width: "+(openSplitSize)+"px");
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: relative; left: 0px; top: 0px; right: 5px; bottom: 0px; width: "+(openSplitSize)+"px");
		
		if(isInit){
			
			Log.info("in IS-INIT");
			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),openSplitSize);
//			if(OsMaMainNav.getMenuStatus() == 0)
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),menuCloseSplitSize);
//			else
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), menuOpenSplitSize);
		}
		
		Log.info("Window.getClientWidth() == "+Window.getClientWidth());
		
//		Log.info("menu closeSize "+ menuCloseSplitSize);
//		Log.info("menu openSize "+ menuOpenSplitSize);
		Log.info("openSplitSize "+ openSplitSize);
		Log.info("splitLayoutPanel.getWidget(0).getOffsetWidth() =="+splitLayoutPanel.getWidget(0).getOffsetWidth());
		
		if(splitLayoutPanel.getWidget(0).getOffsetWidth() >= halfScreen){
			Log.info("Window.getClientWidth() == "+Window.getClientWidth());
			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),openSplitSize);
//			if(OsMaMainNav.getMenuStatus() == 0)
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),menuCloseSplitSize);
//			else
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), menuOpenSplitSize);
		}
		
		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
	
		
	}
}