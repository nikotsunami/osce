package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.Arrays;

import org.apache.bcel.generic.LALOAD;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.shared.TraitTypes;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class ScarEditPopupViewImpl extends PopupPanel implements ScarEditPopupView{
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	@UiField(provided = true)
    ValueListBox<TraitTypes> traitTypeBox = new ValueListBox<TraitTypes>(new EnumRenderer<TraitTypes>());

	@UiField
	Label typeLbl;
	
	@UiField
	Label locationLbl;
	
	@UiField 
	TextBox locationTxtBox;
	
	@UiField
	Button okBtn;
	
	@UiField
	Button cancelBtn;
	
	public ValueListBox<TraitTypes> getTraitTypeBox() {
		return traitTypeBox;
	}

	public void setTraitTypeBox(ValueListBox<TraitTypes> traitTypeBox) {
		this.traitTypeBox = traitTypeBox;
	}

	public Label getTypeLbl() {
		return typeLbl;
	}

	public void setTypeLbl(Label typeLbl) {
		this.typeLbl = typeLbl;
	}

	public Label getLocationLbl() {
		return locationLbl;
	}

	public void setLocationLbl(Label locationLbl) {
		this.locationLbl = locationLbl;
	}

	public TextBox getLocationTxtBox() {
		return locationTxtBox;
	}

	public void setLocationTxtBox(TextBox locationTxtBox) {
		this.locationTxtBox = locationTxtBox;
	}

	public Button getOkBtn() {
		return okBtn;
	}
	
	public void setOkBtn(Button okBtn) {
		this.okBtn = okBtn;
	}
	
	// Issue Role 
	public Button getCancelBtn() {
		return cancelBtn;
	}
	
	public void setCancelBtn(Button cancelBtn) {
		this.cancelBtn = cancelBtn;
	}
	// E: Issue Role 
	
	public ScarEditPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		traitTypeBox.setAcceptableValues(Arrays.asList(TraitTypes.values()));
		typeLbl.setText(constants.type());
		locationLbl.setText(constants.location());
		okBtn.setText(constants.okBtn());
		// Issue Role 
		cancelBtn.setText(constants.cancel());
		// E: Issue Role 
	}
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, ScarEditPopupViewImpl> {
	}
}