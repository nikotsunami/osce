package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CriteriaPopupViewImpl extends PopupPanel implements CriteriaPopupView{
	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	@UiField 
	TextBox criteriaTxtBox;
	
	public TextBox getCriteriaTxtBox() {
		return criteriaTxtBox;
	}

	public void setCriteriaTxtBox(TextBox criteriaTxtBox) {
		this.criteriaTxtBox = criteriaTxtBox;
	}

	@UiField
	Button okBtn;
	
	
	public Button getOkBtn() {
		return okBtn;
	}

	public void setOkBtn(Button okBtn) {
		this.okBtn = okBtn;
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public CriteriaPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		okBtn.setText(constants.okBtn());
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, CriteriaPopupViewImpl> {
	}
	
	
}
