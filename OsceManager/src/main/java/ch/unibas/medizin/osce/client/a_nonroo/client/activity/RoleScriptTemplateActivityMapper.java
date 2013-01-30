package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.managed.request.ApplicationRequestFactory;
import ch.unibas.medizin.osce.client.scaffold.place.ProxyPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class RoleScriptTemplateActivityMapper {

    private final ApplicationRequestFactory requests;

    private final PlaceController placeController;
    
    @Inject
    public RoleScriptTemplateActivityMapper(ApplicationRequestFactory requests, PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    public Activity getActivity(ProxyPlace place) {
        switch(place.getOperation()) {
            case DETAILS:
            	return null;//to do
            case EDIT:
                return makeEditActivity(place);
            case CREATE:
                return makeCreateActivity();
        }
        throw new IllegalArgumentException("Unknown operation " + place.getOperation());
    }



    private Activity makeCreateActivity() {
      
    	//todo
    	return null;
    }

    private Activity makeEditActivity(ProxyPlace place) {
    	//todo
    	return null;
    }
}
