package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.BellScheduleView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.BellScheduleViewImpl;

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
public class BellScheduleActivity extends AbstractActivity implements BellScheduleView.Presenter, BellScheduleView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private BellScheduleView view;

	public BellScheduleActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    }

	public void onStop(){
		
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		BellScheduleView systemStartView = new BellScheduleViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			public void onPlaceChange(PlaceChangeEvent event) {
				
				if (event.getNewPlace() instanceof BellSchedulePlace){
					init();
				}
			}
		});
		
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
