/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class AnamnesisCheckDetailsViewImpl extends Composite implements AnamnesisCheckDetailsView {

	private static AnamnesisCheckDetailsViewImplUiBinder uiBinder = GWT
			.create(AnamnesisCheckDetailsViewImplUiBinder.class);

	interface AnamnesisCheckDetailsViewImplUiBinder extends
	UiBinder<Widget, AnamnesisCheckDetailsViewImpl> {
	}

	@UiField
	HasClickHandlers edit;

	@UiField
	HasClickHandlers delete;

	private Delegate delegate;

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
	public AnamnesisCheckDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	SpanElement id;
	
	@UiField
	SpanElement value;
	
	@UiField
	SpanElement text;

//	@UiField
//	SpanElement version;

//	@UiField
//	SpanElement createDate;
//
//	@UiField
//	SpanElement anamnesischecksvalues;
//
//	@UiField
//	SpanElement scars;

	AnamnesisCheckProxy proxy;

	@UiField
	SpanElement displayRenderer;

	private Presenter presenter;

	public void setValue(AnamnesisCheckProxy proxy) {
		this.proxy = proxy;
		id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
		value.setInnerText(proxy.getValue() == null ? "" : String.valueOf(proxy.getValue()));
		text.setInnerText(proxy.getText() == null ? "" : String.valueOf(proxy.getText()));
//		version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
//		createDate.setInnerText(proxy.getCreateDate() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getCreateDate()));
//		anamnesischecksvalues.setInnerText(proxy.getAnamnesischecksvalues() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueProxyRenderer.instance()).render(proxy.getAnamnesischecksvalues()));
//		scars.setInnerText(proxy.getScars() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.ScarProxyRenderer.instance()).render(proxy.getScars()));
		displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.AnamnesisCheckProxyRenderer.instance().render(proxy));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter AnamnesisCheckActivity) {
		this.presenter =  AnamnesisCheckActivity;
	}

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public AnamnesisCheckProxy getValue() {
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
