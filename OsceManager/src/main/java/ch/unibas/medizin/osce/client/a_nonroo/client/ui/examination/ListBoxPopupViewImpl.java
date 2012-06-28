package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;


import java.io.IOException;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class ListBoxPopupViewImpl extends PopupPanel implements ListBoxPopupView{
	
	private static final Binder BINDER = GWT.create(Binder.class);
	
	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	interface Binder extends UiBinder<Widget, ListBoxPopupViewImpl> {
	}
	
	private EntityProxy proxy;
	
	public EntityProxy getProxy() {
		return proxy;
	}

	public void setProxy(EntityProxy proxy) {
		this.proxy = proxy;
	}

	private OscePostSubView oscePostSubView;

	public OscePostSubView getOscePostSubView() {
		return oscePostSubView;
	}

	public void setOscePostSubView(OscePostSubView oscePostSubView) {
		this.oscePostSubView = oscePostSubView;
	}

	@UiField(provided=true)
	ValueListBox<EntityProxy> listBox=new ValueListBox<EntityProxy>(new Renderer<EntityProxy>() {

		@Override
		public String render(EntityProxy object) {
			// TODO Auto-generated method stub
			
			if(object instanceof RoleTopicProxy )
			{
				if(object==null)
					return "";
				else
					return ((RoleTopicProxy)object).getName();
			}
			else if(object instanceof SpecialisationProxy)
			{
				if(object==null)
					return "";
				else
					return ((SpecialisationProxy)object).getName();
			}
			else if(object instanceof RoomProxy)
			{
				if(object==null)
					return "";
				else
					return ((RoomProxy)object).getRoomNumber();
			}
			else if(object instanceof StandardizedRoleProxy)
			{
				if(object==null)
					return "";
				else
					return ((StandardizedRoleProxy)object).getLongName();
			}
			
			return null;
		
			
			
		}

		@Override
		public void render(EntityProxy object, Appendable appendable)
				throws IOException {
			// TODO Auto-generated method stub
			
		}
	});
	
	

	public ValueListBox<EntityProxy> getListBox() {
		return listBox;
	}

	

	@UiField
	Button okBtn;
	
	public Button getOkBtn() {
		return okBtn;
	}

	public ListBoxPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		okBtn.setText(constants.okBtn());
	}
	
	
	
	
	@UiHandler("okBtn")
	public void okBtnClicked(ClickEvent event)
	{
		
		
		Log.info("Event :" + event.getAssociatedType());
		//event.getAssociatedType()
		EntityProxy object=listBox.getValue();
		Log.info("okBtnClicked  :" + object);
		
		
		if(object instanceof StandardizedRoleProxy)
		{
			Log.info("StandardizedRoleProxy");
			delegate.saveStandardizedRole(this);
		}
		
		
	}
	
	
}
