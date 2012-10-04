package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;


import ch.unibas.medizin.osce.client.managed.request.ScarProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentOscesProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.domain.StudentOsces;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(StudentOsces.class)
public interface StudentOsceRequestNonRoo extends RequestContext{
	
	abstract Request<List<StudentOscesProxy>> findStudentOsceByOsce(Long i);
	abstract Request<Long> countStudentByName(String name,Long id);
	
	abstract Request<List<StudentOscesProxy>> findStudentEntriesByName(String name, Long id, int firstResult, int maxResults);
	
	abstract Request<List<StudentOscesProxy>> findStudentEntriesByNameTest(String name,Long id);
	
	abstract Request<List<StudentOscesProxy>> findStudentByRange(int start, int max, Long id, String name);
	
	abstract Request<Integer> countStudentByRange(Long id, String name);
}
