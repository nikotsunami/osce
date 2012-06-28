package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.AnamnesisCheckTitleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.VisibleRange;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.Operation;
import com.google.gwt.place.shared.PlaceChangeEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class AnamnesisCheckActivity extends AbstractActivity implements
        AnamnesisCheckView.Presenter, AnamnesisCheckView.Delegate,
        PlaceChangeEvent.Handler {

    private OsMaRequestFactory requests;
    private PlaceController placeController;
    private AnamnesisCheckPlace place;
    private AcceptsOneWidget widget;
    private AnamnesisCheckView view;
    private SingleSelectionModel<AnamnesisCheckProxy> selectionModel;
    private HandlerRegistration rangeChangeHandler;
    private HandlerRegistration selectionChangeHandler;
    private ActivityManager activityManger;
    private AnamnesisCheckDetailsActivityMapper anamnesisCheckDetailsActivityMapper;
    private final OsceConstants constants = GWT.create(OsceConstants.class);



    static AnamnesisCheckRequest request = null;

    private static final String placeToken = "AnamnesisCheckPlace";

    private String quickSearchTerm = "";

    private HandlerRegistration placeChangeHandlerRegistration;
    
    private boolean sortOrderHasChange = false;

    public AnamnesisCheckActivity(OsMaRequestFactory requests,
            PlaceController placeController, AnamnesisCheckPlace place) {
        this.requests = requests;
        this.placeController = placeController;
        anamnesisCheckDetailsActivityMapper = new AnamnesisCheckDetailsActivityMapper(
                requests, placeController);
        this.activityManger = new ActivityManager(
                anamnesisCheckDetailsActivityMapper, requests.getEventBus());
        this.place = place;


    }

    /**
     * Called when the activity stops
     */
    @Override
    public void onStop() {
        activityManger.setDisplay(null);
        if (rangeChangeHandler != null) {
            rangeChangeHandler.removeHandler();
            rangeChangeHandler = null;
        }

        if (selectionChangeHandler != null) {
            selectionChangeHandler.removeHandler();
            selectionChangeHandler = null;
        }


    if (placeChangeHandlerRegistration != null) {
        placeChangeHandlerRegistration.removeHandler();
    }

    request = null;
    }

    @Override
    public String mayStop() {
    	if (sortOrderHasChange){
    		return constants.leavePageWarn();
    	} else {
    		return null;
    		
    	}
    }


    /**
     * The activity has started
     */
    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        Log.info("SystemStartActivity.start()");
        AnamnesisCheckView systemStartView = getAnamnesisCheckView();
        systemStartView.setPresenter(this);
        this.widget = panel;
        this.view = systemStartView;

        widget.setWidget(systemStartView.asWidget());
        view.getFilterTitle().clear();

        eventBus.addHandler(PlaceChangeEvent.TYPE,
                new PlaceChangeEvent.Handler() {
                    @SuppressWarnings("deprecation")
					public void onPlaceChange(PlaceChangeEvent event) {
                        if (event.getNewPlace() instanceof AnamnesisCheckDetailsPlace) {
                        	final AnamnesisCheckDetailsPlace place = (AnamnesisCheckDetailsPlace) event.getNewPlace();
                        	if(place.getOperation() == Operation.NEW){
                        		
                                requests.find((EntityProxyId<AnamnesisCheckProxy>)place.getProxyId()).with("anamnesisCheckTitle").fire(new Receiver<AnamnesisCheckProxy>() {

                                    @Override
                                    public void onSuccess(AnamnesisCheckProxy proxy) {
                                    	
                                        if (proxy != null ) {
                                            for(int i = 0;i<view.getFilterTitle().getItemCount();i++){
                                            	if(view.getFilterTitle().getValue(i).equals(place.getTitleId())){
                                            		view.getFilterTitle().setSelectedIndex(i);
                                            	}
                                            }
                                            view.getSearchBox().setText(proxy.getText());
                                            List<AnamnesisCheckTitleProxy> titles = new ArrayList<AnamnesisCheckTitleProxy>();
                                            titles.add(proxy.getAnamnesisCheckTitle());
                                            view.loadAnamnesisCheckPanel(titles, true);
                                        }
                                    }
                                 });
                        	}
                        }else if(event.getNewPlace() instanceof AnamnesisCheckTitleDetailsPlace){
                        	final AnamnesisCheckTitleDetailsPlace place = (AnamnesisCheckTitleDetailsPlace) event.getNewPlace();
                        	if(place.getOperation() == Operation.NEW){
                        		requests.find((EntityProxyId<AnamnesisCheckTitleProxy>)place.getProxyId()).fire(new Receiver<AnamnesisCheckTitleProxy>() {
                        			
                        			@Override
                        			public void onSuccess(AnamnesisCheckTitleProxy proxy) {
                        				
                        				if (proxy != null ) {
                        					view.getFilterTitle().addItem(proxy.getText(), String.valueOf(proxy.getId()));
                        					view.getFilterTitle().setSelectedIndex(view.getFilterTitle().getItemCount()-1);
                        					view.getSearchBox().setText("");
                        					List<AnamnesisCheckTitleProxy> titles = new ArrayList<AnamnesisCheckTitleProxy>();
                        					titles.add(proxy);
                        					view.loadAnamnesisCheckPanel(titles, true);
                        				}
                        			}
                        		});
                        	}
                        }
                    }
                });
        init();

        activityManger.setDisplay(view.getDetailsPanel());

        view.setDelegate(this);

    }


    private void init() {


        if (place.getSearchStr().equals("")) {
            view.setSearchBoxShown(place.DEFAULT_SEARCHSTR);

            if (place.getFilterTileId().equals("")) {
                view.setSearchFocus(false);
            } else {

                view.setSearchFocus(true);
            }
        } else {
            view.setSearchBoxShown(place.getSearchStr());
            view.setSearchFocus(true);
        }

        view.setDelegate(this);             
        
        getTitles();

    }

    @Override
    public void moveUp(final AnamnesisCheckProxy proxy) {
		if (request == null) {
			requests.anamnesisCheckRequestNonRoo().moveUp().using(proxy).fire(
					new Receiver<Void>() {
						@Override
						public void onSuccess(Void response) {
							Log.info("moved");
							// init();
							findTitlesWithSearching();
						}
					});
		} else {
			Window.alert(constants.unSaveOrderWarn());
		}
    }

    @Override
    public void moveDown(final AnamnesisCheckProxy proxy) {
		if (request == null) {
			requests.anamnesisCheckRequestNonRoo().moveDown().using(proxy)
					.fire(new Receiver<Void>() {
						@Override
						public void onSuccess(Void response) {
							Log.info("moved");
							// init();
							findTitlesWithSearching();
						}
					});
		} else {
			Window.alert(constants.unSaveOrderWarn());
		}
    }

    @Override
    public void deleteClicked(AnamnesisCheckProxy proxy) {
      
    }

    private void fireRangeRequest(String q, AnamnesisCheckProxy title,
            final Range range,
            final Receiver<List<AnamnesisCheckProxy>> callback) {
        createRangeRequest(q, title, range).with(view.getPaths())
                .fire(callback);
    }

    protected Request<java.util.List<AnamnesisCheckProxy>> createRangeRequest(
            String q, AnamnesisCheckProxy title, Range range) {
        return requests.anamnesisCheckRequestNonRoo()
                .findAnamnesisChecksBySearchWithTitle(q, title, range.getStart(),
                        range.getLength());
    }

    protected void fireCountRequest(String q, AnamnesisCheckProxy title,
            Receiver<Long> callback) {
        requests.anamnesisCheckRequestNonRoo()
                .countAnamnesisChecksBySearchWithTitle(q, title).fire(callback);
    }

    protected void fireTitleValueRequest(Receiver<List<AnamnesisCheckTitleProxy>> callback) {
    	requests.anamnesisCheckTitleRequest().findAllAnamnesisCheckTitles().fire(callback);
    }

    // find check value by title
    protected void fireCheckValueRequest(String searchValue,
            AnamnesisCheckProxy title,
            Receiver<List<AnamnesisCheckProxy>> callback) {
        requests.anamnesisCheckRequestNonRoo()
                .findAnamnesisChecksByTitle(searchValue, title)
                .with(view.getPaths()).fire(callback);
    }


    @Override
    public void newDetailClicked(String titleId) {
        Log.debug("newClicked()");
        goTo(new AnamnesisCheckDetailsPlace(Operation.CREATE , titleId));
    }

    @Override
    public void performSearch(String q) {
        Log.debug("Search for " + q);
        quickSearchTerm = q;
        goToAnamesisCheckPlace();
    }

    String getSelectedTitleId() {
        return view.getFilterTitle().getValue(
                view.getFilterTitle().getSelectedIndex());
    }

    @Override
    public void goTo(Place place) {

        placeController.goTo(place);

    }

    private void goToAnamesisCheckPlace() {
    	
        goTo(new AnamnesisCheckPlace(placeToken,0, "ALL",
                view.getSearchBoxShown(), getSelectedTitleId()));
    }


    /**
     * change Filter Title ListBox selectedValue
     */
    @SuppressWarnings("deprecation")
	@Override
    public void changeFilterTitleShown(String selectedtTitle) {

    	final String searchStr = view.getSearchBox().getText();
    	requests.anamnesisCheckRequestNonRoo().findTitlesContatisAnamnesisChecksWithSearching(searchStr, getSelectedFilterTitle()).fire(new Receiver<List<AnamnesisCheckTitleProxy>>(){

			@Override
			public void onSuccess(List<AnamnesisCheckTitleProxy> response) {
				view.getAnamnesisCheckPanel().clear();
				if((searchStr == null || searchStr.equals("")) && getSelectedFilterTitle() == null){
					view.loadAnamnesisCheckPanel(response, false);
				}else{
					view.loadAnamnesisCheckPanel(response, true);
				}
			
			}
    		
    	});
    }
    
    
    @SuppressWarnings("deprecation")
	private void getTitles() {

		fireTitleValueRequest(new Receiver<List<AnamnesisCheckTitleProxy>>() {
			public void onFailure(ServerFailure error) {
				GWT.log("!!!!!!!!error : " + error.getMessage());
			}

			@Override
			public void onSuccess(List<AnamnesisCheckTitleProxy> response) {
				anamnesisCheckTitles = response;
				setAnamnesisCheckTitleList(anamnesisCheckTitles);
				findTitlesWithSearching();
			}

		});
    		
    	
		

    }
    
    private void findTitlesWithSearching(){
    	
    	final String searchStr = view.getSearchBox().getText();
		requests.anamnesisCheckRequestNonRoo().findTitlesContatisAnamnesisChecksWithSearching(searchStr, getSelectedFilterTitle()).fire(new Receiver<List<AnamnesisCheckTitleProxy>>() {

			@Override
			public void onSuccess(List<AnamnesisCheckTitleProxy> response) {
				if((searchStr == null || searchStr.equals("")) && getSelectedFilterTitle() == null){
					view.loadAnamnesisCheckPanel(response, false);
				}else{
					view.loadAnamnesisCheckPanel(response, true);
				}

			}
		});
    }

    /**
     * get Fileter Title AnamnesisCheckProxy
     *
     * @return AnamnesisCheckProxy
     */
    private AnamnesisCheckTitleProxy getSelectedFilterTitle() {

        for (AnamnesisCheckTitleProxy title : anamnesisCheckTitles) {
        	
            if (view.getFilterTitle().getSelectedIndex() != -1 && getSelectedTitleId().equals(String.valueOf(title.getId()))) {
                return title;
            }
        }
        return null;
    }

    private List<AnamnesisCheckTitleProxy> anamnesisCheckTitles = new ArrayList<AnamnesisCheckTitleProxy>();

    /**
     * set Fileter Title ListBox install Value
     *
     * @param type
     *
     */
    public void setAnamnesisCheckTitleList(List<AnamnesisCheckTitleProxy> titles) {
    	 		view.getFilterTitle().clear();
            	GWT.log("fireTitleValueRequest sucess and getFilterTitle = "+place.getFilterTileId());
                view.getFilterTitle().addItem(constants.filterTitle(),"");
                view.getFilterTitle().setSelectedIndex(0);

                int idx = 1;
                for (AnamnesisCheckTitleProxy title : titles) {
                    view.getFilterTitle().addItem(title.getText(),
                            String.valueOf(title.getId()));

                    if (place.getFilterTileId() != null
                            && place.getFilterTileId().equals(
                                    title.getId().toString())) {
                        view.getFilterTitle().setSelectedIndex(idx);
                    }

                    idx++;
                }
    	

    }
//    
//    private void loadAnamnesisCheckPanel(List<AnamnesisCheckTitleProxy> titles){
//    	
//    }

    static AnamnesisCheckView systemStartViewS = null;

    /**
     * get AnamnesisCheckView
     *
     * @return AnamnesisCheckView
     */
    private static AnamnesisCheckView getAnamnesisCheckView() {
        if (systemStartViewS == null) {
            systemStartViewS = new AnamnesisCheckViewImpl();
        }
        return systemStartViewS;
    }


	@Override
	public List<AnamnesisCheckProxy> orderEdited(AnamnesisCheckProxy proxy,
			String sortOrderStr,
			ListDataProvider<AnamnesisCheckProxy> dataProvider) {
		
	
		//TODO delete
		for (AnamnesisCheckProxy anamnesisCheckProxy : dataProvider
				.getList()) {

			GWT.log("$$$$$$$$$$$$$$$$$$$$"+anamnesisCheckProxy.getText());


		}
		
		AnamnesisCheckProxy currentProxy = proxy;

		AnamnesisCheckRequest req = getRequest();

		try {
			if(sortOrderStr.matches("[0-9]+")) {

				Integer editSortOrder = Integer.valueOf(sortOrderStr);

				Integer dataSize = dataProvider.getList().size();

				if (editSortOrder > dataSize) {
					editSortOrder = dataSize;
				}

				if (editSortOrder > 0) {
					
					for (AnamnesisCheckProxy check : dataProvider.getList()) {
						// if has same sortOder with this sortOder
						if (check.getSort_order() == editSortOrder
								&& check.getId() != currentProxy.getId()) {
							GWT.log("************************find same sort Order");
							if (currentProxy.getSort_order() > editSortOrder) {
								// move up
								// for(AnamnesisCheckProxy anamnesisCheckProxy :
								// dataProvider.getList()){
								for (int i = dataSize - 1; i >= 0; i--) {
									// find between editableProxy and
									// currentProxy
									AnamnesisCheckProxy anamnesisCheckProxy = dataProvider
											.getList().get(i);
									if (anamnesisCheckProxy.getSort_order() >= editSortOrder
											&& anamnesisCheckProxy
													.getSort_order() < currentProxy
													.getSort_order()
											&& anamnesisCheckProxy.getId() != proxy
													.getId()) {
										GWT.log(anamnesisCheckProxy.getText()
												+ "sortOder+1");
										AnamnesisCheckProxy editanamnesisCheckProxy = req
												.edit(anamnesisCheckProxy);
										editanamnesisCheckProxy
												.setSort_order(anamnesisCheckProxy
														.getSort_order() + 1);
										req.persist().using(editanamnesisCheckProxy);
										dataProvider.getList().set(
												editanamnesisCheckProxy
														.getSort_order() - 1,
												editanamnesisCheckProxy);

										GWT.log(anamnesisCheckProxy
												.getSort_order()
												+ " change to "
												+ editanamnesisCheckProxy
														.getSort_order());

									}
								}
								// dataProvider.getList().set(editableProxy.getSort_order()
								// - 1, editableProxy);
							} else if (currentProxy.getSort_order() < editSortOrder) {
								// move down

								for (AnamnesisCheckProxy anamnesisCheckProxy : dataProvider
										.getList()) {
									// find between editableProxy and
									// currentProxy
									Integer anamnesisCheckProxySortorder = anamnesisCheckProxy.getSort_order();
									if (anamnesisCheckProxySortorder <= editSortOrder
											&& anamnesisCheckProxySortorder > currentProxy
													.getSort_order()
											&& anamnesisCheckProxy.getId() != proxy
													.getId()) {
										GWT.log(anamnesisCheckProxy.getText()
												+ "sortOder-1");
										AnamnesisCheckProxy editanamnesisCheckProxy = req
												.edit(anamnesisCheckProxy);
										editanamnesisCheckProxy
												.setSort_order(anamnesisCheckProxySortorder - 1);
										req.persist().using(editanamnesisCheckProxy);
										dataProvider.getList().set(
												editanamnesisCheckProxy
														.getSort_order() - 1,
												editanamnesisCheckProxy);

										GWT.log(anamnesisCheckProxySortorder
												+ " change to "
												+ editanamnesisCheckProxy
														.getSort_order());

									}
								}

							}
//							AnamnesisCheckProxy editableProxy = req.edit(proxy);
//							editableProxy.setSort_order(editSortOrder);
//							dataProvider.getList().set(
//									editableProxy.getSort_order() - 1,
//									editableProxy);
							// TODO change the number in text

						}else {
							//not has same number
							GWT.log(">>>>>>>>not has same number");

						}

					}
					
					AnamnesisCheckProxy editableProxy = req.edit(proxy);
					editableProxy.setSort_order(editSortOrder);
					req.persist().using(editableProxy);
//					Window.alert(editableProxy.getSort_order() - 1 + ","+editableProxy.getText());
					dataProvider.getList().set(
							editableProxy.getSort_order() - 1,
							editableProxy);

					
					// TODO delete
					for (AnamnesisCheckProxy anamnesisCheckProxy : dataProvider
							.getList()) {

						GWT.log(anamnesisCheckProxy.getText() + "sortOrder = "
								+ anamnesisCheckProxy.getSort_order());

					}
				} else {
					// TODO change the number in text
					Window.alert(constants.enterSortOrderWarn());
				}

			} else  {
				// TODO change the number in text
				Window.alert(constants.enterSortOrderWarn());
//				dataProvider.getList().set(
//						proxy.getSort_order() - 1,
//						proxy);
			}
			sortOrderHasChange = true;
		} catch (Exception e) {
			System.err.println(e);
			findTitlesWithSearching();
		}
		return dataProvider.getList();

	}

    @Override
    public void onPlaceChange(PlaceChangeEvent event) {

    }


    @SuppressWarnings("deprecation")
    @Override
    public void saveOrder() {


        getRequest().fire(new Receiver(){

            @Override
            public void onSuccess(Object response) {

            	sortOrderHasChange = false;
            	goToAnamesisCheckPlace();
            }

        });

        request = null;
    }


    private AnamnesisCheckRequest getRequest(){
        if (request == null){
            request = requests.anamnesisCheckRequest();
        }
        return request;
    }

	@SuppressWarnings("deprecation")
	@Override
	public void setQuestionTableData(final ListDataProvider<AnamnesisCheckProxy> dataProvider, AnamnesisCheckTitleProxy title) {

		if (dataProvider.getList() != null && dataProvider.getList().size() == 0) {
			requests.anamnesisCheckRequestNonRoo().findAnamnesisChecksBySearchWithAnamnesisCheckTitle(view.getSearchBoxShown(), title).fire(new Receiver<List<AnamnesisCheckProxy>>() {

				@Override
				public void onSuccess(List<AnamnesisCheckProxy> response) {

					if (dataProvider.getList() != null && dataProvider.getList().size() == 0) {
						dataProvider.getList().addAll(response);
						dataProvider.refresh();
						dataProvider.setList(dataProvider.getList());
					}

				}
			});
		}
	}

	@Override
	public void newTitleClicked() {
		goTo(new AnamnesisCheckTitleDetailsPlace(Operation.CREATE));
	}
	
    /**
     * Called from the table selection handler
     *
     * @param anamnesisCheck
     */
	@Override
    public void showDetails(AnamnesisCheckProxy anamnesisCheck) {

        Log.debug(anamnesisCheck.getId().toString());

        goTo(new AnamnesisCheckDetailsPlace(anamnesisCheck.stableId(),
                Operation.DETAILS));
    }

	@Override
	public void moveDownTitle(AnamnesisCheckTitleProxy proxy) {
		if (request == null) {
			requests.anamnesisCheckTitleRequestNonRoo().moveDown().using(proxy)
					.fire(new Receiver<Void>() {

						@Override
						public void onSuccess(Void response) {
							// init();
							findTitlesWithSearching();
						}
					});
		} else {
			Window.alert(constants.unSaveOrderWarn());
		}
	}

	@Override
	public void moveUpTitle(AnamnesisCheckTitleProxy proxy) {
		if (request == null) {
			requests.anamnesisCheckTitleRequestNonRoo().moveUp().using(proxy)
					.fire(new Receiver<Void>() {

						@Override
						public void onSuccess(Void response) {

							findTitlesWithSearching();
						}
					});
		} else {
			Window.alert(constants.unSaveOrderWarn());
		}
	}
	
}
