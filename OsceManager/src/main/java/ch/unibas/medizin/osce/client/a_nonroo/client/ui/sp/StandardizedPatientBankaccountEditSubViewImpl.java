package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientBankaccountEditSubViewImpl extends Composite implements StandardizedPatientBankaccountEditSubView, Editor<BankaccountProxy> {
	private static final Binder BINDER = GWT.create(Binder.class);
	private Delegate delegate;
	
	@UiField
	SpanElement labelBankName;
	@UiField
	SpanElement labelBankBIC;
	@UiField
	SpanElement labelBankIBAN;
	@UiField
	SpanElement labelBankPlz;
	@UiField
	SpanElement labelBankCity;
	@UiField
	SpanElement labelBankCountry;
	@UiField
	SpanElement labelOwnerName;
	
	@UiField
	TextBox bankName;
	@UiField
	TextBox BIC;
	@UiField
	TextBox IBAN;
	@UiField
	TextBox postalCode;
	@UiField
	TextBox city;
	
	//Issue # 122 : Replace pull down with autocomplete.
	@UiField
	public DefaultSuggestBox<NationalityProxy, EventHandlingValueHolderItem<NationalityProxy>> country;

	/*@UiField (provided = true)
	ValueListBox<NationalityProxy> country = new ValueListBox<NationalityProxy>(new NationalityProxyRenderer());
	*/
	//Issue # 122 : Replace pull down with autocomplete.
	@UiField
	TextBox ownerName;
	
	interface Binder extends UiBinder<Widget, StandardizedPatientBankaccountEditSubViewImpl> {}
	interface Driver extends RequestFactoryEditorDriver<BankaccountProxy, StandardizedPatientBankaccountEditSubViewImpl> {}
	
	// Highlight onViolation
	Map<String, Widget> bankAccountMap;
	// E Highlight onViolation
	
	public StandardizedPatientBankaccountEditSubViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		OsceConstants constants = GWT.create(OsceConstants.class);
		labelBankName.setInnerText(constants.bank());
		labelBankBIC.setInnerText(constants.bic());
		labelBankIBAN.setInnerText(constants.iban());
		labelBankPlz.setInnerText(constants.plz());
		labelBankCity.setInnerText(constants.city());
		labelBankCountry.setInnerText(constants.country());
		labelOwnerName.setInnerText(constants.ownerName());
		
		// Highlight onViolation
		bankAccountMap=new HashMap<String, Widget>();
		bankAccountMap.put("bankName", bankName);
		bankAccountMap.put("IBAN", IBAN);
		bankAccountMap.put("BIC", BIC);
		bankAccountMap.put("ownerName", ownerName);
		bankAccountMap.put("postalCode", postalCode);
		bankAccountMap.put("city", city);
		bankAccountMap.put("country", country);		
		// E Highlight onViolation
	}
	
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public RequestFactoryEditorDriver<BankaccountProxy, StandardizedPatientBankaccountEditSubViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<BankaccountProxy, StandardizedPatientBankaccountEditSubViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}


	@Override
	public void setCountryPickerValues(Collection<NationalityProxy> values) {
	//	country.setAcceptableValues(values);
	//}

		//Issue # 122 : Replace pull down with autocomplete.
		DefaultSuggestOracle<NationalityProxy> suggestOracle1 = (DefaultSuggestOracle<NationalityProxy>) country.getSuggestOracle();
		suggestOracle1.setPossiblilities((List)values);
		country.setSuggestOracle(suggestOracle1);
		//country.setRenderer(new NationalityProxyRenderer());
		country.setRenderer(new AbstractRenderer<NationalityProxy>() {

			@Override
			public String render(NationalityProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getNationality();
				}
				else
				{
					return "";
				}
			}
		});
		//country.setAcceptableValues(values);

		//Issue # 122 : Replace pull down with autocomplete.
	}


	@Override
	public Map getBankAccountMap() {
		// TODO Auto-generated method stub
		//return null;
		return bankAccountMap;
	}
	
	@Override
	public DefaultSuggestBox<NationalityProxy, EventHandlingValueHolderItem<NationalityProxy>> getCountry()
	{
		return country;
	}
	
}
