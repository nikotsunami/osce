package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ChecklistImportPojoValueProxy;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.managed.request.StandardizedRoleProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChecklistImportTabPopupViewImpl extends PopupPanel implements ChecklistImportTabPopupView {

	
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ChecklistImportTabPopupViewImpl> {
	}

	private OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;

	@UiField
	DivElement arrowBorder;

	@UiField
	DivElement arrow;

	@UiField
	HorizontalPanel specialisationPanel;

	@UiField
	Label specialisationLbl;

	@UiField
	DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> specialisationSuggestionBox;

	@UiField
	HorizontalPanel rolePanel;

	@UiField
	Label roleLbl;

	@UiField
	DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> standardizedRoleSuggestionBox;
	
	@UiField
	HorizontalPanel checklistTopicPanel;

	@UiField
	Label checklistTopicLbl;

	@UiField
	DefaultSuggestBox<ChecklistImportPojoValueProxy, EventHandlingValueHolderItem<ChecklistImportPojoValueProxy>> tabSuggestionBox;
	
	@UiField
	IconButton okBtn;

	@UiField
	IconButton cancelBtn;
	
	ChecklistImportTabPopupView checklistImportTabPopupView;

	public ChecklistImportTabPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		setAutoHideEnabled(true);
		specialisationLbl.setText(constants.discipline());
		roleLbl.setText(constants.role());
		checklistTopicLbl.setText(constants.roleTopic());
		okBtn.setText(constants.okBtn());
		cancelBtn.setText(constants.cancel());
		arrowBorder.addClassName("checklist-popup-arrow-border");
		arrow.addClassName("checklist-popup-arrow");
		
		this.specialisationSuggestionBox.setRenderer(new AbstractRenderer<SpecialisationProxy>() {

			@Override
			public String render(SpecialisationProxy object) {
				if(object!=null) {
					return object.getName();
				}
				else {
					return "";
				}
			}
		});
		this.standardizedRoleSuggestionBox.setRenderer(new AbstractRenderer<StandardizedRoleProxy>() {

			@Override
			public String render(StandardizedRoleProxy object) {
				if(object!=null) {
					return object.getShortName();
				}
				else {
					return "";
				}
			}
		});
		this.tabSuggestionBox.setRenderer(new AbstractRenderer<ChecklistImportPojoValueProxy>() {

			@Override
			public String render(ChecklistImportPojoValueProxy object) {
				if(object!=null) {
					return object.getName();
				}
				else {
					return "";
				}
			}
		});
		standardizedRoleSuggestionBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				Log.info("value change handler");	
				StandardizedRoleProxy standardizedRole = standardizedRoleSuggestionBox.getSelected();
				delegate.standardizedRoleSuggectionBoxValueSelected(standardizedRole.getCheckList().getId(), ChecklistImportTabPopupViewImpl.this);
			}
		});
		
		specialisationSuggestionBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				Log.info("specializationLstBox value changed");
				SpecialisationProxy specialisationProxy = specialisationSuggestionBox.getSelected();
				delegate.specialisationSuggectionBoxValueSelected(specialisationProxy.getId(), ChecklistImportTabPopupViewImpl.this);
			}
		});
	}

	public IconButton getOkBtn() {
		return okBtn;
	}

	public IconButton getCancelBtn() {
		return cancelBtn;
	}

	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate=delegate;		
	}

	@Override
	public ChecklistImportTabPopupViewImpl getView() {
		return this;
	}

	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> getSpecialisationSuggestionBox() {
		return specialisationSuggestionBox;
	}
	
	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestionBox() {
		return standardizedRoleSuggestionBox;
	}
	
	public DefaultSuggestBox<ChecklistImportPojoValueProxy, EventHandlingValueHolderItem<ChecklistImportPojoValueProxy>> getTabSuggestionBox() {
		return tabSuggestionBox;
	}
	
	@Override
	public Long getTabId(){
		return tabSuggestionBox.getSelected().getId();
	}
	
	@Override
	public Long getStandardizedRoleId(){
		return standardizedRoleSuggestionBox.getSelected().getId();
	}

}
