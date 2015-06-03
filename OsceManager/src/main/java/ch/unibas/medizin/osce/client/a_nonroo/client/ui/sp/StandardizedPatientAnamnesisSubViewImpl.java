package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.client.style.widgets.ScrolledTabLayoutPanel;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAnamnesisSubViewImpl extends Composite
		implements StandardizedPatientAnamnesisSubView {

	private static StandardizedPatientScarSubViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientScarSubViewImplUiBinder.class);

	interface StandardizedPatientScarSubViewImplUiBinder extends
			UiBinder<Widget, StandardizedPatientAnamnesisSubViewImpl> {
	}

	private int selectedAnamnesisTab;
	private Delegate delegate;
	private OsceConstants constants = GWT.create(OsceConstants.class);

	
	//ScrolledTab Changes start

	/*@UiField
	TabPanel circuitTabPanel;*/
	
	//private final UiIcons uiIcons = GWT.create(UiIcons.class);
	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	public ImageResource icon1 = uiIcons.triangle1West(); 
	public ImageResource icon2=  uiIcons.triangle1East();
	Unit u=Unit.PX;
	

	@UiField
	public HorizontalPanel horizontalanamnesisPanel;

	/*// Panels
	@UiField
	TabPanel patientPanel;
*/
	
	
	@UiField(provided=true)
	public ScrolledTabLayoutPanel anamnesisTabs=new ScrolledTabLayoutPanel(40L, u, icon1, icon2);
	
	
	/*@UiField
	TabPanel anamnesisTabs;
	*/
	//ScrolledTab Changes end

	@UiField(provided = true)
	QuickSearchBox searchBox;

	@UiField
	CheckBox showAnswered;

	@UiField
	CheckBox showUnanswered;

	//Added for OMS-150.
	@UiField
	CheckBox showComment;
	
	//Added for OMS-151.
	@UiField
	IconButton btnPrevious;
	@UiField
	IconButton btnNext;
	private boolean isNextClicked=false;
	private boolean isPreviousClicked=false;
	
	boolean isSearchChanged=false;
	private boolean isRequestSentForAllData;
	
	public StandardizedPatientAnamnesisSubViewImpl() {
		initSearchBox();
		initWidget(uiBinder.createAndBindUi(this));
		horizontalanamnesisPanel.addStyleName("horizontalPanelStyle");
		horizontalanamnesisPanel.add(anamnesisTabs);
		/*anamnesisTabs.setHeight("300px");*/
		initCheckBoxes();
		//Added for OMS-151.
		registerPreviousNextButtonClickHandler();
		setVisibilityOfPreNextButton("");
	}
	//Added for OMS-151.
	/**
	 * Registering click handler of previous, next buttons.
	 */
	private void registerPreviousNextButtonClickHandler() {
		Log.info("Registering click handler of previous, next buttons");
		btnPrevious.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("previous button clicked");
				isPreviousClicked=true;
				delegate.previousButtonClicked();
			}
		});
		btnNext.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Log.info("next button clicked");
				isNextClicked=true;
				delegate.nextButtonClicked();
			}
		});
		
	}

	private void initSearchBox() {
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				//changed for OMS-151.
				isSearchChanged=true;
				setVisibilityOfPreNextButton(searchBox.getText());
				delegate.findAnamnesisChecksValuesForAllTabs();
			}
			
		});
	}

	private void initCheckBoxes() {
		showAnswered.setValue(true);
		showAnswered.setText(constants.showAnswered());
		showUnanswered.setText(constants.showUnanswered());
		showComment.setText(constants.showComments());
		
		showAnswered.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isSearchChanged=true;
				delegate.performAnamnesisSearch();
			}
		});
		showUnanswered.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isSearchChanged=true;
				delegate.performAnamnesisSearch();
			}
		});
		
		//Added for OMS-150.
		showComment.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Log.info("show comment value changed new value is : " + event.getValue());
				delegate.addOrRemoveCommentsColumn(event.getValue());
			}

			
		});
		// FIXME: temporarily disabled...
//		showAnswered.removeFromParent();
//		showUnanswered.removeFromParent();
	}

	@Override
	public StandardizedPatientAnamnesisTableSubView addAnamnesisCheckTitle(AnamnesisCheckTitleProxy title) {
		String titleText = "";
		if (title.getText() != null) {
			titleText = title.getText();
		}
		StandardizedPatientAnamnesisTableSubView tableSubView = new StandardizedPatientAnamnesisTableSubViewImpl();
		anamnesisTabs.add((Widget) tableSubView, titleText);
		if (anamnesisTabs.getWidgetCount() == 1) {
			anamnesisTabs.addSelectionHandler(new SelectionHandler<Integer>() {
				@Override
				public void onSelection(SelectionEvent<Integer> event) {
					//changed during OMS-151.
					if(isPreviousClicked || isNextClicked || isRequestSentForAllData ){
						//When previous or next  button is clicked then we already have search result so showing data from that.
						isNextClicked=false;
						isPreviousClicked=false;
					}else{
						//fetching result when we don't have cache data.
						delegate.performAnamnesisSearch();
						delegate.storeDisplaySettings();
					}
				}
			});
		}
		return tableSubView;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public boolean areUnansweredQuestionsShown() {
		return showUnanswered.getValue();
	}

	public boolean areAnsweredQuestionsShown() {
		return showAnswered.getValue();
	}

	public String getSearchString() {
		return searchBox.getText();
	}

	@Override
	public int getSelectedTab() {
		//ScrolledTab Changes start 
		//return anamnesisTabs.getTabBar().getSelectedTab();
		return anamnesisTabs.getSelectedIndex();
		//ScrolledTab Changes end
	}
	
	@Override
	public void allTitlesAreLoaded() {
		if (anamnesisTabs.getWidgetCount() - 1 > selectedAnamnesisTab) {
			selectedAnamnesisTab = anamnesisTabs.getWidgetCount() - 1;
		} else if (selectedAnamnesisTab < 0) {
			selectedAnamnesisTab = 0;
		}
		anamnesisTabs.selectTab(selectedAnamnesisTab);
	}

	@Override
	public void setSelectedAnamnesisTab(int selectedAnamnesisTab) {
		this.selectedAnamnesisTab = selectedAnamnesisTab;
	}
	
	/**
	 * return value of show comment check box.
	 */
	@Override
	public boolean isToShowCommentsColumn() {
		return showComment.getValue();
	}

	//Added for OMS-151.
	
	private void setVisibilityOfPreNextButton(String text) {
		if(text.isEmpty()){
			btnNext.setVisible(false);
			btnPrevious.setVisible(false);
		}else{
			btnNext.setVisible(true);
			btnPrevious.setVisible(true);
		}
		
	}
	@Override
	public ScrolledTabLayoutPanel getAnamnesisTabs() {
		return anamnesisTabs;
	}
	@Override
	public void setPreviousButtonEnable(boolean isEnable){
		btnPrevious.setEnabled(isEnable);
	}
	@Override
	public void setNextButtonEnable(boolean isEnable){
		btnNext.setEnabled(isEnable);
	}

	@Override
	public boolean isSearchChanged() {
		return isSearchChanged;
	}
	@Override
	public void setSearchChanged(boolean isSearchChanged) {
		this.isSearchChanged = isSearchChanged;
	}

	@Override
	public void setIsRquestAlreadySentForAllData(boolean result) {
		this.isRequestSentForAllData=result;
		
	}
	
}
