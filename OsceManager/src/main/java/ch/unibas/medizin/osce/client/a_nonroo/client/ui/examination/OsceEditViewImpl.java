package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.scaffold.ui.ShortBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class OsceEditViewImpl extends Composite implements OsceEditView, Editor<OsceProxy> {
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

	@UiField(provided = true)
	public CheckBox isRepeOsce = new CheckBox() {

		public void setValue(Boolean value) {
			super.setValue(value == null ? Boolean.FALSE : value);
		}
	};
	
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
	@UiField
	DivElement labelIsRepe;
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
	@Ignore
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
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	// Highlight onViolation
		Map<String, Widget> osceMap;
	// E Highlight onViolation

		
	public OsceEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
		
		oscePanel.selectTab(0);
		oscePanel.getTabBar().setTabText(0, constants.manageOsces());
		TabPanelHelper.moveTabBarToBottom(oscePanel);
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
		labelIsRepe.setInnerText(constants.osceIsRepe());
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
		osceMap.put("isRepeOsce", isRepeOsce);
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
		
		
		
		
		// E Highlight onViolation
		
		if(isRepeOsce.isChecked()==true)
		{
			copiedOsce.setEnabled(true);
		}
		
		isRepeOsce.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// TODO Auto-generated method stub
				if(event.getValue()==true)
				{
					copiedOsce.setEnabled(true);
				}
				else
				{
					copiedOsce.setEnabled(false);
				}
			}
		});		
		
		horizontalTabPanel.getElement().getStyle().setMarginTop(5, Unit.PX);
		//horizontalTabPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
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
		
		  
		
		
		
		delegate.saveClicked();
	}

	interface Binder extends UiBinder<Widget, OsceEditViewImpl> {
	}

	interface Driver extends RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> {
	}

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
}
