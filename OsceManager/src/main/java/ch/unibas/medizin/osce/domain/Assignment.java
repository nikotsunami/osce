package ch.unibas.medizin.osce.domain;


import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.persistence.EntityManager;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.format.annotation.DateTimeFormat;

import com.allen_sauer.gwt.log.client.Log;

import ch.unibas.medizin.osce.domain.OsceDay;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.domain.PatientInRole;
import ch.unibas.medizin.osce.domain.Doctor;

@RooJavaBean
@RooToString
@RooEntity
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
    private Student student;

    @ManyToOne
    private PatientInRole patientInRole;

    @ManyToOne
    private Doctor examiner;
    
    private Integer sequenceNumber;
    
    public static List<Assignment> retrieveAssignments(Long osceDayId,Long osceSequenceId,Long courseId,Long oscePostId)
    {
    	Log.info("retrieveAssignments :");
    	EntityManager em = entityManager();
    	String queryString="SELECT  a FROM Assignment as a where a.osceDay="+osceDayId+"  and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost="+oscePostId+" and opr.course="+courseId+" ) order by a.timeStart asc";
    	TypedQuery<Assignment> query=em.createQuery(queryString
 , Assignment.class);
    	List<Assignment> assignmentList=query.getResultList();
    	
    	Log.info("retrieveAssignments query String :" + queryString);
    	Log.info("Assignment List Size :" +assignmentList.size());
    	return assignmentList;
    }
    
    public static List<Assignment> retrieveAssignmenstOfTypeStudent(Long osceDayId,Long osceSequenceId,Long courseId,Long oscePostId)
    {
    	Log.info("retrieveAssignmenstOfTypeStudent :");
    	EntityManager em = entityManager();
    	String queryString="SELECT  a FROM Assignment as a where a.osceDay="+osceDayId+"  and type=0 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost="+oscePostId+" and opr.course="+courseId+" ) order by a.timeStart asc";
    	TypedQuery<Assignment> query=em.createQuery(queryString
 , Assignment.class);
    	List<Assignment> assignmentList=query.getResultList();
    	
    	Log.info("retrieveAssignmenstOfTypeStudent query String :" + queryString);
    	Log.info("Assignment List Size :" +assignmentList.size());
    	return assignmentList;
    }
    
    public static List<Assignment> retrieveAssignmenstOfTypeSP(Long osceDayId,Long osceSequenceId,Long courseId,Long oscePostId)
    {
    	Log.info("retrieveAssignmenstOfTypeSP :");
    	EntityManager em = entityManager();
    	String queryString="SELECT  a FROM Assignment as a where a.osceDay="+osceDayId+"  and type=1 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost="+oscePostId+" and opr.course="+courseId+" ) order by a.timeStart asc";
    	TypedQuery<Assignment> query=em.createQuery(queryString
 , Assignment.class);
    	List<Assignment> assignmentList=query.getResultList();
    	
    	Log.info("retrieveAssignmenstOfTypeSP query String :" + queryString);
    	Log.info("Assignment List Size :" +assignmentList.size());
    	return assignmentList;
    }
    
    public static List<Assignment> retrieveAssignmenstOfTypeExaminer(Long osceDayId,Long osceSequenceId,Long courseId,Long oscePostId)
    {
    	Log.info("retrieveAssignmenstOfTypeExaminer :");
    	EntityManager em = entityManager();
    	String queryString="SELECT  a FROM Assignment as a where a.osceDay="+osceDayId+"  and type=2 and a.oscePostRoom in(select opr.id from OscePostRoom as opr where opr.oscePost="+oscePostId+" and opr.course="+courseId+" ) order by a.timeStart asc ";
    	TypedQuery<Assignment> query=em.createQuery(queryString
 , Assignment.class);
    	List<Assignment> assignmentList=query.getResultList();
    	
    	Log.info("retrieveAssignmenstOfTypeExaminer query String :" + queryString);
    	Log.info("Assignment List Size :" +assignmentList.size());
    	return assignmentList;
    }
}
