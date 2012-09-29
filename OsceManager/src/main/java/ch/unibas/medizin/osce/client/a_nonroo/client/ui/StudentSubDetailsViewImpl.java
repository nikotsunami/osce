package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.activity.StudentsActivity;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class StudentSubDetailsViewImpl extends Composite implements StudentSubDetailsView {

	
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, StudentSubDetailsViewImpl> {
	}


	
private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;

	private StudentSubDetailsViewImpl StudentSubDetailsViewImpl;
	
	@UiField (provided = true)
	public QuickSearchBox searchBox;


	@UiField (provided = true)
	SimplePager pager;

	@UiField (provided = true)
	CellTable<StudentOscesProxy> table;
	
	@UiField
	public Hidden hidden;
	
	@UiField
	FileUpload fileUpload;
	
	@UiField
	FormPanel uploadFormPanel;
	
	@UiField Button importfile;

//	public OsceProxy osceProxy;

	

	protected Set<String> paths = new HashSet<String>();

	public Hidden getHidden() {
		return hidden;
	}

	public void setHidden(Hidden hidden) {
		this.hidden = hidden;
	}


	private Presenter presenter;

	

	public QuickSearchBox getSearchBox() {
		return searchBox;
	}

	public void setSearchBox(QuickSearchBox searchBox) {
		this.searchBox = searchBox;
	}

	public StudentSubDetailsViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<StudentOscesProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		initWidget(BINDER.createAndBindUi(this));
		
		 uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		 uploadFormPanel.setMethod(FormPanel.METHOD_POST);
		 uploadFormPanel.setAction(GWT.getHostPageBaseURL()+"CsvFileUploadServlet");
		 
		 hidden.setName("hidden");
		 importfile.setText(constants.importStudents());	 
		
		 
		 uploadFormPanel.addSubmitHandler(new FormPanel.SubmitHandler()
		    {
		        @Override
		        public void onSubmit(SubmitEvent event)
		        {
		            // TODO Auto-generated method stub
		            String fileName = fileUpload.getFilename();

		            if(fileName.length() == 0)
		            {
		                /*Window.alert("Error: no file is selected. Please select a file to be uploaded.");*/
		            	// Highlight onViolation
		            	fileUpload.addStyleName("higlight_onViolation");
		            	// E Highlight onViolation
		                event.cancel();
		            }
		            else if(!fileName.endsWith("xml") && !fileName.endsWith("json") && !fileName.endsWith("csv"))
		            {
		                /*Window.alert("Error: file format not supported. Only supports XML, CSV and JSON");*/
		            	// Highlight onViolation
		            	fileUpload.addStyleName("higlight_onViolation");
		            	// E Highlight onViolation
		                event.cancel();
		            }
		            //else
		                //loadBox.hide();
		        }
		    });

		 uploadFormPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
				
			
		        @Override
		        public void onSubmitComplete(SubmitCompleteEvent event)
		        {
		            // TODO Auto-generated method stub
		           // Window.alert(event.getResults());
		        }
		    });
		

		
		
		

		init();
//		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
//		newButton.setText(constants.addTrait());
	}
	
	public StudentSubDetailsViewImpl(OsceProxy osceProxy) {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<StudentOscesProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});
		initWidget(BINDER.createAndBindUi(this));
		
		 uploadFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		 uploadFormPanel.setMethod(FormPanel.METHOD_POST);
		 uploadFormPanel.setAction(GWT.getHostPageBaseURL()+"CsvFileUploadServlet");
		 
		 hidden.setName("hidden");
		 hidden.setValue(String.valueOf(osceProxy.getId()));
		 importfile.setText(constants.importStudents());	 
		
		 
		 uploadFormPanel.addSubmitHandler(new FormPanel.SubmitHandler()
		    {
		        @Override
		        public void onSubmit(SubmitEvent event)
		        {
		            // TODO Auto-generated method stub
		            String fileName = fileUpload.getFilename();

		           if(fileName.length() == 0)
		            {
		                /*Window.alert("Error1: no file is selected. Please select a file to be uploaded.");*/
		             // Highlight onViolation
		            	fileUpload.addStyleName("higlight_onViolation");
		            	// E Highlight onViolation
		                event.cancel();
		            }
		            else if(!fileName.endsWith("xml") && !fileName.endsWith("json") && !fileName.endsWith("csv"))
		            {
		                //Window.alert("Error1: file format not supported. Only supports XML, CSV and JSON");
		            	// Highlight onViolation
		            	fileUpload.addStyleName("higlight_onViolation");
		            	// E Highlight onViolation
		                event.cancel();
		            }
		            //else
		                //loadBox.hide();
		        }
		    });

		 uploadFormPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
				
			
		        @Override
		        public void onSubmitComplete(SubmitCompleteEvent event)
		        {
		            // TODO Auto-generated method stub
		           // Window.alert(event.getResults());
		        	delegate.importClicked();
		        }
		    });
		

		
		
		

		init();
//		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
//		newButton.setText(constants.addTrait());
	}
	
	@UiHandler("importfile")
	public void importfileClicked(ClickEvent event)
	{
		Log.info("~~OSCE ID FOR SERVLET : " + hidden.getValue());
		uploadFormPanel.submit();
		
		
	}

	
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		
		// bugfix to avoid hiding of all panels (maybe there is a better lution...?!)
		//DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
//		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
//		
		paths.add("name");
		table.addColumn(new TextColumn<StudentOscesProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StudentOscesProxy object) {
			//	Log.info("get value here "+ object.getStudent().getName());
				return renderer.render(object.getStudent().getName());
			
			}
		}, constants.name());
		
		
		paths.add("Prename");
		
		table.addColumn(new TextColumn<StudentOscesProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(StudentOscesProxy object) {
				return renderer.render(object.getStudent().getPreName());
			}
		}, constants.preName());
		
//		addColumn(new ActionCell<StudentOscesProxy>(
//				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<StudentOscesProxy>() {
//					public void execute(StudentOscesProxy studentOscesProxy) {
//						//Window.alert("You clicked " + institution.getInstitutionName());
//						if(Window.confirm("wirklich löschen?"))
//							delegate.deleteClicked(studentOscesProxy);
//					}
//				}), "", new GetValue<StudentOscesProxy>() {
//			public StudentOscesProxy getValue(StudentOscesProxy studentOscesProxy) {
//				return studentOscesProxy;
//			}
//		}, null);
//	

	table.addColumn(new IdentityColumn<StudentOscesProxy>(
			  
				new ActionCell<StudentOscesProxy>("",
						 
						new ActionCell.Delegate<StudentOscesProxy>() {
							@Override
							public void execute(
									StudentOscesProxy object) {
								Log.info("~~INSIDE EXECUTE OF ONRENDER");
							  
								
							 
							}

						}) {
					
					@Override
							public void render(
									com.google.gwt.cell.client.Cell.Context context,
									StudentOscesProxy value, SafeHtmlBuilder sb) {
						
						Log.info("~~INSIDE RENDER OF ONRENDER");
							Log.info("appnend called");
							
		 
							sb.append((value.getIsEnrolled())?OsMaConstant.CHECK_ICON
									: OsMaConstant.UNCHECK_ICON);
						super.render(context, value, sb);
							}
					
					
					@Override
					public void onBrowserEvent(
							com.google.gwt.cell.client.Cell.Context context,
							Element elem, StudentOscesProxy studentOscesProxy,
							NativeEvent nativeEvent,
							ValueUpdater<StudentOscesProxy> valueUpdater) {
						
						Log.info("~~Inside OnBrowse Event");
					
						
						Boolean test = delegate.onRender(studentOscesProxy);
						
						Log.info("Test : " + test);
						
						if (test == true)
						{
							
							elem.setInnerHTML(OsMaConstant.UNCHECK_ICON.asString());
						}
						else if (test == false)
						{
							elem.setInnerHTML(OsMaConstant.CHECK_ICON.asString());
						}
						
					//	init();
					
					/*	int test = delegate.onRender(studentOscesProxy);
						
						Log.info("~~TEST : " + test);
						
						if (test == 1)
							elem.setInnerHTML(OsMaConstant.UNCHECK_ICON.asString());
						else if (test == 2)
						*/	
						
						super.onBrowserEvent(context, elem, studentOscesProxy, nativeEvent, valueUpdater);
					} 
					
				}) ) ;
				

	//	table.addColumnStyleName(2, "iconCol");
	}
	
	/**
	 * Add a column with a header.
	 *
	 * @param <C> the cell type
	 * @param cell the cell used to render the column
	 * @param headerText the header string
	 * @param getter the value getter for the cell
	 */
	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<StudentOscesProxy, C> fieldUpdater) {
		Column<StudentOscesProxy, C> column = new Column<StudentOscesProxy, C>(cell) {
			@Override
			public C getValue(StudentOscesProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		table.addColumn(column, headerText);
	}
	
	/**
	 * Get a cell value from a record.
	 *
	 * @param <C> the cell type
	 */
	private static interface GetValue<C> {
		C getValue(StudentOscesProxy contact);
	}

	private List<AbstractEditableCell<?, ?>> editableCells;

	@Override
	public CellTable<StudentOscesProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
}



