package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;




import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.SpBankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.SpStandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.WorkPermission;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author manishp
 *
 */
public class SPDetailsReviewViewImpl extends PopupPanel implements  SPDetailsReviewView {

	private static SPDetailsReviewViewImplUiBinder uiBinder = GWT
			.create(SPDetailsReviewViewImplUiBinder.class);

	interface SPDetailsReviewViewImplUiBinder extends UiBinder<Widget, SPDetailsReviewViewImpl> {
		
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	//private Presenter presenter;
	private StandardizedPatientProxy proxy;
	
	private SpStandardizedPatientProxy spStandardizedPatientProxy;
	
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
	StandardizedPatientMediaSubViewImpl standardizedPatientMediaSubViewImplOforOldImage;

	@UiField
	StandardizedPatientMediaSubViewImpl standardizedPatientMediaSubViewImplOforNewImage;
	
	private SPDetailsReviewViewImpl spDetailsReviewViewImpl;

	// Buttons
	@UiField
	IconButton acceptChangesButton;
	
	@UiField
	IconButton discardChangesButton;
	
	@UiField
	IconButton closeButton;
	
	@UiField
	SpanElement labelStreet;
	
	@UiField
	SpanElement streetOldValue;
	
	@UiField
	SpanElement streetNewValue;
	
	@UiField
	SpanElement labelPLZCity;
	@UiField
	SpanElement labelTelephone;
	//Added for OMS-157
	@UiField
	SpanElement labelCountry;
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
	SpanElement genderOldValue;
	
	@UiField
	SpanElement genderNewValue;
	
	@UiField
	SpanElement cityOldValue;
	@UiField
	SpanElement postalCodeOldValue;
	
	@UiField
	SpanElement cityNewValue;
	@UiField
	SpanElement postalCodeNewValue;
	
	@UiField
	SpanElement telephoneOldValue;

	@UiField
	SpanElement telephoneNewValue;
	//Added for OMS-157.
	@UiField
	SpanElement countryOldValue;

	@UiField
	SpanElement countryNewValue;
	
	@UiField
	SpanElement telephone2OldValue;
	
	@UiField
	SpanElement telephone2NewValue;
	
	@UiField
	SpanElement mobileOldValue;
	
	@UiField
	SpanElement mobileNewValue;
	
	@UiField
	SpanElement birthdayOldValue;
	
	@UiField
	SpanElement birthdayNewValue;
	
	@UiField
	SpanElement heightOldValue;
	@UiField
	SpanElement heightNewValue;
	
	@UiField
	SpanElement weightOldValue;
	
	@UiField
	SpanElement weightNewValue;
	
	@UiField
	Anchor emailOldValue;
	@UiField
	Anchor emailNewValue;
	
	@UiField
	SpanElement nationalityOldValue;
	
	@UiField
	SpanElement nationalityNewValue;
	
	@UiField
	SpanElement professionOldValue;
	
	@UiField
	SpanElement professionNewValue;
	
//	@UiField
//	SpanElement langskills;
	
	@UiField
	SpanElement displayRenderer;
	@UiField
	SpanElement bankIBANOldValue;
	@UiField
	SpanElement bankNameOldValue;
	@UiField
	SpanElement bankBICOldValue;
	@UiField
	SpanElement bankCountryOldValue;
	@UiField
	SpanElement bankPostalCodeOldValue;
	@UiField
	SpanElement bankCityOldValue;
	@UiField
	SpanElement bankOwnerNameOldValue;
	
	
	@UiField
	SpanElement bankIBANNewValue;
	@UiField
	SpanElement bankNameNewValue;
	@UiField
	SpanElement bankBICNewValue;
	@UiField
	SpanElement bankCountryNewValue;
	@UiField
	SpanElement bankPostalCodeNewValue;
	@UiField
	SpanElement bankCityNewValue;
	@UiField
	SpanElement bankOwnerNameNewValue;
	
	@UiField
	SpanElement workPermissionOldValue;
	
	@UiField
	SpanElement workPermissionNewValue;
	
	@UiField
	SpanElement maritalStatusOldValue;
	@UiField
	SpanElement maritalStatusNewValue;
	
	@UiField
	SpanElement socialInsuranceNoOldValue;
	
	@UiField
	SpanElement socialInsuranceNoNewValue;
	
	private Delegate delegate;

	@UiField
	SPDetailsReviewAnamnesisSubViewImpl spDetailsReviewAnamnesisSubViewImpl;
	
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
	public SPDetailsReviewViewImpl() {
		add(uiBinder.createAndBindUi(this));
		
		spDetailsReviewViewImpl=this;
		
		horizontalPatientPanel.addStyleName("horizontalPanelStyle");
		horizontalPatientPanel.add(patientPanel);
		patientPanel.setHeight("250px");
		
		
		
		patientPanel.selectTab(0);
		

		acceptChangesButton.setText(constants.acceptChanges());
		discardChangesButton.setText(constants.discardChanges());
		closeButton.setText(constants.close());
		
		addClickHandlerOfButtons();
		
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
		
		spDetailsReviewViewImpl.setGlassEnabled(true);
		spDetailsReviewViewImpl.setPopupPosition(432,45);
		spDetailsReviewViewImpl.addStyleName("popupPanelWidth");
		spDetailsReviewViewImpl.hide();
	}

	private void addClickHandlerOfButtons() {
	
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				spDetailsReviewViewImpl.hide();
				
			}
		});	
	}

	private void storeDisplaySettings() {
		if (delegate != null) {
			//delegate.storeDisplaySettings();
		}
	}
	
	private void setTabTexts() {
		
		//ScrolledTab Changes start
		patientPanel.setTabText(0, constants.contactInfo());
		patientPanel.setTabText(1, constants.details());
		patientPanel.setTabText(2, constants.bankAccount());
		
		patientPanel.tabLIstBox.addItem(constants.contactInfo());
		patientPanel.tabLIstBox.addItem(constants.details());
		patientPanel.tabLIstBox.addItem(constants.bankAccount());
	
	}
	
	private void setLabelTexts() {
		labelPLZCity.setInnerText(constants.plzCity() + ":");
		labelEmail.setInnerText(constants.email() + ":");
		labelMobile.setInnerText(constants.mobile() + ":");
		labelStreet.setInnerText(constants.street() + ":");
		labelTelephone.setInnerText(constants.telephone() + ":");
		labelTelephone2.setInnerText(constants.telephone() + " 2:");
		//Added for OMS-157.
		labelCountry.setInnerText(constants.country() + ":");
		
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

	@Override
	public void setValue(StandardizedPatientProxy proxy,SpStandardizedPatientProxy spStandardizedPatientProxy) {
		this.proxy = proxy;
		this.spStandardizedPatientProxy=spStandardizedPatientProxy;
		
		displayRenderer.setInnerText(((proxy.getName() == null) ? "" : proxy.getName()) + ((proxy.getPreName() == null) ? "" : " " + proxy.getPreName()));
		
		standardizedPatientMediaSubViewImplOforOldImage.setMediaContent(proxy.getImmagePath());
		
		if(proxy.getImmagePath()!=null){
			standardizedPatientMediaSubViewImplOforOldImage.uploadMessage.addStyleName("oldImage");
		}
		
		String imageDownloadHitUrl=GWT.getHostPageBaseURL()+ OsMaConstant.FILE_DOWNLOAD_SERVLET_HIT_PATH;
		
		Log.info("Host name : " +GWT.getHostPageBaseURL());
		
		if(spStandardizedPatientProxy.getImmagePath()!=null){
			standardizedPatientMediaSubViewImplOforNewImage.setRemoteImage(imageDownloadHitUrl +spStandardizedPatientProxy.getImmagePath());
		}
		
		setContactDetailsValue(proxy,spStandardizedPatientProxy);
		setParticularsValue(proxy,spStandardizedPatientProxy);
		setBankDetailsValue(proxy,spStandardizedPatientProxy);
	}

	private void setContactDetailsValue(StandardizedPatientProxy proxy,SpStandardizedPatientProxy spStandardizedPatientProxy) {
		
		boolean isChangedData=false;
		
		streetOldValue.setInnerText(proxy.getStreet() == null ? "" : String.valueOf(proxy.getStreet()));
		streetNewValue.setInnerText(spStandardizedPatientProxy.getStreet() == null ? "" : String.valueOf(spStandardizedPatientProxy.getStreet()));
		
		if(!streetOldValue.getInnerText().equals(streetNewValue.getInnerText())){

			streetOldValue.setClassName("oldData");
			streetNewValue.setClassName("newData");
			isChangedData=true;
		}else{
			streetOldValue.removeClassName("oldData");
			streetNewValue.removeClassName("newData");
		}
		
		if(streetOldValue.getInnerText().equals("")){
			streetOldValue.setClassName("nullDataStyle");
		}else{
			streetOldValue.removeClassName("nullDataStyle");
		}
		if(streetNewValue.getInnerText().equals("")){
			streetNewValue.setClassName("nullDataStyle");
		}else{
			streetNewValue.removeClassName("nullDataStyle");
		}
		
		postalCodeOldValue.setInnerText(proxy.getPostalCode() == null ? "" : String.valueOf(proxy.getPostalCode()));
		postalCodeNewValue.setInnerText(spStandardizedPatientProxy.getPostalCode() == null ? "" : String.valueOf(spStandardizedPatientProxy.getPostalCode()));

		if(!postalCodeOldValue.getInnerText().equals(postalCodeNewValue.getInnerText())){

			postalCodeOldValue.setClassName("oldData");
			postalCodeNewValue.setClassName("newData");
			isChangedData=true;
		}else{
			postalCodeOldValue.removeClassName("oldData");
			postalCodeNewValue.removeClassName("newData");
		}
		if(postalCodeOldValue.getInnerText().equals("")){
			postalCodeOldValue.setClassName("nullDataStyle");
		}else{
			postalCodeOldValue.removeClassName("nullDataStyle");
		}
		if(postalCodeNewValue.getInnerText().equals("")){
			postalCodeNewValue.setClassName("nullDataStyle");
		}else{
			postalCodeNewValue.removeClassName("nullDataStyle");
		}
		
		cityOldValue.setInnerText(proxy.getCity() == null ? "" : String.valueOf(proxy.getCity()));
		cityNewValue.setInnerText(spStandardizedPatientProxy.getCity() == null ? "" : String.valueOf(spStandardizedPatientProxy.getCity()));
		
		if(!cityOldValue.getInnerText().equals(cityNewValue.getInnerText())){

			cityOldValue.setClassName("oldData");
			cityNewValue.setClassName("newData");
			isChangedData=true;
		}else{
			cityOldValue.removeClassName("oldData");
			cityNewValue.removeClassName("newData");
		}
		if(cityOldValue.getInnerText().equals("")){
			cityOldValue.setClassName("nullDataStyle");
		}else{
			cityOldValue.removeClassName("nullDataStyle");
		}
		if(cityNewValue.getInnerText().equals("")){
			cityNewValue.setClassName("nullDataStyle");
		}else{
			cityNewValue.removeClassName("nullDataStyle");
		}
		
		//Added for OMS-157
		countryOldValue.setInnerText(proxy.getCountry() == null ? "" : String.valueOf(proxy.getCountry().getNationality()));
		countryNewValue.setInnerText(spStandardizedPatientProxy.getCountry() == null ? "" : String.valueOf(spStandardizedPatientProxy.getCountry().getNationality()));
		
		if(!countryOldValue.getInnerText().equals(countryNewValue.getInnerText())){
				
			countryOldValue.setClassName("oldData");
			countryNewValue.setClassName("newData");
				isChangedData=true;
		}else{
			countryOldValue.removeClassName("oldData");
			countryNewValue.removeClassName("newData");
		}
		if(countryOldValue.getInnerText().equals("")){
			countryOldValue.setClassName("nullDataStyle");
		}else{
			countryOldValue.removeClassName("nullDataStyle");
		}
		if(countryNewValue.getInnerText().equals("")){
			countryNewValue.setClassName("nullDataStyle");
		}else{
			countryNewValue.removeClassName("nullDataStyle");
		}
				
		telephoneOldValue.setInnerText(proxy.getTelephone() == null ? "" : String.valueOf(proxy.getTelephone()));
		telephoneNewValue.setInnerText(spStandardizedPatientProxy.getTelephone() == null ? "" : String.valueOf(spStandardizedPatientProxy.getTelephone()));
		
		if(!telephoneOldValue.getInnerText().equals(telephoneNewValue.getInnerText())){

			telephoneOldValue.setClassName("oldData");
			telephoneNewValue.setClassName("newData");
			isChangedData=true;
		}else{
			telephoneOldValue.removeClassName("oldData");
			telephoneNewValue.removeClassName("newData");
		}
		if(telephoneOldValue.getInnerText().equals("")){
			telephoneOldValue.setClassName("nullDataStyle");
		}else{
			telephoneOldValue.removeClassName("nullDataStyle");
		}
		if(telephoneNewValue.getInnerText().equals("")){
			telephoneNewValue.setClassName("nullDataStyle");
		}else{
			telephoneNewValue.removeClassName("nullDataStyle");
		}
		
		telephone2OldValue.setInnerText(proxy.getTelephone2() == null ? "" : String.valueOf(proxy.getTelephone2()));
		telephone2NewValue.setInnerText(spStandardizedPatientProxy.getTelephone2() == null ? "" : String.valueOf(spStandardizedPatientProxy.getTelephone2()));
		
		if(!telephone2OldValue.getInnerText().equals(telephone2NewValue.getInnerText())){

			telephone2OldValue.setClassName("oldData");
			telephone2NewValue.setClassName("newData");
			isChangedData=true;
		}else{
			telephone2OldValue.removeClassName("oldData");
			telephone2NewValue.removeClassName("newData");
		}
		
		if(telephone2OldValue.getInnerText().equals("")){
			telephone2OldValue.setClassName("nullDataStyle");
		}else{
			telephone2OldValue.removeClassName("nullDataStyle");
		}
		if(telephone2NewValue.getInnerText().equals("")){
			telephone2NewValue.setClassName("nullDataStyle");
		}else{
			telephone2NewValue.removeClassName("nullDataStyle");
		}
		
		mobileOldValue.setInnerText(proxy.getMobile() == null ? "" : String.valueOf(proxy.getMobile()));
		mobileNewValue.setInnerText(spStandardizedPatientProxy.getMobile() == null ? "" : String.valueOf(spStandardizedPatientProxy.getMobile()));
		
		if(!mobileOldValue.getInnerText().equals(mobileNewValue.getInnerText())){

			mobileOldValue.setClassName("oldData");
			mobileNewValue.setClassName("newData");
			isChangedData=true;
		}else{
			mobileOldValue.removeClassName("oldData");
			mobileNewValue.removeClassName("newData");
		}
		
		if(mobileOldValue.getInnerText().equals("")){
			mobileOldValue.setClassName("nullDataStyle");
		}else{
			mobileOldValue.removeClassName("nullDataStyle");
		}
		if(mobileNewValue.getInnerText().equals("")){
			mobileNewValue.setClassName("nullDataStyle");
		}else{
			mobileNewValue.removeClassName("nullDataStyle");
		}
		
		emailOldValue.setHref("mailto:" + (proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		emailNewValue.setHref("mailto:" + (spStandardizedPatientProxy.getEmail() == null ? "" : String.valueOf(spStandardizedPatientProxy.getEmail())));
		
		emailOldValue.setText((proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		emailNewValue.setText((spStandardizedPatientProxy.getEmail() == null ? "" : String.valueOf(spStandardizedPatientProxy.getEmail())));
		
		if(!emailOldValue.toString().equals(emailNewValue.toString())){

			emailOldValue.addStyleName("oldData");
			emailNewValue.addStyleName("newData");
			isChangedData=true;
		}else{
			emailOldValue.removeStyleName("oldData");
			emailNewValue.removeStyleName("newData");
		}
		if(isChangedData){
			patientPanel.getTabWidget(0).getParent().addStyleName("chnagedTabStyle");
		}else{
			patientPanel.getTabWidget(0).getParent().removeStyleName("chnagedTabStyle");
		}
	}

	private void setParticularsValue(StandardizedPatientProxy proxy,SpStandardizedPatientProxy spStandardizedPatientProxy) {
		boolean isChangedData=false;
		
		genderOldValue.setInnerText(new EnumRenderer<Gender>().render(proxy.getGender()));
		genderNewValue.setInnerText(new EnumRenderer<Gender>().render(spStandardizedPatientProxy.getGender()));
		
		if(!genderOldValue.getInnerText().equals(genderNewValue.getInnerText())){

			genderOldValue.addClassName("oldData");
			genderNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			genderOldValue.removeClassName("oldData");
			genderNewValue.removeClassName("newData");
		}
		
		if(genderOldValue.getInnerText().equals("")){
			genderOldValue.setClassName("nullDataStyle");
		}else{
			genderOldValue.removeClassName("nullDataStyle");
		}
		if(genderNewValue.getInnerText().equals("")){
			genderNewValue.setClassName("nullDataStyle");
		}else{
			genderNewValue.removeClassName("nullDataStyle");
		}
		birthdayOldValue.setInnerText(proxy.getBirthday() == null ? "" : DateTimeFormat.getFormat(constants.dateTimeFormat()).format(proxy.getBirthday()));
		birthdayNewValue.setInnerText(spStandardizedPatientProxy.getBirthday() == null ? "" : DateTimeFormat.getFormat(constants.dateTimeFormat()).format(spStandardizedPatientProxy.getBirthday()));
		
		if(!birthdayOldValue.getInnerText().equals(birthdayNewValue.getInnerText())){

			birthdayOldValue.addClassName("oldData");
			birthdayNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			birthdayOldValue.removeClassName("oldData");
			birthdayNewValue.removeClassName("newData");
		}
		
		if(birthdayOldValue.getInnerText().equals("")){
			birthdayOldValue.setClassName("nullDataStyle");
		}else{
			birthdayOldValue.removeClassName("nullDataStyle");
		}
		if(birthdayNewValue.getInnerText().equals("")){
			birthdayNewValue.setClassName("nullDataStyle");
		}else{
			birthdayNewValue.removeClassName("nullDataStyle");
		}
		
		heightOldValue.setInnerText(proxy.getHeight() == null ? "" : String.valueOf(proxy.getHeight()));
		heightNewValue.setInnerText(spStandardizedPatientProxy.getHeight() == null ? "" : String.valueOf(spStandardizedPatientProxy.getHeight()));
		
		if(!heightOldValue.getInnerText().equals(heightNewValue.getInnerText())){

			heightOldValue.addClassName("oldData");
			heightNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			heightOldValue.removeClassName("oldData");
			heightNewValue.removeClassName("newData");
		}
		
		if(heightOldValue.getInnerText().equals("")){
			heightOldValue.setClassName("nullDataStyle");
		}else{
			heightOldValue.removeClassName("nullDataStyle");
		}
		if(heightNewValue.getInnerText().equals("")){
			heightNewValue.setClassName("nullDataStyle");
		}else{
			heightNewValue.removeClassName("nullDataStyle");
		}
		
		weightOldValue.setInnerText(proxy.getWeight() == null ? "" : String.valueOf(proxy.getWeight()));
		weightNewValue.setInnerText(spStandardizedPatientProxy.getWeight() == null ? "" : String.valueOf(spStandardizedPatientProxy.getWeight()));
		
		if(!weightOldValue.getInnerText().equals(weightNewValue.getInnerText())){

			weightOldValue.addClassName("oldData");
			weightNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			weightOldValue.removeClassName("oldData");
			weightNewValue.removeClassName("newData");
		}
		
		if(weightOldValue.getInnerText().equals("")){
			weightOldValue.setClassName("nullDataStyle");
		}else{
			weightOldValue.removeClassName("nullDataStyle");
		}
		if(weightNewValue.getInnerText().equals("")){
			weightNewValue.setClassName("nullDataStyle");
		}else{
			weightNewValue.removeClassName("nullDataStyle");
		}
		
		workPermissionOldValue.setInnerText(new EnumRenderer<WorkPermission>().render(proxy.getWorkPermission()));
		workPermissionNewValue.setInnerText(new EnumRenderer<WorkPermission>().render(spStandardizedPatientProxy.getWorkPermission()));
		
		if(!workPermissionOldValue.getInnerText().equals(workPermissionNewValue.getInnerText())){

			workPermissionOldValue.addClassName("oldData");
			workPermissionNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			workPermissionOldValue.removeClassName("oldData");
			workPermissionNewValue.removeClassName("newData");
		}
		
		if(workPermissionOldValue.getInnerText().equals("")){
			workPermissionOldValue.setClassName("nullDataStyle");
		}else{
			workPermissionOldValue.removeClassName("nullDataStyle");
		}
		if(workPermissionNewValue.getInnerText().equals("")){
			workPermissionNewValue.setClassName("nullDataStyle");
		}else{
			workPermissionNewValue.removeClassName("nullDataStyle");
		}
		
		maritalStatusOldValue.setInnerText(new EnumRenderer<MaritalStatus>().render(proxy.getMaritalStatus()));
		maritalStatusNewValue.setInnerText(new EnumRenderer<MaritalStatus>().render(spStandardizedPatientProxy.getMaritalStatus()));
		
		if(!maritalStatusOldValue.getInnerText().equals(maritalStatusNewValue.getInnerText())){

			maritalStatusOldValue.addClassName("oldData");
			maritalStatusNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			maritalStatusOldValue.removeClassName("oldData");
			maritalStatusNewValue.removeClassName("newData");
		}
		
		if(maritalStatusOldValue.getInnerText().equals("")){
			maritalStatusOldValue.setClassName("nullDataStyle");
		}else{
			maritalStatusOldValue.removeClassName("nullDataStyle");
		}
		if(maritalStatusNewValue.getInnerText().equals("")){
			maritalStatusNewValue.setClassName("nullDataStyle");
		}else{
			maritalStatusNewValue.removeClassName("nullDataStyle");
		}
		
		socialInsuranceNoOldValue.setInnerText((proxy.getSocialInsuranceNo() == null) ? "" : proxy.getSocialInsuranceNo());
		socialInsuranceNoNewValue.setInnerText((spStandardizedPatientProxy.getSocialInsuranceNo() == null) ? "" : spStandardizedPatientProxy.getSocialInsuranceNo());
		
		if(!socialInsuranceNoOldValue.getInnerText().equals(socialInsuranceNoNewValue.getInnerText())){

			socialInsuranceNoOldValue.addClassName("oldData");
			socialInsuranceNoNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			socialInsuranceNoOldValue.removeClassName("oldData");
			socialInsuranceNoNewValue.removeClassName("newData");
		}
		
		if(socialInsuranceNoOldValue.getInnerText().equals("")){
			socialInsuranceNoOldValue.setClassName("nullDataStyle");
		}else{
			socialInsuranceNoOldValue.removeClassName("nullDataStyle");
		}
		if(socialInsuranceNoNewValue.getInnerText().equals("")){
			socialInsuranceNoNewValue.setClassName("nullDataStyle");
		}else{
			socialInsuranceNoNewValue.removeClassName("nullDataStyle");
		}
		
		nationalityOldValue.setInnerText(proxy.getNationality() == null ? "" : (proxy.getNationality().getNationality() == null) ? "" : proxy.getNationality().getNationality());
		nationalityNewValue.setInnerText(spStandardizedPatientProxy.getNationality() == null ? "" : (spStandardizedPatientProxy.getNationality().getNationality() == null) ? "" : spStandardizedPatientProxy.getNationality().getNationality());
		
		if(!nationalityOldValue.getInnerText().equals(nationalityNewValue.getInnerText())){

			nationalityOldValue.addClassName("oldData");
			nationalityNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			nationalityOldValue.removeClassName("oldData");
			nationalityNewValue.removeClassName("newData");
		}
		
		if(nationalityOldValue.getInnerText().equals("")){
			nationalityOldValue.setClassName("nullDataStyle");
		}else{
			nationalityOldValue.removeClassName("nullDataStyle");
		}
		if(nationalityNewValue.getInnerText().equals("")){
			nationalityNewValue.setClassName("nullDataStyle");
		}else{
			nationalityNewValue.removeClassName("nullDataStyle");
		}
		
		professionOldValue.setInnerText(proxy.getProfession() == null ? "" : (proxy.getProfession().getProfession() == null) ? "" : proxy.getProfession().getProfession());
		professionNewValue.setInnerText(spStandardizedPatientProxy.getProfession() == null ? "" : (spStandardizedPatientProxy.getProfession().getProfession() == null) ? "" : spStandardizedPatientProxy.getProfession().getProfession());
		
		if(!professionOldValue.getInnerText().equals(professionNewValue.getInnerText())){

			professionOldValue.addClassName("oldData");
			professionNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			professionOldValue.removeClassName("oldData");
			professionNewValue.removeClassName("newData");
		}
		
		if(professionOldValue.getInnerText().equals("")){
			professionOldValue.setClassName("nullDataStyle");
		}else{
			professionOldValue.removeClassName("nullDataStyle");
		}
		if(professionNewValue.getInnerText().equals("")){
			professionNewValue.setClassName("nullDataStyle");
		}else{
			professionNewValue.removeClassName("nullDataStyle");
		}
		
		if(isChangedData){
			patientPanel.getTabWidget(1).getParent().addStyleName("chnagedTabStyle");
		}else{
			patientPanel.getTabWidget(1).getParent().removeStyleName("chnagedTabStyle");
		}
	}
	private void setBankDetailsValue(StandardizedPatientProxy proxy,SpStandardizedPatientProxy spStandardizedPatientProxy) {
		
		boolean isChangedData=false;
		
		BankaccountProxy bank = proxy.getBankAccount();
		
		SpBankaccountProxy spBank = spStandardizedPatientProxy.getBankAccount();
		
		bankNameOldValue.setInnerText((bank == null || bank.getBankName() == null) ? "" : String.valueOf(bank.getBankName()));
		bankNameNewValue.setInnerText((spBank == null || spBank.getBankName() == null) ? "" : String.valueOf(spBank.getBankName()));
		
		if(!bankNameOldValue.getInnerText().equals(bankNameNewValue.getInnerText())){

			bankNameOldValue.addClassName("oldData");
			bankNameNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			bankNameOldValue.removeClassName("oldData");
			bankNameNewValue.removeClassName("newData");
		}
		
		if(bankNameOldValue.getInnerText().equals("")){
			bankNameOldValue.setClassName("nullDataStyle");
		}else{
			bankNameOldValue.removeClassName("nullDataStyle");
		}
		if(bankNameNewValue.getInnerText().equals("")){
			bankNameNewValue.setClassName("nullDataStyle");
		}else{
			bankNameNewValue.removeClassName("nullDataStyle");
		}
		
		bankIBANOldValue.setInnerText((bank == null || bank.getIBAN() == null) ? "" : String.valueOf(bank.getIBAN()));
		bankIBANNewValue.setInnerText((spBank == null || spBank.getIBAN() == null) ? "" : String.valueOf(spBank.getIBAN()));
		
		if(!bankIBANOldValue.getInnerText().equals(bankIBANNewValue.getInnerText())){

			bankIBANOldValue.addClassName("oldData");
			bankIBANNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			bankIBANOldValue.removeClassName("oldData");
			bankIBANNewValue.removeClassName("newData");
		}
		
		if(bankIBANOldValue.getInnerText().equals("")){
			bankIBANOldValue.setClassName("nullDataStyle");
		}else{
			bankIBANOldValue.removeClassName("nullDataStyle");
		}
		if(bankIBANNewValue.getInnerText().equals("")){
			bankIBANNewValue.setClassName("nullDataStyle");
		}else{
			bankIBANNewValue.removeClassName("nullDataStyle");
		}
		
		bankBICOldValue.setInnerText((bank == null || bank.getBIC() == null) ? "" : String.valueOf(bank.getBIC()));
		bankBICNewValue.setInnerText((spBank == null || spBank.getBIC() == null) ? "" : String.valueOf(spBank.getBIC()));
		
		if(!bankBICOldValue.getInnerText().equals(bankBICNewValue.getInnerText())){

			bankBICOldValue.addClassName("oldData");
			bankBICNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			bankBICOldValue.removeClassName("oldData");
			bankBICNewValue.removeClassName("newData");
		}
		
		if(bankBICOldValue.getInnerText().equals("")){
			bankBICOldValue.setClassName("nullDataStyle");
		}else{
			bankBICOldValue.removeClassName("nullDataStyle");
		}
		if(bankBICNewValue.getInnerText().equals("")){
			bankBICNewValue.setClassName("nullDataStyle");
		}else{
			bankBICNewValue.removeClassName("nullDataStyle");
		}
		
		if (bank == null || bank.getCountry() == null) {
			bankCountryOldValue.setInnerText("");
		} else {
			bankCountryOldValue.setInnerText(String.valueOf(bank.getCountry().getNationality()));
		}
		
		if (spBank == null || spBank.getCountry() == null) {
			bankCountryNewValue.setInnerText("");
		} else {
			bankCountryNewValue.setInnerText(String.valueOf(spBank.getCountry().getNationality()));
		}
		
		if(!bankCountryOldValue.getInnerText().equals(bankCountryNewValue.getInnerText())){

			bankCountryOldValue.addClassName("oldData");
			bankCountryNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			bankCountryOldValue.removeClassName("oldData");
			bankCountryNewValue.removeClassName("newData");
		}
		
		if(bankCountryOldValue.getInnerText().equals("")){
			bankCountryOldValue.setClassName("nullDataStyle");
		}else{
			bankCountryOldValue.removeClassName("nullDataStyle");
		}
		if(bankCountryNewValue.getInnerText().equals("")){
			bankCountryNewValue.setClassName("nullDataStyle");
		}else{
			bankCountryNewValue.removeClassName("nullDataStyle");
		}
		
		bankCityOldValue.setInnerText((bank == null || bank.getCity() == null) ? "" : String.valueOf(bank.getCity()));
		bankCityNewValue.setInnerText((spBank == null || spBank.getCity() == null) ? "" : String.valueOf(spBank.getCity()));
		
		if(!bankCityOldValue.getInnerText().equals(bankCityNewValue.getInnerText())){

			bankCityOldValue.addClassName("oldData");
			bankCityNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			bankCityOldValue.removeClassName("oldData");
			bankCityNewValue.removeClassName("newData");
		}
		
		if(bankCityOldValue.getInnerText().equals("")){
			bankCityOldValue.setClassName("nullDataStyle");
		}else{
			bankCityOldValue.removeClassName("nullDataStyle");
		}
		if(bankCityNewValue.getInnerText().equals("")){
			bankCityNewValue.setClassName("nullDataStyle");
		}else{
			bankCityNewValue.removeClassName("nullDataStyle");
		}
		
		bankPostalCodeOldValue.setInnerText((bank == null || bank.getPostalCode() == null) ? "" : String.valueOf(bank.getPostalCode()));
		bankPostalCodeNewValue.setInnerText((spBank == null || spBank.getPostalCode() == null) ? "" : String.valueOf(spBank.getPostalCode()));
		

		if(!bankPostalCodeOldValue.getInnerText().equals(bankPostalCodeNewValue.getInnerText())){

			bankPostalCodeOldValue.addClassName("oldData");
			bankPostalCodeNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			bankPostalCodeOldValue.removeClassName("oldData");
			bankPostalCodeNewValue.removeClassName("newData");
		}
		
		if(bankPostalCodeOldValue.getInnerText().equals("")){
			bankPostalCodeOldValue.setClassName("nullDataStyle");
		}else{
			bankPostalCodeOldValue.removeClassName("nullDataStyle");
		}
		if(bankPostalCodeNewValue.getInnerText().equals("")){
			bankPostalCodeNewValue.setClassName("nullDataStyle");
		}else{
			bankPostalCodeNewValue.removeClassName("nullDataStyle");
		}
		
		bankOwnerNameOldValue.setInnerText((bank == null || bank.getOwnerName() == null) ? "" : String.valueOf(bank.getOwnerName()));
		bankOwnerNameNewValue.setInnerText((spBank == null || spBank.getOwnerName() == null) ? "" : String.valueOf(spBank.getOwnerName()));
		
		if(!bankOwnerNameOldValue.getInnerText().equals(bankOwnerNameNewValue.getInnerText())){

			bankOwnerNameOldValue.addClassName("oldData");
			bankOwnerNameNewValue.addClassName("newData");
			isChangedData=true;
		}else{
			bankOwnerNameOldValue.removeClassName("oldData");
			bankOwnerNameNewValue.removeClassName("newData");
		}
		
		if(bankOwnerNameOldValue.getInnerText().equals("")){
			bankOwnerNameOldValue.setClassName("nullDataStyle");
		}else{
			bankOwnerNameOldValue.removeClassName("nullDataStyle");
		}
		if(bankOwnerNameNewValue.getInnerText().equals("")){
			bankOwnerNameNewValue.setClassName("nullDataStyle");
		}else{
			bankOwnerNameNewValue.removeClassName("nullDataStyle");
		}
		
		if(isChangedData){
			patientPanel.getTabWidget(2).getParent().addStyleName("chnagedTabStyle");
		}else{
			patientPanel.getTabWidget(2).getParent().removeStyleName("chnagedTabStyle");
		}
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	/*@Override
	public void setPresenter(Presenter StandardizedPatientActivity) {
		this.presenter =  StandardizedPatientActivity;
	}*/

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public StandardizedPatientProxy getValue() {
		return proxy;
	}
	
	@Override
	public boolean isPatientDisclosurePanelOpen() {
		return patientDisclosurePanel.isOpen();
	}
	
	/*@Override
	public boolean isAnamnesisDisclosurePanelOpen() {
		return anamnesisDisclosurePanel.isOpen();
	}
	
	
	@Override
	public int getSelectedDetailsTab() {
		//ScrolledTab Changes start
		//return patientPanel.getTabBar().getSelectedTab();
		return patientPanel.getSelectedIndex();
		//ScrolledTab Changes start
	}*/
	
	@UiHandler("anamnesisPanelArrow")
	public void anamnesisPanelHandler(ClickEvent e) {
		boolean isOpen = anamnesisDisclosurePanel.isOpen(); 
		setAnamnesisDisclosurePanelOpen(!isOpen);
		storeDisplaySettings();
	}
	
	//@Override
	public void setAnamnesisDisclosurePanelOpen(boolean value) {
		ImageResource icon = (value) ? uiIcons.triangle1South() : uiIcons.triangle1East();
		anamnesisDisclosurePanel.setOpen(value);
		anamnesisPanelArrow.setResource(icon);
	}

	@UiHandler("patientPanelArrow")
	public void handleClick(ClickEvent e) {
		if (patientDisclosurePanel.isOpen()) {
			setPatientDisclosurePanelOpen(false);
			spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.setHeight((ResolutionSettings.getRightWidgetHeight()-200)+"px");
			
			
		} else {
			setPatientDisclosurePanelOpen(true);
			spDetailsReviewAnamnesisSubViewImpl.anamnesisTabs.setHeight((ResolutionSettings.getRightWidgetHeight()/2)+"px");
		}
		storeDisplaySettings();
	}

	@Override
	public void setPatientDisclosurePanelOpen(boolean value) {
		ImageResource icon = (value) ? uiIcons.triangle1South() : uiIcons.triangle1East();
		patientDisclosurePanel.setOpen(value);
		patientPanelArrow.setResource(icon);
	}

	public void setViewVisible(boolean isViewVisible) {
		if(isViewVisible){
			spDetailsReviewViewImpl.show();
		}
		else{
			spDetailsReviewViewImpl.hide();
		}
	}

	public SPDetailsReviewAnamnesisSubViewImpl getSpDetailsReviewAnamnesisSubViewImpl() {
		return spDetailsReviewAnamnesisSubViewImpl;
	}

	public IconButton getAcceptChangesButton() {
		return acceptChangesButton;
	}

	public IconButton getDiscardChangesButton() {
		return discardChangesButton;
	}

}
