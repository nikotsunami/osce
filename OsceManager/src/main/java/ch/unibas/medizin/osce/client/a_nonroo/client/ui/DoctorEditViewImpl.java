

package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class DoctorEditViewImpl extends Composite implements DoctorEditView, Editor<DoctorProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	TabPanel doctorPanel;

	@UiField
	Button cancel;
	@UiField
	Button save;
	@UiField
	DivElement errors;

	@UiField
	SpanElement header;
	
	@UiField(provided = true)
    public ValueListBox<Gender> gender = new ValueListBox<Gender>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Gender>() {
    	EnumRenderer<Gender> renderer = new EnumRenderer<Gender>();
        public String render(ch.unibas.medizin.osce.shared.Gender obj) {
            return obj == null ? "" : renderer.render(obj);
        }
    }); 
	
	@UiField 
	public TextBox title;
	@UiField
	public TextBox name;
	@UiField
	public TextBox preName;
	@UiField
	public TextBox email;
	@UiField
	public TextBox telephone;
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	/*@UiField(provided = true)
	ValueListBox<ClinicProxy> clinic = new ValueListBox<ClinicProxy>(ch.unibas.medizin.osce.client.managed.ui.ClinicProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.ClinicProxy>());
	*/
	@UiField
	public DefaultSuggestBox<ClinicProxy, EventHandlingValueHolderItem<ClinicProxy>> clinic;

	@UiField
	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> specialisation;

	//Issue # 122 : Replace pull down with autocomplete.
	
	
	/*@UiField
	SimplePanel officePanel;
	*/
	@UiField
	public OfficeEditViewImpl officeEditViewImpl;
	
	@UiField
    SpanElement labelGender;
    @UiField
    SpanElement labelTitle;
    @UiField
    SpanElement labelName;
    @UiField
    SpanElement labelPreName;
    @UiField
    SpanElement labelEmail;
    @UiField
    SpanElement labelTelephone;
    @UiField
    SpanElement labelClinic;
	
    @UiField
    SpanElement labelSpecialisation;
    
	private Delegate delegate;

	private Presenter presenter;

	// Highlight onViolation
		Map<String, Widget> doctorMap;	
		public int selTab=0;
	// E Highlight onViolation
	
	
	public DoctorEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		gender.setValue(Gender.values()[0]);
		gender.setAcceptableValues(Arrays.asList(Gender.values()));
		
		doctorPanel.selectTab(0);

		doctorPanel.getTabBar().setTabText(0, constants.generalInformation());
		doctorPanel.getTabBar().setTabText(1, constants.officeDetails());
		
		TabPanelHelper.moveTabBarToBottom(doctorPanel);
		
		save.setText(constants.save());
		cancel.setText(constants.cancel());
		
		labelGender.setInnerText(constants.gender() + ":");
		labelTitle.setInnerText(constants.title() + ":");
		labelName.setInnerText(constants.name() + ":");
		labelPreName.setInnerText(constants.preName() + ":");
		labelEmail.setInnerText(constants.email() + ":");
		labelTelephone.setInnerText(constants.telephone() + ":");
		labelClinic.setInnerText(constants.clinic() + ":");
		labelSpecialisation.setInnerText(constants.specification() + ":");
		
		doctorPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				// Highlight onViolation
				Log.info("Change Tab" + doctorPanel.getTabBar().getSelectedTab());
				selTab=doctorPanel.getTabBar().getSelectedTab();
				// E Highlight onViolation
				if (delegate != null)
					delegate.storeDisplaySettings();
			}
		});
		
		// Highlight onViolation
						
		doctorMap=new HashMap<String, Widget>();
		doctorMap.put("title",title );
		doctorMap.put("name", name);
		doctorMap.put("preName", preName);
		doctorMap.put("gender",gender );
		doctorMap.put("email", email);
		doctorMap.put("telephone", telephone);
		doctorMap.put("clinic", clinic);
		doctorMap.put("specialisation", specialisation);
		
				
		
		
		
		// E Highlight onViolation
	}


	/*@Override
	public RequestFactoryEditorDriver<DoctorProxy, DoctorEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<DoctorProxy, DoctorEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}*/

	public void setCreating(boolean creating) {
		if (creating) {
			header.setInnerText(constants.createDoctor());
		} else {
			header.setInnerText(constants.editDoctor());
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

	interface Binder extends UiBinder<Widget, DoctorEditViewImpl> {
	}

	/*interface Driver extends RequestFactoryEditorDriver<DoctorProxy, DoctorEditViewImpl> {
	}
*/
	@Override
	public void setEditTitle(boolean edit) {
		
		if (edit) {
			header.setInnerText(constants.editDoctor());
        } else {
			header.setInnerText(constants.createDoctor());
        }

	}

	@Override
	public void setPresenter(Presenter doctorEditActivity) {
		this.presenter=doctorEditActivity;
	}

	@Override
	public void setClinicPickerValues(Collection<ClinicProxy> clinicList) {
		
		//Issue # 122 : Replace pull down with autocomplete.
		DefaultSuggestOracle<ClinicProxy> suggestOracle1 = (DefaultSuggestOracle<ClinicProxy>) clinic.getSuggestOracle();
		suggestOracle1.setPossiblilities((List)clinicList);
		clinic.setSuggestOracle(suggestOracle1);
		//clinic.setRenderer(new ClinicProxyRenderer());
		clinic.setRenderer(new AbstractRenderer<ClinicProxy>() {

			@Override
			public String render(ClinicProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getName();
				}
				else
				{
					return "";
				}
			}
		});
		//clinic.setAcceptableValues(clinicList);
		//Issue # 122 : Replace pull down with autocomplete.
	}
	
	@Override
	public void setSpecialisationPickerValues(Collection<SpecialisationProxy> specialisationList) {
		
		//Issue # 122 : Replace pull down with autocomplete.
		DefaultSuggestOracle<SpecialisationProxy> suggestOracle1 = (DefaultSuggestOracle<SpecialisationProxy>) specialisation.getSuggestOracle();
		suggestOracle1.setPossiblilities((List)specialisationList);
		specialisation.setSuggestOracle(suggestOracle1);
		//clinic.setRenderer(new ClinicProxyRenderer());
		specialisation.setRenderer(new AbstractRenderer<SpecialisationProxy>() {

			@Override
			public String render(SpecialisationProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getName();
				}
				else
				{
					return "";
				}
			}
		});
		//clinic.setAcceptableValues(clinicList);
		//Issue # 122 : Replace pull down with autocomplete.
	}
	
	@Override
	public SimplePanel getOfficePanel(){
		//return officePanel;
		return null;
		
	}


	@Override
	public void setSelectedDetailsTab(int detailsTab) {
		doctorPanel.selectTab(detailsTab);
	}


	@Override
	public int getSelectedDetailsTab() {
		return doctorPanel.getTabBar().getSelectedTab();
	}

	// Highlight onViolation
	@Override
	public Map getDoctorMap() {
		
		return this.doctorMap;
	}
	@Override
	public int getSelectedTab() {
		
		return this.selTab;
	}


	/*@Override
	public RequestFactoryEditorDriver<DoctorProxy, DoctorEditViewImpl> createEditorDriver() {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	@Override
	public void setValueForEdit(DoctorProxy proxy)
	{
		title.setValue(proxy.getTitle());
		name.setValue(proxy.getName());
		preName.setValue(proxy.getPreName());
		gender.setValue(proxy.getGender());
		email.setValue(proxy.getEmail());
		telephone.setValue(proxy.getTelephone());
		clinic.setSelected(proxy.getClinic());
		specialisation.setSelected(proxy.getSpecialisation());
		
		officeEditViewImpl.title.setValue(proxy.getOffice().getTitle());
		officeEditViewImpl.name.setValue(proxy.getOffice().getName());
		officeEditViewImpl.preName.setValue(proxy.getOffice().getPreName());
		officeEditViewImpl.gender.setValue(proxy.getOffice().getGender());
		officeEditViewImpl.email.setValue(proxy.getOffice().getEmail());
		officeEditViewImpl.telephone.setValue(proxy.getOffice().getTelephone());
		
	}
	
	@Override
	public DoctorEditViewImpl getDoctorEditViewImpl()
	{
		return this;
	}
	// E Highlight onViolation
}
