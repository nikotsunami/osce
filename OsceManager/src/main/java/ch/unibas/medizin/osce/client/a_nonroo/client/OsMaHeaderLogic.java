package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisFormPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.IndividualSchedulesPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.LogPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OsMaDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplatePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleAssignmentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguagePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SummoningsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.Locale;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;

public class OsMaHeaderLogic implements OsMaHeader.Delegate {
	private OsMaRequestFactory requestFactory;
	private PlaceController placeController;
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private List<BreadCrumb> breadCrumbs = new ArrayList<BreadCrumb>();
	private EnumRenderer<Operation> renderer = new EnumRenderer<Operation>();
	private Place currentPlace;
	
	@Inject
	public OsMaHeaderLogic(OsMaRequestFactory requestFactory, PlaceController placeController, EventBus eventBus) {
		this.requestFactory = requestFactory;
		this.placeController = placeController;
		
		PlaceChangeEventHandler eventHandler = new PlaceChangeEventHandler();
		eventBus.addHandler(PlaceChangeEvent.TYPE, eventHandler);
//		addNewPlace(placeController.getWhere());
		Log.debug("new OsMaHeaderLogic()");
	}
	
	private class BreadCrumb {
		private Place place;
		private String description;
		
		public BreadCrumb(Place place, String desc) {
			this.place = place;
			this.description = desc;
		}
		
		public Place getPlace() {
			return place;
		}
		
		public String getDescription() {
			return description;
		}
		
		public void setDescription(String desc) {
			this.description = desc;
		}
	}
	
	private class PlaceChangeEventHandler implements PlaceChangeEvent.Handler {
		@Override
		public void onPlaceChange(PlaceChangeEvent event) {
			Log.debug("onPlaceChange()");
			Place newPlace = event.getNewPlace();
			currentPlace = newPlace;
			removeOldPlace(newPlace);
			addNewPlace(newPlace);
		}
	}
	
	private class ObjectReceiver extends Receiver<Object> {
		private BreadCrumb crumb;
		
		public ObjectReceiver(BreadCrumb crumb) {
			this.crumb = crumb;
		}
		
		@Override
		public void onSuccess(Object response) {
			String desc = crumb.getDescription();
			if (response instanceof AdministratorProxy) {
				AdministratorProxy proxy = (AdministratorProxy) response;
				if (proxy.getPreName() != null && proxy.getName() != null) {
					desc += proxy.getPreName() + " " + proxy.getName();
				}
			} else if (response instanceof AnamnesisCheckProxy) {
				AnamnesisCheckProxy proxy = (AnamnesisCheckProxy) response;
				if (proxy.getText() != null) {
					desc += proxy.getText();
				}
			} else if (response instanceof AnamnesisFormProxy) {
				AnamnesisFormProxy proxy = (AnamnesisFormProxy) response;
				if (proxy.getId() != null) {
					desc += proxy.getId();
				}				
			} else if (response instanceof ClinicProxy) {
				ClinicProxy proxy = (ClinicProxy) response;
				if (proxy.getName() != null && proxy.getCity() != null) {
					desc += proxy.getName() + ", " + proxy.getCity();
				}
			} else if (response instanceof DoctorProxy) {
				DoctorProxy proxy = (DoctorProxy) response;
				if (proxy.getTitle() != null && proxy.getPreName() != null && proxy.getName() != null) {
					desc += proxy.getTitle() + " " + proxy.getPreName() + " " + proxy.getName();
				}
			} else if (response instanceof NationalityProxy) {
				NationalityProxy proxy = (NationalityProxy) response;
				if (proxy.getNationality() != null) {
					desc += proxy.getNationality();
				}
			} else if (response instanceof OsceProxy) {
				OsceProxy proxy = (OsceProxy) response;
				if (proxy.getId() != null) {
					desc += proxy.getId();
				}
			} else if (response instanceof ProfessionProxy) {
				ProfessionProxy proxy = (ProfessionProxy) response;
				if (proxy.getProfession() != null) {
					desc += proxy.getProfession();
				}
			} else if (response instanceof SpokenLanguageProxy) {
				SpokenLanguageProxy proxy = (SpokenLanguageProxy) response;
				if (proxy.getLanguageName() != null) {
					desc += proxy.getLanguageName();
				}
			} else if (response instanceof StandardizedPatientProxy) {
				StandardizedPatientProxy proxy = (StandardizedPatientProxy) response;
				if (proxy.getPreName() != null && proxy.getName() != null) {
					desc += proxy.getPreName() + " " + proxy.getName();
				}
			} else {
				Log.warn("unknown proxy: " + response.toString());
			}
			crumb.setDescription(desc);
			breadCrumbs.add(crumb);
			logBreadCrumbs();
		}
	}
	
	private void removeOldPlace(Place newPlace) {
		int indexOfLastCrumb = breadCrumbs.size() - 1;
		if (indexOfLastCrumb < 0) {
			return;
		}
		
		BreadCrumb lastCrumb = breadCrumbs.get(indexOfLastCrumb);
		if (lastCrumb == null) {
			return;
		}
		
		if (newPlace instanceof OsMaDetailsPlace) {
			if (lastCrumb.getPlace() instanceof OsMaDetailsPlace) {
				breadCrumbs.remove(indexOfLastCrumb);
			}
		} else {
			breadCrumbs.remove(indexOfLastCrumb--);
			if (indexOfLastCrumb >= 0) {
				breadCrumbs.remove(indexOfLastCrumb);
			}
		}
		logBreadCrumbs();
	}
	
	private void addDetailsPlace(OsMaDetailsPlace place) {
		String placeDescription;
		if (place.getOperation() == Operation.CREATE) {
			placeDescription = renderer.render(place.getOperation());
			breadCrumbs.add(new BreadCrumb((Place) place, placeDescription));
			logBreadCrumbs();
		} else {
			placeDescription = renderer.render(place.getOperation()) + ": ";
			requestFactory.find(place.getProxyId()).fire(new ObjectReceiver(new BreadCrumb((Place)place, placeDescription)));
		}
	}
	
	private void addRootPlace(Place place) {
		String placeDescription;
		if (place instanceof AdministratorPlace) {
			placeDescription = constants.user();
		} else if (place instanceof AnamnesisCheckPlace) {
			placeDescription = constants.anamnesisValues();
		} else if (place instanceof AnamnesisFormPlace) {
			placeDescription = "AnamnesisFormPlace";
		} else if (place instanceof BellSchedulePlace) {
			placeDescription = "BelLSchedulePlace";
		} else if (place instanceof CircuitPlace) {
			placeDescription = constants.circuit();
		} else if (place instanceof ClinicPlace) {
			placeDescription = constants.clinics();
		} else if (place instanceof DoctorPlace) {
			placeDescription = constants.doctors();
		} else if (place instanceof ExaminationSchedulePlace) {
			placeDescription = constants.examinationSchedule();
		} else if (place instanceof IndividualSchedulesPlace) {
			placeDescription = constants.printIndividualSchedules();
		} else if (place instanceof LogPlace) {
			placeDescription = constants.log();
		} else if (place instanceof NationalityPlace) {
			placeDescription = constants.nationalities();
		} else if (place instanceof OscePlace) {
			placeDescription = constants.osces();
		} else if (place instanceof ProfessionPlace) {
			placeDescription = constants.professions();
		} else if (place instanceof RoleAssignmentsPlace) {
			placeDescription = constants.roleAssignments();
		} else if (place instanceof RolePlace) {
			placeDescription = constants.roles();
		} else if (place instanceof RoomPlace) {
			placeDescription = constants.rooms();
		} else if (place instanceof ScarPlace) {
			placeDescription = constants.traits();
		} else if (place instanceof SpokenLanguagePlace) {
			placeDescription = constants.languages();
		} else if (place instanceof StandardizedPatientPlace) {
			placeDescription = constants.simulationPatients();
		} else if (place instanceof StudentsPlace) {
			placeDescription = constants.students();
		} else if (place instanceof SummoningsPlace) {
			placeDescription = constants.sendSummonings();
		}
		//By spec role management[
		else if (place instanceof TopicsAndSpecPlace) {
			placeDescription = constants.topicsAndSpec();
		}
		else if (place instanceof RoleScriptTemplatePlace) {
			placeDescription = constants.roleScriptTemplate();
		}
		else if (place instanceof RoomMaterialsPlace) {
			placeDescription = constants.roomMaterials();
		}
		else if (place instanceof RoleAssignmentPlace) {
			placeDescription = constants.simulationPatients();
		}
		
		//By spec Role Management]
		else {
			Log.warn("Unknown instance of place");
			placeDescription = "?";
		}
		breadCrumbs.add(new BreadCrumb(place, placeDescription));
		logBreadCrumbs();
	}

	private void addNewPlace(Place place) {
		if (place instanceof OsMaDetailsPlace) {
			addDetailsPlace((OsMaDetailsPlace) place);
		} else {
			addRootPlace(place);
		}
	}
	
	private void logBreadCrumbs() {
		if (Log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("BreadCrumbs: ");
			for (BreadCrumb element : breadCrumbs) {
				sb.append(element.getDescription());
				sb.append(" > ");
			}
			Log.debug(sb.toString());
		}
	}
	
	public void changeLocale(Locale locale) {
		int indexOfHash;
		String newLocaleString;
		String url = Location.getHref();
		
		url = url.replaceAll("locale=[a-z]{2,2}", "locale=" + locale.toString());
		if (url.indexOf("locale") < 0) {
			if (url.indexOf("?") > -1) {
				newLocaleString = "&locale=" + locale.toString();
			} else {
				newLocaleString = "?locale=" + locale.toString();
			}
			
			if ((indexOfHash = url.indexOf("#")) > -1) {
				url = url.substring(0, indexOfHash) + newLocaleString + url.substring(indexOfHash);
			} else {
				url = url + newLocaleString;
			}
		}
		Window.open(url, "_self", "");
	}

	@Override
	public void changeRecordValue(String val) {
	
		Log.info("~~Selected Record Value : " + val);
		//handlerManager.fireEvent(new RecordChangeEvent(val));
		requestFactory.getEventBus().fireEvent(new RecordChangeEvent(val));
		
	}
}
