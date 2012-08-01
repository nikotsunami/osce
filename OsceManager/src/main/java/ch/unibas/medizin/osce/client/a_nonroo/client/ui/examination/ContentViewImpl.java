package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;


import ch.unibas.medizin.osce.client.a_nonroo.client.activity.CircuitDetailsActivity;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentViewImpl extends Composite implements  ContentView{

	private Delegate delegate;
	
	@UiField
	HorizontalPanel postHP;
	
	public HorizontalPanel getPostHP() {
		return postHP;
	}
	
	@UiField
	 ScrollPanel  scrollPanel;
	
	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}

	

	@UiField
	 VerticalPanel contentPanel;
	
	public VerticalPanel getContentPanel() {
		return contentPanel;
	}

	

	@UiField
	HorizontalPanel oscePostHP;
	
	private CourseProxy courseProxy;
	
	
	public CourseProxy getCourseProxy() {
		return courseProxy;
	}



	public void setCourseProxy(CourseProxy courseProxy) {
		this.courseProxy = courseProxy;
	}



	public HorizontalPanel getOscePostHP() {
		return oscePostHP;
	}

	

	public void setPostHP(HorizontalPanel postHP) {
		this.postHP = postHP;
	}

	@UiField
	AbsolutePanel postAP;
	
	private CircuitDetailsActivity dragHandler;
	
	PickupDragController dragController;
	
	public PickupDragController getDragController() {
		return dragController;
	}

	public void setDragController(PickupDragController dragController) {
		this.dragController = dragController;
	}

	HorizontalPanelDropController dropController;
	
	public HorizontalPanelDropController getDropController() {
		return dropController;
	}

	public void setDropController(HorizontalPanelDropController dropController) {
		this.dropController = dropController;
	}

	private static ContentViewImplUiBinder uiBinder = GWT
			.create(ContentViewImplUiBinder.class);

	interface ContentViewImplUiBinder extends UiBinder<Widget, ContentViewImpl> {
	}
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}
	
	public ContentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		dragController=new PickupDragController(postAP, false);
    
   // dragController.setBehaviorDragProxy(true);
    

		dropController=new HorizontalPanelDropController(postHP);//set target
		dragController.registerDropController(dropController);
		dragController.setBehaviorScrollIntoView(true);
		
	}

	

	
}
