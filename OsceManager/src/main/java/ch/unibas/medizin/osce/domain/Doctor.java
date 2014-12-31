package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.shared.Gender;
import ch.unibas.medizin.osce.shared.Sorting;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@Configurable
@Entity
public class Doctor {
	
	private static Logger Log = Logger.getLogger(Doctor.class);

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @Enumerated
    private Gender gender;

    @Size(max = 40)
    private String title;

    @Size(max = 40)
    private String name;

    @Size(max = 40)
    private String preName;

    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;

    @Size(max = 30)
    private String telephone;

    @ManyToOne
    private Clinic clinic;

    @OneToOne(cascade = CascadeType.ALL)
    private Office office;

    private Boolean isActive;

    @ManyToOne
    private Specialisation specialisation;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examiner")
	 private Set<Assignment> assignments = new HashSet<Assignment>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private Set<RoleParticipant> roleParticipants = new HashSet<RoleParticipant>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examiner")
    private List<PostAnalysis> postAnalysis = new ArrayList<PostAnalysis>();

 /*   public static Long countDoctorsBySearch(String q) {
        EntityManager em = entityManager();
       
        
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM Doctor o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", Long.class);
        query.setParameter("q", "%" + q + "%");
        return query.getSingleResult();
    }*/

    public static Long countDoctorsBySearchWithClinic(String q,Long id) {
        EntityManager em = entityManager();
        String queryString="";
        if(id==null)
        {
        	queryString="SELECT COUNT(o) FROM Doctor o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q";
        }
        else
        {
        	queryString="SELECT COUNT(o) FROM Doctor o WHERE (o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q ) and o.clinic.id=" +id;
        }
        //TypedQuery<Long> query = em.createQuery("SELECT COUNT(o) FROM Doctor o WHERE (o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q ) and o.clinic.id=" +id, Long.class);
        TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        query.setParameter("q", "%" + q + "%");
        return query.getSingleResult();
    }

   
    
    
    /*public static List<Doctor> findDoctorsBySearch(String q, int firstResult, int maxResults,Sorting sortorder,String sortFiled) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
        String queryString="SELECT o FROM Doctor AS o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q ORDER BY "+sortFiled + " " +sortorder;
       // TypedQuery<Doctor> query = em.createQuery("SELECT o FROM Doctor AS o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", Doctor.class);
        TypedQuery<Doctor> query = em.createQuery(queryString, Doctor.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }
    */
    public static List<Doctor> findDoctorsBySearchWithClinic(String q,Long id, int firstResult, int maxResults,Sorting sortorder,String sortFiled) {
        if (q == null) throw new IllegalArgumentException("The q argument is required");
        EntityManager em = entityManager();
       String queryString="";
        if(id==null)
        {
        	queryString="SELECT o FROM Doctor AS o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q ORDER BY "+sortFiled + " " +sortorder;
        }
        else
        {
        	queryString="SELECT o FROM Doctor AS o WHERE (o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q) and o.clinic.id="+id +" ORDER BY "+sortFiled + " " +sortorder;
        }
        //String queryString="SELECT o FROM Doctor AS o WHERE (o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q) and o.clinic.id="+id +" ORDER BY "+sortFiled + " " +sortorder;
       // TypedQuery<Doctor> query = em.createQuery("SELECT o FROM Doctor AS o WHERE o.name LIKE :q OR o.preName LIKE :q OR o.email LIKE :q", Doctor.class);
        TypedQuery<Doctor> query = em.createQuery(queryString, Doctor.class);
        query.setParameter("q", "%" + q + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }
    
    // SPEC START =
   	public static java.util.List<Doctor> findDoctorWithRoleTopic(Long stadRoleid)  // Fill Doctor Name in Value List Box
   	{
   		EntityManager em = entityManager();
   		Log.info("~QUERY findDoctorWithRoleTopic()");
   		//Log.info("~QUERY BEFOREE EXECUTION role topic id : " + roleTopicid + " Standardized Role ID: " + stadRoleid);		
   		//String queryString="SELECT doc from Doctor doc";
   		//String queryString="select d from Doctor d where d.id not in(select r.doctor from RoleParticipant r join r.standardizedRole sr where sr.id="+stadRoleid+") and d.specialisation in (select s.id from Specialisation s join s.roleTopics rt join rt.standardizedRoles sr where sr.id="+stadRoleid+")";// WORKING		
   		//String queryString="select d from Doctor d where d.id not in(select r.doctor from RoleParticipant r join r.standardizedRole sr where sr.id="+stadRoleid+") and d.specialisation in (select s.id from Specialisation s join s.roleTopics rt join rt.standardizedRoles sr where s.id=rt.specialisation and rt.id=sr.roleTopic and sr.id="+stadRoleid+")";
   		String queryString="select d from Doctor d where d.id not in(select r.doctor from RoleParticipant r join r.standardizedRole sr where sr.id="+stadRoleid+")";
   		//select d.* from doctor d where d.id not in(select r.doctor from role_participant r join standardized_role sr where sr.id = 2 ) and d.specialisation in (select s.id from specialisation s join role_topic rt join standardized_role sr where s.id = rt.specialisation and rt.id = sr.role_topic and sr.id = 2 );
   		Log.info("~QUERY String: " + queryString);
   		TypedQuery<Doctor> q = em.createQuery(queryString, Doctor.class);
   		java.util.List<Doctor> result = q.getResultList();
   		Log.info("~QUERY Result : " + result);
   		return result;
   		//String queryString="SELECT doc from Doctor doc JOIN doc.specialisation sp JOIN sp.roleTopics rt JOIN rt.standardizedRoles sr WHERE rt.id = 1 and  sr.id <>"+id;
   		//String queryString="SELECT doc from Doctor doc JOIN RoleTopic rt with doc.specialisation = rt.specialisation JOIN rt.standardizedRoles sr WHERE sr.id <> " + id;		
   		//String queryString="SELECT sp.doctors from Specialisation sp join sp.roleTopics rt JOIN rt.standardizedRoles sr WHERE sr.id <> " + id;		
   		//String queryString="SELECT distinct doc from Doctor doc JOIN doc.specialisation.roleTopics rt JOIN rt.standardizedRoles sr WHERE rt.id = " + id + " and  sr.id <>"+id;
   	}
   	
   	// SPEC END =
    
   	//Module 6 Start
	public static java.util.List<Doctor> findDoctorByClinicID(Long clinicid)  
   	{
   		EntityManager em = entityManager();
   		String queryString="select d from Doctor d where d.clinic.id = " + clinicid;
   		TypedQuery<Doctor> q = em.createQuery(queryString, Doctor.class);
   		java.util.List<Doctor> result = q.getResultList();
   		return result;
   	}
   	
   	//Module 6 End
	
	// Module10 Create plans
    //Find Student by Osce Id
    public static List<Doctor> findDoctorByOsceId(Long osceId)
    {
		Log.info("Call findDoctorByOsceId for id" + osceId);	
		EntityManager em = entityManager();
		//String queryString = "select distinct d from Doctor as d, OsceDay as od, Assignment as assi, Osce as o " + "where o.id=od.osce and od.id=assi.osceDay and assi.examiner=d.id and o.id=" + osceId;
                String queryString = "select d from Doctor as d where d.id in (select distinct assi.examiner from Assignment as assi where assi.osceDay in(select od.id from OsceDay as od where od.osce=" + osceId + ") and assi.examiner is not null)";
		Log.info("Query String: " + queryString);
		TypedQuery<Doctor> q = em.createQuery(queryString,Doctor.class);		
		List<Doctor> result  = q.getResultList();        
		Log.info("EXECUTION IS SUCCESSFUL: RECORDS FOUND "+result);
        return result;    	    
    }
 // E Module10 Create plans
  
    public static List<Specialisation> findSpecialisationByClinicId(Long clinicId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT d.specialisation FROM Doctor AS d WHERE d.clinic = " + clinicId + " GROUP BY d.specialisation";
    	TypedQuery<Specialisation> q = em.createQuery(sql, Specialisation.class);
    	return q.getResultList();
    }

    public static List<Doctor> findDoctorByAssignment(Long specialisationId,Long clinicId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT d FROM Doctor AS d, Assignment As a WHERE d.specialisation = " + specialisationId + " AND d.clinic = " + clinicId + " AND d.id = a.examiner GROUP BY d.id";
    	TypedQuery<Doctor> q = em.createQuery(sql, Doctor.class);
    	return q.getResultList();
    }
    
    public static void updateExaminerIdToSession(List<Long> examinerIdList)
    {
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute(OsMaFilePathConstant.EXAMINER_LIST_QR, examinerIdList);
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
            Doctor attached = Doctor.findDoctor(this.id);
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
    public Doctor merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Doctor merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Doctor().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countDoctors() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Doctor o", Long.class).getSingleResult();
    }

	public static List<Doctor> findAllDoctors() {
        return entityManager().createQuery("SELECT o FROM Doctor o", Doctor.class).getResultList();
    }

	public static Doctor findDoctor(Long id) {
        if (id == null) return null;
        return entityManager().find(Doctor.class, id);
    }

	public static List<Doctor> findDoctorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Doctor o", Doctor.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public Gender getGender() {
        return this.gender;
    }

	public void setGender(Gender gender) {
        this.gender = gender;
    }

	public String getTitle() {
        return this.title;
    }

	public void setTitle(String title) {
        this.title = title;
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

	public String getTelephone() {
        return this.telephone;
    }

	public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

	public Clinic getClinic() {
        return this.clinic;
    }

	public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

	public Office getOffice() {
        return this.office;
    }

	public void setOffice(Office office) {
        this.office = office;
    }

	public Boolean getIsActive() {
        return this.isActive;
    }

	public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

	public Specialisation getSpecialisation() {
        return this.specialisation;
    }

	public void setSpecialisation(Specialisation specialisation) {
        this.specialisation = specialisation;
    }

	public Set<Assignment> getAssignments() {
        return this.assignments;
    }

	public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

	public Set<RoleParticipant> getRoleParticipants() {
        return this.roleParticipants;
    }

	public void setRoleParticipants(Set<RoleParticipant> roleParticipants) {
        this.roleParticipants = roleParticipants;
    }

	public List<PostAnalysis> getPostAnalysis() {
        return this.postAnalysis;
    }

	public void setPostAnalysis(List<PostAnalysis> postAnalysis) {
        this.postAnalysis = postAnalysis;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assignments: ").append(getAssignments() == null ? "null" : getAssignments().size()).append(", ");
        sb.append("Clinic: ").append(getClinic()).append(", ");
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("Gender: ").append(getGender()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsActive: ").append(getIsActive()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Office: ").append(getOffice()).append(", ");
        sb.append("PostAnalysis: ").append(getPostAnalysis() == null ? "null" : getPostAnalysis().size()).append(", ");
        sb.append("PreName: ").append(getPreName()).append(", ");
        sb.append("RoleParticipants: ").append(getRoleParticipants() == null ? "null" : getRoleParticipants().size()).append(", ");
        sb.append("Specialisation: ").append(getSpecialisation()).append(", ");
        sb.append("Telephone: ").append(getTelephone()).append(", ");
        sb.append("Title: ").append(getTitle()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
