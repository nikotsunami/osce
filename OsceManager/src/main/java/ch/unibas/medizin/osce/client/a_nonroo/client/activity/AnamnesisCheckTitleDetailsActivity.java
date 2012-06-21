package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckTitleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckTitleDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckTitleDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public class AnamnesisCheckTitleDetailsActivity extends AbstractActivity
		implements AnamnesisCheckTitleDetailsView.Presenter,
		AnamnesisCheckTitleDetailsView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private AnamnesisCheckTitleDetailsView view;
	private CellTable<AnamnesisCheckTitleProxy> table;
	private SingleSelectionModel<AnamnesisCheckTitleProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;

	private AnamnesisCheckTitleDetailsPlace place;
	private AnamnesisCheckTitleProxy anamnesisCheckTitleProxy;

	public AnamnesisCheckTitleDetailsActivity(
			AnamnesisCheckTitleDetailsPlace place, OsMaRequestFactory requests,
			PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public void onStop() {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
//		Window.alert("!!!!!!!!!!! String.valueOf(anamnesisCheckProxy.getId()) ");
		AnamnesisCheckTitleDetailsView anamnesisCheckDetailsView = new AnamnesisCheckTitleDetailsViewImpl();
		anamnesisCheckDetailsView.setPresenter(this);

		this.widget = panel;
		this.view = anamnesisCheckDetailsView;
		widget.setWidget(anamnesisCheckDetailsView.asWidget());

		view.setDelegate(this);
	
		requests.find(place.getProxyId()).fire(new Receiver<Object>() {

			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}

			@Override
			public void onSuccess(Object response) {
				if (response instanceof AnamnesisCheckTitleProxy) {
//					Window.alert("!!!!!!!!!!! String.valueOf(anamnesisCheckProxy.getId()) ");
					// Log.info(((AnamnesisCheckTitleProxy)
					// response).getId().toString());
					final AnamnesisCheckTitleProxy anamnesisCheckProxy = (AnamnesisCheckTitleProxy) response;
					requests.anamnesisCheckTitleRequest().findAnamnesisCheckTitle(anamnesisCheckProxy.getId()).fire(new Receiver<AnamnesisCheckTitleProxy>(){

						@Override
						public void onSuccess(AnamnesisCheckTitleProxy response) {
							// TODO Auto-generated method stub
							String titleId="";
							if(response!=null){
//								Window.alert("!!!!!!!!!!!!!!!!!!!!!!! String.valueOf(anamnesisCheckProxy.getId()) "+String.valueOf(anamnesisCheckProxy.getId()));
								titleId=String.valueOf(response.getId());
								
							}
							init(anamnesisCheckProxy,String.valueOf(response.getId()));
							
						}
						
					});
					
					
				}
			}
		});
	}

	private void init(AnamnesisCheckTitleProxy anamnesisCheckTitleProxy,String titleId) {
		this.anamnesisCheckTitleProxy = anamnesisCheckTitleProxy;
		view.setValue(anamnesisCheckTitleProxy,titleId);
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void editClicked() {
		// Log.info("edit clicked");
		if (anamnesisCheckTitleProxy != null) {
			goTo(new AnamnesisCheckTitleDetailsPlace(
					anamnesisCheckTitleProxy.stableId(), Operation.EDIT));
		}
	}

	@Override
	public void deleteClicked() {
//		if (!Window
//				.confirm("Really delete this entry? You cannot undo this change.")) {
//			return;
//		}
		//
		requests.anamnesisCheckTitleRequest().remove()
				.using(anamnesisCheckTitleProxy).fire(new Receiver<Void>() {

					public void onSuccess(Void ignore) {
						// if (widget == null) {
						// return;
						if (widget != null) {
							widget.setWidget(null);
						}
						if (!Window
								.confirm("Really delete this entry? You cannot undo this change.")) {
							return;
						}
						placeController.goTo(new AnamnesisCheckPlace());
						// }

					}
					public void onFailure(ServerFailure error) {
						Window.alert("Don't delete this Title");
					}

				});
	}
}
