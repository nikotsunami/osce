package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.ui.DoctorSetEditor;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ClinicEditViewImpl extends Composite implements ClinicEditView, Editor<ClinicProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private Delegate delegate;
	private Presenter presenter;
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	TabPanel clinicPanel;
	
	@UiField
	SpanElement header;

	@UiField
	TextBox name;
	@UiField
	TextBox street;
	@UiField
	TextBox city;
	@UiField
	IntegerBox postalCode;
	@UiField
	DoctorSetEditor doctors;

	@UiField
	IconButton cancel;
	@UiField
	IconButton save;

	@UiField
	DivElement errors;
	
	@UiField
	SpanElement labelName;
	@UiField
	SpanElement labelStreet;
	@UiField
	SpanElement labelCity;

	// Highlight onViolation
		Map<String, Widget> clinicMap;		
		ClinicEditView clinicView;
	// E Highlight onViolation
	
	public ClinicEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
		clinicPanel.selectTab(0);

		clinicPanel.getTabBar().setTabText(0, constants.generalInformation());
		clinicPanel.getTabBar().setTabText(1, constants.doctors());

		TabPanelHelper.moveTabBarToBottom(clinicPanel);

		cancel.setText(constants.cancel());
		save.setText(constants.save());

		labelName.setInnerText(constants.name() + ":");
		labelStreet.setInnerText(constants.street() + ":");
		labelCity.setInnerText(constants.plz() + ", " + constants.city() + ":");
		clinicPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (delegate != null) {
					delegate.storeDisplaySettings();
				}
			}
		});
		
		// Highlight onViolation
		clinicView=this;
		
		clinicMap=new HashMap<String, Widget>();
		clinicMap.put("name", name);
		clinicMap.put("street", street);
		clinicMap.put("city", city);
		clinicMap.put("postalCode", postalCode);
		clinicMap.put("doctors", doctors);
		// E Highlight onViolation
	}

	@Override
	public RequestFactoryEditorDriver<ClinicProxy, ClinicEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<ClinicProxy, ClinicEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}

	public void setCreating(boolean creating) {
		if (creating) {
			header.setInnerText(constants.createClinic());
		} else {
			header.setInnerText(constants.editClinic());
		}
	}

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
		delegate.saveClicked();
	}

	interface Binder extends UiBinder<Widget, ClinicEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<ClinicProxy, ClinicEditViewImpl> {
	}

	@Override
	public void setEditTitle(boolean edit) {
		if (edit) {
			header.setInnerText(constants.editClinic());
		} else {
			header.setInnerText(constants.createClinic());
		}

	}

	@Override
	public void setPresenter(Presenter doctorEditActivity) {
		this.presenter = doctorEditActivity;
	}

	@Override
	public void setDoctorsPickerValues(Collection<DoctorProxy> values) {
		doctors.setAcceptableValues(values);
	}

	@Override
	public void setSelectedDetailsTab(int detailsTab) {
		clinicPanel.selectTab(detailsTab);
	}

	@Override
	public int getSelectedDetailsTab() {
		return clinicPanel.getTabBar().getSelectedTab();
	}

	// Highlight onViolation

				@Override
				public ClinicEditView getClinicView() {
					// TODO Auto-generated method stub
					return this.clinicView;
				}

				@Override
				public Map getClinicMap() {
					// TODO Auto-generated method stub
					return this.clinicMap;
				}
				
	// E Highlight onViolation				
}
