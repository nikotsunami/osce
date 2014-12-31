package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.OsceCreationType;
import ch.unibas.medizin.osce.shared.Sorting;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@Entity
@Configurable
public class Student {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
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
    	String sql = "";
    	
    	if (OsceCreationType.Manual.equals(ass.getOsceDay().getOsce().getOsceCreationType()))
    	{
    		if (ass.getStudent() == null)
    			sql = "SELECT DISTINCT so.student FROM StudentOsces so WHERE so.osce.id = " + ass.getOsceDay().getOsce().getId() + " AND so.isEnrolled = 1 ORDER BY so.student.id";
        	else	
        		sql = "SELECT DISTINCT so.student FROM StudentOsces so WHERE so.osce.id = " + ass.getOsceDay().getOsce().getId() + " AND so.isEnrolled = 1 AND so.student.id <> " + ass.getStudent().getId() + " ORDER BY so.student.id";
    	}
    	else if (OsceCreationType.Automatic.equals(ass.getOsceDay().getOsce().getOsceCreationType()))
    	{
    		if (ass.getStudent() == null)
        		sql = "SELECT DISTINCT s FROM Student s, Assignment a WHERE s.id = a.student AND a.osceDay.osce = " + ass.getOsceDay().getOsce().getId() + " ORDER BY s.id";
        	else	
        		sql = "SELECT DISTINCT s FROM Student s, Assignment a WHERE s.id = a.student AND s.id <> " + ass.getStudent().getId() + " AND a.osceDay.osce = " + ass.getOsceDay().getOsce().getId() + " ORDER BY s.id";
    	} 
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
	
	public static void updateStudentToSession(List<Long> studIdList){
		
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute(OsMaFilePathConstant.STUDENT_LIST_QR, studIdList);
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Answer: ").append(getAnswer() == null ? "null" : getAnswer().size()).append(", ");
        sb.append("Assignments: ").append(getAssignments() == null ? "null" : getAssignments().size()).append(", ");
        sb.append("City: ").append(getCity()).append(", ");
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("Gender: ").append(getGender()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("PreName: ").append(getPreName()).append(", ");
        sb.append("Street: ").append(getStreet()).append(", ");
        sb.append("StudentId: ").append(getStudentId()).append(", ");
        sb.append("StudentOsces: ").append(getStudentOsces() == null ? "null" : getStudentOsces().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Gender getGender() {
        return this.gender;
    }

	public void setGender(Gender gender) {
        this.gender = gender;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getPreName() {
        return this.preName;
    }

	public void setPreName(String preName) {
        this.preName = preName;
    }

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getStudentId() {
        return this.studentId;
    }

	public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

	public String getStreet() {
        return this.street;
    }

	public void setStreet(String street) {
        this.street = street;
    }

	public String getCity() {
        return this.city;
    }

	public void setCity(String city) {
        this.city = city;
    }

	public Set<Assignment> getAssignments() {
        return this.assignments;
    }

	public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

	public Set<StudentOsces> getStudentOsces() {
        return this.studentOsces;
    }

	public void setStudentOsces(Set<StudentOsces> studentOsces) {
        this.studentOsces = studentOsces;
    }

	public Set<Answer> getAnswer() {
        return this.answer;
    }

	public void setAnswer(Set<Answer> answer) {
        this.answer = answer;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Student attached = Student.findStudent(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Student merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Student merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Student().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countStudents() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Student o", Long.class).getSingleResult();
    }

	public static List<Student> findAllStudents() {
        return entityManager().createQuery("SELECT o FROM Student o", Student.class).getResultList();
    }

	public static Student findStudent(Long id) {
        if (id == null) return null;
        return entityManager().find(Student.class, id);
    }

	public static List<Student> findStudentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Student o", Student.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
