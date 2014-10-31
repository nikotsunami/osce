package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.EncryptionType;
import ch.unibas.medizin.osce.shared.TimeUnit;

@RooJavaBean
@RooToString
@RooEntity
public class OsceSettings {
	
	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	private BucketInfoType infotype;
	
	private String host;
	
	private String username;
	
	private String password;
	
	private String bucketName;
	
	private String settingPassword;
	
	private Integer backupPeriod;
	
	private TimeUnit timeunit;
	
	private EncryptionType encryptionType;
	
	@Size(max=5000)
	private String screenSaverText;
	
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean nextExaminee;  
	 
	private String symmetricKey;
	
	@OneToOne
	private Osce osce;

	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean reviewMode;  
	
	public static OsceSettings findOsceSettingsByOsce(Long osceId) {
		EntityManager em = entityManager();
		String sql = "SELECT os from OsceSettings os where os.osce.id = " + osceId;
		TypedQuery<OsceSettings> query = em.createQuery(sql,OsceSettings.class);
		List<OsceSettings> resultList = query.getResultList();

		if (resultList != null && resultList.isEmpty() == false)
			return resultList.get(0);
		else 
			return null;
	}
	public static OsceSettings findOsceSttingsForId(Long osceSettingsId) {
		EntityManager em = entityManager();
		String sql = "SELECT os from OsceSettings os where os.id = " + osceSettingsId;
		TypedQuery<OsceSettings> query = em.createQuery(sql,OsceSettings.class);
		List<OsceSettings> resultList = query.getResultList();

		if (resultList != null && resultList.isEmpty() == false)
			return resultList.get(0);
		else 
			return null;
	}
}
