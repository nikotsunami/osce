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
//    private CellTable<AnamnesisCheckProxy> table;
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
        return null;
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


        eventBus.addHandler(PlaceChangeEvent.TYPE,
                new PlaceChangeEvent.Handler() {
                    public void onPlaceChange(PlaceChangeEvent event) {
                        if (event.getNewPlace() instanceof AnamnesisCheckDetailsPlace) {
//                            init();
                        }
                    }
                });
        init();

        activityManger.setDisplay(view.getDetailsPanel());

        view.setDelegate(this);
        placeChangeHandlerRegistration = eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {

            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                if (event.getNewPlace() instanceof AnamnesisCheckDetailsPlace) {
                    AnamnesisCheckDetailsPlace place = (AnamnesisCheckDetailsPlace) event.getNewPlace();
                    if (place.getOperation() == Operation.NEW) {
                        getSearchStringByEntityProxyId((EntityProxyId<AnamnesisCheckProxy>)place.getProxyId());
                    }
                } else if (event.getNewPlace() instanceof AnamnesisCheckPlace) {
                    AnamnesisCheckPlace place = (AnamnesisCheckPlace) event.getNewPlace();
                    if (place.getToken().contains("!DELETED")) {
//                        initSearch();
                    }
                }
            }
        });
    }

    /**
      * Used to fill table and search field after creating new entity.
      * @param entityId
      */
     private void getSearchStringByEntityProxyId(EntityProxyId<AnamnesisCheckProxy> entityId) {
         requests.find(entityId).fire(new Receiver<AnamnesisCheckProxy>() {

            @Override
            public void onSuccess(AnamnesisCheckProxy proxy) {
                if (proxy != null) {
                    List<AnamnesisCheckProxy> values = new ArrayList<AnamnesisCheckProxy>();
                    values.add(proxy);
                    view.getSearchBox().setText(proxy.getText());

                }
            }
         });
     }

    private void init() {
//    	Window.alert("init");
        view.getFilterTitle().clear();
        view.getAnamnesisCheckPanel().clear();
//        view.getScrollPanel().clear();
        getTitles();

        if (place.getSearchStr().equals("")) {
            view.setSearchBoxShown(place.DEFAULT_SEARCHSTR);
//            initSearch();
            if (place.getFilterTileId().equals("")) {
                view.setSearchFocus(false);
            } else {

                view.setSearchFocus(true);
            }
        } else {
            view.setSearchBoxShown(place.getSearchStr());
//            initSearch();
            view.setSearchFocus(true);
        }

        view.setDelegate(this);

    }

//    private void initSearch() {
//        fireCountRequest(quickSearchTerm, getSelectedFilterTitle(), new Receiver<Long>() {
//            @Override
//            public void onSuccess(Long response) {
//                if (view == null) {
//                    // This activity is dead
//                    return;
//                }
//                Log.debug("Geholte Intitution aus der Datenbank: " + response);
//                setTableRowCount(response.intValue(), true);
//                view.setListBoxItem(place.getPageLen());
//                onRangeChanged(quickSearchTerm);
//            }
//        });
//
//        if (rangeChangeHandler != null) {
//            rangeChangeHandler.removeHandler();
//            rangeChangeHandler = null;
//        }
//
//        if (selectionChangeHandler != null) {
//            selectionChangeHandler.removeHandler();
//            selectionChangeHandler = null;
//        }

//        rangeChangeHandler = table
//                .addRangeChangeHandler(new RangeChangeEvent.Handler() {
//                    public void onRangeChange(RangeChangeEvent event) {
//                        AnamnesisCheckActivity.this.onRangeChanged(quickSearchTerm);
//                    }
//                });
//    }





    @Override
    public void moveUp(AnamnesisCheckProxy proxy) {
        requests.anamnesisCheckRequestNonRoo().moveUp().using(proxy)
                .fire(new Receiver<Void>() {
                    @Override
                    public void onSuccess(Void response) {
                        Log.info("moved");
                        init();

                    }
                });
    }

    @Override
    public void moveDown(AnamnesisCheckProxy proxy) {
        requests.anamnesisCheckRequestNonRoo().moveDown().using(proxy)
                .fire(new Receiver<Void>() {
                    @Override
                    public void onSuccess(Void response) {
                        Log.info("moved");
                        init();
                    }
                });
    }

    @Override
    public void deleteClicked(AnamnesisCheckProxy proxy) {
        // TODO implement
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
//        initSearch();
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
    	GWT.log("###########this is changeFilterTitleShown");

//		Window.alert("getSelectedFilterTitle() = " + getSelectedFilterTitle());
    	requests.anamnesisCheckRequestNonRoo().findTitlesContatisAnamnesisChecksWithSearching(place.getSearchStr(), getSelectedFilterTitle()).fire(new Receiver<List<AnamnesisCheckTitleProxy>>(){

			@Override
			public void onSuccess(List<AnamnesisCheckTitleProxy> response) {
				GWT.log("????????in changeFilterTitleShown response = "+response.size());
				view.getAnamnesisCheckPanel().clear();
				if((place.getSearchStr() == null || place.getSearchStr().equals("")) && getSelectedFilterTitle() == null){
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
					GWT.log("place.getSearchStr() = " + place.getSearchStr());
					requests.anamnesisCheckRequestNonRoo().findTitlesContatisAnamnesisChecksWithSearching(place.getSearchStr(), getSelectedFilterTitle()).fire(new Receiver<List<AnamnesisCheckTitleProxy>>() {

						@Override
						public void onSuccess(List<AnamnesisCheckTitleProxy> response) {
							GWT.log("findTitlesContatisAnamnesisChecksWithSearching size = "+response.size());
							if((place.getSearchStr() == null || place.getSearchStr().equals("")) && getSelectedFilterTitle() == null){
								view.loadAnamnesisCheckPanel(response, false);
							}else{
								view.loadAnamnesisCheckPanel(response, true);
							}

						}
					});

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
    public void orderEdited(AnamnesisCheckProxy proxy,
            String sortOrderStr) {
        AnamnesisCheckRequest req = getRequest();
        AnamnesisCheckProxy editableProxy = req.edit(proxy);
        try {
            editableProxy.setSort_order(Integer
                    .valueOf(sortOrderStr));
            req.persist().using(editableProxy);

        } catch (Exception e) {

            System.err.println(e);
        }

    }


    @Override
    public void onPlaceChange(PlaceChangeEvent event) {
    }


    @SuppressWarnings("deprecation")
    @Override
    public void saveOrder() {
    	Window.alert("sava oder");


        getRequest().fire(new Receiver(){

            @Override
            public void onSuccess(Object response) {
//                requests.anamnesisCheckRequestNonRoo().normalizeOrder().fire(new Receiver(){
//
//                    @Override
//                    public void onSuccess(Object response) {
//                        init();
//                    }
//                });
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
		GWT.log("this is setQuestionTableData dataProvider = " + dataProvider);
		GWT.log("this is setQuestionTableData title = " + title);
		if (dataProvider.getList() != null && dataProvider.getList().size() == 0) {
			GWT.log("this is setQuestionTableData view.getSearchBoxShown() = " + view.getSearchBoxShown());
			requests.anamnesisCheckRequestNonRoo().findAnamnesisChecksBySearchWithAnamnesisCheckTitle(view.getSearchBoxShown(), title).fire(new Receiver<List<AnamnesisCheckProxy>>() {

				@Override
				public void onSuccess(List<AnamnesisCheckProxy> response) {
					GWT.log("########setQuestionTableData sueccess and response size = " + response.size());
					GWT.log("########dataProvider.getList().size() = " + dataProvider.getList().size());

					if (dataProvider.getList() != null && dataProvider.getList().size() == 0) {
						dataProvider.getList().addAll(response);
						dataProvider.refresh();
						dataProvider.setList(dataProvider.getList());
					}

					// dataProvider.setList(anamnesisCheckProxyList);
				}
			});
		}
	}

	@Override
	public void newTitleClicked() {
		// TODO Auto-generated method stub
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
	
}
