package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.Collection;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.NationalityProxyRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
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
	IntegerBox postalCode;
	@UiField
	TextBox city;
	@UiField (provided = true)
	ValueListBox<NationalityProxy> country = new ValueListBox<NationalityProxy>(new NationalityProxyRenderer());
	@UiField
	TextBox ownerName;
	
	interface Binder extends UiBinder<Widget, StandardizedPatientBankaccountEditSubViewImpl> {}
	interface Driver extends RequestFactoryEditorDriver<BankaccountProxy, StandardizedPatientBankaccountEditSubViewImpl> {}
	
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
		country.setAcceptableValues(values);
	}
}
