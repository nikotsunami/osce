package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.HashSet;
import java.util.Set;

import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy;
import ch.unibas.medizin.osce.client.managed.request.AnamnesisChecksValueProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableNoHilightResources;
import ch.unibas.medizin.osce.client.style.resources.MySimplePagerResources;
import ch.unibas.medizin.osce.client.style.widgets.cell.IconCell;
import ch.unibas.medizin.osce.client.style.widgets.cell.VariableSelectorCell;
import ch.unibas.medizin.osce.client.style.widgets.cell.VariableSelectorCell.Alignment;
import ch.unibas.medizin.osce.client.style.widgets.cell.VariableSelectorCell.Choice;
import ch.unibas.medizin.osce.client.style.widgets.cell.VariableSelectorCell.Choices;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import ch.unibas.medizin.osce.shared.OsMaConstant;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientAnamnesisTableSubViewImpl extends Composite
		implements StandardizedPatientAnamnesisTableSubView {

	private static StandardizedPatientAnamnesisTableSubViewImplUiBinder uiBinder = GWT
			.create(StandardizedPatientAnamnesisTableSubViewImplUiBinder.class);

	interface StandardizedPatientAnamnesisTableSubViewImplUiBinder extends
			UiBinder<Widget, StandardizedPatientAnamnesisTableSubViewImpl> {
	}
	
	@UiField (provided = true)
	CellTable<AnamnesisChecksValueProxy> table;
	
	@UiField (provided = true)
	SimplePager pager;

	private Set<String> paths = new HashSet<String>();
	private Timer changeRequestTimer = new ChangeRequestTimer();
	private Delegate delegate;
	private OsceConstants constants = GWT.create(OsceConstants.class);
	private MyCellTableNoHilightResources tableResources = GWT.create(MyCellTableNoHilightResources.class);

	public StandardizedPatientAnamnesisTableSubViewImpl() {
		SimplePager.Resources pagerResources = GWT.create(MySimplePagerResources.class);
		pager = new SimplePager(SimplePager.TextLocation.RIGHT, pagerResources, true, OsMaConstant.TABLE_JUMP_SIZE, true);
		table = new CellTable<AnamnesisChecksValueProxy>(OsMaConstant.TABLE_PAGE_SIZE, tableResources);
		initWidget(uiBinder.createAndBindUi(this));
		
		initTable();
	}
	
	private void initTable() {
		paths.add("truth");
		paths.add("comment");
		paths.add("anamnesisChecksValue");
		paths.add("anamnesischeck");
		paths.add("anamnesischeck.anamnesisCheckTitle");
		
		table.setRowStyles(new CustomRowStyles());
		table.addColumn(new StatusColumn(), constants.answered());
		table.addColumn(new QuestionColumn(), constants.question());
		table.addColumn(new AnswerColumn(), constants.answer());
		table.addColumn(new CommentColumn(), constants.comment());
		table.addColumnStyleName(0, "iconCol");
	}

	private static class CustomRowStyles implements RowStyles<AnamnesisChecksValueProxy> {
		private static MyCellTableNoHilightResources tableResources = GWT.create(MyCellTableNoHilightResources.class);
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
	
	private class StatusColumn extends Column<AnamnesisChecksValueProxy, Integer> {
		
		public StatusColumn() {
			super(new IconCell(new String[] {"closethick", "check"}, new String[] {constants.answerPending(), constants.answerGiven()}));
		}
		
		@Override
		public Integer getValue(AnamnesisChecksValueProxy proxy) {
			Log.debug("status data received");
			boolean questionAnswered = !(proxy.getTruth() == null && proxy.getAnamnesisChecksValue() == null);
			return (questionAnswered) ? 1 : 0;
		}
	}
	
	private static class QuestionColumn extends TextColumn<AnamnesisChecksValueProxy> {
		@Override
		public String getValue(AnamnesisChecksValueProxy proxy) {
			AnamnesisCheckProxy check = proxy.getAnamnesischeck();
			Log.debug("question data received: " + ((check.getText() == null) ? "null" : check.getText()));
			return (check.getText() == null) ? "" : String.valueOf(check.getText());
		}
	}
	
	private class AnswerFieldUpdater implements FieldUpdater<AnamnesisChecksValueProxy, VariableSelectorCell.Choices> {
		@Override
		public void update(int index, AnamnesisChecksValueProxy proxy, Choices answer) {
			String anamnesisChecksValue = null;
			Boolean truth = null;
			switch (proxy.getAnamnesischeck().getType()) {
			case QUESTION_YES_NO:
				VariableSelectorCell.Choice selectedChoice = answer.getSelectedChoice();
				if (selectedChoice != null && constants.yes().equals(selectedChoice.getOption())) {
					truth = true;
				} else {
					truth = false;
				}
				break;
			case QUESTION_MULT_M:
			case QUESTION_MULT_S:
				StringBuilder anamnesisChecksValueBuilder = new StringBuilder();
				for (Choice choice : answer.getChoices()) {
					if (choice.isChecked()) {
						anamnesisChecksValueBuilder.append("1-");
					} else {
						anamnesisChecksValueBuilder.append("0-");
					}
				}
				anamnesisChecksValueBuilder.replace(anamnesisChecksValueBuilder.length()-1, anamnesisChecksValueBuilder.length(), "");
				anamnesisChecksValue = anamnesisChecksValueBuilder.toString();
				break;
			default:
				anamnesisChecksValue = answer.getCustomChoice();
			}
			saveProxyChanges(proxy,anamnesisChecksValue,truth);
		}
	}
	
	// TODO: rename into something meaningful...
	private AnamnesisChecksValueProxy proxyToBeChanged;
	private String anamnesisChecksValueToBeChanged;
	private Boolean truthToBeChanged;
	
	private class ChangeRequestTimer extends Timer {
		@Override
		public void run() {
			delegate.saveAnamnesisChecksValueProxyChanges(proxyToBeChanged, anamnesisChecksValueToBeChanged, truthToBeChanged);
			proxyToBeChanged = null;
			anamnesisChecksValueToBeChanged = null;
			truthToBeChanged = null;
		}
	}

	private void saveProxyChanges(AnamnesisChecksValueProxy proxy, String anamnesisChecksValue, Boolean truth) {
		changeRequestTimer.cancel();
		if (proxyToBeChanged != null && !proxy.getId().equals(proxyToBeChanged.getId())) {
			delegate.saveAnamnesisChecksValueProxyChanges(proxyToBeChanged, anamnesisChecksValueToBeChanged, truthToBeChanged);
		}

		proxyToBeChanged = proxy;
		anamnesisChecksValueToBeChanged = anamnesisChecksValue;
		truthToBeChanged = truth;
		changeRequestTimer.schedule(1200);
	}
	
	private class AnswerColumn extends Column<AnamnesisChecksValueProxy, Choices> {
		public AnswerColumn() {
			super(new VariableSelectorCell(Alignment.HORIZONTAL));
			setFieldUpdater(new AnswerFieldUpdater());
		}
		
		@Override
		public VariableSelectorCell.Choices getValue(AnamnesisChecksValueProxy proxy) {
			boolean questionAnswered = !(proxy.getTruth() == null && proxy.getAnamnesisChecksValue() == null);
			VariableSelectorCell.Choices answer;
			AnamnesisCheckTypes type = proxy.getAnamnesischeck().getType();
			if (type == AnamnesisCheckTypes.QUESTION_YES_NO) {
				answer = new Choices(VariableSelectorCell.SelectorType.RADIO);
				
				if (questionAnswered && proxy.getTruth() == true) {
					answer.addChoice(constants.yes(), true);
					answer.addChoice(constants.no(), false);
				} else if (questionAnswered) {
					answer.addChoice(constants.yes(), false);
					answer.addChoice(constants.no(), true);
				} else {
					answer.addChoice(constants.yes(), false);
					answer.addChoice(constants.no(), false);
				}
			} else if (type == AnamnesisCheckTypes.QUESTION_MULT_M) {
				answer = new Choices(VariableSelectorCell.SelectorType.CHECKBOX, proxy.getAnamnesischeck().getValue().split("\\|"));
				
				if (questionAnswered) {
					int i = 0;
					for (String selected : proxy.getAnamnesisChecksValue().split("-")) {
						try {
							boolean isSelected = (Integer.parseInt(selected) > 0) ? true : false;
							if (isSelected) {
								answer.setSelected(i);
							}
						} catch (NumberFormatException ex) {}
						i++;
					}
				}
			} else if (type == AnamnesisCheckTypes.QUESTION_MULT_S) {
				answer = new Choices(VariableSelectorCell.SelectorType.RADIO, proxy.getAnamnesischeck().getValue().split("\\|"));
				if (questionAnswered) {
					int i = 0;
					for (String selected : proxy.getAnamnesisChecksValue().split("-")) {
						try {
							boolean isSelected = (Integer.parseInt(selected) > 0) ? true : false;
							if (isSelected) {
								answer.setSelected(i);
							}
						} catch (NumberFormatException ex) {}
						i++;
					}
				}
			} else {
				// It's an open question or title...
				answer = new Choices(proxy.getAnamnesisChecksValue());
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
	public String[] getPaths() {
		return paths.toArray(new String[paths.size()]);
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public CellTable<AnamnesisChecksValueProxy> getTable() {
		return table;
	}
}
