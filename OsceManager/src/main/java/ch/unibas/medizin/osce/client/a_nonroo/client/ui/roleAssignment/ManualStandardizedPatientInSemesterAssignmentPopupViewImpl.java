package ch.unibas.medizin.osce.client.a_nonroo.client.ui.roleAssignment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.StandardizedPatientStatus;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

public class ManualStandardizedPatientInSemesterAssignmentPopupViewImpl extends
		DialogBox implements
		ManualStandardizedPatientInSemesterAssignmentPopupView {

	interface Binder
			extends
			UiBinder<Widget, ManualStandardizedPatientInSemesterAssignmentPopupViewImpl> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);
	// private static ManualStandardizedPatientInSemesterAssignmentPopupViewImpl
	// manualStdPatientInSemesterAssignmentPopupViewImpl;

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	// public static MultiWordSuggestOracle stdPatientMultiWordSuggestOracle =
	// new MultiWordSuggestOracle();
	private Delegate delegate;
	private StandardizedPatientProxy standardizedPatientProxy;
	private List<StandardizedPatientProxy> standardizedPatientProxies;

	
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	public DefaultSuggestBox<StandardizedPatientProxy, EventHandlingValueHolderItem<StandardizedPatientProxy>> standardizedPatientSugestionBox;

	/*@UiField(provided = true)
	SuggestBox standardizedPatientSugestionBox = new SuggestBox(
			new ProxySuggestOracle<StandardizedPatientProxy>(
					new AbstractRenderer<StandardizedPatientProxy>() {
						@Override
						public String render(StandardizedPatientProxy object) {
							return object.getName() + " " + object.getPreName();
						}
					}// ));
					, ",;:. \t?!_-/\\"));
	*/
	//Issue # 122 : Replace pull down with autocomplete.

	@UiField
	IconButton standardizedPatientAddButton;

	@UiField
	IconButton addAllButton;

	@UiField
	IconButton closeBoxButton;

	@UiHandler("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent e) {
		hide();
	}

	//MODULE 3 : TACK B
	@UiHandler("addAllButton")
	public void addAllButtonClicked(ClickEvent e) {
		Log.info("standardizedPatientProxies.size()" +standardizedPatientProxies.size());
		if (standardizedPatientProxies.size() > 0 && isActivePatientAvailable()) {
			delegate.onAddAllActive(standardizedPatientProxies);
		} 
		else 
		{
			suggestionBoxLbl.setText(constants.patientIsNotAvailable());
		}
	}

	private boolean isActivePatientAvailable(){
		for (Iterator<StandardizedPatientProxy> iterator = standardizedPatientProxies.iterator(); iterator.hasNext();) {
			StandardizedPatientProxy standardizedPatientProxy = (StandardizedPatientProxy) iterator.next();
			if(standardizedPatientProxy.getStatus() == StandardizedPatientStatus.ACTIVE)
				return true;
		}
		return false;
	}	
	//MODULE 3 : TACK B
	
	@UiField
	Label suggestionBoxLbl;

	
	// Highlight onViolation
	Map<String, Widget> patientInSemesterMap;
		// E Highlight onViolation
	
	public ManualStandardizedPatientInSemesterAssignmentPopupViewImpl() {
		setWidget(BINDER.createAndBindUi(this));

		this.standardizedPatientAddButton.setText(constants.addPatient());
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.setText(constants.addManually());
		this.closeBoxButton.setText(constants.close());
		this.addAllButton.setText(constants.addAllActive());

		initSuggestBox();

		this.center();

		// Highlight onViolation
		patientInSemesterMap=new HashMap<String, Widget>();
		
		//Issue # 122 : Replace pull down with autocomplete.
		//patientInSemesterMap.put("semester", standardizedPatientSugestionBox);
		//patientInSemesterMap.put("standardizedPatient", standardizedPatientSugestionBox);
		
		patientInSemesterMap.put("semester", standardizedPatientSugestionBox.getTextField().advancedTextBox);
		patientInSemesterMap.put("standardizedPatient", standardizedPatientSugestionBox.getTextField().advancedTextBox);
		//Issue # 122 : Replace pull down with autocomplete.
		// E Highlight onViolatio
		this.getElement().getStyle().setZIndex(1); 
	}

	@UiHandler("standardizedPatientAddButton")
	public void onClickEvent(ClickEvent clickEvent) {
		Log.info("Call standardizedPatientAddButton addPatient");
		/*if (standardizedPatientSugestionBox.getValue().trim().compareTo("") != 0) 
		{*/
			if (this.delegate != null) 
			{
				
				// Highlight onViolation
				/*if (standardizedPatientProxy != null) 
				{*/					
					Log.info("delegate is avilable");
					if(standardizedPatientProxy==null)
					{
						Log.info("Null Proxy");
						
						standardizedPatientSugestionBox.addStyleName("higlight_onViolation");
						return;
					}
					else
					{
						this.delegate.onStandizedPatientAddBtnClick(standardizedPatientProxy);	
					}
					
				/*} 
				else 
				{*/
					/*suggestionBoxLbl.setText(constants.enterPatient());*/
					//Issue # 122 : Replace pull down with autocomplete.
					//standardizedPatientSugestionBox.setText(constants.enterPatient());
					//standardizedPatientSugestionBox.getTextField().advancedTextBox.setText(constants.enterPatient());
					//Issue # 122 : Replace pull down with autocomplete.

				/*}*/
			} 
			else {
				Log.info("delegate Value is NULL");
			}
			// showPatientAssignmentPopup(false);
		/*} 
		else 
		{
			Log.info("Suggest Box Value is NULL");
		}*/

	}

	public void setDetails(
			List<StandardizedPatientProxy> standardizedPatientProxies,
			Delegate delegate, Button parentButton) {

		this.setDelegate(delegate);
		this.setStandizedPatientAutocompleteValue(standardizedPatientProxies);
		this.standardizedPatientProxies = standardizedPatientProxies;

		this.setPopupPosition(parentButton.getAbsoluteLeft(),
				parentButton.getAbsoluteTop() - getOffsetHeight() / 2 - 6);
		delegate.showApplicationLoading(false);
		this.show();

	}

	// public void showPatientAssignmentPopup(boolean show) {
	// if (show) {
	// if (!this.isShowing()) {
	// this.standardizedPatientSugestionBox.getTextBox().setText(
	// constants.enterPatient());
	// this.show();
	// }
	// } else if (this != null) {
	// this.hide();
	// }
	// }

	private void initSuggestBox() {

		// standardizedPatientSugestionBox

		//Issue # 122 : Replace pull down with autocomplete.
/*		standardizedPatientSugestionBox.setText(constants.enterPatient());
		standardizedPatientSugestionBox.getTextBox().addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						if (standardizedPatientSugestionBox.getText().equals(
								constants.enterPatient())) {
							standardizedPatientSugestionBox.setText("");
							suggestionBoxLbl.setText("");
						}
					}
				});
		standardizedPatientSugestionBox.getTextBox().addBlurHandler(
				new BlurHandler() {
					@Override
					public void onBlur(BlurEvent event) {
						if (standardizedPatientSugestionBox.getText()
								.equals("")) {
							standardizedPatientSugestionBox.setText(constants
									.enterPatient());
							suggestionBoxLbl.setText("");
						}
					}
				});
*/
		//Issue # 122 : Replace pull down with autocomplete.
		
		
		//Issue # 122 : Replace pull down with autocomplete.

		/*
		standardizedPatientSugestionBox
				.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

					@Override
					public void onSelection(SelectionEvent<Suggestion> event) {
						// TODO Auto-generated method stub
						System.out.println("on Selection");
						standardizedPatientSugestionBox.setValue(event
								.getSelectedItem().getReplacementString());

						standardizedPatientProxy = (StandardizedPatientProxy) ((ProxySuggestOracle<StandardizedPatientProxy>.ProxySuggestion) event
								.getSelectedItem()).getProxy();
					}

				});

		standardizedPatientSugestionBox.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				// TODO Auto-generated method stub
				System.out.println("on Change");
				standardizedPatientSugestionBox.setValue(((SuggestBox) sender).getTextBox().getValue());
				suggestionBoxLbl.setText("");
			}
		});*/
		
		standardizedPatientSugestionBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
			Log.info("value change handler");	
			//Issue # 122 : Replace pull down with autocomplete.
			//if(this.roleLstBox.getValue()==null)
				if(standardizedPatientSugestionBox.getSelected()!=null)
				{
					standardizedPatientProxy=standardizedPatientSugestionBox.getSelected();
				}
				else
				{
					standardizedPatientProxy=null;
				}
			}
			
			
		});
	}

		
		
	//Issue # 122 : Replace pull down with autocomplete.

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
		Log.info("Delegate set .. ");

	}

	@Override
	public void setStandizedPatientAutocompleteValue(
			List<StandardizedPatientProxy> values) {

		// Log.info("List of Values of SP is: " + values.size());

		//Issue # 122 : Replace pull down with autocomplete.
		
		/*((ProxySuggestOracle<StandardizedPatientProxy>) standardizedPatientSugestionBox
				.getSuggestOracle()).addAll(values);
		*/
		Log.info("list size==="+values.size());

		DefaultSuggestOracle<StandardizedPatientProxy> suggestOracle1 = (DefaultSuggestOracle<StandardizedPatientProxy>) standardizedPatientSugestionBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);
		standardizedPatientSugestionBox.setSuggestOracle(suggestOracle1);
		
		standardizedPatientSugestionBox.setRenderer(new AbstractRenderer<StandardizedPatientProxy>() {

			@Override
			
			public String render(StandardizedPatientProxy object) {
				// TODO Auto-generated method stub
				if(object.getName()!=null)
				{
				return object.getName()+" "+object.getPreName();}
				else
				{
					return "";
				}
				
			}
		});
		

		//Issue # 122 : Replace pull down with autocomplete.

		// for (int i = 0; i < values.size(); i++) {
		// if (values.get(i) != null) {
		// try {
		// ((ProxySuggestOracle<StandardizedPatientProxy>)
		// standardizedPatientSugestionBox
		// .getSuggestOracle()).add(values.get(i));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }

	}

	public SuggestBox getStandardizedPatientSugestionBox() 
	{
		//Issue # 122 : Replace pull down with autocomplete.
		//return standardizedPatientSugestionBox;
		//Issue # 122 : Replace pull down with autocomplete.
		return null;
	}

	public Delegate getDelegate() {
		return delegate;
	}

	// Highlight onViolation
	@Override
	public Map getPatientInSemesterMap()
	{
		return this.patientInSemesterMap;
	}
	// E Highlight onViolation
}
