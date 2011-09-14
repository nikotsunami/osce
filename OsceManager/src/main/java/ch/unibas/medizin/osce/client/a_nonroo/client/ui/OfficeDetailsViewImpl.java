/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.OfficeProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author niko2
 *
 */
public class OfficeDetailsViewImpl extends Composite implements OfficeDetailsView {

	private static OfficeDetailsViewImplUiBinder uiBinder = GWT
			.create(OfficeDetailsViewImplUiBinder.class);

	interface OfficeDetailsViewImplUiBinder extends
			UiBinder<Widget, OfficeDetailsViewImpl> {
	}

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
	public OfficeDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	

    @UiField
    SpanElement id;

    @UiField
    SpanElement version;

    @UiField
    SpanElement gender;

    @UiField
    SpanElement title;

    @UiField
    SpanElement name;

    @UiField
    SpanElement preName;

    @UiField
    SpanElement email;

    @UiField
    SpanElement telephone;

	OfficeProxy proxy;

		private Presenter presenter;

	    public void setValue(OfficeProxy proxy) {
	    	this.proxy = proxy;
	        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
	        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
	        gender.setInnerText(proxy.getGender() == null ? "" : String.valueOf(proxy.getGender()));
	        title.setInnerText(proxy.getTitle() == null ? "" : String.valueOf(proxy.getTitle()));
	        name.setInnerText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
	        preName.setInnerText(proxy.getPreName() == null ? "" : String.valueOf(proxy.getPreName()));
	        email.setInnerText(proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail()));
	        telephone.setInnerText(proxy.getTelephone() == null ? "" : String.valueOf(proxy.getTelephone()));
	    }

		@Override
		public void setDelegate(Delegate delegate) {
			this.delegate = delegate;
			
		}

		@Override
		public void setPresenter(Presenter OfficeActivity) {
			this.presenter =  OfficeActivity;
			
		}
		
	    public Widget asWidget() {
	        return this;
	    }

	    public boolean confirm(String msg) {
	        return Window.confirm(msg);
	    }

	    public OfficeProxy getValue() {
	        return proxy;
	    }

}
