package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation.StatisticalEvaluationDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;



@SuppressWarnings("deprecation")
public class StatisticalEvaluationDetailsActivity extends AbstractActivity implements
StatisticalEvaluationDetailsView.Presenter, 
StatisticalEvaluationDetailsView.Delegate
{
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
		private StatisticalEvaluationDetailsView view;
		
		private final OsceConstants constants;
		
		StatisticalEvaluationDetailsActivity statisticalEvaluationDetailsActivity;		
			
		
		private StatisticalEvaluationDetailsPlace place;
		private StatisticalEvaluationDetailsActivity activity;
		

		public StatisticalEvaluationDetailsActivity(StatisticalEvaluationDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) 
		{
			Log.info("Call StatisticalEvaluationDetailsPlace(3arg)");
			this.place = place;
	    	this.requests = requests;
	    	this.placeController = placeController;
	    	this.activity=this;
	    	this.statisticalEvaluationDetailsActivity=this;
	    	constants = GWT.create(OsceConstants.class);		    	
	    }
		
		public void onStop()
		{
			
		}

		@Override
		public void start(AcceptsOneWidget panel, EventBus eventBus) 
		{
			Log.info("StatisticalEvaluationDetailsActivity.start()");
			final StatisticalEvaluationDetailsView statisticalEvaluationDetailsView = new StatisticalEvaluationDetailsViewImpl();			
			statisticalEvaluationDetailsView.setPresenter(this);			
			this.widget = panel;
			this.view = statisticalEvaluationDetailsView;
			widget.setWidget(statisticalEvaluationDetailsView.asWidget());			
			view.setDelegate(this);			
			statisticalEvaluationDetailsView.setDelegate(this);	
			
			MenuClickEvent.register(requests.getEventBus(), (StatisticalEvaluationDetailsViewImpl)view);
			
			ApplicationLoadingScreenEvent.initialCounter();
			ApplicationLoadingScreenEvent.register(requests.getEventBus(),
					new ApplicationLoadingScreenHandler() {
						@Override
						public void onEventReceived(
								ApplicationLoadingScreenEvent event) {
							Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
							event.display();
						}
					});						
			
			init();
			
		}
	
				
		private void init() 
		{
			Log.info("Init Call.");
		}
		
		
		@Override
		public void goTo(Place place) 
		{
			placeController.goTo(place);
			
		}
			

		
}
