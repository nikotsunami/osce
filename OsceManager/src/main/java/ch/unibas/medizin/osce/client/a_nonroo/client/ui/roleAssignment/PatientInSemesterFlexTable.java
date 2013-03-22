package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.FlexTable;

public class PatientInSemesterFlexTable extends FlexTable {
	// private String headerStyle;
	// private String selectedStyle;
	private List<PatientInSemesterData> patientInSemesterDatas;
	private int lastSelectedRowIndex = 1;
	private boolean setNavigationButton = false;

	// private RoleAssignmentView.Delegate delegate;

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

	public int getLastSelectedRowIndex() {
		return lastSelectedRowIndex;
	}

	public void setLastSelectedRowIndex(int lastSelectedRowIndex) {
		this.lastSelectedRowIndex = lastSelectedRowIndex;
	}

	public PatientInSemesterFlexTable() {
		super();
		this.setCellPadding(1);
		this.setCellSpacing(0);
	}

	public void setSource(List<PatientInSemesterData> patientInSemesterDatas, String[] headers) {

		removeAllRows();
		// this.delegate = delegate;
//		for (int i = this.getRowCount(); i > 0; i--) {
//			this.removeRow(0);
//		}
		if (patientInSemesterDatas == null) {
			return;
		}

		this.patientInSemesterDatas = patientInSemesterDatas;
		this.setHeaderStyle(headers);
		this.setTableStyle();
		// setStyleFlexTable();

		this.setPatientInSemesterDatas(patientInSemesterDatas);
	}

	public void reSetTableStyle(){
		int rows = patientInSemesterDatas.size();
		PatientInSemesterData patientInSemesterData;
		for (int i = 0; i < rows; i++) {
			patientInSemesterData = patientInSemesterDatas.get(i);
			this.getRowFormatter().removeStyleName(i + 1, patientInSemesterData.getRowSetColor());
			patientInSemesterData.setRowSetColor((i % 2 == 0) ? "flexTableEvenRow" : "flexTableOddRow");
			patientInSemesterData.setRowNumber(i);
			this.getRowFormatter().addStyleName(i + 1, patientInSemesterData.getRowSetColor());
		}
	}
	
	public void setNewPatient(PatientInSemesterProxy tempPatientInSemesterProxy, RoleAssignmentView.Delegate tempdelegate) {
		int i = patientInSemesterDatas.size();

		PatientInSemesterData patientInSemesterData = new PatientInSemesterData(tempPatientInSemesterProxy, i, tempdelegate);
		
		patientInSemesterData.setNavigationButton(setNavigationButton);

		patientInSemesterData.setRowSetColor((i % 2 == 0) ? "flexTableEvenRow" : "flexTableOddRow");
		this.setText(i + 1, 0, patientInSemesterData.name);
		// this.setWidget(row, 1, patientInSemesterData.acceptedImage);
		this.setWidget(i + 1, 1, patientInSemesterData.acceptedIconBtn);

		this.setWidget(i + 1, 2, patientInSemesterData.assignedTo);
		this.setWidget(i + 1, 3, patientInSemesterData.navigationButton);

		// Module 3 Task B
		this.setWidget(i + 1, 4, patientInSemesterData.deleteButton);
		//			this.getRowFormatter().addStyleName(row, "flexTableRow");
		//			this.getCellFormatter().addStyleName(row, 0, patientInSemesterData.getRowSetColor());

		//		for (int j = 0; j < headers.length; j++) {
		//				RowFormatter rowFormatter = new RowFormatter();
		this.getRowFormatter().addStyleName(i + 1, patientInSemesterData.getRowSetColor());
		patientInSemesterDatas.add(patientInSemesterData);
		
		
	}
	
	public void setTableStyle() {
		
		
		// String[] headers = source.getHeaderRow();
	

			// Element headerTr = DOM.createTR();
			// DOM.insertChild(this.getElement(), head, 0);
			// DOM.insertChild(head, headerTr, 0);
			// DOM.setElementAttribute(headerTr, "style",
			// "overflow:auto;text-align: center;");
			//
			// Element tBody = getBodyElement();
			// DOM.setElementAttribute(tBody, "style",
			// "overflow:auto;text-align: left;");
		
		int rows = patientInSemesterDatas.size();
		PatientInSemesterData patientInSemesterData;

		for (int i = 0; i < rows; i++) {
			patientInSemesterData = patientInSemesterDatas.get(i);
			patientInSemesterData.setRowSetColor((i % 2 == 0) ? "flexTableEvenRow" : "flexTableOddRow");
			this.setText(i+1, 0, patientInSemesterData.name);
			// this.setWidget(row, 1, patientInSemesterData.acceptedImage);
			this.setWidget(i+1, 1, patientInSemesterData.acceptedIconBtn);

			this.setWidget(i+1, 2, patientInSemesterData.assignedTo);
			
			this.setWidget(i+1, 3, patientInSemesterData.editButton);
			
			this.setWidget(i+1, 4, patientInSemesterData.navigationButton);
			
			// Module 3 Task B
			this.setWidget(i+1, 5, patientInSemesterData.deleteButton);
			
			
			//			this.getRowFormatter().addStyleName(row, "flexTableRow");
			//			this.getCellFormatter().addStyleName(row, 0, patientInSemesterData.getRowSetColor());

//			for (int j = 0; j < headers.length; j++) {
				//				RowFormatter rowFormatter = new RowFormatter();
				this.getRowFormatter().addStyleName(i+1, patientInSemesterData.getRowSetColor());

				//				this.setRowFormatter(rowFormatter);
				//				this.getCellFormatter().addStyleName(row, j, patientInSemesterData.getRowSetColor());

				//				this.getCellFormatter().addStyleName(row,j, "flexTableRow");
//			}
			// Module 3 Task B

		//	row++;
		}

	}

	public void setRowData( PatientInSemesterProxy patientInSemesterProxy){
		
		PatientInSemesterData patientInSemesterData = patientInSemesterDatas.get(this.lastSelectedRowIndex - 1);
		patientInSemesterData.setPatientInSemesterProxy(patientInSemesterProxy);
//		this.getWidget(this.lastSelectedRowIndex - 1, 2).removeFromParent();
		patientInSemesterData.setAssignToLabel();
//		this.removeCell(this.lastSelectedRowIndex - 1, 2);
		
//		this.setWidget(this.lastSelectedRowIndex - 1, 2, patientInSemesterData.assignedTo);
		patientInSemesterDatas.set(this.lastSelectedRowIndex - 1, patientInSemesterData);

	}

	
	private void setHeaderStyle(String[] headers) {

		if (headers != null) {
			for (int i = 0; i < headers.length; i++) {
				this.setText(0, i, headers[i]);
			}
		}

		this.getRowFormatter().addStyleName(0, "flexTableHeader");
		// this.getColumnFormatter().addStyleName(0, "flexTableFirstColumn");
		// this.getColumnFormatter().addStyleName(3, "flexTableLastColumn");
		this.getCellFormatter().addStyleName(0, 0, "flexTableFirstColumnHeader");
		this.getCellFormatter().addStyleName(0, 1, "flexTableColumnHeader");
		this.getCellFormatter().addStyleName(0, 2, "flexTableColumnHeader");		
		// Module 3 Task B
		this.getCellFormatter().addStyleName(0, 3, "flexTableColumnHeader");		
		//this.getCellFormatter().addStyleName(0, 4, "flexTableLastColumnHeader");
		this.getCellFormatter().addStyleName(0, 4, "flexTableColumnHeader");
		this.getCellFormatter().addStyleName(0, 5, "flexTableLastColumnHeader");
		// Module 3 Task B

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

	public void setPatientInSemesterDatas(List<PatientInSemesterData> patientInSemesterDatas) {
		this.patientInSemesterDatas = patientInSemesterDatas;
	}

	public void setNavigationButtonEnable(boolean enabled) {
//		Log.info("Check for Button");
		this.setNavigationButton = enabled;
		if (this.patientInSemesterDatas != null && this.patientInSemesterDatas.size() > 0) {
			int rows = this.patientInSemesterDatas.size();
			PatientInSemesterData patientInSemesterData;

			for (int i = 0; i < rows; i++) {
				patientInSemesterData = patientInSemesterDatas.get(i);
				patientInSemesterData.setNavigationButton(enabled);
			}
		}
	}

	public PatientInSemesterProxy onRowClick(int selectedRow) {

		PatientInSemesterData patientInSemesterData = null;
		//		Cell selectedCell = this.getCellForEvent(event);		
		if (selectedRow > 0) {
			Log.info("lastSelectedRowIndex is : " + lastSelectedRowIndex);
			Log.info("patientInSemesterDatas.size() is : " + patientInSemesterDatas.size());
			
			if(patientInSemesterDatas.size() >= this.lastSelectedRowIndex && this.lastSelectedRowIndex != 0){
			
			Log.info("lastSelectedRowIndex is : " + lastSelectedRowIndex);

			removeSelectedStyle(this.lastSelectedRowIndex);
			}

			this.lastSelectedRowIndex = selectedRow;
			patientInSemesterData = patientInSemesterDatas.get(this.lastSelectedRowIndex - 1);
			Log.info("lastSelectedRowIndex is : " + lastSelectedRowIndex);
			this.getRowFormatter().addStyleName(this.lastSelectedRowIndex, "flexTableSelectedRow");
		}

		return patientInSemesterData.getPatientInSemesterProxy();

	}
	
	public void removeSelectedStyle(int row){
		if(row > 0){
			Log.info("lastSelectedRowIndex is : " + row);
			Log.info("patientInSemesterDatas.size() is : " + patientInSemesterDatas.size());
			
			PatientInSemesterData patientInSemesterData = patientInSemesterDatas.get(row - 1);
			this.getRowFormatter().removeStyleName(row, "flexTableSelectedRow");
			this.getRowFormatter().addStyleName(row,patientInSemesterData.getRowSetColor());
		}
		
	}
	
}