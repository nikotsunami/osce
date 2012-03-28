/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author niko2
 *
 */
public class DoctorDetailsViewImpl extends Composite implements  DoctorDetailsView{

	private static DoctorDetailsViewImplUiBinder uiBinder = GWT
			.create(DoctorDetailsViewImplUiBinder.class);

	interface DoctorDetailsViewImplUiBinder extends
			UiBinder<Widget, DoctorDetailsViewImpl> {
	}	

	@UiField
	TabPanel doctorPanel;
    @UiField
    IconButton edit;
    @UiField
    IconButton delete;

    private Delegate delegate;
	

//    @UiField
//    SpanElement id;
//    @UiField
//    SpanElement version;
    @UiField
    SpanElement gender;
    @UiField
    SpanElement title;
    @UiField
    SpanElement name;
    @UiField
    SpanElement preName;
    @UiField
    Anchor email;
    @UiField
    SpanElement telephone;
    @UiField
    SpanElement clinic;
    @UiField
    SimplePanel officePanel;
    
    @UiField
    SpanElement labelGender;
    @UiField
    SpanElement labelTitle;
    @UiField
    SpanElement labelName;
    @UiField
    SpanElement labelPreName;
    @UiField
    SpanElement labelEmail;
    @UiField
    SpanElement labelTelephone;
    @UiField
    SpanElement labelClinic;
    
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
	public DoctorDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		OsceConstants constants = GWT.create(OsceConstants.class);
		
		doctorPanel.selectTab(0);

		doctorPanel.getTabBar().setTabText(0, constants.generalInformation());
		doctorPanel.getTabBar().setTabText(1, constants.officeDetails());
		
		TabPanelHelper.moveTabBarToBottom(doctorPanel);
		
		edit.setText(constants.edit());
		delete.setText(constants.delete());
		
		labelGender.setInnerText(constants.gender() + ":");
		labelTitle.setInnerText(constants.title() + ":");
		labelName.setInnerText(constants.name() + ":");
		labelPreName.setInnerText(constants.preName() + ":");
		labelEmail.setInnerText(constants.email() + ":");
		labelTelephone.setInnerText(constants.telephone() + ":");
		labelClinic.setInnerText(constants.clinic() + ":");
	}
  

    @Override
    public SimplePanel getOfficeDetailsPanel() {
		return officePanel;
	}

	DoctorProxy proxy;

    @UiField
    SpanElement displayRenderer;

	private Presenter presenter;

	public void setValue(DoctorProxy proxy) {
		this.proxy = proxy;
//		version.setInnerText(proxy.getVersion() == null ? "" : String
//				.valueOf(proxy.getVersion()));
		gender.setInnerText(proxy.getGender() == null ? "" : String.valueOf(proxy.getGender()));
		title.setInnerText(proxy.getTitle() == null ? "" : String.valueOf(proxy.getTitle()));
		name.setInnerText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
		preName.setInnerText(proxy.getPreName() == null ? "" : String.valueOf(proxy.getPreName()));
		
		email.setHref("mailto:" + (proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		email.setText((proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		telephone.setInnerText(proxy.getTelephone() == null ? "" : String.valueOf(proxy.getTelephone()));
		clinic.setInnerText(proxy.getClinic() == null ? "" : String.valueOf(proxy.getClinic().getName()));
		// office.setInnerText(proxy.getOffice() == null ? "" :
		// ch.unibas.medizin.osce.client.managed.ui.OfficeProxyRenderer.instance().render(proxy.getOffice()));
		displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.DoctorProxyRenderer.instance().render(proxy));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	@Override
	public void setPresenter(Presenter DoctorActivity) {
		this.presenter = DoctorActivity;

	}

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public DoctorProxy getValue() {
		return proxy;
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
	}
}
