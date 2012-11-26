/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.managed.request.CourseProxy;
import ch.unibas.medizin.osce.client.managed.ui.CourseProxyRenderer;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class IndividualSchedulesDetailsViewImpl extends Composite implements IndividualSchedulesDetailsView, MenuClickHandler {

	private static IndividualSchedulesDetailsViewUiBinder uiBinder = GWT.create(IndividualSchedulesDetailsViewUiBinder.class);

	interface IndividualSchedulesDetailsViewUiBinder extends UiBinder<Widget, IndividualSchedulesDetailsViewImpl> {
	}

	private Delegate delegate;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;
		
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	VerticalPanel vpStudent;
	
	@UiField
	Button btnPrintCopyStudent;
	
	@UiField
	RadioButton rbSelectedStud;
	
	@UiField
	RadioButton rbAllStud;
	
	@UiField
	VerticalPanel vpSP;
	
	@UiField
	Button btnPrintCopySP;
	
	@UiField
	RadioButton rbSelectedSP;
	
	@UiField
	RadioButton rbAllSP;
	
	@UiField
	VerticalPanel vpExaminor;
	
	@UiField
	Button btnPrintCopyExaminor;
	
	@UiField
	RadioButton rbSelectedExaminor;
	
	@UiField
	RadioButton rbAllExaminor;
	
	@UiField(provided = true)
    ValueListBox<CourseProxy> parcourListBox = new ValueListBox<CourseProxy>((Renderer<CourseProxy>) GWT.create(CourseProxyRenderer.class));
	
	
	/*@UiField
	SplitLayoutPanel splitLayoutPanel;*/
	@Override
	public ValueListBox<CourseProxy> getParcourListBox() {
		return parcourListBox;
	}

	@Override
	public void setParcourListBox(ValueListBox<CourseProxy> parcourListBox) {
		this.parcourListBox = parcourListBox;
	}

	@UiField
	VerticalPanel dataPanelVPStud;
	
	@UiField
	VerticalPanel dataPanelVPSP;
	
	@UiField
	VerticalPanel dataPanelVPExaminer;
	
	@UiField
	DisclosurePanel disclosureDataPanelStud;
	
	@UiField
	DisclosurePanel disclosureDataPanelSP;
	
	@UiField
	DisclosurePanel disclosureDataPanelExaminer;
	
	
	
	@UiHandler("btnPrintCopyStudent")
	public void btnPrintCopyStudentClicked(ClickEvent event)
	{
		delegate.printCopyforStud(event);
	}
	
	
	@UiHandler("btnPrintCopySP")
	public void btnPrintCopySPClicked(ClickEvent event)
	{
		delegate.printCopyforSP(event);
	}
	
	
	@UiHandler("btnPrintCopyExaminor")
	public void btnPrintCopyExaminorClicked(ClickEvent event)
	{
		delegate.printCopyforExaminor(event);
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
	public IndividualSchedulesDetailsViewImpl() 
	{
		Log.info("Call IndividualSchedulesDetailsViewImpl");
		initWidget(uiBinder.createAndBindUi(this));
		
		int height = ResolutionSettings.getRightWidgetHeight() - 55;
		scrollPanel.setHeight(height+"px");
		
		init();
	}

	public String[] getPaths() 
	{
		return paths.toArray(new String[paths.size()]);
	}

	public void init() 
	{
//		 String panelWidth = (OsMaMainNav.getMenuStatus() == 0) ? "1350px" : "1130px";
//		 scrollPanel.setWidth(panelWidth);
		
		//DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 50px; right: 5px; bottom: 0px;");
		/*disclosureDataPanelStud.addOpenHandler<DisclosurePanel>(new OpenHandler<DisclosurePanel>() 
		{

			  @Override
			  public void onOpen(OpenEvent<DisclosurePanel> event) 
			  {
			    checkbox.setValue(true);
			  }
		});*/
		OsceConstants constants = GWT.create(OsceConstants.class);
		
		rbSelectedStud.setText(constants.selected());
		rbSelectedExaminor.setText(constants.selected());
		rbSelectedSP.setText(constants.selected());
		rbAllExaminor.setText(constants.all());
		rbAllSP.setText(constants.all());
		rbAllStud.setText(constants.all());
		
		btnPrintCopyExaminor.setText(constants.summoningsPrintExa());
		btnPrintCopySP.setText(constants.summoningsPrintSp());
		btnPrintCopyStudent.setText(constants.schedulesPrintStud());
		
		disclosureDataPanelExaminer.addCloseHandler(new CloseHandler<DisclosurePanel>() 
		{
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) 
			{
				disclosureDataPanelExaminer.setOpen(true);
			}
		});
		
		disclosureDataPanelSP.addCloseHandler(new CloseHandler<DisclosurePanel>() 
		{
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) 
			{
				disclosureDataPanelSP.setOpen(true);
			}
		});
		
		disclosureDataPanelStud.addCloseHandler(new CloseHandler<DisclosurePanel>() 
		{
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) 
			{
				disclosureDataPanelStud.setOpen(true);
			}
		});
		
				
					
			
		Log.info("Call init()");
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
		
		rbSelectedStud.addClickHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				if(rbSelectedStud.isEnabled())
				{
					Log.info("rbSelectedStud");					
				}
			}
		});

		rbAllStud.addClickHandler(new ClickHandler() 
		{
	
			@Override
			public void onClick(ClickEvent event) 
			{
				if(rbAllStud.isEnabled())
				{
					Log.info("rbAllStud");		
					delegate.clickAllStud();
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public VerticalPanel getVpStudent() 
	{		
		return this.vpStudent;
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
	public RadioButton getSpRb()
	{
		return this.rbSelectedSP;
	}
	
	@Override
	public RadioButton getStudRb()
	{
		return this.rbSelectedStud;
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
	public RadioButton getStudAllRb()
	{
		return this.rbAllStud;
	}
	
	@Override
	public RadioButton getExaminerAllRb()
	{
		return this.rbAllExaminor;
	}
	
	@Override
	public VerticalPanel getDataVPStud() 
	{	
		return this.dataPanelVPStud;
	}
	@Override
	public VerticalPanel getDataVPSP() 
	{	
		return this.dataPanelVPSP;
	}
	@Override
	public VerticalPanel getDataVPExaminer() 
	{	
		return this.dataPanelVPExaminer;
	}

	@Override
	public DisclosurePanel getDisclosureStudentPanel()
	{
		return this.disclosureDataPanelStud;
	}
	
	@Override
	public DisclosurePanel getDisclosureSPPanel()
	{
		return this.disclosureDataPanelSP;
	}
	
	@Override
	public DisclosurePanel getDisclosureExaminerPanel()
	{
		return this.disclosureDataPanelExaminer;
	}
	
	@Override
	public void onMenuClicked(MenuClickEvent event) {
		
		OsMaMainNav.setMenuStatus(event.getMenuStatus());
		
		int height = ResolutionSettings.getRightWidgetHeight() - 55;
		scrollPanel.setHeight(height+"px");
	}
}
