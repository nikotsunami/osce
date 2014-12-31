package ch.unibas.medizin.osce.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import ch.unibas.medizin.osce.server.ExportSettingsXml;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.util.qrcode.Encryptor;
import ch.unibas.medizin.osce.server.util.qrcode.QRCodePlist;
import ch.unibas.medizin.osce.server.util.qrcode.QRCodeUtil;
import ch.unibas.medizin.osce.shared.BucketInfoType;
import ch.unibas.medizin.osce.shared.EncryptionType;
import ch.unibas.medizin.osce.shared.QRCodeType;
import ch.unibas.medizin.osce.shared.TimeUnit;
import com.itextpdf.text.pdf.BarcodeQRCode;

@Entity
@Configurable
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
	
	private Integer screenSaverTime;
	
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean autoSelection;  
	
	@Size(max=5000)
	private String webServicePath;
	
	@Size(max=5000)
	private String registerDevicePath;
	
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
	
	public static String createSettingsQRImageById(Long osceSttingsId){
		
		QRCodePlist qrCodePlist=new QRCodePlist();
		QRCodeUtil qrCodeUtil=new QRCodeUtil();
		
		String qrCodeBase64="";
		qrCodePlist.setQrCodeType(QRCodeType.SETTING_QR_CODE);
		ByteArrayOutputStream newOs = new ByteArrayOutputStream();
		ExportSettingsXml.createSettingsXmlFile(newOs, osceSttingsId);
		String settingsXml = new String(newOs.toByteArray());
		
		settingsXml = settingsXml + OsMaFilePathConstant.EXTRA_SPACE_QR;
		java.io.ByteArrayOutputStream encryptedBytes;
		try {
			encryptedBytes = Encryptor.encryptFile(OsMaFilePathConstant.getSymmetricKey(),settingsXml.getBytes());
			String base64String = Base64.encodeBase64String(encryptedBytes.toByteArray());
			qrCodePlist.setData(base64String);
			
			String plistString = qrCodeUtil.generatePlistFile(qrCodePlist);
			//put data into plist
			if(plistString != null){
				int qrCodeWidth = Integer.parseInt(OsMaFilePathConstant.getQRCodeWidth());
				int qrCodeHeight = Integer.parseInt(OsMaFilePathConstant.getQRCodeHeight());
				BarcodeQRCode qrBarCode = new  BarcodeQRCode(plistString,qrCodeWidth,qrCodeHeight, null);
				java.awt.Image awtImage = qrBarCode.createAwtImage(Color.BLACK, Color.WHITE);
				BufferedImage bufferedImage = qrCodeUtil.toBufferedImage(awtImage);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write( bufferedImage, "png", baos );
				byte[] imageInByte = baos.toByteArray();
				
				qrCodeBase64 = Base64.encodeBase64String(imageInByte);
				baos.flush();
				baos.close();
				return qrCodeBase64;
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}
	return "";
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
            OsceSettings attached = OsceSettings.findOsceSettings(this.id);
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
    public OsceSettings merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        OsceSettings merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new OsceSettings().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countOsceSettingses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OsceSettings o", Long.class).getSingleResult();
    }

	public static List<OsceSettings> findAllOsceSettingses() {
        return entityManager().createQuery("SELECT o FROM OsceSettings o", OsceSettings.class).getResultList();
    }

	public static OsceSettings findOsceSettings(Long id) {
        if (id == null) return null;
        return entityManager().find(OsceSettings.class, id);
    }

	public static List<OsceSettings> findOsceSettingsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OsceSettings o", OsceSettings.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public BucketInfoType getInfotype() {
        return this.infotype;
    }

	public void setInfotype(BucketInfoType infotype) {
        this.infotype = infotype;
    }

	public String getHost() {
        return this.host;
    }

	public void setHost(String host) {
        this.host = host;
    }

	public String getUsername() {
        return this.username;
    }

	public void setUsername(String username) {
        this.username = username;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public String getBucketName() {
        return this.bucketName;
    }

	public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

	public String getSettingPassword() {
        return this.settingPassword;
    }

	public void setSettingPassword(String settingPassword) {
        this.settingPassword = settingPassword;
    }

	public Integer getBackupPeriod() {
        return this.backupPeriod;
    }

	public void setBackupPeriod(Integer backupPeriod) {
        this.backupPeriod = backupPeriod;
    }

	public TimeUnit getTimeunit() {
        return this.timeunit;
    }

	public void setTimeunit(TimeUnit timeunit) {
        this.timeunit = timeunit;
    }

	public EncryptionType getEncryptionType() {
        return this.encryptionType;
    }

	public void setEncryptionType(EncryptionType encryptionType) {
        this.encryptionType = encryptionType;
    }

	public String getScreenSaverText() {
        return this.screenSaverText;
    }

	public void setScreenSaverText(String screenSaverText) {
        this.screenSaverText = screenSaverText;
    }

	public Boolean getNextExaminee() {
        return this.nextExaminee;
    }

	public void setNextExaminee(Boolean nextExaminee) {
        this.nextExaminee = nextExaminee;
    }

	public String getSymmetricKey() {
        return this.symmetricKey;
    }

	public void setSymmetricKey(String symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

	public Osce getOsce() {
        return this.osce;
    }

	public void setOsce(Osce osce) {
        this.osce = osce;
    }

	public Boolean getReviewMode() {
        return this.reviewMode;
    }

	public void setReviewMode(Boolean reviewMode) {
        this.reviewMode = reviewMode;
    }

	public Integer getScreenSaverTime() {
        return this.screenSaverTime;
    }

	public void setScreenSaverTime(Integer screenSaverTime) {
        this.screenSaverTime = screenSaverTime;
    }

	public Boolean getAutoSelection() {
        return this.autoSelection;
    }

	public void setAutoSelection(Boolean autoSelection) {
        this.autoSelection = autoSelection;
    }
	
	public String getWebServicePath() {
		return webServicePath;
	}
	public String getRegisterDevicePath() {
		return registerDevicePath;
	}
	
	public void setRegisterDevicePath(String registerDevicePath) {
		this.registerDevicePath = registerDevicePath;
	}
	
	public void setWebServicePath(String webServicePath) {
		this.webServicePath = webServicePath;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AutoSelection: ").append(getAutoSelection()).append(", ");
        sb.append("BackupPeriod: ").append(getBackupPeriod()).append(", ");
        sb.append("BucketName: ").append(getBucketName()).append(", ");
        sb.append("EncryptionType: ").append(getEncryptionType()).append(", ");
        sb.append("Host: ").append(getHost()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Infotype: ").append(getInfotype()).append(", ");
        sb.append("NextExaminee: ").append(getNextExaminee()).append(", ");
        sb.append("Osce: ").append(getOsce()).append(", ");
        sb.append("Password: ").append(getPassword()).append(", ");
        sb.append("ReviewMode: ").append(getReviewMode()).append(", ");
        sb.append("ScreenSaverText: ").append(getScreenSaverText()).append(", ");
        sb.append("ScreenSaverTime: ").append(getScreenSaverTime()).append(", ");
        sb.append("SettingPassword: ").append(getSettingPassword()).append(", ");
        sb.append("SymmetricKey: ").append(getSymmetricKey()).append(", ");
        sb.append("Timeunit: ").append(getTimeunit()).append(", ");
        sb.append("Username: ").append(getUsername()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }
}
