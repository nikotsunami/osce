/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.RoomProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class RoomViewImpl extends Composite implements RoomView {

	private static RoomViewUiBinder uiBinder = GWT
			.create(RoomViewUiBinder.class);

	interface RoomViewUiBinder extends UiBinder<Widget, RoomViewImpl> {
	}

	private Delegate delegate;
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	int left = 0;
	int top = 0;
	
	@UiField
	SplitLayoutPanel splitLayoutPanel;

	@UiField (provided = true)
	QuickSearchBox searchBox;

/*	@UiField
	TextBox newRoomNumber;

	@UiField
	TextBox newRoomLength;

	@UiField
	TextBox newRoomWidth; */

	@UiField
	Button newButton;

	@UiField (provided = true)
	SimplePager pager;

	@UiField (provided = true)
	CellTable<RoomProxy> table;

	protected Set<String> paths = new HashSet<String>();

	public RoomEditPopupView roomEditPopup;
	
	private Presenter presenter;

	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {

		if (roomEditPopup == null)
		{
			roomEditPopup = new RoomEditPopupViewImpl();
			
			((RoomEditPopupViewImpl)roomEditPopup).setAnimationEnabled(true);
			
			((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().setValue(constants.roomNumber());
			((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().setValue(constants.roomLength());
			((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().setValue(constants.roomWidth());
			
			((RoomEditPopupViewImpl)roomEditPopup).setWidth("200px");
			
			RootPanel.get().add(((RoomEditPopupViewImpl)roomEditPopup));
			
			// Highlight onViolation
			
			roomNewMap=new HashMap<String, Widget>();
			roomNewMap.put("roomNumber",((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber());
			roomNewMap.put("oscePostRooms",((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber());
			roomNewMap.put("length",((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength());
			roomNewMap.put("width",((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth());
			
			// E Highlight onViolation
			
			roomEditPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					// TODO Auto-generated method stub
					// Highlight onViolation

					/*if ((((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().getValue()).equals(constants.roomNumber()) || (((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().getValue()).equals(constants.roomLength()) || (((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().getValue()).equals(constants.roomWidth()))
					{
						MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						messageConfirmationDialogBox.showConfirmationDialog("Enter Correct Value");
						
						//Window.alert("Enter Correct Value");
					}
					else
					{*/
					// E Highlight onViolation
					double roomLength = ((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().getValue().length() > 0 ? Double.parseDouble(((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().getValue()) : 0;
						double roomWidth = ((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().getValue().length() > 0 ? Double.parseDouble(((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().getValue()) : 0;
						
						delegate.newClicked(((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().getValue() , roomLength, roomWidth);
						//Window.alert("Button Clicked");
						
						((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().setValue(constants.roomNumber());
						((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().setValue(constants.roomLength());
						((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().setValue(constants.roomWidth());
						// Highlight onViolation
						/*						
						((RoomEditPopupViewImpl)roomEditPopup).hide(true);					
					 }*/
						// E Highlight onViolation
					
				}
			});
		}
		
		roomEditPopup.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				
				((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().setValue(constants.roomNumber());
				((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().setValue(constants.roomLength());
				((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().setValue(constants.roomWidth());
				((RoomEditPopupViewImpl)roomEditPopup).hide(true);
			}
		});
		
		((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().setValue(constants.roomNumber());
		((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().setValue(constants.roomLength());
		((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().setValue(constants.roomWidth());
		
		((RoomEditPopupViewImpl)roomEditPopup).setPopupPosition(event.getClientX(), event.getClientY());
		
		((RoomEditPopupViewImpl)roomEditPopup).show();		

	
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
	
	// Highlight onViolation
	Map<String, Widget> roomMap;
	Map<String, Widget> roomNewMap;
	RoomView roomView;
// E Highlight onViolation
	
	public RoomViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<RoomProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);

		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		init();

		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		newButton.setText(constants.addRoom());
		
		// Highlight onViolation
		roomView=this;
				
						
				// E Highlight onViolation
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

       public void editPopupView(final RoomProxy proxy) {
		
		
			roomEditPopup = new RoomEditPopupViewImpl();
			
			((RoomEditPopupViewImpl)roomEditPopup).setAnimationEnabled(true);
			
			((RoomEditPopupViewImpl)roomEditPopup).setWidth("200px");
			
			((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().setValue(proxy.getRoomNumber());
			((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().setValue(String.valueOf(proxy.getLength()));
			((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().setValue(String.valueOf(proxy.getWidth()));
			
			RootPanel.get().add(((RoomEditPopupViewImpl)roomEditPopup));
			
			// Highlight onViolation
			
			roomMap=new HashMap<String, Widget>();
			roomMap.put("roomNumber",((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber());
			roomMap.put("oscePostRooms",((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber());
			roomMap.put("length",((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength());
			roomMap.put("width",((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth());
			
			// E Highlight onViolation
			
			roomEditPopup.getOkBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					// TODO Auto-generated method stub
					// Highlight onViolation
				
					/*if ((((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().getValue()).equals(constants.roomNumber()) || (((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().getValue()).equals(constants.roomLength()) || (((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().getValue()).equals(constants.roomWidth()))
					{
						MessageConfirmationDialogBox messageConfirmationDialogBox = new MessageConfirmationDialogBox(constants.warning());
						messageConfirmationDialogBox.showConfirmationDialog("Enter Correct Value");
					}
					else
					{*/
					// E Highlight onViolation
						double roomLength = ((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().getValue().length() > 0 ? Double.parseDouble(((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().getValue()) : 0;
						double roomWidth = ((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().getValue().length() > 0 ? Double.parseDouble(((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().getValue()) : 0;
						
						delegate.editClicked(proxy, ((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().getValue() , roomLength, roomWidth);
						//Window.alert("Button Clicked");
						
						// Highlight onViolation
						/*
						((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().setValue(constants.roomNumber());
						((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().setValue(constants.roomLength());
						((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().setValue(constants.roomWidth());
						
						((RoomEditPopupViewImpl)roomEditPopup).hide(true);
					}*/
						// E Highlight onViolation
					
				}
			});
	
			roomEditPopup.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					
					((RoomEditPopupViewImpl)roomEditPopup).getNewRoomNumber().setValue(constants.roomNumber());
					((RoomEditPopupViewImpl)roomEditPopup).getNewRoomLength().setValue(constants.roomLength());
					((RoomEditPopupViewImpl)roomEditPopup).getNewRoomWidth().setValue(constants.roomWidth());
					((RoomEditPopupViewImpl)roomEditPopup).hide(true);
				}
			});
			
			((RoomEditPopupViewImpl)roomEditPopup).setPopupPosition(left-350, top - 50);
			
			((RoomEditPopupViewImpl)roomEditPopup).show();
	}

	public void init() {
	/*	newRoomNumber.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					newButtonClicked(null);
			}
		});
		newRoomLength.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					newButtonClicked(null);
			}
		});
		newRoomWidth.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					newButtonClicked(null);
			}
		}); */

               	table.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				left = event.getClientX();
				top = event.getClientY();
				
			}
		}, ClickEvent.getType());


		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();

		paths.add("room_number");
		table.addColumn(new TextColumn<RoomProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoomProxy object) {
				return renderer.render(object.getRoomNumber());
			}
		}, constants.roomNumber());
		paths.add("length");
		table.addColumn(new TextColumn<RoomProxy>() {

			Renderer<Double> renderer = new AbstractRenderer<Double>() {

				public String render(Double obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoomProxy object) {
				return renderer.render(object.getLength());
			}
		}, constants.roomLength());
		table.addColumn(new TextColumn<RoomProxy>() {

			Renderer<Double> renderer = new AbstractRenderer<Double>() {

				public String render(Double obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(RoomProxy object) {
				return renderer.render(object.getWidth());
			}
		}, constants.roomWidth());
		
		addColumn(new ActionCell<RoomProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<RoomProxy>() {
						public void execute(RoomProxy room){
								editPopupView(room);
						}
		}), "", new GetValue<RoomProxy>() {
			public RoomProxy getValue(RoomProxy room){
				return room;
			}
		}, null);
		
		addColumn(new ActionCell<RoomProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<RoomProxy>() {
					public void execute(RoomProxy room) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						if(Window.confirm("wirklich löschen?"))
							delegate.deleteClicked(room);
					}
				}), "", new GetValue<RoomProxy>() {
			public RoomProxy getValue(RoomProxy room) {
				return room;
			}
		}, null);

		table.addColumnStyleName(2, "iconCol");
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
			final GetValue<C> getter, FieldUpdater<RoomProxy, C> fieldUpdater) {
		Column<RoomProxy, C> column = new Column<RoomProxy, C>(cell) {
			@Override
			public C getValue(RoomProxy object) {
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
		C getValue(RoomProxy contact);
	}

	private List<AbstractEditableCell<?, ?>> editableCells;

	@Override
	public CellTable<RoomProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	//	@Override
	//	public SimplePanel getDetailsPanel() {
	//		return detailsPanel;
	//	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	// Highlight onViolation

			@Override
			public RoomView getRoomView() {
				// TODO Auto-generated method stub
				return this.roomView;
			}

			@Override
			public RoomEditPopupView getRoomEditPopView() {
				// TODO Auto-generated method stub
				return this.roomEditPopup;
			}
			
			@Override
			public Map getRoomMap() {
				return this.roomMap;
			}
			
			@Override
			public Map getNewRoomMap() {
				return this.roomNewMap;
			}
			// E Highlight onViolation
	
}
