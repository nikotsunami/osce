package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.RoleDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LearningObjectiveView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.ScarProxyRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CheckListQuestionPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CheckListTopicPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CheckListTopicPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CriteriaPopupViewImpl;
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
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleLearningPopUpView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleLearningSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleOsceSemesterSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleOtherSearchCriteriaView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleOtherSearchCriteriaViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleRoleParticipantSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleRoleParticipantSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsDetailsSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoomMaterialsPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRolePrintFilterViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchAnamnesisPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchGenderPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchGenderPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchLanguagePopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchMaritialStatusPopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchMaritialStatusPopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchNationalityPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchNationalityPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchProfessionPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchProfessionPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchScarPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchWorkPermissionPopup;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandardizedPatientAdvancedSearchWorkPermissionPopupImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUp;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria.StandartizedPatientAdvancedSearchSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.ApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistQuestionRequest;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistTopicRequest;
import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.FileProxy;
import ch.unibas.medizin.osce.client.managed.request.FileRequest;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.managed.request.KeywordRequest;
import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MainSkillRequest;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillRequest;
import ch.unibas.medizin.osce.client.managed.request.NationalityProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
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
import ch.unibas.medizin.osce.client.managed.request.SkillHasApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleRequest;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialProxy;
import ch.unibas.medizin.osce.client.managed.request.UsedMaterialRequest;
import ch.unibas.medizin.osce.client.managed.ui.MaterialListProxyRenderer;
import ch.unibas.medizin.osce.client.style.resources.LearningObjectiveData;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import ch.unibas.medizin.osce.shared.MaritalStatus;
import ch.unibas.medizin.osce.shared.MaterialUsedFromTypes;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.PossibleFields;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.RoleParticipantTypes;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.WorkPermission;
import ch.unibas.medizin.osce.shared.util;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.requestfactory.shared.Violation;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
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
		StandardizedPatientAdvancedSearchGenderPopupView.Delegate,
		RoleRoleParticipantSubView.Delegate, 
		RoleKeywordSubView.Delegate,
		ImportTopicPopupView.Delegate,DragHandler,
		
		RoleDetailsChecklistSubViewChecklistTopicItemView.Delegate,
		RoleDetailsChecklistSubViewChecklistQuestionItemView.Delegate,
		
		RoleDetailsChecklistSubViewChecklistCriteriaItemView.Delegate,
		RoleDetailsChecklistSubViewChecklistOptionItemView.Delegate,
		RoleBaseTableItemView.Delegate,RoleBaseTableItemValueView.Delegate,
		// Issue Role Module
		RoomMaterialsPopupView.Delegate,
		// E Issue Role Module
		//learning objective
		RoleLearningSubView.Delegate,
		RoleLearningPopUpView.Delegate,
		StandardizedPatientAdvancedSearchProfessionPopup.Delegate, 
		StandardizedPatientAdvancedSearchWorkPermissionPopup.Delegate,
		StandardizedPatientAdvancedSearchMaritialStatusPopupView.Delegate,
		LearningObjectiveView.Delegate,RoleOsceSemesterSubView.Delegate		

{

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private RoleDetailsView view; // --
	private RoleDetailsPlace place;
	private StandardizedRoleProxy standardizedRoleProxy;
	//ScrolledTab Changes start
	//private TabPanel roleDetailTabPanel;
	private ScrolledTabLayoutPanel roleDetailTabPanel;
	//ScrolledTab Changes end
	public Iterator<StandardizedRoleProxy> stRoleIterator;
	public RoleDetailsActivity roleDetailActivity;
	public RoleDetailsChecklistSubViewChecklistCriteriaItemView tempCriView;
	public RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl tempQueView;
	
	//try 
	int arrayIndx = 0;
	public StandardizedRoleDetailsView[] standardizedRoleDetailsView_sample;
	
	public StandardizedRoleDetailsViewImpl[] standardizedRoleDetailsView;
	public RoleEditCheckListSubViewImpl[] roleEditCheckListSubView;
	
	private static int selecTab = 0;

	private RoleDetailsChecklistSubViewChecklistTopicItemViewImpl reViewImpl;
	// Assignment I
	public String selectedKeyword;
		private PopupPanel toolTip;
		private HorizontalPanel toolTipContentPanel;
		private TextBox toolTipTextBox;
		private IconButton toolTipChange;
		//private final OsceConstants constants = GWT.create(OsceConstants.class);
		private RoleBaseTableItemViewImpl[] roleBaseTableItemViewImpl;
		// End I
	//Assignment E[
	public RoleTopicProxy roleTopicProxy;
	
	//AssignmentE]
	
	//issue
		private StandardizedPatientAdvancedSearchProfessionPopup professionPopup;
		private StandardizedPatientAdvancedSearchWorkPermissionPopup workPermissionPopup;
		private StandardizedPatientAdvancedSearchMaritialStatusPopupView maritialStausPopup;
		private int srcOtionIndexOnDragStart=0;
		private int srcTopicIndexOnDragStart=0;
		private int srcQuestionIndexOnDragStart=0;
		private int srcCriteriaIndexOnDragStart=0;
	// SPEC START =
	
	// Highlight onViolation
	public int mapVar=0;
	// E Highlight onViolation
	//public static RoleActivity roleActivity;
RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl checklistCriteriaItemViewImpl;
	public RoleRoleParticipantSubViewImpl roleRoleParticipantSubViewImpl;
	public RoleDetailsViewImpl roleDetailsViewImpl;
	public RoleKeywordSubViewImpl roleKeywordSubViewImpl;
	
	private CellTable<RoleParticipantProxy> authorTable;
	private CellTable<RoleParticipantProxy> reviewerTable;


	KeywordProxy selKeywordProxy;
	
	public CheckListTopicPopupView topicPopup;
	
	RoleDetailsChecklistSubViewChecklistTopicItemView checklistTopicItemView;
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
	private StandardizedPatientAdvancedSearchGenderPopupView genderPopup;
	/** Holds the table with the advanced search criteria */
	private CellTable<AdvancedSearchCriteriaProxy> advancedSearchPatientTable[];
	private StandartizedPatientAdvancedSearchSubView advancedSearchSubViews[];


	private HandlerRegistration rangeAdvanceSearchTableChangeHandler;

	private SingleSelectionModel<AdvancedSearchCriteriaProxy> selectionAdvanceSearchModel;

	RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl checklistQuestionItemViewImpl;
	
	private int questionCount=0;
	// ]Assignment F
	
	//learning
		 private Long mainClassificationId = null;
		 private Long classificaitonTopicId = null;
		 private Long topicId = null;
		 private Long skillLevelId = null;
		 private Long applianceId = null;
		 
		 private LearningObjectiveView learningObjectiveView;
		 private RoleOsceSemesterSubView roleOsceSemesterSubView; 
		 
		 private List<LearningObjectiveData> learningObjectiveData = new ArrayList<LearningObjectiveData>();
		 private LearningObjectiveData learningObjective;
		 
		 String temp = "";

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
	
	private void initLoading(){
		ApplicationLoadingScreenEvent.initialCounter();
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
//						Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
					}
				});

	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("RoleDetailsActivity.start()");
		RoleDetailsView roleDetailsView = new RoleDetailsViewImpl();
		roleDetailsView.setPresenter(this);
		this.widget = panel;
		this.view = roleDetailsView;
		this.roleDetailActivity = this;
		
		initLoading();

		
		checklistTopicItemView = new RoleDetailsChecklistSubViewChecklistTopicItemViewImpl();
	
		roleDetailTabPanel = (ScrolledTabLayoutPanel)view.getRoleDetailTabPanel();
		

		// SPEC START =
		roleDetailsViewImpl = new RoleDetailsViewImpl();
		// SPEC END =
		
		widget.setWidget(roleDetailsView.asWidget());
		view.setDelegate(this);
		
		// SPEC START
		//requests.find(place.getProxyId()).with("standardizedRoles","standardizedRoles.keywords").fire(new InitializeActivityReceiver());
		showApplicationLoading(true);
		requests.find(place.getProxyId()).with("standardizedRoles","standardizedRoles.checkList").fire(new InitializeActivityReceiver());
		showApplicationLoading(false);
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
		
		roleOsceSemesterSubView = standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleOsceSemesterSubViewImpl();
		roleOsceSemesterSubView.setDelegate(this);

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
						//ScrolledTab Changes start
						/*
						init(standRoleProxy.getId(), roleDetailTabPanel
								.getTabBar().getSelectedTab());
						*/
						init(standRoleProxy.getId(), roleDetailTabPanel
								.getSelectedIndex());
						//ScrolledTab Changes end

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
						//ScrolledTab Changes start
						/*
						init(standRoleProxy.getId(), roleDetailTabPanel
								.getTabBar().getSelectedTab());
						*/
						init(standRoleProxy.getId(), roleDetailTabPanel
								.getSelectedIndex());
						//ScrolledTab Changes end
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
				//ScrolledTab Changes start
				/*init(standRoleProxy.getId(), roleDetailTabPanel.getTabBar()
						.getSelectedTab());*/
				init(standRoleProxy.getId(), roleDetailTabPanel.getSelectedIndex());
				//ScrolledTab Changes end
			}
		});

	}

	@Override
	public void changeFilterTitleShown(String selectedTitle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newFileClicked(String fileName, String fileDescription,
			final StandardizedRoleProxy proxy,final FormPanel formPanel) {		
		// TODO Auto-generated method stub
		
		Log.info("call newFileClicked");

		if (fileName != null) {
			//ScrolledTab Changes start
			//sortOrder = fileTable[roleDetailTabPanel.getTabBar().getSelectedTab()].getRowCount() + 1;
			sortOrder = fileTable[roleDetailTabPanel.getSelectedIndex()].getRowCount() + 1;
			//ScrolledTab Changes end
			Log.debug("Add File");

			FileRequest fileReq = requests.fileRequest();
			FileProxy file = fileReq.create(FileProxy.class);
			// reques.edit(scar);
			file.setPath(fileName);
			file.setDescription(fileDescription);
			file.setSortOrder(sortOrder);
			file.setStandardizedRole(proxy);

			// Highlight onViolation
			Log.info("Map Size:"+ standardizedRoleDetailsView[selecTab].getRoleFileSubViewImpl().getFileMap().size());
			fileReq.persist().using(file).fire(new OSCEReceiver<Void>(standardizedRoleDetailsView[selecTab].getRoleFileSubViewImpl().getFileMap()) {
			// E Highlight onViolation
				@Override
				public void onSuccess(Void arg0) 
				{
					//ScrolledTab Changes start
					//init(proxy.getId(), roleDetailTabPanel.getTabBar().getSelectedTab());
					init(proxy.getId(), roleDetailTabPanel.getSelectedIndex());
					formPanel.submit();
					//ScrolledTab Changes end
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
		Log.info("Call newUsedMaterialButtonClicked");
		
		UsedMaterialRequest usedMaterialRequest = requests
				.usedMaterialRequest();

		UsedMaterialProxy usedMaterialProxy = usedMaterialRequest
				.create(UsedMaterialProxy.class);
		usedMaterialProxy.setMaterialCount(materialCount);

		usedMaterialProxy.setUsed_from(used_from);

		usedMaterialProxy.setStandardizedRole(standardizedRoleProxy);
		usedMaterialProxy.setMaterialList(materialList);
		//ScrolledTab Changes start
		/*usedMaterialProxy.setSort_order(usedMaterialTable[roleDetailTabPanel
				.getTabBar().getSelectedTab()].getRowCount() + 1);*/
		usedMaterialProxy.setSort_order(usedMaterialTable[roleDetailTabPanel
		                                  				.getSelectedIndex()].getRowCount() + 1);
		//ScrolledTab Changes end
		

		// Highlight onViolation
		/*	Log.debug("Add UsedMaterial (" + usedMaterialProxy.getMaterialCount()
					+ " - id " + usedMaterialProxy.getId() + " "
					+ usedMaterialProxy.getMaterialList().getName()
					+ usedMaterialProxy.getStandardizedRole().getShortName());*/
		Log.info("Map Size: "+standardizedRoleDetailsView[selecTab].getRoomMaterialsDetailsSubViewImpl().getUsedMaterialMap().size());
		usedMaterialRequest.persist().using(usedMaterialProxy).fire(new OSCEReceiver<Void>(standardizedRoleDetailsView[selecTab].getRoomMaterialsDetailsSubViewImpl().getUsedMaterialMap()) 
		{
				// E Highlight onViolation
					@Override
					public void onSuccess(Void response) {
						Log.debug("UsedMaterialReceiver Added successfully");
						standardizedRoleDetailsView[selecTab].getRoomMaterialsDetailsSubViewImpl().getRoomMaterialsPopupViewImpl().hide();
						//ScrolledTab Changes start
						//initUsedMaterial2(standardizedRoleProxy.getId(),roleDetailTabPanel.getTabBar().getSelectedTab());
						initUsedMaterial2(standardizedRoleProxy.getId(),roleDetailTabPanel.getSelectedIndex());
						//ScrolledTab Changes end
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

		Log.info("Call fireUsedMaterialCountRequest");		
		requests.usedMaterialRequestNonRoo().countUsedMaterialsByStandardizedRoleID(standardizedRoleID).fire(callback);
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
						
						//ScrolledTab Changes start
						/*
						initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
						*/
						initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getSelectedIndex());
						
						//ScrolledTab Changes end
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
						
						//ScrolledTab Changes start
						/*initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
						*/
						initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getSelectedIndex());
						
						//ScrolledTab Changes start
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
						
						//ScrolledTab Changes start
						/*initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
						*/
						initUsedMaterial2(standardizedRoleProxy.getId(),
								roleDetailTabPanel.getSelectedIndex());
						
						//ScrolledTab Changes start
					}
				});
	}
	
	public void refreshSelectedTab(final StandardizedRoleDetailsViewImpl view,StandardizedRoleProxy standardizedRoleProxy)
	{
		
		showApplicationLoading(true);
		requests.standardizedRoleRequest().findStandardizedRole(standardizedRoleProxy.getId()).with("oscePosts","roleTopic","simpleSearchCriteria","roleParticipants","advancedSearchCriteria","roleTemplate","keywords","previousVersion","checkList","checkList.checkListTopics","checkList.checkListTopics.checkListQuestions","checkList.checkListTopics.checkListQuestions.checkListCriterias","checkList.checkListTopics.checkListQuestions.checkListOptions").fire(new OSCEReceiver<StandardizedRoleProxy>() {

			@Override
			public void onSuccess(StandardizedRoleProxy proxy) {
				// TODO Auto-generated method stub
				
				showApplicationLoading(true);
		
		
		int index=roleDetailTabPanel.getSelectedIndex();
		// Boolean object has to be checked for null value?
		if(proxy.getActive().booleanValue() == true) 	{
		
			view.checkListsVerticalPanel.clear();

		standardizedRoleDetailsView[index] = view;
		System.out.println("length previous version--"+proxy.getPreviousVersion());
		if(proxy.getPreviousVersion()==null)
		{
			standardizedRoleDetailsView[index].previous.setEnabled(false);
		}
		else
			standardizedRoleDetailsView[index].previous.setEnabled(true);
		
		if(proxy.getOscePosts().size()>0)
			standardizedRoleDetailsView[index].delete.setEnabled(false);
		else
			standardizedRoleDetailsView[index].delete.setEnabled(true);
		
		standardizedRoleDetailsView[index].edit.setEnabled(true);

		standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].setHomeRole(proxy);
		standardizedRoleDetailsView[index].setBaseProxy(proxy);
		standardizedRoleDetailsView[index].home.setVisible(false);
		standardizedRoleDetailsView[index].setValue(proxy);
		//roleDetailTabPanel.insert(standardizedRoleDetailsView[index],"" + proxy.getShortName(), index);
		standardizedRoleDetailsView[index].shortName.setInnerText(proxy.getShortName() == null ? "": String.valueOf(proxy.getShortName()));						
		standardizedRoleDetailsView[index].longName.setInnerText(proxy.getLongName() == null ? "": String.valueOf(proxy.getLongName()));
		// Issue Role
		standardizedRoleDetailsView[index].roleType.setInnerText(proxy.getRoleType() == null ? "": proxy.getRoleType().name()); // ADDED
		standardizedRoleDetailsView[index].studyYear.setInnerText(new EnumRenderer<StudyYears>().render(proxy.getStudyYear())); // ADDED
		standardizedRoleDetailsView[index].factor.setInnerText(proxy.getFactor() == null ? "": proxy.getFactor().toString());
		standardizedRoleDetailsView[index].sum.setInnerText(proxy.getSum() == null ? "": proxy.getSum().toString());
		
		// E: Issue Role
		standardizedRoleDetailsView[index].labelLongNameHeader.setText("" + proxy.getLongName()+ " ("+proxy.getMainVersion()+"."+proxy.getSubVersion()+")");

		//setRoleDetailTabData(proxy, response, index);
		standardizedRoleDetailsView[index].rolePanel.selectTab(0);
		standardizedRoleDetailsView[index].setDelegate(roleDetailActivity);

		// Assignment :H[
		fileView[index] = standardizedRoleDetailsView[index].getRoleFileSubViewImpl();
		fileView[index].setValue(proxy);
		fileView[index].setDelegate(roleDetailActivity);
		setFileTable(fileView[index].getTable(), proxy.getId(),index);
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

		roomMaterialsDetailsSubView[index].setValue(proxy);
	// Issue Role Module
		roomMaterialsDetailsSubView[index].getRoomMaterialsPopupViewImpl().setDelegate(roleDetailActivity);
		roomMaterialsDetailsSubView[index].getRoomMaterialsPopupViewImpl().setMaterialListPickerValues(Collections.<MaterialListProxy> emptyList());						
	//E Issue Role Module
	
final int index2 = index;

			requests.materialListRequest().findMaterialListEntries(0, 50).with(MaterialListProxyRenderer.instance().getPaths()).fire(new Receiver<List<MaterialListProxy>>() {
					public void onSuccess(List<MaterialListProxy> response) 
					{
						List<MaterialListProxy> values = new ArrayList<MaterialListProxy>();
						values.add(null);
						values.addAll(response);
						// Issue Role Module
						roomMaterialsDetailsSubView[index2].getRoomMaterialsPopupViewImpl().setMaterialListPickerValues(values);										
						// roomMaterialsDetailsSubView[index2].setMaterialListPickerValues(values);
						// E Issue Role Module
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
		
		requests.osceRequestNonRoo().findAllOsceSemester(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId(),roleOsceSemesterSubView.getStartDate().getValue() , roleOsceSemesterSubView.getEndDate().getValue()).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() {

			@Override
			public void onSuccess(List<OsceProxy> response) {
				// TODO Auto-generated method stub
				if(response!=null)
				{
					
				
				roleOsceSemesterSubView.getOsceSemesterTable().setRowCount(response.size());
				roleOsceSemesterSubView.getOsceSemesterTable().setRowData(response);
				}
				else
				{
					Log.info("record not found");
				}
				
			}
		});
		
		standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().setDelegate(roleDetailActivity);
		standardizedRoleDetailsView[index].getRoleKeywordSubViewImpl().setDelegate(roleDetailActivity);
		authorTable = standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().authorTable; // ==>
		reviewerTable = standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().authorTable; // ==>
	
		ProvidesKey<RoleParticipantProxy> autkeyProvider = ((AbstractHasData<RoleParticipantProxy>) authorTable).getKeyProvider();
		
		final int innerindex = index;
		final int getStandardizedRole = Integer	.parseInt(standardizedRoleDetailsView[index].getValue().getId().toString());
		//auther and reviewer table changes start
		
		
		requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 0).with("doctor").fire(new Receiver<Long>() 
		{
				@Override
				public void onSuccess(Long response) 
				{
						Log.info("~Success Call....");
						Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
						Log.info("~Set Data In Author Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response);
						standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.setRowCount(response.intValue(),true);
					}
		});
		
		final Range autherRange = standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.getVisibleRange();
		
		requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 0,autherRange.getStart(),autherRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
		{
				@Override
				public void onSuccess(List<RoleParticipantProxy> response) 
				{
						Log.info("~Success Call....");
						Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
						Log.info("~Set Data In Author Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response.size());
						standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.setRowData(autherRange.getStart(),response);
				}
		});

		requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 1).with("doctor").fire(new Receiver<Long>() 
				{
					@Override
					public void onSuccess(Long response) 
					{
						Log.info("~Success Call....");
						Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
						Log.info("~Set Data In Reviewer Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response);
						standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowCount(response.intValue(),true);
					}
				});
		
		final Range reviewerRange = standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.getVisibleRange();
		requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 1,reviewerRange.getStart(),reviewerRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
		{
			@Override
			public void onSuccess(List<RoleParticipantProxy> response) 
			{
				Log.info("~Success Call....");
				Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
				Log.info("~Set Data In Reviewer Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response.size());
				standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowData(reviewerRange.getStart(),response);
			}
		});

		
		standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[innerindex].getValue().getId(), 1).with("doctor").fire(new Receiver<Long>() 
						{
							@Override
							public void onSuccess(Long response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response);
								standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowCount(response.intValue(),true);
							}
						});
				
				final Range reviewerRange = standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.getVisibleRange();
				requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[innerindex].getValue().getId(), 1,reviewerRange.getStart(),reviewerRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
				{
					@Override
					public void onSuccess(List<RoleParticipantProxy> response) 
					{
						Log.info("~Success Call....");
						Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
						Log.info("~Set Data In Reviewer Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response.size());
						standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowData(reviewerRange.getStart(),response);
					}
				});
				
				}
		});
		
		standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[innerindex].getValue().getId(), 0).with("doctor").fire(new Receiver<Long>() 
						{
								@Override
								public void onSuccess(Long response) 
								{
										Log.info("~Success Call....");
										Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
										Log.info("~Set Data In Author Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response);
										standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.setRowCount(response.intValue(),true);
									}
						});
						
						final Range autherRange = standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.getVisibleRange();
						
						requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[innerindex].getValue().getId(), 0,autherRange.getStart(),autherRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
						{
								@Override
								public void onSuccess(List<RoleParticipantProxy> response) 
								{
										Log.info("~Success Call....");
										Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
										Log.info("~Set Data In Author Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response.size());
										standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.setRowData(autherRange.getStart(),response);
								}
						});
				
				}
		});
		//auther and reviewer table changes end
		
		//Log.info("==>>>Index: "+ innerindex+ " ID Pass: "+ ((RoleTopicProxy) response).getId()+ "SR ID: "+ standardizedRoleDetailsView[innerindex].getValue().getId());

		// Issue Role
		// V4
		//requests.doctorRequest().findAllDoctors().fire(new Receiver<List<DoctorProxy>>() {
		requests.doctorRequestNonRoo().findDoctorWithRoleTopic(standardizedRoleDetailsView[innerindex].getValue().getId()).fire(new Receiver<List<DoctorProxy>>() 
		{ 
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
							//Issue Role
							//standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible();
						}
						else
						{
							//Issue Role
							//standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible(true);
							//Issue # 122 : Replace pull down with autocomplete.
							
							//standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setAcceptableValues(response);
							
							DefaultSuggestOracle<DoctorProxy> suggestOracle1 = (DefaultSuggestOracle<DoctorProxy>) standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.getSuggestOracle();
							suggestOracle1.setPossiblilities(response);


							standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setSuggestOracle(suggestOracle1);

						//	standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setRenderer(new DoctorProxyRenderer());
							
							standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setRenderer(new AbstractRenderer<DoctorProxy>() {

								@Override
								public String render(DoctorProxy object) {
									// TODO Auto-generated method stub
									if(object!=null)
									{
									return object.getName()+"";
									}
									else
									{
										return "";
									}
								}
							});

							//Issue # 122 : Replace pull down with autocomplete.
						}
						// SPEC END MODIFIED =
					}

				});

		requests.keywordRequestNonRoo().findKeywordByStandRoleCount(standardizedRoleDetailsView[index].getValue()).fire(new Receiver<Long>()						 
		{
				@Override
				public void onSuccess(Long response) 
				{									
					Log.info("~Success Call....");
					Log.info("~findKeywordByStandRole()");
					Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.intValue());
					
					standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.intValue(), true);
					
					/*Range keywordTableRange= standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
					standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.size(),true);
					standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
					
					standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);*/
					//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
				
		final Range keywordRange = standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
		requests.keywordRequestNonRoo().findKeywordByStandRole(standardizedRoleDetailsView[innerindex].getValue(),keywordRange.getStart(),keywordRange.getLength()).fire(new Receiver<List<KeywordProxy>>()						 
				{
						@Override
						public void onSuccess(List<KeywordProxy> response) 
						{									
							Log.info("~Success Call....");
							Log.info("~findKeywordByStandRole()");
							Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.size());
							
							
							
							//Range keywordTableRange= standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
							//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.size(),true);
							//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
							//table.setRowData(range.getStart(), values);
							standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
							//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
						}
				});
				}
		});
		standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				requests.keywordRequestNonRoo().findKeywordByStandRoleCount(standardizedRoleDetailsView[innerindex].getValue()).fire(new Receiver<Long>()						 
						{
								@Override
								public void onSuccess(Long response) 
								{									
									Log.info("~Success Call....");
									Log.info("~findKeywordByStandRole()");
									Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.intValue());
									
									standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.intValue(), true);
									
									/*Range keywordTableRange= standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
									standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.size(),true);
									standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
									
									standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);*/
									//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
								
						final Range keywordRange = standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
						requests.keywordRequestNonRoo().findKeywordByStandRole(standardizedRoleDetailsView[innerindex].getValue(),keywordRange.getStart(),keywordRange.getLength()).fire(new Receiver<List<KeywordProxy>>()						 
		{
				@Override
				public void onSuccess(List<KeywordProxy> response) 
				{									
					Log.info("~Success Call....");
					Log.info("~findKeywordByStandRole()");
					Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.size());
											
											
											
											//Range keywordTableRange= standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
											//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.size(),true);
											//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
											//table.setRowData(range.getStart(), values);
											//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
											standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
											//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
										}
								});
								}
						});
				
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

		standardizedRoleDetailsView[index].getRoleLearningSubViewImpl().setDelegate(roleDetailActivity);
		
		final int innerindex2 = index;
		
		//main skill change
		
		requests.mainSkillRequestNonRoo().countMainSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {								
				/*standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.intValue(),true);
				Log.info("~~DATA SET");
			}
		});
		final Range mainSkillRange = standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.getVisibleRange();
		requests.mainSkillRequestNonRoo().findMainSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId(),mainSkillRange.getStart(),mainSkillRange.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MainSkillProxy>>() {

			@Override
			public void onSuccess(List<MainSkillProxy> response) {								
			/*	standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(mainSkillRange.getStart(),response);
				Log.info("~~DATA SET");
			}
		});
					
		standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				
				requests.mainSkillRequestNonRoo().countMainSkillEntriesByRoleID(standardizedRoleDetailsView[innerindex2].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<Long>() {

					@Override
					public void onSuccess(Long response) {								
						/*standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.intValue(),true);
						Log.info("~~DATA SET");
					}
				});
				final Range mainSkillRange = standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.getVisibleRange();
				requests.mainSkillRequestNonRoo().findMainSkillEntriesByRoleID(standardizedRoleDetailsView[innerindex2].getValue().getId(),mainSkillRange.getStart(),mainSkillRange.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MainSkillProxy>>() {

					@Override
					public void onSuccess(List<MainSkillProxy> response) {								
					/*	standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(mainSkillRange.getStart(),response);
						Log.info("~~DATA SET");
					}
				});
				
				}
		});

		//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
		//main skill change end
		
		
		//minor skill start
		requests.minorSkillRequestNonRoo().countMinorSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {								
				/*standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowCount(response.intValue(),true);
				Log.info("~~DATA SET");
			}
		});
		final Range minorSkillRange = standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.getVisibleRange();
		requests.minorSkillRequestNonRoo().findMinorSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId(),minorSkillRange.getStart(),minorSkillRange.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MinorSkillProxy>>() {

			@Override
			public void onSuccess(List<MinorSkillProxy> response) {								
			/*	standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowData(minorSkillRange.getStart(),response);
				Log.info("~~DATA SET");
			}
		});
					
		standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				
				requests.minorSkillRequestNonRoo().countMinorSkillEntriesByRoleID(standardizedRoleDetailsView[innerindex2].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<Long>() {

					@Override
					public void onSuccess(Long response) {								
						/*standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowCount(response.intValue(),true);
						Log.info("~~DATA SET");
					}
				});
				final Range minorSkillRange = standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.getVisibleRange();
				requests.minorSkillRequestNonRoo().findMinorSkillEntriesByRoleID(standardizedRoleDetailsView[innerindex2].getValue().getId(),minorSkillRange.getStart(),minorSkillRange.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MinorSkillProxy>>() {

					@Override
					public void onSuccess(List<MinorSkillProxy> response) {								
					/*	standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowData(minorSkillRange.getStart(),response);
						Log.info("~~DATA SET");
					}
				});
				
				}
		});

		
/*		requests.minorSkillRequestNonRoo().findMinorSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MinorSkillProxy>>() {

			@Override
			public void onSuccess(List<MinorSkillProxy> response) {
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowCount(response.size());
				Log.info("DATA IS SET FOR MINOR TABLE");
			}
		});			
		*/
		//minor skill end
		// SPEC END =
		
		//Assignment E[
		//set CheckList Title
		//Log.info(proxy.getCheckList().getTitle());
		if(proxy.getCheckList()!=null)
		{
			
		((StandardizedRoleDetailsViewImpl)standardizedRoleDetailsView[index]).roleSubPanel.getTabBar().setTabText(0,proxy.getCheckList().getTitle()==null ? constants.checkList() : constants.checkList() + " " + proxy.getCheckList().getTitle());					
		
		Log.info("checklisttopic Proxy Size:" + proxy.getCheckList().getCheckListTopics().size());
		Iterator<ChecklistTopicProxy> topicIterator=proxy.getCheckList().getCheckListTopics().iterator();
//		RoleDetailsChecklistSubViewChecklistQuestionItemView queView[]=new RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl[proxy.getCheckList().getCheckListTopics().size()];
		//create Topic View
		standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getDragController().addDragHandler(roleDetailActivity);
		roleDetailActivity.questionCount=0;
		while(topicIterator.hasNext())
		{
			ChecklistTopicProxy topicProxy=topicIterator.next();
			RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView=createCheckListTopic(index,topicProxy);
			
			//create Question View
			Iterator<ChecklistQuestionProxy> questionIterator=topicProxy.getCheckListQuestions().iterator();
			
			
			while(questionIterator.hasNext())
			{
				
				ChecklistQuestionProxy questionProxy=questionIterator.next();
				RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(index, questionProxy, topicView,++roleDetailActivity.questionCount);
				
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
		
		}
		//Assignment E]
		
		
////Initialise RoleScript (I) Assignment I
		
		
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
		
		//temp[
		initRoleScript(standarDizedViewIndex,proxy.getRoleTemplate(),false,false);
		/* 
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
		});*/
		
		//temp]
		
		// End I
		

	}
		showApplicationLoading(false);
			}
		});
		
		showApplicationLoading(false);
	}
	
	// ]Assignment G

	public class InitializeActivityReceiver extends OSCEReceiver<Object> {
		@Override
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
		}

		@Override
		public void onSuccess(Object response) {
			showApplicationLoading(true);
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
					int totalrole = roleDetailTabPanel.getWidgetCount(); // Total Role Tabs
					
					int size = ((RoleTopicProxy) response).getStandardizedRoles().size(); // Total Size of Data (standardized_role)
					
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
						
						roleDetailTabPanel.insert(standardizedRoleDetailsView[index],"" + proxy.getShortName(), index);
						/*if(index==0)
						{
							refreshSelectedTab(standardizedRoleDetailsView[index], proxy);
						}*/
						index++;

					}
					}
					//this will fire role selected event and refresh selected tab
					roleDetailTabPanel.selectTab(selecTab);
					view.setStandardizedRoleDetailsViewImpl((StandardizedRoleDetailsViewImpl[]) standardizedRoleDetailsView);

				} else {
					System.out.println("Sorry No Roles Aveilable");
				}

				init(((RoleTopicProxy) response));
			}
			
			showApplicationLoading(false);
		}
	}
	
	public void initRoleScript(final int index, final RoleTemplateProxy roleTemplate, final boolean create,final boolean isPrevious)
	{		
		//final int index = view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
	//	requests.roleBaseItemRequestNoonRoo().findRoleBaseItemByStandardizedRole(standardizedRoleDetailsView[index].getValue().getId()).with("roleSubItem","roleItemAccess").fire(new Receiver<List<RoleBaseItemProxy>>() {
		if(roleTemplate!=null)
		{
			requests.roleBaseItemRequestNoonRoo().findRoleBaseItemByStandardizedRoleAndRoleTemplateId(standardizedRoleDetailsView[index].getValue().getId(),roleTemplate.getId()).with("roleSubItem","roleItemAccess").fire(new Receiver<List<RoleBaseItemProxy>>() {
	
				@Override
				public void onSuccess(List<RoleBaseItemProxy> response) {
					
					if(response != null && response.size() > 0 ){
					
						Log.info("Response Size" + response.size());
						
						standardizedRoleDetailsView[index].getRoleBaseItemVerticalPanel().clear();
						
						Iterator<RoleBaseItemProxy> listRoleBaseItemProxy = response.iterator();
					
						while(listRoleBaseItemProxy.hasNext())
						{
							final RoleBaseTableItemValueViewImpl roleBaseTableItemViewImpl=new RoleBaseTableItemValueViewImpl();
							roleBaseTableItemViewImpl.description.setEnabled(false);
							roleBaseTableItemViewImpl.setDelegate(roleDetailActivity);
							roleBaseTableItemViewImpl.description.setEnabled(false);
							RoleBaseItemProxy roleBaseItemProxy = listRoleBaseItemProxy.next();
							
							if(roleBaseItemProxy.getItem_defination().name().equals("table_item"))
							{
								Log.info("Found Role_Table_Item");
								if(roleBaseItemProxy.getDeleted())
									continue;
								roleBaseTableItemViewImpl.setValue(roleBaseItemProxy);						
								roleBaseTableItemViewImpl.description.setEnabled(false);
								
								
								standardizedRoleDetailsView[index].getRoleBaseItemVerticalPanel().add(roleBaseTableItemViewImpl);
								roleBaseTableItemViewImpl.description.setEnabled(false);
								requests.roleTableItemValueRequestNonRoo().findRoleTableItemValueByStandardizedRoleANDRoleBaseItemValues(standardizedRoleDetailsView[index].getValue().getId(), roleBaseItemProxy.getId()).with("roleTableItem").fire(new Receiver<List<RoleTableItemValueProxy>>() {
		
									@Override
									public void onSuccess(
											List<RoleTableItemValueProxy> response) {
										if(response!=null)
										{
										Range range = roleBaseTableItemViewImpl.getTable().getVisibleRange();										
										roleBaseTableItemViewImpl.getTable().setRowCount(response.size());
										roleBaseTableItemViewImpl.getTable().setRowData(range.getStart(),response);
										
										//SPEC Change
										
										if(isPrevious){
											roleBaseTableItemViewImpl.removeLastColumn();
										}
										//SPEC Change
										
										roleBaseTableItemViewImpl.description.setEnabled(false);
										}
										
									}
								});
							
								
								
								roleBaseTableItemViewImpl.toolbar.removeFromParent();
								roleBaseTableItemViewImpl.description.removeFromParent();
								roleBaseTableItemViewImpl.addRichTextAreaValue.removeFromParent();
								roleBaseTableItemViewImpl.description.setEnabled(false);
								// To Add Access Values	
								addAccessValue(roleBaseItemProxy,roleBaseTableItemViewImpl);
							}
							else
							{
								Log.info("Found Role_RichText_Area");
								roleBaseTableItemViewImpl.description.setEnabled(false);
								if(roleBaseItemProxy.getDeleted())
									continue;
								roleBaseTableItemViewImpl.setValue(roleBaseItemProxy);
								roleBaseTableItemViewImpl.description.setEnabled(false);
								
								if(roleBaseItemProxy.getRoleSubItem() != null)
								{
									//roleBaseTableItemViewImpl.description.setText(roleBaseItemProxy.getRoleSubItem().iterator().next().getItemText());
									roleBaseTableItemViewImpl.description.setEnabled(false);
									roleBaseTableItemViewImpl.descriptionValue.setInnerHTML(roleBaseItemProxy.getRoleSubItem().iterator().next().getItemText());
									roleBaseTableItemViewImpl.description.setHTML(roleBaseItemProxy.getRoleSubItem().iterator().next().getItemText());
									roleBaseTableItemViewImpl.description.setEnabled(false);
								}
								// To remove extra RoleBase Item Proxy view Component
								roleBaseTableItemViewImpl.description.setEnabled(false);
								roleBaseTableItemViewImpl.roleBaseItemDisclosurePanel.setStyleName("border=0");
								roleBaseTableItemViewImpl.table.removeFromParent();
								
								//	view.getTableItem().add(roleBaseTableItemViewImpl);
								roleBaseTableItemViewImpl.description.setEnabled(false);
								standardizedRoleDetailsView[index].getRoleBaseItemVerticalPanel().add(roleBaseTableItemViewImpl);
								
								
								// To Add Access Values	
									addAccessValue(roleBaseItemProxy,roleBaseTableItemViewImpl);
							}
							
							//SPEC Change
							roleBaseTableItemViewImpl.description.setEnabled(false);
							if(isPrevious){
//								roleBaseTableItemViewImpl.toolbar.setVisible(false);
								roleBaseTableItemViewImpl.description.setEnabled(false);
								roleBaseTableItemViewImpl.addRichTextAreaValue.setEnabled(false);
								roleBaseTableItemViewImpl.saveRichTextArea.setEnabled(false);
							}
							
							//SPEC Change
							roleBaseTableItemViewImpl.description.setEnabled(false);
						}
	
					
					}
					/*else{
						if(create)
						{
							createDefaultSubValueItem(roleTemplate);
							Log.info("Rolebase item value assigne successfully");
							//initRoleScript(index, roleTemplate,false);
							Log.info("Rolebase item refresh successfully");
					}
					}*/
					
				}
			});
		}
}
	
	//assignment E [
		public void roleTopicInit(final ChecklistTopicProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView)
		{
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();//gets selected tab of standardized role
			//ScrolledTab Changes start
			System.out.println("Checklist Id is "+standardizedRoleDetailsView[selectedtab].getValue().getCheckList().getId());
			requests.checkListRequest().findCheckList(standardizedRoleDetailsView[selectedtab].getValue().getCheckList().getId()).with("checkListTopics").with("checkListTopics.checkListQuestions").with("checkListTopics.checkListQuestions.checkListCriterias").with("checkListTopics.checkListQuestions.checkListOptions").fire(new Receiver<CheckListProxy>() {

				@Override
				public void onSuccess(CheckListProxy response) {
					Log.info("checklisttopic Proxy Size:" + response.getCheckListTopics().size());
					Iterator<ChecklistTopicProxy> topicIterator=response.getCheckListTopics().iterator();
					
					//create Topic View
					int i=0;
					standardizedRoleDetailsView[selectedtab].checkListsVerticalPanel.clear();
					//standardizedRoleDetailsView[selectedtab].checkListAP.clear();
					
					topicView.getDragController().makeDraggable(topicView.getqueVP());
					roleDetailActivity.questionCount=0;
					while(topicIterator.hasNext())
					{
						ChecklistTopicProxy topicProxy=topicIterator.next();
						RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView=createCheckListTopic(selectedtab,topicProxy);
						//topicView=createCheckListTopic(selectedtab,topicProxy);
						reViewImpl = topicView;
						//create Question View
						Iterator<ChecklistQuestionProxy> questionIterator=topicProxy.getCheckListQuestions().iterator();
						
						
						while(questionIterator.hasNext())
						{
							ChecklistQuestionProxy questionProxy=questionIterator.next();
							RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(selectedtab, questionProxy, topicView,++roleDetailActivity.questionCount);
							
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

	public void setRoleDetailTabData(StandardizedRoleProxy proxy,Object response, int index) {
		// Issue Role
		
		//System.out.println("===============================>" + "" + index+ proxy.getShortName());
		//System.out.println("===============================>" + "" + index+ proxy.getLongName());
		//System.out.println("===============================>" + "" + index+ proxy.getRoleType().valueOf(proxy.getRoleType().name()));
		//System.out.println("===============================>" + "" + index+ proxy.getStudyYear().valueOf(proxy.getStudyYear().name()));

		//E:  Issue Role
	}

	private void init(RoleTopicProxy proxy) {
		//ScrolledTab Changes start
		//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
		final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();
		//ScrolledTab Changes end
		Log.info("Init Selected for tab" +selectedtab);
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	// Issue : 120 : Start
	@Override
	public void printRoleClicked(
			StandardizedRolePrintFilterViewImpl standardizedRolePrintFilterViewImpl) {
		Log.info("Print clicked");
		Long selectedRoleItemAccess = -1L;
		if (standardizedRolePrintFilterViewImpl.getSelectedRoleItemAccess() != null
				&& standardizedRolePrintFilterViewImpl
						.getSelectedRoleItemAccess().getName() != null) {
			selectedRoleItemAccess = (standardizedRolePrintFilterViewImpl
					.getSelectedRoleItemAccess().getName().trim()
					.compareTo(constants.all().trim()) == 0) ? 0L
					: standardizedRolePrintFilterViewImpl
							.getSelectedRoleItemAccess().getId();
		}
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
				
		StringBuilder requestData = new StringBuilder();
		String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.STANDARDIZED_ROLE.ordinal()));
		requestData.append(ResourceDownloadProps.ENTITY).append("=").append(ordinal).append("&")
					.append(ResourceDownloadProps.ID).append("=").append(URL.encodeQueryString(standardizedRolePrintFilterViewImpl
								.getStandardizedRoleProxy().getId().toString())).append("&");
					
		for (String filter : standardizedRolePrintFilterViewImpl.getFilters()) {
			requestData.append(ResourceDownloadProps.FILTER).append("=").append(URL.encodeQueryString(filter)).append("&");	
		}
		requestData.append(ResourceDownloadProps.SELECTED_ROLE_ITEM_ACCESS).append("=").append(URL.encodeQueryString(selectedRoleItemAccess.toString())).append("&");
		requestData.append(ResourceDownloadProps.LOCALE).append("=").append(URL.encodeQueryString(locale));
		
		String url = GWT.getHostPageBaseURL() + "downloadFile?" + requestData.toString(); 
		Log.info("--> url is : " +url);
		Window.open(url, "", "");
		
		
		
//		requests.standardizedRoleRequestNonRoo()
//				.getRolesPrintPdfBySearch(
//						standardizedRolePrintFilterViewImpl
//								.getStandardizedRoleProxy().getId(),
//						standardizedRolePrintFilterViewImpl.getFilters(),
//						selectedRoleItemAccess,locale)
//				.fire(new StandardizedRolePdfFileReceiver());
	}

	private class StandardizedRolePdfFileReceiver extends OSCEReceiver<String> {
		@Override
		public void onFailure(ServerFailure error) {
			Log.error(error.getMessage());
			// onStop();
		}

		@Override
		public void onSuccess(String response) {
			Window.open(response, "_blank", "enabled");
		}
	}

	@Override
	public void getRoleScriptListPickerValues(
			final StandardizedRolePrintFilterViewImpl standardizedRolePrintFilterViewImpl) {

		requests.roleItemAccessRequest().findAllRoleItemAccesses()
				.fire(new OSCEReceiver<List<RoleItemAccessProxy>>() {
					@Override
					public void onSuccess(List<RoleItemAccessProxy> response) {

						RoleItemAccessProxy roleItemAccessProxy = requests
								.roleItemAccessRequest().create(
										RoleItemAccessProxy.class);
						roleItemAccessProxy.setName(constants.all());

						List<RoleItemAccessProxy> roleItemAccessProxies = new ArrayList<RoleItemAccessProxy>(
								response);
						roleItemAccessProxies.add(0, roleItemAccessProxy);
						Log.info("~~~ Length of roleItemAccessProxy"
								+ roleItemAccessProxies.size());
						standardizedRolePrintFilterViewImpl
								.setRoleScriptListPickerValues(roleItemAccessProxies);
						return;
					}

					@Override
					public void onViolation(Set<Violation> errors) {
						// TODO Auto-generated method stub
						super.onViolation(errors);
					}

					@Override
					public void onFailure(ServerFailure error) {
						// TODO Auto-generated method stub
						super.onFailure(error);
					}
				});
	}

	// Issue : 120 Stop
	
	@Override
	public void editRoleClicked(StandardizedRoleProxy standardizedRoleProxy) {
		Log.info("edit clicked");
		System.out
				.println("============================Jump to StandardizedPatientDetailActivity editPatientClicked() =========================");
		
		//ScrolledTab Changes start
		/*System.out.println("==>"
				+ roleDetailTabPanel.getTabBar().getSelectedTab());
		int selTabID = roleDetailTabPanel.getTabBar().getSelectedTab();
		*/
		System.out.println("==>"
				+ roleDetailTabPanel.getSelectedIndex());
		int selTabID = roleDetailTabPanel.getSelectedIndex();
		
		//ScrolledTab Changes end
		RoleEditActivity.setSelectedTab(roleDetailTabPanel.getSelectedIndex());
		goTo(new RoleDetailsPlace(standardizedRoleProxy.stableId(),
				Operation.EDIT));
	}
	
	
	@Override
	public void copyRoleClicked(StandardizedRoleProxy standardizedRoleProxy) {
		Log.info("copy clicked");
		
		requests.standardizedRoleRequestNonRoo().copyStandardizedRole(standardizedRoleProxy.getId()).fire(new OSCEReceiver<Boolean>() {

			

			@Override
			public void onSuccess(Boolean response) {
				// TODO Auto-generated method stub
				Log.info("successfully role created--"+response);
				Log.info(" StandardizedRole created succeessfully");
				RoleEditActivity.roleActivity.setInserted(false);
				RoleEditActivity.roleActivity.initSearch();
				goTo(new RoleDetailsPlace(roleTopicProxy.stableId(),	Operation.DETAILS));
			}
		});
		//ScrolledTab Changes start
		/*System.out.println("==>"
				+ roleDetailTabPanel.getTabBar().getSelectedTab());
		int selTabID = roleDetailTabPanel.getTabBar().getSelectedTab();
		*/
		
		
		//ScrolledTab Changes end
		
	}

	@Override
	public void deleteRoleClicked(final StandardizedRoleProxy proxy) {
		Log.info("delete clicked");
		
		// Issue Role
		 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
		 dialogBox.showYesNoDialog(constants.confirmationDeleteRole());
		 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();
					
					Log.info("yes click");	
					requests.standardizedRoleRequest().remove().using(proxy)
					.fire(new Receiver<Void>() {
						public void onSuccess(Void ignore) {
							if (widget == null) {
								return;
							}
							RoleEditActivity.roleActivity.setInserted(false);
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
			});
			
				dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();
					Log.info("no click");
					return;
					
				}
			});
		// E: Issue Role
		
		
		
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
		RoleEditActivity.setSelectedTab(roleDetailTabPanel.getSelectedIndex());
		goTo(new RoleDetailsPlace(this.roleTopicProxy.stableId() ,Operation.CREATE));
	}
	
	//Assignment E[
		public RoleDetailsChecklistSubViewChecklistTopicItemViewImpl createCheckListTopic(int selectedTab,ChecklistTopicProxy proxy)
		{
			
			//saveCheckListTopic(checkListTopic);
			
			
			RoleDetailsChecklistSubViewChecklistTopicItemView view = new RoleDetailsChecklistSubViewChecklistTopicItemViewImpl();
			
			view.getDragController().addDragHandler(roleDetailActivity);
						
			
			view.setDelegate(this);
			view.setProxy(proxy);
			((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view).descriptionLbl.setText(proxy.getDescription());
			((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view).checkListTopicLbl.setText(proxy.getTitle());
			standardizedRoleDetailsView[selectedTab].checkListsVerticalPanel.insert(((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view), standardizedRoleDetailsView[selectedTab].checkListsVerticalPanel.getWidgetCount());
			standardizedRoleDetailsView[selectedTab].checkListTopicView.add(view);
			standardizedRoleDetailsView[selectedTab].getDragController().makeDraggable(view.asWidget(),view.getDraglbl());
			//standardizedRoleDetailsView[selectedTab].getDragController().addDragHandler(roleDetailActivity);
			view.getTopicsdiscloserVP();
			
			Log.info("MAKE draggable~~~~");
			//view.getDragController().makeDraggable((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view.asWidget(),view.getDraglbl());
			
			
			return (RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)view;
		}
		
		public void saveCheckListTopic(final String checkListTopic,final String description)
		{
			Log.info("Call saveCheckListTopic");
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();//gets selected tab of standardized role
			//ScrolledTab Changes end
			ChecklistTopicRequest request=requests.checklistTopicRequest();
			
			final ChecklistTopicProxy proxy=request.create(ChecklistTopicProxy.class);
			
			
			
			proxy.setTitle(checkListTopic);
			proxy.setDescription(description);
			
			proxy.setSort_order(standardizedRoleDetailsView[selectedtab].checkListsVerticalPanel.getWidgetCount());
			proxy.setCheckList(standardizedRoleDetailsView[selectedtab].getValue().getCheckList());
			// Highlight onViolation
			Log.info("Map Size: "+ standardizedRoleDetailsView[selectedtab].getChecklistTopicMap().size());
			request.persist().using(proxy).fire(new OSCEReceiver<Void>(standardizedRoleDetailsView[selectedtab].getChecklistTopicMap()) {
			// E Highlight onViolation

				@Override
				public void onSuccess(Void response) 
				{
					Log.info("Check List Topic Saved");
					Log.info("Topic ID : " + proxy.getId());				
					createCheckListTopic(selectedtab,proxy);
					// Highlight onViolation					
					((CheckListTopicPopupViewImpl)(standardizedRoleDetailsView[selectedtab].topicPopup)).hide();
					// E Highlight onViolation
				}
			});
		}
		
		
		public void saveCheckListQuestion(String Question,String Instruction,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView)
		{
			Log.info("saveCheckListQuestion called");
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();//gets selected tab of standardized role
			//ScrolledTab Changes end
			 ChecklistQuestionRequest request=requests.checklistQuestionRequest();
			
			final ChecklistQuestionProxy proxy=request.create(ChecklistQuestionProxy.class);
			proxy.setQuestion(Question);
			proxy.setInstruction(Instruction);
			proxy.setCheckListTopic(topicView.getProxy());
			proxy.setSequenceNumber(topicView.checkListQuestionVerticalPanel.getWidgetCount());
			proxy.setIsOveralQuestion(topicView.questionPopup.getIsOverallQuestionChkBox().getValue());
			// Highlight onViolation
			Log.info("Map ::"+topicView.getChecklistQuestionMap());
			//Log.info("Map Size: "+ standardizedRoleDetailsView[selectedtab].checkListTopicView.get(selectedtab).getChecklistQuestionMap().size());
			request.persist().using(proxy).fire(new OSCEReceiver<Void>(topicView.getChecklistQuestionMap()) {
			// E Highlight onViolation
				@Override
				public void onSuccess(Void response) 
				{
					Log.info("Call Success...");	
					((CheckListQuestionPopupViewImpl)(topicView.questionPopup)).hide();
					createQuestionView(selectedtab,proxy,topicView,++roleDetailActivity.questionCount);
			//	roleTopicInit(topicView.getProxy(), topicView);	
				}
			});
		}
		
		public RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl createQuestionView(int selectedTab,ChecklistQuestionProxy proxy,RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView,int questionCount)
		{
			RoleDetailsChecklistSubViewChecklistQuestionItemView questionView=new RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl();
			//questionView.getQuestionItemLbl().setText(proxy.getQuestion());
			questionView.getQuestionItemLbl().setTitle(proxy.getQuestion());
			//questionView.getQuestionItemLbl().setText(questionCount +". "+util.getFormatedString(proxy.getQuestion(),60));
			questionView.getQuestionItemLbl().setText(questionCount +". "+ proxy.getQuestion());
			
			questionView.setChecklistQuestionCount(questionCount);
			
			//view.getCriteriaLbl().setText(util.getFormatedString(proxy.getCriteria(),10));
			//questionView.getQuestionInstruction().setText(proxy.getInstruction());
			//questionView.getQuestionInstruction().setText(util.getFormatedString(proxy.getInstruction(), 60));
			questionView.getQuestionInstruction().setText(proxy.getInstruction());
			questionView.getQuestionInstruction().setTitle(proxy.getInstruction());
			
			questionView.setProxy(proxy);
			((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)questionView).getOptionDragController().addDragHandler(this);
			((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)questionView).getCriteriaDragController().addDragHandler(this);
			
			questionView.setDelegate(this);
			topicView.checkListQuestionVerticalPanel.insert(questionView, topicView.checkListQuestionVerticalPanel.getWidgetCount());
			topicView.getDragController().makeDraggable(questionView.asWidget(), questionView.getQuestionItemLbl());
			//topicView.getDragController().addDragHandler(roleDetailActivity);
			//topicView.getCheckListQuestionVerticalPanel();
		//	roleTopicInit(topicView.getProxy(),topicView);
			Log.info("after View Created");
	
			
			return (RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)questionView;
		}
		
		
		public void saveCriteria(final String criteria,final RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			Log.info("saveCriteria");
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();//gets selected tab of standardized role
			//ScrolledTab Changes end
			ChecklistCriteriaRequest request = requests.checklistCriteriaRequest();
			
			final ChecklistCriteriaProxy proxy=request.create(ChecklistCriteriaProxy.class);
			proxy.setChecklistQuestion(questionView.getProxy());
			proxy.setCriteria(criteria);
			proxy.setSequenceNumber(questionView.criteriaHorizontalPanel.getWidgetCount());
			// Highlight onViolation
			request.persist().using(proxy).fire(new OSCEReceiver<Void>() {
			// E Highlight onViolation
				@Override
				public void onSuccess(Void response) {
					Log.info("Criteria Saved Successfully");
					((CriteriaPopupViewImpl)(questionView.criteriaPopup)).hide();
					createCriteriaView(selectedtab,proxy,questionView);
					
				}
			});
		}
		
		
		public void createCriteriaView(int selectedTab,ChecklistCriteriaProxy proxy,RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			RoleDetailsChecklistSubViewChecklistCriteriaItemView view=new RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl();
			//view.getCriteriaLbl().setText(proxy.getCriteria());
			//change for design 
			view.getCriteriaLbl().setText(util.getFormatedString(proxy.getCriteria(),10));
			view.getCriteriaLbl().setTitle(proxy.getCriteria());
			view.setProxy(proxy);
			view.setDelegate(this);
			questionView.getCriteriaDragController().makeDraggable(view.asWidget(),view.getCriteriaLbl());
			//questionView.getCriteriaDragController().addDragHandler(this);
			questionView.criteriaHorizontalPanel.insert(view, questionView.criteriaHorizontalPanel.getWidgetCount());
//			questionView.criteriaHorizontalPanel.insert(new Label(),questionView.criteriaHorizontalPanel.getWidgetCount());
		}
		
		public void saveOption(String option,String value,String description, final RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			Log.info("saveOption");
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
			final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();//gets selected tab of standardized role
			//ScrolledTab Changes end
			ChecklistOptionRequest request=requests.checklistOptionRequest();
			
			final ChecklistOptionProxy proxy=request.create(ChecklistOptionProxy.class);
			proxy.setOptionName(option);
			proxy.setChecklistQuestion(questionView.getProxy());
			proxy.setValue(value);
			proxy.setInstruction(description);
			proxy.setSequenceNumber(questionView.optionVerticalPanel.getWidgetCount());
			proxy.setCriteriaCount(new Integer(questionView.optionPopup.getCriteriaCountLstBox().getValue(questionView.optionPopup.getCriteriaCountLstBox().getSelectedIndex())));
			// Highlight onViolation
			Log.info("Map Size: " + questionView.getChecklistOptionMap().size());
			request.persist().using(proxy).fire(new OSCEReceiver<Void>(questionView.getChecklistOptionMap()) 
			{
			// E Highlight onViolation
				@Override
				public void onSuccess(Void response) {
					Log.info("Option Saved Successfully");	
					// Highlight onViolation
					((CheckListTopicPopupViewImpl)(questionView.optionPopup)).hide();
					// E Highlight onViolation
					createOptionView(selectedtab,proxy,questionView);
					
				}
			});
		}
		
		public void createOptionView(int selectedtab,ChecklistOptionProxy optionProxy,RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			RoleDetailsChecklistSubViewChecklistOptionItemView view=new RoleDetailsChecklistSubViewChecklistOptionItemViewImpl();
			view.getOptionLbl().setText(optionProxy.getOptionName());
			//change for design 
			//util.getFormatedString(text, length);
			view.getOptionLbl().setText(util.getFormatedString(optionProxy.getOptionName(), 10));
			view.getOptionLbl().setTitle(optionProxy.getOptionName()+"["+optionProxy.getCriteriaCount()+"]");
			if(optionProxy.getValue()!=null)
			{
				//view.getOptionLbl().setText(view.getOptionLbl().getText() +"("+ optionProxy.getValue() +")");
				view.getOptionLbl().setText(util.getFormatedString(optionProxy.getOptionName()+"("+ optionProxy.getValue() +")", 10));
				view.getOptionLbl().setTitle(optionProxy.getOptionName()+"("+ optionProxy.getValue() +")");
				//view.getOptionLbl().setTitle(view.getOptionLbl().getText());
			}
			//change for design
			//view.getOptionValueLbl().setText(optionProxy.getValue());
			view.setProxy(optionProxy);
			view.setDelegate(this);
			questionView.getOptionDragController().makeDraggable(view.asWidget(), view.getOptionLbl());
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
					
					final ChecklistTopicProxy finalProxy=proxy;
					Log.info("Map Size: " + topicView.getChecklistTopicMap().size());
					checklisttopicreq.persist().using(proxy).fire(new OSCEReceiver<Void>(topicView.getChecklistTopicMap()) 
					{

						@Override
						public void onSuccess(Void arg0) {
							// TODO Auto-generated method stub
							((CheckListTopicPopupViewImpl)topicView.topicPopup).hide(true);
							topicView.setProxy(finalProxy);
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
		
		public void setSpecialisationListBox(final ImportTopicPopupView popupView)
		{
			
			Log.info("setSpecialisationListBox");
			popupView.setDelegate(this);
			
			requests.specialisationRequest().findAllSpecialisations().fire(new OSCEReceiver<List<SpecialisationProxy>>() {

				@Override
				public void onSuccess(List<SpecialisationProxy> response) {
					Log.info("setSpecialisationListBox success");
					 DefaultSuggestOracle<SpecialisationProxy> suggestOracle1 = (DefaultSuggestOracle<SpecialisationProxy>) popupView.getView().getSpecializationLstBox().getSuggestOracle();
					 suggestOracle1.setPossiblilities(response);
					 popupView.getView().getSpecializationLstBox().setSuggestOracle(suggestOracle1);
					
					 DefaultSuggestOracle<StandardizedRoleProxy> rolesuggestOracle=(DefaultSuggestOracle<StandardizedRoleProxy>)popupView.getView().roleLstBox.getSuggestOracle();
					 rolesuggestOracle.clear();
					 popupView.getView().roleLstBox.setSuggestOracle(rolesuggestOracle);
					 
					 DefaultSuggestOracle<Object> topicsuggestOracle=(DefaultSuggestOracle<Object>)popupView.getView().topicLstBox.getSuggestOracle();
					 topicsuggestOracle.clear();
					 popupView.getView().topicLstBox.setSuggestOracle(topicsuggestOracle);
					 /*popupView.getView().getSpecializationLstBox().setRenderer(new AbstractRenderer<SpecialisationProxy>() {

							@Override
							public String render(SpecialisationProxy object) {
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
						});*/
				}
			});
		}
		
		public void setRoleListBoxValue(ImportTopicPopupView popupView)
		{
			popupView.setDelegate(this);			
			
			//ScrolledTab Changes start
			//StandardizedRoleProxy proxy=standardizedRoleDetailsView[view.getRoleDetailTabPanel().getTabBar().getSelectedTab()].getValue();
			StandardizedRoleProxy proxy=standardizedRoleDetailsView[view.getRoleDetailTabPanel().getSelectedIndex()].getValue();
			//ScrolledTab Changes end
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
			//Issue # 122 : Replace pull down with autocomplete.
			
			Log.info("before value set");
			 DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedRoleProxy>) popupView.getView().roleLstBox.getSuggestOracle();
			 suggestOracle1.setPossiblilities(roles);
			 popupView.getView().roleLstBox.setSuggestOracle(suggestOracle1);
			
			 
			
			 
			 DefaultSuggestOracle<Object> topicsuggestOracle=(DefaultSuggestOracle<Object>)popupView.getView().topicLstBox.getSuggestOracle();
			 topicsuggestOracle.clear();
			 popupView.getView().topicLstBox.setSuggestOracle(topicsuggestOracle);
			 popupView.getView().topicLstBox.setText("");
			Log.info("before value set 1");
			 //popupView.getView().roleLstBox.setRenderer(new StandardizedRoleProxyRenderer());
			/*popupView.getView().roleLstBox.setRenderer(new AbstractRenderer<StandardizedRoleProxy>() {

				@Override
				public String render(StandardizedRoleProxy object) {
					// TODO Auto-generated method stub
					if(object!=null)
					{
					return object.getShortName();
					}
					else
					{
						return "";
					}
				}
			});*/

			Log.info("after value set");
			//popupView.getRoleLstBox().setAcceptableValues(roles);
			
			//Issue # 122 : Replace pull down with autocomplete.
		}
		public void specialisationListBoxValueSelected(SpecialisationProxy specialisationProxy,final ImportTopicPopupViewImpl importPopupview)
		{
			Log.info("specialisationListBoxValueSelected");
			
			
			final StandardizedRoleProxy standardizedRoleProxy=standardizedRoleDetailsView[view.getRoleDetailTabPanel().getSelectedIndex()].getValue();
			
			requests.specialisationRequest().findSpecialisation(specialisationProxy.getId()).with("roleTopics","roleTopics.standardizedRoles","roleTopics.standardizedRoles.checkList").fire(new OSCEReceiver<SpecialisationProxy>() {

				@Override
				public void onSuccess(SpecialisationProxy response) {
					Log.info("specialisationListBoxValueSelected success");
					Iterator<RoleTopicProxy> roleTopicIterator=response.getRoleTopics().iterator();
					List<StandardizedRoleProxy> roles=new ArrayList<StandardizedRoleProxy>();
					while(roleTopicIterator.hasNext())
					{
						RoleTopicProxy roleTopicProxy=roleTopicIterator.next();
						
						Iterator<StandardizedRoleProxy> roleIterator=roleTopicProxy.getStandardizedRoles().iterator();
		
						while(roleIterator.hasNext())
						{
							StandardizedRoleProxy roleProxy=roleIterator.next();
							if(standardizedRoleProxy.getId()!=roleProxy.getId() && roleProxy.getActive()==true)
								roles.add(roleProxy);
						}
						
						
					}
					
					 DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedRoleProxy>) importPopupview.roleLstBox.getSuggestOracle();
					 suggestOracle1.setPossiblilities(roles);
					 importPopupview.roleLstBox.setSuggestOracle(suggestOracle1);
					 importPopupview.roleLstBox.setText("");
					
					 
					 DefaultSuggestOracle<Object> topicsuggestOracle=(DefaultSuggestOracle<Object>)importPopupview.getView().topicLstBox.getSuggestOracle();
					 topicsuggestOracle.clear();
					 importPopupview.getView().topicLstBox.setSuggestOracle(topicsuggestOracle);
					 importPopupview.topicLstBox.setText("");
					
				}
			});
			
			
			
		}
		public void roleListBoxValueSelected(StandardizedRoleProxy proxy,final ImportTopicPopupViewImpl importPopupView)
		{
			Log.info("roleListBoxValueSelected");
			Log.info("CheckList Id :"+  proxy.getCheckList().getId());
			requests.checkListRequest().findCheckList(proxy.getCheckList().getId()).with("checkListTopics").with("checkListTopics.checkListQuestions").with("checkListTopics.checkListQuestions.checkListCriterias").with("checkListTopics.checkListQuestions.checkListOptions").fire(new Receiver<CheckListProxy>() {

				@Override
				public void onSuccess(CheckListProxy response) {
					//Issue # 122 : Replace pull down with autocomplete.
					//importPopupView.getTopicLstBox().setAcceptableValues(response.getCheckListTopics());
					
					if(response!=null)
					{
					 DefaultSuggestOracle<Object> suggestOracle1 = (DefaultSuggestOracle<Object>) importPopupView.getView().topicLstBox.getSuggestOracle();
					 
					
					 List<Object> possiblities=new ArrayList<Object>();
					 if(response.getCheckListTopics().size()>0 && !importPopupView.queListBox.isVisible())
						 possiblities.add("All");
					 
					 possiblities.addAll(response.getCheckListTopics());
					 
					 suggestOracle1.setPossiblilities(possiblities);
					 
					 
							 
					 importPopupView.getView().topicLstBox.setSuggestOracle(suggestOracle1);
					 if(response.getCheckListTopics().size()>0 && !importPopupView.queListBox.isVisible())
						 importPopupView.getView().topicLstBox.getTextField().setText("All");
					 else
						 importPopupView.getView().topicLstBox.getTextField().setText("");
					 Log.info("before value set 1");
					//importPopupView.getView().topicLstBox.setRenderer(new ChecklistTopicProxyRenderer());
					/*importPopupView.getView().topicLstBox.setRenderer(new AbstractRenderer<ChecklistTopicProxy>() {

						@Override
						public String render(ChecklistTopicProxy object) {
							// TODO Auto-generated method stub
							if(object!=null)
							{
							return object.getTitle();
							}
							else
							{
								return "";
							}
						}
					});*/
					/* DefaultSuggestOracle<ChecklistTopicProxy> topicsuggestOracle=(DefaultSuggestOracle<ChecklistTopicProxy>)importPopupView.getView().topicLstBox.getSuggestOracle();
					// topicsuggestOracle.clear();
					 importPopupView.getView().topicLstBox.setSuggestOracle(topicsuggestOracle);*/
				//	 importPopupView.topicLstBox.setText("");

					}
					//importPopupView.getView().topicLstBox.setRenderer(new CheckListProxyRenderer());
					Log.info("after value set");
					//Issue # 122 : Replace pull down with autocomplete.
					
				}
			});
			
		}
		
		public void topicListBoxValueSelected(ChecklistTopicProxy proxy,final ImportTopicPopupViewImpl importPopupView)
		{
			Log.info("topicListBox Selected");
		//	Log.info("topic Id "+proxy.getId());
			
		
			
			if(proxy==null)
				return;
			requests.checklistTopicRequest().findChecklistTopic(proxy.getId()).with("checkListQuestions").with("checkListQuestions.checkListCriterias").with("checkListQuestions.checkListOptions").fire(new Receiver<ChecklistTopicProxy>() {
				@Override
				public void onSuccess(ChecklistTopicProxy response) {
					//Issue # 122 : Replace pull down with autocomplete.
					if(response!=null)
					{
					 DefaultSuggestOracle<ChecklistQuestionProxy> suggestOracle1 = (DefaultSuggestOracle<ChecklistQuestionProxy>) importPopupView.getView().queListBox.getSuggestOracle();
					 suggestOracle1.setPossiblilities(response.getCheckListQuestions());
					 importPopupView.getView().queListBox.setSuggestOracle(suggestOracle1);
					Log.info("before value set 1");
					//importPopupView.getView().queListBox.setRenderer(new ChecklistQuestionProxyRenderer());
					/*importPopupView.getView().queListBox.setRenderer(new AbstractRenderer<ChecklistQuestionProxy>() {

						@Override
						public String render(ChecklistQuestionProxy object) {
							// TODO Auto-generated method stub
							if(object!=null)
							{
							return object.getQuestion();
							}
							else
							{
								return "";
							}
						}
					});*/
					}
				//	importPopupView.getQueListBox().setAcceptableValues(response.getCheckListQuestions());
					//Issue # 122 : Replace pull down with autocomplete.
					
				}
			});
		}
		
		public void importAllTopic(final ImportTopicPopupViewImpl viewTopicPopup)
		{
			final int selectedTab=view.getRoleDetailTabPanel().getSelectedIndex();
			//ScrolledTab Changes start
			CheckListProxy checklistProxy=standardizedRoleDetailsView[selectedTab].getValue().getCheckList();
			DefaultSuggestOracle<Object> suggestOracle = (DefaultSuggestOracle<Object>) viewTopicPopup.getView().topicLstBox.getSuggestOracle();
			List<Object> topics=suggestOracle.getPossiblilities();
			
			for(int i=1;i<topics.size();i++)
			{
				ChecklistTopicProxy topicProxy=(ChecklistTopicProxy)topics.get(i);
				copyTopic(topicProxy,checklistProxy,viewTopicPopup);
			}
			
		}
		
		private void copyTopic(final ChecklistTopicProxy proxy,CheckListProxy checklistProxy,final ImportTopicPopupViewImpl viewTopicPopup)
		{
			final int selectedTab=view.getRoleDetailTabPanel().getSelectedIndex();
			
			ChecklistTopicRequest request=requests.checklistTopicRequest();
			
			final ChecklistTopicProxy addTopicProxy=request.create(ChecklistTopicProxy.class);
			
			addTopicProxy.setCheckList(checklistProxy);
			addTopicProxy.setDescription(proxy.getDescription());
			addTopicProxy.setSort_order(proxy.getSort_order());
			addTopicProxy.setTitle(proxy.getTitle());

			// Highlight onViolation
			Log.info("Map Size: " + viewTopicPopup.getChecklistTopicMap().size());
			request.persist().using(addTopicProxy).fire(new OSCEReceiver<Void>(viewTopicPopup.getChecklistTopicMap()) {
			// E Highlight onViolation
				@Override
				public void onSuccess(Void response) {
					
					requests.find(addTopicProxy.stableId()).fire(new OSCEReceiver<Object>() {

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
								addQuestionProxy.setIsOveralQuestion(questionProxy.getIsOveralQuestion());
								questionRequest.persist().using(addQuestionProxy).fire(new Receiver<Void>() {

									@Override
									public void onSuccess(Void response) {
										requests.find(addQuestionProxy.stableId()).fire(new OSCEReceiver<Object>() {

											@Override
											public void onSuccess(Object response) {
												ChecklistQuestionProxy queProxy=(ChecklistQuestionProxy)response;
												//create question view
												RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(selectedTab, queProxy, topicView,++roleDetailActivity.questionCount);
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
														addOptionProxy.setSequenceNumber(optionProxy.getSequenceNumber());
														addOptionProxy.setCriteriaCount(optionProxy.getCriteriaCount());
														
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
														addCriteriaProxy.setSequenceNumber(criteriaProxy.getSequenceNumber());
														criteriaRequest.persist().using(addCriteriaProxy).fire();
														
														createCriteriaView(selectedTab, addCriteriaProxy, questionView);
														
													}
												}

											}
										});

										
									}
								});
															
								
															
							}
							viewTopicPopup.hide();		
						}
					});
				}
			});
		}
		
		public void importTopic(final ChecklistTopicProxy proxy,final ImportTopicPopupViewImpl viewTopicPopup)
		{
			
			Log.info("importTopic");
			// Highlight onViolation			
			/*if(proxy==null)
			{
				Log.info("Proxy is Null");
				proxy.setTitle(null);
				proxy.setDescription(null);
			}*/
			// E Highlight onViolation
			
			//ScrolledTab Changes start
			//final int selectedTab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			
			if(viewTopicPopup.topicLstBox.getTextField().getText().equalsIgnoreCase("All"))
			{
				Log.info("All Selected");
				importAllTopic(viewTopicPopup);
				return;
			}
			else if(proxy != null)
			{
				final int selectedTab=view.getRoleDetailTabPanel().getSelectedIndex();
				//ScrolledTab Changes start
				CheckListProxy checklistProxy=standardizedRoleDetailsView[selectedTab].getValue().getCheckList();
				
				
				copyTopic(proxy,checklistProxy,viewTopicPopup);
			}
			
			
			
			
			
		}
		// Highlight onViolation
		public void importQuestion(final ChecklistQuestionProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView,final ImportTopicPopupViewImpl viewforMap)
		// E Highlight onViolation
		{
			Log.info("import Question");
			//ScrolledTab Changes start
			//final int selectedTab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			final int selectedTab=view.getRoleDetailTabPanel().getSelectedIndex();
			//ScrolledTab Changes start
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
			addQuestionProxy.setIsOveralQuestion(proxy.getIsOveralQuestion());
//			questionRequest.persist().using(addQuestionProxy).with("checkListQuestions","checkListQuestions.checkListCriterias","checkListQuestions.checkListOptions").fire(new Receiver<void>() {
	//
//				@Override
//				public void onSuccess(void response) {
//					Log.info("Question Added....");
//					queProxy=response;
//					
//				}
//			});
			// Highlight onViolation
			Log.info("Map Size: " + viewforMap.getChecklistQuestionMap().size());
			questionRequest.persist().using(addQuestionProxy).fire(new OSCEReceiver<Void>(viewforMap.getChecklistQuestionMap()) {
			// E Highlight onViolation

				@Override
				public void onSuccess(Void response) {
					
					requests.find(addQuestionProxy.stableId()).fire(new OSCEReceiver<Object>() {

						@Override
						public void onSuccess(Object response) {
							// TODO Auto-generated method stub
							ChecklistQuestionProxy queProxy= (ChecklistQuestionProxy)response;
							
							//create question view
							RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(selectedTab, queProxy, topicView,++roleDetailActivity.questionCount);
							
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
									addOptionProxy.setCriteriaCount(optionProxy.getCriteriaCount());
									
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
							
							viewforMap.hide();
						}
					});
					
					
				}
				
			});
			
			
			
		}
		
		public void editOption(final String question,final String instruction, final RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView)
		{
			Log.info("editOption");
			 ChecklistQuestionProxy questionProxy=questionView.getProxy();
			
			ChecklistQuestionRequest request=requests.checklistQuestionRequest();
			questionProxy=request.edit(questionProxy);
			questionProxy.setQuestion(question);
			questionProxy.setInstruction(instruction);
			questionProxy.setIsOveralQuestion(questionView.editquestionpopup.getIsOverallQuestionChkBox().getValue());
			// Highlight onViolation	
			Log.info("Map Size: " + questionView.getChecklistQuestionMap().size());
			 final ChecklistQuestionProxy finalquestionProxy=questionProxy;
				
			request.persist().using(questionProxy).fire(new OSCEReceiver<Void>(questionView.getChecklistQuestionMap()) {
			// E Highlight onViolation
				
				@Override
				public void onSuccess(Void response) {
					questionView.questionItemLbl.setText(questionView.getChecklistQuestionCount() + ". " + question);
					questionView.questionInstruction.setText(instruction);
					questionView.setProxy(finalquestionProxy);
					
					questionView.questionItemLbl.setTitle(question);
					questionView.questionInstruction.setTitle(instruction);
					// Highlight onViolation	
					((CheckListQuestionPopupViewImpl)(questionView.editquestionpopup)).hide();
					// E Highlight onViolation
				}
			});
		}
		

		@Override
		public void topicMoveDown(final ChecklistTopicProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			
			Log.info("else if called ~~~~");
			VerticalPanel vp=((VerticalPanel)(topicView).getParent());
			Log.info("Widget Index" + vp.getWidgetIndex(topicView));
			int index=vp.getWidgetIndex(topicView);
			
			if(index==vp.getWidgetCount()-1)
				return;
			index++;
			
			topicView.removeFromParent();
			
			vp.insert(topicView, index);
		
		for(int i=0;i<vp.getWidgetCount();i++)
		{
			
			Log.info("value~~~~"+ i);
			if(i==vp.getWidgetCount()-1)
				updateSequence(((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)vp.getWidget(i)).getProxy(),i,true,topicView,proxy);
			else
				updateSequence(((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)vp.getWidget(i)).getProxy(),i,false,null,null);
		
		
		}
			
			
		/*	final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();
			//ScrolledTab Changes end
			requests.checklistTopicRequestNonRoo()
			.topicMoveDown(standardizedRoleDetailsView[selectedtab].getValue().getCheckList().getId()).using(proxy)
			.fire(new Receiver<Void>() {
				@Override
				public void onSuccess(Void response) {
					Log.info("moved");
					roleTopicInit(proxy,topicView);

				}
			});
			*/
		}

		@Override
		public void topicMoveUp(final ChecklistTopicProxy proxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			
			Log.info("else if called ~~~~");
			VerticalPanel vp=((VerticalPanel)(topicView).getParent());
			Log.info("Widget Index" + vp.getWidgetIndex(topicView));
			int index=vp.getWidgetIndex(topicView);
			
			if(index==0)
				return;
			
			index--;
			
			topicView.removeFromParent();
			
			vp.insert(topicView, index);
		
			for(int i=0;i<vp.getWidgetCount();i++)
			{
				
				Log.info("value~~~~"+ i);
				if(i==vp.getWidgetCount()-1)
					updateSequence(((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)vp.getWidget(i)).getProxy(),i,true,topicView,proxy);
				else
					updateSequence(((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)vp.getWidget(i)).getProxy(),i,false,null,null);
			
			
			}
			
			
			
			
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			
			/*final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();
			//ScrolledTab Changes end
			requests.checklistTopicRequestNonRoo().topicMoveUp(standardizedRoleDetailsView[selectedtab].getValue().getCheckList().getId()).using(proxy).fire(new Receiver<Void>() {
				@Override
				public void onSuccess(Void response) {
					Log.info("moved");
					roleTopicInit(proxy,topicView);
				}
			});*/
			
		}

		@Override
		public void questionMoveUp(RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView,final ChecklistTopicProxy topicProxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			
			
			/*
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();
			//ScrolledTab Changes end
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
			*/
			
			
			VerticalPanel vpQ=((VerticalPanel)(questionView.getParent()));
			Log.info("Widget Index" + vpQ.getWidgetIndex(questionView));
			int index=vpQ.getWidgetIndex(questionView);
			if(index==0)
				return;
			index--;
			
			questionView.removeFromParent();
			
			vpQ.insert(questionView, index);
			for(int i=0;i<vpQ.getWidgetCount();i++)
			{
				Log.info("value~~~~"+ i);
				if(i==(vpQ.getWidgetCount()-1))
					updateQueSequence(((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)vpQ.getWidget(i)).getProxy(),i,true,topicView,topicProxy);
				else
					updateQueSequence(((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)vpQ.getWidget(i)).getProxy(),i,false,topicView,topicProxy);
			
			
				
			}
		}

		@Override
		public void questionMoveDown(RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView,final ChecklistTopicProxy topicProxy,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView) {
			
			//ScrolledTab Changes start
			//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();
			
			VerticalPanel vpQ=((VerticalPanel)(questionView.getParent()));
			Log.info("Widget Index" + vpQ.getWidgetIndex(questionView));
			int index=vpQ.getWidgetIndex(questionView);
			
			if(index==vpQ.getWidgetCount()-1)
				return;
			
			index++;
			questionView.removeFromParent();			
			vpQ.insert(questionView, index);
			
			for(int i=0;i<vpQ.getWidgetCount();i++)
			{
				Log.info("value~~~~"+ i);
				if(i==(vpQ.getWidgetCount()-1))
					updateQueSequence(((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)vpQ.getWidget(i)).getProxy(),i,true,topicView,topicProxy);
				else
					updateQueSequence(((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)vpQ.getWidget(i)).getProxy(),i,false,topicView,topicProxy);
			
			
				
			}
			
			/*final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();
			//ScrolledTab Changes end
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
			});*/
		}
		
		//Assignment E]

	// Assignment F[

	@Override
	public void addBasicCriteriaClicked(Button addBasicData) {
		Log.info("addBasicCriteriaClicked");
		mapVar=1;
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == basicCriteriaPopUp) {
				return;
			}
		}

		basicCriteriaPopUp = new StandartizedPatientAdvancedSearchBasicCriteriaPopUpImpl();
		basicCriteriaPopUp.setDelegate(this);
		//basicCriteriaPopUp.display(addBasicData);
		
		int x = addBasicData.getAbsoluteLeft() - 235;
		int y = addBasicData.getAbsoluteTop() - addBasicData.getOffsetHeight() - 15;
		basicCriteriaPopUp.display(addBasicData);
		
		//SPEC Change
		
		
		advancedSearchPopup = basicCriteriaPopUp;
	}

	@Override
	public void filterTableClicked() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addScarCriteriaClicked(Button parentButton) {
		Log.info("addScarCriteriaClicked");
		mapVar=2;
		requests.scarRequest().findAllScars().fire(new ScarCriteriaReceiver());
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == scarPopup) {
				return;
			}
		}
		scarPopup = new StandardizedPatientAdvancedSearchScarPopupImpl();
		scarPopup.setDelegate(this);
		//scarPopup.display(parentButton);

		int x = parentButton.getAbsoluteLeft() - 235;
		int y = parentButton.getAbsoluteTop() - parentButton.getOffsetHeight() - 15;
		scarPopup.display(parentButton);
		
		
		advancedSearchPopup = scarPopup;
	}
	
	@Override
	public void addGenderClicked(IconButton parentButton) {
		Log.info("addGenderClicked()");
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == genderPopup) {
				return;
			}
		}
		genderPopup = new StandardizedPatientAdvancedSearchGenderPopupViewImpl();
		genderPopup.setDelegate(this);
		
		genderPopup.display(parentButton);
		advancedSearchPopup = genderPopup;
	}

	@Override
	public void addAnamnesisCriteriaClicked(Button parentButton) {
		Log.info("addAnamnesisCriteriaClicked");
		mapVar=3;
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
		//anamnesisPopup.display(parentButton);
		
		int x = parentButton.getAbsoluteLeft() - 235;
		int y = parentButton.getAbsoluteTop() - parentButton.getOffsetHeight() - 15;
		anamnesisPopup.display(parentButton);
		advancedSearchPopup = anamnesisPopup;
	}

	@Override
	public void addLanguageCriteriaClicked(Button addLanguageButton) {
		Log.info("addLanguageCriteriaClicked");
		mapVar=4;
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
		
		//SPEC Change
		
//		languagePopup.display(addLanguageButton);
		int x = addLanguageButton.getAbsoluteLeft() - 235;
		int y = addLanguageButton.getAbsoluteTop() - addLanguageButton.getOffsetHeight() - 15;
		languagePopup.display(addLanguageButton);
		
		//SPEC Change
		advancedSearchPopup = languagePopup;
	}

	@Override
	public void addNationalityCriteriaClicked(IconButton addNationalityButton) {
		Log.info("addNationalityCriteriaClicked");
		mapVar=5;
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
		
		//SPEC Change
		
//		nationalityPopup.display(addNationalityButton);
		int x = addNationalityButton.getAbsoluteLeft() - 235;
		int y = addNationalityButton.getAbsoluteTop() - addNationalityButton.getOffsetHeight() - 15;
		nationalityPopup.display(addNationalityButton);
		
		//SPEC Change
		
		
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
		fireAdvancedSearchCriteriasCountRequest(standardizedRoleID,	new Receiver<Long>() {
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
			long standardizedRoleID, Receiver<Long> callback) 
	{
		Log.info("Call fireAdvancedSearchCriteriasCountRequest");
		requests.advancedSearchCriteriaNonRoo().countAdvancedSearchCriteriasByStandardizedRoleID(standardizedRoleID).fire(callback);
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
	public void deleteAdvancedSearchCriteria(final AdvancedSearchCriteriaProxy criterion) 
	{
		// Issue Role
				 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
				 dialogBox.showYesNoDialog("Really delete this entry? You cannot undo this change.");
				 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();							
							Log.info("yes click");	
							
							//ScrolledTab Changes start
							//final int selectedTab = roleDetailTabPanel.getTabBar().getSelectedTab();
							final int selectedTab = roleDetailTabPanel.getSelectedIndex();
							//ScrolledTab Changes end
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
					});
					
						dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();
							Log.info("no click");
							return;
							
						}
					});
				// E: Issue Role
				
		
		
		
		
		
		/*if (!Window.confirm("Really delete this entry? You cannot undo this change.")) {
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
				});*/

	}

	// Highlight onViolation
	@Override
	//anamnesisCheck.getId(), answer,displayValue, bindType, PossibleFields.ANAMNESIS, comparison
	public void addAdvSeaBasicButtonClicked(Long objectId, String value,String shownValue, BindType bindType,PossibleFields possibleFields, Comparison comparison) 
	{
		// E Highlight onViolation		
		Log.info("Call addAdvSeaBasicButtonClicked");		
		switch (possibleFields) {
		case BMI:{
			shownValue = constants.bmi()
					+ " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC)
							.render(comparison) + " " + value;
			break;}
		case HEIGHT:{
			shownValue = constants.height()
					+ " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC)
							.render(comparison) + " " + value + "cm";
			break;}
		case WEIGHT:{
			shownValue = constants.weight()
					+ " "
					+ new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC)
							.render(comparison) + " " + value + "kg";
			break;}
		case AGE:{
			shownValue = constants.age() + " " + new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC).render(comparison) + " " 
					+ value + "years";
			break;}
		case GENDER:{
			shownValue = constants.gender() + " " + new EnumRenderer<Comparison>(EnumRenderer.Type.NUMERIC).render(comparison) + " " 
					+ value;
			break;}
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

		//ScrolledTab Changes start
		//final int selectedTab = roleDetailTabPanel.getTabBar().getSelectedTab();
		final int selectedTab = roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		searchCriteriaProxy
				.setStandardizedRole(standardizedRoleDetailsView[selectedTab].getValue());
		final Long stRoleId = standardizedRoleDetailsView[selectedTab].getValue().getId();
			

		Log.info("Stand role Proxy ID: " + stRoleId);
		// Log.info("Stand Role Topoc Id : "+ S);

		// searchCriteriaProxy.setStandardizedRole();

		// Highlight onViolation
		Log.info("~~~~MapVar: " + mapVar);
		Map<String, Widget> tempMap=new HashMap<String, Widget>();
		if(mapVar==1)
		{
			tempMap=basicCriteriaPopUp.getAdvanceSearchCriteriaMap();			
		}
		else if(mapVar==2)
		{
			tempMap=scarPopup.getAdvanceSearchCriteriaMap();
		}
		else if(mapVar==3)
		{
			tempMap=anamnesisPopup.getMap();
		}
		else if(mapVar==4)
		{
			tempMap=languagePopup.getMap();
		}
		else if(mapVar==5)
		{
			tempMap=nationalityPopup.getMap();
		}
		//Log.info("Map Size: " + scarPopup.getAdvanceSearchCriteriaMap().size());
		//Log.info("Map Size: " + basicCriteriaPopUp.getAdvanceSearchCriteriaMap().size());	
		
		searchCriteriaRequest.persist().using(searchCriteriaProxy).fire(new OSCEReceiver<Void>(tempMap)
				// E Highlight onViolation
		{
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
		Log.info("Call addScarButtonClicked");
		Log.info("ScarType:" + scarProxy.getTraitType().toString() + ": "+ scarProxy.getBodypart());
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.SCAR).render(comparison)+ " "+ new ScarProxyRenderer().render(scarProxy);
		String value = scarProxy.getTraitType().toString() + ":"+ scarProxy.getBodypart();
		addAdvSeaBasicButtonClicked(scarProxy.getId(), value, displayValue,bindType, PossibleFields.SCAR, comparison);
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
		Log.info("Call addAnamnesisValueButtonClicked");
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
						
						//ScrolledTab Changes start
						/*simpleSearchInit(standRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
						*/
						simpleSearchInit(standRoleProxy.getId(),
								roleDetailTabPanel.getSelectedIndex());
						
						//ScrolledTab Changes end

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
						
						//ScrolledTab Changes start
						/*simpleSearchInit(standRoleProxy.getId(),
								roleDetailTabPanel.getTabBar().getSelectedTab());
						*/
						simpleSearchInit(standRoleProxy.getId(),
								roleDetailTabPanel.getSelectedIndex());
						
						//ScrolledTab Changes end
					}
				});

	}

	@Override
	public void simpleSearchDeleteClicked(final SimpleSearchCriteriaProxy proxy,final StandardizedRoleProxy standRoleProxy) {
		
		// Issue Role
		 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
		 dialogBox.showYesNoDialog("Really delete this entry? You cannot undo this change.");
		 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("yes click");	
					requests.simpleSearchCriteriaRequest().remove().using(proxy)
					.fire(new Receiver<Void>() {
						public void onSuccess(Void ignore) {
							Log.debug("Sucessfully deleted");
							
							//ScrolledTab Changes start
							/*simpleSearchInit(standRoleProxy.getId(),
									roleDetailTabPanel.getTabBar().getSelectedTab());
							*/
							simpleSearchInit(standRoleProxy.getId(),
									roleDetailTabPanel.getSelectedIndex());
							
							//ScrolledTab Changes end
						}
					});
				}
			});
			
				dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();
					Log.info("no click");
					return;
					
				}
			});
		// E: Issue Role
		
	}

	@Override
	public void newSimpleSearchClicked(String SearchName, String SearchValue,
			final StandardizedRoleProxy standRoleProxy) {
		Log.info("Call newSimpleSearchClicked");
		
		if (SearchName != null) 
		{
			//ScrolledTab Changes start
			//simpleSearchSortOrder = simpleSearchcriteriaTable[roleDetailTabPanel.getTabBar().getSelectedTab()].getRowCount() + 1;
			simpleSearchSortOrder = simpleSearchcriteriaTable[roleDetailTabPanel.getSelectedIndex()].getRowCount() + 1;
			//ScrolledTab Changes start
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
			// Highlight onViolation
			Log.info("Map Size: " + simpleSearchCriteriaView[selecTab].getSimpleSearchCriteriaMap().size());
			simpleSearchreq.persist().using(simpleSearchProxy).fire(new OSCEReceiver<Void>(simpleSearchCriteriaView[selecTab].getSimpleSearchCriteriaMap()) {
			// E Highlight onViolation
						@Override
						public void onSuccess(Void arg0) {
							Log.info("Simple Search Criteria added..");
							
							//ScrolledTab Changes start
							/*simpleSearchInit(standRoleProxy.getId(),
									roleDetailTabPanel.getTabBar()
											.getSelectedTab());
							*/
							simpleSearchInit(standRoleProxy.getId(),
									roleDetailTabPanel.getSelectedIndex());
							
							//ScrolledTab Changes end
						}
					});
		}

	}

	// ]Assignment F
	
	
	@Override
	public void previousRoleClicked(final StandardizedRoleProxy standardizedRoleProxy) {
		Log.info("previous clicked");
		System.out.println("============================Jump to StandardizedPatientDetailActivity previousClick() =========================");
	//	System.out.println("==>"+roleDetailTabPanel.getTabBar().getSelectedTab());
	//	int selTabID=roleDetailTabPanel.getTabBar().getSelectedTab();
		
		if(standardizedRoleProxy!=null)
		{
			
			showApplicationLoading(true);
		requests.standardizedRoleRequest().findStandardizedRole(standardizedRoleProxy.getId()).with("previousVersion","previousVersion.oscePosts","previousVersion.roleTopic","previousVersion.simpleSearchCriteria","previousVersion.roleParticipants","previousVersion.advancedSearchCriteria","previousVersion.roleTemplate","previousVersion.keywords","previousVersion.previousVersion","previousVersion.checkList","previousVersion.checkList.checkListTopics","previousVersion.checkList.checkListTopics.checkListQuestions","previousVersion.checkList.checkListTopics.checkListQuestions.checkListCriterias","previousVersion.checkList.checkListTopics.checkListQuestions.checkListOptions").fire(new Receiver<StandardizedRoleProxy>() {
			
			@Override
			public void onSuccess(StandardizedRoleProxy response) {
				showApplicationLoading(true);
				// TODO Auto-generated method stub
			 	
				StandardizedRoleDetailsViewImpl previousRole;	
				
				//ScrolledTab Changes start
				//previousRole=standardizedRoleDetailsView[roleDetailTabPanel.getTabBar().getSelectedTab()];
				previousRole=standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()];
				//ScrolledTab Changes end
				StandardizedRoleProxy roleProxy=response.getPreviousVersion();
				if(roleProxy!=null)
				{
					
					System.out.println("Set value");
					//goTo(new RoleDetailsPlace(RoleEditActivity.roleTopic.stableId(),	Operation.DETAILS));
				//	goTo(new RoleDetailsPlace((standardizedRoleProxy.getPreviousVersion()).roleTopic.stableId(),Operation.DETAILS));
				//	goTo(new RoleDetailsPlace((standardizedRoleProxy.getPreviousVersion()).stableId(),Operation.DETAILS));
					//previousRole=standardizedRoleDetailsView[roleDetailTabPanel.getTabBar().getSelectedTab()];
					
					//ScrolledTab Changes start
					//previousRole=standardizedRoleDetailsView[roleDetailTabPanel.getTabBar().getSelectedTab()];
					previousRole=standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()];
					
					previousRole.setValue(roleProxy);
					refreshPreviousStandardizedRoleDetailView(previousRole);
					//ScrolledTab Changes end
					previousRole.edit.setEnabled(false);
					previousRole.delete.setEnabled(false);
					previousRole.home.setVisible(true);
				//	previousRole.setTitle(roleProxy.getShortName() == null ? " " : String.valueOf(roleProxy.getShortName()));
				//	previousRole.shortName.setInnerText(roleProxy.getShortName() == null ? " " : String.valueOf(roleProxy.getShortName()));
				//	previousRole.longName.setInnerText(roleProxy.getLongName() == null ? " " : String.valueOf(roleProxy.getLongName()));
				//	previousRole.roleType.setInnerText(roleProxy.getRoleType().name()); //ADDED
				//	previousRole.studyYear.setInnerText(roleProxy.getStudyYear().name()); //ADDED
				//	//previousRole.labelLongNameHeader.setText(""+roleProxy.getLongName());
				//	previousRole.labelLongNameHeader.setText("" + roleProxy.getLongName()+ " ("+roleProxy.getMainVersion()+"."+roleProxy.getSubVersion()+")");
				//	previousRole.setValue(roleProxy);
					if(roleProxy==null)
					{
						previousRole.previous.setEnabled(false);
					}
				}
				else
				{
					previousRole.previous.setEnabled(false);
					//Window.alert("No More Record");
				}
				showApplicationLoading(false);
			}
		});
		
		showApplicationLoading(false);
		}
	}
	

	// SPEC START =
	@Override
	public void AddAuthorClicked() 
	{
		Log.info("~Call AddAuthorClicked:RoleDetailActivity");		
		//ScrolledTab Changes start
		//final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();
		final int selectedTabId = roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		
		// Highlight onViolation
		
		/*if(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue() == null)
		{
			//Window.alert("Please Select the Doctor from List.");
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
			
			 dialogBox.showConfirmationDialog("Select Doctor from List");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
						}
					});						
		// E: Issue Role
		}*/
		//else
		//{
				//Log.info("~Selected Author Name: : "+ standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue().getName());		
				//Log.info("~Selected Author Id: "+ standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue().getId());
				
				RoleParticipantRequest roleParticipantRequest = requests.roleParticipantRequest();
				RoleParticipantProxy roleParticipantProxy = roleParticipantRequest.create(RoleParticipantProxy.class);
		
				roleParticipantProxy.setType(RoleParticipantTypes.AUTHOR);	// If Author
				
				//Issue # 122 : Replace pull down with autocomplete.
				
				
				//roleParticipantProxy.setDoctor(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue());
				//Log.info("selected value--"+standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getSelected().getName());
				roleParticipantProxy.setDoctor(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getSelected());
				
				Log.info("after value set suggest box");
				
				//Issue # 122 : Replace pull down with autocomplete.
				
				roleParticipantProxy.setStandardizedRole(standardizedRoleDetailsView[selectedTabId].getValue());
				
				//Log.info("~Stand Role Id: " + roleParticipantProxy.getStandardizedRole().getId());
				//Log.info("~Doctor Name:" + roleParticipantProxy.getDoctor().getName());
				
			
				Log.info("Map Size:" + standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().getRoleParticipantMap().size());
				roleParticipantRequest.persist().using(roleParticipantProxy).fire(new OSCEReceiver<Void>(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().getRoleParticipantMap()) 
				{
						@Override
						public void onSuccess(Void response) 
						{
							Log.info("~Success Call....");
							Log.info("~roleParticipantRequest.persist()");
							
							// REFRESH LIST VIEW
							refreshDoctorList();
							
							// REFRESH Author Table
							//auther table pager changes
							requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 0).with("doctor").fire(new Receiver<Long>() 
									{
											@Override
											public void onSuccess(Long response) 
											{																																
												Log.info("~Success Call....");
												Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
												Log.info("~Set Data In Author Table: Resp Size: "+ response);														
												standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().authorTable.setRowCount(response.intValue(),true);
											}
									});
							
							final Range autherRange = standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().authorTable.getVisibleRange();
							
							requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 0,autherRange.getStart(),autherRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
							{
									@Override
									public void onSuccess(List<RoleParticipantProxy> response) 
									{																																
										Log.info("~Success Call....");
										Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
										Log.info("~Set Data In Author Table: Resp Size: "+ response.size());														
										standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().authorTable.setRowData(autherRange.getStart(),response);
									}
							});
							
							//auther table pager changes
								
						}
				});
				
				
				
				
				// REFRESH LIST VIEW
				//refreshDoctorList();	
		//}
		// E Highlight onViolation
	}

	@Override
	public void AddReviewerClicked() 
	{
		Log.info("~Call AddReviewerClicked:RoleDetailActivity");
		//final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();
		
		//ScrolledTab Changes start
		//final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();
		final int selectedTabId = roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		// Highlight onViolation
		/*		if(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue() == null)
		{
			//Window.alert("Please Select the Doctor from List.");
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
			 dialogBox.showConfirmationDialog("Please Select the Doctor from List.");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");	
						}
					});

			
			
		// E: Issue Role
			
		}*/
		//else
		//{
		
		//Log.info("~Selected Reviewer Name: : "+ standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue().getName());		
		//Log.info("~Selected Reviewer Id: "+ standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue().getId());

		RoleParticipantRequest roleParticipantRequest = requests.roleParticipantRequest();
		RoleParticipantProxy roleParticipantProxy = roleParticipantRequest.create(RoleParticipantProxy.class);

		roleParticipantProxy.setType(RoleParticipantTypes.REVIEWER);// IfReviewer
		
		//Issue # 122 : Replace pull down with autocomplete.
		
		//roleParticipantProxy.setDoctor(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getValue());
		//Log.info("selected value--"+standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getSelected().getName());
		roleParticipantProxy.setDoctor(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getSelected());
		Log.info("after value set suggest box");
		//Issue # 122 : Replace pull down with autocomplete.
		
		roleParticipantProxy.setStandardizedRole(standardizedRoleDetailsView[selectedTabId].getValue());
		
		//Log.info("~Stand Role Id: " + roleParticipantProxy.getStandardizedRole().getId());
		//Log.info("~Doctor Name:" + roleParticipantProxy.getDoctor().getName());
		Log.info("Map Size:" + standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().getRoleParticipantMap().size());
		roleParticipantRequest.persist().using(roleParticipantProxy).fire(new OSCEReceiver<Void>(standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().getRoleParticipantMap()) 
		{			
				@Override
				public void onSuccess(Void response) 
				{
					Log.info("~Success Call....");
					Log.info("~roleParticipantRequest.persist()");									
								
					// REFRESH LIST VIEW
					refreshDoctorList();
					
					// REFRESH Reviewer Table
					
					//reviewer table pager changes start
					requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 1).with("doctor").fire(new Receiver<Long>() 
					{
							@Override
							public void onSuccess(Long response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: Resp Size: "+ response);																							
								standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowCount(response.intValue(),true);
								//standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.removeFromParent();
							}
					});
					
					final Range reviewerRange = standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().reviewerTable.getVisibleRange();
					requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 1,reviewerRange.getStart(),reviewerRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
					{
							@Override
							public void onSuccess(List<RoleParticipantProxy> response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: Resp Size: "+ response.size());																							
								standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowData(reviewerRange.getStart(),response);
								//standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.removeFromParent();
							}
					});
					
					//reviewer table pager changes end
				}				
		});
			
		//}
		// E Highlight onViolation
	}

	@Override
	public void addKeywordClicked() 
	{
			
		Log.info("~Click on Add Keyword for Role");
		
		//ScrolledTab Changes start
		//final int selectedTabId=roleDetailTabPanel.getTabBar().getSelectedTab();
		final int selectedTabId=roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		
		//Issue # 122 : Replace pull down with autocomplete.
		//final String selectedKeyword=standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.value();
		
		if(standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getSelected()==null)
		{
			Log.info("null value");
		  selectedKeyword=standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getTextField().getText();
		}
		else
		{
			Log.info("not null value");
			  selectedKeyword=standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getSelected().getName();
		}
		//Issue # 122 : Replace pull down with autocomplete.
		Log.info("~Set Value To Null");
		//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());
		
		//SuggestBox suggestBox = null;
		//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getTextBox().setText(null);		
		//Log.info("~TextBox Value : " + standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getValue());
		
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
					
					// Highlight onViolation
					Log.info("Map Size:" + standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().getKeywordMap().size());
					keywordRequest.persist().using(keywordProxy).fire(new OSCEReceiver<Void>(standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().getKeywordMap())
					// E Highlight onViolation
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
						
						// SPEC Change
						
						boolean isExists = false;
						
						for(KeywordProxy proxy:setKeyworkdProxy){
							
							if(proxy.getName().equals(keywordProxy.getName())){
								isExists = true;
								break;
							}
							
						}
						
						if(isExists){
							
							
							
							MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.addKeyword());
							confirmationDialogBox.showConfirmationDialog(constants.keywordExists());
							
							// SPEC Change
							
						}else{
						
						setKeyworkdProxy.add(keywordProxy);
						stRoleProxy.setKeywords(setKeyworkdProxy);
						final StandardizedRoleProxy role=stRoleProxy;
						// Highlight onViolation
						srRequest.persist().using(stRoleProxy).fire(new OSCEReceiver<Void>()
						// E Highlight onViolation
						{									
							@Override
							public void onSuccess(Void response) 
							{
								Log.info("~Success Call....");
								Log.info("~srRequest.persist()");	
								Log.info("Add new Recoerd in standardized_role_keywords Table");
								// Highlight onViolation			
								
								//Issue # 122 : Replace pull down with autocomplete.
								//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.setValue("");
								standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.setSelected(null);
								//Issue # 122 : Replace pull down with autocomplete.
								// E Highlight onViolation
									
								// REFRESH LOGICAL (RELATIONSHIP) TABLE DATA [PROXY]
								//change for bug
								standardizedRoleDetailsView[selectedTabId].setValue(role);
								//refreshRelationshipProxy();
								
								//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.setText(null);
								//Log.info("Remove TextBox Value : " + standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.getValue());
								
								// REFRESH KEYWORD TABLE DATA
								
										requests.keywordRequestNonRoo().findKeywordByStandRoleCount(standardizedRoleDetailsView[selectedTabId].getValue()).fire(new Receiver<Long>()
												{
														@Override
														public void onSuccess(Long response) 
														{
															Log.info("~Success Call....");
															Log.info("~REFRESH KEYWORD TABLE DATA");
															Log.info("~findKeywordByStandRole()");
															Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.intValue());
															Log.info("~Fetch/SET Table Data");
															//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
															//Range keywordTableRange= standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
															//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
															standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.intValue(),true);
															//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
															
														}
												});
								
									final Range keywordRange = standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
									requests.keywordRequestNonRoo().findKeywordByStandRole(standardizedRoleDetailsView[selectedTabId].getValue(),keywordRange.getStart(),keywordRange.getLength()).fire(new Receiver<List<KeywordProxy>>()
								{
										@Override
										public void onSuccess(List<KeywordProxy> response) 
										{
											Log.info("~Success Call....");
											Log.info("~REFRESH KEYWORD TABLE DATA");
											Log.info("~findKeywordByStandRole()");
											Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.size());
											Log.info("~Fetch/SET Table Data");
											//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
											//Range keywordTableRange= standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
											/*standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
											standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.size(),true);*/
											standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
											
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
											standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().setKeywordAutocompleteValue(response);
										}
								}); 
								
								
								//Issue # 122 : Replace pull down with autocomplete.
								/*standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.addChangeListener(new ChangeListener() 
								{									
									@Override
									public void onChange(Widget sender) 
									{									
										Log.info("~onChange keywordSugestionBox");										
										standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());	
									}
								});	
							*/
								
								
								//Issue # 122 : Replace pull down with autocomplete.
							}
						});
						}
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
		
		//ScrolledTab Changes start
		//final int selectedTabId=roleDetailTabPanel.getTabBar().getSelectedTab();
		final int selectedTabId=roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
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
		final StandardizedRoleProxy role=stRoleProxy;
		srRequest.persist().using(stRoleProxy).fire(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void response1) 
			{
				Log.info("~Record Deleted Successfully");
				Log.info("~Success......");
				Log.info("~srRequest.persist()");
				
				// REFRESH LOGICAL (RELATIONSHIP) TABLE DATA [PROXY]
				//change for bug
				standardizedRoleDetailsView[selectedTabId].setValue(role);
				//refreshRelationshipProxy();
				
				// REFRESH KEYWORD TABLE DATA
				
				requests.keywordRequestNonRoo().findKeywordByStandRoleCount(standardizedRoleDetailsView[selectedTabId].getValue()).fire(new Receiver<Long>()
				{
						@Override
						public void onSuccess(Long response) 
						{
							Log.info("~Success Call....");
							Log.info("~REFRESH KEYWORD TABLE DATA");
							Log.info("~findKeywordByStandRole()");
							Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.intValue());
							Log.info("~Fetch/SET Table Data");									
							//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
							//Range keywordTableRange= standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
							standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.intValue(),true);
							//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
							//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
						}
				});	
				
				final Range keywordRange = standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
				requests.keywordRequestNonRoo().findKeywordByStandRole(standardizedRoleDetailsView[selectedTabId].getValue(),keywordRange.getStart(),keywordRange.getLength()).fire(new Receiver<List<KeywordProxy>>()
				{
						@Override
						public void onSuccess(List<KeywordProxy> response) 
						{
							Log.info("~Success Call....");
							Log.info("~REFRESH KEYWORD TABLE DATA");
							Log.info("~findKeywordByStandRole()");
							Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.size());
							Log.info("~Fetch/SET Table Data");									
											//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
											//Range keywordTableRange= standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
											//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.size(),true);
											//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
											standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
						}
				});	
			}
			
		});								
	}	// END deleteKeywordClicked

	@Override
	public void deleteDoctorClicked(RoleParticipantProxy roleParticipantProxy,int i) 
	{
		
		//ScrolledTab Changes start
		//final int selectedTabId=roleDetailTabPanel.getTabBar().getSelectedTab();
		final int selectedTabId=roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
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
					
					//auther table pager change start
					requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 0).with("doctor").fire(new Receiver<Long>() 
						{
								@Override
								public void onSuccess(Long response) 
								{
									Log.info("~Success Call....");
									Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
									Log.info("~Set Data In Author Table: Resp Size: "+ response);														
									standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().authorTable.setRowCount(response.intValue(),true);								
								}
						});
					final Range autherRange = standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().authorTable.getVisibleRange();
					requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 0,autherRange.getStart(),autherRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
					{
							@Override
							public void onSuccess(List<RoleParticipantProxy> response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Author Table: Resp Size: "+ response.size());														
								standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().authorTable.setRowData(autherRange.getStart(),response);								
							}
					});
					
					//auther table pager change end
					
				}
				else	// REVIEWER DELETED
				{
					// REFRESH Reviewer Table
					
					requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 1).with("doctor").fire(new Receiver<Long>() 
							{
									@Override
									public void onSuccess(Long response) 
									{
										Log.info("~Success Call....");
										Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
										Log.info("~Set Data In Reviewer Table: Resp Size: "+ response);																							
										standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowCount(response.intValue(),true);								
									}
							});		
					final Range reviewerRange = standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().reviewerTable.getVisibleRange();
					requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId(), 1,reviewerRange.getStart(),reviewerRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
					{
							@Override
							public void onSuccess(List<RoleParticipantProxy> response) 
							{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: Resp Size: "+ response.size());																							
								standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowData(reviewerRange.getStart(),response);								
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
		//ScrolledTab Changes start
		//final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();
		final int selectedTabId = roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		requests.find(place.getProxyId()).with("standardizedRoles","standardizedRoles.keywords").fire(new OSCEReceiver<Object>()
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
		//ScrolledTab Changes start
		//final int selectedTabId = roleDetailTabPanel.getTabBar().getSelectedTab();
		final int selectedTabId = roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		// Issue Role
		//	V4
		//requests.doctorRequest().findAllDoctors().fire(new Receiver<List<DoctorProxy>>() {
		requests.doctorRequestNonRoo().findDoctorWithRoleTopic(standardizedRoleDetailsView[selectedTabId].getValue().getId()).fire(new Receiver<List<DoctorProxy>>() {
			@Override
			public void onSuccess(List<DoctorProxy> response) 
			{
				Log.info("~Success Call....");
				Log.info("~refreshDoctorList");
				Log.info("~Set Data In ValueListBox" + "Resp. Size: " + response.size());				
				
				//Issue # 122 : Replace pull down with autocomplete.
				//standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setValue(null);
				standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setSelected(null);
				//Issue # 122 : Replace pull down with autocomplete.
				
				// SPEC START MODIFIED =
				if(response.size()==0)
				{											
					System.out.println("~Keyword Null for Role " + selectedTabId );
					//standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible(false);
				}
				else
				{
					//standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible(true);
					//Issue # 122 : Replace pull down with autocomplete.
				
					//standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setAcceptableValues(response);
					
					DefaultSuggestOracle<DoctorProxy> suggestOracle1 = (DefaultSuggestOracle<DoctorProxy>) standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.getSuggestOracle();
					suggestOracle1.setPossiblilities(response);
					standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setSuggestOracle(suggestOracle1);
					//standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setRenderer(new DoctorProxyRenderer());
					standardizedRoleDetailsView[selectedTabId].getRoleRoleParticipantSubViewImpl().lstDoctor.setRenderer(new AbstractRenderer<DoctorProxy>() {

						@Override
						public String render(DoctorProxy object) {
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
					
					
				}
				// SPEC END MODIFIED =
			}

		});			
	}
	
	// SPEC END =
	
	//AssignMEnt I
	// Issue Role Module

	@Override
	public void addRoleBaseSubItem(RoleBaseItemProxy roleBaseItemProxy,CellTable<RoleTableItemProxy> table,RoleBaseTableItemViewImpl roleBaseTableItemViewImpl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roleTableItemEditButtonClicked(
			RoleTableItemProxy roleTableItem, Long id,
			CellTable<RoleTableItemProxy> table, int left,int top) {
		// TODO Auto-generated method stub
		
	}
	// E Issue Role Module

	
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

	public void createDefaultSubValueItem(final RoleTemplateProxy roleTemplateProxy)
	{		
		Log.info("create default sub value item");
	
		requests.roleBaseItemRequestNoonRoo().createRoleBaseItemValueForStandardizedRole(standardizedRoleDetailsView[selectedtab].getValue().getId(),roleTemplateProxy.getId()).fire(new Receiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				// TODO Auto-generated method stub
				Log.info("Successfully created");
				initRoleScript(selectedtab, roleTemplateProxy,false,false);
				
			}
		});
		/*requests.roleBaseItemRequestNoonRoo().findAllRoleBaseItemOnTemplateId(roleTemplateProxy.getId()).with("roleTableItem","roleItemAccess").fire(new Receiver<List<RoleBaseItemProxy>>() {
			@Override
			public void onSuccess(List<RoleBaseItemProxy> response) {
				Log.info("Total Result is :" +response.size());
											 
				Iterator<RoleBaseItemProxy> listRoleBaseItemProxy = response.iterator();
				
				while(listRoleBaseItemProxy.hasNext())
				{
										
					final RoleBaseItemProxy roleBaseItemProxy = listRoleBaseItemProxy.next();
					
					if(roleBaseItemProxy.getItem_defination().name().equals("table_item"))
					{
						if(roleBaseItemProxy.getDeleted())
							continue;											
						
						selectedtab=roleDetailTabPanel.getTabBar().getSelectedTab();
																				
						RoleTableItemProxy[] arrRoleTableItemProxy = new  RoleTableItemProxy[roleBaseItemProxy.getRoleTableItem().size()];								
						roleBaseItemProxy.getRoleTableItem().toArray(arrRoleTableItemProxy);
						List<RoleTableItemProxy> listRoleTableItemProxy = Arrays.asList(arrRoleTableItemProxy);						
						
						final List<RoleTableItemValueProxy> listRoleTableItemValueProxy = new ArrayList<RoleTableItemValueProxy>();
						
						Iterator<RoleTableItemProxy> roleTableItemProxy = listRoleTableItemProxy.iterator();
						while(roleTableItemProxy.hasNext()){
							
							RoleTableItemValueRequest roleTableItemValue = requests.roleTableItemValueRequest();
							final RoleTableItemValueProxy roleTableItemValueProxy = roleTableItemValue.create(RoleTableItemValueProxy.class); 
							roleTableItemValueProxy.setStandardizedRole(standardizedRoleDetailsView[selectedtab].getValue());
							roleTableItemValueProxy.setRoleTableItem(roleTableItemProxy.next());
							
							//role template spec
							//roleTableItemValueProxy.setValue("Value");
							roleTableItemValueProxy.setValue("");
							
							roleTableItemValue.persist().using(roleTableItemValueProxy).fire(new Receiver<Void>() {
								@Override
								public void onSuccess(Void response) {									
									Log.info("Persisted Role_Table_Item_Value");
						
									requests.find(roleTableItemValueProxy.stableId()).with("roleTableItem").fire(new Receiver<Object>() {

										@Override
										public void onSuccess(Object response) {
											
											Log.info("Size of ListRole To set " );
											listRoleTableItemValueProxy.add((RoleTableItemValueProxy)response);
											
													
										}
										
									});
									
								}
								
							});
						}						
					
					}
					else
					{
						if(roleBaseItemProxy.getDeleted())
							continue;
						
						
						
						// Add Values in RoleTableItem Table
						
						RoleSubItemValueRequest roleSubitemValueReq = requests.roleSubItemValueRequest();
						RoleSubItemValueProxy roleSubItemValueProxy = roleSubitemValueReq.create(RoleSubItemValueProxy.class);
						roleSubItemValueProxy.setRoleBaseItem(roleBaseItemProxy);
						roleSubItemValueProxy.setStandardizedRole(standardizedRoleDetailsView[selectedtab].getValue());
						
						//role template spec
						//roleSubItemValueProxy.setItemText("Enter Value");
						roleSubItemValueProxy.setItemText("");
						
						roleSubitemValueReq.persist().using(roleSubItemValueProxy).fire();
						Log.info("Save Role_sub_item_value");											
																						
					}
				}						
			}						
			
		});  */
		
	
	}
	
	// To ADD All RoleBaseItem For Particular RoleTemplate Dynamically
	int selectedtab;
	@Override
	public void roleTemplateValueButtonClicked(final RoleTemplateProxy roleTemplateProxy) {
	Log.info("Call roleTemplateValueButtonClicked");		
	
		//ScrolledTab Changes start
		//selectedtab=roleDetailTabPanel.getTabBar().getSelectedTab();
	selectedtab=roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		standardizedRoleProxy= standardizedRoleDetailsView[selectedtab].getValue();
		StandardizedRoleRequest request = requests.standardizedRoleRequest();
		standardizedRoleProxy = request.edit(standardizedRoleProxy);
		
		Log.info(standardizedRoleProxy.getId()+"---");
		standardizedRoleProxy.setRoleTemplate(roleTemplateProxy);
		// Highlight onViolation
		Log.info("Map Size: " + standardizedRoleDetailsView[selecTab].getStandardizedRoleTemplateMap().size());
		request.persist().using(standardizedRoleProxy).fire(new OSCEReceiver<Void>(standardizedRoleDetailsView[selecTab].getStandardizedRoleTemplateMap()) {
			// E Highlight onViolation
			@Override
			public void onSuccess(Void response) {
				Log.info("stand role update succesfully"+ standardizedRoleProxy.getId());
				standardizedRoleDetailsView[selectedtab].setValue(standardizedRoleProxy);
				//initRoleScript(selectedtab, roleTemplateProxy,true);
				if(roleTemplateProxy!=null)
				{
					createDefaultSubValueItem(roleTemplateProxy);	
				}
				
			}
		});
			
		
		
	}
	
	/*public void roleTemplateValueButtonClicked(final RoleTemplateProxy roleTemplateProxy) {
			
		
		selectedtab=0;
		requests.roleBaseItemRequestNoonRoo().findAllRoleBaseItemOnTemplateId(roleTemplateProxy.getId()).with("roleTableItem","roleItemAccess").fire(new Receiver<List<RoleBaseItemProxy>>() {
			@Override
			public void onSuccess(List<RoleBaseItemProxy> response) {
				Log.info("Total Result is :" +response.size());
				
				
				//role template spec
				selectedtab=roleDetailTabPanel.getTabBar().getSelectedTab();
				standardizedRoleProxy= standardizedRoleDetailsView[selectedtab].getValue();
				StandardizedRoleRequest request = requests.standardizedRoleRequest();
				standardizedRoleProxy = request.edit(standardizedRoleProxy);
				
				Log.info(standardizedRoleProxy.getId()+"---");
				standardizedRoleProxy.setRoleTemplate(roleTemplateProxy);
				request.persist().using(standardizedRoleProxy).fire(new Receiver<Void>() {

					@Override
					public void onSuccess(Void response) {
						// TODO Auto-generated method stub
						Log.info("stand role update succesfully"+ standardizedRoleProxy.getId());
					}
				});
				//role template spec
				
				
				
				
				
			//	standardizedRoleDetailsView[selectedtab].getRoleBaseItemVerticalPanel().add(roleBaseTableItemViewImpl);
				 
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
					
					//	Log.info("standardizedRoleDetailsView[selectedtab]" + standardizedRoleDetailsView[selectedtab].getValue().getShortName());
						
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
							
							//role template spec
							//roleTableItemValueProxy.setValue("Value");
							roleTableItemValueProxy.setValue("");
							
							roleTableItemValue.persist().using(roleTableItemValueProxy).fire(new Receiver<Void>() {
								@Override
								public void onSuccess(Void response) {									
									Log.info("Persisted Role_Table_Item_Value");
						
									requests.find(roleTableItemValueProxy.stableId()).with("roleTableItem").fire(new OSCEReceiver<Object>() {
										
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
						
						//role template spec
						//roleSubItemValueProxy.setItemText("Enter Value");
						roleSubItemValueProxy.setItemText("");
						
						roleSubitemValueReq.persist().using(roleSubItemValueProxy).fire();
						Log.info("Save Role_sub_item_value");
						// To remove extra RoleBase Item Proxy view Component
						
						roleBaseTableItemViewImpl.roleBaseItemDisclosurePanel.setStyleName("border=0");
						roleBaseTableItemViewImpl.table.removeFromParent();
						
						//role template spec
						//roleBaseTableItemViewImpl.description.setText("Enter Value");
						roleBaseTableItemViewImpl.description.setText("");
						
						
						//	view.getTableItem().add(roleBaseTableItemViewImpl);
						int selectedtab2=roleDetailTabPanel.getTabBar().getSelectedTab();
						standardizedRoleDetailsView[selectedtab2].getRoleBaseItemVerticalPanel().add(roleBaseTableItemViewImpl);
						
						
						// To Add Access Values	
						addAccessValue(roleBaseItemProxy,roleBaseTableItemViewImpl);
					}
				}
				}
			
		});
	}*/
	
	
	public void addAccessValue(RoleBaseItemProxy roleBaseItemProxy,RoleBaseTableItemValueViewImpl roleBaseTableItemViewImpl){
		RoleBaseTableAccessViewImpl roleBaseTableAccssViewImpl = new RoleBaseTableAccessViewImpl();
		//roleBaseTableAccssViewImpl.setDelegate(roleDetailActivity);
		Set<RoleItemAccessProxy> setroleItemAccess = roleBaseItemProxy.getRoleItemAccess();
		Iterator<RoleItemAccessProxy> listRoleItemAccesProxy = setroleItemAccess.iterator();
		roleBaseTableItemViewImpl.description.setEnabled(false);
		while(listRoleItemAccesProxy.hasNext())
		{
			roleBaseTableAccssViewImpl.accessDataLabel.setText(listRoleItemAccesProxy.next().getName());
			roleBaseTableItemViewImpl.accessDataPanel.add(roleBaseTableAccssViewImpl);
			roleBaseTableItemViewImpl.description.setEnabled(false);
			roleBaseTableAccssViewImpl.accessDataCloseButton.setVisible(false);
		}
	}

	@Override
	public void addRoleScriptTableItemValue(
			final RoleTableItemValueProxy roleTableItemValueProxy,final Long roleBaseItemProxyid,
			final CellTable<RoleTableItemValueProxy> table,int left,int top) {
	
		//ScrolledTab Changes start
		//final int strId =roleDetailTabPanel.getTabBar().getSelectedTab();
		final int strId =roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		
		toolTip= new PopupPanel(true);
		
		toolTip.setWidth("200px");
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
		
		toolTipChange = new IconButton(constants.save());
		toolTipChange.setIcon("disk");
	 
		toolTipChange.setWidth("60px");
		toolTipChange.setHeight("25px");       
		
		int x=table.getAbsoluteLeft();
		int y=table.getAbsoluteTop(); 
		toolTipContentPanel.add(toolTipTextBox);
		toolTipContentPanel.add(toolTipChange);
	     
		toolTipTextBox.setText(roleTableItemValueProxy.getValue());
	    
		// Highlight onViolation
		final Map<String, Widget> roleTableItemValueMap=new HashMap<String, Widget>();
		roleTableItemValueMap.put("value", toolTipTextBox);
		roleTableItemValueMap.put("roleTableItem", toolTipTextBox);
		// E Highlight onViolation
		   
		toolTip.add(toolTipContentPanel);   // you can add any widget here
	        
		// Issue Role V1
		//toolTip.setPopupPosition(x+110,y-50);
		//toolTip.setPopupPosition(left,top);
		
		// Issue Role V2
		toolTip.setPopupPosition(left-150,top);
		// E: Issue Role V2
		
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
									// Highlight onViolation
									roleTableItemValueReq.persist().using(roleTableItemValueProxy).fire(new OSCEReceiver<Void>(roleTableItemValueMap){
									// E Highlight onViolation
										
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
	
	
	
	//refresh a tab / refresh to previous standardizedRoleDetailsView	
	public void refreshPreviousStandardizedRoleDetailView(StandardizedRoleDetailsViewImpl view)
	{
		//clear views
		Log.info("refreshStandardizedRoleDetailView");
		view.checkListsVerticalPanel.clear();
		
		int index =roleDetailTabPanel.getSelectedIndex();
		selectedtab=roleDetailTabPanel.getSelectedIndex();
		standardizedRoleDetailsView[index] = view;
		
		standardizedRoleProxy= standardizedRoleDetailsView[selectedtab].getValue();
		
		StandardizedRoleProxy proxy = standardizedRoleProxy;
		
		// Boolean object has to be checked for null value?
		//if(proxy.getActive().booleanValue() == true) 	{
		
		

		
		System.out.println("length previous version--"+proxy.getPreviousVersion());
		if(proxy.getPreviousVersion()==null)
		{
			standardizedRoleDetailsView[index].previous.setEnabled(false);
		}
		standardizedRoleDetailsView[index].setBaseProxy(proxy);
		//standardizedRoleDetailsView[index].home.setVisible(false);
		standardizedRoleDetailsView[index].setValue(proxy);
		//roleDetailTabPanel.insert(standardizedRoleDetailsView[index],"" + proxy.getShortName(), index);
		standardizedRoleDetailsView[index].shortName.setInnerText(proxy.getShortName() == null ? "": String.valueOf(proxy.getShortName()));						
		standardizedRoleDetailsView[index].longName.setInnerText(proxy.getLongName() == null ? "": String.valueOf(proxy.getLongName()));
		// Issue Role
		standardizedRoleDetailsView[index].roleType.setInnerText(proxy.getRoleType() == null ? "": proxy.getRoleType().name()); // ADDED
		standardizedRoleDetailsView[index].studyYear.setInnerText(new EnumRenderer<StudyYears>().render(proxy.getStudyYear())); // ADDED
		standardizedRoleDetailsView[index].factor.setInnerText(proxy.getFactor() == null ? "": proxy.getFactor().toString());
		standardizedRoleDetailsView[index].sum.setInnerText(proxy.getSum() == null ? "": proxy.getSum().toString());
		
		// E: Issue Role
		standardizedRoleDetailsView[index].labelLongNameHeader.setText("" + proxy.getLongName()+ " ("+proxy.getMainVersion()+"."+proxy.getSubVersion()+")");

		//setRoleDetailTabData(proxy, response, index);
		standardizedRoleDetailsView[index].rolePanel.selectTab(0);
		standardizedRoleDetailsView[index].setDelegate(roleDetailActivity);

		// Assignment :H[
		fileView[index] = standardizedRoleDetailsView[index].getRoleFileSubViewImpl();
		fileView[index].setValue(proxy);
		fileView[index].setDelegate(roleDetailActivity);
		setFileTable(fileView[index].getTable(), proxy.getId(),index);
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

		roomMaterialsDetailsSubView[index].setValue(proxy);
	// Issue Role Module
		roomMaterialsDetailsSubView[index].getRoomMaterialsPopupViewImpl().setDelegate(roleDetailActivity);
		roomMaterialsDetailsSubView[index].getRoomMaterialsPopupViewImpl().setMaterialListPickerValues(Collections.<MaterialListProxy> emptyList());						
	//E Issue Role Module
	
final int index2 = index;

			requests.materialListRequest().findMaterialListEntries(0, 50).with(MaterialListProxyRenderer.instance().getPaths()).fire(new Receiver<List<MaterialListProxy>>() {
					public void onSuccess(List<MaterialListProxy> response) 
					{
						List<MaterialListProxy> values = new ArrayList<MaterialListProxy>();
						values.add(null);
						values.addAll(response);
						// Issue Role Module
						roomMaterialsDetailsSubView[index2].getRoomMaterialsPopupViewImpl().setMaterialListPickerValues(values);										
						// roomMaterialsDetailsSubView[index2].setMaterialListPickerValues(values);
						// E Issue Role Module
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
		
		requests.osceRequestNonRoo().findAllOsceSemester(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId(), roleOsceSemesterSubView.getStartDate().getValue(),roleOsceSemesterSubView.getEndDate().getValue()).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() {

			@Override
			public void onSuccess(List<OsceProxy> response) {
				// TODO Auto-generated method stub
				if(response!=null)
				{
					
				
				roleOsceSemesterSubView.getOsceSemesterTable().setRowCount(response.size());
				roleOsceSemesterSubView.getOsceSemesterTable().setRowData(response);
				}
				else
				{
					Log.info("record not found");
				}
				
			}
		});

		
		standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().setDelegate(roleDetailActivity);
		standardizedRoleDetailsView[index].getRoleKeywordSubViewImpl().setDelegate(roleDetailActivity);
		authorTable = standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().authorTable; // ==>
		reviewerTable = standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().authorTable; // ==>
	
		ProvidesKey<RoleParticipantProxy> autkeyProvider = ((AbstractHasData<RoleParticipantProxy>) authorTable).getKeyProvider();
		
		final int innerindex = index;
		final int getStandardizedRole = Integer	.parseInt(standardizedRoleDetailsView[index].getValue().getId().toString());
		
		requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 0).with("doctor").fire(new Receiver<Long>() 
				{
						@Override
						public void onSuccess(Long response) 
						{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Author Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response);
								standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.setRowCount(response.intValue(),true);
						}
				});

		
		final Range autherRange = standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().authorTable.getVisibleRange();
		
		requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 0,autherRange.getStart(),autherRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
		{
				@Override
				public void onSuccess(List<RoleParticipantProxy> response) 
				{
						Log.info("~Success Call....");
						Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
						Log.info("~Set Data In Author Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response.size());
						standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().authorTable.setRowData(autherRange.getStart(),response);
				}
		});

		requests.roleParticipantRequestNonRoo().countDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 1).with("doctor").fire(new Receiver<Long>() 
				{
						@Override
						public void onSuccess(Long response) 
						{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response);
								standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowCount(response.intValue(),true);
						}
				});
		final Range reviewerRange = standardizedRoleDetailsView[index].getRoleRoleParticipantSubViewImpl().reviewerTable.getVisibleRange();
		requests.roleParticipantRequestNonRoo().findDoctorWithStandardizedRoleAndRoleTopic(standardizedRoleDetailsView[index].getValue().getId(), 0,reviewerRange.getStart(),reviewerRange.getLength()).with("doctor").fire(new Receiver<List<RoleParticipantProxy>>() 
				{
						@Override
						public void onSuccess(List<RoleParticipantProxy> response) 
						{
								Log.info("~Success Call....");
								Log.info("~findDoctorWithStandardizedRoleAndRoleTopic()");
								Log.info("~Set Data In Reviewer Table: " +"SR: " + getStandardizedRole+ "Resp Size: "+ response.size());
								standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().reviewerTable.setRowData(reviewerRange.getStart(),response);
						}
				});
		

		
	//	Log.info("==>>>Index: "+ innerindex+ " ID Pass: "+ ((RoleTopicProxy) response).getId()+ "SR ID: "+ standardizedRoleDetailsView[innerindex].getValue().getId());

		// Issue Role
		// V4
		//requests.doctorRequest().findAllDoctors().fire(new Receiver<List<DoctorProxy>>() {
		requests.doctorRequestNonRoo().findDoctorWithRoleTopic(standardizedRoleDetailsView[innerindex].getValue().getId()).fire(new Receiver<List<DoctorProxy>>() 
		{ 
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
							//Issue Role
							//standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible();
						}
						else
						{
							//Issue Role
							//standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setVisible(true);
							//Issue # 122 : Replace pull down with autocomplete.
							
							//standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setAcceptableValues(response);
							
							DefaultSuggestOracle<DoctorProxy> suggestOracle1 = (DefaultSuggestOracle<DoctorProxy>) standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.getSuggestOracle();
							suggestOracle1.setPossiblilities(response);


							standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setSuggestOracle(suggestOracle1);

						//	standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setRenderer(new DoctorProxyRenderer());
							
							standardizedRoleDetailsView[innerindex].getRoleRoleParticipantSubViewImpl().lstDoctor.setRenderer(new AbstractRenderer<DoctorProxy>() {

								@Override
								public String render(DoctorProxy object) {
									// TODO Auto-generated method stub
									if(object!=null)
									{
									return object.getName()+"";
									}
									else
									{
										return "";
									}
								}
							});

							//Issue # 122 : Replace pull down with autocomplete.
						}
						// SPEC END MODIFIED =
					}

				});

		requests.keywordRequestNonRoo().findKeywordByStandRoleCount(standardizedRoleDetailsView[index].getValue()).fire(new Receiver<Long>()						 
		{
				@Override
				public void onSuccess(Long response) 
				{									
					Log.info("~Success Call....");
					Log.info("~findKeywordByStandRole()");
					Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.intValue());
					//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
				//	Range keywordTableRange= standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
					standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.intValue(),true);
			//		standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
				//	standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
				}
		});
		
		final Range keywordRange = standardizedRoleDetailsView[index].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
		requests.keywordRequestNonRoo().findKeywordByStandRole(standardizedRoleDetailsView[index].getValue(),keywordRange.getStart(),keywordRange.getLength()).fire(new Receiver<List<KeywordProxy>>()
		{
				@Override
				public void onSuccess(List<KeywordProxy> response) 
				{									
					Log.info("~Success Call....");
					Log.info("~findKeywordByStandRole()");
					Log.info("~Set Data In Keyword Table:" + "Resp. Size: " + response.size());
				//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(response);
				//Range keywordTableRange= standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.getVisibleRange();
				//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowCount(response.size(),true);
				//standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setPageSize(5);
				standardizedRoleDetailsView[innerindex].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
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

		standardizedRoleDetailsView[index].getRoleLearningSubViewImpl().setDelegate(roleDetailActivity);
		
		final int innerindex2 = index;
		
		
		/*requests.mainSkillRequestNonRoo().findMainSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MainSkillProxy>>() {

			@Override
			public void onSuccess(List<MainSkillProxy> response) {								
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());
				Log.info("~~DATA SET");
			}
		});
*/
		//main skill change
		
				requests.mainSkillRequestNonRoo().countMainSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<Long>() {

					@Override
					public void onSuccess(Long response) {								
						/*standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.intValue(),true);
						Log.info("~~DATA SET");
					}
				});
				final Range mainSkillRange = standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.getVisibleRange();
				requests.mainSkillRequestNonRoo().findMainSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId(),mainSkillRange.getStart(),mainSkillRange.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MainSkillProxy>>() {

					@Override
					public void onSuccess(List<MainSkillProxy> response) {								
					/*	standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(mainSkillRange.getStart(),response);
						Log.info("~~DATA SET");
					}
				});
							
				//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
				//main skill change end
	
		//minor sikk start		
		/*requests.minorSkillRequestNonRoo().findMinorSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MinorSkillProxy>>() {

			@Override
			public void onSuccess(List<MinorSkillProxy> response) {
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowCount(response.size());
				Log.info("DATA IS SET FOR MINOR TABLE");
			}
		});		*/	
				requests.minorSkillRequestNonRoo().countMinorSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<Long>() {

					@Override
					public void onSuccess(Long response) {								
						/*standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowCount(response.intValue(),true);
						Log.info("~~DATA SET");
					}
				});
				final Range minorSkillRange = standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.getVisibleRange();
				requests.minorSkillRequestNonRoo().findMinorSkillEntriesByRoleID(standardizedRoleDetailsView[index].getValue().getId(),minorSkillRange.getStart(),minorSkillRange.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MinorSkillProxy>>() {

					@Override
					public void onSuccess(List<MinorSkillProxy> response) {								
					/*	standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().minorTable.setRowData(mainSkillRange.getStart(),response);
						Log.info("~~DATA SET");
					}
				});
						
		
		//minor sikk end
		// SPEC END =
		
		//Assignment E[
		//set CheckList Title
		//Log.info(proxy.getCheckList().getTitle());
		if(proxy.getCheckList()!=null)
		{
			
		((StandardizedRoleDetailsViewImpl)standardizedRoleDetailsView[index]).roleSubPanel.getTabBar().setTabText(0,proxy.getCheckList().getTitle()==null ? constants.checkList() : constants.checkList() + " "+proxy.getCheckList().getTitle());					
		
		Log.info("checklisttopic Proxy Size:" + proxy.getCheckList().getCheckListTopics().size());
		Iterator<ChecklistTopicProxy> topicIterator=proxy.getCheckList().getCheckListTopics().iterator();
//		RoleDetailsChecklistSubViewChecklistQuestionItemView queView[]=new RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl[proxy.getCheckList().getCheckListTopics().size()];
		//create Topic View
		
		roleDetailActivity.questionCount=0;
		while(topicIterator.hasNext())
		{
			ChecklistTopicProxy topicProxy=topicIterator.next();
			RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView=createCheckListTopic(index,topicProxy);
			
			// SPEC Change
			topicView.addCheckListQuestionButton.setEnabled(false);
			topicView.importQuestionButton.setEnabled(false);
			
			topicView.addCheckListQuestionButton.removeStyleName("expbtn");
			topicView.importQuestionButton.removeStyleName("expbtn");
			topicView.addCheckListQuestionButton.addStyleName("disableButton");
			topicView.importQuestionButton.addStyleName("disableButton");
			
			topicView.getEdit().setEnabled(false);
			topicView.getDelete().setEnabled(false);
			topicView.getUp().setVisible(false);
			topicView.getDown().setVisible(false);
			standardizedRoleDetailsView[index].getDragController().makeNotDraggable(topicView);
			// SPEC Change
			
			//create Question View
			Iterator<ChecklistQuestionProxy> questionIterator=topicProxy.getCheckListQuestions().iterator();
			
			
			while(questionIterator.hasNext())
			{
				ChecklistQuestionProxy questionProxy=questionIterator.next();
				RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl questionView=createQuestionView(index, questionProxy, topicView,++roleDetailActivity.questionCount);
				
				// SPEC Change
				questionView.addCriteriaButton.setEnabled(false);
				questionView.addOptionButton.setEnabled(false);
				questionView.edit.setEnabled(false);
				questionView.delete.setEnabled(false);
				questionView.up.setVisible(false);
				questionView.down.setVisible(false);
				questionView.questionItemLbl.setWidth("538px");
				topicView.getDragController().makeNotDraggable(questionView);
				// SPEC Change
				
				//create Criteria View
				Iterator<ChecklistCriteriaProxy> criteriaIterator=questionProxy.getCheckListCriterias().iterator();
				while(criteriaIterator.hasNext())
				{
					ChecklistCriteriaProxy criteriaProxy=criteriaIterator.next();
					createCriteriaView(index, criteriaProxy, questionView);
					// SPEC Change
					
					RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl criteriaItemView = (RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl)questionView.criteriaHorizontalPanel.getWidget(questionView.criteriaHorizontalPanel.getWidgetCount()-1);
					
					criteriaItemView.getEdit().setEnabled(false);
					criteriaItemView.getDelete().setEnabled(false);
					
					questionView.getCriteriaDragController().makeNotDraggable(criteriaItemView.asWidget());
					// SPEC Change
					
				}
				
				//create Option View
				
				Iterator<ChecklistOptionProxy> optionIterator=questionProxy.getCheckListOptions().iterator();
				while(optionIterator.hasNext())
				{
					ChecklistOptionProxy optionProxy=optionIterator.next();
					createOptionView(index, optionProxy, questionView);
					
					//SPEC Change
					
					RoleDetailsChecklistSubViewChecklistOptionItemViewImpl optionItemViewImpl = (RoleDetailsChecklistSubViewChecklistOptionItemViewImpl) questionView.optionVerticalPanel.getWidget(questionView.optionVerticalPanel.getWidgetCount()-1);
					optionItemViewImpl.getEditBtn().setEnabled(false);
					optionItemViewImpl.getDeleteBtn().setEnabled(false);
					questionView.getOptionDragController().makeNotDraggable(optionItemViewImpl.asWidget());
					//SPEC Change
					
				}
				
				
				questionView.setChecklistTopicProxy(topicProxy);
				questionView.setRoleDetailsChecklistSubViewChecklistTopicItemView(topicView);
			}
		}
		
		}
		//Assignment E]
		
		
////Initialise RoleScript (I) Assignment I
		
		
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
		
		//temp[
		initRoleScript(standarDizedViewIndex,proxy.getRoleTemplate(),false,true);

		//SPEC Change
		
		setEnableAllViews(standardizedRoleDetailsView[index], false);
		
		//SPEC Change
		
		// End I
		//index++;
		//}
	}
		
	//SPEC Change
	private void setEnableAllViews(StandardizedRoleDetailsViewImpl standardizedRoleDetailsView, boolean enable){
		
		int index =roleDetailTabPanel.getSelectedIndex();
		
		RoleFileSubViewImpl fileView = standardizedRoleDetailsView.getRoleFileSubViewImpl();
		RoomMaterialsDetailsSubViewImpl roomMaterialsDetailsSubView = standardizedRoleDetailsView.getRoomMaterialsDetailsSubViewImpl();
		RoleOtherSearchCriteriaViewImpl simpleSearchCriteriaView = standardizedRoleDetailsView.getRoleOtherSearchCriteriaViewImpl();
		
		fileView.getFileDescription().setEnabled(enable);
		fileView.getFileUpload().setEnabled(enable);
		fileView.getNewButton().setEnabled(enable);
		
		roomMaterialsDetailsSubView.getNewButton().setEnabled(enable);
		
		simpleSearchCriteriaView.getAddSimpleSearch().setEnabled(enable);
		simpleSearchCriteriaView.getSearchName().setEnabled(enable);
		simpleSearchCriteriaView.getSearchValue().setEnabled(enable);
		
		standardizedRoleDetailsView.edit.setEnabled(enable);
		standardizedRoleDetailsView.delete.setEnabled(enable);
		standardizedRoleDetailsView.copy.setEnabled(enable);
		
		standardizedRoleDetailsView.getImportTopicButton().setEnabled(enable);
		standardizedRoleDetailsView.addCheckListTopicButton.setEnabled(enable);
		
		standardizedRoleDetailsView.roleTemplateListBox.setEnabled(enable);
		standardizedRoleDetailsView.roleTemplateValueButon.setEnabled(enable);
		
		standardizedRoleDetailsView.getRoleKeywordSubViewImpl().KeywordAddButton.setEnabled(enable);
		standardizedRoleDetailsView.getRoleKeywordSubViewImpl().keywordSugestionBox.setEnabled(enable);
		standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().btnAddAuthor.setEnabled(enable);
		standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().btnAddReviewer.setEnabled(enable);
		standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().lstDoctor.setEnabled(enable);
		standardizedRoleDetailsView.getRoleLearningSubViewImpl().btnAdd.setEnabled(enable);
		
		standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().getAddAnamnesis().setEnabled(enable);
		standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().getAddBasicData().setEnabled(enable);
		standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().getAddLanguage().setEnabled(enable);
		standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().getAddMaritialStatus().setEnabled(enable);
		standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().getAddNationality().setEnabled(enable);
		standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().getAddProfession().setEnabled(enable);
		standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().getAddScar().setEnabled(enable);
		standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().getAddWorkPermission().setEnabled(enable);

		roleOsceSemesterSubView.getStartDate().setEnabled(enable);
		roleOsceSemesterSubView.getEndDate().setEnabled(enable);
		roleOsceSemesterSubView.getSearchButton().setEnabled(enable);
		if(enable){
			
			standardizedRoleDetailsView.addCheckListTopicButton.removeStyleName("disableButton");
			standardizedRoleDetailsView.addCheckListTopicButton.addStyleName("expTopicButton");
			standardizedRoleDetailsView.getImportTopicButton().removeStyleName("disableButton");
			standardizedRoleDetailsView.getImportTopicButton().addStyleName("expTopicButton");
			
			standardizedRoleDetailsView.getRoleFileSubViewImpl().addLastThreColumns();
			standardizedRoleDetailsView.getRoomMaterialsDetailsSubViewImpl().addLastThreeColumns();
			standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().addAuthorLastColumn();
			standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().addReviewerLastColumn();
			standardizedRoleDetailsView.getRoleKeywordSubViewImpl().addLastColumn();
			standardizedRoleDetailsView.getRoleLearningSubViewImpl().addMinorTableLastColumn();
			standardizedRoleDetailsView.getRoleLearningSubViewImpl().addMajorTableLastColumn();
			
			standardizedRoleDetailsView.getRoleOtherSearchCriteriaViewImpl().addLastThreeColumn();
			standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().addLastColumn();
		}else{
			
			standardizedRoleDetailsView.addCheckListTopicButton.removeStyleName("expTopicButton");
			standardizedRoleDetailsView.addCheckListTopicButton.addStyleName("disableButton");
			standardizedRoleDetailsView.getImportTopicButton().removeStyleName("expTopicButton");
			standardizedRoleDetailsView.getImportTopicButton().addStyleName("disableButton");
			

//			fileView[index].getTable().setColumnWidth(fileView[index].getTable().getColumn(1), 0, Unit.PX);
			
			// Remove column from file Table
			
//			if(fileView.getTable() != null){
//				if(fileView.getTable().getColumnCount() > 4){
//					fileView.getTable().removeColumn( fileView.getTable().getColumnCount()-1);
//					fileView.getTable().removeColumn( fileView.getTable().getColumnCount()-1);
//					fileView.getTable().removeColumn( fileView.getTable().getColumnCount()-1);
//				}
//			}
			standardizedRoleDetailsView.getRoleFileSubViewImpl().removeLastThreeColumns();

//			// Remove column from room Table
//			CellTable<UsedMaterialProxy> roomTable = standardizedRoleDetailsView.getRoomMaterialsDetailsSubViewImpl().getUsedMaterialTable();
//			if(roomTable != null){
//				if(roomTable.getColumnCount() > 5){
//					roomTable.removeColumn(roomTable.getColumnCount()-1);
//					roomTable.removeColumn(roomTable.getColumnCount()-1);
//					roomTable.removeColumn(roomTable.getColumnCount()-1);
//				}
//			}
			standardizedRoleDetailsView.getRoomMaterialsDetailsSubViewImpl().removeLastThreeColumns();
//			
//			// Remove column from author Table
//			
//			CellTable<RoleParticipantProxy> authorTable = standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().authorTable;
//			
//			if(authorTable != null){
//				if(authorTable.getColumnCount() > 1){
//					authorTable.removeColumn(authorTable.getColumnCount()-1);
//				}
//			}
			standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().removeAuthorLastColumn();
//			
//			// Remove column from reviewer Table
//			
//			CellTable<RoleParticipantProxy> reviewerTable = standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().reviewerTable;
//			
//			if(reviewerTable != null){
//				if(reviewerTable.getColumnCount() > 1){
//					reviewerTable.removeColumn(reviewerTable.getColumnCount()-1);
//				}
//			}
			standardizedRoleDetailsView.getRoleRoleParticipantSubViewImpl().removeReviewerLastColumn();
//
//			// Remove column from keyword Table
//			CellTable<KeywordProxy> keywordTable = standardizedRoleDetailsView.getRoleKeywordSubViewImpl().getKeywordTable();
//			
//			if(keywordTable != null){
//				if(keywordTable.getColumnCount() > 1){
//					keywordTable.removeColumn(keywordTable.getColumnCount()-1);
//				}
//			}
			standardizedRoleDetailsView.getRoleKeywordSubViewImpl().removeLastColumn();
//			
//			// Remove column from minor skill Table
//			CellTable<MinorSkillProxy> minorTable = standardizedRoleDetailsView.getRoleLearningSubViewImpl().minorTable;
//			
//			if(minorTable != null){
//				if(minorTable.getColumnCount() > 4){
//					minorTable.removeColumn(minorTable.getColumnCount()-1);
//				}
//			}
			standardizedRoleDetailsView.getRoleLearningSubViewImpl().removeMinorTableLastColumn();
//			
//			// Remove column from major skill Table
//			CellTable<MainSkillProxy> majorTable = standardizedRoleDetailsView.getRoleLearningSubViewImpl().majorTable;
//			
//			if(majorTable != null){
//				if(majorTable.getColumnCount() > 4){
//					majorTable.removeColumn(majorTable.getColumnCount()-1);
//				}
//			}
			standardizedRoleDetailsView.getRoleLearningSubViewImpl().removeMajorTableLastColumn();
			
			standardizedRoleDetailsView.getRoleOtherSearchCriteriaViewImpl().removeLastThreeColumns();
			standardizedRoleDetailsView.getStandartizedPatientAdvancedSearchSubViewImpl().removeLastColumn();
	
	
			
		}
		
	}	
	//SPEC Change
	
	
	// Highlight onViolation
	@Override
	public void addRichTextAreaValue(final RoleBaseItemProxy roleBaseItemProxy,final RichTextArea description,int majorOrMinar, final Map roleSubItemValueMap,final DivElement descriptionValue,final IconButton saveRichTextArea) {
	// E Highlight onViolation
		
		//description.setEnabled(false);
		System.out.println("Major Change Or minor change--"+majorOrMinar);
		
		//ScrolledTab Changes start
		//selectedtab =roleDetailTabPanel.getTabBar().getSelectedTab();
		selectedtab =roleDetailTabPanel.getSelectedIndex();
		//ScrolledTab Changes end
		standardizedRoleProxy= standardizedRoleDetailsView[selectedtab].getValue();
		if(majorOrMinar==0)
		{
		//	Log.info("role proxy---"+standardizedRoleProxy.getShortName());						
			StandardizedRoleRequest request = requests.standardizedRoleRequest();
			standardizedRoleProxy = request.edit(standardizedRoleProxy);
			Log.info("role proxy short name---"+standardizedRoleProxy.getShortName());
			Log.info("role proxy keyword---"+standardizedRoleProxy.getKeywords());
			Log.info("role proxy role script---"+standardizedRoleProxy.getRoleScript());
			Log.info("role proxy advance search---"+standardizedRoleProxy.getAdvancedSearchCriteria());
			Log.info("role proxy osce post---"+standardizedRoleProxy.getOscePosts());
			Log.info("role proxy role participant---"+standardizedRoleProxy.getRoleParticipants());
			Log.info("role proxy simple search ---"+standardizedRoleProxy.getSimpleSearchCriteria());
			Log.info("role proxy template---"+standardizedRoleProxy.getRoleTemplate());
			
			if(standardizedRoleProxy.getSubVersion()==null)
			{
				standardizedRoleProxy.setSubVersion(1);
			}
			else
			{
				standardizedRoleProxy.setSubVersion(standardizedRoleProxy.getSubVersion()+1);
			}
			// Highlight onViolation
			request.persist().using(standardizedRoleProxy).fire(new OSCEReceiver<Void>() {
				// E Highlight onViolation
				@Override
				public void onSuccess(Void response) {
					Log.info("stand role create updated"+ standardizedRoleProxy.getId());
				
				}
			});
	
			Log.info("Html size--"+description.getHTML().length());
	
	RoleSubItemValueRequest roleSubItemValueReq = requests.roleSubItemValueRequest();
	RoleSubItemValueProxy roleSubItemValueProxy = roleSubItemValueReq.edit(roleBaseItemProxy.getRoleSubItem().iterator().next());
			roleSubItemValueProxy.setItemText(description.getHTML());
			
			roleSubItemValueProxy.setStandardizedRole(standardizedRoleProxy);
			// Highlight onViolation
			Log.info("Map Size: " + roleSubItemValueMap.size());
			roleSubItemValueReq.persist().using(roleSubItemValueProxy).fire(new OSCEReceiver<Void>(roleSubItemValueMap) {
			// E Highlight onViolation

				@Override
				public void onSuccess(Void response) {
					Log.info("RichTextArea Value edited succeessfully");
				//	Window.alert("Rich Text Value Set Successfully");
					
					standardizedRoleDetailsView[selectedtab].setValue(standardizedRoleProxy);
					descriptionValue.setInnerHTML(description.getHTML());
					description.setEnabled(false);
					refreshSelectedTab();
					
				}
				@Override
				public void onViolation(Set<Violation> errors) {
					System.out.println("violate");
					description.setVisible(true);
					descriptionValue.setInnerHTML("");
					/*Iterator<Violation> iter = errors.iterator();
					String message = "";
					while (iter.hasNext()) {
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(" in rich text -" + message);*/
					saveRichTextArea.setVisible(true);
				}
				@Override
				public void onFailure(ServerFailure error) {
					// TODO Auto-generated method stub
					System.out.println("failure");
					description.setVisible(true);
					descriptionValue.setInnerHTML("");
					saveRichTextArea.setVisible(true);
					
				}
			});
			
		}
		else
		{
			Log.info("major changes");
			
			
			requests.standardizedRoleRequestNonRoo().createStandardizedRoleMajorVersion(standardizedRoleProxy.getId(),(roleBaseItemProxy.getRoleSubItem().iterator().next()).getId(), description.getHTML()).fire(new OSCEReceiver<StandardizedRoleProxy>() {

				@Override
				public void onSuccess(StandardizedRoleProxy newCreatedStandardizedRoleProxy) {
					// TODO Auto-generated method stub
					Log.info("successfully role created--"+newCreatedStandardizedRoleProxy.getId());
					standardizedRoleDetailsView[selectedtab].setValue(newCreatedStandardizedRoleProxy);
					Log.info("RichTextArea Value edited succeessfully");
					description.setEnabled(false);
					descriptionValue.setInnerHTML(description.getHTML());
						refreshSelectedTab();
				//	RoleSubItemValueRequest roleSubItemValueReq=requests.roleSubItemValueRequest();
				//	final RoleSubItemValueProxy roleSubItemValueProxy=roleSubItemValueReq.create(RoleSubItemValueProxy.class);
					
					//change for major version 17-10-2012 start
					/*RoleSubItemValueRequest roleSubItemValueReq = requests.roleSubItemValueRequest();
					RoleSubItemValueProxy roleSubItemValueProxy = roleSubItemValueReq.edit(roleBaseItemProxy.getRoleSubItem().iterator().next());
					
					//change for major version 17-10-2012 end
					//roleSubItemValueProxy.setItemText(description.getText());


					//final String text=roleSubItemValueProxy.getItemText();
					//roleBaseItemProxy.getRoleSubItem().iterator().next()
					roleSubItemValueProxy.setItemText(description.getHTML());
				//	roleSubItemValueProxy.setStandardizedRole(newCreatedStandardizedRoleProxy);
				//	roleSubItemValueProxy.setStandardizedRole(standardizedRoleProxy);
					//change for major minor start 17-10-2012
					//roleSubItemValueProxy.setRoleBaseItem(roleBaseItemProxy);
					//change for major minor end 17-10-2012
					standardizedRoleDetailsView[selectedtab].setValue(newCreatedStandardizedRoleProxy);
					// Highlight onViolation
					roleSubItemValueReq.persist().using(roleSubItemValueProxy).fire(new OSCEReceiver<Void>(roleSubItemValueMap) {
						// E Highlight onViolation
						@Override
						public void onSuccess(Void response) {
							Log.info("RichTextArea Value edited succeessfully");
							description.setEnabled(false);
							descriptionValue.setInnerHTML(description.getHTML());
							refreshSelectedTab();
						//	Window.alert("Rich Text Value Set Successfully");
						}
						@Override
						public void onViolation(Set<Violation> errors) {
							System.out.println("violate");
							description.setVisible(true);
							descriptionValue.setInnerHTML("");
							Iterator<Violation> iter = errors.iterator();
							String message = "";
							while (iter.hasNext()) {
								message += iter.next().getMessage() + "<br>";
							}
							Log.warn(" in rich text -" + message);
							saveRichTextArea.setVisible(true);
						}
						
						
						@Override
						public void onFailure(ServerFailure error) {
							// TODO Auto-generated method stub
							System.out.println("failure");
							description.setVisible(true);
							descriptionValue.setInnerHTML("");
							saveRichTextArea.setVisible(true);
							
						}
					});
*/
				}
			});
			//create new proxy
								
			/*StandardizedRoleRequest majorRequest = this.requests.standardizedRoleRequest();
			final StandardizedRoleProxy newStandardizedRoleProxy= majorRequest.create(StandardizedRoleProxy.class);
		
			
			newStandardizedRoleProxy.setShortName(standardizedRoleProxy.getShortName());
			newStandardizedRoleProxy.setLongName(standardizedRoleProxy.getLongName());
			newStandardizedRoleProxy.setStudyYear(standardizedRoleProxy.getStudyYear());
			newStandardizedRoleProxy.setRoleType(standardizedRoleProxy.getRoleType());
			newStandardizedRoleProxy.setMainVersion(1);
			newStandardizedRoleProxy.setSubVersion(1);
			newStandardizedRoleProxy.setActive(true);
			newStandardizedRoleProxy.setPreviousVersion(standardizedRoleProxy);
			
			
			newStandardizedRoleProxy.setCheckList(standardizedRoleProxy.getCheckList());//spec
			newStandardizedRoleProxy.setRoleTopic(standardizedRoleProxy.getRoleTopic());
			newStandardizedRoleProxy.setAdvancedSearchCriteria(standardizedRoleProxy.getAdvancedSearchCriteria());
			newStandardizedRoleProxy.setKeywords(standardizedRoleProxy.getKeywords());
			if(standardizedRoleProxy.getOscePosts()!= null)
			{
				newStandardizedRoleProxy.setOscePosts(standardizedRoleProxy.getOscePosts());
			}
			newStandardizedRoleProxy.setRoleParticipants(standardizedRoleProxy.getRoleParticipants());
			newStandardizedRoleProxy.setRoleTemplate(standardizedRoleProxy.getRoleTemplate());
			newStandardizedRoleProxy.setRoleParticipants(standardizedRoleProxy.getRoleParticipants());
			newStandardizedRoleProxy.setSimpleSearchCriteria(standardizedRoleProxy.getSimpleSearchCriteria());
			newStandardizedRoleProxy.setCaseDescription(standardizedRoleProxy.getCaseDescription());
			newStandardizedRoleProxy.setRoleScript(standardizedRoleProxy.getRoleScript());
			newStandardizedRoleProxy.setRoleTableItemValue(standardizedRoleProxy.getRoleTableItemValue());
			newStandardizedRoleProxy.setRoleSubItemValue(standardizedRoleProxy.getRoleSubItemValue());
			
			
			
			
			majorRequest.persist().using(newStandardizedRoleProxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					// TODO Auto-generated method stub
					
					Log.info("new Role successfully saved.");	
					standardizedRoleDetailsView[selectedtab].setValue(newStandardizedRoleProxy);
					
					
				}
				
				public void onFailure(ServerFailure error) {
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
			});

			
			
			
			//edit old value
			
//			standardizedRoleProxy= standardizedRoleDetailsView[selectedtab].getValue();
//			StandardizedRoleRequest request = requests.standardizedRoleRequest();
//			standardizedRoleProxy = request.edit(standardizedRoleProxy);
			Log.info("role proxy short name---"+standardizedRoleProxy.getShortName());
			Log.info("role proxy keyword---"+standardizedRoleProxy.getKeywords());
			Log.info("role proxy role script---"+standardizedRoleProxy.getRoleScript());
			Log.info("role proxy advance search---"+standardizedRoleProxy.getAdvancedSearchCriteria());
			Log.info("role proxy osce post---"+standardizedRoleProxy.getOscePosts());
			Log.info("role proxy role participant---"+standardizedRoleProxy.getRoleParticipants());
			Log.info("role proxy simple search ---"+standardizedRoleProxy.getSimpleSearchCriteria());
			Log.info("role proxy template---"+standardizedRoleProxy.getRoleTemplate());
			standardizedRoleProxy= standardizedRoleDetailsView[selectedtab].getValue();
			StandardizedRoleRequest request = requests.standardizedRoleRequest();
			standardizedRoleProxy = request.edit(standardizedRoleProxy);

			standardizedRoleProxy.setActive(false);
			if(standardizedRoleProxy.getMainVersion()==null)
			{
				standardizedRoleProxy.setMainVersion(1);
			}
			else
			{
				standardizedRoleProxy.setMainVersion(standardizedRoleProxy.getMainVersion()+1);
			}
						
			
			request.persist().using(standardizedRoleProxy).fire(new Receiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					Log.info("stand role update succesfully"+ standardizedRoleProxy.getId());
					RoleSubItemValueRequest roleSubItemValueReq = requests.roleSubItemValueRequest();
					RoleSubItemValueProxy roleSubItemValueProxy = roleSubItemValueReq.edit(roleBaseItemProxy.getRoleSubItem().iterator().next());
					roleSubItemValueProxy.setItemText(description.getText());
					roleSubItemValueProxy.setStandardizedRole(newStandardizedRoleProxy);
	roleSubItemValueReq.persist().using(roleSubItemValueProxy).fire(new Receiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("RichTextArea Value edited succeessfully");
						//	Window.alert("Rich Text Value Set Successfully");
						}
						@Override
						public void onViolation(Set<Violation> errors) {
							System.out.println("violate");
							Iterator<Violation> iter = errors.iterator();
							String message = "";
							while (iter.hasNext()) {
								message += iter.next().getMessage() + "<br>";
		}
							Log.warn(" in rich text -" + message);
						}
					});
				
				}
				
				@Override
				public void onViolation(Set<Violation> errors) {
					System.out.println("violate");
					Iterator<Violation> iter = errors.iterator();
					String message = "";
					while (iter.hasNext()) {
						message += iter.next().getMessage() + "<br>";
					}
					Log.warn(" in old Role -" + message);
				}
				
	});
	
			
			*/

			


		}
		
	/*RoleSubItemValueRequest roleSubItemValueReq = requests.roleSubItemValueRequest();
	RoleSubItemValueProxy roleSubItemValueProxy = roleSubItemValueReq.edit(roleBaseItemProxy.getRoleSubItem().iterator().next());
	roleSubItemValueProxy.setItemText(description.getText());
	
	roleSubItemValueReq.persist().using(roleSubItemValueProxy).fire(new Receiver<Void>() {

		@Override
		public void onSuccess(Void response) {
			Log.info("RichTextArea Value edited succeessfully");
		//	Window.alert("Rich Text Value Set Successfully");
		}
	});*/
	
	}

	// Issue Role Module
	@Override
	public void pencliButtonclickEvent(RoleBaseItemProxy roleBaseItemProxy,
			ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
	// Issue Role Module

	@Override
	public void actualRoleClicked(StandardizedRoleProxy standardizedRoleProxy) {
		// TODO Auto-generated method stub
	//refresh all tabs
		
	
		//goTo(new RoleDetailsPlace(RoleEditActivity.roleTopic.stableId(), Operation.DETAILS));
		
		
		refreshSelectedTab(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()], standardizedRoleProxy);
		setEnableAllViews(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()], true);
/*requests.standardizedRoleRequest().findStandardizedRole(standardizedRoleProxy.getId()).fire(new Receiver<StandardizedRoleProxy>() {
			
			@Override
			public void onSuccess(StandardizedRoleProxy response) {
				// TODO Auto-generated method stub
				
*/			//	StandardizedRoleDetailsViewImpl homeRole;	
				/*StandardizedRoleProxy roleProxy=response.getPreviousVersion();*/
				/*if(roleProxy!=null)
				{*/
					
			//		System.out.println("Set value");
					//goTo(new RoleDetailsPlace(RoleEditActivity.roleTopic.stableId(),	Operation.DETAILS));
				//	goTo(new RoleDetailsPlace((standardizedRoleProxy.getPreviousVersion()).roleTopic.stableId(),Operation.DETAILS));
				//	goTo(new RoleDetailsPlace((standardizedRoleProxy.getPreviousVersion()).stableId(),Operation.DETAILS));
				//	homeRole=standardizedRoleDetailsView[roleDetailTabPanel.getTabBar().getSelectedTab()];
					
					//ScrolledTab Changes start
					//homeRole=standardizedRoleDetailsView[roleDetailTabPanel.getTabBar().getSelectedTab()];
	//				homeRole=standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()];
					//ScrolledTab Changes end
		//			homeRole.edit.setEnabled(true);
		//			homeRole.delete.setEnabled(true);
		//			homeRole.home.setVisible(false);
		//			homeRole.previous.setEnabled(true);
					
				/*	homeRole.setTitle(standardizedRoleProxy.getShortName() == null ? " " : String.valueOf(standardizedRoleProxy.getShortName()));
					homeRole.shortName.setInnerText(standardizedRoleProxy.getShortName() == null ? " " : String.valueOf(standardizedRoleProxy.getShortName()));
					homeRole.longName.setInnerText(standardizedRoleProxy.getLongName() == null ? " " : String.valueOf(standardizedRoleProxy.getLongName()));
					homeRole.roleType.setInnerText(standardizedRoleProxy.getRoleType().name()); //ADDED
					homeRole.studyYear.setInnerText(standardizedRoleProxy.getStudyYear().name()); //ADDED
				//	homeRole.labelLongNameHeader.setText(""+standardizedRoleProxy.getLongName());
					homeRole.labelLongNameHeader.setText("" + standardizedRoleProxy.getLongName()+ " ("+standardizedRoleProxy.getMainVersion()+"."+standardizedRoleProxy.getSubVersion()+")");
					homeRole.setValue(standardizedRoleProxy);
				*/
				/*}
				else
				{
					Window.alert("No More Record");
				}*/
				
			}
	/*	});*/

@Override
public void onDragStart(DragStartEvent event) {
	
	if(event.getSource() instanceof RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)
	{	
		FlowPanel flowPanel=((FlowPanel)((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)event.getSource()).getParent());
		srcOtionIndexOnDragStart=flowPanel.getWidgetIndex((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)event.getSource());
		
	}else if(event.getSource() instanceof RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl){
	
		VerticalPanel vpQ=((VerticalPanel)((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)event.getSource()).getParent());
		srcQuestionIndexOnDragStart=vpQ.getWidgetIndex((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)event.getSource());
		
	}else if (event.getSource() instanceof RoleDetailsChecklistSubViewChecklistTopicItemViewImpl ){
		
		VerticalPanel vp=((VerticalPanel)((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)event.getSource()).getParent());
		srcTopicIndexOnDragStart=vp.getWidgetIndex((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)event.getSource());
		
	}else if(event.getSource() instanceof RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl){
		
		FlowPanel flowPanel=((FlowPanel)((RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl)event.getSource()).getParent());
		srcCriteriaIndexOnDragStart=flowPanel.getWidgetIndex((RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl)event.getSource());
		
	}
		Log.info("in Drag Start");
	
		
}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		// TODO Auto-generated method stub
		Log.info("onPreviewDragEnd~~~~~~");
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
		// TODO Auto-generated method stub
		Log.info("onPreviewDragStart~~~~~~");
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		// TODO Auto-generated method stub
		
	
		
	
	
		
		if(event.getSource() instanceof RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)
			{
			Log.info("if called ~~~~~~");
			VerticalPanel vpQ=((VerticalPanel)((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)event.getSource()).getParent());
			
			int dstIndex=vpQ.getWidgetIndex((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)event.getSource());
			if(srcQuestionIndexOnDragStart==dstIndex)
				return;
			
			List<ChecklistQuestionProxy> questionIdList=new ArrayList<ChecklistQuestionProxy>();
			
			for(int i=0;i<vpQ.getWidgetCount();i++)
			{
				Log.info("value~~~~"+ i);
				
				//updateQueSequence(((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)vpQ.getWidget(i)).getProxy(),i,false,null,null);
				questionIdList.add(((RoleDetailsChecklistSubViewChecklistQuestionItemViewImpl)vpQ.getWidget(i)).getProxy());
			
				
			}
			requests.checklistQuestionRequestNonRoo().updateSequence(questionIdList).fire(new OSCEReceiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					Log.info("Option sequence updated success :" + response);
					
				}
			});
		
		
			}

		else if (event.getSource() instanceof RoleDetailsChecklistSubViewChecklistTopicItemViewImpl ){
			Log.info("else if called ~~~~");
			VerticalPanel vp=((VerticalPanel)((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)event.getSource()).getParent());
		
			int dstIndex=vp.getWidgetIndex((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)event.getSource());
			if(srcTopicIndexOnDragStart==dstIndex)
				return;
			
			List<ChecklistTopicProxy> topicIdList=new ArrayList<ChecklistTopicProxy>();
		for(int i=0;i<vp.getWidgetCount();i++)
		{
			
			Log.info("value~~~~"+ i);
			topicIdList.add(((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)vp.getWidget(i)).getProxy());
//			topicIdList.add(((RoleDetailsChecklistSubViewChecklistTopicItemViewImpl)vp.getWidget(i)).getProxy().getId());
		
		}
			requests.checklistTopicRequestNonRoo().updateSequence(topicIdList).fire(new OSCEReceiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					Log.info("Option sequence updated success :" + response);
					
				}
			});
		}
		
		else if(event.getSource() instanceof RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)
		{
			Log.info("else if called ~~~~");
			FlowPanel flowPanel=((FlowPanel)((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)event.getSource()).getParent());
			int dstIndex=flowPanel.getWidgetIndex((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)event.getSource());
			if(srcOtionIndexOnDragStart==dstIndex)
				return;
			List<ChecklistOptionProxy> optionIdList=new ArrayList<ChecklistOptionProxy>();
			for(int i=0;i<flowPanel.getWidgetCount();i++)
			{
		
				Log.info("value~~~~"+ i);
				Log.info("option name" +((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)flowPanel.getWidget(i)).getProxy().getOptionName());
				optionIdList.add(((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)flowPanel.getWidget(i)).getProxy());
		
			}
			requests.checklistOptionRequestNonRooo().updateSequence(optionIdList).fire(new OSCEReceiver<Boolean>() {

				@Override
				public void onSuccess(Boolean response) {
					Log.info("Option sequence updated success :" + response);
					
				}
			});
			
			/*int dstIndex=flowPanel.getWidgetIndex((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)event.getSource());
			int srcIndex=((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)event.getSource()).getProxy().getSequenceNumber().intValue();
			
			int dstId=((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)event.getSource()).getProxy().getId().intValue();
			int srcId=((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)flowPanel.getWidget((1+dstIndex))).getProxy().getId().intValue();
			
			Log.info("Dst Id :" + dstId + "dst Index: "  + dstIndex + "");
			Log.info("Src Id :" + srcId + "Src Index: "  + srcIndex + "");
			
			if(srcIndex == dstIndex)
				return;
			else
			{
				
			
				requests.checklistOptionRequestNonRooo().updateSequence(dstId, dstIndex, srcId, srcIndex).fire(new OSCEReceiver<Boolean>() {
	
					@Override
					public void onSuccess(Boolean response) {
						Log.info("Option sequence updated success :" + response);
						
					}
				});
			}*/
		/*for(int i=0;i<flowPanel.getWidgetCount();i++)
		{
			
			Log.info("value~~~~"+ i);
			Log.info("option name" +((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)flowPanel.getWidget(i)).getProxy().getOptionName());
			updateOptionSequence(((RoleDetailsChecklistSubViewChecklistOptionItemViewImpl)flowPanel.getWidget(i)).getProxy(),i);
		
		
		}*/
		}
		
		else if(event.getSource() instanceof RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl)
		{
			Log.info("else if called ~~~~");
			FlowPanel flowPanel=((FlowPanel)((RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl)event.getSource()).getParent());
		
			int dstIndex=flowPanel.getWidgetIndex((RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl)event.getSource());
			if(srcCriteriaIndexOnDragStart==dstIndex)
				return;
			List<ChecklistCriteriaProxy> criteriaIdList=new ArrayList<ChecklistCriteriaProxy>();
			
		for(int i=0;i<flowPanel.getWidgetCount();i++)
		{
			
			Log.info("value~~~~"+ i);
			//updateCriteriaSequence(((RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl)flowPanel.getWidget(i)).getProxy(),i);
			criteriaIdList.add(((RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl)flowPanel.getWidget(i)).getProxy());
		
		}
		requests.checklistCriteriaRequestNonRooo().updateSequence(criteriaIdList).fire(new OSCEReceiver<Boolean>() {

			@Override
			public void onSuccess(Boolean response) {
				Log.info("Option sequence updated success :" + response);
				
			}
		});
		}
		else{
			Log.info("no selected event");
		}
		
}



	private void updateQueSequence(ChecklistQuestionProxy proxy, int i,final boolean isLast,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView,final ChecklistTopicProxy topicProxy) {
		// TODO Auto-generated method stub
		ChecklistQuestionRequest request = requests.checklistQuestionRequest();
		proxy = request.edit(proxy);
		proxy.setSequenceNumber(i);
		request.persist().using(proxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				if(isLast)
				{
					//lets check in future if require
					//	roleTopicInit(topicProxy, topicView);
				}
				
			}
		});
	
		Log.info("que seque no saved successfully~~~~~"+ i);
	}
	
	
	private void updateOptionSequence(ChecklistOptionProxy proxy,int i)
	{
		ChecklistOptionRequest request=requests.checklistOptionRequest();
		
		proxy=request.edit(proxy);
		proxy.setSequenceNumber(i);
		request.persist().using(proxy).fire();
		Log.info("updateOptionSequence");
	}
	
	private void updateCriteriaSequence(ChecklistCriteriaProxy proxy,int i)
	{
		ChecklistCriteriaRequest request=requests.checklistCriteriaRequest();
		
		proxy=request.edit(proxy);
		proxy.setSequenceNumber(i);
		request.persist().using(proxy).fire();
		Log.info("updateCriteriaSequence");
	}
	
	private void updateSequence(ChecklistTopicProxy proxy, int i,final boolean isLast,final RoleDetailsChecklistSubViewChecklistTopicItemViewImpl topicView,final ChecklistTopicProxy topicProxy) {
		// TODO Auto-generated method stub
		ChecklistTopicRequest request = requests.checklistTopicRequest();
		proxy= request.edit(proxy);
		proxy.setSort_order(i);
		request.persist().using(proxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				if(isLast)
				{
					//lets check in future if require
				//	roleTopicInit(topicProxy, topicView);
				}
				
			}
		});
		
		Log.info("at update method::::");
	}

	//learning objective
	@Override
	public void minorDeleteClicked(final MinorSkillProxy minorSkill) {
		
		requests.minorSkillRequest().remove().using(minorSkill).fire(new Receiver<Void>() {
			public void onSuccess(Void ignore) {
				refreshMinorSkillData();
			}
		});
	}

	@Override
	public void majorDeleteClicked(final MainSkillProxy mainSkill) {

		requests.mainSkillRequest().remove().using(mainSkill).fire(new Receiver<Void>() {
			public void onSuccess(Void ignore) {
				refreshMainSkillData();
			}
		});	
	}

	@Override
	public void setMainClassiPopUpListBox(final RoleLearningPopUpView popupView) {	
		popupView.setDelegate(this);		
			
		requests.mainClassificationRequest().findAllMainClassifications().fire(new Receiver<List<MainClassificationProxy>>() {

			@Override
			public void onSuccess(List<MainClassificationProxy> response) {
				popupView.getMainClassiListBox().setAcceptableValues(response);
			}
		});
		
	
	}

	@Override
	public void mainClassiListBoxClicked(MainClassificationProxy proxy,
		final RoleLearningPopUpView popupView) {	
		requests.classificationTopicRequestNonRoo().findClassiTopicByMainClassi(proxy.getId()).fire(new OSCEReceiver<List<ClassificationTopicProxy>>() {

			@Override
			public void onSuccess(List<ClassificationTopicProxy> response) {
				popupView.getClassiTopicListBox().setAcceptableValues(response);
				classiTopicListBoxClicked(response.get(0), popupView);
			}
		});
	}

	@Override
	public void classiTopicListBoxClicked(ClassificationTopicProxy proxy,
			final RoleLearningPopUpView popupView) {
		requests.topicRequestNonRoo().findTopicByClassiTopic(proxy.getId()).fire(new OSCEReceiver<List<TopicProxy>>() {

			@Override
			public void onSuccess(List<TopicProxy> response) {
				popupView.getTopicListBox().setAcceptableValues(response);
			}
		});
		
	}

	/*@Override
	public void addMainSkillClicked(TopicProxy topicProxy, SkillLevelProxy skillLevelProxy) {
		
		Long skillLevelId;
		
		if (skillLevelProxy == null)
		{
			skillLevelId = 0l;
		}
		else
		{
			skillLevelId = skillLevelProxy.getId();
		}
			
		requests.skillRequestNonRoo().findSkillByTopicIDAndSkillLevelID(topicProxy.getId(), skillLevelId).fire(new OSCEReceiver<List<SkillProxy>>() {
			
			@Override
			public void onSuccess(List<SkillProxy> response) {
				if (response.size() == 0)
				{
					MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog("Incorrect Topic and Skill Level is Selected");
				}
				else
				{	
					MainSkillRequest mainSkillRequest = requests.mainSkillRequest();
					MainSkillProxy mainSkillProxy = mainSkillRequest.create(MainSkillProxy.class);
					
					mainSkillProxy.setRole(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue());
					mainSkillProxy.setSkill(response.get(0));
					
					mainSkillRequest.persist().using(mainSkillProxy).fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) 
						{
							refreshMainSkillData();
							Log.info("Record Inserted Successfully");
						}
						
					});
				}
			}
		});
		
	}

	@Override
	public void addMinorSkillClicked(TopicProxy topicProxy, SkillLevelProxy skillLevelProxy) {
		
		Long skillLevelId;
		
		if (skillLevelProxy == null)
		{
			skillLevelId = 0l;
		}
		else
		{
			skillLevelId = skillLevelProxy.getId();
		}
			
		requests.skillRequestNonRoo().findSkillByTopicIDAndSkillLevelID(topicProxy.getId(), skillLevelId).fire(new OSCEReceiver<List<SkillProxy>>() {
			
			@Override
			public void onSuccess(List<SkillProxy> response) {
				if (response.size() == 0)
				{
					MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
					dialogBox.showConfirmationDialog("Incorrect Topic and Skill Level is Selected");
				}
				else
				{	
					MinorSkillRequest minorSkillRequest = requests.minorSkillRequest();
					MinorSkillProxy mainSkillProxy = minorSkillRequest.create(MinorSkillProxy.class);
					
					mainSkillProxy.setRole(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue());
					mainSkillProxy.setSkill(response.get(0));
					
					minorSkillRequest.persist().using(mainSkillProxy).fire(new OSCEReceiver<Void>() {

						@Override
						public void onSuccess(Void response) 
						{
							refreshMinorSkillData();
							Log.info("Record Inserted Successfully");
						}
						
					});
				}
			}
		});		
	}*/

	@Override
	public void setSkillLevelPopupListBox(final RoleLearningPopUpView popupView) {
		
		popupView.setDelegate(this);		
		requests.skillLevelRequest().findAllSkillLevels().fire(new OSCEReceiver<List<SkillLevelProxy>>() {
			@Override
			public void onSuccess(List<SkillLevelProxy> response) {
						popupView.getLevelListBox().setAcceptableValues(response);
				
			}
		});
	}	
	
	public void refreshMainSkillData()
	{
		/*requests.mainSkillRequestNonRoo().findMainSkillEntriesByRoleID(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MainSkillProxy>>() {

			@Override
			public void onSuccess(List<MainSkillProxy> response) {
				standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().majorTable.setRowData(response);
				standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());
			}
		});*/
		//main skill change
		
				requests.mainSkillRequestNonRoo().countMainSkillEntriesByRoleID(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<Long>() {

					@Override
					public void onSuccess(Long response) {								
						/*standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().majorTable.setRowCount(response.intValue(),true);
						Log.info("~~DATA SET");
					}
				});
				final Range mainSkillRange = standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().majorTable.getVisibleRange();
				requests.mainSkillRequestNonRoo().findMainSkillEntriesByRoleID(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId(),mainSkillRange.getStart(),mainSkillRange.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MainSkillProxy>>() {

					@Override
					public void onSuccess(List<MainSkillProxy> response) {								
					/*	standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
						standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
						standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().majorTable.setRowData(mainSkillRange.getStart(),response);
						Log.info("~~DATA SET");
					}
				});
							
				//standardizedRoleDetailsView[selectedTabId].getRoleKeywordSubViewImpl().keywordTable.setRowData(keywordRange.getStart(),response);
				//main skill change end

	}
	
	public void refreshMinorSkillData()
	{
		//minor changes changes
/*		requests.minorSkillRequestNonRoo().findMinorSkillEntriesByRoleID(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MinorSkillProxy>>() {

			@Override
			public void onSuccess(List<MinorSkillProxy> response) {
				standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().minorTable.setRowData(response);
				standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().minorTable.setRowCount(response.size());
			}
		});
		*/
		requests.minorSkillRequestNonRoo().countMinorSkillEntriesByRoleID(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<Long>() {

			@Override
			public void onSuccess(Long response) {								
				/*standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
				standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().minorTable.setRowCount(response.intValue(),true);
				Log.info("~~DATA SET");
			}
		});
		final Range minorSkillRange = standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().minorTable.getVisibleRange();
		requests.minorSkillRequestNonRoo().findMinorSkillEntriesByRoleID(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId(),minorSkillRange.getStart(),minorSkillRange.getLength()).with("skill","skill.topic","skill.skillLevel","skill.topic.classificationTopic", "skill.topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<MinorSkillProxy>>() {

			@Override
			public void onSuccess(List<MinorSkillProxy> response) {								
			/*	standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowData(response);
				standardizedRoleDetailsView[innerindex2].getRoleLearningSubViewImpl().majorTable.setRowCount(response.size());*/
				standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().minorTable.setRowData(minorSkillRange.getStart(),response);
				Log.info("~~DATA SET");
			}
		});
				
		//minor chanes end

	}
	
	@Override
	public void addPorfessionClicked(IconButton parentButton) {
		requests.professionRequest().findAllProfessions().fire(new OSCEReceiver<List<ProfessionProxy>>() {

			@Override
			public void onSuccess(List<ProfessionProxy> response) {
				if (professionPopup == null) {
					return;
				}
				List<ProfessionProxy> values = new ArrayList<ProfessionProxy>();
				values.addAll(response);
				if (values.size() > 0 ) {
					professionPopup.getProfessionBox().setValue(values.get(0));
				}
				professionPopup.getProfessionBox().setAcceptableValues(values);
			}
		});

		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == professionPopup) {
				return;
			}
		}
		professionPopup = new StandardizedPatientAdvancedSearchProfessionPopupImpl();
		professionPopup.setDelegate(this);

		//SPEC Change
		
//		professionPopup.display(parentButton);
		int x = parentButton.getAbsoluteLeft() - 335;
		int y = parentButton.getAbsoluteTop() - parentButton.getOffsetHeight() - 15;
		professionPopup.display(parentButton);
		
		//SPEC Change
		
		advancedSearchPopup = professionPopup;
		
	}

	@Override
	public void addWorkPermissionClicked(IconButton parentButton) {
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == workPermissionPopup) {
				return;
			}
		}
		workPermissionPopup = new StandardizedPatientAdvancedSearchWorkPermissionPopupImpl();
		workPermissionPopup.setDelegate(this);
		//workPermissionPopup.display(parentButton);
		int x = parentButton.getAbsoluteLeft() - 335;
		int y = parentButton.getAbsoluteTop() - parentButton.getOffsetHeight() - 15;
		workPermissionPopup.display(parentButton);
		advancedSearchPopup = workPermissionPopup;		
	}

	@Override
	public void addMaritialStatusClicked(IconButton parentButton) {
		if (advancedSearchPopup != null && advancedSearchPopup.isShowing()) {
			advancedSearchPopup.hide();
			if (advancedSearchPopup == maritialStausPopup) {
				return;
			}
		}
		maritialStausPopup = new StandardizedPatientAdvancedSearchMaritialStatusPopupViewImpl();
		maritialStausPopup.setDelegate(this);
		
		int x = parentButton.getAbsoluteLeft() - 335;
		int y = parentButton.getAbsoluteTop() - parentButton.getOffsetHeight() - 15;
		maritialStausPopup.display(parentButton);
		//maritialStausPopup.display(parentButton);
		advancedSearchPopup = maritialStausPopup;		
	}

	@Override
	public void addProfessionButtonClicked(ProfessionProxy profession,
			BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.PROFESSION).render(comparison) + " " 
				+ profession.getProfession();
		addAdvSeaBasicButtonClicked(profession.getId(), profession.getProfession(), displayValue, bindType, PossibleFields.PROFESSION, comparison);
	}

	@Override
	public void addWokPermissionButtonClicked(WorkPermission workpermission,
			BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.WORKPERMISSION).render(comparison) + " " + workpermission + " WorkPermission";
		addAdvSeaBasicButtonClicked(null, workpermission.toString(), displayValue, bindType, PossibleFields.WORKPERMISSION, comparison);
	}

	@Override
	public void addMaritialStatusButtonClicked(MaritalStatus maritialStatus,
			BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.MARITIALSTATUS).render(comparison) 
				+ " " + new EnumRenderer<MaritalStatus>().render(maritialStatus); 
		addAdvSeaBasicButtonClicked(null, maritialStatus.toString(), displayValue, bindType, PossibleFields.MARITIALSTATUS, comparison);
	}
	
	@Override
	public void addGenderButtonClicked(Gender gender, BindType bindType, Comparison comparison) {
		String displayValue = new EnumRenderer<Comparison>(EnumRenderer.Type.MARITIALSTATUS).render(comparison) 
				+ " " + new EnumRenderer<Gender>().render(gender); 
		addAdvSeaBasicButtonClicked(null, gender.toString(), displayValue, bindType, PossibleFields.GENDER, comparison);
	}

	@Override
	public void updateOption(final String topic, final String description, String optionDesc,
			final RoleDetailsChecklistSubViewChecklistOptionItemViewImpl optionView) {
		Log.info("saveOption");
		//ScrolledTab Changes start
		//final int selectedtab=view.getRoleDetailTabPanel().getTabBar().getSelectedTab();//gets selected tab of standardized role
		final int selectedtab=view.getRoleDetailTabPanel().getSelectedIndex();//gets selected tab of standardized role
		//ScrolledTab Changes end
		ChecklistOptionRequest request=requests.checklistOptionRequest();
		
		ChecklistOptionProxy proxy=optionView.getProxy();
		proxy = request.edit(proxy);
		proxy.setOptionName(topic);
		proxy.setChecklistQuestion(optionView.getProxy().getChecklistQuestion());
		proxy.setValue(description);
		proxy.setInstruction(optionDesc);
		proxy.setCriteriaCount(new Integer(optionView.optionPopup.getCriteriaCountLstBox().getValue(optionView.optionPopup.getCriteriaCountLstBox().getSelectedIndex())));
		// Highlight onViolation
		request.persist().using(proxy).fire(new OSCEReceiver<Void>(optionView.getChecklistOptionMap()) 
		{
		// E Highlight onViolation
			@Override
			public void onSuccess(Void response) {
				Log.info("Option Saved Successfully");	
				// E Highlight onViolation
				//createOptionView(selectedtab,proxy,questionView);
				//optionView.getOptionLbl().setText(topic);
				optionView.getOptionLbl().setText(util.getFormatedString(topic, 10));
				//change for design
				optionView.getOptionLbl().setTitle(topic);
				if(description!=null)
				{
				/*optionView.getOptionLbl().setText(optionView.getOptionLbl().getText() +"("+ description +")");
				optionView.getOptionLbl().setTitle(optionView.getOptionLbl().getText() );*/
					optionView.getOptionLbl().setText(util.getFormatedString(topic +"("+ description +")",10));
					optionView.getOptionLbl().setTitle(topic +"("+ description +")");
				}
				//optionView.getOptionValueLbl().setText(description);
			}
		});
		
		optionView.setProxy(proxy);
	}

	@Override
	public void updateCriteria(final String criteria, final RoleDetailsChecklistSubViewChecklistCriteriaItemViewImpl criteriaView) {
		Log.info("updateCriteria");
		
		ChecklistCriteriaRequest request = requests.checklistCriteriaRequest();		
		ChecklistCriteriaProxy proxy=criteriaView.getProxy();
		
		proxy = request.edit(proxy);
		proxy.setCriteria(criteria);
		// Highlight onViolation
		request.persist().using(proxy).fire(new OSCEReceiver<Void>() {
		// E Highlight onViolation
			@Override
			public void onSuccess(Void response) {
				Log.info("Criteria Saved Successfully");
				//criteriaView.getCriteriaLbl().setText(criteria);
				//change for design
				criteriaView.getCriteriaLbl().setText(util.getFormatedString(criteria,10));
				criteriaView.getCriteriaLbl().setTitle(criteria);
			}
		});
		criteriaView.setProxy(proxy);
	}
	
	//learning

		@Override
		public void loadLearningObjectiveData() {
			
			learningObjectiveView = standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getRoleLearningSubViewImpl().getLearningObjectiveViewImpl();
			
			 
			//RecordChangeEvent.register(requests.getEventBus(), (LearningObjectiveViewImpl) learningObjectiveView);
			
			learningObjectiveView.setDelegate(this);		
			
			final int start =0; //learningObjectiveView.getTable().getVisibleRange().getStart();
			final int length =15; //learningObjectiveView.getTable().getVisibleRange().getLength();
			final int rowCount = 0;
			
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			
			requests.skillRequestNonRoo().countSkillBySearchCriteria(mainClassificationId, classificaitonTopicId, topicId, skillLevelId, applianceId).fire(new OSCEReceiver<Integer>() {
				@Override
				public void onSuccess(Integer response) {
					loadRange();
					System.out.println("~~~~RESPONSE : " + response);
					learningObjectiveView.getTable().setRowCount(response, true);
					
					requests.skillRequestNonRoo().findSkillBySearchCriteria(start, length, mainClassificationId, classificaitonTopicId, topicId, skillLevelId, applianceId).with("topic", "skillLevel", "skillHasAppliances", "skillHasAppliances.appliance", "topic.classificationTopic", "topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<SkillProxy>>() {

						@Override
						public void onSuccess(List<SkillProxy> response) {
							
							fillMainClassificationSuggestBox();
							
							fillClassificationTopicSuggestBox(mainClassificationId);
							
							fillTopicSuggestBox(classificaitonTopicId);
							
							fillSkillLevelSuggestBox();
							
							fillApplianceSuggestBox();				
						
							for (int i=0; i<response.size(); i++)
							{
								learningObjective = new LearningObjectiveData();
								SkillProxy skill = response.get(i);
								
								temp = skill.getTopic().getClassificationTopic().getMainClassification().getShortcut() + " " + skill.getTopic().getClassificationTopic().getShortcut() + " " + skill.getShortcut();
								learningObjective.setCode(temp);
								learningObjective.setSkill(skill);
								learningObjective.setText(skill.getDescription());
								learningObjective.setTopic(skill.getTopic().getTopicDesc());	
								
								if (skill.getSkillLevel() != null)
									learningObjective.setSkillLevel(String.valueOf(skill.getSkillLevel().getLevelNumber()));
								else
									learningObjective.setSkillLevel("");
								
								Iterator<SkillHasApplianceProxy> iter = skill.getSkillHasAppliances().iterator();
								
								while (iter.hasNext())
								{
									SkillHasApplianceProxy skillHasApplianceProxy = iter.next();
									
									if (skillHasApplianceProxy.getAppliance().getShortcut().equals("D"))
										learningObjective.setD("D");
									else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("T"))
										learningObjective.setT("T");
									else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("E"))
										learningObjective.setE("E");
									else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("P"))
										learningObjective.setP("P");
									else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("G"))
										learningObjective.setG("G");
								}
								
								learningObjectiveData.add(learningObjective);
								learningObjective = null;
							}
						
							learningObjectiveView.getTable().setRowData(learningObjectiveView.getTable().getVisibleRange().getStart(), learningObjectiveData);
							//learningObjectiveView.getTable().setVisibleRange(start, length);
							
							requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
						}
					});
					
				}
			});
			
			
			
			/*requests.osceRequestNonRoo().findAllOsceSemester(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId()).fire(new OSCEReceiver<List<OsceProxy>>() {

				@Override
				public void onSuccess(List<OsceProxy> response) {
					// TODO Auto-generated method stub
					
				}
			});*/
			
			
			
			
		}
		
		public void loadRange()
		{
			learningObjectiveView.getTable().addRangeChangeHandler(new RangeChangeEvent.Handler(){

				@Override
				public void onRangeChange(RangeChangeEvent event) {
					System.out.println("**IN RANGE**");
					onLearningObjectiveRangeChanged();
				}
				
			});	
		}
		
		public void fillMainClassificationSuggestBox()
		{
			requests.mainClassificationRequest().findAllMainClassifications().fire(new OSCEReceiver<List<MainClassificationProxy>>() {

				@Override
				public void onSuccess(List<MainClassificationProxy> response) {
					DefaultSuggestOracle<MainClassificationProxy> suggestOracle1 = (DefaultSuggestOracle<MainClassificationProxy>) learningObjectiveView.getMainClassificationSuggestBox().getSuggestOracle();
					suggestOracle1.setPossiblilities(response);
					learningObjectiveView.getMainClassificationSuggestBox().setSuggestOracle(suggestOracle1);
					
					learningObjectiveView.getMainClassificationSuggestBox().setRenderer(new AbstractRenderer<MainClassificationProxy>() {

						@Override
						public String render(MainClassificationProxy object) {
							// TODO Auto-generated method stub
							if (object != null)
								return (object.getDescription() +  "[" + object.getShortcut() + "]");
							else
								return "";
						}
					});
					
					
				}
			});
		}
		
		public void fillClassificationTopicSuggestBox(Long mainClassiId)
		{
			
			requests.classificationTopicRequestNonRoo().findClassiTopicByMainClassi(mainClassiId).fire(new OSCEReceiver<List<ClassificationTopicProxy>>() {

				@Override
				public void onSuccess(List<ClassificationTopicProxy> response) {
					DefaultSuggestOracle<ClassificationTopicProxy> suggestOracle = (DefaultSuggestOracle<ClassificationTopicProxy>) learningObjectiveView.getClassificationTopicSuggestBox().getSuggestOracle();
					suggestOracle.setPossiblilities(response);
					learningObjectiveView.getClassificationTopicSuggestBox().setSuggestOracle(suggestOracle);
					
					learningObjectiveView.getClassificationTopicSuggestBox().setRenderer(new AbstractRenderer<ClassificationTopicProxy>() {

						@Override
						public String render(ClassificationTopicProxy object) {
							if (object != null)
								return (object.getDescription() + "[" + object.getShortcut() + "]");
							else
								return "";
						}
					});
					
				}
			});
		}
		
		public void fillTopicSuggestBox(Long classiTopicId)
		{
			requests.topicRequestNonRoo().findTopicByClassiTopic(classiTopicId).fire(new OSCEReceiver<List<TopicProxy>>() {

				@Override
				public void onSuccess(List<TopicProxy> response) {
									
					DefaultSuggestOracle<TopicProxy> suggestOracle = (DefaultSuggestOracle<TopicProxy>) learningObjectiveView.getTopicSuggestBox().getSuggestOracle();
					suggestOracle.setPossiblilities(response);
					learningObjectiveView.getTopicSuggestBox().setSuggestOracle(suggestOracle);
					
					learningObjectiveView.getTopicSuggestBox().setRenderer(new AbstractRenderer<TopicProxy>() {

						@Override
						public String render(TopicProxy object) {
							if (object != null)
								return object.getTopicDesc();
							else
								return "";
						}
					});
				}
			});
		}
		
		public void fillSkillLevelSuggestBox()
		{
			requests.skillLevelRequest().findAllSkillLevels().fire(new OSCEReceiver<List<SkillLevelProxy>>() {

				@Override
				public void onSuccess(List<SkillLevelProxy> response) {
					DefaultSuggestOracle<SkillLevelProxy> suggestOracle = (DefaultSuggestOracle<SkillLevelProxy>) learningObjectiveView.getSkillLevelSuggestBox().getSuggestOracle();
					suggestOracle.setPossiblilities(response);
					learningObjectiveView.getSkillLevelSuggestBox().setSuggestOracle(suggestOracle);
					
					learningObjectiveView.getSkillLevelSuggestBox().setRenderer(new AbstractRenderer<SkillLevelProxy>() {

						@Override
						public String render(SkillLevelProxy object) {
							if (object != null)
								return String.valueOf(object.getLevelNumber());
							else
								return "";
						}
					});
				}
			});
		}

		public void fillApplianceSuggestBox()
		{
			requests.applianceRequest().findAllAppliances().fire(new OSCEReceiver<List<ApplianceProxy>>() {

				@Override
				public void onSuccess(List<ApplianceProxy> response) {
					DefaultSuggestOracle<ApplianceProxy> suggestOracle = (DefaultSuggestOracle<ApplianceProxy>) learningObjectiveView.getApplianceSuggestBox().getSuggestOracle();
					suggestOracle.setPossiblilities(response);
					learningObjectiveView.getApplianceSuggestBox().setSuggestOracle(suggestOracle);
					
					learningObjectiveView.getApplianceSuggestBox().setRenderer(new AbstractRenderer<ApplianceProxy>() {

						@Override
						public String render(ApplianceProxy object) {
							if (object != null)
								return object.getShortcut();
							else
								return "";
						}
					});			
				}
			});
		}
		
		protected void onLearningObjectiveRangeChanged() {
			
			System.out.println("~~~in onLearningObjectiveRangeChanged");
			
			learningObjectiveData.clear();		
			final Range range = learningObjectiveView.getTable().getVisibleRange();
			
			System.out.println("Start : " + range.getStart());
			System.out.println("Length : " + range.getLength());
			
			requests.skillRequestNonRoo().countSkillBySearchCriteria(mainClassificationId, classificaitonTopicId, topicId, skillLevelId, applianceId).fire(new OSCEReceiver<Integer>() {
				@Override
				public void onSuccess(Integer response) {
					learningObjectiveView.getTable().setRowCount(response, true);
					
					requests.skillRequestNonRoo().findSkillBySearchCriteria(range.getStart(), range.getLength(), mainClassificationId, classificaitonTopicId, topicId, skillLevelId, applianceId).with("topic", "skillLevel", "skillHasAppliances", "skillHasAppliances.appliance", "topic.classificationTopic", "topic.classificationTopic.mainClassification").fire(new OSCEReceiver<List<SkillProxy>>() {

						@Override
						public void onSuccess(List<SkillProxy> response) {
						
							for (int i=0; i<response.size(); i++)
							{
								learningObjective = new LearningObjectiveData();
								SkillProxy skill = response.get(i);
								
								temp = skill.getTopic().getClassificationTopic().getMainClassification().getShortcut() + " " + skill.getTopic().getClassificationTopic().getShortcut() + " " + skill.getShortcut();
								learningObjective.setCode(temp);
								learningObjective.setSkill(skill);
								learningObjective.setText(skill.getDescription());
								learningObjective.setTopic(skill.getTopic().getTopicDesc());	
								
								if (skill.getSkillLevel() != null)
									learningObjective.setSkillLevel(String.valueOf(skill.getSkillLevel().getLevelNumber()));
								else
									learningObjective.setSkillLevel("");
								
								Iterator<SkillHasApplianceProxy> iter = skill.getSkillHasAppliances().iterator();
								
								while (iter.hasNext())
								{
									SkillHasApplianceProxy skillHasApplianceProxy = iter.next();
									
									if (skillHasApplianceProxy.getAppliance().getShortcut().equals("D"))
										learningObjective.setD("D");
									else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("T"))
										learningObjective.setT("T");
									else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("E"))
										learningObjective.setE("E");
									else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("P"))
										learningObjective.setP("P");
									else if (skillHasApplianceProxy.getAppliance().getShortcut().equals("G"))
										learningObjective.setG("G");
								}
								
								learningObjectiveData.add(learningObjective);
							}
							
							learningObjectiveView.getTable().setRowData(range.getStart(), learningObjectiveData);
						}
					});
				}
			});
			
			
				
		}

		@Override
		public void mainClassificationSuggestboxChanged(Long value) {
			mainClassificationId = value;
			
			learningObjectiveView.getClassificationTopicSuggestBox().setSelected(null);
			classificaitonTopicId = null;
			
			fillClassificationTopicSuggestBox(mainClassificationId);
			onLearningObjectiveRangeChanged();
		}

		@Override
		public void classificationTopicSuggestboxChanged(Long value) {
			classificaitonTopicId = value;		
			
			learningObjectiveView.getTopicSuggestBox().setSelected(null);
			topicId = null;
			
			fillTopicSuggestBox(classificaitonTopicId);
			onLearningObjectiveRangeChanged();
		}

		@Override
		public void topicSuggestboxChanged(Long value) {
			topicId = value;
			onLearningObjectiveRangeChanged();
		}

		@Override
		public void skillLevelSuggestboxChanged(Long value) {
			skillLevelId = value;
			onLearningObjectiveRangeChanged();
		}

		@Override
		public void applianceSuggestboxChanged(Long value) {
			applianceId = value;
			onLearningObjectiveRangeChanged();
		}

		@Override
		public void addLearningObjectiveTableRangeHandler() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addMainClicked() {
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			
			Iterator<LearningObjectiveData> itr = learningObjectiveView.getMultiselectionModel().getSelectedSet().iterator();
			
			// SPEC Change
			
			if(itr.hasNext()){
			while (itr.hasNext())
			{
				LearningObjectiveData learningObjectiveData = itr.next();
				
				MainSkillRequest mainSkillRequest = requests.mainSkillRequest();
				MainSkillProxy mainSkillProxy = mainSkillRequest.create(MainSkillProxy.class);
				
				mainSkillProxy.setRole(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue());
				mainSkillProxy.setSkill(learningObjectiveData.getSkill());
				
				mainSkillRequest.persist().using(mainSkillProxy).fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void response) 
					{				
						Log.info("Record Inserted Successfully");
					}
					
				});
			}
			
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			
			MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.success());
			dialogBox.showConfirmationDialog(constants.confirmationMajorSkillAdded());		
		}else{
			
			MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
			dialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
			
		}

			// SPEC Change
			
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));		
		}

		@Override
		public void addMinorClicked() {
			
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(true));
			
			Iterator<LearningObjectiveData> itr = learningObjectiveView.getMultiselectionModel().getSelectedSet().iterator();
			
			//SPEC Change
			
			if(itr.hasNext()){
				
			while (itr.hasNext())
			{
				LearningObjectiveData learningObjectiveData = itr.next();
				
				MinorSkillRequest minorSkillRequest = requests.minorSkillRequest();
				MinorSkillProxy mainSkillProxy = minorSkillRequest.create(MinorSkillProxy.class);
			
				mainSkillProxy.setRole(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue());
				mainSkillProxy.setSkill(learningObjectiveData.getSkill());
			
				minorSkillRequest.persist().using(mainSkillProxy).fire(new OSCEReceiver<Void>() {

					@Override
					public void onSuccess(Void response) 
					{
						
						Log.info("Record Inserted Successfully");
					}
				
				});
			}
		
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			
			MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.success());
			dialogBox.showConfirmationDialog(constants.confirmationMinorSkillAdded());	
			}else{
				
				MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.warning());
				dialogBox.showConfirmationDialog(constants.warningFillRequiredFields());
				
			}

			//SPEC Change
			
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(false));
			
		}

		@Override
		public void closeButtonClicked() {
			refreshMainSkillData();
			refreshMinorSkillData();
			
			mainClassificationId = null;
			classificaitonTopicId = null;
			topicId = null;
			skillLevelId = null;
			applianceId = null;
			learningObjectiveView = null;
		}
	
		public void refreshSelectedTab()
		{
			refreshSelectedTab(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()], standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue());
		}
	
		@Override
		public void clearAllButtonClicked() {
			loadLearningObjectiveData();
		}
		
		
		
		public void showApplicationLoading(Boolean show) {
			requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(show));

		}
		
		public void downloadFile(final String path)
		{
			Log.info(" downloadFile  :" );
			final String url= GWT.getHostPageBaseURL() + "downloadRoleFile?path="+path;
			
		   Window.open(url, path, "enabled");
						 

			
		}
	
		@Override
		public void changeDasteValueForOsceSemesterCall() {
			// TODO Auto-generated method stub
			
			requests.osceRequestNonRoo().findAllOsceSemester(standardizedRoleDetailsView[roleDetailTabPanel.getSelectedIndex()].getValue().getId(),roleOsceSemesterSubView.getStartDate().getValue() , roleOsceSemesterSubView.getEndDate().getValue()).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() {

				@Override
				public void onSuccess(List<OsceProxy> response) {
					// TODO Auto-generated method stub
					if(response!=null)
					{
						
					
					roleOsceSemesterSubView.getOsceSemesterTable().setRowCount(response.size());
					roleOsceSemesterSubView.getOsceSemesterTable().setRowData(response);
					}
					else
					{
						Log.info("record not found");
					}
					
				}
			});
			
			
		}

		//spec change[
		/*@Override
		public void clearSearchCriteriaButtonClicked() {
			mainClassificationId = null;	
			classificaitonTopicId = null;
			topicId = null;
			skillLevelId = null;
			applianceId = null;
			
			learningObjectiveView.getMainClassificationSuggestBox().setSelected(null);
			learningObjectiveView.getClassificationTopicSuggestBox().setSelected(null);
			learningObjectiveView.getTopicSuggestBox().setSelected(null);
			learningObjectiveView.getSkillLevelSuggestBox().setSelected(null);
			learningObjectiveView.getApplianceSuggestBox().setSelected(null);
			
			onLearningObjectiveRangeChanged();
		}*/
		//spec change]	

		//checklist change
		@Override
		public void exportChecklistClicked(
				StandardizedRoleProxy standardizedRoleProxy) {
			showApplicationLoading(true);
			
			requests.standardizedRoleRequestNonRoo().exportChecklistByStandardizedRole(standardizedRoleProxy.getId()).fire(new OSCEReceiver<String>() {

				@Override
				public void onSuccess(String response) {
					showApplicationLoading(false);
				
					String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.CHECKLIST.ordinal()));          
					String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal)
							.concat("&").concat(ResourceDownloadProps.NAME).concat("=").concat(URL.encodeQueryString(response));
					Log.info("--> url is : " +url);
					Window.open(url, "", "");
					//Window.open(response, "_blank", "enabled");
				}
			});
			
		}
		//checklist change
}

	