/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author niko2
 * 
 */
public class ClinicDetailsViewImpl extends Composite implements ClinicDetailsView {

	private static ClinicDetailsViewImplUiBinder uiBinder = GWT.create(ClinicDetailsViewImplUiBinder.class);

	interface ClinicDetailsViewImplUiBinder extends UiBinder<Widget, ClinicDetailsViewImpl> {
	}
	
	@UiField
	IconButton edit;

	@UiField
	IconButton delete;

	private Presenter presenter;
	private Delegate delegate;

	@UiField
	TabPanel clinicPanel;

	// @UiField
	// SpanElement id;
	// @UiField
	// SpanElement version;
	@UiField
	SpanElement name;
	@UiField
	SpanElement street;
	@UiField
	SpanElement city;
	
	@UiField
	VerticalPanel specialTabPanel;

	ClinicProxy proxy;

	@UiField
	SpanElement labelName;
	@UiField
	SpanElement labelStreet;
	@UiField
	SpanElement labelCity;

	@UiField
	SpanElement displayRenderer;


		
	int selectedTab = 0 ;
	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public ClinicDetailsViewImpl() {
		OsceConstants constants = GWT.create(OsceConstants.class);
		initWidget(uiBinder.createAndBindUi(this));

		clinicPanel.selectTab(selectedTab);

		clinicPanel.getTabBar().setTabText(0, constants.generalInformation());
		clinicPanel.getTabBar().setTabText(1, constants.doctors());

		TabPanelHelper.moveTabBarToBottom(clinicPanel);

		edit.setText(constants.edit());
		delete.setText(constants.delete());

		labelName.setInnerText(constants.name() + ":");
		labelStreet.setInnerText(constants.street() + ":");
		labelCity.setInnerText(constants.plz() + ", " + constants.city() + ":");
		
		clinicPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				Log.info("tab selection" + event.getSelectedItem());
				if (delegate != null) {
					Log.info("Delegate SET here Impl");
				
					delegate.storeDisplaySettings();
				}
			}
		});
	}

	public void setValue(ClinicProxy proxy) {
		this.proxy = proxy;
		// id.setInnerText(proxy.getId() == null ? "" :
		// String.valueOf(proxy.getId()));
		// version.setInnerText(proxy.getVersion() == null ? "" :
		// String.valueOf(proxy.getVersion()));
		name.setInnerText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
		street.setInnerText(proxy.getStreet() == null ? "" : String.valueOf(proxy.getStreet()));
		if (proxy.getCity() != null && proxy.getPostalCode() != null) {
			city.setInnerText(String.valueOf(proxy.getPostalCode()) + " " + String.valueOf(proxy.getCity()));
		}
		else if (proxy.getCity() != null && proxy.getPostalCode() == null)
		{
			city.setInnerText(String.valueOf(proxy.getCity()));
		}
		else if (proxy.getCity() == null && proxy.getPostalCode() != null)
		{
			city.setInnerText(String.valueOf(proxy.getPostalCode()));
		}
//		doctors.setInnerText(proxy.getDoctors() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(
//				ch.unibas.medizin.osce.client.managed.ui.DoctorProxyRenderer.instance()).render(proxy.getDoctors()));
		displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.ClinicProxyRenderer.instance().render(proxy));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	@Override
	public void setPresenter(Presenter ClinicActivity) {
		this.presenter = ClinicActivity;

	}

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public ClinicProxy getValue() {
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

	public VerticalPanel getSpecialTabPanel() {
		return this.specialTabPanel;
	}

	public void setSpecialTabPanel(HorizontalPanel VerticalPanel) {
		this.specialTabPanel = specialTabPanel;
	}

	
	@Override
	public int getSelectedDetailsTab() {
		return clinicPanel.getTabBar().getSelectedTab();
	}

	@Override
	public void setSelectedDetailsTab(int detailsTab) {
		clinicPanel.selectTab(detailsTab);
	}
}
