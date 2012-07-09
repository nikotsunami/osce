package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.domain.Student;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Student.class)
public interface StudentRequestNonRoo extends RequestContext{
	

	abstract Request<Long> countStudentByName(String name);
	
	abstract Request<List<StudentProxy>> findStudentEntriesByName(String name, int firstResult, int maxResults);

	

}
