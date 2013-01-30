package ch.unibas.medizin.osce.client.style.resources;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface AnamnesisQuestionTypeImages extends ClientBundle {
		@Source("images/questionOpen.png")
		public ImageResource questionOpen();
		@Source("images/questionYesNo.png")
		public ImageResource questionYesNo();
		@Source("images/questionMultM.png")
		public ImageResource questionMultM();
		@Source("images/questionMultS.png")
		public ImageResource questionMultS();
		@Source("images/title.png")
		public ImageResource title();
	}