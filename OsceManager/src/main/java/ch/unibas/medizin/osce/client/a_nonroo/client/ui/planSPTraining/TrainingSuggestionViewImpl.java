package ch.unibas.medizin.osce.client.a_nonroo.client.ui.planSPTraining;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TrainingSuggestionViewImpl extends Composite implements TrainingSuggestionView {

	private static TrainingSuggestionViewImplUiBinder uiBinder = GWT.create(TrainingSuggestionViewImplUiBinder.class);
	
	interface TrainingSuggestionViewImplUiBinder extends UiBinder<Widget, TrainingSuggestionViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	private DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
	
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MM-yyyy");
		
	private Delegate delegate;
	
	private SuggestionPopupView poupView = new SuggestionPopupView();
	
	@UiField
	IconButton moreButton;
	
	/*@UiField
	IconButton afternoonMoreButton;
	*/
	
	@UiHandler("moreButton")
	public void mouseDownEventOfMoreButton(MouseDownEvent event)
	{
		event.preventDefault();
		event.stopPropagation();
	}
	
	@UiHandler("moreButton")
	public void clickEventOfMoreButton(ClickEvent event)
	{
		event.preventDefault();
		event.stopPropagation();
	}
	
	@UiField
	VerticalPanel mainVerticalPanel;
	
	@UiField
	VerticalPanel roleNameVerticalPanel;

	/*@UiField
	VerticalPanel afternoonVerticalPanel;*/
	
	@UiField
	FocusPanel mainFocusPanel;

	private boolean isMorning;

	private List<StandardizedRoleProxy> morningRoleList;

	private List<StandardizedRoleProxy> afterNoonRoleList;
	
	public TrainingSuggestionViewImpl() {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		morningRoleList = new ArrayList<StandardizedRoleProxy>();
		afterNoonRoleList = new ArrayList<StandardizedRoleProxy>();
		
		moreButton.addStyleName("moreButton");
		
		mainFocusPanel.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				
			Log.info("onMouseOver called of suggestion view");
			
			String titleText="";
			
			if(getIsMorning()){
				
				for( StandardizedRoleProxy standardizedRoleProxy :  getMorningRoleList()){
						titleText+=(standardizedRoleProxy.getShortName() +" - " + standardizedRoleProxy.getLongName() + "\n");
					}
				
			}else{
				
				for( StandardizedRoleProxy standardizedRoleProxy :  getAfterNoonRoleList()){
						titleText+=(standardizedRoleProxy.getShortName() +" - " + standardizedRoleProxy.getLongName() + "\n");
					}
				
				}
			
				int totalWidget =roleNameVerticalPanel.getWidgetCount();
				for(int widgetCount=0; widgetCount < totalWidget ;widgetCount++){
					roleNameVerticalPanel.getWidget(widgetCount).setTitle(new HTML(titleText).getHTML());
				}
			}
		});
		
	}
	
	@UiHandler(value={"moreButton"})
	public void mouseOverEventOfButton(MouseOverEvent event)
	{
		event.preventDefault();
		event.stopPropagation();
	}
	
	@UiHandler("moreButton")
	public void moreButtonClicked(ClickEvent event){
		Log.info("More button clicked");
		if(getIsMorning()){
			VerticalPanel vpanel =poupView.getVerticalPanel();
			vpanel.clear();
			for(StandardizedRoleProxy standardizedRoleProxy : getMorningRoleList()){
				Label label = new Label(standardizedRoleProxy.getShortName());
				vpanel.add(label);
			}
			poupView.show();
			poupView.setPopupPosition(event.getClientX()-50,event.getClientY()-100);
			
		}else{
			
			VerticalPanel vpanel =poupView.getVerticalPanel();
			vpanel.clear();
			for(StandardizedRoleProxy standardizedRoleProxy : getAfterNoonRoleList()){
				Label label = new Label(standardizedRoleProxy.getShortName());
				vpanel.add(label);
			}
			poupView.show();
			poupView.setPopupPosition(event.getClientX()-50,event.getClientY()-100);
		}
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}


	@Override
	public void setIsMorning(boolean isMorning) {
		this.isMorning=isMorning;
	}

	public Boolean getIsMorning(){
		return this.isMorning;
	}

	@Override
	public void setMorningRoleList(List<StandardizedRoleProxy> morningRolesList) {
		this.morningRoleList=morningRolesList;
	}

	@Override
	public List<StandardizedRoleProxy> getMorningRoleList(){
		return this.morningRoleList;
	}

	@Override
	public void setAfterNoonRoleList(List<StandardizedRoleProxy> afterNoonRolesList) {
		this.afterNoonRoleList=afterNoonRolesList;
	}

	@Override
	public List<StandardizedRoleProxy> getAfterNoonRoleList(){
		return this.afterNoonRoleList;
	}

	@Override
	public VerticalPanel getRoleNameVerticalPanel() {
		return this.roleNameVerticalPanel;
	}


	@Override
	public IconButton getMoreButton() {
		return moreButton;
	}
	
}
