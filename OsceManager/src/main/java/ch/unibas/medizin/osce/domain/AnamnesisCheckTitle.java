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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import com.allen_sauer.gwt.log.client.Log;
import ch.unibas.medizin.osce.domain.spportal.SpAnamnesisCheckTitle;

@Configurable
@Entity
public class AnamnesisCheckTitle {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @NotNull
    @Size(max = 255)
    private String text;

    @NotNull
    private Integer sort_order;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anamnesisCheckTitle")
   	private Set<AnamnesisCheck> anamnesisChecks = new HashSet<AnamnesisCheck>();
    
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
        AnamnesisCheckTitle other = (AnamnesisCheckTitle) obj;
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

    public void insertNewSortOder(Integer previousSortOder){
        //TODO
        System.out.println("!!!!!!!!!!!!!!!!this is insertNewSortOder ");
        if(previousSortOder == 0){
	        List<AnamnesisCheckTitle> anamnesisCheckTitles = findAllAnamnesisCheckTitles();
	        for(AnamnesisCheckTitle anamnesisCheckTitleBlow : anamnesisCheckTitles){
	            if(anamnesisCheckTitleBlow.sort_order != null){
	                anamnesisCheckTitleBlow.sort_order = anamnesisCheckTitleBlow.sort_order + 1;
	                anamnesisCheckTitleBlow.persist();
	            }
	        }
	        this.sort_order = 1;
	        this.persist();
        }
    }

    private static List<AnamnesisCheckTitle> getReSortingList(Integer sortFrom) {

        System.out.println(">>>>>>>>>sortFrom = "+sortFrom);
        EntityManager em = AnamnesisCheck.entityManager();
        TypedQuery<AnamnesisCheckTitle> q = em
                .createQuery(
                        "SELECT o FROM AnamnesisCheckTitle AS o WHERE o.sort_order >= :sortFrom ORDER BY sort_order ASC",
                        AnamnesisCheckTitle.class);
        q.setParameter("sortFrom", sortFrom);
        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        }

        return q.getResultList();

    }

    public static void reSorting(Integer sortFrom){
        List<AnamnesisCheckTitle> reSortingList = getReSortingList(sortFrom);
        if(reSortingList != null){
            for (AnamnesisCheckTitle checkTitle : reSortingList) {
                if(checkTitle.sort_order != null){
                    checkTitle.sort_order = checkTitle.sort_order - 1;
                    checkTitle.persist();
                }

            }
        }
    }

    public void moveUp() {
        if (this.entityManager == null) {
            this.entityManager = entityManager();
        }
        AnamnesisCheckTitle anamnesisCheckTitle = findAnamnesisCheckTitleByOrderSmaller(this.sort_order - 1);
        if (anamnesisCheckTitle == null) {
            return;
        }
        anamnesisCheckTitle.setSort_order(this.sort_order);
        anamnesisCheckTitle.persist();
        setSort_order(sort_order - 1);
        this.persist();
    }

    public void moveDown() {
        if (this.entityManager == null) {
            this.entityManager = entityManager();
        }
        AnamnesisCheckTitle anamnesisCheckTitle = findAnamnesisCheckTitleByOrderGreater(this.sort_order + 1);
        if (anamnesisCheckTitle == null) {
            return;
        }
        anamnesisCheckTitle.setSort_order(this.sort_order);
        anamnesisCheckTitle.persist();
        setSort_order(sort_order + 1);
        this.persist();
    }

    //find next title
    private AnamnesisCheckTitle findAnamnesisCheckTitleByOrderGreater(int sort_order) {
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheckTitle> query = em
                .createQuery(
                        "SELECT o FROM AnamnesisCheckTitle AS o WHERE o.sort_order >= :sort_order ORDER BY sort_order ASC",
                        AnamnesisCheckTitle.class);
        query.setParameter("sort_order", sort_order);
        List<AnamnesisCheckTitle> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
    }

    //find previous tilte
    public AnamnesisCheckTitle findAnamnesisCheckTitleByOrderSmaller(int sort_order) {
        EntityManager em = entityManager();
        TypedQuery<AnamnesisCheckTitle> query = em
                .createQuery(
                        "SELECT o FROM AnamnesisCheckTitle AS o WHERE o.sort_order <= :sort_order ORDER BY sort_order DESC",
                        AnamnesisCheckTitle.class);
        query.setParameter("sort_order", sort_order);
        List<AnamnesisCheckTitle> resultList = query.getResultList();
        if (resultList == null || resultList.size() == 0)
            return null;
        return resultList.get(0);
    }

    public static List<AnamnesisCheckTitle> findAllAnamnesisCheckTitles() {
        return entityManager().createQuery("SELECT o FROM AnamnesisCheckTitle o ORDER BY sort_order", AnamnesisCheckTitle.class).getResultList();
    }

	@Transactional
	public void persist() {
    	if (this.entityManager == null) this.entityManager = entityManager();
    	if (this.sort_order == null) {
    		TypedQuery<Integer> q = entityManager.createQuery("SELECT MAX(o.sort_order) FROM AnamnesisCheckTitle AS o", Integer.class);
    		this.sort_order = q.getSingleResult();
    	}
        this.entityManager.persist(this);
    }
	
	//issue sol
	public static Integer findMaxSortOrder()
	{
		EntityManager em = entityManager();
		String sql = "SELECT MAX(a.sort_order) FROM AnamnesisCheckTitle a";
		TypedQuery<Integer> q = em.createQuery(sql, Integer.class);
		return q.getSingleResult();
	}
	
	public static Boolean saveAnamnesisCheckTitleInSpPortal(AnamnesisCheckTitle anamnesisCheckTitle){
		Log.info("saving anamesis check title data in spportal");
		try{
			SpAnamnesisCheckTitle spAnamnesisCheckTitle =new SpAnamnesisCheckTitle();
			spAnamnesisCheckTitle.setSort_order(anamnesisCheckTitle.getSort_order());
			spAnamnesisCheckTitle.setText(anamnesisCheckTitle.getText());
			spAnamnesisCheckTitle.setId(anamnesisCheckTitle.getId());
			spAnamnesisCheckTitle.persist();
			
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		return true;
	}
	
	public static Boolean edittitleInSpportal(Long anamnesisCheckTitleId){
		Log.info("editing anamesis check title data in spportal");
		try{
			
				AnamnesisCheckTitle anCheckTitle= AnamnesisCheckTitle.findAnamnesisCheckTitle(anamnesisCheckTitleId);
			
				SpAnamnesisCheckTitle spAnamnesisCheckTitle= SpAnamnesisCheckTitle.findAnamnisisCheckTitleBasedonId(anamnesisCheckTitleId);
				
				if(spAnamnesisCheckTitle!=null){
				
					spAnamnesisCheckTitle.setSort_order(anCheckTitle.getSort_order());
				
					spAnamnesisCheckTitle.setText(anCheckTitle.getText());
				
					spAnamnesisCheckTitle.persist();
				
				return true;
			}else{
				return null;
			}
		}catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	}
	
	public static Boolean deleteTitleFromSpPortal(Long deletedTitleId){
		Log.info("deleting anamesis check title data in spportal");
		try{
			
			SpAnamnesisCheckTitle spAnamnesisCheckTitle= SpAnamnesisCheckTitle.findAnamnisisCheckTitleBasedonId(deletedTitleId);
			
			if(spAnamnesisCheckTitle!=null){
				spAnamnesisCheckTitle.remove();
				return true;
			}else{
				return false;
			}
	}catch (Exception e) {
		Log.error(e.getMessage(), e);
		return null;
	}
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
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AnamnesisCheckTitle attached = AnamnesisCheckTitle.findAnamnesisCheckTitle(this.id);
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
    public AnamnesisCheckTitle merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AnamnesisCheckTitle merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new AnamnesisCheckTitle().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAnamnesisCheckTitles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AnamnesisCheckTitle o", Long.class).getSingleResult();
    }

	public static AnamnesisCheckTitle findAnamnesisCheckTitle(Long id) {
        if (id == null) return null;
        return entityManager().find(AnamnesisCheckTitle.class, id);
    }

	public static List<AnamnesisCheckTitle> findAnamnesisCheckTitleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AnamnesisCheckTitle o", AnamnesisCheckTitle.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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

	public Set<AnamnesisCheck> getAnamnesisChecks() {
        return this.anamnesisChecks;
    }

	public void setAnamnesisChecks(Set<AnamnesisCheck> anamnesisChecks) {
        this.anamnesisChecks = anamnesisChecks;
    }
}
