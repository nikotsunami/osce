package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoomMaterialsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.MaterialListRequest;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class RoomMaterialsEditActivity extends AbstractActivity implements
		RoomMaterialsEditView.Presenter, RoomMaterialsEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoomMaterialsEditView view;
	private RoomMaterialsDetailsPlace place;
	private OsceConstants constants = GWT.create(OsceConstants.class);

	private RequestFactoryEditorDriver<MaterialListProxy, RoomMaterialsEditViewImpl> editorDriver;
	private MaterialListProxy materialList;

	private boolean save;

	public RoomMaterialsEditActivity(RoomMaterialsDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public RoomMaterialsEditActivity(RoomMaterialsDetailsPlace place,
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
			return constants.changesDiscarded();
		else
			return null;
	}

	// use this to check if some value has changed since editing has started
	private boolean changed() {
		return editorDriver != null && editorDriver.flush().isChanged();
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("start");
		this.view = new RoomMaterialsEditViewImpl();
		this.widget = panel;

		editorDriver = view.createEditorDriver();

		view.setDelegate(this);

		if (this.place.getOperation() == Operation.CREATE) {
			Log.info("new MaterialListProxy");
			// RoomMaterialsPlace.setProxyId(RoomMaterials.stableId());
			init();
		} else {
			Log.info("edit");
			requests.find(place.getProxyId()).fire(new Receiver<Object>() {

				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
				}

				@Override
				public void onSuccess(Object response) {
					if (response instanceof MaterialListProxy) {
						Log.info(((MaterialListProxy) response).getName());
						// init((RoomMaterialsProxy) response);
						materialList = (MaterialListProxy) response;
						init();
					}
				}
			});
		}

		// view.
		// view.initialiseDriver(requests);
		widget.setWidget(view.asWidget());

	}

	private void init() {

		MaterialListRequest request = requests.materialListRequest();
		// DescriptionRequest descriptionRequest =
		// requests.descriptionRequest();

		if (materialList == null) {
			materialList = request.create(MaterialListProxy.class);
			// editorDriver.edit(RoomMaterials, request);
			view.setEditTitle(false);
			Log.info("create");
		} else {

			materialList = request.edit(materialList);
			view.setEditTitle(true);
			Log.info("edit");
		}

		Log.info("edit");
		editorDriver.edit(materialList, request);

		Log.info("persist");
		request.persist().using(materialList);
		// request.persist().using(description);

		Log.info("flush");
		// editorDriver.flush();
		// descriptionDriver.flush();

		Log.debug("Create für: " + materialList.getName());
	}

	private List<Integer> getIntegerList(int minValue, int maxValue) {
		List<Integer> values = new ArrayList<Integer>();
		for (int i = minValue; i <= maxValue; i++) {
			values.add(new Integer(i));
		}
		return values;
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() {
		if (this.place.getOperation() == Operation.EDIT)
			placeController.goTo(new RoomMaterialsDetailsPlace(materialList
					.stableId(), Operation.DETAILS));
		else
			placeController.goTo(new RoomMaterialsPlace(
					"RoomMaterialsPlace!CANCEL"));

		if (RoomMaterialsDetailsActivity.roomMaterialsActivity != null) {
			RoomMaterialsDetailsActivity.roomMaterialsActivity
					.performSearch("");
		}
	}

	@Override
	public void saveClicked() {
		Log.info("saveClicked");


		// Violation Changes Highlight

		materialList.setType(view.getType());
		materialList.setPrice(view.getPrice());
		materialList.setPriceType(view.getPriceType());

	/*	if (view.getName() == null || (view.getName().compareTo("") == 0)) 
		{
			Window.confirm(OsMaConstant.BLANK_FIELD_MESSAGE + " Name");
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Success");
			 dialogBox.showConfirmationDialog(OsMaConstant.BLANK_FIELD_MESSAGE + " Name");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
					return;
						}
					});

			
			
		//E: Issue Role
			return;
		} 
		else 
		{
			materialList.setName(view.getName());			
		}
		if (view.getType() == null	|| (view.getType().toString().compareTo("") == 0)) 
		{
			Window.confirm(OsMaConstant.BLANK_FIELD_MESSAGE + " Type");
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Success");
			 dialogBox.showConfirmationDialog(OsMaConstant.BLANK_FIELD_MESSAGE + " Type");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
					return;
						}
					});

			
			
		//E: Issue Role
			return;
		} 
		else 
		{
			materialList.setType(view.getType());
			
		}
		if (view.getPrice() == null
				|| (view.getPrice().toString().compareTo("") == 0)) 
		{
			Window.confirm(OsMaConstant.BLANK_FIELD_MESSAGE + " Price");
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Success");
			 dialogBox.showConfirmationDialog(OsMaConstant.BLANK_FIELD_MESSAGE + " Price");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
					return;
						}
					});

			
			
		//E: Issue Role
			return;
		} 
		else 
		{
			materialList.setPrice(view.getPrice());			
		}
		if (view.getPriceType() == null
				|| (view.getPriceType().toString().compareTo("") == 0)) 
		{
			Window.confirm(OsMaConstant.BLANK_FIELD_MESSAGE + " PriceType");
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Success");
			 dialogBox.showConfirmationDialog(OsMaConstant.BLANK_FIELD_MESSAGE + " PriceType");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
					return;
						}
					});

			
			
		//E: Issue Role
			return;
		} 
		else 
		{
			materialList.setPriceType(view.getPriceType());
		}*/
		
	
		Log.info("Map Size: " + view.getEditViewMap().size());
		editorDriver.flush().fire(new OSCEReceiver<Void>(view.getEditViewMap()) {
		// E Violation Changes Highlight
			public void onFailure(ServerFailure error) 
			{
				Log.error(error.getMessage());

			}
			// Violation Changes Highlight

			/*@Override
			public void onViolation(Set<Violation> errors) {
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(" in RoomMaterials -" + message);
			}*/
			// E Violation Changes Highlight

			@Override
			public void onSuccess(Void response) {
				Log.info("RoomMaterials successfully saved.");

				save = true;
				placeController.goTo(new RoomMaterialsDetailsPlace(materialList
						.stableId(), Operation.NEW));

				// saveDescription();
			}
		});

		if (RoomMaterialsDetailsActivity.roomMaterialsActivity != null) {
			RoomMaterialsDetailsActivity.roomMaterialsActivity
					.performSearch("");
		}
	}

}
