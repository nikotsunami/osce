package ch.unibas.medizin.osce.client.managed.ui;

import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class UsedMaterialMobileDetailsView extends
		UsedMaterialMobileDetailsView_Roo_Gwt {

	private static final Binder BINDER = GWT.create(Binder.class);

	private static ch.unibas.medizin.osce.client.managed.ui.UsedMaterialMobileDetailsView instance;

	@UiField
	HasClickHandlers delete;

	private Delegate delegate;

	public UsedMaterialMobileDetailsView() {
		initWidget(BINDER.createAndBindUi(this));
	}

	public static ch.unibas.medizin.osce.client.managed.ui.UsedMaterialMobileDetailsView instance() {
		if (instance == null) {
			instance = new UsedMaterialMobileDetailsView();
		}
		return instance;
	}

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public UsedMaterialProxy getValue() {
		return proxy;
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	interface Binder extends UiBinder<HTMLPanel, UsedMaterialMobileDetailsView> {
	}
}
