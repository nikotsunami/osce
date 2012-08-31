package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.PriceType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoomMaterialsDetailsViewImpl extends Composite implements
		RoomMaterialsDetailsView {

	private static RoomMaterialsDetailsViewImplUiBinder uiBinder = GWT
			.create(RoomMaterialsDetailsViewImplUiBinder.class);

	interface RoomMaterialsDetailsViewImplUiBinder extends
			UiBinder<Widget, RoomMaterialsDetailsViewImpl> {

	}

	@UiField
	IconButton edit;

	@UiField
	IconButton delete;

	@UiField
	TabPanel materialListPanel;

	@UiField
	SpanElement labelName;

	@UiField
	SpanElement labelType;
	@UiField
	SpanElement labelPrice;
	@UiField
	SpanElement labelPriceType;

	@UiField
	SpanElement header;

	@UiField
	SpanElement name;

	@UiField
	SpanElement type;

	@UiField
	SpanElement price;
	@UiField
	SpanElement priceType;

	MaterialListProxy proxy;

	private OsceConstants constants = GWT.create(OsceConstants.class);
	private Presenter presenter;
	private Delegate delegate;

	public RoomMaterialsDetailsViewImpl() {
		// TODO Auto-generated constructor stub
		initWidget(uiBinder.createAndBindUi(this));

		materialListPanel.selectTab(0);
		materialListPanel.getTabBar().setTabText(0, constants.roomMaterials());
		TabPanelHelper.moveTabBarToBottom(materialListPanel);

		edit.setText(constants.edit());
		delete.setText(constants.delete());

		labelType.setInnerText(constants.roomMaterialType() + ":");
		labelName.setInnerText(constants.roomMaterialName() + ":");
		labelPrice.setInnerText(constants.roomMaterialPrice() + ":");
		labelPriceType.setInnerText(constants.roomMaterialPriceType() + ":");

	}

	public void setValue(MaterialListProxy materialListProxy) {
		this.proxy = materialListProxy;
		String headerText = "[";

		headerText += materialListProxy.getId() == null ? "" : String
				.valueOf(materialListProxy.getId());

		headerText += "] ";

		headerText += materialListProxy.getName() == null ? "" : String
				.valueOf(materialListProxy.getName());

		header.setInnerText(headerText);

		name.setInnerText(materialListProxy.getName() == null ? ""
				: materialListProxy.getName());

		type.setInnerText(materialListProxy.getType() == null ? ""
				: new EnumRenderer<MaterialType>().render(proxy.getType()));

		// : new EnumRenderer<MaterialType>().render(materialListProxy
		// .getType()));

		price.setInnerText(proxy.getPrice() == null ? "" : String.valueOf(proxy
				.getPrice()));
		priceType.setInnerText(materialListProxy.getType() == null ? ""
				: new EnumRenderer<PriceType>().render(proxy.getPriceType()));

	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter StandardizedPatientActivity) {
		this.presenter = StandardizedPatientActivity;
	}

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public MaterialListProxy getValue() {
		return proxy;
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
	}

}
