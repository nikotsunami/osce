package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView.Delegate;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView.Presenter;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsViewImpl.TopicsAndSpecDetailsViewImplUiBinder;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class RoleScriptTemplateDetailsViewImpl extends Composite implements RoleScriptTemplateDetailsView{

	private static RoleScriptTemplateDetailsViewImplUiBinder uiBinder = GWT
			.create(RoleScriptTemplateDetailsViewImplUiBinder.class);

	interface RoleScriptTemplateDetailsViewImplUiBinder extends UiBinder<Widget, RoleScriptTemplateDetailsViewImpl> {
		
	}

	private OsceConstants constants = GWT.create(OsceConstants.class);
	private Presenter presenter;
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
	public RoleScriptTemplateDetailsViewImpl() {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		//todo
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter StandardizedPatientActivity) {
		this.presenter =  StandardizedPatientActivity;
	}

	public Widget asWidget() {
		return this;
	}
}
