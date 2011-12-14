package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.dialect.function.StandardAnsiSqlAggregationFunctions.MinFunction;

import ch.unibas.medizin.osce.client.i18n.Messages;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;

import com.allen_sauer.gwt.log.client.Log;
import com.gargoylesoftware.htmlunit.AlertHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedPatientFilterView extends PopupPanel {

	private static StandardizedPatientFilterPopupUiBinder uiBinder = GWT.create(StandardizedPatientFilterPopupUiBinder.class);

	interface StandardizedPatientFilterPopupUiBinder extends
			UiBinder<Widget, StandardizedPatientFilterView> {
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
	private int checkedItems = 0;
	
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
	CheckBox mobile;
	@UiField
	CheckBox email;
	@UiField
	CheckBox birthday;
	@UiField
	CheckBox gender;
	@UiField
	CheckBox height;
	@UiField
	CheckBox weight;
	@UiField
	CheckBox profession;
	@UiField
	CheckBox nationality;
	@UiField
	CheckBox languageSkills;
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
		checkedItems = 2;
	}
	
	private class CheckBoxChangeHandler implements ValueChangeHandler<Boolean> {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			Iterator<CheckBoxItem> iter = fields.iterator();
			CheckBoxItem e;
			
			int uncheckedBoxes = 0;
			while(iter.hasNext()) {
				e = iter.next();
				if (e.checkbox.getValue() == false) {
					uncheckedBoxes++;
				}
				e.checkbox.setEnabled(true);
			}
			
			if (uncheckedBoxes >= fields.size() - minApplicableFilters) {
				iter = fields.iterator();
				while(iter.hasNext()) {
					e = iter.next();
					if (e.checkbox.getValue())
						e.checkbox.setEnabled(false);
				}
			} else if (fields.size() - uncheckedBoxes >= maxApplicableFilters) {
				iter = fields.iterator();
				while(iter.hasNext()) {
					e = iter.next();
					if (!e.checkbox.getValue())
						e.checkbox.setEnabled(false);
				}
			}
			
			checkedItems = fields.size() - uncheckedBoxes;
			
			String msg = "Searching for: ";
			String filters[] = getFilters();
			for (int i=0; i < filters.length; i++)
				msg = msg + filters[i] + ", ";
			Log.info(msg);
		}
	}
	
	public StandardizedPatientFilterView() {
		super(true);
		add(uiBinder.createAndBindUi(this));
		filterPanelRoot.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				hide();
			}
		});
		
		resetButton.setText(Messages.RESET_FILTERS);
		
		initCheckBox(name, "name", Messages.NAME);
		initCheckBox(prename, "pre_name", Messages.PRENAME);
		initCheckBox(street, "street", Messages.STREET);
		initCheckBox(city, "city", Messages.CITY);
		initCheckBox(postalCode, "postal_code", Messages.PLZ);
		initCheckBox(telephone, "telephone", Messages.TELEPHONE);
		initCheckBox(mobile, "telephone2", Messages.MOBILE);
		initCheckBox(email, "email", Messages.EMAIL);
		initCheckBox(birthday, "birthday", Messages.BIRTHDAY);
		initCheckBox(gender, "gender", Messages.GENDER);
		initCheckBox(height, "height", Messages.HEIGHT);
		initCheckBox(weight, "weight", Messages.WEIGHT);
		initCheckBox(profession, "profession", Messages.PROFESSION);
		initCheckBox(nationality, "nationality", Messages.NATIONALITY);
		initCheckBox(languageSkills, "lang_skill", Messages.LANGUAGE_SKILLS);
		initCheckBox(bankName, "bank_account.bank_name", Messages.BANK_NAME);
		initCheckBox(bankBIC, "bank_account.bic", Messages.BANK_BIC);
		initCheckBox(bankIBAN, "bank_account.IBAN", Messages.BANK_IBAN);
		initCheckBox(description, "description", Messages.DESCRIPTION);
		
		name.setValue(true);
		prename.setValue(true);
		checkedItems = 2;
		
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
	public String[] getFilters() {
		String filters[] = new String[checkedItems];
		int i = 0;
		
		Iterator<CheckBoxItem> fieldIter = fields.iterator();
		while (fieldIter.hasNext()) {
			CheckBoxItem item = fieldIter.next();
			if (item.checkbox.getValue())
				filters[i++] = item.name;
		}
		
		return filters;
	}
}