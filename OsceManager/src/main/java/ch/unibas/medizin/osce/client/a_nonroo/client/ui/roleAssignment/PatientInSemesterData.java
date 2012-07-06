package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.Iterator;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.TrainingProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;

@SuppressWarnings("deprecation")
public class PatientInSemesterData {

	public String name;
	// public Image acceptedImage;
	public Button acceptedIconBtn;
	public PatientAssignLabel assignedTo;
	public Button navigationButton;
	// private int index;

	private RoleAssignmentView.Delegate delegate;
	private PatientInSemesterProxy patientInSemesterProxy;
	private PatientInSemesterData patientInSemesterData;
	private String rowSetColor;

	// private final UiIcons uiIcons = GWT.create(UiIcons.class);

	/** Creates a new instance of PatientInSemesterData */
	public PatientInSemesterData() {
	}

	public PatientInSemesterData(
			PatientInSemesterProxy tempPatientInSemesterProxy,// int index,
			RoleAssignmentView.Delegate tempdelegate) {

		// setIndex(index);
		this.delegate = tempdelegate;
		patientInSemesterData = this;
		this.patientInSemesterProxy = tempPatientInSemesterProxy;

		this.name = ((tempPatientInSemesterProxy.getStandardizedPatient()
				.getPreName() != null) ? tempPatientInSemesterProxy
				.getStandardizedPatient().getPreName() : "")
				+ ", "
				+ tempPatientInSemesterProxy.getStandardizedPatient().getName();

		// this.acceptedImage = new Image();
		// this.acceptedImage.setStyleName("ui-icon-squaresmall-close");

		this.acceptedIconBtn = new Button();
		this.setAcceptedImage();
		this.acceptedIconBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				// boolean accepted = (!((patientInSemesterProxy.getAccepted()
				// != null) ? patientInSemesterProxy
				// .getAccepted().booleanValue() : false));
				delegate.onAcceptedClick(patientInSemesterData);// getIndex(),
																// accepted);

			}
		});

		StringBuffer tempAssignedRole = new StringBuffer();
		for (Iterator<PatientInRoleProxy> iterator = tempPatientInSemesterProxy
				.getPatientInRole().iterator(); iterator.hasNext();) {
			PatientInRoleProxy patientInRoleProxy = (PatientInRoleProxy) iterator
					.next();
			Log.info("patientInRoleProxy.getOscePost()"
					+ patientInRoleProxy.getOscePost());
			if (patientInRoleProxy.getOscePost() != null) {
				if (tempAssignedRole.toString().compareTo("") != 0) {
					tempAssignedRole.append(" ,");
				}
				tempAssignedRole.append(patientInRoleProxy.getOscePost()
						.getStandardizedRole().getShortName());
			}

		}

		this.assignedTo = new PatientAssignLabel(tempAssignedRole.toString());
		// ,getIndex());

		this.setPatientInSemesterProxy(tempPatientInSemesterProxy);
		this.navigationButton = new Button();
		this.navigationButton.setHTML(OsMaConstant.SEEK_FIRST_ICON);		
		this.navigationButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				delegate.onDetailViewClicked(patientInSemesterData);

			}
		});
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

	public void setPatientInSemesterProxy(
			PatientInSemesterProxy patientInSemesterProxy) {
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

				Log.info("Index of Patient in semester : "
						+ ((PatientAssignLabel) event.getSource()));

				StringBuffer tempTraining = new StringBuffer();
				StringBuffer tempOsceDay = new StringBuffer();
				StringBuffer tempAssignedRole = new StringBuffer();

				if (patientInSemesterProxy.getTrainings() != null) {

					for (Iterator<TrainingProxy> iterator = patientInSemesterProxy
							.getTrainings().iterator(); iterator.hasNext();) {
						TrainingProxy trainingProxy = (TrainingProxy) iterator
								.next();
						Log.info("TrainingProxy.getName()"
								+ trainingProxy.getName());
						if (trainingProxy != null) {
							if (tempTraining.toString().compareTo("") != 0) {
								tempTraining.append(" ,");
							}
							tempTraining.append(trainingProxy.getName());
						}

					}
				}

				// DateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
				if (patientInSemesterProxy.getOsceDays() != null) {
					for (Iterator<OsceDayProxy> iterator = patientInSemesterProxy
							.getOsceDays().iterator(); iterator.hasNext();) {
						OsceDayProxy osceDayProxy = (OsceDayProxy) iterator
								.next();
						Log.info("OsceDayProxy.getName()"
								+ osceDayProxy.getOsce().getName());
						if (osceDayProxy != null) {
							if (tempOsceDay.toString().compareTo("") != 0) {
								tempOsceDay.append(" ,");
							}
							tempOsceDay
									.append((osceDayProxy.getOsceDate() != null) ? // dateFormat.format
									(DateTimeFormat.getShortDateFormat()
											.format(osceDayProxy.getOsceDate()))
											: ""
													+ " - "
													+ ((osceDayProxy.getOsce() != null && osceDayProxy
															.getOsce()
															.getName() != null) ? osceDayProxy
															.getOsce()
															.getName() : ""));
						}

					}
				}
				if (patientInSemesterProxy.getPatientInRole() != null) {

					for (Iterator<PatientInRoleProxy> iterator = patientInSemesterProxy
							.getPatientInRole().iterator(); iterator.hasNext();) {
						PatientInRoleProxy patientInRoleProxy = (PatientInRoleProxy) iterator
								.next();
						Log.info("patientInRoleProxy.getOscePost()"
								+ patientInRoleProxy.getOscePost());
						if (patientInRoleProxy.getOscePost() != null) {
							if (tempAssignedRole.toString().compareTo("") != 0) {
								tempAssignedRole.append(" ,");
							}
							tempAssignedRole.append(patientInRoleProxy
									.getOscePost().getStandardizedRole()
									.getShortName());
						}

					}
				}

				if ((tempTraining.toString().compareTo("") == 0)
						&& (tempOsceDay.toString().compareTo("") == 0)
						&& (tempAssignedRole.toString().compareTo("") == 0)) {
					return;
				}

				RoleAssignmentPopupViewImpl.setPopUpText(
						tempTraining.toString(), tempOsceDay.toString(),
						tempAssignedRole.toString(), event.getClientX(),
						event.getClientY());

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
		// this.acceptedImage
		// .setResource(((patientInSemesterProxy.getAccepted() != null) ?
		// patientInSemesterProxy
		// .getAccepted().booleanValue() : false) ? uiIcons
		// .acceptedYesIcon() : uiIcons.acceptedNoIcon());

		acceptedIconBtn
				.setHTML(((patientInSemesterProxy.getAccepted() != null) ? patientInSemesterProxy
						.getAccepted().booleanValue() : false) ? OsMaConstant.CHECK_ICON
						: OsMaConstant.UNCHECK_ICON);

	}

	public String getRowSetColor() {
		return rowSetColor;
	}

	public void setRowSetColor(String rowSetColor) {
		this.rowSetColor = rowSetColor;
	}
}