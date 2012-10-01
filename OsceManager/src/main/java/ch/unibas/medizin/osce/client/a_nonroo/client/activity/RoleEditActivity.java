package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.RolePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleEditCheckListSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleEditCheckListSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleEditViewImpl;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.CheckListRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleRequest;
import ch.unibas.medizin.osce.client.managed.ui.RoleTopicProxyRenderer;
import ch.unibas.medizin.osce.domain.AdvancedSearchCriteria;
import ch.unibas.medizin.osce.domain.ChecklistTopic;
import ch.unibas.medizin.osce.domain.RoleParticipant;
import ch.unibas.medizin.osce.domain.RoleTemplate;
import ch.unibas.medizin.osce.domain.SimpleSearchCriteria;
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
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class RoleEditActivity extends AbstractActivity implements RoleEditView.Presenter, RoleEditView.Delegate,RoleEditCheckListSubView.Delegate,RoleEditCheckListSubView.Presenter {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleEditView view;
	private RoleEditCheckListSubView checkListView;//spec
	
	private RoleDetailsPlace place;	
	public static RoleTopicProxy roleTopic;
	public static RoleTopicProxy oldRoleTopic;
	
	private static int selectedTab;
	
	public static void setSelectedTab(int selectedTab) {
		RoleEditActivity.selectedTab = selectedTab;
	}


	//vigna
	public static RoleActivity roleActivity;

	private StandardizedRoleRequest standardizedRoleRequest;

	private RequestFactoryEditorDriver<StandardizedRoleProxy, RoleEditViewImpl> editorDriver;
	
	private RequestFactoryEditorDriver<CheckListProxy, RoleEditCheckListSubViewImpl> checkListEditorDriver;//spec
	
	
	
	
	
	private RequestFactoryEditorDriver<StandardizedRoleProxy, RoleEditViewImpl> majoreditorDriver;
	
	
	private StandardizedRoleProxy standardizedRole;
	private StandardizedRoleProxy standardizedRole1;
	private CheckListProxy checkListProxy;//spec
	
	private StandardizedRoleProxy  proxy;//spec
	private StandardizedRoleProxy  oldProxy;//spec
	private CheckListProxy  checkListProxy1;//spec
	
	private CheckListProxy checkList;//spec
	
	private MessageConfirmationDialogBox confirmationDialogBox;
	
	private StandardizedRoleRequest majorRequest;
	private CheckListRequest majorCheckListRequest;//spec
	private StandardizedRoleRequest majorRequest1;
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	public StandardizedRoleProxy getProxy() {
		return proxy;
	}

	public void setProxy(StandardizedRoleProxy proxy) {
		this.proxy = proxy;
	}

	
	
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
		Log.info("==Call RoleEditActivity start();==");		
		this.view = new RoleEditViewImpl();

		
		this.widget = panel;
	
		//spec start
		this.checkListView=new RoleEditCheckListSubViewImpl();
		editorDriver = view.createEditorDriver();
		this.view.getRoleEditCheckListPanel().add(this.checkListView);		
			checkListEditorDriver=checkListView.createCheckListEditorDriver();//spec
			checkListView.setDelegate(this);//spec
				
	//spec end
		view.setDelegate(this);
		
		
	//	view.setDelegate(this);
		
		
	

		if (this.place.getOperation() == Operation.EDIT) {
			Log.info("edit");
			//spec start
			Log.info("Proxy ID : " + place.getProxyId());
			requests.find(place.getProxyId()).with("oscePosts","roleTopic","simpleSearchCriteria","roleParticipants","advancedSearchCriteria","roleTemplate","keywords","previousVersion","checkList","checkList.checkListTopics","checkList.checkListTopics.checkListQuestions","checkList.checkListTopics.checkListQuestions.checkListCriterias","checkList.checkListTopics.checkListQuestions.checkListOptions")
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
								checkListProxy=((StandardizedRoleProxy) response).getCheckList();//spec
								//checkListProxy=standardizedRole.getCheckList();
								view.setStandardizedRoleProxy(standardizedRole);
								
								/* TODO is this right? There is potential for null ptr exception, therefore I
								 * made the request conditional.
								 * -michaelwgnr */
//								if (standardizedRole.getRoleTopic() != null) {
									requests.roleTopicRequestNonRoo().
											findAllRoleTopic(standardizedRole.getRoleTopic().getId().intValue()).
											fire(new RoleTopicRecevier());
//								}
									Log.info("roletopic --"+ roleTopic);
									
									
									//Issue # 122 : Replace pull down with autocomplete.
									roleTopic=((StandardizedRoleProxy) response).getRoleTopic();
									//((RoleEditViewImpl)view).roleTopic.setRenderer(new RoleTopicProxyRenderer());
									((RoleEditViewImpl)view).roleTopic.setRenderer(new AbstractRenderer<RoleTopicProxy>() {

										@Override
										public String render(RoleTopicProxy object) {
											// TODO Auto-generated method stub
											if(object!=null)
											{
												return object.getName();	
											}
											else
											{
												return "";
											}
											
										}
									});

									((RoleEditViewImpl)view).roleTopic.setSelected(roleTopic);
									//Issue # 122 : Replace pull down with autocomplete.
									
									Log.info("value--"+((RoleEditViewImpl)view).roleTopic.getSelected());
									Log.info("roletopic --"+ roleTopic);
								init();
							}
						}
					});
			
			
		} else {
			Log.info("new StandardizedRole");
			//Issue # 122 : Replace pull down with autocomplete.
			((RoleEditViewImpl)view).roleTopic.setVisible(false);
			((RoleEditViewImpl)view).labelRoleTopic.setInnerText("");
			((RoleEditViewImpl)view).roleTopic.setRenderer(new AbstractRenderer<RoleTopicProxy>() {

				@Override
				public String render(RoleTopicProxy object) {
					// TODO Auto-generated method stub
					if(object!=null)
					{
					return object.getName();
					}
					else
					{
						return "";
					}
				}
			});
			//Issue # 122 : Replace pull down with autocomplete.
			//((RoleEditViewImpl)view).roleTopic.setRenderer(new RoleTopicProxyRenderer());
			((RoleEditViewImpl)view).roleTopic.setSelected(roleTopic);
			//Issue # 122 : Replace pull down with autocomplete.
			//((RoleEditViewImpl)view).roleTopic.setValue(roleTopic);
		//	((RoleEditViewImpl)view).roleTopic.setSelected(roleTopic);
			/*((RoleEditViewImpl)view).roleTopic.setSelected(roleTopic);
			((RoleEditViewImpl)view).roleTopic.getTextField().setText(roleTopic.getName());
			((RoleEditViewImpl)view).roleTopic.getTextField().advancedTextBox.setText(roleTopic.getName());
			*/
			//Issue # 122 : Replace pull down with autocomplete.
			init();
		}
		widget.setWidget(view.asWidget());
	}

	
	private class RoleTopicRecevier extends Receiver<List<RoleTopicProxy>> {
		@Override
		public void onSuccess(List<RoleTopicProxy> response) {
		//	filterView.setSpecialisationBoxValues(response);
			Log.debug("roletopic success");
			view.setRoleTopicListBoxValues(response);
			
			
		}
	}
	private void init() {

		StandardizedRoleRequest request = requests.standardizedRoleRequest();
		CheckListRequest checkListRequest=requests.checkListRequest();//spec
	
		

		if (standardizedRole == null) {
			System.out.println("====================standardizedRole=null in RoleEditActivity==================");
			standardizedRole = request.create(StandardizedRoleProxy.class);
			checkListProxy=request.create(CheckListProxy.class);//spec
			standardizedRole.setCheckList(checkListProxy);//spec
			standardizedRole.setSubVersion(1);
			standardizedRole.setMainVersion(1);
			standardizedRole.setActive(true);
			checkListProxy.setVersion(0);//spec
			view.setEditTitle(false);
				
			requests.roleTopicRequest().findAllRoleTopics().fire(new RoleTopicRecevier());
			
			Log.info("Proxy ID : " + place.getProxyId());
				
			requests.find(place.getProxyId()).with("roleTopic")
			.fire(new Receiver<Object>() {

				public void onFailure(ServerFailure error) {
					Log.error(error.getMessage());
				}

				@Override
				public void onSuccess(Object response) {
					if (response instanceof RoleTopicProxy) {
						Log.info(((RoleTopicProxy) response).getName());
						// init((StandardizedPatientProxy) response);
						RoleTopicProxy proxy1;
						 proxy1 = (RoleTopicProxy) response;
						
						Log.info("Role Topic ID : " + proxy1.getId());
						
						RoleEditViewImpl test1 = new RoleEditViewImpl();
						test1 = (RoleEditViewImpl) view;
						//Issue # 122 : Replace pull down with autocomplete.
						//test1.roleTopic.setValue(proxy1);
						//test1.roleTopic.setSelected(proxy1);
						//Issue # 122 : Replace pull down with autocomplete.
						//	requests.roleTopicRequestNonRoo().
						//			findAllRoleTopic(proxy1.getId().intValue()).
						//			fire(new RoleTopicRecevier());
//						
				
					}
				}
			}); 
		//	requests.roleTopicRequest().findAllRoleTopics().fire(new RoleTopicRecevier());
			
			//requests.roleTopicRequestNonRoo().findAllRoleTopic(Integer.parseInt(standardizedRole.getRoleTopic().getId().toString())).fire(new RoleTopicRecevier());
			Log.info("create");
			//Issue # 122 : Replace pull down with autocomplete.
			//((RoleEditViewImpl)view).roleTopic.setValue(this.roleTopic);
			//((RoleEditViewImpl)view).roleTopic.setSelected(this.roleTopic);
			//((RoleEditViewImpl)view).roleTopic.getTextField().advancedTextBox.setValue(this.roleTopic.getName());
			//Issue # 122 : Replace pull down with autocomplete.
		} else {
			//set TabText when edit clicked
			view.getRoleDetailPanel().getTabBar().setTabText(RoleDetailsActivity.getSelecTab(), standardizedRole.getShortName());
			System.out
					.println("====================standardizedRole not null in RoleEditActivity=============");
			standardizedRole = request.edit(standardizedRole);
			
			Log.info("roletopic befor =====--"+ standardizedRole.getRoleTopic());
			
		//	((RoleEditViewImpl)view).roleTopic.setRenderer(new roletopicre)
		//	((RoleEditViewImpl)view).roleTopic.setSelected(standardizedRole.getRoleTopic())
		//	((RoleEditViewImpl)view).roleTopic.setText(standardizedRole.getRoleTopic().getName());
			//((RoleEditViewImpl)view).roleTopic.getTextField().advancedTextBox.setText(standardizedRole.getRoleTopic().getName());
			Log.info("roletopic after--"+ standardizedRole.getRoleTopic());
			
			//spec start
			//checkListProxy=request.edit(standardizedRole.getCheckList());
		
			//spec end
		
			view.setEditTitle(true);
			Log.info("edit");
		}

		Log.info("edit");
		editorDriver.edit(standardizedRole, request);
//spec start
		checkListEditorDriver.edit(checkListProxy, request);//spec
//spec end
		
	
		
	//	checkListRequest.persist().using(checkListProxy);//spec
		
		
		Log.info(" persist");
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
		
		// SPEC Change
		
		if (this.place.getOperation() == Operation.EDIT){
			goTo(new RoleDetailsPlace(roleTopic.stableId(),	Operation.DETAILS));	
			//placeController.goTo(new RoleDetailsPlace(standardizedRole.stableId(), Operation.DETAILS));
		}else{
//			placeController.goTo(new RolePlace("RolePlace!CANCEL"));
			goTo(new RoleDetailsPlace(roleTopic.stableId(),	Operation.DETAILS));
		}
		
		// SPEC Change			
	}

	@Override
	public void saveClicked()
	{
		
		Log.info("saveClicked");
		
		Log.info("Long Name "+standardizedRole.getLongName());
		
		Log.info("prev version"+standardizedRole.getPreviousVersion());
		
		Log.info("Sub version"+standardizedRole.getSubVersion());
		
	//	standardizedRole.setRoleTopic(roleTopic);
		//checkListProxy.setTitle("aaa");
		//checkListProxy.setTitle(((RoleEditCheckListSubViewImpl)checkListView).title.getValue());//spec
		
	//	standardizedRole.setCheckList(checkListProxy);//spec
		if (Log.isInfoEnabled() && standardizedRole.getRoleTopic() != null) {
			Log.info("Role Topic: "+ standardizedRole.getRoleTopic().getName());
		}
		
	//	((RoleEditViewImpl)view).roleTopic.setValue(this.roleTopic);
		//Issue # 122 : Replace pull down with autocomplete.
		//standardizedRole.setRoleTopic(((RoleEditViewImpl)view).roleTopic.getValue());
		standardizedRole.setRoleTopic(((RoleEditViewImpl)view).roleTopic.getSelected());
		//Issue # 122 : Replace pull down with autocomplete.
//		checkListProxy.setTitle(((RoleEditCheckListSubViewImpl)checkListView).title.getValue());//spec
//		standardizedRole.setCheckList(checkListProxy);//spec
		
		// return '0' means minor clicked and '1' means Major Button Clicked
		
		if(this.place.getOperation() == Operation.EDIT)
		{
			 //checkListProxy= standardizedRole.getCheckList();//spec
			
			 
			
				//standardizedRole.setCheckList(checkListProxy);//spec
			//	System.out.println("Checklist----1: "+checkListProxy.getTitle());
				
				 majorRequest = requests.standardizedRoleRequest();
				 proxy= majorRequest.create(StandardizedRoleProxy.class);
				 
				
				 //spec
				 
				 /*majorCheckListRequest = requests.checkListRequest();
				 checkListProxy= majorCheckListRequest.create(CheckListProxy.class);*/
				 
			//	 checkListProxy= standardizedRole.getCheckList();//spec
				// checkListProxy.setTitle("ccc");//spec
				//	checkListProxy.setVersion(0);//spec
				 //spec
			//	 checkListProxy.setTitle(((RoleEditCheckListSubViewImpl)checkListView).title.getValue());//spec
				 proxy.setRoleTopic(roleTopic);
				 //copy(standardizedRole);
				// proxy.setActive(((RoleEditViewImpl)view).active.getValue());
				 proxy.setActive(true);
				 proxy.setShortName(((RoleEditViewImpl)view).shortName.getValue());
					proxy.setLongName(((RoleEditViewImpl)view).longName.getValue());
					proxy.setStudyYear(((RoleEditViewImpl)view).studyYear.getValue());
					proxy.setRoleType(((RoleEditViewImpl)view).roleType.getValue());
					
					proxy.setPreviousVersion(standardizedRole);
					
				//	proxy.setAdvancedSearchCriteria(standardizedRole.getAdvancedSearchCriteria());
					proxy.setKeywords(standardizedRole.getKeywords());
				//	proxy.setOscePosts(standardizedRole.getOscePosts());
				//	proxy.setRoleParticipants(standardizedRole.getRoleParticipants());
					proxy.setRoleTemplate(standardizedRole.getRoleTemplate());
				//	proxy.setRoleParticipants(standardizedRole.getRoleParticipants());
				//	proxy.setSimpleSearchCriteria(standardizedRole.getSimpleSearchCriteria());
					
					if(standardizedRole.getMainVersion()==null)
					{
						proxy.setMainVersion(1);
					}
					else
					{
					proxy.setMainVersion(standardizedRole.getMainVersion()+1);
					}
					proxy.setSubVersion(0);
					
					//proxy.setCheckList(checkListProxy);//spec
					//Issue # 122 : Replace pull down with autocomplete.
					//proxy.setRoleTopic(((RoleEditViewImpl)view).roleTopic.getValue());
					proxy.setRoleTopic(((RoleEditViewImpl)view).roleTopic.getSelected());
					//Issue # 122 : Replace pull down with autocomplete.

					majorRequest1 = this.requests.standardizedRoleRequest();
					oldProxy= majorRequest1.create(StandardizedRoleProxy.class);
					oldProxy.setActive(((RoleEditViewImpl)view).active.getValue());
				//	oldProxy.setActive(true);
					oldProxy.setShortName(((RoleEditViewImpl)view).shortName.getValue());
					oldProxy.setLongName(((RoleEditViewImpl)view).longName.getValue());
					oldProxy.setStudyYear(((RoleEditViewImpl)view).studyYear.getValue());
					oldProxy.setRoleType(((RoleEditViewImpl)view).roleType.getValue());
						
						//proxy1.setPreviousVersion(standardizedRole);
						//proxy1.setMainVersion(standardizedRole.getMainVersion()+1);
					oldProxy.setMainVersion(1);
					oldProxy.setSubVersion(1);
						
					oldProxy.setCheckList(checkListProxy);//spec
					oldProxy.setRoleTopic(roleTopic);

					oldProxy.setAdvancedSearchCriteria(standardizedRole.getAdvancedSearchCriteria());
					oldProxy.setKeywords(standardizedRole.getKeywords());
					oldProxy.setOscePosts(standardizedRole.getOscePosts());
					oldProxy.setRoleParticipants(standardizedRole.getRoleParticipants());
					oldProxy.setRoleTemplate(standardizedRole.getRoleTemplate());
					oldProxy.setRoleParticipants(standardizedRole.getRoleParticipants());
					oldProxy.setSimpleSearchCriteria(standardizedRole.getSimpleSearchCriteria());
					oldProxy.setCaseDescription(standardizedRole.getCaseDescription());
					oldProxy.setRoleScript(standardizedRole.getRoleScript());
					
					
			view.getMajorMinorChange();
			
			
			
		}
		else
		{
			 checkListProxy= standardizedRole.getCheckList();//spec

			checkListProxy.setTitle(((RoleEditCheckListSubViewImpl)checkListView).title.getValue());//spec
			standardizedRole.setCheckList(checkListProxy);//spec
			System.out.println("Checklist----2: "+checkListProxy.getTitle());
			
			//Issue # 122 : Replace pull down with autocomplete.
			/*((RoleEditViewImpl)view).roleTopic.setSelected(roleTopic);
			((RoleEditViewImpl)view).roleTopic.getTextField().advancedTextBox.setText(roleTopic.getName());*/
			//Issue # 122 : Replace pull down with autocomplete.
			save();
		}
		
		
		
	}
	
	
	public void copy(StandardizedRoleProxy srp)
	{
		proxy.setActive(srp.getActive());
		proxy.setShortName(srp.getShortName());
		proxy.setLongName(srp.getLongName());
		proxy.setStudyYear(srp.getStudyYear());
		proxy.setRoleType(srp.getRoleType());
		proxy.setPreviousVersion(srp);
		proxy.setMainVersion(srp.getMainVersion()+1);
		proxy.setSubVersion(1);
		
	//	checkListProxy.setTitle(srp.getCheckList().getTitle());//spec 
	//	proxy.setCheckList(checkListProxy);//spec
		
		//previous inactive
		
	}
	
	
	public void finalSave()
	{
		Log.info("Call Final Save()");
		
		// Highlight onViolation
		Log.info("Map Size: " + view.getStandardizedRoleMap().size());
		editorDriver.flush().fire(new OSCEReceiver<Void>(view.getStandardizedRoleMap()) {
			
			/*public void onFailure(ServerFailure error) {
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
*/
			// E Highlight onViolation
			@Override
			public void onSuccess(Void response) {
				Log.info("Role successfully saved.");

				save = true;
				
				RoleDetailsActivity.setSelecTab(selectedTab);
				//RoleDetailsActivity.setSelecTab(findTabIndex());
				roleActivity.setInserted(false);
				roleActivity.initSearch();
				goTo(new RoleDetailsPlace(roleTopic.stableId(),	Operation.DETAILS));				
				
		}
			
		});
		
		
	}
	
	public void save()
	{
		Log.info("Call Save()");
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		//if(((RoleEditViewImpl)view).roleTopic.getValue().getId()!=roleTopic.getId())
		
		if(((RoleEditViewImpl)view).roleTopic.getSelected().getId()!=roleTopic.getId())
		{
			//Issue # 122 : Replace pull down with autocomplete.	 
		 System.out.println("not Same");
			
		 //
		 
		 majorRequest1 = this.requests.standardizedRoleRequest();
			oldProxy= majorRequest1.create(StandardizedRoleProxy.class);
		//	oldProxy.setActive(((RoleEditViewImpl)view).active.getValue());
			oldProxy.setActive(true);
			oldProxy.setShortName(((RoleEditViewImpl)view).shortName.getValue());
			oldProxy.setLongName(((RoleEditViewImpl)view).longName.getValue());
			oldProxy.setStudyYear(((RoleEditViewImpl)view).studyYear.getValue());
			oldProxy.setRoleType(((RoleEditViewImpl)view).roleType.getValue());
				
				//proxy1.setPreviousVersion(standardizedRole);
				//proxy1.setMainVersion(standardizedRole.getMainVersion()+1);
			oldProxy.setMainVersion(1);
			oldProxy.setSubVersion(1);
				
			oldProxy.setCheckList(checkListProxy);//spec
			oldProxy.setRoleTopic(roleTopic);
		 
			oldProxy.setAdvancedSearchCriteria(standardizedRole.getAdvancedSearchCriteria());
			oldProxy.setKeywords(standardizedRole.getKeywords());
			oldProxy.setOscePosts(standardizedRole.getOscePosts());
			oldProxy.setRoleParticipants(standardizedRole.getRoleParticipants());
			oldProxy.setRoleTemplate(standardizedRole.getRoleTemplate());
			oldProxy.setRoleParticipants(standardizedRole.getRoleParticipants());
			oldProxy.setSimpleSearchCriteria(standardizedRole.getSimpleSearchCriteria());
			oldProxy.setCaseDescription(standardizedRole.getCaseDescription());
			oldProxy.setRoleScript(standardizedRole.getRoleScript());
			
		 //
			
			Log.info(" save role---check"+oldProxy.getShortName());
			// Highlight onViolation
			Log.info("Map Size: " + view.getStandardizedRoleMap().size());
			majorRequest1.persist().using(oldProxy).fire(new OSCEReceiver<Void>(view.getStandardizedRoleMap()) {
				// E Highlight onViolation
			@Override
			public void onSuccess(Void response) {
				// TODO Auto-generated method stub
				
				Log.info("new Role successfully saved.");
				
				finalSave();
				
				
				
			}
			
			// Highlight onViolation
/*			public void onFailure(ServerFailure error) {
				System.out.println("Error");
				Log.error(error.getMessage());

			}
			
			
			@Override
			public void onViolation(Set<Violation> errors) {
				System.out.println("violate");
				Iterator<Violation> iter = errors.iterator();
				String message = "";
				while (iter.hasNext()) {
					message += iter.next().getMessage() + "<br>";
				}
				Log.warn(" in Role -" + message);
			}
*/			// E Highlight onViolation
		});
		
		//save();
		
		
		}
		
		else
		{
			Log.info("Goto Else Part");
		finalSave();
		
		}
		
		
			
	}
	
	public void saveMajor()
	{
		
		
		
	Log.info("before persist after popup");		
	Log.info("Proxy---"+proxy.getLongName());		
		
		// Highlight onViolation
		Log.info("Map Size: "  + view.getStandardizedRoleMap().size());
		
		 CheckListProxy checkListProxy=null;
		

				CheckListRequest checklistRequest=requests.checkListRequest();
				 checkListProxy=checklistRequest.create(CheckListProxy.class);
	
				 checkListProxy.setCheckListTopics(standardizedRole.getCheckList().getCheckListTopics());
				checkListProxy.setTitle(((RoleEditCheckListSubViewImpl)checkListView).title.getValue());//spec
				final CheckListProxy newCheckList=checkListProxy;
				
				
				
				
				
				
				checklistRequest.persist().using(checkListProxy).fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void response) {
						Iterator<ChecklistTopicProxy> topicIterator=standardizedRole.getCheckList().getCheckListTopics().iterator();
						//create new create new topic 
						while(topicIterator.hasNext())
						{
							final ChecklistTopicProxy oldTopc=topicIterator.next();
							
							ChecklistTopicRequest topicRequest=requests.checklistTopicRequest();
							ChecklistTopicProxy topic=topicRequest.create(ChecklistTopicProxy.class);
							
							final ChecklistTopicProxy topic1=topic;
							
							topic.setCheckList(newCheckList);
							topic.setDescription(oldTopc.getDescription());
							topic.setSort_order(oldTopc.getSort_order());
							topic.setTitle(oldTopc.getTitle());
							
							topicRequest.persist().using(topic).fire(new OSCEReceiver<Void>() {

								@SuppressWarnings("deprecation")
								@Override
								public void onSuccess(Void response) {
									Iterator<ChecklistQuestionProxy> questionIterator=oldTopc.getCheckListQuestions().iterator();
									while(questionIterator.hasNext())
									{
										final ChecklistQuestionProxy oldQuestion=questionIterator.next();
										
										ChecklistQuestionRequest questionRequest=requests.checklistQuestionRequest();
										
										ChecklistQuestionProxy question=questionRequest.create(ChecklistQuestionProxy.class);
										
										final ChecklistQuestionProxy question1=question;
										
										question.setCheckListTopic(topic1);
										question.setInstruction(oldQuestion.getInstruction());
										question.setQuestion(oldQuestion.getQuestion());
										question.setSequenceNumber(oldQuestion.getSequenceNumber());
										
										questionRequest.persist().using(question).fire(new OSCEReceiver<Void>() {

											@SuppressWarnings("deprecation")
											@Override
											public void onSuccess(Void response) {
												Iterator<ChecklistOptionProxy> optionIterator=oldQuestion.getCheckListOptions().iterator();
												
												while(optionIterator.hasNext())
												{
													final ChecklistOptionProxy oldOption=optionIterator.next();
													
													ChecklistOptionRequest optionRequest=requests.checklistOptionRequest();
													
													ChecklistOptionProxy option=optionRequest.create(ChecklistOptionProxy.class);
													
													final ChecklistOptionProxy option1 = option;
													
													option.setChecklistQuestion(question1);
													option.setName(oldOption.getName());
													option.setOptionName(oldOption.getOptionName());
													option.setValue(oldOption.getValue());
													
													optionRequest.persist().using(option).fire(new OSCEReceiver<Void>() {

														@Override
														public void onSuccess(
																Void response) {
															// TODO Auto-generated method stub
															
														}
													});
													
												
													
													
													final Iterator<ChecklistCriteriaProxy> criteriaIterator=oldQuestion.getCheckListCriterias().iterator();
													
													while(criteriaIterator.hasNext())
													{
														final ChecklistCriteriaProxy oldCriteria=criteriaIterator.next();
														
														ChecklistCriteriaRequest criteriaRequest=requests.checklistCriteriaRequest();
														
														
														ChecklistCriteriaProxy criteria=criteriaRequest.create(ChecklistCriteriaProxy.class);
														
														final ChecklistCriteriaProxy criteria1 = criteria;
														
														criteria.setChecklistQuestion(question1);
														criteria.setCriteria(oldCriteria.getCriteria());
														
														
														
														optionRequest.persist().using(option).fire(new OSCEReceiver<Void>() {

															@Override
															public void onSuccess(
																	Void response) {
																
															}
														});
													
													}
												}
												
											}
										});
									}
									
								}
							});
						}
						
						
						//save new role
						
						
						
						
						
						//create Role template
					/*	RoleTemplateRequest roleTemplateRequest=requests.roleTemplateRequest();
						RoleTemplateProxy roleTemplateProxy=roleTemplateRequest.create(RoleTemplateProxy.class);
						roleTemplateProxy.setDate_cretaed(standardizedRole.getRoleTemplate().getDate_cretaed());
						roleTemplateProxy.setDate_edited(standardizedRole.getRoleTemplate().getDate_edited());
						roleTemplateProxy.setRoleBaseItem(standardizedRole.getRoleTemplate().getRoleBaseItem());
						roleTemplateProxy.setTemplateName(standardizedRole.getRoleTemplate().getTemplateName());
					*/	
						
						proxy.setCheckList(newCheckList);
						proxy.setRoleTemplate(standardizedRole.getRoleTemplate());
						final StandardizedRoleProxy proxy1=proxy;
						 majorRequest.persist().using(proxy).fire(new OSCEReceiver<Void>(view.getStandardizedRoleMap()) {

								

								/*	public void onFailure(ServerFailure error) {
										Log.info("error in persist");
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
								 */
									// E Highlight onViolation		 

									@Override
									public void onSuccess(Void response) {
										Log.info("Role successfully saved.");
										
										//previous version Inactve
										
										save = true;
										
										//create simple search criteria
											
										 	Iterator<SimpleSearchCriteriaProxy> simpleSearchIterator=standardizedRole.getSimpleSearchCriteria().iterator();
											while(simpleSearchIterator.hasNext())
											{
												SimpleSearchCriteriaProxy oldSimpleSearchProxy=simpleSearchIterator.next();
												SimpleSearchCriteriaRequest simpleSearchCriteriaRequest=requests.simpleSearchCriteriaRequest();
												
												SimpleSearchCriteriaProxy simpleSearchCriteriaProxy=simpleSearchCriteriaRequest.create(SimpleSearchCriteriaProxy.class);
												
												simpleSearchCriteriaProxy.setName(oldSimpleSearchProxy.getName());
												simpleSearchCriteriaProxy.setSortOrder(oldSimpleSearchProxy.getSortOrder());
												simpleSearchCriteriaProxy.setStandardizedRole(proxy1);
												simpleSearchCriteriaProxy.setValue(oldSimpleSearchProxy.getValue());
												
												simpleSearchCriteriaRequest.persist().using(simpleSearchCriteriaProxy).fire();
												
												
											}
											
											
											Iterator<RoleParticipantProxy> roleParticipantProxyIterator=standardizedRole.getRoleParticipants().iterator();
											while(roleParticipantProxyIterator.hasNext())
											{
												RoleParticipantProxy oldRoleParticipantProxy=roleParticipantProxyIterator.next();
												RoleParticipantRequest roleParticipantRequest=requests.roleParticipantRequest();
												
												RoleParticipantProxy roleParticipantProxy=roleParticipantRequest.create(RoleParticipantProxy.class);
												
												roleParticipantProxy.setDoctor(oldRoleParticipantProxy.getDoctor());
												roleParticipantProxy.setStandardizedRole(proxy1);
												roleParticipantProxy.setType(oldRoleParticipantProxy.getType());
												
												
												roleParticipantRequest.persist().using(roleParticipantProxy).fire();
												
												
											}
											
											final Iterator<AdvancedSearchCriteriaProxy> advancedSearchCriteriaProxyIterator=standardizedRole.getAdvancedSearchCriteria().iterator();
											
											if(!advancedSearchCriteriaProxyIterator.hasNext())
											{
												((RoleEditViewImpl)view).active.setValue(false);
												
												((RoleEditViewImpl)view).shortName.setValue(standardizedRole.getShortName());
												((RoleEditViewImpl)view).longName.setValue(standardizedRole.getLongName());
												((RoleEditViewImpl)view).roleType.setValue(standardizedRole.getRoleType());
												((RoleEditViewImpl)view).studyYear.setValue(standardizedRole.getStudyYear());
												((RoleEditViewImpl)view).getRoleEditCheckListPanel().setTitle(standardizedRole.getCheckList().getTitle());
												//((RoleEditCheckListSubViewImpl)checkListView).title.setValue("ABC");//spec
												
												save();
												return;
											}
											
											while(advancedSearchCriteriaProxyIterator.hasNext())
											{
												AdvancedSearchCriteriaProxy oldAdvancedSearchCriteriaProxy=advancedSearchCriteriaProxyIterator.next();
												AdvancedSearchCriteriaRequest advancedSearchCriteriaRequest=requests.advancedSearchCriteriaRequest();
												
												AdvancedSearchCriteriaProxy advancedSearchCriteriaProxy=advancedSearchCriteriaRequest.create(AdvancedSearchCriteriaProxy.class);
												
												
												advancedSearchCriteriaProxy.setBindType(oldAdvancedSearchCriteriaProxy.getBindType());
												advancedSearchCriteriaProxy.setComparation(oldAdvancedSearchCriteriaProxy.getComparation());
												advancedSearchCriteriaProxy.setField(oldAdvancedSearchCriteriaProxy.getField());
												advancedSearchCriteriaProxy.setObjectId(oldAdvancedSearchCriteriaProxy.getObjectId());
												advancedSearchCriteriaProxy.setShownValue(oldAdvancedSearchCriteriaProxy.getShownValue());
												advancedSearchCriteriaProxy.setStandardizedRole(proxy1);
												advancedSearchCriteriaProxy.setValue(oldAdvancedSearchCriteriaProxy.getValue());
												
												
												advancedSearchCriteriaRequest.persist().using(advancedSearchCriteriaProxy).fire(new OSCEReceiver<Void>() {

													@Override
													public void onSuccess(
															Void response) {
														if(!advancedSearchCriteriaProxyIterator.hasNext())
														{
															
															((RoleEditViewImpl)view).active.setValue(false);
															
															((RoleEditViewImpl)view).shortName.setValue(standardizedRole.getShortName());
															((RoleEditViewImpl)view).longName.setValue(standardizedRole.getLongName());
															((RoleEditViewImpl)view).roleType.setValue(standardizedRole.getRoleType());
															((RoleEditViewImpl)view).studyYear.setValue(standardizedRole.getStudyYear());
															((RoleEditViewImpl)view).getRoleEditCheckListPanel().setTitle(standardizedRole.getCheckList().getTitle());
															//((RoleEditCheckListSubViewImpl)checkListView).title.setValue("ABC");//spec
															
															save();
														}
														
													}
												});
												
												
											}
									
									
										
										
										
									
									}
									
								});

					}
				});
		
		 
		 
	

			
	}
	public int findTabIndex()
	{
		int i=0;
		if(roleTopic.getStandardizedRoles()==null)
		{
			Log.info("st role null");
			return 1;
		}
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

	


}
