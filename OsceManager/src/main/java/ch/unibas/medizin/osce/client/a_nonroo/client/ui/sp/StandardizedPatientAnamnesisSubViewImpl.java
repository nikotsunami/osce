package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
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

	@UiField
	TabPanel anamnesisTabs;

	@UiField(provided = true)
	QuickSearchBox searchBox;

	@UiField
	CheckBox showAnswered;

	@UiField
	CheckBox showUnanswered;

	public StandardizedPatientAnamnesisSubViewImpl() {
		initSearchBox();
		initWidget(uiBinder.createAndBindUi(this));
		initCheckBoxes();
	}

	private void initSearchBox() {
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performAnamnesisSearch();
			}
		});
	}

	private void initCheckBoxes() {
		showAnswered.setValue(true);
		showAnswered.setText(constants.showAnswered());
		showUnanswered.setText(constants.showUnanswered());

		showAnswered.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				delegate.performAnamnesisSearch();
			}
		});
		showUnanswered.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				delegate.performAnamnesisSearch();
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
					delegate.performAnamnesisSearch();
					delegate.storeDisplaySettings();
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
		return anamnesisTabs.getTabBar().getSelectedTab();
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
}
