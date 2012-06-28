package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnamnesisCheckTitleEditViewImpl extends Composite implements AnamnesisCheckTitleEditView, Editor<AnamnesisCheckTitleProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private boolean multipleFields = false;
	public int selectedIndex= -1;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private  AnamnesisCheckTitleProxy anamnesisCheckTitle=null;

	@UiField
	TabPanel anamnesisTitlePanel;
	
	@UiField
	SpanElement header;
	

	
	@UiField
	TextBox text;

	
	// TODO: Fill
	public String value = "";
	

	@UiField
	SpanElement labelText;

	@UiField
	SpanElement labelpreviousTitle;
//

	@UiField
	ListBox	previousTitleListBox;



	@UiField
	IconButton cancel;
	@UiField
	IconButton save;

	IconButton addButton = new IconButton();

	@UiField
	DivElement errors;

	private Delegate delegate;
	private Presenter presenter;
	
	ArrayList<HorizontalPanel> mCFields = new ArrayList<HorizontalPanel>();

	public AnamnesisCheckTitleEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
		anamnesisTitlePanel.selectTab(0);
		anamnesisTitlePanel.getTabBar().setTabText(0, constants.anamnesisValues());
		TabPanelHelper.moveTabBarToBottom(anamnesisTitlePanel);

		cancel.setText(constants.cancel());
		save.setText(constants.save());
		addButton.setIcon("plusthick");
		addButton.setText(constants.addAnswer());
		addButton.setVisible(false);

		labelText.setInnerText(constants.anamnesisCheckTitle() + ":");
		labelpreviousTitle.setInnerText(constants.previousTitle()+":");
		previousTitleListBox.addItem(constants.previousTitle(),"");
	}
	

	private IconButton createDeleteButton() {
		IconButton button = new IconButton();
		button.setIcon("trash");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				deleteValueField((HorizontalPanel) ((Widget) event.getSource()).getParent());
			}
		});
		return button;
	}
	
	

	@Override
	public RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public void setEnabled(boolean enabled) {
		save.setEnabled(enabled);
	}

	public void showErrors(List<EditorError> errors) {
		SafeHtmlBuilder b = new SafeHtmlBuilder();
		for (EditorError error : errors) {
			b.appendEscaped(error.getPath()).appendEscaped(": ");
			b.appendEscaped(error.getMessage()).appendHtmlConstant("<br>");
		}
		this.errors.setInnerHTML(b.toSafeHtml().asString());
	}

	@UiHandler("cancel")
	void onCancel(ClickEvent event) {
		delegate.cancelClicked();
	}

	@UiHandler("save")
	void onSave(ClickEvent event) {
		value = text.getText();
	
		delegate.saveClicked();
	}

	interface Binder extends UiBinder<Widget, AnamnesisCheckTitleEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> {
	}

	@Override
	public void update(AnamnesisCheckTitleProxy anamnesisCheckTitleProxy) {
		String value = anamnesisCheckTitleProxy.getText();
	}

	@Override
	public void setInsideTitleListBox(List<AnamnesisCheckTitleProxy> titleList) {
		for(AnamnesisCheckTitleProxy title:titleList){
			
			if (title != null) {
				previousTitleListBox.addItem(title.getText(), String.valueOf(title.getId()));
			}
		}
		
	}
	
	@Override
	public String getSelectedInsideTitle() {
		int selectedIndex = previousTitleListBox.getSelectedIndex();
		String selectedInsideTitle = previousTitleListBox.getValue(selectedIndex);
		return selectedInsideTitle;
	}


	@Override
	public void setSeletedInsideTitle(String anamnesisCheckTitleId) {
		for (int i = 0; i < previousTitleListBox.getItemCount(); i++) {
			GWT.log("insideTitleListBox.getValue(i) = "+previousTitleListBox.getValue(i));
			if (previousTitleListBox.getValue(i).equals(anamnesisCheckTitleId)) {
				previousTitleListBox.setSelectedIndex(i);
			}
		}
		
	}


}