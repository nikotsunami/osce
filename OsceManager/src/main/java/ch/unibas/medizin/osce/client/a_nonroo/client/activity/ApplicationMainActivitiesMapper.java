package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExportOscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImportObjectiveViewPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ImporteOSCEPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.LogPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.PaymentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplatePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentManagementPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class ApplicationMainActivitiesMapper implements AsyncActivityMapper {

	public static HandlerManager handler;
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	public static SemesterProxy semesterProxy;
	public ActivityCallbackHandler callbackHandler;
	@Inject
	public ApplicationMainActivitiesMapper(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	@Override
	public void getActivity(Place place ,final ActivityCallbackHandler callbackHandler) {
		Log.debug("ApplicationMainActivitiesMapper.getActivity"
				+ placeController.getWhere());
		this.callbackHandler=callbackHandler;
		
		if (place instanceof StandardizedPatientPlace) {
			Log.debug("is StandardizedPatientPlace");
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new StandardizedPatientActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide StandardizedPatientActivity");
				}
			});
			
		}

		if (place instanceof ScarPlace) {
			Log.debug("is ScarPlace");
			//return new ScarActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new ScarActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide ScarActivity");
				}
			});
		}

		if (place instanceof RoomPlace) {
			Log.debug("is RoomPlace");
			//return new RoomActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new RoomActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide RoomActivity");
				}
			});
		}

		if (place instanceof LogPlace) {
			Log.debug("is LogPlace");
			//return new LogActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new LogActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide LogActivity");
				}
			});
		}

		if (place instanceof AnamnesisCheckPlace) {
			final Place places =place;
			Log.debug("is AnamnesisCheckPlace");
			//return new AnamnesisCheckActivity(requests, placeController,(AnamnesisCheckPlace) place);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new AnamnesisCheckActivity(requests, placeController,((AnamnesisCheckPlace) places)));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide AnamnesisCheckActivity");
				}
			});
		}

		if (place instanceof ClinicPlace) {
			Log.debug("is ClinicPlace");
			//return new ClinicActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new ClinicActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide ClinicActivity");
				}
			});
		}

		if (place instanceof DoctorPlace) {
			Log.debug("is DoctorPlace");
			//return new DoctorActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new DoctorActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide DoctorActivity");
				}
			});
		}

		if (place instanceof AdministratorPlace) {
			Log.debug("is AdministratorPlace");
			//return new AdministratorActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new AdministratorActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide AdministratorActivity");
				}
			});
		}

		if (place instanceof NationalityPlace) {
			Log.debug("is NationalityPlace");
			//return new NationalityActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new NationalityActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide NationalityActivity");
				}
			});
		}

		if (place instanceof SpokenLanguagePlace) {
			Log.debug("is SpokenLanguagePlace");
			//return new SpokenLanguageActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new SpokenLanguageActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide SpokenLanguageActivity");
				}
			});
		}

		if (place instanceof ProfessionPlace) {
			Log.debug("is ProfessionPlace");
			//return new ProfessionActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new ProfessionActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide ProfessionActivity");
				}
			});
		}
		
		/*
		 * if (place instanceof OscePlace) { Log.debug("is OscePlace"); return
		 * new OsceActivity(requests, placeController); }
		 */

		// G: SPEC START =

		if (place instanceof OscePlace) {
			Log.debug("is OscePlace");
			final Place places =place;
			/*OscePlace oscePlace = (OscePlace) place;
			if (oscePlace.handler == null) {
				oscePlace.handler = handler;
			}

			if (oscePlace.semesterProxy == null) {
				oscePlace.semesterProxy = semesterProxy;
			}*/

			//return new OsceActivity(requests, placeController,(OscePlace) place);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					OscePlace oscePlace = (OscePlace) places;
					if (oscePlace.handler == null) {
						oscePlace.handler = handler;
					}

					if (oscePlace.semesterProxy == null) {
						oscePlace.semesterProxy = semesterProxy;
					}
					
					callbackHandler.onRecieveActivity(new OsceActivity(requests, placeController,(OscePlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide OsceActivity");
				}
			});
		}

		// G: SPEC END =

		if (place instanceof CircuitPlace) {
			Log.debug("is CircuitPlace");

			final Place places =place;
			/*if (CircuitPlace.handler == null) {
				CircuitPlace.handler = handler;
			}

			if (CircuitPlace.semesterProxy == null) {
				CircuitPlace.semesterProxy = semesterProxy;
			}*/

			// return new CircuitActivity(requests, placeController,(CircuitPlace) place);

			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					if (CircuitPlace.handler == null) {
						CircuitPlace.handler = handler;
					}

					if (CircuitPlace.semesterProxy == null) {
						CircuitPlace.semesterProxy = semesterProxy;
					}
					callbackHandler.onRecieveActivity(new CircuitActivity(requests, placeController,(CircuitPlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide CircuitActivity");
				}
			});
		}

		if (place instanceof StudentsPlace) {
			Log.debug("is StudentsPlace");
			final Place places =place;
			/*StudentsPlace studentsPlace = (StudentsPlace) place;
			if (studentsPlace.handler == null) {
				studentsPlace.handler = handler;
			}

			if (studentsPlace.semesterProxy == null) {
				studentsPlace.semesterProxy = semesterProxy;
			}*/

			//return new StudentsActivity(requests, placeController,
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					StudentsPlace studentsPlace = (StudentsPlace) places;
					if (studentsPlace.handler == null) {
						studentsPlace.handler = handler;
					}

					if (studentsPlace.semesterProxy == null) {
						studentsPlace.semesterProxy = semesterProxy;
					}
					callbackHandler.onRecieveActivity(new StudentsActivity(requests, placeController,(StudentsPlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide StudentsActivity");
				}
			});
		}

		if (place instanceof ExaminationSchedulePlace) {
			Log.debug("is ExaminationSchedulePlace");
			
			final Place places = place;
			/*if (ExaminationSchedulePlace.handler == null) {
				ExaminationSchedulePlace.handler = handler;
			}

			if (ExaminationSchedulePlace.semesterProxy == null) {
				ExaminationSchedulePlace.semesterProxy = semesterProxy;
			}*/

			//return new ExaminationScheduleActivity(requests, placeController,(ExaminationSchedulePlace) place);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					if (ExaminationSchedulePlace.handler == null) {
						ExaminationSchedulePlace.handler = handler;
					}

					if (ExaminationSchedulePlace.semesterProxy == null) {
						ExaminationSchedulePlace.semesterProxy = semesterProxy;
					}
					
					callbackHandler.onRecieveActivity(new ExaminationScheduleActivity(requests, placeController,(ExaminationSchedulePlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide ExaminationScheduleActivity");
				}
			});

		}

		if (place instanceof SummoningsPlace) {
			Log.debug("is SummoningsPlace");
			// return new SummoningsActivity(requests, placeController);
			final Place places = place;
			/*SummoningsPlace summoningsPlace = (SummoningsPlace) place;
			if (summoningsPlace.handlerManager == null) {
				summoningsPlace.handlerManager = handler;
			}

			if (summoningsPlace.semesterProxy == null) {
				summoningsPlace.semesterProxy = semesterProxy;
			}*/

			//return new SummoningsActivity((SummoningsPlace) place, requests,placeController);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					SummoningsPlace summoningsPlace = (SummoningsPlace) places;
					if (summoningsPlace.handlerManager == null) {
						summoningsPlace.handlerManager = handler;
					}

					if (summoningsPlace.semesterProxy == null) {
						summoningsPlace.semesterProxy = semesterProxy;
					}
					
					callbackHandler.onRecieveActivity(new SummoningsActivity((SummoningsPlace) places, requests,placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide SummoningsActivity");
				}
			});
		}

		if (place instanceof IndividualSchedulesPlace) {
			Log.debug("is IndividualSchedulesPlace");
			// return new IndividualSchedulesActivity(requests,
			// placeController);
			// Module10 Create plans
			final Place places =place;
			/*IndividualSchedulesPlace individualSchedulesPlace = (IndividualSchedulesPlace) place;
			if (individualSchedulesPlace.handler == null) {
				individualSchedulesPlace.handler = handler;
			}

			if (individualSchedulesPlace.semesterProxy == null) {
				individualSchedulesPlace.semesterProxy = semesterProxy;
			}*/

			//return new IndividualSchedulesActivity(requests, placeController,(IndividualSchedulesPlace) place);
			// E Module10 Create plans
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					IndividualSchedulesPlace individualSchedulesPlace = (IndividualSchedulesPlace) places;
					if (individualSchedulesPlace.handler == null) {
						individualSchedulesPlace.handler = handler;
					}

					if (individualSchedulesPlace.semesterProxy == null) {
						individualSchedulesPlace.semesterProxy = semesterProxy;
					}
					
					callbackHandler.onRecieveActivity(new IndividualSchedulesActivity(requests, placeController,(IndividualSchedulesPlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide IndividualSchedulesActivity");
				}
			});
		}

		if (place instanceof StatisticalEvaluationPlace) {
			Log.debug("is StatisticalEvaluationPlace");
			final Place places =place;
			/*if (StatisticalEvaluationPlace.handler == null) {
				StatisticalEvaluationPlace.handler = handler;
			}

			if (StatisticalEvaluationPlace.semesterProxy == null) {
				StatisticalEvaluationPlace.semesterProxy = semesterProxy;
			}
*/
			//return new StatisticalEvaluationActivity(requests, placeController,(StatisticalEvaluationPlace) place);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					if (StatisticalEvaluationPlace.handler == null) {
						StatisticalEvaluationPlace.handler = handler;
					}

					if (StatisticalEvaluationPlace.semesterProxy == null) {
						StatisticalEvaluationPlace.semesterProxy = semesterProxy;
					}

					
					callbackHandler.onRecieveActivity(new StatisticalEvaluationActivity(requests, placeController,(StatisticalEvaluationPlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide StatisticalEvaluationActivity");
				}
			});
		}

		if (place instanceof BellSchedulePlace) {
			Log.debug("is BellSchedulePlace");
			final Place places =place;
			
			/*BellSchedulePlace bellSchedulePlace = (BellSchedulePlace) place;
			if (bellSchedulePlace.handler == null) {
				bellSchedulePlace.handler = handler;
			}

			if (bellSchedulePlace.semesterProxy == null) {
				bellSchedulePlace.semesterProxy = semesterProxy;
			}*/

			//return new BellScheduleActivity(requests, placeController,(BellSchedulePlace) place);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					BellSchedulePlace bellSchedulePlace = (BellSchedulePlace) places;
					if (bellSchedulePlace.handler == null) {
						bellSchedulePlace.handler = handler;
					}

					if (bellSchedulePlace.semesterProxy == null) {
						bellSchedulePlace.semesterProxy = semesterProxy;
					}
					
					callbackHandler.onRecieveActivity(new BellScheduleActivity(requests, placeController,(BellSchedulePlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide BellScheduleActivity");
				}
			});
		}

		if (place instanceof RolePlace) {
			Log.debug("is RolePlace");
			//return new RoleActivity(requests, placeController);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new RoleActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide RoleActivity");
				}
			});
		}

		if (place instanceof RoleAssignmentsPlace) {
			Log.debug("is RoleAssignmentsPlace");
			//
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new RoleAssignmentPatientInSemesterActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide RoleAssignmentPatientInSemesterActivity");
				}
			});
		}	
		// By Spec role management functionality[
		if (place instanceof TopicsAndSpecPlace) {
			Log.debug("is TopicsAndSpecPlace");
			//return new TopicsAndSpecActivity(requests, placeController);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new TopicsAndSpecActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide TopicsAndSpecActivity");
				}
			});

		}

		if (place instanceof RoleScriptTemplatePlace) {
			Log.debug("is RoleScriptTemplatePlace");
			//return new RoleScriptTemplateActivity(requests, placeController);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new RoleScriptTemplateActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide RoleScriptTemplateActivity");
				}
			});

		}

		if (place instanceof RoomMaterialsPlace) {
			Log.debug("is RoomMaterialsPlace");
			//return new RoomMaterialsActivity(requests, placeController);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new RoomMaterialsActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide RoomMaterialsActivity");
				}
			});
		}

		if (place instanceof RoleAssignmentPlace) {
			Log.debug("is SPRoleAssignmentPlace");
			final Place places=place;
			/*if (RoleAssignmentPlace.handler == null) {
				RoleAssignmentPlace.handler = handler;
			}

			if (RoleAssignmentPlace.semesterProxy == null) {
				RoleAssignmentPlace.semesterProxy = semesterProxy;
			}*/

			//return new RoleAssignmentPatientInSemesterActivity(requests,placeController, (RoleAssignmentPlace) place);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					if (RoleAssignmentPlace.handler == null) {
						RoleAssignmentPlace.handler = handler;
					}

					if (RoleAssignmentPlace.semesterProxy == null) {
						RoleAssignmentPlace.semesterProxy = semesterProxy;
					}
					
					callbackHandler.onRecieveActivity(new RoleAssignmentPatientInSemesterActivity(requests,placeController, (RoleAssignmentPlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide RoleAssignmentPatientInSemesterActivity");
				}
			});

		}
		// By Spec]

		// by learning objective
		if (place instanceof ImportObjectiveViewPlace) {
			Log.debug("is ImportObjectiveViewPlace");
			//return new ImportObjectiveViewActivity(requests, placeController);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new ImportObjectiveViewActivity(requests, placeController));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide ImportObjectiveViewActivity");
				}
			});


		}
		// by learning objective

		// by eosce
		if (place instanceof ImporteOSCEPlace) {
			Log.info("is ImporteOSCEPlace");
			final Place places=place;
			/*ImporteOSCEPlace importeOSCEPlace = (ImporteOSCEPlace) place;
			if (importeOSCEPlace.handlerManager == null) {
				importeOSCEPlace.handlerManager = handler;
			}

			if (importeOSCEPlace.semesterProxy == null) {
				importeOSCEPlace.semesterProxy = semesterProxy;
			}*/
			//return new ImporteOSCEActivity(requests, placeController, (ImporteOSCEPlace) place);
			
			//return new ImporteOSCEActivity(requests, placeController);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					ImporteOSCEPlace importeOSCEPlace = (ImporteOSCEPlace) places;
					if (importeOSCEPlace.handlerManager == null) {
						importeOSCEPlace.handlerManager = handler;
					}

					if (importeOSCEPlace.semesterProxy == null) {
						importeOSCEPlace.semesterProxy = semesterProxy;
					}
					
					callbackHandler.onRecieveActivity(new ImporteOSCEActivity(requests, placeController, (ImporteOSCEPlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide ImporteOSCEActivity");
				}
			});

		}
		// by eosce

		if (place instanceof ExportOscePlace) {
			Log.info("is ExportOscePlace");
			final Place places =place;
			/*ExportOscePlace exportOscePlace = (ExportOscePlace) place;
			if (exportOscePlace.handlerManager == null) {
				exportOscePlace.handlerManager = handler;
			}

			if (exportOscePlace.semesterProxy == null) {
				exportOscePlace.semesterProxy = semesterProxy;
			}*/
			//return new ExportOsceActivity(requests, placeController,(ExportOscePlace) place);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					ExportOscePlace exportOscePlace = (ExportOscePlace) places;
					if (exportOscePlace.handlerManager == null) {
						exportOscePlace.handlerManager = handler;
					}

					if (exportOscePlace.semesterProxy == null) {
						exportOscePlace.semesterProxy = semesterProxy;
					}
					
					callbackHandler.onRecieveActivity(new ExportOsceActivity(requests, placeController,(ExportOscePlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide ExportOsceActivity");
				}
			});

		}

		// payment
		if (place instanceof PaymentPlace) {
			Log.info("is PaymentPlace");
			 final Place places=place;
			/*PaymentPlace paymentPlace = (PaymentPlace) place;

			if(paymentPlace.handlerManager == null) {
				paymentPlace.handlerManager = handler;	
			}

			if(paymentPlace.semesterProxy == null) {
				paymentPlace.semesterProxy = semesterProxy;	
			}*/

			//return new PaymentActivity(requests, placeController,(PaymentPlace) place);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					
					PaymentPlace paymentPlace = (PaymentPlace) places;

					if(paymentPlace.handlerManager == null) {
						paymentPlace.handlerManager = handler;	
					}

					if(paymentPlace.semesterProxy == null) {
						paymentPlace.semesterProxy = semesterProxy;	
					}
					
					callbackHandler.onRecieveActivity(new PaymentActivity(requests, placeController,(PaymentPlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide PaymentActivity");
				}
			});

		}
		// payment
		if(place instanceof StudentManagementPlace){
			Log.info("is StudentManagementPlace");
			final Place places=place;
			//return new StudentManagmentActivity(requests, placeController, (StudentManagementPlace) place);
			
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					callbackHandler.onRecieveActivity(new StudentManagmentActivity(requests, placeController, (StudentManagementPlace) places));	
				}
				
				@Override
				public void onFailure(Throwable reason) {
					
					Window.alert("Not able to provide StudentManagmentActivity");
				}
			});
			
		}

		//return null;
	}

}
