/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class IndividualSchedulesViewImpl extends Composite implements IndividualSchedulesView {

	private static IndividualSchedulesViewUiBinder uiBinder = GWT
			.create(IndividualSchedulesViewUiBinder.class);

	interface IndividualSchedulesViewUiBinder extends UiBinder<Widget, IndividualSchedulesViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	// Module10 Create plans
	@UiField
	TabPanel osceTab;
	// E Module10 Create plans
	
	
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
	public IndividualSchedulesViewImpl() 
	{
		Log.info("Call IndividualSchedulesViewImpl");
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() 
	{
		Log.info("Call init()");
	
		
	}
	

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	// Module10 Create plans
	@Override
	public TabPanel getosceTab()
	{
		return this.osceTab;
	}
	// E Module10 Create plans
}
