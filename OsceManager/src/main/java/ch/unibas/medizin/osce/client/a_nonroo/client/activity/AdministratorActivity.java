package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AdministratorDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AdministratorView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AdministratorViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.Sorting;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class AdministratorActivity extends AbstractActivity implements
AdministratorView.Presenter, AdministratorView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AdministratorView view;
	//cell table changes
	/*private CellTable<AdministratorProxy> table;*/
	private AdvanceCellTable<AdministratorProxy> table;
	int x;
	int y;
	//cell table changes
	private SingleSelectionModel<AdministratorProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private AdministratorDetailsActivityMapper administratorDetailsActivityMapper;
	

	@Inject
	public AdministratorActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    	administratorDetailsActivityMapper = new AdministratorDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(administratorDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
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
		AdministratorView systemStartView = new AdministratorViewImpl();
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());
		
		
		//celltable changes start
		table.addHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				// TODO Auto-generated method stub
				Log.info("mouse down");
				x = event.getClientX();
				y = event.getClientY();

				if(table.getRowCount()>0)
				{
				Log.info(table.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

				
				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < table.getRowElement(0).getAbsoluteTop()) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

					Log.info("right event");
				}
				}
				else
				{
					if(event.getNativeButton() == NativeEvent.BUTTON_RIGHT)
					{
						table.getPopup().setPopupPosition(x, y);
						table.getPopup().show();
					}
				}
			}
		}, MouseDownEvent.getType());
		
		
		
		table.getPopup().addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				// TODO Auto-generated method stub
				//addColumnOnMouseout();
				table.getPopup().hide();
				
			}
		}, MouseOutEvent.getType());
		
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				// By SPEC[Start

				Column<AdministratorProxy, String> col = (Column<AdministratorProxy, String>) event.getColumn();
				
				
				int index = table.getColumnIndex(col);
				
				/*String[] path =	systemStartView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;	*/
				
				if (index % 2 == 1 ) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

				} 
			}
		});
		

		
		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (AdministratorViewImpl)view);
		//by spec
		
		MenuClickEvent.register(requests.getEventBus(), (AdministratorViewImpl)view);
		
		init();

		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<AdministratorProxy> keyProvider = ((AbstractHasData<AdministratorProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<AdministratorProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						AdministratorProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							view.setDetailPanel(true);
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							Log.debug(selectedObject.getEmail()
									+ " selected!");
							showDetails(selectedObject);
						}
						else{
							view.setDetailPanel(false);
						}
					}
				});

		view.setDelegate(this);
		
	}
	
	private void init() {

		String name = "Dani";
		requests.administratorRequest()
		.countAdministrators().fire(new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Intitution aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged();
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						AdministratorActivity.this.onRangeChanged();
					}
				});
	}
	
	protected void showDetails(AdministratorProxy administrator) {
		
		Log.debug(administrator.getEmail());
		
		goTo(new AdministratorDetailsPlace(administrator.stableId(),
				Operation.DETAILS));
		
	}
	

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<AdministratorProxy>> callback = new Receiver<List<AdministratorProxy>>() {
			@Override
			public void onSuccess(List<AdministratorProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
//				idToRow.clear();
//				idToProxy.clear();
//				for (int i = 0, row = range.getStart(); i < values.size(); i++, row++) {
//					AdministratorProxy administrator = values.get(i);
//					@SuppressWarnings("unchecked")
//					// Why is this cast needed?
//					EntityProxyId<AdministratorProxy> proxyId = (EntityProxyId<AdministratorProxy>) administrator
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

		fireRangeRequest(range, callback);

	}
	
	private void fireRangeRequest(final Range range,
			final Receiver<List<AdministratorProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<java.util.List<ch.unibas.medizin.osce.client.managed.request.AdministratorProxy>> createRangeRequest(
			Range range) {
		return requests.administratorRequest().findAdministratorEntries(range.getStart(), range.getLength());
	}


	private void setTable(CellTable<AdministratorProxy> table) {
		//this.table = table;
		//cell table changes
		this.table = (AdvanceCellTable<AdministratorProxy>)table;
	}

	@Override
	public void newClicked() {
		Log.info("new clicked");
		view.setDetailPanel(true);
		placeController.goTo(new AdministratorDetailsPlace(Operation.CREATE));
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

}
