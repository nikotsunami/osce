package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.Paging;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.SpokenLanguageDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.EditPopViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SpokenLanguageView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.SpokenLanguageViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageRequest;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.shared.Operation;

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

public class SpokenLanguageActivity extends AbstractActivity implements
SpokenLanguageView.Presenter, SpokenLanguageView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeControler;
	private AcceptsOneWidget widget;
	private SpokenLanguageView view;
	
	//cell table changes
	//private CellTable<SpokenLanguageProxy> table;
	private AdvanceCellTable<SpokenLanguageProxy> table;
	int x;
	int y;
	//cell table changes
	private SingleSelectionModel<SpokenLanguageProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private SpokenLanguageDetailsActivityMapper SpokenLanguageDetailsActivityMapper;

	public SpokenLanguageActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeControler = placeController;
    	SpokenLanguageDetailsActivityMapper = new SpokenLanguageDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(SpokenLanguageDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		SpokenLanguageView systemStartView = new SpokenLanguageViewImpl();
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

				Column<SpokenLanguageProxy, String> col = (Column<SpokenLanguageProxy, String>) event.getColumn();
				
				
				int index = table.getColumnIndex(col);
				
			
				if (index % 2 == 1 ) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

				} 
			}
		});
		
		
		//cell table changes end

		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (SpokenLanguageViewImpl)view);
		//by spec
		MenuClickEvent.register(requests.getEventBus(), (SpokenLanguageViewImpl)view);
		
		
		setInserted(false);
		init();

//		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<SpokenLanguageProxy> keyProvider = ((AbstractHasData<SpokenLanguageProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<SpokenLanguageProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						SpokenLanguageProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getLanguageName()
									+ " selected!");
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);
		
	}
	
	private int totalRecords;
	private boolean isInserted;
	
	public boolean isInserted() {
		return isInserted;
	}

	public void setInserted(boolean isInserted) {
		this.isInserted = isInserted;
	}
	
	private void init() {
		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						SpokenLanguageActivity.this.onRangeChanged();
					}
				});

		startRequestChain();
	}
	
	private void startRequestChain() {
		fireCountRequest(new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Sprachen aus der Datenbank: " + response);

				totalRecords = response.intValue();
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged();
			}

		});
	}
	
	protected void showDetails(SpokenLanguageProxy SpokenLanguage) {
		
		Log.debug(SpokenLanguage.getLanguageName());
		
		goTo(new SpokenLanguageDetailsPlace(SpokenLanguage.stableId(),
				Operation.DETAILS));
	}
	

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<SpokenLanguageProxy>> callback = new Receiver<List<SpokenLanguageProxy>>() {
			@Override
			public void onSuccess(List<SpokenLanguageProxy> values) {
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

		fireRangeRequest(range, callback);

	}
	
	private void fireRangeRequest(final Range range, final Receiver<List<SpokenLanguageProxy>> callback) {
		createRangeRequest(range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<SpokenLanguageProxy>> createRangeRequest(Range range) {
		String name = view.getSearchTerm();
		return requests.languageRequestNonRoo().findLanguagesByName(name, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(Receiver<Long> callback) {
//		requests.spokenLanguageRequest().countSpokenLanguages().fire(callback);
		String name = view.getSearchTerm();
		requests.languageRequestNonRoo().countLanguagesByName(name).fire(callback);
	}

	private void setTable(CellTable<SpokenLanguageProxy> table) {
		//this.table = table;
		//cell table changes
		this.table = (AdvanceCellTable<SpokenLanguageProxy>)table;
		
	}

	@Override
	public void newClicked(String name) {
		Log.debug("Add language");
		SpokenLanguageRequest langReq = requests.spokenLanguageRequest();
		SpokenLanguageProxy lang = langReq.create(SpokenLanguageProxy.class);
		//reques.edit(scar);
		lang.setLanguageName(name);
		// Highlight onViolation
		Log.info("Map Size: " + view.getNewLanguageMap().size());
		langReq.persist().using(lang).fire(new OSCEReceiver<Void>(view.getNewLanguageMap()){
		// E Highlight onViolation
			@Override
			public void onSuccess(Void arg0) {
				setInserted(true);
				init();
			}
		});
	}
	
	@Override
	public void deleteClicked(SpokenLanguageProxy lang) {
		requests.spokenLanguageRequest().remove().using(lang).fire(new Receiver<Void>() {
			public void onSuccess(Void ignore) {
				Log.debug("Sucessfully deleted");
				setInserted(false);
				init();
			}
		});
	}
	
	@Override
	public void performSearch() {
		setInserted(false);
		startRequestChain();
	}

	@Override
	public void goTo(Place place) {
		placeControler.goTo(place);
		
	}

	@Override
	public void updateClicked(SpokenLanguageProxy proxy, String value) {
		SpokenLanguageRequest langRequest = requests.spokenLanguageRequest();
		proxy = langRequest.edit(proxy);
		proxy.setLanguageName(value);
		
		// Highlight onViolation
		Log.info("Map Size: " + view.getSpokenLanguageView().getLanguageMap().size());
		langRequest.persist().using(proxy).fire(new OSCEReceiver<Void>(view.getSpokenLanguageView().getLanguageMap()) {
		// E Highlight onViolation			
			@Override
			public void onSuccess(Void response) 
			{
				// Highlight onViolation
				((EditPopViewImpl)view.getEditPopView()).hide();
				// E: Highlight onViolation
				setInserted(false);
				init();				
			}
		});
		
		
	}

}
