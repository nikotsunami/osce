package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.unibas.medizin.osce.client.a_nonroo.client.ui.renderer.EnumRenderer;
import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import ch.unibas.medizin.osce.shared.StudyYears;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class RoleAddPopupViewImpl extends PopupPanel implements RoleAddPopupView{

	private static final Binder BINDER = GWT.create(Binder.class);

	interface Binder extends
			UiBinder<Widget, RoleAddPopupViewImpl> {
	}
	
	public Delegate delegate;
	
	@UiField
	TextBox topicName;
	
	@UiField(provided = true)
	ListBox slots_till_change=new ListBox();
	
	@UiField(provided = true)
    ValueListBox<StudyYears> studyYearBox = new ValueListBox<StudyYears>(new EnumRenderer<StudyYears>());
	
	@UiField
	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> SpecialisationBox;
	
	@UiField
	public IconButton newButton;
	
	private final OsceConstants constants = GWT.create(OsceConstants.class);
	
	public RoleAddPopupViewImpl() {
		super(true);
		add(BINDER.createAndBindUi(this));
		
		for(int i=1;i<=20;i++)
		{
			slots_till_change.addItem(i+"");
		}
		
		studyYearBox.setValue(StudyYears.values()[0]);
		studyYearBox.setAcceptableValues(Arrays.asList(StudyYears.values()));
		
		newButton.setText(constants.save());
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		this.delegate = delegate;
	}

	
	public TextBox getTopicName() {
		return topicName;
	}

	public void setTopicName(TextBox topicName) {
		this.topicName = topicName;
	}

	public ListBox getSlots_till_change() {
		return slots_till_change;
	}

	public void setSlots_till_change(ListBox slots_till_change) {
		this.slots_till_change = slots_till_change;
	}

	public ValueListBox<StudyYears> getStudyYearBox() {
		return studyYearBox;
	}

	public void setStudyYearBox(ValueListBox<StudyYears> studyYearBox) {
		this.studyYearBox = studyYearBox;
	}

	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> getSpecialisationBox() {
		return SpecialisationBox;
	}	

	public void setSpecialisationBoxValues(List<SpecialisationProxy> values) {
		boolean areValuesValid = (values != null) && (values.size() > 0);
		
		if (!areValuesValid) {			
			return;
		}
		
		List<Object> objectValue=new ArrayList<Object>();
		objectValue.addAll(values);
	
	
		DefaultSuggestOracle<SpecialisationProxy> suggestOracle1 = (DefaultSuggestOracle<SpecialisationProxy>) SpecialisationBox.getSuggestOracle();
		suggestOracle1.setPossiblilities(values);
		SpecialisationBox.setSuggestOracle(suggestOracle1);
	
		SpecialisationBox.setRenderer(new AbstractRenderer<SpecialisationProxy>() {
			@Override
			public String render(SpecialisationProxy object) {
			
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
	
		SpecialisationBox.setWidth(120);
	}

	public IconButton getNewButton() {
		return newButton;
	}

	public void setNewButton(IconButton newButton) {
		this.newButton = newButton;
	}
	
	
}
