package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;

import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TopicsAndSpecViewImpl extends Composite implements  TopicsAndSpecView, RecordChangeHandler {
	
	private static TopicsAndSpecViewUiBinder uiBinder = GWT
			.create(TopicsAndSpecViewUiBinder.class);

	interface TopicsAndSpecViewUiBinder extends UiBinder<Widget, TopicsAndSpecViewImpl> {
	}
	
	// Violation Changes Highlight
	Map<String, Widget> viewMap;
	// E Violation Changes Highlight
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;
	// Issue Role
		int left=0,top=0;
	
	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	
	@UiField
	public SimplePanel detailsPanel;
	
	@UiField (provided = true)
	public QuickSearchBox searchBox;
	
//	@UiField
//	Button FilterButton;
	


	@UiField(provided=true)
	CellTable<SpecialisationProxy> table;
	
	protected Set<String> paths = new HashSet<String>();
	@UiField
	TextBox	AddTextBox;
	
	@UiField 
	IconButton AddButton;
	
	@UiField (provided = true)
	SimplePager Pager;
		
	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	int widthSize=1225,decreaseSize=0;
	Timer timer;
	
	
	@UiHandler ("AddButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.newClicked(AddTextBox.getValue());
		AddTextBox.setValue("");
	}
	private Presenter presenter;
	
	public TopicsAndSpecViewImpl() {
		// TODO Auto-generated constructor stub
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<SpecialisationProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
				
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		Pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
				
				initWidget(uiBinder.createAndBindUi(this));
				init();
				splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);		
//				FilterButton.setText("Filter");	
				AddButton.setText(constants.addDiscipline());
				
				// Violation Changes Highlight
				Log.info("Call TopicsAndSpecViewImpl Constructor..");
				viewMap=new HashMap<String, Widget>();				
				viewMap.put("name",AddTextBox);
			// E Violation Changes Highlight
				
				
	}
	
	
	
	public String getQuery() {
		return searchBox.getValue();
	}
	// @Manish
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}
	
	public void init()
	{
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		AddTextBox.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		        	newButtonClicked(null);
		    }
		});
		
		
		//By spec for table insert
		
		
		// Issue Role
		
		table.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				left=event.getClientX();
				top=event.getClientY();
			}
		}, ClickEvent.getType());
		
		// E: Issue Role		
		
		paths.add("name");
		table.addColumn(new TextColumn<SpecialisationProxy>() {
			{ this.setSortable(true); }

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(SpecialisationProxy object) {
				return renderer.render(object.getName());
				
			}
		},constants.clinicalSpecialisation());
				
		//Edit Button
		addColumn(new ActionCell<SpecialisationProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<SpecialisationProxy>() {
					public void execute(final SpecialisationProxy specialization) {
										
						//delegate.editClicked(specialization);
						//Issue Role				
						delegate.editClicked(specialization,left,top);
											
					}
				}), "", new GetValue<SpecialisationProxy>() {
			public SpecialisationProxy getValue(SpecialisationProxy specialization) {
				return specialization;
			}
		}, null);
		table.addColumnStyleName(1, "iconCol");

		// Detete button
		addColumn(new ActionCell<SpecialisationProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<SpecialisationProxy>() {
					public void execute(final SpecialisationProxy specialization) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						
						/*if(Window.confirm("wirklich löschen?"))
							delegate.deleteClicked(specialization);*/
						
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();									
									Log.info("yes click");
									delegate.deleteClicked(specialization);
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
				}), "", new GetValue<SpecialisationProxy>() {
			public SpecialisationProxy getValue(SpecialisationProxy specialization) {
				return specialization;
			}
		}, null);
		table.addColumnStyleName(1, "iconCol");

	
	
	}
	
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<SpecialisationProxy, C> fieldUpdater) {
		Column<SpecialisationProxy, C> column = new Column<SpecialisationProxy, C>(cell) {
			@Override
			public C getValue(SpecialisationProxy object) {
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
	public CellTable<SpecialisationProxy> getTable() {
		return table;
	}
	
	private static interface GetValue<C> {
		C getValue(SpecialisationProxy contact);
	}
	
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}
	public void setDetailPanel(boolean isDetailPlace) {

		splitLayoutPanel.setWidgetSize(westPanel, Integer.parseInt(constants.widthSize()) - Integer.parseInt(constants.widthMin()) );
		splitLayoutPanel.animate(Integer.parseInt(constants.animationTime()));	
		/*splitLayoutPanel.animate(150000);
//		widthSize = 1200;
//		decreaseSize = 0;
//		splitLayoutPanel.setWidgetSize(westPanel, widthSize);
		if (isDetailPlace) {

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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
		
	// Violation Changes Highlight

	@Override
	public Map getMap() {
		// TODO Auto-generated method stub
		return this.viewMap;
	}

	@Override
	public TextBox getTextBox() 
	{
		return this.AddTextBox;		
	}
	// E Violation Changes Highlight
	
	// by spec
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
	// by spec

}
