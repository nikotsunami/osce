package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsViewImpl;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

@SuppressWarnings("deprecation")
public class RoomMaterialsDetailsActivity  extends AbstractActivity implements RoomMaterialsDetailsView.Presenter,RoomMaterialsDetailsView.Delegate{

    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoomMaterialsDetailsView view;
	private RoomMaterialsDetailsPlace place;
	
	public RoomMaterialsDetailsActivity(RoomMaterialsDetailsPlace place,OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
	}
	
	public void onStop(){

	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoomMaterialsDetailsActivity.start()");
		RoomMaterialsDetailsView roomMaterialsDetailsView = new RoomMaterialsDetailsViewImpl();
		roomMaterialsDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = roomMaterialsDetailsView;
		
		
		widget.setWidget(roomMaterialsDetailsView.asWidget());
		
		view.setDelegate(this);
		roomMaterialsDetailsView.setDelegate(this);
		
		
		
	}
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);		
	}
}
