package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.CheckListProxy;
import ch.unibas.medizin.osce.client.managed.request.ChecklistItemProxy;
import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;
import ch.unibas.medizin.osce.shared.OsceChecklistQuestionPojo;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = OsceChecklistQuestionPojo.class)
public interface OsceChecklistQuestionPojoValueProxy  extends ValueProxy{
	
	
	public List<ChecklistItemProxy> getQuestion();

	public OsceProxy getOsce();
	
	public SemesterProxy getSemester();
	
	public CheckListProxy getCheckList();
	
}
