package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import com.google.gwt.user.client.ui.IsWidget;

public interface PaymentView extends IsWidget {

	 public interface Presenter {
	 
	 }
	 
	 void setDelegate(Delegate delegate);

	 void setPresenter(Presenter systemStartActivity);

	 interface Delegate {
		 void printRecord();
	 }	    
}
