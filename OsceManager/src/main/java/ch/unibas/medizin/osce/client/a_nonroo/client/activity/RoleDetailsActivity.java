package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Iterator;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * @author dk
 * 
 */
public class RoleDetailsActivity extends AbstractActivity implements RoleDetailsView.Presenter, 
RoleDetailsView.Delegate,
StandardizedRoleDetailsView.Delegate

{

	
	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleDetailsView view; // --
	private RoleDetailsPlace place;
	private StandardizedRoleProxy standardizedRoleProxy;
	private TabPanel roleDetailTabPanel;
	public Iterator<StandardizedRoleProxy> stRoleIterator;
	public RoleDetailsActivity roleDetailActivity;
	public StandardizedRoleDetailsViewImpl[] standardizedRoleDetailsView;
	/**
	 * 
	 */
	private static int selecTab=0;
	
	
	
	public static int getSelecTab() {
		return selecTab;
	}

	public static void setSelecTab(int selecTab) {
		RoleDetailsActivity.selecTab = selecTab;
	}

	public RoleDetailsActivity(RoleDetailsPlace place,OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
		
		
				
	}
	
	public void onStop()
	{
		selecTab=0;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleDetailsActivity.start()");
		RoleDetailsView roleDetailsView = new RoleDetailsViewImpl();
		roleDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = roleDetailsView;
		this.roleDetailActivity=this;
					
		roleDetailTabPanel=view.getRoleDetailTabPanel();	
		widget.setWidget(roleDetailsView.asWidget());
		view.setDelegate(this);		
				
		requests.find(place.getProxyId()).with("standardizedRoles").fire(new InitializeActivityReceiver());		
					
	}
	public class InitializeActivityReceiver extends Receiver<Object> 
	{
		@Override
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
		}

		@Override
		public void onSuccess(Object response) {			
			System.out.println("==================================Call onSuccess Method==================================");
			
			
			//Remove InAcive Roles from the role topic
		/*	if (response instanceof RoleTopicProxy) 
			{						
				if(((RoleTopicProxy)response).getStandardizedRoles()!=null)
				{
					
					
					Iterator<StandardizedRoleProxy> iterator=((RoleTopicProxy)response).getStandardizedRoles().iterator();
					
					while(iterator.hasNext())
					{
						StandardizedRoleProxy srp=iterator.next();
						if(srp.getActive()==false)
						{
							((RoleTopicProxy)response).getStandardizedRoles().remove(srp);
						}
					}
				}
			}
			*/
			if (response instanceof RoleTopicProxy) 
			{						
				if(((RoleTopicProxy)response).getStandardizedRoles()!=null)
				{
					System.out.println("Success Role Found");													
					System.out.println("Size: "+((RoleTopicProxy)response).getStandardizedRoles().size()); // Return Size of Data
					
					int index=0;	// Array Object bIndex (SET VALUE TO OBJECT)
					//int objcnt=-1;	// Total Objects	(INITIALIZE THE OBJECT)
					int totalrole=roleDetailTabPanel.getWidgetCount(); // Total Role Tabs
					int size=((RoleTopicProxy)response).getStandardizedRoles().size(); // Total Size of Data (standardized_role)
					System.out.println("Total Role: " + totalrole);							
					//roleDetailTabPanel.insert(standardizedRoleDetailsView,"Test Tab", totalTab-1);
					standardizedRoleDetailsView=new StandardizedRoleDetailsViewImpl[size];				
					stRoleIterator = ((RoleTopicProxy)response).getStandardizedRoles().iterator();						
					while(stRoleIterator.hasNext())
					{						
											
						standardizedRoleDetailsView[index] = new StandardizedRoleDetailsViewImpl();																		
						StandardizedRoleProxy proxy=stRoleIterator.next();						
						standardizedRoleDetailsView[index].setValue(proxy);	//nalim
						
						roleDetailTabPanel.insert(standardizedRoleDetailsView[index],""+proxy.getShortName(),index );
						standardizedRoleDetailsView[index].shortName.setInnerText(proxy.getShortName() == null ? "" : String.valueOf(proxy.getShortName()));
						standardizedRoleDetailsView[index].longName.setInnerText(proxy.getLongName() == null ? "" : String.valueOf(proxy.getLongName()));
						standardizedRoleDetailsView[index].roleType.setInnerText(proxy.getRoleType().name()); //ADDED
						standardizedRoleDetailsView[index].studyYear.setInnerText(proxy.getStudyYear().name()); //ADDED
						standardizedRoleDetailsView[index].labelLongNameHeader.setText(""+proxy.getLongName());
						
						setRoleDetailTabData(proxy,response,index);																	
						standardizedRoleDetailsView[index].rolePanel.selectTab(0);
						standardizedRoleDetailsView[index].setDelegate(roleDetailActivity);
						index++;
						
						
					}	
					roleDetailTabPanel.selectTab(selecTab);
					view.setStandardizedRoleDetailsViewImpl((StandardizedRoleDetailsViewImpl[])standardizedRoleDetailsView);
					
				}
				else
				{
					System.out.println("Sorry No Roles Aveilable");
				}
								
				init(((RoleTopicProxy)response));
			}		
		}
	}
	
	public void setRoleDetailTabData(StandardizedRoleProxy proxy,Object response,int index)
	{				
		System.out.println("===============================>"+ ""+index+ proxy.getShortName());
		System.out.println("===============================>"+ ""+index+ proxy.getLongName() );		
		System.out.println("===============================>"+ ""+index+ proxy.getRoleType().valueOf(proxy.getRoleType().name()));		
		System.out.println("===============================>"+ ""+index+ proxy.getStudyYear().valueOf(proxy.getStudyYear().name()));
	}

	private void init(RoleTopicProxy proxy) {
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void printRoleClicked(){
		Log.info("Print clicked");
		/*requests.standardizedPatientRequestNonRoo().getPdfPatientsBySearch(standardizedPatientProxy).fire(new StandardizedPatientPdfFileReceiver());*/
	}
	
	@Override
	public void editRoleClicked(StandardizedRoleProxy standardizedRoleProxy) {
		Log.info("edit clicked");
		System.out.println("============================Jump to StandardizedPatientDetailActivity editPatientClicked() =========================");
		System.out.println("==>"+roleDetailTabPanel.getTabBar().getSelectedTab());
		int selTabID=roleDetailTabPanel.getTabBar().getSelectedTab();
		goTo(new RoleDetailsPlace(standardizedRoleProxy.stableId(),Operation.EDIT));
	}
	
	@Override
	public void deleteRoleClicked(StandardizedRoleProxy proxy) {
		Log.info("delete clicked");
		
		if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
            return;
        }
		
        requests.standardizedRoleRequest().remove().using(proxy).fire(new Receiver<Void>() 
		{
            public void onSuccess(Void ignore) {
                if (widget == null) {
                    return;
                }
                RoleEditActivity.roleActivity.initSearch();
                goTo(new RoleDetailsPlace(RoleEditActivity.roleTopic.stableId(),	Operation.DETAILS));	
                
                //placeController.goTo(new RoleDetailsPlace("StandardizedRolePlace!DELETED"));
                //placeController.goTo(new RolePlace(Operation.DETAILS));
                
            }
        });
		
	}

	/*@Override
	public void deleteRoleClicked(StandardizedRoleProxy standardizedRoleProxy() {
		
		
	}*/
	
	/*@Override
	public void createRole(StandardizedRoleProxy standardizedRoleProxy) 
	{		
		
		System.out.println("Call createRole() of RoleDetailActivity");				
		goTo(new RoleDetailsPlace(standardizedRoleProxy.stableId(),	Operation.CREATE));
	}*/

	@Override
	public void editClicked() {
		System.out.println("Call editClicked() of RoleDetailActivity");		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createRole() 
	{
		System.out.println("Call createRole() of RoleDetailActivity");				
		goTo(new RoleDetailsPlace(	Operation.CREATE));
	}
	
	
	

}
