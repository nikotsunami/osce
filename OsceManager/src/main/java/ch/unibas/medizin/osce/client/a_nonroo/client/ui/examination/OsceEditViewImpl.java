package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Textbox;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSettingsProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.scaffold.ui.ShortBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.EncryptionType;
import ch.unibas.medizin.osce.shared.OsceCreationType;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.TimeUnit;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class OsceEditViewImpl extends Composite implements OsceEditView {
	private static final Binder BINDER = GWT.create(Binder.class);

	/*@UiField
	Element editTitle;*/

// change{
	/*@UiField
	Element createTitle;*/
	// change}
	@UiField
	TabPanel oscePanel;
	
	@UiField(provided = true)
	public ValueListBox<StudyYears> studyYear = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
	
	@UiField(provided = true)
	public ValueListBox<OsceCreationType> osceCreationType = new ValueListBox<OsceCreationType>(new AbstractRenderer<OsceCreationType>() {

		@Override
		public String render(OsceCreationType object) {
			if (object != null)
				return object.toString();
			else
				return "";
		}
	});
	
	@UiField
	public TextBox name;

	
	@UiField
	public IntegerBox maxNumberStudents;

	@UiField
	public ShortBox shortBreak;
	
	/*@UiField
	IntegerBox postLength;
	*/

	@UiField
	public IntegerBox numberCourses;

	@UiField
	public ShortBox LongBreak;
	
	@UiField
	public ShortBox lunchBreak;
	
	@UiField
	public ShortBox lunchBreakRequiredTime;
	
	@UiField
	public ShortBox longBreakRequiredTime;
	
	@UiField
	public ShortBox middleBreak;
	
	// change {
	
	
	//remove number post
	/*@UiField
	public IntegerBox numberPosts;
	*/
	//remove number post
	
	@UiField
	public IntegerBox numberRooms;
	
	

	@UiField
	public ShortBox shortBreakSimpatChange;
	
	
	// change }
	/*@UiField
	TaskSetEditor tasks;
	*/
	
	@UiField
	public IntegerBox postLength;

	/*@UiField(provided = true)
	public CheckBox isRepeOsce = new CheckBox() {

		public void setValue(Boolean value) {
			super.setValue(value == null ? Boolean.FALSE : value);
		}
	};*/
	
	@UiField(provided = true)
	public CheckBox spStayInPost = new CheckBox() {

		public void setValue(Boolean value) {
			super.setValue(value == null ? Boolean.FALSE : value);
		}
	};
	
	@UiField
	DivElement labelRemark;
	@UiField
	DivElement labelMaxParcours;
	@UiField
	DivElement labelMaxStudents;
	@UiField
	DivElement labelStudyYear;
	@UiField
	DivElement labelMaxRooms;
	/*@UiField
	DivElement labelIsRepe;*/
	@UiField
	DivElement labelOsceRepe;
	@UiField
	DivElement labelShortBreak;
	@UiField
	DivElement labelLongBreak;
	@UiField
	DivElement labelLunchBreak;
	
	@UiField
	DivElement labelLunchBreakRequiredTime;
	@UiField
	DivElement labelLOngBreakRequiredTime;
	
	@UiField
	DivElement labelStationLength;
	@UiField
	DivElement labelMiddleBreak;
	@UiField
	public DivElement labelOsceForTask;
	
	@UiField
	DivElement labeShortBreakSimpatChange;

	@UiField
	DivElement labelSpStayInPost;
	
	@UiField
	public DivElement labelOsceCreationType;
	
	//remove number post
//	@UiField
//	DivElement numberPost;
//	
	//remove number post
	/*
	@UiField
	IntegerBox numberRooms;

	@UiField(provided = true)
	CheckBox isValid = new CheckBox() {

		public void setValue(Boolean value) {
			super.setValue(value == null ? Boolean.FALSE : value);
		}
	};*/

/*	@UiField(provided = true)
	public ValueListBox<SemesterProxy> semester = new ValueListBox<SemesterProxy>(ch.unibas.medizin.osce.client.managed.ui.SemesterProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.SemesterProxy>());
*/
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	//@Ignore
	public DefaultSuggestBox<OsceProxy, EventHandlingValueHolderItem<OsceProxy>> osceValue;

	
	
	/*@UiField(provided = true)
	@Ignore
	public ValueListBox<OsceProxy> osceValue = new ValueListBox<OsceProxy>(ch.unibas.medizin.osce.client.managed.ui.OsceProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.OsceProxy>());
*/
	//Issue # 122 : Replace pull down with autocomplete.

	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	public DefaultSuggestBox<OsceProxy, EventHandlingValueHolderItem<OsceProxy>> copiedOsce;

	/*
	@UiField(provided = true)
	public ValueListBox<OsceProxy> copiedOsce = new ValueListBox<OsceProxy>(ch.unibas.medizin.osce.client.managed.ui.OsceProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.OsceProxy>());
*/
	//Issue # 122 : Replace pull down with autocomplete.
	/*
	@UiField
	OsceDaySetEditor osce_days;

	@UiField
	CourseSetEditor courses;

	@UiField
	TaskSetEditor tasks;

	@UiField
	StudentOscesSetEditor osceStudents;
*/

	//   @UiField
	//   ResponsibilitySetEditor responsibilities;

	@UiField
	IconButton cancel;

	@UiField
	SpanElement title;

	
	@UiField
	 IconButton save;

	@UiField
	DivElement errors;
	
	@UiField
	SpanElement labelTitleGeneral;

	@UiField
	SpanElement labelTitleAttributes;

	@UiField
	SpanElement labelTitleBreaks;

	
	private Delegate delegate;

	private Presenter presenter;
	
	@UiField
	IconButton preview;
	
	@UiField
	HTMLPanel canvasPanel;
	
	@UiField
	HorizontalPanel horizontalTabPanel;
	
	// Settings element starts
	@UiField
	DivElement bucketInfoLabel;
	
	@UiField
	SpanElement labelKeySettings;
	
	@UiField
	SpanElement labelGeneralSettings;
	
	@UiField(provided = true)
	public ValueListBox<BucketInfoType> bucketInfo=new ValueListBox<BucketInfoType>(new AbstractRenderer<BucketInfoType>() {

		@Override
		public String render(BucketInfoType object) {
			if (object != null)
				return object.toString();
			else
				return "";
		}
	});
	
	@UiField
	DivElement lblHost;
	
	@UiField
	public TextBox host;
	
	@UiField
	DivElement lblUserName;

	@UiField
	public TextBox userName;
	
	@UiField
	DivElement lblPassword;
	
	@UiField
	public TextBox password;
	
	@UiField
	DivElement lblBucketName;
	
	@UiField
	public TextBox bucketName;
	
	@UiField
	DivElement lblSettingPassword;
	
	@UiField
	public TextBox settingPassword;
	
	@UiField
	DivElement lblBackUpPeriod;
	
	@UiField
	public IntegerBox backUpPeriod;
	
	@UiField
	DivElement lblTimeUnit;
	
	@UiField
	DivElement lblPointNxtExaminee;
	
	@UiField
	public CheckBox pointNextExaminee;
	
	@UiField
	DivElement lblSymmetricKey;
	
	@UiField
	public TextBox symmetricKey;
	
	
	@UiField
	DivElement lblExamReviewMode;
	
	@UiField
	public CheckBox  examReviewMode;
	
	@UiField
	public DivElement lblScreenSaverText;
	
	@UiField 
	public TextArea screenSaverText;
	
	
	@UiField(provided=true)
    public ValueListBox<TimeUnit> timeUnit = new ValueListBox<TimeUnit>(new EnumRenderer<TimeUnit>());
	
	@UiField
	DivElement lblEncryptionType;
	
	@UiField(provided = true)
    public ValueListBox<EncryptionType> encryptionType = new ValueListBox<EncryptionType>(new EnumRenderer<EncryptionType>());
	
	private OsceProxy osce;
	private OsceSettingsProxy osceSettingsProxy;
	
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	// Highlight onViolation
		Map<String, Widget> osceMap;
		
	// E Highlight onViolation

		
	public OsceEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
		oscePanel.selectTab(0);
		oscePanel.getTabBar().setTabText(0, constants.manageOsces());
		TabPanelHelper.moveTabBarToBottom(oscePanel);
		oscePanel.getTabBar().setTabText(1, constants.osceSettings());
		cancel.setText(constants.cancel());
		save.setText(constants.save());
		preview.setText(constants.preview());
		//copiedOsce.setEnabled(false);
		
		labelTitleGeneral.setInnerText(constants.general());
		labelTitleAttributes.setInnerText(constants.attributes());
		labelTitleBreaks.setInnerText(constants.breaks());
		
		labelRemark.setInnerText(constants.remark());
		labelMaxParcours.setInnerText(constants.osceMaxCircuits());
		labelMaxStudents.setInnerText(constants.osceMaxStudents());
		labelStudyYear.setInnerText(constants.studyYears());
		labelMaxRooms.setInnerText(constants.osceMaxRooms());
		//labelIsRepe.setInnerText(constants.osceIsRepe());
		labelOsceRepe.setInnerText(constants.osceRepe());
		labelShortBreak.setInnerText(constants.osceShortBreak());
		labelLongBreak.setInnerText(constants.osceLongBreak());
		labelLunchBreak.setInnerText(constants.osceLunchBreak());
		labelStationLength.setInnerText(constants.osceStationLength());
		labelMiddleBreak.setInnerText(constants.osceMediumBreak());
		labelOsceForTask.setInnerText(constants.osceForTask());
		labeShortBreakSimpatChange.setInnerText(constants.osceSimpatsInShortBreak());
		labelSpStayInPost.setInnerText(constants.osceSpStayInPost());
		labelLunchBreakRequiredTime.setInnerText(constants.osceLunchBreakRequiredFiled());
		labelLOngBreakRequiredTime.setInnerText(constants.osceLongBreakRequiredFiled());
		labelOsceCreationType.setInnerText(constants.osceCreationType());
		
		bucketInfoLabel.setInnerText(constants.bucketInformation());
		lblSettingPassword.setInnerText(constants.settingPassword());
		lblBackUpPeriod.setInnerText(constants.backUpPeriod());
		lblTimeUnit.setInnerText(constants.timeUnit());
		lblEncryptionType.setInnerText(constants.encryptionType());
		lblPointNxtExaminee.setInnerText(constants.pointNxtExaminee());
		lblExamReviewMode.setInnerText(constants.examReviewMode());
		lblSymmetricKey.setInnerText(constants.symmetricKey());
		labelKeySettings.setInnerText(constants.bucketParameters());
		lblUserName.setInnerText(constants.accessKey());
		lblPassword.setInnerText(constants.secretKey());
		lblBucketName.setInnerText(constants.bucketName());
		lblScreenSaverText.setInnerText(constants.osceScreenSaverText());
		labelGeneralSettings.setInnerText(constants.generalInformation());
		screenSaverText.setWidth("95%");
		timeUnit.setWidth("90%");
		bucketInfo.setWidth("90%");
		encryptionType.setWidth("90%");
		//remove number post
		//numberPost.setInnerText(constants.circuitStation());
		
		//remove number post
		// Highlight onViolation
		osceMap=new HashMap<String, Widget>();
		osceMap.put("name", name);
		osceMap.put("shortBreak", shortBreak);
		osceMap.put("numberCourses", numberCourses);
		osceMap.put("LongBreak", LongBreak);
		osceMap.put("maxNumberStudents", maxNumberStudents);
		osceMap.put("lunchBreak", lunchBreak);
		osceMap.put("studyYear", studyYear);
		osceMap.put("postLength", postLength);
		//osceMap.put("isRepeOsce", isRepeOsce);
		//Issue # 122 : Replace pull down with autocomplete.
		//osceMap.put("osceValue", osceValue);
		osceMap.put("osceValue", osceValue.getTextField().advancedTextBox);
		//Issue # 122 : Replace pull down with autocomplete.
		//Issue # 122 : Replace pull down with autocomplete.
		//osceMap.put("copiedOsce", copiedOsce);
		osceMap.put("copiedOsce", copiedOsce.getTextField().advancedTextBox);
		//Issue # 122 : Replace pull down with autocomplete.
		osceMap.put("middleBreak", middleBreak);
		osceMap.put("shortBreak", shortBreakSimpatChange);
		osceMap.put("lunchBreakRequiredTime", lunchBreakRequiredTime);
		osceMap.put("longBreakRequiredTime", longBreakRequiredTime);
		
		osceCreationType.setValue(OsceCreationType.Automatic);
		osceCreationType.setAcceptableValues(Arrays.asList(OsceCreationType.values()));
		
		bucketInfo.setValue(BucketInfoType.S3);
		bucketInfo.setAcceptableValues(Arrays.asList(BucketInfoType.values()));
		
		timeUnit.setValue(TimeUnit.SECOND);
		timeUnit.setAcceptableValues(Arrays.asList(TimeUnit.values()));
		
		encryptionType.setValue(EncryptionType.ASYM);
		encryptionType.setAcceptableValues(Arrays.asList(EncryptionType.values()));
		host.getElement().getStyle().setDisplay(Display.NONE);
		
		osceCreationType.addValueChangeHandler(new ValueChangeHandler<OsceCreationType>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<OsceCreationType> event) {
				if (OsceCreationType.Manual.equals(event.getValue()))
				{
					Document.get().getElementById("numberCoursesLbl").getStyle().setDisplay(Display.NONE);
					Document.get().getElementById("numberCourses").getStyle().setDisplay(Display.NONE);
					Document.get().getElementById("maxNumberStudentsLbl").getStyle().setDisplay(Display.NONE);
					Document.get().getElementById("maxNumberStudents").getStyle().setDisplay(Display.NONE);
					Document.get().getElementById("maxNoRoomLbl").getStyle().setDisplay(Display.NONE);
					Document.get().getElementById("maxNoRoom").getStyle().setDisplay(Display.NONE);
				}
				else if (OsceCreationType.Automatic.equals(event.getValue()))
				{
					Document.get().getElementById("numberCoursesLbl").getStyle().clearDisplay();
					Document.get().getElementById("numberCourses").getStyle().clearDisplay();
					Document.get().getElementById("maxNumberStudentsLbl").getStyle().clearDisplay();
					Document.get().getElementById("maxNumberStudents").getStyle().clearDisplay();
					Document.get().getElementById("maxNoRoomLbl").getStyle().clearDisplay();
					Document.get().getElementById("maxNoRoom").getStyle().clearDisplay();
				}
			}
		});
		
		bucketInfo.addValueChangeHandler(new ValueChangeHandler<BucketInfoType>() {

			@Override
			public void onValueChange(ValueChangeEvent<BucketInfoType> event) {
				userName.setValue("");
				password.setValue("");
				host.setValue("");
				bucketName.setValue("");
				
				if(event.getValue().equals(BucketInfoType.S3)){
					Document.get().getElementById("hostTd").getStyle().setDisplay(Display.NONE);
					Document.get().getElementById("lblHostTd").getStyle().setDisplay(Display.NONE);
					lblUserName.setInnerText(constants.accessKey());
					lblPassword.setInnerText(constants.secretKey());
					lblBucketName.setInnerText(constants.bucketName());
					setS3Values();
				}
				else if(event.getValue().equals(BucketInfoType.FTP)){
					Document.get().getElementById("hostTd").getStyle().clearDisplay();
					Document.get().getElementById("lblHostTd").getStyle().clearDisplay();
					host.getElement().getStyle().clearDisplay();
					lblUserName.setInnerText(constants.userName());
					lblPassword.setInnerText(constants.password());
					lblBucketName.setInnerText(constants.basePath());
					lblHost.setInnerText(constants.host());
					setSFTPValues();
				}
			}
		
		});
		
		// E Highlight onViolation
		
		/*if(isRepeOsce.isChecked()==true)
		{
			copiedOsce.setEnabled(true);
		}
		
		isRepeOsce.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()==true)
				{
					copiedOsce.setEnabled(true);
				}
				else
				{
					copiedOsce.setEnabled(false);
				}
			}
		});		*/
		
		horizontalTabPanel.getElement().getStyle().setMarginTop(5, Unit.PX);
		//horizontalTabPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}

	/*@Override
	public RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}*/
	
	private void setS3Values() {
		
		if (osceSettingsProxy == null)
		{
				lblUserName.setInnerText(constants.accessKey());
				lblPassword.setInnerText(constants.secretKey());
				lblBucketName.setInnerText(constants.bucketName());
				userName.setValue("");
				password.setValue("");
				bucketName.setValue("");
		}
		else if (osceSettingsProxy.getInfotype().equals(BucketInfoType.S3)) {
		
				userName.setValue(osceSettingsProxy.getUsername());
				password.setValue(osceSettingsProxy.getPassword());
				bucketName.setValue(osceSettingsProxy.getBucketName());	
		} 
	}
	
	private void setSFTPValues(){
		
		if (osceSettingsProxy == null) {
				userName.setValue("");
				password.setValue("");
				bucketName.setValue("");	
				host.setValue("");
		}
		
		else if (osceSettingsProxy.getInfotype().equals(BucketInfoType.FTP)) {
				userName.setValue(osceSettingsProxy.getUsername());
				password.setValue(osceSettingsProxy.getPassword());
				bucketName.setValue(osceSettingsProxy.getBucketName());	
				host.setValue(osceSettingsProxy.getHost());
				lblUserName.setInnerText(constants.userName());
				lblPassword.setInnerText(constants.password());
				lblBucketName.setInnerText(constants.basePath());
				lblHost.setInnerText(constants.host());
		}	
	}
	
	public void setCreating(boolean creating) {
		if (creating) {
			title.setInnerText(constants.addOsce());
			//editTitle.getStyle().setDisplay(Display.NONE);
			// change{	//	createTitle.getStyle().clearDisplay();
			oscePanel.getTabBar().setTabText(0, "New OSCE");
		} else {
			title.setInnerText(constants.editOsce());
			//editTitle.getStyle().clearDisplay();
			// change{	createTitle.getStyle().setDisplay(Display.NONE);
			oscePanel.getTabBar().setTabText(0, "Edit OSCE");
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
		if (validateSettingFields()) {
			delegate.saveClicked(osce,osceSettingsProxy);
		} else {
			MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
			confirmationDialogBox.showConfirmationDialog(constants.credentialsMustNotBeEmpty());
		}
	}

	private boolean validateSettingFields() {
		boolean flag = false;
		if (userName.getValue() != null && userName.getValue().isEmpty() == false && userName.getValue() != "") {
			flag = true;
			userName.removeStyleName("higlight_onViolation");
		} else {
			flag = false;
			userName.addStyleName("higlight_onViolation");
		}
		
		if (password.getValue() != null && password.getValue().isEmpty() == false && password.getValue() != "") {
			flag = true;
			password.removeStyleName("higlight_onViolation");
		} else {
			flag = false;
			password.addStyleName("higlight_onViolation");
		}
		
		if (bucketName.getValue() != null && bucketName.getValue().isEmpty() == false && bucketName.getValue() != "") {
			flag = true;
			bucketName.removeStyleName("higlight_onViolation");
		} else {
			flag = false;
			bucketName.addStyleName("higlight_onViolation");
		}
		
		if (BucketInfoType.FTP.equals(bucketInfo.getValue()) && host.getValue() != null && host.getValue().isEmpty() == false && host.getValue() != "") {
			flag = true;
			host.removeStyleName("higlight_onViolation");
		} else if (BucketInfoType.FTP.equals(bucketInfo.getValue())) {
			flag = false;
			host.addStyleName("higlight_onViolation");
		}
		
		return flag;
	}

	interface Binder extends UiBinder<Widget, OsceEditViewImpl> {
	}

	/*interface Driver extends RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> {
	}*/

	@Override
	public void setEditTitle(boolean edit) {

		if (edit) {
			//editTitle.getStyle().clearDisplay();
			// change{	createTitle.getStyle().setDisplay(Display.NONE);
			title.setInnerText(constants.editOsce());
		} else {
			//editTitle.getStyle().setDisplay(Display.NONE);
			// change{createTitle.getStyle().clearDisplay();
			title.setInnerText(constants.addOsce());
		}

	}

	@Override
	public void setPresenter(Presenter osceEditActivity) {
		this.presenter = osceEditActivity;
	}


	@Override
	public void setStudyYearPickerValues(List<StudyYears> values) {
		studyYear.setAcceptableValues(values);
	}


	/*@Override
	public void setTasksPickerValues(List<TaskProxy> values) {
		tasks.setAcceptableValues(values);
	}
*/

	@Override
	public void setSemsterValues(List<SemesterProxy> emptyList) {
		// TODO Auto-generated method stub
	//	semester.setAcceptableValues(emptyList);
	}
	
	@Override
	public void setOsceValues(List<OsceProxy> emptyList) {
		// TODO Auto-generated method stub
		//Issue # 122 : Replace pull down with autocomplete.
		//osceValue.setAcceptableValues(emptyList);
		DefaultSuggestOracle<OsceProxy> suggestOracle1 = (DefaultSuggestOracle<OsceProxy>) osceValue.getSuggestOracle();
		suggestOracle1.setPossiblilities(emptyList);
		osceValue.setSuggestOracle(suggestOracle1);
		osceValue.setRenderer(new AbstractRenderer<OsceProxy>() {

			@Override
			public String render(OsceProxy object) {
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
		//change {
		osceValue.setWidth(110);
		//change }
		//osceValue.setRenderer(new SpecialisationProxyRenderer());
		//copiedOsce.setAcceptableValues(emptyList);
		//copiedOsce.setSuggestOracle(suggestOracle1);
		//copiedOsce.setRenderer(new AbstractRenderer<OsceProxy>() {

		final EnumRenderer<StudyYears> studyYearEnumRenderer = new EnumRenderer<StudyYears>();
		final EnumRenderer<Semesters> semesterEnumRenderer = new EnumRenderer<Semesters>();
		
		DefaultSuggestOracle<OsceProxy> suggestOracle = (DefaultSuggestOracle<OsceProxy>) copiedOsce.getSuggestOracle();
		suggestOracle1.setPossiblilities(emptyList);
		copiedOsce.setSuggestOracle(suggestOracle1);
		copiedOsce.setRenderer(new AbstractRenderer<OsceProxy>() {

			@Override
			public String render(OsceProxy object) {
				if(object!=null)
				{
					String studyYear = studyYearEnumRenderer.render(object.getStudyYear());
					String semsterStr = semesterEnumRenderer.render(object.getSemester().getSemester()) + " " + object.getSemester().getCalYear();
					studyYear = studyYear + " - " + semsterStr;
					
					if (object.getIsRepeOsce())
						studyYear = studyYear + " (Repe)";
					
					return studyYear;
					//return object.getName();
				}
				else
				{
					return "";
				}
			}
		});

		// change {
		copiedOsce.setWidth(110);
		
		// change }
		//Issue # 122 : Replace pull down with autocomplete.
		
	}
	
	@Override
	public Set<TaskProxy> getTaskValue()
	{
		//Issue # 122 : Replace pull down with autocomplete.
		//return osceValue.getValue().getTasks();
		if(osceValue.getSelected()!=null)
		{
		return osceValue.getSelected().getTasks();
		}
		else
			return null;
		//Issue # 122 : Replace pull down with autocomplete.
	}


	@Override
	public void setTasksPickerValues(List<TaskProxy> emptyList) {
		// TODO Auto-generated method stub
		
	}
	

	// Highlight onViolation
	@Override
	public Map getOsceMap()
	{
		return this.osceMap;
	}
	// E Highlight onViolation	
	
	@UiHandler("preview")
	public void previewButtonClicked(ClickEvent e)
	{
		if (validateFields() == false)
		{
			MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
			dialogBox.showConfirmationDialog(constants.warningNoOfOscePost());
			
			return;
		}
		
		delegate.previewButtonClicked(preview.getAbsoluteLeft(), preview.getAbsoluteTop());
	}
	
	private boolean validateFields() {
		boolean flag = true;
	
		if (numberCourses.getValue() == null)
			flag = false;
		else if (maxNumberStudents.getValue() == null)
			flag = false;
		else if (numberRooms.getValue() == null)
			flag = false;
		else if (postLength.getValue() == null)
			flag = false;
		else if (shortBreak.getValue() == null)
			flag = false;
		else if (shortBreakSimpatChange.getValue() == null)
			flag = false;
		else if (middleBreak.getValue() == null)
			flag = false;
		else if (LongBreak.getValue() == null)
			flag = false;
		else if (lunchBreak.getValue() == null)
			flag = false;
		else if (longBreakRequiredTime.getValue() == null)
			flag = false;
		else if (lunchBreakRequiredTime.getValue() == null)
			flag = false;
		
		return flag;
	}

	public HorizontalPanel getHorizontalTabPanel() {
		return horizontalTabPanel;
	}
	
	public DivElement getLabelOsceCreationType() {
		return labelOsceCreationType;
	}

	@Override
	public void setOsceProxy(OsceProxy osce) {
		this.osce=osce;
	}

	@Override
	public void setValue(final OsceProxy osce,final OsceSettingsProxy osceSettingsProxy) {


		name.setValue(osce.getName());
		maxNumberStudents.setValue(osce.getMaxNumberStudents());
		
		osceCreationType.setValue(osce.getOsceCreationType());
		
		LongBreak.setValue(osce.getLongBreak());
		shortBreak.setValue(osce.getShortBreak());
		lunchBreak.setValue(osce.getLunchBreak());
		lunchBreakRequiredTime.setValue(osce.getLunchBreakRequiredTime());
		longBreakRequiredTime.setValue(osce.getLongBreakRequiredTime());
		middleBreak.setValue(osce.getMiddleBreak());
		studyYear.setValue(osce.getStudyYear());
		shortBreakSimpatChange.setValue(osce.getShortBreakSimpatChange());
		
		
		postLength.setValue(osce.getPostLength());
		numberCourses.setValue(osce.getNumberCourses());
		numberRooms.setValue(osce.getNumberRooms());
		spStayInPost.setValue(osce.getSpStayInPost());

		if(osceSettingsProxy != null){
			bucketInfo.setValue(osceSettingsProxy.getInfotype(), true);
			setS3Values();
			setSFTPValues();
			setGeneralInfoValues(osceSettingsProxy);
		}
	}

	private void setGeneralInfoValues(OsceSettingsProxy osceSettingsProxy) {
		settingPassword.setValue(osceSettingsProxy.getSettingPassword());
		backUpPeriod.setValue(osceSettingsProxy.getBackupPeriod());
		timeUnit.setValue(osceSettingsProxy.getTimeunit());
		pointNextExaminee.setValue(osceSettingsProxy.getNextExaminee());
		encryptionType.setValue(osceSettingsProxy.getEncryptionType());
		examReviewMode.setValue(osceSettingsProxy.getReviewMode());
		symmetricKey.setValue(osceSettingsProxy.getSymmetricKey());
		screenSaverText.setValue(osceSettingsProxy.getScreenSaverText());
	}

	
	@Override
	public void setOsceSttingsProxy(OsceSettingsProxy response) {
		this.osceSettingsProxy=response;
	}

}
