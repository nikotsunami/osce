package ch.unibas.medizin.osce.client.style.widgets.cell;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class VariableSelectorCell extends AbstractInputCell<VariableSelectorCell.Choices, VariableSelectorCell.Choices> {
	private static Templates templates = GWT.create(Templates.class);
	private final Alignment _alignment;
	private EditTextCell cell;
	private TextInputCell cell2;
	
	public enum SelectorType {
		TEXT, RADIO, CHECKBOX, SELECT
	}

	public enum Alignment {
		VERTICAL, HORIZONTAL
	}
	
	public static interface Templates extends SafeHtmlTemplates {@SafeHtmlTemplates.Template("<select class=\"gwt-ListBox\" name=\"{0}\">")
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
	
	public static class Choices {
		public List<String> selectedChoices;
		public List<String> availableChoices;
		public SelectorType type;
	}
	
	public VariableSelectorCell() {
		this(Alignment.VERTICAL);
	}
	
	public VariableSelectorCell(Alignment alignment) {
		// Register change event
		super("change");
		_alignment = alignment;
	}
	
	@Override
	public void onBrowserEvent(Cell.Context context, Element parent, Choices value, NativeEvent event, ValueUpdater<Choices> valueUpdater) {
		if (value == null) {
			return;
		}
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if ("change".equals(event.getType())) {
			Object key = context.getKey();
			Choices newValue;
			switch(value.type) {
			case CHECKBOX:
				newValue = checkBoxInputEvent(parent, value);
				break;
			case RADIO:
				newValue = radioInputEvent(parent, value);
				break;
			case SELECT:
				newValue = selectInputEvent(parent, value);
				break;
			default:
				newValue = textInputEvent(parent, value);
			}
			setViewData(key, newValue);
			finishEditing(parent, newValue, key, valueUpdater);
			if (valueUpdater != null) {
				valueUpdater.update(newValue);
			}
		}
	}
	
	@Override
	public void onEnterKeyDown(Cell.Context context, Element parent, Choices value, NativeEvent event, ValueUpdater<Choices> valueUpdater) {
		// TODO implement???
	}

	private Choices selectInputEvent(Element parent, Choices value) {
		Log.info("selectInputEvent() -- ");
		SelectElement select = parent.getFirstChild().cast();
		value.selectedChoices.clear();
		int index = select.getSelectedIndex();
		String newSelectedAnswer = value.availableChoices.get(index);
		value.selectedChoices.add(newSelectedAnswer);
		return value;
	}

	private Choices radioInputEvent(Element parent, Choices value) {
		Log.info("radioInputEvent()");
		for (int i=0; i < parent.getChildCount(); i++) {
			InputElement input = parent.getChild(i).getFirstChild().cast();
			if (input.isChecked()) {
				value.selectedChoices.clear();
				value.selectedChoices.add(value.availableChoices.get(i));
				break;
			}
		}
		return value;
	}

	private Choices checkBoxInputEvent(Element parent, Choices value) {
		Log.info("checkBoxInputEvent()");
		value.selectedChoices.clear();
		for (int i=0; i < parent.getChildCount(); i++) {
			InputElement input = parent.getChild(i).getFirstChild().cast();
			if (input.isChecked()) {
				value.selectedChoices.add(value.availableChoices.get(i));
			}
		}
		return value;
	}

	private Choices textInputEvent(Element parent, Choices value) {
		Log.info("textInputEvent()");
		value.selectedChoices.clear();
		InputElement input = parent.getFirstChild().cast();
		value.selectedChoices.add(input.getValue());
		return value;
	}

	@Override
	public void render(Cell.Context context, VariableSelectorCell.Choices answer, SafeHtmlBuilder sb) {
		if (answer == null) {
			return;
		}
		
		if (answer.type == SelectorType.TEXT) {
			// it's an open question (written text!)
			renderTextBox(answer, sb);
		} else if (answer.type == SelectorType.CHECKBOX) {
			// Multiple choices only possible with checkoxes
			renderCheckBoxes(context, answer, sb);
		} else if (answer.type == SelectorType.SELECT) {
			// a dropdown selection is preferred to radio buttons
			renderSelectBox(context, answer, sb);
		} else {
			// radio buttons are chosen...
			renderRadios(context, answer, sb);
		}
	}
	
	private void renderSelectBox(Cell.Context context, Choices answer, SafeHtmlBuilder sb) {
		sb.append(templates.selectTemplate(context.getKey().toString()));
		for (String possibleAnswer : answer.availableChoices) {
			if (answer.selectedChoices != null && answer.selectedChoices.size() > 0 && possibleAnswer.equals(answer.selectedChoices.get(0))) {
				sb.append(templates.optionTemplateSelected(possibleAnswer));
			} else {
				sb.append(templates.optionTemplate(possibleAnswer));
			}
		}
		sb.appendHtmlConstant("</select>");
	}
	
	private void renderRadios(Cell.Context context, Choices answer, SafeHtmlBuilder sb) {
		if (answer.availableChoices == null) {
			return;
		}
		
		int i = 0;
		
		for (String element : answer.availableChoices) {
			if (answer.selectedChoices != null && answer.selectedChoices.size() > 0 && element.equals(answer.selectedChoices.get(0))) {
				sb.append(templates.radioTemplateChecked(context.getKey().toString(), i++, element));
			} else {
				sb.append(templates.radioTemplateUnchecked(context.getKey().toString(), i++, element));
			}
			if (_alignment == Alignment.VERTICAL) {
				sb.appendHtmlConstant("<br />");
			}
		}
	}
	
	private void renderCheckBoxes(Cell.Context context, Choices answer, SafeHtmlBuilder sb) {
		if (answer.availableChoices == null) {
			return;
		}
		
		boolean selectedAnswerFound = false;
		int i=0;
		
		for (String possibleAnswer : answer.availableChoices) {
			if (answer.selectedChoices != null && answer.selectedChoices.size() > 0) {
				for (String selectedAnswer : answer.selectedChoices) {
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

	private void renderTextBox(Choices answer, SafeHtmlBuilder sb) {
		if (answer.selectedChoices != null && answer.selectedChoices.size() > 0 && answer.selectedChoices.get(0) != null) {
			sb.append(templates.textBoxTemplate(answer.selectedChoices.get(0)));
		} else {
			sb.append(SafeHtmlUtils.fromSafeConstant("<input class=\"gwt-TextBox\" type=\"text\"></input>"));
		}
	}
}
