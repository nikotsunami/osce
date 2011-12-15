package ch.unibas.medizin.osce.client.a_nonroo.client.ui.sp;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.ValueListBox;

import ch.unibas.medizin.osce.client.managed.request.LangSkillProxy;

public interface StandardizedPatientLangSkillSubView {
	
	interface Delegate {
		void deleteLangSkillClicked(LangSkillProxy langSkill);
		void addLangSkillClicked();
	}
	
	CellTable<LangSkillProxy> getLangSkillTable();
	ValueListBox<LangSkillProxy> getLanguageBox();
	ValueListBox<LangSkillProxy> getLangSkillBox(); 
    
	void setDelegate(Delegate delegate);
}
