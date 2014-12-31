package ch.unibas.medizin.osce.domain.spportal;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@Table(name="bankaccount")
public class SpBankaccount {

	@PersistenceContext(unitName="spportalPersistenceUnit")
	 transient EntityManager entityManager;
	
    @Size(max = 40)
    private String bankName;

    @Size(max = 40)
    private String IBAN;

    @Size(max = 40)
    private String BIC;
    
    @Size(max = 40)
    private String ownerName;
    
    @Size(max = 15)
    private String postalCode;
    
    @Size(max = 30)
    private String city;
    
    @ManyToOne
    private SpNationality country;

	public String getBankName() {
        return this.bankName;
    }

	public void setBankName(String bankName) {
        this.bankName = bankName;
    }

	public String getIBAN() {
        return this.IBAN;
    }

	public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

	public String getBIC() {
        return this.BIC;
    }

	public void setBIC(String BIC) {
        this.BIC = BIC;
    }

	public String getOwnerName() {
        return this.ownerName;
    }

	public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

	public String getPostalCode() {
        return this.postalCode;
    }

	public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

	public String getCity() {
        return this.city;
    }

	public void setCity(String city) {
        this.city = city;
    }

	public SpNationality getCountry() {
        return this.country;
    }

	public void setCountry(SpNationality country) {
        this.country = country;
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
            SpBankaccount attached = SpBankaccount.findSpBankaccount(this.id);
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
    public SpBankaccount merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SpBankaccount merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SpBankaccount().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSpBankaccounts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SpBankaccount o", Long.class).getSingleResult();
    }

	public static List<SpBankaccount> findAllSpBankaccounts() {
        return entityManager().createQuery("SELECT o FROM SpBankaccount o", SpBankaccount.class).getResultList();
    }

	public static SpBankaccount findSpBankaccount(Long id) {
        if (id == null) return null;
        return entityManager().find(SpBankaccount.class, id);
    }

	public static List<SpBankaccount> findSpBankaccountEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SpBankaccount o", SpBankaccount.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BIC: ").append(getBIC()).append(", ");
        sb.append("BankName: ").append(getBankName()).append(", ");
        sb.append("City: ").append(getCity()).append(", ");
        sb.append("Country: ").append(getCountry()).append(", ");
        sb.append("IBAN: ").append(getIBAN()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("OwnerName: ").append(getOwnerName()).append(", ");
        sb.append("PostalCode: ").append(getPostalCode()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
