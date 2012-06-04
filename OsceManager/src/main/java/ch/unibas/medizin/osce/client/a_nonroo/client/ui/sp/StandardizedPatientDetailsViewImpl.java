/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import org.springframework.jdbc.object.StoredProcedure;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.UserPlaceSettings;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author niko2
 *
 */
public class StandardizedPatientDetailsViewImpl extends Composite implements  StandardizedPatientDetailsView {

	private static StandardizedPatientDetailsViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientDetailsViewImplUiBinder.class);

	interface StandardizedPatientDetailsViewImplUiBinder extends UiBinder<Widget, StandardizedPatientDetailsViewImpl> {
		
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private Presenter presenter;
	StandardizedPatientProxy proxy;
	private final UiIcons uiIcons = GWT.create(UiIcons.class);

	//SPEC START
	@UiField
	DisclosurePanel patientDisclosurePanel;
	@UiField
	Image arrow;
	//SPEC End
	
	// Panels
	@UiField
	TabPanel patientPanel;
	@UiField
	TabPanel scarAnamnesisPanel;

	// SubViews
	@UiField 
	StandardizedPatientScarSubViewImpl standardizedPatientScarSubViewImpl;
	@UiField
	StandardizedPatientAnamnesisSubViewImpl standardizedPatientAnamneisSubViewImpl;
	@UiField
	StandardizedPatientLangSkillSubViewImpl standardizedPatientLangSkillSubViewImpl;
	@UiField
	StandardizedPatientMediaSubViewImpl standardizedPatientMediaSubViewImpl;

	@Override
	public StandardizedPatientMediaSubViewImpl getStandardizedPatientMediaSubViewImpl() {
		return standardizedPatientMediaSubViewImpl;
	}

	// Buttons
	@UiField
	IconButton print;
	@UiField
	IconButton edit;
	@UiField
	IconButton delete;
	@UiField
	IconButton maps;
	
	// Labels (Fieldnames)
	@UiField
	SpanElement labelStreet;
	@UiField
	SpanElement labelPLZCity;
	@UiField
	SpanElement labelTelephone;
	@UiField
	SpanElement labelTelephone2;
	@UiField
	SpanElement labelMobile;
	@UiField
	SpanElement labelEmail;
	@UiField
	SpanElement labelBankName;
	@UiField
	SpanElement labelBankIBAN;
	@UiField
	SpanElement labelBankBIC;
	@UiField
	SpanElement labelBankCity;
	@UiField
	SpanElement labelBankPlz;
	@UiField
	SpanElement labelBankCountry;
	@UiField
	SpanElement labelOwnerName;
	@UiField
	SpanElement labelBirthdate;
	@UiField
	SpanElement labelGender;
	@UiField
	SpanElement labelHeight;
	@UiField
	SpanElement labelWeight;
	@UiField
	SpanElement labelNationality;
	@UiField
	SpanElement labelProfession;
	@UiField
	SpanElement labelWorkPermission;
	@UiField
	SpanElement labelMaritalStatus;
	@UiField
	SpanElement labelSocialInsuranceNo;

	// Fields
	@UiField
	SpanElement gender;
	@UiField
	SpanElement street;
	@UiField
	SpanElement city;
	@UiField
	SpanElement postalCode;
	@UiField
	SpanElement telephone;
	@UiField
	SpanElement telephone2;
	@UiField
	SpanElement mobile;
	@UiField
	SpanElement birthday;
	@UiField
	SpanElement height;
	@UiField
	SpanElement weight;
	@UiField
	Anchor email;
	@UiField
	SpanElement nationality;
	@UiField
	SpanElement profession;
//	@UiField
//	SpanElement langskills;
	@UiField
	DivElement description;
	@UiField
	SpanElement displayRenderer;
	@UiField
	SpanElement bankIBAN;
	@UiField
	SpanElement bankName;
	@UiField
	SpanElement bankBIC;
	@UiField
	SpanElement bankCountry;
	@UiField
	SpanElement bankPostalCode;
	@UiField
	SpanElement bankCity;
	@UiField
	SpanElement bankOwnerName;
	@UiField
	SpanElement workPermission;
	@UiField
	SpanElement maritalStatus;
	@UiField
	SpanElement socialInsuranceNo;
	@UiField
	Button send;
	@UiField
	Button pull;

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
	public StandardizedPatientDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		patientPanel.selectTab(0);
		scarAnamnesisPanel.selectTab(0);
		
		// reorder the Tab- and Content-Panels
		TabPanelHelper.moveTabBarToBottom(patientPanel);
		pull.setText(constants.pull());
		send.setText(constants.send());
		print.setText(constants.print());
		edit.setText(constants.edit());
		delete.setText(constants.delete());
		maps.setText(constants.googleMaps());
				
		setTabTexts();
		setLabelTexts();
		
		//spec start
		patientDisclosurePanel.setContent(patientPanel);
		patientDisclosurePanel.setStyleName("");		
		//spec end
		
		patientPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				storeDisplaySettings();
			}
		});
		
		scarAnamnesisPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				storeDisplaySettings();
			}
		});
		
	}
	
	private void storeDisplaySettings() {
		if (delegate != null) {
			delegate.storeDisplaySettings();
		}
	}
	
	private void setTabTexts() {
		patientPanel.getTabBar().setTabText(0, constants.contactInfo());
		patientPanel.getTabBar().setTabText(1, constants.details());
		patientPanel.getTabBar().setTabText(2, constants.languageSkills());
		patientPanel.getTabBar().setTabText(3, constants.bankAccount());
		patientPanel.getTabBar().setTabText(4, constants.description());
		
		scarAnamnesisPanel.getTabBar().setTabText(0, constants.traits());
		scarAnamnesisPanel.getTabBar().setTabText(1, constants.anamnesisValues());
	}
	
	private void setLabelTexts() {
		labelPLZCity.setInnerText(constants.plzCity() + ":");
		labelEmail.setInnerText(constants.email() + ":");
		labelMobile.setInnerText(constants.mobile() + ":");
		labelStreet.setInnerText(constants.street() + ":");
		labelTelephone.setInnerText(constants.telephone() + ":");
		labelTelephone2.setInnerText(constants.telephone() + " 2:");
		
		labelBankName.setInnerText(constants.bank() + ":");
		labelBankIBAN.setInnerText(constants.iban() + ":");
		labelBankBIC.setInnerText(constants.bic() + ":");
		labelBankCountry.setInnerText(constants.country() + ":");
		labelBankCity.setInnerText(constants.city());
		labelBankPlz.setInnerText(constants.plz());
		labelOwnerName.setInnerText(constants.ownerName());
		
		labelBirthdate.setInnerText(constants.birthday() + ":");
		labelGender.setInnerText(constants.gender() + ":");
		labelHeight.setInnerText(constants.height() + ":");
		labelWeight.setInnerText(constants.weight() + ":");
		labelNationality.setInnerText(constants.nationality() + ":");
		labelProfession.setInnerText(constants.profession() + ":");
		labelWorkPermission.setInnerText(constants.workPermission() + ":");
		labelMaritalStatus.setInnerText(constants.maritalStatus() + ":");
		labelSocialInsuranceNo.setInnerText(constants.socialInsuranceNo() + ":");
	}

	public void setValue(StandardizedPatientProxy proxy) {
		this.proxy = proxy;
		//version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
		gender.setInnerText(new EnumRenderer<Gender>().render(proxy.getGender()));
		street.setInnerText(proxy.getStreet() == null ? "" : String.valueOf(proxy.getStreet()));
		city.setInnerText(proxy.getCity() == null ? "" : String.valueOf(proxy.getCity()));
		postalCode.setInnerText(proxy.getPostalCode() == null ? "" : String.valueOf(proxy.getPostalCode()));
		telephone.setInnerText(proxy.getTelephone() == null ? "" : String.valueOf(proxy.getTelephone()));
		mobile.setInnerText(proxy.getMobile() == null ? "" : String.valueOf(proxy.getMobile()));
		telephone2.setInnerText(proxy.getTelephone2() == null ? "" : String.valueOf(proxy.getTelephone2()));
		birthday.setInnerText(proxy.getBirthday() == null ? "" : DateTimeFormat.getFormat(constants.dateTimeFormat()).format(proxy.getBirthday()));
		height.setInnerText(proxy.getHeight() == null ? "" : String.valueOf(proxy.getHeight()));
		weight.setInnerText(proxy.getWeight() == null ? "" : String.valueOf(proxy.getWeight()));
		workPermission.setInnerText(new EnumRenderer<WorkPermission>().render(proxy.getWorkPermission()));
		maritalStatus.setInnerText(new EnumRenderer<MaritalStatus>().render(proxy.getMaritalStatus()));
		socialInsuranceNo.setInnerText((proxy.getSocialInsuranceNo() == null) ? "" : proxy.getSocialInsuranceNo());
		
		email.setHref("mailto:" + (proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		email.setText((proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		
		nationality.setInnerText(proxy.getNationality() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance().render(proxy.getNationality()));
		profession.setInnerText(proxy.getProfession() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance().render(proxy.getProfession()));
//		langskills.setInnerText(proxy.getLangskills() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.LangSkillProxyRenderer.instance()).render(proxy.getLangskills()));
		
//		Set<LangSkillProxy> langSkillSet = proxy.getLangskills();
		
		description.setInnerHTML(proxy.getDescriptions() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.DescriptionProxyRenderer.instance().render(proxy.getDescriptions()));
		displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientProxyRenderer.instance().render(proxy));
		
		BankaccountProxy bank = proxy.getBankAccount();
		bankName.setInnerText((bank == null || bank.getBankName() == null) ? "" : String.valueOf(bank.getBankName()));
		bankIBAN.setInnerText((bank == null || bank.getIBAN() == null) ? "" : String.valueOf(bank.getIBAN()));
		bankBIC.setInnerText((bank == null || bank.getBIC() == null) ? "" : String.valueOf(bank.getBIC()));
		if (bank == null || bank.getCountry() == null) {
			bankCountry.setInnerText("");
		} else {
			bankCountry.setInnerText(String.valueOf(bank.getCountry().getNationality()));
		}
		bankCity.setInnerText((bank == null || bank.getCity() == null) ? "" : String.valueOf(bank.getCity()));
		bankPostalCode.setInnerText((bank == null || bank.getPostalCode() == null) ? "" : String.valueOf(bank.getPostalCode()));
		bankOwnerName.setInnerText((bank == null || bank.getOwnerName() == null) ? "" : String.valueOf(bank.getOwnerName()));
	}
	
	private String createGoogleMapsLink(StandardizedPatientProxy proxy) {
		return "http://maps.google.com/maps?q=" + proxy.getStreet() + ",+" + proxy.getPostalCode() + "+" + proxy.getCity();
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

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public StandardizedPatientProxy getValue() {
		return proxy;
	}
	
	@UiHandler("print")
	public void onPrintClicked(ClickEvent e) {
		delegate.printPatientClicked();
	}
	
	@UiHandler("maps")
	public void onMapsClicked(ClickEvent e) {
		Window.open(createGoogleMapsLink(this.proxy), "_blank", "");
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deletePatientClicked();
	}
	@UiHandler("send")
	void onSend(ClickEvent event) {
		delegate.sendClicked();
	}
	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editPatientClicked();
	}
	@UiHandler("pull")
	void onPull(ClickEvent event) {
		delegate.pullClicked();
	}

	@Override
	public ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisSubViewImpl getStandardizedPatientAnamnesisSubViewImpl() {
		return standardizedPatientAnamneisSubViewImpl;
	}

	@Override
	public ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubViewImpl getStandardizedPatientScarSubViewImpl() {
		return standardizedPatientScarSubViewImpl;
	}

	@Override
	public StandardizedPatientLangSkillSubViewImpl getStandardizedPatientLangSkillSubViewImpl() {
		return standardizedPatientLangSkillSubViewImpl;
	}
	
	@Override
	public boolean isPatientDisclosurePanelOpen() {
		return patientDisclosurePanel.isOpen();
	}
	
	@Override
	public int getSelectedDetailsTab() {
		return patientPanel.getTabBar().getSelectedTab();
	}
	
	@Override
	public int getSelectedScarAnamnesisTab() {
		return scarAnamnesisPanel.getTabBar().getSelectedTab();
	}
	
	@UiHandler("arrow")
	void handleClick(ClickEvent e) {
		if (patientDisclosurePanel.isOpen()) {
			setPatientDisclosurePanelOpen(false);
		} else {
			setPatientDisclosurePanelOpen(true);
		}
		storeDisplaySettings();
	}

	@Override
	public void setPatientDisclosurePanelOpen(boolean value) {
		ImageResource icon = (value) ? uiIcons.triangle1South() : uiIcons.triangle1East();
		patientDisclosurePanel.setOpen(value);
		arrow.setResource(icon);
	}

	@Override
	public void setSelectedDetailsTab(int tab) {
		patientPanel.selectTab(tab);
	}

	@Override
	public void setSelectedScarAnamnesisTab(int tab) {
		scarAnamnesisPanel.selectTab(tab);
	}
}
