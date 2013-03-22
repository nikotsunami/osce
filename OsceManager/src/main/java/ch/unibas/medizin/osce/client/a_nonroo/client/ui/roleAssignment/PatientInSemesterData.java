package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.Iterator;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.util;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PatientInSemesterData {

	public String name;
	// public Image acceptedImage;
	public IconButton acceptedIconBtn;
	public PatientAssignLabel assignedTo;
	public IconButton navigationButton;
	public IconButton deleteButton;
	
	public IconButton editButton;
	// private int index;

	private RoleAssignmentView.Delegate delegate;
	private PatientInSemesterProxy patientInSemesterProxy;
	private PatientInSemesterData patientInSemesterData;
	private String rowSetColor;
	private int rowNumber;

	private static final OsceConstants constants = GWT.create(OsceConstants.class);

	// private final UiIcons uiIcons = GWT.create(UiIcons.class);

	/** Creates a new instance of PatientInSemesterData */
	public PatientInSemesterData() {
	}

	public PatientInSemesterData(PatientInSemesterProxy tempPatientInSemesterProxy,final int rowNumber, RoleAssignmentView.Delegate tempdelegate) {

		// setIndex(index);
		this.delegate = tempdelegate;
		delegate.showApplicationLoading(true);
		patientInSemesterData = this;
//		this.patientInSemesterProxy = tempPatientInSemesterProxy;
		this.setPatientInSemesterProxy(tempPatientInSemesterProxy);

		this.name = ((tempPatientInSemesterProxy.getStandardizedPatient().getPreName() != null) ? tempPatientInSemesterProxy.getStandardizedPatient().getPreName() : "") + ", " + tempPatientInSemesterProxy.getStandardizedPatient().getName();

		// this.acceptedImage = new Image();
		// this.acceptedImage.setStyleName("ui-icon-squaresmall-close");

		this.acceptedIconBtn = new IconButton();
		this.setAcceptedImage();
		this.rowNumber = rowNumber;
		this.acceptedIconBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				// boolean accepted = (!((patientInSemesterProxy.getAccepted()
				// != null) ? patientInSemesterProxy
				// .getAccepted().booleanValue() : false));
				
				final int row = delegate.getSelectedRow();
				delegate.onRowSelected(PatientInSemesterData.this.rowNumber);
				delegate.onDeleteClicked(row);
				delegate.onAcceptedClick(patientInSemesterData);// getIndex(),
																// accepted);

			}
		});
//
//		StringBuffer tempAssignedRole = new StringBuffer();
//		for (Iterator<PatientInRoleProxy> iterator = tempPatientInSemesterProxy.getPatientInRole().iterator(); iterator.hasNext();) {
//			PatientInRoleProxy patientInRoleProxy = (PatientInRoleProxy) iterator.next();
////			Log.info("patientInRoleProxy.getOscePost()" + patientInRoleProxy.getOscePost());
//			if (patientInRoleProxy.getOscePost() != null) {
//				if (tempAssignedRole.toString().compareTo("") != 0) {
//					tempAssignedRole.append(" ,");
//				}
//				tempAssignedRole.append(patientInRoleProxy.getOscePost().getStandardizedRole().getShortName());
//			}
//
//		}
//
//		this.assignedTo = new PatientAssignLabel(util.getFormatedString(tempAssignedRole.toString(),30));
//		this.assignedTo.addStyleName("flexTableLabel");
		// ,getIndex());
		this.assignedTo = new PatientAssignLabel("");
		
		setAssignToLabel();
		
		this.navigationButton = new IconButton();
		this.navigationButton.setIcon("seek-next");		
		this.navigationButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				delegate.onRowSelected(PatientInSemesterData.this.rowNumber);
				delegate.onDetailViewClicked(patientInSemesterData);
			}
		});

		// Module 3 Task B
		this.deleteButton = new IconButton();
		this.deleteButton.setIcon("trash");
		this.deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				final int row = delegate.getSelectedRow();
				delegate.onRowSelected(PatientInSemesterData.this.rowNumber);
				if (patientInSemesterProxy.getPatientInRole() != null && patientInSemesterProxy.getPatientInRole().size() > 0) {

					MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog(constants.warningPatientHasRole());

					delegate.onRowSelected(row-1);
					
				} else {

					final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.reallyDelete());

					dialogBox.getYesBtn().addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent arg0) {
							delegate.onDeleteClicked(row);
							delegate.onDeleteButtonClicked(patientInSemesterData);
							dialogBox.hide();
						}
					});
					dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
						
							delegate.onRowSelected(row-1);
						}
					});
				
					dialogBox.showYesNoDialog(constants.confirmationDeleteAssignment());
				}

			}
		});
		
		this.editButton = new IconButton();
		this.editButton.setIcon("plusthick");
		this.editButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//Window.alert("Clicked : " + patientInSemesterProxy.getId());
				/*final PopupPanel panel = new PopupPanel();
				final VerticalPanel vp = new VerticalPanel();
				final IconButton okBtn = new IconButton();
				okBtn.setText(constants.okBtn());
				vp.add(okBtn);
				
				panel.add(vp);
				
				panel.center();
				panel.show();*/
				
				delegate.editRoleAssignmentClicked(patientInSemesterProxy, event.getClientX(), event.getClientY());
			}
		});
		
		delegate.showApplicationLoading(false);
		// Module 3 Task B
	}

	public void setAssignToLabel(){

		StringBuffer tempAssignedRole = new StringBuffer();
		for (Iterator<PatientInRoleProxy> iterator = this.patientInSemesterProxy.getPatientInRole().iterator(); iterator.hasNext();) {
			PatientInRoleProxy patientInRoleProxy = (PatientInRoleProxy) iterator.next();
//			Log.info("patientInRoleProxy.getOscePost()" + patientInRoleProxy.getOscePost());
			if (patientInRoleProxy.getOscePost() != null) {
				if (tempAssignedRole.toString().compareTo("") != 0) {
					tempAssignedRole.append(" ,");
				}
				tempAssignedRole.append(patientInRoleProxy.getOscePost().getStandardizedRole().getShortName());
			}

		}

//		Log.info("check for tempAssignedRole is : " + tempAssignedRole.toString());
		
		this.assignedTo.setText(util.getFormatedString(tempAssignedRole.toString(),30));
		this.assignedTo.addStyleName("flexTableLabel");
	}
	
	
	
	
	
	public void setNavigationButton(boolean enabled){
		navigationButton.setEnabled(enabled);
		if (!enabled) {
			navigationButton.addStyleName("flexTable-Button-Disabled");
		} else {
			navigationButton.removeStyleName("flexTable-Button-Disabled");
		}
		
	}
	
	public PatientInSemesterProxy getPatientInSemesterProxy() {
		return patientInSemesterProxy;
	}

	public void setPatientInSemesterProxy(PatientInSemesterProxy patientInSemesterProxy) {
		this.patientInSemesterProxy = patientInSemesterProxy;
	}

	class PatientAssignLabel extends Label implements MouseDownHandler {
		// private int index;

		public PatientAssignLabel(String text, boolean wordWrap) {
			super(text, wordWrap);
			// setIndex(index);
			initDetail();

		}

		private void initDetail() {
			this.addMouseDownHandler(this);
			this.setWordWrap(false);
			this.setPixelSize(200, 5);
		}

		public PatientAssignLabel(String text) {
			super(text);
			// setIndex(index);
			initDetail();
		}

		public PatientAssignLabel() {
			super();
			// setIndex(index);
			initDetail();
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {

			if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT) {
				event.stopPropagation();
				event.preventDefault();

				// Window.alert("right click from mouse down handler"
				// + ((PatientAssignLabel) event.getSource()).getText());

				Log.info("Index of Patient in semester : " + ((PatientAssignLabel) event.getSource()));

				delegate.getDetailedPatient(patientInSemesterProxy, event.getClientX(), event.getClientY());
//				
//				StringBuffer tempTraining = new StringBuffer();
//				StringBuffer tempOsceDay = new StringBuffer();
//				StringBuffer tempAssignedRole = new StringBuffer();
//
//				if (tempPatientInSemesterProxy.getTrainings() != null) {
//
//					for (Iterator<TrainingProxy> iterator = tempPatientInSemesterProxy.getTrainings().iterator(); iterator.hasNext();) {
//						TrainingProxy trainingProxy = (TrainingProxy) iterator.next();
//						Log.info("TrainingProxy.getName()" + trainingProxy.getName());
//						if (trainingProxy != null) {
//							if (tempTraining.toString().compareTo("") != 0) {
//								tempTraining.append(" ,");
//							}
//							tempTraining.append(trainingProxy.getName());
//						}
//
//					}
//				}
//
//				// DateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
//				if (tempPatientInSemesterProxy.getOsceDays() != null) {
//					for (Iterator<OsceDayProxy> iterator = tempPatientInSemesterProxy.getOsceDays().iterator(); iterator.hasNext();) {
//						OsceDayProxy osceDayProxy = (OsceDayProxy) iterator.next();
//						Log.info("OsceDayProxy.getName()" + osceDayProxy.getOsce().getName());
//						if (osceDayProxy != null) {
//							if (tempOsceDay.toString().compareTo("") != 0) {
//								tempOsceDay.append(" ,");
//							}
//							tempOsceDay.append((osceDayProxy.getOsceDate() != null) ? // dateFormat.format
//							(DateTimeFormat.getShortDateFormat().format(osceDayProxy.getOsceDate()))
//									: "" + " - " + ((osceDayProxy.getOsce() != null && osceDayProxy.getOsce().getName() != null) ? osceDayProxy.getOsce().getName() : ""));
//						}
//
//					}
//				}
//				if (tempPatientInSemesterProxy.getPatientInRole() != null) {
//
//					for (Iterator<PatientInRoleProxy> iterator = tempPatientInSemesterProxy.getPatientInRole().iterator(); iterator.hasNext();) {
//						PatientInRoleProxy patientInRoleProxy = (PatientInRoleProxy) iterator.next();
//						Log.info("patientInRoleProxy.getOscePost()" + patientInRoleProxy.getOscePost());
//						if (patientInRoleProxy.getOscePost() != null) {
//							if (tempAssignedRole.toString().compareTo("") != 0) {
//								tempAssignedRole.append(" ,");
//							}
//							tempAssignedRole.append(patientInRoleProxy.getOscePost().getStandardizedRole().getShortName());
//						}
//
//					}
//				}
//
//				if ((tempTraining.toString().compareTo("") == 0) && (tempOsceDay.toString().compareTo("") == 0) && (tempAssignedRole.toString().compareTo("") == 0)) {
//					return;
//				}
//
//				RoleAssignmentPopupViewImpl.setPopUpText(tempTraining.toString(), tempOsceDay.toString(), tempAssignedRole.toString(), event.getClientX(), event.getClientY());

			}

		}

		// public int getIndex() {
		// return index;
		// }
		//
		// public void setIndex(int Index) {
		// this.index = Index;
		// }
	}

	// public int getIndex() {
	// return index;
	// }
	//
	// public void setIndex(int Index) {
	// this.index = Index;
	// }

	public void setAcceptedImage() {
		if (patientInSemesterProxy.getAccepted() != null) {
			if (patientInSemesterProxy.getAccepted().booleanValue()) {
				acceptedIconBtn.setIcon("check");
			} else {
				acceptedIconBtn.setIcon("close");
			}
		}
	}

	public String getRowSetColor() {
		return rowSetColor;
	}

	public void setRowSetColor(String rowSetColor) {
		this.rowSetColor = rowSetColor;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
}