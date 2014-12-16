package ch.unibas.medizin.osce.shared.scaffold;

import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;
import ch.unibas.medizin.osce.client.managed.request.StudentProxy;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.shared.Sorting;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;

@SuppressWarnings("deprecation")
@Service(Student.class)
public interface StudentRequestNonRoo extends RequestContext{
	

	abstract Request<Long> countStudentByName(String name);
	
	abstract Request<List<StudentProxy>> findStudentEntriesByName(String name, int firstResult, int maxResults);

	// Module10 Create plans
	abstract Request<List<StudentProxy>> findStudentByOsceId(long OsceId);
	// E Module10 Create plans

	//by spec change[
	abstract Request<List<StudentProxy>> findStudnetByAssignment(Long assId);
	//by spec change]

	abstract Request<List<StudentProxy>> getStudents(String sortColumn, Sorting order, Integer firstResult, Integer maxResults,boolean isFirstTime,String searchValue);
	
	abstract Request<Long> getCountOfStudent(String sortCoiumn,Sorting order,String searchValue);
	
	abstract Request<List<OsceProxy>> findOsceBasedOnStudent(Long studentID);
	abstract Request<List<StudentProxy>> findStudentByOsceIdAndCourseId(long oscetID, long courseId);

	abstract Request<Void> updateStudentToSession(List<Long> studId);

}
