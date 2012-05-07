package ch.unibas.medizin.osce.client.a_nonroo.client.activity;



import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientAnamnesisSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientLangSkillSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientMediaSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubView;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

@SuppressWarnings("deprecation")
public class TopicsAndSpecDetailsActivity extends AbstractActivity implements
TopicsAndSpecDetailsView.Presenter, 
TopicsAndSpecDetailsView.Delegate
{
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private TopicsAndSpecDetailsView view;

	private TopicsAndSpecDetailsPlace place;
	
	public TopicsAndSpecDetailsActivity(TopicsAndSpecDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    }
	
	public void onStop(){

	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("TopicsAndSpecDetailsActivity.start()");
		TopicsAndSpecDetailsView topicsAndSpecDetailsView = new TopicsAndSpecDetailsViewImpl();
		topicsAndSpecDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = topicsAndSpecDetailsView;
		
		
		widget.setWidget(topicsAndSpecDetailsView.asWidget());
		
		view.setDelegate(this);
		topicsAndSpecDetailsView.setDelegate(this);
	}
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);		
	}
	
}
