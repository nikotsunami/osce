package ch.unibas.medizin.osce.client.style.resources;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.SimplePager;

public interface MySimplePagerResources extends SimplePager.Resources {
	@Source("MySimplePager.css")
	SimplePager.Style simplePagerStyle();
	
	@Source("images/fastForward.png")
	ImageResource simplePagerFastForward();
	
	@Source("images/fastForwardDisabled.png")
	ImageResource simplePagerFastForwardDisabled();
	
	@Source("images/jumpStart.png")
	ImageResource simplePagerFirstPage();
	
	@Source("images/jumpStartDisabled.png")
	ImageResource simplePagerFirstPageDisabled();
	
	@Source("images/jumpEnd.png")
	ImageResource simplePagerLastPage();
	
	@Source("images/jumpEndDisabled.png")
	ImageResource simplePagerLastPageDisabled();
	
	@Source("images/forward.png")
	ImageResource simplePagerNextPage();
	
	@Source("images/forwardDisabled.png")
	ImageResource simplePagerNextPageDisabled();
	
	@Source("images/back.png")
	ImageResource simplePagerPreviousPage();
	
	@Source("images/backDisabled.png")
	ImageResource simplePagerPreviousPageDisabled();
}
