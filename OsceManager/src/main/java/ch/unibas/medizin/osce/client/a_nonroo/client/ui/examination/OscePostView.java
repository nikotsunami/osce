package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface OscePostView extends IsWidget{
	
	interface Delegate {
		public void deleteOscePost(OscePostView view);
		
		void deletePostClicked(OscePostViewImpl oscePostViewImpl);		
	}
	
	 void setDelegate(Delegate delegate);
	 
	 public Label getPostTypeLbl();
	 
	 public HorizontalPanel getOscePostSubViewHP() ;
	 
	 public OscePostProxy getProxy();
	 
	 public void setProxy(OscePostProxy proxy);
	 
	 public boolean isAnemanis();
	 
	 public void setAnemanis(boolean isAnemanis);
	 
	 public OscePostProxy getNextOscePostProxy();
	 
	 public void setNextOscePostProxy(OscePostProxy nextOscePostProxy);
	 
	 public OscePostBlueprintProxy getOscePostBlueprintProxy() ;
	 
	 public void setOscePostBlueprintProxy(
				OscePostBlueprintProxy oscePostBlueprintProxy);
	 
	 public OscePostBlueprintProxy getOscePostBlueprintProxyNext();
	 
	 public void setOscePostBlueprintProxyNext(
				OscePostBlueprintProxy oscePostBlueprintProxyNext);
}
