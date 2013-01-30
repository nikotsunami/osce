package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.PaymentPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.PaymentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.PaymentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class PaymentActivity extends AbstractActivity implements PaymentView.Delegate, PaymentView.Presenter {

	private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private SemesterProxy semesterProxy;
	private HandlerManager handlerManager;
	private PaymentView view;
	private AcceptsOneWidget widget;
	private SelectChangeHandler removeHandler;
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";
	
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
		
		RecordChangeEvent.register(requests.getEventBus(), (PaymentViewImpl) view);
		MenuClickEvent.register(requests.getEventBus(), (PaymentViewImpl) view);
		
		this.addSelectChangeHandler(new SelectChangeHandler() {			
			@Override
			public void onSelectionChange(SelectChangeEvent event) {
				semesterProxy = event.getSemesterProxy();
				init();
				//System.out.println("SEMESTER ID : " + semesterProxy.getId());
			}
		});
		
		view.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
			
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				onRangeChanged();
			}
		});
		
		view.getTable().addColumnSortHandler(new ColumnSortEvent.Handler() {
			
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<StandardizedPatientProxy, String> col = (Column<StandardizedPatientProxy, String>) event.getColumn();				
				
				int index = view.getTable().getColumnIndex(col);
				sortname = view.getPaths().get(index-1);
				sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
				
				init();
			}
		});
		
		init();
		
		view.setDelegate(this);
	}
	
	public void init()
	{
		requests.assignmentRequestNonRoo().countStandardizedPatientBySemester(semesterProxy.getId()).fire(new OSCEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				view.getTable().setRowCount(response.intValue());
			}
		});
		
		requests.assignmentRequestNonRoo().findStandardizedPatientBySemester(0, OsMaConstant.TABLE_PAGE_SIZE, sortname, sortorder, semesterProxy.getId()).fire(new OSCEReceiver<List<StandardizedPatientProxy>>() {

			@Override
			public void onSuccess(List<StandardizedPatientProxy> response) {
				view.getTable().setRowData(response);				
			}
		});
	}
	
	public void addSelectChangeHandler(SelectChangeHandler handler) 
	{
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);	
		removeHandler=handler;
	}
	
	public void onRangeChanged()
	{
		final Range range = view.getTable().getVisibleRange();
		
		requests.assignmentRequestNonRoo().countStandardizedPatientBySemester(semesterProxy.getId()).fire(new OSCEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {
				view.getTable().setRowCount(response.intValue());
			}
		});
		
		requests.assignmentRequestNonRoo().findStandardizedPatientBySemester(range.getStart(), range.getLength(), sortname, sortorder, semesterProxy.getId()).fire(new OSCEReceiver<List<StandardizedPatientProxy>>() {

			@Override
			public void onSuccess(List<StandardizedPatientProxy> response) {
				view.getTable().setRowData(range.getStart(), response);
			}
		});
	}

	@Override
	public void exportButtonClicked() {
		requests.assignmentRequestNonRoo().findAssignmentByPatinetInRole(semesterProxy.getId()).fire(new OSCEReceiver<String>() {
			@Override
			public void onSuccess(String response) {
				System.out.println("~~SUCCESS~~");
				Window.open(response, "_blank", "enabled");
			}
		});
	}

	@Override
	public void printButtonClicked() {
		Iterator<StandardizedPatientProxy> itr = view.getMultiselectionModel().getSelectedSet().iterator();
		List<Long> stdPatIdList = new ArrayList<Long>();
		
		while (itr.hasNext())
		{
			StandardizedPatientProxy stdPat = itr.next();
			stdPatIdList.add(stdPat.getId());
		}
		
		//stdPatIdList is List of Selected Standardized Patient
		
		requests.standardizedPatientRequestNonRoo().setStandardizedPatientListToSession(stdPatIdList,sortname,sortorder).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				
				String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.STANDARDIZED_PATIENT_PAYMENT.ordinal()));
				String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal);
				Log.info("--> url is : " +url);
				Window.open(url, "", "");
				
			}
			
		});
	}
	
}
