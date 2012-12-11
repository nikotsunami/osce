/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.PatientInSemesterSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.PatientInSemesterSelectedHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleSelectedEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RoleSelectedHandler;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.PatientInSemesterProxy;
import ch.unibas.medizin.osce.client.style.widgets.DisclosurePanel;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.OSCESecurityStatus;
import ch.unibas.medizin.osce.shared.PatientAveragePerPost;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * @author spec
 *
 */
public class OsceDaySubViewImpl extends Composite implements OsceDaySubView, PatientInSemesterSelectedHandler,RoleSelectedHandler  {

	private static OsceDaySubViewUiBinder uiBinder = GWT
			.create(OsceDaySubViewUiBinder.class);

	interface OsceDaySubViewUiBinder extends UiBinder<Widget, OsceDaySubViewImpl> {
	}

	private Delegate delegate;
	
	private OsceDayProxy osceDayProxy;

	private OsceDaySubViewImpl osceDaySubViewImpl;
	
	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	public DisclosurePanel simpleDiscloserPanel;
	
	//Modlue 3: Assignemnt D[
	@UiField
	VerticalPanel sequenceVP;
	
	boolean isHeaderChangeEvent = false;
	boolean isPanelOpen = false;
	boolean fromOnClose=false,fromOnOpen=false;
	private OsceProxy osceProxy;
	
	
	public OsceProxy getOsceProxy() {
		return osceProxy;
	}

	public void setOsceProxy(OsceProxy osceProxy) {
		this.osceProxy = osceProxy;
	}

	FocusableValueListBox<PatientAveragePerPost> patientAvgPerPost=new FocusableValueListBox<PatientAveragePerPost>(new Renderer<PatientAveragePerPost>() {

		@Override
		public String render(PatientAveragePerPost object) {
			// TODO Auto-generated method stub
			// Manish changes return object == null ? "" : String.valueOf(object.getTypeId());
			return object == null ? null : String.valueOf(object.getTypeId());
		}

		@Override
		public void render(PatientAveragePerPost arg0, Appendable arg1)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	FocusableValueListBox<OSCESecurityStatus> patientSecurity = new FocusableValueListBox<OSCESecurityStatus>(new EnumRenderer<OSCESecurityStatus>());
	
//	public DefaultSuggestBox<PatientAveragePerPost, EventHandlingValueHolderItem<PatientAveragePerPost>> patientAvgPostSuggestBox = new DefaultSuggestBox<PatientAveragePerPost, EventHandlingValueHolderItem<PatientAveragePerPost>>();
//	public DefaultSuggestBox<OSCESecurityStatus, EventHandlingValueHolderItem<OSCESecurityStatus>> oSCESecurityStatusSuggestBox = new DefaultSuggestBox<OSCESecurityStatus, EventHandlingValueHolderItem<OSCESecurityStatus>>();
	
	public FocusableValueListBox<OSCESecurityStatus> getPatientSecurity() {
		return patientSecurity;
	}

	public VerticalPanel getSequenceVP() {
		return sequenceVP;
	}

	public void setSequenceVP(VerticalPanel sequenceVP) {
		this.sequenceVP = sequenceVP;
	}
	//Modlue 3: Assignemnt D]
	
	// Module 3 d {
	
		private PatientInSemesterProxy patientInSemesterProxy;
		
		//private HashMap<OsceDayProxy,OsceDaySubViewImpl> osceDayProxyAndViewMap = new HashMap<OsceDayProxy, OsceDaySubViewImpl>();
		
		private Set<OsceDayProxy> setOsceDayProxy;
		
		public PatientInSemesterProxy getPatientInSemesterProxy(){
			return this.patientInSemesterProxy;
		}
		
		public Set<OsceDayProxy> getSetOsceDayProxy(){
			return this.setOsceDayProxy;
		}
		
		public OsceDayProxy getOsceDayProxy(){
			return this.osceDayProxy;
		}
		// Module 3 d }
	
	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	
	private void onValueChangeSecurity(boolean isSecurityStatusChange) {

//		Log.info("temposceDayProxy "+ osceDayProxy.getId());
//		Log.info("temposceDayProxy "+ osceProxy);
		final OSCESecurityStatus osceSecurityStatus = patientSecurity.getValue();
		
		if (osceProxy != null && osceProxy.getSecurity() != null && osceSecurityStatus != null) {

			if (isSecurityStatusChange) {
				
				if (osceProxy.getSecurity().ordinal() != osceSecurityStatus
						.ordinal()) {
					final MessageConfirmationDialogBox dialogBox = new MessageConfirmationDialogBox(
							constants.warning());

					dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							delegate.onOSCESecurityChange(
									osceProxy, osceSecurityStatus,
									null, true);
							dialogBox.hide();
						}
					});
					dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {

						public void onClick(ClickEvent event) {
							patientSecurity.setValue(osceProxy.getSecurity());
							dialogBox.hide();
						}
					});

					dialogBox.showYesNoDialog(constants.roleOSCESecurityChange());
				}
			} else {
				final PatientAveragePerPost patientAveragePerPost = patientAvgPerPost.getValue();
				if (osceProxy.getPatientAveragePerPost() != null) {
					if (patientAveragePerPost.ordinal() != osceProxy.getPatientAveragePerPost().ordinal()) {
						delegate.onOSCESecurityChange(osceProxy, null,patientAveragePerPost, false);
					}
				} else {
					delegate.onOSCESecurityChange(osceProxy, null,
							patientAveragePerPost, false);
				}
			}
		} else {
			if (isSecurityStatusChange) {
//				Log.info("osceProxy : "+osceProxy.getName());
				delegate.onOSCESecurityChange(osceProxy,patientSecurity.getValue(), null, true);
			} else {
				delegate.onOSCESecurityChange(osceProxy, null,patientAvgPerPost.getValue(), false);
			}
		}
	}
	
	public OsceDaySubViewImpl() {
	
		this.osceDaySubViewImpl=this;
		initWidget(uiBinder.createAndBindUi(this));
	}

	public OsceDaySubViewImpl(OsceDayProxy osceDayProxy) {

		this.osceDaySubViewImpl = this;
		this.setOsceDayProxy(osceDayProxy);
	
		initWidget(uiBinder.createAndBindUi(this));

	}
	
	public void getHeaderPanelForTitle(final String title) {

		HorizontalPanel panel = new HorizontalPanel();
		
//		panel.setSpacing(5);
//		panel.setWidth("100%");
//		panel.setHeight("100%");
		
		panel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		
		
//		panel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		
		
		final HorizontalPanel headerPanel = new HorizontalPanel() {
			public void onBrowserEvent(Event event) {
				switch (DOM.eventGetType(event)) {
				case Event.ONCLICK:
					// Prevent link default action.
					DOM.eventPreventDefault(event);
					simpleDiscloserPanel.setOpen(!simpleDiscloserPanel.isOpen());
				}
			}
		};
		headerPanel.setWidth("500px");
		
		final HorizontalPanel iconImagePanel = new HorizontalPanel();
		iconImagePanel.addStyleName("rightIcon");
		
		Label label = new Label(title);
		
		headerPanel.sinkEvents(Event.ONCLICK);
		iconImagePanel.setWidth("15px");
		
//		setHeaderSuggestionBox();
		init();
		
		headerPanel.add(iconImagePanel);
		headerPanel.add(label);
		headerPanel.add(new Label());
		panel.add(headerPanel);
			
		HorizontalPanel comboPanel = new HorizontalPanel();
		comboPanel.setWidth("100px");
		comboPanel.setStyleName("alignRightPanel");
		
		
		patientAvgPerPost.setStyleName("alignRight");
		
//		Label patientAvgPerPostLabel = new Label(constants.avgOfSPPerPost());
//		comboPanel.add(patientAvgPerPostLabel);
		
		
//		Label patientSecurityLabel = new Label(constants.securityType());
//		comboPanel.add(patientSecurityLabel);
		
		patientSecurity.setStyleName("alignRight");
		
		comboPanel.add(patientAvgPerPost);
		
		comboPanel.add(patientSecurity);
		
//		comboPanel.setStyleName("alignRight");
		
		panel.add(comboPanel);

		OpenHandler<DisclosurePanel> openHandler = new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
//				displayComboValue();
				Log.info(" onOpen isOnMouseUpevent : "+isHeaderChangeEvent);
				if (isHeaderChangeEvent) {
					isHeaderChangeEvent = false;
					fromOnOpen = true;
					simpleDiscloserPanel.setOpen(isPanelOpen);
					Log.info(" onOpen isOnMouseUpevent : " + isHeaderChangeEvent);
				} else if (!fromOnClose) {
					iconImagePanel.removeStyleName("rightIcon");	
					iconImagePanel.addStyleName("downIcon");
					Log.info("In onOpen Handler");
					fromOnClose = false;
					delegate.discloserPanelOpened(osceDayProxy,
							osceDaySubViewImpl);
				}
			}
		};
		
		CloseHandler<DisclosurePanel> closeHandler = new CloseHandler<DisclosurePanel>() {
			
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
//				displayComboValue();
				Log.info(" onClose isOnMouseUpevent : " + isHeaderChangeEvent);
				if (isHeaderChangeEvent) {
					isHeaderChangeEvent = false;
					fromOnClose = true;
					simpleDiscloserPanel.setOpen(isPanelOpen);
//					Log.info(" onClose isOnMouseUpevent : " + isHeaderChangeEvent);
				} else if (!fromOnOpen) {
					iconImagePanel.removeStyleName("downIcon");
					iconImagePanel.addStyleName("rightIcon");
//					Log.info("In onClose Handler");
					fromOnOpen = false;
					delegate.discloserPanelClosed(osceDayProxy,
							osceDaySubViewImpl);
				}
			}
		};

		// when open DisclosurePanel,add data to table
		simpleDiscloserPanel.addOpenHandler(openHandler);
		simpleDiscloserPanel.addCloseHandler(closeHandler);
				
//		closeHandler.onClose(null);
		simpleDiscloserPanel.setHeader(panel);	

//		return panel;
			}
	
		
	private void setHeaderEvent(){
		isHeaderChangeEvent = true;	
		isPanelOpen = simpleDiscloserPanel.isOpen();
	}

	public void setOsceDayProxy(OsceDayProxy osceDayProxy){
		this.osceDayProxy =osceDayProxy;
	}
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {

		
		//Log.info("!!!!!!!~~~~~~~~~~~~~~~osceProxy"+ osceProxy);
		patientAvgPerPost.setAcceptableValues(java.util.Arrays
				.asList(PatientAveragePerPost.values()));
		patientSecurity.setAcceptableValues(java.util.Arrays
				.asList(OSCESecurityStatus.values()));

		if (osceProxy != null) {
			if (osceProxy.getPatientAveragePerPost() != null) {
				patientAvgPerPost.setValue(osceProxy
						.getPatientAveragePerPost());
				Log.info("getPatientAveragePerPost"
						+ osceProxy.getPatientAveragePerPost());

			}
			if (osceProxy.getSecurity() != null) {
				patientSecurity.setValue(osceProxy.getSecurity());
				Log.info("getSecurity"+osceProxy.getSecurity());				
			}
			else{
				patientSecurity.setValue(OSCESecurityStatus.FEDERAL_EXAM);
				onValueChangeSecurity(true);
			}
		}
		
		patientAvgPerPost.addValueChangeHandler(new ValueChangeHandler<PatientAveragePerPost>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<PatientAveragePerPost> arg0) {
				delegate.showApplicationLoading(true);
				Log.info("OsceProxy Id: " + osceDayProxy.getId());				
				Log.info("osceProxy"+ osceProxy);
				//Manish change to prevent call when null selected
				if(arg0.getValue() !=null)
				onValueChangeSecurity(false);
				else
				patientAvgPerPost.setValue(osceProxy.getPatientAveragePerPost());	
				
				delegate.showApplicationLoading(false);
				
			}
		});
		
		patientSecurity.addValueChangeHandler(new ValueChangeHandler<OSCESecurityStatus>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<OSCESecurityStatus> arg0) {
				delegate.showApplicationLoading(true);
				Log.info("OsceProxy Id: " + osceDayProxy.getId());
				Log.info("osceProxy"+ osceProxy);
				if(arg0.getValue() !=null)
				onValueChangeSecurity(true);
				else
				patientSecurity.setValue(osceProxy.getSecurity());	
				delegate.showApplicationLoading(false);
				
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
	
	// Module 3 d {
		@Override
		public void onPatientInSemesterSelectedEventReceived(
				PatientInSemesterSelectedEvent event) {		
			
			delegate.showApplicationLoading(true);
			patientInSemesterProxy=event.getPatientInSemesterProxy();
			setOsceDayProxy=event.getOsceDayProxies();
			delegate.patientInSemesterSelected(patientInSemesterProxy,setOsceDayProxy,osceDaySubViewImpl);
			delegate.showApplicationLoading(false);
		}

		@Override
		public void onRoleSelectedEventReceived(RoleSelectedEvent event) {
			Log.info("onRoleSelectedEventReceived");
			delegate.roleSelectedevent(event.getStandardizedRoleProxy(),osceDaySubViewImpl);
			
		}
		
		// Module 3 d }

//	private void setHeaderSuggestionBox() {
//		
//		FocusHandler focusHandler = new FocusHandler() {
//
//			@Override
//			public void onFocus(FocusEvent event) {
//				setHeaderEvent();
//				Log.info("Select post is : " + event.getType());
//			}
//		};
//
//		ValueChangeHandler<String> valueChangeHandler = new ValueChangeHandler<String>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<String> event) {
//				setHeaderEvent();
//				Log.info("Select post is : " + event.getValue());
//
//			}
//		};
//
//		ClickHandler clickHandler = new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				setHeaderEvent();
//				Log.info("Select post is : " + event.getType());
//			}
//		};
//
//		KeyPressHandler keyPressHandler = new KeyPressHandler() {
//
//			@Override
//			public void onKeyPress(KeyPressEvent arg0) {
//				setHeaderEvent();
//
//			}
//		};
//
//		ChangeHandler changeHandler = new ChangeHandler() {
//			@Override
//			public void onChange(ChangeEvent event) {
//				setHeaderEvent();
//				Log.info("Select post is : " + event.getType());
////				onValueChangeSecurity(true);
//
//			}
//		};
//
////		oSCESecurityStatusSuggestBox
////		.addHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
////
////			@Override
////			public void onSelection(SelectionEvent<Suggestion> event) {
////				// TODO Auto-generated method stub
////				System.out.println("on Selection :"+ ((OSCESecurityStatus) ((ProxySuggestOracle<OSCESecurityStatus>.ProxySuggestion) event
////						.getSelectedItem()).getProxy()).name());
////				onValueChangeSecurity( (OSCESecurityStatus) ((ProxySuggestOracle<OSCESecurityStatus>.ProxySuggestion) event
////						.getSelectedItem()).getProxy());
////							
////			}
////			
////
////		});
//		
//		
//		DefaultSuggestOracle<PatientAveragePerPost> patientAvgSuggestOracle = (DefaultSuggestOracle<PatientAveragePerPost>) patientAvgPostSuggestBox.getSuggestOracle();
//		patientAvgSuggestOracle.setPossiblilities(
//				java.util.Arrays
//				.asList(PatientAveragePerPost.values()));
//		
//		patientAvgPostSuggestBox.setSuggestOracle(patientAvgSuggestOracle);			
//		patientAvgPostSuggestBox.setRenderer(new AbstractRenderer<PatientAveragePerPost>() {
//
//			@Override
//			public String render(PatientAveragePerPost object) {
//				// TODO Auto-generated method stub
//				return ""+object.getTypeId();
//			}
//		});
//		
//		DefaultSuggestOracle<OSCESecurityStatus> securityStatuSuggestOracle = (DefaultSuggestOracle<OSCESecurityStatus>) oSCESecurityStatusSuggestBox.getSuggestOracle();
//		securityStatuSuggestOracle.setPossiblilities(
//				java.util.Arrays
//				.asList(OSCESecurityStatus.values()));
//		
//		oSCESecurityStatusSuggestBox.setSuggestOracle(securityStatuSuggestOracle);			
//		oSCESecurityStatusSuggestBox.setRenderer(new AbstractRenderer<OSCESecurityStatus>() {
//
//			@Override
//			public String render(OSCESecurityStatus object) {
//				return ""+object.name();
//			}
//		});
//		
//		patientAvgPostSuggestBox.getTextField().addFocusHandler(focusHandler);
//
//		patientAvgPostSuggestBox.getTextField().addValueChangeHandler(
//				valueChangeHandler);
//
//		patientAvgPostSuggestBox.getTextField().arrowButton
//				.addClickHandler(clickHandler);
//
//		patientAvgPostSuggestBox.getTextField().addKeyPressHandler(
//				keyPressHandler);
//
//		patientAvgPostSuggestBox.addHandler(changeHandler);
//		
//		
//		oSCESecurityStatusSuggestBox.getTextField().addFocusHandler(focusHandler);
//
//		oSCESecurityStatusSuggestBox.getTextField().addValueChangeHandler(
//				valueChangeHandler);
//
//		oSCESecurityStatusSuggestBox.getTextField().arrowButton
//				.addClickHandler(clickHandler);
//
//		oSCESecurityStatusSuggestBox.getTextField().addKeyPressHandler(
//				keyPressHandler);
//
//		oSCESecurityStatusSuggestBox.addHandler(changeHandler);	
//		
//		
//		if (osceProxy != null) {
//			if (osceProxy.getPatientAveragePerPost() != null) {				
//				patientAvgPostSuggestBox.getTextField().advancedTextBox.setText(""+osceProxy
//						.getPatientAveragePerPost().getTypeId());
//				Log.info("getPatientAveragePerPost"
//						+ osceProxy.getPatientAveragePerPost());
//
//			}
//			if (osceProxy.getSecurity() != null) {				
//				oSCESecurityStatusSuggestBox.setSelected(osceProxy.getSecurity());
//				oSCESecurityStatusSuggestBox.getTextField().advancedTextBox.setText(osceProxy.getSecurity().name());
//				Log.info("getSecurity"+osceProxy.getSecurity());				
//			}
//		}
//
//	}
}
