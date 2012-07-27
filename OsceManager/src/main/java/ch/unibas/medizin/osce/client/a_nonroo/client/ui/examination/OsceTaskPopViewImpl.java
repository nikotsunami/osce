package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.OsceDetailsView.Delegate;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AdministratorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.TaskProxy;
import ch.unibas.medizin.osce.client.managed.ui.AdministratorProxyRenderer;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class OsceTaskPopViewImpl extends PopupPanel  implements OsceTaskPopView{
	
	private static OsceFilterPopupUiBinder uiBinder = GWT.create(OsceFilterPopupUiBinder.class);
	
	interface OsceFilterPopupUiBinder extends
	UiBinder<Widget, OsceTaskPopViewImpl> {
	}
	
	// Highlight onViolation
	Map<String, Widget> taskMap;
		// E Highlight onViolation
	
	@UiField
	FocusPanel filterPanelRoot;
	
	@UiField
	public TextBox taskName;
	
	@UiField
	Button save;
	
	

	@UiField
	Button cancel;
	
	public Boolean isedit;
	
	@UiField
	public DateBox deadline;
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	
/*	@UiField(provided = true)
	public ValueListBox<AdministratorProxy> administrator = new ValueListBox<AdministratorProxy>(ch.unibas.medizin.osce.client.managed.ui.AdministratorProxyRenderer.instance(), new com.google.gwt.requestfactory.ui.client.EntityProxyKeyProvider<ch.unibas.medizin.osce.client.managed.request.AdministratorProxy>());
*/

	@UiField
	public DefaultSuggestBox<AdministratorProxy, EventHandlingValueHolderItem<AdministratorProxy>> administrator;

	//Issue # 122 : Replace pull down with autocomplete.
	private Delegate delegate;
		
	public OsceProxy proxy;
	public TaskProxy editProxy;
	
	
	public OsceTaskPopViewImpl(){
		super(true);
		  
		
	
		
		add(uiBinder.createAndBindUi(this));
	
		
		/*
		filterPanelRoot.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				int mouseX = event.getClientX();
				int mouseY = event.getClientY();
				
				if (mouseX < getAbsoluteLeft() || mouseX > getAbsoluteLeft() + getOffsetWidth() 
						|| mouseY < getAbsoluteTop() || mouseY > getAbsoluteTop() + getOffsetHeight()) {

					// TODO: handle it from view
					//view.updateSearch();
					
					hide();
				}
				
			}
		});*/
		
		
		
		OsceConstants constants = GWT.create(OsceConstants.class);
		
		deadline.getTextBox().setReadOnly(true);
		deadline.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				// TODO Auto-generated method stub
				

				Date today = new Date();
				Date futureDate=new Date();
				futureDate.setYear(today.getYear()+2);
				
				/*Date date = new Date();
				Date d=new Date();
			//	Calendar cal = Calendar.getInstance();
				d.setYear(date.getYear()+2);*/
			if(event.getValue().before(today))
			{
				Window.alert("Date should be past date");
				deadline.setValue(null);
			}
			if(event.getValue().after(futureDate))
			{
				Window.alert("Date should be not allowed after 2 year");
				deadline.setValue(null);
			}
			}

			
		});
	
		// Highlight onViolation		
		taskMap=new HashMap<String, Widget>();
		taskMap.put("name", taskName);
		taskMap.put("deadline", deadline);
		//Issue # 122 : Replace pull down with autocomplete.
		//taskMap.put("administrator", administrator);
		taskMap.put("administrator", administrator.getTextField().advancedTextBox);
		//Issue # 122 : Replace pull down with autocomplete.
		
		// E Highlight onViolation
		
	}
		
	
	
	
	
	
		
	
	
	@Override
	public void setValue(OsceProxy proxy) {
		// TODO Auto-generated method stub
		this.proxy=proxy;
	}
	@Override
	public void setDelegate(Delegate delegate) {
		// TODO Auto-generated method stub
		this.delegate=delegate;
	}
	@Override
	public void setPresenter(Presenter osceActivity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setAdministratorValue(List<AdministratorProxy> emptyList) {
		// TODO Auto-generated method stub
		
		//Issue # 122 : Replace pull down with autocomplete.
		//administrator.setAcceptableValues(emptyList);
		
		DefaultSuggestOracle<AdministratorProxy> suggestOracle1 = (DefaultSuggestOracle<AdministratorProxy>) administrator.getSuggestOracle();
		suggestOracle1.setPossiblilities(emptyList);
		administrator.setSuggestOracle(suggestOracle1);
		//administrator.setRenderer(new AdministratorProxyRenderer());
		
		administrator.setRenderer(new AbstractRenderer<AdministratorProxy>() {

			@Override
			public String render(AdministratorProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getName();
				}
				else
				{
					return "";
				}
			}
		});
		//Issue # 122 : Replace pull down with autocomplete.
		
	}


	@UiHandler ("cancel")
	public void cancelClicked(ClickEvent event) {
		hide();
	}


	@UiHandler ("save")
	public void newButtonClicked(ClickEvent event) {
		
				
		
		Date today = new Date();
		Date futureDate=new Date();
		futureDate.setYear(today.getYear()+2);
		
		/*if(taskName.getValue().length()<3 )
		{
			Window.alert("please enter proper  name of atleast 3 charater");
			return;
		}
		else if(administrator.getValue()==null)
		{
			Window.alert("please select administrator value");
			return;
		}*/
		
		/*else if(deadline.getValue()==null)
		{
			Window.alert("please select deadline date");
			return;
		}
		else if(isedit==true)
		{
			if(deadline.getValue().after(futureDate))
			{
				Window.alert("Please enter proper date");
				return;
			}
		}
		else if(deadline.getValue().after(futureDate) || deadline.getValue().before(today) )
		{
			Window.alert("please enter proper date");
			return;
		}*/
		
		//System.out.println("before save call"+proxy+"---"+editProxy);
		//Issue # 122 : Replace pull down with autocomplete.
		//delegate.saveClicked(isedit,taskName.getText(),administrator.getValue(),deadline.getValue(),proxy,editProxy);
		delegate.saveClicked(isedit,taskName.getText(),administrator.getSelected(),deadline.getValue(),proxy,editProxy);
		//Issue # 122 : Replace pull down with autocomplete.
		isedit=false;
		taskName.setValue("");
		deadline.getTextBox().setValue("");
		//hide();
		
		
	}
	// Highlight onViolation	
	@Override
	public Map getTaskMap()
	{
		return this.taskMap;
	}
	// E Highlight onViolation

	


	




	
	
	
}
