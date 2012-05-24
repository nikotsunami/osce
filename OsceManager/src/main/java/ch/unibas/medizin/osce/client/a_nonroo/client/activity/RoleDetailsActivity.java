package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleFileSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleFileSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsViewImpl;
import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.client.managed.request.FileRequest;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialRequest;
import ch.unibas.medizin.osce.client.managed.ui.MaterialListProxyRenderer;
import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;
import ch.unibas.medizin.osce.shared.Operation;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author dk
 * 
 */
public class RoleDetailsActivity extends AbstractActivity implements
		RoleDetailsView.Presenter, RoleDetailsView.Delegate,
		StandardizedRoleDetailsView.Delegate, RoleFileSubView.Delegate,
		RoleFileSubView.Presenter, RoomMaterialsDetailsSubView.Delegate,
		RoomMaterialsDetailsSubView.Presenter

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

	private static int selecTab = 0;

	// Assignment :H[
	private RoleFileSubView[] fileView;
	public CellTable<FileProxy> fileTable[];
	private SingleSelectionModel<FileProxy> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private int sortOrder;

	// Assignment :G
	private CellTable<UsedMaterialProxy>[] usedMaterialTable;
	private RoomMaterialsDetailsSubView[] roomMaterialsDetailsSubView;

	private HandlerRegistration rangeUsedMaterialTableChangeHandler;
	private HandlerRegistration selectionUsedMaterialTableChangeHandler;

	private SingleSelectionModel<UsedMaterialProxy> selectionUsedMaterialModel;

	// ]End

	public static int getSelecTab() {
		return selecTab;
	}

	public static void setSelecTab(int selecTab) {
		RoleDetailsActivity.selecTab = selecTab;
	}

	public RoleDetailsActivity(RoleDetailsPlace place,
			OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		this.placeController = placeController;

	}

	public void onStop() {
		selecTab = 0;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleDetailsActivity.start()");
		RoleDetailsView roleDetailsView = new RoleDetailsViewImpl();
		roleDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = roleDetailsView;
		this.roleDetailActivity = this;

		roleDetailTabPanel = view.getRoleDetailTabPanel();
		widget.setWidget(roleDetailsView.asWidget());
		view.setDelegate(this);

		requests.find(place.getProxyId()).with("standardizedRoles")
				.fire(new InitializeActivityReceiver());

	}

	// Assignment H[
	// For File
	private void init(long StandardizedRoleID, int index) {
		init2(StandardizedRoleID, index);
	}

	private void init2(final long StandardizedRoleID, final int index) {

		// fix to avoid having multiple rangeChangeHandlers attached
		if (rangeChangeHandler != null) {
			rangeChangeHandler.removeHandler();
		}

		fireCountRequest(StandardizedRoleID, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (fileView == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Narben aus der Datenbank: " + response);
				fileView[index].getTable().setRowCount(response.intValue(),
						true);

				onRangeChanged(StandardizedRoleID, index);
			}
		});

		rangeChangeHandler = fileTable[index]
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						RoleDetailsActivity.this.onRangeChanged(
								StandardizedRoleID, index);
					}
				});
	}

	protected void onRangeChanged(long StandardizedRoleID, final int index) {
		final Range range = fileTable[index].getVisibleRange();

		final Receiver<List<FileProxy>> callback = new Receiver<List<FileProxy>>() {
			@Override
			public void onSuccess(List<FileProxy> values) {
				if (fileView == null) {
					// This activity is dead
					return;
				}
				fileTable[index].setRowData(range.getStart(), values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequest(StandardizedRoleID, range, callback, index);
	}

	private void fireRangeRequest(long StandardizedRoleID, final Range range,
			final Receiver<List<FileProxy>> callback, int index) {
		createRangeRequest(StandardizedRoleID, range).with(
				fileView[index].getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<FileProxy>> createRangeRequest(
			long StandardizedRoleID, Range range) {
		// return requests.scarRequest().findScarEntries(range.getStart(),
		// range.getLength());
		return requests.fileRequestNooRoo()
				.findFileEntriesByStandardizedRoleID(StandardizedRoleID,
						range.getStart(), range.getLength());
	}

	protected void fireCountRequest(long StandardizedRoleID,
			Receiver<Long> callback) {
		// requests.scarRequest().countScars().fire(callback);
		requests.fileRequestNooRoo()
				.countFilesByStandardizedRoleID(StandardizedRoleID)
				.fire(callback);
	}

	private void setFileTable(CellTable<FileProxy> table,
			long StandardizedRoleID, int index) {
		this.fileTable[index] = table;
		init(StandardizedRoleID, index);
	}

	@Override
	public void fileMoveUp(FileProxy proxy,
			final StandardizedRoleProxy standRoleProxy) {
		// TODO Auto-generated method stub
		requests.fileRequestNooRoo().fileMoveUp(standRoleProxy.getId()).using(proxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.info("moved");
						init(standRoleProxy.getId(), roleDetailTabPanel
								.getTabBar().getSelectedTab());

					}
				});

	}

	@Override
	public void fileMoveDown(FileProxy proxy,
			final StandardizedRoleProxy standRoleProxy) {
		// TODO Auto-generated method stub
		requests.fileRequestNooRoo().fileMoveDown(standRoleProxy.getId()).using(proxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.info("moved");
						init(standRoleProxy.getId(), roleDetailTabPanel
								.getTabBar().getSelectedTab());
					}
				});
	}

	@Override
	public void fileDeleteClicked(FileProxy proxy,
			final StandardizedRoleProxy standRoleProxy) {
		// TODO Auto-generated method stub
		requests.fileRequest().remove().using(proxy).fire(new Receiver<Void>() {
			public void onSuccess(Void ignore) {
				Log.debug("Sucessfully deleted");
				init(standRoleProxy.getId(), roleDetailTabPanel.getTabBar()
						.getSelectedTab());
			}
		});

	}

	@Override
	public void changeFilterTitleShown(String selectedTitle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newFileClicked(String fileName, String fileDescription,
			final StandardizedRoleProxy proxy) {
		// TODO Auto-generated method stub

		if (fileName != null) {
			sortOrder = fileTable[roleDetailTabPanel.getTabBar()
					.getSelectedTab()].getRowCount() + 1;
			Log.debug("Add File");

			FileRequest fileReq = requests.fileRequest();
			FileProxy file = fileReq.create(FileProxy.class);
			// reques.edit(scar);
			file.setPath(fileName);
			file.setDescription(fileDescription);
			file.setSortOrder(sortOrder);
			file.setStandardizedRole(proxy);

			fileReq.persist().using(file).fire(new Receiver<Void>() {
				@Override
				public void onSuccess(Void arg0) {
					init(proxy.getId(), roleDetailTabPanel.getTabBar()
							.getSelectedTab());
				}
			});
		}
	}

	// ]Assignment H

	// Assignment G[
	// For UsedMaterial

	private void initUsedMaterial2(final long standardizedRoleID,
			final int index) {

		// fix to avoid having multiple rangeChangeHandlers attached
		if (rangeUsedMaterialTableChangeHandler != null) {
			rangeUsedMaterialTableChangeHandler.removeHandler();
		}
		Log.info("standardizedRoleID:" + standardizedRoleID);
		fireUsedMaterialCountRequest(standardizedRoleID, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Narben aus der Datenbank: " + response);
				roomMaterialsDetailsSubView[index].getUsedMaterialTable()
						.setRowCount(response.intValue(), true);

				onRangeChangedUsedMaterialTable(standardizedRoleID, index);
			}
		});

		rangeUsedMaterialTableChangeHandler = usedMaterialTable[index]
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						RoleDetailsActivity.this
								.onRangeChangedUsedMaterialTable(
										standardizedRoleID, index);
					}
				});
	}

	private void setUsedMaterialTable(CellTable<UsedMaterialProxy> table,
			long standardizedRoleID, int index) {
		this.usedMaterialTable[index] = table;
		Log.info("standardizedRoleID:" + standardizedRoleID);
		initUsedMaterial2(standardizedRoleID, index);
	}

	@Override
	public void newUsedMaterialButtonClicked(Integer materialCount,
			MaterialUsedFromTypes used_from,
			final StandardizedRoleProxy standardizedRoleProxy,
			MaterialListProxy materialList) {
		UsedMaterialRequest usedMaterialRequest = requests
				.usedMaterialRequest();

		UsedMaterialProxy usedMaterialProxy = usedMaterialRequest
				.create(UsedMaterialProxy.class);
		usedMaterialProxy.setMaterialCount(materialCount);

		usedMaterialProxy.setUsed_from(used_from);

		usedMaterialProxy.setStandardizedRole(standardizedRoleProxy);
		usedMaterialProxy.setMaterialList(materialList);
		usedMaterialProxy.setSort_order(usedMaterialTable[roleDetailTabPanel
				.getTabBar().getSelectedTab()].getRowCount() + 1);

		Log.debug("Add UsedMaterial (" + usedMaterialProxy.getMaterialCount()
				+ " - id " + usedMaterialProxy.getId() + " "
				+ usedMaterialProxy.getMaterialList().getName()
				+ usedMaterialProxy.getStandardizedRole().getShortName());

		usedMaterialRequest.persist().using(usedMaterialProxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.debug("UsedMaterialReceiver Added successfully");
						initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());

					}
				});
	}

	public void setUsedMaterialTableRowCount(int size, int index) {
		roomMaterialsDetailsSubView[index].getUsedMaterialTable().setRowCount(
				size, true);
	}

	protected void onRangeChangedUsedMaterialTable(long standardizedRoleID,
			final int index) {
		final Range range = usedMaterialTable[index].getVisibleRange();

		final Receiver<List<UsedMaterialProxy>> callback = new Receiver<List<UsedMaterialProxy>>() {
			@Override
			public void onSuccess(List<UsedMaterialProxy> values) {
				if (roomMaterialsDetailsSubView == null) {
					return;
				}
				usedMaterialTable[index].setRowData(range.getStart(), values);

				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireUsedMaterialRangeRequest(standardizedRoleID, range, callback, index);
	}

	private void fireUsedMaterialRangeRequest(long standardizedRoleID,
			final Range range,
			final Receiver<List<UsedMaterialProxy>> callback, int index) {
		createUsedMaterialRangeRequest(standardizedRoleID, range).with(
				roomMaterialsDetailsSubView[index].getPaths()).fire(callback);

	}

	protected Request<List<UsedMaterialProxy>> createUsedMaterialRangeRequest(
			long standardizedRoleID, Range range) {
		return requests.usedMaterialRequestNonRoo()
				.findUsedMaterialsByStandardizedRoleID(standardizedRoleID,
						range.getStart(), range.getLength());
	}

	protected void fireUsedMaterialCountRequest(long standardizedRoleID,
			Receiver<Long> callback) {

		requests.usedMaterialRequestNonRoo()
				.countUsedMaterialsByStandardizedRoleID(standardizedRoleID)
				.fire(callback);
	}

	@Override
	public void moveUsedMaterialDown(UsedMaterialProxy proxy,
			final StandardizedRoleProxy standardizedRoleProxy) {
		requests.usedMaterialRequestNonRoo().moveMaterialDown(standardizedRoleProxy.getId()).using(proxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.info("moved UsedMaterial");
						initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
					}
				});

	}

	@Override
	public void moveUsedMaterialUp(UsedMaterialProxy proxy,
			final StandardizedRoleProxy standardizedRoleProxy) {
		requests.usedMaterialRequestNonRoo().moveMaterialUp(standardizedRoleProxy.getId()).using(proxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.info("moved UsedMaterial");
						initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
					}
				});
	}

	@Override
	public void deleteUsedFromClicked(UsedMaterialProxy proxy,
			final StandardizedRoleProxy standardizedRoleProxy) {

		requests.usedMaterialRequest().remove().using(proxy)
				.fire(new Receiver<Void>() {
					public void onSuccess(Void ignore) {
						Log.debug("Sucessfully deleted");
						initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
					}
				});
	}

	// ]Assignment G

	public class InitializeActivityReceiver extends Receiver<Object> {
		@Override
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
		}

		@Override
		public void onSuccess(Object response) {
			System.out
					.println("==================================Call onSuccess Method==================================");

			// Remove InAcive Roles from the role topic
			/*
			 * if (response instanceof RoleTopicProxy) {
			 * if(((RoleTopicProxy)response).getStandardizedRoles()!=null) {
			 * 
			 * 
			 * Iterator<StandardizedRoleProxy>
			 * iterator=((RoleTopicProxy)response
			 * ).getStandardizedRoles().iterator();
			 * 
			 * while(iterator.hasNext()) { StandardizedRoleProxy
			 * srp=iterator.next(); if(srp.getActive()==false) {
			 * ((RoleTopicProxy)response).getStandardizedRoles().remove(srp); }
			 * } } }
			 */
			if (response instanceof RoleTopicProxy) {
				if (((RoleTopicProxy) response).getStandardizedRoles() != null) {
					System.out.println("Success Role Found");
					System.out.println("Size: "
							+ ((RoleTopicProxy) response)
									.getStandardizedRoles().size()); // Return
																		// Size
																		// of
																		// Data

					int index = 0; // Array Object bIndex (SET VALUE TO OBJECT)
					// int objcnt=-1; // Total Objects (INITIALIZE THE OBJECT)
					int totalrole = roleDetailTabPanel.getWidgetCount(); // Total
																			// Role
																			// Tabs
					int size = ((RoleTopicProxy) response)
							.getStandardizedRoles().size(); // Total Size of
															// Data
															// (standardized_role)
					System.out.println("Total Role: " + totalrole);
					// roleDetailTabPanel.insert(standardizedRoleDetailsView,"Test Tab",
					// totalTab-1);
					standardizedRoleDetailsView = new StandardizedRoleDetailsViewImpl[size];
					// Assignment :H[
					fileView = new RoleFileSubViewImpl[size];
					fileTable = new CellTable[size];
					// Assignment :G[
					roomMaterialsDetailsSubView = new RoomMaterialsDetailsSubViewImpl[size];
					usedMaterialTable = new CellTable[size];
					// ]End

					stRoleIterator = ((RoleTopicProxy) response)
							.getStandardizedRoles().iterator();
					while (stRoleIterator.hasNext()) {

						standardizedRoleDetailsView[index] = new StandardizedRoleDetailsViewImpl();
						StandardizedRoleProxy proxy = stRoleIterator.next();
						standardizedRoleDetailsView[index].setValue(proxy); // nalim

						roleDetailTabPanel.insert(
								standardizedRoleDetailsView[index],
								"" + proxy.getShortName(), index);
						standardizedRoleDetailsView[index].shortName
								.setInnerText(proxy.getShortName() == null ? ""
										: String.valueOf(proxy.getShortName()));
						standardizedRoleDetailsView[index].longName
								.setInnerText(proxy.getLongName() == null ? ""
										: String.valueOf(proxy.getLongName()));
						standardizedRoleDetailsView[index].roleType
								.setInnerText(proxy.getRoleType().name()); // ADDED
						standardizedRoleDetailsView[index].studyYear
								.setInnerText(proxy.getStudyYear().name()); // ADDED
						standardizedRoleDetailsView[index].labelLongNameHeader
								.setText("" + proxy.getLongName());

						setRoleDetailTabData(proxy, response, index);
						standardizedRoleDetailsView[index].rolePanel
								.selectTab(0);
						standardizedRoleDetailsView[index]
								.setDelegate(roleDetailActivity);

						// Assignment :H[
						fileView[index] = standardizedRoleDetailsView[index]
								.getRoleFileSubViewImpl();
						fileView[index].setValue(proxy);
						fileView[index].setDelegate(roleDetailActivity);
						setFileTable(fileView[index].getTable(), proxy.getId(),
								index);
						ProvidesKey<FileProxy> keyProvider = ((AbstractHasData<FileProxy>) fileTable[index])
								.getKeyProvider();
						selectionModel = new SingleSelectionModel<FileProxy>(
								keyProvider);
						fileTable[index].setSelectionModel(selectionModel);

						selectionModel
								.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
									public void onSelectionChange(
											SelectionChangeEvent event) {
										FileProxy selectedObject = selectionModel
												.getSelectedObject();
										if (selectedObject != null) {
											Log.debug(selectedObject.getPath()
													+ " selected!");
											// showDetails(selectedObject);
										}
									}
								});

						// Assignment :G[
						roomMaterialsDetailsSubView[index] = standardizedRoleDetailsView[index]
								.getRoomMaterialsDetailsSubViewImpl();
						roomMaterialsDetailsSubView[index]
								.setDelegate(roleDetailActivity);

						roomMaterialsDetailsSubView[index].setValue(proxy);
						roomMaterialsDetailsSubView[index]
								.setMaterialListPickerValues(Collections
										.<MaterialListProxy> emptyList());
						final int index2 = index;

						requests.materialListRequest()
								.findMaterialListEntries(0, 50)
								.with(MaterialListProxyRenderer.instance()
										.getPaths())
								.fire(new Receiver<List<MaterialListProxy>>() {

									public void onSuccess(
											List<MaterialListProxy> response) {
										List<MaterialListProxy> values = new ArrayList<MaterialListProxy>();
										values.add(null);
										values.addAll(response);
										roomMaterialsDetailsSubView[index2]
												.setMaterialListPickerValues(values);
									}
								});

						setUsedMaterialTable(
								roomMaterialsDetailsSubView[index]
										.getUsedMaterialTable(),
								proxy.getId(), index);
						ProvidesKey<UsedMaterialProxy> keyProviderUsedMaterial = ((AbstractHasData<UsedMaterialProxy>) usedMaterialTable[index])
								.getKeyProvider();
						selectionUsedMaterialModel = new SingleSelectionModel<UsedMaterialProxy>(
								keyProviderUsedMaterial);
						usedMaterialTable[index]
								.setSelectionModel(selectionUsedMaterialModel);

						selectionUsedMaterialModel
								.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
									public void onSelectionChange(
											SelectionChangeEvent event) {
										UsedMaterialProxy selectedObject = selectionUsedMaterialModel
												.getSelectedObject();
										if (selectedObject != null) {
											Log.debug(selectedObject
													.getMaterialList()
													.getName()
													+ " selected!");
											// showDetails(selectedObject);
										}
									}
								});
						// ]End

						index++;

					}
					roleDetailTabPanel.selectTab(selecTab);
					view.setStandardizedRoleDetailsViewImpl((StandardizedRoleDetailsViewImpl[]) standardizedRoleDetailsView);

				} else {
					System.out.println("Sorry No Roles Aveilable");
				}

				init(((RoleTopicProxy) response));
			}
		}
	}

	public void setRoleDetailTabData(StandardizedRoleProxy proxy,
			Object response, int index) {
		System.out.println("===============================>" + "" + index
				+ proxy.getShortName());
		System.out.println("===============================>" + "" + index
				+ proxy.getLongName());
		System.out.println("===============================>" + "" + index
				+ proxy.getRoleType().valueOf(proxy.getRoleType().name()));
		System.out.println("===============================>" + "" + index
				+ proxy.getStudyYear().valueOf(proxy.getStudyYear().name()));
	}

	private void init(RoleTopicProxy proxy) {
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public void printRoleClicked() {
		Log.info("Print clicked");
		/*
		 * requests.standardizedPatientRequestNonRoo().getPdfPatientsBySearch(
		 * standardizedPatientProxy).fire(new
		 * StandardizedPatientPdfFileReceiver());
		 */
	}

	@Override
	public void editRoleClicked(StandardizedRoleProxy standardizedRoleProxy) {
		Log.info("edit clicked");
		System.out
				.println("============================Jump to StandardizedPatientDetailActivity editPatientClicked() =========================");
		System.out.println("==>"
				+ roleDetailTabPanel.getTabBar().getSelectedTab());
		int selTabID = roleDetailTabPanel.getTabBar().getSelectedTab();
		goTo(new RoleDetailsPlace(standardizedRoleProxy.stableId(),
				Operation.EDIT));
	}

	@Override
	public void deleteRoleClicked(StandardizedRoleProxy proxy) {
		Log.info("delete clicked");

		if (!Window
				.confirm("Really delete this entry? You cannot undo this change.")) {
			return;
		}

		requests.standardizedRoleRequest().remove().using(proxy)
				.fire(new Receiver<Void>() {
					public void onSuccess(Void ignore) {
						if (widget == null) {
							return;
						}
						RoleEditActivity.roleActivity.initSearch();
						goTo(new RoleDetailsPlace(RoleEditActivity.roleTopic
								.stableId(), Operation.DETAILS));

						// placeController.goTo(new
						// RoleDetailsPlace("StandardizedRolePlace!DELETED"));
						// placeController.goTo(new
						// RolePlace(Operation.DETAILS));

					}
				});

	}

	/*
	 * @Override public void deleteRoleClicked(StandardizedRoleProxy
	 * standardizedRoleProxy() {
	 * 
	 * 
	 * }
	 */

	/*
	 * @Override public void createRole(StandardizedRoleProxy
	 * standardizedRoleProxy) {
	 * 
	 * System.out.println("Call createRole() of RoleDetailActivity"); goTo(new
	 * RoleDetailsPlace(standardizedRoleProxy.stableId(), Operation.CREATE)); }
	 */

	@Override
	public void editClicked() {
		System.out.println("Call editClicked() of RoleDetailActivity");
		// TODO Auto-generated method stub

	}

	@Override
	public void createRole() {
		System.out.println("Call createRole() of RoleDetailActivity");
		goTo(new RoleDetailsPlace(Operation.CREATE));
	}

}
