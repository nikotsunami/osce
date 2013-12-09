package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import com.google.gwt.place.shared.Place;

/**
*
* @author manish
*/
public interface AsyncActivityMapper{
	
	void getActivity(Place place, ActivityCallbackHandler activityCallbackHandler);

}
