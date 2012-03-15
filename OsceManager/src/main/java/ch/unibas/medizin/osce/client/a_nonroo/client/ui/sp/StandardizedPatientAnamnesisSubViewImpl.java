package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.ProxySuggestOracle;
import ch.unibas.medizin.osce.client.style.widgets.cell.GenericAnswerCell;
import ch.unibas.medizin.osce.client.style.widgets.cell.GenericAnswerCell.Alignment;
import ch.unibas.medizin.osce.client.style.widgets.cell.GenericAnswerCell.Answer;
import ch.unibas.medizin.osce.client.style.widgets.cell.IconCell;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
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
	
	@UiField
	CheckBox showAnswered;
	
	@UiField
	CheckBox showUnanswered;

	public StandardizedPatientAnamnesisSubViewImpl() {
		initSuggestBox();
		MyCellTableResources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<AnamnesisChecksValueProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		initWidget(uiBinder.createAndBindUi(this));
		initTable();
		
		showAnswered.setValue(true);
		showAnswered.setText(Messages.SHOW_ANSWERED);
		showUnanswered.setText(Messages.SHOW_UNANSWERED);
	}
	
	private void initSuggestBox() {
		anamnesisQuestionSuggestBox = new SuggestBox(new ProxySuggestOracle<AnamnesisChecksValueProxy>(new AbstractRenderer<AnamnesisChecksValueProxy>() {
			@Override
			public String render(AnamnesisChecksValueProxy proxy) {
				AnamnesisCheckProxy check = proxy.getAnamnesischeck();
				return check.getText();
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

		paths.add("truth");
		paths.add("comment");
		paths.add("anamnesisChecksValue");
		paths.add("anamnesischeck");
		
		table.setRowStyles(new CustomRowStyles());
		table.addColumn(new StatusColumn(), Messages.ANSWERED);
		table.addColumn(new QuestionColumn(), Messages.QUESTION);
		table.addColumn(new AnswerColumn(), Messages.ANSWER);
		table.addColumn(new CommentColumn(), Messages.COMMENT);
		
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
		table.addColumnStyleName(0, "iconCol");
		table.addColumnStyleName(table.getColumnCount()-1, "iconCol");
	}

	private static class CustomRowStyles implements RowStyles<AnamnesisChecksValueProxy> {
		private static MyCellTableResources tableResources = GWT.create(MyCellTableResources.class);
		@Override
		public String getStyleNames(AnamnesisChecksValueProxy proxy, int rowIndex) {
			boolean questionAnswered = !(proxy.getTruth() == null && proxy.getAnamnesisChecksValue() == null);
			boolean rowIsEven = (rowIndex % 2 == 0);
			if (questionAnswered) {
				if (rowIsEven) {
					return tableResources.cellTableStyle().cellTableEvenYesRow();						
				}
				return tableResources.cellTableStyle().cellTableOddYesRow();
			}
				
			if (rowIsEven) {
				return tableResources.cellTableStyle().cellTableEvenNoRow();
			}
			return tableResources.cellTableStyle().cellTableOddNoRow();
		}
	}
	
	private static class StatusColumn extends Column<AnamnesisChecksValueProxy, Integer> {
		private static final String[] ICON_DESCRIPTORS = {"closethick", "check"};
		private static final String[] ICON_TITLES = {Messages.ANSWER_GIVEN, Messages.ANSWER_PENDING};
		
		public StatusColumn() {
			super(new IconCell(ICON_DESCRIPTORS, ICON_TITLES));
		}
		
		@Override
		public Integer getValue(AnamnesisChecksValueProxy proxy) {
			boolean questionAnswered = !(proxy.getTruth() == null && proxy.getAnamnesisChecksValue() == null);
			return (questionAnswered) ? 1 : 0;
		}
	}
	
	private static class QuestionColumn extends TextColumn<AnamnesisChecksValueProxy> {
		@Override
		public String getValue(AnamnesisChecksValueProxy proxy) {
			AnamnesisCheckProxy check = proxy.getAnamnesischeck();
			return (check.getText() == null) ? "" : String.valueOf(check.getText());
		}
	}
	
	private class AnswerFieldUpdater implements FieldUpdater<AnamnesisChecksValueProxy, GenericAnswerCell.Answer> {
		@Override
		public void update(int index, AnamnesisChecksValueProxy proxy, Answer answer) {
			String anamnesisChecksValue = null;
			Boolean truth = null;
			switch (proxy.getAnamnesischeck().getType()) {
			case QuestionYesNo:
				if (Messages.YES.equals(answer.selectedAnswers.get(0))) {
					truth = true;
				} else {
					truth = false;
				}
				break;
			case QuestionMultM:
				StringBuilder anamnesisChecksValueBuilder = new StringBuilder();
				Iterator<String> i = answer.selectedAnswers.iterator();
				
				while (i.hasNext()) {
					anamnesisChecksValueBuilder.append(i.next()).append("|");
				}
				
				if (anamnesisChecksValueBuilder.length() > 0) {
					anamnesisChecksValue = anamnesisChecksValueBuilder.toString();
				}
				break;
			default:
				if (answer.selectedAnswers.get(0) != null && answer.selectedAnswers.get(0).length() > 0) {
					anamnesisChecksValue = answer.selectedAnswers.get(0);
				}
			}
			delegate.saveAnamnesisChecksValueProxyChanges(proxy, anamnesisChecksValue, truth);
		}
	}
	
	private class AnswerColumn extends Column<AnamnesisChecksValueProxy, Answer> {
		public AnswerColumn() {
			super(new GenericAnswerCell(Alignment.HORIZONTAL));
			setFieldUpdater(new AnswerFieldUpdater());
		}
		
		@Override
		public GenericAnswerCell.Answer getValue(AnamnesisChecksValueProxy proxy) {
			boolean questionAnswered = !(proxy.getTruth() == null && proxy.getAnamnesisChecksValue() == null);
			GenericAnswerCell.Answer answer = new GenericAnswerCell.Answer();;
			
			AnamnesisCheckTypes type = proxy.getAnamnesischeck().getType();
			answer.selectedAnswers = new ArrayList<String>();
			if (type == AnamnesisCheckTypes.QuestionYesNo) {
				answer.type = GenericAnswerCell.ElementType.RADIO;
				answer.possibleAnswers = new ArrayList<String>();
				answer.possibleAnswers.add(Messages.NO);
				answer.possibleAnswers.add(Messages.YES);
				if (questionAnswered && proxy.getTruth() == true) {
					answer.selectedAnswers.add(Messages.YES);
				} else if (questionAnswered) {
					answer.selectedAnswers.add(Messages.NO);
				}
			} else if (type == AnamnesisCheckTypes.QuestionMultM) {
				answer.type = GenericAnswerCell.ElementType.CHECKBOX;
				answer.possibleAnswers = Arrays.asList(proxy.getAnamnesischeck().getValue().split("\\|"));
				if (questionAnswered) {
					answer.selectedAnswers = Arrays.asList(proxy.getAnamnesisChecksValue().split("\\|"));
				}
			} else if (type == AnamnesisCheckTypes.QuestionMultS) {
				answer.type = GenericAnswerCell.ElementType.RADIO;
				answer.possibleAnswers = Arrays.asList(proxy.getAnamnesischeck().getValue().split("\\|"));
				if (questionAnswered) {
					answer.selectedAnswers = Arrays.asList(proxy.getAnamnesisChecksValue().split("\\|"));
				}
			} else {
				// It's an open question or title...
				answer.type = GenericAnswerCell.ElementType.TEXT;
				answer.possibleAnswers = null;
				answer.selectedAnswers.add(proxy.getAnamnesisChecksValue());
			}
			return answer;
		}
	}
	
	private static class CommentFieldUpdater implements FieldUpdater<AnamnesisChecksValueProxy, String> {
		@Override
		public void update(int index, AnamnesisChecksValueProxy object, String value) {
			// TODO Auto-generated method stub
			Log.info("comment updated " + value);
		}
	}
	
	private static class CommentColumn extends Column<AnamnesisChecksValueProxy, String> {
		public CommentColumn() {
			super(new TextInputCell());
			setFieldUpdater(new CommentFieldUpdater());
		}
		
		public String getValue(AnamnesisChecksValueProxy proxy) {
			String comment = proxy.getComment();
			return (comment != null) ? comment : "";
		}
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
	
	public boolean areUnansweredQuestionsShown() {
		return showUnanswered.getValue();
	}
	
	public boolean areAnsweredQuestionsShown() {
		return showAnswered.getValue();
	}
}
