package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.Paging;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ProfessionDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.EditPopViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ProfessionView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.ProfessionViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionRequest;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
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
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;

public class ProfessionActivity extends AbstractActivity implements
ProfessionView.Presenter, ProfessionView.Delegate {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private ProfessionView view;
	
	//cell table changes
	/*private CellTable<ProfessionProxy> table;*/
	private AdvanceCellTable<ProfessionProxy> table;
	int x;
	int y;
	//cell table changes
	private SingleSelectionModel<ProfessionProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private ActivityManager activityManger;
	private ProfessionDetailsActivityMapper ProfessionDetailsActivityMapper;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public ProfessionActivity(OsMaRequestFactory requests, PlaceController placeController) {
    	this.requests = requests;
    	this.placeController = placeController;
    	ProfessionDetailsActivityMapper = new ProfessionDetailsActivityMapper(requests, placeController);
		this.activityManger = new ActivityManager(ProfessionDetailsActivityMapper, requests.getEventBus());
    }

	public void onStop(){
		activityManger.setDisplay(null);
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		ProfessionView systemStartView = new ProfessionViewImpl();
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

				Column<ProfessionProxy, String> col = (Column<ProfessionProxy, String>) event.getColumn();
				
				
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
		
		
		//cell table changes end


		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (ProfessionViewImpl)view);
		//by spec
		MenuClickEvent.register(requests.getEventBus(), (ProfessionViewImpl)view);

		
		setInserted(false);
		init();

//		activityManger.setDisplay(view.getDetailsPanel());

		// Inherit the view's key provider
		ProvidesKey<ProfessionProxy> keyProvider = ((AbstractHasData<ProfessionProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<ProfessionProxy>(keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						ProfessionProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							Log.debug(selectedObject.getProfession()
									+ " selected!");
							showDetails(selectedObject);
						}
					}
				});

		view.setDelegate(this);
		
	}
	
	private int totalRecords;
	private boolean isInserted;
	
	private ProfessionProxy editedProfession;
	
	public boolean isInserted() {
		return isInserted;
	}

	public void setInserted(boolean isInserted) {
		this.isInserted = isInserted;
	}
	
	private void init() {
		init2("");
	}
	
	private void init2(final String q) {

		// fix to avoid having multiple rangeChangeHandlers attached
		if (rangeChangeHandler != null){
			rangeChangeHandler.removeHandler();
		}
		
		fireCountRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Berufe aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);
				totalRecords = response.intValue();
				onRangeChanged(q);
			}

		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						ProfessionActivity.this.onRangeChanged(q);
					}
				});
	}
	
	protected void showDetails(ProfessionProxy Profession) {
		
		Log.debug(Profession.getProfession());
		
		goTo(new ProfessionDetailsPlace(Profession.stableId(),
				Operation.DETAILS));
	}
	

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<ProfessionProxy>> callback = new Receiver<List<ProfessionProxy>>() {
			@Override
			public void onSuccess(List<ProfessionProxy> values) {
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
	
	private void fireRangeRequest(String name, final Range range,
			final Receiver<List<ProfessionProxy>> callback) {
		createRangeRequest(name, range).with(view.getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}
	
	protected Request<List<ProfessionProxy>> createRangeRequest(String name, Range range) {
//		return requests.professionRequest().findProfessionEntries(range.getStart(), range.getLength());
		return requests.professionRequest().findProfessionsByName(name, range.getStart(), range.getLength());
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
//		requests.professionRequest().countProfessions().fire(callback);
		requests.professionRequest().countProfessionsByName(name).fire(callback);
	}

	private void setTable(CellTable<ProfessionProxy> table) {
		//this.table = table;
		//cell table changes
		this.table = (AdvanceCellTable<ProfessionProxy>)table;
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void newClicked(String name) {
		Log.debug("Add profession");
		ProfessionRequest profReq = requests.professionRequest();
		final ProfessionProxy profession = profReq.create(ProfessionProxy.class);
		profession.setProfession(name);
		
		// Highlight onViolation
		Log.info("Map Size: " + view.getNewLanguageMap().size());
		profReq.persist().using(profession).fire(new OSCEReceiver<Void>(view.getNewLanguageMap()){
		// E Highlight onViolation
			@Override
			public void onSuccess(Void arg0) {
				
				requests.professionRequest().saveNewProfessionInSpPortal(profession).fire(new OSCEReceiver<Boolean>() {

					@Override
					public void onSuccess(Boolean response) {
					
						if(response==false){
							showErrorMessageToUser("System could not create Profession in SpPortal");
						}
					}
				});
				setInserted(true);
				init();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void deleteClicked(final ProfessionProxy prof) {
		requests.professionRequest().deleteProfessionInSpportal(prof).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				if(response==false){
					showErrorMessageToUser("System could not delete Profession in spportal");
				}
				
				requests.professionRequest().remove().using(prof).fire(new Receiver<Void>() {
					public void onSuccess(Void ignore) {
						Log.debug("Sucessfully deleted");
						setInserted(false);
						init();
					}
				});
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
		placeController.goTo(place);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateClicked(final ProfessionProxy proxy,final String value) {
		
		editedProfession=proxy;
		
		requests.professionRequest().editProfessionInSpPortal(proxy,value).fire(new OSCEReceiver<Boolean>() {

			
			@Override
			public void onSuccess(Boolean response) {
				if(response==false){
					showErrorMessageToUser("System could not edit Professsion for spportal");
				}

				ProfessionRequest profRequest = requests.professionRequest();
				editedProfession = profRequest.edit(editedProfession);
				editedProfession.setProfession(value);
				// Highlight onViolation		
				Log.info("Map Size: " + view.getProfessionMap().size());
				profRequest.persist().using(editedProfession).fire(new OSCEReceiver<Void>(view.getProfessionMap()) {
					// E Highlight onViolation
					@Override
					public void onSuccess(Void response) {
						// Highlight onViolation	
						((EditPopViewImpl)view.getEditPopView()).hide();
						// E Highlight onViolation
						setInserted(false);
						init();			
					}
				});
			}
		});
		
	}

	public void showErrorMessageToUser(String message){
		final MessageConfirmationDialogBox confirmationDialogBox =new MessageConfirmationDialogBox(constants.warning());
		confirmationDialogBox.showConfirmationDialog(message);
	}
}
