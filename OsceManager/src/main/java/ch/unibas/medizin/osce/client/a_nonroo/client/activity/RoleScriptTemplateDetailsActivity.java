package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplateDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

@SuppressWarnings("deprecation")
public class RoleScriptTemplateDetailsActivity extends AbstractActivity implements
RoleScriptTemplateDetailsView.Presenter, 
RoleScriptTemplateDetailsView.Delegate{

    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleScriptTemplateDetailsView view;

	private RoleScriptTemplateDetailsPlace place;
	
	public RoleScriptTemplateDetailsActivity(RoleScriptTemplateDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    }
	
	public void onStop(){
			//todo
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleScriptTemplateDetailsActivity.start()");
		RoleScriptTemplateDetailsView roleScriptTemplateDetailsView = new RoleScriptTemplateDetailsViewImpl();
		roleScriptTemplateDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = roleScriptTemplateDetailsView;
		
		
		widget.setWidget(roleScriptTemplateDetailsView.asWidget());
		
		view.setDelegate(this);
		roleScriptTemplateDetailsView.setDelegate(this);
	}
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);		
	}
}
