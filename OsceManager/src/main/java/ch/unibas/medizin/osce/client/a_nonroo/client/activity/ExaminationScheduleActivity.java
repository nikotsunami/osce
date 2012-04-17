package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ExaminationSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ExaminationScheduleView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ExaminationScheduleViewImpl;

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
public class ExaminationScheduleActivity extends AbstractActivity implements ExaminationScheduleView.Presenter, ExaminationScheduleView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private ExaminationScheduleView view;

	public ExaminationScheduleActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    }

	public void onStop(){
		
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		ExaminationScheduleView systemStartView = new ExaminationScheduleViewImpl();
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
