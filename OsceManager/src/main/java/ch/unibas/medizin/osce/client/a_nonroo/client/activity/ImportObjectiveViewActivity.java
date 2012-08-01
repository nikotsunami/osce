package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ImportObjectiveView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ImportObjectiveViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class ImportObjectiveViewActivity extends AbstractActivity implements ImportObjectiveView.Presenter, ImportObjectiveView.Delegate {

	 private OsMaRequestFactory requests;
	 private PlaceController placeController;
	 private AcceptsOneWidget widget;
	 private ImportObjectiveView view; 
	 private HandlerRegistration rangeChangeHandler;
	 private ActivityManager activityManger;
	 
	
	public ImportObjectiveViewActivity(OsMaRequestFactory requests, PlaceController placeController)
	{
		this.requests = requests;
    	this.placeController = placeController;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		Log.info("SystemStartActivity.start()");
		ImportObjectiveView systemStartView = new ImportObjectiveViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		view.setDelegate(this);
	}

	@Override
	public void goTo(Place place) {
	}

}
