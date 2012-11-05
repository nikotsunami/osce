package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import javax.swing.AbstractAction;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.PaymentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StatisticalEvaluationPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.PaymentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.PaymentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class PaymentActivity extends AbstractActivity implements PaymentView.Delegate, PaymentView.Presenter {

	private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private SemesterProxy semesterProxy;
	private HandlerManager handlerManager;
	private PaymentView view;
	private AcceptsOneWidget widget;
	private SelectChangeHandler removeHandler;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	public PaymentActivity(OsMaRequestFactory requests, PlaceController placeController, PaymentPlace place) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	this.semesterProxy = place.semesterProxy;
    	this.handlerManager = place.handlerManager;
    }

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		PaymentView systemStartView = new PaymentViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		
		this.addSelectChangeHandler(new SelectChangeHandler() {			
			@Override
			public void onSelectionChange(SelectChangeEvent event) {
				semesterProxy = event.getSemesterProxy();
				
				System.out.println("SEMESTER ID : " + semesterProxy.getId());
			}
		});
		
		view.setDelegate(this);
	}
	
	public void addSelectChangeHandler(SelectChangeHandler handler) 
	{
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);	
		removeHandler=handler;
	}

	@Override
	public void printRecord() {
		requests.assignmentRequestNonRoo().findAssignmentByPatinetInRole(semesterProxy.getId()).fire(new OSCEReceiver<String>() {
			@Override
			public void onSuccess(String response) {
				System.out.println("~~SUCCESS~~");
				Window.open(response, "_blank", "enabled");
			}
		});
	}
	
}
