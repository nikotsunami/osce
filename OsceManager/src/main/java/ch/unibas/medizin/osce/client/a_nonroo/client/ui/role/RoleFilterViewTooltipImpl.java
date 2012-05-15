package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.management.relation.Role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;


import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.domain.Specialisation;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class RoleFilterViewTooltipImpl extends PopupPanel {
	
	private static RoleTopicFilterPopupUiBinder uiBinder = GWT.create(RoleTopicFilterPopupUiBinder.class);
	
	interface RoleTopicFilterPopupUiBinder extends
	UiBinder<Widget, RoleFilterViewTooltipImpl> {
	}
	
	
	private class CheckBoxItem {
		public CheckBox checkbox;
		public String name;
		
		public CheckBoxItem(CheckBox box, String n) {
			checkbox = box;
			name = n;
		}
	}
	
	private ArrayList<CheckBoxItem> fields = new ArrayList<CheckBoxItem>();
	private int maxApplicableFilters;
	private int minApplicableFilters = 1;
	private boolean selectionChanged = false;
	
	
	@UiField
	FocusPanel filterPanelRoot;
	
	@UiField
	CheckBox TopicName;
	
	@UiField
	CheckBox Keyword;
	
	@UiField
	CheckBox ItemName;
	
	@UiField
	CheckBox RoleName;
	
	@UiField
	CheckBox CheckListItem;
	
	@UiField
	CheckBox ItemValue;
	
	@UiField
	Label Complexity;
	
	@UiField
	TextBox ComplexityText;
	
	@UiField
	ListBox ComplexityListBox;
	
	@UiField
	Label Author;
	
	@UiField
	ListBox AuthorListBox;
	
	@UiField
	Label Reviewer;
	
	@UiField
	ListBox ReviewerListBox;

	@UiField
	Label Specification;
	
	@UiField
	ListBox SpecificationListBox;
	
	@UiField
	Label StudyYear;
	
//	@UiField
//	ListBox StudyYearListBox;
	
	
	@UiField(provided = true)
    ValueListBox<StudyYears> StudyYearListBox = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
	
	
	
	@UiField
	Label Keywordlbl;
	
	@UiField
	ListBox KeywordListBox;
	
	@UiField
	IconButton resetButton;
	
	
	
	
private class CheckBoxChangeHandler implements ValueChangeHandler<Boolean> {
		

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {			
			selectionChanged = true;
			int uncheckedBoxes = 0;
			for (CheckBoxItem item : fields) {
				if (item.checkbox.getValue() == false) {
					uncheckedBoxes++;
				}
				item.checkbox.setEnabled(true);
			}
			
			if (uncheckedBoxes >= fields.size() - minApplicableFilters) {
				for (CheckBoxItem item : fields) {
					if (item.checkbox.getValue())
						item.checkbox.setEnabled(false);
				}
			} else if (fields.size() - uncheckedBoxes >= maxApplicableFilters) {
				for (CheckBoxItem item : fields) {
					if (!item.checkbox.getValue())
						item.checkbox.setEnabled(false);
				}
			}
			
			String msg = "Searching for: ";
			Iterator<String> i = getFilters().iterator();
			while(i.hasNext())
				msg = msg + i.next() + ", ";
			Log.info(msg);
		}
	}
	
	
	public List<String> getFilters() {
		List<String> filters = new ArrayList<String>();
		for(CheckBoxItem checkBoxItem : fields) {
			if (checkBoxItem.checkbox.getValue()) {
				filters.add(checkBoxItem.name);
			}
		}
		return filters;
	}
	
	
	
	
	
	
	public RoleFilterViewTooltipImpl(){
		super(true);
		add(uiBinder.createAndBindUi(this));
		filterPanelRoot.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				int mouseX = event.getClientX();
				int mouseY = event.getClientY();
				
				if (mouseX < getAbsoluteLeft() || mouseX > getAbsoluteLeft() + getOffsetWidth() 
						|| mouseY < getAbsoluteTop() || mouseY > getAbsoluteTop() + getOffsetHeight()) {

					// TODO: handle it from view
					//view.updateSearch();
					hide();
				}
				
			}
		});
		
		
		OsceConstants constants = GWT.create(OsceConstants.class);
		resetButton.setText(constants.resetFilters());
		
		
		//TopicName  Keyword ItemName ItemName RoleName CheckListItem ItemValue
		
		
		initCheckBox(TopicName, "name", constants.topic());
		initCheckBox(Keyword, "keyword", constants. keyword());
		initCheckBox(ItemName, "itemName", constants.itemName());
		initCheckBox(RoleName, "roles", constants.roleName());
		initCheckBox(CheckListItem, "checkListItem", constants.checkListItem());
		initCheckBox(ItemValue, "itemValue", constants.itemValue());
		
		maxApplicableFilters = fields.size();
		Iterator<CheckBoxItem> fieldIter = fields.iterator();
		while (fieldIter.hasNext()) {
			CheckBox box = fieldIter.next().checkbox;
			box.addValueChangeHandler(new CheckBoxChangeHandler());
		}
		
		
		
		
		
		//ComplexityDD=new ListBox(false);
		ComplexityListBox.addItem("Complexity1");
		ComplexityListBox.addItem("Complexity2");
		ComplexityListBox.addItem("Complexity3");
		ComplexityListBox.addItem("Complexity4");
		
		AuthorListBox.addItem("Author1");
		AuthorListBox.addItem("Author2");
		AuthorListBox.addItem("Author3");
		AuthorListBox.addItem("Author4");
		
		ReviewerListBox.addItem("Reviewr1");
		ReviewerListBox.addItem("Reviewr2");
		ReviewerListBox.addItem("Reviewr3");
		ReviewerListBox.addItem("Reviewr4");
		
		SpecificationListBox.addItem("Specification1");
		SpecificationListBox.addItem("Specification2");
		SpecificationListBox.addItem("Specification3");
		SpecificationListBox.addItem("Specification4");
		
		StudyYearListBox.setValue(StudyYears.values()[0]);
		StudyYearListBox.setAcceptableValues(Arrays.asList(StudyYears.values()));
		
		
		
		KeywordListBox.addItem("Keyword1");
		KeywordListBox.addItem("Keyword2");
		KeywordListBox.addItem("Keyword3");
		KeywordListBox.addItem("Keyword4");
	}
	public void clearSelectionChanged() {
		selectionChanged = false;
	}
	
	public boolean selectionChanged() {
		return selectionChanged;
	}
	
	
	private void initCheckBox(CheckBox uiField, String name, String text) {
		uiField.setText(text);
		fields.add(new CheckBoxItem(uiField, name));
	}
	
	public void setMaxApplicableFilters(int n) {
		maxApplicableFilters = n;
	}
	
	public void setMinApplicableFilters(int n) {
		if (minApplicableFilters > fields.size())
			minApplicableFilters = fields.size();
		minApplicableFilters = n;
	}
	
	public HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler) {
		return super.addCloseHandler(handler);
	}
	
	
}
