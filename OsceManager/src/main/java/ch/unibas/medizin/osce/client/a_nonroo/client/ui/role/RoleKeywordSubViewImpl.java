package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.KeywordProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
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
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Widget;

public class RoleKeywordSubViewImpl extends Composite implements RoleKeywordSubView {

	private static RoleKeywordSubViewUiBinder uiBinder = GWT.create(RoleKeywordSubViewUiBinder.class);

	interface RoleKeywordSubViewUiBinder extends UiBinder<Widget, RoleKeywordSubViewImpl> {
	}

	private List<AbstractEditableCell<?, ?>> editableCells;
	protected Set<String> paths = new HashSet<String>();
	private boolean addBoxesShown = true;
	private Delegate delegate;
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	public  static MultiWordSuggestOracle keywordoracle = new MultiWordSuggestOracle();
	
	/*private KeywordProxy keywordProxy;
	private IsWidget currentKeywordWidget;*/
	
	@UiField (provided = true)
	public CellTable<KeywordProxy> keywordTable;
	@UiField (provided = true)
	public SimplePager pager;
	
	
	@UiField
	public IconButton KeywordAddButton;
	
	/*@UiField(provided = true)
	public SuggestBox keywordSugestionBox =  new SuggestBox(keywordoracle);*/
	
	//public TextBox textbox;;
		
	// Issue Role Module
	
	//Issue # 122 : Replace pull down with autocomplete.
		
	@UiField
	public DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> keywordSugestionBox;
	/*
		@UiField(provided = true)
		public SuggestBox keywordSugestionBox =  //new SuggestBox(keywordoracle);
				new SuggestBox(
						new ProxySuggestOracle<KeywordProxy>(
								new AbstractRenderer<KeywordProxy>() {
									@Override
									public String render(KeywordProxy object) {
										return object.getName() ;
									}
								}// ));
								, ",;:. \t?!_-/\\"));
		*/
		//Issue # 122 : Replace pull down with autocomplete.
		
		
		//public TextBox textbox;
		// Issue Role Module
		
		// Highlight onViolation
		Map<String, Widget> keywordMap;
		// E Highlight onViolation
		
	public RoleKeywordSubViewImpl() 
	{
				
				
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		keywordTable = new CellTable<KeywordProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		
				
		initWidget(uiBinder.createAndBindUi(this));	
		initTable();
//		initSearchBox();
		initSuggestBox();
		KeywordAddButton.setText(constants.addKeyword());
		
		// Highlight onViolation
		keywordMap=new HashMap<String, Widget>();
		keywordMap.put("name", keywordSugestionBox.getTextField().advancedTextBox);
		keywordMap.put("standardizedRoles", keywordSugestionBox.getTextField().advancedTextBox);
		// E Highlight onViolation
		
				
	}
	
//	private void initSearchBox() 
//	{
//		searchKeywordBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
//			@Override
//			public void performAction() {
//				delegate.performKeywordSearch();
//			}
//		});
//	}
//	
	private void initSuggestBox()
	{
		
		// Issue Role Module
		/*keywordSugestionBox = new SuggestBox(new ProxySuggestOracle<KeywordProxy>(new AbstractRenderer<KeywordProxy>() {
				@Override
				public String render(KeywordProxy object) {
					return object.getName();
				}
		}));
		*/
//		keywordSugestionBox.setText(constants.enterQuestion());
		// Highlight onViolation
		
		/*keywordSugestionBox.getTextBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (keywordSugestionBox.getText().equals(constants.enterKeyword())) {					
					keywordSugestionBox.setText("");	
				}
			}
		});
		keywordSugestionBox.getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (keywordSugestionBox.getText().equals("")) {
					keywordSugestionBox.setText(constants.enterKeyword());
					
				}
			}
		});
		 keywordSugestionBox.addChangeListener(new ChangeListener() 
			{			
				@Override
				public void onChange(Widget sender) 
				{
					// TODO Auto-generated method stub
					System.out.println("on Change");
					keywordSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());					
				}
			});
		 keywordSugestionBox.setValue(constants.enterKeyword());*/
		// E Highlight onViolation
			// E Issue Role Module
		/*keywordSugestionBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() 
		{			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) 
			{
				// TODO Auto-generated method stub				
				System.out.println("on Selection");
				keywordSugestionBox.setValue(event.getSelectedItem().getReplacementString());
			}
			
		});
		
		keywordSugestionBox.addChangeListener(new ChangeListener() 
		{			
			@Override
			public void onChange(Widget sender) 
			{
				// TODO Auto-generated method stub
				System.out.println("on Change");
				keywordSugestionBox.setValue(((SuggestBox)sender).getTextBox().getValue());					
			}
		});*/
	}
	
	
	
	private void initTable() 
	{
		keywordTable.addColumn(new TextColumn<KeywordProxy>() 
		{
			Renderer<java.lang.String> renderer = new AbstractRenderer<String>() 
			{
				public String render(String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(KeywordProxy object) 
			{
				String keyword;
								
				if (object == null) 
				{
					Log.error("KeywordProxy is null");
				}
				else if ((keyword = object.getName()) == null) 
				{
					Log.error("Keyword = null");
				}
				else 
				{
					return renderer.render(keyword);
				} 
				return "";
			}
			
		}, constants.keywords());
		
		addLastColumn();
		
	}

	//SPEC Change
	public void removeLastColumn() {
		if(keywordTable != null){
			if(keywordTable.getColumnCount() > 1){
				keywordTable.removeColumn(keywordTable.getColumnCount()-1);
			}
		}
	}
	
	//SPEC Change
	public void addLastColumn() {
		addColumn(new ActionCell<KeywordProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<KeywordProxy>() {
					public void execute(final KeywordProxy keywordProxy) {
						
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();
									
									Log.info("yes click");
									delegate.deleteKeywordClicked(keywordProxy);
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
						
						
						/*if(Window.confirm(constants.reallyDelete()))
						{
							delegate.deleteKeywordClicked(keywordProxy);
						}*/
					}
				}), "", new GetValue<KeywordProxy>() {
					public KeywordProxy getValue(KeywordProxy keyword) {
						return keyword;
					}
		}, null);
		keywordTable.addColumnStyleName(2, "iconCol");
		
	}
	
	@Override
	public void setKeywordAutocompleteValue(List<KeywordProxy> values) {
	
		/*int index=0;
		while(index<values.size())
		{		
			keywordoracle.add(values.get(index).getName());
			index++;
		}
		textbox=new TextBox();
		//keywordSugestionBox = new SuggestBox(keywordoracle, new TextBox());
		keywordSugestionBox = new SuggestBox(keywordoracle, textbox);*/
		// Issue Role Module		
		
		//Issue # 122 : Replace pull down with autocomplete.
		

		DefaultSuggestOracle<KeywordProxy> suggestOracle1 = (DefaultSuggestOracle<KeywordProxy>) keywordSugestionBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);
		keywordSugestionBox.setSuggestOracle(suggestOracle1);
		//keywordSugestionBox.setRenderer(new KeywordProxyRenderer());
		
		keywordSugestionBox.setRenderer(new AbstractRenderer<KeywordProxy>() {

			@Override
			public String render(KeywordProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
					return object.getName();	
				}
				else
				{
					return "";
				}
				
			}
		});

				//((ProxySuggestOracle<KeywordProxy>) keywordSugestionBox	.getSuggestOracle()).addAll(values);
				// E Issue Role Module
				
				//Issue # 122 : Replace pull down with autocomplete.
	}
	
	private <C> void addColumn(Cell<C> cell, String headerText,	final GetValue<C> getter, FieldUpdater<KeywordProxy, C> fieldUpdater) 
	{
		Column<KeywordProxy, C> column = new Column<KeywordProxy, C>(cell) 
		{
			@Override
			public C getValue(KeywordProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		keywordTable.addColumn(column, headerText);
	}
	
	private static interface GetValue<C> 
	{
		C getValue(KeywordProxy proxyvalue);
	}
	
	@UiHandler("KeywordAddButton")
	public void KeywordAddButton(ClickEvent e) 
	{
		
		/*if(keywordSugestionBox.getValue().trim().equals(""))
		{
			Log.info("Suggest Box Value is NULL");
			Window.alert("Please Select/Add new Keyword");
		}
		else
		{
			delegate.addKeywordClicked("");
		
		}*/
		
		// Issue Role Module		
		// Highlight onViolation
		Log.info("KeywordAddButton call");
		/*if(keywordSugestionBox.getValue().trim().equals("") || keywordSugestionBox.getValue().trim().equals(constants.enterKeyword()))
				{
					Log.info("Suggest Box Value is NULL");
					Log.info("getTextBox().getValue() TextBox Value: " + keywordSugestionBox.getTextBox().getValue());
					//Window.alert("Please Select/Add new Keyword");
					
					// Issue Role
					 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox("Success");
					 dialogBox.showConfirmationDialog("Please Select/Add new Keyword");
					 
					 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.hide();							
							Log.info("ok click");	
								}
							});

					
					
				// E: Issue Role
				}*/
				//else
				//{			
					delegate.addKeywordClicked();
					//keywordSugestionBox.setText(constants.enterKeyword());
				//}
				// E Highlight onViolation
				// E Issue Role Module
				

				
				
	}
	
	@Override
	public CellTable<KeywordProxy> getKeywordTable() {
		return keywordTable;
	}
	
	@Override
	public void setDelegate(Delegate delegate) 
	{
		this.delegate=delegate;			
	}
	// Highlight onViolation
	@Override
	public Map getKeywordMap()
	{
		return this.keywordMap;
	}
	
	// E Highlight onViolation
	
}
