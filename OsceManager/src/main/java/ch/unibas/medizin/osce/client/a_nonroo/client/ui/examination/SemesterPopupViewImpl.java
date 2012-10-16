package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

//public class SemesterPopupViewImpl extends DialogBox implements SemesterPopupView
public class SemesterPopupViewImpl extends DialogBox implements SemesterPopupView
{
//	private static final Binder BINDER = GWT.create(Binder.class);
	
private static SemesterPopupViewImplUiBinder uiBinder = GWT.create(SemesterPopupViewImplUiBinder.class);
	
	interface SemesterPopupViewImplUiBinder extends UiBinder<Widget, SemesterPopupViewImpl> {
	}
	
	private Delegate delegate;
	
	
	@UiField 
	public TextBox txtYear;
	
	/*@UiField 
	public TextBox txtSemester;*/
	
	@UiField(provided = true)
	public FocusableValueListBox<Semesters> enumSemester = new FocusableValueListBox<Semesters>(new EnumRenderer<Semesters>());
	
	
	/*@UiField 
	public DoubleBox txtMaxYearEarning;
	
	@UiField 
	public DoubleBox txtPriceStatist;
	
	@UiField 
	public DoubleBox txtPriceSP;*/
	
	@UiField 
	public TextBox txtMaxYearEarning;
	
	@UiField 
	public TextBox txtPriceStatist;
	
	@UiField 
	public TextBox txtPriceSP;
	
	
	@UiField 
	public Button btnSave;
		
	@UiField 
	public Button btnClose;
	
	@UiField
	public Label labelYear;
	
	@UiField
	public Label labelSemester;
	
	@UiField
	public Label labelMaxYearEarning;
	
	@UiField
	public Label labelPriceSimpat;
	
	@UiField
	public Label labelPriceStatist;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	// Highlight onViolation
		public Map<String, Widget> semesterMap;
	// E Highlight onViolation
	
	public SemesterPopupViewImpl() 
	{
		super(true);
		Log.info("Call SemesterPopupViewImpl() Constructor");				
		setWidget(uiBinder.createAndBindUi(this));			
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setAutoHideEnabled(false);
		center();
		setText(constants.semester());
		
		this.getElement().getStyle().setZIndex(1);
		
		enumSemester.setAcceptableValues(Arrays.asList(Semesters.values()));
		enumSemester.setValue(Semesters.HS);
		
		// Highlight onViolation
		semesterMap=new HashMap<String, Widget>();
		semesterMap.put("semester", enumSemester);
		semesterMap.put("calYear", txtYear);
		semesterMap.put("maximalYearEarnings",txtMaxYearEarning);
		semesterMap.put("pricestatist", txtPriceStatist);
		semesterMap.put("priceStandardizedPartient", txtPriceSP);
		// E Highlight onViolation
		
		labelYear.setText(constants.year());
		labelSemester.setText(constants.semester());
		labelMaxYearEarning.setText(constants.maxYearlySalary());
		labelPriceStatist.setText(constants.salarySimpat());
		labelPriceSimpat.setText(constants.salaryExtra());
		
		btnSave.setText(constants.save());
		btnClose.setText(constants.close());
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
		
}
