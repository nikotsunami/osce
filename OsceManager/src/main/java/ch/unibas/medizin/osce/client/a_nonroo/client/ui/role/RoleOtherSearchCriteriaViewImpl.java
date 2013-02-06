/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.SimpleSearchCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 * 
 */
public class RoleOtherSearchCriteriaViewImpl extends Composite implements RoleOtherSearchCriteriaView {

	// private AnamnesisCheckPlace place = null;

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends
			UiBinder<Widget, RoleOtherSearchCriteriaViewImpl> {
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private Delegate delegate;
	private OsMaRequestFactory requests;
	StandardizedRoleProxy proxy;

	// Highlight onViolation
	Map<String, Widget> simpleSearchCriteriaMap;
	// E Highlight onViolation

	@UiField
	Button addSimpleSearch;

	// @UiField
	// SimplePanel detailsPanel;

	@UiField(provided = true)
	CellTable<SimpleSearchCriteriaProxy> table;

	@UiField
	TextBox searchName;
	
	@UiField
	TextBox SearchValue;

	//SPEC Change
	@Override
	public Button getAddSimpleSearch() {
		return addSimpleSearch;
	}

	@Override
	public TextBox getSearchName() {
		return searchName;
	}

	@Override
	public TextBox getSearchValue() {
		return SearchValue;
	}
	//SPEC Change

	/*public void initList() {

		for (VisibleRange range : VisibleRange.values()) {
			// rangeNum.addItem(range.getName(), range.getName());
		}
		// rangeNum.setItemSelected(1, true);
	}*/

	protected ArrayList<String> paths = new ArrayList<String>();

	private Presenter presenter;

	@UiHandler("addSimpleSearch")
	public void newButtonClicked(ClickEvent event) {

		System.out.println(searchName.getValue() + "*"+ SearchValue.getValue() + "*");
		if ((searchName.getValue().trim().compareToIgnoreCase("") == 0)|| (searchName.getValue() == null)) {
			//Window.confirm("Search Name cant not be left blank.");
			
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
			 dialogBox.showConfirmationDialog("Search Name cant not be left blank.");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");
					return;
						}
					});

			
			
			return;
			// E: Issue Role
			
			
		}
		if ((SearchValue.getValue().trim().compareToIgnoreCase("") == 0)|| (SearchValue.getValue() == null)) {
			/*Window.confirm("Search Value cant not be left blank.");
			return;*/
			
			// Issue Role
			 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
			 dialogBox.showConfirmationDialog("Search Value cant not be left blank.");
			 
			 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					dialogBox.hide();							
					Log.info("ok click");
					return;
						}
					});

		
		return;
			// E: Issue Role
			
		}

		delegate.newSimpleSearchClicked(searchName.getValue(),SearchValue.getValue(), this.getValue());
		searchName.setValue("");
		SearchValue.setValue("");

	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public RoleOtherSearchCriteriaViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<SimpleSearchCriteriaProxy>(15, tableResources);

		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);

		initWidget(uiBinder.createAndBindUi(this));
		
		init();
		addSimpleSearch.setText(constants.addSimpleSearchCriteria());
		
		// Highlight onViolation
		simpleSearchCriteriaMap=new HashMap<String, Widget>();
		simpleSearchCriteriaMap.put("name", searchName);
		simpleSearchCriteriaMap.put("value", SearchValue);		
		// E Highlight onViolation
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// bugfix to avoid hiding of all panels (maybe there is a better
		// solution...?!)
		// DOM.setElementAttribute(splitLayoutPanel.getElement(), "style",
		// "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		paths.add("name");
		table.addColumn(new TextColumn<SimpleSearchCriteriaProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(SimpleSearchCriteriaProxy object) {
				return renderer.render(object.getName());
			}
		}, constants.simpleSearchCriteriaName());
		paths.add("value");
		table.addColumn(new TextColumn<SimpleSearchCriteriaProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(SimpleSearchCriteriaProxy object) {
				return renderer.render(object.getValue());
			}
		}, constants.simpleSearchCriteriaValue());
		addLastThreeColumn();

		// initList();
	}
	
	// SPEC Change
	public void removeLastThreeColumns() {
		if(table != null){
			if(table.getColumnCount() > 4){
				table.removeColumn(table.getColumnCount()-1);
				table.removeColumn(table.getColumnCount()-1);
				table.removeColumn(table.getColumnCount()-1);
			}
		}
	}

	//SPEC Change
	public void addLastThreeColumn() {
		addColumn(new ActionCell<SimpleSearchCriteriaProxy>(OsMaConstant.DOWN_ICON,
				new ActionCell.Delegate<SimpleSearchCriteriaProxy>() {
					public void execute(SimpleSearchCriteriaProxy proxy) {
						delegate.simpleSearchMoveDown(proxy, getValue());
					}
				}), "", new GetValue<SimpleSearchCriteriaProxy>() {
			public SimpleSearchCriteriaProxy getValue(SimpleSearchCriteriaProxy proxy) {
				return proxy;
			}
		}, null);
		addColumn(new ActionCell<SimpleSearchCriteriaProxy>(OsMaConstant.UP_ICON,
				new ActionCell.Delegate<SimpleSearchCriteriaProxy>() {
					public void execute(SimpleSearchCriteriaProxy proxy) {
						delegate.simpleSearchMoveUp(proxy, getValue());
					}
				}), "", new GetValue<SimpleSearchCriteriaProxy>() {
			public SimpleSearchCriteriaProxy getValue(SimpleSearchCriteriaProxy proxy) {
				return proxy;
			}
		}, null);

		addColumn(new ActionCell<SimpleSearchCriteriaProxy>(OsMaConstant.DELETE_ICON,
				new ActionCell.Delegate<SimpleSearchCriteriaProxy>() {
					public void execute(final SimpleSearchCriteriaProxy file) {
						// Window.alert("You clicked " +
						// institution.getInstitutionName());
						
						//if (Window.confirm(constants.reallyDelete()))
						
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									
									Log.info("yes click");	
									delegate.simpleSearchDeleteClicked(file, getValue());
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
				}), "", new GetValue<SimpleSearchCriteriaProxy>() {
			public SimpleSearchCriteriaProxy getValue(SimpleSearchCriteriaProxy simpleSearch) {
				return simpleSearch;
			}
		}, null);

		table.addColumnStyleName(2, "iconCol");
		table.addColumnStyleName(3, "iconCol");
		table.addColumnStyleName(4, "iconCol");

		// initList();
	}

	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter, FieldUpdater<SimpleSearchCriteriaProxy, C> fieldUpdater) {
		Column<SimpleSearchCriteriaProxy, C> column = new Column<SimpleSearchCriteriaProxy, C>(cell) {
			@Override
			public C getValue(SimpleSearchCriteriaProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		table.addColumn(column, headerText);
	}

	/**
	 * Get a cell value from a record.
	 * 
	 * @param <C>
	 *            the cell type
	 */
	private static interface GetValue<C> {
		C getValue(SimpleSearchCriteriaProxy proxy);
	}

	@Override
	public CellTable<SimpleSearchCriteriaProxy> getTable() {
		return table;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	// @Override
	// public SimplePanel getDetailsPanel() {
	// return detailsPanel;
	// }

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

//	@Override
//	public void setListBoxItem(String length) {
//		int index = 0;
//		int selectedIndex = 0;
//		for (VisibleRange range : VisibleRange.values()) {
//			if (range.getName().equals(length)) {
//				selectedIndex = index;
//
//			}
//			index++;
//		}
//
//		// rangeNum.setItemSelected(selectedIndex, true);
//	}
//
	@Override
	public void setValue(StandardizedRoleProxy proxy) {
		this.proxy = proxy;
	}

	public StandardizedRoleProxy getValue() {
		return proxy;
	}

	// Highlight onViolation
	@Override
	public Map getSimpleSearchCriteriaMap()
	{
		return this.simpleSearchCriteriaMap;
	}
	// E Highlight onViolation

}
