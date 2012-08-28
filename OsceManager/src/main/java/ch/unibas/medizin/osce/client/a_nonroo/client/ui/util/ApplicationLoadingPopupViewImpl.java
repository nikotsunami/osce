package ch.unibas.medizin.osce.client.a_nonroo.client.ui.util;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationLoadingPopupViewImpl extends DialogBox implements
		ApplicationLoadingPopupView {

	private static ApplicationLoadingPopupViewImplUiBinder uiBinder = GWT
			.create(ApplicationLoadingPopupViewImplUiBinder.class);

	interface ApplicationLoadingPopupViewImplUiBinder extends
			UiBinder<Widget, ApplicationLoadingPopupViewImpl> {
	}

//	private final OsceConstants constants = GWT.create(OsceConstants.class);

//	@UiField
//	NotificationMole mole;

//	@UiField
//	VerticalPanel vPanel;

	static private ApplicationLoadingPopupViewImpl applicationLoadingPopupViewImpl;

	private ApplicationLoadingPopupViewImpl() {

		setWidget(uiBinder.createAndBindUi(this));
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setAutoHideEnabled(false);
		center();
//		setText(constants.loading());
		this.getElement().getStyle().setZIndex(1); 

	}

	public static void showApplicationLoadingPopup(boolean show) {

		if (show) {
			if (applicationLoadingPopupViewImpl == null) {
				applicationLoadingPopupViewImpl = new ApplicationLoadingPopupViewImpl();
			}
			if (!applicationLoadingPopupViewImpl.isShowing()) {
				applicationLoadingPopupViewImpl.show();
			}
		} else if (applicationLoadingPopupViewImpl != null) {
			applicationLoadingPopupViewImpl.hide();
		}
	}

}