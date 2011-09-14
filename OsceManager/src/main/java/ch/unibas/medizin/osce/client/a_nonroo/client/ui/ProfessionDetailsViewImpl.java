/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import ch.unibas.medizin.osce.client.managed.request.ProfessionProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
public class ProfessionDetailsViewImpl extends Composite implements  ProfessionDetailsView{

	private static ProfessionDetailsViewImplUiBinder uiBinder = GWT
			.create(ProfessionDetailsViewImplUiBinder.class);

	interface ProfessionDetailsViewImplUiBinder extends
			UiBinder<Widget, ProfessionDetailsViewImpl> {
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
	public ProfessionDetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	  @UiField
	    SpanElement id;

	    @UiField
	    SpanElement version;

	    @UiField
	    SpanElement profession;

	    @UiField
	    SpanElement standardizedpatients;

	    ProfessionProxy proxy;

	    @UiField
	    SpanElement displayRenderer;



		private Presenter presenter;

	    public void setValue(ProfessionProxy proxy) {
	    	this.proxy = proxy;
	        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
	        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
	        profession.setInnerText(proxy.getProfession() == null ? "" : String.valueOf(proxy.getProfession()));
	        standardizedpatients.setInnerText(proxy.getStandardizedpatients() == null ? "" : ch.unibas.medizin.osce.client.scaffold.place.CollectionRenderer.of(ch.unibas.medizin.osce.client.managed.ui.StandardizedPatientProxyRenderer.instance()).render(proxy.getStandardizedpatients()));
	        displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.ProfessionProxyRenderer.instance().render(proxy));
	    }

		@Override
		public void setDelegate(Delegate delegate) {
			this.delegate = delegate;
			
		}

		@Override
		public void setPresenter(Presenter ProfessionActivity) {
			this.presenter =  ProfessionActivity;
			
		}
		
	    public Widget asWidget() {
	        return this;
	    }

	    public boolean confirm(String msg) {
	        return Window.confirm(msg);
	    }

	    public ProfessionProxy getValue() {
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
