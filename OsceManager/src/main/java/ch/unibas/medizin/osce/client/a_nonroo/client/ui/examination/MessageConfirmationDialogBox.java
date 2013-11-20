package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;



import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public  class MessageConfirmationDialogBox extends DialogBox{
	HorizontalPanel hp;
	IconButton yesBtn;
	IconButton noBtnl;
	IconButton mayBeBtn;
	VerticalPanel vp;
	Label msgLbl;
	
	public static MessageConfirmationDialogBox dialogBox;
	
	private static final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public  Button  getYesBtn() {
		return this.yesBtn;
	}
	
	public IconButton getNoBtnl() {
		return this.noBtnl;
	}
	
	public Button getMayBeBtn() {
		return mayBeBtn;
	}
	
	public MessageConfirmationDialogBox(String caption) {
		
		vp=new VerticalPanel();
		hp=new HorizontalPanel();
		//vp.add(new HTML(msg));
		yesBtn=new IconButton();
		noBtnl=new IconButton();
		mayBeBtn = new IconButton();
		yesBtn.setIcon("check");
		noBtnl.setIcon("closethick");
		mayBeBtn.setIcon("cancel");
		yesBtn.setText(constants.yes());
		noBtnl.setText(constants.no());
		mayBeBtn.setVisible(false);
		
		msgLbl=new Label();
		vp.add(msgLbl);
		hp.add(yesBtn);
		hp.add(noBtnl);
		hp.add(mayBeBtn);
		hp.setSpacing(10);
		vp.add(hp);
		super.getCaption().asWidget().addStyleName("confirmbox");
		
		//super.setText(constants.warning());
		super.setText(caption);
		this.add(vp);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		dialogBox=this;
		this.getElement().getStyle().setZIndex(3);
	}
	
	public static MessageConfirmationDialogBox create(String msg)
	{
		if(dialogBox==null)
		{	
			dialogBox=new MessageConfirmationDialogBox(msg);
			dialogBox.getYesBtn().setText(constants.yes());
			dialogBox.getNoBtnl().setText(constants.no());
			dialogBox.hide();
		}
		//dialogBox.show();
		
		
		
		return dialogBox;
	}
	
	public void showDialog() {
		super.center();
		this.getElement().getStyle().setZIndex(3);
		super.show();
	}
	
	public void showYesNoDialog(String str)
	{
		msgLbl.setText(str);
		this.getNoBtnl().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				dialogBox.hide();
			}
		});
		
		super.center();
		this.getElement().getStyle().setZIndex(3);
		super.show();
	}
	
	public void showConfirmationDialog()
	{
		this.getYesBtn().setVisible(false);
		this.getNoBtnl().setIcon("check");
		
		this.getNoBtnl().setText(constants.okBtn());
		this.getCaption().setText(constants.success());
		this.showDialog();
		
		this.getNoBtnl().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
			
				dialogBox.hide();	
			}
		});
	}
	
	public void showConfirmationDialog(String msg)
	{
		this.getYesBtn().setVisible(false);
		this.getNoBtnl().setIcon("check");
		
		msgLbl.setText(msg);
		
		this.getNoBtnl().setText(constants.okBtn());
		//this.getCaption().setText(constants.success());
		this.showDialog();
		this.getNoBtnl().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
			
				dialogBox.hide();	
			}
		});
	}
	
	public void showYesNoMayBeDialog(String str)
	{
		msgLbl.setText(str);
		mayBeBtn.setVisible(true);
		this.getNoBtnl().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				dialogBox.hide();
			}
		});
		
		super.center();
		this.getElement().getStyle().setZIndex(3);
		super.show();
	}
}
