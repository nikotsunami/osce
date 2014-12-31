package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class StudentOsces {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
    private Boolean isEnrolled;

    @ManyToOne
    @NotNull
    private Osce osce;

    @ManyToOne
    @NotNull
    private Student student;
    
    public static List<StudentOsces> findStudentOsceByOsce(Long id) {
    	EntityManager em = entityManager();
    	
    	String queryString="SELECT o FROM StudentOsces as o  where o.osce="+id;
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
      	return q.getResultList();
    }
    
    public static int findStudentByStudIdAndOsceId(Long studid, Long osceid)
    {
    	EntityManager em = entityManager();
    	String s = "SELECT o FROM StudentOsces o WHERE o.student =" + studid +" AND o.osce ="+ osceid;
    	TypedQuery<StudentOsces> q = em.createQuery(s, StudentOsces.class);
       	return q.getResultList().size();
    }
    
    public static Long countStudentByName(String name1,Long id) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM StudentOsces o WHERE o.student.name LIKE :name1 and o.osce.id LIKE :id", Long.class);
    	q.setParameter("name1", "%" + name1 + "%");
    	 q.setParameter("id", id);
    	return q.getSingleResult();
    }
    
    public static List<StudentOsces> findStudentEntriesByName(String name,Long id, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<StudentOsces> q = em.createQuery("SELECT o FROM StudentOsces o WHERE  o.student.name LIKE :name and o.osce.id LIKE :id", StudentOsces.class);
        q.setParameter("name", "%" + name + "%");
        q.setParameter("id", id);
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    public static List<StudentOsces> findStudentEntriesByNameTest(String name,Long id) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        
        EntityManager em = entityManager();
        TypedQuery<StudentOsces> q = em.createQuery("SELECT o FROM StudentOsces o WHERE  o.student.name LIKE :name and o.osce.id = " + id, StudentOsces.class);
        q.setParameter("name", "%" + name + "%");
        return q.getResultList();
    }
    
    public static List<StudentOsces> findStudentByRange(int start, int max, Long id, String name)
    {
    	EntityManager em = entityManager();
    	
    	String queryString = "";
    	if (name.equals(""))
    		queryString="SELECT o FROM StudentOsces as o where o.osce = " + id;
    	else
    		queryString="SELECT o FROM StudentOsces as o where o.student.name LIKE '%" + name + "%' AND o.osce = " + id;
    	
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
    	q.setFirstResult(start);
    	q.setMaxResults(max);
    	return q.getResultList();
    }
    
    public static Integer countStudentByRange(Long id, String name)
    {
    	EntityManager em = entityManager();
    	String queryString = "";
    	if (name.equals(""))
    		queryString="SELECT o FROM StudentOsces as o where o.osce = " + id;
    	else
    		queryString="SELECT o FROM StudentOsces as o where o.student.name LIKE '%" + name + "%' AND o.osce = " + id;
    	
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
    	return q.getResultList().size();
    }
    
    public static Integer countStudentByOsce(Long osceId)
    {
    	EntityManager em = entityManager();
    	String queryString = "SELECT o FROM StudentOsces as o where o.osce = " + osceId + " AND o.isEnrolled = true";
    	TypedQuery<StudentOsces> q = em.createQuery(queryString, StudentOsces.class);
    	return q.getResultList().size();
    }
    
    public static List<StudentOsces> findStudentByIsEnrolledAndOsceId(Long osceId) {
    	EntityManager em = entityManager();
    	String sql = "SELECT o FROM StudentOsces as o where o.osce.id = " + osceId + " AND o.isEnrolled = true";
    	TypedQuery<StudentOsces> query = em.createQuery(sql, StudentOsces.class);
    	return query.getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsEnrolled: ").append(getIsEnrolled()).append(", ");
        sb.append("Osce: ").append(getOsce()).append(", ");
        sb.append("Student: ").append(getStudent()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
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
            StudentOsces attached = StudentOsces.findStudentOsces(this.id);
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
    public StudentOsces merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        StudentOsces merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new StudentOsces().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countStudentOsceses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StudentOsces o", Long.class).getSingleResult();
    }

	public static List<StudentOsces> findAllStudentOsceses() {
        return entityManager().createQuery("SELECT o FROM StudentOsces o", StudentOsces.class).getResultList();
    }

	public static StudentOsces findStudentOsces(Long id) {
        if (id == null) return null;
        return entityManager().find(StudentOsces.class, id);
    }

	public static List<StudentOsces> findStudentOscesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StudentOsces o", StudentOsces.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Boolean getIsEnrolled() {
        return this.isEnrolled;
    }

	public void setIsEnrolled(Boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }

	public Osce getOsce() {
        return this.osce;
    }

	public void setOsce(Osce osce) {
        this.osce = osce;
    }

	public Student getStudent() {
        return this.student;
    }

	public void setStudent(Student student) {
        this.student = student;
    }
}
