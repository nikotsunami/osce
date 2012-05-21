/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;


import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.RoleDetailsViewImpl;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.FocusableValueListBox;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.TabPanelHelper;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author niko2
 *
 */
public class StandardizedRoleDetailsViewImpl extends Composite implements  StandardizedRoleDetailsView {

		
	private static StandardizedRoleDetailsViewImplUiBinder uiBinder = GWT.create(StandardizedRoleDetailsViewImplUiBinder.class);

	interface StandardizedRoleDetailsViewImplUiBinder extends UiBinder<Widget, StandardizedRoleDetailsViewImpl> {
		
	}

	private OsceConstants constants = GWT.create(OsceConstants.class);
	
	StandardizedRoleProxy proxy;


	@UiField
	DisclosurePanel roleDisclosurePanel;
	@UiField
	Image arrow;
	//SPEC End
	
	// Panels
	
	@UiField
	public TabPanel rolePanel;
		
		
	// Buttons
	@UiField
	IconButton print;
	@UiField
	IconButton edit;
	@UiField
	IconButton delete;
	
	// Labels (Fieldnames)
	
	@UiField
	SpanElement labelShortName;
	@UiField
	SpanElement labellongName;
	@UiField
	SpanElement labelroletype;
	@UiField
	SpanElement labelstudyYear;
	
	// Temp Fields
	
/*	@UiField
	SpanElement labelShortNameValue;
	@UiField
	SpanElement labellongValue;
	@UiField
	SpanElement labelroletypeValue;
	@UiField
	SpanElement labelstudyYearValue;*/
	@UiField
	public com.google.gwt.user.client.ui.Label labelLongNameHeader;
	
	
	// Fields
	@UiField
	public SpanElement shortName;
	@UiField
	public SpanElement longName;
//	@UiField(provided = true)
//	public FocusableValueListBox<RoleTypes> roleType = new FocusableValueListBox<RoleTypes>(new EnumRenderer<RoleTypes>());

	@UiField
	public SpanElement roleType;
	
	@UiField
	public SpanElement studyYear;
	
	@UiField
	public TabPanel roleSubPanel;
	/*@UiField(provided = true)
	public FocusableValueListBox<StudyYears> studyYear = new FocusableValueListBox<StudyYears>(new EnumRenderer<StudyYears>());*/

	
		
private Delegate delegate;

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

public StandardizedRoleDetailsViewImpl() 
{
	
	initWidget(uiBinder.createAndBindUi(this));	
	//rolePanel.selectTab(0);
	roleDisclosurePanel.setOpen(true);
	
	TabPanelHelper.moveTabBarToBottom(rolePanel);
	
	print.setText(constants.print());
	edit.setText(constants.edit());
	delete.setText(constants.delete());
	
	roleDisclosurePanel.setContent(rolePanel);
	roleDisclosurePanel.setStyleName("");
	
	setLabelTexts();
	
	//Assignment E start[
	roleSubPanel.selectTab(0);
	//Assignment E start]
}

private void setLabelTexts() {
	labelLongNameHeader.setText("");
	labelShortName.setInnerText(constants.shortName() + ":");
	labellongName.setInnerText(constants.name() + ":");
	labelroletype.setInnerText(constants.roleType() + ":");		
	labelstudyYear.setInnerText(constants.studyYear() + ":");		
}


@Override
public void setValue(StandardizedRoleProxy proxy) {
	/*this.proxy = proxy;		
	shortName.setInnerText(proxy.getShortName() == null ? "" : String.valueOf(proxy.getShortName()));
	longName.setInnerText(proxy.getLongName() == null ? "" : String.valueOf(proxy.getLongName()));
	roleType.setInnerText(proxy.getRoleType() == null ? "" : String.valueOf(proxy.getRoleType()));
	studyYear.setInnerText(proxy.getStudyYear() == null ? "" : String.valueOf(proxy.getStudyYear()));*/	
	
	this.proxy=proxy;
}



@Override
public void setDelegate(Delegate delegate) {
	this.delegate = delegate;
}




public Widget asWidget() {
	return this;
}


@UiHandler("print")
public void onPrintClicked(ClickEvent e) {
	//delegate.printRoleClicked();
}

@UiHandler("delete")
public void onDeleteClicked(ClickEvent e) {
	delegate.deleteRoleClicked(this.getValue());
}

@UiHandler("edit")
public void onEditClicked(ClickEvent e) {
	System.out.println("============================Click Edit Button=========================");
	System.out.println("============================Call delegate.editPatientClciked=========================");
	delegate.editRoleClicked(this.getValue());
}

@UiHandler("arrow")
void handleClick(ClickEvent e) 
{	
	if(roleDisclosurePanel.isOpen())
	{
			roleDisclosurePanel.setOpen(false);
			arrow.setUrl("/osMaEntry/gwt/unibas/images/arrowdownselect.png");//set url of up image
					
	}
	else
	{
			roleDisclosurePanel.setOpen(true);
			arrow.setUrl("/osMaEntry/gwt/unibas/images/arrowdownselect.png");//set url of down image
	}

}

/*@UiHandler("roleDetailPanel")
public void roleDetailPanelClicked(SelectionEvent<Integer> click)
{		
	if(roleDetailPanel.getTabBar().getSelectedTab()==(roleDetailPanel.getWidgetCount()-1))
	{
		Log.info("roleDetailPanel plus clicked");
		delegate.createRole();			
	}
	else
	{
		Log.info("roleDetailPanel clicked");
	}
}*/

public StandardizedRoleProxy getValue() {
	return proxy;
}

}
