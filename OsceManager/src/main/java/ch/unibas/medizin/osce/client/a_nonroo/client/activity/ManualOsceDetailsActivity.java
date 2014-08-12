package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ManualOsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.AccordianPanelViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.ContentViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.HeaderViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceChangeBreakView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceChangeBreakViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceCreateOscePostPopUpView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceCreateOscePostPopUpViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceDaySubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceDaySubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceDetailsView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceDetailsViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceEditView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceEditViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceParcourView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceParcourViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOscePopupView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOscePopupViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOscePostSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOscePostSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceSequenceParcourView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceSequenceParcourViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceSequenceView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceSequenceViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceSubView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceSubViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ApplicationLoadingScreenHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.ManualOsceRoomRefreshEvent;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostRequest;
import ch.unibas.medizin.osce.client.managed.request.OscePostRoomProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceRequest;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceRequest;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.ColorPicker;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class ManualOsceDetailsActivity extends AbstractActivity implements
										ManualOsceEditView.Delegate,
										ManualOsceDetailsView.Delegate,
										ManualOsceSubView.Delegate,
										ManualOsceDaySubView.Delegate,
										ManualOsceSequenceParcourView.Delegate,
										ManualOsceParcourView.Delegate,
										ManualOsceSequenceView.Delegate,
										ManualOsceCreateOscePostPopUpView.Delegate,
										ManualOscePostSubView.Delegate,
										ManualOscePopupView.Delegate,
										ManualOsceChangeBreakView.Delegate,
										AccordianPanelView.Delegate, 
										AccordianPanelView.ParcourDelegate,
										ContentView.Delegate,
										HeaderView.Delegate,
										DragHandler{

	private final ManualOsceDetailsPlace place;
	private final OsMaRequestFactory requests;
	//private final PlaceController placeController;
	private AcceptsOneWidget widget;
	//private EventBus eventBus;
	private ManualOsceDetailsView view;
	private OsceProxy osceProxy;
	private ManualOsceSubViewImpl manualOsceSubViewImpl;
	private OsceConstants constants = GWT.create(OsceConstants.class);

	//OsceDayID = ManualOsceDaySubViewImpl
	private Map<Long, ManualOsceDaySubViewImpl> osceDayViewMap = new HashMap<Long, ManualOsceDaySubViewImpl>();
	
	//OsceDayID = ManualOsceSequenceViewImpl
	private Map<Long, ManualOsceSequenceViewImpl> osceSequenceViewMap = new HashMap<Long, ManualOsceSequenceViewImpl>(); 
	
	//OsceSequenceID = ManualOsceDaySubViewImpl
	private Map<Long, ManualOsceSequenceParcourViewImpl> osceSequenceParcourViewMap = new HashMap<Long, ManualOsceSequenceParcourViewImpl>();
	
	//CourseID = ManualOsceParcourView
	private Map<Long, ManualOsceParcourView> courseViewMap = new HashMap<Long, ManualOsceParcourView>();
	
	//OscePostRoomID = ManualOscePostSubView
	private Map<Long, ManualOscePostSubView> oscePostViewMap = new HashMap<Long, ManualOscePostSubView>();
	
	private List<Long> oprList = new ArrayList<Long>();
	
	private ManualOsceCreateOscePostPopUpView manualOsceCreateOscePostPopUpView;
	
	Long lastCourseId = 0l;
	
	private OsceConstantsWithLookup constantsLookup = GWT.create(OsceConstantsWithLookup.class);
	
	public ManualOsceDetailsActivity(ManualOsceDetailsPlace place, OsMaRequestFactory requests, PlaceController placeController) {
		this.place = place;
		this.requests = requests;
		//this.placeController = placeController;		
    }
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.widget = panel;
		//this.eventBus = eventBus;
		
		ManualOsceDetailsView manualOsceView = new ManualOsceDetailsViewImpl();
		widget.setWidget(manualOsceView.asWidget());
		manualOsceView.setDelegate(this);
		
		this.view = manualOsceView;
		
		manualOsceSubViewImpl = view.getManualOsceSubViewImpl();		
		manualOsceSubViewImpl.setDelegate(this);
		
		initLoadingScreen();
		
		showApplicationLoading(true);
		
		requests.find(place.getProxyId()).with("osce_days", "osce_days.osceSequences", "osce_days.osceSequences.osceDayRotations", "osce_days.osceSequences.courses").fire(new OSCEReceiver<Object>() {

			@Override
			public void onSuccess(Object response) {
				showApplicationLoading(false);
				if (response != null && response instanceof OsceProxy)
				{
					osceProxy = (OsceProxy) response;
					view.setOsceProxy(osceProxy);
					
					if (manualOsceSubViewImpl != null)
					{
						manualOsceSubViewImpl.getManualOsceEditViewImpl().setDelegate(ManualOsceDetailsActivity.this);
						manualOsceSubViewImpl.getManualOsceEditViewImpl().setOsceProxy(osceProxy);
					}
					initOsceDayView();
				}
			}
		});		
	}
	
	private void initLoadingScreen()
	{
		ApplicationLoadingScreenEvent.initialCounter();
		ApplicationLoadingScreenEvent.register(requests.getEventBus(),
				new ApplicationLoadingScreenHandler() {
					@Override
					public void onEventReceived(ApplicationLoadingScreenEvent event) {
						event.display();
					}
				});	
	}
	
	private void initParcourView(OsceSequenceProxy osceSequenceProxy, ManualOsceSequenceParcourViewImpl manualOsceSequenceParcourViewImpl) 
	{
		for (CourseProxy courseProxy : osceSequenceProxy.getCourses())
		{
			if (courseViewMap.containsKey(courseProxy.getId()))
				courseViewMap.remove(courseProxy.getId());
			
			if (osceSequenceProxy.getCourses().size() > 1)
				createParcourView(osceSequenceProxy, courseProxy, manualOsceSequenceParcourViewImpl, true);
			else
				createParcourView(osceSequenceProxy, courseProxy, manualOsceSequenceParcourViewImpl, false);
				
			lastCourseId = courseProxy.getId();
		}
		
		if (courseViewMap.containsKey(lastCourseId))
		{
			ManualOsceParcourView manualOsceParcourView = courseViewMap.get(lastCourseId);
			((AccordianPanelViewImpl)manualOsceParcourView.getAccordianPanelView()).expand((HeaderViewImpl)manualOsceParcourView.getHeaderView(), ((AccordianPanelViewImpl)manualOsceParcourView.getAccordianPanelView()).sp);
		}
	}
	
	private void createParcourView(OsceSequenceProxy osceSequenceProxy, final CourseProxy courseProxy, ManualOsceSequenceParcourViewImpl manualOsceSequenceParcourViewImpl, boolean deleteBtnFlg)
	{
		if (OsceStatus.OSCE_FIXED.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
		{
			deleteBtnFlg = false;
		}
		
		final ManualOsceParcourView manualOsceParcourView = new ManualOsceParcourViewImpl(courseProxy.getColor(), deleteBtnFlg);
		manualOsceParcourView.setDelegate(this);
		
		manualOsceParcourView.getHeaderView().setDelegate(this);
		manualOsceParcourView.getHeaderView().setProxy(courseProxy);
		manualOsceParcourView.getContentView().setDelegate(this);
		
		manualOsceParcourView.setCourseProxy(courseProxy);
		manualOsceParcourView.setOsceSequenceProxy(osceSequenceProxy);
		
		manualOsceParcourView.getAccordianPanelView().setDelegate(this);
		manualOsceParcourView.getAccordianPanelView().setParcourDelegate(this);
				
		if (manualOsceParcourView.getContentView().getPostHP().getWidgetCount() > 1)
		{
			for (int i=0; i<manualOsceParcourView.getContentView().getPostHP().getWidgetCount(); i++)
			{
				Widget widget = manualOsceParcourView.getContentView().getPostHP().getWidget(i);
				if (widget instanceof ManualOscePostSubView)
				{
					manualOsceParcourView.getContentView().getPostHP().remove(i);
				}
			}
		}
		
		requests.oscePostRoomRequestNonRoo().findOscePostRoomByCourseIdOrderByOscePostSeqNo(courseProxy.getId()).with("room", "oscePost", "oscePost.oscePostBlueprint", "oscePost.standardizedRole", "oscePost.standardizedRole", "oscePost.oscePostBlueprint.roleTopic", "oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

			@Override
			public void onSuccess(List<OscePostRoomProxy> response) {
				
				for (OscePostRoomProxy oscePostRoomProxy : response)
				{
					createOscePostView(oscePostRoomProxy, manualOsceParcourView, courseProxy);
				}
				
				if (OsceStatus.OSCE_FIXED.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
				{
					manualOsceParcourView.getAddOscePostVerticalPanel().getElement().getStyle().setDisplay(Display.NONE);					
				}
			}
		});		
		
		manualOsceSequenceParcourViewImpl.getParcourPanel().add(manualOsceParcourView);
		
		courseViewMap.put(courseProxy.getId(), manualOsceParcourView);
	}

	private void createOscePostView(OscePostRoomProxy oscePostRoomProxy, ManualOsceParcourView manualOsceParcourView, CourseProxy courseProxy)
	{
		ManualOscePostSubView manualOscePostSubView = new ManualOscePostSubViewImpl();
		
		if (OsceStatus.OSCE_FIXED.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
		{
			manualOscePostSubView.getDeletePost().getElement().getStyle().setDisplay(Display.NONE);
		}
		
		if (OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
		{
			manualOscePostSubView.getEditRoom().getElement().getStyle().setDisplay(Display.NONE);
			manualOscePostSubView.getEditStandardizedRole().getElement().getStyle().setDisplay(Display.NONE);
		}
		
		ManualOsceRoomRefreshEvent.register(requests.getEventBus(), (ManualOscePostSubViewImpl)manualOscePostSubView);
		manualOscePostSubView.setDelegate(this);
		manualOscePostSubView.setCourseProxy(courseProxy);
		manualOscePostSubView.setOscePostRoomProxy(oscePostRoomProxy);
		
		manualOscePostSubView.setValue(oscePostRoomProxy);
		
		manualOsceParcourView.getContentView().getPostHP().insert(manualOscePostSubView, manualOsceParcourView.getContentView().getPostHP().getWidgetCount() - 1);
		manualOsceParcourView.getContentView().getDragController().makeDraggable(manualOscePostSubView.asWidget(), manualOscePostSubView.getPostNameLbl());
		manualOsceParcourView.getContentView().getDragController().addDragHandler(ManualOsceDetailsActivity.this);
		
		oscePostViewMap.put(oscePostRoomProxy.getId(), manualOscePostSubView);
	}
	
	public void initOsceDayView()
	{	
		if (osceProxy != null && osceProxy.getOsce_days() != null && osceProxy.getOsce_days().isEmpty() == false)
		{
			showApplicationLoading(true);
			if (OsceStatus.OSCE_FIXED.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
			{
				manualOsceSubViewImpl.getAddOsceDayVerticalPanel().getElement().getStyle().setDisplay(Display.NONE);
			}
			
			int osceDayIndex = 1;
			for (OsceDayProxy osceDayProxy : osceProxy.getOsce_days())
			{
				if (osceDayViewMap.containsKey(osceDayProxy.getId()) == false)
				{
					ManualOsceDaySubViewImpl manualOsceDaySubViewImpl = new ManualOsceDaySubViewImpl();
					manualOsceDaySubViewImpl.getDayLabel().setText(constants.circuitDay() + " " + osceDayIndex);
					manualOsceDaySubViewImpl.setDelegate(this);
					manualOsceDaySubViewImpl.setOsceDayProxy(osceDayProxy);
					osceDayViewMap.put(osceDayProxy.getId(), manualOsceDaySubViewImpl);
					manualOsceSubViewImpl.getOsceDayVp().add(manualOsceDaySubViewImpl);
					osceDayIndex += 1;
					
					if (OsceStatus.OSCE_FIXED.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
					{
						manualOsceDaySubViewImpl.getSaveOsceDayValue().getElement().getStyle().setDisplay(Display.NONE);
						manualOsceDaySubViewImpl.getDeleteOsceDay().getElement().getStyle().setDisplay(Display.NONE);
					}
					
					showApplicationLoading(false);
					initOsceSequenceView(osceDayProxy, manualOsceDaySubViewImpl);
				}
			}
			
			showApplicationLoading(false);
		}
	}

	private void initOsceSequenceView(OsceDayProxy osceDayProxy, ManualOsceDaySubViewImpl manualOsceDaySubViewImpl) {
		if (osceDayProxy.getOsceSequences() != null)
		{
			showApplicationLoading(true);
			ManualOsceSequenceViewImpl manualOsceSequenceViewImpl = new ManualOsceSequenceViewImpl();
			
			if (osceProxy != null)
				manualOsceSequenceViewImpl.getManualOscelunchBreakViewImpl().getLunchBreakDuration().setValue(osceProxy.getLunchBreak().toString());
		
			if (OsceStatus.OSCE_FIXED.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
			{	
				manualOsceSequenceViewImpl.getAddSequencePanel().getElement().getStyle().setDisplay(Display.NONE);
				manualOsceSequenceViewImpl.getManualOscelunchBreakViewImpl().getLunchBreakDuration().setReadOnly(true);
				manualOsceSequenceViewImpl.getManualOscelunchBreakViewImpl().getLunchBreakDuration().setEnabled(false);
			}
			
			manualOsceSequenceViewImpl.setDelegate(this);
			manualOsceSequenceViewImpl.setOsceDayProxy(osceDayProxy);
			osceSequenceViewMap.put(osceDayProxy.getId(), manualOsceSequenceViewImpl);
			
			manualOsceDaySubViewImpl.getSequencePanel().add(manualOsceSequenceViewImpl);			
			
			if (osceDayProxy.getOsceSequences().size() == 1)
			{
				ManualOsceSequenceParcourViewImpl firstManualOsceSequenceParcourViewImpl = new ManualOsceSequenceParcourViewImpl();
				firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setOsceSequenceProxy(osceDayProxy.getOsceSequences().get(0));
				firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setDelegate(this);
				firstManualOsceSequenceParcourViewImpl.setDelegate(this);
				firstManualOsceSequenceParcourViewImpl.setOsceSequenceProxy(osceDayProxy.getOsceSequences().get(0));
				firstManualOsceSequenceParcourViewImpl.setOsceDayProxy(osceDayProxy);
				manualOsceSequenceViewImpl.getFirstOsceSequencePanel().add(firstManualOsceSequenceParcourViewImpl);
				osceSequenceParcourViewMap.put(osceDayProxy.getOsceSequences().get(0).getId(), firstManualOsceSequenceParcourViewImpl);
				
				if (OsceStatus.OSCE_FIXED.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
				{	
					firstManualOsceSequenceParcourViewImpl.getAddParcourVerticalPanel().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getDeleteOsceSeqBtn().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getEditSequence().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getBreakSooner().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getBreakLater().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getAddRotation().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRemoveRotation().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRotationNumber().setReadOnly(true);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRotationNumber().setEnabled(false);
				}
				
				initParcourView(osceDayProxy.getOsceSequences().get(0), firstManualOsceSequenceParcourViewImpl);
			}
			else if (osceDayProxy.getOsceSequences().size() == 2)
			{
				ManualOsceSequenceParcourViewImpl firstManualOsceSequenceParcourViewImpl = new ManualOsceSequenceParcourViewImpl();
				firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setOsceSequenceProxy(osceDayProxy.getOsceSequences().get(0));
				firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setDelegate(this);
				firstManualOsceSequenceParcourViewImpl.setDelegate(this);
				firstManualOsceSequenceParcourViewImpl.setOsceSequenceProxy(osceDayProxy.getOsceSequences().get(0));
				firstManualOsceSequenceParcourViewImpl.setOsceDayProxy(osceDayProxy);
				manualOsceSequenceViewImpl.getFirstOsceSequencePanel().add(firstManualOsceSequenceParcourViewImpl);
				osceSequenceParcourViewMap.put(osceDayProxy.getOsceSequences().get(0).getId(), firstManualOsceSequenceParcourViewImpl);
				
				initParcourView(osceDayProxy.getOsceSequences().get(0), firstManualOsceSequenceParcourViewImpl);
				
				manualOsceSequenceViewImpl.getAddSequencePanel().getElement().getStyle().setDisplay(Display.NONE);
				
				ManualOsceSequenceParcourViewImpl secondManualOsceSequenceParcourViewImpl = new ManualOsceSequenceParcourViewImpl();
				secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setOsceSequenceProxy(osceDayProxy.getOsceSequences().get(1));
				secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setDelegate(this);
				secondManualOsceSequenceParcourViewImpl.setDelegate(this);
				secondManualOsceSequenceParcourViewImpl.setOsceSequenceProxy(osceDayProxy.getOsceSequences().get(1));
				secondManualOsceSequenceParcourViewImpl.setOsceDayProxy(osceDayProxy);
				secondManualOsceSequenceParcourViewImpl.addOsceSequenceDeleteButton();
				manualOsceSequenceViewImpl.getSecondOsceSequencePanel().add(secondManualOsceSequenceParcourViewImpl);
				osceSequenceParcourViewMap.put(osceDayProxy.getOsceSequences().get(1).getId(), secondManualOsceSequenceParcourViewImpl);
				
				if (OsceStatus.OSCE_FIXED.equals(osceProxy.getOsceStatus()) || OsceStatus.OSCE_CLOSED.equals(osceProxy.getOsceStatus()))
				{
					firstManualOsceSequenceParcourViewImpl.getAddParcourVerticalPanel().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getDeleteOsceSeqBtn().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getEditSequence().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getBreakSooner().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getBreakLater().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getAddRotation().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRemoveRotation().getElement().getStyle().setDisplay(Display.NONE);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRotationNumber().setReadOnly(true);
					firstManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRotationNumber().setEnabled(false);
					
					secondManualOsceSequenceParcourViewImpl.getAddParcourVerticalPanel().getElement().getStyle().setDisplay(Display.NONE);
					secondManualOsceSequenceParcourViewImpl.getDeleteOsceSeqBtn().getElement().getStyle().setDisplay(Display.NONE);
					secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getEditSequence().getElement().getStyle().setDisplay(Display.NONE);
					secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getBreakSooner().getElement().getStyle().setDisplay(Display.NONE);
					secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getBreakLater().getElement().getStyle().setDisplay(Display.NONE);
					secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getAddRotation().getElement().getStyle().setDisplay(Display.NONE);
					secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRemoveRotation().getElement().getStyle().setDisplay(Display.NONE);
					secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRotationNumber().setReadOnly(true);
					secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRotationNumber().setEnabled(false);
				}
				
				
				initParcourView(osceDayProxy.getOsceSequences().get(1), secondManualOsceSequenceParcourViewImpl);
			}
			
			showApplicationLoading(false);
		}
	}

	@Override
	public void retrieveContent(AccordianPanelViewImpl view, Widget header, Widget sp) {
	}

	@Override
	public void refreshParcourContent(AccordianPanelViewImpl view, Widget header, OsceSequenceProxy osceSequenceProxy) {
		
		final HeaderView parHeaderView = (HeaderView) header;
		final CourseProxy courseProxy = parHeaderView.getProxy();
		
		if (courseProxy != null)
		{
			showApplicationLoading(true);
			
			lastCourseId = courseProxy.getId();
			
			if (courseViewMap.containsKey(lastCourseId))
			{
				final ManualOsceParcourView manualOsceParcourView = courseViewMap.get(lastCourseId);
								
				requests.oscePostRoomRequestNonRoo().findOscePostRoomByCourseID(courseProxy.getId()).with("room", "oscePost", "oscePost.oscePostBlueprint", "oscePost.standardizedRole", "oscePost.standardizedRole", "oscePost.oscePostBlueprint.roleTopic", "oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

					@Override
					public void onSuccess(List<OscePostRoomProxy> response) {
						
						for (OscePostRoomProxy oscePostRoomProxy : response)
						{
							if (oscePostViewMap.containsKey(oscePostRoomProxy.getId()) == false)
								createOscePostView(oscePostRoomProxy, manualOsceParcourView, courseProxy);
						}
						
						showApplicationLoading(false);
					}
				});
			}	
		}
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		if (event.getSource() instanceof ManualOscePostSubView)
		{
			final List<Long> oscePostRoomProxyIdList = new ArrayList<Long>();
			
			if (((ManualOscePostSubViewImpl)event.getSource()).getParent() instanceof HorizontalPanel)
			{
				showApplicationLoading(true);
				
				HorizontalPanel horizontalPanel = (HorizontalPanel) ((ManualOscePostSubViewImpl)event.getSource()).getParent();

				for (int i=0; i<horizontalPanel.getWidgetCount(); i++)
				{
					Widget widget = horizontalPanel.getWidget(i);
					if (widget instanceof ManualOscePostSubView)
					{
						if (((ManualOscePostSubView) widget).getOscePostRoomProxy() != null)
						{
							oscePostRoomProxyIdList.add(((ManualOscePostSubView) widget).getOscePostRoomProxy().getId());							
						}
					}
				}				
				
				if (oprList.equals(oscePostRoomProxyIdList) == false)
				{
					oprList.addAll(oscePostRoomProxyIdList);
					requests.oscePostRoomRequestNonRoo().updateOscePostBlueprintSeqNumber(oscePostRoomProxyIdList).with("courses").fire(new OSCEReceiver<List<OsceSequenceProxy>>() {

						@Override
						public void onSuccess(List<OsceSequenceProxy> response) {
							for (OsceSequenceProxy osceSequenceProxy : response)
							{
								if (osceSequenceParcourViewMap.containsKey(osceSequenceProxy.getId()))
								{
									ManualOsceSequenceParcourViewImpl manualOsceSequenceParcourViewImpl = osceSequenceParcourViewMap.get(osceSequenceProxy.getId());
									manualOsceSequenceParcourViewImpl.getParcourPanel().clear();
									initParcourView(osceSequenceProxy, manualOsceSequenceParcourViewImpl);
								}
							}
						}
					});
				}
				
				showApplicationLoading(false);
			}
		}
	}

	@Override
	public void onDragStart(DragStartEvent event) {
		oprList = new ArrayList<Long>();
	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
	}

	@Override
	public void colorChanged(final HeaderView view, final String color) {
		showApplicationLoading(true);
		
		CourseProxy proxy = view.getProxy();
		
		CourseRequest courseRequest=requests.courseRequest();
		proxy=courseRequest.edit(proxy);
		proxy.setColor(color);
		
		courseRequest.persist().using(proxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				view.changeHeaderColor(ColorPicker.valueOf(color));
				changeContentViewColor(((view.getContentView())),color);
				showApplicationLoading(false);
			}
		});
	
	}
	
	public void changeContentViewColor(ContentView contentViewImpl,String color)
	{
		ContentViewImpl tempView = (ContentViewImpl) contentViewImpl;
		ColorPicker cp[]=ColorPicker.values();
	
		for(int i=0;i<ColorPicker.values().length;i++)
		{
			if(!cp[i].equals(color) && color!=null)
			{
				tempView.removeStyleName("accordion-title-selected" +cp[i].toString());					
			}
		}
		
		if(color==null)	
		{
			contentViewImpl.getContentPanel().addStyleDependentName("selectedwhite");
		}				
		else
		{
			String c="accordion-title-selected"+color.toString();
			tempView.addStyleName(c);
		}		
	}
	
	@Override
	public void addOsceDayClicked() {
		if (osceProxy != null)
		{
			showApplicationLoading(true);
			requests.osceDayRequestNooRoo().createOsceDaySequenceCourseAndOscePost(osceProxy.getId()).with("osce_days", "osce_days.osceSequences", "osce_days.osceSequences.osceDayRotations", "osce_days.osceSequences.courses").fire(new OSCEReceiver<OsceProxy>() {

				@Override
				public void onSuccess(OsceProxy response) {
					osceProxy = response;
					view.setOsceProxy(osceProxy);
					initOsceDayView();
					showApplicationLoading(false);
				}
			});
		}
		
	}

	@Override
	public void addParcourClicked(final OsceSequenceProxy osceSequenceProxy, final Boolean copyWithBreak, final boolean copyToAllSequence) {
		
		if (osceSequenceProxy != null)
		{
			showApplicationLoading(true);
			requests.courseRequestNonRoo().createNewCourse(osceSequenceProxy.getId(), copyWithBreak, copyToAllSequence).with("osceSequence").fire(new OSCEReceiver<List<CourseProxy>>() {

				@Override
				public void onSuccess(List<CourseProxy> response) {
						
					for (CourseProxy courseProxy : response)
					{
						if (courseProxy.getOsceSequence() != null)
						{
							final ManualOsceSequenceParcourViewImpl manualOsceSequenceParcourViewImpl = osceSequenceParcourViewMap.get(courseProxy.getOsceSequence().getId());
							
							if (manualOsceSequenceParcourViewImpl.getParcourPanel().getWidgetCount() == 1)
							{
								Widget widget = manualOsceSequenceParcourViewImpl.getParcourPanel().getWidget(0);
								if (widget instanceof ManualOsceParcourView)
								{
									((ManualOsceParcourView)widget).addDeleteButton();
								}
							}
							
							createParcourView(courseProxy.getOsceSequence(), courseProxy, manualOsceSequenceParcourViewImpl, true);
						}						
					}
					
					showApplicationLoading(false);
				}
			});
		}			
	}

	protected void addDeleteButton() {
		if (courseViewMap.size() == 1)
		{			
			for (ManualOsceParcourView view : courseViewMap.values()) {
				view.addDeleteButton();
			}
		}
	}

	@Override
	public void addOsceSequenceClicked(final OsceDayProxy osceDayProxy) {
		final ManualOsceSequenceViewImpl manualOsceSequenceViewImpl = osceSequenceViewMap.get(osceDayProxy.getId());
		
		if (manualOsceSequenceViewImpl != null)
		{
			showApplicationLoading(true);
			requests.osceSequenceRequestNonRoo().createOsceSequence(osceDayProxy.getId()).fire(new OSCEReceiver<Long>() {

				@Override
				public void onSuccess(Long osceSeqId) {
					if (osceSeqId != null)
					{
						requests.osceSequenceRequest().findOsceSequence(osceSeqId).with("courses").fire(new OSCEReceiver<OsceSequenceProxy>() {

							@Override
							public void onSuccess(OsceSequenceProxy response) {
								manualOsceSequenceViewImpl.getAddSequencePanel().getElement().getStyle().setDisplay(Display.NONE);
								
								ManualOsceSequenceParcourViewImpl secondManualOsceSequenceParcourViewImpl = new ManualOsceSequenceParcourViewImpl();
								secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setOsceSequenceProxy(response);
								secondManualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setDelegate(ManualOsceDetailsActivity.this);
								secondManualOsceSequenceParcourViewImpl.setOsceDayProxy(osceDayProxy);
								secondManualOsceSequenceParcourViewImpl.setDelegate(ManualOsceDetailsActivity.this);
								secondManualOsceSequenceParcourViewImpl.setOsceSequenceProxy(response);
								secondManualOsceSequenceParcourViewImpl.addOsceSequenceDeleteButton();
								manualOsceSequenceViewImpl.getSecondOsceSequencePanel().add(secondManualOsceSequenceParcourViewImpl);
								osceSequenceParcourViewMap.put(response.getId(), secondManualOsceSequenceParcourViewImpl);
								
								initParcourView(response, secondManualOsceSequenceParcourViewImpl);
							}
						});
					}
					
					showApplicationLoading(false);
				}
			});
		}
	}

	@Override
	public void addOscePost(CourseProxy courseProxy, PostType value, final FocusableValueListBox<PostType> postTypeListBox) {
		
		if (PostType.NORMAL.equals(value) || PostType.DUALSP.equals(value))
		{
			manualOsceCreateOscePostPopUpView = new ManualOsceCreateOscePostPopUpViewImpl();
			manualOsceCreateOscePostPopUpView.setDelegate(this);
			manualOsceCreateOscePostPopUpView.setCourseProxy(courseProxy);
			manualOsceCreateOscePostPopUpView.setPostType(value);
			showApplicationLoading(true);
			
			requests.specialisationRequestNonRoo().findSpecialisationSortByName(osceProxy.getStudyYear()).with("roleTopics", "roleTopics.standardizedRoles").fire(new OSCEReceiver<List<SpecialisationProxy>>() {

				@Override
				public void onSuccess(List<SpecialisationProxy> response) {
					initSpecialisationSuggestBox(response);
					
					requests.roomRequestNonRoo().findAllRoomsOrderByRoomNumber().fire(new OSCEReceiver<List<RoomProxy>>() {

						@Override
						public void onSuccess(List<RoomProxy> response) {
							showApplicationLoading(false);
							
							initRoomSuggestBox(response);
							
							((ManualOsceCreateOscePostPopUpViewImpl)manualOsceCreateOscePostPopUpView).showRelativeTo(postTypeListBox);
							int left = ((ManualOsceCreateOscePostPopUpViewImpl)manualOsceCreateOscePostPopUpView).getAbsoluteLeft();
							int top = ((ManualOsceCreateOscePostPopUpViewImpl)manualOsceCreateOscePostPopUpView).getAbsoluteTop();
							int width = postTypeListBox.getOffsetWidth();
							if (left == postTypeListBox.getAbsoluteLeft())							
								((ManualOsceCreateOscePostPopUpViewImpl)manualOsceCreateOscePostPopUpView).setPopupPosition((left - width), top);
							else
								((ManualOsceCreateOscePostPopUpViewImpl)manualOsceCreateOscePostPopUpView).setPopupPosition(left, top);
						}
					});
				}
			});
		}
		else if (PostType.BREAK.equals(value))
		{
			createBreakPostPopup(courseProxy);
		}
	}
	
	private void createBreakPostPopup(final CourseProxy courseProxy) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setAutoHideEnabled(true);
		dialogBox.setGlassEnabled(true);
		
		VerticalPanel vp=new VerticalPanel();
		HorizontalPanel hp=new HorizontalPanel();
		HorizontalPanel checkBoxHp=new HorizontalPanel();
		
		IconButton okBtn=new IconButton();
		IconButton cancelBtn=new IconButton();
		 
		final CheckBox horizontalCheckbox = new CheckBox(constants.manualOsceHorizontally());
		final CheckBox verticalCheckbox = new CheckBox(constants.manualOsceVertically());
		
		checkBoxHp.add(horizontalCheckbox);
		//checkBoxHp.add(verticalCheckbox);
		
		okBtn.setIcon("check");
		cancelBtn.setIcon("closethick");

		okBtn.setText(constants.okBtn());
		cancelBtn.setText(constants.cancel());
		
		hp.setSpacing(5);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.add(okBtn);
		hp.add(cancelBtn);		
				
		/*Label msgLbl=new Label();
		msgLbl.setText(constants.manualOsceBreakPostWarning());

		HorizontalPanel msgHp = new HorizontalPanel();
		msgHp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		msgHp.add(msgLbl);*/
		
		vp.setSpacing(2);
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		/*vp.add(msgHp);*/
		vp.add(checkBoxHp);
		vp.add(hp);
				
		dialogBox.getCaption().asWidget().addStyleName("confirmbox");		
		dialogBox.setText(constants.warning());
		dialogBox.add(vp);
		
		dialogBox.getElement().getStyle().setZIndex(3);
		
		dialogBox.center();
				
		okBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();			
				
				requests.oscePostRequestNonRoo().createBreakOscePostBluePrintOscePostAndOscePostRoom(courseProxy.getId(), PostType.BREAK, horizontalCheckbox.getValue(), verticalCheckbox.getValue()).with("course", "room", "oscePost", "oscePost.oscePostBlueprint", "oscePost.standardizedRole", "oscePost.standardizedRole", "oscePost.oscePostBlueprint.roleTopic", "oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

					@Override
					public void onSuccess(List<OscePostRoomProxy> response) {
						refreshParcourPanel(response);
					}
				});
			}
		});
		
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
	}

	private void initRoomSuggestBox(List<RoomProxy> response) {
		DefaultSuggestOracle<RoomProxy> suggestOracle = (DefaultSuggestOracle<RoomProxy>) manualOsceCreateOscePostPopUpView.getRoomSuggestBox().getSuggestOracle();
		suggestOracle.setPossiblilities(response);
		manualOsceCreateOscePostPopUpView.getRoomSuggestBox().setSuggestOracle(suggestOracle);
		manualOsceCreateOscePostPopUpView.getRoomSuggestBox().setRenderer(new AbstractRenderer<RoomProxy>() {

			@Override
			public String render(RoomProxy object) {
				if (object != null)
					return object.getRoomNumber();
				else
					return "";
			}
		});
	}
	
	private void initSpecialisationSuggestBox(List<SpecialisationProxy> response)
	{
		DefaultSuggestOracle<SpecialisationProxy> defaultSuggestOracle = (DefaultSuggestOracle<SpecialisationProxy>) manualOsceCreateOscePostPopUpView.getSpecialisationSuggestBox().getSuggestOracle();
		defaultSuggestOracle.setPossiblilities(response);
		manualOsceCreateOscePostPopUpView.getSpecialisationSuggestBox().setSuggestOracle(defaultSuggestOracle);
		manualOsceCreateOscePostPopUpView.getSpecialisationSuggestBox().setRenderer(new AbstractRenderer<SpecialisationProxy>() {

			@Override
			public String render(SpecialisationProxy object) {
				if (object != null)
					return object.getName();
				else
					return "";
			}
		});
	}

	@Override
	public void specialisationSuggestBoxChanged(SpecialisationProxy specialisationProxy) {
		if (manualOsceCreateOscePostPopUpView != null && specialisationProxy.getRoleTopics() != null)
		{
			DefaultSuggestOracle<RoleTopicProxy> suggestOracle = (DefaultSuggestOracle<RoleTopicProxy>) manualOsceCreateOscePostPopUpView.getRoleTopicSuggestBox().getSuggestOracle();
			suggestOracle.setPossiblilities(new ArrayList<RoleTopicProxy>(specialisationProxy.getRoleTopics()));
			manualOsceCreateOscePostPopUpView.getRoleTopicSuggestBox().setSuggestOracle(suggestOracle);
			manualOsceCreateOscePostPopUpView.getRoleTopicSuggestBox().setRenderer(new AbstractRenderer<RoleTopicProxy>() {

				@Override
				public String render(RoleTopicProxy object) {
					if (object != null)
						return object.getName();
					else
						return "";
				}
			});
		}
	}

	@Override
	public void roleTopicSuggestBoxChanged(RoleTopicProxy roleTopicProxy) {
		if (manualOsceCreateOscePostPopUpView != null && roleTopicProxy.getStandardizedRoles() != null)
		{
			DefaultSuggestOracle<StandardizedRoleProxy> suggestOracle = (DefaultSuggestOracle<StandardizedRoleProxy>) manualOsceCreateOscePostPopUpView.getStandardizedRoleSuggestBox().getSuggestOracle();
			suggestOracle.setPossiblilities(roleTopicProxy.getStandardizedRoles());
			manualOsceCreateOscePostPopUpView.getStandardizedRoleSuggestBox().setSuggestOracle(suggestOracle);
			manualOsceCreateOscePostPopUpView.getStandardizedRoleSuggestBox().setRenderer(new AbstractRenderer<StandardizedRoleProxy>() {

				@Override
				public String render(StandardizedRoleProxy object) {
					if (object != null)
						return object.getShortName();
					else
						return "";
				}
			});			
		}
	}

	@Override
	public void saveOscePost(SpecialisationProxy specialisationProxy, RoleTopicProxy roleTopicProxy, StandardizedRoleProxy standardizedRoleProxy, RoomProxy roomProxy, final CourseProxy courseProxy, PostType postType) {
		Long standardizedRoleId = null;
		Long roomId = null;
				
		if (standardizedRoleProxy != null)
			standardizedRoleId = standardizedRoleProxy.getId();
		
		if (roomProxy != null)
			roomId = roomProxy.getId();
		
		requests.oscePostRequestNonRoo().createOscePostBluePrintOscePostAndOscePostRoom(specialisationProxy.getId(), roleTopicProxy.getId(), standardizedRoleId, roomId, courseProxy.getId(), postType).with("course", "room", "oscePost", "oscePost.oscePostBlueprint", "oscePost.standardizedRole", "oscePost.standardizedRole", "oscePost.oscePostBlueprint.roleTopic", "oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

			@Override
			public void onSuccess(List<OscePostRoomProxy> response) {
				refreshParcourPanel(response);
			}
		});
	}
	
	private void refreshParcourPanel(List<OscePostRoomProxy> response) {
		for (OscePostRoomProxy oscePostRoomProxy : response)
		{
			if (oscePostRoomProxy.getCourse() != null)
			{
				ManualOsceParcourView manualOsceParcourView = courseViewMap.get(oscePostRoomProxy.getCourse().getId());
				if (manualOsceParcourView.getPostTypeListBox() != null)
					manualOsceParcourView.getPostTypeListBox().setValue(null);
				
				createOscePostView(oscePostRoomProxy, manualOsceParcourView, oscePostRoomProxy.getCourse());
			}
		}
	}

	@Override
	public void deleteOscePost(OscePostRoomProxy oscePostRoomProxy, CourseProxy courseProxy) {
		if (oscePostRoomProxy != null && courseProxy != null)
		{
			final Long oscePostRoomId = oscePostRoomProxy.getId();
			showApplicationLoading(true);
			
			requests.oscePostRoomRequestNonRoo().deleteOscePostRoom(oscePostRoomId).with("courses").fire(new OSCEReceiver<List<OsceSequenceProxy>>() {

				@Override
				public void onSuccess(List<OsceSequenceProxy> response) {
					for (OsceSequenceProxy osceSequenceProxy : response)
					{
						ManualOsceSequenceParcourViewImpl manualOsceSequenceParcourViewImpl = osceSequenceParcourViewMap.get(osceSequenceProxy.getId());
						if (manualOsceSequenceParcourViewImpl != null)
						{
							for (CourseProxy courseProxy : osceSequenceProxy.getCourses())
							{
								courseViewMap.remove(courseProxy.getId());
							}
							manualOsceSequenceParcourViewImpl.getParcourPanel().clear();
							
							initParcourView(osceSequenceProxy, manualOsceSequenceParcourViewImpl);
						}
					}
					showApplicationLoading(false);
				}
			});
		}
	}

	@Override
	public void deleteParcour(final OsceSequenceProxy osceSequenceProxy, final CourseProxy courseProxy) {
		if (courseProxy != null && osceSequenceProxy != null)
		{
			showApplicationLoading(true);
			requests.courseRequestNonRoo().deleteCourse(courseProxy.getId()).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					ManualOsceParcourView manualOsceParcourView = courseViewMap.get(courseProxy.getId());
					ManualOsceSequenceParcourViewImpl manualOsceSequenceViewImpl = osceSequenceParcourViewMap.get(osceSequenceProxy.getId());
					manualOsceSequenceViewImpl.getParcourPanel().remove(manualOsceParcourView);
					
					courseViewMap.remove(courseProxy.getId());
					
					if (manualOsceSequenceViewImpl.getParcourPanel().getWidgetCount() == 1)
					{
						Widget widget = manualOsceSequenceViewImpl.getParcourPanel().getWidget(0);
						if (widget instanceof ManualOsceParcourView)
						{
							((ManualOsceParcourView)widget).removeDeleteButton();
						}
					}
					
					showApplicationLoading(false);
				}
			});
		}		
	}

	@Override
	public void deleteOsceSequenceClicked(final OsceSequenceProxy osceSequenceProxy, final OsceDayProxy osceDayProxy) {
		if (osceSequenceProxy != null && osceDayProxy != null)
		{
			showApplicationLoading(true);
			requests.osceSequenceRequestNonRoo().removeOsceSequence(osceSequenceProxy.getId()).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					ManualOsceSequenceViewImpl manualOsceSequenceViewImpl = osceSequenceViewMap.get(osceDayProxy.getId());
					ManualOsceSequenceParcourViewImpl manualOsceSequenceParcourViewImpl = osceSequenceParcourViewMap.get(osceSequenceProxy.getId());
					manualOsceSequenceViewImpl.getSecondOsceSequencePanel().remove(manualOsceSequenceParcourViewImpl);
					
					manualOsceSequenceViewImpl.getAddSequencePanel().getElement().getStyle().clearDisplay();
					
					osceSequenceParcourViewMap.remove(osceSequenceProxy.getId());
					
					showApplicationLoading(false);
				}
			});
		}	
	}

	@Override
	public void deleteCourse(HeaderView view) {
	}

	@Override
	public void createEditStandardizedRolePopup(RoleTopicProxy roleTopicProxy, final StandardizedRoleProxy standardizedRoleProxy, final IconButton editStandardizedRole, final OscePostRoomProxy oscePostRoomProxy, final OscePostProxy oscePostProxy) {
		showApplicationLoading(true);
		requests.standardizedRoleRequestNonRoo().findRoleByRoleTopic(roleTopicProxy.getId()).fire(new OSCEReceiver<List<StandardizedRoleProxy>>() {

			@Override
			public void onSuccess(List<StandardizedRoleProxy> response) {
				showApplicationLoading(false);
				ManualOscePopupView manualOscePopupView = new ManualOscePopupViewImpl();
				manualOscePopupView.createStandardizedRolePopup(response);
				manualOscePopupView.setOscePostProxy(oscePostProxy);
				manualOscePopupView.setOscePostRoomProxy(oscePostRoomProxy);
				manualOscePopupView.setDelegate(ManualOsceDetailsActivity.this);
				
				if (standardizedRoleProxy != null)
					manualOscePopupView.getStandardizedRoleSuggestBox().setSelected(standardizedRoleProxy);
			
				((ManualOscePopupViewImpl)manualOscePopupView).showRelativeTo(editStandardizedRole);
				
				int width = ((ManualOscePopupViewImpl)manualOscePopupView).getOffsetWidth() - editStandardizedRole.getOffsetWidth();
				int left = ((ManualOscePopupViewImpl)manualOscePopupView).getAbsoluteLeft();
				int top = ((ManualOscePopupViewImpl)manualOscePopupView).getAbsoluteTop() - editStandardizedRole.getOffsetHeight();
				
				if (left == editStandardizedRole.getAbsoluteLeft())
					((ManualOscePopupViewImpl)manualOscePopupView).setPopupPosition((left-width), top);
				else
					((ManualOscePopupViewImpl)manualOscePopupView).setPopupPosition(left, top);
			}
		});
	}

	@Override
	public void saveStandardizedRole(final OscePostRoomProxy oscePostRoomProxy, OscePostProxy oscePostProxy, StandardizedRoleProxy selectedStandardizedRoleProxy) {
		if (oscePostProxy != null)
		{
			showApplicationLoading(true);
			OscePostRequest oscePostRequest = requests.oscePostRequest();
			oscePostProxy = oscePostRequest.edit(oscePostProxy);
			oscePostProxy.setStandardizedRole(selectedStandardizedRoleProxy);
			final OscePostProxy oscePostProxy2 = oscePostProxy;
			oscePostRequest.persist().using(oscePostProxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					
					requests.oscePostRoomRequestNonRoo().findOscePostRoomByOscePostId(oscePostProxy2.getId()).with("room", "oscePost", "oscePost.oscePostBlueprint", "oscePost.standardizedRole", "oscePost.standardizedRole", "oscePost.oscePostBlueprint.roleTopic", "oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

						@Override
						public void onSuccess(List<OscePostRoomProxy> response) {
							for (OscePostRoomProxy oprProxy : response)
							{
								if (oscePostViewMap.containsKey(oprProxy.getId()))
								{
									ManualOscePostSubView manualOscePostSubView = oscePostViewMap.get(oprProxy.getId());
									manualOscePostSubView.setOscePostRoomProxy(oprProxy);
									if (oprProxy.getOscePost() != null)
										manualOscePostSubView.setOscePostProxy(oprProxy.getOscePost());
									else
										manualOscePostSubView.setOscePostProxy(oscePostProxy2);
								}
							}
							showApplicationLoading(false);
						}
					});
				}
			});
		}
	}

	@Override
	public void createEditRoomPopup(final IconButton editRoom, final OscePostRoomProxy oscePostRoomProxy, final RoomProxy roomProxy) {
		showApplicationLoading(true);
		requests.roomRequestNonRoo().findAllRoomsOrderByRoomNumber().fire(new OSCEReceiver<List<RoomProxy>>() {

			@Override
			public void onSuccess(List<RoomProxy> response) {
				showApplicationLoading(false);
				ManualOscePopupView manualOscePopupView = new ManualOscePopupViewImpl();
				manualOscePopupView.createRoomPopup(response);
				manualOscePopupView.setOscePostRoomProxy(oscePostRoomProxy);
				manualOscePopupView.setDelegate(ManualOsceDetailsActivity.this);
				
				if (roomProxy != null)
					manualOscePopupView.getRoomSuggestBox().setSelected(roomProxy);
			
				((ManualOscePopupViewImpl)manualOscePopupView).showRelativeTo(editRoom);
				
				int width = ((ManualOscePopupViewImpl)manualOscePopupView).getOffsetWidth() - editRoom.getOffsetWidth();
				int left = ((ManualOscePopupViewImpl)manualOscePopupView).getAbsoluteLeft();
				int top = ((ManualOscePopupViewImpl)manualOscePopupView).getAbsoluteTop() - editRoom.getOffsetHeight();
				
				if (left == editRoom.getAbsoluteLeft())
					((ManualOscePopupViewImpl)manualOscePopupView).setPopupPosition((left-width), top);
				else
					((ManualOscePopupViewImpl)manualOscePopupView).setPopupPosition(left, top);
			}
		});
	}

	@Override
	public void saveRoom(final OscePostRoomProxy oscePostRoomProxy, final RoomProxy selectedRoomProxy) {
		if (oscePostRoomProxy != null && selectedRoomProxy != null)
		{
			showApplicationLoading(true);
			requests.oscePostRoomRequestNonRoo().findOscePostRoomListByRoomAndOscePostRoomId(oscePostRoomProxy.getId(), selectedRoomProxy.getId()).fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

				@Override
				public void onSuccess(List<OscePostRoomProxy> response) {
					showApplicationLoading(false);
					if (response.size() <= 0)
					{
						requests.oscePostRoomRequestNonRoo().insertRoomForVerticalOscePost(oscePostRoomProxy.getId(), selectedRoomProxy.getId()).with("room", "oscePost", "oscePost.oscePostBlueprint", "oscePost.standardizedRole", "oscePost.standardizedRole", "oscePost.oscePostBlueprint.roleTopic", "oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

							@Override
							public void onSuccess(List<OscePostRoomProxy> oscePostRoomList) {
								for (OscePostRoomProxy oprProxy : oscePostRoomList)
								{
									if (oscePostViewMap.containsKey(oprProxy.getId()))
									{
										ManualOscePostSubView manualOscePostSubView = oscePostViewMap.get(oprProxy.getId());
										manualOscePostSubView.setOscePostRoomProxy(oprProxy);
										manualOscePostSubView.setRoomProxy(oprProxy.getRoom());
									}
								}
							}
						});
					}
					else
					{
						final MessageConfirmationDialogBox dialog=new MessageConfirmationDialogBox(constants.warning());
						dialog.showYesNoDialog(constants.roomAlreadyAssignWantToExcahnge());
						dialog.getYesBtn().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) 
							{
								dialog.hide();
								
								requests.oscePostRoomRequestNonRoo().replaceRoomAndAssignRoomVertically(oscePostRoomProxy.getId(), selectedRoomProxy.getId()).with("room", "oscePost", "oscePost.oscePostBlueprint", "oscePost.standardizedRole", "oscePost.standardizedRole", "oscePost.oscePostBlueprint.roleTopic", "oscePost.oscePostBlueprint.specialisation").fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

									@Override
									public void onSuccess(List<OscePostRoomProxy> oscePostRoomList) {
										for (OscePostRoomProxy oprProxy : oscePostRoomList)
										{
											if (oscePostViewMap.containsKey(oprProxy.getId()))
											{
												ManualOscePostSubView manualOscePostSubView = oscePostViewMap.get(oprProxy.getId());
												manualOscePostSubView.setOscePostRoomProxy(oprProxy);
												manualOscePostSubView.setRoomProxy(oprProxy.getRoom());
											}
										}
									}
								});
							}
							
						});
						
						dialog.getNoBtnl().addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) 
							{
								dialog.hide();
							}
						});
					}
				}
			});
		}
	}

	@Override
	public void deleteOsceDayClicked(final ManualOsceDaySubViewImpl manualOsceDaySubViewImpl, final OsceDayProxy osceDayProxy) {
		showApplicationLoading(true);
		requests.osceDayRequestNooRoo().removeOsceDay(osceDayProxy.getId()).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				showApplicationLoading(false);
				osceDayViewMap.remove(osceDayProxy.getId());
				manualOsceDaySubViewImpl.removeFromParent();
			}
		});
	}

	@Override
	public void saveOsceDayClicked(OsceDayProxy osceDayProxy, Date osceDate, Date osceStartTime) {
		showApplicationLoading(true);
		OsceDayRequest osceDayRequest = requests.osceDayRequest();
		osceDayProxy = osceDayRequest.edit(osceDayProxy);
		osceDayProxy.setOsceDate(osceDate);
		osceDayProxy.setTimeStart(osceStartTime);
		osceDayRequest.persist().using(osceDayProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				showApplicationLoading(false);
			}
		});
	}

	@Override
	public void changeLunchBreak(final String value) {
		if (this.osceProxy != null)
		{
			showApplicationLoading(true);
			OsceRequest osceRequest = requests.osceRequest();
			osceProxy = osceRequest.edit(osceProxy);
			osceProxy.setLunchBreak(Short.parseShort(value));
			osceRequest.persist().using(osceProxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					showApplicationLoading(false);
					if (osceSequenceViewMap.keySet().size() > 1)
					{
						for (Long key : osceSequenceViewMap.keySet())
						{
							ManualOsceSequenceViewImpl manualOsceSequenceViewImpl = osceSequenceViewMap.get(key);
							manualOsceSequenceViewImpl.getManualOscelunchBreakViewImpl().getLunchBreakDuration().setValue(value);
						}
					}
				}
			});
		}
	}

	@Override
	public void changeOsceSequenceLabel(String sequenceName, OsceSequenceProxy osceSequenceProxy) {
		OsceSequenceRequest osceSequenceRequest = requests.osceSequenceRequest();
		osceSequenceProxy = osceSequenceRequest.edit(osceSequenceProxy);
		osceSequenceProxy.setLabel(sequenceName);
		osceSequenceRequest.persist().using(osceSequenceProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				
			}
		});
	}

	@Override
	public void saveOsceClicked(OsceProxy osceProxy, ManualOsceEditViewImpl manualOsceEditViewImpl) {
		OsceRequest osceRequest = requests.osceRequest();
		osceProxy = osceRequest.edit(osceProxy);
		
		manualOsceEditViewImpl.setValueInOsceProxy(osceProxy);
		
		this.osceProxy = osceProxy;
		
		osceRequest.persist().using(osceProxy).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
			}
		});
	}

	@Override
	public void fixedButtonClicked(OsceProxy osceProxy, final ManualOsceEditViewImpl manualOsceEditViewImpl) {
		showApplicationLoading(true);
		requests.osceRequestNonRoo().changeOsceStatus(osceProxy.getId(), OsceStatus.OSCE_FIXED).with("osce_days", "osce_days.osceSequences", "osce_days.osceDayRotations", "osce_days.osceSequences.courses").fire(new OSCEReceiver<OsceProxy>() {

			@Override
			public void onSuccess(OsceProxy response) {
				showApplicationLoading(false);
				if (response != null)
				{
					ManualOsceDetailsActivity.this.osceProxy = response;
					
					changeOsceViewByOsceStatus(response);
					manualOsceEditViewImpl.changeButtonByOsceStatus(OsceStatus.OSCE_FIXED);
					manualOsceEditViewImpl.disableEnableTextBox(false);
				}			
				else
				{
					MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
					dialogBox.showConfirmationDialog(constants.manualOsceCalculateError());
				}
			}
		});
	}
	
	public void changeOsceViewByOsceStatus(OsceProxy osceProxy)
	{
		final OsceStatus osceStatus = osceProxy.getOsceStatus();
		
		if (manualOsceSubViewImpl != null)
		{
			manualOsceSubViewImpl.getAddOsceDayVerticalPanel().getElement().getStyle().setDisplay(Display.NONE);
		}
		
		List<OsceDayProxy> osceDayProxyList = osceProxy.getOsce_days();
		if (osceDayProxyList != null && osceDayProxyList.isEmpty() == false)
		{
			for (OsceDayProxy osceDayProxy : osceDayProxyList)
			{
				if (osceDayViewMap.containsKey(osceDayProxy.getId()))
				{
					ManualOsceDaySubViewImpl manualOsceDaySubViewImpl = osceDayViewMap.get(osceDayProxy.getId());
					manualOsceDaySubViewImpl.getSaveOsceDayValue().getElement().getStyle().setDisplay(Display.NONE);
					manualOsceDaySubViewImpl.getDeleteOsceDay().getElement().getStyle().setDisplay(Display.NONE);
				}
				
				if (osceSequenceViewMap.containsKey(osceDayProxy.getId()))
				{
					ManualOsceSequenceViewImpl manualOsceSequenceViewImpl = osceSequenceViewMap.get(osceDayProxy.getId());
					manualOsceSequenceViewImpl.getAddSequencePanel().getElement().getStyle().setDisplay(Display.NONE);
					manualOsceSequenceViewImpl.getManualOscelunchBreakViewImpl().getLunchBreakDuration().setReadOnly(true);
					manualOsceSequenceViewImpl.getManualOscelunchBreakViewImpl().getLunchBreakDuration().setEnabled(false);
				}
				
				List<OsceSequenceProxy> osceSeqProxyList = osceDayProxy.getOsceSequences();
				
				if (osceSeqProxyList != null && osceSeqProxyList.isEmpty() == false)
				{
					for (OsceSequenceProxy osceSequenceProxy : osceSeqProxyList)
					{
						if (osceSequenceParcourViewMap.containsKey(osceSequenceProxy.getId()))
						{
							ManualOsceSequenceParcourViewImpl manualOsceSequenceParcourViewImpl = osceSequenceParcourViewMap.get(osceSequenceProxy.getId());
							manualOsceSequenceParcourViewImpl.getAddParcourVerticalPanel().getElement().getStyle().setDisplay(Display.NONE);
							manualOsceSequenceParcourViewImpl.getDeleteOsceSeqBtn().getElement().getStyle().setDisplay(Display.NONE);
							manualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getEditSequence().getElement().getStyle().setDisplay(Display.NONE);
							manualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getBreakSooner().getElement().getStyle().setDisplay(Display.NONE);
							manualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getBreakLater().getElement().getStyle().setDisplay(Display.NONE);
							manualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getAddRotation().getElement().getStyle().setDisplay(Display.NONE);
							manualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRemoveRotation().getElement().getStyle().setDisplay(Display.NONE);
							manualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRotationNumber().setReadOnly(true);
							manualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().getRotationNumber().setEnabled(false);
						}
						
						List<CourseProxy> courseProxyList = osceSequenceProxy.getCourses();
						
						if (courseProxyList != null && courseProxyList.isEmpty() == false)
						{
							for (CourseProxy courseProxy : courseProxyList)
							{
								if (courseViewMap.containsKey(courseProxy.getId()))
								{
									ManualOsceParcourView manualOsceParcourView = courseViewMap.get(courseProxy.getId());
									manualOsceParcourView.getAddOscePostVerticalPanel().getElement().getStyle().setDisplay(Display.NONE);
									manualOsceParcourView.getDeleteButton().getElement().getStyle().setDisplay(Display.NONE);
								}
								
								
								requests.oscePostRoomRequestNonRoo().findOscePostRoomByCourseIdOrderByOscePostSeqNo(courseProxy.getId()).fire(new OSCEReceiver<List<OscePostRoomProxy>>() {

									@Override
									public void onSuccess(List<OscePostRoomProxy> response) {
										if (response != null && response.isEmpty() == false)
										{
											for (OscePostRoomProxy oscePostRoomProxy : response)
											{
												if (oscePostViewMap.containsKey(oscePostRoomProxy.getId()))
												{
													ManualOscePostSubView manualOscePostSubView = oscePostViewMap.get(oscePostRoomProxy.getId());
												
													if (OsceStatus.OSCE_FIXED.equals(osceStatus) || OsceStatus.OSCE_CLOSED.equals(osceStatus))
													{
														manualOscePostSubView.getDeletePost().getElement().getStyle().setDisplay(Display.NONE);
													}
													
													if (OsceStatus.OSCE_CLOSED.equals(osceStatus))
													{
														manualOscePostSubView.getEditRoom().getElement().getStyle().setDisplay(Display.NONE);
														manualOscePostSubView.getEditStandardizedRole().getElement().getStyle().setDisplay(Display.NONE);
													}
													else
													{
														manualOscePostSubView.getEditRoom().getElement().getStyle().clearDisplay();
														manualOscePostSubView.getEditStandardizedRole().getElement().getStyle().clearDisplay();
													}
												}
											}
										}
									}
								});		
								
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void clearAllButtonClicked(OsceProxy osceProxy, final ManualOsceEditViewImpl manualOsceEditViewImpl) {
		if (osceProxy != null)
		{
			showApplicationLoading(true);
			requests.osceRequestNonRoo().clearAllManualOsce(osceProxy.getId()).with("osce_days", "osce_days.osceSequences", "osce_days.osceSequences.osceDayRotations", "osce_days.osceSequences.courses").fire(new OSCEReceiver<OsceProxy>() {

				@Override
				public void onSuccess(OsceProxy response) {
					manualOsceEditViewImpl.changeButtonByOsceStatus(OsceStatus.OSCE_NEW);
					manualOsceEditViewImpl.disableEnableTextBox(true);
					showApplicationLoading(false);
					
					if (response != null)
					{
						osceDayViewMap.clear();
						osceSequenceViewMap.clear();
						osceSequenceParcourViewMap.clear();
						courseViewMap.clear();
						oscePostViewMap.clear();
						
						ManualOsceDetailsActivity.this.osceProxy = response;
						view.setOsceProxy(response);
						
						if (manualOsceSubViewImpl != null)
						{
							manualOsceSubViewImpl.getManualOsceEditViewImpl().setDelegate(ManualOsceDetailsActivity.this);
							manualOsceSubViewImpl.getManualOsceEditViewImpl().setOsceProxy(response);
						}
						
						manualOsceSubViewImpl.getOsceDayVp().clear();
						initOsceDayView();
					}
				}
			});
		}
	}

	@Override
	public void closeButtonClicked(final OsceProxy osceProxy, final ManualOsceEditViewImpl manualOsceEditViewImpl) {		
		if (osceProxy != null)
		{
			showApplicationLoading(true);
			requests.osceRequestNonRoo().createAssignmentInManualOsce(osceProxy.getId()).fire(new OSCEReceiver<String>() {

				@Override
				public void onSuccess(String response) {
										
					showApplicationLoading(false);
					if (response.isEmpty())
					{
						requests.osceRequest().findOsce(osceProxy.getId()).with("osce_days", "osce_days.osceSequences", "osce_days.osceDayRotations", "osce_days.osceSequences.courses").fire(new OSCEReceiver<OsceProxy>() {

							@Override
							public void onSuccess(OsceProxy osceProxyResponse) {
								manualOsceEditViewImpl.changeButtonByOsceStatus(OsceStatus.OSCE_CLOSED);
								changeOsceViewByOsceStatus(osceProxyResponse);
							}
						});
					}
					else
					{
						MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(constants.error());
						dialogBox.showConfirmationDialog(constantsLookup.getString(response));
					}
				}
			});
		}
	}

	@Override
	public void reopenButtonClicked(OsceProxy osceProxy, final ManualOsceEditViewImpl manualOsceEditViewImpl) {
		if (osceProxy != null)
		{
			showApplicationLoading(true);
			requests.assignmentRequestNonRoo().removeManualOsceAssignmentByOsceId(osceProxy.getId()).with("osce_days", "osce_days.osceSequences", "osce_days.osceDayRotations", "osce_days.osceSequences.courses").fire(new OSCEReceiver<OsceProxy>() {

				@Override
				public void onSuccess(OsceProxy response) {
					manualOsceEditViewImpl.changeButtonByOsceStatus(OsceStatus.OSCE_FIXED);
					
					showApplicationLoading(false);
					if (response != null)
					{
						changeOsceViewByOsceStatus(response);
					}
				}
			});
		}
	}
	
	@Override
	public void calculateButtonClicked(final OsceProxy osceProxy)
	{
		showApplicationLoading(true);
		requests.osceRequestNonRoo().calculateManualOsce(osceProxy.getId()).fire(new OSCEReceiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				showApplicationLoading(false);
				refreshOsceView(osceProxy.getId());
			}
		});
	}
	
	private void refreshOsceView(Long osceId) {
		showApplicationLoading(true);
		requests.osceRequest().findOsce(osceId).with("osce_days", "osce_days.osceSequences", "osce_days.osceSequences.osceDayRotations", "osce_days.osceSequences.courses").fire(new OSCEReceiver<OsceProxy>() {

			@Override
			public void onSuccess(OsceProxy response) {
				osceProxy = response;
				view.setOsceProxy(osceProxy);
				List<OsceDayProxy> osceDayList = osceProxy.getOsce_days();
				if (osceDayList != null)
				{
					for (OsceDayProxy osceDayProxy : osceDayList)
					{
						if (osceDayViewMap.containsKey(osceDayProxy.getId()))
						{
							ManualOsceDaySubViewImpl manualOsceDaySubViewImpl = osceDayViewMap.get(osceDayProxy.getId());
							manualOsceDaySubViewImpl.setOsceDayProxy(osceDayProxy);
						}
						
						List<OsceSequenceProxy> osceSeqProxyList = osceDayProxy.getOsceSequences();
						
						for (OsceSequenceProxy osceSequenceProxy : osceSeqProxyList)
						{
							if (osceSequenceParcourViewMap.containsKey(osceSequenceProxy.getId()))
							{
								ManualOsceSequenceParcourViewImpl manualOsceSequenceParcourViewImpl = osceSequenceParcourViewMap.get(osceSequenceProxy.getId());
								manualOsceSequenceParcourViewImpl.setOsceSequenceProxy(osceSequenceProxy);
								manualOsceSequenceParcourViewImpl.setOsceDayProxy(osceDayProxy);
								manualOsceSequenceParcourViewImpl.getManualOsceChangeBreakViewImpl().setOsceSequenceProxy(osceSequenceProxy);
							}
						}
					}
				}
				showApplicationLoading(false);
			}
		});
	}
	
	public void showApplicationLoading(Boolean show) {
		requests.getEventBus().fireEvent(new ApplicationLoadingScreenEvent(show));
	}

	@Override
	public void breakSoonerClicked(final OsceSequenceProxy osceSequenceProxy) {
		if (osceSequenceProxy != null)
		{
			showApplicationLoading(true);
			requests.osceSequenceRequestNonRoo().manualOsceBreakSooner(osceSequenceProxy.getId()).fire(new OSCEReceiver<String>() {

				@Override
				public void onSuccess(String response) {
					if (response != null && response.isEmpty() == false)
					{
						showApplicationLoading(false);
						MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constantsLookup.getString(response));
					}
					else 
					{
						requests.osceDayRequestNooRoo().findOsceDayByOsceSequenceId(osceSequenceProxy.getId()).with("osceSequences", "osceSequences.osceDayRotations", "osceSequences.courses").fire(new OSCEReceiver<OsceDayProxy>() {

							@Override
							public void onSuccess(OsceDayProxy response) {
								showApplicationLoading(false);
								if (response != null)
								{
									if (osceDayViewMap.containsKey(response.getId()))
									{
										ManualOsceDaySubViewImpl manualOsceDaySubViewImpl = osceDayViewMap.get(response.getId());
										manualOsceDaySubViewImpl.setOsceDayProxy(response);
									}
								}
							}
						});
					}
				}
			});
		}
	}

	@Override
	public void breakLaterClicked(final OsceSequenceProxy osceSequenceProxy) {
		if (osceSequenceProxy != null)
		{
			showApplicationLoading(true);
			requests.osceSequenceRequestNonRoo().manualOsceBreakLater(osceSequenceProxy.getId()).fire(new OSCEReceiver<String>() {

				@Override
				public void onSuccess(String response) {
					if (response != null && response.isEmpty() == false)
					{
						showApplicationLoading(false);
						MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						confirmationDialogBox.showConfirmationDialog(constantsLookup.getString(response));
					}
					else 
					{
						requests.osceDayRequestNooRoo().findOsceDayByOsceSequenceId(osceSequenceProxy.getId()).with("osceSequences", "osceSequences.osceDayRotations", "osceSequences.courses").fire(new OSCEReceiver<OsceDayProxy>() {

							@Override
							public void onSuccess(OsceDayProxy response) {
								showApplicationLoading(false);
								if (response != null)
								{
									if (osceDayViewMap.containsKey(response.getId()))
									{
										ManualOsceDaySubViewImpl manualOsceDaySubViewImpl = osceDayViewMap.get(response.getId());
										manualOsceDaySubViewImpl.setOsceDayProxy(response);
									}
								}
							}
						});
					}
				}
			});
		}
	}
	
	@Override
	public void changeRotationNumber(OsceSequenceProxy osceSequenceProxy, int noOfRotation, final ManualOsceChangeBreakViewImpl manualOsceChangeBreakViewImpl)
	{
		if (osceSequenceProxy != null && noOfRotation > 0 && manualOsceChangeBreakViewImpl != null)
		{
			showApplicationLoading(true);
			
			OsceSequenceRequest osceSequenceRequest = requests.osceSequenceRequest();
			osceSequenceProxy = osceSequenceRequest.edit(osceSequenceProxy);
			osceSequenceProxy.setNumberRotation(noOfRotation);
			final OsceSequenceProxy osceSequenceProxy2 = osceSequenceProxy;
			osceSequenceRequest.persist().using(osceSequenceProxy).fire(new OSCEReceiver<Void>() {

				@Override
				public void onSuccess(Void response) {
					manualOsceChangeBreakViewImpl.setOsceSequenceProxy(osceSequenceProxy2);
					showApplicationLoading(false);
				}
			});
		}
	}
}
