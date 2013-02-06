/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.shared.BellAssignmentType;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.TimeBell;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 * 
 */
public class BellScheduleViewImpl extends Composite implements
		BellScheduleView, RecordChangeHandler, MenuClickHandler {

	private static BellScheduleViewUiBinder uiBinder = GWT
			.create(BellScheduleViewUiBinder.class);

	interface BellScheduleViewUiBinder extends
			UiBinder<Widget, BellScheduleViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	@UiField
	SplitLayoutPanel splitLayoutPanel;

	@UiField(provided = true)
	SimplePager pager;

	@UiField(provided = true)
	CellTable<BellAssignmentType> table;

	@UiField
	Label lblTitle;

	@UiField
	Button bellScheduleUpload;

	@UiField
	Button bellScheduleExport;

	@UiField
	RadioButton plusTime;

	@UiField
	RadioButton minusTime;

	@UiField
	Label lblMoveTime;

	@UiField
	Label lblMin;

	@UiField
	Label lblValidator;

	@UiField
	IntegerBox timeBox;

	@UiField
	Button okBtn;

	@UiHandler("okBtn")
	public void okButtonClicked(ClickEvent event) {
		if ((timeBox != null && timeBox.getValue() != null)
		// Module 15 Bug Report Change include o to reset table.
				&& (timeBox.getValue() >= 0)) {
			// Module 15 Bug Report Change include o to reset table.
			if ((plusTime != null && plusTime.getValue())
					|| (minusTime != null && minusTime.getValue())) {
				lblValidator.setText("");
				delegate.getNewSchedule();
			} else {
				lblValidator.setText(constants.bellSchedValidatePlusMinus());
			}
		} else {
			lblValidator.setText(constants.bellSchedValidateMinute());
		}
	}

	@UiHandler("bellScheduleExport")
	public void bellScheduleExportButtonClicked(ClickEvent event) {
		delegate.onBellScheduleUpload();
	}

	OsceConstants constants = GWT.create(OsceConstants.class);

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public BellScheduleViewImpl(String semesterName) {
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		table = new CellTable<BellAssignmentType>(OsMaConstant.TABLE_PAGE_SIZE,
				tableResources);

		SimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
				true, OsMaConstant.TABLE_JUMP_SIZE, true);

		initWidget(uiBinder.createAndBindUi(this));
		init(semesterName);

		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0),
				OsMaConstant.SPLIT_PANEL_MINWIDTH);
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init(String semesterName) {

		initView(semesterName);
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
//		
//		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style",
//				"position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(OsMaMainNav.getMenuStatus() == 0)
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//		else
//			splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
//		
		

		paths.add(constants.osce());
		table.addColumn(new TextColumn<BellAssignmentType>() {

			Renderer<String> renderer = new AbstractRenderer<String>() {

				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(BellAssignmentType object) {
				return renderer.render(object.getOsceName());
			}
		}, constants.osce());

		paths.add(constants.bellSchedDateHeader());
		table.addColumn(new TextColumn<BellAssignmentType>() {

			Renderer<Date> renderer = new AbstractRenderer<Date>() {

				public String render(Date obj) {
					return obj == null ? "" : DateTimeFormat
							.getShortDateFormat().format(obj);
				}
			};

			@Override
			public String getValue(BellAssignmentType object) {
				return renderer.render(object.getOsceDate());
			}
		}, constants.bellSchedDateHeader());

		paths.add(constants.bellSchedTimeHeader());
		table.addColumn(new TextColumn<BellAssignmentType>() {

			Renderer<Date> renderer = new AbstractRenderer<Date>() {

				public String render(Date obj) {
					return obj == null ? "" : DateTimeFormat
							.getShortTimeFormat().format(obj);
				}
			};

			@Override
			public String getValue(BellAssignmentType object) {
				return renderer.render(object.getOsceTime());
			}
		}, constants.bellSchedTimeHeader());

		paths.add(constants.bellSchedToneHeader());
		table.addColumn(new TextColumn<BellAssignmentType>() {

			Renderer<Integer> renderer = new AbstractRenderer<Integer>() {

				public String render(Integer obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(BellAssignmentType object) {
				return renderer.render(object.getBellTone().getTone());
			}
		}, constants.bellSchedToneHeader());

	}

	private void initView(String semesterName) {

		lblTitle.setText(constants.bellSchedule() + " " + semesterName);
		lblMoveTime.setText(constants.bellSchedAdjustTime());
		lblMin.setText(constants.bellSchedMinute());

		plusTime.setText(constants.bellSchedPlusTime());
		minusTime.setText(constants.bellSchedMinusTime());
		okBtn.setText(constants.okBtn());

		bellScheduleUpload.setText(constants.bellScheduleUpload());
		bellScheduleExport.setText(constants.bellScheduleExport());

		ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				lblValidator.setText("");
			}
		};

		plusTime.addClickHandler(clickHandler);
		minusTime.addClickHandler(clickHandler);
		timeBox.addClickHandler(clickHandler);

		timeBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				lblValidator.setText("");
			}
		});

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
	public CellTable<BellAssignmentType> getTable() {
		return table;
	}

	@Override
	public void onRecordChange(RecordChangeEvent event) {
		int pagesize = 0;

		if (event.getRecordValue() == "ALL") {
			pagesize = table.getRowCount();
			OsMaConstant.TABLE_PAGE_SIZE = pagesize;
		} else {
			pagesize = Integer.parseInt(event.getRecordValue());
		}

		table.setPageSize(pagesize);
//		table.setVisibleRange(0,OsMaConstant.TABLE_PAGE_SIZE);
	}

	@Override
	public void setSemesterName(String semesterName) {
		lblTitle.setText(constants.bellSchedule() + " " + semesterName);
	}

	@Override
	public int getTimeInMinute() {
		if ((timeBox != null) && (timeBox.getValue() != null)) {
			return timeBox.getValue();
		} else {
			return 0;
		}
	}

	// public boolean isTimeSelected() {
	// return (!plusTime.getValue() && !plusTime.getValue());
	// }

	@Override
	public TimeBell isPlusTime() {
		// Log.info("\n\nplusTime: " + plusTime);
		// Log.info("\n\n plusTime value" + plusTime.getValue());
		// Log.info("\n\nminusTime: " + minusTime);
		// Log.info("\nminusTime value" + minusTime.getValue());

		return ((plusTime != null && plusTime.getValue()) ? TimeBell.TRUE
				: ((minusTime != null && minusTime.getValue()) ? TimeBell.FALSE
						: TimeBell.NONE));

	}
	


	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());		
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,false);
//		int left = (OsMaMainNav.getMenuStatus() == 0) ? 40 : 225;
//		
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: "+left+"px; top: 30px; right: 5px; bottom: 0px;");
//		
//		if(splitLayoutPanel.getWidget(0).getOffsetWidth() >= 1220){
//			
//			if(OsMaMainNav.getMenuStatus() == 0)
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1412);
//			else
//				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0), 1220);
//		}
			
	}



}
