package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.LangSkillLevel;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientLangSkillSubViewImpl extends Composite implements StandardizedPatientLangSkillSubView {

	private static StandardizedPatientLangSkillSubViewUiBinder uiBinder = GWT
			.create(StandardizedPatientLangSkillSubViewUiBinder.class);

	interface StandardizedPatientLangSkillSubViewUiBinder extends
			UiBinder<Widget, StandardizedPatientLangSkillSubViewImpl> {
	}

	// CellTable (for Langskills)
	@UiField (provided = true)
	public CellTable<LangSkillProxy>langTable;
	@UiField (provided = true)
	public SimplePager pager;
	private List<AbstractEditableCell<?, ?>> editableCells;
	protected Set<String> paths = new HashSet<String>();

	@UiField
	IconButton langSkillAddButton;
	
	private Delegate delegate;
	
	@UiField(provided = true)
    ValueListBox<SpokenLanguageProxy> languageBox = new ValueListBox<SpokenLanguageProxy>(new AbstractRenderer<SpokenLanguageProxy>() {
        public String render(SpokenLanguageProxy obj) {
            return obj == null ? "" : String.valueOf(obj.getLanguageName());
        }
    });
	
	@UiField(provided = true)
    ValueListBox<LangSkillLevel> langSkillBox = new ValueListBox<LangSkillLevel>(new AbstractRenderer<LangSkillLevel>() {
        public String render(LangSkillLevel obj) {
            return obj == null ? "" : String.valueOf(obj.toString());
        }
    });
	
	public StandardizedPatientLangSkillSubViewImpl() {
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		langTable = new CellTable<LangSkillProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		initTable();
		langSkillAddButton.setText(Messages.ADD_LANGSKILL);
		langSkillBox.setAcceptableValues(Arrays.asList(LangSkillLevel.values()));
	}
	
	private void initTable() {
		langTable.addColumn(new TextColumn<LangSkillProxy>() {
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};
			
			@Override
			public String getValue(LangSkillProxy object) {
				return renderer.render(object.getSpokenlanguage().getLanguageName().toString());
			}
			
		}, Messages.LANGUAGES);

		langTable.addColumn(new TextColumn<LangSkillProxy>() {
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() {
				public String render(String obj) {
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(LangSkillProxy object) {
				return renderer.render(object.getSkill().toString());
			}
		}, Messages.LANGUAGE_SKILLS);
		
		addColumn(new ActionCell<LangSkillProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<LangSkillProxy>() {
					public void execute(LangSkillProxy langSkill) {
						if(Window.confirm(Messages.REALLY_DELETE))
							delegate.deleteLangSkillClicked(langSkill);
					}
				}), "", new GetValue<LangSkillProxy>() {
					public LangSkillProxy getValue(LangSkillProxy skill) {
						return skill;
					}
		}, null);
		langTable.addColumnStyleName(2, "iconCol");
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
			final GetValue<C> getter, FieldUpdater<LangSkillProxy, C> fieldUpdater) {
		Column<LangSkillProxy, C> column = new Column<LangSkillProxy, C>(cell) {
			@Override
			public C getValue(LangSkillProxy object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		langTable.addColumn(column, headerText);
	}
	
	private static interface GetValue<C> {
		C getValue(LangSkillProxy contact);
	}
	
	@UiHandler("langSkillAddButton")
	public void langSkillAddButtonClicked(ClickEvent e) {
		// TODO send relevant data...
//		delegate.addLangSkillClicked();
	}


	@Override
	public CellTable<LangSkillProxy> getLangSkillTable() {
		return langTable;
	}

	@Override
	public ValueListBox<SpokenLanguageProxy> getLanguageBox() {
		return languageBox;
	}

	@Override
	public ValueListBox<LangSkillLevel> getLangSkillBox() {
		return langSkillBox;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void setLanguagePickerValues(List<SpokenLanguageProxy> values) {
		languageBox.setAcceptableValues(values);
	}
}
