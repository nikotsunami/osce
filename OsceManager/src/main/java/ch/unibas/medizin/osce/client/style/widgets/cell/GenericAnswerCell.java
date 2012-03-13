package ch.unibas.medizin.osce.client.style.widgets.cell;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.servlet.tags.form.InputTag;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class GenericAnswerCell extends AbstractInputCell<GenericAnswerCell.Answer, String> {
	private static Templates templates = GWT.create(Templates.class);
	private HashMap<Object, Answer> _answersByRowKeys = new HashMap<Object, Answer>();
	private final Alignment _alignment;
	
	public enum ElementType {
		TEXT, RADIO, CHECKBOX, SELECT
	}

	public enum Alignment {
		VERTICAL, HORIZONTAL
	}
	
	public static interface Templates extends SafeHtmlTemplates {
		//TODO : required?
		
		@SafeHtmlTemplates.Template("<select class=\"gwt-ListBox\" name=\"{0}\">")
		SafeHtml selectTemplate(String key);
		@SafeHtmlTemplates.Template("<option value=\"{0}\">{0}</option>")
		SafeHtml optionTemplate(String value);
		@SafeHtmlTemplates.Template("<option value=\"{0}\" selected=\"selected\">{0}</option>")
		SafeHtml optionTemplateSelected(String value);
		@SafeHtmlTemplates.Template("<span class=\"gwt-RadioButton\">" +
					"<input type=\"radio\" id=\"{0}-{1}\" name=\"{0}\" value=\"{2}\" />" +
					"<label for=\"{0}-{1}\">{2}</label>" +
				"</span>")
		SafeHtml radioTemplateUnchecked(String fieldName, int id, String value);
		@SafeHtmlTemplates.Template("<span class=\"gwt-RadioButton\">" +
					"<input type=\"radio\" id=\"{0}-{1}\" name=\"{0}\" value=\"{2}\" checked=\"checked\"/>" +
					"<label for=\"{0}-{1}\">{2}</label>" +
				"</span>")
		SafeHtml radioTemplateChecked(String fieldName, int id, String value);
		@SafeHtmlTemplates.Template("<span class=\"gwt-CheckBox\">" +
				"<input type=\"checkbox\" id=\"{0}-{1}\" name=\"{0}\" value=\"{2}\" />" +
				"<label for=\"{0}-{1}\">{2}</label>" +
				"</span>")
		SafeHtml checkBoxTemplateUnchecked(String fieldName, int id, String value);
		@SafeHtmlTemplates.Template("<span class=\"gwt-CheckBox\">" +
				"<input type=\"checkbox\" id=\"{0}-{1}\" name=\"{0}\" value=\"{2}\" checked=\"checked\" />" +
				"<label for=\"{0}-{1}\">{2}</label>" +
				"</span>")
		SafeHtml checkBoxTemplateChecked(String fieldName, int id, String value);
		@SafeHtmlTemplates.Template("<input class=\"gwt-TextBox\" type=\"text\" value=\"{0}\" />")
		SafeHtml textBoxTemplate(String text);
	}
	
	public static class Answer {
		public List<String> selectedAnswers;
		public List<String> possibleAnswers;
		public ElementType type;
	}
	
	public GenericAnswerCell() {
		this(Alignment.VERTICAL);
	}
	
	public GenericAnswerCell(Alignment alignment) {
		// Register change event
		super("change");
		_alignment = alignment;
	}
	
	@Override
	public void onBrowserEvent(Cell.Context context, Element parent, Answer value, NativeEvent event, ValueUpdater<Answer> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		String eventType = event.getType();
		if ("change".equals(eventType)) {
			Object key = context.getKey();
			Answer newValue;
			switch(value.type) {
			case CHECKBOX:
				newValue = checkBoxInputEvent(context, parent, value);
				break;
			case RADIO:
				newValue = radioInputEvent(context, parent, value);
				break;
			case SELECT:
				newValue = selectInputEvent(context, parent, value);
				break;
			default:
				newValue = textInputEvent(context, parent, value);
			}
		}
	}

	private Answer selectInputEvent(Cell.Context context, Element parent, Answer value) {
		// TODO Auto-generated method stub
		Log.info("selectInputEvent() -- " + parent.getInnerHTML());
		SelectElement select = parent.getFirstChild().cast();
		return null;
	}

	private Answer radioInputEvent(Cell.Context context, Element parent, Answer value) {
		// TODO Auto-generated method stub
		Log.info("radioInputEvent()" + parent.getInnerHTML());
		for (int i=0; i < parent.getChildCount(); i++) {
			InputElement input = parent.getChild(i).getFirstChild().cast();
			if (input.isChecked()) {
				// TODO mark as selected!
				break;
			}
		}
		return null;
	}

	private Answer checkBoxInputEvent(Cell.Context context, Element parent, Answer value) {
		// TODO Auto-generated method stub
		Log.info("checkBoxInputEvent()" + parent.getInnerHTML());
		return null;
	}

	private Answer textInputEvent(Cell.Context context, Element parent, Answer value) {
		// TODO Auto-generated method stub
		Log.info("textInputEvent()" + parent.getInnerHTML());
		return null;
	}

	@Override
	public void render(Cell.Context context, GenericAnswerCell.Answer answer, SafeHtmlBuilder sb) {
		if (answer == null) {
			return;
		}
		
		_answersByRowKeys.put(context.getKey(), answer);
		
		if (answer.type == ElementType.TEXT) {
			// it's an open question (written text!)
			renderTextBox(answer, sb);
		} else if (answer.type == ElementType.CHECKBOX) {
			// Multiple choices only possible with checkoxes
			renderCheckBoxes(context, answer, sb);
		} else if (answer.type == ElementType.SELECT) {
			// a dropdown selection is preferred to radio buttons
			renderSelectBox(context, answer, sb);
		} else {
			// radio buttons are chosen...
			renderRadios(context, answer, sb);
		}
	}
	
	private void renderSelectBox(Cell.Context context, Answer answer, SafeHtmlBuilder sb) {
		sb.append(templates.selectTemplate(context.getKey().toString()));
		for (String possibleAnswer : answer.possibleAnswers) {
			if (answer.selectedAnswers != null && answer.selectedAnswers.size() > 0 && possibleAnswer.equals(answer.selectedAnswers.get(0))) {
				sb.append(templates.optionTemplateSelected(possibleAnswer));
			} else {
				sb.append(templates.optionTemplate(possibleAnswer));
			}
		}
		sb.appendHtmlConstant("</select>");
	}
	
	private void renderRadios(Cell.Context context, Answer answer, SafeHtmlBuilder sb) {
		if (answer.possibleAnswers == null) {
			return;
		}
		
		int i = 0;
		
		for (String element : answer.possibleAnswers) {
			if (answer.selectedAnswers != null && answer.selectedAnswers.size() > 0 && element.equals(answer.selectedAnswers.get(0))) {
				sb.append(templates.radioTemplateChecked(context.getKey().toString(), i++, element));
			} else {
				sb.append(templates.radioTemplateUnchecked(context.getKey().toString(), i++, element));
			}
			if (_alignment == Alignment.VERTICAL) {
				sb.appendHtmlConstant("<br />");
			}
		}
	}
	
	private void renderCheckBoxes(Cell.Context context, Answer answer, SafeHtmlBuilder sb) {
		if (answer.possibleAnswers == null) {
			return;
		}
		
		boolean selectedAnswerFound = false;
		int i=0;
		
		for (String possibleAnswer : answer.possibleAnswers) {
			if (answer.selectedAnswers != null && answer.selectedAnswers.size() > 0) {
				for (String selectedAnswer : answer.selectedAnswers) {
					if (selectedAnswer.equals(possibleAnswer)) {
						selectedAnswerFound = true;
					}
				}
			}
			
			if (selectedAnswerFound) {
				sb.append(templates.checkBoxTemplateChecked(context.getKey().toString(), i++, possibleAnswer));
			} else {
				sb.append(templates.checkBoxTemplateUnchecked(context.getKey().toString(), i++, possibleAnswer));
			}
			
			if (_alignment == Alignment.VERTICAL) {
				sb.appendHtmlConstant("<br />");
			}

			selectedAnswerFound = false;
		}
	}

	private void renderTextBox(Answer answer, SafeHtmlBuilder sb) {
		if (answer.selectedAnswers != null && answer.selectedAnswers.size() > 0 && answer.selectedAnswers.get(0) != null) {
			sb.append(templates.textBoxTemplate(answer.selectedAnswers.get(0)));
		} else {
			sb.append(SafeHtmlUtils.fromSafeConstant("<input class=\"gwt-TextBox\" type=\"text\"></input>"));
		}
	}
}
