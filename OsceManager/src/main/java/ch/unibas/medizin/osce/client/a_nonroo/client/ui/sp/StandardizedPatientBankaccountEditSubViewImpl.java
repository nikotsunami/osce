package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;
import ch.unibas.medizin.osce.shared.Gender;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
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
	TextBox bankName;
	@UiField
	TextBox BIC;
	@UiField
	TextBox IBAN;
	
	interface Binder extends UiBinder<Widget, StandardizedPatientBankaccountEditSubViewImpl> {}
	interface Driver extends RequestFactoryEditorDriver<BankaccountProxy, StandardizedPatientBankaccountEditSubViewImpl> {}
	
	public StandardizedPatientBankaccountEditSubViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		labelBankName.setInnerText(Messages.BANK_NAME);
		labelBankBIC.setInnerText(Messages.BANK_BIC);
		labelBankIBAN.setInnerText(Messages.BANK_IBAN);
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
}
