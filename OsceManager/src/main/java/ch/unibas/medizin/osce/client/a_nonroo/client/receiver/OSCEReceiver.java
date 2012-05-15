package ch.unibas.medizin.osce.client.a_nonroo.client.receiver;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

@SuppressWarnings("deprecation")
public abstract class OSCEReceiver<T> extends Receiver<T> {

	@Override
	public void onFailure(ServerFailure error) {
		Log.error(error.getMessage());
		final String errorMsg=error.getMessage();
		final PopupPanel popupPanel=new PopupPanel();
		HorizontalPanel hp=new HorizontalPanel();
		
		Label msgLbl=new Label();
		msgLbl.setWidth("500px");
		popupPanel.setTitle(error.getExceptionType());
		msgLbl.setWordWrap(true);
		msgLbl.setText(errorMsg);
		
				//pop up hide
				
				popupPanel.add(msgLbl);
			
				hp.setSpacing(25);
				popupPanel.setWidth("300px");
				popupPanel.setHeight("150px");
				popupPanel.setAnimationEnabled(true);
				popupPanel.center();
				popupPanel.setGlassEnabled(true);
				
				hp.add(msgLbl);
				popupPanel.add(hp);
				RootPanel.get().add(popupPanel);
				
				popupPanel.show();
				
				
				
				Timer t = new Timer() {
					
					@Override
					public void run() {
						
						popupPanel.hide();
					}
					
				};
				t.schedule(5000);
				
				
	
		
		
		
		
		
	}

	@Override
	public abstract void onSuccess(T response);
	

}
