package ch.unibas.medizin.osce.client.style.widgets.tooltip;

import java.lang.Math;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TooltipPanel extends PopupPanel implements PopupPanel.PositionCallback {
	
	public static interface Style extends ClientBundle {
		public static interface Css extends CssResource {
			public String tooltipPanel();
			public String tooltipPanelParent();
			public String tooltipPanelChild();
//			public String popup();
		}
		
		@Source("TooltipPanel.css")
		public Css css();
	}
	
	public static interface PointerImages extends ClientBundle {
		@Source("images/popupTriangle-n.png")
		public ImageResource popupTriangleN();
		
		@Source("images/popupTriangle-s.png")
		public ImageResource popupTriangleS();
	}
	
	private static final int POINTER_OFFSET = 30;
	
	private static final PointerImages images = GWT.create(PointerImages.class);
	private static final Style style = GWT.create(Style.class);
//	private SimplePanel widgetContainer;
	private VerticalPanel vPanel;
	private Image triangle;
	private int ptrPosition;
	
	private UIObject referenceObject;
	private Align align;
	private Position position;
		
	public static enum Position {
		ABOVE, BELOW
	}
	
	public static enum Align {
		LEFT, RIGHT
	}
	
	public TooltipPanel(UIObject reference) {
		this(reference, Position.ABOVE, Align.LEFT);
	}
	
	public TooltipPanel(UIObject reference, Align align) {
		this(reference, Position.ABOVE, align);
	}
	
	public TooltipPanel(UIObject reference, Position pos) {
		this(reference, pos, Align.LEFT);
	}
	
	public TooltipPanel(UIObject reference, Position pos, Align align) {
		super();
		this.referenceObject = reference;
		this.position = pos;
		this.align = align;
		
		initPanelStructure(pos);
	}
	
	/**
	 * Has to be called before anything else!
	 * @param pos
	 */
	private void initPanelStructure(Position pos) {
		// initialize inner panels
//		widgetContainer = new SimplePanel();
		vPanel = new VerticalPanel();
		
		// initialize popup panel and add the inner panels as children
		this.setAnimationEnabled(false);
		this.setAutoHideEnabled(true);
		this.setGlassEnabled(false);
		super.setWidget(vPanel);
		this.setStyleName(style.css().tooltipPanelParent());
//		widgetContainer.setStyleName(style.css().tooltipPanel());
		style.css().ensureInjected();
	}
	
	@Override
	public void setWidget(Widget widget) {
		attachWidget(widget);
	}
	
	public void setPopupPositionAndShow() {
		if (getWidget() == null) throw new IllegalStateException("No widget is attached to the panel. Attach a widget via initWidget()");
		Log.info("setPopupPositionAndShow()");
//		setPopupPositionAndShow(new PositionCallbackImpl());
//		PositionCallback callback = this;
		show();
//		callback.setPosition(getOffsetWidth(), getOffsetHeight());
	}
	
	@Override
	public void onLoad() {
		setPosition(getOffsetWidth(), getOffsetHeight());
	}
	
//	public void hide() {
//		super.hide();
//	}
	
	private void attachWidget(Widget widget) {
//		widgetContainer.setWidget(widget);
		
		while (vPanel.getWidgetCount() > 0) {
			vPanel.remove(0);
		}
		
		if (position == Position.BELOW) {
			triangle = new Image(images.popupTriangleN());
			vPanel.add(triangle);
			vPanel.add(widget);
		} else {
			triangle = new Image(images.popupTriangleS());
			vPanel.add(widget);
			vPanel.add(triangle);
		}
		
		widget.addStyleName(style.css().tooltipPanelChild());
	}
	
	@Override
	public boolean remove(Widget w) {
		// TODO implment
		return false;
	}
	
	private void positionPointer(int offsetWidth) {
		if (align == Align.LEFT) {
			triangle.getElement().getStyle().setPropertyPx("marginLeft", POINTER_OFFSET);
		} else {
			triangle.getElement().getStyle().setPropertyPx("marginLeft", offsetWidth - POINTER_OFFSET - triangle.getOffsetWidth());
		}
	}
	
	@Override
	public void setPosition(int offsetWidth, int offsetHeight) {
		int left = referenceObject.getAbsoluteLeft();
		int top = referenceObject.getAbsoluteTop();
		int right = referenceObject.getAbsoluteLeft() + referenceObject.getOffsetWidth();
		
		Log.info("ref left: " + left);
		Log.info("ref top: " + top);
		Log.info("ref right: " + right);
		Log.info("client width: " + Window.getClientWidth());
		
		if (align == Align.RIGHT) {
			left = Math.min(right - offsetWidth, Window.getClientWidth() - offsetWidth);
			Log.info("ref width: " + referenceObject.getOffsetWidth());
			Log.info("popupWidth: " + offsetWidth);
		}
		
		if (position == Position.ABOVE) {
			top = top - offsetHeight;
		} else {
			top = top + referenceObject.getOffsetHeight();
		}
		TooltipPanel.this.positionPointer(offsetWidth);
		TooltipPanel.this.setPopupPosition(left, top);
	}
}
