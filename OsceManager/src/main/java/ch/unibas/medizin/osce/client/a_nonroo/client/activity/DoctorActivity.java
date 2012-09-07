package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.DoctorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.DoctorViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.managed.request.ClinicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class DoctorActivity extends AbstractActivity implements
DoctorView.Presenter, DoctorView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private DoctorView view;
	private CellTable<DoctorProxy> table;
	private SingleSelectionModel<DoctorProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private DoctorDetailsActivityMapper DoctorDetailsActivityMapper;
	private String quickSearchTerm = "";
	private HandlerRegistration placeChangeHandlerRegistration;
	

	public DoctorActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    	DoctorDetailsActivityMapper = new DoctorDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(DoctorDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);

		if (placeChangeHandlerRegistration != null) {
			placeChangeHandlerRegistration.removeHandler();
		}
	}
	
	public void registerLoading() {
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						Log.info(" ApplicationLoadingScreenEvent onEventReceived Called");
					event.display();
					}
				});
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		DoctorView systemStartView = new DoctorViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());
		
		MenuClickEvent.register(requests.getEventBus(), (DoctorViewImpl)view);
		
		//Issue # 122 : Replace pull down with autocomplete.
		//view.getFilterTitle().clear();
		//Issue # 122 : Replace pull down with autocomplete.
		initFilterTitleFill();
		initSearch();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<DoctorProxy> keyProvider = ((AbstractHasData<DoctorProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<DoctorProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						DoctorProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getName()
									+ " selected!");
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);
		placeChangeHandlerRegistration = eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			@Override
			public void onPlaceChange(PlaceChangeEvent event) {
				if (event.getNewPlace() instanceof DoctorDetailsPlace) {
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
					view.setDetailPanel(true);
					requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					DoctorDetailsPlace place = (DoctorDetailsPlace) event.getNewPlace();
					if (place.getOperation() == Operation.NEW) {
						initSearch();
						requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
					}
				}
				else{
					view.setDetailPanel(false);
				}
			}
		});
		
	}
	
	//Module 6 Start
	private void initFilterTitleFill()
	{
		requests.clinicRequest().findAllClinics().fire(new Receiver<List<ClinicProxy>>() {

			@Override
			public void onSuccess(List<ClinicProxy> resonse) {
				
				//Issue # 122 : Replace pull down with autocomplete.
				DefaultSuggestOracle<ClinicProxy> suggestOracle1 = (DefaultSuggestOracle<ClinicProxy>) view.getSuggestBox().getSuggestOracle();
				suggestOracle1.setPossiblilities(resonse);
				view.getSuggestBox().setSuggestOracle(suggestOracle1);

				view.getSuggestBox().setRenderer(new AbstractRenderer<ClinicProxy>() {

						@Override
						public String render(ClinicProxy object) {
							// TODO Auto-generated method stub
							if(object!=null)
							{
							return object.getName();
							}
							else
							{
								return "";
							}
						}
					});
				
				

				/*view.getFilterTitle().addItem("", "0");
				ClinicProxy temp = null;
				for (int i=0; i<resonse.size(); i++)
				{
					temp = resonse.get(i);
					view.getFilterTitle().addItem(temp.getName(), String.valueOf(temp.getId()));
				}
				*/
				//Issue # 122 : Replace pull down with autocomplete.
			}
			
		});
	}
	//Module 6 End
	
	private void initSearch() {
		
		if(rangeChangeHandler!=null){
			rangeChangeHandler.removeHandler();
			rangeChangeHandler=null;
		}

		fireCountRequest(quickSearchTerm, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Intitution aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(quickSearchTerm);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						DoctorActivity.this.onRangeChanged(quickSearchTerm);
					}
				});
	}
	
	protected void showDetails(DoctorProxy Doctor) {
		
		Log.debug(Doctor.getName());
		
		goTo(new DoctorDetailsPlace(Doctor.stableId(),
				Operation.DETAILS));
	}
	

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<DoctorProxy>> callback = new Receiver<List<DoctorProxy>>() {
			@Override
			public void onSuccess(List<DoctorProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					DoctorProxy Doctor = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<DoctorProxy> proxyId = (EntityProxyId<DoctorProxy>) Doctor
//							.stableId();
//
//				}
				table.setRowData(range.getStart(), values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(q, range, callback);

	}
	
	private void fireRangeRequest(String q, final Range range, final Receiver<List<DoctorProxy>> callback) {
		createRangeRequest(q, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<DoctorProxy>> createRangeRequest(String q, Range range) {
//		return requests.doctorRequest().findDoctorEntries(range.getStart(), range.getLength());
		return requests.doctorRequestNonRoo().findDoctorsBySearch(q, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String q, Receiver<Long> callback) {
//		requests.doctorRequest().countDoctors().fire(callback);
		requests.doctorRequestNonRoo().countDoctorsBySearch(q).fire(callback);
	}

	private void setTable(CellTable<DoctorProxy> table) {
		this.table = table;
		
	}

	@Override
	public void newClicked() {
		Log.info("create clicked");
		placeController.goTo(new DoctorDetailsPlace(Operation.CREATE));
	}
	
	@Override
	public void performSearch(String q) {
		quickSearchTerm = q;
		Log.debug("Search for " + q);
		initSearch();
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	//Module 6 Start
	
	//Issue # 122 : Replace pull down with autocomplete.
	@Override
	public void changeFilterTitleShown(ClinicProxy selectedTitle) {
	
		
		if (selectedTitle != null)
		{
		
			requests.doctorRequestNonRoo().findDoctorByClinicID(selectedTitle.getId()).with("office").fire(new OSCEReceiver<List<DoctorProxy>>() {

				@Override
				public void onSuccess(List<DoctorProxy> response) {
					view.getTable().setRowCount(response.size());
					view.getTable().setRowData(response);
				
				}
			});
		}
		else
		{
			requests.doctorRequest().findAllDoctors().with("office").fire(new OSCEReceiver<List<DoctorProxy>>() {

				@Override
				public void onSuccess(List<DoctorProxy> response) {
					view.getTable().setRowCount(response.size());
					view.getTable().setRowData(response);
				}
			});
		}
		
	}
	
/*	@Override
	public void changeFilterTitleShown() {
	
		Log.info("change event in changeFilterTitleShown method");
		if (view.getSuggestBox().getSelected()==null)
		{
		
			requests.doctorRequestNonRoo().findDoctorByClinicID(Long.parseLong("0")).with("office").fire(new OSCEReceiver<List<DoctorProxy>>() {

				@Override
				public void onSuccess(List<DoctorProxy> response) {
					view.getTable().setRowCount(response.size());
					view.getTable().setRowData(response);
				
	}
			});
		}
		else
		{
			requests.doctorRequest().findAllDoctors().with("office").fire(new OSCEReceiver<List<DoctorProxy>>() {

				@Override
				public void onSuccess(List<DoctorProxy> response) {
					view.getTable().setRowCount(response.size());
					view.getTable().setRowData(response);
				}
			});
		}
		
	}*/
	//Issue # 122 : Replace pull down with autocomplete.
	//Module 6 End

}
