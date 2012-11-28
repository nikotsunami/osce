package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.TraitTypes;
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
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientScarSubViewImpl extends Composite implements StandardizedPatientScarSubView  {

	private static StandardizedPatientScarSubViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientScarSubViewImplUiBinder.class);

	interface StandardizedPatientScarSubViewImplUiBinder extends
	UiBinder<Widget, StandardizedPatientScarSubViewImpl> {
	}
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);

	@UiField (provided = true)
	CellTable<ScarProxy> table;
	
	@UiField (provided = true)
	SimplePager pager;

	protected Set<String> paths = new HashSet<String>();
	
	@UiField
	IconButton scarAddButton;
	
	Delegate delegate;
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	//Issue # 122 : Replace pull down with autocomplete.
	
	@UiField
	public DefaultSuggestBox<ScarProxy, EventHandlingValueHolderItem<ScarProxy>> scarBox;

	/*
	@UiField(provided = true)
    ValueListBox<ScarProxy> scarBox = new ValueListBox<ScarProxy>(new ScarProxyRenderer());
*/
	//Issue # 122 : Replace pull down with autocomplete.
	private boolean addBoxesShown = true;

	// Highlight onViolation
	Map<String, Widget> anemnasisFormMap;
	// E Highlight onViolation
	
	public StandardizedPatientScarSubViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<ScarProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		init();
		scarAddButton.setText(constants.addTrait());
		
		// Highlight onViolation
		anemnasisFormMap=new HashMap<String, Widget>();
		anemnasisFormMap.put("scar", scarBox);
		// E Highlight onViolation
		
	}

	@UiHandler("scarAddButton")
	public void scarAddButtonClicked(ClickEvent event){
		delegate.addScarClicked();
	}


	public void init() {
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
		paths.add("trait_type");
		table.addColumn(new TextColumn<ScarProxy>() {

			Renderer<TraitTypes> renderer = new EnumRenderer<TraitTypes>(); 

			@Override
			public String getValue(ScarProxy object) {
				return renderer.render(object.getTraitType());
			}
		}, constants.traits());
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
		}, constants.location());
		addColumn(new ActionCell<ScarProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<ScarProxy>() {
					public void execute(final ScarProxy scar) {
						/*if(Window.confirm("wirklich löschen?"))
							delegate.deleteScarClicked(scar);*/
						final MessageConfirmationDialogBox dialogBox=new MessageConfirmationDialogBox(constants.warning());
						 dialogBox.showYesNoDialog(constants.reallyDelete());
						 dialogBox.getYesBtn().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									dialogBox.hide();									
									Log.info("yes click");
									
									delegate.deleteScarClicked(scar);
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
	
	public ValueListBox<ScarProxy> getScarBox() {
		//Issue # 122 : Replace pull down with autocomplete.
		//return scarBox;
		return null;
		//Issue # 122 : Replace pull down with autocomplete.
	}
	
	//Issue # 122 : Replace pull down with autocomplete.
	public DefaultSuggestBox<ScarProxy, EventHandlingValueHolderItem<ScarProxy>> getNewScarBox()
	{
		return scarBox;
	}

	//Issue # 122 : Replace pull down with autocomplete.

	@Override
	public CellTable<ScarProxy> getTable() {
		return table;
	}

	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
	
	private void setAddBoxesShown(boolean show) {
		if (addBoxesShown  == show) {
			return;
		}
		
		scarBox.setVisible(show);
		scarAddButton.setVisible(show);
		addBoxesShown = show;
		
	}
	private void showAddBoxes() {
		setAddBoxesShown(true);
	}
	
	private void hideAddBoxes() {
		setAddBoxesShown(false);
	}

	@Override
	public void setScarBoxValues(List<ScarProxy> values) {
		boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {
			hideAddBoxes();
			return;
		}
		
		showAddBoxes();
	//	scarBox.setValue(values.get(0));
	//	scarBox.setAcceptableValues(values);
	

		//Issue # 122 : Replace pull down with autocomplete.
		DefaultSuggestOracle<ScarProxy> suggestOracle1 = (DefaultSuggestOracle<ScarProxy>) scarBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);
		scarBox.setSuggestOracle(suggestOracle1);
		//scarBox.setRenderer(new ScarProxyRenderer());
		scarBox.setRenderer(new AbstractRenderer<ScarProxy>() {

			@Override
			public String render(ScarProxy object) {
				// TODO Auto-generated method stub
				if(object!=null)
				{
				return object.getTraitType() + " " +object.getBodypart();
				}
				else
				{
					return "";
				}
				//return object.get;
			}
		});

		
		//scarBox.setValue(values.get(0));
		//scarBox.setAcceptableValues(values);
		
		//Issue # 122 : Replace pull down with autocomplete.
	}
	// E Highlight onViolation

	@Override
	public Map getAnemnasisFormMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
