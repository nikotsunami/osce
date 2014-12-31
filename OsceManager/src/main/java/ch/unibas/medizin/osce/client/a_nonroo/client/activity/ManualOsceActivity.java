package ch.unibas.medizin.osce.client.a_nonroo.client.activity;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.place.ManualOsceDetailsPlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.place.ManualOscePlace;
import ch.unibas.medizin.osce.client.a_nonroo.client.receiver.OSCEReceiver;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceView;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.manualosce.ManualOsceViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.SelectChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.Operation;
import ch.unibas.medizin.osce.shared.OsceCreationType;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimplePanel;

@SuppressWarnings("deprecation")
public class ManualOsceActivity extends AbstractActivity implements ManualOsceView.Delegate {

	private final OsMaRequestFactory requests;
	private final PlaceController placeController;
	private AcceptsOneWidget widget;
	private EventBus eventBus;
	public ManualOsceView view;
	private SemesterProxy semesterProxy;
	private HandlerManager handlerManager;
	private SelectChangeHandler removeHandler;
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private ManualOsceDetailsActivityMapper manualOsceDetailsActivityMapper;
	private ActivityManager activityManager;
	private List<OsceProxy> osceProxyList;

	public ManualOsceActivity(OsMaRequestFactory requests, PlaceController placeController, ManualOscePlace place) {
		this.requests = requests;
		this.placeController = placeController;
		semesterProxy = place.semesterProxy;
		handlerManager = place.handlerManager;
		
		manualOsceDetailsActivityMapper = new ManualOsceDetailsActivityMapper(requests, placeController);
		activityManager = new ActivityManager(manualOsceDetailsActivityMapper, requests.getEventBus());
    }
	
	
	@Override
	public void start(AcceptsOneWidget widget, EventBus eventBus) {
		this.widget = widget;
		this.eventBus = eventBus;
		
		this.addSelectChangeHandler(new SelectChangeHandler() {
			
			@Override
			public void onSelectionChange(SelectChangeEvent event) {
				System.out.println("SELECTED ID : " + event.getSemesterProxy().getId());
				semesterProxy = event.getSemesterProxy();
				init();
			}
		});
		
		osceProxyList = new ArrayList<OsceProxy>();
		
		init();
	}
	
	public void init()
	{
		ManualOsceView view = new ManualOsceViewImpl();
		this.view = view;
		this.widget.setWidget(view.asWidget());
		view.setDelegate(this);
		
		MenuClickEvent.register(requests.getEventBus(), (ManualOsceViewImpl)view);
		
		initOsceTab();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onStop() {
		activityManager.setDisplay(null);
		handlerManager.removeHandler(SelectChangeEvent.getType(),removeHandler);
	}
	
	public void initOsceTab()
	{
		view.getOsceTabPanel().addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {				
				SimplePanel simplePanel = (SimplePanel)view.getOsceTabPanel().getWidget(event.getSelectedItem());
				activityManager.setDisplay(simplePanel);
				if (osceProxyList != null && event.getSelectedItem() <= osceProxyList.size())
				{
					goTo(new ManualOsceDetailsPlace(osceProxyList.get(event.getSelectedItem()).stableId(),Operation.DETAILS));
				}
			}
		});
		
		requests.osceRequest().findAllOsceBySemesterIdAndCreationType(semesterProxy.getId(), OsceCreationType.Manual).with("semester").fire(new OSCEReceiver<List<OsceProxy>>() {

			@Override
			public void onSuccess(List<OsceProxy> response) {
				int tabIndex = 0;
				osceProxyList = response;
				
				if (response.size() == 0)
					view.getMainHTMLPanel().getElement().getStyle().setDisplay(Display.NONE);
				else
					view.getMainHTMLPanel().getElement().getStyle().clearDisplay();
				
				for (OsceProxy osceProxy : response)
				{
					String osceLable = new EnumRenderer<StudyYears>().render(osceProxy.getStudyYear()) + "." + new EnumRenderer<Semesters>().render(osceProxy.getSemester().getSemester());
					osceLable = osceLable + ((osceProxy.getIsRepeOsce() == true) ? " (" + constants.repe() + ")" : "");
					view.getOsceTabPanel().insert(new SimplePanel(), osceLable, tabIndex);
					tabIndex += 1;					
				}
				
				if (response.size() > 0)
					view.getOsceTabPanel().selectTab(0);
					
			}
		});
		
	}
	
	/*private void initOsceView(Integer selectedTab) {
		Widget widget = view.getOsceTabPanel().getTabWidget(selectedTab);
		
		if (widget != null && widget instanceof SimplePanel)
		{
			OsceDayViewImpl osceDayView = new OsceDayViewImpl();
			SimplePanel simplePanel = (SimplePanel) widget;
			simplePanel.add(new Label("Selected Tab : " + selectedTab));
		}
	}*/

	@SuppressWarnings("unchecked")
	public void addSelectChangeHandler(SelectChangeHandler handler) 
	{
		handlerManager.addHandler(SelectChangeEvent.getType(), handler);	
		removeHandler = handler;
	}

	@Override
	public void goTo(Place place) {
		placeController.goTo(place);
	}
}
