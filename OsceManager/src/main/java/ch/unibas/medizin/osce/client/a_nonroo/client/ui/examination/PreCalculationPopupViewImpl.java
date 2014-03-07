package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class PreCalculationPopupViewImpl extends PopupPanel implements PreCalculationPopupView {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, PreCalculationPopupViewImpl> {
	}

	private Delegate delegate;
	
	@UiField
	Label osceDateValLbl;
	 
	@UiField
	Label startTimeValLbl;
	
	@UiField
	Label endTimeValLbl;
	
	@UiField
	IconButton closeButton;
	
	public PreCalculationPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		setAutoHideEnabled(true);
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent e)
	{
		this.hide();
	}

	public Label getOsceDateValLbl() {
		return osceDateValLbl;
	}
	
	public Label getStartTimeValLbl() {
		return startTimeValLbl;
	}
	
	public Label getEndTimeValLbl() {
		return endTimeValLbl;
	}
}
