package ch.unibas.medizin.osce.shared;

import java.util.List;

import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistItem;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.Semester;

public class OsceChecklistQuestionPojo {

	
	public Osce osce;
	
	public List<ChecklistItem> question;
	
	public CheckList CheckList;
	
	public CheckList getCheckList() {
		return CheckList;
	}

	public void setCheckList(CheckList checkList) {
		CheckList = checkList;
	}

	public Semester semester;
	

	public Osce getOsce() {
		return osce;
	}

	public void setOsce(Osce osce) {
		this.osce = osce;
	}

	public List<ChecklistItem> getQuestion() {
		return question;
	}

	public void setQuestion(List<ChecklistItem> question) {
		this.question = question;
	}
	
	public Semester getSemester() {
		return semester;
	}
	
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	
	
}
