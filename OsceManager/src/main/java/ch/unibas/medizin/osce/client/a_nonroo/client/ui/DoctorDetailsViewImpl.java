/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.Date;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceDayProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author niko2
 *
 */
public class DoctorDetailsViewImpl extends Composite implements  DoctorDetailsView{

	private static DoctorDetailsViewImplUiBinder uiBinder = GWT
			.create(DoctorDetailsViewImplUiBinder.class);

	interface DoctorDetailsViewImplUiBinder extends
			UiBinder<Widget, DoctorDetailsViewImpl> {
	}	

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField
	TabPanel doctorPanel;
    @UiField
    IconButton edit;
    @UiField
    IconButton delete;

    private Delegate delegate;
	

//    @UiField
//    SpanElement id;
//    @UiField
//    SpanElement version;
    @UiField
    SpanElement gender;
    @UiField
    SpanElement title;
    @UiField
    SpanElement name;
    @UiField
    SpanElement preName;
    @UiField
    Anchor email;
    @UiField
    SpanElement telephone;
    @UiField
    SpanElement clinic;
    @UiField
    SimplePanel officePanel;
    
    @UiField
    SpanElement labelGender;
    @UiField
    SpanElement labelTitle;
    @UiField
    SpanElement labelName;
    @UiField
    SpanElement labelPreName;
    @UiField
    SpanElement labelEmail;
    @UiField
    SpanElement labelTelephone;
    @UiField
    SpanElement labelClinic;
    
    //Module : 6
    @UiField (provided = true)
	CellTable<OsceDayProxy> oscetable;
    
	@UiField(provided = true)
	public SimplePager pager;
	
	@UiField (provided = true)
	CellTable<RoleParticipantProxy> roletable;
    
	@UiField(provided = true)
	public SimplePager rolepager;
	
	protected ArrayList<String> paths = new ArrayList<String>();
	
	protected ArrayList<String> paths1 = new ArrayList<String>();
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
	public DoctorDetailsViewImpl() {
		
		//Module 6 START
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		oscetable = new CellTable<OsceDayProxy>(5, tableResources);

		SimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
				true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		
		CellTable.Resources tableResources1 = GWT
				.create(MyCellTableResources.class);
		roletable = new CellTable<RoleParticipantProxy>(5, tableResources1);

		SimplePager.Resources pagerResources1 = GWT
				.create(MySimplePagerResources.class);
		rolepager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources1,
				true, OsMaConstant.TABLE_JUMP_SIZE, true);

		//Module 6 END
		
		initWidget(uiBinder.createAndBindUi(this));
		OsceConstants constants = GWT.create(OsceConstants.class);
		
		doctorPanel.selectTab(0);

		doctorPanel.getTabBar().setTabText(0, constants.generalInformation());
		doctorPanel.getTabBar().setTabText(1, constants.officeDetails());
		doctorPanel.getTabBar().setTabText(2, constants.oscedoc());
		doctorPanel.getTabBar().setTabText(3, constants.role());
		
		TabPanelHelper.moveTabBarToBottom(doctorPanel);
		
		edit.setText(constants.edit());
		delete.setText(constants.delete());
		
		labelGender.setInnerText(constants.gender() + ":");
		labelTitle.setInnerText(constants.title() + ":");
		labelName.setInnerText(constants.name() + ":");
		labelPreName.setInnerText(constants.preName() + ":");
		labelEmail.setInnerText(constants.email() + ":");
		labelTelephone.setInnerText(constants.telephone() + ":");
		labelClinic.setInnerText(constants.clinic() + ":");
		
		doctorPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (delegate != null)
					delegate.storeDisplaySettings();
			}
		});
		
		initTableOsce();
		
		initRoleTable();
	}
  

    @Override
    public SimplePanel getOfficeDetailsPanel() {
		return officePanel;
	}

	DoctorProxy proxy;

    @UiField
    SpanElement displayRenderer;

	private Presenter presenter;

	public void setValue(DoctorProxy proxy) {
		this.proxy = proxy;
		gender.setInnerText(proxy.getGender() == null ? "" : new EnumRenderer<Gender>().render(proxy.getGender()));
		title.setInnerText(proxy.getTitle() == null ? "" : String.valueOf(proxy.getTitle()));
		name.setInnerText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
		preName.setInnerText(proxy.getPreName() == null ? "" : String.valueOf(proxy.getPreName()));
		email.setHref("mailto:" + (proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		email.setText((proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail())));
		telephone.setInnerText(proxy.getTelephone() == null ? "" : String.valueOf(proxy.getTelephone()));
		clinic.setInnerText(proxy.getClinic() == null ? "" : String.valueOf(proxy.getClinic().getName()));
		displayRenderer.setInnerText(ch.unibas.medizin.osce.client.managed.ui.DoctorProxyRenderer.instance().render(proxy));
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	@Override
	public void setPresenter(Presenter DoctorActivity) {
		this.presenter = DoctorActivity;

	}

	public Widget asWidget() {
		return this;
	}

	public boolean confirm(String msg) {
		return Window.confirm(msg);
	}

	public DoctorProxy getValue() {
		return proxy;
	}

	@UiHandler("delete")
	public void onDeleteClicked(ClickEvent e) {
		delegate.deleteClicked();
	}

	@UiHandler("edit")
	public void onEditClicked(ClickEvent e) {
		delegate.editClicked();
	}

	@Override
	public void setSelectedDetailsTab(int detailsTab) {
		doctorPanel.selectTab(detailsTab);
	}

	@Override
	public int getSelectedDetailsTab() {
		return doctorPanel.getTabBar().getSelectedTab();
	}

	//Module : 6 Start
	public void initTableOsce()
	{
		paths.add("oscename");
		oscetable.addColumn(new TextColumn<OsceDayProxy>() {
			Renderer<String> renderer = new AbstractRenderer<String>() {

				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceDayProxy object) {
				return renderer.render(object.getOsce().getName());
			}
		}, constants.oscedoc());
		
		paths.add("starttime");
		oscetable.addColumn(new TextColumn<OsceDayProxy>() {

			Renderer<Date> renderer = new AbstractRenderer<Date>() {

				public String render(Date obj) {
					return obj == null ? "" : String.valueOf(DateTimeFormat.getFormat("HH:mm").format(obj).substring(0,5));
				}
			};

			@Override
			public String getValue(OsceDayProxy object) {
				return renderer.render(object.getTimeStart());
			}
		}, constants.starttimedoc());
		
		paths.add("endtime");
		oscetable.addColumn(new TextColumn<OsceDayProxy>() {
			Renderer<Date> renderer = new AbstractRenderer<Date>() {
				public String render(Date obj){
					return obj == null ? "" : String.valueOf(DateTimeFormat.getFormat("HH:mm").format(obj).substring(0,5));
				}
			};
			
			@Override
			public  String getValue(OsceDayProxy object)
			{
				return renderer.render(object.getTimeEnd());
			}
		}, constants.endtimedoc());

	}
	
	public void initRoleTable()
	{
		paths1.add("spshortname");
		roletable.addColumn(new TextColumn<RoleParticipantProxy>() {
			Renderer<String> renderer = new AbstractRenderer<String>() {

				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleParticipantProxy object) {
				return renderer.render(object.getStandardizedRole().getShortName());
			}
		}, constants.roleAcronym());
		
		paths1.add("version");
		roletable.addColumn(new TextColumn<RoleParticipantProxy>() {

			Renderer<Integer> renderer = new AbstractRenderer<Integer>() {

				public String render(Integer obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleParticipantProxy object) {
				return renderer.render(object.getVersion());
			}
		}, constants.version());
		
		paths1.add("roletype");
		roletable.addColumn(new TextColumn<RoleParticipantProxy>() {
			Renderer<String> renderer = new AbstractRenderer<String>() {
				public String render(String obj){
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public  String getValue(RoleParticipantProxy object)
			{
				return renderer.render(object.getType().name());
			}
		}, constants.roleType());
		
	}

	@Override
	public CellTable<OsceDayProxy> getTable() {
		return oscetable;
	}


	@Override
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}


	@Override
	public CellTable<RoleParticipantProxy> getRoleTable() {
		
		return roletable;
	}
	
	//Module : 6 End
}
