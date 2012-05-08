package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientFilterViewImpl extends PopupPanel {

	private static StandardizedPatientFilterPopupUiBinder uiBinder = GWT.create(StandardizedPatientFilterPopupUiBinder.class);

	interface StandardizedPatientFilterPopupUiBinder extends
			UiBinder<Widget, StandardizedPatientFilterViewImpl> {
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
	SpanElement labelSearch;
	@UiField
	FocusPanel filterPanelRoot;
	@UiField
	CheckBox name;
	@UiField
	CheckBox prename;
	@UiField
	CheckBox street;
	@UiField
	CheckBox city;
	@UiField
	CheckBox postalCode;
	@UiField
	CheckBox telephone;
	@UiField
	CheckBox telephone2;
	@UiField
	CheckBox mobile;
	@UiField
	CheckBox email;
	@UiField
	CheckBox bankName;
	@UiField
	CheckBox bankBIC;
	@UiField
	CheckBox bankIBAN;
	@UiField
	CheckBox description;
	
	@UiField
	IconButton resetButton;
	

	@UiHandler("resetButton")
	void onClick(ClickEvent e) {
		Iterator<CheckBoxItem> iter = fields.iterator();
		while(iter.hasNext()) {
			iter.next().checkbox.setValue(false);
		}
		name.setValue(true);
		prename.setValue(true);
	}
	
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
	
	public StandardizedPatientFilterViewImpl() {
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
		labelSearch.setInnerText(constants.searchFor());
		resetButton.setText(constants.resetFilters());
		
		initCheckBox(name, "name", constants.name());
		initCheckBox(prename, "preName", constants.preName());
		initCheckBox(street, "street", constants.street());
		initCheckBox(city, "city", constants.city());
		initCheckBox(postalCode, "postalCode", constants.plz());
		initCheckBox(telephone, "telephone", constants.telephone());
		initCheckBox(telephone2, "telephone2", constants.telephone() + " 2");
		initCheckBox(mobile, "mobile", constants.mobile());
		initCheckBox(email, "email", constants.email());
		initCheckBox(bankName, "bankName", constants.bank());
		initCheckBox(bankBIC, "BIC", constants.bic());
		initCheckBox(bankIBAN, "IBAN", constants.iban());
		// FIXME: if "comment" field is selected, quick search does not work anymore
		initCheckBox(description, "comment", constants.description());
		
		name.setValue(true);
		prename.setValue(true);
		
		maxApplicableFilters = fields.size();
		
		
		Iterator<CheckBoxItem> fieldIter = fields.iterator();
		while (fieldIter.hasNext()) {
			CheckBox box = fieldIter.next().checkbox;
			box.addValueChangeHandler(new CheckBoxChangeHandler());
		}
	}
	
	private void initCheckBox(CheckBox uiField, String name, String text) {
		uiField.setText(text);
		fields.add(new CheckBoxItem(uiField, name));
	}
	
	/*
	private void initListBox(ListBox uiField, String name, String text) {
		uiField.setText(text);
		fields.add(new ListBoxItem(uiField, name));
	}
	
	private void initFromTo(ListBox uiFieldFrom, ListBox uiFieldTo, String name, String text) {
		uiFieldFrom.setText(constants.FROM);
		uiFieldTo.setText(constants.TO);
		fields.add(new CheckBoxItem(uiField, name));
	}
	*/
	
	/**
	 * Sets the maximum number of filters that can be active at once.
	 * @param n
	 */
	public void setMaxApplicableFilters(int n) {
		maxApplicableFilters = n;
	}
	
	/**
	 * Sets the minimum number of filters that must be active at once.
	 * @param n
	 */
	public void setMinApplicableFilters(int n) {
		if (minApplicableFilters > fields.size())
			minApplicableFilters = fields.size();
		minApplicableFilters = n;
	}
	
	/**
	 * Returns a string array of all db fields to search in
	 * @return
	 */
	public List<String> getFilters() {
		List<String> filters = new ArrayList<String>();
		for(CheckBoxItem checkBoxItem : fields) {
			if (checkBoxItem.checkbox.getValue()) {
				filters.add(checkBoxItem.name);
			}
		}
		return filters;
	}
	
	public HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler) {
		return super.addCloseHandler(handler);
	}
	
	public void clearSelectionChanged() {
		selectionChanged = false;
	}
	
	public boolean selectionChanged() {
		return selectionChanged;
	}
}