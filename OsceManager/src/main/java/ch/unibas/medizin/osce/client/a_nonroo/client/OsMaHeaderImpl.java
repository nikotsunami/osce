package ch.unibas.medizin.osce.client.a_nonroo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OsMaHeaderImpl extends Composite implements OsMaHeader {

	private static OsMaHeaderUiBinder uiBinder = GWT.create(OsMaHeaderUiBinder.class);

	interface OsMaHeaderUiBinder extends UiBinder<Widget, OsMaHeaderImpl> {
	}
	
	private Delegate delegate;

	public OsMaHeaderImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

}
