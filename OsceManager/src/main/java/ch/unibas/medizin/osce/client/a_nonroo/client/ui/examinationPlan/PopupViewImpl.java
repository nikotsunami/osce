package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examinationPlan;

import java.io.IOException;
import java.util.Date;

import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class PopupViewImpl  extends PopupPanel  implements PopupView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	Label examinerNameLbl;
	
	public ValueListBox<Date> getEndTimeListBox() {
		return endTimeListBox;
	}
	public Button getOkButton() {
		return okButton;
	}
	public Button getCancelButton() {
		return cancelButton;
	}
	
	@UiField
	Button saveBtn;
	
	
	
	public Button getSaveBtn() {
		return saveBtn;
	}
	@UiField
	IconButton edit;
	
	@UiField
	Label examinerNameValue;
	
	public Label getExaminerNameValue() {
		return examinerNameValue;
	}
	public IconButton getEdit() {
		return edit;
	}

	@UiField(provided = true)
	ValueListBox<Date> endTimeListBox=new ValueListBox<Date>(new Renderer<Date>() {

		@Override
		public String render(Date object) {
			if(object==null)
				return "";
			else
				return DateTimeFormat.getShortDateTimeFormat().format(object);
		}

		@Override
		public void render(Date object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	@UiField
	IconButton okButton;
	
	@UiField
	Button cancelButton;
	
	
	
	@UiField(provided = true)
	public SuggestBox examinerSuggestionBox =  //new SuggestBox(keywordoracle);
			new SuggestBox(
					new ProxySuggestOracle<DoctorProxy>(
							new AbstractRenderer<DoctorProxy>() {
								@Override
								public String render(DoctorProxy object) {
									return object.getName() ;
								}
							}// ));
							, ",;:. \t?!_-/\\"));
	
	public void setExaminerSuggestionBox(SuggestBox examinerSuggestionBox) {
		this.examinerSuggestionBox = examinerSuggestionBox;
	}

	@UiField
	Label nameLbl;
	
	@UiField 
	Label nameValue;
	
	public Label getExaminerNameLbl() {
		return examinerNameLbl;
	}
	public SuggestBox getExaminerSuggestionBox() {
		return examinerSuggestionBox;
	}
	public Label getNameLbl() {
		return nameLbl;
	}
	public Label getNameValue() {
		return nameValue;
	}
	public Label getStartTimeLbl() {
		return startTimeLbl;
	}
	public Label getStartTimeValue() {
		return startTimeValue;
	}
	public Label getEndTimeLbl() {
		return endTimeLbl;
	}
	public Label getEndTimeValue() {
		return endTimeValue;
	}

	@UiField
	Label startTimeLbl;
	
	@UiField
	Label startTimeValue;
	
	@UiField
	Label endTimeLbl;
	
	@UiField
	Label endTimeValue;
	
	public PopupViewImpl()
	{
		super(true);
		add(BINDER.createAndBindUi(this));
	}
	public void createSPPopupView()
	{
		examinerNameLbl.removeFromParent();
		examinerSuggestionBox.removeFromParent();
		edit.removeFromParent();
		
		nameLbl.setVisible(true);
		nameValue.setVisible(true);
		nameLbl.setText(constants.spName());
		
		startTimeLbl.setVisible(true);		
		startTimeValue.setVisible(true);
		startTimeLbl.setText(constants.circuitStart());
		
		endTimeLbl.setVisible(true);
		endTimeValue.setVisible(true);
		endTimeLbl.setText(constants.circuitEndTime());
		//endTimeValue.removeFromParent();
		endTimeListBox.removeFromParent();
		
		okButton.setVisible(true);
		okButton.setText(constants.close());
		cancelButton.setVisible(false);
		saveBtn.removeFromParent();
	}
	public void createOscePostPopupView()
	{
		examinerNameLbl.removeFromParent();
		examinerSuggestionBox.removeFromParent();
		edit.removeFromParent();
		
		nameLbl.setVisible(true);
		nameValue.setVisible(true);
		nameLbl.setText(constants.postType());
		
		startTimeLbl.setVisible(true);		
		startTimeValue.setVisible(true);
		startTimeLbl.setText(constants.roleTopic());
		
		okButton.setVisible(true);
		okButton.setText(constants.close());
		cancelButton.setVisible(false);
		endTimeLbl.removeFromParent();
		endTimeListBox.removeFromParent();
		endTimeValue.removeFromParent();
		saveBtn.removeFromParent();
		
		
	}
	public void createExaminerInfoPopupView()
	{
		examinerNameLbl.setVisible(true);
		examinerNameLbl.setText(constants.examinerName());
		examinerSuggestionBox.setVisible(false);
		examinerNameValue.setVisible(true);
		edit.setVisible(true);
		saveBtn.setVisible(false);
		
		nameLbl.setVisible(false);
		nameValue.setVisible(false);
		
		//nameLbl.setText(constants.examinerName());
		
		startTimeLbl.setVisible(true);		
		startTimeValue.setVisible(true);
		startTimeLbl.setText(constants.circuitStart());
		
		okButton.setVisible(true);
		okButton.setText(constants.close());
		cancelButton.setVisible(false);
		
		endTimeLbl.setVisible(true);
		endTimeListBox.removeFromParent();
		endTimeValue.setVisible(true);
		endTimeLbl.setText(constants.circuitEndTime());
	}
	
	public void createExaminerAssignPopupView() {
		
		//enable
		examinerNameLbl.setVisible(true);
		examinerSuggestionBox.setVisible(true);
		startTimeLbl.setVisible(true);
		
		startTimeValue.setVisible(true);
		endTimeLbl.setVisible(true);
		endTimeListBox.setVisible(true);
		okButton.setVisible(true);
		cancelButton.setVisible(true);
		
		startTimeLbl.setText(constants.circuitStart());
		examinerNameLbl.setText(constants.examinerName());
		endTimeLbl.setText(constants.circuitEndTime());
		
		okButton.setText(constants.okBtn());
		okButton.setIcon("check");
		cancelButton.setText(constants.cancel());
		
		//set visible false
		
		edit.removeFromParent();
		nameLbl.removeFromParent();
		nameValue.removeFromParent();
		endTimeValue.removeFromParent();
		saveBtn.removeFromParent();
		
		
		
	}
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, PopupViewImpl> {
	}
}
