package ch.unibas.medizin.osce.domain;

import java.util.Iterator;
import java.util.List;

import javax.persistence.ManyToOne;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

//import ch.unibas.medizin.osce.domain.StandardizedRole;

@RooJavaBean
@RooToString
@RooEntity
public class PatientInRole {

	@ManyToOne
	private PatientInSemester patientInSemester;

	// @ManyToOne
	// private StandardizedRole standardizedRole;

	@ManyToOne
	private OscePost oscePost;

	Boolean fit_criteria;
	Boolean is_backup;
	
	
	private static List<PatientInRole> getPatientIRoleList(Long osceId) {

		List<PatientInRole> patientInRoleList = findAllPatientInRoles();
//		System.out.println("osceId : " + osceId);
//		System.out.println("patientInRoleList size" + patientInRoleList.size());

		for (Iterator<PatientInRole> iterator = patientInRoleList.iterator(); iterator
				.hasNext();) {
			PatientInRole patientInRole = (PatientInRole) iterator.next();

			// System.out.println("~~~~ .getOsce().getId() : "+patientInRole.getOscePost().getOsceSequence().getOsceDay().getOsce().getId()
			// );
			// System.out.println("");

			if (patientInRole.getOscePost().getOsceSequence().getOsceDay()
					.getOsce().getId().longValue() == osceId.longValue()) {
				// System.out.println("\n\n Removed PIR is : "+patientInRole.getId());
				patientInRole.remove();

			}

		}

		return null;
		
//		
//		
//		 osce.getOsce_days()
//		 
//		 OsceDay osceDay;
//		 osceDay.getOsceSequences()
//		
//		List<PatientInSemester> resultList =
//
//		List<PatientInSemester> PISList = new ArrayList<PatientInSemester>();		
//
//		if (resultList == null || resultList.size() == 0)
//			return null;
//		else {
//			for (Iterator<PatientInSemester> iterator = resultList.iterator(); iterator
//					.hasNext();) {
//				PatientInSemester patientInSemester = (PatientInSemester) iterator
//						.next();
//				List<PatientInRole> returnList = new ArrayList<PatientInRole>();
//				System.out.println("Selected OsceDay iD is :"+osceId);
//				
//				if (patientInSemester.getPatientInRole() != null) {
//					Set<PatientInRole> patientInRoles = patientInSemester.getPatientInRole();
//					for (Iterator<PatientInRole> pIRiterator = patientInRoles.iterator(); pIRiterator
//							.hasNext();) {
//						PatientInRole patientInRole = (PatientInRole) pIRiterator.next();
//						OsceDay osceDay=  patientInRole.getOscePost().getOsceSequence().getOsceDay();
//						
//						if (osceDay.getOsce().getId().longValue() == osceId.longValue()) {
////							patientInSemester.getPatientInRole().remov				
//							returnList.add(patientInRole);
//						}
//					}
//
//					if (returnList.size() > 0) {
//						PISList.add(patientInSemester);
//						patientInSemester.getPatientInRole().removeAll(
//								returnList);
//						em.persist(patientInSemester);
//					}	
//				}					
//			}
//			return PISList;
//		}
	}

	public static Integer removePatientInRoleByOSCE(Long osceId) {
		
//		EntityManager em = entityManager();
//		List<PatientInSemester> resultList = 
		getPatientIRoleList(osceId);
//		if (resultList == null) {
//			Log.info("Return as null");
//			return 0;
//		}
//		else{
//			for (Iterator<PatientInSemester> iterator = resultList.iterator(); iterator.hasNext();) {
//				PatientInSemester patientInSemester = (PatientInSemester) iterator
//						.next();
//				Log.info("Remove id: "+patientInSemester.getId());
//				
//				em.remove(patientInSemester);
//				em.clear();				
//				Log.info("Remove : "+patientInSemester.standardizedPatient.getName());
//			}
//			
//		}
//
//		Log.info("Size of PatientInSemester , for advanced search is : "
//				+ resultList.size());
		return 0;

	}
	
   
 
}
