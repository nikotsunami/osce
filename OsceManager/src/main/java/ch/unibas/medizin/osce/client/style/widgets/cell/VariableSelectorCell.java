package ch.unibas.medizin.osce.client.style.widgets.cell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class VariableSelectorCell extends AbstractInputCell<VariableSelectorCell.Choices, VariableSelectorCell.Choices> {
	private static Templates templates = GWT.create(Templates.class);
	private final Alignment _alignment;
	
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
	
	public static class Choice {
		private String option;
		private boolean checked;
		
		public Choice(String option, boolean checked) {
			this.option = option;
			this.checked = checked;
		}
		
		public String getOption() {
			return option;
		}
		
		public void setOption(String option) {
			this.option = option;
		}
		
		public boolean isChecked() {
			return checked;
		}
		
		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}
	
	public static class Choices {
		private List<Choice> choices = new ArrayList<Choice>();
		private String customChoice;
		private SelectorType selectorType;
		
		public Choices(SelectorType selectorType) {
			this.selectorType = selectorType;
		}
		
		public Choices(String customChoice) {
			this.customChoice = customChoice;
			this.selectorType = SelectorType.TEXT;
		}
		
		public Choices (SelectorType selectorType, List<String> options) {
			this.selectorType = selectorType;
			for (String option : options) {
				choices.add(new Choice(option, false));
			}
		}
		
		public Choices (SelectorType selectorType, String[] options) {
			this.selectorType = selectorType;
			for (String option : options) {
				choices.add(new Choice(option, false));
			}
		}
		
		public Choice get(int index) {
			return choices.get(index);
		}
		
		public void setSelected(int index) {
			choices.get(index).setChecked(true);
		}
		
		public void clearSelected(int index) {
			choices.get(index).setChecked(false);
		}
		
		public void setExclusivelySelected(int index) {
			int i = 0;
			for (Choice choice : choices) {
				if (index == i++) {
					choice.setChecked(true);
				} else {
					choice.setChecked(false);
				}
			}
		}
		
		public void setCustomChoice(String customChoice) {
			this.customChoice = customChoice;
		}
		
		public SelectorType getSelectorType() {
			return selectorType;
		}
		
		public String getCustomChoice() {
			return customChoice;
		}
		
		public List<Choice> getChoices() {
			return choices;
		}
		
		public Choice getSelectedChoice() {
			for (Choice choice : choices) {
				if (choice.isChecked()) {
					return choice;
				}
			}
			return null;
		}
		
		public List<Choice> getSelectedChoices() {
			List<Choice> selectedChoices = new ArrayList<Choice>();
			for (Choice choice : choices) {
				if (choice.isChecked()) {
					selectedChoices.add(choice);
				}
			}
			return selectedChoices;
		}
		
		public void addChoice(Choice choice) {
			choices.add(choice);
		}
		
		public void addChoice(String option, boolean checked) {
			choices.add(new Choice(option, checked));
		}
		
		@Override
		public boolean equals (Object other) {
			if (!(other instanceof Choices)) {
				return false;
			}
			Choices otherChoices = (Choices) other;
			return (selectorType.equals(otherChoices.selectorType) && 
					equalsOrNull(customChoice, otherChoices.customChoice) &&
					equalsOrNull(choices, otherChoices.choices));
		}
		
		private static boolean equalsOrNull(Object a, Object b) {
			if (a != null) {
				return a.equals(b);
			}
			return (b == null);
		}
	}
	
	public static class ViewData {
		private Choices currentChoices;
		private Choices oldChoices;
		
		public ViewData(Choices choices) {
			currentChoices = choices;
			oldChoices = choices;
		}
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof ViewData)) {
				return false;
			}
			ViewData otherViewData = (ViewData) other;
			return otherViewData.currentChoices.equals(currentChoices) 
					&& otherViewData.oldChoices.equals(oldChoices);
		}
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
			switch(value.getSelectorType()) {
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
		int index = select.getSelectedIndex();
		value.setExclusivelySelected(index);
		return value;
	}

	private Choices radioInputEvent(Element parent, Choices value) {
		Log.info("radioInputEvent()");
		for (int i=0; i < parent.getChildCount(); i++) {
			InputElement input = parent.getChild(i).getFirstChild().cast();
			if (input.isChecked()) {
				value.setExclusivelySelected(i);
				break;
			}
		}
		return value;
	}

	private Choices checkBoxInputEvent(Element parent, Choices value) {
		Log.info("checkBoxInputEvent()");
		for (int i=0; i < parent.getChildCount(); i++) {
			InputElement input = parent.getChild(i).getFirstChild().cast();
			if (input.isChecked()) {
				value.setSelected(i);
			} else {
				value.clearSelected(i);
			}
		}
		return value;
	}

	private Choices textInputEvent(Element parent, Choices value) {
		Log.info("textInputEvent()");
		InputElement input = parent.getFirstChild().cast();
		value.setCustomChoice(input.getValue());
		return value;
	}

	@Override
	public void render(Cell.Context context, VariableSelectorCell.Choices answer, SafeHtmlBuilder sb) {
		if (answer == null) {
			return;
		}
		
		if (answer.getSelectorType() == SelectorType.TEXT) {
			// it's an open question (written text!)
			renderTextBox(answer, sb);
		} else if (answer.getSelectorType() == SelectorType.CHECKBOX) {
			// Multiple choices only possible with checkoxes
			renderCheckBoxes(context, answer, sb);
		} else if (answer.getSelectorType() == SelectorType.SELECT) {
			// a dropdown selection is preferred to radio buttons
			renderSelectBox(context, answer, sb);
		} else {
			// radio buttons are chosen...
			renderRadios(context, answer, sb);
		}
	}
	
	private void renderSelectBox(Cell.Context context, Choices answer, SafeHtmlBuilder sb) {
		if (answer.getChoices() == null) {
			return;
		}
		sb.append(templates.selectTemplate(context.getKey().toString()));
		for (Choice choice : answer.getChoices()) {
			if (choice.isChecked()) {
				sb.append(templates.optionTemplateSelected(choice.getOption()));
			} else {
				sb.append(templates.optionTemplate(choice.getOption()));
			}
		}
		sb.appendHtmlConstant("</select>");
	}
	
	private void renderRadios(Cell.Context context, Choices answer, SafeHtmlBuilder sb) {
		if (answer.getChoices() == null) {
			return;
		}
		
		int i = 0;
		for (Choice choice : answer.getChoices()) {
			if (choice.isChecked()) {
				sb.append(templates.radioTemplateChecked(context.getKey().toString(), i++, choice.getOption()));
			} else {
				sb.append(templates.radioTemplateUnchecked(context.getKey().toString(), i++, choice.getOption()));
			}
			if (_alignment == Alignment.VERTICAL) {
				sb.appendHtmlConstant("<br />");
			}
		}
	}
	
	private void renderCheckBoxes(Cell.Context context, Choices answer, SafeHtmlBuilder sb) {
		if (answer.getChoices() == null) {
			return;
		}
		
		int i=0;
		for (Choice choice : answer.getChoices()) {
			if (choice.isChecked()) {
				sb.append(templates.checkBoxTemplateChecked(context.getKey().toString(), i++, choice.getOption()));
			} else {
				sb.append(templates.checkBoxTemplateUnchecked(context.getKey().toString(), i++, choice.getOption()));
			}
			
			if (_alignment == Alignment.VERTICAL) {
				sb.appendHtmlConstant("<br />");
			}
		}
	}

	private void renderTextBox(Choices answer, SafeHtmlBuilder sb) {
		String text = answer.getCustomChoice();
		if (text == null) {
			text = "";
		}
		sb.append(templates.textBoxTemplate(text));
	}
}
