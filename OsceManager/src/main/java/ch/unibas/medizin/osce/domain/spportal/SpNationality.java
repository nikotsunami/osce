package ch.unibas.medizin.osce.domain.spportal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.domain.Profession;

@RooJavaBean
@RooToString
@RooEntity
@Table(name="nationality")
public class SpNationality {
	
	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Size(max = 40)
    private String nationality;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private Set<SpStandardizedPatient> standardizedpatients = new HashSet<SpStandardizedPatient>();

    private static Logger log = Logger.getLogger(SpNationality.class);
    
	public static SpNationality findNationalityOnNationalityText(String nationality) {
		
		log.info("finding Nationality of text :" + nationality);
		
		try{
		
			EntityManager em = SpNationality.entityManager();
			
			String sql="SELECT n FROM SpNationality as n where n.nationality='"+nationality + "'";

			TypedQuery<SpNationality> query = em.createQuery(sql,SpNationality.class);
			
			List<SpNationality> listOfNationalities = query.getResultList();
			
			if(listOfNationalities.size()==1){
				return listOfNationalities.get(0);
			}else{
				return null;
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
    
}
