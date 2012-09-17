package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.DockMenuSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaMainNav;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.MenuClickHandler;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;
import ch.unibas.medizin.osce.client.managed.request.ApplianceProxy;
import ch.unibas.medizin.osce.client.managed.request.ClassificationTopicProxy;
import ch.unibas.medizin.osce.client.managed.request.MainClassificationProxy;
import ch.unibas.medizin.osce.client.managed.request.SkillLevelProxy;
import ch.unibas.medizin.osce.client.managed.request.TopicProxy;
import ch.unibas.medizin.osce.client.style.resources.LearningObjectiveData;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;

public class LearningObjectiveViewImpl extends Composite implements LearningObjectiveView, RecordChangeHandler
{
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, LearningObjectiveViewImpl> {
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	@UiField (provided = true)
    SimplePager pager;
    
    @UiField (provided = true)
    CellTable<LearningObjectiveData> table;
    
    @UiField
    Label mainClassiLbl;
    
    @UiField 
    Label classiTopicLbl;
    
    @UiField
    Label topicLbl;
        
    @UiField
    Label skillLevelLbl;
    
    @UiField
    Label applianceLbl;
    
    @UiField
    ScrollPanel learningScrollPanel;
  
    protected Set<String> paths = new HashSet<String>();
    
    public MultiSelectionModel<LearningObjectiveData> multiselectionModel;
    
    @UiField
	public DefaultSuggestBox<MainClassificationProxy, EventHandlingValueHolderItem<MainClassificationProxy>> mainClassificationSuggestBox;
    
    @UiField
	public DefaultSuggestBox<ClassificationTopicProxy, EventHandlingValueHolderItem<ClassificationTopicProxy>> classificationTopicSuggestBox;
    
    @UiField
	public DefaultSuggestBox<TopicProxy, EventHandlingValueHolderItem<TopicProxy>> TopicSuggestBox;
    
    @UiField
	public DefaultSuggestBox<SkillLevelProxy, EventHandlingValueHolderItem<SkillLevelProxy>> skillLevelSuggestBox;
    
    @UiField
	public DefaultSuggestBox<ApplianceProxy, EventHandlingValueHolderItem<ApplianceProxy>> applianceSuggestBox;
    
    @UiField
    public HorizontalPanel hpBtnPanel;
    
    public boolean flag = false;
	
	public LearningObjectiveViewImpl() {		
		if (!flag)
			flag = false;
		
		init();		
	}
	
	public LearningObjectiveViewImpl(boolean value) {			
		flag = value;
		init();		
	}
	
	public void init()
	{
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<LearningObjectiveData>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
	
		initWidget(BINDER.createAndBindUi(this));
		 
		 mainClassiLbl.setText(constants.mainClassi());
		 classiTopicLbl.setText(constants.classiTopic());
		 topicLbl.setText(constants.topicLbl());
		 skillLevelLbl.setText(constants.skillLevel());
		 applianceLbl.setText(constants.appliance());
		
		 	 
		 init2();
		 
		 mainClassificationSuggestBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (mainClassificationSuggestBox.getSelected() != null)
				{
					//System.out.println("MAIN CLASSI : " + mainClassificationSuggestBox.getSelected().getId());
					delegate.mainClassificationSuggestboxChanged(mainClassificationSuggestBox.getSelected().getId());
				}
				else
				{
					//System.out.println("MAIN CLASSI : " + mainClassificationSuggestBox.getSelected().getId());
					delegate.mainClassificationSuggestboxChanged(null);
				}
			}
		});
		 
		 classificationTopicSuggestBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (classificationTopicSuggestBox.getSelected() != null)
					delegate.classificationTopicSuggestboxChanged(classificationTopicSuggestBox.getSelected().getId());
				else
					delegate.classificationTopicSuggestboxChanged(null);
			}
		});
		 
		 TopicSuggestBox.addHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				if (TopicSuggestBox.getSelected() != null)
					delegate.topicSuggestboxChanged(TopicSuggestBox.getSelected().getId());
				else
					delegate.topicSuggestboxChanged(null);
			}
		});
		 
		 skillLevelSuggestBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (skillLevelSuggestBox.getSelected() != null)
					delegate.skillLevelSuggestboxChanged(skillLevelSuggestBox.getSelected().getId());
				else
					delegate.skillLevelSuggestboxChanged(null);
			}
		});
		 
		 applianceSuggestBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (applianceSuggestBox.getSelected() != null)
					delegate.applianceSuggestboxChanged(applianceSuggestBox.getSelected().getId());
				else
					delegate.applianceSuggestboxChanged(null);
			}
		});
	}
				
	public void init2() {
		
		System.out.println("FLAG VALUE : " + flag);
		
		if (flag)
		{
			ProvidesKey<LearningObjectiveData> keyProvider1 = ((AbstractHasData<LearningObjectiveData>) table).getKeyProvider();
			multiselectionModel=new MultiSelectionModel<LearningObjectiveData>(keyProvider1);
		
			Column<LearningObjectiveData, Boolean> checkColumn = new Column<LearningObjectiveData, Boolean>(new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(LearningObjectiveData object) {
					// Get the value from the selection model.
					return multiselectionModel.isSelected(object);
					}
				};
			
				table.setSelectionModel(multiselectionModel,DefaultSelectionEventManager.<LearningObjectiveData> createCheckboxManager());
				table.addColumn(checkColumn);
				table.setColumnWidth(checkColumn, 40, Unit.PX);
		}

		paths.add("code");
		
		TextColumn<LearningObjectiveData> codeCol = new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return object.getCode();
			}
		}; 
		
		table.addColumn(codeCol, "Code");
		table.setColumnWidth(codeCol, "80px");
		
		paths.add("text");
		table.addColumn(new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return object.getText();
			}
		}, "Text");
		
		paths.add("topic");
		table.addColumn(new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return object.getTopic();
			}
		}, "Topic");
		
		paths.add("level");
		table.addColumn(new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return String.valueOf(object.getSkillLevel());
			}
		}, "Level");
		
		paths.add("d");
		table.addColumn(new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return object.getD();
			}
		}, "D");
		
		paths.add("t");
		table.addColumn(new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return object.getT();
			}
		}, "T");		
		
		paths.add("e");
		table.addColumn(new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return object.getE();
			}
		}, "E");
		
		paths.add("p");
		table.addColumn(new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return object.getP();
			}
		}, "P");
		
		paths.add("g");
		table.addColumn(new TextColumn<LearningObjectiveData>() {
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LearningObjectiveData object) {
				return object.getG();
			}
		}, "G");
	}	
	
	
	
	public HorizontalPanel getHpBtnPanel() {
		return hpBtnPanel;
	}

	public void setHpBtnPanel(HorizontalPanel hpBtnPanel) {
		this.hpBtnPanel = hpBtnPanel;
	}

	public MultiSelectionModel<LearningObjectiveData> getMultiselectionModel() {
		return multiselectionModel;
	}

	public void setMultiselectionModel(
			MultiSelectionModel<LearningObjectiveData> multiselectionModel) {
		this.multiselectionModel = multiselectionModel;
	}

	public CellTable<LearningObjectiveData> getTable() {
		return table;
	}

	public void setTable(CellTable<LearningObjectiveData> table) {
		this.table = table;
	}
		
	public DefaultSuggestBox<MainClassificationProxy, EventHandlingValueHolderItem<MainClassificationProxy>> getMainClassificationSuggestBox() {
		return mainClassificationSuggestBox;
	}
	
	public void setMainClassificationSuggestBox(
			DefaultSuggestBox<MainClassificationProxy, EventHandlingValueHolderItem<MainClassificationProxy>> mainClassificationSuggestBox) {
		this.mainClassificationSuggestBox = mainClassificationSuggestBox;
	}

	public DefaultSuggestBox<ClassificationTopicProxy, EventHandlingValueHolderItem<ClassificationTopicProxy>> getClassificationTopicSuggestBox() {
		return classificationTopicSuggestBox;
	}

	public void setClassificationTopicSuggestBox(
			DefaultSuggestBox<ClassificationTopicProxy, EventHandlingValueHolderItem<ClassificationTopicProxy>> classificationTopicSuggestBox) {
		this.classificationTopicSuggestBox = classificationTopicSuggestBox;
	}

	public DefaultSuggestBox<TopicProxy, EventHandlingValueHolderItem<TopicProxy>> getTopicSuggestBox() {
		return TopicSuggestBox;
	}

	public void setTopicSuggestBox(
			DefaultSuggestBox<TopicProxy, EventHandlingValueHolderItem<TopicProxy>> topicSuggestBox) {
		TopicSuggestBox = topicSuggestBox;
	}

	public DefaultSuggestBox<SkillLevelProxy, EventHandlingValueHolderItem<SkillLevelProxy>> getSkillLevelSuggestBox() {
		return skillLevelSuggestBox;
	}

	public void setSkillLevelSuggestBox(
			DefaultSuggestBox<SkillLevelProxy, EventHandlingValueHolderItem<SkillLevelProxy>> skillLevelSuggestBox) {
		this.skillLevelSuggestBox = skillLevelSuggestBox;
	}

	public DefaultSuggestBox<ApplianceProxy, EventHandlingValueHolderItem<ApplianceProxy>> getApplianceSuggestBox() {
		return applianceSuggestBox;
	}

	public void setApplianceSuggestBox(
			DefaultSuggestBox<ApplianceProxy, EventHandlingValueHolderItem<ApplianceProxy>> applianceSuggestBox) {
		this.applianceSuggestBox = applianceSuggestBox;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public Label getMainClassiLbl() {
		return mainClassiLbl;
	}

	public void setMainClassiLbl(Label mainClassiLbl) {
		this.mainClassiLbl = mainClassiLbl;
	}

	public Label getClassiTopicLbl() {
		return classiTopicLbl;
	}

	public void setClassiTopicLbl(Label classiTopicLbl) {
		this.classiTopicLbl = classiTopicLbl;
	}

	public Label getTopicLbl() {
		return topicLbl;
	}

	public void setTopicLbl(Label topicLbl) {
		this.topicLbl = topicLbl;
	}

	public Label getSkillLevelLbl() {
		return skillLevelLbl;
	}

	public void setSkillLevelLbl(Label skillLevelLbl) {
		this.skillLevelLbl = skillLevelLbl;
	}

	public Label getApplianceLbl() {
		return applianceLbl;
	}

	public void setApplianceLbl(Label applianceLbl) {
		this.applianceLbl = applianceLbl;
	}

	public ScrollPanel getLearningScrollPanel() {
		return learningScrollPanel;
	}

	public void setLearningScrollPanel(ScrollPanel learningScrollPanel) {
		this.learningScrollPanel = learningScrollPanel;
	}
	
	@Override
	public void onRecordChange(RecordChangeEvent event) {
			int pagesize = 0;

			if (event.getRecordValue() == "ALL") {
				pagesize = table.getRowCount();
				OsMaConstant.TABLE_PAGE_SIZE = pagesize;
			} else {
				pagesize = Integer.parseInt(event.getRecordValue());
			}

			table.setPageSize(pagesize);
	}

}
	