package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.OscePostBlueprintProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OscePostViewImpl  extends Composite implements  OscePostView{
	
	private Delegate delegate;
	
	private  final OsceConstants constants = GWT.create(OsceConstants.class);
	
	  MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Warning");
	  
	
	private final OscePostViewImpl oscePostViewImpl;
	
	public OscePostBlueprintProxy oscePostBlueprintProxy;
	
	
	
	public OscePostBlueprintProxy getOscePostBlueprintProxy() {
		return oscePostBlueprintProxy;
	}

	public void setOscePostBlueprintProxy(
			OscePostBlueprintProxy oscePostBlueprintProxy) {
		this.oscePostBlueprintProxy = oscePostBlueprintProxy;
	}

	public OscePostBlueprintProxy getOscePostBlueprintProxyNext() {
		return oscePostBlueprintProxyNext;
	}

	public void setOscePostBlueprintProxyNext(
			OscePostBlueprintProxy oscePostBlueprintProxyNext) {
		this.oscePostBlueprintProxyNext = oscePostBlueprintProxyNext;
	}
	public OscePostBlueprintProxy oscePostBlueprintProxyNext;
	
	private static OscePostViewImplUiBinder uiBinder = GWT
			.create(OscePostViewImplUiBinder.class);
	
	private boolean isAnemanis=false;
	
	public boolean isAnemanis() {
		return isAnemanis;
	}
	private boolean isGenerated=false;
	
	public void setAnemanis(boolean isAnemanis) {
		this.isAnemanis = isAnemanis;
	}

	public OscePostProxy getNextOscePostProxy() {
		return nextOscePostProxy;
	}

	public void setNextOscePostProxy(OscePostProxy nextOscePostProxy) {
		this.nextOscePostProxy = nextOscePostProxy;
	}


	private OscePostProxy nextOscePostProxy;
	
	@UiField
	Label postTypeLbl;
	
	@UiField
	HorizontalPanel oscePostSubViewHP;
	
	@UiField
	IconButton deletePost;
	
	private OscePostProxy proxy;
	
	

	public OscePostProxy getProxy() {
		return proxy;
	}

	public void setProxy(OscePostProxy proxy) {
		this.proxy = proxy;
	}

	public HorizontalPanel getOscePostSubViewHP() {
		return oscePostSubViewHP;
	}

	public void setOscePostSubViewHP(HorizontalPanel oscePostSubViewHP) {
		this.oscePostSubViewHP = oscePostSubViewHP;
	}

	public Label getPostTypeLbl() {
		return postTypeLbl;
	}

	public void setPostTypeLbl(Label postTypeLbl) {
		this.postTypeLbl = postTypeLbl;
	}

	interface OscePostViewImplUiBinder extends UiBinder<Widget, OscePostViewImpl> {
	}
	
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;
		
	}
	
	public OscePostViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		oscePostViewImpl=this;
			
			
		
		
			dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
				deletePost();
				
			}
		});
		
			dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
				return;
				
			}
		});
		
		
	}
	
	public void deletePost()
	{
		delegate.deleteOscePost(oscePostViewImpl);
	}
	
	@UiHandler("deletePost")
	public void deleteOscePost(ClickEvent event)
	{
		Log.info("Delete Clicked.");
		
		Log.info("deletePost Clicked");
		//delegate.deleteOscePost(this);
		
		
		dialogBox.showYesNoDialog(constants.deleteOsceBluePrintPost());
		//delegate.deleteOscePost(oscePostViewImpl);
		Log.info("milan");
	}
	
	// Module 5 bug Report Change
	@Override
	public IconButton getDeletePostButton()
	{
		return this.deletePost;
	}
	// E Module 5 bug Report Change	
}
