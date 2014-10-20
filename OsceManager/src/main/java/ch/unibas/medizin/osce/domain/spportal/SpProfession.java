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

@RooJavaBean
@RooToString
@RooEntity
@Table(name="profession")
public class SpProfession {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Size(max = 60)
    private String profession;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profession")
    private Set<SpStandardizedPatient> standardizedpatients = new HashSet<SpStandardizedPatient>();

    private static Logger log = Logger.getLogger(SpProfession.class);
    
	public static SpProfession findProfessionBasedOnProfessionText(String profession) {
		log.info("fininding profession of text : " + profession);
		try{
			
			EntityManager em = SpProfession.entityManager();
			
			String sql="SELECT p FROM SpProfession as p where p.profession='"+profession + "'";
			
			TypedQuery<SpProfession> query = em.createQuery(sql,SpProfession.class);
			
			List<SpProfession> listOfProfession = query.getResultList();
			
			if(listOfProfession.size()==1){
				return listOfProfession.get(0);
			}else{
				return null;
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
}
    
}
