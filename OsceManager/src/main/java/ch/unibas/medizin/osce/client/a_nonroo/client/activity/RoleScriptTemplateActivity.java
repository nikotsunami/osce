package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleScriptTemplateDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleScriptTemplateViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.Sorting;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.scaffold.RoleTemplateRequestNonRoo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class RoleScriptTemplateActivity extends AbstractActivity implements
		RoleScriptTemplateView.Presenter, RoleScriptTemplateView.Delegate {

	/** Holds the applications request factory */
	private OsMaRequestFactory requests;
	/** Holds the applications placeController */
	private PlaceController placeController;
	/** Holds the applications' activityManager */
	private ActivityManager activityManger;
	/** Holds the panel in which the view will be displayed */
	private AcceptsOneWidget widget;
	/** Holds the main view managed by this activity */
	private RoleScriptTemplateView view;
	/** Holds this activities' activityMapper */
	private RoleScriptTemplateDetailsActivityMapper RoleScriptTemplateDetailsActivityMapper;
	private HandlerRegistration rangeChangeHandler;
	
	//cell table changes
	private AdvanceCellTable<RoleTemplateProxy> table;
	int x;
	int y;
	//private CellTable<RoleTemplateProxy> table;
	private SingleSelectionModel<RoleTemplateProxy> selectionModel;
	private RoleTemplateRequestNonRoo requestAdvSeaCritStd;
	private List<RoleTemplateProxy> searchCriteria = new ArrayList<RoleTemplateProxy>();

	/** List of fields that should be searched for the quickSearchTerm */
	private List<String> searchThrough = Arrays.asList("templateName");
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	// Tooltip components initialisation
	private PopupPanel toolTip;
	private HorizontalPanel toolTipContentPanel;
	private TextBox toolTipLabel;
	private IconButton toolTipChange;

	public Sorting sortorder = Sorting.ASC;
	public String sortname = "templateName";
	private String quickSearchTerm = "";

	/**
	 * go to another place
	 * 
	 * @param place
	 *            the place to go to
	 */
	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	/**
	 * Sets the dependencies of this activity and initializes the corresponding
	 * activity manager
	 * 
	 * @param requests
	 *            The request factory to use
	 * @param placeController
	 *            the place controller to use
	 */
	public RoleScriptTemplateActivity(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
		RoleScriptTemplateDetailsActivityMapper = new RoleScriptTemplateDetailsActivityMapper(
				requests, placeController);
		this.activityManger = new ActivityManager(
				RoleScriptTemplateDetailsActivityMapper, requests.getEventBus());
	}

	/**
	 * Clean up activity on finish (close popups and disable display of current
	 * activities view)
	 */
	public void onStop() {

		activityManger.setDisplay(null);
	}

	public void registerLoading() {
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
						Log.info(" ApplicationLoadingScreenEvent onEventReceived Called");
					event.display();
					}
				});
	}
	/**
	 * Initializes the corresponding views and initializes the tables as well as
	 * their corresponding handlers.
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleScriptTemplateActivity.start()");
		// By SPEC[Start
		// StandardizedPatientView systemStartView = new
		// StandardizedPatientViewImpl();
		final RoleScriptTemplateView roleScriptTemplateView = new RoleScriptTemplateViewImpl();
		// By SPEC] End
		roleScriptTemplateView.setPresenter(this);

		this.widget = panel;
		this.view = roleScriptTemplateView;
		widget.setWidget(roleScriptTemplateView.asWidget());

		//by spec
		RecordChangeEvent.register(requests.getEventBus(), (RoleScriptTemplateViewImpl)view);
		//by spec
		
		MenuClickEvent.register(requests.getEventBus(), (RoleScriptTemplateViewImpl)view);
		
		// widget.setWidget(systemStartView.asWidget());
		setTable(view.getTable());

		

		table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				RoleScriptTemplateActivity.this.onRangeChanged();
			}
		});

		/*table.addColumnSortHandler(new ColumnSortEvent.Handler() {
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				// By SPEC[Start
				Column<RoleTemplateProxy, String> col = (Column<RoleTemplateProxy, String>) event
						.getColumn();
				int index = table.getColumnIndex(col);
				String[] path = roleScriptTemplateView.getPathsString();
				sortname = path[index];
				sortorder = (event.isSortAscending()) ? Sorting.ASC
						: Sorting.DESC;
				// By SPEC]end
				RoleScriptTemplateActivity.this.onRangeChanged();
			}
		});*/

		//celltable changes start
		table.addHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				// TODO Auto-generated method stub
				Log.info("mouse down");
				
				if(table.getRowCount()>0)
				{
				Log.info(table.getRowElement(0).getAbsoluteTop() + "--"+ event.getClientY());

				x = event.getClientX();
				y = event.getClientY();

				if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT&& event.getClientY() < table.getRowElement(0).getAbsoluteTop()) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

					Log.info("right event");
				}
				}
			}
		}, MouseDownEvent.getType());
		
		
		table.getPopup().addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				// TODO Auto-generated method stub
				//addColumnOnMouseout();
				table.getPopup().hide();
				
			}
		}, MouseOutEvent.getType());
		
		
		
		table.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				// By SPEC[Start

				Column<RoleTemplateProxy, String> col = (Column<RoleTemplateProxy, String>) event.getColumn();
				
				//(RoleScriptTemplateViewImpl)view
				int index = table.getColumnIndex(col);
				
				/*String[] path =	systemStartView.getPaths();	            			
				sortname = path[index];
				sortorder=(event.isSortAscending())?Sorting.ASC:Sorting.DESC;	*/
				
				if (index % 2 == 1 ) {
					
					table.getPopup().setPopupPosition(x, y);
					table.getPopup().show();

				} else {
					// path = systemStartView.getPaths();
					//String[] path =	systemStartView.getPaths();	
					Log.info("index value--"+index +((RoleScriptTemplateViewImpl)view).getPaths().get(index)+"--"+((RoleScriptTemplateViewImpl)view).getPaths().get(index));
					sortname = ((RoleScriptTemplateViewImpl)view).getPaths().get(index);
					sortorder = (event.isSortAscending()) ? Sorting.ASC: Sorting.DESC;
					// By SPEC]end
					// RoleActivity.this.init2("");
					Log.info("Call Init Search from addColumnSortHandler");
					// filter.hide();
					RoleScriptTemplateActivity.this.onRangeChanged();
					
				}
			}
		});

		
	//	init();
//cell table changes
		initSearch();

		// Inherit the view's key provider
		ProvidesKey<RoleTemplateProxy> keyProvider = ((AbstractHasData<RoleTemplateProxy>) table)
				.getKeyProvider();
		selectionModel = new SingleSelectionModel<RoleTemplateProxy>(
				keyProvider);
		table.setSelectionModel(selectionModel);

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						RoleTemplateProxy selectedObject = selectionModel
								.getSelectedObject();
						if (selectedObject != null) {
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
							view.setDetailPanel(true);
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
							Log.debug(selectedObject.getTemplateName()
									+ " selected!");
							showDetails(selectedObject);
						}
						else{
							view.setDetailPanel(false);
						}
					}
				});

		view.setDelegate(this);

		initSearch();

		activityManger.setDisplay(view.getDetailsPanel());

		view.setDelegate(this);

	}

	private void init() {
		init2("");
	}

	private void init2(final String q) {

		// fix to avoid having multiple rangeChangeHandlers attached
		if (rangeChangeHandler != null) {
			rangeChangeHandler.removeHandler();
		}

		fireCountRequest(q, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Narben aus der Datenbank: " + response);
				view.getTable().setRowCount(response.intValue(), true);

				onRangeChanged(q);
			}
		});

		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						RoleScriptTemplateActivity.this.onRangeChanged(q);
					}
				});
	}

	protected void fireCountRequest(String name, Receiver<Long> callback) {
		// requests.scarRequest().countScars().fire(callback);
		requests.roleTemplateRequestNonRoo()
				.countRoleTemplateByName(quickSearchTerm, searchThrough)
				.fire(callback);
	}

	/**
	 * Receiver for the standardized patients that met the search criteria. If
	 * execution was successful, the table will be filled with the patients.
	 */
	@SuppressWarnings("deprecation")
	private class RoleTemplateReceiver extends
			Receiver<List<RoleTemplateProxy>> {
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
			// onStop();
		}

		public void onViolation(Set<Violation> errors) {
			Iterator<Violation> iter = errors.iterator();
			String message = "";
			while (iter.hasNext()) {
				Violation it = iter.next();
				message += "message " + it.getMessage() + "\n";
				message += "path " + it.getPath() + "\n";
				message += "class " + it.getClass() + "\n";
				message += "INV " + it.getInvalidProxy() + "\n";
				message += "OR " + it.getOriginalProxy() + "\n";
				message += "ID " + it.getProxyId() + "<br>";
			}
			Log.warn(" in Simpat -" + message);
			// onStop();
		}

		@Override
		public void onSuccess(List<RoleTemplateProxy> response) {
			final Range range = table.getVisibleRange();
			// By SPEC[
			table.setRowCount(response.size());
			// By SPEC]
			table.setRowData(range.getStart(), response);

		}
	}

	protected void onRangeChanged(String q) {
		final Range range = table.getVisibleRange();

		final Receiver<List<RoleTemplateProxy>> callback = new Receiver<List<RoleTemplateProxy>>() {
			@Override
			public void onSuccess(List<RoleTemplateProxy> values) {
				if (view == null) {
					// This activity is dead
					return;
				}
				table.setRowData(range.getStart(), values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(q, range, callback);
	}

	private void fireRangeRequest(String name, final Range range,
			final Receiver<List<RoleTemplateProxy>> callback) {
		createRangeRequest(name, range).with(view.getPathsString()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<RoleTemplateProxy>> createRangeRequest(String name,
			Range range) {
		// return requests.scarRequest().findScarEntries(range.getStart(),
		// range.getLength());
		return requests.roleTemplateRequestNonRoo().findRoleTemplateByName(
				sortname, sortorder, quickSearchTerm, searchThrough,
				range.getStart(), range.getLength());
	}

	private void setTable(CellTable<RoleTemplateProxy> table) {
		/*this.table = table;*/
		this.table = (AdvanceCellTable<RoleTemplateProxy>)table;
		//cell table changes
	}

	// @SuppressWarnings({ "deprecation" })
	// protected void onRangeChanged() {
	// // TODO: some bug about request
	// requestAdvSeaCritStd = requests.roleTemplateRequestNonRoo();
	//
	// for (RoleTemplateProxy criterion : searchCriteria) {
	// Log.info("Criterion: " + criterion.getTemplateName().toString());
	// }
	//
	// Range range = table.getVisibleRange();
	// Log.debug(range.getStart() + ": start : length " + range.getLength());
	//
	// //By SPEC[Start
	// //requestAdvSeaCritStd.findPatientsByAdvancedSearchAndSort("name",
	// Sorting.ASC, quickSearchTerm,
	// //searchThrough, searchCriteria /*fields, bindType, comparations, values
	// */).
	// //fire(new StandardizedPatientReceiver());
	//
	// requestAdvSeaCritStd.findRoleTemplateByName(sortname, sortorder ,
	// quickSearchTerm,
	// searchThrough, range.getStart(), range.getLength() /*fields, bindType,
	// comparations, values */).
	// fire(new RoleTemplateReceiver());
	// //By SPEC]End
	// }

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final Receiver<List<RoleTemplateProxy>> callback = new Receiver<List<RoleTemplateProxy>>() {

			@Override
			public void onSuccess(List<RoleTemplateProxy> response) {
				if (view == null) {
					return;
				}
				Log.info("Successfully RoleTemplate result value set in table");
				table.setRowData(range.getStart(), response);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}

		};

		fireRangeRequest(range, callback);
	}

	private void fireRangeRequest(final Range range,
			final Receiver<List<RoleTemplateProxy>> callback) {
		Log.info("Inside fireRangeRequest()");
		createRangeRequest(range).with(view.getPathsString()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<RoleTemplateProxy>> createRangeRequest(Range range) {
		System.out.println("Calling FindAllSpecialization with value : ");
		return requests.roleTemplateRequestNonRoo().findAllTemplateName(
				sortname, sortorder, quickSearchTerm, range.getStart(),
				range.getLength());
	}

	/**
	 * Shows the details place for a given StandardizedPatientProxy
	 * 
	 * @param StandardizedPatient
	 *            the patient of whom details should be displayed
	 */
	protected void showDetails(RoleTemplateProxy roleTemplate) {
		Log.debug(roleTemplate.getTemplateName());
		goTo(new RoleScriptTemplateDetailsPlace(roleTemplate.stableId(),
				Operation.DETAILS));
	}

	/**
	 * Initializes the search for standardized patients, by first executing a
	 * count request. Execution is then continued in
	 * StandardizedPatientCountReceiver
	 */

	@SuppressWarnings("deprecation")
	private void initSearch() {

		requestAdvSeaCritStd = requests.roleTemplateRequestNonRoo();

		Range range = table.getVisibleRange();

		requestAdvSeaCritStd.countRoleTemplateByName(quickSearchTerm,
				searchThrough).fire(new RoleTemplateCountReceiver());

	}

	private class RoleTemplateCountReceiver extends Receiver<Long> {
		@Override
		public void onSuccess(Long response) {
			if (view == null) {
				// This activity is dead
				return;
			}
			Log.debug("Geholte Patienten aus der Datenbank: " + response);
			view.getTable().setRowCount(response.intValue(), true);
			onRangeChanged();
		}
	}

	@Override
	public void goToDetailClicked() {
		goTo(new RoleScriptTemplateDetailsPlace(Operation.DETAILS));
	}

	@Override
	public void performSearch(String q) {
		quickSearchTerm = q;
		Log.debug("Search for " + q);
		// init2(q);
		initSearch();
	}

	@Override
	public void deleteClicked(RoleTemplateProxy roleTemplate) {
		requests.roleTemplateRequest().remove().using(roleTemplate)
				.fire(new Receiver<Void>() {
					public void onSuccess(Void ignore) {
						Log.debug("Sucessfully deleted");
						init();
					}
				});
	}

// Issue Role Module
	@Override
	public void editClicked(final RoleTemplateProxy roleTemplate,int left,int top) {
	// E: Issue Role Module

//	@Override
//	public void editClicked(final RoleTemplateProxy roleTemplate) {

		// ToolTip Function Added by spec
		Log.info("ToolTip opened");
		toolTip = new PopupPanel(true);

		toolTip.setWidth("180px");
		toolTip.setHeight("40px");
		toolTip.setAnimationEnabled(true);

		toolTipContentPanel = new HorizontalPanel();

		toolTipContentPanel.setWidth("160px");
		toolTipContentPanel.setHeight("22px");
		toolTipContentPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		toolTipContentPanel
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		toolTipLabel = new TextBox();

		toolTipLabel.setWidth("120px");
		toolTipLabel.setHeight("25px");

		toolTipChange = new IconButton(constants.save());
		toolTipChange.setIcon("disk");

		toolTipChange.setWidth("40px");
		toolTipChange.setHeight("25px");

		toolTipContentPanel.add(toolTipLabel);
		toolTipContentPanel.add(toolTipChange);

		toolTipLabel.setText(roleTemplate.getTemplateName());

		toolTip.add(toolTipContentPanel); // you can add any widget here

		table.getColumnIndex(table.getColumn(1));

	//	toolTip.setPopupPosition(new Integer(constants.TopicsAndSpecViewPopupXPosition()),new Integer(constants.TopicsAndSpecViewPopupYPosition()));
// Issue Role Module
		toolTip.setPopupPosition(left,top);
		// Issue Role Module

		toolTip.show();
		
		// Violation Changes Highlight
				final Map<String, Widget> popupMap=new HashMap<String, Widget>();
				popupMap.put("templateName", toolTipLabel);
				// E Violation Changes Highlight

		toolTipChange.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				requests.roleTemplateRequest()
						.findRoleTemplate(roleTemplate.getId())
						.fire(new Receiver<RoleTemplateProxy>() {

							@Override
							public void onSuccess(RoleTemplateProxy response) 
							{
								RoleTemplateRequest spReq = requests.roleTemplateRequest();
								response = spReq.edit(response);
								response.setTemplateName(toolTipLabel.getText());
								response.setDate_edited(new Date());
								// Violation Changes Highlight
								spReq.persist().using(roleTemplate).fire(new OSCEReceiver<Void>(popupMap) 
								{
								// E Violation Changes Highlight
								/*spReq.persist().using(roleTemplate).fire(new Receiver<Void>() 
								{*/
											@Override
											public void onFailure(
													ServerFailure error) {
												Log.error("onFilure");
												Log.error(error.getMessage());
											}

											@Override
											public void onSuccess(Void arg0) {
												Log.info("Save RoleTemplate values value Succesfully according to ToolTip value");
												toolTip.clear();
												toolTip.hide();
												init();

											}
										});
							}
						});

			}

		});

	}

	@Override
	public void newClicked(String name) {
		Log.debug("Add Role Template");
		RoleTemplateRequest roletempreq = requests.roleTemplateRequest();
		RoleTemplateProxy roleTemplate = roletempreq
				.create(RoleTemplateProxy.class);
		// reques.edit(scar);

		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		// Date date = new Date();
		roleTemplate.setTemplateName(name);
		roleTemplate.setDate_cretaed(new Date());
		roleTemplate.setDate_edited(new Date());

		// Violation Changes Highlight
		Log.info("Map Size: " + view.getAadTemplateMap().size());
		roletempreq.persist().using(roleTemplate).fire(new OSCEReceiver<Void>(view.getAadTemplateMap()) {
		// E Violation Changes Highlight
		@Override
		public void onSuccess(Void arg0) {
			init();
			}
		});
	}

}
