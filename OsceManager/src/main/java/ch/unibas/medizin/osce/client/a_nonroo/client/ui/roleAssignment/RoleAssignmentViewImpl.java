package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleSelectedHandler;
import ch.unibas.medizin.osce.client.managed.request.AdvancedSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.RowCountChangeEvent;

public class RoleAssignmentViewImpl extends Composite implements RoleAssignmentView, RoleSelectedHandler, MenuClickHandler, ClickHandler {

	private static RoleAssignmentViewImplUiBinder uiBinder = GWT.create(RoleAssignmentViewImplUiBinder.class);

	interface RoleAssignmentViewImplUiBinder extends UiBinder<Widget, RoleAssignmentViewImpl> {
	}

	// private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;

	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	@UiField
	public SimplePanel detailsPanel;

	// Module 3 {

	@UiField
	VerticalPanel osceDaySubViewContainerPanel;

	@Override
	public VerticalPanel getOsceDaySubViewContainerPanel() {
		return this.osceDaySubViewContainerPanel;
	}

	// Module 3 }

	@UiField
	PatientInSemesterFlexTable table;

	@UiField(provided = true)
	CellTable<AdvancedSearchCriteriaProxy> advancedSearchCriteriaTable;

	@UiField(provided = true)
	SimplePager pager;

	@UiField
	Button surveyImpBtn;
	@UiField
	Button autoAssignmentBtn;
	@UiField
	Button addManuallyBtn;

	private Presenter presenter;

	private List<PatientInSemesterData> patientInSemesterDataList;
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private String[] headers;

	public RoleAssignmentViewImpl() {
		advancedSearchCriteriaTable = new CellTable<AdvancedSearchCriteriaProxy>(
				OsMaConstant.TABLE_PAGE_SIZE,
				(CellTable.Resources) GWT.create(MyCellTableResources.class));
		pager = new SimplePager(SimplePager.TextLocation.RIGHT,
				(SimplePager.Resources) GWT
						.create(MySimplePagerResources.class), true,
				OsMaConstant.TABLE_JUMP_SIZE, true);

		initWidget(uiBinder.createAndBindUi(this));

		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0),
				OsMaConstant.SPLIT_PANEL_MINWIDTH);
		surveyImpBtn.setText(constants.roleSurveyImport());
		autoAssignmentBtn.setText(constants.roleAutoAssign());
		addManuallyBtn.setText(constants.roleAddManually());
		headers = new String[] { constants.name(), constants.roleAccepted(),
				constants.roleAssignTo(), "", "" };

		init();

	}

	@UiHandler("surveyImpBtn")
	public void onSurveyImpBtnClicked(ClickEvent event) {
		// TODO : import patient detail logic
		delegate.surveyImpBtnClicked();
	}

	@UiHandler("autoAssignmentBtn")
	public void onAutoAssignmentBtnClicked(ClickEvent event) {
		// TODO : Auto assignment detail logic
		// Module 3 f {
				delegate.autoAssignmentBtnClicked();
				// Module 3 f }

	}

	@UiHandler("addManuallyBtn")
	public void onAddManuallyBtnClicked(ClickEvent event) {

		delegate.onAddManuallyClicked();
	}

	public void init() {

		int left = ResolutionSettings.getSplitLayoutPanelLeft();
		
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style","position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
		
		table.addClickHandler(this);

//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);

		initAdvancedCriteria();
	}

	public void initAdvancedCriteria() {
		advancedSearchCriteriaTable.addColumn(new Column<AdvancedSearchCriteriaProxy, SafeHtml>(new SafeHtmlCell()) {
					@Override
			public SafeHtml getValue(AdvancedSearchCriteriaProxy criterion) {
						switch (criterion.getField()) {
						case NATIONALITY:
							return OsMaConstant.FLAG_ICON;
						case LANGUAGE:
							return OsMaConstant.COMMENT_ICON;
						case ANAMNESIS:
							return OsMaConstant.EDIT_ICON;
						case SCAR:
							return OsMaConstant.SEARCH_ICON;
						default:
							return OsMaConstant.WRENCH_ICON;
						}
					}
				});

		advancedSearchCriteriaTable.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {
					Renderer<BindType> renderer = new EnumRenderer<BindType>();

					@Override
					public String getValue(AdvancedSearchCriteriaProxy object) {
						return renderer.render(object.getBindType());
					}
				}, constants.bindType());

		advancedSearchCriteriaTable.addColumn(new TextColumn<AdvancedSearchCriteriaProxy>() {

					public String getValue(AdvancedSearchCriteriaProxy criterion) {
						return criterion.getShownValue();
					}
				}, constants.criterion());

		advancedSearchCriteriaTable.addColumn(new IdentityColumn<AdvancedSearchCriteriaProxy>(new ActionCell<AdvancedSearchCriteriaProxy>(OsMaConstant.CHECK_ICON, new ActionCell.Delegate<AdvancedSearchCriteriaProxy>() {
									@Override
			public void execute(AdvancedSearchCriteriaProxy object) {
										// delegate.deleteAdvancedSearchCriteria(object);
									}

								}) {
							@Override
			public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element elem, AdvancedSearchCriteriaProxy advancedSearchCriteriaProxy, NativeEvent nativeEvent, ValueUpdater<AdvancedSearchCriteriaProxy> valueUpdater) {
								if (nativeEvent.getButton() == NativeEvent.BUTTON_LEFT) {
					elem.setInnerHTML(delegate.onAdvancedSearchCriteriaClicked(advancedSearchCriteriaProxy));
								}
				super.onBrowserEvent(context, elem, advancedSearchCriteriaProxy, nativeEvent, valueUpdater);

							}
						}));
		advancedSearchCriteriaTable.addColumnStyleName(advancedSearchCriteriaTable.getColumnCount() - 1, "iconCol");

		advancedSearchCriteriaTable.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {

					@Override
					public void onRowCountChange(RowCountChangeEvent event) {
						if (event.getNewRowCount() > 0) {
							advancedSearchCriteriaTable.setVisible(true);
							pager.setVisible(true);
						} else {
							advancedSearchCriteriaTable.setVisible(false);
							pager.setVisible(false);
						}
					}
				});

		advancedSearchCriteriaTable.setVisible(false);
		pager.setVisible(false);
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setData(List<PatientInSemesterData> patientInSemesterDataList) {

		table.setSource(patientInSemesterDataList, getHeaderRow());

	}

	public List<PatientInSemesterData> getPatientInSemesterData() {
		return patientInSemesterDataList;
	}

	private String[] getHeaderRow() {
		return headers;
	}

	@Override
	public Button getAddManuallyBtn() {
		return addManuallyBtn;
	}

	@Override
	public CellTable<AdvancedSearchCriteriaProxy> getAdvancedSearchCriteriaTable() {
		return advancedSearchCriteriaTable;
	}

	@Override
	public void setAdvancedSearchCriteriaTable(CellTable<AdvancedSearchCriteriaProxy> advancedSearchCriteriaTable) {
		this.advancedSearchCriteriaTable = advancedSearchCriteriaTable;
	}

	@Override
	public void onRoleSelectedEventReceived(RoleSelectedEvent event) {
delegate.showApplicationLoading(true);
		delegate.setSelectedRoleOsceDay(event.getOsceDayProxy());
		delegate.initAdvancedSearchByStandardizedRole(event.getStandardizedRoleProxy().getId());
	}
	
	@Override
	public PatientInSemesterFlexTable getDataTable(){
		 return table;
	}

	
	
	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());		
		int left = ResolutionSettings.getSplitLayoutPanelLeft();
		
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
		
//		if(splitLayoutPanel.getWidget(0).getOffsetWidth() >= 1220){
//			
//			if(OsMaMainNav.getMenuStatus() == 0)
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//			else
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
//		}
			
	}
	
	@Override
	public void onClick(ClickEvent event) {
		delegate.showApplicationLoading(true);
		delegate.firePatientInSemesterRowSelectedEvent(table.onRowClick(table.getCellForEvent(event).getRowIndex()));
		delegate.showApplicationLoading(false);

	}

}
