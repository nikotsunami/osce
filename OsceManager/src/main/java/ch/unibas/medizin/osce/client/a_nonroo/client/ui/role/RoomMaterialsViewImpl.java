package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.a_nonroo.client.SearchCriteria;
import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoomMaterialsViewImpl extends Composite implements
		RoomMaterialsView {

	private static RoomMaterialsViewImplUiBinder uiBinder = GWT
			.create(RoomMaterialsViewImplUiBinder.class);

	interface RoomMaterialsViewImplUiBinder extends
			UiBinder<Widget, RoomMaterialsViewImpl> {
	}

	// private final OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;
	@UiField
	public SplitLayoutPanel splitLayoutPanel;
	@UiField
	public SimplePanel detailsPanel;

	@UiField(provided = true)
	public QuickSearchBox searchBox;
	// @UiField
	// public IconButton filterButton;

	@UiField(provided = true)
	CellTable<MaterialListProxy> table;

	protected ArrayList<String> paths = new ArrayList<String>();

	@UiField
	IconButton AddButton;

	@UiField(provided = true)
	SimplePager Pager;

	@UiHandler("AddButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.editClicked();

	}

	private Presenter presenter;
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	public RoomMaterialsViewImpl() {
		CellTable.Resources tableResources = GWT
				.create(MyCellTableResources.class);
		table = new CellTable<MaterialListProxy>(OsMaConstant.TABLE_PAGE_SIZE,
				tableResources);

		SimplePager.Resources pagerResources = GWT
				.create(MySimplePagerResources.class);
		Pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources,
				true, OsMaConstant.TABLE_JUMP_SIZE, true);

		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performSearch(searchBox.getValue());
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0),
				OsMaConstant.SPLIT_PANEL_MINWIDTH);

		AddButton.setText(constants.addMaterial());
	}

	// @UiHandler("filterButton")
	// public void filterButtonHover(MouseOverEvent event) {
	// delegate.performSearch(searchBox.getValue());
	// }

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {

		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style",
				"position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");

		// @SPEC

		paths.add("name");
		table.addColumn(new TextColumn<MaterialListProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer.render((materialListProxy.getName() == null) ? ""
						: materialListProxy.getName());

			}
		}, constants.roomMaterialName());

		paths.add("type");
		table.addColumn(new TextColumn<MaterialListProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<String> renderer = new AbstractRenderer<String>() {

				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer
						.render((materialListProxy.getType().name() == null) ? ""
								: materialListProxy.getType().name());

			}
		}, constants.type());

		paths.add("price");
		table.addColumn(new TextColumn<MaterialListProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<String> renderer = new AbstractRenderer<String>() {

				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer.render((materialListProxy.getPrice() == null) ? ""
						: materialListProxy.getPrice().toString());

			}
		}, constants.roomMaterialPrice());

		paths.add("priceType");
		table.addColumn(new TextColumn<MaterialListProxy>() {
			{
				this.setSortable(true);
			}

			Renderer<String> renderer = new AbstractRenderer<String>() {

				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer
						.render((materialListProxy.getPriceType().name() == null) ? ""
								: materialListProxy.getPriceType().name());

			}
		}, constants.roomMaterialPriceType());

		table.addColumnStyleName(2, "iconCol");

		// // Edit Button
		// addColumn(new ActionCell<MaterialListProxy>("Edit",
		// new ActionCell.Delegate<MaterialListProxy>() {
		// public void execute(MaterialListProxy specialization) {
		// // Window.alert("You clicked " +
		// // institution.getInstitutionName());
		// if (Window.confirm("wirklich löschen?"))
		// delegate.
		// deleteClicked(specialization);
		// }
		// }), "", new GetValue<MaterialListProxy>() {
		// public MaterialListProxy getValue(MaterialListProxy specialization) {
		// return specialization;
		// }
		// }, null);
		// table.addColumnStyleName(1, "iconCol");

		// Delete button
		addColumn(new ActionCell<MaterialListProxy>(OsMaConstant.DELETE_ICON,
				new ActionCell.Delegate<MaterialListProxy>() {
					public void execute(MaterialListProxy materialListProxy) {
						// Window.alert("You clicked " +
						// institution.getInstitutionName());
						if (Window.confirm("wirklich löschen?"))
							delegate.deleteClicked(materialListProxy);
					}
				}), "", new GetValue<MaterialListProxy>() {
			public MaterialListProxy getValue(
					MaterialListProxy materialListProxy) {
				return materialListProxy;
			}
		}, null);
		table.addColumnStyleName(1, "iconCol");
	}

	private <C> void addColumn(Cell<C> cell, String headerText,
			final GetValue<C> getter,
			FieldUpdater<MaterialListProxy, C> fieldUpdater) {
		Column<MaterialListProxy, C> column = new Column<MaterialListProxy, C>(
				cell) {
			@Override
			public C getValue(MaterialListProxy object) {
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
	public CellTable<MaterialListProxy> getTable() {
		return table;
	}

	private static interface GetValue<C> {
		C getValue(MaterialListProxy contact);
	}

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

	public SearchCriteria getCriteria() {
		return new SearchCriteria();

	}

	@Override
	public String getQuery() {
		return searchBox.getValue();
	}

	@Override
	public void updateSearch() {
		String q = searchBox.getValue();
		delegate.performSearch(q);
	}

	// @UiHandler("showSubview")
	// public void showSubviewClicked(ClickEvent event) {
	// delegate.showSubviewClicked();
	// }
}
