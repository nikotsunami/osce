package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.RoleTopicProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicsAndSpecDetailsViewImpl  extends Composite implements  TopicsAndSpecDetailsView{

	private static TopicsAndSpecDetailsViewImplUiBinder uiBinder = GWT
			.create(TopicsAndSpecDetailsViewImplUiBinder.class);

	interface TopicsAndSpecDetailsViewImplUiBinder extends UiBinder<Widget, TopicsAndSpecDetailsViewImpl> {
		
	}

	private OsceConstants constants = GWT.create(OsceConstants.class);
	private Presenter presenter;
	private Delegate delegate;
	
	// Issue Role
	
	int left=0,top=0;
	PopupPanel addRolePopup;
	HorizontalPanel popupHP;
	//HorizontalPanel popupbtnHP;
	VerticalPanel popupLabelVP;
	VerticalPanel popupValueVP;
	Button btnSave;
	Button btnCancel;
	com.google.gwt.user.client.ui.Label lblRoleTopic;
	com.google.gwt.user.client.ui.Label lblMaxStudent;
	com.google.gwt.user.client.ui.Label lblStudyYear;
	
	@UiField(provided = true)
	CellTable<RoleTopicProxy>table;
	
	//@UiField
	TextBox AddTextBox;
	
	@UiField
	IconButton AddButton;
	
	@UiField (provided = true)
	SimplePager Pager;
	
	@UiField (provided = true)
	public QuickSearchBox searchBox;
	
	
//	@UiField
//	public SplitLayoutPanel splitLayoutPanel;
	
//	@UiField
//	public SimplePanel detailsPanel;
	
	protected ArrayList<String> paths = new ArrayList<String>();
	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}
	
	//@UiField(provided = true)
	ListBox slots_till_change=new ListBox();
	
	//@UiField(provided = true)
	ValueListBox<StudyYears> StudyYearListBox = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
	
	
	
	@UiField
	Button FilterButton;
	
	// Issue Role
	@UiHandler ("AddButton")
	public void newButtonClicked(ClickEvent event) 
		{
	Log.info("Click Add Button.");
		//delegate.newClicked(AddTextBox.getValue(),slots_till_change.getValue(slots_till_change.getSelectedIndex()),StudyYearListBox.getValue());
		AddTextBox.setValue("");
		initPopup(event);
	}

	private void initPopup(ClickEvent event) 
	{
		Log.info("initPopup() Call");
				
		//SimplePanel container=new SimplePanel();
		AddTextBox.setValue(null);
		slots_till_change.setSelectedIndex(0);
		StudyYearListBox.setValue(null);
		
		addRolePopup=new PopupPanel(true);
		addRolePopup.setAnimationEnabled(true);		
		addRolePopup.setSize("230px", "140px");		
		
		lblRoleTopic.addStyleName("marginTop15");
		lblMaxStudent.addStyleName("marginTop15");
		lblStudyYear.addStyleName("marginTop15");
		
		AddTextBox.addStyleName("marginTop7");
		slots_till_change.addStyleName("marginTop7");
		StudyYearListBox.addStyleName("marginTop7");
		
		btnSave.addStyleName("marginTop15");
		btnCancel.addStyleName("marginTop15");
		
		lblRoleTopic.setText("Role Topic: ");
		lblMaxStudent.setText("Max. Student: ");
		lblStudyYear.setText("Study year: ");
		
		btnSave.setText(constants.save());
		btnCancel.setText(constants.cancel());
			
		popupLabelVP.add(lblRoleTopic);
		popupLabelVP.add(lblMaxStudent);
		popupLabelVP.add(lblStudyYear);
		popupLabelVP.add(btnSave);
				
		popupValueVP.add(AddTextBox);
		popupValueVP.add(slots_till_change);
		popupValueVP.add(StudyYearListBox);
		popupValueVP.add(btnCancel);
				
		popupHP.add(popupLabelVP);
		popupHP.add(popupValueVP);
				
		addRolePopup.add(popupHP);
	
		addRolePopup.setPopupPosition(event.getClientX(), event.getClientY());
		addRolePopup.show();
		
		btnSave.addClickHandler(new ClickHandler() 
		{		
			@Override
			public void onClick(ClickEvent event) {
				Log.info("Click on Save Role Button.");
				Log.info("==>" + AddTextBox.getValue()+"==>" + slots_till_change.getValue(slots_till_change.getSelectedIndex())+"==>" + StudyYearListBox.getValue());
				
				if(AddTextBox.getValue()==null || StudyYearListBox.getValue()==null || AddTextBox.getText().equals(""))
				{
					/*Window.alert("Please Enter appropriate value for Role Base Item");*/
					// Issue Role
					 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Please Enter appropriate value for Role Base Item");
					 dialogBox.showConfirmationDialog();
					 
					 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();							
							Log.info("ok click");	
							return;
								}
							});

					
//E: Issue Role
				}
				else
				{
				delegate.newClicked(AddTextBox.getValue(),slots_till_change.getValue(slots_till_change.getSelectedIndex()),StudyYearListBox.getValue());
				addRolePopup.hide();
				}
			}
		});
		
		btnCancel.addClickHandler(new ClickHandler() 
		{		
			@Override
			public void onClick(ClickEvent event) 
			{
				Log.info("Click on Cancel Role Button.");
				addRolePopup.hide();
			}
		});
		
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
	public TopicsAndSpecDetailsViewImpl() {
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<RoleTopicProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		Pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		
		
		
		for(int i=1;i<=12;i++)
		{
			slots_till_change.addItem(i+"");
		}
		
		StudyYearListBox.setValue(StudyYears.values()[0]);
		StudyYearListBox.setAcceptableValues(Arrays.asList(StudyYears.values()));
		init();
//		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		
		FilterButton.setText("Filter");
		AddButton.setText("add topic");
		//todo
	}
	
	public String getQuery() {
		return searchBox.getValue();
	}
	
	public void init(){
		
		// Issue Role		
		
				AddTextBox=new TextBox();		
				
				popupHP=new HorizontalPanel();
				//popupbtnHP=new HorizontalPanel();
				
				popupLabelVP=new VerticalPanel();
				popupValueVP=new VerticalPanel();
				
				lblRoleTopic=new com.google.gwt.user.client.ui.Label();
				lblMaxStudent=new com.google.gwt.user.client.ui.Label();
				lblStudyYear=new com.google.gwt.user.client.ui.Label();
				
				btnSave=new Button();
				btnCancel=new Button();
				
				table.addDomHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) 
					{
						left=event.getClientX();
						top=event.getClientY();				
					}
				}, ClickEvent.getType());
		
		AddTextBox.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		        	newButtonClicked(null);
		    }
		});
		
	//	DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		paths.add("name");
		table.addColumn(new TextColumn<RoleTopicProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoleTopicProxy object) {
				return renderer.render(object.getName());
				
			}
		},constants.roletopic());
	
		paths.add("slots_until_change");
		TextColumn<RoleTopicProxy> slotColumn=new TextColumn<RoleTopicProxy>() {
			{ this.setSortable(true); }

			 Renderer<java.lang.Integer> renderer = new AbstractRenderer<java.lang.Integer>() {
				 
				            public String render(java.lang.Integer obj) {
				              return obj == null ? "" : String.valueOf(obj);
				          }
				
			};

			@Override
			public String getValue(RoleTopicProxy object) {
				return renderer.render(object.getSlotsUntilChange());
				
			}
		};
		
		table.setColumnWidth(slotColumn, "180px");
		table.addColumn(slotColumn,constants.slotUntilChange());


		paths.add("study_year");
		table.addColumn(new TextColumn<RoleTopicProxy>() {
			{ this.setSortable(true); }

			 Renderer<StudyYears> renderer = new AbstractRenderer<StudyYears>() {
				 				           
							@Override
							public String render(StudyYears object) {
								 return object == null ? "" : String.valueOf(object);
								}
			};

			@Override
			public String getValue(RoleTopicProxy object) {
				return renderer.render(object.getStudyYear());
				
			}
		},constants.studyYears());
		
		//edit Button
		addColumn(new ActionCell<RoleTopicProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<RoleTopicProxy>() {
					public void execute(RoleTopicProxy roletopic) {
						//Window.alert("You clicked " + institution.getInstitutionName());
//						if(Window.confirm("wirklich löschen?"))
//							delegate.deleteClicked(roletopic);

						// Issue Role
						delegate.editClicked(roletopic,left,top);
						
						//delegate.editClicked(roletopic);
					}
				}), "", new GetValue<RoleTopicProxy>() {
			public RoleTopicProxy getValue(RoleTopicProxy roletopic) {
				return roletopic;
			}
		}, null);
		table.addColumnStyleName(1, "iconCol");

		
		
		//Delete Buuton
		addColumn(new ActionCell<RoleTopicProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<RoleTopicProxy>() {
					public void execute(final RoleTopicProxy roletopic) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						/*if(Window.confirm("wirklich löschen?"))
							delegate.deleteClicked(roletopic);*/
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.reallyDelete());
						 dialogBox.showDialog();
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();									
									Log.info("yes click");	
									delegate.deleteClicked(roletopic);
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
				}), "", new GetValue<RoleTopicProxy>() {
			public RoleTopicProxy getValue(RoleTopicProxy roletopic) {
				return roletopic;
			}
		}, null);
		table.addColumnStyleName(1, "iconCol");

	}
	
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<RoleTopicProxy, C> fieldUpdater) {
		Column<RoleTopicProxy, C> column = new Column<RoleTopicProxy, C>(cell) {
			@Override
			public C getValue(RoleTopicProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column, headerText);
	}
	
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	
	
	@Override
	public CellTable<RoleTopicProxy> getTable() {
		return table;
	}
	private static interface GetValue<C> {
	
		C getValue(RoleTopicProxy object);
	}
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter StandardizedPatientActivity) {
		this.presenter =  StandardizedPatientActivity;
	}

	public Widget asWidget() {
		return this;
	}
}
