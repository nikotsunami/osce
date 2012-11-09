package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LearningObjectiveViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleOsceSemesterSubViewImpl extends Composite implements RoleOsceSemesterSubView {

	private static RoleOsceSemesterSubViewUiBinder uiBinder = GWT
			.create(RoleOsceSemesterSubViewUiBinder.class);

	interface RoleOsceSemesterSubViewUiBinder extends
			UiBinder<Widget, RoleOsceSemesterSubViewImpl> {
	}

	
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	protected List<String> paths = new ArrayList<String>();

	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField (provided = true)
	public CellTable<OsceProxy> osceSemesterTable;
			
	public RoleOsceSemesterSubViewImpl() 
	{
		/*SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);*/
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		osceSemesterTable = new CellTable<OsceProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		initWidget(uiBinder.createAndBindUi(this));	
		
		osceSemesterTable.addColumn(new TextColumn<OsceProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				String value="";
				if(object.getStudyYear()!=null)
				{
					value=value+object.getStudyYear();
				}
				else
				{
					value=value+" ";
				}
				if(object.getSemester()!=null)
				{
					value=value+" "+object.getSemester().getSemester().name();
				}
					return renderer.render(value);
				
			}
		}, constants.osce());
		
		osceSemesterTable.addColumn(new TextColumn<OsceProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(OsceProxy object) {
				if(object.getSemester()==null)
				{
					return renderer.render(" ");
				}
				else
				{
				return renderer.render( object.getSemester().getSemester().toString());
				}
			}
		}, constants.semester());
		
					
	}
	
	public void setDelegate(Delegate delegate) 
	{
		this.delegate=delegate;			
	}

	@Override
	public CellTable<OsceProxy> getOsceSemesterTable() {
		// TODO Auto-generated method stub
		return osceSemesterTable;
	}
	
	
	
	
	
}
