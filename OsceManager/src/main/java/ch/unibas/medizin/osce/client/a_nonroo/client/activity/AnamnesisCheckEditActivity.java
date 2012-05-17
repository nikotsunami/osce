package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.StandardizedPatientDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckRequest;
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
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AnamnesisCheckEditActivity extends AbstractActivity implements
AnamnesisCheckEditView.Presenter, AnamnesisCheckEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AnamnesisCheckEditView view;
	private AnamnesisCheckDetailsPlace place;

	private RequestFactoryEditorDriver<AnamnesisCheckProxy, AnamnesisCheckEditViewImpl> editorDriver;
	private AnamnesisCheckProxy anamnesisCheck;
	private boolean save;
	
	private AnamnesisCheckRequest request;

	public AnamnesisCheckEditActivity(AnamnesisCheckDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public AnamnesisCheckEditActivity(AnamnesisCheckDetailsPlace place,
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

		Log.info("AnamnesisCheckEditActivity.start()");
		AnamnesisCheckEditView anamnesisCheckEditView = new AnamnesisCheckEditViewImpl();

		this.widget = panel;
		this.view = anamnesisCheckEditView;
		
		editorDriver = view.createEditorDriver();

		view.setDelegate(this);

		if (this.place.getOperation() == Operation.EDIT) {
			Log.info("edit");
			requests.find(place.getProxyId()).with("anamnesisForm","title").fire(new Receiver<Object>() {
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
				}

				@Override
				public void onSuccess(Object response) {
					if (response instanceof AnamnesisCheckProxy) {
						Log.info(((AnamnesisCheckProxy) response).getId().toString());
						// init((AnamnesisCheckProxy) response);
						anamnesisCheck = (AnamnesisCheckProxy) response;
						init();
					}

				}
			});
		} else {

			Log.info("new AnamnesisCheck");
			// anamnesisCheckPlace.setProxyId(anamnesisCheck.stableId());
			init();
		}
		// view.initialiseDriver(requests);
		widget.setWidget(anamnesisCheckEditView.asWidget());
		// setTable(view.getTable());
	}

	private void init() {
		request = requests.anamnesisCheckRequest();

		if (anamnesisCheck == null) {
			AnamnesisCheckProxy anamnesisCheck = request.create(AnamnesisCheckProxy.class);
			this.anamnesisCheck = anamnesisCheck;
			view.setEditTitle(false);
		} else {
			// cannot be set via editor...
			view.setEditTitle(true);
		}
		
		Log.info("edit");

		Log.info("persist");
		
		request.persist().using(anamnesisCheck);
		editorDriver.edit(anamnesisCheck, request);

		Log.info("flush");
//		editorDriver.flush();
		Log.debug("Create für: " + anamnesisCheck.getId());
		
		// manually update value fields... (no editor support)
		
		view.update(anamnesisCheck);
		
		initInsideTitle();
		getQuestionsByselected(anamnesisCheck.getType(),anamnesisCheck.getTitle());

	}
	
	@SuppressWarnings("deprecation")
	private void initInsideTitle(){
		requests.anamnesisCheckRequestNonRoo().findAnamnesisChecksByType(AnamnesisCheckTypes.QUESTION_TITLE).with("anamnesisForm","title").fire(new Receiver<List<AnamnesisCheckProxy>>() {
			public void onFailure(ServerFailure error) {
				GWT.log("in AnamnesisCheckEditActivity initInsideTitle error = "+error);
			}
			
			@Override
			public void onSuccess(List<AnamnesisCheckProxy> response) {
				view.setInsideTitleListBox(response);
				if(anamnesisCheck.getTitle() != null){
				view.setSeletedInsideTitle(String.valueOf(anamnesisCheck.getTitle().getId()));
				}

			}
		});

			
	}
	
	@SuppressWarnings("deprecation")
	private void getQuestionsByselected(AnamnesisCheckTypes anamnesisChecktype, final AnamnesisCheckProxy title){

		if(anamnesisChecktype!=null&&anamnesisChecktype==AnamnesisCheckTypes.QUESTION_TITLE){
			requests.anamnesisCheckRequestNonRoo().findAnamnesisChecksByType(AnamnesisCheckTypes.QUESTION_TITLE).with("anamnesisForm","title").fire(new Receiver<List<AnamnesisCheckProxy>>() {
				public void onFailure(ServerFailure error) {
					GWT.log("in AnamnesisCheckEditActivity findAnamnesisChecksByType error = "+error);
				}

				@Override
				public void onSuccess(List<AnamnesisCheckProxy> response) {
					initPreviousQuestion(response);
					
				}
			});
		}else if(title!=null){
			requests.anamnesisCheckRequestNonRoo().findAnamnesisChecksByTitle("", title).with("anamnesisForm","title").fire(new Receiver<List<AnamnesisCheckProxy>>() {
				public void onFailure(ServerFailure error) {
					GWT.log("in AnamnesisCheckEditActivity findAnamnesisChecksByType error = "+error);
				}

				@Override
				public void onSuccess(List<AnamnesisCheckProxy> response) {
					List<AnamnesisCheckProxy> previousQuestions = new ArrayList<AnamnesisCheckProxy>();
					previousQuestions.add(title);
					previousQuestions.addAll(response);
					initPreviousQuestion(previousQuestions);
					
				}
			});
		}

	}
	
	private void initPreviousQuestion(List<AnamnesisCheckProxy> anamnesisCheckList){
		AnamnesisCheckProxy deleteAnamnesisCheckProxy = null;
		for (AnamnesisCheckProxy anamnesisCheckProxy : anamnesisCheckList) {
			if (anamnesisCheck.getId()!=null && String.valueOf(anamnesisCheckProxy.getId()).equals(String.valueOf(anamnesisCheck.getId()))) {
				deleteAnamnesisCheckProxy = anamnesisCheckProxy;
			}
		}
		if (deleteAnamnesisCheckProxy != null) {
			anamnesisCheckList.remove(deleteAnamnesisCheckProxy);
		}
		view.setPreviousQuestionListBox(anamnesisCheckList);
		if(anamnesisCheck.getSort_order() != null){
			if(anamnesisCheck.getType()!=null&&anamnesisCheck.getType()!=AnamnesisCheckTypes.QUESTION_TITLE){
				view.setSeletedPreviousQuestion(String.valueOf(anamnesisCheck.getSort_order()-1));
			}else{
				requests.anamnesisCheckRequestNonRoo().findPreviousTitleBySortOder(anamnesisCheck.getSort_order()).fire(new Receiver<AnamnesisCheckProxy>() {

					@Override
					public void onSuccess(AnamnesisCheckProxy response) {
						if(response!=null){
							view.setSeletedPreviousQuestion(String.valueOf(response.getSort_order()));
						}
					}
				});
			}
		}
	}
	
	private void gotoDetailsPlace(){
		placeController.goTo(new AnamnesisCheckDetailsPlace(anamnesisCheck.stableId(),
				Operation.NEW));
	}
	
	@SuppressWarnings("deprecation")
	private void sortOderByPrevious(){
	
		if (view.getSelectedPreviousQuestion() != null && !view.getSelectedPreviousQuestion().equals("") && view.getSelectedPreviousQuestion().length() != 0) {
			int previousSortOder = Integer.valueOf(view.getSelectedPreviousQuestion());
		
			if (anamnesisCheck.getSort_order()!=null && previousSortOder > anamnesisCheck.getSort_order() ) {

				requests.anamnesisCheckRequestNonRoo().orderDownByPrevious(previousSortOder).using(anamnesisCheck).fire(new Receiver<Void>() {
					public void onFailure(ServerFailure error) {
						Log.error(error.getMessage());

					}
					
					@Override
					public void onSuccess(Void response) {
						gotoDetailsPlace();
					}
				});
			} else if (anamnesisCheck.getSort_order()!=null && previousSortOder < anamnesisCheck.getSort_order() - 1) {
				requests.anamnesisCheckRequestNonRoo().orderUpByPrevious(previousSortOder).using(anamnesisCheck).fire(new Receiver<Void>() {
					public void onFailure(ServerFailure error) {
						Log.error(error.getMessage());

					}
					
					@Override
					public void onSuccess(Void response) {
						gotoDetailsPlace();

					}
				});
			}else if(anamnesisCheck.getSort_order() == null){
				requests.anamnesisCheckRequestNonRoo().insertNewSortOder(previousSortOder).using(anamnesisCheck).fire(new Receiver<Void>() {
					public void onFailure(ServerFailure error) {
						GWT.log("insertNewSortOder error = "+error);

					}
					
					@Override
					public void onSuccess(Void response) {
						gotoDetailsPlace();

					}
				});
			}else{
				
				gotoDetailsPlace();
				
			}
		}else{
			if(anamnesisCheck.getSort_order() == null){
				GWT.log("anamnesisCheck.getSort_order() is null ");
				requests.anamnesisCheckRequestNonRoo().insertNewSortOder(-1).using(anamnesisCheck).fire(new Receiver<Void>() {
					public void onFailure(ServerFailure error) {
						GWT.log("insertNewSortOder error = "+error);

					}
					
					@Override
					public void onSuccess(Void response) {
						gotoDetailsPlace();

					}
				});
			}else{
				GWT.log("no selected previos qustion");
				if(anamnesisCheck.getTitle()!=null&&anamnesisCheck.getTitle().getSort_order() > anamnesisCheck.getSort_order()){
					requests.anamnesisCheckRequestNonRoo().orderDownByPrevious(-1).using(anamnesisCheck).fire(new Receiver<Void>() {
						public void onFailure(ServerFailure error) {
							Log.error(error.getMessage());

						}
						
						@Override
						public void onSuccess(Void response) {
							gotoDetailsPlace();
						}
					});
				}else if(anamnesisCheck.getTitle()!=null&&anamnesisCheck.getTitle().getSort_order() < anamnesisCheck.getSort_order()-1){
					requests.anamnesisCheckRequestNonRoo().orderUpByPrevious(-1).using(anamnesisCheck).fire(new Receiver<Void>() {
						public void onFailure(ServerFailure error) {
							Log.error(error.getMessage());

						}
						
						@Override
						public void onSuccess(Void response) {
							gotoDetailsPlace();

						}
					});
				}else{
					gotoDetailsPlace();
					}
			}
		}
	}

	
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if (this.place.getOperation() == Operation.EDIT)
			placeController.goTo(new AnamnesisCheckDetailsPlace(anamnesisCheck.stableId(),
					Operation.DETAILS));
		else
			placeController.goTo(new AnamnesisCheckPlace("AnamnesisCheckPlace!CANCEL"));
	}
	


	@SuppressWarnings("deprecation")
	@Override
	public void saveClicked() {
		Log.info("saveClicked");
		request = (AnamnesisCheckRequest) editorDriver.flush();
		anamnesisCheck = request.edit(anamnesisCheck);
		
		anamnesisCheck.setValue(view.getValue());
		
		Long selectedInsideTitle = -1L;
		if (view.getSelectedInsideTitle() != null && !view.getSelectedInsideTitle().equals("") && view.getSelectedInsideTitle().length() != 0) {
			selectedInsideTitle = Long.valueOf(view.getSelectedInsideTitle());
		}
			requests.anamnesisCheckRequest().findAnamnesisCheck(selectedInsideTitle).with("anamnesisForm","title").fire(new Receiver<AnamnesisCheckProxy>() {
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());

				}

				@Override
				public void onSuccess(AnamnesisCheckProxy response) {
					
					anamnesisCheck.setTitle(response);					
					
					//if anamnesisCheck type is title , should clear the value and title field
					if(anamnesisCheck.getType() == AnamnesisCheckTypes.QUESTION_TITLE){
					anamnesisCheck.setValue("");
					anamnesisCheck.setTitle(null);
					}
					
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

							// TODO mcAppFactory.getErrorPanel().setErrorMessage(message);
						}
						
						@Override
						public void onSuccess(Void response) {
							Log.info("AnamnesisCheck successfully saved.");

							save = true;

							sortOderByPrevious();
							placeController.goTo(new AnamnesisCheckDetailsPlace(anamnesisCheck.stableId(), Operation.NEW));
						}
					});
					
				}
			});
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void changePreviousQuestion(final AnamnesisCheckTypes anamnesisCheckType, String seletedTitleId) {
		
		if(seletedTitleId!=null&&!seletedTitleId.equals("")&&seletedTitleId.length()!=0){
			requests.anamnesisCheckRequest().findAnamnesisCheck(Long.valueOf(seletedTitleId)).fire(new Receiver<AnamnesisCheckProxy>() {
				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());

				}

				@Override
				public void onSuccess(AnamnesisCheckProxy response) {
				
					getQuestionsByselected(anamnesisCheckType,response);
				}
			});
			
		}else{
			getQuestionsByselected(anamnesisCheckType,null);
		}
	}
	
}
