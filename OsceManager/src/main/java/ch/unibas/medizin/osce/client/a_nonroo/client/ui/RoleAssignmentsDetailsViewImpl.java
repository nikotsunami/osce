/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class RoleAssignmentsDetailsViewImpl extends Composite implements RoleAssignmentsDetailsView {

	private static RoleAssignmentsDetailsViewImplUiBinder uiBinder = GWT
			.create(RoleAssignmentsDetailsViewImplUiBinder.class);

	interface RoleAssignmentsDetailsViewImplUiBinder extends UiBinder<Widget, RoleAssignmentsDetailsViewImpl> {
	}

	private Delegate delegate;

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
	public RoleAssignmentsDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}
	
	public void init() {
		// TODO implement this!
	}

	private Presenter presenter;

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter nationalityActivity) {
		this.presenter =  nationalityActivity;
	}

	public Widget asWidget() {
		return this;
	}
}
