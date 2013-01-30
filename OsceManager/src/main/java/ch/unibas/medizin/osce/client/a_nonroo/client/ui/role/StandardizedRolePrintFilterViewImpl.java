package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.role.StandardizedRoleDetailsView.Delegate;
import ch.unibas.medizin.osce.client.managed.request.RoleItemAccessProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

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
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class StandardizedRolePrintFilterViewImpl extends PopupPanel {

	private static StandardizedRolePrintFilterPopupUiBinder uiBinder = GWT
			.create(StandardizedRolePrintFilterPopupUiBinder.class);

	interface StandardizedRolePrintFilterPopupUiBinder extends
			UiBinder<Widget, StandardizedRolePrintFilterViewImpl> {
	}

	static StandardizedRolePrintFilterViewImpl standardizedRolePrintFilterViewImpl;

	private ArrayList<CheckBoxItem> fields = new ArrayList<CheckBoxItem>();
	private int maxApplicableFilters;
	private int minApplicableFilters = 1;
	private boolean selectionChanged = false;
	private Delegate delegate;

	@UiField
	SpanElement printFor;
	@UiField
	FocusPanel filterPanelRoot;

	// @UiField
	// CheckBox shortName;
	// @UiField
	// CheckBox studyYear;
	// @UiField
	// CheckBox roleType;

	@UiField
	CheckBox basicData;

	@UiField
	Label roleItemAceess;

	@UiField
	CheckBox CheckList;
	@UiField
	CheckBox roomMaterials;
	@UiField
	CheckBox files;

	@UiField(provided = true)
	ValueListBox<RoleItemAccessProxy> roleScript = new ValueListBox<RoleItemAccessProxy>(
			new AbstractRenderer<RoleItemAccessProxy>() {
				public String render(RoleItemAccessProxy obj) {
					return obj == null ? "" : String.valueOf(obj.getName());
				}
			});

	@UiField
	IconButton resetButton;

	@UiField
	IconButton printButton;

	private StandardizedRoleProxy standardizedRoleProxy;

	@UiHandler("resetButton")
	void onResetButtonClick(ClickEvent e) {
		this.resetAllCheckBox();
	}

	private void resetAllCheckBox() {
		Iterator<CheckBoxItem> iter = fields.iterator();
		while (iter.hasNext()) {
			CheckBoxItem checkBoxItem = iter.next();
			checkBoxItem.checkbox.setValue(false);
			checkBoxItem.checkbox.setEnabled(true);
		}
		fields.get(0).checkbox.setValue(true);
		fields.get(0).checkbox.setEnabled(false);

	}

	@UiHandler("printButton")
	void onPrintButtonClick(ClickEvent e) {
		delegate.printRoleClicked(standardizedRolePrintFilterViewImpl);
	}

	private class CheckBoxItem {
		public CheckBox checkbox;
		public String name;

		public CheckBoxItem(CheckBox box, String n) {
			checkbox = box;
			name = n;
		}
	}

	public StandardizedRoleProxy getStandardizedRoleProxy() {
		return standardizedRoleProxy;
	}

	public void setStandardizedRoleProxy(
			StandardizedRoleProxy standardizedRoleProxy) {
		this.standardizedRoleProxy = standardizedRoleProxy;
	}

	public static StandardizedRolePrintFilterViewImpl getStandardizedRolePrintFilterViewImpl(
			StandardizedRoleProxy standardizedRoleProxy, Delegate delegate) {
		if (standardizedRolePrintFilterViewImpl == null) {
			standardizedRolePrintFilterViewImpl = new StandardizedRolePrintFilterViewImpl();
		}
		standardizedRolePrintFilterViewImpl
				.setStandardizedRoleProxy(standardizedRoleProxy);

		standardizedRolePrintFilterViewImpl.basicData.setValue(true);

		standardizedRolePrintFilterViewImpl.setDelegate(delegate);
		// standardizedRolePrintFilterViewImpl.roleScript = new
		// ValueListBox<RoleItemAccessProxy>(
		// new AbstractRenderer<RoleItemAccessProxy>() {
		// public String render(RoleItemAccessProxy obj) {
		// return obj == null ? "" : String.valueOf(obj.getName());
		// }
		// });

		delegate.getRoleScriptListPickerValues(standardizedRolePrintFilterViewImpl);
		return standardizedRolePrintFilterViewImpl;

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

			// String msg = "Printing for: ";
			// Iterator<String> i = getFilters().iterator();
			// while (i.hasNext())
			// msg = msg + i.next() + ", ";
			// Log.info(msg);
		}
	}

	private StandardizedRolePrintFilterViewImpl() {
		super(true);
		add(uiBinder.createAndBindUi(this));
		filterPanelRoot.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				int mouseX = event.getClientX();
				int mouseY = event.getClientY();

				if (mouseX < getAbsoluteLeft()
						|| mouseX > getAbsoluteLeft() + getOffsetWidth()
						|| mouseY < getAbsoluteTop()
						|| mouseY > getAbsoluteTop() + getOffsetHeight()) {

					// TODO: handle it from view
					// view.updateSearch();
					hide();
				}
			}
		});

		OsceConstants constants = GWT.create(OsceConstants.class);
		printFor.setInnerText(constants.printFor());
		printButton.setText(constants.print());
		resetButton.setText(constants.resetFilters());
		roleItemAceess.setText(constants.roleScript());

		// initCheckBox(shortName, "shortName", constants.shortName());
		// initCheckBox(studyYear, "studyYear", constants.studyYears());
		// initCheckBox(roleType, "roleType", constants.roleType());

		initCheckBox(basicData, "basicData", constants.basicData());
		initCheckBox(CheckList, "CheckList", constants.checkList());
		initCheckBox(roomMaterials, "roomMaterials", constants.roomMaterials());
		initCheckBox(files, "files", constants.fileDetail());

		maxApplicableFilters = fields.size();

		Iterator<CheckBoxItem> fieldIter = fields.iterator();
		while (fieldIter.hasNext()) {
			CheckBox box = fieldIter.next().checkbox;
			box.addValueChangeHandler(new CheckBoxChangeHandler());
		}
		this.setAutoHideEnabled(false);
	}

	private void initCheckBox(CheckBox uiField, String name, String text) {
		uiField.setText(text);
		fields.add(new CheckBoxItem(uiField, name));
	}

	/*
	 * private void initListBox(ListBox uiField, String name, String text) {
	 * uiField.setText(text); fields.add(new ListBoxItem(uiField, name)); }
	 * 
	 * private void initFromTo(ListBox uiFieldFrom, ListBox uiFieldTo, String
	 * name, String text) { uiFieldFrom.setText(constants.FROM);
	 * uiFieldTo.setText(constants.TO); fields.add(new CheckBoxItem(uiField,
	 * name)); }
	 */

	/**
	 * Sets the maximum number of filters that can be active at once.
	 * 
	 * @param n
	 */
	public void setMaxApplicableFilters(int n) {
		maxApplicableFilters = n;
	}

	/**
	 * Sets the minimum number of filters that must be active at once.
	 * 
	 * @param n
	 */
	public void setMinApplicableFilters(int n) {
		if (minApplicableFilters > fields.size())
			minApplicableFilters = fields.size();
		minApplicableFilters = n;
	}

	/**
	 * Returns a string array of all db fields to search in
	 * 
	 * @return
	 */
	public List<String> getFilters() {
		List<String> filters = new ArrayList<String>();
		for (CheckBoxItem checkBoxItem : fields) {
			if (checkBoxItem.checkbox.getValue()) {
				filters.add(checkBoxItem.checkbox.getText());
			}
		}
		return filters;
	}

	public RoleItemAccessProxy getSelectedRoleItemAccess() {
		return roleScript.getValue();
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

	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;

	}

	public void setRoleScriptListPickerValues(List<RoleItemAccessProxy> values) {
		Log.info("Length of roleItemAccessProxy" + values.size());
		roleScript.setValue(null);
		roleScript.setAcceptableValues(values);
	}

	@Override
	public void show() {
		this.resetAllCheckBox();
		super.show();
	}
}