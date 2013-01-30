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
public class SummoningsPopupViewImpl extends PopupPanel implements SummoningsPopupView {

//	private static SummoningsPopupViewUiBinder uiBinder = GWT
//			.create(SummoningsPopupViewUiBinder.class);

	private static final Binder BINDER = GWT.create(Binder.class);
	
	interface Binder extends UiBinder<Widget, SummoningsPopupViewImpl> {
	}

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
	
	@UiField
	HeadingElement please;
	
	@UiField
	SpanElement mailTemplate;
	
	@UiField
	SpanElement summoningsToName;
	
	@UiField
	SpanElement summoningsFromName;
	
	@UiField
	SpanElement summoningsAssignment;
	
	@UiField
	SpanElement varFrom;
	
	@UiField
	SpanElement varTo;
	
	@UiField
	SpanElement varAssignment;
	
	@UiField
	HeadingElement onlyIfExaminer;
	
	@UiField
	SpanElement assignmentFormatHead;
	
	@UiField
	SpanElement assignmentFormat;
	
	@UiField(provided = true)
	RichTextArea message;
	
	@UiField(provided = true)
	RichTextToolbar messageToolbar;
	
	@UiField
	public Button sendMailButton;
	
	@UiField
	public Button saveTemplateButton;

	@UiField
	public Button restoreTemplateButton;
	
	@UiField
	public IconButton closeButton;
	
	@UiField
	public ListBox semesterList;
	
	@UiField
	public Button loadTemplateButton;
	
	@UiHandler("sendMailButton")
	public void sendMailButtonClicked(ClickEvent event) {
		// TODO export action
		
	}
	
	@UiHandler("saveTemplateButton")
	public void saveTemplateButtonClicked(ClickEvent event) {
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
	
	public SummoningsPopupViewImpl() {
		
		super(false,true);
		this.setGlassEnabled(true);
		message = new RichTextArea();
		message.setSize("100%", "14em");
		messageToolbar = new RichTextToolbar(message);
		messageToolbar.setWidth("100%");
		this.addStyleName("printPopupPanel");
		init();
		add(BINDER.createAndBindUi(this));
		OsceConstants constants = GWT.create(OsceConstants.class);
		
		mailTemplate.setInnerText(constants.summoningsTpl());
		sendMailButton.setText(constants.summoningsSend());
		saveTemplateButton.setText(constants.summoningsSaveTpl());
		loadTemplateButton.setText(constants.summoningsLoadTpl());
		restoreTemplateButton.setText(constants.summoningsRestoreTpl());
		please.setInnerText(constants.summoningsPlease());
		
		varAssignment.setInnerText(constants.summoningsVarAssignment());
		varTo.setInnerText(constants.summoningsVarTo());
		varFrom.setInnerText(constants.summoningsVarFrom());
		
		summoningsAssignment.setInnerText(constants.summoningsAssignment());
		summoningsToName.setInnerText(constants.summoningsToName());
		summoningsFromName.setInnerText(constants.summoningsFromName());
		onlyIfExaminer.setInnerText(constants.summoningsOnlyIfExaminer());
		assignmentFormatHead.setInnerText(constants.summoningsAssignmentFormatHead());
		assignmentFormat.setInnerText(constants.summoningsAssignmentFormat());
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

	@Override
	public Button getSendMailButton() {
		return sendMailButton;
	}
	
	@Override
	public Button getSaveTemplateButton() {
		return saveTemplateButton;
	}

	@Override
	public Button getRestoreTemplateButton() {
		return restoreTemplateButton;
	}
	
	@Override
	public ListBox getSemesterList() {
		return semesterList;
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
	
}
