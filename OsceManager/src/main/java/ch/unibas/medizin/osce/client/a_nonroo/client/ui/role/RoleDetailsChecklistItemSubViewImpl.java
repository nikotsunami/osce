package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ResolutionSettings;
import ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination.MessageConfirmationDialogBox;
import ch.unibas.medizin.osce.client.managed.request.ChecklistCriteriaProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistOptionProxy;
import ch.unibas.medizin.osce.client.style.resources.MyCellTableResources;
import ch.unibas.medizin.osce.client.style.resources.UiIcons;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.OsMaConstant;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RoleDetailsChecklistItemSubViewImpl extends Composite implements RoleDetailsChecklistItemSubView{

	private static RoleDetailsChecklistItemSubViewImplUiBinder uiBinder = GWT.create(RoleDetailsChecklistItemSubViewImplUiBinder.class);
	
	interface RoleDetailsChecklistItemSubViewImplUiBinder extends UiBinder<Widget, RoleDetailsChecklistItemSubViewImpl> {
	}
	
	private final UiIcons uiIcons = GWT.create(UiIcons.class);
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	private Delegate delegate;
	
	@UiField
	IconButton edit;
	
	@UiField
	IconButton delete;
	
	@UiField
	Label questionNameLbl;
	
	@UiField
	Label questionDescLbl;
	
	@UiField
	Image arrow;
	
	@UiField
	DisclosurePanel checkListQuestionDisclosurePanel;
	
	@UiField
	VerticalPanel headerPanel;
	
	@UiField (provided = true)
	public CellTable<ChecklistOptionProxy> optionTable;
	
	@UiField (provided = true)
	public CellTable<ChecklistCriteriaProxy> criteriaTable;
	
	private ChecklistItemProxy checklistItemProxy;
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	private int criteriaLeft = 0;
	
	private int criteriaTop = 0;
	
	private int optionLeft = 0;
	
	private int optionTop = 0;


	public RoleDetailsChecklistItemSubViewImpl() {
		CellTable.Resources tableResources = GWT.create(MyCellTableResources.class);
		optionTable = new CellTable<ChecklistOptionProxy>(100, tableResources);
		criteriaTable = new CellTable<ChecklistCriteriaProxy>(100, tableResources);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		initOptionTable();
		
		initCriteriaTable();
	}

	private void initCriteriaTable() {
		
		criteriaTable.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				criteriaLeft = event.getClientX();
				criteriaTop = event.getClientY();
			}
		}, ClickEvent.getType());
		
		TextColumn<ChecklistCriteriaProxy> nameCol = new TextColumn<ChecklistCriteriaProxy>() 
		{
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(ChecklistCriteriaProxy object) 
			{
				return renderer.render(object.getCriteria());
			}
		};
		
		criteriaTable.addColumn(nameCol, constants.roleCriteriaLabel());		
		
		addCriteriaColumn(new ActionCell<ChecklistCriteriaProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<ChecklistCriteriaProxy>() {
					public void execute(final ChecklistCriteriaProxy criteriaProxy) {
						editCriteriaPopup(criteriaProxy);
					}
				}), "", new GetValueCriteria<ChecklistCriteriaProxy>() {
			public ChecklistCriteriaProxy getValue(ChecklistCriteriaProxy criteriaProxy) {
				return criteriaProxy;
			}
		}, null, null);
		
		criteriaTable.addColumnStyleName(1, "iconCol");
		
		BtnHeader btnHeader = new BtnHeader(new ButtonCell(), constants.addCriteria()) {
			protected void updateHeader() {
				createCriteriaPopup();
			}
		};
		
		addCriteriaColumn(new ActionCell<ChecklistCriteriaProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<ChecklistCriteriaProxy>() {
					public void execute(final ChecklistCriteriaProxy criteriaProxy) {
						
					}
				}), "", new GetValueCriteria<ChecklistCriteriaProxy>() {
			public ChecklistCriteriaProxy getValue(ChecklistCriteriaProxy criteriaProxy) {
				return criteriaProxy;
			}
		}, null, btnHeader);
		
		criteriaTable.addColumnStyleName(2, "iconCol");
	}

	private void editCriteriaPopup(final ChecklistCriteriaProxy criteriaProxy) {
		if (criteriaProxy != null) {
			final ChecklistiOSCEOptionPopupViewImpl criteriaPopupViewImpl = new ChecklistiOSCEOptionPopupViewImpl();
			criteriaPopupViewImpl.createCriteriaPopup();
			criteriaPopupViewImpl.getNameTextBox().setValue(criteriaProxy.getCriteria());
			criteriaPopupViewImpl.getDescriptionTextArea().setValue(criteriaProxy.getDescription());
			
			criteriaPopupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					criteriaPopupViewImpl.hide();
					String name = criteriaPopupViewImpl.getNameTextBox().getValue();
					if (validateField(name)) {
						String description = criteriaPopupViewImpl.getDescriptionTextArea().getValue();
						delegate.updateCriteriaClicked(name, description, checklistItemProxy, criteriaProxy, RoleDetailsChecklistItemSubViewImpl.this);
						criteriaPopupViewImpl.getNameTextBox().setValue("");
						criteriaPopupViewImpl.getDescriptionTextArea().setValue("");
					} else {
						MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
						confirmationDialogBox.showConfirmationDialog(constants.tabErrorMessage());		
					}
				}
			});
			
			criteriaPopupViewImpl.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					criteriaPopupViewImpl.hide();
					criteriaPopupViewImpl.getNameTextBox().setValue("");
					criteriaPopupViewImpl.getDescriptionTextArea().setValue("");
				}
			});
			
			criteriaPopupViewImpl.showRelativeTo(criteriaTable);
			int left = criteriaLeft - 260;
			int height = criteriaPopupViewImpl.getOffsetHeight() + criteriaTop;
			
			if (height > (ResolutionSettings.getRightWidgetHeight() + 20)) {
				int top = criteriaTop - criteriaPopupViewImpl.getOffsetHeight();
				criteriaPopupViewImpl.setPopupPosition(left, top);
			}			
			else {
				criteriaPopupViewImpl.setPopupPosition(left, criteriaTop);
			}
		}
	}

	private void createCriteriaPopup() {
		if (checklistItemProxy != null) {
			final ChecklistiOSCEOptionPopupViewImpl criteriaPopupViewImpl = new ChecklistiOSCEOptionPopupViewImpl();
			criteriaPopupViewImpl.createCriteriaPopup();
			
			criteriaPopupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					criteriaPopupViewImpl.hide();
					String name = criteriaPopupViewImpl.getNameTextBox().getValue();
					if (validateField(name)) {
						String description = criteriaPopupViewImpl.getDescriptionTextArea().getValue();
						delegate.saveCriteriaClicked(name, checklistItemProxy, description, RoleDetailsChecklistItemSubViewImpl.this);
						criteriaPopupViewImpl.getNameTextBox().setValue("");
						criteriaPopupViewImpl.getDescriptionTextArea().setValue("");
					} else {
						MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
						confirmationDialogBox.showConfirmationDialog(constants.tabErrorMessage());
					}
				}
			});
			
			criteriaPopupViewImpl.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					criteriaPopupViewImpl.hide();
					criteriaPopupViewImpl.getNameTextBox().setValue("");
					criteriaPopupViewImpl.getDescriptionTextArea().setValue("");
				}
			});
			
			criteriaPopupViewImpl.showRelativeTo(criteriaTable);
			int left = criteriaLeft - 260;
			int height = criteriaPopupViewImpl.getOffsetHeight() + criteriaTop;
			
			if (height > (ResolutionSettings.getRightWidgetHeight() + 20)) {
				int top = criteriaTop - criteriaPopupViewImpl.getOffsetHeight();
				criteriaPopupViewImpl.setPopupPosition(left, top);
			}			
			else {
				criteriaPopupViewImpl.setPopupPosition(left, criteriaTop);
			}
		}
	}

	private void initOptionTable() {
		
		optionTable.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				optionLeft = event.getClientX();
				optionTop = event.getClientY();
			}
		}, ClickEvent.getType());
		
		TextColumn<ChecklistOptionProxy> nameCol = new TextColumn<ChecklistOptionProxy>() 
		{
			
			Renderer<java.lang.String> renderer = new AbstractRenderer<java.lang.String>() 
			{
				public String render(java.lang.String obj) 
				{
					return obj == null ? "" : String.valueOf(obj);
				}
			};

			@Override
			public String getValue(ChecklistOptionProxy object) 
			{
				return renderer.render(object.getOptionName() + "(" + object.getValue() + ")");
			}
		};
		
		optionTable.addColumn(nameCol, constants.roleOptionLabel());
		
		addOptionColumn(new ActionCell<ChecklistOptionProxy>(
				OsMaConstant.EDIT_ICON, new ActionCell.Delegate<ChecklistOptionProxy>() {
					public void execute(final ChecklistOptionProxy optionProxy) {
						editOptionPopup(optionProxy);
					}
				}), "", new GetValueOption<ChecklistOptionProxy>() {
			public ChecklistOptionProxy getValue(ChecklistOptionProxy optionProxy) {
				return optionProxy;
			}
		}, null, null);
		
		optionTable.addColumnStyleName(1, "iconCol");
		
		BtnHeader btnHeader = new BtnHeader(new ButtonCell(), constants.addOption()) {
			protected void updateHeader() {
				createOptionPopup();
			}
		};
		
		addOptionColumn(new ActionCell<ChecklistOptionProxy>(
				OsMaConstant.DELETE_ICON, new ActionCell.Delegate<ChecklistOptionProxy>() {
					public void execute(final ChecklistOptionProxy optionProxy) {
						
					}
				}), "", new GetValueOption<ChecklistOptionProxy>() {
			public ChecklistOptionProxy getValue(ChecklistOptionProxy optionProxy) {
				return optionProxy;
			}
		}, null, btnHeader);

		optionTable.addColumnStyleName(2, "iconCol");
	}

	private void editOptionPopup(final ChecklistOptionProxy optionProxy) {
		if (optionProxy != null) {
			final ChecklistiOSCEOptionPopupViewImpl optionPopupViewImpl = new ChecklistiOSCEOptionPopupViewImpl();
			optionPopupViewImpl.getNameTextBox().setValue(optionProxy.getOptionName());
			optionPopupViewImpl.getDescriptionTextArea().setValue(optionProxy.getDescription());
			optionPopupViewImpl.getValueTextBox().setValue(optionProxy.getValue());
			optionPopupViewImpl.getCriteriaCountBox().setSelectedIndex(optionProxy.getCriteriaCount().intValue());			
			
			optionPopupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					optionPopupViewImpl.hide();
					String name = optionPopupViewImpl.getNameTextBox().getValue();
					if (validateField(name)) {
						String description = optionPopupViewImpl.getDescriptionTextArea().getValue();
						String value = optionPopupViewImpl.getValueTextBox().getValue();
						String criteriaCount = optionPopupViewImpl.getCriteriaCountBox().getValue(optionPopupViewImpl.getCriteriaCountBox().getSelectedIndex());
						delegate.updateOptionClicked(name, description, value, criteriaCount, checklistItemProxy, optionProxy, RoleDetailsChecklistItemSubViewImpl.this);
						optionPopupViewImpl.getNameTextBox().setValue("");
						optionPopupViewImpl.getDescriptionTextArea().setValue("");
						optionPopupViewImpl.getValueTextBox().setValue("");
					} else {
						MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
						confirmationDialogBox.showConfirmationDialog(constants.tabErrorMessage());
					}
				}
			});
			
			optionPopupViewImpl.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					optionPopupViewImpl.hide();
					optionPopupViewImpl.getNameTextBox().setValue("");
					optionPopupViewImpl.getDescriptionTextArea().setValue("");
					optionPopupViewImpl.getValueTextBox().setValue("");
				}
			});
			
			optionPopupViewImpl.showRelativeTo(optionTable);
			int left = optionLeft - 150;
			int height = optionPopupViewImpl.getOffsetHeight() + optionTop;
			
			if (height > (ResolutionSettings.getRightWidgetHeight() + 20)) {
				int top = optionTop - optionPopupViewImpl.getOffsetHeight();
				optionPopupViewImpl.setPopupPosition(left, top);
			}			
			else {
				optionPopupViewImpl.setPopupPosition(left, optionTop);
			}
		}		
	}

	private void createOptionPopup() {
		if (checklistItemProxy != null) {
			final ChecklistiOSCEOptionPopupViewImpl optionPopupViewImpl = new ChecklistiOSCEOptionPopupViewImpl();
			
			optionPopupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					optionPopupViewImpl.hide();
					String name = optionPopupViewImpl.getNameTextBox().getValue();
					String value = optionPopupViewImpl.getValueTextBox().getValue();
					if (validateOptionField(name, value)) {
						String description = optionPopupViewImpl.getDescriptionTextArea().getValue();
						String criteriaCount = optionPopupViewImpl.getCriteriaCountBox().getValue(optionPopupViewImpl.getCriteriaCountBox().getSelectedIndex());
						delegate.saveOptionClicked(name, description, value, criteriaCount, checklistItemProxy, RoleDetailsChecklistItemSubViewImpl.this);
						optionPopupViewImpl.getNameTextBox().setValue("");
						optionPopupViewImpl.getDescriptionTextArea().setValue("");
						optionPopupViewImpl.getValueTextBox().setValue("");
					} else {
						/*MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
						confirmationDialogBox.showConfirmationDialog(constants.tabErrorMessage());*/
					}
				}
			});
			
			optionPopupViewImpl.getCancelBtn().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					optionPopupViewImpl.hide();
					optionPopupViewImpl.getNameTextBox().setValue("");
					optionPopupViewImpl.getDescriptionTextArea().setValue("");
					optionPopupViewImpl.getValueTextBox().setValue("");
				}
			});
			
			optionPopupViewImpl.showRelativeTo(optionTable);
			int left = optionLeft - 150;
			int height = optionPopupViewImpl.getOffsetHeight() + optionTop;
			
			if (height > (ResolutionSettings.getRightWidgetHeight() + 20)) {
				int top = optionTop - optionPopupViewImpl.getOffsetHeight();
				optionPopupViewImpl.setPopupPosition(left, top);
			}			
			else {
				optionPopupViewImpl.setPopupPosition(left, optionTop);
			}
		}
	
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;		
	}
	
	@UiHandler("arrow")
	public void arrowButtonClicked(ClickEvent event) {
		if(checkListQuestionDisclosurePanel.isOpen()) {
			checkListQuestionDisclosurePanel.setOpen(false);
			arrow.setResource(uiIcons.triangle1East());
			headerPanel.addStyleName("bottomBorder");
		}
		else {
			checkListQuestionDisclosurePanel.setOpen(true);
			arrow.setResource(uiIcons.triangle1South());
			headerPanel.removeStyleName("bottomBorder");
		}
	}
	
	@UiHandler("delete") 
	public void deleteQuestionClicked(ClickEvent e) {
		
	}
	
	@UiHandler("edit")
	public void editQuestionClicked(ClickEvent e) {
		final ChecklistiOSCEPopupViewImpl popupViewImpl = new ChecklistiOSCEPopupViewImpl();
		popupViewImpl.setPopupStyle(ItemType.TOPIC);
		popupViewImpl.createQuestionPopup();
		if (checklistItemProxy != null) {
			popupViewImpl.getNameTextBox().setValue(checklistItemProxy.getName());
			popupViewImpl.getDescriptionTextArea().setValue(checklistItemProxy.getDescription());
			popupViewImpl.getIsOverallQuestionChkBox().setValue(checklistItemProxy.getIsRegressionItem());
			popupViewImpl.getOptionTypeBox().setValue(checklistItemProxy.getOptionType());
		}
		popupViewImpl.getSaveBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popupViewImpl.hide();
				if (validateField(popupViewImpl.getNameTextBox().getValue())) {
					delegate.updateChecklistQuestion(popupViewImpl.getItemTypeBox().getValue(), popupViewImpl.getNameTextBox().getValue(), popupViewImpl.getDescriptionTextArea().getValue(), popupViewImpl.getIsOverallQuestionChkBox().getValue(), popupViewImpl.getOptionTypeBox().getValue(), RoleDetailsChecklistItemSubViewImpl.this, checklistItemProxy);
					popupViewImpl.getNameTextBox().setValue("");
					popupViewImpl.getDescriptionTextArea().setValue("");
				} else {
					MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					confirmationDialogBox.showConfirmationDialog(constants.tabErrorMessage());				
				}
			}
		});
		
		popupViewImpl.getCancelBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popupViewImpl.hide();
				popupViewImpl.getNameTextBox().setValue("");
				popupViewImpl.getDescriptionTextArea().setValue("");
			}
		});
		
		popupViewImpl.showRelativeTo(edit);
		int height = popupViewImpl.getOffsetHeight() + edit.getAbsoluteTop();
		if (height > (ResolutionSettings.getRightWidgetHeight() + 20)) {
			popupViewImpl.setDownArrowStyle();
			popupViewImpl.setPopupPosition(popupViewImpl.getAbsoluteLeft(), popupViewImpl.getAbsoluteTop() - 6);
		}			
		else {
			popupViewImpl.setPopupPosition(popupViewImpl.getAbsoluteLeft(), popupViewImpl.getAbsoluteTop() + 8);
		}
	}
	
	private boolean validateField(String name) {
		if (name != null && name.isEmpty() == false && name.length() > 0) 
			return true;
		
		return false;
	}
	
	private boolean validateOptionField(String name, String optionValue) {
		if (name != null && name.isEmpty() == false && name.length() > 0) {
			
			String regex = "\\d+";
			
			if (optionValue != null && optionValue.isEmpty() == false && optionValue.length() > 0) {
				if (optionValue.matches(regex) == false) {
					MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
					confirmationDialogBox.showConfirmationDialog(constants.optionValueErrMessage());
					return false;
				}
				return true;
			} else {
				MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
				confirmationDialogBox.showConfirmationDialog(constants.optionValueEmptyMessage());
				return false;
			}
			
		} else {
			MessageConfirmationDialogBox confirmationDialogBox = new MessageConfirmationDialogBox(constants.error());
			confirmationDialogBox.showConfirmationDialog(constants.tabErrorMessage());
			return false;
		}
	}
	
	public Label getQuestionNameLbl() {
		return questionNameLbl;
	}
	
	public Label getQuestionDescLbl() {
		return questionDescLbl;
	}
	
	@Override
	public ChecklistItemProxy getChecklistItemProxy() {
		return checklistItemProxy;
	}
	
	@Override
	public void setChecklistItemProxy(ChecklistItemProxy checklistItemProxy) {
		this.checklistItemProxy = checklistItemProxy;
	}
	
	public CellTable<ChecklistOptionProxy> getOptionTable() {
		return optionTable;
	}
	
	public CellTable<ChecklistCriteriaProxy> getCriteriaTable() {
		return criteriaTable;
	}
	
	private <C> void addOptionColumn(Cell<C> cell, String headerText,final GetValueOption<C> getter, FieldUpdater<ChecklistOptionProxy, C> fieldUpdater, BtnHeader btnHeader) 
	{
		Column<ChecklistOptionProxy, C> column = new Column<ChecklistOptionProxy, C>(cell) 
		{
			@Override
			public C getValue(ChecklistOptionProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		
		if (btnHeader == null)
			optionTable.addColumn(column, headerText);
		else
			optionTable.addColumn(column, btnHeader);		
	}
	
	private static interface GetValueOption<C> {
		C getValue(ChecklistOptionProxy proxyvalue);
	}
	
	private <C> void addCriteriaColumn(Cell<C> cell, String headerText,final GetValueCriteria<C> getter, FieldUpdater<ChecklistCriteriaProxy, C> fieldUpdater, BtnHeader btnHeader) 
	{
		Column<ChecklistCriteriaProxy, C> column = new Column<ChecklistCriteriaProxy, C>(cell) 
		{
			@Override
			public C getValue(ChecklistCriteriaProxy object) 
			{				
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) 
		{
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		
		if (btnHeader == null)
			criteriaTable.addColumn(column, headerText);
		else
			criteriaTable.addColumn(column, btnHeader);
	}
	
	private static interface GetValueCriteria<C> {
		C getValue(ChecklistCriteriaProxy proxyvalue);
	}
	
	public static class BtnHeader extends Header<String> {

		private final String toolTip;

		public BtnHeader(ButtonCell cell, String toolTip) {
			super(cell);
			this.toolTip = toolTip;
	    }
		
		@Override
		public void onBrowserEvent(Context context, Element elem,NativeEvent nativeEvent) {
			int eventType = Event.as(nativeEvent).getTypeInt();
	        if (eventType == Event.ONCLICK) {
	            nativeEvent.preventDefault();
	            updateHeader();
	        }
		}
		
		protected void updateHeader() {
		}

		@Override
		public void render(Context context, SafeHtmlBuilder sb) {
			SafeHtml PLUS_ICON = new SafeHtmlBuilder().appendHtmlConstant("<span title=\""+ toolTip + "\" class=\"ui-icon ui-icon-plusthick\"></span>").toSafeHtml();
			sb.append(PLUS_ICON);
		}

		@Override
		public String getValue() {
			return "";
		}
	}
}
