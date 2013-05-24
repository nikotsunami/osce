package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.BellSchedulePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.BellScheduleView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.BellScheduleViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BellAssignmentType;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.ResourceDownloadProps;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.TimeBell;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.ServerFailure;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

/**
 * @author dk
 * 
 */

@SuppressWarnings("deprecation")
public class BellScheduleActivity extends AbstractActivity implements
		BellScheduleView.Presenter, BellScheduleView.Delegate {

	private OsMaRequestFactory requests;
	private PlaceController placeController;
	private AcceptsOneWidget widget;
	private BellScheduleView view;

	private BellSchedulePlace place;
	private BellScheduleActivity bellScheduleActivity;
	private SemesterProxy semesterProxy = null;
	private ActivityManager activityManager;
	public HandlerManager handlerManager;
	private BellScheduleActivityMapper activityMapper;
	private HandlerRegistration rangeChangeHandler;
	// @SPEC table to add data and remove
	private CellTable<BellAssignmentType> table;
	private List<AssignmentProxy> assignmentProxies;
	
	private SelectChangeHandler removeHandler;
	List<BellAssignmentType> bellAssignmentTypes;
	
	private OsceConstants constants = GWT.create(OsceConstants.class);

	public BellScheduleActivity(OsMaRequestFactory requests,
			PlaceController placeController) {
		this.requests = requests;
		this.placeController = placeController;
	}

	public BellScheduleActivity(OsMaRequestFactory requests,
			PlaceController placeController, BellSchedulePlace place) {

		this.requests = requests;
		this.placeController = placeController;
		this.place = place;
		this.handlerManager = place.handler;
		this.semesterProxy = place.semesterProxy;

		activityMapper = new BellScheduleActivityMapper(requests,
				placeController);
		this.activityManager = new ActivityManager(activityMapper,
				requests.getEventBus());

		// ApplicationLoadingScreenEvent.initialCounter();
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(
							ApplicationLoadingScreenEvent event) {
//						Log.info("ApplicationLoadingScreenEvent onEventReceived Called");
						event.display();
					}
				});

		this.addSelectChangeHandler(new SelectChangeHandler() {
			@Override
			public void onSelectionChange(SelectChangeEvent event) {				
				semesterProxy = event.getSemesterProxy();
				init();
			}
		});

	}

	public void addSelectChangeHandler(SelectChangeHandler handler) {
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);
		removeHandler=handler;
	}

	public void onStop() 
	{
		handlerManager.removeHandler(SelectChangeEvent.getType(), removeHandler);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		Log.info("SystemStartActivity.start()");
		BellScheduleView systemStartView = new BellScheduleViewImpl(
				getSemesterName());
		systemStartView.setPresenter(this);
		this.widget = panel;
		this.view = systemStartView;
		widget.setWidget(systemStartView.asWidget());

		RecordChangeEvent.register(requests.getEventBus(),
				(BellScheduleViewImpl) view);
		
		
		MenuClickEvent.register(requests.getEventBus(),
				(BellScheduleViewImpl) view);
		
		setTable(view.getTable());

		init();

		view.setDelegate(this);
	}

	private String getSemesterName() {
		String name = new EnumRenderer<Semesters>().render(semesterProxy.getSemester());
		name += " " + this.semesterProxy.getCalYear();
		return name;
	}

	private void init() {
		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));

		view.setSemesterName(getSemesterName());
		init2();
	}

	private class QwtFileReceiver extends OSCEReceiver<String> {
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

	public void init2() {

		System.out.println("Inside INIT2()");

		// fireCountAssignmentRequest(new OSCEReceiver<Integer>() {
		// @Override
		// public void onSuccess(Integer response) {
		// if (view == null) {
		// // This activity is dead
		// return;
		// }
		// Log.debug("Geholte Nationalitäten aus der Datenbank: "
		// + response);
		// System.out
		// .println("Arrived result of count set table size according to it");
		// view.getTable().setRowCount(response.intValue(), true);
		//
		// onRangeChanged();
		// }
		// });

		view.getTable().setRowCount(0, true);

		onRangeChanged();

		table.setVisibleRange(0,OsMaConstant.TABLE_PAGE_SIZE); 
		rangeChangeHandler = table
				.addRangeChangeHandler(new RangeChangeEvent.Handler() {
					public void onRangeChange(RangeChangeEvent event) {						
						onRangeChanged();
					}
				});

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));
	}

	private void setTable(CellTable<BellAssignmentType> table) {
		this.table = table;

	}

	public void fireCountAssignmentRequest(OSCEReceiver<Integer> callback) {

		requests.assignmentRequestNonRoo()
				.getCountAssignmentsBySemester(this.semesterProxy.getId())
				.fire(callback);
	}

	protected void onRangeChanged() {
		final Range range = table.getVisibleRange();

		final OSCEReceiver<List<AssignmentProxy>> callback = new OSCEReceiver<List<AssignmentProxy>>() {

			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				if (view == null) {
					return;
				}
				Log.info("On range Changed event : ");

				bellAssignmentTypes = BellAssignmentType
						.getBellAssignmentProxyType(response,
								view.getTimeInMinute(), view.isPlusTime(),
								semesterProxy);

				assignmentProxies = response;

				// Module 15 Bug Report Change
				// Log.info("Range Start from : "+ range.getStart() );
				// Log.info("Range Length is : "+ range.getLength() );
				// Log.info("OsMaConstant.TABLE_PAGE_SIZE is : "+OsMaConstant.TABLE_PAGE_SIZE );
				// Log.info("bellAssignmentTypes.size()"+bellAssignmentTypes.size());
				
//				table.setVisibleRange(range.getStart(),OsMaConstant.TABLE_PAGE_SIZE);
//				table.setRowCount(bellAssignmentTypes.size());
				
				table.setRowData(range.getStart(), bellAssignmentTypes.subList(range.getStart(), bellAssignmentTypes.size()));
				// Module 15 Bug Report Change
				
				if (widget != null) {
					widget.setWidget(view.asWidget());
				}
			}

		};

		fireRangeRequest(callback);
	}

	private void fireRangeRequest(final OSCEReceiver<List<AssignmentProxy>> callback) {
		createRangeRequest().with(view.getPaths()).fire(callback);
	}

	protected Request<List<AssignmentProxy>> createRangeRequest() {

		// return null;

		return requests.assignmentRequestNonRoo().getAssignmentsBySemester(this.semesterProxy.getId()).with("osceDay", "osceDay.osce", "osceDay.osce.semester");
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public SemesterProxy getSemester() {
		return this.semesterProxy;
	}

	@Override
	public void getNewSchedule() {
		init();

	}

	@Override
	public void onBellScheduleUpload() {

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(true));

		requests.assignmentRequestNonRoo()
				.getQwtBellSchedule(
						// bellAssignmentTypes,assignmentProxies,
						this.semesterProxy.getId(), view.getTimeInMinute(),
						view.isPlusTime()).fire(new QwtFileReceiver());

		requests.getEventBus().fireEvent(
				new ApplicationLoadingScreenEvent(false));

	}

	@Override
	public void exportCsvSupervisorClicked(final int x, final int y) {
		final PopupPanel popupPanel = new PopupPanel();
		
		popupPanel.setAutoHideEnabled(true);
		popupPanel.setAnimationEnabled(true);
		
		final VerticalPanel mainVp = new VerticalPanel();
		mainVp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainVp.setSpacing(7);
		
		final HorizontalPanel osceDayHp = new HorizontalPanel();
		Label osceDayLbl = new Label(constants.osceDay() + " : ");
		osceDayLbl.setWidth("130px");
		final ValueListBox<OsceDayProxy> listBox = new ValueListBox<OsceDayProxy>(new AbstractRenderer<OsceDayProxy>() {

			@Override
			public String render(OsceDayProxy object) {
				String dateVal = "";
				if (object != null)
					dateVal = DateTimeFormat.getFormat("yyyy-MM-dd").format(object.getOsceDate());
				
				return dateVal;
			}
		});
		osceDayHp.add(osceDayLbl);
		osceDayHp.add(listBox);
		
		requests.semesterRequestNonRoo().findAllOsceDayBySemester(semesterProxy.getId()).fire(new OSCEReceiver<List<OsceDayProxy>>() {

			@Override
			public void onSuccess(List<OsceDayProxy> response) {
				if (response.size() > 0)
					listBox.setValue(response.get(0));
				
				listBox.setAcceptableValues(response);		
				
				HorizontalPanel startToneHp = new HorizontalPanel();
				Label startToneLbl = new Label(constants.startTone() + " : ");
				startToneLbl.setWidth("130px");
				final TextBox startToneTxtBox = new TextBox();
				startToneTxtBox.setWidth("95px");
				startToneHp.add(startToneLbl);
				startToneHp.add(startToneTxtBox);
				startToneTxtBox.setValue("Sound");
				startToneTxtBox.addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						if (startToneTxtBox.getValue().isEmpty())
							startToneTxtBox.setValue("Sound");
					}
				});
				startToneTxtBox.addFocusHandler(new FocusHandler() {
					
					@Override
					public void onFocus(FocusEvent event) {
						if (startToneTxtBox.getValue().equals("Sound"))
							startToneTxtBox.setValue("");
					}
				});
				
				
				HorizontalPanel endToneHp = new HorizontalPanel();
				Label endToneLbl = new Label(constants.endDate() + " : ");
				endToneLbl.setWidth("130px");
				final TextBox endToneTxtBox = new TextBox();
				endToneTxtBox.setWidth("95px");
				endToneHp.add(endToneLbl);
				endToneHp.add(endToneTxtBox);
				
				endToneTxtBox.setValue("Sound");
				endToneTxtBox.addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						if (endToneTxtBox.getValue().isEmpty())
							endToneTxtBox.setValue("Sound");
					}
				});
				endToneTxtBox.addFocusHandler(new FocusHandler() {
					
					@Override
					public void onFocus(FocusEvent event) {
						if (endToneTxtBox.getValue().equals("Sound"))
							endToneTxtBox.setValue("");
					}
				});
				
				HorizontalPanel prePostHp = new HorizontalPanel();
				Label prePostLbl = new Label(constants.prePostEnd() + " : ");
				prePostLbl.setWidth("130px");
				final TextBox prePostTxtBox = new TextBox();
				final TextBox prePostToneTxtBox = new TextBox();
				prePostTxtBox.setWidth("42px");
				prePostTxtBox.getElement().getStyle().setMarginRight(5, Unit.PX);
				prePostToneTxtBox.setWidth("42px");
				prePostHp.add(prePostLbl);
				prePostHp.add(prePostTxtBox);
				prePostHp.add(prePostToneTxtBox);
				
				prePostTxtBox.setValue("Minutes");
				prePostToneTxtBox.setValue("Sound");
				prePostTxtBox.addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						if (prePostTxtBox.getValue().isEmpty())
							prePostTxtBox.setValue("Minutes");
					}
				});
				prePostTxtBox.addFocusHandler(new FocusHandler() {
					
					@Override
					public void onFocus(FocusEvent event) {
						if (prePostTxtBox.getValue().equals("Minutes"))
							prePostTxtBox.setValue("");
					}
				});
				
				prePostToneTxtBox.addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						if (prePostToneTxtBox.getValue().isEmpty())
							prePostToneTxtBox.setValue("Sound");
					}
				});
				prePostToneTxtBox.addFocusHandler(new FocusHandler() {
					
					@Override
					public void onFocus(FocusEvent event) {
						if (prePostToneTxtBox.getValue().equals("Sound"))
							prePostToneTxtBox.setValue("");
					}
				});
				
				HorizontalPanel preBreakHp = new HorizontalPanel();
				Label preBreakLbl = new Label(constants.preBreakEnd() + " : ");
				preBreakLbl.setWidth("130px");
				final TextBox preBreakTxtBox = new TextBox();
				final TextBox preBreakToneTxtBox = new TextBox();
				preBreakTxtBox.getElement().getStyle().setMarginRight(5, Unit.PX);
				preBreakTxtBox.setWidth("42px");
				preBreakToneTxtBox.setWidth("42px");				
				preBreakHp.add(preBreakLbl);
				preBreakHp.add(preBreakTxtBox);
				preBreakHp.add(preBreakToneTxtBox);
				
				preBreakTxtBox.setValue("Minutes");
				preBreakToneTxtBox.setValue("Sound");
				preBreakTxtBox.addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						if (preBreakTxtBox.getValue().isEmpty())
							preBreakTxtBox.setValue("Minutes");
					}
				});
				preBreakTxtBox.addFocusHandler(new FocusHandler() {
					
					@Override
					public void onFocus(FocusEvent event) {
						if (preBreakTxtBox.getValue().equals("Minutes"))
							preBreakTxtBox.setValue("");
					}
				});
				
				preBreakToneTxtBox.addBlurHandler(new BlurHandler() {
					
					@Override
					public void onBlur(BlurEvent event) {
						if (preBreakToneTxtBox.getValue().isEmpty())
							preBreakToneTxtBox.setValue("Sound");
					}
				});
				preBreakToneTxtBox.addFocusHandler(new FocusHandler() {
					
					@Override
					public void onFocus(FocusEvent event) {
						if (preBreakToneTxtBox.getValue().equals("Sound"))
							preBreakToneTxtBox.setValue("");
					}
				});
				
				HorizontalPanel okHp = new HorizontalPanel();
				okHp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);		
				IconButton okBtn = new IconButton();
				okBtn.setIcon("check");
				okBtn.setText(constants.okBtn());
				
				okBtn.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
						if (startToneTxtBox.getValue().isEmpty() || startToneTxtBox.getValue().equals("Sound") || endToneTxtBox.getValue().isEmpty() || endToneTxtBox.getValue().equals("Sound"))
						{
							MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
							dialogBox.showConfirmationDialog(constants.startEndToneErr());
							return;							
						}
						
						if ((startToneTxtBox.getValue().equals("Sound")) && startToneTxtBox.getValue().matches("[0-9]+"))
						{
							MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
							dialogBox.showConfirmationDialog("Enter correct value for start tone");
							return;
						}
						
						if ((endToneTxtBox.getValue().equals("Sound")) && !endToneTxtBox.getValue().matches("[0-9]+"))
						{
							MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
							dialogBox.showConfirmationDialog("Enter correct value for end tone");
							return;
						}
						
						if ((!prePostTxtBox.getValue().equals("Minutes")) && !prePostTxtBox.getValue().matches("[0-9]+"))
						{
							MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
							dialogBox.showConfirmationDialog("Enter correct value for pre post end time");
							return;
						}
						
						if ((!prePostToneTxtBox.getValue().equals("Sound")) && !prePostToneTxtBox.getValue().matches("[0-9]+"))
						{
							MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
							dialogBox.showConfirmationDialog("Enter correct value for pre post end tone");
							return;
						}
						
						if ((!preBreakTxtBox.getValue().equals("Minutes")) && !preBreakTxtBox.getValue().matches("[0-9]+"))
						{
							MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
							dialogBox.showConfirmationDialog("Enter correct value for pre break end value");
							return;
						}
						
						if ((!preBreakToneTxtBox.getValue().equals("Sound")) && !preBreakToneTxtBox.getValue().matches("[0-9]+"))
						{
							MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
							dialogBox.showConfirmationDialog("Enter correct value for pre break tone");
							return;
						}
						
						popupPanel.hide();
						OsceDayProxy osceDayProxy = listBox.getValue();
						
						int time = view.getTimeInMinute();
						TimeBell timeBell = view.isPlusTime();
						if (timeBell.equals(TimeBell.FALSE))
							time = -time;
						else if (timeBell.equals(TimeBell.NONE))
							time = 0;
						
						String ordinal = URL.encodeQueryString(String.valueOf(ResourceDownloadProps.Entity.ALARM_SCHEDULE_SUPERVISOR.ordinal()));          
						String url = GWT.getHostPageBaseURL() + "downloadFile?".concat(ResourceDownloadProps.ENTITY).concat("=").concat(ordinal)
								.concat("&").concat(ResourceDownloadProps.OSCEDAYID).concat("=").concat(URL.encodeQueryString(osceDayProxy.getId().toString()))
								.concat("&").concat(ResourceDownloadProps.START_TONE).concat("=").concat(URL.encodeQueryString(startToneTxtBox.getValue()))
								.concat("&").concat(ResourceDownloadProps.END_TONE).concat("=").concat(URL.encodeQueryString(endToneTxtBox.getValue()))
								.concat("&").concat(ResourceDownloadProps.PRE_POST_END_TIME).concat("=").concat(URL.encodeQueryString((prePostTxtBox.getValue().isEmpty() || prePostTxtBox.getValue().equals("Minutes")) ? "0" : prePostTxtBox.getValue()))
								.concat("&").concat(ResourceDownloadProps.PRE_POST_END_TONE).concat("=").concat(URL.encodeQueryString((prePostToneTxtBox.getValue().isEmpty() || prePostToneTxtBox.getValue().equals("Sound")) ? "0" : prePostToneTxtBox.getValue()))
								.concat("&").concat(ResourceDownloadProps.PRE_BREAK_END_TIME).concat("=").concat(URL.encodeQueryString((preBreakTxtBox.getValue().isEmpty() || preBreakTxtBox.getValue().equals("Minutes")) ? "0" : preBreakTxtBox.getValue()))
								.concat("&").concat(ResourceDownloadProps.PRE_BREAK_END_TONE).concat("=").concat(URL.encodeQueryString((preBreakToneTxtBox.getValue().isEmpty() || preBreakToneTxtBox.getValue().equals("Sound")) ? "0" : preBreakToneTxtBox.getValue()))
								.concat("&").concat(ResourceDownloadProps.PLUS_TIME).concat("=").concat(URL.encodeQueryString(String.valueOf(time)));
						Log.info("--> url is : " +url);
						Window.open(url, "", "");	
						
					}
				});
				
				okHp.add(okBtn);
				
				mainVp.add(osceDayHp);
				mainVp.add(startToneHp);
				mainVp.add(endToneHp);
				mainVp.add(prePostHp);
				//mainVp.add(prePostToneHp);
				mainVp.add(preBreakHp);
				//mainVp.add(preBreakToneHp);
				mainVp.add(okHp);
				
				popupPanel.add(mainVp);
				
				popupPanel.setPopupPosition(x, y-275);
				popupPanel.show();
			}
		});
		
		
	}
}
