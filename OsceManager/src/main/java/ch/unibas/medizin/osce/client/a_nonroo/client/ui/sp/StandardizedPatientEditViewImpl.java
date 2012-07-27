

package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.NationalityProxyRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.CalendarUtil;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.BankaccountProxy;
import ch.unibas.medizin.osce.client.managed.request.DescriptionProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.ui.DoctorSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.LangSkillSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.WorkPermission;
//import ch.unibas.medizin.osce.client.shared.Gender;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.google.gwt.user.datepicker.client.DateBox;

public class StandardizedPatientEditViewImpl extends Composite implements StandardizedPatientEditView, Editor<StandardizedPatientProxy> {

	private static final Binder BINDER = GWT.create(Binder.class);

	private static StandardizedPatientEditView instance;
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	TabPanel patientPanel;
//	@UiField
//	Element editTitle;
//	@UiField
//	Element createTitle;
	@UiField
	SpanElement title;

	@UiField(provided = true)
	FocusableValueListBox<Gender> gender = new FocusableValueListBox<Gender>(new EnumRenderer<Gender>());

	@UiField
	TextBox name;
	@UiField
	TextBox preName;
	@UiField
	TextBox street;
	@UiField
	TextBox city;
	@UiField
	IntegerBox postalCode;
	@UiField
	TextBox telephone;
	@UiField
	TextBox telephone2;
	@UiField
	TextBox mobile;
	@UiField
	IntegerBox height;
	@UiField
	IntegerBox weight;
	@UiField
	TextBox email;
	
	Date birthday;

	@Editor.Ignore
	@UiField (provided = true)
	FocusableValueListBox<Integer> day = new FocusableValueListBox<Integer>(new AbstractRenderer<Integer>() {
		@Override
		public String render(Integer day) {
			return (day == null) ? "" : day.toString();
		}
	});


	@Editor.Ignore
	@UiField (provided = true)
	FocusableValueListBox<Integer> month = new FocusableValueListBox<Integer>(new AbstractRenderer<Integer>() {
		@Override
		public String render(Integer date) {
			return (date == null) ? "" : date.toString();
		}
	});
	@Editor.Ignore
	@UiField (provided = true)
	FocusableValueListBox<Integer> year = new FocusableValueListBox<Integer>(new AbstractRenderer<Integer>() {
		@Override
		public String render(Integer date) {
			return (date == null) ? "" : date.toString();
		}
	});
	

	//Issue # 122 : Replace pull down with autocomplete.
	
		@UiField
		public DefaultSuggestBox<NationalityProxy, EventHandlingValueHolderItem<NationalityProxy>> nationality;


		
	/*@UiField(provided = true)
	FocusableValueListBox<NationalityProxy> nationality = new FocusableValueListBox<NationalityProxy>(new NationalityProxyRenderer());
	*/
	//Issue # 122 : Replace pull down with autocomplete.
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	public DefaultSuggestBox<ProfessionProxy, EventHandlingValueHolderItem<ProfessionProxy>> profession;

	/*@UiField(provided = true)
	FocusableValueListBox<ProfessionProxy> profession = new FocusableValueListBox<ProfessionProxy>(ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.ProfessionProxy>());
	*/
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField(provided = true)
	FocusableValueListBox<MaritalStatus> maritalStatus = new FocusableValueListBox<MaritalStatus>(new EnumRenderer<MaritalStatus>());
	@UiField(provided = true)
	FocusableValueListBox<WorkPermission> workPermission = new FocusableValueListBox<WorkPermission>(new EnumRenderer<WorkPermission>());
	@UiField
	TextBox socialInsuranceNo;

	@UiField
	SimplePanel descriptionPanel;
	
	@UiField
	SimplePanel bankEditPanel;

	// Labels (Fieldnames)
	@UiField
	SpanElement labelName;
	@UiField
	SpanElement labelPreName;
	@UiField
	SpanElement labelStreet;
	@UiField
	SpanElement labelPLZCity;
	@UiField
	SpanElement labelTelephone;
	@UiField
	SpanElement labelTelephone2;
	@UiField
	SpanElement labelMobile;
	@UiField
	SpanElement labelEmail;
	@UiField
	SpanElement labelBirthdate;
	@UiField
	SpanElement labelGender;
	@UiField
	SpanElement labelHeight;
	@UiField
	SpanElement labelWeight;
	@UiField
	SpanElement labelNationality;
	@UiField
	SpanElement labelProfession;
	@UiField
	SpanElement labelWorkPermission;
	@UiField
	SpanElement labelMaritalStatus;
	@UiField
	SpanElement labelSocialInsuranceNo;
	@UiField
	IconButton cancel;
	@UiField
	IconButton save;
	@UiField
	DivElement errors;

	private Delegate delegate;
	private Presenter presenter;
	private CalendarUtil cal = new CalendarUtil();

	// Highlight onViolation
	Map<String, Widget> standardizedPatientMap;
	int selectedTab=2;
	// E Highlight onViolation
	
	public StandardizedPatientEditViewImpl() {
		
		initWidget(BINDER.createAndBindUi(this));
		gender.setAcceptableValues(Arrays.asList(Gender.values()));
		
		TabPanelHelper.moveTabBarToBottom(patientPanel);
		
//		DateTimeFormat fmt = DateTimeFormat.getFormat("dd.MM.yyyy");
//		birthday.setFormat(new DateBox.DefaultFormat(fmt));
		
		
		cancel.setText(constants.cancel());
		save.setText(constants.save());
		
		patientPanel.getTabBar().setTabText(0, constants.contactInfo());
		patientPanel.getTabBar().setTabText(1, constants.details());
		patientPanel.getTabBar().setTabText(2, constants.bankAccount());
		patientPanel.getTabBar().setTabText(3, constants.description());
		
		patientPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				// Highlight onViolation
				Log.info("Selected Tab:" + patientPanel.getTabBar().getSelectedTab());
				selectedTab=patientPanel.getTabBar().getSelectedTab();
				// E Highlight onViolation
				if (delegate != null)
					delegate.storeDisplaySettings();
			}
		});
		
		setTabTexts();
		setLabelTexts();
		
//		createKeyHandlers();
//		createFocusHandlers();
		
		patientPanel.selectTab(0);
		preName.setFocus(true);
		preName.selectAll();
		
		month.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				cal.setMonth(month.getValue());
				if (day.getValue() > cal.getDaysInMonth()) {
					day.setValue(cal.getDaysInMonth());
				}
				day.setAcceptableValues(getIntegerList(1, cal.getDaysInMonth()));
			}
		});
		
		year.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				cal.setYear(year.getValue());
				if (day.getValue() > cal.getDaysInMonth()) {
					day.setValue(cal.getDaysInMonth());
				}
				day.setAcceptableValues(getIntegerList(1, cal.getDaysInMonth()));
			}
		});
		
		standardizedPatientMap=new HashMap<String, Widget>();
		
		standardizedPatientMap.put("name",name);
		standardizedPatientMap.put("preName",preName);
		standardizedPatientMap.put("street",street);
		standardizedPatientMap.put("postalCode",postalCode);
		standardizedPatientMap.put("city",city);		
		standardizedPatientMap.put("telephone",telephone);
		standardizedPatientMap.put("mobile",mobile);
		standardizedPatientMap.put("email",email);
		standardizedPatientMap.put("telephone2",telephone2);
		standardizedPatientMap.put("birthday",day);
		standardizedPatientMap.put("birthday",month);
		standardizedPatientMap.put("birthday",year);
		standardizedPatientMap.put("gender",gender);
		standardizedPatientMap.put("height",height);
		standardizedPatientMap.put("weight",weight);
		standardizedPatientMap.put("profession",profession);
		standardizedPatientMap.put("nationality",nationality);
		standardizedPatientMap.put("maritalStatus",maritalStatus);
		standardizedPatientMap.put("workPermission",workPermission);
		standardizedPatientMap.put("socialInsuranceNo",socialInsuranceNo);
		
	}
	
	private List<Integer> getIntegerList(int minValue, int maxValue) {
		List<Integer> values = new ArrayList<Integer>();
		for (int i = minValue; i <= maxValue; i++) {
			values.add(new Integer(i));
		}
		return values;
	}
	
	private void createFocusHandlers() {
		preName.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				preName.selectAll();
			}
		});
		name.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				name.selectAll();
			}
		});
		street.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				street.selectAll();
			}
		});
		postalCode.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				postalCode.selectAll();
			}
		});
		city.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				city.selectAll();
			}
		});
		telephone.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				telephone.selectAll();
			}
		});
		telephone2.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				telephone2.selectAll();
			}
		});
		mobile.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				mobile.selectAll();
			}
		});
		email.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				email.selectAll();
			}
		});
		telephone2.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				telephone2.selectAll();
			}
		});
		
		// what about birthday?
		
		height.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				height.selectAll();
			}
		});
		weight.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				weight.selectAll();
			}
		});
	}
	
	private void createKeyHandlers() {		
		preName.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					name.setFocus(true);
					if (event.isControlKeyDown()) {
						delegate.saveClicked();
					}
				} 
			}
		});
		name.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					street.setFocus(true);
			}
		});
		street.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					postalCode.setFocus(true);
			}
		});
		postalCode.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					city.setFocus(true);
			}
		});
		city.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					telephone.setFocus(true);
			}
		});
		telephone.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					mobile.setFocus(true);
			}
		});
		mobile.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					email.setFocus(true);
			}	
		});
		email.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					telephone2.setFocus(true);
			}
		});
		telephone2.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_TAB)
					patientPanel.selectTab(1);
			}
		});
//		birthday.addHandler(new KeyUpHandler() {
//			public void onKeyUp(KeyUpEvent event) {
//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//					gender.setFocus(true);
//				}
//			}
//		}, KeyUpEvent.getType());
	}

	private void setTabTexts() {
		patientPanel.getTabBar().setTabText(0, constants.contactInfo());
		patientPanel.getTabBar().setTabText(1, constants.details());
		patientPanel.getTabBar().setTabText(2, constants.bankAccount());
		patientPanel.getTabBar().setTabText(3, constants.description());
	}
	
	private void setLabelTexts() {
		labelName.setInnerText(constants.name() + ":");
		labelPreName.setInnerText(constants.preName() + ":");
		labelPLZCity.setInnerText(constants.plzCity() + ":");
		labelEmail.setInnerText(constants.email() + ":");
		labelMobile.setInnerText(constants.mobile() + ":");
		labelStreet.setInnerText(constants.street() + ":");
		labelTelephone.setInnerText(constants.telephone() + ":");
		labelTelephone2.setInnerText(constants.telephone() + " 2:");
		
		labelBirthdate.setInnerText(constants.birthday() + ":");
		labelGender.setInnerText(constants.gender() + ":");
		labelHeight.setInnerText(constants.height() + ":");
		labelWeight.setInnerText(constants.weight() + ":");
		labelNationality.setInnerText(constants.nationality() + ":");
		labelProfession.setInnerText(constants.profession() + ":");
		labelSocialInsuranceNo.setInnerText(constants.socialInsuranceNo() + ":");
		labelWorkPermission.setInnerText(constants.workPermission() + ":");
		labelMaritalStatus.setInnerText(constants.maritalStatus() + ":");
	}

	@Override
	public RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}

	public void setCreating(boolean creating) {
		if (creating) {
			title.setInnerText(constants.addPatient());
		} else {
			title.setInnerText(constants.editPatient());
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

	interface Binder extends UiBinder<Widget, StandardizedPatientEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<StandardizedPatientProxy, StandardizedPatientEditViewImpl> {
	}

	@Override
	public void setEditTitle(boolean edit) {
		if (edit) {
			title.setInnerText(constants.editPatient());
		} else {
			title.setInnerText(constants.addPatient());
		}
	}

	@Override
	public void setPresenter(Presenter standardizedPatientEditActivity) {
		this.presenter=standardizedPatientEditActivity;
	}


	@Override
	public SimplePanel getDescriptionPanel() {
		return descriptionPanel;
	}
	
	@Override
	public SimplePanel getBankEditPanel() {
		return bankEditPanel;
	}


	@Override
	public void setNationalityPickerValues(Collection<NationalityProxy> values) {
//		if (values!= null) {
//			nationality.setValue(values.iterator().next());
//		}
		
		//Issue # 122 : Replace pull down with autocomplete.
		
				DefaultSuggestOracle<NationalityProxy> suggestOracle1 = (DefaultSuggestOracle<NationalityProxy>) nationality.getSuggestOracle();
				suggestOracle1.setPossiblilities((List)values);
				nationality.setSuggestOracle(suggestOracle1);
				//nationality.setRenderer(new NationalityProxyRenderer());
				nationality.setRenderer(new AbstractRenderer<NationalityProxy>() {

					@Override
					public String render(NationalityProxy object) {
						// TODO Auto-generated method stub
						if(object!=null)
						{
						return object.getNationality();
						}
						else
						{
							return "";
						}
					}
				});
				//nationality.setAcceptableValues(values);
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		
	}


	@Override
	public void setProfessionPickerValues(Collection<ProfessionProxy> values) {
//		if (values != null) {
//			profession.setValue(values.iterator().next());
//		}
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		DefaultSuggestOracle<ProfessionProxy> suggestOracle1 = (DefaultSuggestOracle<ProfessionProxy>) profession.getSuggestOracle();
		suggestOracle1.setPossiblilities((List)values);
		profession.setSuggestOracle(suggestOracle1);
		//profession.setRenderer(new ProfessionProxyRenderer());
		profession.setRenderer(new AbstractRenderer<ProfessionProxy>() {

			@Override
			public String render(ProfessionProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getProfession();
				}
				else
				{
					return "";
				}
			}
		});

		//profession.setAcceptableValues(values);		
		
		//Issue # 122 : Replace pull down with autocomplete.
	}
//	@Override
//	public String getPatientId() {
//		return patientId.getValue();
//	}
//	@Override
//	public void setPatientId(String patientId) {
//		this.patientId.setValue(patientId);
//		
//	}
	
	@Override
	public Integer getDay() {
		return day.getValue();
	}
	
	@Override
	public Integer getMonth() {
		return month.getValue();
	}
	
	@Override
	public Integer getYear() {
		return year.getValue();
	}
	
	@Override
	public void setAcceptableDays(List<Integer> days) {
		day.setAcceptableValues(days);
	}
	
	@Override
	public void setAcceptableMonths(List<Integer> months) {
		month.setAcceptableValues(months);
	}
	
	@Override
	public void setAcceptableYears(List<Integer> years) {
		year.setAcceptableValues(years);
	}
	
	@Override
	public void setDay(int day) {
		this.day.setValue(day);
	}
	
	@Override
	public void setMonth(int month) {
		this.month.setValue(month);
	}
	
	@Override
	public void setYear(int year) {
		this.year.setValue(year);
	}

	@Override
	public void setWorkPermissionPickerValues(List<WorkPermission> values) {
		workPermission.setAcceptableValues(values);
	}

	@Override
	public void setMaritalStatusPickerValues(List<MaritalStatus> values) {
		maritalStatus.setAcceptableValues(values);
	}

	@Override
	public int getSelectedDetailsTab() {
		return patientPanel.getTabBar().getSelectedTab();
	}

	@Override
	public void setSelectedDetailsTab(int detailsTab) {
		patientPanel.getTabBar().selectTab(detailsTab);
	}

	// Highlight onViolation

	@Override
	public Map getStandardizedPatientMap() {

		return this.standardizedPatientMap;
	}
	
	@Override
	public int getSelectedTab() {

		return this.selectedTab;
	}
	
	// E Highlight onViolation
}
