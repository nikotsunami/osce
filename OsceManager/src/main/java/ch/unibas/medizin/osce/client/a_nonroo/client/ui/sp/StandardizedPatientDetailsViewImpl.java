/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;


import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.dom.client.Node;

/**
 * @author niko2
 *
 */
public class StandardizedPatientDetailsViewImpl extends Composite implements  StandardizedPatientDetailsView{

	private static StandardizedPatientDetailsViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientDetailsViewImplUiBinder.class);

	interface StandardizedPatientDetailsViewImplUiBinder extends UiBinder<Widget, StandardizedPatientDetailsViewImpl> {
		
	}

	private Presenter presenter;
	StandardizedPatientProxy proxy;

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

	// Buttons
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
	SpanElement description;
	@UiField
	SpanElement displayRenderer;
	@UiField
	SpanElement bankIBAN;
	@UiField
	SpanElement bankName;
	@UiField
	SpanElement bankBIC;

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
		
		edit.setText(Messages.EDIT);
		delete.setText(Messages.DELETE);
		maps.setText(Messages.GOOGLE_MAPS);
		
		TabPanelHelper.reorderTabs(patientPanel);
		TabPanelHelper.reorderTabs(scarAnamnesisPanel);
		
		setTabTexts();
		setLabelTexts();
	}
	
	private void setTabTexts() {
		patientPanel.getTabBar().setTabText(0, Messages.CONTACT_INFO);
		patientPanel.getTabBar().setTabText(1, Messages.DETAILS);
		patientPanel.getTabBar().setTabText(2, Messages.LANGUAGE_SKILLS);
		patientPanel.getTabBar().setTabText(3, Messages.BANK_ACCOUNT);
		patientPanel.getTabBar().setTabText(4, Messages.DESCRIPTION);
		
		scarAnamnesisPanel.getTabBar().setTabText(0, Messages.TRAITS);
		scarAnamnesisPanel.getTabBar().setTabText(1, Messages.ANAMNESIS_VALUES);
	}
	
	private void setLabelTexts() {
		labelPLZCity.setInnerText(Messages.PLZCITY + ":");
		labelEmail.setInnerText(Messages.EMAIL + ":");
		labelMobile.setInnerText(Messages.MOBILE + ":");
		labelStreet.setInnerText(Messages.STREET + ":");
		labelTelephone.setInnerText(Messages.TELEPHONE + ":");
		
		labelBankName.setInnerText(Messages.BANK_NAME + ":");
		labelBankIBAN.setInnerText(Messages.BANK_IBAN + ":");
		labelBankBIC.setInnerText(Messages.BANK_BIC + ":");
		
		labelBirthdate.setInnerText(Messages.BIRTHDAY + ":");
		labelGender.setInnerText(Messages.GENDER + ":");
		labelHeight.setInnerText(Messages.HEIGHT + ":");
		labelWeight.setInnerText(Messages.WEIGHT + ":");
		labelNationality.setInnerText(Messages.NATIONALITY + ":");
		labelProfession.setInnerText(Messages.PROFESSION + ":");
	}

	public void setValue(StandardizedPatientProxy proxy) {
		this.proxy = proxy;
		//version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
		gender.setInnerText(proxy.getGender() == null ? "" : String.valueOf(proxy.getGender()));
		street.setInnerText(proxy.getStreet() == null ? "" : String.valueOf(proxy.getStreet()));
		city.setInnerText(proxy.getCity() == null ? "" : String.valueOf(proxy.getCity()));
		postalCode.setInnerText(proxy.getPostalCode() == null ? "" : String.valueOf(proxy.getPostalCode()));
		telephone.setInnerText(proxy.getTelephone() == null ? "" : String.valueOf(proxy.getTelephone()));
		mobile.setInnerText(proxy.getMobile() == null ? "" : String.valueOf(proxy.getMobile()));
		birthday.setInnerText(proxy.getBirthday() == null ? "" : DateTimeFormat.getFormat(Messages.DATE_TIME_FORMAT).format(proxy.getBirthday()));
		height.setInnerText(proxy.getHeight() == null ? "" : String.valueOf(proxy.getHeight()));
		weight.setInnerText(proxy.getWeight() == null ? "" : String.valueOf(proxy.getWeight()));
		
		email.setHref("mailto:" + (proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		email.setText((proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		
		nationality.setInnerText(proxy.getNationality() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.NationalityProxyRenderer.instance().render(proxy.getNationality()));
		profession.setInnerText(proxy.getProfession() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance().render(proxy.getProfession()));
//		langskills.setInnerText(proxy.getLangskills() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.LangSkillProxyRenderer.instance()).render(proxy.getLangskills()));
		
//		Set<LangSkillProxy> langSkillSet = proxy.getLangskills();
		
		description.setInnerText(proxy.getDescriptions() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.DescriptionProxyRenderer.instance().render(proxy.getDescriptions()));
		displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientProxyRenderer.instance().render(proxy));
		
		BankaccountProxy bank = proxy.getBankAccount();
		bankName.setInnerText(bank == null ? "" : String.valueOf(bank.getBankName()));
		bankIBAN.setInnerText(bank == null ? "" : String.valueOf(bank.getIBAN()));
		bankBIC.setInnerText(bank == null ? "" : String.valueOf(bank.getBIC()));
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
	
	@UiHandler("maps")
	public void onMapsClicked(ClickEvent e) {
		Window.open(createGoogleMapsLink(this.proxy), "_blank", "");
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
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
}
