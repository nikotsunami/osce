package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;


import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
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
	Image patientPanelArrow;
	//SPEC End
	
	@UiField
	DisclosurePanel anamnesisDisclosurePanel;
	@UiField
	Image anamnesisPanelArrow;
	@UiField
	SpanElement anamnesisPanelTitle;
	
	@UiField
	IconButton imageUpload;
	
	@UiField 
	IconButton videoUpload;
	
	UploadPopupViewImpl uploadPopUpView;
	
	UploadPopupViewImpl videoUploadPopUpView;
	
	//ScrolledTab Changes start

		/*@UiField
		TabPanel circuitTabPanel;*/
		
		//private final UiIcons uiIcons = GWT.create(UiIcons.class);
		public ImageResource icon1 = uiIcons.triangle1West(); 
		public ImageResource icon2=  uiIcons.triangle1East();
		Unit u=Unit.PX;
		

		@UiField
		HorizontalPanel horizontalPatientPanel;

		/*// Panels
		@UiField
		TabPanel patientPanel;
*/
		
		
		@UiField(provided=true)
		ScrolledTabLayoutPanel patientPanel=new ScrolledTabLayoutPanel(40L, u, icon1, icon2);
		
		//ScrolledTab Changes end

		
		// SubViews
	@UiField 
	StandardizedPatientScarSubViewImpl standardizedPatientScarSubViewImpl;
	@UiField
	public StandardizedPatientAnamnesisSubViewImpl standardizedPatientAnamneisSubViewImpl;
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
	@UiField
	IconButton status;
	
	@UiField
	IconButton anonymize;
	
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
	IconButton send;
	@UiField
	IconButton pull;

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
		//TabPanelHelper.moveTabBarToBottom(patientPanel);
		horizontalPatientPanel.addStyleName("horizontalPanelStyle");
		horizontalPatientPanel.add(patientPanel);
		patientPanel.setHeight("250px");
		//roleDetailPanel.addStyleName("autoHeight");
		//patientPanel.setWidth("570px");
		
		//patientPanel.addStyleName("gwt-InvertedTabPanel");
		
		
		patientPanel.selectTab(0);
		
		// reorder the Tab- and Content-Panels
		//ScrolledTab Changes start
		
		//ScrolledTab Changes start
		pull.setText(constants.pull());
		send.setText(constants.send());
		print.setText(constants.print());
		edit.setText(constants.edit());
		delete.setText(constants.delete());
		maps.setText(constants.googleMaps());
		anamnesisPanelTitle.setInnerText(constants.anamnesisValues());
				
		setTabTexts();
		setLabelTexts();
		
		//spec start
//		patientDisclosurePanel.setContent(patientPanel);
		patientDisclosurePanel.setStyleName("");
		anamnesisDisclosurePanel.setStyleName("");
		//spec end
		
		patientPanel.addSelectionHandler(new SelectionHandler<Integer>() {
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
		
		//ScrolledTab Changes start
		patientPanel.setTabText(0, constants.contactInfo());
		patientPanel.setTabText(1, constants.details());
		patientPanel.setTabText(2, constants.bankAccount());
		patientPanel.setTabText(3, constants.description());
		patientPanel.setTabText(4, constants.languageSkills());
		patientPanel.setTabText(5, constants.traits());
		
		patientPanel.tabLIstBox.addItem(constants.contactInfo());
		patientPanel.tabLIstBox.addItem(constants.details());
		patientPanel.tabLIstBox.addItem(constants.bankAccount());
		patientPanel.tabLIstBox.addItem(constants.description());
		patientPanel.tabLIstBox.addItem(constants.languageSkills());
		patientPanel.tabLIstBox.addItem(constants.traits());
		
		/*patientPanel.getTabWidget(0).setTitle(constants.contactInfo());
		patientPanel.getTabWidget(1).setTitle(constants.details());
		patientPanel.getTabWidget(2).setTitle(constants.bankAccount());
		patientPanel.getTabWidget(3).setTitle(constants.description());
		patientPanel.getTabWidget(4).setTitle(constants.languageSkills());
		patientPanel.getTabWidget(5).setTitle(constants.traits());
		*/
		
		/*patientPanel.getTabBar().setTabText(0, constants.contactInfo());
		patientPanel.getTabBar().setTabText(1, constants.details());
		patientPanel.getTabBar().setTabText(2, constants.bankAccount());
		patientPanel.getTabBar().setTabText(3, constants.description());
		patientPanel.getTabBar().setTabText(4, constants.languageSkills());
		patientPanel.getTabBar().setTabText(5, constants.traits());*/
		//ScrolledTab Changes start
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
		
		nationality.setInnerText(proxy.getNationality() == null ? "" : (proxy.getNationality().getNationality() == null) ? "" : proxy.getNationality().getNationality());
		profession.setInnerText(proxy.getProfession() == null ? "" : (proxy.getProfession().getProfession() == null) ? "" : proxy.getProfession().getProfession());
		
//		Set<LangSkillProxy> langSkillSet = proxy.getLangskills();
		
		description.setInnerHTML(proxy.getDescriptions() == null ? "" : ch.unibas.medizin.osce.client.managed.ui.DescriptionProxyRenderer.instance().render(proxy.getDescriptions()));
		displayRenderer.setInnerText(((proxy.getName() == null) ? "" : proxy.getName()) + ((proxy.getPreName() == null) ? "" : " " + proxy.getPreName()));
		
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

		// Module 3 Task B
		setStatusIcon(proxy.getStatus());
		setAnonymizeButton(proxy.getStatus());
		// Module 3 Task B
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

	// Module 3 Task B
	@UiHandler("status")
	public void onStatusClicked(ClickEvent e) {
		delegate.showApplicationLoading(true);
		Log.info("onStatusClicked");
		if (proxy.getStatus() == StandardizedPatientStatus.ANONYMIZED) {
			status.setVisible(false);
		} else {
			delegate.statusClicked();
		}
		delegate.showApplicationLoading(false);
	}
	
	// Module 3 Task B
	@UiHandler("anonymize")
	public void onAnonymizeClicked(ClickEvent e) {
		delegate.showApplicationLoading(true);
		Log.info("onAnonymizeClicked");

		anonymize.setVisible(false);
		delegate.onAnonymizeClicked();
		delegate.showApplicationLoading(false);

	}

	@Override
	public void setStatusIcon(StandardizedPatientStatus standardizedPatientStatus) {
		status.setEnabled(true);
		if (standardizedPatientStatus == StandardizedPatientStatus.ANONYMIZED) {
			status.setVisible(false);
		} else {
			if (standardizedPatientStatus == StandardizedPatientStatus.EXPORTED) {
				status.setEnabled(false);
			}
			Log.info("proxy.getStatus() : " + proxy.getStatus());
			status.setText((standardizedPatientStatus == StandardizedPatientStatus.ACTIVE) ? constants.spInactive() : constants.spActive());
			status.setIcon((standardizedPatientStatus == StandardizedPatientStatus.ACTIVE) ? "close" : "check");
			status.setVisible(true);
		}
		setDmzEditOnStatus(standardizedPatientStatus);
		setAnonymizeButton(standardizedPatientStatus);
	}
	
	public void setAnonymizeButton(StandardizedPatientStatus standardizedPatientStatus) {
		anonymize.setEnabled(standardizedPatientStatus == StandardizedPatientStatus.INACTIVE);	
		anonymize.setVisible(standardizedPatientStatus == StandardizedPatientStatus.INACTIVE);
		anonymize.setText(constants.anonymize());
		anonymize.setIcon("cancel");
//		setDmzEditOnStatus(standardizedPatientStatus);
	}
	
	private void setDmzEditOnStatus(StandardizedPatientStatus standardizedPatientStatus) {
		switch(standardizedPatientStatus) {
		case ACTIVE:
			edit.setEnabled(true);
			send.setEnabled(true);
			pull.setEnabled(false);
			break;
		case ANONYMIZED:
			edit.setEnabled(false);
			send.setEnabled(false);
			pull.setEnabled(false);
			break;
		case EXPORTED:
			edit.setEnabled(false);
			send.setEnabled(false);
			pull.setEnabled(true);
			break;
		case EXPORTED_FOR_SCHEDULING:
		case INACTIVE:
			edit.setEnabled(true);
			send.setEnabled(false);
			pull.setEnabled(false);
		}
//		boolean isEnable= (standardizedPatientStatus != null &&  standardizedPatientStatus == StandardizedPatientStatus.ACTIVE);			
//			edit.setEnabled(isEnable);
//			send.setEnabled(isEnable);	
//			pull.setEnabled(isEnable);
	}
	
	// Module 3 Task B

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
	public boolean isAnamnesisDisclosurePanelOpen() {
		return anamnesisDisclosurePanel.isOpen();
	}
	
	
	@Override
	public int getSelectedDetailsTab() {
		//ScrolledTab Changes start
		//return patientPanel.getTabBar().getSelectedTab();
		return patientPanel.getSelectedIndex();
		//ScrolledTab Changes start
	}
	
	@UiHandler("anamnesisPanelArrow")
	public void anamnesisPanelHandler(ClickEvent e) {
		boolean isOpen = anamnesisDisclosurePanel.isOpen(); 
		setAnamnesisDisclosurePanelOpen(!isOpen);
		storeDisplaySettings();
	}
	
	@Override
	public void setAnamnesisDisclosurePanelOpen(boolean value) {
		ImageResource icon = (value) ? uiIcons.triangle1South() : uiIcons.triangle1East();
		anamnesisDisclosurePanel.setOpen(value);
		anamnesisPanelArrow.setResource(icon);
	}

	@UiHandler("patientPanelArrow")
	public void handleClick(ClickEvent e) {
		if (patientDisclosurePanel.isOpen()) {
			setPatientDisclosurePanelOpen(false);
			standardizedPatientAnamneisSubViewImpl.anamnesisTabs.setHeight((ResolutionSettings.getRightWidgetHeight()-200)+"px");
			
			
		} else {
			setPatientDisclosurePanelOpen(true);
			standardizedPatientAnamneisSubViewImpl.anamnesisTabs.setHeight((ResolutionSettings.getRightWidgetHeight()/2)+"px");
		}
		storeDisplaySettings();
	}

	@Override
	public void setPatientDisclosurePanelOpen(boolean value) {
		ImageResource icon = (value) ? uiIcons.triangle1South() : uiIcons.triangle1East();
		patientDisclosurePanel.setOpen(value);
		patientPanelArrow.setResource(icon);
	}

	@Override
	public void setSelectedDetailsTab(int tab) {
		patientPanel.selectTab(tab);
		
	}
	
	@UiHandler("imageUpload")
	public void imageUploadClicked(ClickEvent event)
	{
		Log.info("imageUpload Clicked");
		
		createUploadPopUp();
		
		
	}
	public void createUploadPopUp()
	{
		if(uploadPopUpView==null)
		{
			uploadPopUpView=new UploadPopupViewImpl();
			uploadPopUpView.setAnimationEnabled(true);
			RootPanel.get().add(uploadPopUpView);
			uploadPopUpView.setPopupPosition(imageUpload.getAbsoluteLeft()-50, imageUpload.getAbsoluteTop()-90);
		}
		uploadPopUpView.id.setValue(this.getValue().getId().toString());
		uploadPopUpView.name.setValue(this.getValue().getName());
		uploadPopUpView.setStandardizedPatientMediaSubViewImpl(this.getStandardizedPatientMediaSubViewImpl());
		
		uploadPopUpView.show();
		
	}
	@UiHandler("videoUpload")
	public void videoUploadClicked(ClickEvent event)
	{
		if(videoUploadPopUpView==null)
		{
			videoUploadPopUpView=new UploadPopupViewImpl(true);
			videoUploadPopUpView.setAnimationEnabled(true);
			RootPanel.get().add(videoUploadPopUpView);
			videoUploadPopUpView.setPopupPosition(imageUpload.getAbsoluteLeft()-5, imageUpload.getAbsoluteTop()-90);
		}
		videoUploadPopUpView.id.setValue(this.getValue().getId().toString());
		videoUploadPopUpView.name.setValue(this.getValue().getName());
		videoUploadPopUpView.setStandardizedPatientMediaSubViewImpl(this.getStandardizedPatientMediaSubViewImpl());
		
		videoUploadPopUpView.show();
		
	}
}
