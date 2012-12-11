package ch.unibas.medizin.osce.domain;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.Sorting;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

@RooJavaBean
@RooToString
@RooEntity
public class Student {

	private static Logger Log = Logger.getLogger(Student.class);
	
    @Enumerated
    private Gender gender;
    
    private String name;

    private String preName;

    
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;
        
    private String studentId;
    
    private String street;
    
    private String city;
    
    // Module10 Create plans
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Set<Assignment> assignments = new HashSet<Assignment>();
    // E Module10 Create plans
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Set<StudentOsces> studentOsces = new HashSet<StudentOsces>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Set<Answer> answer = new HashSet<Answer>();
    
    public static List<Student> findStudentByStudentIdAndByEmail(String studid, String email)
    {
    	EntityManager em = entityManager();
 
    	String sql = "";
    	
    	if (email.equals(""))
    		sql = "SELECT s FROM Student s WHERE s.studentId = '" + studid + "'";
    	else if (studid.equals(""))
    		sql = "SELECT s FROM Student s WHERE s.email = '" + email + "'";
    	else
    		sql = "SELECT s FROM Student s WHERE s.studentId = " + studid + " AND s.email = '" + email + "'";
    	
    	TypedQuery<Student> q = em.createQuery(sql, Student.class);
    	return q.getResultList();
    }
    
    public static List<Student> findStudentByEmail(String email)
    {
    	EntityManager em = entityManager();
    	TypedQuery<Student> q = em.createQuery("SELECT o FROM Student o WHERE o.email LIKE :studemail", Student.class);
     	q.setParameter("studemail", "%" + email + "%");
     	return q.getResultList();
    }
    
    public static Long countStudentByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM Student o WHERE o.name LIKE :name", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<Student> findStudentEntriesByName(String name, int firstResult, int maxResults) {
    	
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<Student> q = em.createQuery("SELECT o FROM Student AS o WHERE o.name LIKE :name", Student.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }

    // Module10 Create plans
    //Find Student by Osce Id
    public static List<Student> findStudentByOsceId(long osceId)
    {
		Log.info("Call findStudentByOsceId for id" + osceId);	
		EntityManager em = entityManager();
		//String queryString = "select distinct stud from Student as stud, OsceDay as od, Assignment as assi, Osce as o " +"where o.id=od.osce and od.id=assi.osceDay and assi.student=stud.id and o.id=" + osceId;
		
		// Fetch All The Student Which are in this OSCE
		//String queryString="select id from student where id in (select id  from student_osces where osce="+osceId+")";
		
		// Fetch All The Student which are in this OSCE and has a ASSIGNMENT
		String queryString="select distinct stud from Student as stud where stud.id in (select assi.student from Assignment as assi where assi.osceDay in(select id from OsceDay where osce=" + osceId + ") and assi.student is not null)";		
		
		Log.info("Query String: " + queryString);
		TypedQuery<Student> q = em.createQuery(queryString,Student.class);		
		List<Student> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result;    	    
    }
    
    public static List<Student> findStudentByOsceIdAndCourseId(long osceId,long courseId)
    {
		Log.info("Call findStudentByOsceId for id" + osceId);	
		EntityManager em = entityManager();
		//String queryString = "select distinct stud from Student as stud, OsceDay as od, Assignment as assi, Osce as o " +"where o.id=od.osce and od.id=assi.osceDay and assi.student=stud.id and o.id=" + osceId;
		
		// Fetch All The Student Which are in this OSCE
		//String queryString="select id from student where id in (select id  from student_osces where osce="+osceId+")";
		
		// Fetch All The Student which are in this OSCE and has a ASSIGNMENT
		//String queryString="select distinct stud from Student as stud where stud.id in (select assi.student from Assignment as assi where assi.osceDay in(select id from OsceDay where osce=" + osceId + ") and assi.oscePostRoom.course="+ courseId +" and assi.student is not null)";
		//select distinct  student from assignment where osce_post_room in (select id from osce_post_room where course=127) and student is not null and osce_day = 18 order by id;
		String queryString="select distinct assi.student from Assignment as assi where assi.osceDay.osce = " + osceId + " and assi.oscePostRoom.course = " + courseId + " and assi.student is not null";
		Log.info("Query String: " + queryString);
		TypedQuery<Student> q = em.createQuery(queryString,Student.class);		
		List<Student> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result;    	    
    }
 // E Module10 Create plans
   
    //by spec issue change[
    public static List<Student> findStudnetByAssignment(Long assId)
    {
    	Assignment ass = Assignment.findAssignment(assId);
    	
    	EntityManager em = entityManager();
    	String sql = "SELECT DISTINCT s FROM Student s, Assignment a WHERE s.id = a.student AND s.id <> " + ass.getStudent().getId() + " AND a.osceDay.osce = " + ass.getOsceDay().getOsce().getId() + " ORDER BY s.id";
    	//System.out.println("~~QUERY : " + sql);
    	TypedQuery<Student> q = em.createQuery(sql, Student.class);
    	return q.getResultList();
    }
    //by spec issue change]
 public static List<Student> getStudents(String sortColumn, Sorting order, Integer firstResult, Integer maxResults,boolean isFirstTime,String searchValue) {
    		Log.info("Inside getStudents()");
 
    		EntityManager em = entityManager();
        	
    		String query="SELECT s FROM Student AS s where s.name LIKE :name1 order by "+sortColumn + " "+ order;
            
    		TypedQuery<Student> q = em.createQuery(query, Student.class);
         	q.setParameter("name1", "%" + searchValue + "%");

    		
    		if(!isFirstTime){
             q.setFirstResult(firstResult);
             q.setMaxResults(maxResults);
    		}

             List<Student> result = q.getResultList();
        
             return result;
      //  return result;
        	
        }
    public static Long getCountOfStudent(String sortColumn,Sorting order, String searchValue){
    	
    	Log.info("Inside getCountOfStudent()");
    	 
		EntityManager em = entityManager();
    	
		String query="SELECT count(s) FROM Student AS s where s.name LIKE :name1 order by "+sortColumn + " "+ order;
        
		TypedQuery<Long> q = em.createQuery(query, Long.class);
		q.setParameter("name1", "%" + searchValue + "%");

		return q.getSingleResult();
    }
    public static List<Osce> findOsceBasedOnStudent(Long studentID){
    	
    	Log.info("Inside findOsceBasedOnStudent() with Student" + studentID );
   	 
		EntityManager em = entityManager();
    	
		String query="select o  from Osce as o, StudentOsces as so where o.id = so.osce and so.student="+studentID;

		TypedQuery<Osce> q = em.createQuery(query, Osce.class);
         
		return q.getResultList();
    }

	public static List<String> findStudentFromAssignmentByOsceDayRoomAndTime(OsceDay osceDayEntity, Long oscePostRoomId, Date timeStart, Date timeEnd) 
	{
		Log.info("Inside findStudentFromAssignmentByOsceDayRoomAndTime() with OsceDay" + osceDayEntity.getId() + " OscePostRoom: " +  oscePostRoomId + "Start Date: " + timeStart + "End Date: " +timeEnd);
		List<String> nameList=new ArrayList<String>();
		EntityManager em = entityManager();
    	TypedQuery<Student> q = em.createQuery("SELECT a.student FROM Assignment as a WHERE a.student is not null and a.osceDay=:osceDay and a.oscePostRoom="+oscePostRoomId+" and a.timeStart>=:startTime and a.timeEnd<=:endTime", Student.class);    	
     	q.setParameter("osceDay", osceDayEntity);
     	q.setParameter("startTime", timeStart);
     	q.setParameter("endTime", timeEnd);
     	
     	for(Student s:q.getResultList())
     	{
     		nameList.add(s.getPreName()+" "+s.getName());
     	}
     	
     	return nameList;
	}
}
