/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.request.OsMaRequestFactory;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.i18n.OsceConstantsWithLookup;
import ch.unibas.medizin.osce.client.managed.activity.AdministratorEditActivityWrapper.View;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.style.resources.AnamnesisCheckImageResources;
import ch.unibas.medizin.osce.client.style.resources.AnamnesisQuestionTypeImages;
import ch.unibas.medizin.osce.client.style.resources.AnamnesisTableResources;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.dom.client.Style.VerticalAlign;

/**
 * @author dk
 * 
 */
public class AnamnesisCheckViewImpl extends Composite implements
		AnamnesisCheckView {

	// private AnamnesisCheckPlace place = null;

	private static SystemStartViewUiBinder uiBinder = GWT
			.create(SystemStartViewUiBinder.class);

	interface SystemStartViewUiBinder extends
			UiBinder<Widget, AnamnesisCheckViewImpl> {
	}

	private final OsceConstants constants = GWT.create(OsceConstants.class);
	private Delegate delegate;
	private OsMaRequestFactory requests;
	private Column<AnamnesisCheckProxy, String> checkOrderColumn;

	@UiField
	SplitLayoutPanel splitLayoutPanel;

	@UiField(provided = true)
	QuickSearchBox searchBox;

	@UiField
	Button newButton;

	@UiField
	SimplePanel detailsPanel;
//
//	@UiField(provided = true)
//	SimplePager pager;

//	@UiField(provided = true)
//	CellTable<AnamnesisCheckProxy> table;

//	@UiField
//	ListBox rangeNum;
	@UiField
	ListBox filterTitle;
	@UiField
	Button saveOrder;
	IconButton refreshButton;
	
	@UiField
    VerticalPanel anamnesisCheckPanel;
	
	@UiField
	ScrollPanel scrollPanel;

	@UiHandler("refreshButton")
	public void refreshButtonClicked(ClickEvent event) {
		delegate.performSearch(searchBox.getValue());
	}

	@UiHandler("filterTitle")
	public void filterTitleChangeHandler(ChangeEvent event) {

		delegate.changeFilterTitleShown(filterTitle.getItemText(filterTitle
				.getSelectedIndex()));

	}

//	@UiHandler("rangeNum")
//	public void rangeNumChangeHandler(ChangeEvent event) {
//		delegate.changeNumRowShown(rangeNum.getItemText(rangeNum
//				.getSelectedIndex()));
//
//	}

	@UiHandler("saveOrder")
	public void onSaveOrder(ClickEvent event) {
		GWT.log("onSaveOrder delegate = " + delegate);
		delegate.saveOrder();
//		table.removeColumn(0);
//		table.removeColumn(1);
//		table.removeColumn(2);
//		table.removeColumn(0);
//		table.removeColumn(1);
//		table.removeColumn(checkOrderColumn);
		init();

	}

//	public void initList() {
//
//		for (VisibleRange range : VisibleRange.values()) {
//			rangeNum.addItem(range.getName(), range.getName());
//		}
//		rangeNum.setItemSelected(1, true);
//	}

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	@UiHandler("newButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.newTitleClicked();
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
	public AnamnesisCheckViewImpl() {

//		CellTable.Resources tableResources = GWT
//				.create(MyCellTableResources.class);
//		table = new CellTable<AnamnesisCheckProxy>(15, tableResources);

//		SimplePager.Resources pagerResources = GWT
//				.create(MySimplePagerResources.class);
//		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
//				true, 30, true);

		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		init();
//		initList();

	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		// bugfix to avoid hiding of all panels (maybe there is a better
		// solution...?!)
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0),
				OsMaConstant.SPLIT_PANEL_MINWIDTH);
		saveOrder.setText(constants.saveOrder());
		newButton.setText(constants.addAnamnesisValue());
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style",
				"position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

//		paths.add("type");
//		table.addColumn(new QuestionTypeColumn(), constants.type());
//		paths.add("text");
//		table.addColumn(new SimpleTextColumn(), constants.question());
//		paths.add("value");
//		table.addColumn(new ValueColumn(), constants.possibleAnswers());

//		checkOrderColumn = new Column<AnamnesisCheckProxy, String>(
//				new TextInputCell()) {
//			@Override
//			public String getValue(AnamnesisCheckProxy object) {
//				if (object.getUserSpecifiedOrder() != null) {
//					return String.valueOf(object.getUserSpecifiedOrder());
//				} else {
//
//					return "";
//				}
//			}
//		};
//		checkOrderColumn
//				.setFieldUpdater(new FieldUpdater<AnamnesisCheckProxy, String>() {
//
//					@Override
//					public void update(int index, AnamnesisCheckProxy object,
//							String value) {
//						try {
//							GWT.log("??????checkOrderColumn.setFieldUpdater value = "
//									+ value);
//							GWT.log("????object type= " + object.getType());
//							GWT.log("????object sortoder= "
//									+ object.getSort_order());
//							GWT.log("????object value= " + value);
//							// GWT.log("????object UserSpecifiedOrder= "+object.getUserSpecifiedOrder());
//							// Integer userSpecifiedOrder =
//							// Integer.valueOf(value);
//							// delegate.resetUserSpecifiedOrder(object, value);
//							if (value != null && !value.equals("")) {
//								delegate.orderEdited(object, value);
//							} else {
//								delegate.orderEdited(object, null);
//							}
//							// object.setUserSpecifiedOrder(userSpecifiedOrder);
//						} catch (Exception e) {
//							GWT.log(value + e);
//						}
//
//					}
//				});
//		table.addColumn(checkOrderColumn, constants.order());
//		table.setColumnWidth(checkOrderColumn, "10px");
//
//		addColumn(new ActionCell<AnamnesisCheckProxy>(OsMaConstant.DOWN_ICON,
//				new ActionCell.Delegate<AnamnesisCheckProxy>() {
//					public void execute(AnamnesisCheckProxy proxy) {
//						delegate.moveDown(proxy);
//					}
//				}), "", new GetValue<AnamnesisCheckProxy>() {
//			public AnamnesisCheckProxy getValue(AnamnesisCheckProxy proxy) {
//				return proxy;
//			}
//		}, null);
//		addColumn(new ActionCell<AnamnesisCheckProxy>(OsMaConstant.UP_ICON,
//				new ActionCell.Delegate<AnamnesisCheckProxy>() {
//					public void execute(AnamnesisCheckProxy proxy) {
//						delegate.moveUp(proxy);
//					}
//				}), "", new GetValue<AnamnesisCheckProxy>() {
//			public AnamnesisCheckProxy getValue(AnamnesisCheckProxy proxy) {
//				return proxy;
//			}
//		}, null);
//
	}
//
//	private <C> void addColumn(Cell<C> cell, String headerText,
//			final GetValue<C> getter,
//			FieldUpdater<AnamnesisCheckProxy, C> fieldUpdater) {
//		Column<AnamnesisCheckProxy, C> column = new Column<AnamnesisCheckProxy, C>(
//				cell) {
//			@Override
//			public C getValue(AnamnesisCheckProxy object) {
//				return getter.getValue(object);
//			}
//		};
//		column.setFieldUpdater(fieldUpdater);
//		table.addColumn(column, headerText);
//	}

	/**
	 * Get a cell value from a record.
	 * 
	 * @param <C>
	 *            the cell type
	 */
	private static interface GetValue<C> {
		C getValue(AnamnesisCheckProxy proxy);
	}

	private class QuestionTypeColumn extends
			Column<AnamnesisCheckProxy, SafeHtml> {
		public QuestionTypeColumn() {
			super(new SafeHtmlCell());
		}

		@Override
		public SafeHtml getValue(AnamnesisCheckProxy proxy) {
			OsceConstantsWithLookup constantsWithLookup = GWT
					.create(OsceConstantsWithLookup.class);
			AnamnesisQuestionTypeImages resources = GWT
					.create(AnamnesisQuestionTypeImages.class);
			String html = "";
			switch (proxy.getType()) {
			case QUESTION_TITLE:
				html = "<img src=\"" + resources.title().getURL()
						+ "\" title=\"" + constantsWithLookup.QUESTION_TITLE()
						+ "\" />";
				break;
			case QUESTION_MULT_M:
				html = "<img src=\"" + resources.questionMultM().getURL()
						+ "\" title=\"" + constantsWithLookup.QUESTION_MULT_M()
						+ "\" />";
				break;
			case QUESTION_MULT_S:
				html = "<img src=\"" + resources.questionMultS().getURL()
						+ "\" title=\"" + constantsWithLookup.QUESTION_MULT_S()
						+ "\" />";
				break;
			case QUESTION_YES_NO:
				html = "<img src=\"" + resources.questionYesNo().getURL()
						+ "\" title=\"" + constantsWithLookup.QUESTION_YES_NO()
						+ "\" />";
				break;
			case QUESTION_OPEN:
			default:
				html = "<img src=\"" + resources.questionOpen().getURL()
						+ "\" title=\"" + constantsWithLookup.QUESTION_OPEN()
						+ "\" />";
			}
			return (new SafeHtmlBuilder().appendHtmlConstant(html).toSafeHtml());
		}
	}
	


	private class SimpleTextColumn extends TextColumn<AnamnesisCheckProxy> {
		@Override
		public String getValue(AnamnesisCheckProxy object) {
			String text = object.getText();
			return (text == null) ? "" : text;
		}
	}

	private class ValueColumn extends Column<AnamnesisCheckProxy, SafeHtml> {
		public ValueColumn() {
			super(new SafeHtmlCell());
		}

		@Override
		public SafeHtml getValue(AnamnesisCheckProxy proxy) {
			
			String html = "";
			String[] values;

			if (proxy != null) {
				values = proxy.getValue().split("\\|");
				if (values.length > 1) {
					html = "<ul>";
					for (int i = 0; i < values.length; i++) {
						html += "<li>" + values[i] + "</li>";
					}
					html += "</ul>";
				} else {
					html = values[0];
				}
			}

			return (new SafeHtmlBuilder().appendHtmlConstant(html))
					.toSafeHtml();
		}
	}

//	@Override
//	public CellTable<AnamnesisCheckProxy> getTable() {
//		return table;
//	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public SimplePanel getDetailsPanel() {
		return detailsPanel;
	}

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
//		rangeNum.setItemSelected(selectedIndex, true);
//	}

	@Override
	public void setSearchBoxShown(String selectedValue) {

		searchBox.setText(selectedValue);
	}

	@Override
	public String getSearchBoxShown() {

		return searchBox.getValue();
	}

	@Override
	public void setSearchFocus(boolean focused) {
		searchBox.setFocus(focused);
	}

	@Override
	public ListBox getFilterTitle() {
		return filterTitle;
	}

	@Override
	public QuickSearchBox getSearchBox() {
		return searchBox;
	}
//
//	@Override
//	public ListBox getRangNumBox() {
//		return rangeNum;
//	}


	@Override
	public void loadAnamnesisCheckPanel(List<AnamnesisCheckTitleProxy> anamnesisCheckTitleList , final boolean isOpen) {
		anamnesisCheckPanel.clear();
		for(final AnamnesisCheckTitleProxy anamnesisCheckTitleProxy : anamnesisCheckTitleList){
			AnamnesisCheckTable anamnesisCheckTable = new AnamnesisCheckTable(anamnesisCheckTitleProxy);
			final ListDataProvider<AnamnesisCheckProxy> dataProvider = new ListDataProvider<AnamnesisCheckProxy>();
			final SingleSelectionModel<AnamnesisCheckProxy> selectionModel = new SingleSelectionModel<AnamnesisCheckProxy>();
			anamnesisCheckTable.setDataProvider(dataProvider);
			anamnesisCheckTable.setSelectionModel(selectionModel);
			anamnesisCheckTable.setDelegate(delegate);
			selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler(){

				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					AnamnesisCheckProxy selectedObject = selectionModel.getSelectedObject();
					if (selectedObject != null) {						
						delegate.showDetails(selectedObject);
					}
					
				}
				
			});
			CellTable<AnamnesisCheckProxy> cellTable = anamnesisCheckTable.initTable();
			
			
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setWidth("100%");
			horizontalPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
			horizontalPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);

			Label label = new Label(anamnesisCheckTitleProxy.getText());
			label.setWidth("200px");
			final HorizontalPanel iconImagePanel = new HorizontalPanel();
			iconImagePanel.setWidth("15px");
			Button addBtnButton = new Button("Add detail");
			addBtnButton.setWidth("80px");
			addBtnButton.addDomHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					delegate.newDetailClicked(String.valueOf(anamnesisCheckTitleProxy.getId()));
				}
				
			}, ClickEvent.getType());

			
			final AnamnesisCheckImageResources anamnesisCheckImageResources = GWT.create(AnamnesisCheckImageResources.class);
			AnamnesisTableResources anamnesisTableResources = GWT.create(AnamnesisTableResources.class);
			HorizontalPanel moveUpPanel = new HorizontalPanel();
			moveUpPanel.addStyleName("upIcon");
			moveUpPanel.addDomHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					//TODO move up
					delegate.moveUpTitle(anamnesisCheckTitleProxy);
				}
				
			}, ClickEvent.getType());
			

			HorizontalPanel moveDownPanel = new HorizontalPanel();
			moveDownPanel.addStyleName("downIcon");
			moveDownPanel.addDomHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					event.stopPropagation();
					delegate.moveDownTitle(anamnesisCheckTitleProxy);
				}
				
			}, ClickEvent.getType());
			
			horizontalPanel.add(iconImagePanel);
			horizontalPanel.add(label);
			horizontalPanel.add(addBtnButton);
			horizontalPanel.add(moveUpPanel);
			horizontalPanel.add(moveDownPanel);
			
			DisclosurePanel advancedDisclosure = new DisclosurePanel(horizontalPanel);
			advancedDisclosure.setAnimationEnabled(true);
			advancedDisclosure.ensureDebugId("cwDisclosurePanel");
			
			//Add table to DisclosurePanel
			advancedDisclosure.setContent(cellTable);
			advancedDisclosure.setWidth("100%");
			
			//when open DisclosurePanel,add data to table
			advancedDisclosure.addOpenHandler(new OpenHandler<DisclosurePanel>() {
				
				@Override
				public void onOpen(OpenEvent<DisclosurePanel> event) {

					iconImagePanel.removeStyleName("rightIcon");
					iconImagePanel.addStyleName("downIcon");
					
				}
			});			
			advancedDisclosure.addCloseHandler(new CloseHandler<DisclosurePanel>() {
				
				@Override
				public void onClose(CloseEvent<DisclosurePanel> event) {
					
					iconImagePanel.removeStyleName("downIcon");
					iconImagePanel.addStyleName("rightIcon");
				}
			});
			

				advancedDisclosure.setOpen(true);
				iconImagePanel.removeStyleName("rightIcon");
				iconImagePanel.addStyleName("downIcon");
				delegate.setQuestionTableData(dataProvider,anamnesisCheckTitleProxy);

			anamnesisCheckPanel.add(advancedDisclosure);
		}
		
	}

	@Override
	public VerticalPanel getAnamnesisCheckPanel() {
		return anamnesisCheckPanel;
	}

	

}

