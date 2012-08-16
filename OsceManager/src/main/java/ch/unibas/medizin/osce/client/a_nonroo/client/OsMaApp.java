package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.unibas.medizin.osce.client.a_nonroo.client.activity.ApplicationMainActivitiesMapper;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.NationalityPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlaceHistoryFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.OscePlaceHistoryMapper;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ScarPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.managed.activity.ApplicationDetailsActivities;
import ch.unibas.medizin.osce.client.scaffold.request.RequestEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.CachingActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.requestfactory.client.RequestFactoryLogHandler;
import com.google.gwt.requestfactory.shared.LoggingRequest;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;

public class OsMaApp {

	private static final Logger log = Logger.getLogger(OsMaApp.class.getName());

	private final OsMaShell shell;
	private final OsMaRequestFactory requestFactory;
	private final EventBus eventBus;
	private final PlaceController placeController;
	private final OscePlaceHistoryFactory oscePlaceHistoryFactory;
	private final ApplicationMainActivitiesMapper applicationMainActivitiesMapper;
	private final ApplicationDetailsActivities applicationDetailsActivities;

	@Inject
	public OsMaApp(OsMaShell shell, OsMaRequestFactory requestFactory, EventBus eventBus,
	                          PlaceController placeController, OscePlaceHistoryFactory oscePlaceHistoryFactory,
	                          ApplicationMainActivitiesMapper applicationMainActivitiesMapper,
	                          ApplicationDetailsActivities applicationDetailsActivities) {
		Log.debug("OsceApp.OsceApp");
		this.shell = shell;
		this.requestFactory = requestFactory;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.oscePlaceHistoryFactory = oscePlaceHistoryFactory;
		this.applicationMainActivitiesMapper = applicationMainActivitiesMapper;
		this.applicationDetailsActivities = applicationDetailsActivities;
	}

	public void run() {
		
		Log.debug("OsceApp.run()");

		/* Add handlers, setup activities */

		init();

		/* Hide the loading message */

		Element loading = Document.get().getElementById("loading");
		loading.getParentElement().removeChild(loading);

		/* And show the user the shell */

		RootLayoutPanel.get().add(shell);

	}

	private void init() {

		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				Log.error("Error: " + e);
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		});

		if (LogConfiguration.loggingIsEnabled()) {
			// Add remote logging handler
			RequestFactoryLogHandler.LoggingRequestProvider provider = new RequestFactoryLogHandler.LoggingRequestProvider() {
				public LoggingRequest getLoggingRequest() {
					return requestFactory.loggingRequest();
				}
			};
			Logger.getLogger("").addHandler(
					new RequestFactoryLogHandler(provider, Level.WARNING,
							new ArrayList<String>()));
		}

		RequestEvent.register(eventBus, new RequestEvent.Handler() {
			// Only show loading status if a request isn't serviced in 250ms.
			private static final int LOADING_TIMEOUT = 250;

			public void onRequestEvent(RequestEvent requestEvent) {
				if (requestEvent.getState() == RequestEvent.State.SENT) {
					shell.getMole().showDelayed(LOADING_TIMEOUT);
				} else {
					shell.getMole().hide();
				}
			}
		});

		
		CachingActivityMapper cached = new CachingActivityMapper(applicationMainActivitiesMapper);
		FilterForMainPlaces filterForMainPlaces = new FilterForMainPlaces();
		ActivityMapper masterActivityMap = new FilteredActivityMapper(filterForMainPlaces, cached);
		final ActivityManager masterActivityManager = new ActivityManager(masterActivityMap, eventBus);

		masterActivityManager.setDisplay(shell.getMainPanel());

//		ProxyListPlacePicker proxyListPlacePicker = new ProxyListPlacePicker(placeController, proxyPlaceToListPlace);
//		HasConstrainedValue<ProxyListPlace> listPlacePickerView = shell.getPlacesBox();
//		listPlacePickerView.setAcceptableValues(getTopPlaces());
//		proxyListPlacePicker.register(eventBus, listPlacePickerView);

//		final ActivityManager detailsActivityManager = new ActivityManager(applicationDetailsActivities, eventBus);

//		detailsActivityManager.setDisplay(shell.getDetailsPanel());
		
		/* Browser history integration */
		OscePlaceHistoryMapper mapper = GWT.create(OscePlaceHistoryMapper.class);
		mapper.setFactory(oscePlaceHistoryFactory);
		PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(mapper);
		placeHistoryHandler.register(placeController, eventBus, new StandardizedPatientPlace());
		
		OsMaMainNav nav = new OsMaMainNav(requestFactory, placeController,placeHistoryHandler);
		
		
		
		OsMaHeader header = new OsMaHeaderImpl();
		header.setDelegate(new OsMaHeaderLogic(requestFactory, placeController, eventBus));
		
		shell.setNavigation(nav);
		shell.setHeader(header);


	
//		ProxyListPlace defaultPlace = getTopPlaces().iterator().next();
		
		//placeHistoryHandler.handleCurrentHistory();
	}

}
