package ch.unibas.medizin.osce.client.a_nonroo.client.activity;
/**
 * @author manish
 */
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;

public class AsyncCachingActivityMapper implements AsyncActivityMapper{

	private final AsyncActivityMapper wrapped;

	  private Place lastPlace;
	  private Activity lastActivity;

	  /**
	   * Constructs a CachingActivityMapper object.
	   *
	   * @param wrapped an ActivityMapper object
	   */
	  public AsyncCachingActivityMapper(AsyncActivityMapper wrapped) {
	    this.wrapped = wrapped;
	  }


	@Override
	public void getActivity(Place place,
			ActivityCallbackHandler activityCallbackHandler) {
		if (!place.equals(lastPlace)) {
		      lastPlace = place;
		      wrapped.getActivity(place,activityCallbackHandler);
		
		}
	}
}
