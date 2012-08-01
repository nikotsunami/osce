package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OsceSequenceViewImpl  extends Composite implements OsceSequenceView{
	
	private static OsceSequenceViewImplUiBinder uiBinder = GWT
			.create(OsceSequenceViewImplUiBinder.class);

	interface OsceSequenceViewImplUiBinder extends UiBinder<Widget, OsceSequenceViewImpl> {
	}

	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	HorizontalPanel accordianHP;
	
	public HorizontalPanel getAccordianHP() {
		return accordianHP;
	}

	public OsceSequenceViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

}
