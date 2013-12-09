package ch.unibas.medizin.osce.client.a_nonroo.client.activity;
/**
 * @author manish
 */
import com.google.gwt.place.shared.Place;

public class AsyncFilteredActivityMapper implements AsyncActivityMapper {

	/**
	   * Implemented by objects that want to interpret one place as another.
	   */
	  public interface Filter {
	    /**
	     * Returns the filtered interpretation of the given {@link Place}.
	     *
	     * @param place the input {@link Place}.
	     * @return the output {@link Place}.
	     */
	    Place filter(Place place);
	  }
	  
	  private final Filter filter;
	  private final AsyncActivityMapper wrapped;
	  public AsyncFilteredActivityMapper(Filter filter, AsyncActivityMapper wrapped ) {
		    this.filter = filter;
		    this.wrapped = wrapped;
		  }
	  
	@Override
	public void getActivity(Place place,
			ActivityCallbackHandler activityCallbackHandler) {
		wrapped.getActivity(filter.filter(place),activityCallbackHandler);
		
	}

}
