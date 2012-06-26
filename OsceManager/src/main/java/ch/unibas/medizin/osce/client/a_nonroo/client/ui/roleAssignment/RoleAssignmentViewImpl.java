package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleAssignmentViewImpl extends Composite implements
		RoleAssignmentView {

	private static RoleAssignmentViewImplUiBinder uiBinder = GWT
			.create(RoleAssignmentViewImplUiBinder.class);

	interface RoleAssignmentViewImplUiBinder extends
			UiBinder<Widget, RoleAssignmentViewImpl> {
	}

	// private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;

	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	@UiField
	public SimplePanel detailsPanel;

	@UiField
	PatientInSemesterFlexTable table;

	// @UiField
	// FTable fTable;

	@UiField
	Button surveyImpBtn;
	@UiField
	Button autoAssignmentBtn;
	@UiField
	Button addManuallyBtn;

	private Presenter presenter;

	private List<PatientInSemesterData> patientInSemesterDataList;
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private String[] headers;

	public RoleAssignmentViewImpl() {

		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0),
				OsMaConstant.SPLIT_PANEL_MINWIDTH);
		surveyImpBtn.setText(constants.surveyImport());
		autoAssignmentBtn.setText(constants.autoAssign());
		addManuallyBtn.setText(constants.addManually());
		headers = new String[] { constants.name(), constants.accepted(),
				constants.assignTo(), "" };

		// fTable = new FTable(OsMaConstant.TABLE_PAGE_SIZE, tableResources);

	}

	@UiHandler("surveyImpBtn")
	public void onSurveyImpBtnClicked(ClickEvent event) {
		// TODO : import patient detail logic

	}

	@UiHandler("autoAssignmentBtn")
	public void onAutoAssignmentBtnClicked(ClickEvent event) {
		// TODO : Auto assignment detail logic

	}

	@UiHandler("addManuallyBtn")
	public void onAddManuallyBtnClicked(ClickEvent event) {

		delegate.onAddManuallyClicked();
	}

	public void init() {

		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style",
				"position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

		// table.addTableListener(new TableListener() {
		// public void onCellClicked(SourcesTableEvents sender, int row,
		// int cell) {
		// // History.newItem( ""+row );
		// Log.info("row" + row + "Cell " + cell);
		// }
		// });

		// class MyObject{
		// private CheckBox checkBox;
		//
		// public MyObject(CheckBox checkBox) {
		// this.setCheckBox(checkBox);
		// }
		//
		// public CheckBox getCheckBox() {
		// return checkBox;
		// }
		//
		// public void setCheckBox(CheckBox checkBox) {
		// this.checkBox = checkBox;
		// }
		//
		// }
		// CellTable<MyObject> cellTable = new CellTable<MyObject>();
		// TextColumn<MyObject> firstColumn = new TextColumn<MyObject>() {
		// @Override
		// public CheckBox getValue(MyObject object) {
		// return object.getCheckBox;
		// }
		// };
		// cellTable.addColumn(firstColumn , "Checkbox");

		// fTable.addColumn(new TextColumn<PatientInSemesterData>() {
		// {
		// this.setSortable(true);
		// }
		//
		// Renderer<java.lang.String> renderer = new
		// AbstractRenderer<java.lang.String>() {
		//
		// public String render(java.lang.String obj) {
		// return obj == null ? "" : String.valueOf(obj);
		// }
		// };
		//
		// @Override
		// public String getValue(PatientInSemesterData pISD) {
		// return renderer.render((pISD.name == null) ? "" : pISD.name);
		//
		// }
		// }, constants.roomMaterialName());
		//
		// addColumn(new ActionCell<PatientInSemesterData>(
		// OsMaConstant.DELETE_ICON,
		// new ActionCell.Delegate<PatientInSemesterData>() {
		// public void execute(PatientInSemesterData materialListProxy) {
		// // Window.alert("You clicked " +
		// // institution.getInstitutionName());
		// if (Window.confirm("wirklich löschen?")) {
		// // delegate.deleteClicked(materialListProxy);
		// }
		// }
		// }), "", new GetValue<PatientInSemesterData>() {
		// public PatientInSemesterData getValue(
		// PatientInSemesterData patientInSemesterData) {
		// return patientInSemesterData;
		// }
		//
		// }, null);
		// fTable.addColumnStyleName(1, "iconCol");
		//
		// fTable.addColumn(new TextColumn<PatientInSemesterData>() {
		// {
		// this.setSortable(true);
		// }
		//
		// Renderer<String> renderer = new AbstractRenderer<String>() {
		//
		// public String render(String obj) {
		// return obj == null ? "" : String.valueOf(obj);
		// }
		// };
		//
		// @Override
		// public String getValue(PatientInSemesterData materialListProxy) {
		// return renderer
		// .render((materialListProxy.assignedTo == null) ? ""
		// : materialListProxy.assignedTo);
		//
		// }
		// }, constants.roomMaterialPrice());

	}

	// private static interface GetValue<C> {
	// C getValue(PatientInSemesterData patientInSemesterData);
	// }

	// private <C> void addColumn(Cell<C> cell, String headerText,
	// final GetValue<C> getter,
	// FieldUpdater<PatientInSemesterData, C> fieldUpdater) {
	//
	// Column<PatientInSemesterData, C> column = new
	// Column<PatientInSemesterData, C>(
	// cell) {
	// @Override
	// public C getValue(PatientInSemesterData object) {
	// return getter.getValue(object);
	// }
	// };
	// column.setFieldUpdater(fieldUpdater);
	// if (cell instanceof AbstractEditableCell<?, ?>) {
	// editableCells.add((AbstractEditableCell<?, ?>) cell);
	// }
	// fTable.addColumn(column, headerText);
	// }

	// private class RightClickableHeader extends SafeHtmlHeader {
	// public RightClickableHeader(SafeHtml headerHtml, String columnId,
	// String datatype) {
	// super(headerHtml);
	// sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP
	// | Event.ONCONTEXTMENU);
	// }
	//
	// @Override
	// public void onBrowserEvent(Context context, Element elem,
	// final NativeEvent event) {
	// if ((event.getButton() & NativeEvent.BUTTON_RIGHT) > 0) {
	// Window.alert("Right click!");
	// } else {
	// super.onBrowserEvent(context, elem, event);
	// }
	// }
	// }
	//
	// private List<AbstractEditableCell<?, ?>> editableCells;

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setData(List<PatientInSemesterData> patientInSemesterDataList) {

		table.setSource(patientInSemesterDataList, getHeaderRow());

	}

	public List<PatientInSemesterData> getPatientInSemesterData() {
		return patientInSemesterDataList;
	}

	private String[] getHeaderRow() {
		return headers;
	}

	@Override
	public Button getAddManuallyBtn() {
		return addManuallyBtn;
	}

}
