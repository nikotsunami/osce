package ch.unibas.medizin.osce.client.style.widgets;



import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link TabLayoutPanel} that shows scroll buttons if necessary
 */
public class ScrolledTabLayoutPanel extends TabLayoutPanel  {

        private static final int IMAGE_PADDING_PIXELS = 4;
        private int mouseWheelScrollPositionStep = 20;
        
        public TabBar demo=new TabBar();
        
        private LayoutPanel panel;
               public int offset;
        public FlowPanel tabBar;
      
        
        public HorizontalPanel tabBarFlowPanel = new HorizontalPanel();
       public TabBar b=new TabBar();
       public HorizontalPanel hh=new HorizontalPanel();
        public HorizontalPanel horizontalPanelForButton = new HorizontalPanel();
       // public HorizontalPanel h2= new HorizontalPanel();
        public ScrollPanel scrollPanel= new ScrollPanel();
        public FocusPanel focusForMouseWheelPanel = new FocusPanel();
        public IconButton left=new IconButton();
        public IconButton right=new IconButton();
        public IconButton down =new IconButton();
      
        
        public PopupPanel tabList=new PopupPanel();
        public ListBox tabLIstBox=new ListBox();
        public Image scrollLeftButton;
        public Image scrollRightButton;
        private HandlerRegistration windowResizeHandler;

        private ImageResource leftArrowImage;
        private ImageResource rightArrowImage;
        
        public int HPwidth;
        public boolean isAttachcall=false;
        
        
        
        public ScrolledTabLayoutPanel(double barHeight, Unit barUnit,
            ImageResource leftArrowImage, ImageResource rightArrowImage) {
                super(barHeight, barUnit);
             
                left.setIcon("triangle-1-w");
        		right.setIcon("triangle-1-e");
        		down.setIcon("triangle-1-s");
        	
                horizontalPanelForButton.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
                horizontalPanelForButton.setHeight((barHeight/2)+"px");
                horizontalPanelForButton.getElement().getStyle().setRight(0, Unit.PX);
                horizontalPanelForButton.getElement().getStyle().setBottom(0, Unit.PX);
                horizontalPanelForButton.add(down);
                horizontalPanelForButton.add(left);
                horizontalPanelForButton.add(right);
               
                
                horizontalPanelForButton.setWidth("50px");
               // horizontalPanelForButton.addStyleName("autoHeight");
                tabList.getElement().getStyle().setZIndex(2);
                tabList.add(tabLIstBox);
                tabLIstBox.setVisibleItemCount(5);
                tabList.setSize("100px", "100px");
             
                
                this.leftArrowImage = leftArrowImage;
                this.rightArrowImage = rightArrowImage;
                
                // The main widget wrapped by this composite, which is a LayoutPanel with the tab bar & the tab content
                panel = (LayoutPanel) getWidget();
                          // Find the tab bar, which is the first flow panel in the LayoutPanel
                for (int i = 0; i < panel.getWidgetCount(); ++i) {
                        Widget widget = panel.getWidget(i);
                        if (widget instanceof FlowPanel) {
                        	tabBar = (FlowPanel) widget;
                            tabBar.setStyleName("ScrolledTabLayoutPanel");
                           // tabBar.removeFromParent();
                           // tabBar.setWidth("600px"); 
                          //  tabBar.setWidth("1600px");    
                            tabBar.getElement().getStyle().setWidth(1600, Unit.PX);
                    
                            panel.remove(i); 
                           
                            scrollPanel.setAlwaysShowScrollBars(true);
                           // h2.setWidth("250px");
                            
                            scrollPanel.setStyleName("tabpanel_tabbar_no_scroll");
                            scrollPanel.add(tabBar);
                           // scrollPanel.addStyleName("autoHeight");
                           hh.setWidth(scrollPanel.getOffsetWidth()+"px");
                        
                            
                            //focusForMouseWheelPanel.add(scrollPanel);
                            hh.add(focusForMouseWheelPanel);
                            //focusForMouseWheelPanel.setWidth("3000px");
                            tabBarFlowPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
                            tabBarFlowPanel.getElement().getStyle().setVerticalAlign(VerticalAlign.BOTTOM);
                            //tabBarFlowPanel.add(focusForMouseWheelPanel);
                           
                           tabBarFlowPanel.add(horizontalPanelForButton);
                           Log.info("flow panel width--"+this.getElement().getStyle().getWidth());
                          // scrollPanel.setWidth(tabBarFlowPanel.getElement().getStyle().getWidth());
                           
                            tabBarFlowPanel.add(scrollPanel);
                                                      
                            panel.insert(tabBarFlowPanel,i);
                            panel.setHeight("41px");
                            panel.getElement().getStyle().setHeight(41, Unit.PX);
                            break; // tab bar found
                        }
                }

                // initScrollButtons(); //need to remove
                left.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						Log.info("left click");
						onresizecall();
						scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition()- mouseWheelScrollPositionStep);
					}
				});
                
                right.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						Log.info("right click");
						
						/*if(h2.getHorizontalScrollPosition() < HPwidth-50)
							h2.setHorizontalScrollPosition(h2.getHorizontalScrollPosition() + mouseWheelScrollPositionStep);
							*/
						/*if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()>(offset))
				    	{*/
						
						//if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()>(left.getAbsoluteLeft()+getOffsetWidth()-320))
						onresizecall();
						if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()+tabBar.getWidget(tabBar.getWidgetCount()-1).getOffsetWidth()>(left.getAbsoluteLeft()+scrollPanel.getOffsetWidth()))
				    	{
				    	//if(h2.getHorizontalScrollPosition() < HPwidth-50+10)
							System.out.println("total width--"+tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft());
							System.out.println("position--"+ scrollPanel.getHorizontalScrollPosition());
							scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition() + mouseWheelScrollPositionStep);
				    	}
					}
				});
                
                tabBar.addDomHandler(new MouseWheelHandler() {
					
					@Override
					public void onMouseWheel(MouseWheelEvent event) {
						
						
						
						if (event.isNorth()) {
							scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition()- mouseWheelScrollPositionStep);
						    } else if (event.isSouth()) {
						    	onresizecall();
						    	System.out.println("Left--"+ left.getAbsoluteLeft());
						    	/*if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()>(offset+50))
						    	{*/
						    	//if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()>(left.getAbsoluteLeft()+getOffsetWidth()-320))
						    	Log.info(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()+tabBar.getWidget(tabBar.getWidgetCount()-1).getOffsetWidth()+">"+(left.getAbsoluteLeft()+scrollPanel.getOffsetWidth()));
						    	if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()+tabBar.getWidget(tabBar.getWidgetCount()-1).getOffsetWidth()>(left.getAbsoluteLeft()+scrollPanel.getOffsetWidth()))
						    	{
						    	//if(h2.getHorizontalScrollPosition() < HPwidth-50+10)
						    		System.out.println("total width--"+tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft());
									System.out.println("position--"+ scrollPanel.getHorizontalScrollPosition());
						    		scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition() + mouseWheelScrollPositionStep);
						    	}
						    }
						    event.preventDefault();
					
								
					}
				}, MouseWheelEvent.getType());
                
                down.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						Log.info("on click down");
						if(tabList.isShowing())
						{
							tabList.hide();
						}
						else
						{
						int x = ((Widget)event.getSource()).getAbsoluteLeft();
						int y = ((Widget)event.getSource()).getAbsoluteTop()+down.getOffsetHeight();
						tabList.setPopupPosition(x, y);
																
						tabList.show();
						}
					}
						
				});
               
                tabLIstBox.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						Log.info("selected index--"+tabLIstBox.getSelectedIndex());
						tabList.hide();
						onresizecall();
						if(tabLIstBox.getSelectedIndex()==-1)
						{
						return;	
						}
						
						selectTab(tabLIstBox.getSelectedIndex());
						
						//scrollPanel.setHorizontalScrollPosition(scrollPanel.getHorizontalScrollPosition() + mouseWheelScrollPositionStep);
						
						scrollPanel.setHorizontalScrollPosition(0);
						
					//	scrollPanel.setHorizontalScrollPosition(tabBar.getWidget(getSelectedIndex()).getElement().getOffsetLeft());
										
						for(int i=0;i<=tabBar.getWidget(getSelectedIndex()).getElement().getOffsetLeft();i=i+20)
						{
							/*if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()>(offset))
					    	{*/
							//if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()>(left.getAbsoluteLeft()+getOffsetWidth()-320))
							if(tabBar.getWidget(tabBar.getWidgetCount()-1).getAbsoluteLeft()+tabBar.getWidget(tabBar.getWidgetCount()-1).getOffsetWidth()>(left.getAbsoluteLeft()+scrollPanel.getOffsetWidth()))
					    	{
					    	//if(h2.getHorizontalScrollPosition() < HPwidth-50+10)
								System.out.println("in loop position--"+i);
								scrollPanel.setHorizontalScrollPosition(i);
					    	}
			         		 
						}
						
						
					}
				});
                
              tabLIstBox.addDoubleClickHandler(new DoubleClickHandler() {
					
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						// TODO Auto-generated method stub
					
					
					}
				});
                
                tabList.addDomHandler(new MouseOutHandler() {
					
					@Override
					public void onMouseOut(MouseOutEvent event) {
						// TODO Auto-generated method stub
						tabList.hide();
					}
				}, MouseOutEvent.getType());
              
        		   
        }
        
      /*  public void setscrollwidth(int width1)
        {
        	scrollPanel.setWidth(width1+"px");
        }
        */
        @Override
        
        public boolean remove(int index) {
        	
        	Boolean result=super.remove(index);       	
        	Log.info("remove call");
        	tabBar.remove(index);
        	tabLIstBox.removeItem(index);      	
        	return result;
        	//return true;
        	
        	
        };
      
       
       public int getSelectedIndex() {
    	
    	  return super.getSelectedIndex();
       };
        
        
        public void selectTab(int index) {
        	super.selectTab(index);
        	
        };
        
        @Override
        public void insert(Widget child, String text, int beforeIndex) {
        	super.insert(child, text, beforeIndex);
        	Log.info("insert call----"+text);
            tabLIstBox.addItem(text);
           
            
        };
        
       /* @Override
        public void add(Widget child, String text) {
        // TODO Auto-generated method stub
        super.add(child, text);
         
        }*/

    
        @Override
        public boolean remove(Widget w) {
                boolean b = super.remove(w);
                checkIfScrollButtonsNecessary();
                return b;
        }

        public void onresizecall()
        {
        	Log.info("resize---"+this.getOffsetWidth());
        	scrollPanel.setWidth((this.getOffsetWidth()-40)+"px");
        }
        @Override
        protected void onLoad() {
                super.onLoad();

                if (windowResizeHandler == null) {
                        windowResizeHandler = Window.addResizeHandler(new ResizeHandler() {
                                @Override
                                public void onResize(ResizeEvent event) {
                                        checkIfScrollButtonsNecessary();
                                        onresizecall();
                                }
                        });
                }
           
                horizontalPanelForButton.setWidth("50px");
                //panel.setWidgetRightWidth(focusForMouseWheelPanel, -(HPwidth-20), Unit.PX, HPwidth, Unit.PX);
               
                
                HPwidth=0;
               // tabBar.addStyleName("ScrolledTabLayoutPanel");
           	tabBar.setStyleName("ScrolledTabLayoutPanel");
           	//System.out.println("ON attach call--"+isAttachcall);
           	int maxWidth=0;
         	 
         	   int widgetCount = tabBar.getWidgetCount();
         	  for(int i=0;i< tabBar.getWidgetCount();i++)
               {
         		  
         		  if(maxWidth<tabBar.getWidget(i).getOffsetWidth())
         			  maxWidth=tabBar.getWidget(i).getOffsetWidth();
         	 HPwidth=HPwidth+tabBar.getWidget(i).getOffsetWidth()-28;
         		
               }  
         	  HPwidth=HPwidth-50;
         	
         	  tabBar.setWidth(10000 + "px");
         
         	 scrollPanel.setAlwaysShowScrollBars(true);
         	 // focusForMouseWheelPanel.setWidth("100%");
         	  
         	 offset=this.getOffsetWidth();
         	// Window.alert("value--"+this.getOffsetWidth());
         	Log.info("resize---"+this.getOffsetWidth());
         	//onresizecall();
        }
        
       
        
        @Override
        protected void onDetach() {
        	super.onDetach();    
        	
        }
        /*
        
          
        @Override
        protected void onAttach() {
        	super.onAttach();
        	HPwidth=getTabbarWidth();
            //HPwidth = this.getOffsetWidth() - 50;
            h2.setWidth((HPwidth)+"px");
            focusForMouseWheelPanel.setWidth("100%"); 
            h1.getParent().setWidth("50px");      	  
        }
        
        @Override
        protected void onDetach() {
        	super.onDetach();
        }
        
        */
        private int getTabbarWidth1(){
        	HPwidth=0;
        	
        	 
        	  for(int i=0;i< tabBar.getWidgetCount();i++)
              {
        		  
        		  
        		  //HPwidth=HPwidth+tabBar.getWidget(i).getOffsetWidth();
        		  HPwidth=HPwidth+(tabBar.getWidget(i).getElement().getParentElement().getClientWidth()-14);
              }        	  
        	
        	  return HPwidth;
        }

        @Override
        protected void onUnload() {
                super.onUnload();

                if (windowResizeHandler != null) {
                        windowResizeHandler.removeHandler();
                        windowResizeHandler = null;
                }
        }

        private ClickHandler createScrollClickHandler(final int diff) {
                return new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                                Widget lastTab = getLastTab();
                                if (lastTab == null)
                                        return;

                                int newLeft = parsePosition(scrollPanel.getElement().getStyle().getLeft()) + diff;
                                int rightOfLastTab = getRightOfWidget(lastTab);

                                // Prevent scrolling the last tab too far away form the right border,
                                // or the first tab further than the left border position
                                if (newLeft <= 0 && (getTabBarWidth() - newLeft < (rightOfLastTab + 20))) {
                                	
                                        scrollTo(newLeft);
                                }
                        }
                };
        }

        /** Create and attach the scroll button images with a click handler */
        private void initScrollButtons() {
                scrollLeftButton = new Image(leftArrowImage);
                
                int leftImageWidth = scrollLeftButton.getWidth();
                panel.insert(scrollLeftButton, 0);                
                panel.setWidgetLeftWidth(scrollLeftButton, 0, Unit.PX, leftImageWidth, Unit.PX);
                panel.setWidgetTopHeight(scrollLeftButton, 0, Unit.PX, scrollLeftButton.getWidth(), Unit.PX);
                scrollLeftButton.addClickHandler(createScrollClickHandler(+20));
                scrollLeftButton.setVisible(false);

                scrollRightButton = new Image(rightArrowImage);
                panel.insert(scrollRightButton, 0);
                panel.setWidgetLeftWidth(scrollRightButton, leftImageWidth + IMAGE_PADDING_PIXELS, Unit.PX, scrollRightButton.getWidth(), Unit.PX);
                panel.setWidgetTopHeight(scrollRightButton, 0, Unit.PX, scrollRightButton.getHeight(), Unit.PX);
                
                scrollRightButton.addClickHandler(createScrollClickHandler(-20));
                scrollRightButton.setVisible(false);
        }

        private void checkIfScrollButtonsNecessary() {
                // Defer size calculations until sizes are available, when calculating immediately after
                // add(), all size methods return zero
                DeferredCommand.addCommand(new Command() {
                        @Override
                        public void execute() {
                                boolean isScrolling = isScrollingNecessary();
                                // When the scroll buttons are being hidden, reset the scroll position to zero to
                                // make sure no tabs are still out of sight
                                /*if (scrollRightButton.isVisible() && !isScrolling) {
                                        resetScrollPosition();
                                }
                                scrollRightButton.setVisible(isScrolling);
                                scrollLeftButton.setVisible(isScrolling);*/
                        }
                });
        }

        @Override
        public void animate(int duration) {
        // TODO Auto-generated method stub
        super.animate(duration);
        }
        
        private void resetScrollPosition() {
                scrollTo(0);
        }

        private void scrollTo(int pos) {
        	Log.info("inside scrolled to method");
        	scrollPanel.getElement().getStyle().setLeft(pos, Unit.PX);
               
               
        }

        private boolean isScrollingNecessary() {
                Widget lastTab = getLastTab();
                if (lastTab == null)
                        return false;

                return getRightOfWidget(lastTab) > getTabBarWidth();
        }

        private int getRightOfWidget(Widget widget) {
                return widget.getElement().getOffsetLeft() + widget.getElement().getOffsetWidth();
        }

        private int getTabBarWidth() {
                return scrollPanel.getElement().getParentElement().getClientWidth();
        }

        private Widget getLastTab() {
                if (tabBar.getWidgetCount() == 0)
                        return null;

                return tabBar.getWidget(tabBar.getWidgetCount() - 1);
        }

        private static int parsePosition(String positionString) {
                int position;
                try {
                        for (int i = 0; i < positionString.length(); i++) {
                                char c = positionString.charAt(i);
                                if (c != '-' && !(c >= '0' && c <= '9')) {
                                        positionString = positionString.substring(0, i);
                                }
                        }

                        position = Integer.parseInt(positionString);
                } catch (NumberFormatException ex) {
                        position = 0;
                }
                return position;
        }
        
              private class WheelTabScroll implements MouseWheelHandler {
	@Override
	public void onMouseWheel(MouseWheelEvent event) {
	    if (event.isNorth()) {
	/*	getScrollPanel().setHorizontalScrollPosition(
			getScrollPanel().getHorizontalScrollPosition()
				- mouseWheelScrollPositionStep);*/
	    } else if (event.isSouth()) {
	/*	getScrollPanel().setHorizontalScrollPosition(
			getScrollPanel().getHorizontalScrollPosition()
				+ mouseWheelScrollPositionStep);
				*/
	    }
	    event.preventDefault();
	}
    }

         
}
