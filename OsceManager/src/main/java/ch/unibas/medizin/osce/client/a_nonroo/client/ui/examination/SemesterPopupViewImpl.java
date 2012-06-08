package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
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

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	
	
	public SemesterPopupViewImpl() 
	{
		super(true);
		Log.info("Call SemesterPopupViewImpl() Constructor");				
		setWidget(uiBinder.createAndBindUi(this));			
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setAutoHideEnabled(false);
		center();
		setText("Semester");
				
		enumSemester.setAcceptableValues(Arrays.asList(Semesters.values()));
		enumSemester.setValue(Semesters.HS);
		
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
}
