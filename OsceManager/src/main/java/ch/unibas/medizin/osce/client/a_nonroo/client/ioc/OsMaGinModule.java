package ch.unibas.medizin.osce.client.a_nonroo.client.ioc;


import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.scaffold.ioc.ScaffoldModule;
import ch.unibas.medizin.osce.client.scaffold.request.EventSourceRequestTransport;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class OsMaGinModule extends ScaffoldModule {
	
	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(OsMaRequestFactory.class).toProvider(RequestFactoryProvider.class).in(Singleton.class);
		bind(PlaceController.class).toProvider(PlaceControllerProvider.class).in(Singleton.class);
	}

	static class PlaceControllerProvider implements Provider<PlaceController> {

		private final PlaceController placeController;

		@Inject
		public PlaceControllerProvider(EventBus eventBus) {
			this.placeController = new PlaceController(eventBus);
		}

		public PlaceController get() {
			return placeController;
		}
	}

	static class RequestFactoryProvider implements Provider<OsMaRequestFactory> {

		private final OsMaRequestFactory requestFactory;

		@Inject
		public RequestFactoryProvider(EventBus eventBus) {
			requestFactory = GWT.create(OsMaRequestFactory.class);
			requestFactory.initialize(eventBus, new EventSourceRequestTransport(
					eventBus));
		}

		public OsMaRequestFactory get() {
			return requestFactory;
		}
	}

}
