package ch.unibas.medizin.osce.domain;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class KeyPair {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@Size(max = 5000)
    private String privateKey;
 	
 	@NotNull
    @Size(max = 5000)
    private String publicKey;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd.MM.YYYY hh:mm:ss")
	private Date date;    
    
    private Long userId;
    
    private Boolean active;

	public static KeyPair findActiveAdminKeyPair() {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<KeyPair> criteriaQuery = criteriaBuilder.createQuery(KeyPair.class);
		Root<KeyPair> from = criteriaQuery.from(KeyPair.class);
		criteriaQuery.where(criteriaBuilder.equal(from.get("active"), Boolean.TRUE));
		TypedQuery<KeyPair> query = entityManager().createQuery(criteriaQuery);
		if (query.getResultList().size() > 0)
			return query.getResultList().get(0);
		else
			return null;
	}
}
