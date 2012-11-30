/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.AnalysisType;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class StatisticalEvaluationDetailsViewImpl extends Composite implements StatisticalEvaluationDetailsView, MenuClickHandler {

	private static StatisticalEvaluationDetailsViewUiBinder uiBinder = GWT.create(StatisticalEvaluationDetailsViewUiBinder.class);

	interface StatisticalEvaluationDetailsViewUiBinder extends UiBinder<Widget, StatisticalEvaluationDetailsViewImpl> {
	}

	private Delegate delegate;

	

	private Presenter presenter;
	
	OsceConstants constants = GWT.create(OsceConstants.class);
	
	//@UiField
	//Button importBtn;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	VerticalPanel sequenceVP;
	
	@UiField(provided=true)
	FocusableValueListBox<AnalysisType> analysisListBox = new FocusableValueListBox<AnalysisType>(new EnumRenderer<AnalysisType>());
	
	@UiField
	Button calculateBtn;
	
	@UiField
	Button exportBtn;
	
	public Button getExportBtn() {
		return exportBtn;
	}






	public VerticalPanel getSequenceVP() {
		return sequenceVP;
	}

	




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
	public StatisticalEvaluationDetailsViewImpl() 
	{
		Log.info("Call StatisticalEvaluationDetailsViewImpl");
		initWidget(uiBinder.createAndBindUi(this));
		analysisListBox.setAcceptableValues(Arrays.asList(AnalysisType.values()));
		
		analysisListBox.addValueChangeHandler(new ValueChangeHandler<AnalysisType>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<AnalysisType> event) {
				analysisListBoxValueChangedEvent(event);
				
			}
		});
		
		analysisListBox.setValue(AnalysisType.item_analysis,true);
	
		
		
		
		calculateBtn.setText(constants.export());
		int height = ResolutionSettings.getRightWidgetHeight() - 55;
		
		scrollPanel.setHeight(height+"px");
		//importBtn.setText(constants.importBtn());
		
		sequenceVP.setWidth(ResolutionSettings.getRightWidgetWidth()-60+"px");
		
	}
	
	public StatisticalEvaluationDetailsViewImpl(Delegate delegate) 
	{
		Log.info("Call StatisticalEvaluationDetailsViewImpl");
		initWidget(uiBinder.createAndBindUi(this));
		
		this.delegate=delegate;
		
		analysisListBox.setAcceptableValues(Arrays.asList(AnalysisType.values()));
		
		analysisListBox.addValueChangeHandler(new ValueChangeHandler<AnalysisType>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<AnalysisType> event) {
				analysisListBoxValueChangedEvent(event);
				
			}
		});
		
		analysisListBox.setValue(AnalysisType.item_analysis,true);
	
		
		
		
		calculateBtn.setText(constants.calculate());
		int height = ResolutionSettings.getRightWidgetHeight() - 55;
		
		scrollPanel.setHeight(height+"px");
		//importBtn.setText(constants.importBtn());
		
		sequenceVP.setWidth(ResolutionSettings.getRightWidgetWidth()-60+"px");
		
	}


	
	

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("calculateBtn")
	public void calcuateButtonClicked(ClickEvent event)
	{
		Log.info("calcuateButtonClicked");
		delegate.calculate();
	}
	
	
	public void analysisListBoxValueChangedEvent(ValueChangeEvent<AnalysisType> event)
	{
		Log.info("analysisListBoxValueChangedEvent :" + event.getValue());
		if(event.getValue() == null)
			return;
		
		delegate.analysisListBoxValueChanged(event.getValue());
	}
	
	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
		int height = ResolutionSettings.getRightWidgetHeight() - 55;
		scrollPanel.setHeight(height+"px");
	}
	
	@UiHandler("exportBtn")
	public void exportBtnClick(ClickEvent event)
	{
		delegate.exportStatisticData();
	}
}
