package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.CircuitDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitOsceSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.CircuitOsceSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ListBoxPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ListBoxPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OSCENewSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OSCENewSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceCreatePostBluePrintSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceCreatePostBluePrintSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceDayView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceDayViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceGenerateSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceGenerateSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OscePostSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OscePostSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OscePostView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OscePostViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.SequenceOsceSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.SequenceOsceSubView.Delegate;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.SequenceOsceSubViewImpl;

import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.shared.ColorPicker;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.util;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;


@SuppressWarnings("deprecation")
public class CircuitDetailsActivity extends AbstractActivity implements
CircuitDetailsView.Presenter, 
CircuitDetailsView.Delegate,CircuitOsceSubView.Delegate,OsceGenerateSubView.Delegate,OscePostSubView.Delegate,ListBoxPopupView.Delegate,OscePostView.Delegate
,HeaderView.Delegate,DragHandler,//5C:SPEC START
OSCENewSubView.Delegate,
OsceCreatePostBluePrintSubView.Delegate,OsceDayView.Delegate ,SequenceOsceSubView.Delegate {//Assignment E:Module 5

		private OsMaRequestFactory requests;
		private PlaceController placeController;
		private AcceptsOneWidget widget;
		private CircuitDetailsView view;
		private boolean isgenerated=false;
		//5C:SPEC START
		private final OsceConstants constants;
		private OsceProxy osceProxy;
		private OsceDayViewImpl osceDayViewImpl;
		private SequenceOsceSubViewImpl sequenceOsceSubViewImpl;
		private List<SequenceOsceSubViewImpl> sequenceOsceSubViewImpl1;
		
		private CircuitOsceSubViewImpl circuitOsceSubViewImpl;
		
		private OscePostBlueprintProxy oscePostBlueprintProxy;
		List<OscePostSubViewImpl>  oscePostSubViewImpl;
		HorizontalPanel newPostAddHP;
		HorizontalPanel newPostHP;
		OscePostViewImpl oscePostViewImpl;
		CircuitDetailsActivity circuitDetailsActivity;
		
		OsceCreatePostBluePrintSubViewImpl osceCreatePostBluePrintSubViewImpl;
		private List<SpecialisationProxy> specialisationList;
		int indexGlobal;
		int maxSeq=0;
		OSCENewSubViewImpl oSCENewSubViewImpl;	
		//5C:SPEC END
		
		
		private CircuitDetailsPlace place;
		private CircuitDetailsActivity activity;
		public CircuitDetailsActivity(CircuitDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
			this.place = place;
	    	this.requests = requests;
	    	this.placeController = placeController;
	    	this.activity=this;
	    	this.circuitDetailsActivity=this;
	    	constants = GWT.create(OsceConstants.class);	    	
	    }
		
		public void onStop(){

		}

		@Override
		public void start(AcceptsOneWidget panel, EventBus eventBus) {
			Log.info("CircuitDetailsActivity.start()");
			final CircuitDetailsView circuitDetailsView = new CircuitDetailsViewImpl();
			circuitDetailsView.setPresenter(this);
			this.widget = panel;
			this.view = circuitDetailsView;
			widget.setWidget(circuitDetailsView.asWidget());
			
			view.setDelegate(this);
			
			circuitDetailsView.setDelegate(this);
			
			
			
			requests.specialisationRequest().findAllSpecialisations().with("roleTopics").fire(new OSCEReceiver<List<SpecialisationProxy>>() 
					{

						public void onSuccess(List<SpecialisationProxy> response) 
						{				
							specialisationList = new ArrayList<SpecialisationProxy>();				
							specialisationList.addAll(response);	
					
					//5C:SPEC START
			requests.find(place.getProxyId()).with("osces").with("oscePostBlueprints","oscePostBlueprints.specialisation","oscePostBlueprints.roleTopic").with("osce_days").with("osce_days.osceSequences").with("osce_days.osceSequences.oscePosts").with("osce_days.osceSequences.oscePosts.oscePostBlueprint").with("osce_days.osceSequences.oscePosts.standardizedRole").with("osce_days.osceSequences.oscePosts.oscePostBlueprint.roleTopic").with("osce_days.osceSequences.oscePosts.oscePostBlueprint.specialisation").with("osce_days.osceSequences.oscePosts.oscePostBlueprint.postType").with("osce_days.osceSequences.courses").with("osce_days.osceSequences.courses.oscePostRooms").with("osce_days.osceSequences.courses.oscePostRooms.oscePost").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.standardizedRole").with("osce_days.osceSequences.courses.oscePostRooms.room").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.roleTopic").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.postType").with("osce_days.osceSequences.courses.oscePostRooms.oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<Object>() {

				@Override
				public void onSuccess(Object response) {
					if(response instanceof OsceProxy && response != null){
						
						osceProxy=(OsceProxy)response;
						Log.info("Arrived OsceProxy At CircuitDetailActivity");
						
						circuitOsceSubViewImpl=view.getcircuitOsceSubViewImpl();
						
						OsceStatus status = ((OsceProxy) response).getOsceStatus();
						
						String style = status.getOsceStatus(status);
						
						setOsceStatusStyle(style);
						
						//circuitOsceSubViewImpl.setStyleName(style);
						circuitOsceSubViewImpl.shortBreakTextBox.setValue(util.checkShort((((OsceProxy) response).getShortBreak())));
						circuitOsceSubViewImpl.longBreakTextBox.setValue(util.checkShort((((OsceProxy) response).getLongBreak())));
						circuitOsceSubViewImpl.launchBreakTextBox.setValue(util.checkShort((((OsceProxy) response).getLunchBreak())));
						circuitOsceSubViewImpl.maxStudentTextBox.setValue(util.checkInteger(((OsceProxy) response).getMaxNumberStudents()));
						circuitOsceSubViewImpl.maxParcourTextBox.setValue(util.checkInteger(((OsceProxy) response).getNumberCourses()));
						//Log.info("Room Value"+ ((OsceProxy) response).getNumberRooms());
						circuitOsceSubViewImpl.maxRoomsTextBox.setValue(util.checkInteger((((OsceProxy) response).getNumberRooms())));
						
						
						
						
						circuitOsceSubViewImpl.setProxy((OsceProxy)response);
						circuitOsceSubViewImpl.setClearAllBtn(((OsceProxy)response).getOsceStatus() == OsceStatus.OSCE_GENRATED);
						circuitOsceSubViewImpl.setDelegate(activity);
						
						

						//Assignment E:Module 5[
						//5C:SPEC START		
						view.getScrollPanel().setStylePrimaryName("Osce-Status");
						//view.getScrollPanel().setStyleName("Osce-Status");
						osceProxy=(OsceProxy)response;
						if((osceProxy.getOsceStatus() == OsceStatus.OSCE_BLUEPRINT) || (osceProxy.getOsceStatus() == OsceStatus.OSCE_NEW))
						{
							
						if((osceProxy.getOsceStatus() == OsceStatus.OSCE_NEW))
						{
							view.getScrollPanel().addStyleDependentName("NEW");
						}
						else
						if((osceProxy.getOsceStatus() == OsceStatus.OSCE_BLUEPRINT))
						{
							view.getScrollPanel().addStyleDependentName("BluePrint");
						}
						List<OscePostBlueprintProxy>listOscePostBlueprintProxy = osceProxy.getOscePostBlueprints();
						
						 oSCENewSubViewImpl=new OSCENewSubViewImpl();//.getOSCENewSubViewImpl();
						view.getGenerateVP().insert(oSCENewSubViewImpl, view.getGenerateVP().getWidgetCount());
						//oSCENewSubViewImpl.
						((CircuitDetailsViewImpl)view).oSCENewSubViewImpl=oSCENewSubViewImpl;
						oSCENewSubViewImpl.setDelegate(activity);
						
						//Osce Days[
						osceDayViewImpl = oSCENewSubViewImpl.getOsceDayViewImpl();
						osceDayViewImpl.setDelegate(activity);
						// Day Assignment 
						
						
						Log.info("Before Iterator");
						
					
						
						List<OsceDayProxy> setOsceDays = ((OsceProxy) response).getOsce_days();
						if(setOsceDays.size()==0){
							Log.info("OsceDay null for proxy : " +osceProxy.getId());
							osceDayViewImpl.setOsceDayProxy(null);
						}
						else{
							Log.info("Osce Exist for OsceProxy : " + osceProxy.getId());
							Iterator<OsceDayProxy> osceDays = setOsceDays.iterator();
							if(osceDays.hasNext()){
								osceDayViewImpl.setOsceDayProxy(osceDays.next());
							}
						}
						
						setDayStatusStyle(style);
						osceDayViewImpl.init();
						addTimeHendlers();
						
						//Osce Days]
						
						
						
						//Log.info("Osce Post Blue Print Size : "+ listOscePostBlueprintProxy.size());						
							
							osceCreatePostBluePrintSubViewImpl=new OsceCreatePostBluePrintSubViewImpl(specialisationList);
							osceCreatePostBluePrintSubViewImpl.setStyleName("Osce-Status-BluePrint-Create", true);
							newPostAddHP=oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getOscePostBluePrintSubViewImplHP();
							newPostHP=oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().newPostHP;
							//newPostAddHP.setStyleName("Osce_BluePrint_Status");							
							
							if(listOscePostBlueprintProxy.size()>0)
							{
								Log.info("Osce has Osce Post Blueprint " + listOscePostBlueprintProxy.size());
								
								int index=0;
							
								if(oscePostSubViewImpl == null){
									oscePostSubViewImpl = new ArrayList<OscePostSubViewImpl>();
								}																								
								OscePostSubViewImpl tempOscePostSubViewImpl;				
								
								Iterator<OscePostBlueprintProxy> iter = listOscePostBlueprintProxy.iterator();								
								
									while (iter.hasNext()) 
									{																				
										oscePostBlueprintProxy=iter.next();
													
								
										Log.info("~OsceBluerint Id: " + oscePostBlueprintProxy.getId());
										
										tempOscePostSubViewImpl=new OscePostSubViewImpl();										
										tempOscePostSubViewImpl.enableDisableforBluePrintStatus();
										tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
										tempOscePostSubViewImpl.getRoleTopicLbl().setText(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName());
										tempOscePostSubViewImpl.getSpecializationLbl().setText(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName());

										oscePostViewImpl=new OscePostViewImpl();
										oscePostViewImpl.setStyleName("Osce-Status-BluePrint-Save", true);
										oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getDragController().makeDraggable(oscePostViewImpl,oscePostViewImpl.getPostTypeLbl());
										oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getDragController().addDragHandler(activity);
										
										oscePostViewImpl.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR MAIN POST VIEW
										oscePostViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;	
										//oscePostViewImpl.getOscePostSubViewHP().setStyleName("Osce_Genrated_Status");						
										
										oscePostViewImpl.getPostTypeLbl().setText(oscePostBlueprintProxy.getPostType().name());
										oscePostSubViewImpl.add(tempOscePostSubViewImpl);					
										oscePostSubViewImpl.get(index).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
										oscePostSubViewImpl.get(index).oscePostBlueprintProxy=oscePostBlueprintProxy;		
										oscePostSubViewImpl.get(index).getPostNameLbl().setText("Post "+oscePostBlueprintProxy.getSequenceNumber());
										maxSeq=oscePostBlueprintProxy.getSequenceNumber();
										oscePostViewImpl.getOscePostSubViewHP().add(oscePostSubViewImpl.get(index));	// ADD SUBVIEW IN POSTVIEW
										
										if(oscePostBlueprintProxy.getPostType()==PostType.ANAMNESIS_THERAPY || oscePostBlueprintProxy.getPostType()==PostType.PREPARATION)
										{
											Log.info("~Anemnis");				
											if(iter.hasNext())
											{
												oscePostBlueprintProxy=iter.next();											
												
												tempOscePostSubViewImpl=new OscePostSubViewImpl();
												tempOscePostSubViewImpl.enableDisableforBluePrintStatus();	
												tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR SUBVIEW
										
												tempOscePostSubViewImpl.getSpecializationLbl().setText(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName());
												tempOscePostSubViewImpl.getRoleTopicLbl().setText(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName());
												tempOscePostSubViewImpl.getPostNameLbl().setText("Post "+oscePostBlueprintProxy.getSequenceNumber());
												Log.info("OsceBluerint Next Id: " + oscePostBlueprintProxy.getId());												
												oscePostSubViewImpl.add(tempOscePostSubViewImpl);												
												index++;																							
												oscePostViewImpl.getOscePostSubViewHP().add(oscePostSubViewImpl.get(index));	// ADD SUBVIEW IN POSTVIEW
												oscePostSubViewImpl.get(index).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
												oscePostSubViewImpl.get(index).oscePostBlueprintProxy=oscePostBlueprintProxy;	
												oscePostViewImpl.oscePostBlueprintProxyNext=oscePostBlueprintProxy;
												maxSeq=oscePostBlueprintProxy.getSequenceNumber();
											}
											else
											{
												Log.info("Not Next Set");
											}
																												
										}										
										newPostAddHP.add(oscePostViewImpl);												
										index++;
										}										
								
							}
							else
							{
								Log.info("Osce has No Osce Post Blueprint");								
							}									
							newPostHP.add(osceCreatePostBluePrintSubViewImpl);
							addCreateListBoxHandler(); // Create Osce Post Blueprint Handler
						
						}
						else if(status.equals(OsceStatus.OSCE_GENRATED))
						{
							view.getScrollPanel().removeStyleDependentName("BluePrint");
							view.getScrollPanel().addStyleDependentName("Genrated");
							
							
							Iterator<OsceDayProxy> osceDayIterator=((OsceProxy)response).getOsce_days().iterator();
							Log.info("number of Osce Day :" + ((OsceProxy)response).getOsce_days().size());
							while(osceDayIterator.hasNext())
							{
								OsceDayProxy osceDayProxy=osceDayIterator.next();
								OsceGenerateSubView generateView=createGenerateView((OsceDayProxy)osceDayProxy);
								
								Iterator<OsceSequenceProxy> osceSeqProxyIterator=osceDayProxy.getOsceSequences().iterator();
								sequenceOsceSubViewImpl1=new ArrayList<SequenceOsceSubViewImpl>(osceDayProxy.getOsceSequences().size());
							
								while(osceSeqProxyIterator.hasNext())
								{
									OsceSequenceProxy osceSeqProxy=osceSeqProxyIterator.next();
									
									
									//create All Parcor View
									//Iterator<CourseProxy> courseProxyIterator=osceSeqProxy.getCourses().iterator();
									//create Accordian
									AccordianPanelView accordianView=new AccordianPanelViewImpl(true);
									ScrollPanel sp=new ScrollPanel(accordianView.asWidget());
									
									sp.setWidth("720px");
									HorizontalPanel accordingHp=new HorizontalPanel();
									accordingHp.add(sp);
									
									
									//create each parcor
									//while(courseProxyIterator.hasNext())
									//{
										//CourseProxy courseProxy=courseProxyIterator.next();
										createParcorView(accordianView,osceSeqProxy);
										
									//}
									
									//create Sequence View
									
										//sequence start
									//	sequenceOsceSubViewImpl=new SequenceOsceSubViewImpl(osceSeqProxy);
										
										 sequenceOsceSubViewImpl = new SequenceOsceSubViewImpl();
									//	sequenceOsceSubViewImpl=sequenceOsceSubViewImpl2;
									//	sequenceOsceSubViewImpl1.add(sequenceOsceSubViewImpl);
										sequenceOsceSubViewImpl.setDelegate(activity);
										sequenceOsceSubViewImpl.nameOfSequence.setText((osceSeqProxy.getLabel()==null?"aaa":osceSeqProxy.getLabel()));
										sequenceOsceSubViewImpl.sequenceRotation.setText(osceSeqProxy.getNumberRotation()==null?"":osceSeqProxy.getNumberRotation().toString());
										sequenceOsceSubViewImpl.osceSequenceProxy=osceSeqProxy;
										sequenceOsceSubViewImpl.osceDayProxy=osceDayProxy;
									//	addClickHandler(sequenceOsceSubViewImpl);
										//sequenceOsceSubViewImpl.setStyleName(status.getOsceStatus(OsceStatus.OSCE_GENRATED));
										accordingHp.add(sequenceOsceSubViewImpl);
										//sequence end
										
										//add accordian and sequence to vertical panel
										accordingHp.setSpacing(20);
										generateView.getAccordianVP().insert(accordingHp, generateView.getAccordianVP().getWidgetCount());
									
									
								}
								
									//create Day view
								//Osce Days[
								osceDayViewImpl = generateView.getOsceDayViewImpl();
								osceDayViewImpl.setDelegate(activity);
								
								// Day Assignment 
								
								
								Log.info("Before Iterator");
								
							
								
								/*Set<OsceDayProxy> setOsceDays = ((OsceProxy) response).getOsce_days();
								if(setOsceDays.size()==0){
									Log.info("OsceDay null for proxy : " +osceProxy.getId());
									osceDayViewImpl.setOsceDayProxy(null);
								}
								else{
									Log.info("Osce Exist for OsceProxy : " + osceProxy.getId());
									Iterator<OsceDayProxy> osceDays = setOsceDays.iterator();
									if(osceDays.hasNext()){
										osceDayViewImpl.setOsceDayProxy(osceDays.next());
									}
								}*/
								
								osceDayViewImpl.setOsceDayProxy(osceDayProxy);
								setDayStatusStyle(style);
								osceDayViewImpl.init();
								
								addTimeHendlers();
								
								//Osce Days]
							}
							
							
						}
						//Assignment E:Module 5]
					}
					
				}
			});
			
						}
						
					});
			
			// spec End==
	
	}
		
		
	//sequence start
		
	
		//sequence end
		
		
		//Assignment E:Module 5[

		@Override
		public void onDragEnd(DragEndEvent event) {
			Log.info("onDragEnd :");
			
			Log.info("Parent : "+((HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent()).getWidgetCount());
			
			if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED)
			{
				HorizontalPanel hp=((HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent());
				int j=0;
				for(int i=0;i<hp.getWidgetCount();i++)
				{
					
					if(((OscePostView)hp.getWidget(i)).isAnemanis()==true || ((OscePostView)hp.getWidget(i)).getProxy().getOscePostBlueprint().getPostType()==PostType.PREPARATION)
					{
						
						updateSequence(((OscePostView)hp.getWidget(i)).getProxy(),i+j+1);
						j++;
						updateSequence(((OscePostView)hp.getWidget(i)).getNextOscePostProxy(),i+j+1);
					}
					else
						updateSequence(((OscePostView)hp.getWidget(i)).getProxy(),i+j+1);
					//((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setText("Post "+i+1);
				}
			}
			else if(osceProxy.getOsceStatus()==OsceStatus.OSCE_BLUEPRINT)
			{
				Log.info("Osce BluePrint On Drag End");
				Log.info("Event Source :"+((OscePostView)event.getSource()).getOscePostBlueprintProxy().getSequenceNumber());
				HorizontalPanel hp=((HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent());
				int j=0;
				for(int i=0;i<hp.getWidgetCount();i++)
				{
					if(hp.getWidget(i) instanceof OsceCreatePostBluePrintSubViewImpl)
						continue;
					
					
					else if(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy().getPostType()==PostType.ANAMNESIS_THERAPY ||  ((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy().getPostType()==PostType.PREPARATION)
					{
						
						updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy(),i+j+1);
						j++;
						updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxyNext(),i+j+1);
					}
					else
						updateBluePrintSequence(((OscePostView)hp.getWidget(i)).getOscePostBlueprintProxy(),i+j+1);
					//((OscePostSubView)((OscePostView)hp.getWidget(i)).getOscePostSubViewHP().getWidget(0)).getPostNameLbl().setText("Post "+i+1);
				}
			}
			Log.info("onDragEnd");
			// TODO Auto-generated method stub
			/* hp=(HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent();
			ScrollPanel sp=(ScrollPanel)hp.getParent();
			AbsolutePanel ap=(AbsolutePanel)sp.getParent();
			VerticalPanel vp=(VerticalPanel) ap.getParent();
			ContentViewImpl cv=(ContentViewImpl)vp.getParent();
			cv.isdragging=false;*/
			
		}
		
		//
		public void updateSequence(OscePostProxy proxy,int seqNum)
		{
			OscePostRequest oscePostRequest=requests.oscePostRequest();
			proxy=oscePostRequest.edit(proxy);
			proxy.setSequenceNumber(seqNum);
			oscePostRequest.persist().using(proxy).fire();
		}
		
		public void updateBluePrintSequence(OscePostBlueprintProxy proxy,int seqNum)
		{
			OscePostBlueprintRequest oscePostRequest=requests.oscePostBlueprintRequest();
			proxy=oscePostRequest.edit(proxy);
			proxy.setSequenceNumber(seqNum);
			oscePostRequest.persist().using(proxy).fire();
		}
		
		
		public void updateBluePrintSequences()
		{
			Iterator<OscePostBlueprintProxy> bluePrintIterator=osceProxy.getOscePostBlueprints().iterator();
			int i=0;
			while(bluePrintIterator.hasNext())
			{
				OscePostBlueprintProxy proxy=bluePrintIterator.next();
				updateBluePrintSequence(proxy, ++i);
			}
		}
		@Override
		public void onDragStart(DragStartEvent event) {
			Log.info("onDragStart");
			// TODO Auto-generated method stub
			/*HorizontalPanel hp=(HorizontalPanel)((OscePostViewImpl)event.getSource()).getParent();
			ScrollPanel sp=(ScrollPanel)hp.getParent();
			AbsolutePanel ap=(AbsolutePanel)sp.getParent();
			VerticalPanel vp=(VerticalPanel) ap.getParent();
			ContentViewImpl cv=(ContentViewImpl)vp.getParent();
			cv.isdragging=true;
			Log.info("dragging" + cv.isdragging);*/
		}

		@Override
		public void onPreviewDragEnd(DragEndEvent event)
				throws VetoDragException {
			Log.info("onPreviewDragEnd");
			
		}

		@Override
		public void onPreviewDragStart(DragStartEvent event)
				throws VetoDragException {
			// TODO Auto-generated method stub
			
		}
		
				
		public void deleteCourse(final HeaderView view)
		{
			Log.info("deleteCourse");
			/*CourseProxy proxy=view.getProxy();
			
			requests.courseRequest().remove().using(proxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("deleteCourse Success");
					((HeaderViewImpl)view).removeFromParent();
					
				}
			});
			*/
		}
		
		public void colorChanged(final HeaderView view)
		{
			view.getColorPicker().getValue();
			CourseProxy proxy=view.getProxy();
			
			CourseRequest courseRequest=requests.courseRequest();
			proxy=courseRequest.edit(proxy);
			proxy.setColor(view.getColorPicker().getValue().toString());
			
			courseRequest.persist().using(proxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("colorChanged success");
					view.changeHeaderColor(view.getColorPicker().getValue());
					
				}
			});
		}
		
		public void deleteOscePost(final OscePostView view)
		{
			
			Log.info("deleteOscePost");
		/*	OscePostProxy proxy=view.getProxy();
			
			requests.oscePostRequest().remove().using(proxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					((OscePostViewImpl)view).removeFromParent();
					
				}
			});
			*/
			Log.info(osceProxy.getOsceStatus().toString());
			Log.info(OsceStatus.OSCE_BLUEPRINT.toString());
			Log.info(new Integer(OsceStatus.OSCE_BLUEPRINT.ordinal()).toString());
			if((osceProxy.getOsceStatus() == OsceStatus.OSCE_BLUEPRINT))
				deletePostClicked((OscePostViewImpl)view);
			
		}
		public void saveStandardizedRole(final ListBoxPopupView view)
		{
			Log.info("saveStandardizedRole ");
			OscePostProxy oscePostProxy=(OscePostProxy)view.getProxy();
			final StandardizedRoleProxy standardizedRoleProxy=(StandardizedRoleProxy)view.getListBox().getValue();
			
			OscePostRequest oscePostRequest=requests.oscePostRequest();
			oscePostProxy=oscePostRequest.edit(oscePostProxy);
			oscePostProxy.setStandardizedRole(standardizedRoleProxy);
			oscePostRequest.persist().using(oscePostProxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("Success saveStandardizedRole ");
					view.getOscePostSubView().getStandardizedRoleLbl().setText(standardizedRoleProxy.getLongName());
					
					((ListBoxPopupViewImpl)view).hide();
				}
			});
		}
		
		public void findStandardizedRoles(final OscePostSubView view)
		{
			requests.roleTopicRequest().findRoleTopic(((OscePostSubViewImpl)view).getOscePostProxy().getOscePostBlueprint().getRoleTopic().getId()).with("standardizedRoles").fire(new OSCEReceiver<RoleTopicProxy>() {

				@Override
				public void onSuccess(RoleTopicProxy response) {
					
					Log.info("findStandardizedRoles"+response.getStandardizedRoles().get(0).getLongName());
					ArrayList list=new ArrayList();
					//list.add( (EntityProxy)response.getStandardizedRoles().get(0));
					list.addAll(response.getStandardizedRoles());
					
					((OscePostSubViewImpl)view).createOptionPopup();
					((OscePostSubViewImpl)view).popupView.setDelegate(activity);
					((OscePostSubViewImpl)view).popupView.setOscePostSubView(view);
					((OscePostSubViewImpl)view).popupView.setProxy(view.getOscePostProxy());
					((OscePostSubViewImpl)view).showPopUpView();
					((OscePostSubViewImpl)view).popupView.getListBox().setAcceptableValues(list);
					
				}
			});
			
		}
		//create parcor view
		public void createParcorView(AccordianPanelView accordianView,EntityProxy proxy)
		{
			if(proxy instanceof OsceSequenceProxy)
			{
				
				OsceSequenceProxy osceSequenceProxy=(OsceSequenceProxy)proxy;
				ContentViewImpl contentView=new ContentViewImpl();
				contentView.getDragController().addDragHandler(this);
				//create All Posts
				Iterator<OscePostProxy> oscePostIterator=osceSequenceProxy.getOscePosts().iterator();
				
				
				
				while(oscePostIterator.hasNext())
				{
				
					OscePostProxy oscePostProxy=oscePostIterator.next();
				
				//createPost
					if(oscePostProxy.getOscePostBlueprint().getPostType().equals(PostType.ANAMNESIS_THERAPY) || oscePostProxy.getOscePostBlueprint().getPostType().equals(PostType.PREPARATION))
					{
						createAnamnesisTherapyPost(contentView,oscePostProxy,oscePostIterator.next());
					}
					else
					if(osceSequenceProxy.getOscePosts().size()==1)
						createUndraggablePost(contentView,oscePostProxy);
					else
						createOscePost(contentView,oscePostProxy);
					
				
				
				
				}
				//create Header View
				HeaderView headerView=new HeaderViewImpl();
				
				accordianView.add(headerView.asWidget(), contentView);
				
				((AccordianPanelViewImpl)accordianView).expand((HeaderViewImpl)headerView, ((AccordianPanelViewImpl)accordianView).sp);
				
				CourseProxy courseProxy=osceSequenceProxy.getCourses().iterator().next();
				
				headerView.setDelegate(this);
				headerView.setProxy(courseProxy);
				if(courseProxy.getColor()!=null)
				{
					headerView.getColorPicker().setValue(ColorPicker.valueOf(courseProxy.getColor()));
					headerView.changeHeaderColor(ColorPicker.valueOf(courseProxy.getColor()));
					
				}
				
			}
			
			
		}
		
		//create Post Inside Content
		public void createAnamnesisTherapyPost(ContentViewImpl contentView,OscePostProxy oscePostProxy,OscePostProxy oscePostProxyNext)
		{
			OscePostView oscePostView=new OscePostViewImpl();
			oscePostView.getPostTypeLbl().setText(oscePostProxy.getOscePostBlueprint().getPostType().toString());
			oscePostView.setDelegate(this);
			oscePostView.setProxy(oscePostProxy);
			oscePostView.setAnemanis(true);
			oscePostView.setNextOscePostProxy(oscePostProxyNext);
			//first Post
			OscePostSubView oscePostSubView=new OscePostSubViewImpl();
			
			oscePostSubView.getPostNameLbl().setText("Post " +oscePostProxy.getSequenceNumber());
			oscePostSubView.getSpecializationLbl().setText(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName());
			oscePostSubView.getRoleTopicLbl().setText(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName());
			if(oscePostProxy.getStandardizedRole()!=null)
				oscePostSubView.getStandardizedRoleLbl().setText(oscePostProxy.getStandardizedRole().getLongName());
			
			oscePostSubView.setDelegate(this);
			oscePostSubView.setOscePostProxy(oscePostProxy);
			
			
			oscePostSubView.enableDisableforGeneratedStatus();
			
			oscePostView.getOscePostSubViewHP().insert(oscePostSubView, oscePostView.getOscePostSubViewHP().getWidgetCount());
			
			//Second Post
			OscePostSubView oscePostSubViewNext=new OscePostSubViewImpl();
			
			oscePostSubViewNext.getPostNameLbl().setText("Post " +oscePostProxyNext.getSequenceNumber());
			oscePostSubViewNext.getSpecializationLbl().setText(oscePostProxyNext.getOscePostBlueprint().getSpecialisation().getName());
			oscePostSubViewNext.getRoleTopicLbl().setText(oscePostProxyNext.getOscePostBlueprint().getRoleTopic().getName());
			if(oscePostProxyNext.getStandardizedRole()!=null)
				oscePostSubView.getStandardizedRoleLbl().setText(oscePostProxyNext.getStandardizedRole().getLongName());
			
			oscePostSubViewNext.setDelegate(this);
			oscePostSubViewNext.setOscePostProxy(oscePostProxyNext);
			
			
			oscePostSubViewNext.enableDisableforGeneratedStatus();
			
			oscePostView.getOscePostSubViewHP().insert(oscePostSubViewNext, oscePostView.getOscePostSubViewHP().getWidgetCount());
			
			//make content draggable
			
			contentView.getDragController().makeDraggable(oscePostView.asWidget(),oscePostView.getPostTypeLbl().asWidget());
			
			contentView.getPostHP().insert(oscePostView, contentView.getPostHP().getWidgetCount());
		}
		
		public void createUndraggablePost(ContentViewImpl contentView,OscePostProxy oscePostProxy)
		{
			OscePostView oscePostView=new OscePostViewImpl();
			oscePostView.getPostTypeLbl().setText(oscePostProxy.getOscePostBlueprint().getPostType().toString());
			oscePostView.setDelegate(this);
			oscePostView.setProxy(oscePostProxy);
			OscePostSubView oscePostSubView=new OscePostSubViewImpl();
			
			oscePostSubView.getPostNameLbl().setText("Post " +oscePostProxy.getSequenceNumber());
			oscePostSubView.getSpecializationLbl().setText(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName());
			oscePostSubView.getRoleTopicLbl().setText(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName());
			if(oscePostProxy.getStandardizedRole()!=null)
				oscePostSubView.getStandardizedRoleLbl().setText(oscePostProxy.getStandardizedRole().getLongName());
			
			oscePostSubView.setDelegate(this);
			oscePostSubView.setOscePostProxy(oscePostProxy);
			
			
			oscePostSubView.enableDisableforGeneratedStatus();
			
			
			oscePostView.getOscePostSubViewHP().insert(oscePostSubView, oscePostView.getOscePostSubViewHP().getWidgetCount());
			//contentView.getDragController().makeDraggable(oscePostView.asWidget(),oscePostView.getPostTypeLbl().asWidget());
			
			contentView.getPostHP().insert(oscePostView, contentView.getPostHP().getWidgetCount());
		}
		public void createOscePost(ContentViewImpl contentView,OscePostProxy oscePostProxy)
		{
			OscePostView oscePostView=new OscePostViewImpl();
			oscePostView.getPostTypeLbl().setText(oscePostProxy.getOscePostBlueprint().getPostType().toString());
			oscePostView.setDelegate(this);
			oscePostView.setProxy(oscePostProxy);
			OscePostSubView oscePostSubView=new OscePostSubViewImpl();
			
			oscePostSubView.getPostNameLbl().setText("Post " +oscePostProxy.getSequenceNumber());
			oscePostSubView.getSpecializationLbl().setText(oscePostProxy.getOscePostBlueprint().getSpecialisation().getName());
			oscePostSubView.getRoleTopicLbl().setText(oscePostProxy.getOscePostBlueprint().getRoleTopic().getName());
			if(oscePostProxy.getStandardizedRole()!=null)
				oscePostSubView.getStandardizedRoleLbl().setText(oscePostProxy.getStandardizedRole().getLongName());
			
			oscePostSubView.setDelegate(this);
			oscePostSubView.setOscePostProxy(oscePostProxy);
			
			
			oscePostSubView.enableDisableforGeneratedStatus();
			
			
			oscePostView.getOscePostSubViewHP().insert(oscePostSubView, oscePostView.getOscePostSubViewHP().getWidgetCount());
			contentView.getDragController().makeDraggable(oscePostView.asWidget(),oscePostView.getPostTypeLbl().asWidget());
			
			contentView.getPostHP().insert(oscePostView, contentView.getPostHP().getWidgetCount());
		}
		
		public OsceGenerateSubView createGenerateView(OsceDayProxy osceDayProxy)
		{
			
			OsceGenerateSubView generateView=new OsceGenerateSubViewImpl();
			view.getGenerateVP().insert(generateView, view.getGenerateVP().getWidgetCount());
			generateView.setDelegate(this);
			return generateView;
					
		}
		
		//Assignment E:Module 5]
		@Override
		public void goTo(Place place) {
			placeController.goTo(place);		
		}

		@Override
		public void saveOsceData(OsceProxy osceProxy) {
			Log.info("Call saveOsceData");
			
			CircuitOsceSubViewImpl circuitOsceSubViewImp = view.getcircuitOsceSubViewImpl();
			OsceRequest osceReq = requests.osceRequest();
			osceProxy = osceReq.edit(osceProxy);
			
			osceProxy.setShortBreak(circuitOsceSubViewImp.shortBreakTextBox.getValue());
			osceProxy.setLongBreak(circuitOsceSubViewImp.longBreakTextBox.getValue());
			osceProxy.setLunchBreak(circuitOsceSubViewImp.launchBreakTextBox.getValue());
			osceProxy.setMaxNumberStudents(circuitOsceSubViewImp.maxStudentTextBox.getValue());
			osceProxy.setNumberCourses(circuitOsceSubViewImp.maxParcourTextBox.getValue());
			osceProxy.setNumberRooms(circuitOsceSubViewImp.maxRoomsTextBox.getValue());
			// Highlight onViolation
			Log.info("Map Size: " + circuitOsceSubViewImp.osceMap.size());
			osceReq.persist().using(osceProxy).fire(new OSCEReceiver<Void>(circuitOsceSubViewImp.osceMap) {
			// E Highlight onViolation
				@Override
				public void onSuccess(Void response) {
					Log.info("Osce Value Updated");
					final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
					dialogBox.showConfirmationDialog(constants.updateOsce());
					dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();
							
						}
					});
					
						dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();
							
						}
					});
					//Window.alert("Osce Data Updated sucessfully");
					
				}
			});
		}
		@Override
		public void clearAll(OsceProxy proxy) {
			// Highlight onViolation
			CircuitOsceSubViewImpl circuitOsceSubViewImp = view.getcircuitOsceSubViewImpl();
			// E Highlight onViolation
			
			OsceRequest osceReq = requests.osceRequest();
			proxy = osceReq.edit(proxy);			
			proxy.setOsceStatus(OsceStatus.OSCE_BLUEPRINT);		
			// Highlight onViolation
			osceReq.persist().using(proxy).fire(new OSCEReceiver<Void>(circuitOsceSubViewImp.osceMap) {
				// E Highlight onViolation

				@Override
				public void onSuccess(Void response) {
					Log.info("Osce Value Updated");
					//Window.alert("Osce Data Updated sucessfully");
					
				}
			});

			
		}
		
		// 5C: SPEC START
		
		// 5C: SPEC START
		
				private void addCreateListBoxHandler() 
				{				
					osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().addValueChangeHandler(new ValueChangeHandler<PostType>() {

						@Override
						public void onValueChange(ValueChangeEvent<PostType> event) 
						{
								Log.info("Osce Id: " + osceProxy.getId());	
								//if(event.getValue()==PostType.BREAK)
								//{
									Log.info("Osce Id: " + osceProxy.getId());					
									/*if (Window.confirm("Do you want to Add Specialisation?")) 
									{
										Log.info("Select Specialisation to Add..");								
									}
									else
									{*/
										changeOsceStatus();
									//}
								//}												
						}

					});
					
					osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().addValueChangeHandler(new ValueChangeHandler<RoleTopicProxy>() 
					{
						@Override
						public void onValueChange(ValueChangeEvent<RoleTopicProxy> event) 
						{
									Log.info("Osce Id: " + osceProxy.getId());							
									changeOsceStatus();						
						}
					
					});
							
					
					osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().addValueChangeHandler(new ValueChangeHandler<SpecialisationProxy>() 
					{
						@Override
						public void onValueChange(ValueChangeEvent<SpecialisationProxy> event) 
						{
							Log.info("Osce Id: " + osceProxy.getId());					
							/*if (Window.confirm("Do you want to Change/Add Role Topic")) 
							{
								Log.info("Add Role Topic...");
								if(event.getValue().getRoleTopics()!=null)
								{
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setValue(null);
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setAcceptableValues(event.getValue().getRoleTopics());
								}
								else
								{
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setValue(null);
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setAcceptableValues(new ArrayList<RoleTopicProxy>());
								}
							}
							else
							{
								Log.info("Not Add Role Topic");
								if(event.getValue() != null )
								{							
									
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setAcceptableValues(event.getValue().getRoleTopics());
									changeOsceStatus();
									
								}							
								else
								{						
									osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setAcceptableValues(new ArrayList<RoleTopicProxy>());							
									changeOsceStatus();
								}
							}
							*/
							
							changeOsceStatus();
						}
						
					});	
					
				}
				
				// 5C: SPEC END
				public void changeOsceStatus()
				{
					if(osceProxy.getOsceStatus()==OsceStatus.OSCE_NEW)
					{	
						OsceProxy proxy1=osceProxy;
						OsceRequest osceRequest=requests.osceRequest();
						osceProxy=osceRequest.edit(osceProxy);
						osceProxy.setOsceStatus(OsceStatus.OSCE_BLUEPRINT);
						osceRequest.persist().using(osceProxy).fire(new OSCEReceiver<Void>() {

							@Override
							public void onSuccess(Void response) {
								Log.info("Osce status change to blueprint");
								//view.getScrollPanel().setStyleName("Osce_BluePrint_Status");
								//view.getScrollPanel().setStyleDependentName("BluePrint", false);
								//osceProxy.setOsceStatus(OsceStatus.OSCE_BLUEPRINT);
								view.getScrollPanel().removeStyleDependentName("NEW");
								view.getScrollPanel().addStyleDependentName("BluePrint");
								
								saveOscePostBlueprint(osceProxy);
							}
							
						});
						
					}
					else
						saveOscePostBlueprint(osceProxy);
				}
				public void saveOscePostBlueprint(final OsceProxy osceProxy1)
				{			
					Log.info("Call Save() : " + osceProxy.getId());
					Log.info("Total Widget is:" + newPostAddHP.getWidgetCount());
					if((osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue())==null)
					{
						Window.alert("Please Select Post Type.");
						return;
					}
				/*	if((osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue())==null && (osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue())!=PostType.BREAK)
					{
						Window.alert("Please Select the Specialization.");
						return;
					}	*/		
					//else
					//{
						
						if((osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue())==null)
						{
							Log.info("Role Topic is Null");				
						}
						Log.info("Not Null");
					
						Log.info("~In PERSIST OTHER");
						
							OscePostBlueprintRequest oscePostRequest= requests.oscePostBlueprintRequest();
							final OscePostBlueprintProxy oscePostBlueprintProxy1 = oscePostRequest.create(OscePostBlueprintProxy.class);
							
							//create blue print proxy
							oscePostBlueprintProxy1.setRoleTopic((osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue())==null? null:(RoleTopicProxy)osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue());
							oscePostBlueprintProxy1.setOsce(osceProxy);					
							oscePostBlueprintProxy1.setPostType((PostType)osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue());
							oscePostBlueprintProxy1.setSpecialisation((osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue())==null? null:(SpecialisationProxy)osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue());
							oscePostBlueprintProxy1.setIsPossibleStart(null);
							oscePostBlueprintProxy1.setSequenceNumber(++maxSeq);
							
							oscePostRequest.persist().using(oscePostBlueprintProxy1).fire(new OSCEReceiver<Void>() 
							{									
									@Override
									public void onSuccess(Void response) 
									{
										Log.info("~Success Call....");
										Log.info("~oscePostBlueprintRequest.persist()");	
										Log.info("Add New Post in Keyword Table");
										Log.info("Data Saved Successfully....");	
										requests.find(oscePostBlueprintProxy1.stableId()).with("roleTopic","specialisation").fire(new OSCEReceiver<Object>() {

											@Override
											public void onSuccess(Object response) 
											{
												// TODO Auto-generated method stub	
												final OscePostBlueprintProxy oscePostBlueprintProxy=(OscePostBlueprintProxy)response;
												Log.info("~~Total Widget Before is:" + newPostAddHP.getWidgetCount());											
												
												Log.info("~~Total Widget After is:" + newPostAddHP.getWidgetCount());
												
												if(osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()==PostType.ANAMNESIS_THERAPY || osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue()==PostType.PREPARATION)
												{
													Log.info("~In PERSIST ANAMNESIS_THERAPY || PREPARATION");
													
													OscePostBlueprintRequest oscePostRequestNew= requests.oscePostBlueprintRequest();
													final OscePostBlueprintProxy oscePostBlueprintProxyNew = oscePostRequestNew.create(OscePostBlueprintProxy.class);
													
													oscePostBlueprintProxyNew.setRoleTopic((osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue())==null? null:(RoleTopicProxy)osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().getValue());
													oscePostBlueprintProxyNew.setOsce(osceProxy);						
													oscePostBlueprintProxyNew.setPostType((PostType)osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().getValue());
													oscePostBlueprintProxyNew.setSpecialisation((osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue())==null? null:(SpecialisationProxy)osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().getValue());
													
													oscePostBlueprintProxyNew.setIsPossibleStart(null);
													oscePostBlueprintProxyNew.setSequenceNumber(++maxSeq);
													Log.info(""+oscePostBlueprintProxyNew.getPostType().name());
													Log.info(""+oscePostBlueprintProxyNew.getOsce().getId());
																	
													oscePostRequestNew.persist().using(oscePostBlueprintProxyNew).fire(new OSCEReceiver<Void>() 
													{									
															@Override
															public void onSuccess(Void response) 
															{					
																requests.find(oscePostBlueprintProxyNew.stableId()).with("roleTopic","specialisation").fire(new OSCEReceiver<Object>()
																		{

																			@Override
																			public void onSuccess(
																					Object response) {
																				Log.info("~Success Call....");	
																				newPostAddHP.insert(setAnemniesOsceBluePrintHP(oscePostBlueprintProxy, (((OscePostBlueprintProxy)response))),newPostAddHP.getWidgetCount());
																				
																			}
																	
																		});
																														
															}																		
													});
													
												}
												else
												{
													newPostAddHP.insert(setNewOsceBluePrintHP(((OscePostBlueprintProxy)response)),newPostAddHP.getWidgetCount());
												}
												
												final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
												dialogBox.showConfirmationDialog(constants.saveOsceBlueprint());
												dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
													
													@Override
													public void onClick(ClickEvent event) {
														dialogBox.hide();
													
														
														
													}
												});
												
													dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
													
													@Override
													public void onClick(ClickEvent event) {
														dialogBox.hide();
														
													
														
													}
												});
												//Window.alert("Osce Post Blueprint Saved Successfully.");
												osceCreatePostBluePrintSubViewImpl.getPostTypeListBox().setValue(null);
												osceCreatePostBluePrintSubViewImpl.getRoleTopicListBox().setValue(null);
												osceCreatePostBluePrintSubViewImpl.getSpecializationListBox().setValue(null);
											}
										});
									}																		
							});
												
							
							
					}
				//}

				public OscePostViewImpl setNewOsceBluePrintHP(OscePostBlueprintProxy oscePostBlueprintProxy)
				{
					
						Log.info("~setNewOsceBluePrintHP OscePostBluePrintProxy: " + oscePostBlueprintProxy.getId() + "as" + newPostAddHP.getWidgetCount()) ;
						int innerindex=newPostAddHP.getWidgetCount();
						
						OscePostSubViewImpl tempOscePostSubViewImpl=new OscePostSubViewImpl();										
						tempOscePostSubViewImpl.enableDisableforBluePrintStatus();
						
						
						tempOscePostSubViewImpl.getPostNameLbl().setText("Post "+oscePostBlueprintProxy.getSequenceNumber());
						tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
						tempOscePostSubViewImpl.getRoleTopicLbl().setText(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName());
						tempOscePostSubViewImpl.getSpecializationLbl().setText(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName());			
						oscePostViewImpl=new OscePostViewImpl();	
						oscePostViewImpl.setStyleName("Osce-Status-BluePrint-Save", true);
						oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getDragController().makeDraggable(oscePostViewImpl,oscePostViewImpl.getPostTypeLbl());
						
						oscePostViewImpl.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR MAIN POST VIEW	
						
						oscePostViewImpl.getPostTypeLbl().setText(oscePostBlueprintProxy.getPostType().name());	
						if(oscePostSubViewImpl == null){
							oscePostSubViewImpl = new ArrayList<OscePostSubViewImpl>();
						}
						oscePostSubViewImpl.add(tempOscePostSubViewImpl);					
						oscePostSubViewImpl.get(innerindex).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW						
						oscePostViewImpl.getOscePostSubViewHP().add(tempOscePostSubViewImpl);	// ADD SUBVIEW IN POSTVIEW
						oscePostSubViewImpl.get(innerindex).oscePostBlueprintProxy=oscePostBlueprintProxy;	
						oscePostViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
						
						
						
						return oscePostViewImpl;
					
				
				}
				
				public OscePostViewImpl setAnemniesOsceBluePrintHP(OscePostBlueprintProxy oscePostBlueprintProxy,OscePostBlueprintProxy oscePostBlueprintProxyNext)
				{
					
					Log.info("~setNewOsceBluePrintHP OscePostBluePrintProxy: " + oscePostBlueprintProxy.getId() + "as" + newPostAddHP.getWidgetCount()) ;
					int innerindex=newPostAddHP.getWidgetCount();
					
					OscePostSubViewImpl tempOscePostSubViewImpl=new OscePostSubViewImpl();										
					tempOscePostSubViewImpl.enableDisableforBluePrintStatus();
					
					
					tempOscePostSubViewImpl.getPostNameLbl().setText("Post "+oscePostBlueprintProxy.getSequenceNumber());
					tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
					tempOscePostSubViewImpl.getRoleTopicLbl().setText(oscePostBlueprintProxy.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxy.getRoleTopic().getName());
					tempOscePostSubViewImpl.getSpecializationLbl().setText(oscePostBlueprintProxy.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxy.getSpecialisation().getName());			
					oscePostViewImpl=new OscePostViewImpl();
					//oscePostViewImpl.setStylePrimaryName("Osce-Status-BluePrint");
					oscePostViewImpl.setStyleName("Osce-Status-BluePrint-Save", true);
					//oscePostViewImpl.addStyleDependentName("Save");
					//oscePostViewImpl.setStyleName("Osce-Status-BluePrint-Save");
					oSCENewSubViewImpl.getOscePostBluePrintSubViewImpl().getDragController().makeDraggable(oscePostViewImpl,oscePostViewImpl.getPostTypeLbl());
					
					oscePostViewImpl.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR MAIN POST VIEW	
					
					oscePostViewImpl.getPostTypeLbl().setText(oscePostBlueprintProxy.getPostType().name());	
					if(oscePostSubViewImpl == null){
						oscePostSubViewImpl = new ArrayList<OscePostSubViewImpl>();
					}
					oscePostSubViewImpl.add(tempOscePostSubViewImpl);	
					tempOscePostSubViewImpl.setDelegate(circuitDetailsActivity);
					tempOscePostSubViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
					//oscePostSubViewImpl.get(innerindex).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW						
					oscePostViewImpl.getOscePostSubViewHP().add(tempOscePostSubViewImpl);	// ADD SUBVIEW IN POSTVIEW
					oscePostSubViewImpl.get(innerindex).oscePostBlueprintProxy=oscePostBlueprintProxy;	
					oscePostViewImpl.oscePostBlueprintProxy=oscePostBlueprintProxy;
					
					if(oscePostBlueprintProxy.getPostType()==PostType.ANAMNESIS_THERAPY || oscePostBlueprintProxy.getPostType()==PostType.PREPARATION)
					{
						Log.info("~Anemnis");				
							
						OscePostSubViewImpl tempOscePostSubViewImplNext=new OscePostSubViewImpl();
							tempOscePostSubViewImplNext.enableDisableforBluePrintStatus();	
							tempOscePostSubViewImplNext.setDelegate(circuitDetailsActivity);	// SET DELEGATE FOR SUBVIEW
								tempOscePostSubViewImplNext.getSpecializationLbl().setText(oscePostBlueprintProxyNext.getSpecialisation()==null?constants.select()+": ":oscePostBlueprintProxyNext.getSpecialisation().getName());
							tempOscePostSubViewImplNext.getRoleTopicLbl().setText(oscePostBlueprintProxyNext.getRoleTopic()==null?constants.select()+": ":oscePostBlueprintProxyNext.getRoleTopic().getName());
							oscePostViewImpl.oscePostBlueprintProxyNext=oscePostBlueprintProxyNext;
							tempOscePostSubViewImplNext.getPostNameLbl().setText("Post "+ oscePostBlueprintProxyNext.getSequenceNumber());
							tempOscePostSubViewImplNext.oscePostBlueprintProxy=oscePostBlueprintProxyNext;
							Log.info("OsceBluerint Next Id: " + oscePostBlueprintProxyNext.getId());												
							oscePostSubViewImpl.add(tempOscePostSubViewImplNext);	
							oscePostSubViewImpl.get(innerindex).setDelegate(circuitDetailsActivity);
							innerindex++;																							
							oscePostViewImpl.getOscePostSubViewHP().add(tempOscePostSubViewImplNext);	// ADD SUBVIEW IN POSTVIEW	
							oscePostViewImpl.oscePostBlueprintProxyNext=oscePostBlueprintProxyNext;
							oscePostSubViewImpl.get(innerindex).setDelegate(circuitDetailsActivity); // SET DELEGATE FOR SUBVIEW
							oscePostSubViewImpl.get(innerindex).oscePostBlueprintProxyNext=oscePostBlueprintProxyNext;	
							
																																											
					}	
					
					return oscePostViewImpl;
					
				
				}
				
				@Override
				public void specializationEditClicked(final OscePostSubViewImpl oscePostSubViewImpledit) 
				{
					
					Log.info("~specializationEditClicked() from Activity");
					
					oscePostSubViewImpledit.oscePostBlueprintProxy=this.oscePostBlueprintProxy;					
					requests.specialisationRequest().findAllSpecialisations().fire(new OSCEReceiver<List<SpecialisationProxy>>() 
					{
						public void onSuccess(List<SpecialisationProxy> response) 
						{				
							
							//Log.info("oscePostSubViewImpledit: " + oscePostSubViewImpledit.oscePostBlueprintProxy.getId());
							
							((OscePostSubViewImpl)oscePostSubViewImpledit).createOptionPopup();						
							HorizontalPanel spHorizontalPanel=((OscePostSubViewImpl)oscePostSubViewImpledit).getSpecializationHP();
							HorizontalPanel rtHorizontalPanel=((OscePostSubViewImpl)oscePostSubViewImpledit).getRoleTopicHP();
							
							((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).setPopupPosition(spHorizontalPanel.getAbsoluteLeft()-40, rtHorizontalPanel.getAbsoluteTop()-80);					
							((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).getListBox().setValue(null);
							ArrayList proxy=new ArrayList();
							proxy.addAll(response);
							((OscePostSubViewImpl)oscePostSubViewImpledit).popupView.getListBox().setAcceptableValues(proxy);
							((ListBoxPopupViewImpl)((OscePostSubViewImpl)oscePostSubViewImpledit).popupView).show();										
							((OscePostSubViewImpl)oscePostSubViewImpledit).popupView.getListBox().setValue(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation());
							
						
							}
					});	
				}

				@Override
				public void roleEditClicked(final OscePostSubViewImpl oscePostSubViewImpledit) 
				{
					if(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation()==null)
					{
						Window.alert("Select Specialisation.");
						return;
					}
					if(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation()==null)
					{
						Window.alert("Please Select Specialisation.");
						return;
					}
					Log.info("~roleEditClicked() from Activity" + oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation().getId());	
											
					((OscePostSubViewImpl)oscePostSubViewImpledit).createOptionPopup();						
					HorizontalPanel spHorizontalPanel=((OscePostSubViewImpl)oscePostSubViewImpledit).getSpecializationHP();
					HorizontalPanel rtHorizontalPanel=((OscePostSubViewImpl)oscePostSubViewImpledit).getRoleTopicHP();
					((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.setPopupPosition(spHorizontalPanel.getAbsoluteLeft()-40, rtHorizontalPanel.getAbsoluteTop()-80);
					((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.show();
					
					
					
					requests.roleTopicRequestNonRoo().findRoleTopicBySpecialisation(oscePostSubViewImpledit.oscePostBlueprintProxy.getSpecialisation().getId()).fire(new OSCEReceiver<List<RoleTopicProxy>>() 
					{
						@Override
						public void onSuccess(List<RoleTopicProxy> response) 
						{
							Log.info("Find RoleTopic for Specialization Size: " + response.size());	
							((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getListBox().setValue(null);
							ArrayList proxy=new ArrayList();
							proxy.addAll(response);
							((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getListBox().setAcceptableValues(proxy);	
							if(oscePostSubViewImpledit.oscePostBlueprintProxy.getRoleTopic()==null)
							{
								Log.info("Null Role Topic.");
							}
							else
							{						
								((OscePostSubViewImpl)oscePostSubViewImpledit).listBoxPopupViewImpl.getListBox().setValue(oscePostSubViewImpledit.oscePostBlueprintProxy.getRoleTopic());	
							}					
						}
					});
					
				}

				@Override
				public void saveSpecialisation(final OscePostSubViewImpl oscePostSubViewImplok) 
				{
					Log.info("~okClicked() from Activity");			
					Log.info("savSpecialisation");
					
					//oscePostSubViewImplok.oscePostBlueprintProxy=oscePostBlueprintProxy;
					
					Log.info("oscePostSubViewImpledit" + oscePostSubViewImplok.oscePostBlueprintProxy.getId());	
					Log.info("oscePostSubViewImpledit" + oscePostSubViewImplok.oscePostBlueprintProxy.getId());
										
					OscePostBlueprintProxy oscePostBlueprintProxy=oscePostSubViewImplok.oscePostBlueprintProxy;
					final SpecialisationProxy specialisationProxy=(SpecialisationProxy)oscePostSubViewImplok.getListBoxPopupViewImpl().getListBox().getValue();
					
					Log.info("Specialisation Selected: " + specialisationProxy.getName());
					
					OscePostBlueprintRequest oscePostBlueprintRequest=requests.oscePostBlueprintRequest();
					oscePostBlueprintProxy=oscePostBlueprintRequest.edit(oscePostBlueprintProxy);		
					
					oscePostBlueprintProxy.setSpecialisation(specialisationProxy);
					oscePostBlueprintProxy.setRoleTopic(null);
					
					oscePostSubViewImplok.oscePostBlueprintProxy=oscePostBlueprintProxy;
					Log.info("oscePostSubViewImplok : " + oscePostSubViewImplok.oscePostBluePrintMap.size());
					oscePostBlueprintRequest.persist().using(oscePostBlueprintProxy).fire(new OSCEReceiver<Void>(oscePostSubViewImplok.oscePostBluePrintMap)
					{
						@Override
						public void onSuccess(Void response) 
						{
							Log.info("Success saveSpecialisation ");
							oscePostSubViewImplok.getSpecializationLbl().setText(specialisationProxy.getName());
							oscePostSubViewImplok.listBoxPopupViewImpl.hide();					
							//Window.alert("The Role Topic for Specialisation " + specialisationProxy.getName()+" is deleted, You need to Select thr Role.");
							oscePostSubViewImplok.getRoleTopicLbl().setText(constants.select()+":");
							oscePostSubViewImplok.listBoxPopupViewImpl.hide();	
						}
					
					});		
					
				}

				@Override
				public void saveRoleTopic(final OscePostSubViewImpl oscePostSubViewImplok) 
				{
					Log.info("~okClicked() from Activity");			
					Log.info("saveRoleTopic");
					Log.info("oscePostSubViewImpledit" + oscePostSubViewImplok.oscePostBlueprintProxy.getId());	
					
					OscePostBlueprintProxy oscePostBlueprintProxy=oscePostSubViewImplok.oscePostBlueprintProxy;
					final RoleTopicProxy roleTopicProxy=(RoleTopicProxy)oscePostSubViewImplok.getListBoxPopupViewImpl().getListBox().getValue();
					
					Log.info("Role Topic Selected: " + roleTopicProxy.getName());
					
					OscePostBlueprintRequest oscePostBlueprintRequest=requests.oscePostBlueprintRequest();
					oscePostBlueprintProxy=oscePostBlueprintRequest.edit(oscePostBlueprintProxy);
					oscePostBlueprintProxy.setRoleTopic(roleTopicProxy);
					oscePostSubViewImplok.oscePostBlueprintProxy=oscePostBlueprintProxy;
					Log.info("oscePostSubViewImplok : " + oscePostSubViewImplok.oscePostBluePrintMap.size());
					oscePostBlueprintRequest.persist().using(oscePostBlueprintProxy).fire(new OSCEReceiver<Void>(oscePostSubViewImplok.oscePostBluePrintMap)
					{
						@Override
						public void onSuccess(Void response) 
						{
							Log.info("Success saveSpecialisation ");
							oscePostSubViewImplok.getRoleTopicLbl().setText(roleTopicProxy.getName());
							oscePostSubViewImplok.listBoxPopupViewImpl.hide();
						}
					
					});			
				}

				@Override
				public void deletePostClicked(final OscePostViewImpl oscePostViewImpl) 
				{
					Log.info("Delete Post Call.."  + oscePostViewImpl.oscePostBlueprintProxy.getId());
					requests.oscePostBlueprintRequest().remove().using(oscePostViewImpl.oscePostBlueprintProxy).fire(new OSCEReceiver<Void>() 
					{
						public void onSuccess(Void ignore) 
						{
							
							
							if(oscePostViewImpl.oscePostBlueprintProxy.getPostType()!=null && (oscePostViewImpl.oscePostBlueprintProxy.getPostType()==PostType.ANAMNESIS_THERAPY || oscePostViewImpl.oscePostBlueprintProxy.getPostType()==PostType.PREPARATION))				
							{
								Log.info("Delete Two Records.");
								/*requests.oscePostBlueprintRequest().findOscePostBlueprint((oscePostViewImpl.oscePostBlueprintProxyNext.getId())).fire(new OSCEReceiver<OscePostBlueprintProxy>() {

									@Override
									public void onSuccess(OscePostBlueprintProxy response) 
									{*/
										requests.oscePostBlueprintRequest().remove().using(oscePostViewImpl.oscePostBlueprintProxyNext).fire(new OSCEReceiver<Void>() 
										{
											public void onSuccess(Void ignore) 
											{
													//Window.alert("TWO POST Successfully Deleted");	
													oscePostViewImpl.removeFromParent();
												//	maxSeq--;
												//	maxSeq--;
													//updateBluePrintSequences();
											}
										});		
									/*}
								
								});*/
							}
							else
							{
								//Window.alert("POST Successfully Deleted");
								oscePostViewImpl.removeFromParent();
							//	maxSeq--;
							}
						}
					});	
												
					
				}
				
				// OSCE Day Assignment Start 
				
				public void setOsceStatusStyle(String style){
					circuitOsceSubViewImpl.setStyleName(style);
				}
				public void setDayStatusStyle(String style){
					osceDayViewImpl.setStyleName(style);
				}
				public void addTimeHendlers(){
				
					
					osceDayViewImpl.startTimeTextBox.addBlurHandler(new BlurHandler() {
						public boolean dayStartTimeflag=true;
						@Override
						public void onBlur(BlurEvent event) {
							if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED){
							if(osceDayViewImpl.startTimeTextBox.getValue() !=null ){
								
								if(! checkStarttimeValidation()){
									return;
								}
								
							}
							
							Time startDate=new Time( osceProxy.getOsce_days().iterator().next().getTimeStart().getTime());
							Time endDate=new Time( osceProxy.getOsce_days().iterator().next().getTimeEnd().getTime());
							
							long diff=Math.abs((startDate.getTime())-endDate.getTime());
							
							Log.info("diff--"+diff);
							
						//	DateFormat dateformat = new SimpleDateFormat("HH:mm");
							
							try{
								//Date newDateTime=DateTimeFormat.getShortDateTimeFormat().parse((osceDayViewImpl.startTimeTextBox.getValue()));
								
								//Log.info("Before Date Format" + osceDayViewImpl.startTimeTextBox.getValue());
								
								Date newDateTime=new Date();
								String hrs=osceDayViewImpl.startTimeTextBox.getValue().substring(0, 2);
								newDateTime.setHours(new Integer(hrs));
								String mts=osceDayViewImpl.startTimeTextBox.getValue().substring(3, 5);
								newDateTime.setMinutes(new Integer(mts));
								
								//Date oldDate = new Date(osceDayViewImpl.startTimeTextBox.getValue());
								//Date newDateTime=DateTimeFormat.getShortDateTimeFormat().parse(oldDate.toString());
								
								
								
								//Log.info("After Date Format" + newDateTime);
							//	Date newDateTime=dateformat.parse((osceDayViewImpl.startTimeTextBox.getValue()));
								Log.info("New Date is" + newDateTime);
								
								Time newTime=new Time(newDateTime.getTime());
								newTime.setTime(newTime.getTime() + diff);
								osceDayViewImpl.endTimeTextBox.setText(newTime.toString().substring(0,5));
							}catch(Exception e){
								Log.info("Parse Exception Occured :");
								e.printStackTrace();
							}
							
						}
							else{
									if(osceDayViewImpl.startTimeTextBox.getValue() !=null )
									if(! checkStarttimeValidation()){
										return;
									}
							}
						}
					}); 
					

					osceDayViewImpl.endTimeTextBox.addBlurHandler(new BlurHandler() {
						
						@Override
						public void onBlur(BlurEvent event) {
							if(osceProxy.getOsceStatus()==OsceStatus.OSCE_GENRATED){
								
								if(osceDayViewImpl.endTimeTextBox.getValue() !=null ){
									
									if( ! checkEndTimeValidation())
									return;
								}
							Time startDate=new Time( osceProxy.getOsce_days().iterator().next().getTimeStart().getTime());
							Time endDate=new Time( osceProxy.getOsce_days().iterator().next().getTimeEnd().getTime());
							
							long diff=Math.abs((startDate.getTime())-endDate.getTime());
							

							System.out.println("diff--"+diff);
							
							
							//DateFormat dateformat = new SimpleDateFormat("HH:mm");
							
							try{
								Date endTime=new Date();
								String hrs=osceDayViewImpl.endTimeTextBox.getValue().substring(0, 2);
								endTime.setHours(new Integer(hrs));
								String mts=osceDayViewImpl.endTimeTextBox.getValue().substring(3, 5);
								endTime.setMinutes(new Integer(mts));
								
							//	Date newDateTime=DateTimeFormat.getShortDateTimeFormat().parse(endTime.toString());
								
								//Date newDateTime=dateformat.parse((osceDayViewImpl.endTimeTextBox.getValue()));
								Log.info("New Date is" + endTime);
								
								Time newTime=new Time(endTime.getTime());
								newTime.setTime(newTime.getTime() - diff);
								osceDayViewImpl.startTimeTextBox.setText(newTime.toString().substring(0,5));
							} catch(Exception e){
								Log.info("Parse Exception Occured :");
								e.printStackTrace();
							}

							}
							else{
								if(osceDayViewImpl.endTimeTextBox.getValue() !=null ){
									
									if(! checkEndTimeValidation())
									return;
								}
							}
						}
					});
				}
				public boolean checkStarttimeValidation(){
					boolean dayStartTimeValidflag=true;
					String sTimeValue=osceDayViewImpl.startTimeTextBox.getValue();
					if(! sTimeValue.matches("^[]0-9]{2}\\:[0-9]{2}$")){
						Window.alert("please Enter valid formatted Time Valid format is HH:MM");
						dayStartTimeValidflag=false;
						return dayStartTimeValidflag;
					}
					
					if(new Integer(sTimeValue.substring(0,2)) >= 24){
						Window.alert("Please Enter Valid Hour (Allowed Till 24)");
						dayStartTimeValidflag=false;
						return dayStartTimeValidflag;
					}
					if(new Integer(sTimeValue.substring(3,5))> 59){
						Window.alert("Please Enter Valid Minutes (Allowed Till 59)");
						dayStartTimeValidflag=false;
						return dayStartTimeValidflag;
					}
					return dayStartTimeValidflag;
				}
				public boolean checkEndTimeValidation(){
					boolean dayEndTimeValidFlag=true;
					String sTimeValue=osceDayViewImpl.endTimeTextBox.getValue();
					if(! sTimeValue.matches("^[]0-9]{2}\\:[0-9]{2}$")){
						Window.alert("please Enter valid formatted Time Valid format is HH:MM");
						dayEndTimeValidFlag=false;
						return dayEndTimeValidFlag;
					}
					
					if(new Integer(sTimeValue.substring(0,2)) >= 24){
						Window.alert("Please Enter Valid Hour (Allowed Till 24)");
						dayEndTimeValidFlag=false;
						return dayEndTimeValidFlag;
					}
					if(new Integer(sTimeValue.substring(3,5))> 59){
						Window.alert("Please Enter Valid Minutes (Allowed Till 59)");
						dayEndTimeValidFlag=false;
						return dayEndTimeValidFlag;
					}
					return dayEndTimeValidFlag;
				}
				
				
				
				@Override
				public void saveOsceDayValue(OsceDayProxy osceDayProxy,
						boolean insertflag) {
					Log.info("Insert Value flag status :" +insertflag);
					if(insertflag==true){
						
						
						if(osceDayViewImpl.dateTextBox.getValue()==null){
							Window.alert("Data must not empty");
							return;
						}
						if(osceDayViewImpl.startTimeTextBox.getValue()==null){
							Window.alert("Start Time must not Empty");
							return;
						}
						if(osceDayViewImpl.startTimeTextBox.getValue() !=null ){
							
							if(! checkStarttimeValidation())
							return;
						}
										
						if(osceDayViewImpl.endTimeTextBox.getValue()==null){
							Window.alert("EndDate Mus Not Empty");
							return;
						}
						if(osceDayViewImpl.endTimeTextBox.getValue() !=null ){
							if(! checkEndTimeValidation())
								return;
						}
						
						
						OsceDayRequest osceDayReq = requests.osceDayRequest();
						osceDayProxy = osceDayReq.create(OsceDayProxy.class);
			
						osceDayProxy.setOsceDate(osceDayViewImpl.dateTextBox.getValue());
						
						osceDayProxy.setOsce(osceProxy);
						
						//DateFormat updatetdNewStartTimeDateFormat = new SimpleDateFormat("HH:mm");
						//DateFormat updatetdNewEndTimeDateFormat = new SimpleDateFormat("HH:mm");
						
						
						
						try{
							Log.info("In side new Day persist  try block");
							
							Date newDateWithStartTime=new Date();
							
							String hrs=osceDayViewImpl.startTimeTextBox.getValue().substring(0, 2);
							newDateWithStartTime.setHours(new Integer(hrs));
							
							String mts=osceDayViewImpl.startTimeTextBox.getValue().substring(3, 5);
							newDateWithStartTime.setMinutes(new Integer(mts));
							
							Date newDateWitnEndTime=new Date();
							hrs=osceDayViewImpl.endTimeTextBox.getValue().substring(0, 2);
							newDateWitnEndTime.setHours(new Integer(hrs));
							mts=osceDayViewImpl.endTimeTextBox.getValue().substring(3, 5);
							newDateWitnEndTime.setMinutes(new Integer(mts));
							
							
							//Date newDateWithStartTime =updatetdNewStartTimeDateFormat.parse(osceDayViewImpl.startTimeTextBox.getValue());
							//Date newDateWitnEndTime = updatetdNewEndTimeDateFormat.parse(osceDayViewImpl.endTimeTextBox.getValue());
							
							Date oldDate = osceDayViewImpl.dateTextBox.getValue();
												
							Log.info("Updated Start Time :" +newDateWithStartTime.getTime());
							
							Date newStartTimeDate = new Date(oldDate.getYear(), oldDate.getMonth(), oldDate.getDate(), newDateWithStartTime.getHours(),newDateWithStartTime.getMinutes());
							Date newEndTimeDate = new Date(oldDate.getYear(), oldDate.getMonth(), oldDate.getDate(), newDateWitnEndTime.getHours(),newDateWitnEndTime.getMinutes());
							
										
							Log.info("Value of newDate" + newStartTimeDate);
							Log.info("Value getSuccessfully");
							
							osceDayProxy.setTimeStart(newStartTimeDate);
							osceDayProxy.setTimeEnd(newEndTimeDate);
					
						}catch(Exception e){
							Log.info("Parsing exception During new Day persist");
						}
						
						// Highlight onViolation	
						Log.info(""+osceDayViewImpl.osceDayMap.size());
						osceDayReq.persist().using(osceDayProxy).fire(new OSCEReceiver<Void>(osceDayViewImpl.osceDayMap) {
						// E Highlight onViolation
							
							@Override
							public void onSuccess(Void response) {
								final MessageConfirmationDialogBox dialogbox=new MessageConfirmationDialogBox(constants.success());								
								dialogbox.showConfirmationDialog(constants.osceDaySuccess());
								Log.info("Osce Day Saved successfully");
							}
						});
					}
					else{
						
						Log.info("Inside to Update Day");
						Log.info(""+osceDayViewImpl.dateTextBox.getValue());
						if(osceDayViewImpl.dateTextBox.getValue()==null){
							Window.alert("Data must not empty");
							return;
						}
						if(osceDayViewImpl.startTimeTextBox.getValue()==null){
							Window.alert("Start Time must not Empty");
							return;
						}
						if(osceDayViewImpl.startTimeTextBox.getValue() !=null ){

								if(! checkStarttimeValidation())
									return;
						}
						if(osceDayViewImpl.endTimeTextBox.getValue()==null){
							Window.alert("EndDate Mus Not Empty");
							return;
						}
						if(osceDayViewImpl.endTimeTextBox.getValue() !=null ){

							if(! checkEndTimeValidation())
								return;
						}
						
						
						OsceDayRequest osceDayReq = requests.osceDayRequest();
						osceDayProxy = osceDayReq.edit(osceDayProxy);
						
						osceDayProxy.setOsceDate(osceDayViewImpl.dateTextBox.getValue());
						
						
						//DateFormat updatetdNewStartTimeDateFormat = new SimpleDateFormat("HH:mm");
						//DateFormat updatetdNewEndTimeDateFormat = new SimpleDateFormat("HH:mm");
						
						
						
						try{
							Log.info("In side update Day persist try block");
							
							Date newDateWithStartTime=new Date();
							String hrs=osceDayViewImpl.startTimeTextBox.getValue().substring(0, 2);
							newDateWithStartTime.setHours(new Integer(hrs));
							String mts=osceDayViewImpl.startTimeTextBox.getValue().substring(3, 5);
							newDateWithStartTime.setMinutes(new Integer(mts));
							
							Date newDateWitnEndTime=new Date();
							 hrs=osceDayViewImpl.endTimeTextBox.getValue().substring(0, 2);
							newDateWitnEndTime.setHours(new Integer(hrs));
							 mts=osceDayViewImpl.endTimeTextBox.getValue().substring(3, 5);
							newDateWitnEndTime.setMinutes(new Integer(mts));
							//Date newDateWithStartTime=DateTimeFormat.getShortDateTimeFormat().parse((osceDayViewImpl.startTimeTextBox.getValue()));
							
							//Date newDateWitnEndTime=DateTimeFormat.getShortDateTimeFormat().parse((osceDayViewImpl.endTimeTextBox.getValue()));
							//Date newDateWithStartTime =updatetdNewStartTimeDateFormat.parse(osceDayViewImpl.startTimeTextBox.getValue());
							//Date newDateWitnEndTime = updatetdNewEndTimeDateFormat.parse(osceDayViewImpl.endTimeTextBox.getValue());
							
							Date oldDate = osceDayProxy.getOsceDate();
												
							Log.info("Updated Start Time :" +newDateWithStartTime.getTime());
							
							Date newStartTimeDate = new Date(oldDate.getYear(), oldDate.getMonth(), oldDate.getDate(), newDateWithStartTime.getHours(),newDateWithStartTime.getMinutes());
							Date newEndTimeDate = new Date(oldDate.getYear(), oldDate.getMonth(), oldDate.getDate(), newDateWitnEndTime.getHours(),newDateWitnEndTime.getMinutes());
							
							//oldDate.setTime(osceDayViewImpl.startTimeTextBox.getValue());
							//Date updatedstartTime = updatestartTimeDateFormat.parse(osceDayViewImpl.startTimeTextBox.getValue());
							
							Log.info("Value of newDate" + newStartTimeDate);
							Log.info("Value getSuccessfully");
							
							osceDayProxy.setTimeStart(newStartTimeDate);
							osceDayProxy.setTimeEnd(newEndTimeDate);
					
						}catch(Exception e){
							Log.info("Parsing exception During Day persist");
						}
						//osceDayProxy.setOsce(osceProxy);
						// Highlight onViolation	
						Log.info(""+osceDayViewImpl.osceDayMap.size());
						osceDayReq.persist().using(osceDayProxy).fire(new OSCEReceiver<Void>(osceDayViewImpl.osceDayMap) {
						// E: Highlight onViolation	
							@Override
							public void onSuccess(Void response) {
								final MessageConfirmationDialogBox dialogbox=new MessageConfirmationDialogBox(constants.success());
								
								dialogbox.showConfirmationDialog(constants.osceDaySuccess());
								Log.info("Osce Day Updated successfully");
							}
						}); 

						
					}
					
				}
				
				//sequence start
				
				private void addClickHandler(final SequenceOsceSubViewImpl sequenceOsceSubViewImpl)
				{
					sequenceOsceSubViewImpl.ok.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							
							sequenceOsceSubViewImpl.chaneNameOfSequence.setVisible(false);
							sequenceOsceSubViewImpl.ok.setVisible(false);
							sequenceOsceSubViewImpl.nameOfSequence.setText(sequenceOsceSubViewImpl.chaneNameOfSequence.getValue());
						//	sequenceOsceSubViewImpl.osceSequenceProxy.setLabel(sequenceOsceSubViewImpl[i].nameOfSequence.getText());
							System.out.println(sequenceOsceSubViewImpl.osceSequenceProxy.getLabel());
							sequenceOsceSubViewImpl.ok.setVisible(false);
							sequenceOsceSubViewImpl.chaneNameOfSequence.setVisible(false);
							
							OsceSequenceRequest osceSequenceRequest=requests.osceSequenceRequest();
							OsceSequenceProxy proxy=osceSequenceRequest.edit(sequenceOsceSubViewImpl.osceSequenceProxy);
							proxy.setLabel(sequenceOsceSubViewImpl.nameOfSequence.getText());
							sequenceOsceSubViewImpl.osceSequenceProxy=proxy;
							
							osceSequenceRequest.persist().using(proxy).fire(new Receiver<Void>() {
								@Override
								public void onSuccess(Void response) {	
									// TODO Auto-generated method stub
							//		System.out.println("INside success");
									Log.info("osce sequence updated  successfully with label--"+sequenceOsceSubViewImpl.osceSequenceProxy.getLabel());
								//	init2();
								
									}
							});
							
						}
					});
				}
				@Override
				public void saveSequenceLabel(final SequenceOsceSubViewImpl sequenceOsceSubViewImpl) {
					// TODO Auto-generated method stub
					Log.info("ok button click");
					
					sequenceOsceSubViewImpl.chaneNameOfSequence.setVisible(false);
					sequenceOsceSubViewImpl.ok.setVisible(false);
					sequenceOsceSubViewImpl.nameOfSequence.setText(sequenceOsceSubViewImpl.chaneNameOfSequence.getValue());
				//	sequenceOsceSubViewImpl.osceSequenceProxy.setLabel(sequenceOsceSubViewImpl[i].nameOfSequence.getText());
					System.out.println(sequenceOsceSubViewImpl.osceSequenceProxy.getLabel());
					
					OsceSequenceRequest osceSequenceRequest=requests.osceSequenceRequest();
					OsceSequenceProxy proxy=osceSequenceRequest.edit(sequenceOsceSubViewImpl.osceSequenceProxy);
					proxy.setLabel(sequenceOsceSubViewImpl.nameOfSequence.getText());
					sequenceOsceSubViewImpl.osceSequenceProxy=proxy;
					
					// Highlight onViolation
					Log.info("Map Size: "+sequenceOsceSubViewImpl.osceSequenceMap.size());
					osceSequenceRequest.persist().using(proxy).fire(new OSCEReceiver<Void>(sequenceOsceSubViewImpl.osceSequenceMap) {
					// E Highlight onViolation
						@Override
						public void onSuccess(Void response) {	
							// TODO Auto-generated method stub
					//		System.out.println("INside success");
							Log.info("osce sequence updated  successfully with label--"+sequenceOsceSubViewImpl.osceSequenceProxy.getLabel());
						//	init2();
						
							}
					});
					
					
					
				}

				@Override
				public void saveOsceDataSplit(SequenceOsceSubViewImpl sequenceOsceSubViewImpl) {
					// TODO Auto-generated method stub
				//	final OsceSequenceProxy newOsce;
					if(sequenceOsceSubViewImpl.osceDayProxy.getTimeEnd().getHours()>13)
					{
						
					requests.osceSequenceRequestNonRoo().splitSequence(sequenceOsceSubViewImpl.osceSequenceProxy.getId()).fire(new OSCEReceiver<OsceSequenceProxy>() {

						@Override
						public void onSuccess(OsceSequenceProxy osceSequenceProxy) {
							// TODO Auto-generated method stub
							Log.info("spliting of sequence done:--"+osceSequenceProxy.getId());
							goTo(new CircuitDetailsPlace(osceProxy.stableId(),Operation.DETAILS));
						}
					});
					
					}
					else
					{
						Log.info("Spliting not allowed");
					}
						
/*						
						// TODO Auto-generated method stub
						Log.info("end time:- "+sequenceOsceSubViewImpl.osceDayProxy.getTimeEnd().getHours());
						if(sequenceOsceSubViewImpl.osceDayProxy.getTimeEnd().getHours()>13)
						{
							Log.info("perform spliting");
							requests.osceSequenceRequest().findOsceSequence(sequenceOsceSubViewImpl.osceSequenceProxy.getId()).with("osceDay","oscePosts","courses").fire(new Receiver<OsceSequenceProxy>() {

								@Override
								public void onSuccess(OsceSequenceProxy response) {
									// TODO Auto-generated method stub
									final OsceSequenceProxy globalResponse=response;
									System.out.println("success fetch--"+response.getCourses().size()+"-"+response.getOscePosts().size());
								
							
							//,"osceSequences.osceDay","osceSequences.oscePosts","osceSequences.courses"
									
							OsceSequenceRequest sequenceRequest=requests.osceSequenceRequest();
							final OsceSequenceProxy sequenceProxy=sequenceRequest.create(OsceSequenceProxy.class);
							
							sequenceProxy.setLabel(response.getLabel());
							sequenceProxy.setNumberRotation(response.getNumberRotation());
					//		
							sequenceProxy.setOsceDay(response.getOsceDay());
							System.out.println("before set course");

							sequenceRequest.persist().using(sequenceProxy).fire(new Receiver<Void>() {

								@Override
								public void onSuccess(Void response) {
									// TODO Auto-generated method stub
									System.out.println("new sequence save successfully");
									
									//requests.osceSequenceRequestNonRoo().findMaxOsceSequence().fire(new Receiver<OsceSequenceProxy>() {
									requests.find(sequenceProxy.stableId()).fire(new Receiver<Object>() {

										@Override
										public void onSuccess(Object response) {
											// TODO Auto-generated method stub
											
											System.out.println("new size--"+globalResponse.getOscePosts().size());
											Iterator<OscePostProxy> postIterator=globalResponse.getOscePosts().iterator();
											while(postIterator.hasNext())
											{
												OscePostProxy post=postIterator.next();
												OscePostRequest postRequest=requests.oscePostRequest();
												
												OscePostProxy addpost=postRequest.create(OscePostProxy.class);
												addpost.setIsPossibleStart(post.getIsPossibleStart());
												addpost.setOscePostBlueprint(post.getOscePostBlueprint());
												addpost.setOscePostRooms(post.getOscePostRooms());
												addpost.setOsceSequence((OsceSequenceProxy)response);
												addpost.setStandardizedRole(post.getStandardizedRole());
												addpost.setSequenceNumber(post.getSequenceNumber());
												
												
												postRequest.persist().using(addpost).fire();
											}
											
											Iterator<CourseProxy> courseIterator=globalResponse.getCourses().iterator();
											while(courseIterator.hasNext())
											{
												CourseProxy course=courseIterator.next();
												CourseRequest courseRequest=requests.courseRequest();
												
												CourseProxy addCourse=courseRequest.create(CourseProxy.class);
												
												addCourse.setColor(course.getColor());
												addCourse.setOsce(course.getOsce());
												addCourse.setOscePostRooms(course.getOscePostRooms());
												addCourse.setOsceSequence((OsceSequenceProxy)response);
												
												
												
												courseRequest.persist().using(addCourse).fire();
											}
											
										}
									});
								}
								
								@Override
								public void onViolation(Set<Violation> errors) {
									Iterator<Violation> iter = errors.iterator();
									String message = "";
									while (iter.hasNext()) {
										message += iter.next().getMessage() + "<br>";
									}
									Log.warn(" in sequence -" + message);
								}
								
								public void onFailure(ServerFailure error) {
									Log.error("error--"+error.getMessage());

								}
							
							});
							
							}
							});
						}
						else
						{
							Log.info("spliting not allowed");
						}
						*/
					}
		
				
				
					
		
				
				//  OSCE Day Assignment END
				
				

				// 5C: SPEC END

}
