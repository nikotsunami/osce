package ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public  class MessageConfirmationDialogBox extends DialogBox{

	HorizontalPanel hp;
	 Button yesBtn;
	
	VerticalPanel vp;
	
	
	public static MessageConfirmationDialogBox dialogBox;
	
	private static final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public  Button  getYesBtn() {
		return this.yesBtn;
	}

	

	public  Button getNoBtnl() {
		return this.noBtnl;
	}

	

	 Button noBtnl;
	
	public MessageConfirmationDialogBox(String msg) {
		// TODO Auto-generated constructor stub
		
		vp=new VerticalPanel();
		hp=new HorizontalPanel();
		vp.add(new HTML(msg));
		yesBtn=new Button();
		noBtnl=new Button();
		yesBtn.setText(constants.yes());
		noBtnl.setText(constants.no());
		hp.add(yesBtn);
		hp.add(noBtnl);
		hp.setSpacing(10);
		vp.add(hp);
		super.setText("Warning");
		this.add(vp);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		
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
	public void showDialog()
	{
		super.center();
		super.show();
	}
}
