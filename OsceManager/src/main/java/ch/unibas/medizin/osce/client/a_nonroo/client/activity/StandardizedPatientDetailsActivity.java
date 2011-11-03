package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.StandardizedPatientScarSubView;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormRequest;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class StandardizedPatientDetailsActivity extends AbstractActivity implements
StandardizedPatientDetailsView.Presenter, StandardizedPatientDetailsView.Delegate, StandardizedPatientScarSubView.Delegate  {
	
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private StandardizedPatientDetailsView view;
	private CellTable<StandardizedPatientProxy> table;
	private SingleSelectionModel<StandardizedPatientProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private StandardizedPatientDetailsPlace place;
	private StandardizedPatientProxy standardizedPatientProxy;
	

	public StandardizedPatientDetailsActivity(StandardizedPatientDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    }

	public void onStop(){

	}
	
	StandardizedPatientScarSubView standardizedPatientScarSubView;
	private CellTable<ScarProxy> scarTable;
	private ValueListBox<ScarProxy> scarBox;
	private HandlerRegistration rangeChangeHandlerScars;
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("StandardizedPatientDetailsActivity.start()");
		StandardizedPatientDetailsView standardizedPatientDetailsView = new StandardizedPatientDetailsViewImpl();
		standardizedPatientDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = standardizedPatientDetailsView;
		standardizedPatientScarSubView = view.getStandardizedPatientScarSubViewImpl();
		widget.setWidget(standardizedPatientDetailsView.asWidget());
		
		view.setDelegate(this);
		standardizedPatientScarSubView.setDelegate(this);
		
		requests.find(place.getProxyId()).with("anamnesisForm", "anamnesisForm.scars").fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());
			}
			@Override
			public void onSuccess(Object response) {
				if(response instanceof StandardizedPatientProxy){
					Log.info(((StandardizedPatientProxy) response).getName());
					standardizedPatientProxy = (StandardizedPatientProxy) response;
					init();
					initScar();
				}
			}
		});
	}
	
	protected void initScar() {
		this.scarTable = standardizedPatientScarSubView.getTable();
		this.scarBox = standardizedPatientScarSubView.getScarBox();
		
		requests.scarRequestNonRoo().findScarEntriesByNotAnamnesisForm(standardizedPatientProxy.getAnamnesisForm().getId()).fire(new Receiver<List<ScarProxy>>() {
			
			@Override
			public void onSuccess(List<ScarProxy> scars) {
				scarBox.setValue(null);
				scarBox.setAcceptableValues(scars);
			}
		});

		requests.scarRequestNonRoo().countScarsByAnamnesisForm(standardizedPatientProxy.getAnamnesisForm().getId()).fire(new Receiver<Long>(){

			@Override
			public void onSuccess(Long count) {
				if (view == null) {
					// This activity is dead
					return;
				}
				
				Log.debug(count.toString() + " scars loaded");
				scarTable.setRowCount(count.intValue(), true);
				
				onRangeChangedScarTable();
			}
		});
	}
	
	protected void onRangeChangedScarTable() {
		final Range range = scarTable.getVisibleRange();

		final Receiver<List<ScarProxy>> callback = new Receiver<List<ScarProxy>>() {
			@Override
			public void onSuccess(List<ScarProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				scarTable.setRowData(range.getStart(), values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireScarRangeRequest(range, callback);
	}
	
	private void fireScarRangeRequest(final Range range, final Receiver<List<ScarProxy>> callback) {
		createScarRangeRequest(range).with(standardizedPatientScarSubView.getPaths()).fire(callback);
	}
	
	protected Request<List<ScarProxy>> createScarRangeRequest(Range range) {
		return requests.scarRequestNonRoo().findScarEntriesByAnamnesisForm(standardizedPatientProxy.getAnamnesisForm().getId(), range.getStart(), range.getLength());
	}

	private AnamnesisFormProxy anamnesisForm ;
	
	private void init() {
		view.setValue(standardizedPatientProxy);
		anamnesisForm =  standardizedPatientProxy.getAnamnesisForm();

	}
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
		
	}

	@Override
	public void editClicked() {
		Log.info("edit clicked");
		goTo(new StandardizedPatientDetailsPlace(standardizedPatientProxy.stableId(),
				StandardizedPatientDetailsPlace.Operation.EDIT));
		
	}

	@Override
	public void deleteClicked() {
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
            return;
        }
		
        requests.standardizedPatientRequest().remove().using(this.standardizedPatientProxy).fire(new Receiver<Void>() {

            public void onSuccess(Void ignore) {
                if (widget == null) {
                    return;
                }
            	placeController.goTo(new StandardizedPatientPlace("StandardizedPatientPlace!DELETED"));
            }
        });
		
	}
	
	@Override
	public void deleteScarClicked(ScarProxy scar) {
		AnamnesisFormRequest anamReq = requests.anamnesisFormRequest();
		anamnesisForm =  anamReq.edit(anamnesisForm);
		
		Log.debug("Remove scar (" + scar.getId() + ") from anamnesis-form (" + standardizedPatientProxy.getAnamnesisForm().getId() + ")");
		
		Iterator<ScarProxy> it = anamnesisForm.getScars().iterator();
		while (it.hasNext()) {
			
			ScarProxy scarProxy = (ScarProxy) it.next();
			//Log.warn(scarProxy.stableId() + " ");
			//Log.warn(scar.stableId() + " ");
			if (scarProxy.getId() == scar.getId() ) {
				anamnesisForm.getScars().remove(scarProxy);
				break;
			}
		}
		anamnesisForm.getScars().remove(scar);
		
		anamReq.persist().using(anamnesisForm).fire(new Receiver<Void>(){
			@Override
			public void onSuccess(Void arg0) {
				Log.debug("scar removed from anamnesisform...");
				initScar();
			}
		});
	}

	@Override
	public void scarAddButtonClicked() {
		AnamnesisFormRequest anamReq = requests.anamnesisFormRequest();
		
		ScarProxy scar = scarBox.getValue();
		
		Log.debug("Add scar (" + scar.getBodypart() + " - id " + scar.getId() + ") to anamnesis-form (" + standardizedPatientProxy.getAnamnesisForm().getId() + ")");
		
		anamnesisForm = anamReq.edit(anamnesisForm);
		
		anamnesisForm.getScars().add(scar);
		
		anamReq.persist().using(anamnesisForm).fire(new Receiver<Void>(){
			@Override
			public void onSuccess(Void arg0) {
				Log.debug("scar added...");
				initScar();
			}
		});
	}

}
