package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;
import ch.unibas.medizin.osce.client.managed.request.SpokenLanguageProxy;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import ch.unibas.medizin.osce.client.style.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;
import ch.unibas.medizin.osce.shared.LangSkillLevel;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.ValueListBox;

public interface StandardizedPatientLangSkillSubView {
	
	interface Delegate {
		void deleteLangSkillClicked(LangSkillProxy langSkill);
		void addLangSkillClicked(SpokenLanguageProxy lang, LangSkillLevel skill);
	}
	
	CellTable<LangSkillProxy> getLangSkillTable();
	ValueListBox<SpokenLanguageProxy> getLanguageBox();
	//Issue # 122 : Replace pull down with autocomplete.
	DefaultSuggestBox<SpokenLanguageProxy, EventHandlingValueHolderItem<SpokenLanguageProxy>> getNewLanguageBox();
	//Issue # 122 : Replace pull down with autocomplete.
	ValueListBox<LangSkillLevel> getLangSkillBox(); 
    
	void setDelegate(Delegate delegate);
	void setLanguagePickerValues(List<SpokenLanguageProxy> values);
	
	// Highlight onViolation
	Map getLanguageSkillMap();
	// E Highlight onViolation
}
