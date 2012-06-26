package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;

public class PatientInSemesterFlexTable extends FlexTable {
	// private String headerStyle;
	// private String selectedStyle;
	private List<PatientInSemesterData> patientInSemesterDatas;
	private RoleAssignmentView.Delegate delegate;

	// private int selectedRow;

	// public Table(List<PatientInSemesterData> patientInSemesterDatas, String
	// stylePrefix) {
	// super();
	// this.setCellPadding(1);
	// this.setCellSpacing(0);
	// this.setWidth("100%");
	// this.selectedStyle = stylePrefix + "-selected";
	// this.headerStyle = stylePrefix + "-header";
	// // this.addTableListener(this);
	// this.setSource(patientInSemesterDatas);
	// //
	// // sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOVER
	// // | Event.ONMOUSEOUT);
	// }

	public PatientInSemesterFlexTable() {
		super();
		this.setCellPadding(1);
		this.setCellSpacing(0);
		this.setWidth("100%");

		// this.addTableListener(this);
		// this.setSource(source);

	}

	public void setSource(List<PatientInSemesterData> patientInSemesterDatas,
			String[] headers) {

		this.delegate = delegate;
		for (int i = this.getRowCount(); i > 0; i--) {
			this.removeRow(0);
		}
		if (patientInSemesterDatas == null) {
			return;
		}

		int row = 0;
		// String[] headers = source.getHeaderRow();
		if (headers != null) {
			for (int i = 0; i < headers.length; i++) {
				this.setText(row, i, headers[i]);
			}

			// Element headerTr = DOM.createTR();
			// DOM.insertChild(this.getElement(), head, 0);
			// DOM.insertChild(head, headerTr, 0);
			// DOM.setElementAttribute(headerTr, "style",
			// "overflow:auto;text-align: center;");
			//
			// Element tBody = getBodyElement();
			// DOM.setElementAttribute(tBody, "style",
			// "overflow:auto;text-align: left;");
			row++;
		}
		int rows = patientInSemesterDatas.size();
		PatientInSemesterData patientInSemesterData;

		for (int i = 0; i < rows; i++) {
			patientInSemesterData = patientInSemesterDatas.get(i);
			patientInSemesterData
					.setRowSetColor((i % 2 == 0) ? "flexTableEvenRow"
							: "flexTableOddRow");
			this.setText(row, 0, patientInSemesterData.name);
			// this.setWidget(row, 1, patientInSemesterData.acceptedImage);
			this.setWidget(row, 1, patientInSemesterData.acceptedIconBtn);

			this.setWidget(row, 2, patientInSemesterData.assignedTo);
			this.setWidget(row, 3, patientInSemesterData.navigationButton);
			this.getRowFormatter().addStyleName(row, "flexTableRow");

			this.getCellFormatter().addStyleName(row, 0,
					patientInSemesterData.getRowSetColor());
			this.getCellFormatter().addStyleName(row, 1,
					patientInSemesterData.getRowSetColor());
			this.getCellFormatter().addStyleName(row, 2,
					patientInSemesterData.getRowSetColor());

			this.getCellFormatter().addStyleName(row, 3,
					patientInSemesterData.getRowSetColor());

			row++;
		}

		applyTableStyle();
		// setStyleFlexTable();

		this.setPatientInSemesterDatas(patientInSemesterDatas);
	}

	private void applyTableStyle() {

		this.getRowFormatter().addStyleName(0, "flexTableHeader");
		// this.getColumnFormatter().addStyleName(0, "flexTableFirstColumn");
		// this.getColumnFormatter().addStyleName(3, "flexTableLastColumn");
		this.getCellFormatter()
				.addStyleName(0, 0, "flexTableFirstColumnHeader");
		this.getCellFormatter().addStyleName(0, 1, "flexTableColumnHeader");
		this.getCellFormatter().addStyleName(0, 2, "flexTableColumnHeader");

		this.getCellFormatter().addStyleName(0, 3, "flexTableLastColumnHeader");

		// Element tBody = getBodyElement();
		// Log.info(tBody.getString() + "Child count " + tBody.getChildCount());
		//
		// Element tr = DOM.getChild(tBody, 0);
		//
		// if (tr != null) {
		// Element td = DOM.getChild(tr, 0);
		// DOM.setStyleAttribute(td, "-moz-border-radius-topleft", "8px");
		//
		// td = DOM.getChild(tr, 3);
		// DOM.setStyleAttribute(td, "-moz-border-radius-topright", "8px");
		// }

	}

	// private void setStyleFlexTable() {
	// Element tBody = getBodyElement();
	// Log.info(tBody.getString() + "Child count " + tBody.getChildCount());
	// Element tr;
	//
	// tr = DOM.getChild(tBody, 0);
	// if (tr != null) {
	// DOM.setStyleAttribute(tr, "backgroundColor", "#dadada");
	//
	// // DOM.setStyleAttribute(tr, "gwt-image",
	// // "cellTableHeaderBackground");
	//
	// // DOM.setStyleAttribute(tr, "border-bottom", "1px solid #AAAAAA");
	// // DOM.setStyleAttribute(tr, "border-right", "1px solid #AAAAAA");
	//
	// DOM.setStyleAttribute(tr, "padding", "0px 10px");
	// // DOM.setStyleAttribute(tr, "text-align", "left");
	// DOM.setStyleAttribute(tr, "color", "#212121");
	// DOM.setStyleAttribute(tr, "overflow", "hidden");
	//
	// DOM.setStyleAttribute(tr, "height", "30px");
	// }
	//
	// for (int i = 1; i < tBody.getChildCount(); i++) {
	// tr = DOM.getChild(tBody, i);
	// if (tr != null) {
	//
	// DOM.setStyleAttribute(tr, "backgroundColor",
	// (i % 2 == 1) ? "#ffffff" : "#eee");
	// DOM.setStyleAttribute(tr, "overflow", "hidden");
	// }
	// }
	// }

	public List<PatientInSemesterData> getPatientInSemesterDatas() {
		return patientInSemesterDatas;
	}

	public void setPatientInSemesterDatas(
			List<PatientInSemesterData> patientInSemesterDatas) {
		this.patientInSemesterDatas = patientInSemesterDatas;
	}

}