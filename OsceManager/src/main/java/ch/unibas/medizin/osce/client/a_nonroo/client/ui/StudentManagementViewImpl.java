package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.client.style.resources.AdvanceCellTable;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class StudentManagementViewImpl extends Composite implements StudentManagementView {

	private static StudentManagmentViewImplUiBinder uiBinder = GWT
			.create(StudentManagmentViewImplUiBinder.class);

	interface StudentManagmentViewImplUiBinder extends UiBinder<Widget,StudentManagementViewImpl> {
	}
	
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public Delegate delegate;
	
	public Presenter presenter;
	
	public StudentManagementEditPopupView studentManagementEditPopupView;
	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	
	/*@UiField
	ScrollPanel mainScrollPanel;*/
	
	@UiField(provided = true)
	public SimplePager pager;
	
	Map<String, String> columnName=new HashMap<String, String>();
	List<String> columnNameorder = new ArrayList<String>();
	
	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	public SimplePanel detailsPanel;
	
	Map<String, Widget> studentManagementMap;
	@UiField(provided = true)
	public AdvanceCellTable<StudentProxy> table;
	
	public List<String> paths =new ArrayList<String>();
	
	private List<AbstractEditableCell<?, ?>> editableCells;

	protected int left;

	protected int top;
	
	public StudentManagementViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new AdvanceCellTable<StudentProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
	}

	public List<String> getPaths() {
		

		return paths;
	}
	public void init(){
		
		ResolutionSettings.setSplitLayoutPanelPosition(splitLayoutPanel,true);
		
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
		columnName.put(constants.name(), "name");
		columnNameorder.add(constants.name());
		paths.add("name");
		table.addColumn(new TextColumn<StudentProxy>() {
			
			{ this.setSortable(true); }	

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StudentProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.name());
		
		columnName.put(constants.preName(), "preName");
		columnNameorder.add(constants.preName());
		paths.add("preName");
		table.addColumn(new TextColumn<StudentProxy>() {
			
			{ this.setSortable(true); }	

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StudentProxy object) {
				return renderer.render(object.getPreName());
			}
		}, constants.preName());
		
		columnName.put(constants.email(), "email");		
		columnNameorder.add(constants.email());
		paths.add("email");
		table.addColumn(new TextColumn<StudentProxy>() {

			{ this.setSortable(true); }	//By SPEC
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StudentProxy object) {
				return renderer.render(object.getEmail());
			}
		}, constants.email());
		
		columnName.put(constants.edit(), "edit");		
		columnNameorder.add(constants.edit());
		paths.add("edit");
		addColumn(new ActionCell<StudentProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<StudentProxy>() {
					public void execute(StudentProxy studentProxy) {
						editPopupView(studentProxy);
						//delegate.editStudentData(studentProxy);
						
					}
				}), "", new GetValue<StudentProxy>() {
			public StudentProxy getValue(StudentProxy studentProxy) {
				return studentProxy;
			}
		}, null);
		
		table.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				left = event.getClientX();
				top = event.getClientY();

			}
		}, ClickEvent.getType());
		//table.addColumnStyleName(3, "iconCol");
	}
	

	public void editPopupView(final StudentProxy studentProxy) {
		
		studentManagementEditPopupView = new StudentManagementEditPopupViewImpl();
		
		((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).setAnimationEnabled(true);
		
		((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).setWidth("200px");
		
		studentManagementEditPopupView.getNewName().setValue(studentProxy.getName());
		studentManagementEditPopupView.getNewPreName().setValue(studentProxy.getPreName());
		studentManagementEditPopupView.getNewEmail().setValue(studentProxy.getEmail());
		
		RootPanel.get().add(((StudentManagementEditPopupViewImpl)studentManagementEditPopupView));
		
		// Highlight onViolation
		
		studentManagementMap=new HashMap<String, Widget>();
		studentManagementMap.put("name",((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).getNewName());
		studentManagementMap.put("prename",((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).getNewPreName());
		studentManagementMap.put("email",((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).getNewEmail());
		
		
		// E Highlight onViolation
		
		studentManagementEditPopupView.getOkBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
			
					
					
					delegate.editStudentData(studentProxy, studentManagementEditPopupView.getNewName().getValue(),studentManagementEditPopupView.getNewPreName().getValue(),studentManagementEditPopupView.getNewEmail().getValue());
									
			}
		});

		studentManagementEditPopupView.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				
				((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).hide(true);
			}
		});
		
		((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).setPopupPosition(left-450, top - 50);
		
		((StudentManagementEditPopupViewImpl)studentManagementEditPopupView).show();
	}

	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<StudentProxy, C> fieldUpdater) {
		System.out.println("in Add Column");
		
		Column<StudentProxy, C> column = new Column<StudentProxy, C>(cell) {
			@Override
			public C getValue(StudentProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column);
	}
	
	/**
	 * Get a cell value from a record.
	 *
	 * @param <C> the cell type
	 */
	private static interface GetValue<C> {
		C getValue(StudentProxy contact);
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
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setPresenter(Presenter systemStartActivity) {
		this.presenter = systemStartActivity;
	}
	
	@Override
	public CellTable<StudentProxy> getTable() {
		return table;
	}
	
	@Override
	public Map getSortMap() {
		// TODO Auto-generated method stub
		return columnName;
	}
	
	@Override
	public List<String> getColumnSortSet()
	{
		return columnNameorder;
	}

	@Override
	public void setDetailPanel(boolean isDetailPlace) {

			if (isDetailPlace) {
				
//				int width = splitLayoutPanel.getWidget(0).getOffsetWidth();
//				int minWidth = width/2;
//				
//				Log.info("(splitLayoutPanel.getOffsetWidth()/2) == ="+(splitLayoutPanel.getOffsetWidth()/2));
//				Log.info("minWidth == ="+minWidth);
//				Log.info("width == ="+width);
//				if((width - (splitLayoutPanel.getOffsetWidth()/2)) > 100)
//					splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),width - minWidth);
				
				ResolutionSettings.setSplitLayoutPanelAnimation(splitLayoutPanel);
				
				delegate.doAnimation(true);
				splitLayoutPanel.animate(OsMaConstant.ANIMATION_TIME,
						new AnimationCallback() {
							
							@Override
							public void onLayout(Layer layer, double progress) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationComplete() {
								
							}
						});
				delegate.doAnimation(false);
			} else {
				splitLayoutPanel.setWidgetSize(splitLayoutPanel.getWidget(0),
						OsMaConstant.WIDTH_SIZE);
//				splitLayoutPanel
//				.animate(OsMaConstant.ANIMATION_TIME);
			}
//			widthSize = 1200;
//			decreaseSize = 0;
//			splitLayoutPanel.setWidgetSize(westPanel, widthSize);
			/*if (isDetailPlace) {

				timer = new Timer() {
					@Override
					public void run() {
						if (decreaseSize <= 705) {
							splitLayoutPanel.setWidgetSize(westPanel, 1225
									- decreaseSize);
							decreaseSize += 5;
						} else {
							timer.cancel();
						}
					}
				};
				timer.schedule(1);
				timer.scheduleRepeating(1);

			} else {
				widthSize = 1225;
				decreaseSize = 0;
				splitLayoutPanel.setWidgetSize(westPanel, widthSize);
			}*/

		
	}

	@Override
	public AcceptsOneWidget getDetailsPanel() {
		// TODO Auto-generated method stub
		return detailsPanel;
	}

	@Override
	public StudentManagementEditPopupView getStudentManagementEditPopView() {
		
		return studentManagementEditPopupView;
	}
	

}
