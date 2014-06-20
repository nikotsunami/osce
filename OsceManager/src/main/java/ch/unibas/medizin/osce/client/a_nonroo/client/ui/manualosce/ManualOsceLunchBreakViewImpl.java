package ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ManualOsceLunchBreakViewImpl extends Composite implements ManualOsceLunchBreakView {

	private static ManualOsceLunchBreakViewImplUiBinder uiBinder = GWT.create(ManualOsceLunchBreakViewImplUiBinder.class);
	
	interface ManualOsceLunchBreakViewImplUiBinder extends UiBinder<Widget, ManualOsceLunchBreakViewImpl> {
	}

	private Delegate delegate;
	
	@UiField
	TextBox lunchBreakDuration;
	
	public ManualOsceLunchBreakViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	public TextBox getLunchBreakDuration() {
		return lunchBreakDuration;
	}
	
	public void setLunchBreakDuration(TextBox lunchBreakDuration) {
		this.lunchBreakDuration = lunchBreakDuration;
	}
}
