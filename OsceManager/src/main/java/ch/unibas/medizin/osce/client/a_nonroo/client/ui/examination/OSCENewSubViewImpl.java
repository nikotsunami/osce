
/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashSet;
import java.util.Set;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class OSCENewSubViewImpl extends Composite implements OSCENewSubView {

	private static OSCENewSubViewUiBinder uiBinder = GWT
			.create(OSCENewSubViewUiBinder.class);

	interface OSCENewSubViewUiBinder extends UiBinder<Widget, OSCENewSubViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	@UiField
	HorizontalPanel OSCENewSubViewImplHP;
	
	@UiField
	OsceDayViewImpl osceDayViewImpl;
	
	
	public OsceDayViewImpl getOsceDayViewImpl() {
		return osceDayViewImpl;
	}

	public void setOsceDayViewImpl(OsceDayViewImpl osceDayViewImpl) {
		this.osceDayViewImpl = osceDayViewImpl;
	}
	
	@UiField
	OscePostBluePrintSubViewImpl oscePostBluePrintSubViewImpl;
	
		
	@Override
	public OscePostBluePrintSubViewImpl getOscePostBluePrintSubViewImpl()
	{
		Log.info("Call getOscePostBluePrintSubViewImpl()");
		return this.oscePostBluePrintSubViewImpl;
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
	
	@Override
	public HorizontalPanel getOSCENewSubViewImplHP()
	{
		return this.OSCENewSubViewImplHP;
	}
	
	public OSCENewSubViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
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
