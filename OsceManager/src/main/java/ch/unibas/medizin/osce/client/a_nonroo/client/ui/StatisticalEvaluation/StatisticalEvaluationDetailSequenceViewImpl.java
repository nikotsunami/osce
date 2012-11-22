package ch.unibas.medizin.osce.client.a_nonroo.client.ui.StatisticalEvaluation;

import ch.unibas.medizin.osce.client.managed.request.AnswerProxy;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.OscePostProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceSequenceProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatisticalEvaluationDetailSequenceViewImpl  extends Composite implements StatisticalEvaluationDetailSequenceView{

	
	private static StatisticalEvaluationDetailSequenceViewImplUiBinder uiBinder = GWT.create(StatisticalEvaluationDetailSequenceViewImplUiBinder.class);

	interface StatisticalEvaluationDetailSequenceViewImplUiBinder extends UiBinder<Widget, StatisticalEvaluationDetailSequenceViewImpl> {
	}

	private Delegate delegate;
	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	
	private OsceSequenceProxy osceSequenceProxy;
	
	private OsceDayProxy osceDayProxy;
	
	private AnswerProxy answerProxy;
	
	private OscePostProxy oscePostProxy;
	
	private boolean isSequencePanel=false;
	
	public boolean isSequencePanel() {
		return isSequencePanel;
	}


	public void setSequencePanel(boolean isSequencePanel) {
		this.isSequencePanel = isSequencePanel;
	}

	private boolean isPostPanel=false;
	
	
	
	public boolean isPostPanel() {
		return isPostPanel;
	}


	public void setPostPanel(boolean isPostPanel) {
		this.isPostPanel = isPostPanel;
	}


	public OscePostProxy getOscePostProxy() {
		return oscePostProxy;
	}


	public void setOscePostProxy(OscePostProxy oscePostProxy) {
		this.oscePostProxy = oscePostProxy;
	}


	public AnswerProxy getAnswerProxy() {
		return answerProxy;
	}


	public void setAnswerProxy(AnswerProxy answerProxy) {
		this.answerProxy = answerProxy;
	}


	public OsceDayProxy getOsceDayProxy() {
		return osceDayProxy;
	}


	public void setOsceDayProxy(OsceDayProxy osceDayProxy) {
		this.osceDayProxy = osceDayProxy;
	}


	public OsceSequenceProxy getOsceSequenceProxy() {
		return osceSequenceProxy;
	}
	
	@UiField
	HTMLPanel mainPanel;

	public HTMLPanel getMainPanel() {
		return mainPanel;
	}


	public void setOsceSequenceProxy(OsceSequenceProxy osceSequenceProxy) {
		this.osceSequenceProxy = osceSequenceProxy;
	}

	@UiField
	Image minMaxImage;
	
	@UiField
	DisclosurePanel sequenceDisclosurePanel;

	public DisclosurePanel getSequenceDisclosurePanel() {
		return sequenceDisclosurePanel;
	}

	@UiField
	Label sequenceLbl;
	
	@UiField
	HorizontalPanel postViewHP;
	
	@UiField
	Label sumPerSequenceLbl;
	
	@UiField
	VerticalPanel disclosureVP;
	
	@UiField
	TableElement sequenceHeader;
	
	@UiField
	HorizontalPanel fourthColumnHP;
	
	public HorizontalPanel getFourthColumnHP() {
		return fourthColumnHP;
	}


	public TableElement getSequenceHeader() {
		return sequenceHeader;
	}

	private CourseProxy courseProxy;
	
	public CourseProxy getCourseProxy() {
		return courseProxy;
	}


	public void setCourseProxy(CourseProxy courseProxy) {
		this.courseProxy = courseProxy;
	}


	public VerticalPanel getDisclosureVP() {
		return disclosureVP;
	}


	public Label getSumPerSequenceLbl() {
		return sumPerSequenceLbl;
	}


	public HorizontalPanel getPostViewHP() {
		return postViewHP;
	}


	public Label getSequenceLbl() {
		return sequenceLbl;
	}

	@UiField
	HorizontalPanel postDataHP;
	
	public HorizontalPanel getPostDataHP() {
		return postDataHP;
	}

	OsceConstants constants = GWT.create(OsceConstants.class);
	
	public StatisticalEvaluationDetailSequenceViewImpl() 
	{
		Log.info("Call StatisticalEvaluationDetailSequenceViewImpl");
		initWidget(uiBinder.createAndBindUi(this));
		sequenceDisclosurePanel.addStyleName("custom-disclosure");
		
	}


	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@UiHandler("sequenceLbl")
	public void sequenceLblClickHandler(ClickEvent event)
	{
		/*if(sequenceDisclosurePanel.isOpen())
		{
			sequenceDisclosurePanel.setOpen(false);
			minMaxImage.setResource(uiIcons.triangle1East());
		}
		else
		{
			sequenceDisclosurePanel.setOpen(true);
			minMaxImage.setResource(uiIcons.triangle1South());
		}*/
	}
	
	@UiHandler("minMaxImage")
	public void minMaxImageClickHandler(ClickEvent event)
	{
		/*if(sequenceDisclosurePanel.isOpen())
		{
			sequenceDisclosurePanel.setOpen(false);
			minMaxImage.setResource(uiIcons.triangle1East());
		}
		else
		{
			sequenceDisclosurePanel.setOpen(true);
			minMaxImage.setResource(uiIcons.triangle1South());
		}*/
	}
	
	@UiHandler("sequenceDisclosurePanel")
	public void sequenceDisclosurePanelOpen(OpenEvent<DisclosurePanel> event)
	{
		Log.info("sequenceDisclosurePanelHandler");
	//	if(osceSequenceProxy != null && isSequencePanel())
			delegate.sequenceDisclosurePanelOpen(this);
		
			
		/*else if(courseProxy !=null)
		{
			delegate.parcourDisclosurePanelOpen(this);
		}*/
	}
	
	public Label createPostDataLabel()
	{
		
		Label l=new Label();
		l.addStyleName("postData");
		l.setWordWrap(true);
		return l;
	}
	
	public Widget asWidget() {
		return this;
	}
}
