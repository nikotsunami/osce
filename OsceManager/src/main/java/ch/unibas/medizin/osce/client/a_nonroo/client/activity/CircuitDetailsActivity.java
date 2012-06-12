package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitOsceSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitOsceSubViewImpl;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.shared.OsceStatus;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;


@SuppressWarnings("deprecation")
public class CircuitDetailsActivity extends AbstractActivity implements
CircuitDetailsView.Presenter, 
CircuitDetailsView.Delegate,CircuitOsceSubView.Delegate {

		private OsMaRequestFactory requests;
		private PlaceController placeController;
		private AcceptsOneWidget widget;
		private CircuitDetailsView view;
		
		private CircuitDetailsPlace place;
		private CircuitDetailsActivity activity;
		public CircuitDetailsActivity(CircuitDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
			this.place = place;
	    	this.requests = requests;
	    	this.placeController = placeController;
	    	this.activity=this;
	    	
	    }
		
		public void onStop(){

		}

		@Override
		public void start(AcceptsOneWidget panel, EventBus eventBus) {
			Log.info("CircuitDetailsActivity.start()");
			final CircuitDetailsView circuitDetailsView = new CircuitDetailsViewImpl();
			circuitDetailsView.setPresenter(this);
			this.widget = panel;
			this.view = circuitDetailsView;
			widget.setWidget(circuitDetailsView.asWidget());
			
			view.setDelegate(this);
			
			circuitDetailsView.setDelegate(this);
			
			//spec Start==
			
			requests.find(place.getProxyId()).with("osces").fire(new OSCEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if(response instanceof OsceProxy && response != null){
						
						Log.info("Arrived OsceProxy At CircuitDetailActivity");
						CircuitOsceSubViewImpl circuitOsceSubViewImpl=view.getcircuitOsceSubViewImpl();
						
						OsceStatus status = ((OsceProxy) response).getOsceStatus();
						
						String style = status.getOsceStatus(status);
						
						circuitOsceSubViewImpl.setStyleName(style);
						circuitOsceSubViewImpl.shortBreakTextBox.setText(((OsceProxy) response).getShortBreak().toString());
						circuitOsceSubViewImpl.longBreakTextBox.setText(((OsceProxy) response).getLongBreak().toString());
						circuitOsceSubViewImpl.launchBreakTextBox.setText(((OsceProxy) response).getLunchBreak().toString());
						circuitOsceSubViewImpl.maxStudentTextBox.setText(((OsceProxy) response).getMaxNumberStudents().toString());
						circuitOsceSubViewImpl.maxParcourTextBox.setText(((OsceProxy) response).getNumberCourses().toString());
						circuitOsceSubViewImpl.maxRoomsTextBox.setText(((OsceProxy) response).getNumberRooms().toString());
						circuitOsceSubViewImpl.setProxy((OsceProxy)response);
						circuitOsceSubViewImpl.setDelegate(activity);
					}
					
				}
			});
			
			// spec End==
		}
		
		@Override
		public void goTo(Place place) {
			placeController.goTo(place);		
		}

		@Override
		public void saveOsceData(OsceProxy osceProxy) {
			CircuitOsceSubViewImpl circuitOsceSubViewImp = view.getcircuitOsceSubViewImpl();
			OsceRequest osceReq = requests.osceRequest();
			osceProxy = osceReq.edit(osceProxy);
			
			osceProxy.setShortBreak(circuitOsceSubViewImp.shortBreakTextBox.getValue());
			osceProxy.setLongBreak(circuitOsceSubViewImp.longBreakTextBox.getValue());
			osceProxy.setLunchBreak(circuitOsceSubViewImp.launchBreakTextBox.getValue());
			osceProxy.setMaxNumberStudents(circuitOsceSubViewImp.maxStudentTextBox.getValue());
			osceProxy.setNumberCourses(circuitOsceSubViewImp.maxParcourTextBox.getValue());
			osceProxy.setNumberRooms(circuitOsceSubViewImp.maxRoomsTextBox.getValue());
			
			osceReq.persist().using(osceProxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("Osce Value Updated");
					Window.alert("Osce Data Updated sucessfully");
					
				}
			});
		}
}
