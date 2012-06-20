package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckTitleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckTitleEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckTitleEditViewImpl;
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
		// this.operation=operation;
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
		//view.setPresenter(this);

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
						// init((AnamnesisCheckProxy) response);
						anamnesisCheckTitle = (AnamnesisCheckTitleProxy) response;
						init();
					}

				}
			});
		} else {

			Log.info("new AnamnesisCheck");
//			 anamnesisCheckPlace.setProxyId(anamnesisCheckTitle.stableId());
			init();
		}
//		// view.initialiseDriver(requests);
		widget.setWidget(anamnesisCheckTItleEditView.asWidget());
//		// setTable(view.getTable());
	}

	private void init() {
		request = requests.anamnesisCheckTitleRequest();

		if (anamnesisCheckTitle == null) {
			AnamnesisCheckTitleProxy anamnesisCheckTitle = request.create(AnamnesisCheckTitleProxy.class);
			this.anamnesisCheckTitle = anamnesisCheckTitle;
			//view.setEditTitle(false);
		} else {
			// cannot be set via editor...
			//view.setEditTitle(true);
		}
		
		Log.info("edit");

		Log.info("persist");
		
		request.persist().using(anamnesisCheckTitle);
		editorDriver.edit(anamnesisCheckTitle, request);

//		Log.info("flush");
		editorDriver.flush();
		Log.debug("Create für: " + anamnesisCheckTitle.getId());
		
//		 manually update value fields... (no editor support)
		
		//view.update(anamnesisCheck);
		
	

	}
	

	

	private void gotoDetailsPlace(){
		placeController.goTo(new AnamnesisCheckTitleDetailsPlace(anamnesisCheckTitle.stableId(),
				Operation.NEW));
	}
	
//	@SuppressWarnings("deprecation")
//	private void sortOderByPrevious(){
//	
//		if (view.getSelectedPreviousQuestion() != null && !view.getSelectedPreviousQuestion().equals("") && view.getSelectedPreviousQuestion().length() != 0) {
//			int previousSortOder = Integer.valueOf(view.getSelectedPreviousQuestion());
//		
//			if (anamnesisCheck.getSort_order()!=null && previousSortOder > anamnesisCheck.getSort_order() ) {
//
//				requests.anamnesisCheckRequestNonRoo().orderDownByPrevious(previousSortOder).using(anamnesisCheck).fire(new Receiver<Void>() {
//					public void onFailure(ServerFailure error) {
//						Log.error(error.getMessage());
//
//					}
//					
//					@Override
//					public void onSuccess(Void response) {
//						gotoDetailsPlace();
//					}
//				});
//			} else if (anamnesisCheck.getSort_order()!=null && previousSortOder < anamnesisCheck.getSort_order() - 1) {
//				requests.anamnesisCheckRequestNonRoo().orderUpByPrevious(previousSortOder).using(anamnesisCheck).fire(new Receiver<Void>() {
//					public void onFailure(ServerFailure error) {
//						Log.error(error.getMessage());
//
//					}
//					
//					@Override
//					public void onSuccess(Void response) {
//						gotoDetailsPlace();
//
//					}
//				});
//			}else if(anamnesisCheck.getSort_order() == null){
//				requests.anamnesisCheckRequestNonRoo().insertNewSortOder(previousSortOder).using(anamnesisCheck).fire(new Receiver<Void>() {
//					public void onFailure(ServerFailure error) {
//						GWT.log("insertNewSortOder error = "+error);
//
//					}
//					
//					@Override
//					public void onSuccess(Void response) {
//						gotoDetailsPlace();
//
//					}
//				});
//			}else{
//				
//				gotoDetailsPlace();
//				
//			}
//		}else{
//			if(anamnesisCheck.getSort_order() == null){
//				GWT.log("anamnesisCheck.getSort_order() is null ");
//				requests.anamnesisCheckRequestNonRoo().insertNewSortOder(-1).using(anamnesisCheck).fire(new Receiver<Void>() {
//					public void onFailure(ServerFailure error) {
//						GWT.log("insertNewSortOder error = "+error);
//
//					}
//					
//					@Override
//					public void onSuccess(Void response) {
//						gotoDetailsPlace();
//
//					}
//				});
//			}else{
//				GWT.log("no selected previos qustion");
//				if(anamnesisCheck.getTitle()!=null&&anamnesisCheck.getTitle().getSort_order() > anamnesisCheck.getSort_order()){
//					requests.anamnesisCheckRequestNonRoo().orderDownByPrevious(-1).using(anamnesisCheck).fire(new Receiver<Void>() {
//						public void onFailure(ServerFailure error) {
//							Log.error(error.getMessage());
//
//						}
//						
//						@Override
//						public void onSuccess(Void response) {
//							gotoDetailsPlace();
//						}
//					});
//				}else if(anamnesisCheck.getTitle()!=null&&anamnesisCheck.getTitle().getSort_order() < anamnesisCheck.getSort_order()-1){
//					requests.anamnesisCheckRequestNonRoo().orderUpByPrevious(-1).using(anamnesisCheck).fire(new Receiver<Void>() {
//						public void onFailure(ServerFailure error) {
//							Log.error(error.getMessage());
//
//						}
//						
//						@Override
//						public void onSuccess(Void response) {
//							gotoDetailsPlace();
//
//						}
//					});
//				}else{
//					gotoDetailsPlace();
//					}
//			}
//		}
//	}

	
	
	@Override
	public void goTo(Place place) {
		//placeController.goTo(place);
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
	


	@SuppressWarnings("deprecation")
	@Override
	public void saveClicked() {
		//Log.info("saveClicked");
		request = (AnamnesisCheckTitleRequest) editorDriver.flush();
		anamnesisCheckTitle = request.edit(anamnesisCheckTitle);
//		
		anamnesisCheckTitle.setText(view.getValue());
		editorDriver.flush().fire(new Receiver<Void>() {

			public void onFailure(ServerFailure error){
				Log.error(error.getMessage());

			}
			@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while(iter.hasNext()){
					message += iter.next().getMessage() + "<br>";
				}
				//Log.warn(" in StandardizedPatient -" + message);
			}
			@Override
			public void onSuccess(Void response) {
				//Log.info("StandardizedPatient successfully saved.");

				
//				save = true;
//				placeController.goTo(new AnamnesisCheckTitleDetailsPlace(anamnesisCheckTitle.stableId(), Operation.NEW));
				//saveDescription();
				setOrder();
			}
		});
	
		
	
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
	
	  protected void fireTitleValueRequest(Receiver<List<AnamnesisCheckTitleProxy>> callback) {
	    	requests.anamnesisCheckTitleRequest().findAllAnamnesisCheckTitles().fire(callback);
	    }

//	@SuppressWarnings("deprecation")
//	@Override
//	public void changePreviousQuestion(final AnamnesisCheckTypes anamnesisCheckType, String seletedTitleId) {
//		
//		if(seletedTitleId!=null&&!seletedTitleId.equals("")&&seletedTitleId.length()!=0){
//			requests.anamnesisCheckRequest().findAnamnesisCheck(Long.valueOf(seletedTitleId)).fire(new Receiver<AnamnesisCheckProxy>() {
//				public void onFailure(ServerFailure error) {
//					Log.error(error.getMessage());
//
//				}
//
//				@Override
//				public void onSuccess(AnamnesisCheckProxy response) {
//				
//					getQuestionsByselected(anamnesisCheckType,response);
//				}
//			});
//			
//		}else{
//			getQuestionsByselected(anamnesisCheckType,null);
//		}
//	}
	
}
