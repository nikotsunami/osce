package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.SurveyStatus;

@Configurable
@Entity
@Table(name="semester")
public class SpSemester {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	 
	 @Id
	 @Column(name = "id")
	 private Long id;
	 
    private Integer calYear;
    
    private Double maximalYearEarnings;
    
    private Integer preparationRing;
    
    private Double priceStandardizedPartient;
    
    private Double pricestatist;
    
    @NotNull
    @Enumerated
    private Semesters semester;

    @Enumerated
    private SurveyStatus surveyStatus;   
    

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
    private Set<SpPatientInSemester> patientsInSemester = new HashSet<SpPatientInSemester>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
	private Set<SpTrainingBlock> trainingBlocks = new HashSet<SpTrainingBlock>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semester")
   	private Set<SpOsceDate> osceDates = new HashSet<SpOsceDate>();
	
    private static transient Logger log = Logger.getLogger(SpSemester.class);
    
    public static SpSemester findSemesterBasedOnYearAndSemester(Integer calYear,int semester){
    	
    	log.info("finding semester based on year and semester");
		 
		 /*EntityManagerFactory emFactory=Persistence.createEntityManagerFactory("spportalPersistenceUnit");

		 EntityManager em = emFactory.createEntityManager();*/
		 
    	 EntityManager em =entityManager();
    	 
		 String queryString ="select s from SpSemester s where s.calYear="+calYear +" AND s.semester="+semester;
		 
		 TypedQuery<SpSemester> query =em.createQuery(queryString,SpSemester.class);
		 
		 List<SpSemester> spSemesterList = query.getResultList();
		 
		 if(spSemesterList==null || spSemesterList.size()==0){
			 return null;
		 }else{
		
			 return spSemesterList.get(0);
		 }
		
		 
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getCalYear() {
        return this.calYear;
    }

	public void setCalYear(Integer calYear) {
        this.calYear = calYear;
    }

	public Double getMaximalYearEarnings() {
        return this.maximalYearEarnings;
    }

	public void setMaximalYearEarnings(Double maximalYearEarnings) {
        this.maximalYearEarnings = maximalYearEarnings;
    }

	public Integer getPreparationRing() {
        return this.preparationRing;
    }

	public void setPreparationRing(Integer preparationRing) {
        this.preparationRing = preparationRing;
    }

	public Double getPriceStandardizedPartient() {
        return this.priceStandardizedPartient;
    }

	public void setPriceStandardizedPartient(Double priceStandardizedPartient) {
        this.priceStandardizedPartient = priceStandardizedPartient;
    }

	public Double getPricestatist() {
        return this.pricestatist;
    }

	public void setPricestatist(Double pricestatist) {
        this.pricestatist = pricestatist;
    }

	public Semesters getSemester() {
        return this.semester;
    }

	public void setSemester(Semesters semester) {
        this.semester = semester;
    }

	public SurveyStatus getSurveyStatus() {
        return this.surveyStatus;
    }

	public void setSurveyStatus(SurveyStatus surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

	public Set<SpPatientInSemester> getPatientsInSemester() {
        return this.patientsInSemester;
    }

	public void setPatientsInSemester(Set<SpPatientInSemester> patientsInSemester) {
        this.patientsInSemester = patientsInSemester;
    }

	public Set<SpTrainingBlock> getTrainingBlocks() {
        return this.trainingBlocks;
    }

	public void setTrainingBlocks(Set<SpTrainingBlock> trainingBlocks) {
        this.trainingBlocks = trainingBlocks;
    }

	public Set<SpOsceDate> getOsceDates() {
        return this.osceDates;
    }

	public void setOsceDates(Set<SpOsceDate> osceDates) {
        this.osceDates = osceDates;
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CalYear: ").append(getCalYear()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("MaximalYearEarnings: ").append(getMaximalYearEarnings()).append(", ");
        sb.append("OsceDates: ").append(getOsceDates() == null ? "null" : getOsceDates().size()).append(", ");
        sb.append("PatientsInSemester: ").append(getPatientsInSemester() == null ? "null" : getPatientsInSemester().size()).append(", ");
        sb.append("PreparationRing: ").append(getPreparationRing()).append(", ");
        sb.append("PriceStandardizedPartient: ").append(getPriceStandardizedPartient()).append(", ");
        sb.append("Pricestatist: ").append(getPricestatist()).append(", ");
        sb.append("Semester: ").append(getSemester()).append(", ");
        sb.append("SurveyStatus: ").append(getSurveyStatus()).append(", ");
        sb.append("TrainingBlocks: ").append(getTrainingBlocks() == null ? "null" : getTrainingBlocks().size()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	@Version
    @Column(name = "version")
    private Integer version;

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
            SpSemester attached = SpSemester.findSpSemester(this.id);
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
    public SpSemester merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpSemester merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpSemester().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpSemesters() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpSemester o", Long.class).getSingleResult();
    }

	public static List<SpSemester> findAllSpSemesters() {
        return entityManager().createQuery("SELECT o FROM SpSemester o", SpSemester.class).getResultList();
    }

	public static SpSemester findSpSemester(Long id) {
        if (id == null) return null;
        return entityManager().find(SpSemester.class, id);
    }

	public static List<SpSemester> findSpSemesterEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpSemester o", SpSemester.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
}
