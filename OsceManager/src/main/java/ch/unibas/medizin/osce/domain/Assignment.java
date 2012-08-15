package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.PostType;

import javax.persistence.Enumerated;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.hibernate.Query;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.PostType;

import com.allen_sauer.gwt.log.client.Log;
import ch.unibas.medizin.osce.domain.OsceDay;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.Doctor;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findAssignmentsByOscePostRoomAndOsceDayAndTypeAndSequenceNumber" })
public class Assignment {

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
    private PatientInRole patientInRole;

    @ManyToOne
    private Doctor examiner;

    private Integer sequenceNumber;
    
    /**
	 * Create new student assignment
	 * @param osceDay day on which this assignment takes place
	 * @param oscePR post-room-assignment
	 * @param studentIndex student which is examined in this assignment
	 * @param startTime time when the assignment starts
	 * @param endTime time when the assignment ends
	 * @return
	 */
	public static Assignment createStudentAssignment(OsceDay osceDay, OscePostRoom oscePR, int studentIndex, Date startTime, Date endTime) {
		Assignment ass2 = new Assignment();
		ass2.setType(AssignmentTypes.STUDENT);
		ass2.setOsceDay(osceDay);
		ass2.setSequenceNumber(studentIndex);
		ass2.setTimeStart(startTime);
		ass2.setTimeEnd(endTime);
		ass2.setOscePostRoom(oscePR);
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
        String queryString = "SELECT  a FROM Assignment as a where a.osceDay=" + osceDayId + "  and type=0 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost=" + oscePostId + " and opr.course=" + courseId + " ) order by a.timeStart asc";
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
    
  //Testing task {

    public static List<Assignment> findAssignmentForTestBasedOnCriteria(Long osceDayId,List<AssignmentTypes> type,Long postRoomId){
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
		
		if (postRoomId > 0)
		{
			Predicate pre2 = criteriaBuilder.disjunction();
			pre2 = criteriaBuilder.equal(from.get("oscePostRoom"), postRoomId);
			pre1 = criteriaBuilder.and(pre1,pre2);
		}
		
		if (!type.equals(null))
		{
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
    
    public static List<Assignment> findAssignmentBasedOnOsceDay(Long osceDayId){
    	
    	Log.info("Inside findAssignmentBasedOnOsceDay() ");
    	EntityManager em = entityManager();
    	String query="select a from Assignment a where a.osceDay="+osceDayId + " order by a.type,a.oscePostRoom,a.timeStart";
    	TypedQuery<Assignment> q = em.createQuery(query, Assignment.class);
    	Log.info("Query is :" +query + "Result Size" + q.getResultList().size());
    	return q.getResultList();
    }
    //Testing task }
}
