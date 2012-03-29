package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp.criteria;

import java.util.Arrays;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.ScarProxyRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAdvancedSearchScarPopupImpl extends PopupPanel 
		implements StandardizedPatientAdvancedSearchScarPopup {

	private static StandardizedPatientAdvancedSearchScarPopupImplUiBinder uiBinder = GWT
			.create(StandardizedPatientAdvancedSearchScarPopupImplUiBinder.class);

	interface StandardizedPatientAdvancedSearchScarPopupImplUiBinder extends
			UiBinder<Widget, StandardizedPatientAdvancedSearchScarPopupImpl> {
	}
	
	@UiField
	IconButton addScarButton;
	@UiField
	IconButton closeBoxButton;
	@UiField
	IconButton scarButton;
	
	@UiField(provided = true)
    ValueListBox<BindType> bindType = new ValueListBox<BindType>(new EnumRenderer<BindType>());
    
    @UiField(provided = true)
    ValueListBox<Comparison2> comparison = new ValueListBox<Comparison2>(new AbstractRenderer<ch.unibas.medizin.osce.shared.Comparison2>() {
        public String render(ch.unibas.medizin.osce.shared.Comparison2 obj) {
            return obj == null ? "" : String.valueOf(obj);
        }
    });
	
	@UiField (provided=true)
	ValueListBox<ScarProxy> scarBox = new ValueListBox<ScarProxy>(ScarProxyRenderer.getInstance());

	public StandardizedPatientAdvancedSearchScarPopupImpl() {
		OsceConstants constants = GWT.create(OsceConstants.class);
		setWidget(uiBinder.createAndBindUi(this));
		bindType.setValue(BindType.values()[0]);
		bindType.setAcceptableValues(Arrays.asList(BindType.values()));
		comparison.setValue(Comparison2.values()[0]);
		comparison.setAcceptableValues(Arrays.asList(Comparison2.values()));
		addScarButton.setText(constants.add());
		scarButton.setText(constants.traits());
	}
	
	@UiHandler("addScarButton")
	public void addScarButtonClicked(ClickEvent event) {
		delegate.addScarButtonClicked(scarBox.getValue(), bindType.getValue(), comparison.getValue());
		this.hide();
	}

	@UiHandler("closeBoxButton")
	public void closeBoxButtonClicked(ClickEvent event) {
		this.hide();
	}
	
	@UiHandler("scarButton")
	public void scarButtonClicked(ClickEvent event) {
		this.hide();
	}

	private Delegate delegate;
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void display(Button addScar) {
		this.show();
		this.setPopupPosition(addScar.getAbsoluteLeft() - 5, addScar.getAbsoluteTop() - getOffsetHeight()/2 - 4);
	}
	
	@Override
	public ValueListBox<ScarProxy> getScarBox() {
		return scarBox;
	}
}