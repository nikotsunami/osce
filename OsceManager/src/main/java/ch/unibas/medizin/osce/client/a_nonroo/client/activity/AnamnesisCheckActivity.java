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
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ClinicPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.VisibleRange;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckRequest;
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
    private CellTable<AnamnesisCheckProxy> table;
    private SingleSelectionModel<AnamnesisCheckProxy> selectionModel;
    private HandlerRegistration rangeChangeHandler;
    private HandlerRegistration selectionChangeHandler;
    private ActivityManager activityManger;
    private AnamnesisCheckDetailsActivityMapper anamnesisCheckDetailsActivityMapper;
    private final OsceConstants constants = GWT.create(OsceConstants.class);
    // private List<AnamnesisCheckProxy> tableData = null;


    static AnamnesisCheckRequest request = null;

    private static final String placeToken = "AnamnesisCheckPlace";

    // private String listSelectedValue = "10";
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
        setTable(view.getTable());

        view.getFilterTitle().clear();
        anamnesisCheckTitleList(AnamnesisCheckTypes.QUESTION_TITLE);

        eventBus.addHandler(PlaceChangeEvent.TYPE,
                new PlaceChangeEvent.Handler() {
                    public void onPlaceChange(PlaceChangeEvent event) {
                        if (event.getNewPlace() instanceof AnamnesisCheckDetailsPlace) {
                            init();
                        }
                    }
                });
        init();

        activityManger.setDisplay(view.getDetailsPanel());

        // Inherit the view's key provider
        ProvidesKey<AnamnesisCheckProxy> keyProvider = ((AbstractHasData<AnamnesisCheckProxy>) table)
                .getKeyProvider();
        selectionModel = new SingleSelectionModel<AnamnesisCheckProxy>(
                keyProvider);
        table.setSelectionModel(selectionModel);

        selectionChangeHandler = selectionModel
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {
                        AnamnesisCheckProxy selectedObject = selectionModel
                                .getSelectedObject();
                        if (selectedObject != null) {
                            Log.debug(selectedObject.getId() + " selected!");
                            showDetails(selectedObject);
                        }
                    }
                });
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
                        initSearch();
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
                    table.setRowCount(1, true);
                    table.setRowData(0, values);
                }
            }
         });
     }

    private void init() {

        setStartAndVisiableRange(place.getPageStart(), place.getPageLen());

        if (place.getSearchStr().equals("")) {
            view.setSearchBoxShown(place.DEFAULT_SEARCHSTR);
            initSearch();
            if (place.getFilterTileId().equals("")) {
                view.setSearchFocus(false);
            } else {

                view.setSearchFocus(true);
            }
        } else {
            view.setSearchBoxShown(place.getSearchStr());
            initSearch();
            view.setSearchFocus(true);
        }
        ProvidesKey<AnamnesisCheckProxy> keyProvider = ((AbstractHasData<AnamnesisCheckProxy>) table)
                .getKeyProvider();
        selectionModel = new SingleSelectionModel<AnamnesisCheckProxy>(
                keyProvider);
        table.setSelectionModel(selectionModel);

        selectionChangeHandler = selectionModel
                .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                    public void onSelectionChange(SelectionChangeEvent event) {
                        AnamnesisCheckProxy selectedObject = selectionModel
                                .getSelectedObject();
                        if (selectedObject != null) {
                            Log.debug(selectedObject.getId() + " selected!");
                            showDetails(selectedObject);
                        }
                    }
                });
        view.setDelegate(this);

    }

    private void initSearch() {
        fireCountRequest(quickSearchTerm, getSelectedFilterTitle(), new Receiver<Long>() {
            @Override
            public void onSuccess(Long response) {
                if (view == null) {
                    // This activity is dead
                    return;
                }
                Log.debug("Geholte Intitution aus der Datenbank: " + response);
                setTableRowCount(response.intValue(), true);
                view.setListBoxItem(place.getPageLen());
                onRangeChanged(quickSearchTerm);
            }
        });

        if (rangeChangeHandler != null) {
            rangeChangeHandler.removeHandler();
            rangeChangeHandler = null;
        }

        if (selectionChangeHandler != null) {
            selectionChangeHandler.removeHandler();
            selectionChangeHandler = null;
        }

        rangeChangeHandler = table
                .addRangeChangeHandler(new RangeChangeEvent.Handler() {
                    public void onRangeChange(RangeChangeEvent event) {
                        AnamnesisCheckActivity.this.onRangeChanged(quickSearchTerm);
                    }
                });
    }

    /***
     * set the count row of table
     * */
    private void setTableRowCount(int size, boolean isExact) {
        view.getTable().setRowCount(size, true);
    }

    /**
     * Called from the table selection handler
     *
     * @param AnamnesisCheck
     */
    protected void showDetails(AnamnesisCheckProxy AnamnesisCheck) {

        Log.debug(AnamnesisCheck.getId().toString());

        goTo(new AnamnesisCheckDetailsPlace(AnamnesisCheck.stableId(),
                Operation.DETAILS));
    }

    protected void onRangeChanged(String q) {
        final Range range = table.getVisibleRange();
        final Receiver<List<AnamnesisCheckProxy>> callback = new Receiver<List<AnamnesisCheckProxy>>() {
            @Override
            public void onSuccess(List<AnamnesisCheckProxy> values) {
                setTableData(range.getStart(), values);
            }
        };

        fireRangeRequest(q, getSelectedFilterTitle(), range, callback);
    }

    /**
     * set the data source to Table
     * */
    private void setTableData(int start, List<AnamnesisCheckProxy> values) {
        if (view == null) {
            // This activity is dead
            return;
        }
        table.setRowData(start, values);

        // finishPendingSelection();
        if (widget != null) {
            widget.setWidget(view.asWidget());
        }
    }

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

    protected void fireTitleValueRequest(AnamnesisCheckTypes type,
            Receiver<List<AnamnesisCheckProxy>> callback) {
        requests.anamnesisCheckRequestNonRoo().findAnamnesisChecksByType(type)
                .fire(callback);
    }

    // find check value by title
    protected void fireCheckValueRequest(String searchValue,
            AnamnesisCheckProxy title,
            Receiver<List<AnamnesisCheckProxy>> callback) {
        requests.anamnesisCheckRequestNonRoo()
                .findAnamnesisChecksByTitle(searchValue, title)
                .with(view.getPaths()).fire(callback);
    }

    private void setTable(CellTable<AnamnesisCheckProxy> table) {
        this.table = table;
    }

    @Override
    public void newClicked() {
        Log.debug("newClicked()");
        goTo(new AnamnesisCheckDetailsPlace(Operation.CREATE));
    }

    @Override
    public void performSearch(String q) {
        Log.debug("Search for " + q);
        quickSearchTerm = q;
        initSearch();
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
        // goTo(new AnamnesisCheckPlace(placeToken, table.getVisibleRange()
        // .getStart(), place.getPageLen(),searchText, getSelectedTitleId()));
        goTo(new AnamnesisCheckPlace(placeToken, table.getVisibleRange()
                .getStart(), view.getRangNumBox().getItemText(
                view.getRangNumBox().getSelectedIndex()),
                view.getSearchBoxShown(), getSelectedTitleId()));
    }

    /**
     * change Rang Number ListBox value
     */
    @Override
    public void changeNumRowShown(String selectedValue) {

        // listSelectedValue = selectedValue;

        goTo(new AnamnesisCheckPlace(placeToken, table.getVisibleRange()
                .getStart(), selectedValue, view.getSearchBoxShown(),
                getSelectedTitleId()));
    }

    /**
     * Find matching VisibleRange enum value and set "table" range keeping the
     * start value from the original range
     **/
    public void findVisiableRange(String selectedValue) {
        int start = table.getPageStart();
        setStartAndVisiableRange(start, selectedValue);
    }

    /**
     * Find matching VisibleRange enum value and set "table" range keeping the
     * start value from the original range
     **/
    public void setStartAndVisiableRange(final int start,
            final String selectedValueP) {

        String selectedValue = null;
        if (selectedValueP == null) {
            selectedValue = view.getRangNumBox().getValue(
                    view.getRangNumBox().getSelectedIndex());
        } else {
            selectedValue = selectedValueP;
        }
        final String selectedValueF = selectedValue;

        AnamnesisCheckProxy title = getSelectedFilterTitle();

        // Show all the rows.

        fireCountRequest(view.getSearchBox().getText(), title,
                new Receiver<Long>() {

                    @Override
                    public void onSuccess(Long response) {

                        int start = 0;
                        int rows = response.intValue();

                        if (table.getPageStart() < rows) {
                            start = table.getPageStart();

                        }

                        if (VisibleRange.ALL.getName().equals(selectedValueF)) {

                            Range range = new Range(start, rows);
                            table.setVisibleRange(range);
                            table.setRowCount(rows, true);

                        } else {
                            VisibleRange selectedRange = null;

                            for (VisibleRange range : VisibleRange.values()) {
                                if (range.getName().equals(selectedValueF)) {
                                    selectedRange = range;

                                }
                            }

                            Range range = new Range(start, selectedRange
                                    .getValue());
                            table.setVisibleRange(range);
                            table.setRowCount(rows, true);

                        }

                    }

                });

        return;

    }

    /**
     * change Filter Title ListBox selectedValue
     */
    @Override
    public void changeFilterTitleShown(String selectedtTitle) {

        AnamnesisCheckProxy id = null;

        for (AnamnesisCheckProxy checkId : anamnesisCheck) {
            if (selectedtTitle.equals(checkId.getText())) {
                id = checkId;
            }
        }

        if (id != null) {
            fireCheckValueRequest(view.getSearchBox().getText(), id,
                    new Receiver<List<AnamnesisCheckProxy>>() {

                        @Override
                        public void onSuccess(List<AnamnesisCheckProxy> response) {

                            setTableRowCount(response.size(), true);

                            Range range = table.getVisibleRange();

                            setTableData(range.getStart(), response);

                        }

                    });
        } else {
            initSearch();
        }

    }

    /**
     * get Fileter Title AnamnesisCheckProxy
     *
     * @return AnamnesisCheckProxy
     */
    private AnamnesisCheckProxy getSelectedFilterTitle() {
        for (AnamnesisCheckProxy checkId : anamnesisCheck) {
            if (view.getFilterTitle().getSelectedIndex() != -1
                    && view.getFilterTitle()
                            .getItemText(
                                    view.getFilterTitle().getSelectedIndex())
                            .equals(checkId.getText())) {
                return checkId;
            }
        }
        return null;
    }

    private List<AnamnesisCheckProxy> anamnesisCheck = new ArrayList<AnamnesisCheckProxy>();

    /**
     * set Fileter Title ListBox install Value
     *
     * @param type
     *
     */
    public void anamnesisCheckTitleList(AnamnesisCheckTypes type) {

        fireTitleValueRequest(type, new Receiver<List<AnamnesisCheckProxy>>() {

            @Override
            public void onSuccess(List<AnamnesisCheckProxy> response) {
                anamnesisCheck = response;

                view.getFilterTitle().addItem(constants.filterTitle(),
                        constants.filterTitle());
                view.getFilterTitle().setSelectedIndex(0);

                int idx = 1;
                for (AnamnesisCheckProxy title : response) {
                    view.getFilterTitle().addItem(title.getText(),
                            String.valueOf(title.getId()));

                    if (place.getFilterTileId() != null
                            && place.getFilterTileId().equals(
                                    title.getId().toString())) {
                        view.getFilterTitle().setSelectedIndex(idx);
                    }

                    idx++;
                }

                setStartAndVisiableRange(0, null);
            }

        });

    }

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

    // @SuppressWarnings("deprecation")
    // @Override
    // public void saveOrder() {
    // for (final Entry<Long, Integer> entry :
    // place.getOrderEditedMap().entrySet()) {
    // GWT.log(">>>>>>>>orderEditedMap size = " +
    // place.getOrderEditedMap().size());
    // GWT.log("?????????" + entry.getKey() + "--->" + entry.getValue());
    // // if(entry.getKey() != null &&entry.getValue() !=null &&
    // // !entry.getValue().equals("")){
    // requests.anamnesisCheckRequest().findAnamnesisCheck(entry.getKey()).fire(new
    // Receiver<AnamnesisCheckProxy>() {
    // public void onFailure(ServerFailure error) {
    // GWT.log("findAnamnesisCheck error = " + error);
    // }
    //
    // @Override
    // public void onSuccess(AnamnesisCheckProxy response) {
    // GWT.log("response = " + response);
    // if (response != null) {
    // requests.anamnesisCheckRequestNonRoo().normalizeOrder(entry.getValue()).using(response).fire(new
    // Receiver<Void>() {
    // public void onFailure(ServerFailure error) {
    // GWT.log("normalizeOrder error = " + error);
    // }
    //
    // @Override
    // public void onSuccess(Void response) {
    // // TODO Auto-generated method stub
    //
    // }
    // });
    // }
    // }
    // });
    // // }
    //
    // }

    // requests.anamnesisCheckRequestNonRoo().normalizeOrder(orderEditedMap).fire(new
    // Receiver<Void>() {
    //
    // @Override
    // public void onSuccess(Void response) {
    // // TODO Auto-generated method stub
    //
    // }
    // });

    // @Override
    // public void resetUserSpecifiedOrder(AnamnesisCheckProxy
    // selectedAnamnesisCheck, String value) {
    // try {
    // Integer userSpecifiedOrder = Integer.valueOf(value);
    // if( tableData!=null ){
    // for(AnamnesisCheckProxy anamnesisCheckProxy : tableData){
    // if(anamnesisCheckProxy.getId() == selectedAnamnesisCheck.getId()){
    // anamnesisCheckProxy.setUserSpecifiedOrder(userSpecifiedOrder);
    // }
    // }
    // }
    // } catch (Exception e) {
    // GWT.log("resetUserSpecifiedOrder Exception = "+e);
    // e.printStackTrace();
    // }
    //
    // test();
    // }

    // @Override
    // public void orderEdited(AnamnesisCheckProxy proxy, String
    // userSpecifiedOrderStr) {
    // // TODO
    // if (proxy != null && userSpecifiedOrderStr != null) {
    // try {
    // Integer userSpecifiedOrder = Integer.valueOf(userSpecifiedOrderStr);
    // boolean isExist = false;
    //
    // for (AnamnesisCheckProxy edited : orderEdited) {
    // GWT.log("*******edited.getId() = " + edited.getId());
    // GWT.log("*******proxy.getId() = " + proxy.getId());
    // if (edited.getId() == proxy.getId()) {
    // GWT.log("*******proxy isExist");
    // isExist = true;
    // edited.setUserSpecifiedOrder(userSpecifiedOrder);
    // break;
    // }
    // }
    // if (isExist == false) {
    // GWT.log("*******proxy not isExist");
    // edituserSpecifiedOrder(proxy,userSpecifiedOrder);
    // proxy.setUserSpecifiedOrder(userSpecifiedOrder);
    // orderEdited.add(proxy);
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }

    // @Override
    // public void orderEdited(AnamnesisCheckProxy proxy, String
    // userSpecifiedOrderStr){
    // GWT.log("??????????????? proxy= "+proxy.getId());
    // //TODO
    // try {
    // Integer userSpecifiedOrder = Integer.valueOf(userSpecifiedOrderStr);
    // boolean isExist = false;
    // for(Entry<Long, Integer> entry : place.getOrderEditedMap().entrySet()){
    // GWT.log("?????????"+entry.getKey()+"--->"+entry.getValue());
    // if(entry.getKey()==proxy.getId()){
    // GWT.log("*******proxy isExist");
    // isExist = true;
    // entry.setValue(userSpecifiedOrder);
    // }
    // }
    // if (isExist == false) {
    // GWT.log("*******proxy not isExist");
    // place.getOrderEditedMap().put(proxy.getId(),
    // Integer.valueOf(userSpecifiedOrder));
    // GWT.log("orderEditedMap.put");
    // }
    //
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // }

    @Override
    public void orderEdited(AnamnesisCheckProxy proxy,
            String userSpecifiedOrderStr) {
        AnamnesisCheckRequest req = getRequest();
        AnamnesisCheckProxy editableProxy = req.edit(proxy);
        try {
            editableProxy.setUserSpecifiedOrder(Integer
                    .valueOf(userSpecifiedOrderStr));
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



        getRequest().fire(new Receiver(){

            @Override
            public void onSuccess(Object response) {
                requests.anamnesisCheckRequestNonRoo().normalizeOrder().fire(new Receiver(){

                    @Override
                    public void onSuccess(Object response) {
                        init();
                    }
                });


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

}
