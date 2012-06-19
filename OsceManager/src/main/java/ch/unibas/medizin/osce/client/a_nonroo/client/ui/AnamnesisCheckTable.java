package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.AnamnesisCheckView.Delegate;
import ch.unibas.medizin.osce.client.i18n.OsceConstantsWithLookup;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckTitleProxy;
import ch.unibas.medizin.osce.client.style.resources.AnamnesisQuestionTypeImages;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class AnamnesisCheckTable {

	CellTable<AnamnesisCheckProxy> cellTable = new CellTable<AnamnesisCheckProxy>();
	ListDataProvider<AnamnesisCheckProxy> dataProvider = null;

	List<AnamnesisCheckProxy> anamnesisCheckProxyList = new ArrayList<AnamnesisCheckProxy>();
	AnamnesisCheckTitleProxy anamnesisCheckTitleProxy = null;
	SelectionModel<AnamnesisCheckProxy> selectionModel = null;
	private Delegate delegate;

	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	public AnamnesisCheckTable(AnamnesisCheckTitleProxy anamnesisCheckTitleProxy) {
		this.anamnesisCheckTitleProxy = anamnesisCheckTitleProxy;
	}

	public CellTable<AnamnesisCheckProxy> initTable() {
//		cellTable.setWidth("100%");
		CellTable.Resources tableResources = GWT
		.create(MyCellTableResources.class);
		cellTable = new CellTable<AnamnesisCheckProxy>(15, tableResources);
		cellTable.setStyleName("standardTable");
		
		cellTable.setSelectionModel(selectionModel);

		// Add colum
		Column<AnamnesisCheckProxy, String> checkOrderColumn = new Column<AnamnesisCheckProxy, String>(new TextInputCell()) {

			@Override
			public String getValue(AnamnesisCheckProxy object) {
				return String.valueOf(object.getSort_order());
			}

		};
		checkOrderColumn.setFieldUpdater(new FieldUpdater<AnamnesisCheckProxy, String>() {

			@Override
			public void update(int index, AnamnesisCheckProxy object, String value) {
				try {
					if (value != null && !value.equals("")) {
						delegate.orderEdited(object, value);
					} else {
						delegate.orderEdited(object, null);
					}
				} catch (Exception e) {
					GWT.log(value + e);
				}

			}
		});
		cellTable.addColumn(checkOrderColumn);
		
		cellTable.addColumn(new QuestionTypeColumn());
		cellTable.addColumn(new SimpleTextColumn());
		cellTable.addColumn(new ValueColumn());
		
		addColumn(new ActionCell<AnamnesisCheckProxy>(OsMaConstant.DOWN_ICON, new ActionCell.Delegate<AnamnesisCheckProxy>() {
			public void execute(AnamnesisCheckProxy proxy) {
				delegate.moveDown(proxy);
			}
		}), "", new GetValue<AnamnesisCheckProxy>() {
			public AnamnesisCheckProxy getValue(AnamnesisCheckProxy proxy) {
				return proxy;
			}
		}, null);
		addColumn(new ActionCell<AnamnesisCheckProxy>(OsMaConstant.UP_ICON, new ActionCell.Delegate<AnamnesisCheckProxy>() {
			public void execute(AnamnesisCheckProxy proxy) {
				delegate.moveUp(proxy);
			}
		}), "", new GetValue<AnamnesisCheckProxy>() {
			public AnamnesisCheckProxy getValue(AnamnesisCheckProxy proxy) {
				return proxy;
			}
		}, null);
		
		
		dataProvider.addDataDisplay(cellTable);
		dataProvider.setList(anamnesisCheckProxyList);
		
		
		return cellTable;

	}

	private <C> void addColumn(Cell<C> cell, String headerText, final GetValue<C> getter, FieldUpdater<AnamnesisCheckProxy, C> fieldUpdater) {
		Column<AnamnesisCheckProxy, C> column = new Column<AnamnesisCheckProxy, C>(cell) {
			@Override
			public C getValue(AnamnesisCheckProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		cellTable.addColumn(column);
	}

	private static interface GetValue<C> {
		C getValue(AnamnesisCheckProxy proxy);
	}
	

	private class QuestionTypeColumn extends Column<AnamnesisCheckProxy, SafeHtml> {
		public QuestionTypeColumn() {
			super(new SafeHtmlCell());
		}

		@Override
		public SafeHtml getValue(AnamnesisCheckProxy proxy) {
			OsceConstantsWithLookup constantsWithLookup = GWT.create(OsceConstantsWithLookup.class);
			AnamnesisQuestionTypeImages resources = GWT.create(AnamnesisQuestionTypeImages.class);
			String html = "";
			switch (proxy.getType()) {
			case QUESTION_TITLE:
				html = "<img src=\"" + resources.title().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_TITLE() + "\" />";
				break;
			case QUESTION_MULT_M:
				html = "<img src=\"" + resources.questionMultM().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_MULT_M() + "\" />";
				break;
			case QUESTION_MULT_S:
				html = "<img src=\"" + resources.questionMultS().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_MULT_S() + "\" />";
				break;
			case QUESTION_YES_NO:
				html = "<img src=\"" + resources.questionYesNo().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_YES_NO() + "\" />";
				break;
			case QUESTION_OPEN:
			default:
				html = "<img src=\"" + resources.questionOpen().getURL() + "\" title=\"" + constantsWithLookup.QUESTION_OPEN() + "\" />";
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

			return (new SafeHtmlBuilder().appendHtmlConstant(html)).toSafeHtml();
		}
	}

	public void setDataProvider(ListDataProvider<AnamnesisCheckProxy> dataProvider) {
		this.dataProvider = dataProvider;
	}

	public void setSelectionModel(SelectionModel<AnamnesisCheckProxy> selectionModel) {
		this.selectionModel = selectionModel;
	}

}
