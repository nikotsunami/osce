package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;

import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

public interface AnamnesisCheckTitlePopupView {
	public interface Delegate {
		public void saveEditedTitle();
	}
	
	public RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitlePopupViewImpl> createEditorDriver();
	public void setDelegate(Delegate delegate);
	public void hide();
	public boolean isShowing();
}
