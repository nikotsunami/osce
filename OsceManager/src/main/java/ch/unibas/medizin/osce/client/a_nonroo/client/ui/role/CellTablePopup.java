/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.style.resources.MyCellTableResourcesCustom;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * @author dk
 *
 */
public class CellTablePopup extends PopupPanel {

//	private static RoleViewUiBinder uiBinder = GWT
//			.create(RoleViewUiBinder.class);

	
	private static CellTablePopupUiBinder uiBinder = GWT
			.create(CellTablePopupUiBinder.class);
	
	interface CellTablePopupUiBinder extends UiBinder<Widget, CellTablePopup> {
	}

	
	@UiField
	FocusPanel filterPanelRoot;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField(provided = true)
	public CellTable<String> cellTable;
	
	//public CellTable<String> cellTable =new CellTable<String>();
	
	
	public List<String> defaultColumnList=new ArrayList<String>();
	public MultiSelectionModel<String> multiselectionModel;
	
	public int x;
	public int y;
//	
	public CellTablePopup() {

		super(true);
		//spec  start
		CellTable.Resources tableResources = GWT.create(MyCellTableResourcesCustom.class);

		/*RootLayoutPanel.get().addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());*/
	
		cellTable = new CellTable<String>(15, tableResources);
		
		add(uiBinder.createAndBindUi(this));
		scrollPanel.setWidth("200px");
		ProvidesKey<String> keyProvider1 = ((AbstractHasData<String>) cellTable).getKeyProvider();
		multiselectionModel=new MultiSelectionModel<String>(keyProvider1);
		
		Column<String, Boolean> checkColumn = new Column<String, Boolean>(new CheckboxCell(true, false)) {
			  @Override
			  public Boolean getValue(String object) {
			    // Get the value from the selection model.
			    return multiselectionModel.isSelected(object);
			  }
			};
			
			//cellTable.setSelectionModel(multiselectionModel);
			
			
		cellTable.setSelectionModel(multiselectionModel,DefaultSelectionEventManager.<String> createCheckboxManager());
		//cellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		
		cellTable.addColumn(checkColumn);
		cellTable.setColumnWidth(checkColumn, 40, Unit.PX);
		cellTable.addColumn(new TextColumn<String>() {
			
			@Override
			public String getValue(String object) {
				// TODO Auto-generated method stub
				return object.toString();
			}
		});
		
		multiselectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				// TODO Auto-generated method stub

			for(int i=0;i<defaultColumnList.size();i++)
			{
			
				multiselectionModel.setSelected(defaultColumnList.get(i), true);
				
			}
			
			
			
			}
		});
		/*cellTable.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Log.info("table click");
				addDefaultValue(defaultColumnList);
				
			}
		}, ClickEvent.getType());
*/
		
	
	}
	
	public void addColumnList(List<String> columnList)
	{
		
		
		cellTable.setRowCount(columnList.size());
		cellTable.setRowData(columnList);
		
		
	}
    
	public void addDefaultValue(List<String> defaultList)
	{
		defaultColumnList.clear();
		for(int i=0;i<defaultList.size();i++)
		{
			defaultColumnList.add(defaultList.get(i));
			multiselectionModel.setSelected(defaultList.get(i), true);
			
		}
		
		
	}
    
	public List<String> getDefaultValue()
	{
		return defaultColumnList;
	}
	public CellTable<String> getTable()
	{
			return cellTable;
	}
    
	public MultiSelectionModel<String> getMultiSelectionModel()
	{
			return multiselectionModel;
	}
    
}
