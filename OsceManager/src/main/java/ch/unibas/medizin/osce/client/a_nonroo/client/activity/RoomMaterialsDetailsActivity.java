package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.MaterialListRequest;
import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.PriceType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.RangeChangeEvent;

@SuppressWarnings("deprecation")
public class RoomMaterialsDetailsActivity extends AbstractActivity implements
		RoomMaterialsDetailsView.Presenter, RoomMaterialsDetailsView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoomMaterialsDetailsView view;
	private RoomMaterialsDetailsPlace place;
	private MaterialListProxy materialListProxy;
	private OsceConstants constants;

	private HandlerRegistration rangeChangeHandler;

	public static RoomMaterialsActivity roomMaterialsActivity;

	public RoomMaterialsDetailsActivity(RoomMaterialsDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public void onStop() {

	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoomMaterialsDetailsActivity.start()");
		RoomMaterialsDetailsView roomMaterialsDetailsView = new RoomMaterialsDetailsViewImpl();
		roomMaterialsDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = roomMaterialsDetailsView;
		this.constants = GWT.create(OsceConstants.class);
		
		widget.setWidget(roomMaterialsDetailsView.asWidget());

		view.setDelegate(this);

		requests.find(place.getProxyId())
				.fire(new InitializeActivityReceiver());

	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	private class InitializeActivityReceiver extends Receiver<Object> {
		@Override
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
			Log.error("Error accored in receiving Specialisation proxy");
			System.out
					.println("Error accored in receiving Specialisation proxy");
		}

		@Override
		public void onSuccess(Object response) {
			System.out.println("Cheking whter come Specialization proxy");
			if (response instanceof MaterialListProxy) {
				System.out
						.println("Obtained Specialisation proxy successfully");
				Log.info(((MaterialListProxy) response).getName());
				materialListProxy = (MaterialListProxy) response;
				init();

			}
		}
	}

	public void newClicked(final String name, final MaterialType materialType,
			final Integer price, final PriceType priceType) {

		Log.debug("MaterialList Adding");
		requests.find(place.getProxyId()).fire(new Receiver<Object>() {
			// spReq.persist().using(specialisationProxy).fire(new
			// Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error) {
				Log.error("onFilure");
				Log.error(error.getMessage());
			}

			@Override
			public void onSuccess(Object object) {
				System.out.println("Save RoleTopic values value Succesfully1");
				if (object instanceof MaterialListProxy) {
					MaterialListRequest materialListRequest = requests
							.materialListRequest();
					MaterialListProxy materialListProxy = materialListRequest
							.create(MaterialListProxy.class);

					materialListProxy.setName(name);
					materialListProxy.setType(materialType);
					materialListProxy.setPrice(price);
					materialListProxy.setPriceType(priceType);

					Log.debug("materialListProxy : "
							+ materialListProxy.getName());
					Log.debug("materialListProxy : "
							+ materialListProxy.getVersion());
					Log.debug("materialListProxy : "
							+ materialListProxy.stableId());

					materialListRequest.persist().using(materialListProxy)
							.fire(new Receiver<Void>() {

								@Override
								public void onFailure(ServerFailure error) {
									Log.error("onFilure");
									Log.error(error.getMessage());
								}

								@Override
								public void onSuccess(Void arg0) {
									System.out
											.println("Save RoleTopic values value Succesfully");

									init();

								}
							});
				}
			}
		});

		Log.debug("After persist");
	}

	// @SPEC To add data in table
	private void init() {

		System.out.println("Inside INIT()");

		Log.info("materialListProxy id : " + materialListProxy.getId());
		Log.info("materialListProxy name : " + materialListProxy.getName());
		view.setValue(materialListProxy);

		if (roomMaterialsActivity != null) {
			System.out.println("calling Perform search from Room details:");

			roomMaterialsActivity.performSearch("");
		}
	}

	// public void deleteClicked(MaterialListProxy materialListProxy) {
	// System.out.println("Come to delete recore");
	//
	// requests.materialListRequest().remove().using(materialListProxy)
	// .fire(new Receiver<Void>() {
	// public void onSuccess(Void ignore) {
	// Log.debug("Sucessfully deleted");
	// init();
	// }
	// });
	//
	// }

	@Override
	public void editClicked() {
		Log.info("edit clicked");
		if (materialListProxy != null) {
			goTo(new RoomMaterialsDetailsPlace(materialListProxy.stableId(),
					Operation.EDIT));
		}
	}

	@Override
	public void deleteClicked() {
	/*	if (!Window.confirm("Really delete this entry? You cannot undo this change.")) 
		{
			Log.info("Return from deleted details");
			return;
		}*/
		// Issue Role
				 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
				 dialogBox.showYesNoDialog(constants.reallyDelete());
				 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) 
						{
							dialogBox.hide();							
							Log.info("yes click");	
							requests.materialListRequest().remove().using(materialListProxy)
							.fire(new Receiver<Void>() {

								public void onSuccess(Void ignore) {
									Log.debug("Sucessfully deleted");
									init();
									placeController.goTo(new RoomMaterialsPlace("RoomMaterialsPlace"));									
									// placeController.goTo(new RoomMaterialsDetailsPlace(
									// "RoomMaterialsDetailsPlace!DELETED"));

								}
							});
							return;

								}						
							});

					dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();
							Log.info("no click");
							return;
							
						}
					});
				// E: Issue Role		
	
	}

}
