package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.Paging;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.EditPopViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.NationalityView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.NationalityViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityRequest;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class NationalityActivity extends AbstractActivity implements
NationalityView.Presenter, NationalityView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private NationalityView view;
	private CellTable<NationalityProxy> table;
	private SingleSelectionModel<NationalityProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private NationalityDetailsActivityMapper nationalityDetailsActivityMapper;
	
	Boolean flag = false;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public NationalityActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	nationalityDetailsActivityMapper = new NationalityDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(nationalityDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		NationalityView systemStartView = new NationalityViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (NationalityViewImpl) view);
		//by spec
		
		MenuClickEvent.register(requests.getEventBus(), (NationalityViewImpl) view);
		
		setInserted(false);
		init();

//		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<NationalityProxy> keyProvider = ((AbstractHasData<NationalityProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<NationalityProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						NationalityProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getNationality()
									+ " selected!");
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);
		
	}
	
	private void init() {
		init2("");
	}
	
	private void init2(final String q) {

		fireCountRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Nationalitäten aus der Datenbank: " + response);
				
				totalRecords = response.intValue();
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						NationalityActivity.this.onRangeChanged(q);
					}
				});
	}
	
	protected void showDetails(NationalityProxy nationality) {
		
		Log.debug(nationality.getNationality());
		
		//goTo(new NationalityDetailsPlace(nationality.stableId(),
		//		Operation.DETAILS));
	}
	

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<NationalityProxy>> callback = new Receiver<List<NationalityProxy>>() {
			@Override
			public void onSuccess(List<NationalityProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				table.setRowData(range.getStart(), values);

				if(isInserted){
					
					int start = Paging.getLastPageStart(range.getLength(), totalRecords);
					table.setPageStart(start);
					setInserted(false);
				}
				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(q, range, callback);

	}
	
	private void fireRangeRequest(String name, final Range range, final Receiver<List<NationalityProxy>> callback) {
		createRangeRequest(name, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<NationalityProxy>> createRangeRequest(String name, Range range) {
//		return requests.nationalityRequest().findNationalityEntries(range.getStart(), range.getLength());
		return requests.nationalityRequestNonRoo().findNationalitiesByName(name, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
//		requests.nationalityRequest().countNationalitys().fire(callback);
		requests.nationalityRequestNonRoo().countNationalitiesByName(name).fire(callback);
	}

	private void setTable(CellTable<NationalityProxy> table) {
		this.table = table;
		
	}

	private int totalRecords;
	private boolean isInserted;
	public boolean isInserted() {
		return isInserted;
	}

	public void setInserted(boolean isInserted) {
		this.isInserted = isInserted;
	}
	
	@Override
	public void newClicked(final String name) {
		Log.debug("Add nationality");
		requests.nationalityRequestNonRoo().checkNationnality(name).fire(new OSCEReceiver<Integer>() {
			@Override
			public void onSuccess(Integer response) {
				if (response == 0)
				{
					NationalityRequest nationReq = requests.nationalityRequest();
					NationalityProxy nation = nationReq.create(NationalityProxy.class);
					nation.setNationality(name);
					// Highlight onViolation
					Log.info("Map Size: " + view.getNationalityNewMap().size());
					nationReq.persist().using(nation).fire(new OSCEReceiver<Void>(view.getNationalityNewMap()){
					// E Highlight onViolation
						@Override
						public void onSuccess(Void arg0) {
							setInserted(true);
							init();
						}
					});
				}
				else
				{
					MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
		    		messageConfirmationDialogBox.showConfirmationDialog(constants.nationaltiywarning());
				}
			}
		});
	}
	
	@Override
	public void deleteClicked(NationalityProxy nation) {
		// Highlight onViolation
		requests.nationalityRequest().remove().using(nation).fire(new OSCEReceiver<Void>() {
		// Highlight onViolation
			public void onSuccess(Void ignore) {
				Log.debug("Sucessfully deleted");
				setInserted(false);
				init();
			}
		});
	}
	
	@Override
	public void performSearch(String q) {
		Log.debug("Search for " + q);
		setInserted(false);
		init2(q);
	}

	@Override
	public void goTo(Place place) {
		placeControler.goTo(place);
		
	}

	@Override
	public void updateClicked(NationalityProxy nation, String value) 
	{
		NationalityRequest nationrequest = requests.nationalityRequest();
		nation = nationrequest.edit(nation);
		nation.setNationality(value);
		// Highlight onViolation
		Log.info("Map Size: " + view.getNationalityMap().size());
		nationrequest.persist().using(nation).fire(new OSCEReceiver<Void>(view.getNationalityMap()) 
		// E Highlight onViolation
		{

			@Override
			public void onSuccess(Void response) {
				((EditPopViewImpl)view.getEditPopupView()).hide();
				setInserted(false);
				init();
				
			}
		});
		
		
	}
}
