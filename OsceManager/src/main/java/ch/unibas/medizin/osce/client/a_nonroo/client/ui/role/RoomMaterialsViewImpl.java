package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeEvent;
import ch.unibas.medizin.osce.client.a_nonroo.client.util.RecordChangeHandler;

import ch.unibas.medizin.osce.client.managed.request.MaterialListProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.shared.MaterialType;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.PriceType;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoomMaterialsViewImpl extends Composite implements
		RoomMaterialsView, RecordChangeHandler {

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

	@UiField
	HTMLPanel westPanel;
	
	@UiField
	ScrollPanel scrollPanel;
	
	int widthSize=1225,decreaseSize=0;
	Timer timer;
	
	
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

			Renderer<MaterialType> renderer = new EnumRenderer<MaterialType>();

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer.render(materialListProxy.getType());

			}
		}, constants.roomMaterialType());

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

			Renderer<PriceType> renderer = new EnumRenderer<PriceType>();

			@Override
			public String getValue(MaterialListProxy materialListProxy) {
				return renderer.render(materialListProxy.getPriceType());

			}
		}, constants.roomMaterialPriceType());

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
					public void execute(final MaterialListProxy materialListProxy) {
						// Window.alert("You clicked " +
						// institution.getInstitutionName());
						/*if (Window.confirm("wirklich löschen?"))
							delegate.deleteClicked(materialListProxy);*/
						// Issue Role
						 final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.success());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();									
									Log.info("yes click");	
									delegate.deleteClicked(materialListProxy);
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
				}), "", new GetValue<MaterialListProxy>() {
			public MaterialListProxy getValue(
					MaterialListProxy materialListProxy) {
				return materialListProxy;
			}
		}, null);
		table.addColumnStyleName(4, "iconCol");
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
