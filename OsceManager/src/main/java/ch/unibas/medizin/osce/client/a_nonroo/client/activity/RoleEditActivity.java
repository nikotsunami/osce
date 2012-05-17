package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.activity.RoleDetailsActivity.InitializeActivityReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleRequest;
import ch.unibas.medizin.osce.domain.RoleTopic;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class RoleEditActivity extends AbstractActivity implements RoleEditView.Presenter, RoleEditView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleEditView view;
	private RoleDetailsPlace place;	
	public static RoleTopicProxy roleTopic;
	//vigna
	public static RoleActivity roleActivity;

	private StandardizedRoleRequest standardizedRoleRequest;

	private RequestFactoryEditorDriver<StandardizedRoleProxy, RoleEditViewImpl> editorDriver;
	private StandardizedRoleProxy standardizedRole;
	
	public StandardizedRoleProxy getStandardizedRole() {
		return standardizedRole;
	}

	public void setStandardizedRole(StandardizedRoleProxy standardizedRole) {
		this.standardizedRole = standardizedRole;
	}


	private boolean save;
	
	

	public RoleEditActivity(RoleDetailsPlace place,	OsMaRequestFactory requests, PlaceController placeController) 
	{
		Log.info("==Call RoleEditActivity==");
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public RoleEditActivity(RoleDetailsPlace place,
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
		Log.info("==Call RoleEditActivity start();==");		
		this.view = new RoleEditViewImpl();
		this.widget = panel;
		editorDriver = view.createEditorDriver();
		view.setDelegate(this);
		/*
		 * eventBus.addHandler(PlaceChangeEvent.TYPE, new
		 * PlaceChangeEvent.Handler() { public void
		 * onPlaceChange(PlaceChangeEvent event) { // TODO ??? => profit! } });
		 * 
		 * 
		 * eventBus.addHandler(PlaceChangeEvent.TYPE, new
		 * PlaceChangeEvent.Handler() { public void
		 * onPlaceChange(PlaceChangeEvent event) {
		 * //updateSelection(event.getNewPlace()); // TODO implement } });
		 */

		if (this.place.getOperation() == Operation.EDIT) {
			Log.info("edit");
			// requests.find(place.getProxyId()).with("standardizedRole").fire(new
			// Receiver<Object>() {
			// requests.find(place.getProxyId()).with("standardizedRoles").fire(new
			// InitializeActivityReceiver());
			requests.find(place.getProxyId()).with("standardizedRoles")
					.fire(new Receiver<Object>() {

						public void onFailure(ServerFailure error) {
							Log.error(error.getMessage());
						}

						@Override
						public void onSuccess(Object response) {
							if (response instanceof StandardizedRoleProxy) {
								Log.info(((StandardizedRoleProxy) response)
										.getShortName());
								// init((StandardizedPatientProxy) response);
								standardizedRole = (StandardizedRoleProxy) response;
								view.setStandardizedRoleProxy(standardizedRole);
								
								init();
							}
						}
					});
		} else {
			Log.info("new StandardizedRole");
			init();
		}
		widget.setWidget(view.asWidget());
	}

	private void init() {

		StandardizedRoleRequest request = requests.standardizedRoleRequest();
		// DescriptionRequest descriptionRequest =
		// requests.descriptionRequest();

		if (standardizedRole == null) {
			System.out.println("====================standardizedRole=null in RoleEditActivity==================");
			standardizedRole = request.create(StandardizedRoleProxy.class);
			standardizedRole.setSubVersion(1);
			standardizedRole.setMainVersion(1);
			view.setEditTitle(false);
			Log.info("create");
		} else {
			//set TabText when edit clicked
			view.getRoleDetailPanel().getTabBar().setTabText(RoleDetailsActivity.getSelecTab(), standardizedRole.getShortName());
			System.out
					.println("====================standardizedRole not null in RoleEditActivity=============");
			standardizedRole = request.edit(standardizedRole);
			
			
			view.setEditTitle(true);
			Log.info("edit");
		}

		Log.info("edit");
		editorDriver.edit(standardizedRole, request);

		Log.info("persist");
		request.persist().using(standardizedRole);

		Log.debug("Create für: " + standardizedRole.getLongName());
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void cancelClicked() 
	{
		
		
		
		if (this.place.getOperation() == Operation.EDIT)
			goTo(new RoleDetailsPlace(roleTopic.stableId(),	Operation.DETAILS));	
			//placeController.goTo(new RoleDetailsPlace(standardizedRole.stableId(), Operation.DETAILS));
		else
			placeController.goTo(new RolePlace("RolePlace!CANCEL"));
	}

	@Override
	public void saveClicked()
	{
		Log.info("saveClicked");
		
		Log.info("Long Name "+standardizedRole.getLongName());
		
		Log.info("prev version"+standardizedRole.getPreviousVersion());
		
		
		
		standardizedRole.setRoleTopic(roleTopic
				
				);
		Log.info("Role Topic"+standardizedRole.getRoleTopic().getName());
		
		// return '0' means minor clicked and '1' means Major Button Clicked
		
		if(this.place.getOperation() == Operation.EDIT)
		{
			int majorMinor=view.getMajorMinorChange();
		
			if(majorMinor==0)//minor button clicked
			{
				standardizedRole.setSubVersion(standardizedRole.getSubVersion()+1);
			}
			else	//major button clicked
			{
				StandardizedRoleRequest request = requests.standardizedRoleRequest();
				StandardizedRoleProxy newStandardizedRole=standardizedRole;
				standardizedRole=null;
				standardizedRole = request.create(StandardizedRoleProxy.class);
				
				
				//copy value from newStandardizedRole to standardizedRole
				
				//copy end
			}
		}
		// description.setDescription(descriptionView.getDescriptionContent());
		// bankaccountDriver.flush();
		
		editorDriver.flush().fire(new Receiver<Void>() {

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
				Log.warn(" in Role -" + message);
			}

			@Override
			public void onSuccess(Void response) {
				Log.info("Role successfully saved.");

				save = true;
				// placeController.goTo(new
				// RoleDetailsPlace(standardizedRole.stableId(),Operation.DETAILS));
				// vigna
				// placeController.goTo(new
				// RoleDetailsPlace(Operation.DETAILS)); //ADDED
				
				RoleDetailsActivity.setSelecTab(findTabIndex());
				roleActivity.init2("");
				goTo(new RoleDetailsPlace(roleTopic.stableId(),	Operation.DETAILS));				
				// saveDescription();
			}
		});
	}
	
	
	public int findTabIndex()
	{
		int i=0;
		
		Iterator<StandardizedRoleProxy> iterator=roleTopic.getStandardizedRoles().iterator();
		
		while(iterator.hasNext())
		{
			
			if(iterator.next().getId().equals(standardizedRole.getId()))
			{
				
				break;
			}
			i++;
		}
		if(i==roleTopic.getStandardizedRoles().size())
			i=0;
		return i;
	}

	// private void saveDescription() {
	// // TODO: bug(2011-11-12) - description is NOT saved the first time!
	//
	// descriptionDriver.flush().fire(new Receiver<Void>() {
	//
	// @Override
	// public void onSuccess(Void response) {
	// Log.info("Description successfully saved.");
	//
	// placeController.goTo(new
	// StandardizedPatientDetailsPlace(standardizedPatient.stableId(),
	// StandardizedPatientDetailsPlace.Operation.DETAILS));
	// }
	//
	// public void onFailure(ServerFailure error){
	// Log.error(error.getMessage());
	// }
	//
	// });
	//
	// save = true;
	// }

	/*
	 * private class InitializeActivityReceiver extends Receiver<Object> {
	 * 
	 * @Override public void onFailure(ServerFailure error){
	 * Log.error(error.getMessage()); }
	 * 
	 * @Override public void onSuccess(Object response) { if(response instanceof
	 * StandardizedRoleProxy){ Log.info(((StandardizedRoleProxy)
	 * response).getRoleType()); standardizedRole = (StandardizedRoleProxy)
	 * response; //init(); } } }
	 */

}
