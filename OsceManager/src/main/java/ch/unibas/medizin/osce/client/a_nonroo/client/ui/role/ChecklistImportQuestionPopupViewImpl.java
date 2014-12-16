package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import ch.unibas.medizin.osce.client.a_nonroo.client.ChecklistImportPojoValueProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
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

public class ChecklistImportQuestionPopupViewImpl  extends PopupPanel implements ChecklistImportQuestionPopupView{

	
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends UiBinder<Widget, ChecklistImportQuestionPopupViewImpl> {
	}

	private OsceConstants constants = GWT.create(OsceConstants.class);

	private Delegate delegate;
	
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
	DefaultSuggestBox<ChecklistItemProxy, EventHandlingValueHolderItem<ChecklistItemProxy>> topicSuggestionBox;

	@UiField
	HorizontalPanel	questionPanel;

	@UiField
	Label	checklistQuestionLbl;

	@UiField
	DefaultSuggestBox<ChecklistImportPojoValueProxy, EventHandlingValueHolderItem<ChecklistImportPojoValueProxy>>	checklistQuestionSuggestionBox;

	@UiField
	IconButton okBtn;

	@UiField
	IconButton cancelBtn;
	
	@UiField
	DivElement arrowBorder;

	@UiField
	DivElement arrow;
	
	
	public ChecklistImportQuestionPopupViewImpl() {

	
		super(true);
		add(BINDER.createAndBindUi(this));
		setAutoHideEnabled(true);
		roleLbl.setText(constants.role());
		checklistTopicLbl.setText(constants.roleTopic());
		checklistQuestionLbl.setText(constants.checklistQuestions());
		arrowBorder.addClassName("checklist-topic-popup-arrow-border");
		arrow.addClassName("checklist-topic-popup-arrow");
		okBtn.setText(constants.okBtn());
		cancelBtn.setText(constants.cancel());
		
		this.standardizedRoleSuggestionBox.setRenderer(new AbstractRenderer<StandardizedRoleProxy>() {

			@Override
			public String render(StandardizedRoleProxy object) {
				if(object!=null)
				{
					return object.getShortName();
				}
				else
				{
					return "";
				}
			}
		});
		
		this.topicSuggestionBox.setRenderer(new AbstractRenderer<ChecklistItemProxy>() {

			@Override
			public String render(ChecklistItemProxy object) {
				if(object!=null)
				{
					return object.getName();
				}
				else
				{
					return "";
				}
			}
		});
		
		this.checklistQuestionSuggestionBox.setRenderer(new AbstractRenderer<ChecklistImportPojoValueProxy>() {

			@Override
			public String render(ChecklistImportPojoValueProxy object) {
				if(object!=null)
				{
					return object.getName();
				}
				else
				{
					return "";
				}
			}
		});
		
		standardizedRoleSuggestionBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				Log.info("value change handler");	
				StandardizedRoleProxy standardizedRole = standardizedRoleSuggestionBox.getSelected();
				delegate.standardizedRoleSuggectionBoxValueSelectedQuestionPopup(standardizedRole.getCheckList().getId(), ChecklistImportQuestionPopupViewImpl.this);
			}
		});
		topicSuggestionBox.addHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				delegate.topicSuggestionBoxValueSelectedQuestionPopup(topicSuggestionBox.getSelected().getId(), ChecklistImportQuestionPopupViewImpl.this);
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
	public ChecklistImportQuestionPopupViewImpl getView() {
		return this;
	}
	
	public DefaultSuggestBox<StandardizedRoleProxy, EventHandlingValueHolderItem<StandardizedRoleProxy>> getStandardizedRoleSuggestionBox() {
		return standardizedRoleSuggestionBox;
	}
	
	public DefaultSuggestBox<ChecklistItemProxy, EventHandlingValueHolderItem<ChecklistItemProxy>> getTopicSuggestionBox() {
		return topicSuggestionBox;
	}

	public DefaultSuggestBox<ChecklistImportPojoValueProxy, EventHandlingValueHolderItem<ChecklistImportPojoValueProxy>> getQuestionSuggestionBox() {
		return checklistQuestionSuggestionBox;
	}
	
	public void setDownArrowStyle() {
		arrowBorder.removeClassName("checklist-popup-arrow-border");
		arrow.removeClassName("checklist-popup-arrow");
		arrowBorder.removeClassName("checklist-topic-popup-arrow-border");
		arrow.removeClassName("checklist-topic-popup-arrow");
		arrowBorder.addClassName("checklist-edit-popup-arrow-border");
		arrow.addClassName("checklist-edit-popup-arrow");
	}

	@Override
	public Long getRoleId(){
		return standardizedRoleSuggestionBox.getSelected().getId();
	}
	
	@Override
	public Long getTopicId(){
		return topicSuggestionBox.getSelected().getId();
	}
	
	@Override
	public Long getQuestionId(){
		return checklistQuestionSuggestionBox.getSelected().getId();
	}
}
