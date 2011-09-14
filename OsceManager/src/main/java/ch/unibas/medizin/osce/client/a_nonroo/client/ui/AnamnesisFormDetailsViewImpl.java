/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.AnamnesisFormProxy;

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
 * @author niko2
 *
 */
public class AnamnesisFormDetailsViewImpl extends Composite implements  AnamnesisFormDetailsView{

	private static AnamnesisFormDetailsViewImplUiBinder uiBinder = GWT
			.create(AnamnesisFormDetailsViewImplUiBinder.class);

	interface AnamnesisFormDetailsViewImplUiBinder extends
			UiBinder<Widget, AnamnesisFormDetailsViewImpl> {
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
	public AnamnesisFormDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

    @UiField
    SpanElement id;

    @UiField
    SpanElement version;

    @UiField
    SpanElement createDate;

    @UiField
    SpanElement anamnesischecksvalues;

    @UiField
    SpanElement scars;

    AnamnesisFormProxy proxy;

    @UiField
    SpanElement displayRenderer;



		private Presenter presenter;

	    public void setValue(AnamnesisFormProxy proxy) {
	    	 this.proxy = proxy;
	         id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
	         version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
	         createDate.setInnerText(proxy.getCreateDate() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getCreateDate()));
	         anamnesischecksvalues.setInnerText(proxy.getAnamnesischecksvalues() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.AnamnesisChecksValueProxyRenderer.instance()).render(proxy.getAnamnesischecksvalues()));
	         scars.setInnerText(proxy.getScars() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.ScarProxyRenderer.instance()).render(proxy.getScars()));
	         displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.AnamnesisFormProxyRenderer.instance().render(proxy));
	    }

		@Override
		public void setDelegate(Delegate delegate) {
			this.delegate = delegate;
			
		}

		@Override
		public void setPresenter(Presenter AnamnesisFormActivity) {
			this.presenter =  AnamnesisFormActivity;
			
		}
		
	    public Widget asWidget() {
	        return this;
	    }

	    public boolean confirm(String msg) {
	        return Window.confirm(msg);
	    }

	    public AnamnesisFormProxy getValue() {
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
