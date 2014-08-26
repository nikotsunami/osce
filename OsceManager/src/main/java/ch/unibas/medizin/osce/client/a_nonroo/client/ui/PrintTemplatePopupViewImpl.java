/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.richtext.RichTextToolbar;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class PrintTemplatePopupViewImpl extends PopupPanel implements PrintTemplatePopupView {

//	private static SummoningsPopupViewUiBinder uiBinder = GWT
//			.create(SummoningsPopupViewUiBinder.class);

	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, PrintTemplatePopupViewImpl> {
	}
	
	private OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

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
	
	@UiField(provided = true)
	RichTextArea message;
	
	@UiField(provided = true)
	RichTextToolbar messageToolbar;
	
/*	@UiField
	public Button sendMailButton;*/
	
	@UiField
	public IconButton saveTemplateButton;
	
	@UiField
	public IconButton printTemplateButton;
	
	@UiField
	public IconButton restoreTemplateButton;
	
	@UiField
	public IconButton closeButton;
	
	@UiField
	public ListBox osceList;
	
	@UiField
	public IconButton loadTemplateButton;
	
	@UiField
	SpanElement title;
	@UiField
	SpanElement varTitle;
	@UiField
	SpanElement varName;
	@UiField
	SpanElement varPreName;
	@UiField
	SpanElement varDate;
	@UiField
	SpanElement varStartTime;
	@UiField
	SpanElement varEndTime;
	@UiField
	SpanElement varOsce;
	@UiField
	SpanElement varRole;
	@UiField
	SpanElement varPost;
	@UiField
	SpanElement varRoom;
	@UiField
	SpanElement varLongBreak;
	@UiField
	SpanElement varLunchBreak;
	@UiField
	SpanElement varTitleSep;
	@UiField
	SpanElement varDaySep;
	@UiField
	SpanElement varScheduleSep;
	@UiField
	SpanElement varBreakSep;
	@UiField
	SpanElement varScriptSep;
	@UiField
	SpanElement descTitle;
	@UiField
	SpanElement descName;
	@UiField
	SpanElement descPreName;
	@UiField
	SpanElement descDate;
	@UiField
	SpanElement descStartTime;
	@UiField
	SpanElement descEndTime;
	@UiField
	SpanElement descOsce;
	@UiField
	SpanElement descRole;
	@UiField
	SpanElement descPost;
	@UiField
	SpanElement descRoom;
	@UiField
	SpanElement descLongBreak;
	@UiField
	SpanElement descLunchBreak;
	@UiField
	HeadingElement please;
	
	@UiField
	SpanElement shortRole;
	@UiField
	SpanElement shortRoleDesc;
	@UiField
	SpanElement postNumber;
	@UiField
	SpanElement postNumberDesc;
	
	/*@UiHandler("sendMailButton")
	public void sendMailButtonClicked(ClickEvent event) {
		// TODO export action
		
	}*/
	
	@UiHandler("saveTemplateButton")
	public void saveTemplateButtonClicked(ClickEvent event) {
		// TODO export action
		
	}
	
	@UiHandler("printTemplateButton")
	public void printTemplateButtonClicked(ClickEvent event) {
		// TODO export action
		
	}
	
	@UiHandler("restoreTemplateButton")
	public void restoreTemplateButtonClicked(ClickEvent event) {
		// TODO export action
		
	}
	
	@UiHandler("closeButton")
	public void closeButtonClicked(ClickEvent event) {
		// TODO export action
		this.hide();
	}
	
	public PrintTemplatePopupViewImpl() {
		
		super(false,true);
		this.setGlassEnabled(true);
		message = new RichTextArea();
		message.setSize("100%", "14em");
		messageToolbar = new RichTextToolbar(message);
		messageToolbar.setWidth("100%");
		
//		sendMailButton.setText("Send Mail");
		
		init();
		this.addStyleName("printPopupPanel");
		add(BINDER.createAndBindUi(this));
		
		title.setInnerText(constants.tplTemplate());
		please.setInnerText(constants.summoningsPlease());
		varTitle.setInnerText(constants.tplVarTitle());
		varName.setInnerText(constants.tplVarName());
		varPreName.setInnerText(constants.tplVarPreName());
		varDate.setInnerText(constants.tplVarDate());
		varStartTime.setInnerText(constants.tplVarStartTime());
		varEndTime.setInnerText(constants.tplVarEndTime());
		varOsce.setInnerText(constants.tplVarOsce());
		varRole.setInnerText(constants.tplVarRole());
		varPost.setInnerText(constants.tplVarPost());
		varRoom.setInnerText(constants.tplVarRoom());
		varLongBreak.setInnerText(constants.tplVarLongBreak());
		varLunchBreak.setInnerText(constants.tplVarLunchBreak());
		
		varTitleSep.setInnerText(constants.tplVarTitleSep());
		varDaySep.setInnerText(constants.tplVarOsceDaySep());
		varScheduleSep.setInnerText(constants.tplVarScheduleSep());
		varBreakSep.setInnerText(constants.tplVarBreakSep());
		varScriptSep.setInnerText(constants.tplVarScriptSep());
		
		descTitle.setInnerText(constants.tplDescTitle());
		descName.setInnerText(constants.tplDescName());
		descPreName.setInnerText(constants.tplDescPreName());
		descDate.setInnerText(constants.tplDescDate());
		descStartTime.setInnerText(constants.tplDescStartTime());
		descEndTime.setInnerText(constants.tplDescEndTime());
		descOsce.setInnerText(constants.tplDescOsce());
		descRole.setInnerText(constants.tplDescRole());
		descPost.setInnerText(constants.tplDescPost());
		descRoom.setInnerText(constants.tplDescRoom());
		descLongBreak.setInnerText(constants.tplDescLongBreak());
		descLunchBreak.setInnerText(constants.tplDescLunchBreak());
		
		loadTemplateButton.setText(constants.tplLoadTemplate());
		saveTemplateButton.setText(constants.tplSaveTemplate());
		printTemplateButton.setText(constants.tplPrintTemplate());
		closeButton.setText(constants.close());
		restoreTemplateButton.setText(constants.tplRestoreTemplate());
		
		shortRole.setInnerText(constants.tplShortNameRole());
		shortRoleDesc.setInnerText(constants.tplShortNameRoleDesc());
		postNumber.setInnerText(constants.tplPostNumber());
		postNumberDesc.setInnerText(constants.tplPostNumberDesc());
		
		shortRole.getStyle().setDisplay(Display.NONE);
		shortRoleDesc.getStyle().setDisplay(Display.NONE);
		postNumber.getStyle().setDisplay(Display.NONE);
		postNumberDesc.getStyle().setDisplay(Display.NONE);
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// TODO implement this!
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*@Override
	public Button getSendMailButton() {
		return sendMailButton;
	}*/
	
	@Override
	public Button getSaveTemplateButton() {
		return saveTemplateButton;
	}
	
	@Override
	public Button getPrintTemplateButton() {
		return printTemplateButton;
	}

	@Override
	public Button getRestoreTemplateButton() {
		return restoreTemplateButton;
	}
	
	@Override
	public ListBox getOsceList() 
	{
		return osceList;
	}

	@Override
	public Button getLoadTemplateButton() {
		return loadTemplateButton;
	}

	@Override
	public String getMessageContent(){
		return message.getHTML();
	}
	
	@Override
	public void setMessageContent(String html){
		message.setHTML(html);
	}

	public void displayShortRoleAndPostNumberField()
	{
		shortRole.getStyle().setDisplay(Display.BLOCK);
		shortRoleDesc.getStyle().setDisplay(Display.BLOCK);
		postNumber.getStyle().setDisplay(Display.BLOCK);
		postNumberDesc.getStyle().setDisplay(Display.BLOCK);
	}
}
