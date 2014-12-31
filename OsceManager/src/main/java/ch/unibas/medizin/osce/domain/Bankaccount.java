package ch.unibas.medizin.osce.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Bankaccount {

	@PersistenceContext(unitName="persistenceUnit")
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
    private Nationality country;

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

	public Nationality getCountry() {
        return this.country;
    }

	public void setCountry(Nationality country) {
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
            Bankaccount attached = Bankaccount.findBankaccount(this.id);
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
    public Bankaccount merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Bankaccount merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new Bankaccount().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBankaccounts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Bankaccount o", Long.class).getSingleResult();
    }

	public static List<Bankaccount> findAllBankaccounts() {
        return entityManager().createQuery("SELECT o FROM Bankaccount o", Bankaccount.class).getResultList();
    }

	public static Bankaccount findBankaccount(Long id) {
        if (id == null) return null;
        return entityManager().find(Bankaccount.class, id);
    }

	public static List<Bankaccount> findBankaccountEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Bankaccount o", Bankaccount.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
