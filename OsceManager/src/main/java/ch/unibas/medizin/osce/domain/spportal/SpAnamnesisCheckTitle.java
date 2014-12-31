package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name="anamnesis_check_title")
public class SpAnamnesisCheckTitle {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
	 @Id
	 @Column(name = "id")
	 private Long id;
	 
    @NotNull
    @Size(max = 255)
    private String text;

    @NotNull
    private Integer sort_order;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesisCheckTitle")
   	private Set<SpAnamnesisCheck> anamnesisChecks = new HashSet<SpAnamnesisCheck>();
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sort_order == null) ? 0 : sort_order.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpAnamnesisCheckTitle other = (SpAnamnesisCheckTitle) obj;
        if (sort_order == null) {
            if (other.sort_order != null)
                return false;
        } else if (!sort_order.equals(other.sort_order))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }
    
    public static SpAnamnesisCheckTitle findAnamnisisCheckTitleBasedonId(Long titleId){
    	
		 EntityManager em = SpAnamnesisCheckTitle.entityManager();
		
		String sql ="SELECT act FROM SpAnamnesisCheckTitle as act WHERE act.id="+titleId;
		
		TypedQuery<SpAnamnesisCheckTitle> query = em.createQuery(sql, SpAnamnesisCheckTitle.class);
		
		List<SpAnamnesisCheckTitle> listAnamnesisCheckTitles=query.getResultList();
		
		if(listAnamnesisCheckTitles.size()==1){
			return listAnamnesisCheckTitles.get(0);
		}else{
			return null;
		}
    }

 public static SpAnamnesisCheckTitle findAnamnisisCheckTitleBasedonText(String titleText){
    	
    	EntityManager em = SpAnamnesisCheckTitle.entityManager();
		
		String sql ="SELECT act FROM SpAnamnesisCheckTitle as act WHERE act.text="+titleText;
		
		TypedQuery<SpAnamnesisCheckTitle> query = em.createQuery(sql, SpAnamnesisCheckTitle.class);
		
		List<SpAnamnesisCheckTitle> listAnamnesisCheckTitles=query.getResultList();
		
		if(listAnamnesisCheckTitles.size()==1){
			return listAnamnesisCheckTitles.get(0);
		}else{
			return null;
		}
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
            SpAnamnesisCheckTitle attached = SpAnamnesisCheckTitle.findSpAnamnesisCheckTitle(this.id);
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
    public SpAnamnesisCheckTitle merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpAnamnesisCheckTitle merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpAnamnesisCheckTitle().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpAnamnesisCheckTitles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpAnamnesisCheckTitle o", Long.class).getSingleResult();
    }

	public static List<SpAnamnesisCheckTitle> findAllSpAnamnesisCheckTitles() {
        return entityManager().createQuery("SELECT o FROM SpAnamnesisCheckTitle o", SpAnamnesisCheckTitle.class).getResultList();
    }

	public static SpAnamnesisCheckTitle findSpAnamnesisCheckTitle(Long id) {
        if (id == null) return null;
        return entityManager().find(SpAnamnesisCheckTitle.class, id);
    }

	public static List<SpAnamnesisCheckTitle> findSpAnamnesisCheckTitleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpAnamnesisCheckTitle o", SpAnamnesisCheckTitle.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnamnesisChecks: ").append(getAnamnesisChecks() == null ? "null" : getAnamnesisChecks().size()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Sort_order: ").append(getSort_order()).append(", ");
        sb.append("Text: ").append(getText()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public String getText() {
        return this.text;
    }

	public void setText(String text) {
        this.text = text;
    }

	public Integer getSort_order() {
        return this.sort_order;
    }

	public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }

	public Set<SpAnamnesisCheck> getAnamnesisChecks() {
        return this.anamnesisChecks;
    }

	public void setAnamnesisChecks(Set<SpAnamnesisCheck> anamnesisChecks) {
        this.anamnesisChecks = anamnesisChecks;
    }
}
