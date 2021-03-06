package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class RoleAssignmentPopupViewImpl extends DialogBox implements
		RoleAssignmentPopupView {

	interface Binder extends UiBinder<Widget, RoleAssignmentPopupViewImpl> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);
	private static RoleAssignmentPopupViewImpl ROLE_ASSIGNMENT_POPUP_VIEW_IMPL;

	public final static OsceConstants constants = GWT.create(OsceConstants.class);

	@UiField
	Label trainingHeaderLabel;

	@UiField
	Label trainingTextLabel;

	@UiField
	Label OsceHeaderLabel;

	@UiField
	Label OsceTextLabel;

	@UiField
	Label roleAssignHeaderLabel;

	@UiField
	Label roleAssignTextLabel;

	@UiField
	Button okButton;

	public RoleAssignmentPopupViewImpl() {
		setWidget(BINDER.createAndBindUi(this));
		
		this.trainingHeaderLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		this.OsceHeaderLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		this.roleAssignHeaderLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		this.setTrainingHeaderText(constants.roleAcceptedTrainings());
		this.setOsceHeaderText(constants.roleAcceptedOsces());
		this.setRoleAssignHeaderText(constants.roleAssignedRoles());
		this.okButton.setText(constants.okBtn());

		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.setText(constants.rolePatientInformation());
		this.center();
		
		this.getElement().getStyle().setZIndex(1); 
	}

	@UiHandler("okButton")
	public void onClickEvent(ClickEvent clickEvent) {
		showRoleAssignmentPopup(false);
	}

	private static void showRoleAssignmentPopup(boolean show) {

		if (show) {
			if (!ROLE_ASSIGNMENT_POPUP_VIEW_IMPL.isShowing()) {
				ROLE_ASSIGNMENT_POPUP_VIEW_IMPL.show();
			}
		} else if (ROLE_ASSIGNMENT_POPUP_VIEW_IMPL != null) {
			ROLE_ASSIGNMENT_POPUP_VIEW_IMPL.hide();
		}
	}

	public static void setPopUpText(String trainings, String OSCEDay,
			String roleAssigned, int left, int top) {
		if (ROLE_ASSIGNMENT_POPUP_VIEW_IMPL == null) {
			ROLE_ASSIGNMENT_POPUP_VIEW_IMPL = new RoleAssignmentPopupViewImpl();
		}

		ROLE_ASSIGNMENT_POPUP_VIEW_IMPL.setTrainingText(trainings.isEmpty() ? constants.noEntries() : trainings);
		ROLE_ASSIGNMENT_POPUP_VIEW_IMPL.setOsceText(OSCEDay.isEmpty() ? constants.noEntries() : OSCEDay);
		ROLE_ASSIGNMENT_POPUP_VIEW_IMPL.setRoleAssignText(roleAssigned.isEmpty() ? constants.noEntries() : roleAssigned);
		
		ROLE_ASSIGNMENT_POPUP_VIEW_IMPL.setPopupPosition(left, top);

		showRoleAssignmentPopup(true);
	}

	private void setTrainingText(String trainingText) {
		this.trainingTextLabel.setText(trainingText);
	}

	private void setOsceText(String osceText) {
		this.OsceTextLabel.setText(osceText);
	}

	private void setRoleAssignText(String roleAssignText) {
		this.roleAssignTextLabel.setText(roleAssignText);
	}

	private void setTrainingHeaderText(String trainingHeaderText) {
		this.trainingHeaderLabel.setText(trainingHeaderText);
	}

	private void setOsceHeaderText(String osceHeaderText) {
		this.OsceHeaderLabel.setText(osceHeaderText);
	}

	public void setRoleAssignHeaderText(String roleAssignHeaderText) {
		this.roleAssignHeaderLabel.setText(roleAssignHeaderText);
	}

}
