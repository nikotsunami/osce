package ch.unibas.medizin.osce.client.a_nonroo.client.ui.role;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.SpecialisationProxy;
import ch.unibas.medizin.osce.client.style.widgets.IconButton;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.StudyYears;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;

public interface RoleAddPopupView extends IsWidget {
	interface Delegate {
		
	}
	
	void setDelegate(Delegate delegate);
	
	public TextBox getTopicName();
	
	public void setTopicName(TextBox topicName);

	public ListBox getSlots_till_change();
	
	public void setSlots_till_change(ListBox slots_till_change);
	
	public ValueListBox<StudyYears> getStudyYearBox();
	
	public void setStudyYearBox(ValueListBox<StudyYears> studyYearBox);
	
	public DefaultSuggestBox<SpecialisationProxy, EventHandlingValueHolderItem<SpecialisationProxy>> getSpecialisationBox();
	
	public void setSpecialisationBoxValues(List<SpecialisationProxy> values);
	
	public IconButton getNewButton();
	
	public void setNewButton(IconButton newButton);
}
