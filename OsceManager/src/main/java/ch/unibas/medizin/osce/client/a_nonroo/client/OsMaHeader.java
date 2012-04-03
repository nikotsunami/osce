package ch.unibas.medizin.osce.client.a_nonroo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OsMaHeader extends Composite {

	private static OsMaHeaderUiBinder uiBinder = GWT.create(OsMaHeaderUiBinder.class);

	interface OsMaHeaderUiBinder extends UiBinder<Widget, OsMaHeader> {
	}

	public OsMaHeader() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
