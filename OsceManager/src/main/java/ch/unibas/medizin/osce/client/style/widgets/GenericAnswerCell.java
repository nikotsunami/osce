package ch.unibas.medizin.osce.client.style.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GenericAnswerCell extends AbstractCell<GenericAnswerCell.Answer<?>> {
	
	public class Answer<T> {
		T value;
		T[] possibleValues;
		boolean multipleChoicesAllowed;
	}
	
	public interface Delegate {
		public void answerChangeEvent(Object selectedAnswer);
	}

	private ValueListBox<String> _valueListBox;
	private HorizontalPanel _horizontalPanel;
	private VerticalPanel _verticalPanel;
	
	private boolean _useValueListBox = false;
	private boolean _useHorizontalPanel = false;
	
	public GenericAnswerCell(boolean useValueListBoxInsteadOfRadios) {
		this(useValueListBoxInsteadOfRadios, false);
	}
	
	public GenericAnswerCell(boolean useValueListBoxInsteadOfRadios, boolean useHorizontalPanel) {
		_useValueListBox = useValueListBoxInsteadOfRadios;
		_useHorizontalPanel = useHorizontalPanel;
	}

	@Override
	public void render(Cell.Context context, GenericAnswerCell.Answer<?> answer, SafeHtmlBuilder sb) {
		if (answer == null) {
			return;
		}
		
//		if (possibleValues == null) 
	}

}
