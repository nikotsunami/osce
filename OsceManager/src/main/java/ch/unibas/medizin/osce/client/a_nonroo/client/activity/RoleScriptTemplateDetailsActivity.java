package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplateDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableAccessView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableAccessViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableItemView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableItemViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

@SuppressWarnings("deprecation")
public class RoleScriptTemplateDetailsActivity extends AbstractActivity
		implements RoleScriptTemplateDetailsView.Presenter,
		RoleScriptTemplateDetailsView.Delegate,RoleBaseTableItemView.Delegate,RoleBaseTableAccessView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleScriptTemplateDetailsView view;
	private RoleTemplateProxy roleTemplate;

	
	private RoleScriptTemplateDetailsActivity roleScriptTemplateDetailsActivity;
	

	private PopupPanel toolTip;
	private HorizontalPanel toolTipContentPanel;
	private TextBox toolTipLabel;
	private IconButton toolTipChange;
		
	private int sizeOfTable;
	private final OsceConstants constants = GWT.create(OsceConstants.class);	
	private RoleScriptTemplateDetailsPlace place;

	private CellTable<RoleBaseItemProxy> baseItemTable;
	private SingleSelectionModel<RoleBaseItemProxy> baseItemselectionModel;
	
	private CellTable<RoleTableItemProxy> table;
	private SingleSelectionModel<RoleTableItemProxy> selectionModel;
	
	
	public RoleScriptTemplateDetailsActivity(
		RoleScriptTemplateDetailsPlace place, OsMaRequestFactory requests,
		PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;
	}

	public void onStop() {
	
	}

	public RoleBaseTableItemView roleBaseTableItemViewImpl = new RoleBaseTableItemViewImpl();
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleScriptTemplateDetailsActivity.start()");
		RoleScriptTemplateDetailsView roleScriptTemplateDetailsView = new RoleScriptTemplateDetailsViewImpl();
		roleScriptTemplateDetailsView.setPresenter(this);
		roleScriptTemplateDetailsActivity=this;
		this.widget = panel;
		this.view = roleScriptTemplateDetailsView;

		widget.setWidget(roleScriptTemplateDetailsView.asWidget());
		
		setTable(view.getTable());

				
		view.setDelegate(this);
		//view.getRoleBaseTableItemViewImpl().setDelegate(this);
		
		//roleScriptTemplateDetailsView.setDelegate(this);
		
		//	view.getRoleBaseTableItemViewImpl().getRoleBaseTableAccessViewImpl().setDelegate(this);
		
		requests.find(place.getProxyId()).with("RoleTemplate")
				.fire(new InitializeActivityReceiver());
	
		ProvidesKey<RoleBaseItemProxy> keyProvider = ((AbstractHasData<RoleBaseItemProxy>) baseItemTable).getKeyProvider();
		baseItemselectionModel = new SingleSelectionModel<RoleBaseItemProxy>(keyProvider);
		baseItemTable.setSelectionModel(baseItemselectionModel);
		
		
					init();
					init2();
	}
		
	public void init2()
	{
		requests.find(place.getProxyId()).with("RoleTemplate").fire(new Receiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				if (response instanceof RoleTemplateProxy) {
					requests.roleBaseItemRequest().findAllDeletedRoleBaseItems(((RoleTemplateProxy) response).getId()).fire(new Receiver<List<RoleBaseItemProxy>>() {

						@Override
						public void onSuccess(List<RoleBaseItemProxy> response) {
							Range range = baseItemTable.getVisibleRange();
							baseItemTable.setRowCount(response.size());
							baseItemTable.setRowData(range.getStart(), response);
							
						}
					});
				}
				}
		});
	}	
		
		
	private void init() {		
		requests.find(place.getProxyId()).with("roleBaseItem").with("roleBaseItem.roleTableItem","roleBaseItem.roleItemAccess").fire(new Receiver<Object>() {
					@Override
			public void onSuccess(Object response) {
			
				if(response instanceof RoleTemplateProxy){
					Log.info("Retriving RoleItemProxy");
					List<RoleBaseItemProxy> setRoleBaseItemProxy = ((RoleTemplateProxy) response).getRoleBaseItem();
					Iterator<RoleBaseItemProxy> iteratorRoleBaseItemProxy = setRoleBaseItemProxy.iterator();
					while(iteratorRoleBaseItemProxy.hasNext())
					{
						Log.info("Retriving priviouswidgets");
						//view.addRoleBaseWidget(iteratorRoleBaseItemProxy.next());
						
						
						RoleBaseTableItemViewImpl roleBaseTableItemViewImpl=new RoleBaseTableItemViewImpl();
						//roleBaseTableItemViewImpl.baseItemHeaderLable.setText(rolebaseItem.getItem_name());
						//	System.out.println("rolebaseItem.id : " + rolebaseItem.getId());
						roleBaseTableItemViewImpl.setDelegate(roleScriptTemplateDetailsActivity);
						
						RoleBaseItemProxy roleBaseItemProxy = iteratorRoleBaseItemProxy.next();
						if(roleBaseItemProxy.getItem_defination().name().equals("table_item"))
						{
							if(roleBaseItemProxy.getDeleted())
								continue;
							Log.info("Total Table cound : " + roleBaseItemProxy.getRoleTableItem().size() );
							roleBaseTableItemViewImpl.setValue(roleBaseItemProxy);						
							view.getTableItem().add(roleBaseTableItemViewImpl);
						
							Range range = roleBaseTableItemViewImpl.getTable().getVisibleRange();
							roleBaseTableItemViewImpl.getTable().setRowCount(roleBaseItemProxy.getRoleTableItem().size());
						
							/*RoleTableItemProxy[] arrRoleTableItemProxy = new  RoleTableItemProxy[roleBaseItemProxy.getRoleTableItem().size()];								
							roleBaseItemProxy.getRoleTableItem().toArray(arrRoleTableItemProxy);
							List<RoleTableItemProxy> listRoleTableItemProxy = Arrays.asList(arrRoleTableItemProxy);	*/					
							roleBaseTableItemViewImpl.getTable().setRowData(range.getStart(),roleBaseItemProxy.getRoleTableItem());
						}
						else
						{
							if(roleBaseItemProxy.getDeleted())
								continue;
							roleBaseTableItemViewImpl.setValue(roleBaseItemProxy);
							roleBaseTableItemViewImpl.roleBaseItemDisclosurePanel.setStyleName("border=0");
							roleBaseTableItemViewImpl.table.removeFromParent();
							roleBaseTableItemViewImpl.AddSubItem.removeFromParent();
							roleBaseTableItemViewImpl.txtSubItem.removeFromParent();
							view.getTableItem().add(roleBaseTableItemViewImpl);
						}
						
												
						Set<RoleItemAccessProxy> setRoleItemAccessProxy = roleBaseItemProxy.getRoleItemAccess();
						Iterator<RoleItemAccessProxy> roleAccess = setRoleItemAccessProxy.iterator();
						Log.info("RoleItemAccess set size : " + setRoleItemAccessProxy.size());
						while(roleAccess.hasNext())
						{
							RoleItemAccessProxy roleItemAccessProxy = roleAccess.next();
							RoleBaseTableAccessViewImpl roleBaseTableAccessViewImpl = new RoleBaseTableAccessViewImpl();
							roleBaseTableAccessViewImpl.setDelegate(roleScriptTemplateDetailsActivity);	
							roleBaseTableAccessViewImpl.accessDataLabel.setText(roleItemAccessProxy.getName());
							roleBaseTableItemViewImpl.getAccessDataPanel().add(roleBaseTableAccessViewImpl);
							roleBaseTableAccessViewImpl.setRoleBaseItemProxy(roleBaseItemProxy);
							roleBaseTableAccessViewImpl.setRoleItemAccessProxy(roleItemAccessProxy);
						}
						//roleBaseTableItemViewImpl.getRoleBaseTableAccessViewImpl().accessDataLabel
					}
				}
			}
		});	
	}

	/**
	 * Used as a callback for the request that gets the @StandardizedPatientProxy
	 * that is edited in this activities instance.
	 */

	private class InitializeActivityReceiver extends Receiver<Object> {
		@Override
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
			Log.info("Error in InitializeActivityReceiver");
		}

		@Override
		public void onSuccess(Object response) {
			Log.info("sucess in InitializeActivityReceiver");
			if (response instanceof RoleTemplateProxy) {
				
				Log.info(((RoleTemplateProxy) response).getTemplateName());
				roleTemplate = (RoleTemplateProxy) response;
				// init();
			}
		}
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}
	private void setTable(CellTable<RoleBaseItemProxy> basetable) {
		this.baseItemTable = basetable;
		
	}

	@Override
	public void newClicked(String name, final ch.unibas.medizin.osce.shared.ItemDefination item_defination) {
		Log.debug("Add Role Template Base Item");
		Log.info("item Name :" + name);
		Log.info("item defination  :" + item_defination);

		RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();
		final RoleBaseItemProxy roleBaseitem = roleBaseItemReq
				.create(RoleBaseItemProxy.class);

		roleBaseitem.setItem_name(name);
		roleBaseitem.setItem_defination(item_defination);
		roleBaseitem.setDeleted(false);
		roleBaseitem.setRoleTemplate(roleTemplate);
		roleBaseitem.setSort_order(view.getTableItem().getWidgetCount()+1);
		
		// Violation Changes Highlight
				Log.info("Map Size: " + view.getViewMap().size());
		// E Violation Changes Highlight
		
		roleBaseItemReq.persist().using(roleBaseitem)
				.fire(new Receiver<Void>() {
					
					@Override
					public void onSuccess(Void arg0) {
						Log.info("sucessfully RoleBase Item saved");
						// Violation Changes Highlight					
						requests.find(roleBaseitem.stableId()).fire(new OSCEReceiver<Object>(view.getViewMap()) {
						// E Violation Changes Highlight
							@Override
							public void onSuccess(Object response) {
								//System.out.println(" response id : " + ((RoleBaseItemProxy)response).getId());
								if(item_defination.name().equals("table_item"))
								{
									RoleBaseTableItemViewImpl roleBaseTableItemViewImpl=new RoleBaseTableItemViewImpl();
								//roleBaseTableItemViewImpl.baseItemHeaderLable.setText(rolebaseItem.getItem_name());																
									roleBaseTableItemViewImpl.setDelegate(roleScriptTemplateDetailsActivity);
									roleBaseTableItemViewImpl.setValue((RoleBaseItemProxy)response);
									Range range = roleBaseTableItemViewImpl.getTable().getVisibleRange();
									List<RoleTableItemProxy> proxyList = new ArrayList<RoleTableItemProxy>();
									roleBaseTableItemViewImpl.getTable().setRowCount(proxyList.size());
									roleBaseTableItemViewImpl.getTable().setRowData(range.getStart(), proxyList);
									view.getTableItem().add(roleBaseTableItemViewImpl);
								}
								else
								{
									RoleBaseTableItemViewImpl roleText_AreaTableItemViewImpl = new RoleBaseTableItemViewImpl();
									roleText_AreaTableItemViewImpl.setDelegate(roleScriptTemplateDetailsActivity);
									roleText_AreaTableItemViewImpl.setValue((RoleBaseItemProxy)response);
									view.getTableItem().add(roleText_AreaTableItemViewImpl);
									
									roleText_AreaTableItemViewImpl.roleBaseItemDisclosurePanel.setStyleName("border=0");
									roleText_AreaTableItemViewImpl.table.removeFromParent();
									roleText_AreaTableItemViewImpl.AddSubItem.removeFromParent();
									roleText_AreaTableItemViewImpl.txtSubItem.removeFromParent();
									
								}
							}
							
						});
						
						
					}
				});
		
	}

	
	
	@Override
	public void addRoleBaseSubItem(final RoleBaseItemProxy roleBaseItemProxy,final CellTable<RoleTableItemProxy> table,final RoleBaseTableItemViewImpl roleBaseTableItemViewImpl) {
		this.table=table;
		ProvidesKey<RoleTableItemProxy> keyProvider = ((AbstractHasData<RoleTableItemProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<RoleTableItemProxy>(keyProvider);
		table.setSelectionModel(selectionModel);
		Log.info("Inside of addRoleBaseSubItem() to add and retrive sub item data");
		//final int totalRow = table.getRowCount();
		
		// Violation Changes Highlight
		requests.find(roleBaseItemProxy.stableId()).with("roleTableItem").fire(new OSCEReceiver<Object>(){
		// E Violation Changes Highlight
				@Override
				public void onFailure(ServerFailure error){
					Log.error("onFilure");
					Log.error(error.getMessage());				
				}
				@Override
				public void onSuccess(Object object) {
						if (object instanceof RoleBaseItemProxy){
							
						Log.info("Retriving RoleBase Proxy");
					
						
						RoleTableItemRequest roleTableItemReq = requests.roleTableItemRequest();
						final RoleTableItemProxy roleTableItemProxy = roleTableItemReq.create(RoleTableItemProxy.class);
						Log.info("RoleBaseItem set list count:" +  ((RoleBaseItemProxy)object).getRoleTableItem().size());
						sizeOfTable=(((RoleBaseItemProxy)object).getRoleTableItem().size());
						sizeOfTable+=1;
						// Issue Role Module
						//roleTableItemProxy.setItemName("Title "+sizeOfTable);
						roleTableItemProxy.setItemName(roleBaseTableItemViewImpl.txtSubItem.getText());
						// Issue Role Module
						roleTableItemProxy.setSort_order(sizeOfTable);
						roleTableItemProxy.setRoleBaseItem((RoleBaseItemProxy)object);
						final Long roleBaseItemId = ((RoleBaseItemProxy)object).getId(); 
						
						// Violation Changes Highlight
						Log.info("Map Size Is: " + view.getRoleBaseTableItemViewImpl().getViewMap().size());											
						roleTableItemReq.persist().using(roleTableItemProxy).fire(new OSCEReceiver<Void>(view.getRoleBaseTableItemViewImpl().getViewMap()){
						// E Violation Changes Highlight
							@Override
							public void onFailure(ServerFailure error){
								Log.error("onFilure");
								Log.error(error.getMessage());				
							}
							
							@Override
							public void onSuccess(Void arg0) {
								Log.info("Save RoleTopic values value Succesfully");	
								// Issue Role
								roleBaseTableItemViewImpl.txtSubItem.setText("");
								table.setRowCount(sizeOfTable);
								onRangeChanged(roleBaseItemId);
								
								}
						});					
					}
						else{
								Log.info("Skiped proxy if condition");
						}
				}
			});
		
		
		
		
	}
	
	public void showRoleBaseSubItem(RoleBaseItemProxy roleBaseItemProxy,final CellTable<RoleTableItemProxy> table) {
		this.table=table;
		ProvidesKey<RoleTableItemProxy> keyProvider = ((AbstractHasData<RoleTableItemProxy>) table).getKeyProvider();
		selectionModel = new SingleSelectionModel<RoleTableItemProxy>(keyProvider);
		table.setSelectionModel(selectionModel);
		Log.info("SubItem Proxy Id : " + roleBaseItemProxy.getId());
		onRangeChanged(roleBaseItemProxy.getId());
				
	}
	
	protected void onRangeChanged( Long roleBaseItemId) {
		final Range range = table.getVisibleRange();

		Log.info("Map Size: " + view.getRoleBaseTableItemViewImpl().getViewMap().size());
		// Violation Changes Highlight
		final Receiver<List<RoleTableItemProxy>> callback = new OSCEReceiver<List<RoleTableItemProxy>>(view.getRoleBaseTableItemViewImpl().getViewMap()) {
		// E Violation Changes Highlight
			@Override
			public void onSuccess(List<RoleTableItemProxy> response) {
				if (view == null) {
					return;
				}
				Log.info("Successfully RoleTemplate result value set in table");
				table.setRowCount(response.size());
				table.setRowData(range.getStart(), response);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}

		};

		fireRangeRequest(range, callback,roleBaseItemId);
	}
	private void fireRangeRequest(final Range range,
			final Receiver<List<RoleTableItemProxy>> callback,Long roleBaseItemId) {
		Log.info("Inside fireRangeRequest()");
		 requests.roleTableItemRequest().findRoleTableItemByBaseItemId(roleBaseItemId).fire(callback);
		
	}

	@Override
	public void pencliButtonclickEvent(final RoleBaseItemProxy roleBaseItemProxy,ClickEvent event) {

		
		Log.info("ToolTip opened");
		if(roleBaseItemProxy==null){
			Log.info("RoleBaseProxy is null when pencil clickd");
			return;
		}
		
				Log.info("RoleBase Proxy Item name :" + roleBaseItemProxy.getItem_name());
				toolTip= new PopupPanel(true);
				
				toolTip.setWidth("210px");
				toolTip.setHeight("40px");
			    toolTip.setAnimationEnabled(true);
			    
				toolTipContentPanel=new HorizontalPanel();
				
				toolTipContentPanel.setWidth("200px");
				toolTipContentPanel.setHeight("22px");
				toolTipContentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				toolTipContentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
				toolTipLabel=new TextBox();
				
				toolTipLabel.setWidth("120px");
				toolTipLabel.setHeight("25px");
				
				toolTipChange = new IconButton(constants.save());
				toolTipChange.setIcon("disk");
			 
				toolTipChange.setWidth("70px");
				toolTipChange.setHeight("25px");       
				
				
			
				toolTipContentPanel.add(toolTipLabel);
				toolTipContentPanel.add(toolTipChange);
			     
				toolTipLabel.setText(roleBaseItemProxy.getItem_name());
			       	   
				toolTip.add(toolTipContentPanel);   // you can add any widget here
			        

			   
				// Issue Role Module
				//toolTip.setPopupPosition(new Integer(constants.TopicsAndSpecViewPopupXPosition()),new Integer(constants.TopicsAndSpecViewPopupYPosition()));
				toolTip.setPopupPosition(event.getClientX(),event.getClientY()-45);
			    
			        toolTip.show();
			        
			     // Violation Changes Highlight
			        final Map<String, Widget> editPopupViewMap=new HashMap<String, Widget>();
			        editPopupViewMap.put("item_name", toolTipLabel);			        
				     // E Violation Changes Highlight			    
			        
			       /* toolTipChange.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							requests.roleBaseItemRequest().findRoleBaseItem(roleBaseItemProxy.getId()).fire(new Receiver<RoleBaseItemProxy>() {
							
							
										@Override
										public void onSuccess(RoleBaseItemProxy response) {
											RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();										
											response = roleBaseItemReq.edit(response);
											response.setItem_name(toolTipLabel.getText());
											roleBaseItemReq.persist().using(roleBaseItemProxy).fire(new Receiver<Void>(){
												
												@Override
												public void onFailure(ServerFailure error){
													Log.error("onFilure");
													Log.error(error.getMessage());				
												}
												
												@Override
												public void onSuccess(Void arg0) {
													Log.info("Save RoleBaseItem value Succesfully according to ToolTip value");
													toolTip.clear();
													toolTip.hide();		
													
													view.getTableItem().clear();
													init();
													init2();
																		
												}
											});					
										}							
									}
								);
											
											
							}
								
					});*/
			        
			        toolTipChange.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) 
						{
							
							 // Violation Changes Highlight
							
							/*// Issue Role V2
							
							if(toolTipLabel.getText().trim().equals(""))
							{
								Log.info("Null Value....");
								
								 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
								 dialogBox.showConfirmationDialog("Please Enter appropriate value for Role Template");
								 
								 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) {
										dialogBox.hide();							
										Log.info("ok click");	
										return;
											}
										});
							}*/
							//else
							//{
							 // E: Violation Changes Highlight
								Log.info("Value....");
								 // Violation Changes Highlight
								requests.roleBaseItemRequest().findRoleBaseItem(roleBaseItemProxy.getId()).fire(new OSCEReceiver<RoleBaseItemProxy>() 
								 // E: Violation Changes Highlight
										{
								
								
											@Override
											public void onSuccess(RoleBaseItemProxy response) {
												RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();										
												response = roleBaseItemReq.edit(response);
												response.setItem_name(toolTipLabel.getText());
												Log.info("Map Size: " + editPopupViewMap.size());												
												roleBaseItemReq.persist().using(roleBaseItemProxy).fire(new OSCEReceiver<Void>(editPopupViewMap){
													
													@Override
													public void onFailure(ServerFailure error){
														Log.error("onFilure");
														Log.error(error.getMessage());				
													}
													
													@Override
													public void onSuccess(Void arg0) {
														Log.info("Save RoleBaseItem value Succesfully according to ToolTip value");
														toolTip.clear();
														toolTip.hide();		
														
														view.getTableItem().clear();
														init();
														init2();
																			
													}
												});					
											}							
										});
								//}
							}
								
					});
								
	}

	@Override
public void roleTableItemEditButtonClicked(final RoleTableItemProxy roleTableItem,final Long roleBaseItemId, final CellTable<RoleTableItemProxy> roleTableItemtable, int left,int top) {
		
		Range range=roleTableItemtable.getVisibleRange();
		
		Log.info("Start: " + range.getStart() + range.getLength());
		
		Log.info("RoleBase Proxy Item name to add title of roletable:" + roleTableItem.getItemName());
		Log.info("RoleBase Proxy Item id to add title of roletable:" + roleTableItem.getRoleBaseItem());
		toolTip= new PopupPanel(true);
		
		toolTip.setWidth("210px");
		toolTip.setHeight("40px");
	    toolTip.setAnimationEnabled(true);
	    
		toolTipContentPanel=new HorizontalPanel();
		
		toolTipContentPanel.setWidth("200px");
		toolTipContentPanel.setHeight("22px");
		toolTipContentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		toolTipContentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	
		toolTipLabel=new TextBox();
		
		toolTipLabel.setWidth("120px");
		toolTipLabel.setHeight("25px");
		
		toolTipChange = new IconButton(constants.save());
		toolTipChange.setIcon("disk");
	 
		toolTipChange.setWidth("70px");
		toolTipChange.setHeight("25px");       
		
		
	
		toolTipContentPanel.add(toolTipLabel);
		toolTipContentPanel.add(toolTipChange);
	     
		toolTipLabel.setText(roleTableItem.getItemName());
	       
		    
		toolTip.add(toolTipContentPanel);   // you can add any widget here
	        
//		toolTip.setPopupPosition(new Integer(constants.TopicsAndSpecViewPopupXPosition()),new Integer(constants.TopicsAndSpecViewPopupYPosition()));	   
		//toolTip.setPopupPosition(new Integer(constants.TopicsAndSpecViewPopupXPosition()),new Integer(constants.TopicsAndSpecViewPopupYPosition()));
	toolTip.setPopupPosition(left, top);
	    
	        toolTip.show();
	        
	        toolTipChange.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
				//	requests.roleBaseItemRequest().findRoleBaseItem(roleBaseItemProxy.getId()).fire(new Receiver<RoleBaseItemProxy>() {
					requests.roleTableItemRequest().findRoleTableItem(roleTableItem.getId()).fire(new Receiver<RoleTableItemProxy>() {
					
								@Override
								public void onSuccess(RoleTableItemProxy response) {
									RoleTableItemRequest roleTableItemReq = requests.roleTableItemRequest();										
									response = roleTableItemReq.edit(response);
									response.setItemName(toolTipLabel.getText());

									// Violation Changes Highlight
									Log.info("Map Size: " + view.getRoleBaseTableItemViewImpl().getViewMap());									
									roleTableItemReq.persist().using(roleTableItem).fire(new OSCEReceiver<Void>(view.getRoleBaseTableItemViewImpl().getViewMap()){
									// E Violation Changes Highlight
										
									@Override
									public void onFailure(ServerFailure error){
										Log.error("onFilure");
										Log.error(error.getMessage());				
									}
										
									@Override
									public void onSuccess(Void arg0) {
									Log.info("Save RoleTableItem value Succesfully according to ToolTip value");
										
											toolTip.clear();
											toolTip.hide();
											
										requests.roleTableItemRequest().findRoleTableItemByBaseItemId(roleBaseItemId).fire(new Receiver<List<RoleTableItemProxy>>() {

												@Override
												public void onSuccess(List<RoleTableItemProxy> response) {
													roleTableItemtable.setRowCount(response.size());
													final Range range = roleTableItemtable.getVisibleRange();
													roleTableItemtable.setRowData(range.getStart(),response);
													}
												});	
													
											}
										});
									}
								});		
								}			

		
								});
					}

	@Override
	public void roleTableItemDeleteClicked(RoleTableItemProxy roleTableItem,final Long roleBaseItemId,final CellTable<RoleTableItemProxy> roleTableItemTable) {
		requests.roleTableItemRequest().remove().using(roleTableItem).fire(new Receiver<Void>() {
			public void onSuccess(Void ignore) {
				Log.debug("role Table Item Sucessfully deleted");
				
		requests.roleTableItemRequest().findRoleTableItemByBaseItemId(roleBaseItemId).fire(new Receiver<List<RoleTableItemProxy>>() {

			@Override
			public void onSuccess(List<RoleTableItemProxy> response) {
				roleTableItemTable.setRowCount(response.size());
				final Range range = roleTableItemTable.getVisibleRange();
				roleTableItemTable.setRowData(range.getStart(),response);
			}
		});	
				
			}
		});

	}

	@Override
	public void deleteButtonClickEvent(RoleBaseItemProxy rolebaseItem) {
		Log.info("RoleTopic delete clicked");
		
		
			RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();										
			rolebaseItem = roleBaseItemReq.edit(rolebaseItem);
			rolebaseItem.setDeleted(true);			
			roleBaseItemReq.persist().using(rolebaseItem).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("BaseItem Deleted Successfully");
					view.getTableItem().clear();
					init();
					init2();
				}		
				
			});
			
		}

	@Override
	public void restoreButtonClicked(RoleBaseItemProxy roleBaseItem) {
		
		RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();										
		roleBaseItem = roleBaseItemReq.edit(roleBaseItem);
		roleBaseItem.setDeleted(false);
		Log.info("Map Size: " + view.getViewMap().size());
		// Violation Changes Highlight		
		roleBaseItemReq.persist().using(roleBaseItem).fire(new OSCEReceiver<Void>(view.getViewMap()) 
		{
		// E Violation Changes Highlight
			@Override
			public void onSuccess(Void response) {
				Log.info("BaseItem Deleted Successfully");
				view.getTableItem().clear();
				init();
				init2();
			}		
			
		});
		
		
	}

	@Override
	public void roleTableItemMoveUp(RoleTableItemProxy roleTableItem,final Long roleBaseItemId,final CellTable<RoleTableItemProxy> roleTableItemTable) {
		
		
		Log.info("MoveUP inside Activity");
		requests.roleTableItemRequest().roleTableItemMoveUp(roleBaseItemId).using(roleTableItem)
		.fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.info("moved");
			
		requests.roleTableItemRequest().findRoleTableItemByBaseItemId(roleBaseItemId).fire(new Receiver<List<RoleTableItemProxy>>() {

					@Override
					public void onSuccess(List<RoleTableItemProxy> response) {
						roleTableItemTable.setRowCount(response.size());
						final Range range = roleTableItemTable.getVisibleRange();
						roleTableItemTable.setRowData(range.getStart(),response);
					}
				});	
				
			}
		});
		
	}

	@Override
	public void roleTableItemMoveDown(RoleTableItemProxy roleTableItem,final Long roleBaseItemId,final CellTable<RoleTableItemProxy> roleTableItemTable) {
		
		Log.info("MoveDown inside Activity");
		requests.roleTableItemRequest().roleTableItemMoveDown(roleBaseItemId).using(roleTableItem)
		.fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.info("Downed");
				
				requests.roleTableItemRequest().findRoleTableItemByBaseItemId(roleBaseItemId).fire(new Receiver<List<RoleTableItemProxy>>() {

					@Override
					public void onSuccess(List<RoleTableItemProxy> response) {
						roleTableItemTable.setRowCount(response.size());
						final Range range = roleTableItemTable.getVisibleRange();
						roleTableItemTable.setRowData(range.getStart(),response);
					}
				});	
				
			}
		});
		
	}

	@Override
	public void baseItemUpButtonClicked(RoleBaseItemProxy roleBasedItemProxy) {
		Log.info("MoveUP inside Activity");
		requests.roleBaseItemRequest().baseItemUpButtonClicked().using(roleBasedItemProxy)
		.fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.info("moved");
				view.getTableItem().clear();
				init();
				init2();
			}
		});
		
		
	}

	@Override
	public void baseItemDownButtonClicked(RoleBaseItemProxy roleBasedItemProxy) {
		Log.info("MoveDown inside Activity");
		requests.roleBaseItemRequest().baseItemDownButtonClicked().using(roleBasedItemProxy)
		.fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				Log.info("moved");
				view.getTableItem().clear();
				init();
				init2();
			}
		});
		
	}

	@Override
	public void baseItemAccessButtonClicked(final ClickEvent event,
			final RoleBaseItemProxy roleBasedItemProxy,final HorizontalPanel accessDataPanel) {
				
		Log.info("roleBasedItemProxy Is :" + roleBasedItemProxy.getId());
		
		
		//Log.info("roleBasedItemProxy Is :" + response.getId());
		
		//final RoleBaseItemProxy roleBasedItemProxynew =response;
		
		IconButton accessButton =(IconButton) event.getSource();
		
		final int xPosition = accessButton.getAbsoluteLeft();
		final int yPosition = accessButton.getAbsoluteTop();
		
		toolTip= new PopupPanel(true);
		
		toolTip.setWidth("180px");
		toolTip.setHeight("40px");
	    toolTip.setAnimationEnabled(true);
	    
		toolTipContentPanel=new HorizontalPanel();
		
		toolTipContentPanel.setWidth("160px");
		toolTipContentPanel.setHeight("22px");
		toolTipContentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		toolTipContentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	
		//Issue # 122 : Replace pull down with autocomplete.
		final DefaultSuggestBox<RoleItemAccessProxy, EventHandlingValueHolderItem<RoleItemAccessProxy>> accessList=new DefaultSuggestBox<RoleItemAccessProxy, EventHandlingValueHolderItem<RoleItemAccessProxy>>();
		
		
		
		/*final ValueListBox<RoleItemAccessProxy> accessList = new ValueListBox<RoleItemAccessProxy>(RoleItemAccessProxyRenderer.instance());
		
		accessList.setWidth("150px");
		accessList.setHeight("22px");
		*/
		//Issue # 122 : Replace pull down with autocomplete.
		
		requests.roleBaseItemRequest().findRoleBaseItem(roleBasedItemProxy.getId()).with("roleItemAccess").fire(new OSCEReceiver<RoleBaseItemProxy>() {

			@Override
			public void onSuccess(RoleBaseItemProxy response) {
		
					
			final RoleBaseItemProxy roleBasedItemProxynew=response;		
		
		requests.roleItemAccessRequest().findAllRoleItemAccesses().fire(new Receiver <List<RoleItemAccessProxy>>() {

			@Override
			public void onSuccess(List<RoleItemAccessProxy> response) {
					
				//Issue # 122 : Replace pull down with autocomplete.
				//accessList.setAcceptableValues(response);
				
				//Issue # 122 : Replace pull down with autocomplete.
					
					//Issue # 122 : Replace pull down with autocomplete.
					DefaultSuggestOracle<RoleItemAccessProxy> suggestOracle1 = (DefaultSuggestOracle<RoleItemAccessProxy>) accessList.getSuggestOracle();
					suggestOracle1.setPossiblilities(response);
					accessList.setSuggestOracle(suggestOracle1);
					//accessList1.setRenderer(new RoleItemAccessProxyRenderer());
					accessList.setRenderer(new AbstractRenderer<RoleItemAccessProxy>() {

						@Override
						public String render(RoleItemAccessProxy object) {
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
					



					
					
						

			
		
		//Issue # 122 : Replace pull down with autocomplete.
		toolTipContentPanel.add(accessList);
		//toolTipContentPanel.add(accessList);
		//Issue # 122 : Replace pull down with autocomplete.
		
		
	
		
	     toolTip.add(toolTipContentPanel);   // you can add any widget here
	        
	   
		toolTip.setPopupPosition(xPosition - 155,yPosition-50);
	    
	      toolTip.show();
	      
	    //Issue # 122 : Replace pull down with autocomplete.
	   accessList.addHandler(new ChangeHandler() {
		
		@Override
		public void onChange(ChangeEvent event) {
			// TODO Auto-generated method stub
			
			if(accessList.getSelected()!=null)
			{
				 
						boolean flag=false;
						
						
						Log.info("Selected data : " +accessList.getSelected().getName());
						
						RoleBaseTableAccessViewImpl roleBaseTableAccssViewImpl = new RoleBaseTableAccessViewImpl();
					
						roleBaseTableAccssViewImpl.setDelegate(roleScriptTemplateDetailsActivity);
					
						//roleBaseTableAccssViewImpl.setRoleItemAccessProxy(event.getValue());
						if(roleBasedItemProxynew.getRoleItemAccess() != null)
						{
							
							 Iterator<RoleItemAccessProxy> roleBaseProxy = roleBasedItemProxynew.getRoleItemAccess().iterator(); 
							 while(roleBaseProxy.hasNext())
							 {
								 if(roleBaseProxy.next().getId().longValue()==accessList.getSelected().getId().longValue()){
									 flag=true;
									 break;
								 }
							 }
						}
						if(flag){
							//Window.alert("Same Role Access Is not assign twice");
							MessageConfirmationDialogBox dialog =new MessageConfirmationDialogBox(constants.information());
							dialog.showConfirmationDialog(constants.accessNotification());
							return ;
						}
						
						roleBaseTableAccssViewImpl.accessDataLabel.setText(accessList.getSelected().getName());
						accessDataPanel.add(roleBaseTableAccssViewImpl);
										
						RoleBaseItemProxy editRoleBasedItemProxy = roleBasedItemProxynew;
						
						RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();										
						editRoleBasedItemProxy = roleBaseItemReq.edit(editRoleBasedItemProxy);
						
						Set<RoleItemAccessProxy> setRoleItemAccessProxy = editRoleBasedItemProxy.getRoleItemAccess();
				
						Log.info("setRoleItemAccessProxy is " + setRoleItemAccessProxy.size());
						
						Log.info("accessList.getSelected() Is " + accessList.getSelected());
				
						setRoleItemAccessProxy.add(accessList.getSelected());
						
						editRoleBasedItemProxy.setRoleItemAccess(setRoleItemAccessProxy);
						
						// PERSIST TEST
						roleBaseItemReq.persist().using(editRoleBasedItemProxy).fire(new Receiver<Void>() {

							@Override
							public void onSuccess(Void response) {
								
								Log.info("Successfully added access : " + response);
								toolTip.hide();
								view.getTableItem().clear();
								init();
								init2();						
							}
							
						});
						
						
				   
			}
				
		}
	});
	   
	 //Issue # 122 : Replace pull down with autocomplete.
	      
	 //Issue # 122 : Replace pull down with autocomplete.
	  
	   /*
	      accessList.addValueChangeHandler(new ValueChangeHandler<RoleItemAccessProxy>() {
						
			
			@Override
			public void onValueChange(ValueChangeEvent<RoleItemAccessProxy> event) {
				boolean flag=false;
				
				
				Log.info("Selected data : " +event.getValue().getName());
				
				RoleBaseTableAccessViewImpl roleBaseTableAccssViewImpl = new RoleBaseTableAccessViewImpl();
			
				roleBaseTableAccssViewImpl.setDelegate(roleScriptTemplateDetailsActivity);
			
				//roleBaseTableAccssViewImpl.setRoleItemAccessProxy(event.getValue());
				if(roleBasedItemProxy.getRoleItemAccess() != null)
				{
					
					 Iterator<RoleItemAccessProxy> roleBaseProxy = roleBasedItemProxy.getRoleItemAccess().iterator(); 
					 while(roleBaseProxy.hasNext())
					 {
						 if(roleBaseProxy.next().getId().longValue()==event.getValue().getId().longValue()){
							 flag=true;
						 }
					 }
				}
				if(flag){
					Window.alert("Same Role Access Is not assign twice");
					return ;
				}
				
				roleBaseTableAccssViewImpl.accessDataLabel.setText(event.getValue().getName());
				accessDataPanel.add(roleBaseTableAccssViewImpl);
								
				RoleBaseItemProxy editRoleBasedItemProxy = roleBasedItemProxy;
				
				RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();										
				editRoleBasedItemProxy = roleBaseItemReq.edit(editRoleBasedItemProxy);
				
				Set<RoleItemAccessProxy> setRoleItemAccessProxy = editRoleBasedItemProxy.getRoleItemAccess();
		
		
				setRoleItemAccessProxy.add(event.getValue());
				
				editRoleBasedItemProxy.setRoleItemAccess(setRoleItemAccessProxy);
				
				// PERSIST TEST
				roleBaseItemReq.persist().using(editRoleBasedItemProxy).fire(new Receiver<Void>() {

					@Override
					public void onSuccess(Void response) {
						
						Log.info("Successfully added access : " + response);
						toolTip.hide();
						view.getTableItem().clear();
						init();
						init2();						
					}
					
				});
				
				
				
				
			}
		});
		*/
	 //Issue # 122 : Replace pull down with autocomplete.
		

			}
		});
	   
	   
			}
		});

	}
	
	
		

	@Override
	public void deleteAccessType(RoleBaseItemProxy roleBasedItemProxy,Label dataAccessLabel,RoleItemAccessProxy roleItemAccessProxy) {
		
		
		
		RoleBaseTableAccessViewImpl roleBaseTableAccssViewImpl = new RoleBaseTableAccessViewImpl();
	
		roleBaseTableAccssViewImpl.setDelegate(roleScriptTemplateDetailsActivity);
			
		RoleBaseItemProxy editRoleBasedItemProxy = roleBasedItemProxy;
		RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();										
		editRoleBasedItemProxy = roleBaseItemReq.edit(editRoleBasedItemProxy);
		
		Set<RoleItemAccessProxy> setRoleItemAccessProxy = editRoleBasedItemProxy.getRoleItemAccess();							
		
		Set<RoleItemAccessProxy> editSetRoleItemAccessProxy = new HashSet<RoleItemAccessProxy>(); 	
		editSetRoleItemAccessProxy.addAll(setRoleItemAccessProxy);	
		
		Log.info("RoleItem access count before delete :" + setRoleItemAccessProxy.size());
		
		
		editRoleBasedItemProxy.getRoleItemAccess().removeAll(setRoleItemAccessProxy);
		Log.info("RoleItem access count After delete :" + setRoleItemAccessProxy.size());
		
		
		Iterator<RoleItemAccessProxy> iterRoleItemProxy = editSetRoleItemAccessProxy.iterator();
		
		while(iterRoleItemProxy.hasNext())
		{
			if(roleItemAccessProxy.getId().longValue() == iterRoleItemProxy.next().getId().longValue())
			{
				Log.info("Removing : " + roleItemAccessProxy.getId() + " : " + roleItemAccessProxy.getName());
				iterRoleItemProxy.remove();
				break;
			}
			else
				Log.info("Other : " + roleItemAccessProxy.getId() + " : " + roleItemAccessProxy.getName());
			
		}
				
	
		
		editRoleBasedItemProxy.setRoleItemAccess(editSetRoleItemAccessProxy);		
		roleBaseItemReq.persist().using(editRoleBasedItemProxy).fire(new OSCEReceiver<Void>() {
	
			@Override
			public void onSuccess(Void response) {
				view.getTableItem().clear();
				init();
				init2();
			}

	
			
		});
		
	}

	

}

