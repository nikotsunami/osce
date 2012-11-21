package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StudentManagementDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentManagementDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.StudentManagementDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.util;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.gargoylesoftware.htmlunit.html.Util;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.activity.shared.AbstractActivity;

@SuppressWarnings("deprecation")
public class StudentManagementDetailsActivity extends AbstractActivity implements StudentManagementDetailsView.Delegate,StudentManagementDetailsView.Presenter{

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StudentManagementDetailsView view;
	private StudentProxy studentProxy;
	public List<String> path = new ArrayList<String>();
	
	private SingleSelectionModel<OsceProxy> selectionModel;
	
	private CellTable<OsceProxy> table;
	
	private StudentManagementDetailsActivity studentManagementDetailsActivity;
	
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);	
	private StudentManagementDetailsPlace place;
	
	public StudentManagementDetailsView studentManagementDetailsViewImpl = new StudentManagementDetailsViewImpl();
	public StudentManagementDetailsViewImpl studentDetailsViewImpl = (StudentManagementDetailsViewImpl)studentManagementDetailsViewImpl;
	public StudentManagementDetailsActivity(
			StudentManagementDetailsPlace place, OsMaRequestFactory requests,
		PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public void onStop() {
	
	}

	
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("StudentManagementDetailsActivity.start()");
//		RoleScriptTemplateDetailsView roleScriptTemplateDetailsView = new RoleScriptTemplateDetailsViewImpl();
		studentManagementDetailsViewImpl.setPresenter(this);
		studentManagementDetailsActivity=this;
		this.widget = panel;
		this.view = studentManagementDetailsViewImpl;

		widget.setWidget(studentManagementDetailsViewImpl.asWidget());
		
		this.table = view.getTable();
		
		view.setDelegate(this);
		
		path = studentManagementDetailsViewImpl.getPaths();
		
		 ProvidesKey<OsceProxy> keyProvider = ((AbstractHasData<OsceProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<OsceProxy>(keyProvider);
		table.setSelectionModel(selectionModel);
		
		requests.find(place.getProxyId()).with("osceStudents","studentOsces").fire(new OSCEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				
				if(response instanceof StudentProxy && response !=null){
					studentProxy=(StudentProxy)response;
					
					studentManagementDetailsViewImpl.setStudentProxy(studentProxy);
					studentDetailsViewImpl.Name.setText(util.getEmptyIfNull(studentProxy.getName()));
					studentDetailsViewImpl.Prename.setText(util.getEmptyIfNull(studentProxy.getPreName()));
					studentDetailsViewImpl.Street.setText(util.getEmptyIfNull(studentProxy.getStreet()));
					studentDetailsViewImpl.City.setText(util.getEmptyIfNull(studentProxy.getCity()));
					studentDetailsViewImpl.Gender.setText(util.getEmptyIfNull(studentProxy.getGender()==null?null:studentProxy.getGender().name().toLowerCase()));

//					studentDetailsViewImpl.Gender.setText(util.getEmptyIfNull(studentProxy.getGender().name().toLowerCase()));
					studentDetailsViewImpl.Email.setText(util.getEmptyIfNull(studentProxy.getEmail()));
					
					showOsceParticipation(studentProxy);
					
				}
				
			}
		});
		
	}
	
	public void showOsceParticipation(StudentProxy studentProxy){
		
		requests.studentRequestNonRoo().findOsceBasedOnStudent(studentProxy.getId()).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() {

			@Override
			public void onSuccess(List<OsceProxy> response) {
			
				view.getTable().setRowCount(response.size(), true);
				
				table.setRowData(table.getVisibleRange().getStart(), response);
			}
		});
	}
	private void init() {	
		
	}

	@Override
	public void printCheckList(OsceProxy osceProxy,StudentProxy studentProxy) 
	{
		Log.info("printCheckList Call");
		Log.info("Osce: " + osceProxy.getId()+" Student: " + studentProxy.getId());
		Log.info("Standardized Role: ");
		
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();	
		StringBuilder requestData = new StringBuilder();
		String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.STUDENT_MANAGEMENT.ordinal()));
		requestData.append(ResourceDownloadProps.ENTITY).append("=").append(ordinal).append("&")
					.append(ResourceDownloadProps.ID).append("=").append(URL.encodeQueryString(studentProxy.getId().toString())).append("&")
					.append(ResourceDownloadProps.OSCE_ID).append("=").append(URL.encodeQueryString(osceProxy.getId().toString())).append("&");
					
		requestData.append(ResourceDownloadProps.LOCALE).append("=").append(URL.encodeQueryString(locale));
		
		String url = GWT.getHostPageBaseURL() + "downloadFile?" + requestData.toString(); 
		Log.info("--> url is : " +url);
		Window.open(url, "", "");
		
		
	}

	/**
	 * Used as a callback for the request that gets the @StandardizedPatientProxy
	 * that is edited in this activities instance.
	 */
/*
	private class InitializeActivityReceiver extends Receiver<Object> {
		@Override
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
			Log.info("Error in InitializeActivityReceiver");
		}

		@Override
		public void onSuccess(Object response) {
			Log.info("sucess in InitializeActivityReceiver");
			if (response instanceof StudentProxy) {
				
				Log.info(((StudentProxy) response).getName());
				studentProxy = (StudentProxy) response;
				// init();
			}
		}
	}

*/
}

