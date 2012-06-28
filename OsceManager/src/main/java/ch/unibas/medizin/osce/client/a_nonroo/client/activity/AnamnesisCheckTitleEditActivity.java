package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckTitleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckTitleEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckTitleEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckProxyRenderer;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AnamnesisCheckTitleEditActivity extends AbstractActivity implements
AnamnesisCheckTitleEditView.Presenter, AnamnesisCheckTitleEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AnamnesisCheckTitleEditView view;
	private AnamnesisCheckTitleDetailsPlace place;
	private boolean isTitleChange = false;
	private int previousSortOder;

	private RequestFactoryEditorDriver<AnamnesisCheckTitleProxy, AnamnesisCheckTitleEditViewImpl> editorDriver;
	private AnamnesisCheckTitleProxy anamnesisCheckTitle;
	private boolean save;
	
	private AnamnesisCheckTitleRequest request;

	public AnamnesisCheckTitleEditActivity(AnamnesisCheckTitleDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public AnamnesisCheckTitleEditActivity(AnamnesisCheckTitleDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController,
			Operation operation) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public void onStop() {

	}

	@Override
	public String mayStop() {
		if (!save && changed())
			return "Changes will be discarded!";
		else
			return null;
	}
	
	// use this to check if some value has changed since editing has started
	private boolean changed() {
		return editorDriver != null && editorDriver.flush().isChanged();
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		AnamnesisCheckTitleEditView anamnesisCheckTItleEditView = new AnamnesisCheckTitleEditViewImpl();

		this.widget = panel;
		this.view = anamnesisCheckTItleEditView;
		
		editorDriver = view.createEditorDriver();

		view.setDelegate(this);

		if (this.place.getOperation() == Operation.EDIT) {
			Log.info("edit");
			requests.find(place.getProxyId()).fire(new Receiver<Object>() {
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
				}

				@Override
				public void onSuccess(Object response) {
					if (response instanceof AnamnesisCheckTitleProxy) {
						Log.info(((AnamnesisCheckTitleProxy) response).getId().toString());
						anamnesisCheckTitle = (AnamnesisCheckTitleProxy) response;
						init();
					}

				}
			});
		} else {

			Log.info("new AnamnesisCheck");
			init();
		}
		widget.setWidget(anamnesisCheckTItleEditView.asWidget());
	}

	private void init() {
		request = requests.anamnesisCheckTitleRequest();

		if (anamnesisCheckTitle == null) {
			AnamnesisCheckTitleProxy anamnesisCheckTitle = request.create(AnamnesisCheckTitleProxy.class);
			this.anamnesisCheckTitle = anamnesisCheckTitle;
		} 

		
		Log.info("edit");

		Log.info("persist");
		
		request.persist().using(anamnesisCheckTitle);
		editorDriver.edit(anamnesisCheckTitle, request);

		editorDriver.flush();
		Log.debug("Create für: " + anamnesisCheckTitle.getId());
		initInsideTitle();
	}
	
	//initInside Title
	@SuppressWarnings("deprecation")
	private void initInsideTitle(){

		requests.anamnesisCheckTitleRequest().findAllAnamnesisCheckTitles().fire(new Receiver<List<AnamnesisCheckTitleProxy>>() {

			@Override
			public void onSuccess(List<AnamnesisCheckTitleProxy> response) {
				GWT.log("find titles sucess response size = "+response.size());
				view.setInsideTitleListBox(response);
				
				if(place.getOperation() == Operation.EDIT && anamnesisCheckTitle.getId() != null){
					view.getSelectedInsideTitle();
				}else if(place.getOperation() == Operation.CREATE ){
					view.setSeletedInsideTitle(String.valueOf(place.getTitleId()));
				
					

				}
			}
		});
			
	}


	private void gotoDetailsPlace(){
		placeController.goTo(new AnamnesisCheckTitleDetailsPlace(anamnesisCheckTitle.stableId(),
				Operation.NEW));
	}
	

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);

	}

	@Override
	public void cancelClicked() {
		if (this.place.getOperation() == Operation.EDIT){
			placeController.goTo(new AnamnesisCheckTitleDetailsPlace(anamnesisCheckTitle.stableId(),
					Operation.DETAILS));
		}
		else{
			placeController.goTo(new AnamnesisCheckTitleDetailsPlace("AnamnesisCheckTitleDetailsPlace!CANCEL"));
		}
	}
	

	//save Title
	@SuppressWarnings("deprecation")
	@Override
	public void saveClicked() {
		request = (AnamnesisCheckTitleRequest) editorDriver.flush();
		anamnesisCheckTitle = request.edit(anamnesisCheckTitle);
		//save data
		request.fire(new Receiver<Void>() {

			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());

			}

			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(" in AnamnesisCheck -" + message);

			}
			
			@Override
			public void onSuccess(Void response) {
				Log.info("AnamnesisCheck successfully saved.");

				save = true;
				if (view.getSelectedInsideTitle() != null && !view.getSelectedInsideTitle().equals("") && view.getSelectedInsideTitle().length() != 0){
					requests.anamnesisCheckTitleRequest().findAnamnesisCheckTitle(Long.parseLong(view.getSelectedInsideTitle())).fire(new Receiver<AnamnesisCheckTitleProxy>(){

						@Override
						public void onSuccess(AnamnesisCheckTitleProxy response) {
							// TODO Auto-generated method stub
							previousSortOder = Integer.valueOf(response.getSort_order());
							sortOderByPrevious(previousSortOder);
						}
						
					});
					
				}else{
					sortOderByPrevious(previousSortOder);
				}
				
			}
		});
	
	}
	
	@SuppressWarnings("deprecation")
	private void sortOderByPrevious(int previousSortOder){
		if(place.getOperation() == Operation.CREATE){
			requests.anamnesisCheckTitleRequestNonRoo().insertNewSortOder(previousSortOder).using(anamnesisCheckTitle).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					placeController.goTo(new AnamnesisCheckTitleDetailsPlace(anamnesisCheckTitle.stableId(), Operation.NEW));
				}
			});
		
		}else if(place.getOperation() == Operation.EDIT){
			requests.anamnesisCheckTitleRequestNonRoo().oderByPreviousAnamnesisCheckTitle(previousSortOder).using(anamnesisCheckTitle).fire(new Receiver<Void>() {
				
								@Override
								public void onSuccess(Void response) {
									placeController.goTo(new AnamnesisCheckTitleDetailsPlace(anamnesisCheckTitle.stableId(), Operation.NEW));
								}
				
							});
		}
	
	
			
	}


	
	private void setOrder(){
		if (place.getOperation() == Operation.CREATE) {
			requests.anamnesisCheckTitleRequestNonRoo().insertNewSortOder(0)
					.using(anamnesisCheckTitle).fire(new Receiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							GWT.log("############### insertNewSortOder success");
							save = true;
							placeController
									.goTo(new AnamnesisCheckTitleDetailsPlace(
											anamnesisCheckTitle.stableId(),
											Operation.NEW));
						}
					});
		}else{
			save = true;
			placeController
					.goTo(new AnamnesisCheckTitleDetailsPlace(
							anamnesisCheckTitle.stableId(),
							Operation.NEW));
		}
	}

	

	
}
