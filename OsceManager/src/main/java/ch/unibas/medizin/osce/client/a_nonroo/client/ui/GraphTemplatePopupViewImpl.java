package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class GraphTemplatePopupViewImpl extends PopupPanel {

	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, GraphTemplatePopupViewImpl> {
	}

	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	public IconButton closeButton;
	
	@UiField
	SpanElement popupTitle;

	@UiField
	Image graphImage;
	
	@UiField
	Label imgErrorLbl;	
	
	
	public GraphTemplatePopupViewImpl() {
		super(false,true);
		this.setGlassEnabled(true);
		this.addStyleName("printPopupPanel");
		add(BINDER.createAndBindUi(this));
		imgErrorLbl.setText(constants.imgError());
		popupTitle.setInnerText(constants.graphPopupTitle());	
		//graphImage.setAltText(constants.imgError());
	}

	public IconButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(IconButton closeButton) {
		this.closeButton = closeButton;
	}

	public SpanElement getPopupTitle() {
		return popupTitle;
	}

	public void setPopupTitle(SpanElement title) {
		this.popupTitle = title;
	}

	public Label getImgErrorLbl() {
		return imgErrorLbl;
	}

	public void setImgErrorLbl(Label imgErrorLbl) {
		this.imgErrorLbl = imgErrorLbl;
	}

	public Image getGraphImage() {
		return graphImage;
	}

	public void setGraphImage(Image graphImage) {
		this.graphImage = graphImage;
	}

	public void addCloseClickHandler(ClickHandler handler)
	{
		closeButton.addClickHandler(handler);
	}
	
}
