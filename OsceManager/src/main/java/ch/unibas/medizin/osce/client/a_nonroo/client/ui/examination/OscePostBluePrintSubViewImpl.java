
/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashSet;
import java.util.Set;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class OscePostBluePrintSubViewImpl extends Composite implements OscePostBluePrintSubView {

	private static OscePostBluePrintSubViewUiBinder uiBinder = GWT
			.create(OscePostBluePrintSubViewUiBinder.class);

	interface OscePostBluePrintSubViewUiBinder extends UiBinder<Widget, OscePostBluePrintSubViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	@UiField
	public HorizontalPanel oscePostBluePrintSubViewImplHP;
	
	@UiField
	public HorizontalPanel newPostHP;
	
	

	PickupDragController dragController;
	
	public PickupDragController getDragController() {
		return dragController;
	}

	HorizontalPanelDropController dropController;
	
	@Override
	public HorizontalPanel getOscePostBluePrintSubViewImplHP()
	{
		return this.oscePostBluePrintSubViewImplHP;
	}
	
	@UiField
	public AbsolutePanel oscePostBluePrintSubViewImplAP;
	
	@Override
	public AbsolutePanel getOscePostBluePrintSubViewImplAP()
	{
		return this.oscePostBluePrintSubViewImplAP;
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
	
	public OscePostBluePrintSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		dragController=new PickupDragController(oscePostBluePrintSubViewImplAP, false);
	    
		   // dragController.setBehaviorDragProxy(true);
		    

				dropController=new HorizontalPanelDropController(oscePostBluePrintSubViewImplHP);//set target
				dragController.registerDropController(dropController);
				dragController.setBehaviorScrollIntoView(true);
		init();
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// TODO implement this!
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
		
	
}
