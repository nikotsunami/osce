package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PaymentViewImpl extends Composite implements PaymentView {

	private static PaymentViewImplUiBinder uiBinder = GWT
			.create(PaymentViewImplUiBinder.class);

	interface PaymentViewImplUiBinder extends UiBinder<Widget, PaymentViewImpl> {
	}
	
	@UiField
	IconButton exportButton;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public Delegate delegate;
	
	public Presenter presenter;

	public PaymentViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		exportButton.setText(constants.export());
	}

	@UiHandler("exportButton")
	void onClick(ClickEvent e) {
		delegate.printRecord();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter systemStartActivity) {
		this.presenter = systemStartActivity;
	}

}
