package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.DoctorProxy;
import ch.unibas.medizin.osce.client.managed.request.RoleParticipantProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class RoleRoleParticipantSubViewImpl extends Composite implements RoleRoleParticipantSubView {

	private static RoleRoleParticipantSubViewUiBinder uiBinder = GWT.create(RoleRoleParticipantSubViewUiBinder.class);

	interface RoleRoleParticipantSubViewUiBinder extends UiBinder<Widget, RoleRoleParticipantSubViewImpl> {
	}

	private List<AbstractEditableCell<?, ?>> editableCells;
	protected Set<String> paths = new HashSet<String>();
	private boolean addBoxesShown = true;
	private Delegate delegate;
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField (provided = true)
	public CellTable<RoleParticipantProxy> authorTable;
	
	@UiField (provided = true)
	public CellTable<RoleParticipantProxy> reviewerTable;
	
	
	@UiField (provided = true)
	public SimplePager pagerAuthor;
	
	@UiField (provided = true)
	public SimplePager pagerReviewer;
		
	
	//Issue # 122 : Replace pull down with autocomplete.


	
	@UiField
	public DefaultSuggestBox<DoctorProxy, EventHandlingValueHolderItem<DoctorProxy>> lstDoctor;
	
	/*
	@UiField(provided = true)
    public ValueListBox<DoctorProxy> lstDoctor = new ValueListBox<DoctorProxy>(new AbstractRenderer<DoctorProxy>() {
        public String render(DoctorProxy obj) 
        {
            return obj == null ? "" : String.valueOf(obj.getName());
        }
    });
	*/
	//Issue # 122 : Replace pull down with autocomplete.
	
	// Highlight onViolation
	Map<String, Widget> roleParticipantMap;	
	// E Highlight onViolation
	
	@UiField
	public IconButton btnAddAuthor;
	
	@UiField
	public IconButton btnAddReviewer;
			
	public RoleRoleParticipantSubViewImpl() 
	{
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pagerAuthor = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		pagerReviewer = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		authorTable = new CellTable<RoleParticipantProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		reviewerTable = new CellTable<RoleParticipantProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		
		initWidget(uiBinder.createAndBindUi(this));
		initTable();
		
		btnAddReviewer.setText(constants.addReviewer());
		btnAddAuthor.setText(constants.addAuthor());
		
		// Highlight onViolation
		roleParticipantMap=new HashMap<String, Widget>();
		roleParticipantMap.put("doctor", lstDoctor.getTextField().advancedTextBox);
		roleParticipantMap.put("standardizedRole", lstDoctor);
		roleParticipantMap.put("type", lstDoctor);
		roleParticipantMap.put("email", lstDoctor);
		// E Highlight onViolation
				
	}
	private void initTable() 
	{
		paths.add("Author");
		authorTable.addColumn(new TextColumn<RoleParticipantProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleParticipantProxy object) 
			{
				return renderer.render(object.getDoctor().getName());
			}
		}, constants.author());
		
		
			
		paths.add("Reviewer");
		reviewerTable.addColumn(new TextColumn<RoleParticipantProxy>() 
		{
			{ 
				this.setSortable(true); 
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleParticipantProxy object) 
			{
				return renderer.render(object.getDoctor().getName());
			}
		}, constants.reviewer());
		
		addAuthorLastColumn();
		
		addReviewerLastColumn();
			
	}
	//SPEC Change
	public void removeAuthorLastColumn() {
		if(authorTable != null){
			if(authorTable.getColumnCount() > 1){
				authorTable.removeColumn(authorTable.getColumnCount()-1);
			}
		}
	}
	
	//SPEC Change
	public void removeReviewerLastColumn() {
		if(reviewerTable != null){
			if(reviewerTable.getColumnCount() > 1){
				reviewerTable.removeColumn(reviewerTable.getColumnCount()-1);
			}
		}
	}
	
	//SPEC Change
	public void addAuthorLastColumn() {
		addColumn(new ActionCell<RoleParticipantProxy>(OsMaConstant.DELETE_ICON, new ActionCell.Delegate<RoleParticipantProxy>() 
		{
					public void execute(final RoleParticipantProxy roleParticipantProxy) {
						/*if(Window.confirm(constants.reallyDelete()))
						{
							delegate.deleteDoctorClicked(roleParticipantProxy,0);
						}*/
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									delegate.deleteDoctorClicked(roleParticipantProxy,0);
									Log.info("yes click");	
									
									return;

										}
									});

							dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									Log.info("no click");
									return;									
								}
							});
						// E: Issue Role
					}
				}), "", new GetValueDoctor<RoleParticipantProxy>() {
					public RoleParticipantProxy getValue(RoleParticipantProxy roleParticipantProxy) {
						return roleParticipantProxy;
					}
		}, null);
		authorTable.addColumnStyleName(1, "iconCol");
	}
		
	//SPEC Change
	public void addReviewerLastColumn() {
		addColumnRe(new ActionCell<RoleParticipantProxy>(OsMaConstant.DELETE_ICON, new ActionCell.Delegate<RoleParticipantProxy>() 
				{
							public void execute(final RoleParticipantProxy roleParticipantProxy) {
								/*if(Window.confirm(constants.reallyDelete()))
								{
									delegate.deleteDoctorClicked(roleParticipantProxy,1);
								}*/
								
								// Issue Role
								 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
								 dialogBox.showYesNoDialog(constants.reallyDelete());
								 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
										
										@Override
										public void onClick(ClickEvent event) {
											dialogBox.hide();
											delegate.deleteDoctorClicked(roleParticipantProxy,1);
											Log.info("yes click");	
											
											return;

												}
											});

									dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
										
										@Override
										public void onClick(ClickEvent event) {
											dialogBox.hide();
											Log.info("no click");
											return;
											
										}
									});
								// E: Issue Role
							}
						}), "", new GetValueReviewerDoctor<RoleParticipantProxy>() {
							public RoleParticipantProxy getValue(RoleParticipantProxy roleParticipantProxy) {
								return roleParticipantProxy;
							}
				}, null);
		reviewerTable.addColumnStyleName(1, "iconCol");
			
	}
	
	private <C> void addColumn(Cell<C> cell, String headerText,final GetValueDoctor<C> getter, FieldUpdater<RoleParticipantProxy, C> fieldUpdater) 
	{
		Column<RoleParticipantProxy, C> column = new Column<RoleParticipantProxy, C>(cell) 
		{
			@Override
			public C getValue(RoleParticipantProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		authorTable.addColumn(column, headerText);
	}
	
	private static interface GetValueDoctor<C> {
		C getValue(RoleParticipantProxy proxyvalue);
	}
	private <C> void addColumnRe(Cell<C> cell, String headerText,final GetValueReviewerDoctor<C> getter, FieldUpdater<RoleParticipantProxy, C> fieldUpdater) 
	{
		Column<RoleParticipantProxy, C> column = new Column<RoleParticipantProxy, C>(cell) 
		{
			@Override
			public C getValue(RoleParticipantProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		reviewerTable.addColumn(column, headerText);
	}
	
	private static interface GetValueReviewerDoctor<C> {
		C getValue(RoleParticipantProxy proxyvalue);
	}
			
	@UiHandler("btnAddAuthor")
	public void btnAddAuthorClicked(ClickEvent event)
	{				
		delegate.AddAuthorClicked();		
	}
	
	@UiHandler("btnAddReviewer")
	public void btnAddReviewerClicked(ClickEvent event)
	{
		delegate.AddReviewerClicked();
	}


	@Override
	public void setDelegate(Delegate delegate) 
	{
		this.delegate=delegate;			
	}
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}
	
	// Highlight onViolation
	
	@Override
	public Map getRoleParticipantMap()
	{
		return this.roleParticipantMap;
	}
	
	// E Highlight onViolation
}
