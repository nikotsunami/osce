/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
//file by spec
//Editor<StandardizedRoleProxy>
public class RoleEditCheckListSubViewImpl extends Composite implements RoleEditCheckListSubView  {
	private static final Binder BINDER = GWT.create(Binder.class);
	private Delegate delegate;
	private OsceConstants constants = GWT.create(OsceConstants.class);

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
	
	@UiField
	public TextBox title;
	
	
	@UiField
	public SpanElement labelName;
	
		
	/*private static RoleEditCheckListSubViewImplUiBinder uiBinder = GWT.create(RoleEditCheckListSubViewImplUiBinder.class);

	interface RoleEditCheckListSubViewImplUiBinder extends UiBinder<Widget, RoleEditCheckListSubViewImpl> {
	}*/

	interface Binder extends UiBinder<Widget, RoleEditCheckListSubViewImpl> {}
	

	
	public RoleEditCheckListSubViewImpl() {		
		initWidget(BINDER.createAndBindUi(this));
		init();
	}
	
	public void init() {
		labelName.setInnerText(constants.name() + ":");
	}

	private Presenter presenter;

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	

	
//spec start
	
	
	
		
	//spec end
	
}