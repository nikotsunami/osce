/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class SummoningsViewImpl extends Composite implements SummoningsView, MenuClickHandler {

	private static SummoningsViewUiBinder uiBinder = GWT
			.create(SummoningsViewUiBinder.class);

	interface SummoningsViewUiBinder extends UiBinder<Widget, SummoningsViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
	
	
	@UiField
	HTMLPanel containerHTMLPanel;
	
	@UiField
	Label lblSP;
	
	@UiField
	VerticalPanel vpSP;
	
	@UiField
	Button btnSendMailSP;
	
	@UiField
	Button btnPrintCopySP;
	
	@UiField
	RadioButton rbSelectedSP;
	
	@UiField
	RadioButton rbAllSP;
	
	@UiField
	Label lblExaminor;
	
	@UiField
	VerticalPanel vpExaminor;
	
	@UiField
	Button btnSendMailExaminor;
	
	@UiField
	Button btnPrintCopyExaminor;
	
	@UiField
	RadioButton rbSelectedExaminor;
	
	@UiField
	RadioButton rbAllExaminor;
	
	
	@UiHandler("btnSendMailSP")
	public void btnSendMailSPClicked(ClickEvent event)
	{
		delegate.sendEmailtoSP(event);
	}
	
	@UiHandler("btnPrintCopySP")
	public void btnPrintCopySPClicked(ClickEvent event)
	{
		delegate.printCopyforSP();
	}
	
	@UiHandler("btnSendMailExaminor")
	public void btnSendMailExaminorClicked(ClickEvent event)
	{
		delegate.sendEmailtoExaminor();
	}
	
	@UiHandler("btnPrintCopyExaminor")
	public void btnPrintCopyExaminorClicked(ClickEvent event)
	{
		delegate.printCopyforExaminor();
	}
	
	
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
	
	public SummoningsViewImpl() {
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		
		int panelMarginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		containerHTMLPanel.getElement().setAttribute("style", "margin-left: "+panelMarginLeft+"px;");
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		
		Log.info("Call init()");
		
//		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		lblExaminor.setText(constants.examiner());
		lblSP.setText(constants.standardizedPatient());
		rbAllExaminor.setText(constants.summoningsAll());
		rbAllSP.setText(constants.summoningsAll());
		rbSelectedExaminor.setText(constants.summoningsSelected());
		rbSelectedSP.setText(constants.summoningsSelected());
		btnSendMailSP.setText(constants.summoningsMailSp());
		btnPrintCopySP.setText(constants.summoningsPrintSp());
		btnSendMailExaminor.setText(constants.summoningsMailExa());
		btnPrintCopyExaminor.setText(constants.summoningsPrintExa());
		
		rbSelectedSP.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				if(rbSelectedSP.isEnabled())
				{
					Log.info("rbSelected");										
				}
			}
		});
		
		rbAllSP.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(rbAllSP.isEnabled())
				{
					Log.info("rbAllSP");
					delegate.clickAllSP();
				}
			}
		});
		
		rbSelectedExaminor.addClickHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				if(rbSelectedExaminor.isEnabled())
				{
					Log.info("rbSelectedExaminor");					
				}
			}
		});

		rbAllExaminor.addClickHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				if(rbAllExaminor.isEnabled())
				{
					Log.info("rbAllExaminor");	
					delegate.clickAllExaminor();
				}
			}
		});
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	

	@Override
	public VerticalPanel getVpSP() 
	{
		return this.vpSP;
	}

	@Override
	public VerticalPanel getVpExaminor() 
	{	
		return this.vpExaminor;
	}
	
	@Override 
	public Label getSpLabel()
	{
		return this.lblSP;
	}
	
	@Override 
	public Label getExaminorLabel()
	{
		return this.lblExaminor;
	}
	
	@Override
	public RadioButton getSpRb()
	{
		return this.rbSelectedSP;
	}
	
	@Override
	public RadioButton getExaminerRb()
	{
		return this.rbSelectedExaminor;
	}
	
	@Override
	public RadioButton getSpAllRb()
	{
		return this.rbAllSP;
	}
	
	@Override
	public RadioButton getExaminerAllRb()
	{
		return this.rbAllExaminor;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
		int panelMarginLeft = ResolutionSettings.getRightWidgetMarginLeft();
		containerHTMLPanel.getElement().setAttribute("style", "margin-left: "+panelMarginLeft+"px;");
	}


}
