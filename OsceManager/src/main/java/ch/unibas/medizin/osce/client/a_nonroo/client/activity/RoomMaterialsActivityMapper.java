package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.managed.activity.StandardizedPatientDetailsActivity;
import ch.unibas.medizin.osce.client.managed.activity.StandardizedPatientEditActivityWrapper;
import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientRequest;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientEditView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientMobileDetailsView;
import ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientMobileEditView;
import ch.unibas.medizin.osce.client.scaffold.ScaffoldApp;
import ch.unibas.medizin.osce.client.scaffold.place.CreateAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.FindAndEditProxy;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.inject.Inject;

public class RoomMaterialsActivityMapper {

	   private final ApplicationRequestFactory requests;

	    private final PlaceController placeController;
	    
	   @Inject
	   public RoomMaterialsActivityMapper(ApplicationRequestFactory requests, PlaceController placeController) {
		   this.requests = requests;
	        this.placeController = placeController;
	   }
	   
	    public Activity getActivity(ProxyPlace place) {
	        switch(place.getOperation()) {
	            case DETAILS:
	                 return null;
	            case EDIT:
	                return makeEditActivity(place);
	            case CREATE:
	                return makeCreateActivity();
	        }
	        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
	    }
	    
	    private Activity makeCreateActivity() {
	       return null;
	    }
	    private Activity makeEditActivity(ProxyPlace place) {
	    	return null;
	    }
}
