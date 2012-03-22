package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import ch.unibas.medizin.osce.client.a_nonroo.client.OsMaConstant;
import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.QuickSearchBox;
import ch.unibas.medizin.osce.client.style.widgets.cell.VariableSelectorCell;
import ch.unibas.medizin.osce.client.style.widgets.cell.VariableSelectorCell.Alignment;
import ch.unibas.medizin.osce.client.style.widgets.cell.VariableSelectorCell.Choices;
import ch.unibas.medizin.osce.client.style.widgets.cell.IconCell;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAnamnesisSubViewImpl extends Composite implements StandardizedPatientAnamnesisSubView  {

	private static StandardizedPatientScarSubViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientScarSubViewImplUiBinder.class);

	interface StandardizedPatientScarSubViewImplUiBinder extends
	UiBinder<Widget, StandardizedPatientAnamnesisSubViewImpl> {
	}
	
	private Set<String> paths = new HashSet<String>();
	private Delegate delegate;

	@UiField (provided = true)
	CellTable<AnamnesisChecksValueProxy> table;
	
	@UiField (provided = true)
	SimplePager pager;

	@UiField (provided = true)
	QuickSearchBox searchBox;
	
	@UiField
	CheckBox showAnswered;
	
	@UiField
	CheckBox showUnanswered;

	public StandardizedPatientAnamnesisSubViewImpl() {
		MyCellTableResources tableResources = GWT.create(MyCellTableResources.class);
		table = new CellTable<AnamnesisChecksValueProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		
		initSearchBox();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		initTable();
		initCheckBoxes();
	}
	
	private void initSearchBox() {
		searchBox = new QuickSearchBox(new QuickSearchBox.Delegate() {
			@Override
			public void performAction() {
				delegate.performAnamnesisSearch();
			}
		});
	}
	
	private void initCheckBoxes() {
		showAnswered.setValue(true);
		showAnswered.setText(Messages.SHOW_ANSWERED);
		showUnanswered.setText(Messages.SHOW_UNANSWERED);
		
		showAnswered.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				delegate.performAnamnesisSearch();
			}
		});
		showUnanswered.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				delegate.performAnamnesisSearch();
			}
		});
		
	}
	
	private void initTable() {
		paths.add("truth");
		paths.add("comment");
		paths.add("anamnesisChecksValue");
		paths.add("anamnesischeck");
		
		table.setRowStyles(new CustomRowStyles());
		table.addColumn(new StatusColumn(), Messages.ANSWERED);
		table.addColumn(new QuestionColumn(), Messages.QUESTION);
		table.addColumn(new AnswerColumn(), Messages.ANSWER);
		table.addColumn(new CommentColumn(), Messages.COMMENT);
		table.addColumnStyleName(0, "iconCol");
	}

	private static class CustomRowStyles implements RowStyles<AnamnesisChecksValueProxy> {
		private static MyCellTableResources tableResources = GWT.create(MyCellTableResources.class);
		@Override
		public String getStyleNames(AnamnesisChecksValueProxy proxy, int rowIndex) {
			// TODO: find out why it happens that upon *PAGING BACK* in the table the proxy here may be null (and only here, not in columns)
			if (proxy==null) {
				Log.warn("(null proxy in CustomRowStyles)");
				return null;
			}
			
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
		private static final String[] ICON_TITLES = {Messages.ANSWER_PENDING, Messages.ANSWER_GIVEN};
		
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
	
	private class AnswerFieldUpdater implements FieldUpdater<AnamnesisChecksValueProxy, VariableSelectorCell.Choices> {
		@Override
		public void update(int index, AnamnesisChecksValueProxy proxy, Choices answer) {
			String anamnesisChecksValue = null;
			Boolean truth = null;
			switch (proxy.getAnamnesischeck().getType()) {
			case QuestionYesNo:
				if (Messages.YES.equals(answer.selectedChoices.get(0))) {
					truth = true;
				} else {
					truth = false;
				}
				break;
			case QuestionMultM:
				StringBuilder anamnesisChecksValueBuilder = new StringBuilder();
				Iterator<String> i = answer.selectedChoices.iterator();
				
				while (i.hasNext()) {
					anamnesisChecksValueBuilder.append(i.next()).append("|");
				}
				
				if (anamnesisChecksValueBuilder.length() > 0) {
					anamnesisChecksValue = anamnesisChecksValueBuilder.toString();
				}
				break;
			default:
				if (answer.selectedChoices.get(0) != null && answer.selectedChoices.get(0).length() > 0) {
					anamnesisChecksValue = answer.selectedChoices.get(0);
				}
			}
			delegate.saveAnamnesisChecksValueProxyChanges(proxy, anamnesisChecksValue, truth);
		}
	}
	
	private class AnswerColumn extends Column<AnamnesisChecksValueProxy, Choices> {
		public AnswerColumn() {
			super(new VariableSelectorCell(Alignment.HORIZONTAL));
			setFieldUpdater(new AnswerFieldUpdater());
		}
		
		@Override
		public VariableSelectorCell.Choices getValue(AnamnesisChecksValueProxy proxy) {
			boolean questionAnswered = !(proxy.getTruth() == null && proxy.getAnamnesisChecksValue() == null);
			VariableSelectorCell.Choices answer = new VariableSelectorCell.Choices();;
			
			AnamnesisCheckTypes type = proxy.getAnamnesischeck().getType();
			answer.selectedChoices = new ArrayList<String>();
			if (type == AnamnesisCheckTypes.QuestionYesNo) {
				answer.type = VariableSelectorCell.SelectorType.RADIO;
				answer.availableChoices = new ArrayList<String>();
				answer.availableChoices.add(Messages.NO);
				answer.availableChoices.add(Messages.YES);
				if (questionAnswered && proxy.getTruth() == true) {
					answer.selectedChoices.add(Messages.YES);
				} else if (questionAnswered) {
					answer.selectedChoices.add(Messages.NO);
				}
			} else if (type == AnamnesisCheckTypes.QuestionMultM) {
				answer.type = VariableSelectorCell.SelectorType.CHECKBOX;
				answer.availableChoices = new LinkedList<String>(Arrays.asList(proxy.getAnamnesischeck().getValue().split("\\|")));
				if (questionAnswered) {
					answer.selectedChoices = new LinkedList<String>(Arrays.asList(proxy.getAnamnesisChecksValue().split("\\|")));
				}
			} else if (type == AnamnesisCheckTypes.QuestionMultS) {
				answer.type = VariableSelectorCell.SelectorType.RADIO;
				answer.availableChoices = new LinkedList<String>(Arrays.asList(proxy.getAnamnesischeck().getValue().split("\\|")));
				if (questionAnswered) {
					answer.selectedChoices = new LinkedList<String>(Arrays.asList(proxy.getAnamnesisChecksValue().split("\\|")));
				}
			} else {
				// It's an open question or title...
				answer.type = VariableSelectorCell.SelectorType.TEXT;
				answer.availableChoices = null;
				answer.selectedChoices.add(proxy.getAnamnesisChecksValue());
			}
			return answer;
		}
	}
	
	private class CommentFieldUpdater implements FieldUpdater<AnamnesisChecksValueProxy, String> {
		@Override
		public void update(int index, AnamnesisChecksValueProxy proxy, String value) {
			Log.info("comment updated " + value);
			delegate.saveAnamnesisChecksValueProxyChanges(proxy, value);
		}
	}
	
	private class CommentColumn extends Column<AnamnesisChecksValueProxy, String> {
		public CommentColumn() {
			super(new TextInputCell());
			setFieldUpdater(new CommentFieldUpdater());
		}
		
		public String getValue(AnamnesisChecksValueProxy proxy) {
			String comment = proxy.getComment();
			return (comment != null) ? comment : "";
		}
	}

	@Override
	public CellTable<AnamnesisChecksValueProxy> getTable() {
		return table;
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
	
	public String getSearchString() {
		return searchBox.getText();
	}
}
