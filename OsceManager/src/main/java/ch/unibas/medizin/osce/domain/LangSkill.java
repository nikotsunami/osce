package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
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
import ch.unibas.medizin.osce.shared.LangSkillLevel;

@Configurable
@Entity
public class LangSkill {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Enumerated
    @NotNull
    private LangSkillLevel skill;

    @ManyToOne
    private StandardizedPatient standardizedpatient;

    @ManyToOne
    private SpokenLanguage spokenlanguage;
    
//    private static Logger logger = Logger.getLogger(SpokenLanguage.class);
    
    public static Long countLangSkillsByPatientId(Long patientId) {
    	EntityManager em = entityManager();
    	StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(patientId);
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(skill) FROM LangSkill skill WHERE standardizedpatient = :sp", Long.class);
    	q.setParameter("sp", standardizedPatient);
    	return q.getSingleResult();
    }
    
    public static List<LangSkill> findLangSkillsByPatientId(Long patientId, int firstResult, int maxResults) {
    	EntityManager em = entityManager();
    	StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(patientId);
    	
    	TypedQuery<LangSkill> q = em.createQuery("SELECT skill FROM LangSkill AS skill WHERE standardizedpatient = :sp ORDER BY skill", LangSkill.class);
    	q.setParameter("sp", standardizedPatient);
    	
    	q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
    	return q.getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Skill: ").append(getSkill()).append(", ");
        sb.append("Spokenlanguage: ").append(getSpokenlanguage()).append(", ");
        sb.append("Standardizedpatient: ").append(getStandardizedpatient()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public LangSkillLevel getSkill() {
        return this.skill;
    }

	public void setSkill(LangSkillLevel skill) {
        this.skill = skill;
    }

	public StandardizedPatient getStandardizedpatient() {
        return this.standardizedpatient;
    }

	public void setStandardizedpatient(StandardizedPatient standardizedpatient) {
        this.standardizedpatient = standardizedpatient;
    }

	public SpokenLanguage getSpokenlanguage() {
        return this.spokenlanguage;
    }

	public void setSpokenlanguage(SpokenLanguage spokenlanguage) {
        this.spokenlanguage = spokenlanguage;
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
            LangSkill attached = LangSkill.findLangSkill(this.id);
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
    public LangSkill merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LangSkill merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new LangSkill().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLangSkills() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LangSkill o", Long.class).getSingleResult();
    }

	public static List<LangSkill> findAllLangSkills() {
        return entityManager().createQuery("SELECT o FROM LangSkill o", LangSkill.class).getResultList();
    }

	public static LangSkill findLangSkill(Long id) {
        if (id == null) return null;
        return entityManager().find(LangSkill.class, id);
    }

	public static List<LangSkill> findLangSkillEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LangSkill o", LangSkill.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
