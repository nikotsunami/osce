package ch.unibas.medizin.osce.client.style.resources;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.CellTablePopup;
import ch.unibas.medizin.osce.shared.OsMaConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

public class AdvanceCellTable<T> extends CellTable<T>{

	public CellTablePopup filter=new CellTablePopup();
	List<String> totalColumnList =new ArrayList<String>();
	List<String> defaultColumn =new ArrayList<String>();
	public List<String> InitailColumn =new ArrayList<String>();
	
	 
	//public CellTable<T> table;
	
	/*public CustomCellTable() {
		//CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
	//	table =  new CellTable<T>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
	}*/
	
	
	/**
	   * Constructs a table with a default page size of 15.
	   */
	  public AdvanceCellTable() {
	    super();
	   

	  }

	  /**
	   * Constructs a table with the given page size.
	   * 
	   * @param pageSize the page size
	   */
	  public AdvanceCellTable(final int pageSize) {
	    super(pageSize);
	    
	    /*this.addHandler(ContextMenuHandler, new ContextMenuHandler() {
			
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				Log.info("onContextMenu ContextMenuHandler");
				event.preventDefault();
				event.stopPropagation();
				
			}
		});*/
	  }

	  /**
	   * Constructs a table with a default page size of 15, and the given
	   * {@link ProvidesKey key provider}.
	   * 
	   * @param keyProvider an instance of ProvidesKey<T>, or null if the record
	   *          object should act as its own key
	   */
	  public AdvanceCellTable(ProvidesKey<T> keyProvider) {
	    super( keyProvider);
	  }

	  /**
	   * Constructs a table with the given page size with the specified
	   * {@link Resources}.
	   * 
	   * @param pageSize the page size
	   * @param resources the resources to use for this widget
	   */
	  public AdvanceCellTable(final int pageSize, Resources resources) {
	    super(pageSize, resources);
	  }

	  /**
	   * Constructs a table with the given page size and the given
	   * {@link ProvidesKey key provider}.
	   * 
	   * @param pageSize the page size
	   * @param keyProvider an instance of ProvidesKey<T>, or null if the record
	   *          object should act as its own key
	   */
	  public AdvanceCellTable(final int pageSize, ProvidesKey<T> keyProvider) {
	    super(pageSize,  keyProvider);
	  }

	  /**
	   * Constructs a table with the given page size, the specified
	   * {@link Resources}, and the given key provider.
	   * 
	   * @param pageSize the page size
	   * @param resources the resources to use for this widget
	   * @param keyProvider an instance of ProvidesKey<T>, or null if the record
	   *          object should act as its own key
	   */
	  public AdvanceCellTable(final int pageSize, Resources resources,
	      ProvidesKey<T> keyProvider) {
	    super(pageSize, resources, keyProvider
	        );
	  }

	  /**
	   * Constructs a table with the specified page size, {@link Resources}, key
	   * provider, and loading indicator.
	   * 
	   * @param pageSize the page size
	   * @param resources the resources to use for this widget
	   * @param keyProvider an instance of ProvidesKey<T>, or null if the record
	   *          object should act as its own key
	   * @param loadingIndicator the widget to use as a loading indicator, or null
	   *          to disable
	   */
	  public AdvanceCellTable(final int pageSize, Resources resources,
	      ProvidesKey<T> keyProvider, Widget loadingIndicator) {
	    super( pageSize,resources, keyProvider,loadingIndicator);	   
	   
	  }
	  
	 
	  /**
	   * Adds a column to the end of the table with an associated String header.
	   * 
	   * @param col the column to be added
	   * @param headerString the associated header text, as a String
	   */
	 // @Override
	 
	  public void addColumn(Column<T, ?> col, String headerString) {
			
		  addColumn(col, headerString, true);
			 
		
	  }
	
	  public void addColumn(Column<T, ?> col, String headerString,  boolean defaultSelected)
	  {
		  addColumn(col, headerString,defaultSelected,false);
	  }
	  
	  public void addColumn(Column<T, ?> col, String headerString,  boolean defaultSelected,boolean initalSlected) {
		 
		  //super.insertColumn(getColumnCount(), col, headerString);
		  if(initalSlected==true)
		  {
			  if(!InitailColumn.contains(headerString))
			  {
				  InitailColumn.add(headerString);
				  
			  }
		  }
	   
		  super.addColumn(col, headerString);
	   
	   
		  
	    //insert second column of popup	   
		 
			  super.addColumn(new TextColumn<T>() {
				  {
          		this.setSortable(true);
          		this.setHorizontalAlignment(ALIGN_LEFT);
          		
          		
          	}
        	
  			@Override
  			public String getValue(T object) {
  				// TODO Auto-generated method stub
  				return "";
  			}
  			
  			
          //},columnHeader1);	
  		},OsMaConstant.DOWN_ICONFORCELLTABLE);
			
			  
		 
		 this.setColumnWidth(this.getColumn(this.getColumnCount()-1), 5, Unit.PX);
		  
		  if(defaultSelected==true)
		  {
			  if(!defaultColumn.contains(headerString))
			  {
				  defaultColumn.add(headerString);
				  
			  }
		  }
		  
		  if(!totalColumnList.contains(headerString))
		  {
			  totalColumnList.add(headerString);
			  filter.addColumnList(totalColumnList);
		  }
		  filter.addDefaultValue(defaultColumn);
	   
	  }
	  
	  public List<String> getInitList()
	  {
		  return InitailColumn;
	  }
	
	  public CellTablePopup getPopup()
	  {
		  return filter;
	  }
}
