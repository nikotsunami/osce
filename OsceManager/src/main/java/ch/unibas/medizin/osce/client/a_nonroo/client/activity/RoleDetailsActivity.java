package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.ScarProxyRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CheckListTopicPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CheckListTopicPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.ImportTopicPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.ImportTopicPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableAccessViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableItemValueView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableItemValueViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableItemView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleBaseTableItemViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubViewChecklistCriteriaItemView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubViewChecklistOptionItemView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubViewChecklistOptionItemViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubViewChecklistQuestionItemView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubViewChecklistTopicItemView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsChecklistSubViewChecklistTopicItemViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleEditCheckListSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleFileSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleFileSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleKeywordSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleKeywordSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleOtherSearchCriteriaView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleRoleParticipantSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleRoleParticipantSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchNationalityPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchNationalityPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUp;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubViewImpl;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicRequest;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.client.managed.request.FileRequest;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordRequest;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleSubItemValueRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTableItemValueRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTemplateProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleRequest;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialRequest;
import ch.unibas.medizin.osce.client.managed.ui.MaterialListProxyRenderer;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.RoleParticipantTypes;
import ch.unibas.medizin.osce.shared.ViewType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
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
		RoomMaterialsDetailsSubView.Presenter,
		RoleOtherSearchCriteriaView.Delegate,
		RoleOtherSearchCriteriaView.Presenter,
		StandartizedPatientAdvancedSearchSubView.Delegate,
		StandartizedPatientAdvancedSearchBasicCriteriaPopUp.Delegate,
		StandardizedPatientAdvancedSearchLanguagePopup.Delegate,
		StandardizedPatientAdvancedSearchScarPopup.Delegate,
		StandardizedPatientAdvancedSearchAnamnesisPopup.Delegate,
		StandardizedPatientAdvancedSearchNationalityPopup.Delegate,		
		RoleRoleParticipantSubView.Delegate, 
		RoleKeywordSubView.Delegate,
		RoleDetailsChecklistSubViewChecklistTopicItemView.Delegate,
		RoleDetailsChecklistSubViewChecklistQuestionItemView.Delegate,
		ImportTopicPopupView.Delegate,
		RoleDetailsChecklistSubViewChecklistCriteriaItemView.Delegate,
		RoleDetailsChecklistSubViewChecklistOptionItemView.Delegate,
		RoleBaseTableItemView.Delegate,RoleBaseTableItemValueView.Delegate

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
	public RoleEditCheckListSubViewImpl[] roleEditCheckListSubView;
	private ViewType role_script_Scerrn= ViewType.role_script;
	private static int selecTab = 0;

	
	// Assignment I
		private PopupPanel toolTip;
		private HorizontalPanel toolTipContentPanel;
		private TextBox toolTipTextBox;
		private Button toolTipChange;
		//private final OsceConstants constants = GWT.create(OsceConstants.class);
		private RoleBaseTableItemViewImpl[] roleBaseTableItemViewImpl;
		// End I
	//Assignment E[
	public RoleTopicProxy roleTopicProxy;
	
	//AssignmentE]
	// SPEC START =
	
	public RoleRoleParticipantSubViewImpl roleRoleParticipantSubViewImpl;
	public RoleDetailsViewImpl roleDetailsViewImpl;
	public RoleKeywordSubViewImpl roleKeywordSubViewImpl;
	
	private CellTable<RoleParticipantProxy> authorTable;
	private CellTable<RoleParticipantProxy> reviewerTable;
	private CellTable<KeywordProxy> keywordTable;

	KeywordProxy selKeywordProxy;
	
	public CheckListTopicPopupView topicPopup;
	
	// SPEC END =
	
	
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

	private SingleSelectionModel<UsedMaterialProxy> selectionUsedMaterialModel;

	// ]End

	// Assignment F[
	private RoleOtherSearchCriteriaView[] simpleSearchCriteriaView;
	public CellTable<SimpleSearchCriteriaProxy> simpleSearchcriteriaTable[];
	private SingleSelectionModel<SimpleSearchCriteriaProxy> simpleSearchSelectionModel;
	private HandlerRegistration simpleSearchRangeChangeHandler;
	private int simpleSearchSortOrder;

	private OsceConstants constants = GWT.create(OsceConstants.class);
	public Object[] standardizedRoleProxies;
	/** Holds a reference to the currently selected advancedSearchPopup */
	private StandardizedPatientAdvancedSearchPopup advancedSearchPopup;
	/** Holds a reference to the anamnesisPopup if open */
	private StandardizedPatientAdvancedSearchAnamnesisPopup anamnesisPopup;
	/** Holds a reference to the basicCriteriaPopUp if open */
	private StandartizedPatientAdvancedSearchBasicCriteriaPopUp basicCriteriaPopUp;
	/** Holds a reference to the scarPopup if open */
	private StandardizedPatientAdvancedSearchScarPopup scarPopup;
	/** Holds a reference to the nationalityPopup if open */
	private StandardizedPatientAdvancedSearchLanguagePopup languagePopup;
	/** Holds a reference to the nationalityPopup if open */
	private StandardizedPatientAdvancedSearchNationalityPopup nationalityPopup;
	/** Holds the table with the advanced search criteria */
	private CellTable<AdvancedSearchCriteriaProxy> advancedSearchPatientTable[];
	private StandartizedPatientAdvancedSearchSubView advancedSearchSubViews[];
	private List<AdvancedSearchCriteriaProxy> searchCriteria = new ArrayList<AdvancedSearchCriteriaProxy>();

	private HandlerRegistration rangeAdvanceSearchTableChangeHandler;

	private SingleSelectionModel<AdvancedSearchCriteriaProxy> selectionAdvanceSearchModel;

	// ]Assignment F

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
		if (advancedSearchPopup != null) {
			advancedSearchPopup.hide();
		}
		if (rangeUsedMaterialTableChangeHandler != null) {
			rangeUsedMaterialTableChangeHandler.removeHandler();
		}
		if (rangeAdvanceSearchTableChangeHandler != null) {
			rangeAdvanceSearchTableChangeHandler.removeHandler();
		}
		if (simpleSearchRangeChangeHandler != null) {
			simpleSearchRangeChangeHandler.removeHandler();
		}

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
		

		// SPEC START =
		roleDetailsViewImpl = new RoleDetailsViewImpl();
		// SPEC END =
		
		widget.setWidget(roleDetailsView.asWidget());
		view.setDelegate(this);
		
		// SPEC START
		//requests.find(place.getProxyId()).with("standardizedRoles","standardizedRoles.keywords").fire(new InitializeActivityReceiver());
		requests.find(place.getProxyId()).with("standardizedRoles","standardizedRoles.keywords","standardizedRoles.previousVersion","standardizedRoles.checkList","standardizedRoles.checkList.checkListTopics","standardizedRoles.checkList.checkListTopics.checkListQuestions","standardizedRoles.checkList.checkListTopics.checkListQuestions.checkListCriterias","standardizedRoles.checkList.checkListTopics.checkListQuestions.checkListOptions").fire(new InitializeActivityReceiver());
		
		// SPEC END

		/*requests.find(place.getProxyId()).with("standardizedRoles")
				.fire(new InitializeActivityReceiver());
		*/
		//requests.find(place.getProxyId()).with("standardizedRoles" , "standardizedRoles.previousVersion").fire(new InitializeActivityReceiver());		//spec
		//Assignment E
		//requests.find(place.getProxyId()).with("standardizedRoles").with("standardizedRoles.checkList").with("standardizedRoles.checkList.checkListTopics").with("standardizedRoles.checkList.checkListTopics.checkListQuestions").with("standardizedRoles.checkList.checkListTopics.checkListQuestions.checkListCriterias").with("standardizedRoles.checkList.checkListTopics.checkListQuestions.checkListOptions").fire(new InitializeActivityReceiver());		

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
		requests.fileRequestNooRoo().fileMoveUp(standRoleProxy.getId())
				.using(proxy).fire(new Receiver<Void>() {
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
		requests.fileRequestNooRoo().fileMoveDown(standRoleProxy.getId())
				.using(proxy).fire(new Receiver<Void>() {
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
		requests.usedMaterialRequestNonRoo()
				.moveMaterialDown(standardizedRoleProxy.getId()).using(proxy)
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
		requests.usedMaterialRequestNonRoo()
				.moveMaterialUp(standardizedRoleProxy.getId()).using(proxy)
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
			
			//Assignment E[
			
			roleTopicProxy=(RoleTopicProxy)response;
			//Assignment E]
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

					// Assignment F[
					simpleSearchCriteriaView = new RoleOtherSearchCriteriaView[size];
					simpleSearchcriteriaTable = new CellTable[size];

					advancedSearchSubViews = new StandartizedPatientAdvancedSearchSubViewImpl[size];
					advancedSearchPatientTable = new CellTable[size];

					standardizedRoleProxies = ((RoleTopicProxy) response)
							.getStandardizedRoles().toArray();

					// ]Assignment F

					// Assignment I
					roleBaseTableItemViewImpl = new RoleBaseTableItemViewImpl[size];
					stRoleIterator = ((RoleTopicProxy) response)
							.getStandardizedRoles().iterator();
				
					while (stRoleIterator.hasNext()) {
						StandardizedRoleProxy proxy = stRoleIterator.next();
						
						// Boolean object has to be checked for null value?
						if(proxy.getActive().booleanValue() == true) 	{
						
						

						standardizedRoleDetailsView[index] = new StandardizedRoleDetailsViewImpl();
					
						standardizedRoleDetailsView[index].setValue(proxy);
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
						// Assignment F[

						simpleSearchCriteriaView[index] = standardizedRoleDetailsView[index]
								.getRoleOtherSearchCriteriaViewImpl();
						simpleSearchCriteriaView[index].setValue(proxy);
						simpleSearchCriteriaView[index]
								.setDelegate(roleDetailActivity);
						setSimpleSearchTable(
								simpleSearchCriteriaView[index].getTable(),
								proxy.getId(), index);

						ProvidesKey<SimpleSearchCriteriaProxy> keyProviderSimpleSearch = ((AbstractHasData<SimpleSearchCriteriaProxy>) simpleSearchcriteriaTable[index])
								.getKeyProvider();
						simpleSearchSelectionModel = new SingleSelectionModel<SimpleSearchCriteriaProxy>(
								keyProviderSimpleSearch);
						simpleSearchcriteriaTable[index]
								.setSelectionModel(simpleSearchSelectionModel);

						simpleSearchSelectionModel
								.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
									public void onSelectionChange(
											SelectionChangeEvent event) {
										SimpleSearchCriteriaProxy selectedObject = simpleSearchSelectionModel
												.getSelectedObject();
										if (selectedObject != null) {
											Log.debug(selectedObject.getName()
													+ " selected!");
											// showDetails(selectedObject);
										}
									}
								});

						advancedSearchSubViews[index] = standardizedRoleDetailsView[index]
								.getStandartizedPatientAdvancedSearchSubViewImpl();
						advancedSearchSubViews[index].setValue(proxy);
						advancedSearchSubViews[index]
								.setDelegate(roleDetailActivity);
						setAdvanceSearchTable(
								advancedSearchSubViews[index].getTable(),
								proxy.getId(), index);
						ProvidesKey<AdvancedSearchCriteriaProxy> keyAdvancedSearchProvider = ((AbstractHasData<AdvancedSearchCriteriaProxy>) advancedSearchPatientTable[index])
								.getKeyProvider();
						selectionAdvanceSearchModel = new SingleSelectionModel<AdvancedSearchCriteriaProxy>(
								keyAdvancedSearchProvider);
						advancedSearchPatientTable[index]
								.setSelectionModel(selectionAdvanceSearchModel);

						selectionAdvanceSearchModel
								.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
									public void onSelectionChange(
											SelectionChangeEvent event) {
										AdvancedSearchCriteriaProxy selectedObject = selectionAdvanceSearchModel
												.getSelectedObject();
										if (selectedObject != null) {
											Log.debug(selectedObject.getValue()
													+ " selected!");
											// showDetails(selectedObject);
										}
									}
								});

						roleDetailTabPanel
								.addSelectionHandler(new SelectionHandler<Integer>() {
									@Override
									public void onSelection(
											SelectionEvent<Integer> event) {
										if (advancedSearchPopup != null
												&& advancedSearchPopup
														.isShowing()) {
											advancedSearchPopup.hide();
										}

									}
								});

						standardizedRoleDetailsView[index].getRoleSubPanel()
								.addSelectionHandler(
										new SelectionHandler<Integer>() {
											@Override
											public void onSelection(
													SelectionEvent<Integer> event) {
												if (advancedSearchPopup != null
														&& advancedSearchPopup
																.isShowing()) {
													advancedSearchPopup.hide();
												}

											}
										});

						// ]Assignment F
						
						
						// SPEC START =
						
						standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().setDelegate(roleDetailActivity);
						standardizedRoleDetailsView[index].getRoleKeywordSubViewImpl().setDelegate(roleDetailActivity);
						authorTable = standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().authorTable; // ==>
						reviewerTable = standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().authorTable; // ==>
					
						ProvidesKey<RoleParticipantProxy> autkeyProvider = ((AbstractHasData<RoleParticipantProxy>) authorTable).getKeyProvider();
						
						final int innerindex = index;
						final int getStandardizedRole = Integer	.parseInt(standardizedRoleDetailsView[index].getValue().getId().toString());
						
						requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 0).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
						{
								@Override
								public void onSuccess(List<RoleParticipantProxy> response) 
								{
										Log.info("~Success Call....");
										Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
										Log.info("~Set Data In Author Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response.size());
										standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.setRowData(response);
								}
						});

						
						requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 1).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
						{
							@Override
							public void onSuccess(List<RoleParticipantProxy> response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response.size());
								standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowData(response);
							}
						});

						
						Log.info("==>>>Index: "+ innerindex+ " ID Pass: "+ ((RoleTopicProxy) response).getId()+ "SR ID: "+ standardizedRoleDetailsView[innerindex].getValue().getId());

						requests.doctorRequestNonRoo().findDoctorWithRoleTopic(standardizedRoleDetailsView[innerindex].getValue().getId()).fire(new Receiver<List<DoctorProxy>>() {
									@Override
									public void onSuccess(List<DoctorProxy> response) 
									{
										Log.info("~In doctorInitializeActivityReceiver<==");
										Log.info("~Success Call....");
										Log.info("~findDoctorWithRoleTopic()");
										Log.info("~Set Data In ValueListBox" + "Resp. Size: " + response.size()); 										
										// SPEC START MODIFIED =
										if(response.size()==0)
										{											
											System.out.println("~Keyword Null for Role " + innerindex );
											standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible(false);
										}
										else
										{
											standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible(true);
											standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setAcceptableValues(response);
										}
										// SPEC END MODIFIED =
									}

								});

						requests.keywordRequestNonRoo().findKeywordByStandRole(standardizedRoleDetailsView[index].getValue()).fire(new Receiver<List<KeywordProxy>>()						 
						{
								@Override
								public void onSuccess(List<KeywordProxy> response) 
								{									
									Log.info("~Success Call....");
									Log.info("~findKeywordByStandRole()");
									Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.size());
									standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
								}
						});

						requests.keywordRequest().findAllKeywords().fire(new Receiver<List<KeywordProxy>>() 
						{
							@Override
							public void onSuccess(List<KeywordProxy> response) 
							{
								Log.info("~Success Call....");
								Log.info("~findAllKeywords()");
								Log.info("~Set Keyword Auto Complete Value for SuggestBox: " + "Resp. Size: " + response.size());
								standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().setKeywordAutocompleteValue(response);
							}
						});

						// SPEC END =
						
						//Assignment E[
						//set CheckList Title
						//Log.info(proxy.getCheckList().getTitle());
						((StandardizedRoleDetailsViewImpl)standardizedRoleDetailsView[index]).roleSubPanel.getTabBar().setTabText(0,proxy.getCheckList().getTitle()==null ? "Checklist" : "Checklist "+proxy.getCheckList().getTitle());
						
						Log.info("checklisttopic Proxy Size:" + proxy.getCheckList().getCheckListTopics().size());
						Iterator<ChecklistTopicProxy> topicIterator=proxy.getCheckList().getCheckListTopics().iterator();
//						RoleDetailsChecklistSubViewChecklistQuestionItemView queView[]=new RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl[proxy.getCheckList().getCheckListTopics().size()];
						//create Topic View
						int i=0;
						while(topicIterator.hasNext())
						{
							ChecklistTopicProxy topicProxy=topicIterator.next();
							RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView=createCheckListTopic(index,topicProxy);
							
							//create Question View
							Iterator<ChecklistQuestionProxy> questionIterator=topicProxy.getCheckListQuestions().iterator();
							
							
							while(questionIterator.hasNext())
							{
								ChecklistQuestionProxy questionProxy=questionIterator.next();
								RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(index, questionProxy, topicView);
								
								//create Criteria View
								Iterator<ChecklistCriteriaProxy> criteriaIterator=questionProxy.getCheckListCriterias().iterator();
								while(criteriaIterator.hasNext())
								{
									ChecklistCriteriaProxy criteriaProxy=criteriaIterator.next();
									createCriteriaView(index, criteriaProxy, questionView);
								}
								
								//create Option View
								
								Iterator<ChecklistOptionProxy> optionIterator=questionProxy.getCheckListOptions().iterator();
								while(optionIterator.hasNext())
								{
									ChecklistOptionProxy optionProxy=optionIterator.next();
									createOptionView(index, optionProxy, questionView);
								}
								
								
								questionView.setChecklistTopicProxy(topicProxy);
								questionView.setRoleDetailsChecklistSubViewChecklistTopicItemView(topicView);
							}
						}
						
						
						//Assignment E]
						
						
//  //Initialise RoleScript (I) Assignment I
						
						
						roleBaseTableItemViewImpl[index] = new RoleBaseTableItemViewImpl();
						roleBaseTableItemViewImpl[index].setDelegate(roleDetailActivity);
						final int standarDizedViewIndex=index;
						requests.roleTemplateRequest().findAllRoleTemplates().fire(new Receiver<List<RoleTemplateProxy>>() {
							;
							@Override
							public void onSuccess(List<RoleTemplateProxy> response) {
								
								standardizedRoleDetailsView[standarDizedViewIndex].setRoleTemplateListBox(response);
							
								}
							});
						
						requests.roleBaseItemRequestNoonRoo().findRoleBaseItemByStandardizedRole(standardizedRoleDetailsView[index].getValue().getId()).with("roleSubItem","roleItemAccess").fire(new Receiver<List<RoleBaseItemProxy>>() {

							@Override
							public void onSuccess(List<RoleBaseItemProxy> response) {
								
								if(response != null && response.size() > 0 ){
								
									Log.info("Response Size" + response.size());
									Iterator<RoleBaseItemProxy> listRoleBaseItemProxy = response.iterator();
								
									while(listRoleBaseItemProxy.hasNext())
									{
									final RoleBaseTableItemValueViewImpl roleBaseTableItemViewImpl=new RoleBaseTableItemValueViewImpl();
									roleBaseTableItemViewImpl.setDelegate(roleDetailActivity);
									RoleBaseItemProxy roleBaseItemProxy = listRoleBaseItemProxy.next();
									
									if(roleBaseItemProxy.getItem_defination().name().equals("table_item"))
									{
										Log.info("Found Role_Table_Item");
										if(roleBaseItemProxy.getDeleted())
											continue;
										roleBaseTableItemViewImpl.setValue(roleBaseItemProxy);						
										
										standardizedRoleDetailsView[standarDizedViewIndex].getRoleBaseItemVerticalPanel().add(roleBaseTableItemViewImpl);
									
										requests.roleTableItemValueRequestNonRoo().findRoleTableItemValueByStandardizedRoleANDRoleBaseItemValues(standardizedRoleDetailsView[standarDizedViewIndex].getValue().getId(), roleBaseItemProxy.getId()).with("roleTableItem").fire(new Receiver<List<RoleTableItemValueProxy>>() {

											@Override
											public void onSuccess(
													List<RoleTableItemValueProxy> response) {
												Range range = roleBaseTableItemViewImpl.getTable().getVisibleRange();										
												roleBaseTableItemViewImpl.getTable().setRowCount(response.size());
												roleBaseTableItemViewImpl.getTable().setRowData(range.getStart(),response);
												
												
											}
										});
									
										
										roleBaseTableItemViewImpl.toolbar.removeFromParent();
										roleBaseTableItemViewImpl.description.removeFromParent();
										roleBaseTableItemViewImpl.addRichTextAreaValue.removeFromParent();
										
										// To Add Access Values	
										addAccessValue(roleBaseItemProxy,roleBaseTableItemViewImpl);
									}
									else
									{
										Log.info("Found Role_RichText_Area");
										if(roleBaseItemProxy.getDeleted())
											continue;
										roleBaseTableItemViewImpl.setValue(roleBaseItemProxy);
										
										if(roleBaseItemProxy.getRoleSubItem() != null)
										{
											roleBaseTableItemViewImpl.description.setText(roleBaseItemProxy.getRoleSubItem().iterator().next().getItemText());
										}
										// To remove extra RoleBase Item Proxy view Component
										
										roleBaseTableItemViewImpl.roleBaseItemDisclosurePanel.setStyleName("border=0");
										roleBaseTableItemViewImpl.table.removeFromParent();
										
										//	view.getTableItem().add(roleBaseTableItemViewImpl);
										
										standardizedRoleDetailsView[standarDizedViewIndex].getRoleBaseItemVerticalPanel().add(roleBaseTableItemViewImpl);
										
										
										// To Add Access Values	
											addAccessValue(roleBaseItemProxy,roleBaseTableItemViewImpl);
									}
								}

								}
							}
						});
						
						// End I
						index++;

					}
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
	
	//assignment E [
		public void roleTopicInit(final ChecklistTopicProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView)
		{
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			System.out.println("Checklist Id is "+standardizedRoleDetailsView[selectedtab].getValue().getCheckList().getId());
			requests.checkListRequest().findCheckList(standardizedRoleDetailsView[selectedtab].getValue().getCheckList().getId()).with("checkListTopics").with("checkListTopics.checkListQuestions").with("checkListTopics.checkListQuestions.checkListCriterias").with("checkListTopics.checkListQuestions.checkListOptions").fire(new Receiver<CheckListProxy>() {

				@Override
				public void onSuccess(CheckListProxy response) {
					Log.info("checklisttopic Proxy Size:" + response.getCheckListTopics().size());
					Iterator<ChecklistTopicProxy> topicIterator=response.getCheckListTopics().iterator();
					
					//create Topic View
					int i=0;
					standardizedRoleDetailsView[selectedtab].checkListsVerticalPanel.clear();
					while(topicIterator.hasNext())
					{
						ChecklistTopicProxy topicProxy=topicIterator.next();
						RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView=createCheckListTopic(selectedtab,topicProxy);
						//topicView=createCheckListTopic(selectedtab,topicProxy);
						
						//create Question View
						Iterator<ChecklistQuestionProxy> questionIterator=topicProxy.getCheckListQuestions().iterator();
						
						
						while(questionIterator.hasNext())
						{
							ChecklistQuestionProxy questionProxy=questionIterator.next();
							RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(selectedtab, questionProxy, topicView);
							
							//create Criteria View
							Iterator<ChecklistCriteriaProxy> criteriaIterator=questionProxy.getCheckListCriterias().iterator();
							while(criteriaIterator.hasNext())
							{
								ChecklistCriteriaProxy criteriaProxy=criteriaIterator.next();
								createCriteriaView(selectedtab, criteriaProxy, questionView);
							}
							
							//create Option View
							
							Iterator<ChecklistOptionProxy> optionIterator=questionProxy.getCheckListOptions().iterator();
							while(optionIterator.hasNext())
							{
								ChecklistOptionProxy optionProxy=optionIterator.next();
								createOptionView(selectedtab, optionProxy, questionView);
							}
							questionView.setChecklistTopicProxy(topicProxy);
							questionView.setRoleDetailsChecklistSubViewChecklistTopicItemView(topicView);
						}
					}
				}
			});

		}
		//] Assignment E

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
	public void createRole(StandardizedRoleProxy standardizedRoleProxy) {
		System.out.println("Call createRole() of RoleDetailActivity");
		goTo(new RoleDetailsPlace(this.roleTopicProxy.stableId() ,Operation.CREATE));
	}
	
	//Assignment E[
		public RoleDetailsChecklistSubViewChecklistTopicItemViewImpl createCheckListTopic(int selectedTab,ChecklistTopicProxy proxy)
		{
			
			//saveCheckListTopic(checkListTopic);
			
			
			RoleDetailsChecklistSubViewChecklistTopicItemView view = new RoleDetailsChecklistSubViewChecklistTopicItemViewImpl();
			
			view.setDelegate(this);
			view.setProxy(proxy);
			((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view).descriptionLbl.setText(proxy.getDescription());
			((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view).checkListTopicLbl.setText(proxy.getTitle());
			standardizedRoleDetailsView[selectedTab].checkListsVerticalPanel.insert(((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view), standardizedRoleDetailsView[selectedTab].checkListsVerticalPanel.getWidgetCount());
			standardizedRoleDetailsView[selectedTab].checkListTopicView.add(view);
			
			return (RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view;
		}
		
		public void saveCheckListTopic(final String checkListTopic,final String description)
		{
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			
			ChecklistTopicRequest request=requests.checklistTopicRequest();
			
			final ChecklistTopicProxy proxy=request.create(ChecklistTopicProxy.class);
			
			
			
			proxy.setTitle(checkListTopic);
			proxy.setDescription(description);
			
			proxy.setSort_order(standardizedRoleDetailsView[selectedtab].checkListsVerticalPanel.getWidgetCount());
			proxy.setCheckList(standardizedRoleDetailsView[selectedtab].getValue().getCheckList());
			request.persist().using(proxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("Check List Topic Saved");
					Log.info("Topic ID : " + proxy.getId());
					createCheckListTopic(selectedtab,proxy);
					
				}
			});
		}
		
		
		public void saveCheckListQuestion(String Question,String Instruction,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView)
		{
			Log.info("saveCheckListQuestion called");
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			 ChecklistQuestionRequest request=requests.checklistQuestionRequest();
			
			final ChecklistQuestionProxy proxy=request.create(ChecklistQuestionProxy.class);
			proxy.setQuestion(Question);
			proxy.setInstruction(Instruction);
			proxy.setCheckListTopic(topicView.getProxy());
			proxy.setSequenceNumber(topicView.checkListQuestionVerticalPanel.getWidgetCount());
			
			request.persist().using(proxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					createQuestionView(selectedtab,proxy,topicView);
					
				}
			});
		}
		
		public RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl createQuestionView(int selectedTab,ChecklistQuestionProxy proxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView)
		{
			RoleDetailsChecklistSubViewChecklistQuestionItemView questionView=new RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl();
			questionView.getQuestionItemLbl().setText(proxy.getQuestion());
			questionView.getQuestionInstruction().setText(proxy.getInstruction());
			questionView.setProxy(proxy);
			
			questionView.setDelegate(this);
			topicView.checkListQuestionVerticalPanel.insert(questionView, topicView.checkListQuestionVerticalPanel.getWidgetCount());
			return (RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)questionView;
		}
		
		
		public void saveCriteria(final String criteria,final RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			Log.info("saveCriteria");
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			ChecklistCriteriaRequest request = requests.checklistCriteriaRequest();
			
			final ChecklistCriteriaProxy proxy=request.create(ChecklistCriteriaProxy.class);
			proxy.setChecklistQuestion(questionView.getProxy());
			proxy.setCriteria(criteria);
			
			request.persist().using(proxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("Criteria Saved Successfully");
					createCriteriaView(selectedtab,proxy,questionView);
					
				}
			});
		}
		
		
		public void createCriteriaView(int selectedTab,ChecklistCriteriaProxy proxy,RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			RoleDetailsChecklistSubViewChecklistCriteriaItemView view=new RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl();
			view.getCriteriaLbl().setText(proxy.getCriteria());
			view.setProxy(proxy);
			view.setDelegate(this);
			questionView.criteriaHorizontalPanel.insert(view, questionView.criteriaHorizontalPanel.getWidgetCount());
//			questionView.criteriaHorizontalPanel.insert(new Label(),questionView.criteriaHorizontalPanel.getWidgetCount());
		}
		
		public void saveOption(String option,String value,final RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			Log.info("saveOption");
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			
			ChecklistOptionRequest request=requests.checklistOptionRequest();
			
			final ChecklistOptionProxy proxy=request.create(ChecklistOptionProxy.class);
			proxy.setOptionName(option);
			proxy.setChecklistQuestion(questionView.getProxy());
			proxy.setValue(value);
			
			request.persist().using(proxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("Option Saved Successfully");
					createOptionView(selectedtab,proxy,questionView);
					
				}
			});
		}
		
		public void createOptionView(int selectedtab,ChecklistOptionProxy optionProxy,RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			RoleDetailsChecklistSubViewChecklistOptionItemView view=new RoleDetailsChecklistSubViewChecklistOptionItemViewImpl();
			view.getOptionLbl().setText(optionProxy.getOptionName());
			view.getOptionValueLbl().setText(optionProxy.getValue());
			view.setProxy(optionProxy);
			view.setDelegate(this);
			questionView.optionVerticalPanel.insert(view, questionView.optionVerticalPanel.getWidgetCount());
			
		}
		
		
		public void deleteCheckListTopic(ChecklistTopicProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView)
		{
			requests.checklistTopicRequest().remove().using(proxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("CheckList Topic Deleted Successfully");
					topicView.removeFromParent();
				//placeController.goTo(new RoleDetailsPlace(ro));
					
				}
			});
			
		}
		
		
		@Override
		public void updateCheckListTopic(ChecklistTopicProxy proxy,
						final String topicname, final String description, final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			// TODO Auto-generated method stub
		//	requests.checklistTopicRequest().findChecklistTopic(proxy.getId()).fire(new Receiver<ChecklistTopicProxy>() {

		//		@Override
		//		public void onSuccess(ChecklistTopicProxy response) {
					// TODO Auto-generated method stub
					
					//RoleBaseItemRequest roleBaseItemReq = requests.roleBaseItemRequest();										
					//response = roleBaseItemReq.edit(response);
					//response.setItem_name(toolTipLabel.getText());
					
					ChecklistTopicRequest checklisttopicreq = requests.checklistTopicRequest();
					proxy = checklisttopicreq.edit(proxy);
					
					proxy.setTitle(topicname);
					proxy.setDescription(description);
					
					checklisttopicreq.persist().using(proxy).fire(new Receiver<Void>() {

						@Override
						public void onSuccess(Void arg0) {
							// TODO Auto-generated method stub
							topicView.checkListTopicLbl.setText(topicname);
							topicView.descriptionLbl.setText(description);
							Log.debug("Record Updated Successfully");
							
						}
					});
				//}

			//});
		}

		
		public void deleteQuestion(final RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionViewImpl)
		{
			requests.checklistQuestionRequest().remove().using(questionViewImpl.getProxy()).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
				
					Log.info("CheckList Question Deleted Successfully");
					questionViewImpl.removeFromParent();
				}
			});
		}
		
		public void deleteCriteria(final RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl criteriaView)
		{
			requests.checklistCriteriaRequest().remove().using(criteriaView.getProxy()).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("CheckList Criteria Deleted Successfully");
					criteriaView.removeFromParent();
					
				}
			
			});
		}
		
		public void deleteOption(final RoleDetailsChecklistSubViewChecklistOptionItemViewImpl optionView)
		{
			requests.checklistOptionRequest().remove().using(optionView.getProxy()).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("CheckList Option Deleted Successfully");
					optionView.removeFromParent();
					
				}
			});
		}
		
		public void setRoleListBoxValue(ImportTopicPopupView popupView)
		{
			popupView.setDelegate(this);
			
			StandardizedRoleProxy proxy=standardizedRoleDetailsView[view.getRoleDetailTabPanel().getTabBar().getSelectedTab()].getValue();
			
			ArrayList<StandardizedRoleProxy> roles=new ArrayList<StandardizedRoleProxy>();
			
			Iterator<StandardizedRoleProxy> iterator=roleTopicProxy.getStandardizedRoles().iterator();
			while(iterator.hasNext())
			{
				StandardizedRoleProxy sp=iterator.next();
				if(sp.equals(proxy) || (!sp.getActive()))
				{
					
				}
				else
				{
					roles.add(sp);
				}
			}
			popupView.getRoleLstBox().setAcceptableValues(roles);
		}
		
		public void roleListBoxValueSelected(StandardizedRoleProxy proxy,final ImportTopicPopupViewImpl importPopupView)
		{
			Log.info("roleListBoxValueSelected");
			Log.info("CheckList Id :"+  proxy.getCheckList().getId());
			requests.checkListRequest().findCheckList(proxy.getCheckList().getId()).with("checkListTopics").with("checkListTopics.checkListQuestions").with("checkListTopics.checkListQuestions.checkListCriterias").with("checkListTopics.checkListQuestions.checkListOptions").fire(new Receiver<CheckListProxy>() {

				@Override
				public void onSuccess(CheckListProxy response) {
					importPopupView.getTopicLstBox().setAcceptableValues(response.getCheckListTopics());
					
				}
			});
			
		}
		
		public void topicListBoxValueSelected(ChecklistTopicProxy proxy,final ImportTopicPopupViewImpl importPopupView)
		{
			Log.info("topicListBox Selected");
			Log.info("topic Id "+proxy.getId());
			requests.checklistTopicRequest().findChecklistTopic(proxy.getId()).with("checkListQuestions").with("checkListQuestions.checkListCriterias").with("checkListQuestions.checkListOptions").fire(new Receiver<ChecklistTopicProxy>() {
				@Override
				public void onSuccess(ChecklistTopicProxy response) {
					importPopupView.getQueListBox().setAcceptableValues(response.getCheckListQuestions());
					
				}
			});
		}
		
		
		public void importTopic(final ChecklistTopicProxy proxy)
		{
			
			Log.info("importTopic");
			
			final int selectedTab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			CheckListProxy checklsitProxy=standardizedRoleDetailsView[selectedTab].getValue().getCheckList();
			
			ChecklistTopicRequest request=requests.checklistTopicRequest();
			
			final ChecklistTopicProxy addTopicProxy=request.create(ChecklistTopicProxy.class);
			
			addTopicProxy.setCheckList(checklsitProxy);
			addTopicProxy.setDescription(proxy.getDescription());
			addTopicProxy.setSort_order(proxy.getSort_order());
			addTopicProxy.setTitle(proxy.getTitle());
			
			request.persist().using(addTopicProxy).fire(new Receiver<Void>() {
				@Override
				public void onSuccess(Void response) {
					
					requests.find(addTopicProxy.stableId()).fire(new Receiver<Object>() {

						@Override
						public void onSuccess(Object response) {
							ChecklistTopicProxy topicProxy=(ChecklistTopicProxy)response;
							//create Topic view
							final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView= createCheckListTopic(selectedTab, topicProxy);
							
							Iterator<ChecklistQuestionProxy> questionIterator=proxy.getCheckListQuestions().iterator();
							while(questionIterator.hasNext())
							{
								final ChecklistQuestionProxy questionProxy=questionIterator.next();
								System.out.println("Question id  : " + questionProxy.getId());
								System.out.println("option size : " + questionProxy.getCheckListOptions().size());
								System.out.println("criteria size : " + questionProxy.getCheckListCriterias().size());
								ChecklistQuestionRequest questionRequest=requests.checklistQuestionRequest();
								
								final ChecklistQuestionProxy addQuestionProxy=questionRequest.create(ChecklistQuestionProxy.class);
								
								addQuestionProxy.setCheckListTopic(topicProxy);
								addQuestionProxy.setInstruction(questionProxy.getInstruction());
								addQuestionProxy.setQuestion(questionProxy.getQuestion());
								addQuestionProxy.setSequenceNumber(questionProxy.getSequenceNumber());
								
								questionRequest.persist().using(addQuestionProxy).fire(new Receiver<Void>() {

									@Override
									public void onSuccess(Void response) {
										requests.find(addQuestionProxy.stableId()).fire(new Receiver<Object>() {

											@Override
											public void onSuccess(Object response) {
												ChecklistQuestionProxy queProxy=(ChecklistQuestionProxy)response;
												//create question view
												RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(selectedTab, queProxy, topicView);
												if(questionProxy.getCheckListOptions()!=null)
												{
													Iterator<ChecklistOptionProxy> optionIterator=questionProxy.getCheckListOptions().iterator();
													while(optionIterator.hasNext())
													{
														ChecklistOptionProxy optionProxy=optionIterator.next();
														Log.info("Option Name "+optionProxy.getOptionName());
														ChecklistOptionRequest optionRequest=requests.checklistOptionRequest();
														ChecklistOptionProxy addOptionProxy=optionRequest.create(ChecklistOptionProxy.class);
														
														addOptionProxy.setChecklistQuestion(queProxy);
														addOptionProxy.setOptionName(optionProxy.getOptionName());
														addOptionProxy.setValue(optionProxy.getValue());
														
														optionRequest.persist().using(addOptionProxy).fire();
														
														//create Option view
														createOptionView(selectedTab, addOptionProxy, questionView);
													}
												}
												if(questionProxy.getCheckListCriterias()!=null)
												{
													Iterator<ChecklistCriteriaProxy> criteriaIterator=questionProxy.getCheckListCriterias().iterator();
													while(criteriaIterator.hasNext())
													{
														ChecklistCriteriaProxy criteriaProxy=criteriaIterator.next();
														ChecklistCriteriaRequest criteriaRequest=requests.checklistCriteriaRequest();
														
														ChecklistCriteriaProxy addCriteriaProxy=criteriaRequest.create(ChecklistCriteriaProxy.class);
														addCriteriaProxy.setChecklistQuestion(queProxy);
														addCriteriaProxy.setCriteria(criteriaProxy.getCriteria());
														
														criteriaRequest.persist().using(addCriteriaProxy).fire();
														
														createCriteriaView(selectedTab, addCriteriaProxy, questionView);
														
													}
												}

											}
										});

										
									}
								});
															
								
															
							}
						}
					});
				}
			});
			
			
			
			
		}
		
		public void importQuestion(final ChecklistQuestionProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView)
		{
			Log.info("import Question");
			final int selectedTab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			//final ChecklistQuestionProxy queProxy;
			CheckListProxy checklsitProxy=standardizedRoleDetailsView[selectedTab].getValue().getCheckList();
			
			//ChecklistQuestionRequest request=requests.checklistQuestionRequest();
			
			//ChecklistTopicProxy addTopicProxy=request.create(ChecklistTopicProxy.class);
			
			ChecklistQuestionRequest questionRequest=requests.checklistQuestionRequest();
			
			final ChecklistQuestionProxy addQuestionProxy=questionRequest.create(ChecklistQuestionProxy.class);
			
			addQuestionProxy.setCheckListTopic(topicView.getProxy());
			addQuestionProxy.setInstruction(proxy.getInstruction());
			addQuestionProxy.setQuestion(proxy.getQuestion());
			addQuestionProxy.setSequenceNumber(proxy.getSequenceNumber());
			
//			questionRequest.persist().using(addQuestionProxy).with("checkListQuestions","checkListQuestions.checkListCriterias","checkListQuestions.checkListOptions").fire(new Receiver<void>() {
	//
//				@Override
//				public void onSuccess(void response) {
//					Log.info("Question Added....");
//					queProxy=response;
//					
//				}
//			});
			
			questionRequest.persist().using(addQuestionProxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					
					requests.find(addQuestionProxy.stableId()).fire(new Receiver<Object>() {

						@Override
						public void onSuccess(Object response) {
							// TODO Auto-generated method stub
							ChecklistQuestionProxy queProxy= (ChecklistQuestionProxy)response;
							
							//create question view
							RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(selectedTab, queProxy, topicView);
							
							if(proxy.getCheckListOptions()!=null)
							{
								Iterator<ChecklistOptionProxy> optionIterator=proxy.getCheckListOptions().iterator();
								while(optionIterator.hasNext())
								{
									ChecklistOptionProxy optionProxy=optionIterator.next();
									ChecklistOptionRequest optionRequest=requests.checklistOptionRequest();
									ChecklistOptionProxy addOptionProxy=optionRequest.create(ChecklistOptionProxy.class);
									
									addOptionProxy.setChecklistQuestion(queProxy);
									addOptionProxy.setOptionName(optionProxy.getOptionName());
									addOptionProxy.setValue(optionProxy.getValue());
									
									
									Log.info("Option Question ID is :"+addOptionProxy.getChecklistQuestion().getId());
									optionRequest.persist().using(addOptionProxy).fire(new Receiver<Void>() {

										@Override
										public void onSuccess(Void response) {
											Log.info("CheckList option Added");
											
										}
										
									});
									
									//create Option view
									createOptionView(selectedTab, addOptionProxy, questionView);
								}
							}
							if(proxy.getCheckListCriterias()!=null)
							{
								Iterator<ChecklistCriteriaProxy> criteriaIterator=proxy.getCheckListCriterias().iterator();
								while(criteriaIterator.hasNext())
								{
									ChecklistCriteriaProxy criteriaProxy=criteriaIterator.next();
									ChecklistCriteriaRequest criteriaRequest=requests.checklistCriteriaRequest();
									
									ChecklistCriteriaProxy addCriteriaProxy=criteriaRequest.create(ChecklistCriteriaProxy.class);
									addCriteriaProxy.setChecklistQuestion(queProxy);
									addCriteriaProxy.setCriteria(criteriaProxy.getCriteria());
									
									Log.info("Criteria Question ID is :"+addCriteriaProxy.getChecklistQuestion().getId());
									criteriaRequest.persist().using(addCriteriaProxy).fire(new Receiver<Void>() {

										@Override
										public void onSuccess(Void response) {
											Log.info("CheckList Crierias added");
										}
									});
									
									createCriteriaView(selectedTab, addCriteriaProxy, questionView);
									
								}
							}
							
						}
					});
					
					
				}
				
			});
			
			
			
		}
		
		public void editOption(final String question, final String instruction, final RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			Log.info("editOption");
			 ChecklistQuestionProxy questionProxy=questionView.getProxy();
			
			ChecklistQuestionRequest request=requests.checklistQuestionRequest();
			questionProxy=request.edit(questionProxy);
			questionProxy.setQuestion(question);
			questionProxy.setInstruction(instruction);
			request.persist().using(questionProxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					questionView.questionItemLbl.setText(question);
					questionView.questionInstruction.setText(instruction);
				}
			});
		}
		

		@Override
		public void topicMoveDown(final ChecklistTopicProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			requests.checklistTopicRequestNonRoo()
			.topicMoveDown(standardizedRoleDetailsView[selectedtab].getValue().getCheckList().getId()).using(proxy)
			.fire(new Receiver<Void>() {
				@Override
				public void onSuccess(Void response) {
					Log.info("moved");
					roleTopicInit(proxy,topicView);

				}
			});
			
		}

		@Override
		public void topicMoveUp(final ChecklistTopicProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			requests.checklistTopicRequestNonRoo().topicMoveUp(standardizedRoleDetailsView[selectedtab].getValue().getCheckList().getId()).using(proxy).fire(new Receiver<Void>() {
				@Override
				public void onSuccess(Void response) {
					Log.info("moved");
					roleTopicInit(proxy,topicView);

				}
			});
			
		}

		@Override
		public void questionMoveUp(RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView,final ChecklistTopicProxy topicProxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			CheckListProxy checkListProxy=standardizedRoleDetailsView[selectedtab].getValue().getCheckList();
			System.out.println("Topic Id is "+topicProxy);System.out.println("Topic titlke is "+topicProxy);
			requests.checklistQuestionRequestNonRoo().questionMoveUp(topicProxy.getId()).using(questionView.getProxy()).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					// TODO Auto-generated method stub
					Log.info("question moved UP");
					roleTopicInit(topicProxy, topicView);
				}
			});
			
		}

		@Override
		public void questionMoveDown(RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView,final ChecklistTopicProxy topicProxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			CheckListProxy checkListProxy=standardizedRoleDetailsView[selectedtab].getValue().getCheckList();
//			System.out.println("Topic Id is "+topicProxy.getId());System.out.println("Topic titlke is "+topicProxy.getTitle());
			System.out.println("Topic Id is "+topicProxy);System.out.println("Topic titlke is "+topicProxy);
			requests.checklistQuestionRequestNonRoo().questionMoveDown(topicProxy.getId()).using(questionView.getProxy()).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					// TODO Auto-generated method stub
					Log.info("question moved Down");
					roleTopicInit(topicProxy, topicView);
				}
			});
		}
		
		//Assignment E]

	// Assignment F[

	@Override
	public void addBasicCriteriaClicked(Button addBasicData) {
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == basicCriteriaPopUp) {
				return;
			}
		}

		basicCriteriaPopUp = new StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl();
		basicCriteriaPopUp.setDelegate(this);
		basicCriteriaPopUp.display(addBasicData);
		advancedSearchPopup = basicCriteriaPopUp;
	}

	@Override
	public void filterTableClicked() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addScarCriteriaClicked(Button parentButton) {
		requests.scarRequest().findAllScars().fire(new ScarCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == scarPopup) {
				return;
			}
		}
		scarPopup = new StandardizedPatientAdvancedSearchScarPopupImpl();
		scarPopup.setDelegate(this);
		scarPopup.display(parentButton);
		advancedSearchPopup = scarPopup;
	}

	@Override
	public void addAnamnesisCriteriaClicked(Button parentButton) {
		requests.anamnesisCheckRequest().findAllAnamnesisChecks()
				.fire(new AnamnesisCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == anamnesisPopup) {
				return;
			}
		}
		anamnesisPopup = new StandardizedPatientAdvancedSearchAnamnesisPopupImpl();
		anamnesisPopup.setDelegate(this);
		anamnesisPopup.display(parentButton);
		advancedSearchPopup = anamnesisPopup;
	}

	@Override
	public void addLanguageCriteriaClicked(Button addLanguageButton) {
		requests.spokenLanguageRequest().findAllSpokenLanguages()
				.fire(new LanguageCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == languagePopup) {
				return;
			}
		}
		languagePopup = new StandardizedPatientAdvancedSearchLanguagePopupImpl();
		languagePopup.setDelegate(this);
		languagePopup.display(addLanguageButton);
		advancedSearchPopup = languagePopup;
	}

	@Override
	public void addNationalityCriteriaClicked(IconButton addNationalityButton) {
		requests.nationalityRequest().findAllNationalitys()
				.fire(new NationalityCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == nationalityPopup) {
				return;
			}
		}
		nationalityPopup = new StandardizedPatientAdvancedSearchNationalityPopupImpl();
		nationalityPopup.setDelegate(this);
		nationalityPopup.display(addNationalityButton);
		advancedSearchPopup = nationalityPopup;
	}

	private void setAdvanceSearchTable(
			CellTable<AdvancedSearchCriteriaProxy> table,
			long standardizedRoleID, int index) {
		this.advancedSearchPatientTable[index] = table;
		Log.info("standardizedRoleID:" + standardizedRoleID);
		initAdvancedSearch(standardizedRoleID, index);
	}

	private void initAdvancedSearch(final long standardizedRoleID,
			final int index) {

		// fix to avoid having multiple rangeChangeHandlers attached
		if (rangeAdvanceSearchTableChangeHandler != null) {
			rangeAdvanceSearchTableChangeHandler.removeHandler();
		}
		Log.info("standardizedRoleID:" + standardizedRoleID);
		fireAdvancedSearchCriteriasCountRequest(standardizedRoleID,
				new Receiver<Long>() {
					@Override
					public void onSuccess(Long response) {
						if (advancedSearchSubViews == null) {
							// This activity is dead
							return;
						}
						Log.debug("Advanced search init: " + response);
						advancedSearchSubViews[index].getTable().setRowCount(
								response.intValue(), true);

						onRangeChangedAdvancedSearchCriteriaTable(
								standardizedRoleID, index);
					}
				});

		rangeAdvanceSearchTableChangeHandler = advancedSearchPatientTable[index]
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						RoleDetailsActivity.this
								.onRangeChangedAdvancedSearchCriteriaTable(
										standardizedRoleID, index);
					}
				});
	}

	protected void fireAdvancedSearchCriteriasCountRequest(
			long standardizedRoleID, Receiver<Long> callback) {
		requests.advancedSearchCriteriaNonRoo()
				.countAdvancedSearchCriteriasByStandardizedRoleID(
						standardizedRoleID).fire(callback);
	}

	protected void onRangeChangedAdvancedSearchCriteriaTable(
			long standardizedRoleID, final int index) {
		final Range range = advancedSearchPatientTable[index].getVisibleRange();

		final Receiver<List<AdvancedSearchCriteriaProxy>> callback = new Receiver<List<AdvancedSearchCriteriaProxy>>() {
			@Override
			public void onSuccess(List<AdvancedSearchCriteriaProxy> values) {
				if (advancedSearchSubViews == null) {
					// This activity is dead
					return;
				}
				advancedSearchPatientTable[index].setRowData(range.getStart(),
						values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireAdvancedSearchRangeRequest(standardizedRoleID, range, callback,
				index);
	}

	private void fireAdvancedSearchRangeRequest(long standardizedRoleID,
			final Range range,
			final Receiver<List<AdvancedSearchCriteriaProxy>> callback,
			int index) {
		createAdvancedSearchRangeRequest(standardizedRoleID, range).with(
				advancedSearchSubViews[index].getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<AdvancedSearchCriteriaProxy>> createAdvancedSearchRangeRequest(
			long standardizedRoleID, Range range) {
		// return requests.scarRequest().findScarEntries(range.getStart(),
		// range.getLength());
		return requests
				.advancedSearchCriteriaNonRoo()
				.findAdvancedSearchCriteriasByStandardizedRoleID(
						standardizedRoleID, range.getStart(), range.getLength());
	}

	@Override
	public void deleteAdvancedSearchCriteria(
			AdvancedSearchCriteriaProxy criterion) {

		if (!Window
				.confirm("Really delete this entry? You cannot undo this change.")) {
			return;
		}

		final int selectedTab = roleDetailTabPanel.getTabBar().getSelectedTab();
		final Long stRoleId = ((StandardizedRoleProxy) standardizedRoleProxies[selectedTab])
				.getId();

		requests.advancedSearchCriteriaRequest().remove().using(criterion)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void arg0) {
						Log.info("Deleted...");
						initAdvancedSearch(stRoleId, selectedTab);

					}
				});
	}

	@Override
	public void addAdvSeaBasicButtonClicked(Long objectId, String value,
			String shownValue, BindType bindType,
			PossibleFields possibleFields, Comparison comparison) {

		switch (possibleFields) {
		case BMI:
			shownValue = constants.bmi()
					+ " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC)
							.render(comparison) + " " + value;
			break;
		case HEIGHT:
			shownValue = constants.height()
					+ " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC)
							.render(comparison) + " " + value + "cm";
			break;
		case WEIGHT:
			shownValue = constants.weight()
					+ " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC)
							.render(comparison) + " " + value + "kg";
		}
		AdvancedSearchCriteriaRequest searchCriteriaRequest = requests
				.advancedSearchCriteriaRequest();
		AdvancedSearchCriteriaProxy searchCriteriaProxy = searchCriteriaRequest
				.create(AdvancedSearchCriteriaProxy.class);

		// searchCriteriaProxy = req.edit(searchCriteriaProxy);
		searchCriteriaProxy.setBindType(bindType);
		searchCriteriaProxy.setComparation(comparison);
		searchCriteriaProxy.setField(possibleFields);
		searchCriteriaProxy.setValue(value);
		searchCriteriaProxy.setObjectId(objectId);
		searchCriteriaProxy.setShownValue(shownValue);

		final int selectedTab = roleDetailTabPanel.getTabBar().getSelectedTab();
		searchCriteriaProxy
				.setStandardizedRole((StandardizedRoleProxy) standardizedRoleProxies[selectedTab]);
		final Long stRoleId = ((StandardizedRoleProxy) standardizedRoleProxies[selectedTab])
				.getId();

		Log.info("Stand role Proxy ID: " + stRoleId);
		// Log.info("Stand Role Topoc Id : "+ S);

		// searchCriteriaProxy.setStandardizedRole();
		searchCriteriaRequest.persist().using(searchCriteriaProxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.debug("Adv search Added successfully");
						initAdvancedSearch(stRoleId, selectedTab);

					}
				});

		Log.debug("Added criterion: value = " + value);
	}

	@Override
	public void addNationalityButtonClicked(NationalityProxy nationality,
			BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(
				EnumRenderer.Type.NATIONALITY).render(comparison)
				+ " "
				+ nationality.getNationality();
		addAdvSeaBasicButtonClicked(nationality.getId(),
				nationality.getNationality(), displayValue, bindType,
				PossibleFields.NATIONALITY, comparison);
	}

	@Override
	public void addScarButtonClicked(ScarProxy scarProxy, BindType bindType,
			Comparison comparison) {
		Log.info("ScarType:" + scarProxy.getTraitType().toString() + ": "
				+ scarProxy.getBodypart());
		String displayValue = new EnumRenderer<Comparison>(
				EnumRenderer.Type.SCAR).render(comparison)
				+ " "
				+ new ScarProxyRenderer().render(scarProxy);
		String value = scarProxy.getTraitType().toString() + ":"
				+ scarProxy.getBodypart();
		addAdvSeaBasicButtonClicked(scarProxy.getId(), value, displayValue,
				bindType, PossibleFields.SCAR, comparison);
	}

	@Override
	public void addLanguageButtonClicked(SpokenLanguageProxy languageProxy,
			LangSkillLevel skill, BindType bindType, Comparison comparison) {
		String displayValue = constants.patientSpeaks()
				+ " "
				+ languageProxy.getLanguageName()
				+ " "
				+ new EnumRenderer<Comparison>(EnumRenderer.Type.LANGSKILL)
						.render(comparison) + " "
				+ new EnumRenderer<LangSkillLevel>().render(skill);
		String value = skill.toString();
		addAdvSeaBasicButtonClicked(languageProxy.getId(), value, displayValue,
				bindType, PossibleFields.LANGUAGE, comparison);
	}

	@Override
	public void addAnamnesisValueButtonClicked(
			AnamnesisCheckProxy anamnesisCheck, String answer,
			BindType bindType, Comparison comparison) {
		Log.info("Question:" + anamnesisCheck.getText() + "; options:"
				+ anamnesisCheck.getValue() + "; answer: " + answer);
		String displayValue = "\""
				+ anamnesisCheck.getText()
				+ "\" "
				+ new EnumRenderer<Comparison>(EnumRenderer.Type.ANAMNESIS)
						.render(comparison) + " "
				+ humanReadableAnamnesisAnswer(anamnesisCheck, answer);
		addAdvSeaBasicButtonClicked(anamnesisCheck.getId(), answer,
				displayValue, bindType, PossibleFields.ANAMNESIS, comparison);
	}

	private String humanReadableAnamnesisAnswer(AnamnesisCheckProxy proxy,
			String answer) {
		switch (proxy.getType()) {
		case QUESTION_OPEN:
		case QUESTION_TITLE:
			return answer;
		case QUESTION_YES_NO:
			if ("1".equals(answer))
				return constants.yes();
			return constants.no();
		case QUESTION_MULT_M:
		case QUESTION_MULT_S:
			String[] answerTokens = answer.split("-");
			String[] questionTokens = proxy.getValue().split("\\|");
			for (int i = 0; i < answerTokens.length; i++) {
				if (answerTokens[i].equals("1"))
					return questionTokens[i];
			}
		}
		return "";
	}

	private class NationalityCriteriaReceiver extends
			Receiver<List<NationalityProxy>> {
		@Override
		public void onSuccess(List<NationalityProxy> response) {
			if (nationalityPopup == null) {
				return;
			}
			List<NationalityProxy> values = new ArrayList<NationalityProxy>();
			values.addAll(response);
			if (values.size() > 0) {
				nationalityPopup.getNationalityBox().setValue(values.get(0));
			}
			nationalityPopup.getNationalityBox().setAcceptableValues(values);

		}

	}

	/**
	 * Receiver class that fills the language popups' pulldown with the
	 * available languages. Should be used in request for languages.
	 */
	private class LanguageCriteriaReceiver extends
			Receiver<List<SpokenLanguageProxy>> {
		@Override
		public void onSuccess(List<SpokenLanguageProxy> response) {
			if (languagePopup == null) {
				return;
			}
			List<SpokenLanguageProxy> values = new ArrayList<SpokenLanguageProxy>();
			values.addAll(response);
			if (values.size() > 0) {
				languagePopup.getLanguageBox().setValue(values.get(0));
			}
			languagePopup.getLanguageBox().setAcceptableValues(values);
		}
	}

	/**
	 * Receiver class that fills the Scar Popups Pulldown with the available
	 * scars. Should be used in request for scars.
	 */
	private class ScarCriteriaReceiver extends Receiver<List<ScarProxy>> {
		@Override
		public void onSuccess(List<ScarProxy> response) {
			if (scarPopup == null) {
				return;
			}

			List<ScarProxy> values = new ArrayList<ScarProxy>();
			values.addAll(response);
			if (values.size() > 0) {
				scarPopup.getScarBox().setValue(values.get(0));
			}
			scarPopup.getScarBox().setAcceptableValues(values);
		}
	}

	/**
	 * Receiver class that fills the anamnesis criteria popups' suggest box with
	 * possible values. Should be used in request for anamnesis values.
	 */
	private class AnamnesisCriteriaReceiver extends
			Receiver<List<AnamnesisCheckProxy>> {
		public void onSuccess(List<AnamnesisCheckProxy> response) {
			if (anamnesisPopup == null) {
				return;
			}
			((ProxySuggestOracle<AnamnesisCheckProxy>) anamnesisPopup
					.getAnamnesisQuestionSuggestBox().getSuggestOracle())
					.addAll(response);
		}
	}

	// -

	private void simpleSearchInit(long StandardizedRoleID, int index) {
		simpleSearchInit2(StandardizedRoleID, index);
	}

	private void simpleSearchInit2(final long StandardizedRoleID,
			final int index) {
		if (simpleSearchRangeChangeHandler != null) {
			simpleSearchRangeChangeHandler.removeHandler();
		}

		fireSimpleSearchCountRequest(StandardizedRoleID, new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (simpleSearchCriteriaView == null) {
					// This activity is dead
					return;
				}
				Log.debug("Geholte Narben aus der Datenbank: " + response);
				simpleSearchCriteriaView[index].getTable().setRowCount(
						response.intValue(), true);

				onRangeChangedSimpleSearch(StandardizedRoleID, index);
			}
		});

		simpleSearchRangeChangeHandler = simpleSearchcriteriaTable[index]
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {
						RoleDetailsActivity.this.onRangeChangedSimpleSearch(
								StandardizedRoleID, index);
					}
				});
	}

	protected void onRangeChangedSimpleSearch(long StandardizedRoleID,
			final int index) {
		final Range range = simpleSearchcriteriaTable[index].getVisibleRange();

		final Receiver<List<SimpleSearchCriteriaProxy>> callback = new Receiver<List<SimpleSearchCriteriaProxy>>() {
			@Override
			public void onSuccess(List<SimpleSearchCriteriaProxy> values) {
				if (simpleSearchCriteriaView == null) {
					// This activity is dead
					return;
				}
				simpleSearchcriteriaTable[index].setRowData(range.getStart(),
						values);

				// finishPendingSelection();
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}
		};

		fireRangeRequestSimpleSearch(StandardizedRoleID, range, callback, index);
	}

	protected void fireSimpleSearchCountRequest(long StandardizedRoleID,
			Receiver<Long> callback) {
		// requests.scarRequest().countScars().fire(callback);
		requests.simpleSearchCriteriaRequestNonRoo()
				.countSimpleSearchByStandardizedRoleID(StandardizedRoleID)
				.fire(callback);
	}

	private void fireRangeRequestSimpleSearch(long StandardizedRoleID,
			final Range range,
			final Receiver<List<SimpleSearchCriteriaProxy>> callback, int index) {
		createRangeRequestSimpleSearch(StandardizedRoleID, range).with(
				simpleSearchCriteriaView[index].getPaths()).fire(callback);
		// Log.debug(((String[])view.getPaths().toArray()).toString());
	}

	protected Request<List<SimpleSearchCriteriaProxy>> createRangeRequestSimpleSearch(
			long StandardizedRoleID, Range range) {
		// return requests.scarRequest().findScarEntries(range.getStart(),
		// range.getLength());
		return requests.simpleSearchCriteriaRequestNonRoo()
				.findSimpleSearchByStandardizedRoleID(StandardizedRoleID,
						range.getStart(), range.getLength());
	}

	private void setSimpleSearchTable(
			CellTable<SimpleSearchCriteriaProxy> table,
			long StandardizedRoleID, int index) {
		this.simpleSearchcriteriaTable[index] = table;
		simpleSearchInit(StandardizedRoleID, index);
	}

	@Override
	public void simpleSearchMoveUp(SimpleSearchCriteriaProxy proxy,
			final StandardizedRoleProxy standRoleProxy) {
		requests.simpleSearchCriteriaRequestNonRoo()
				.simpleSearchMoveUp(standRoleProxy.getId()).using(proxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.info("moved");
						simpleSearchInit(standRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());

					}
				});

	}

	@Override
	public void simpleSearchMoveDown(SimpleSearchCriteriaProxy proxy,
			final StandardizedRoleProxy standRoleProxy) {
		requests.simpleSearchCriteriaRequestNonRoo()
				.simpleSearchMoveDown(standRoleProxy.getId()).using(proxy)
				.fire(new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						Log.info("moved");
						simpleSearchInit(standRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
					}
				});

	}

	@Override
	public void simpleSearchDeleteClicked(SimpleSearchCriteriaProxy proxy,
			final StandardizedRoleProxy standRoleProxy) {
		requests.simpleSearchCriteriaRequest().remove().using(proxy)
				.fire(new Receiver<Void>() {
					public void onSuccess(Void ignore) {
						Log.debug("Sucessfully deleted");
						simpleSearchInit(standRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
					}
				});

	}

	@Override
	public void newSimpleSearchClicked(String SearchName, String SearchValue,
			final StandardizedRoleProxy standRoleProxy) {
		if (SearchName != null) {
			simpleSearchSortOrder = simpleSearchcriteriaTable[roleDetailTabPanel
					.getTabBar().getSelectedTab()].getRowCount() + 1;
			Log.debug("Add Simple Search");

			SimpleSearchCriteriaRequest simpleSearchreq = requests
					.simpleSearchCriteriaRequest();
			SimpleSearchCriteriaProxy simpleSearchProxy = simpleSearchreq
					.create(SimpleSearchCriteriaProxy.class);

			simpleSearchProxy.setName(SearchName);
			simpleSearchProxy.setValue(SearchValue);
			simpleSearchProxy.setSortOrder(simpleSearchSortOrder);
			simpleSearchProxy.setStandardizedRole(standRoleProxy);
			// reques.edit(scar);
			// file.setStandardizedRole(proxy);

			simpleSearchreq.persist().using(simpleSearchProxy)
					.fire(new Receiver<Void>() {
						@Override
						public void onSuccess(Void arg0) {
							Log.info("Simple Search Criteria added..");
							simpleSearchInit(standRoleProxy.getId(),
									roleDetailTabPanel.getTabBar()
											.getSelectedTab());
						}
					});
		}

	}

	// ]Assignment F
	
	
	@Override
	public void previousRoleClicked(StandardizedRoleProxy standardizedRoleProxy) {
		Log.info("previous clicked");
		System.out.println("============================Jump to StandardizedPatientDetailActivity previousClick() =========================");
	//	System.out.println("==>"+roleDetailTabPanel.getTabBar().getSelectedTab());
	//	int selTabID=roleDetailTabPanel.getTabBar().getSelectedTab();
		
		if(standardizedRoleProxy!=null)
		{
		requests.standardizedRoleRequest().findStandardizedRole(standardizedRoleProxy.getId()).with("previousVersion").fire(new Receiver<StandardizedRoleProxy>() {
			
			@Override
			public void onSuccess(StandardizedRoleProxy response) {
				// TODO Auto-generated method stub
				
				StandardizedRoleDetailsViewImpl previousRole;	
				StandardizedRoleProxy roleProxy=response.getPreviousVersion();
				if(roleProxy!=null)
				{
					
					System.out.println("Set value");
					//goTo(new RoleDetailsPlace(RoleEditActivity.roleTopic.stableId(),	Operation.DETAILS));
				//	goTo(new RoleDetailsPlace((standardizedRoleProxy.getPreviousVersion()).roleTopic.stableId(),Operation.DETAILS));
				//	goTo(new RoleDetailsPlace((standardizedRoleProxy.getPreviousVersion()).stableId(),Operation.DETAILS));
					previousRole=standardizedRoleDetailsView[roleDetailTabPanel.getTabBar().getSelectedTab()];
					
					previousRole.edit.setEnabled(false);
					previousRole.delete.setEnabled(false);
					previousRole.setTitle(roleProxy.getShortName() == null ? " " : String.valueOf(roleProxy.getShortName()));
					previousRole.shortName.setInnerText(roleProxy.getShortName() == null ? " " : String.valueOf(roleProxy.getShortName()));
					previousRole.longName.setInnerText(roleProxy.getLongName() == null ? " " : String.valueOf(roleProxy.getLongName()));
					previousRole.roleType.setInnerText(roleProxy.getRoleType().name()); //ADDED
					previousRole.studyYear.setInnerText(roleProxy.getStudyYear().name()); //ADDED
					previousRole.labelLongNameHeader.setText(""+roleProxy.getLongName());
					previousRole.setValue(roleProxy);
				}
				else
				{
					Window.alert("No More Record");
				}
				
			}
		});
		}
	}
	

	// SPEC START =
	@Override
	public void AddAuthorClicked() 
	{
		Log.info("~Call AddAuthorClicked:RoleDetailActivity");		
		final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();	
		
		if(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue() == null)
		{
			Window.alert("Please Select the Doctor from List.");
		}
		else
		{
				Log.info("~Selected Author Name: : "+ standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue().getName());		
				Log.info("~Selected Author Id: "+ standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue().getId());
				
				RoleParticipantRequest roleParticipantRequest = requests.roleParticipantRequest();
				RoleParticipantProxy roleParticipantProxy = roleParticipantRequest.create(RoleParticipantProxy.class);
		
				roleParticipantProxy.setType(RoleParticipantTypes.AUTHOR);	// If Author
				roleParticipantProxy.setDoctor(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue());
				roleParticipantProxy.setStandardizedRole(standardizedRoleDetailsView[selectedTabId].getValue());
				
				Log.info("~Stand Role Id: " + roleParticipantProxy.getStandardizedRole().getId());
				Log.info("~Doctor Name:" + roleParticipantProxy.getDoctor().getName());
		
				roleParticipantRequest.persist().using(roleParticipantProxy).fire(new Receiver<Void>() 
				{
						@Override
						public void onSuccess(Void response) 
						{
							Log.info("~Success Call....");
							Log.info("~roleParticipantRequest.persist()");
							
							// REFRESH LIST VIEW
							refreshDoctorList();
							
							// REFRESH Author Table
							requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 0).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
							{
									@Override
									public void onSuccess(List<RoleParticipantProxy> response) 
									{																																
										Log.info("~Success Call....");
										Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
										Log.info("~Set Data In Author Table: Resp Size: "+ response.size());														
										standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().authorTable.setRowData(response);
									}
							});
								
						}
				});
				
				
				
				
				// REFRESH LIST VIEW
				//refreshDoctorList();	
		}
	}

	@Override
	public void AddReviewerClicked() 
	{
		Log.info("~Call AddReviewerClicked:RoleDetailActivity");
		final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();
		
		if(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue() == null)
		{
			Window.alert("Please Select the Doctor from List.");
		}
		else
		{
		
		Log.info("~Selected Reviewer Name: : "+ standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue().getName());		
		Log.info("~Selected Reviewer Id: "+ standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue().getId());

		RoleParticipantRequest roleParticipantRequest = requests.roleParticipantRequest();
		RoleParticipantProxy roleParticipantProxy = roleParticipantRequest.create(RoleParticipantProxy.class);

		roleParticipantProxy.setType(RoleParticipantTypes.REVIEWER);// IfReviewer
		roleParticipantProxy.setDoctor(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue());
		roleParticipantProxy.setStandardizedRole(standardizedRoleDetailsView[selectedTabId].getValue());
		
		Log.info("~Stand Role Id: " + roleParticipantProxy.getStandardizedRole().getId());
		Log.info("~Doctor Name:" + roleParticipantProxy.getDoctor().getName());

		roleParticipantRequest.persist().using(roleParticipantProxy).fire(new Receiver<Void>() 
		{			
				@Override
				public void onSuccess(Void response) 
				{
					Log.info("~Success Call....");
					Log.info("~roleParticipantRequest.persist()");									
								
					// REFRESH LIST VIEW
					refreshDoctorList();
					
					// REFRESH Reviewer Table
					requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 1).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
					{
							@Override
							public void onSuccess(List<RoleParticipantProxy> response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: Resp Size: "+ response.size());																							
								standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowData(response);
								//standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.removeFromParent();
							}
					});						
				}				
		});
			
		}
	}

	@Override
	public void addKeywordClicked(String string) 
	{
			
		Log.info("~Click on Add Keyword for Role");
		
		final int selectedTabId=roleDetailTabPanel.getTabBar().getSelectedTab();
		final String selectedKeyword=standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getValue();
		
		Log.info("~Set Value To Null");
		//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());
		
		//SuggestBox suggestBox = null;
		standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getTextBox().setText(null);		
		Log.info("~TextBox Value : " + standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getValue());
		
		Log.info("~Selected Keyword: " + selectedKeyword);

		requests.keywordRequest().findAllKeywords().fire(new Receiver<List<KeywordProxy>>() 
		{
			@Override
			public void onSuccess(final List<KeywordProxy> response) 
			{
				Log.info("~Success Call....");
				Log.info("~findAllKeywords()");				
				Log.info("~Keyword Resp Size: "+ response.size());
				
				int size=response.size();				
				int i=0;
				boolean flag=false;
								
				for(i=0;i<size;i++)
				{
					Log.info("~Keyword Name: " + response.get(i).getName());
					if(response.get(i).getName().equals(selectedKeyword))
					{				
						Log.info("~Set Proxy for Keyword " + response.get(i).getName());
						flag=true;
						selKeywordProxy= response.get(i);
						break;
					}

				}
				if(flag==false) // Keyword is not Exist in Keyword Table
				{
					Log.info("~Keyword Not Exist");
					Log.info("~Add new Keyword: Name: " + selectedKeyword);
					final int i1=i;
					
					KeywordRequest keywordRequest=requests.keywordRequest();	
					final KeywordProxy keywordProxy=keywordRequest.create(KeywordProxy.class);					
					keywordProxy.setName(selectedKeyword);
					
					keywordRequest.persist().using(keywordProxy).fire(new Receiver<Void>() 
					{						
						@Override
						public void onSuccess(Void response1) 
						{							
							Log.info("~Success Call....");
							Log.info("~keywordRequest.persist()");	
							Log.info("Add New Keyword in Keyword Table");
							assignKeywordToStandRoll(keywordProxy);							
						}

					
					});					

				}
				else
				{
					Log.info("~Keyword Exist");
					Log.info("~Keyword Assign to Role");							
					assignKeywordToStandRoll(selKeywordProxy);					
				}
				
			}
			
			public void assignKeywordToStandRoll(final KeywordProxy tempKeywordProxy) 					
			{
						Log.info("~Call assignKeywordToStandRoll()");
						KeywordProxy keywordProxy=tempKeywordProxy;
						
						StandardizedRoleProxy stRoleProxy = standardizedRoleDetailsView[selectedTabId].getValue();
						
										
						Log.info("~Keyword Proxy:" + keywordProxy.getName());
						Log.info("~Stand-Role Proxy:" + stRoleProxy.getShortName());
						Log.info("~Stand-Role Proxy Keyword Size:" + stRoleProxy.getKeywords().size());						
						
						StandardizedRoleRequest srRequest = requests.standardizedRoleRequest();
						stRoleProxy = srRequest.edit(stRoleProxy);						
						
						Set<KeywordProxy> setKeyworkdProxy = stRoleProxy.getKeywords();														
						if(setKeyworkdProxy == null)
						{
							setKeyworkdProxy = new HashSet<KeywordProxy>();
							Log.info("~Null part for Keyword : "+ selectedKeyword);
						}
						else
						{
							Log.info("~Not Null part for Keyword: " + selectedKeyword);	
						}
						
						setKeyworkdProxy.add(keywordProxy);
						stRoleProxy.setKeywords(setKeyworkdProxy);
						
						srRequest.persist().using(stRoleProxy).fire(new Receiver<Void>() 
						{									
							@Override
							public void onSuccess(Void response) 
							{
								Log.info("~Success Call....");
								Log.info("~srRequest.persist()");	
								Log.info("Add new Recoerd in standardized_role_keywords Table");
										
									
								// REFRESH LOGICAL (RELATIONSHIP) TABLE DATA [PROXY]
								refreshRelationshipProxy();
								
								//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.setText(null);
								//Log.info("Remove TextBox Value : " + standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getValue());
								
								// REFRESH KEYWORD TABLE DATA
								
								requests.keywordRequestNonRoo().findKeywordByStandRole(standardizedRoleDetailsView[selectedTabId].getValue()).fire(new Receiver<List<KeywordProxy>>()
								{
										@Override
										public void onSuccess(List<KeywordProxy> response) 
										{
											Log.info("~Success Call....");
											Log.info("~REFRESH KEYWORD TABLE DATA");
											Log.info("~findKeywordByStandRole()");
											Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.size());
											Log.info("~Fetch/SET Table Data");
											standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
											
										}
								});	
								
																		
								// REFRESH SUGGESION BOX DATA
								
								requests.keywordRequest().findAllKeywords().fire(new Receiver<List<KeywordProxy>>() 
								{
										@Override
										public void onSuccess(List<KeywordProxy> response) 
										{
											Log.info("~Success Call....");
											Log.info("~REFRESH SUGGESION BOX DATA");
											Log.info("~findAllKeywords()");
											Log.info("~Set keyword Autocomplete Value for SuggestBox:" + "Resp. Size: " + response.size());
											Log.info("~Fetch/SET SuggestBox Data");
											//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().setKeywordAutocompleteValue(response);
										}
								}); 
								
								standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.addChangeListener(new ChangeListener() 
								{									
									@Override
									public void onChange(Widget sender) 
									{									
										Log.info("~onChange keywordSugestionBox");										
										standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());	
									}
								});	
							}
						});
						
					}				
				});					
	}

	@Override
	public void performKeywordSearch() 
	{
		Log.info("~Perform Keyword Search");
		onRangeChangedKeyword();
	}

	private void onRangeChangedKeyword() 
	{
		Log.info("~Perform Keyword Search->onRangeChangedKeyword");
	}

	@Override
	public void deleteKeywordClicked(KeywordProxy keywordProxy) 
	{
		final int selectedTabId=roleDetailTabPanel.getTabBar().getSelectedTab();
		Log.info("~DeleteKeywordClicked");
		Log.info("~Delete Keyword for Role: " + standardizedRoleDetailsView[selectedTabId].getValue().getId());
		Log.info("~Delete Keyword: " + keywordProxy.getName());
		
		
		
		StandardizedRoleProxy stRoleProxy = standardizedRoleDetailsView[selectedTabId].getValue();
		StandardizedRoleRequest srRequest = requests.standardizedRoleRequest();
		stRoleProxy = srRequest.edit(stRoleProxy);
		
		Set<KeywordProxy> setKeyworkdProxy = stRoleProxy.getKeywords();
		
		Iterator<KeywordProxy> i = stRoleProxy.getKeywords().iterator();
		
		while (i.hasNext())
		{
			KeywordProxy keywordProxy1 = (KeywordProxy) i.next();
			Log.info("~ OUT Delete Keyword ID: " + keywordProxy.getId() + " " + keywordProxy1.getId());
			
			if (Integer.parseInt(keywordProxy1.getId().toString()) == Integer.parseInt(keywordProxy.getId().toString())) 
			{
				//stRoleProxy.getKeywords().remove(keywordProxy1);				
				Log.info("~Removing : " + keywordProxy1.getName());
				Log.info("~ IF Delete Keyword ID: " + keywordProxy.getId() + " " + keywordProxy1.getId());
				i.remove();
				break;
			}
			else
			{
				Log.info("~Other");
				Log.info("~ELSE Delete Keyword ID: " + keywordProxy.getId()+ " " + keywordProxy1.getId());
			}
		}
		stRoleProxy.setKeywords(setKeyworkdProxy);
		srRequest.persist().using(stRoleProxy).fire(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void response1) 
			{
				Log.info("~Record Deleted Successfully");
				Log.info("~Success......");
				Log.info("~srRequest.persist()");
				
				// REFRESH LOGICAL (RELATIONSHIP) TABLE DATA [PROXY]
				refreshRelationshipProxy();
				
				// REFRESH KEYWORD TABLE DATA
				requests.keywordRequestNonRoo().findKeywordByStandRole(standardizedRoleDetailsView[selectedTabId].getValue()).fire(new Receiver<List<KeywordProxy>>()
				{
						@Override
						public void onSuccess(List<KeywordProxy> response) 
						{
							Log.info("~Success Call....");
							Log.info("~REFRESH KEYWORD TABLE DATA");
							Log.info("~findKeywordByStandRole()");
							Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.size());
							Log.info("~Fetch/SET Table Data");									
							standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
						}
				});	
			}
			
		});								
	}	// END deleteKeywordClicked

	@Override
	public void deleteDoctorClicked(RoleParticipantProxy roleParticipantProxy,int i) 
	{
		final int selectedTabId=roleDetailTabPanel.getTabBar().getSelectedTab();
		final int flag=i;
		Log.info("delete clicked");
		Log.info("~Delete Doctor" + i +"Clicked Name: " + roleParticipantProxy.getDoctor().getName() );				
		Log.info("~Delete Doctor Clicked Role: " +standardizedRoleDetailsView[selectedTabId].getValue().getId());
		Log.info("~RoleParticipantProxyId: " + roleParticipantProxy.getId());
		
		requests.roleParticipantRequest().remove().using(roleParticipantProxy).fire(new Receiver<Void>() 
		{
			public void onSuccess(Void ignore) 
			{
				Log.debug("Sucessfully deleted");

			
				if(flag==0) // Author Deleted
				{
					// REFRESH Author Table
					requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 0).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
					{
							@Override
							public void onSuccess(List<RoleParticipantProxy> response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Author Table: Resp Size: "+ response.size());														
								standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().authorTable.setRowData(response);								
							}
					});	
					
				}
				else	// REVIEWER DELETED
				{
					// REFRESH Reviewer Table
					requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 1).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
					{
							@Override
							public void onSuccess(List<RoleParticipantProxy> response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: Resp Size: "+ response.size());																							
								standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowData(response);								
							}
					});		
				}
						
				//	REFRESH LIST VIEW
				refreshDoctorList();	
			}
		});
		
	} // end deleteDoctorClicked
	
	public void refreshRelationshipProxy()
	{
		// REFRESH LOGICAL (RELATIONSHIP) TABLE DATA [PROXY]
		final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();		
		requests.find(place.getProxyId()).with("standardizedRoles","standardizedRoles.keywords").fire(new Receiver<Object>()
		{
			@Override
			public void onFailure(ServerFailure error) {
				Log.error(error.getMessage());
			}

			@Override
			public void onSuccess(Object response) 
			{
				Log.info("~Success....");
				Log.info("~Refresh Relationship Data");
				Log.info("~Stand Role Size: "+ ((RoleTopicProxy) response).getStandardizedRoles().size()); // Return Size of Data
				if (response instanceof RoleTopicProxy) 
				{										
					if (((RoleTopicProxy) response).getStandardizedRoles() != null) 
					{												
						Object tempStRoleProxy[] = ((RoleTopicProxy) response).getStandardizedRoles().toArray();												
						StandardizedRoleProxy proxy = ((StandardizedRoleProxy)tempStRoleProxy[selectedTabId]);
						standardizedRoleDetailsView[selectedTabId].setValue(proxy); 													
						Log.info("In get updted Role ID : " + proxy.getId() + " keyword : " + proxy.getKeywords().size());		
					}
				}
				else 
				{
						Log.info("~No Roles Aveilable");
				}
			}

		});
		
	}
	
	
	public void refreshDoctorList()
	{
		// REFRESH LIST VIEW		
		final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();		
		requests.doctorRequestNonRoo().findDoctorWithRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId()).fire(new Receiver<List<DoctorProxy>>() {
			@Override
			public void onSuccess(List<DoctorProxy> response) 
			{
				Log.info("~Success Call....");
				Log.info("~refreshDoctorList");
				Log.info("~Set Data In ValueListBox" + "Resp. Size: " + response.size());				
				standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setValue(null);
				// SPEC START MODIFIED =
				if(response.size()==0)
				{											
					System.out.println("~Keyword Null for Role " + selectedTabId );
					standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible(false);
				}
				else
				{
					standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible(true);
					standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setAcceptableValues(response);
				}
				// SPEC END MODIFIED =
			}

		});			
	}
	
	// SPEC END =
	
	//AssignMEnt I
	@Override
	public void addRoleBaseSubItem(RoleBaseItemProxy roleBaseItemProxy,
			CellTable<RoleTableItemProxy> table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pencliButtonclickEvent(RoleBaseItemProxy roleBaseItemProxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roleTableItemEditButtonClicked(
			RoleTableItemProxy roleTableItem, Long id,
			CellTable<RoleTableItemProxy> table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roleTableItemDeleteClicked(RoleTableItemProxy roleTableItem,
			Long id, CellTable<RoleTableItemProxy> roleTableItemProxyTable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteButtonClickEvent(RoleBaseItemProxy roleBasedItemProxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roleTableItemMoveUp(RoleTableItemProxy roleTableItem, Long id,
			CellTable<RoleTableItemProxy> toleTableItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roleTableItemMoveDown(RoleTableItemProxy roleTableItem,
			Long id, CellTable<RoleTableItemProxy> toleTableItem) {
	}

	@Override
	public void baseItemUpButtonClicked(RoleBaseItemProxy roleBasedItemProxy) {
	}

	@Override
	public void baseItemDownButtonClicked(RoleBaseItemProxy roleBasedItemProxy) {
	}

	@Override
	public void baseItemAccessButtonClicked(ClickEvent event,
			RoleBaseItemProxy roleBasedItemProxy,
			HorizontalPanel accessDataPanel) {
	}

	// To ADD All RoleBaseItem For Particular RoleTemplate Dynamically
	int selectedtab;
	@Override
	public void roleTemplateValueButtonClicked(RoleTemplateProxy roleTemplateProxy) {
		
		selectedtab=0;
		requests.roleBaseItemRequestNoonRoo().findAllRoleBaseItemOnTemplateId(roleTemplateProxy.getId()).with("roleTableItem","roleItemAccess").fire(new Receiver<List<RoleBaseItemProxy>>() {
			@Override
			public void onSuccess(List<RoleBaseItemProxy> response) {
				Log.info("Total Result is :" +response.size());
				
				
				Iterator<RoleBaseItemProxy> listRoleBaseItemProxy = response.iterator();
				
				while(listRoleBaseItemProxy.hasNext())
				{
					final RoleBaseTableItemValueViewImpl roleBaseTableItemViewImpl=new RoleBaseTableItemValueViewImpl();
					roleBaseTableItemViewImpl.setDelegate(roleDetailActivity);
					final RoleBaseItemProxy roleBaseItemProxy = listRoleBaseItemProxy.next();
					
					if(roleBaseItemProxy.getItem_defination().name().equals("table_item"))
					{
						if(roleBaseItemProxy.getDeleted())
							continue;
						roleBaseTableItemViewImpl.setValue(roleBaseItemProxy);						
						
						selectedtab=roleDetailTabPanel.getTabBar().getSelectedTab();
						
						standardizedRoleDetailsView[selectedtab].getRoleBaseItemVerticalPanel().add(roleBaseTableItemViewImpl);
					
						roleBaseTableItemViewImpl.toolbar.removeFromParent();
						roleBaseTableItemViewImpl.description.removeFromParent();
						roleBaseTableItemViewImpl.addRichTextAreaValue.removeFromParent();
				
						roleBaseTableItemViewImpl.getTable().setRowCount(roleBaseItemProxy.getRoleTableItem().size());
					
						RoleTableItemProxy[] arrRoleTableItemProxy = new  RoleTableItemProxy[roleBaseItemProxy.getRoleTableItem().size()];								
						roleBaseItemProxy.getRoleTableItem().toArray(arrRoleTableItemProxy);
						List<RoleTableItemProxy> listRoleTableItemProxy = Arrays.asList(arrRoleTableItemProxy);						
						
						
			
						
						// Add Values in RoleTableItem Table
			
						final List<RoleTableItemValueProxy> listRoleTableItemValueProxy = new ArrayList<RoleTableItemValueProxy>();
						
						Iterator<RoleTableItemProxy> roleTableItemProxy = listRoleTableItemProxy.iterator();
						while(roleTableItemProxy.hasNext()){
							
							RoleTableItemValueRequest roleTableItemValue = requests.roleTableItemValueRequest();
							final RoleTableItemValueProxy roleTableItemValueProxy = roleTableItemValue.create(RoleTableItemValueProxy.class); 
							roleTableItemValueProxy.setStandardizedRole(standardizedRoleDetailsView[selectedtab].getValue());
							roleTableItemValueProxy.setRoleTableItem(roleTableItemProxy.next());
							roleTableItemValueProxy.setValue("Value");
							
							roleTableItemValue.persist().using(roleTableItemValueProxy).fire(new Receiver<Void>() {
								@Override
								public void onSuccess(Void response) {									
									Log.info("Persisted Role_Table_Item_Value");
						
									requests.find(roleTableItemValueProxy.stableId()).with("roleTableItem").fire(new Receiver<Object>() {

										@Override
										public void onSuccess(Object response) {
											
											Log.info("Size of ListRole To set " );
											listRoleTableItemValueProxy.add((RoleTableItemValueProxy)response);
											Log.info("Size of ListRole To set In value" + listRoleTableItemValueProxy.size());
											Range range = roleBaseTableItemViewImpl.getTable().getVisibleRange();
											roleBaseTableItemViewImpl.getTable().setRowCount(listRoleTableItemValueProxy.size());
											roleBaseTableItemViewImpl.getTable().setRowData(range.getStart(),listRoleTableItemValueProxy);

													
										}
										
									});
									
								}
								
							});
						}
						// To Add Access Values
						
						addAccessValue(roleBaseItemProxy,roleBaseTableItemViewImpl);
					}
					else
					{
						if(roleBaseItemProxy.getDeleted())
							continue;
						roleBaseTableItemViewImpl.setValue(roleBaseItemProxy);
						
						
						// Add Values in RoleTableItem Table
						
						RoleSubItemValueRequest roleSubitemValueReq = requests.roleSubItemValueRequest();
						RoleSubItemValueProxy roleSubItemValueProxy = roleSubitemValueReq.create(RoleSubItemValueProxy.class);
						roleSubItemValueProxy.setRoleBaseItem(roleBaseItemProxy);
						roleSubItemValueProxy.setStandardizedRole(standardizedRoleDetailsView[selectedtab].getValue());
						roleSubItemValueProxy.setItemText("Enter Value");
						
						roleSubitemValueReq.persist().using(roleSubItemValueProxy).fire();
						Log.info("Save Role_sub_item_value");
						// To remove extra RoleBase Item Proxy view Component
						
						roleBaseTableItemViewImpl.roleBaseItemDisclosurePanel.setStyleName("border=0");
						roleBaseTableItemViewImpl.table.removeFromParent();
						roleBaseTableItemViewImpl.description.setText("Enter Value");
						
						//	view.getTableItem().add(roleBaseTableItemViewImpl);
						int selectedtab2=roleDetailTabPanel.getTabBar().getSelectedTab();
						standardizedRoleDetailsView[selectedtab2].getRoleBaseItemVerticalPanel().add(roleBaseTableItemViewImpl);
						
						
						// To Add Access Values	
						addAccessValue(roleBaseItemProxy,roleBaseTableItemViewImpl);
					}
				}
				}
			
		});
	}
	public void addAccessValue(RoleBaseItemProxy roleBaseItemProxy,RoleBaseTableItemValueViewImpl roleBaseTableItemViewImpl){
		RoleBaseTableAccessViewImpl roleBaseTableAccssViewImpl = new RoleBaseTableAccessViewImpl();
		//roleBaseTableAccssViewImpl.setDelegate(roleDetailActivity);
		Set<RoleItemAccessProxy> setroleItemAccess = roleBaseItemProxy.getRoleItemAccess();
		Iterator<RoleItemAccessProxy> listRoleItemAccesProxy = setroleItemAccess.iterator();
		
		while(listRoleItemAccesProxy.hasNext())
		{
			roleBaseTableAccssViewImpl.accessDataLabel.setText(listRoleItemAccesProxy.next().getName());
			roleBaseTableItemViewImpl.accessDataPanel.add(roleBaseTableAccssViewImpl);
			roleBaseTableAccssViewImpl.accessDataCloseButton.setVisible(false);
		}
	}

	@Override
	public void addRoleScriptTableItemValue(
			final RoleTableItemValueProxy roleTableItemValueProxy,final Long roleBaseItemProxyid,
			final CellTable<RoleTableItemValueProxy> table) {
	
		final int strId =roleDetailTabPanel.getTabBar().getSelectedTab();
		
		toolTip= new PopupPanel(true);
		
		toolTip.setWidth("180px");
		toolTip.setHeight("40px");
	    toolTip.setAnimationEnabled(true);
	    
		toolTipContentPanel=new HorizontalPanel();
		
		toolTipContentPanel.setWidth("160px");
		toolTipContentPanel.setHeight("22px");
		toolTipContentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		toolTipContentPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	
		toolTipTextBox=new TextBox();
		
		toolTipTextBox.setWidth("120px");
		toolTipTextBox.setHeight("25px");
		
		toolTipChange = new Button("Save");
	 
		toolTipChange.setWidth("40px");
		toolTipChange.setHeight("25px");       
		
		int x=table.getAbsoluteLeft();
		int y=table.getAbsoluteTop(); 
		toolTipContentPanel.add(toolTipTextBox);
		toolTipContentPanel.add(toolTipChange);
	     
		toolTipTextBox.setText(roleTableItemValueProxy.getValue());
	       
		    
		toolTip.add(toolTipContentPanel);   // you can add any widget here
	        
		toolTip.setPopupPosition(x+110,y-50);
	        toolTip.show();
	        
	        toolTipChange.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					requests.roleTableItemValueRequest().findRoleTableItemValue(roleTableItemValueProxy.getId()).fire(new Receiver<RoleTableItemValueProxy>() {
					
								@Override
								public void onSuccess(RoleTableItemValueProxy response) {
									RoleTableItemValueRequest roleTableItemValueReq = requests.roleTableItemValueRequest();										
									response = roleTableItemValueReq.edit(response);
									response.setValue(toolTipTextBox.getText());
								
									roleTableItemValueReq.persist().using(roleTableItemValueProxy).fire(new Receiver<Void>(){
										
									@Override
									public void onFailure(ServerFailure error){
										Log.error("onFilure");
										Log.error(error.getMessage());				
									}
										
									@Override
									public void onSuccess(Void arg0) {
									Log.info("Save RoleTableItemvalue's value Succesfully according to ToolTip value");
										
											toolTip.clear();
											toolTip.hide();
											
											requests.roleTableItemValueRequestNonRoo().findRoleTableItemValueByStandardizedRoleANDRoleBaseItemValues(standardizedRoleDetailsView[strId].getValue().getId(), roleBaseItemProxyid).with("roleTableItem").fire(new Receiver<List<RoleTableItemValueProxy>>() {

												@Override
												public void onSuccess(List<RoleTableItemValueProxy> response) 
												{	
													Log.info("response : " + response);
													Range range = table.getVisibleRange();										
													table.setRowCount(response.size());
													table.setRowData(range.getStart(),response);
													
													
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
	public void addRichTextAreaValue(RoleBaseItemProxy roleBaseItemProxy, RichTextArea description) {
	
	RoleSubItemValueRequest roleSubItemValueReq = requests.roleSubItemValueRequest();
	RoleSubItemValueProxy roleSubItemValueProxy = roleSubItemValueReq.edit(roleBaseItemProxy.getRoleSubItem().iterator().next());
	
	roleSubItemValueProxy.setItemText(description.getText());
	
	roleSubItemValueReq.persist().using(roleSubItemValueProxy).fire(new Receiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("RichTextArea Value edited succeessfully");
			Window.alert("Rich Text Value Set Successfully");
		}
	});
	
	}

	
	

	
}
