package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MajorMinorPopupPanelViewImpl extends PopupPanel{
	private static MajorMinorPopupPanelViewImplUiBinder uiBinder = GWT.create(MajorMinorPopupPanelViewImplUiBinder.class);

	interface MajorMinorPopupPanelViewImplUiBinder extends
			UiBinder<Widget, MajorMinorPopupPanelViewImpl> {
	}
	
	@UiField
	 VerticalPanel major;
	
	@UiField
	VerticalPanel minor;
	
	@UiField
	Label majorLbl;
	
	@UiField
	Label minorLbl;
	
	
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	
	public MajorMinorPopupPanelViewImpl() {
		super(true);
		add(uiBinder.createAndBindUi(this));
		minorLbl.setText(constants.minorLbl());
		majorLbl.setText(constants.majorLbl());
		
		
	}
}
