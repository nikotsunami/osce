package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedPatientProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle.ProxySuggestion;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.Gender;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAnamnesisSubViewImpl extends Composite implements StandardizedPatientAnamnesisSubView  {

	private static StandardizedPatientScarSubViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientScarSubViewImplUiBinder.class);

	interface StandardizedPatientScarSubViewImplUiBinder extends
	UiBinder<Widget, StandardizedPatientAnamnesisSubViewImpl> {
	}
	
	private Set<String> paths = new HashSet<String>();
	private Delegate delegate;
	private List<AbstractEditableCell<?, ?>> editableCells;

	@UiField (provided = true)
	CellTable<AnamnesisChecksValueProxy> table;
	
	@UiField (provided = true)
	SimplePager pager;

	@UiField (provided = true)
	SuggestBox anamnesisQuestionSuggestBox;

	public StandardizedPatientAnamnesisSubViewImpl() {
		initSuggestBox();
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<AnamnesisChecksValueProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		initTable();
	}
	
	private void initSuggestBox() {
		anamnesisQuestionSuggestBox = new SuggestBox(new ProxySuggestOracle<AnamnesisCheckProxy>(new AbstractRenderer<AnamnesisCheckProxy>() {
			@Override
			public String render(AnamnesisCheckProxy object) {
				return object.getText();
			}
		}, ",;:. \t?!_-/\\"));
		
		anamnesisQuestionSuggestBox.setText(Messages.ENTER_QUESTION);
		anamnesisQuestionSuggestBox.getTextBox().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				if (anamnesisQuestionSuggestBox.getText().equals(Messages.ENTER_QUESTION)) {
					anamnesisQuestionSuggestBox.setText("");	
				}
			}
		});
		anamnesisQuestionSuggestBox.getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (anamnesisQuestionSuggestBox.getText().equals("")) {
					anamnesisQuestionSuggestBox.setText(Messages.ENTER_QUESTION);
				}
			}
		});
		anamnesisQuestionSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion> () {
			@SuppressWarnings("unchecked")
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				// TODO Auto-generated method stub
				ProxySuggestOracle<AnamnesisChecksValueProxy>.ProxySuggestion suggestion = 
						(ProxySuggestOracle<AnamnesisChecksValueProxy>.ProxySuggestion) event.getSelectedItem(); 
				AnamnesisChecksValueProxy proxy = suggestion.getProxy();
				delegate.searchAnamnesisQuestion(proxy);
			}
		});
		anamnesisQuestionSuggestBox.getTextBox().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				// TODO Auto-generated method stub
				String query = anamnesisQuestionSuggestBox.getText().toLowerCase().trim();
				delegate.searchAnamnesisQuestion(query);
			}
		});
	}

	private void initTable() {
		editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
		
		paths.add("anamnesisCheck.text");
		table.addColumn(new TextColumn<AnamnesisChecksValueProxy>() {
			Renderer<String> renderer = new AbstractRenderer<String>() {
				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(AnamnesisChecksValueProxy proxy) {
				return renderer.render(proxy.getAnamnesischeck().getText());
			}
		}, Messages.TEXT);
		paths.add("anamnesisCheck.type");
		paths.add("anamnesisCheck.value");
		paths.add("anamnesisChecksValue");
		
		table.addColumn(new Column<AnamnesisChecksValueProxy, SafeHtml>(new SafeHtmlCell()){
			@Override
			public SafeHtml getValue(AnamnesisChecksValueProxy proxy) {
				AnamnesisCheckTypes type = proxy.getAnamnesischeck().getType();
				List<String> possibleValues = Arrays.asList(proxy.getAnamnesischeck().getValue().split("\\|"));
				String value = proxy.getAnamnesisChecksValue();
				String html = "";
				
				switch (type) {
				case QuestionMultM:
					// TODO IST DAS NICHT EIN GRUUUSIGER HACK?
					break;

				default:
					break;
				}
				
				return null;
			}
		});
		
//		table.addColumn(new TextColumn<AnamnesisChecksValueProxy>() {
//
//			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
//
//				public String render(java.lang.String obj) {
//					return obj == null ? "" : String.valueOf(obj);
//				}
//			};
//
//			@Override
//			public String getValue(AnamnesisChecksValueProxy proxy) {
//				proxy.getAnamnesischeck().
//				return "";
//			}
//		}, Messages.LOCATION);
		addColumn(new ActionCell<AnamnesisChecksValueProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<AnamnesisChecksValueProxy>() {
					public void execute(AnamnesisChecksValueProxy scar) {
						//Window.alert("You clicked " + institution.getInstitutionName());
						if(Window.confirm("wirklich löschen?")) {
//							delegate.deleteAnamnesisQuestionClicked(scar);
						}
					}
				}), "", new GetValue<AnamnesisChecksValueProxy>() {
			public AnamnesisChecksValueProxy getValue(AnamnesisChecksValueProxy scar) {
				// TODO implement
				return null;
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
			final GetValue<C> getter, FieldUpdater<AnamnesisChecksValueProxy, C> fieldUpdater) {
		Column<AnamnesisChecksValueProxy, C> column = new Column<AnamnesisChecksValueProxy, C>(cell) {
			@Override
			public C getValue(AnamnesisChecksValueProxy object) {
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
		C getValue(AnamnesisChecksValueProxy contact);
	}

	@Override
	public CellTable<AnamnesisChecksValueProxy> getTable() {
		return table;
	}
	
	@Override
	public SuggestBox getAnamnesisQuestionSuggestBox() {
		return anamnesisQuestionSuggestBox;
	}

	@Override
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}
}
