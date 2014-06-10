package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
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
}
