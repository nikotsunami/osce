package ch.unibas.medizin.osce.client.a_nonroo.client.activity;



import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.TopicsAndSpecDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.TopicsAndSpecDetailsViewImpl;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicRequest;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.StudyYears;

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
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

@SuppressWarnings("deprecation")
public class TopicsAndSpecDetailsActivity extends AbstractActivity implements
TopicsAndSpecDetailsView.Presenter, 
TopicsAndSpecDetailsView.Delegate
{
    private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private TopicsAndSpecDetailsView view;
	private SpecialisationProxy specialisationProxy;
	

	//tOOLTIP COMPONENTS
	
	private PopupPanel toolTip;
    private HorizontalPanel toolTipContentPanel;
    private com.google.gwt.user.client.ui.TextBox toolTipLabel;
    private ListBox slotBox;
	
	
	//@By spec to handle table change value
	private HandlerRegistration rangeChangeHandler;
	//@By spec table to add data and remove
	private CellTable<RoleTopicProxy> table;
	
	/*@By spec holds the selection model of the specialisation table */
	private SingleSelectionModel<RoleTopicProxy> selectionModel;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private TopicsAndSpecDetailsPlace place;
	
	public TopicsAndSpecDetailsActivity(TopicsAndSpecDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
    	this.requests = requests;
    	this.placeController = placeController;
    	
    }
	
	public void onStop(){

	}
	
	public Sorting sortorder = Sorting.ASC;
	public String sortname = "name";
	public String searchFilter="";
	
	private class InitializeActivityReceiver extends Receiver<Object> {
		@Override
		public void onFailure(ServerFailure error){
			Log.error(error.getMessage());
			Log.error("Error accored in receiving Specialisation proxy");
			
		}
		
		@Override
		public void onSuccess(Object response) {
			Log.info("Cheking whter Specialization proxy arrived");
			if(response instanceof SpecialisationProxy){
				Log.info("Obtained Specialisation proxy successfully");
				Log.info(((SpecialisationProxy) response).getName());
				specialisationProxy = (SpecialisationProxy) response;
				init();
				
			}
		}
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("TopicsAndSpecDetailsActivity.start()");
		final TopicsAndSpecDetailsView topicsAndSpecDetailsView = new TopicsAndSpecDetailsViewImpl();
		topicsAndSpecDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = topicsAndSpecDetailsView;
		widget.setWidget(topicsAndSpecDetailsView.asWidget());
		
		setTable(view.getTable());
		
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				//By SPEC[Start
				Column<RoleTopicProxy,String> col = (Column<RoleTopicProxy,String>) event.getColumn();
				int index = table.getColumnIndex(col); 
				String[] path =	topicsAndSpecDetailsView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;				
				//By SPEC]end
				TopicsAndSpecDetailsActivity.this.onRangeChanged();
			}
		});
		//Tooltip controls
		
		
// Add this if error occored activityManager.setDisplay(view.getDetailsPanel());
		
		// Added by spec to add selection model on the table to handle click handler.
				ProvidesKey<RoleTopicProxy> keyProvider = ((AbstractHasData<RoleTopicProxy>) table).getKeyProvider();
				selectionModel = new SingleSelectionModel<RoleTopicProxy>(keyProvider);
				table.setSelectionModel(selectionModel);
		
				view.setDelegate(this);
				topicsAndSpecDetailsView.setDelegate(this);
		
				requests.find(place.getProxyId()).with("roleTopics").fire(new InitializeActivityReceiver());
		

	}
	
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);		
	}
	
	private void setTable(CellTable<RoleTopicProxy> table) {
		this.table = table;
		
	}

	@Override
	public void newClicked(final String value, final String value2, final StudyYears value3) {
		
		Log.debug("RoleTopic Adding");
		requests.find(place.getProxyId()).fire(new OSCEReceiver<Object>(){
		//spReq.persist().using(specialisationProxy).fire(new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error){
				Log.error("onFilure");
				Log.error(error.getMessage());				
			}
			
			@Override
			public void onSuccess(Object object) {
				System.out.println("Save RoleTopic values value Succesfully1");
				if (object instanceof SpecialisationProxy)
				{
					RoleTopicRequest roletopicReq = requests.roleTopicRequest();
					RoleTopicProxy roletopic = roletopicReq.create(RoleTopicProxy.class);
					roletopic.setName(value);
					roletopic.setSlotsUntilChange(new Integer(value2));
					roletopic.setStudyYear(value3);
					Log.debug("Specialization : " + specialisationProxy.getName());
					Log.debug("Specialization : " + specialisationProxy.getVersion());
					Log.debug("Specialization : " + specialisationProxy.stableId());					
					roletopic.setSpecialisation((SpecialisationProxy)object);
					// Violation Changes Highlight
					
					//roletopicReq.persist().using(roletopic).fire(new Receiver<Void>(){
					
					Log.info("Map Size:" + view.viewPopupMapAdd().size());
					
					roletopicReq.persist().using(roletopic).fire(new OSCEReceiver<Void>(view.viewPopupMapAdd()){
						// E Violation Changes Highlight
						@Override
						public void onFailure(ServerFailure error){
							Log.error("onFilure");
							Log.error(error.getMessage());				
						}
						
						@Override
						public void onSuccess(Void arg0) {
							System.out.println("Save RoleTopic values value Succesfully");
							// Violation Changes Highlight
							Log.info("Save RoleTopic values value Succesfully");
							view.getAddTextBox().removeStyleName("higlight_onViolation");
							view.getslots_till_change().removeStyleName("higlight_onViolation");
							view.getStudyYearListBox().removeStyleName("higlight_onViolation");
							view.getAddPopupPanel().hide();
							// E Violation Changes Highlight				
							init();
						
						}
					});					
				}							
			}
		});
		
		
		Log.debug("After persist");
	}
	//@By spec  To add data in table
	private void init(){
		init2();
	}
		private void init2() {

			 
			//view.setValue(specialisationProxy);
			Log.info("Inside INIT2()");
			
			Log.info("Role Topic id : " + specialisationProxy.getId());
			Log.info("Role Topic name : " + specialisationProxy.getName());
			Log.info("Role Topic Count : " + specialisationProxy.getRoleTopics().size());
			//view.getTable().setRowCount(specialisationProxy.getRoleTopics().size(), true);
			//onRangeChanged(specialisationProxy);
			
			fetchRoleTopic();
			
					
		}
		
		
		private void fetchRoleTopic()
		{
			
			Log.debug("Start fetchRoleTopic: " );
			requests.roleTopicRequestNonRoo().countRoleTopicBySpecialisationId(searchFilter,specialisationProxy.getId()).fire(new Receiver<Long>() {
				@Override
				public void onSuccess(Long response) {
					if (view == null) {
						// This activity is dead
						return;
					}
					Log.debug("Geholte Nationalitäten aus der Datenbank: " + response);
					System.out.println("Arrived result of TotalRole Topic set table size according to it");
					view.getTable().setRowCount(response.intValue(), true);

					onRangeChanged();
				}

			});
		}
		
		private void onRangeChanged() 
		{
			System.out.println("In side OnRangeChanged()");
			final Range range = table.getVisibleRange();
			requests.roleTopicRequestNonRoo().findRoleTopicBySpecialisationId(sortname,sortorder,searchFilter,specialisationProxy.getId(), range.getStart(), range.getLength()).with("standardizedRoles").fire(new Receiver<List<RoleTopicProxy>>() {				
				@Override
				public void onSuccess(List<RoleTopicProxy> response) {
					if(view == null){
					return;
				}
					System.out.println("Successfully  RoleTopic values set in table");
					table.setRowData(range.getStart(), response);

					// finishPendingSelection();
					if (widget != null) {
						widget.setWidget(view.asWidget());
					}
				}

				});
		}
		

		@Override
		public void deleteClicked(RoleTopicProxy roletopic) {
			Log.info("RoleTopic delete clicked");
			if(roletopic.getStandardizedRoles()!= null && roletopic.getStandardizedRoles().size() > 0){
				Window.alert("Roletopic can not be deleted if standardized role is asigne");
			}
			else{
			requests.roleTopicRequest().remove().using(roletopic).fire(new Receiver<Void>() {
				public void onSuccess(Void ignore) {
					Log.debug("Sucessfully deleted");
					init();
				}
			});
		
			}
		}

		@Override
		public void editClicked(final RoleTopicProxy roletopic,int left,int top) {
			
			Log.info("Opened role topic ToolTip");

			toolTip=new PopupPanel(true);
			
			toolTip.setWidth("300px");
	        toolTip.setHeight("40px");
	        
			toolTipContentPanel=new HorizontalPanel();
			toolTipContentPanel.setWidth("280px");
			toolTipContentPanel.setHeight("22px");
		    
			toolTipContentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			toolTipContentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
			toolTipLabel=new TextBox();
			 toolTipLabel.setWidth("120px");
			 toolTipLabel.setHeight("22px");
			 
			 slotBox =new ListBox();
			 slotBox.setWidth("45px");
			 slotBox.setHeight("22px");
			 
		    for(int i=1;i<=12;i++)
		       {
		    	   slotBox.addItem(i+"");
		       }
			
			
			
	      final ValueListBox<StudyYears> StudyYearListBox = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
	       
	       //ViListBox<StudyYears> study_year = new ListBox<StudyYears>(new EnumRenderer<StudyYears>());
	      
	       StudyYearListBox.setValue(roletopic.getStudyYear());
		   StudyYearListBox.setAcceptableValues(Arrays.asList(StudyYears.values()));
	       StudyYearListBox.setWidth("50px");
		   StudyYearListBox.setHeight("22px");
		   
		   Button toolTipChange = new Button("Save");
	       toolTipChange.setWidth("55px");
	       toolTipChange.setHeight("22px");
	         // newButton.setWidth();
	       
	       slotBox.setSelectedIndex(roletopic.getSlotsUntilChange()-1);
	        toolTipContentPanel.add(toolTipLabel);
	        toolTipContentPanel.add(slotBox);
	        toolTipContentPanel.add(StudyYearListBox);
	        toolTipContentPanel.add(toolTipChange);
	     
	        toolTipLabel.setText(roletopic.getName());
	       
	        
	     //   popup.setHeight("20px");
	        
	        toolTip.add(toolTipContentPanel);   // you can add any widget here
	        // Issue Role
	        //toolTip.setPopupPosition(new Integer(constants.TopicsAndSpecDetailsViewPopupXPosition()),new Integer(constants.TopicsAndSpecDetailsViewPopupYPosition())); 
	        //toolTip.setPopupPosition(left,top);
	     // Violation Changes Highlight
	        toolTip.setPopupPosition(left-180,top+10);
		     // E Violation Changes Highlight
	        // E: Issue Role
	        toolTip.show();
	        
	        toolTipChange.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// Violation Changes Highlight
					final Map<String, Widget> viewPopupMap=new HashMap<String, Widget>();
					viewPopupMap.put("name",toolTipLabel);
					viewPopupMap.put("slotsUntilChange",slotBox);
					viewPopupMap.put("studyYear",StudyYearListBox);
					
					requests.roleTopicRequest().findRoleTopic(roletopic.getId()).fire(new OSCEReceiver<RoleTopicProxy>()
					{

								@Override
								public void onSuccess(RoleTopicProxy response) {
									RoleTopicRequest roleReq = requests.roleTopicRequest();										
									response = roleReq.edit(response);
									response.setName(toolTipLabel.getText());
									response.setSlotsUntilChange(new Integer((slotBox.getSelectedIndex()+1)));
									response.setStudyYear(StudyYearListBox.getValue());
									roleReq.persist().using(roletopic).fire(new OSCEReceiver<Void>(viewPopupMap){
										
										@Override
										public void onFailure(ServerFailure error){
											Log.error("onFilure");
											Log.error(error.getMessage());				
										}
										
										@Override
										public void onSuccess(Void arg0) {
											Log.info("Save RoleTopic values value Succesfully");
											toolTip.clear();
											toolTip.hide();				
											init();
										
										}
									});					
								}							
							});
					// E Violation Changes Highlight
					
					
									
									
					}
						
					});
					
				}

		@Override
		public void performSearch(String value) {
			searchFilter=value;
			Log.info("RoleTopic Searching");
			init2();
			
		}

}
