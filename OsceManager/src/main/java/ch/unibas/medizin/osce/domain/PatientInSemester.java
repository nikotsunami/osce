package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.domain.Semester;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import ch.unibas.medizin.osce.domain.StandardizedPatient;

@RooJavaBean
@RooToString
@RooEntity
public class PatientInSemester {

	@ManyToOne
	private Semester semester;

	@ManyToOne
	private StandardizedPatient standardizedPatient;

	private Boolean accepted;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<OsceDay> osceDays = new HashSet<OsceDay>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "patientInSemester")
	private Set<PatientInRole> patientInRole = new HashSet<PatientInRole>();

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "patientInSemesters")
	private Set<Training> trainings = new HashSet<Training>();

	private static String selectBase = "SELECT o ";
	private static String selectCountBase = "SELECT COUNT(o) ";
	private static String queryBase = "FROM PatientInSemester AS o WHERE o.standardizedPatient.id In ( ";
	private static String semesterCriteriaQuery = " ) and semester.id = :semesterId";

	public static List<PatientInSemester> findPatientInSemesterByAdvancedCriteria(
			Long semesterId, List<AdvancedSearchCriteria> searchCriteria) {

		EntityManager em = entityManager();
		String stanardizedPatientString = getStanardizedPatientIDList(searchCriteria);
		if (stanardizedPatientString == null) {
			Log.info("Return as null");
			return null;
		}
		TypedQuery<PatientInSemester> query = em.createQuery(selectBase
				+ queryBase + stanardizedPatientString + semesterCriteriaQuery,
				PatientInSemester.class);
		query.setParameter("semesterId", semesterId);

		Log.info("!!!!! Query is : " + selectBase + queryBase
				+ stanardizedPatientString + semesterCriteriaQuery
				+ semesterCriteriaQuery);
		List<PatientInSemester> resultList = query.getResultList();
		if (resultList == null || resultList.size() == 0)
			return null;

		Log.info("Size of PatientInSemester , for advanced search is : "
				+ resultList.size());

		return resultList;
	}

	public static Long countPatientinSemesterByAdvancedCriteria(
			Long semesterId, List<AdvancedSearchCriteria> searchCriteria) {

		String stanardizedPatientString = getStanardizedPatientIDList(searchCriteria);
		if (stanardizedPatientString == null) {
			Log.info("Return as null");
			return null;
		}
		EntityManager em = entityManager();
		TypedQuery<Long> query = em.createQuery(selectCountBase + queryBase
				+ stanardizedPatientString + semesterCriteriaQuery, Long.class);
		query.setParameter("semesterId", semesterId);

		return query.getSingleResult();

	}

	private static String getStanardizedPatientIDList(
			List<AdvancedSearchCriteria> searchCriteria) {

		List<StandardizedPatient> standardizedPatientList = StandardizedPatient
				.findPatientsByAdvancedCriteria(searchCriteria);

		if (standardizedPatientList == null
				|| standardizedPatientList.size() == 0) {
			Log.info("Return as null");
			return null;
		}
		Iterator<StandardizedPatient> standardizedPatientIterator = standardizedPatientList
				.iterator();
		StringBuilder standardizedPatientId = new StringBuilder();
		Log.info("Size of standardizedPatientList is : "
				+ standardizedPatientList.size());

		while (standardizedPatientIterator.hasNext()) {
			StandardizedPatient standardizedPatient = (StandardizedPatient) standardizedPatientIterator
					.next();

			standardizedPatientId
					.append(standardizedPatient.getId().toString());
			if (standardizedPatientIterator.hasNext()) {
				standardizedPatientId.append(" ,");
			}

		}

		return standardizedPatientId.toString();
	}
}
