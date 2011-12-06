/**
 * 
 */
package ch.unibas.medizin.osce.client.a_nonroo.client.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.interfaces.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.interfaces.MySimplePagerResources;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.TraitTypes;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author dk
 *
 */
public class ScarViewImpl extends Composite implements ScarView {

	private static ScarViewUiBinder uiBinder = GWT
			.create(ScarViewUiBinder.class);

	interface ScarViewUiBinder extends UiBinder<Widget, ScarViewImpl> {
	}

	private Delegate delegate;

	@UiField
	SplitLayoutPanel splitLayoutPanel;

	@UiField
	TextBox searchBox;

	@UiField
	TextBox newBodypart;
	
	@UiField(provided = true)
    ValueListBox<TraitTypes> traitTypeBox = new ValueListBox<TraitTypes>(new AbstractRenderer<TraitTypes>() {

        public String render(TraitTypes obj) {
            return obj == null ? "" : String.valueOf(obj.toString());
        }
    });

	@UiField
	Button newButton;
	
	@UiField (provided = true)
	SimplePager pager;

	@UiField (provided = true)
	CellTable<ScarProxy> table;

	protected Set<String> paths = new HashSet<String>();

	private Presenter presenter;

	@UiHandler ("newButton")
	public void newButtonClicked(ClickEvent event) {
		delegate.newClicked(traitTypeBox.getValue(), newBodypart.getValue());
		newBodypart.setValue("");
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
	public ScarViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<ScarProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		traitTypeBox.setAcceptableValues(Arrays.asList(TraitTypes.values()));
		init();
		splitLayoutPanel.setWidgetMinSize(splitLayoutPanel.getWidget(0), OsMaConstant.SPLIT_PANEL_MINWIDTH);
		newButton.setText(Messages.ADD_TRAIT);
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	public void init() {
		searchBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent arg0) {
				searchBox.setValue("");
			}
		});
		searchBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent arg0) {
				if(searchBox.getValue().isEmpty()) {
					searchBox.setValue(Messages.SEARCHFIELD);
				}
			}
		});
		searchBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				String q = searchBox.getValue();
				delegate.performSearch(q);
			}
		});
		newBodypart.addKeyDownHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent event) {
		        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		        	newButtonClicked(null);
		    }
		});

		
		// bugfix to avoid hiding of all panels (maybe there is a better solution...?!)
		DOM.setElementAttribute(splitLayoutPanel.getElement(), "style", "position: absolute; left: 0px; top: 0px; right: 5px; bottom: 0px;");
		
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
		paths.add("trait_type");
		table.addColumn(new TextColumn<ScarProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(ScarProxy object) {
				return renderer.render(object.getTraitType().toString());
			}
		}, "Typ");
		paths.add("bodypart");
		table.addColumn(new TextColumn<ScarProxy>() {

			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {

				public String render(java.lang.String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(ScarProxy object) {
				return renderer.render(object.getBodypart());
			}
		}, "Ort");
		addColumn(new ActionCell<ScarProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<ScarProxy>() {
					public void execute(ScarProxy scar) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						if(Window.confirm("wirklich löschen?"))
							delegate.deleteClicked(scar);
					}
				}), "", new GetValue<ScarProxy>() {
			public ScarProxy getValue(ScarProxy scar) {
				return scar;
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
			final GetValue<C> getter, FieldUpdater<ScarProxy, C> fieldUpdater) {
		Column<ScarProxy, C> column = new Column<ScarProxy, C>(cell) {
			@Override
			public C getValue(ScarProxy object) {
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
		C getValue(ScarProxy contact);
	}

	private List<AbstractEditableCell<?, ?>> editableCells;

	@Override
	public CellTable<ScarProxy> getTable() {
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
}
