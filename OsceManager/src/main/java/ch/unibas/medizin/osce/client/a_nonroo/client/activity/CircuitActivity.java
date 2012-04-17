package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.CircuitView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.CircuitViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * @author dk
 *
 */
public class CircuitActivity extends AbstractActivity implements CircuitView.Presenter, CircuitView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private CircuitView view;

	public CircuitActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    }

	public void onStop(){
		
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		CircuitView systemStartView = new CircuitViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		
		init();

		view.setDelegate(this);
	}
	
	private void init() {
		// TODO implement this!
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}
}
