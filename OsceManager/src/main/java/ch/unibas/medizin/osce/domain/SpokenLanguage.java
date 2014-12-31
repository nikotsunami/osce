package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class SpokenLanguage {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	
    @Size(max = 40)
    private String languageName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spokenlanguage")
    private Set<LangSkill> langskills = new HashSet<LangSkill>();
    
    public static Long countLanguagesByName(String name) {
    	EntityManager em = entityManager();
    	TypedQuery<Long> q = em.createQuery("SELECT COUNT(o) FROM SpokenLanguage o WHERE o.languageName LIKE :name ORDER BY o.languageName", Long.class);
    	q.setParameter("name", "%" + name + "%");
    	
    	return q.getSingleResult();
    }
    
    public static List<SpokenLanguage> findLanguagesByName(String name, int firstResult, int maxResults) {
        if (name == null) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = entityManager();
        TypedQuery<SpokenLanguage> q = em.createQuery("SELECT o FROM SpokenLanguage AS o WHERE o.languageName LIKE :name ORDER BY o.languageName", SpokenLanguage.class);
        q.setParameter("name", "%" + name + "%");
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    // nur zum testen...
    public static List<SpokenLanguage> findAllLanguages() {
    	EntityManager em = entityManager();
    	TypedQuery<SpokenLanguage> q = em.createQuery("SELECT o FROM SpokenLanguage o ORDER BY o.languageName", SpokenLanguage.class);
    	return q.getResultList();
    }
    
    /**
     * Finds all the languages not spoken by the given standardized patient.
     * @param patientId ID of the relevant sp
     * @return
     */
    public static List<SpokenLanguage> findLanguagesByNotStandardizedPatient(Long patientId) {
    	EntityManager em = entityManager();
    	StandardizedPatient standardizedPatient = StandardizedPatient.findStandardizedPatient(patientId);
    	TypedQuery<SpokenLanguage> q = em.createQuery("SELECT o FROM SpokenLanguage AS o WHERE o.id NOT IN ( " + 
    			"SELECT ls.spokenlanguage.id FROM LangSkill AS ls WHERE ls.standardizedpatient = :sp)" + 
    			"ORDER BY o.languageName", SpokenLanguage.class);
    	q.setParameter("sp", standardizedPatient);
    	return q.getResultList();
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
            SpokenLanguage attached = SpokenLanguage.findSpokenLanguage(this.id);
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
    public SpokenLanguage merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpokenLanguage merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpokenLanguage().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpokenLanguages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpokenLanguage o", Long.class).getSingleResult();
    }

	public static List<SpokenLanguage> findAllSpokenLanguages() {
        return entityManager().createQuery("SELECT o FROM SpokenLanguage o", SpokenLanguage.class).getResultList();
    }

	public static SpokenLanguage findSpokenLanguage(Long id) {
        if (id == null) return null;
        return entityManager().find(SpokenLanguage.class, id);
    }

	public static List<SpokenLanguage> findSpokenLanguageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpokenLanguage o", SpokenLanguage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Langskills: ").append(getLangskills() == null ? "null" : getLangskills().size()).append(", ");
        sb.append("LanguageName: ").append(getLanguageName()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public String getLanguageName() {
        return this.languageName;
    }

	public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

	public Set<LangSkill> getLangskills() {
        return this.langskills;
    }

	public void setLangskills(Set<LangSkill> langskills) {
        this.langskills = langskills;
    }
}
