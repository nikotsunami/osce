package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.managed.ui.CourseSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.OsceDaySetEditor;
import ch.unibas.medizin.osce.client.managed.ui.StudentOscesSetEditor;
import ch.unibas.medizin.osce.client.managed.ui.TaskSetEditor;
import ch.unibas.medizin.osce.shared.StudyYears;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class OsceEditViewImpl extends Composite implements OsceEditView, Editor<OsceProxy> {
	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	Element editTitle;

	@UiField
	Element createTitle;

	@UiField(provided = true)
	ValueListBox<StudyYears> studyYear = new ValueListBox<StudyYears>(new AbstractRenderer<ch.unibas.medizin.osce.shared.StudyYears>() {

		public String render(ch.unibas.medizin.osce.shared.StudyYears obj) {
			return obj == null ? "" : String.valueOf(obj);
		}
	});

	@UiField
	IntegerBox maxNumberStudents;

	@UiField
	IntegerBox numberPosts;

	@UiField
	IntegerBox numberCourses;

	@UiField
	IntegerBox postLength;

	@UiField(provided = true)
	CheckBox isRepeOsce = new CheckBox() {

		public void setValue(Boolean value) {
			super.setValue(value == null ? Boolean.FALSE : value);
		}
	};

	@UiField
	IntegerBox numberRooms;

	@UiField(provided = true)
	CheckBox isValid = new CheckBox() {

		public void setValue(Boolean value) {
			super.setValue(value == null ? Boolean.FALSE : value);
		}
	};

	@UiField(provided = true)
	ValueListBox<SemesterProxy> semester = new ValueListBox<SemesterProxy>(ch.unibas.medizin.osce.client.managed.ui.SemesterProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.SemesterProxy>());

	@UiField
	OsceDaySetEditor osce_days;

	@UiField
	CourseSetEditor courses;

	@UiField
	TaskSetEditor tasks;

	@UiField
	StudentOscesSetEditor osceStudents;


	//   @UiField
	//   ResponsibilitySetEditor responsibilities;

	@UiField
	Button cancel;

	@UiField
	Button save;

	@UiField
	DivElement errors;

	private Delegate delegate;

	private Presenter presenter;


	public OsceEditViewImpl() {
		initWidget(BINDER.createAndBindUi(this));
	}


	@Override
	public RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> createEditorDriver() {
		RequestFactoryEditorDriver<OsceProxy, OsceEditViewImpl> driver = GWT.create(Driver.class);
		driver.initialize(this);
		return driver;
	}

	public void setCreating(boolean creating) {
		if (creating) {
			editTitle.getStyle().setDisplay(Display.NONE);
			createTitle.getStyle().clearDisplay();
		} else {
			editTitle.getStyle().clearDisplay();
			createTitle.getStyle().setDisplay(Display.NONE);
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
			editTitle.getStyle().clearDisplay();
			createTitle.getStyle().setDisplay(Display.NONE);
		} else {
			editTitle.getStyle().setDisplay(Display.NONE);
			createTitle.getStyle().clearDisplay();
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


	@Override
	public void setTasksPickerValues(List<TaskProxy> values) {
		tasks.setAcceptableValues(values);
	}
}
