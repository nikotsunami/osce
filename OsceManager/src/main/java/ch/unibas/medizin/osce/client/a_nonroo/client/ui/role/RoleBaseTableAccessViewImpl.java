package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;

import ch.unibas.medizin.osce.client.managed.request.RoleBaseItemProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class RoleBaseTableAccessViewImpl extends Composite implements
RoleBaseTableAccessView {

	private static RoleBaseTableAccessViewImplUiBinder uiBinder = GWT
			.create(RoleBaseTableAccessViewImplUiBinder.class);

	interface RoleBaseTableAccessViewImplUiBinder extends
			UiBinder<Widget, RoleBaseTableAccessViewImpl> {

	}

	private OsceConstants constants = GWT.create(OsceConstants.class);
	private Presenter presenter;
	private Delegate delegate;

	private final UiIcons uiIcons = GWT.create(UiIcons.class);

	private  RoleBaseItemProxy roleBasedItemProxy;
	
	private RoleItemAccessProxy roleItemAccessProxy;
	
	
	@UiField
	public Label accessDataLabel;
	
		
	@UiField
	public IconButton accessDataCloseButton;
	
	@UiHandler("accessDataCloseButton")
	public void deleteAccessTypeClicked(ClickEvent event){
		delegate.deleteAccessType(roleBasedItemProxy,accessDataLabel,roleItemAccessProxy);
	}
	
	
	
	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public RoleBaseTableAccessViewImpl() {

		initWidget(uiBinder.createAndBindUi(this));
		
		init();
		// todo
		
	}
	
	protected ArrayList<String> paths = new ArrayList<String>();
	public void init(){
	
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
	@Override
	public Delegate getDelegate() {

		return delegate;
	}


	@Override
	public void setBaseItemMidifiedValue(String value) {
		// TODO Auto-generated method stub
		
	}


	public void setRoleBaseItemProxy(RoleBaseItemProxy roleBasedItemProxy2) {
		this.roleBasedItemProxy=roleBasedItemProxy2;
		
	}



	public void setRoleItemAccessProxy(RoleItemAccessProxy roleItemAccessProxy) {
		this.roleItemAccessProxy=roleItemAccessProxy;
		
	}
	
}
