package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAdvancedSearchAnamnesisPopupImpl extends PopupPanel
		implements StandardizedPatientAdvancedSearchAnamnesisPopup {

	private static StandardizedPatientAdvancedSearchAnamnesisPopupImplUiBinder uiBinder = GWT
			.create(StandardizedPatientAdvancedSearchAnamnesisPopupImplUiBinder.class);

	interface StandardizedPatientAdvancedSearchAnamnesisPopupImplUiBinder extends
			UiBinder<Widget, StandardizedPatientAdvancedSearchAnamnesisPopupImpl> {
	}
	
	@UiField
	IconButton addAnamnesisValueButton;
	@UiField
	IconButton addAnamnesisValues;
	@UiField
	IconButton closeBoxButton;
	
	@UiField
	SuggestBox anamnesisValue;
	
	@UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new AbstractRenderer<ch.unibas.medizin.osce.shared.BindType>() {
        public String render(ch.unibas.medizin.osce.shared.BindType obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });
    
    @UiField(provided = true)
    ValueListBox<Comparison2> comparison = new ValueListBox<Comparison2>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Comparison2>() {
        public String render(ch.unibas.medizin.osce.shared.Comparison2 obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });

	public StandardizedPatientAdvancedSearchAnamnesisPopupImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		comparison.setAcceptableValues(Arrays.asList(Comparison2.values()));
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		addAnamnesisValueButton.setText(Messages.ADD);
		addAnamnesisValues.setText(Messages.ANAMNESIS_VALUES);
	}
	
	@UiHandler("addAnamnesisValueButton")
	public void addAnamnesisValueButtonClicked(ClickEvent e) {
		delegate.addAnamnesisValueButtonClicked();
	}
	
	@UiHandler("addAnamnesisValues")
	public void addAnamnesisValuesClicked(ClickEvent e) {
		hide();
	}
	
	@UiHandler("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent e) {
		hide();
	}
	
	private Delegate delegate;
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void display(Button parentButton) {
		this.show();
		this.setPopupPosition(parentButton.getAbsoluteLeft() - 5, parentButton.getAbsoluteTop() - getOffsetHeight()/2 - 6);
	}
}
