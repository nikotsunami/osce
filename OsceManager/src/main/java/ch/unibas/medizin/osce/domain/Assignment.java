package ch.unibas.medizin.osce.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.hibernate.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.server.util.file.ExcelUtil;
import ch.unibas.medizin.osce.server.util.file.QwtUtil;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.BellAssignmentType;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.RoleTypes;
import ch.unibas.medizin.osce.shared.TimeBell;


@RooJavaBean
@RooToString
@RooEntity(finders = { "findAssignmentsByOscePostRoomAndOsceDayAndTypeAndSequenceNumber" })
public class Assignment {

	private static Logger Log = Logger.getLogger(Assignment.class);
	
    @Enumerated
    private AssignmentTypes type;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date timeStart;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date timeEnd;

    @NotNull
    @ManyToOne
    private OsceDay osceDay;

    @ManyToOne
    private OscePostRoom oscePostRoom;

    @ManyToOne
    private StudentOsces osceStudent;
    
    @ManyToOne
    private Student student;

    @ManyToOne
    private PatientInRole patientInRole;

    @ManyToOne
    private Doctor examiner;

    private Integer sequenceNumber;
    
    private Integer rotationNumber;
    /**
	 * Create new student assignment
	 * @param osceDay day on which this assignment takes place
	 * @param oscePR post-room-assignment
	 * @param studentIndex student which is examined in this assignment
	 * @param startTime time when the assignment starts
	 * @param endTime time when the assignment ends
	 * @param rotation is in which rotation this student is assigned
	 * @return
	 */
	public static Assignment createStudentAssignment(OsceDay osceDay, OscePostRoom oscePR, int studentIndex, Date startTime, Date endTime, int rotation) {
		Assignment ass2 = new Assignment();
		ass2.setType(AssignmentTypes.STUDENT);
		ass2.setOsceDay(osceDay);
		ass2.setSequenceNumber(studentIndex);
		ass2.setTimeStart(startTime);
		ass2.setTimeEnd(endTime);
		ass2.setOscePostRoom(oscePR);
		ass2.setRotationNumber(rotation);
		return ass2;
	}

    public static List<Assignment> retrieveAssignmentsOfTypeSP(Osce osce) {
        Log.info("retrieveAssignmenstOfTypeSP :");
        EntityManager em = entityManager();
        String queryString = "SELECT o FROM Assignment AS o WHERE o.osceDay.osce = :osce AND o.type = :type AND o.oscePostRoom IS NOT NULL";
        TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
        q.setParameter("osce", osce);
        q.setParameter("type", AssignmentTypes.PATIENT);
        List<Assignment> assignmentList = q.getResultList();
        Log.info("retrieveAssignmenstOfTypeSP query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList.size());
        return assignmentList;
    }
    
    public static void updateSequenceNumbersOfTypeSPByTime(int sequenceNumber, Date timeStart, Date timeEnd) {
        EntityManager em = entityManager();
        String queryString = "SELECT o FROM Assignment AS o WHERE ((o.timeStart <= :timeEnd AND :timeStart <= o.timeEnd) OR (o.timeStart = :timeStart AND o.timeEnd = :timeEnd)) AND o.type = :type AND o.oscePostRoom IS NOT NULL";
        TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
        q.setParameter("timeStart", timeStart);
        q.setParameter("timeEnd", timeEnd);
        q.setParameter("type", AssignmentTypes.PATIENT);
        List<Assignment> assignmentList = q.getResultList();
        
        Iterator<Assignment> it = assignmentList.iterator();
        while (it.hasNext()) {
			Assignment assignment = (Assignment) it.next();
			assignment.setSequenceNumber(sequenceNumber);
			assignment.flush();
		}
    }
    
    /**
	 * Clear all SP break assignments (necessary since another run
	 * of the IFS might give different SP allocations and therefore
	 * different SP break assignments).
	 */
    public static void clearSPBreakAssignments(Osce osce) {
    	EntityManager em = entityManager();
    	String queryString = "SELECT o FROM Assignment AS o WHERE o.osceDay.osce = :osce AND o.type = :type AND oscePostRoom IS NULL";
        TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
        q.setParameter("osce", osce);
        q.setParameter("type", AssignmentTypes.PATIENT);
        List<Assignment> assignmentList = q.getResultList();
        
        Iterator<Assignment> it = assignmentList.iterator();
        while (it.hasNext()) {
        	Assignment assignment = (Assignment) it.next();
        	assignment.remove();
        }
    }
    
    //spec[
    public static void clearSPBreakAssignmentsByOsceDay(OsceDay osceDay) {
    	EntityManager em = entityManager();
    	String queryString = "SELECT o FROM Assignment AS o WHERE o.osceDay = :osceDay AND o.type = :type AND oscePostRoom IS NULL";
        TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
        q.setParameter("osceDay", osceDay);
        q.setParameter("type", AssignmentTypes.PATIENT);
        List<Assignment> assignmentList = q.getResultList();
        
        Iterator<Assignment> it = assignmentList.iterator();
        while (it.hasNext()) {
        	Assignment assignment = (Assignment) it.next();
        	assignment.remove();
        }
    }
    //spec]
    
    public static List<Assignment> retrieveAssignmentsOfTypeStudent(Long osceId) {
        EntityManager em = entityManager();
        String queryString = "SELECT o FROM Assignment AS o WHERE o.osceDay.osce.id = :osceId AND o.type = :type";
        TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
        q.setParameter("osceId", osceId);
        q.setParameter("type", AssignmentTypes.STUDENT);
        List<Assignment> assignmentList = q.getResultList();
        Log.info("retrieveAssignmentsOfTypeStudent query String :" + queryString);
        return assignmentList;
    }
    
    public static List<Assignment> retrieveAssignmentsOfTypeSPUniqueTimes(Osce osce) {
        EntityManager em = entityManager();
        String queryString = "SELECT o FROM Assignment AS o WHERE o.osceDay.osce = :osce AND o.type = :type AND o.oscePostRoom.oscePost.oscePostBlueprint.postType = :postType GROUP BY o.timeStart ORDER BY o.timeStart";
        TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
        q.setParameter("osce", osce);
        q.setParameter("postType", PostType.NORMAL);
        q.setParameter("type", AssignmentTypes.PATIENT);
        List<Assignment> assignmentList = q.getResultList();
        return assignmentList;
    }
    
    //spec[
    public static List<Assignment> retrieveAssignmentsOfTypeSPUniqueTimesByOsceDay(OsceDay osceDay) {
        EntityManager em = entityManager();
        String queryString = "SELECT o FROM Assignment AS o WHERE o.osceDay = :osceDay AND o.type = :type AND o.oscePostRoom.oscePost.oscePostBlueprint.postType = :postType GROUP BY o.timeStart ORDER BY o.timeStart";
        TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
        q.setParameter("osceDay", osceDay);
        q.setParameter("postType", PostType.NORMAL);
        q.setParameter("type", AssignmentTypes.PATIENT);
        List<Assignment> assignmentList = q.getResultList();
        return assignmentList;
    }
  //spec]
    
    public Assignment retrieveAssignmentNeighbourOfTypeSP(int neighbour) {
        EntityManager em = Assignment.entityManager();
        String sortOrder = (neighbour == -1 ? "DESC" : "ASC");
        TypedQuery<Assignment> q = em.createQuery("SELECT o FROM Assignment AS o WHERE o.oscePostRoom = :oscePostRoom AND o.osceDay = :osceDay AND o.type = :type ORDER BY o.timeStart " + sortOrder, Assignment.class);
        q.setParameter("oscePostRoom", this.getOscePostRoom());
        q.setParameter("osceDay", this.getOsceDay());
        q.setParameter("type", AssignmentTypes.PATIENT);
        q.setMaxResults(1);
        
        if(q.getResultList().size() == 1) {
        	return q.getSingleResult();
        }
        
        return null;
    }

    public static List<Assignment> retrieveAssignments(Long osceDayId, Long osceSequenceId, Long courseId, Long oscePostId) {
        Log.info("retrieveAssignments :");
        EntityManager em = entityManager();
        String queryString = "SELECT  a FROM Assignment as a where a.osceDay=" + osceDayId + "  and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost=" + oscePostId + " and opr.course=" + courseId + " ) order by a.timeStart asc";
        TypedQuery<Assignment> query = em.createQuery(queryString, Assignment.class);
        List<Assignment> assignmentList = query.getResultList();
        Log.info("retrieveAssignments query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList.size());
        return assignmentList;
    }

    public static List<Assignment> retrieveAssignmenstOfTypeStudent(Long osceDayId, Long osceSequenceId, Long courseId, Long oscePostId) {
    	
    	
    	
        Log.info("retrieveAssignmenstOfTypeStudent :");
        EntityManager em = entityManager();
        //String queryString = "SELECT  a FROM Assignment as a where a.osceDay=" + osceDayId + "  and type=0 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost=" + oscePostId + " and opr.course=" + courseId + " ) order by a.timeStart asc";
        String queryString = "SELECT  a FROM Assignment as a where a.osceDay=" + osceDayId + "  and type=0 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.room in (select rm.room from OscePostRoom as rm where rm.oscePost = " + oscePostId +  " and rm.course= " + courseId + " and rm.version<999) and opr.course=" + courseId + " ) order by a.timeStart asc";
        
        TypedQuery<Assignment> query = em.createQuery(queryString, Assignment.class);
        List<Assignment> assignmentList = query.getResultList();
        Log.info("retrieveAssignmenstOfTypeStudent query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList.size());
        return assignmentList;
    }

    public static List<Assignment> retrieveAssignmenstOfTypeSP(Long osceDayId, Long osceSequenceId, Long courseId, Long oscePostId) {
        Log.info("retrieveAssignmenstOfTypeSP :");
        EntityManager em = entityManager();
        String queryString = "SELECT  a FROM Assignment as a where a.osceDay=" + osceDayId + "  and type=1 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost=" + oscePostId + " and opr.course=" + courseId + " ) order by a.timeStart asc";
        TypedQuery<Assignment> query = em.createQuery(queryString, Assignment.class);
        List<Assignment> assignmentList = query.getResultList();
        Log.info("retrieveAssignmenstOfTypeSP query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList.size());
        return assignmentList;
    }

    public static List<Assignment> retrieveAssignmenstOfTypeExaminer(Long osceDayId, Long osceSequenceId, Long courseId, Long oscePostId) {
        Log.info("retrieveAssignmenstOfTypeExaminer :");
        EntityManager em = entityManager();
        String queryString = "SELECT  a FROM Assignment as a where a.osceDay=" + osceDayId + "  and type=2 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost=" + oscePostId + " and opr.course=" + courseId + " ) order by a.timeStart asc ";
        TypedQuery<Assignment> query = em.createQuery(queryString, Assignment.class);
        List<Assignment> assignmentList = query.getResultList();
        Log.info("retrieveAssignmenstOfTypeExaminer query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList.size());
        return assignmentList;
    }
    
    //retrieve Logical break post
    public static List<Assignment> retrieveAssignmentOfLogicalBreakPost(Long osceDayId,Long osceSequenceId)
    {
    	Log.info("retrieveAssignmentOfLogicalBreakPost :");
    	 EntityManager em = entityManager();
         String queryString = "SELECT  a FROM Assignment as a where a.osceDay=" + osceDayId + "  and type=1 and a.oscePostRoom=null  order by a.timeStart asc ";
         TypedQuery<Assignment> query = em.createQuery(queryString, Assignment.class);
         List<Assignment> assignmentList = query.getResultList();
         Log.info("retrieveAssignmentOfLogicalBreakPost query String :" + queryString);
         Log.info("Assignment List Size :" + assignmentList.size());
         return assignmentList;
    }
    
  //Testing task {

    // Test Case 2
    public static List<Assignment> findAssignmentForTestBasedOnCriteria(Long osceDayId,List<AssignmentTypes> type){
    	Log.info("Inside findAssignmentForTestBasedOnCriteria() ");
    /*	EntityManager em = entityManager();
    	String query="select a from Assignment a where a.osceDay = " + osceDayId+" and a.type In(:osceType) and a.oscePostRoom = "+ postRoomId+" order by a.type, a.oscePostRoom, a.timeStart";
    	TypedQuery<Assignment> q = em.createQuery(query, Assignment.class);
    	q.setParameter("osceType",type==null ? AssignmentTypes.STUDENT+","+AssignmentTypes.PATIENT+","+AssignmentTypes.EXAMINER : type);  */
    	
    	//q.setParameter("postRoomId", postRoomId<=0  ? "a.oscePostRoom": postRoomId);
    	//q.setParameter("courseId", courseId==0 || courseId < 0 ?"a.oscePostRoom.course" : courseId);
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Assignment> criteriaQuery = criteriaBuilder.createQuery(Assignment.class);
		Root<Assignment> from = criteriaQuery.from(Assignment.class);
		CriteriaQuery<Assignment> select = criteriaQuery.select(from);
		
		select.orderBy(criteriaBuilder.asc(from.get("type")), criteriaBuilder.asc(from.get("oscePostRoom")), criteriaBuilder.asc(from.get("timeStart")));
		
		Predicate pre1 = criteriaBuilder.disjunction();
		pre1 = criteriaBuilder.equal(from.get("osceDay"), osceDayId);
		
		/*if (postRoomId > 0)
		{
			Predicate pre2 = criteriaBuilder.disjunction();
			pre2 = criteriaBuilder.equal(from.get("oscePostRoom"), postRoomId);
			pre1 = criteriaBuilder.and(pre1,pre2);
		}*/
		
		if (!type.equals(null) && type.size() > 0)
		{
			Log.info("Is Side When Type is > 0 ");
			Predicate pre3 = criteriaBuilder.disjunction();
			pre3 =from.get("type").in(type);
			pre1 = criteriaBuilder.and(pre1, pre3);
		}
		
		criteriaQuery.where(pre1);
    	
		TypedQuery<Assignment> typedQuery = entityManager().createQuery(select);
		Log.info("~~QUERY : " + typedQuery.unwrap(Query.class).getQueryString());	
		
    	//Log.info("Query is :" +query);
		List<Assignment> assignmentList = typedQuery.getResultList();
		Log.info("~~RESULT SIZE : " + assignmentList.size());
		
    	return assignmentList;
    }
    // Test Case 3
    public static Integer findTotalStudentsBasedOnOsce(Long osceId){
    	
    	Log.info("Inside findTotalStudentsBasedOnOsce() ");
    	EntityManager em = entityManager();
    	String query="select count(a) from Assignment as a,OsceDay as od where od.osce="+osceId+ " and a.osceDay=od.id and a.type = :type";
    	TypedQuery<Long> q = em.createQuery(query, Long.class);
    	q.setParameter("type", AssignmentTypes.STUDENT);
    	Log.info("Query is :" +query);
    	Integer result = q.getSingleResult() != null && q.getSingleResult() != 0 ? q.getSingleResult().intValue() : 0 ;
    	return result;
    }
    // Test Case 4
    public static List<Assignment> findAssignmentBasedOnOsceDay(Long osceDayId){
    	
    	Log.info("Inside findAssignmentBasedOnOsceDay() ");
    	EntityManager em = entityManager();
    	String query="select a from Assignment a where a.osceDay="+osceDayId + " order by a.type,a.oscePostRoom,a.timeStart";
    	TypedQuery<Assignment> q = em.createQuery(query, Assignment.class);
    	Log.info("Query is :" +query + "Result Size" + q.getResultList().size());
    	return q.getResultList();
    }
    // Test case 5 
    public static List<Course> findParcoursForOsce(Long osceId){
    	
    	Log.info("Inside findParcoursForOsce() With Osce IS "+ osceId);
    	EntityManager em = entityManager();
    	String query="select c from Course as c,OsceSequence as os, OsceDay as od where od.osce="+osceId + " and os.osceDay=od.id and c.osceSequence=os.id" ;
    	TypedQuery<Course> q = em.createQuery(query, Course.class);
    	Log.info("Query is :" +query);
    	return q.getResultList();
    }
    public static List<OscePostBlueprint>findOscePostBluePrintForOsceWithTypePreparation(Long osceId){

    	Log.info("Inside findOscePostBluePrintForOsceWithTypePreparation() With Osce IS "+ osceId);
    	EntityManager em = entityManager();
    	String query="select opb from OscePostBlueprint as opb where opb.osce="+osceId + " and opb.postType=3 order by opb.sequenceNumber";
    	TypedQuery<OscePostBlueprint> q = em.createQuery(query, OscePostBlueprint.class);
    	Log.info("Query is :" +query);
    	return q.getResultList();
    }
    
    public static List<Assignment> findAssignmentBasedOnGivenCourseAndPost(Long courseId,Long bluePrintId){
    	
    	Log.info("Inside findAssignmentBasedOnGivenCourseAndPost() With course Id IS "+ courseId + " and bluePrint Id Is :" + bluePrintId);
    	EntityManager em = entityManager();
    	String query="select a from Assignment as a where a.type=0 and a.oscePostRoom IN (select opr.id from OscePostRoom as opr where opr.course=" + courseId + 
    			" and opr.oscePost IN(select op.id from OscePost as op where op.oscePostBlueprint="+bluePrintId +")) order by a.sequenceNumber";
    	TypedQuery<Assignment> q = em.createQuery(query, Assignment.class);
    	Log.info("Query is :" +query);
    	return q.getResultList();
    }
   
    // Test Case 6
    public static List<OscePostBlueprint> findOscePostBluePrintForOsce(Long osceId){
    	
    	Log.info("Inside findOscePostBluePrintForOsce() With Osce IS "+ osceId);
    	EntityManager em = entityManager();
    	String query="select opb from OscePostBlueprint as opb where opb.osce="+osceId + " and opb.postType=2 order by opb.sequenceNumber";
    	TypedQuery<OscePostBlueprint> q = em.createQuery(query, OscePostBlueprint.class);
    	Log.info("Query is :" +query);
    	Log.info("Result is :" + q.getResultList().size());
    	return q.getResultList();
    }
    
    public static OscePostRoom findRoomForCourseAndBluePrint(Long courseId,Long blueprintId){
    	
    	Log.info("Inside findRoomForCourseAndBluePrint() With course Id IS "+ courseId + " and bluePrint Id Is " + blueprintId);
    	EntityManager em = entityManager();
    	String query="select opr from OscePostRoom as opr,OscePost as op where op.oscePostBlueprint="+blueprintId + " and opr.oscePost=op.id and opr.course="+courseId ;
    	TypedQuery<OscePostRoom> q = em.createQuery(query, OscePostRoom.class);
    	Log.info("Query is :" +query);
    	return q.getSingleResult();
    }
    
    public static List<Integer> findAllSequenceNumberForAssignment(Long osceDayId){
    	Log.info(" In side findAllSequenceNumberForAssignment() with OsceDay id" + osceDayId);	
		EntityManager em = entityManager();		
		String queryString = "select distinct sequenceNumber from Assignment where osceDay= "+osceDayId;
		Log.info("Query String: " + queryString);
		TypedQuery<Integer> q = em.createQuery(queryString,Integer.class);		
		List<Integer> result  = q.getResultList();        
		Log.info("findAllSequenceNumberForAssignment List Size is :"+result.size());
        return result;
    }
    
    // Test Case 7
    public static List<Assignment> findAssignmtForOsceDayAndSeq(Integer studentSeqNo,Long osceDayId){
    	
    	Log.info("Inside findAssignmentsByOsceDayAndSequenceNo() ");
    	EntityManager em = entityManager();
    	String query="select a from Assignment as a where a.osceDay="+osceDayId + " and a.type=0 and a.sequenceNumber="+studentSeqNo +" order by a.timeStart";
    	TypedQuery<Assignment> q = em.createQuery(query, Assignment.class);
    	Log.info("Query is :" +query);
    	return q.getResultList();
    }
    
    //Testing task }
    
 // Module10 Create plans
    //Find  Assignemtn by SP Id and Semester Id
    public static List<Assignment> findAssignmentsBySPIdandSemesterId(long spId,long semId,long pirId)
    {
		Log.info("Call findAssignmentBySPIdandSemesterId for SP id" + spId + "for Semester" +semId);	
		EntityManager em = entityManager();
		/*select * from assignment where patient_in_role in (
	    		select patient_in_role.id from patient_in_role where patient_in_semester in (select patient_in_semester.id from patient_in_semester,standardized_patient 
	    		where  patient_in_semester.standardized_patient=standardized_patient.id
	    		       and standardized_patient.id=19
	    		       and patient_in_semester.semester=1));*/
		String queryString = "select assi from Assignment assi where assi.patientInRole in (select pir.id from PatientInRole pir where pir.patientInSemester in " +
				"(select pis.id from PatientInSemester pis, StandardizedPatient sp where pis.standardizedPatient=sp.id and sp.id="+spId+" and pis.semester="+semId+")) and assi.patientInRole="+pirId;
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Assignment> findAssignmentsByOsceDayAndPatientInRole(long osceDayId,long patientInrRoleId)
    {
		Log.info("Call findAssignmentsByOsceDayAndPatientInRole for OsceDay id" + osceDayId + "for Patient_In_Role" +patientInrRoleId);	
		EntityManager em = entityManager();
		/*select * from assignment where patient_in_role in (
	    		select patient_in_role.id from patient_in_role where patient_in_semester in (select patient_in_semester.id from patient_in_semester,standardized_patient 
	    		where  patient_in_semester.standardized_patient=standardized_patient.id
	    		       and standardized_patient.id=19
	    		       and patient_in_semester.semester=1));*/
		String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + "and patientInRole= " + patientInrRoleId;
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Long> findDistinctOsceDayByStudentId(long osceId, long studId)
    {
    	Log.info("Call findDistinctOsceDayByStudentId for osce Id: "+osceId +"Student id: " + studId);	
		EntityManager em = entityManager();		
		String queryString = "select distinct osceDay.id from Assignment where osceDay in (select distinct od.id from OsceDay as od where od.osce="+osceId + ") and student="+studId+" order by osceDay";		
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		List<Long> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Assignment> findAssignmentsByOsceDayAndStudent(long osceDayId,long studentId)
    {
		Log.info("Call findAssignmentsByOsceDayAndStudent for OsceDay id" + osceDayId + "for Student" +studentId);	
		EntityManager em = entityManager();		
		//[SPEC String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + "and student= " + studentId;
		String queryString = "select assi from Assignment assi where assi.osceDay="+osceDayId +" and student= " + studentId + " and assi.type=0 order by assi.timeStart";
		
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Long> findDistinctOsceDayByExaminerId(long examinerId,long osceId)
    {
    	Log.info("Call findDistinctOsceDayByExaminerId for Student id" + examinerId + " OsceId: " + osceId);	
		EntityManager em = entityManager();		
		//String queryString = "select distinct osceDay.id from Assignment where examiner="+examinerId;
		 
		String queryString = "select distinct osceDay.id from Assignment where osceDay in (select distinct od.id from OsceDay as od where od.osce="+osceId + ") and examiner="+examinerId+" order by osceDay";
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		List<Long> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    /*
    public static List<Assignment> findAssignmentsByOsceDayAndExaminer(long osceDayId,long examinerId)
    {
		Log.info("Call findAssignmentsByOsceDayAndStudent for OsceDay id" + osceDayId + "for Student" +examinerId);	
		EntityManager em = entityManager();		
		String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + "and examiner= " + examinerId;
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }*/
    
    public static List<Long> findDistinctPIRByOsceDayAndExaminer(long osceDayId,long examinerId)
    {
		Log.info("Call findDistinctPIRByOsceDayAndExaminer for OsceDay id" + osceDayId + "for Student" +examinerId);	
		EntityManager em = entityManager();		
		//select distinct patient_in_role from assignment where osce_day=1 and examiner=5;
		String queryString = "select distinct patientInRole.id from Assignment where osceDay= "+osceDayId + "and examiner= " + examinerId;
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		List<Long> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Assignment> findAssignmentsByOsceDayExaminerAndPIR(long osceDayId,long examinerId,long pirId)
    {
		Log.info("Call findAssignmentsByOsceDayExaminerAndPIR for OsceDay id" + osceDayId + "for Examiner" +examinerId + "PIR Id " + pirId);	
		EntityManager em = entityManager();		
		String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.patientInRole= "+ pirId;
		//String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.oscePostRoom= "+ pirId;
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Assignment> findAssignmentsByOsceDayExaminerAndOscePostRoomId(long osceDayId,long examinerId,long oscePostRoomId)
    {
		Log.info("Call findAssignmentsByOsceDayExaminerAndPIR for OsceDay id" + osceDayId + "for Examiner" +examinerId + "oscePostRoom Id " + oscePostRoomId);	
		EntityManager em = entityManager();		
		//String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.patientInRole= "+ pirId;
		String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.oscePostRoom= "+ oscePostRoomId;
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Long> findDistinctoscePostRoomByOsceDayAndExaminer(long osceDayId,long examinerId)
    {
		Log.info("Call findDistinctoscePostRoomByOsceDayAndExaminer for OsceDay id" + osceDayId + "for Student" +examinerId);	
		EntityManager em = entityManager();		
		//select distinct patient_in_role from assignment where osce_day=1 and examiner=5;
		String queryString = "select distinct oscePostRoom.id from Assignment where osceDay= "+osceDayId + "and examiner= " + examinerId;
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		List<Long> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Long> findDistinctOsceDayIdByPatientInRoleId(long pirId)
    {
    	Log.info("Call findDistinctOsceDayByPatientInRoleIdId for Student id" + pirId);	
		EntityManager em = entityManager();		
		String queryString = "select distinct osceDay.id from Assignment where patientInRole="+pirId;
		Log.info("Query String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString,Long.class);		
		List<Long> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    public static List<Assignment> findAssignmentsByOsceDayAndPIRId(long osceDayId,long pirId)
    {
		Log.info("Call findAssignmentsByOsceDayAndPIRId for OsceDay id " + osceDayId +" PIR Id " + pirId);	
		EntityManager em = entityManager();		
		String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId +" and assi.patientInRole= "+ pirId;
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
        
    // E Module10 Create plans    
    
    //Module 9
    
    public static List<StandardizedPatient> findAssignedSP(Long semesterId)  
   	{
   		EntityManager em = entityManager();
   		
   		String queryString="select distinct sp ";
   		queryString += "from Assignment a, StandardizedPatient sp, PatientInSemester pis, PatientInRole pir ";
   		queryString += "where pis.semester = "+semesterId+" ";
   		queryString += "and pis.id = pir.patientInSemester ";
   		queryString += "and pir.id = a.patientInRole ";
   		queryString += "and sp.id=pis.standardizedPatient ";
   		
   		TypedQuery<StandardizedPatient> q = em.createQuery(queryString, StandardizedPatient.class);
   		List<StandardizedPatient> result = q.getResultList();
   		return result;
   	}
    
    public static List<Doctor> findAssignedExaminer(Long semesterId)  
   	{
   		EntityManager em = entityManager();
   		
   		String queryString="select distinct d ";
   		queryString += "from Assignment a, Osce o, OsceDay od, Doctor d ";
   		queryString += "where o.semester = "+semesterId+" ";
   		queryString += "and o.id = od.osce ";
   		queryString += "and od.id = a.osceDay ";
   		queryString += "and d.id = a.examiner ";
   		
   		TypedQuery<Doctor> q = em.createQuery(queryString, Doctor.class);
   		List<Doctor> result = q.getResultList();
   		return result;
   	}
    
    // Module 9
    
	// Module : 15

	public static List<Assignment> getAssignmentsBySemester(Long semesterId) {
		Log.info("retrieveAssignmenstOfTypeExaminer :");
		EntityManager em = entityManager();

		List<Assignment> assignmentList = new ArrayList<Assignment>();

		List<OsceDay> osceDays = OsceDay.findAllOsceDaysOrderByDate();

		OsceDay osceDay = null;
		OsceSequence osceSequence = null;
		OscePost oscePost = null;
		Course course = null;

		for (Iterator<OsceDay> iterator = osceDays.iterator(); iterator
				.hasNext();) {

			osceDay = (OsceDay) iterator.next();

			List<OsceSequence> osceSequences = osceDay.getOsceSequences();

			if (osceSequences != null && osceSequences.size() > 0) {
				osceSequence = osceSequences.get(0);

				List<OscePost> oscePosts = osceSequence.getOscePosts();

				if (oscePosts != null && oscePosts.size() > 0) {
					oscePost = oscePosts.get(0);
				}

				List<Course> courses = osceSequence.getCourses();

				if (courses != null && courses.size() > 0) {
					course = courses.get(0);
				}

			}

			// SELECT o.osce_date,a.* FROM osce.assignment a ,osce.osce_day o
			// where
			// osce_day=1 and a.type=0 and a.osce_day = o.id and
			// a.osce_post_room
			// in(select opr.id from osce_post_room opr where osce_post=1 and
			// course=1 ) order by a.osce_day asc,o.osce_date asc,a.time_start
			// asc ;
			// and op.osce_sequence=1;

			String queryString = "SELECT a FROM Assignment as a where a.type=0 and a.osceDay.osce.semester.id="
					+ semesterId
					+ " and a.osceDay.id="
					+ osceDay.getId()
					+ "  and a.oscePostRoom.id in(select opr.id from OscePostRoom opr where opr.oscePost.id="
					+ oscePost.getId()
					+ " and opr.course.id="
					+ course.getId()
					+ " ) order by a.osceDay asc,a.osceDay.osceDate asc,a.timeStart asc ";
			TypedQuery<Assignment> query = em.createQuery(queryString,
					Assignment.class);

			assignmentList.addAll(query.getResultList());

			Log.info("retrieveAssignmenstOfTypeExaminer query String :"
					+ queryString);
			Log.info("Assignment List Size :" + assignmentList.size());
		}
		// return bellAssignmentTypes;
		return assignmentList;
	}

	public static Integer getCountAssignmentsBySemester(Long semesterId) {
		return new Integer(getAssignmentsBySemester(semesterId).size());
	}

	public static String getQwtBellSchedule(// List<Assignment> assignments,
			Long semesterId, Integer time, TimeBell isPlusTime) {
		try {
			List<Assignment> assignments = getAssignmentsBySemester(semesterId);
			Semester semester = Semester.findSemester(semesterId);

			QwtUtil qwtUtil = new QwtUtil();

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();

			String fileName = new String(dateFormat.format(date) + ".qwt");
			//Feature : 154
			qwtUtil.open(StandardizedPatient.fetchRealPath() + fileName, false);

			List<BellAssignmentType> bellAssignmentTypes = QwtUtil
					.getBellAssignmentType(assignments, time, isPlusTime,
							semester);
			qwtUtil.writeQwt(bellAssignmentTypes);

			// qwtUtil.writeQwt(assignments);

			qwtUtil.close();
			//Feature : 154
			return StandardizedPatient.fetchContextPath() + fileName;

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return OsMaConstant.FILENAME;
		return "";
	}

	// Module : 15
	
		public static List<Assignment> findAssignmentByOscePostRoom(Long id, Long osceId, int rotationNumber)
    {
    	EntityManager em = entityManager();
    	String query = "SELECT a FROM Assignment a WHERE a.oscePostRoom.id = " + id + " AND a.type = 0 AND a.osceDay.osce.id = " + osceId + " and a.rotationNumber = " + rotationNumber + " ORDER BY a.timeStart";
    	TypedQuery<Assignment> q = em.createQuery(query, Assignment.class);
    	return q.getResultList();
    }
    
    public static List<Assignment> findAssignmentExamnierByOscePostRoom(Long id, Long osceId)
    {
    	EntityManager em = entityManager();
    	//String query = "SELECT a FROM Assignment a WHERE a.oscePostRoom.id = " + id + " AND a.type = 2 AND a.timeStart > '" + time_start + "' AND timeStart < '" + time_end +"' ORDER BY timeStart";
    	String query = "SELECT a FROM Assignment a WHERE a.oscePostRoom.id = " + id + " AND a.type = 2 AND a.osceDay.osce.id = " + osceId + " ORDER BY a.timeStart";
    	//System.out.println("EXAMINER QUERY : " + query);
    	TypedQuery<Assignment> q = em.createQuery(query, Assignment.class);
    	return q.getResultList();
    }
    
    public static List<Assignment> findAssignedDoctorBySpecialisation(Long specialisationId, Long clinicId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT a FROM Assignment AS a WHERE a.examiner.specialisation = " + specialisationId + " AND a.examiner.clinic = " + clinicId + " GROUP BY a.examiner";
    	TypedQuery<Assignment> q = em.createQuery(sql, Assignment.class);
    	return q.getResultList();
    }
    
    public static List<Assignment> findAssignmentsByOsceDayExaminer(long osceDayId,long examinerId)
    {
		Log.info("Call findAssignmentsByOsceDayExaminerAndPIR for OsceDay id" + osceDayId + "for Examiner" +examinerId);	
		EntityManager em = entityManager();		
		//String queryString = "select assi from Assignment assi where assi.osceDay in (select distinct od.id from OsceDay as od where od.osce="+osceId + ") and student= " + studentId + " and assi.type=0 order by assi.timeStart";
		String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.type=2 order by assi.timeStart";
		//String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.oscePostRoom= "+ pirId;
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    
    public static List<Assignment> findAssignmentBasedOnGivenOsceDayExaminerAndOscePostRoom(long osceDayId,long examinerId,long oscePostRoomId)
    {
		Log.info("Call findAssignmentBasedOnGivenOsceDayExaminerAndOscePostRoom for OsceDay id" + osceDayId + "for Examiner" +examinerId +"and OscePostRoom Id: " + oscePostRoomId);	
		EntityManager em = entityManager();		 
		//String queryString = "select assi from Assignment assi where assi.osceDay in (select distinct od.id from OsceDay as od where od.osce="+osceId + ") and student= " + studentId + " and assi.type=0 order by assi.timeStart";
		//String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.type=2 order by assi.timeStart";
		//String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.oscePostRoom= "+ pirId;
		String queryString = "select assi from Assignment assi where assi.osceDay= "+osceDayId + " and assi.examiner= " + examinerId +" and assi.oscePostRoom="+oscePostRoomId+" and assi.type=2 order by assi.timeStart";
		//select * from assignment where osce_day=97 and examiner=61 and osce_post_room=3121 and type=2;
		Log.info("Query String: " + queryString);
		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
		List<Assignment> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
        return result;    	    
    }
    
    //by spec[
    public static void updateAssignmentByDiff(Long osceDayId, int diff, Date endTimeSlot)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT a FROM Assignment AS a WHERE a.osceDay = " + osceDayId +" AND a.timeEnd > :endTimeSlot";
    	TypedQuery<Assignment> q = em.createQuery(sql, Assignment.class);
    	q.setParameter("endTimeSlot", endTimeSlot);
    	Iterator<Assignment> assList = q.getResultList().iterator();
    	
    	while (assList.hasNext())
    	{
    		Assignment ass = assList.next();
    		
    		Date timeStartDt = dateAddMin(ass.getTimeStart(), diff);
    		Date timeEndDt = dateAddMin(ass.getTimeEnd(), diff);
    		
    		ass.setTimeStart(timeStartDt);
    		ass.setTimeEnd(timeEndDt);
    		
    		ass.persist();    		
    	}
    	
    	OsceDay osceDay=OsceDay.findOsceDay(osceDayId);
    	osceDay.setIsTimeSlotShifted(true);
    	osceDay.persist();
    	
    }
    
    public static Date dateAddMin(Date date, long minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}
    
    public static boolean isSPinOsceDay(PatientInSemester ps, Assignment assignment)
 	{
     	Log.info("Call isSPinOsceDay for Patient in Semester id" + ps.getId() + " of assignment id " + assignment.getId());	
     	boolean flag = false;
     	EntityManager em = entityManager();		 
 		String queryString = "select assi from Assignment assi where assi.type=1 and assi.osceDay= " + assignment.getOsceDay().getId() + " and assi.patientInRole in ( select id from PatientInRole pr where pr.patientInSemester = " + ps.getId() +")";
 		//select * from assignment where osce_day=97 and examiner=61 and osce_post_room=3121 and type=2;
 		Log.info("Query String: " + queryString);
 		TypedQuery<Assignment> q = em.createQuery(queryString,Assignment.class);		
 		List<Assignment> result  = q.getResultList();		
 		if(result!=null && result.size()>0)
 			flag=true;
 		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result.size());
         return flag; 
 	}
    
    public static List<Assignment> retrieveAssignmentsOfTypeSPByOsceDay(OsceDay osceDay) {
        Log.info("retrieveAssignmenstOfTypeSP :");
        EntityManager em = entityManager();
        String queryString = "SELECT o FROM Assignment AS o WHERE o.osceDay = :osceDay AND o.type = :type AND o.oscePostRoom IS NOT NULL";
        TypedQuery<Assignment> q = em.createQuery(queryString, Assignment.class);
        q.setParameter("osceDay", osceDay);
        q.setParameter("type", AssignmentTypes.PATIENT);
        List<Assignment> assignmentList = q.getResultList();
        Log.info("retrieveAssignmenstOfTypeSP query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList.size());
        return assignmentList;
    }
    
    
     public static Date minmumStartTime(Long osceDayId, Long osceSequenceId, Long courseId) {
    	
    	
    	
        Log.info("retrieveAssignmenstOfTypeStudent :");
        EntityManager em = entityManager();
        //String queryString = "SELECT  a FROM Assignment as a where a.osceDay=" + osceDayId + "  and type=0 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost=" + oscePostId + " and opr.course=" + courseId + " ) order by a.timeStart asc";
        String queryString = "SELECT  min(a.timeStart) FROM Assignment as a where a.osceDay=" + osceDayId + "  and type=0 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.room in (select rm.room from OscePostRoom as rm where  rm.course= " + courseId + " and rm.version<999) and opr.course=" + courseId + " ) order by a.timeStart asc";
        
        TypedQuery<Date> query = em.createQuery(queryString, Date.class);
        Date assignmentList = query.getResultList().get(0);
        Log.info("retrieveAssignmenstOfTypeStudent query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList);
        return assignmentList;
    }
     
     
    //by spec]
    
     //payment module
     public static String findAssignmentByPatinetInRole(Long semesterId) 
     {
     	String fileName = "";
     	try
     	{
     		ExcelUtil excelUtil = new ExcelUtil();
         	
         	List<Osce> osceList = Osce.findAllOsceBySemster(semesterId);
         	
         	for (Osce osce : osceList)
         	{
         		Map<Long, List<Long>> mainMap = new HashMap<Long, List<Long>>();
         		
         		EntityManager em = entityManager();
             	
             	String sql = "SELECT ps.standardizedPatient.id AS stdpat, a.osceDay AS assOsDay, pr.oscePost AS prOsPt, sr.roleType AS srRoTy, MIN(a.timeStart) AS minTimeSt, MAX(a.timeEnd) AS maxTimeEnd" +
         				" FROM Assignment AS a, PatientInRole AS pr, PatientInSemester AS ps, OscePost AS op, StandardizedRole AS sr " +
         				" WHERE a.type = 1 AND a.osceDay IN (SELECT od FROM OsceDay od WHERE od.osce = " + osce.getId() + ")" +
         				" AND a.patientInRole = pr.id" +
         				" AND pr.patientInSemester = ps.id" +
         				" AND pr.oscePost = op.id " +
         				" AND op.standardizedRole = sr.id" +
         				" GROUP BY ps.standardizedPatient, a.osceDay, pr.oscePost, sr.roleType";
             	
             	javax.persistence.Query query = em.createQuery(sql);
             	List list = query.getResultList();    	
             	
             	Long spHrs = 0l;
         		Long statistHrs = 0l;
             	
         		for (int i=0; i<list.size(); i++)
             	{
             		Object[] custom = (Object[]) list.get(i);
             	
             		Long newSp = (Long)custom[0];
             	            		
             		Date startDate = (Date)custom[4];
             		Date endDate = (Date)custom[5];
             		
             		Long min = (endDate.getTime() - startDate.getTime()) / (60 * 1000);
             		
             		if (checkLunchBreak(startDate, endDate,((OsceDay)custom[1]).getLunchBreakStart()))
             		{
             			min = min - osce.getLunchBreak();
             		}
             		
             		//System.out.println("SP : " + oldSp + "  ~~MIN : " + min);
             		
             		if (((RoleTypes)custom[3]) == RoleTypes.Simpat)
             			spHrs = spHrs + min;
             		else if (((RoleTypes)custom[3]) == RoleTypes.Statist)
             			statistHrs = statistHrs + min;
             		
             		//System.out.println("SPHRS : " + spHrs);
             		if (i == 0)
             		{
             			List<Long> hrsList = new ArrayList<Long>();
             			hrsList.add(spHrs);
             			hrsList.add(statistHrs);
             			
             			mainMap.put(newSp, hrsList);
             			
             			spHrs = 0l;
             			statistHrs = 0l;
             		}
             		
             		if (i > 0)
             		{
             			Object[] oldObject = (Object[]) list.get(i-1);
             			Long id = (Long) oldObject[0];
             			if (!newSp.equals(id))
             			{
             				//System.out.println("SP : " + newSp + "  ~~SP : " + spHrs + " ~~STATIST : " + statistHrs);
             				
             				List<Long> hrsList = new ArrayList<Long>();
                 			hrsList.add(spHrs);
                 			hrsList.add(statistHrs);
                 			
                 			mainMap.put(newSp, hrsList);
                 			
                 			spHrs = 0l;
                 			statistHrs = 0l;
             			}
             		}
             	}
             	
             	excelUtil.writeSheet(mainMap, osce.getName(), semesterId);
             	
         	}
         	
         	Semester semester = Semester.findSemester(semesterId);
         	fileName = semester.getCalYear().toString() + ".xls";
         	excelUtil.writeExcel(fileName);
     	}
     	catch(Exception e)
     	{
     		Log.info("ERROR : " + e.getMessage());
     	}
     	
     	return StandardizedPatient.fetchContextPath() + fileName;
     }
     
     public static boolean checkLunchBreak(Date timeStart, Date timeEnd, Date startLunchBreak)
     {
     	Boolean test = (startLunchBreak.after(timeStart) && startLunchBreak.before(timeEnd));    	
     	return test;	
     }
     //payment module
   
} 
