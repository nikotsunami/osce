package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.LearningObjectiveViewImpl;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.MainSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.MinorSkillProxy;
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

public class RoleLearningSubViewImpl extends Composite implements RoleLearningSubView {

	private static RoleLearningSubViewUiBinder uiBinder = GWT
			.create(RoleLearningSubViewUiBinder.class);

	interface RoleLearningSubViewUiBinder extends
			UiBinder<Widget, RoleLearningSubViewImpl> {
	}

	Map<String, Widget> mainSkillMap;
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	protected List<String> paths = new ArrayList<String>();

	private Delegate delegate;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	@UiField (provided = true)
	public CellTable<MainSkillProxy> majorTable;
	
	@UiField (provided = true)
	public CellTable<MinorSkillProxy> minorTable;
	
	
	@UiField (provided = true)
	public SimplePager pagerMajor;
	
	@UiField (provided = true)
	public SimplePager pagerMinor;
		
	@UiField
	public IconButton btnAdd;	
	
	public RoleLearningPopUpView popUpView;
	
	public LearningObjectiveViewImpl learningObjectiveViewImpl;
	
	public PopupPanel popup;
	
	public Button addMajor;
	
	public Button addMinor;
	
	public Button clearAll;
	
	public IconButton closeButton;
	
	public boolean loadingFlag = false;
			
	public RoleLearningSubViewImpl() 
	{
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pagerMajor = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		pagerMinor = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		majorTable = new CellTable<MainSkillProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		minorTable = new CellTable<MinorSkillProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));	
	
		btnAdd.setText(constants.addSkill());
		
		addMajor = new Button();
		addMinor = new Button();
		clearAll = new Button();
		closeButton = new IconButton();
		
		addMajor.setText(constants.majorBtnLbl());
		addMinor.setText(constants.minorBtnLbl());
		clearAll.setText(constants.clearAll());
		closeButton.setIcon("close");
		closeButton.addStyleName("learningObjPopupCloseButton");
		
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				learningObjectiveViewImpl = null;
				popup.clear();			
				popup.hide();				
				
				delegate.closeButtonClicked();
			}
		});
		
		addMajor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.addMainClicked();
			}
		});
		
		addMinor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.addMinorClicked();
			}
		});
		
		clearAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				delegate.clearAllButtonClicked();
			}
		});
				
		majorTable.addStyleName("skillTable");
		minorTable.addStyleName("skillTable");
		
		initMajorTable();
		
		initMinorTable();				
	}
	
	private void initMajorTable() 
	{
		paths.add("mainClassi");
		TextColumn<MainSkillProxy> mainClassiCol = new TextColumn<MainSkillProxy>() 
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
			public String getValue(MainSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getClassificationTopic().getMainClassification().getShortcut());
			}
		};
		
		majorTable.addColumn(mainClassiCol, constants.mainClassi());		
		majorTable.setColumnWidth(mainClassiCol, "130px");
				
		paths.add("classificatonTopic");
		TextColumn<MainSkillProxy> classificationTopicCol = new TextColumn<MainSkillProxy>() 
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
			public String getValue(MainSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getClassificationTopic().getShortcut());
			}
		};
		
		majorTable.addColumn(classificationTopicCol, constants.classiTopic());
		majorTable.setColumnWidth(classificationTopicCol, "135px");
		
		paths.add("topic");
		TextColumn<MainSkillProxy> topicCol = new TextColumn<MainSkillProxy>() {
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
			public String getValue(MainSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getTopicDesc());
			}
		};
		majorTable.setTableLayoutFixed(true);
		majorTable.addColumn(topicCol, constants.topicLbl());
			
		paths.add("skillLevel");
		TextColumn<MainSkillProxy> skillLevelCol = new TextColumn<MainSkillProxy>() 
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
			public String getValue(MainSkillProxy object) 
			{
				if (object.getSkill().getSkillLevel() == null)
					return renderer.render("");
				else
					return renderer.render(String.valueOf(object.getSkill().getSkillLevel().getLevelNumber()));
			}
		};
		
		majorTable.setColumnWidth(skillLevelCol, "75px");		
		majorTable.addColumn(skillLevelCol, constants.skillLevel());		
		//majorTable.addColumnStyleName(2, "topicCol");
		
		
		addMajorTableLastColumn();
			
	}

	//SPEC Change
	public void removeMajorTableLastColumn() {
		if(majorTable != null){
			if(majorTable.getColumnCount() > 4){
				majorTable.removeColumn(majorTable.getColumnCount()-1);
			}
		}
	}
	
	//SPEC Change
	public void addMajorTableLastColumn() {
		addColumnMajor(new ActionCell<MainSkillProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<MainSkillProxy>() {
					public void execute(final MainSkillProxy mainSkill) {
						
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									//delegate.deleteDoctorClicked(roleParticipantProxy,1);
									delegate.majorDeleteClicked(mainSkill);
									return;

										}
									});

							dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									return;
									
								}
							});
					}
				}), "", new GetValueMajor<MainSkillProxy>() {
			public MainSkillProxy getValue(MainSkillProxy mainskill) {
				return mainskill;
			}
		}, null);

		majorTable.addColumnStyleName(4, "iconCol");
			
	}
	
	public void initMinorTable()
	{
		paths.add("mainClassi");
		TextColumn<MinorSkillProxy> mainClassiCol = new TextColumn<MinorSkillProxy>() 
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
			public String getValue(MinorSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getClassificationTopic().getMainClassification().getShortcut());
			}
		};
		minorTable.addColumn(mainClassiCol, constants.mainClassi());
		minorTable.setColumnWidth(mainClassiCol, "130px");
			
		paths.add("classificatonTopic");
		TextColumn<MinorSkillProxy> classificationTopicCol = new TextColumn<MinorSkillProxy>() 
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
			public String getValue(MinorSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getClassificationTopic().getShortcut());
			}
		};
		minorTable.addColumn(classificationTopicCol, constants.classiTopic());
		minorTable.setColumnWidth(classificationTopicCol, "135px");
		
		paths.add("topic");
		TextColumn<MinorSkillProxy> topicCol = new TextColumn<MinorSkillProxy>() 
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
			public String getValue(MinorSkillProxy object) 
			{
				return renderer.render(object.getSkill().getTopic().getTopicDesc());
			}
		};
		minorTable.setTableLayoutFixed(true);
		minorTable.addColumn(topicCol, constants.topicLbl());
			
		paths.add("skillLevel");
		TextColumn<MinorSkillProxy> skillLevelCol = new TextColumn<MinorSkillProxy>() 
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
			public String getValue(MinorSkillProxy object) 
			{
				if (object.getSkill().getSkillLevel() == null)
					return renderer.render("");
				else
					return renderer.render(String.valueOf(object.getSkill().getSkillLevel().getLevelNumber()));
			}
		};
		minorTable.addColumn(skillLevelCol, constants.skillLevel());
		minorTable.setColumnWidth(skillLevelCol, "75px");
		
		addMinorTableLastColumn();
	}

	//SPEC Change
	public void removeMinorTableLastColumn() {
		if(minorTable != null){
			if(minorTable.getColumnCount() > 4){
				minorTable.removeColumn(minorTable.getColumnCount()-1);
			}
		}
	}
	//SPEC Change
	public void addMinorTableLastColumn() {
		addColumnMinor(new ActionCell<MinorSkillProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<MinorSkillProxy>() {
					public void execute(final MinorSkillProxy minorSkill) {
						
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									delegate.minorDeleteClicked(minorSkill);
									return;

										}
									});

							dialogBox.getNoBtnl().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									return;
									
								}
							});
					}
				}), "", new GetValueMinor<MinorSkillProxy>() {
			public MinorSkillProxy getValue(MinorSkillProxy mainskill) {
				return mainskill;
			}
		}, null);

		minorTable.addColumnStyleName(4, "iconCol");
	}
	
	private <C> void addColumnMajor(Cell<C> cell, String headerText,final GetValueMajor<C> getter, FieldUpdater<MainSkillProxy, C> fieldUpdater) 
	{
		Column<MainSkillProxy, C> column = new Column<MainSkillProxy, C>(cell) 
		{
			@Override
			public C getValue(MainSkillProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		majorTable.addColumn(column, headerText);
	}
	
	private static interface GetValueMajor<C> {
		C getValue(MainSkillProxy proxyvalue);
	}
	
	private <C> void addColumnMinor(Cell<C> cell, String headerText,final GetValueMinor<C> getter, FieldUpdater<MinorSkillProxy, C> fieldUpdater) 
	{
		Column<MinorSkillProxy, C> column = new Column<MinorSkillProxy, C>(cell) 
		{
			@Override
			public C getValue(MinorSkillProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		minorTable.addColumn(column, headerText);
	}
	
	private static interface GetValueMinor<C> {
		C getValue(MinorSkillProxy proxyvalue);
	}
			
	@UiHandler("btnAdd")
	public void btnAddClicked(ClickEvent event)
	{		
		System.out.println("button clicked");
		
		VerticalPanel vp = new VerticalPanel();
		
		popup = new PopupPanel();
		
		popup.clear();
		
		popup.setGlassEnabled(true);
		
		learningObjectiveViewImpl = new LearningObjectiveViewImpl(true);	
		
		popup.addStyleName("learningObjPopupStyle");
		
		vp.add(closeButton);
		
		addMajor.addStyleName("learningObjPopupLabel");
		addMinor.addStyleName("learningObjPopupButton");
		
		learningObjectiveViewImpl.getMainClassiLbl().addStyleName("learningObjPopupLabel");
		learningObjectiveViewImpl.getSkillLevelLbl().addStyleName("learningObjPopupLabel");
		
		learningObjectiveViewImpl.getTable().setWidth("1190px");
		learningObjectiveViewImpl.getLearningScrollPanel().addStyleName("learningObjScroll");
		
		learningObjectiveViewImpl.getHpBtnPanel().add(addMajor);		
		learningObjectiveViewImpl.getHpBtnPanel().add(addMinor);
		learningObjectiveViewImpl.getHpBtnPanel().add(clearAll);
		
		vp.add(learningObjectiveViewImpl);
		
		popup.add(vp);
		
		delegate.loadLearningObjectiveData();
		
		popup.center();
	}

	@Override
	public void setDelegate(Delegate delegate) 
	{
		this.delegate=delegate;			
	}
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	@Override
	public Map getMainSkillMap()
	{
		return this.mainSkillMap;
	}

	public LearningObjectiveViewImpl getLearningObjectiveViewImpl() {
		return learningObjectiveViewImpl;
	}

	public void setLearningObjectiveViewImpl(
			LearningObjectiveViewImpl learningObjectiveViewImpl) {
		this.learningObjectiveViewImpl = learningObjectiveViewImpl;
	}
	
	
}
