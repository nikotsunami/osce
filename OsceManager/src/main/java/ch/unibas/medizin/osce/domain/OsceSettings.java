package ch.unibas.medizin.osce.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.server.util.settingsbean.Settings;
import ch.unibas.medizin.osce.server.util.settingsbean.Settings.Credentials;
import ch.unibas.medizin.osce.server.util.settingsbean.Settings.OtherInformation;
import ch.unibas.medizin.osce.server.util.settingsbean.Settings.OtherInformation.BackupPeriod;
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

	public static String exportOsceSettingsXml(Long settingsId, ByteArrayOutputStream os) {
		
		
		String fileName = "";
		
		try {
			ch.unibas.medizin.osce.server.util.settingsbean.ObjectFactory objectFactory=new ch.unibas.medizin.osce.server.util.settingsbean.ObjectFactory();
			Settings settingsData=objectFactory.createSettings();
			settingsData.setVersion(1.1f);
			OsceSettings osceSettings = findOsceSttingsForId(settingsId);
			
			//Credentials
			Credentials osceSettingsCredentials = objectFactory.createSettingsCredentials();
			settingsData.setCredentials(osceSettingsCredentials);
			
			osceSettingsCredentials.setType(osceSettings.getInfotype().ordinal());
			osceSettingsCredentials.setUsername(osceSettings.getUsername());
			osceSettingsCredentials.setPassword(osceSettings.getPassword());
			osceSettingsCredentials.setBucketname(osceSettings.getBucketName());
			
			if(osceSettings.getInfotype().equals(BucketInfoType.S3)){
				osceSettingsCredentials.setHostname("");
			}
			if(osceSettings.getInfotype().equals(BucketInfoType.FTP)){
				if(osceSettings.getHost() != null){
					osceSettingsCredentials.setHostname(osceSettings.getHost());	
				}
			}

			osceSettingsCredentials.setUrl(null);
			
			//Other information
			
			OtherInformation otherInformation=objectFactory.createSettingsOtherInformation();
			settingsData.setOtherInformation(otherInformation);
			otherInformation.setSettingPassword(osceSettings.getSettingPassword());
			
			BackupPeriod backupPeriod=objectFactory.createSettingsOtherInformationBackupPeriod();
			if(osceSettings.getBackupPeriod() != null){
				backupPeriod.setTime(osceSettings.getBackupPeriod());
			}
			backupPeriod.setUnit(osceSettings.getTimeunit().ordinal());
			otherInformation.setBackupPeriod(backupPeriod);

			if(osceSettings.getNextExaminee() != null){
				otherInformation.setPointNextExaminee(osceSettings.getNextExaminee().toString());
			}
			otherInformation.setSignMechanism(osceSettings.getEncryptionType().ordinal());
			otherInformation.setSymmetricKey(osceSettings.getSymmetricKey());
			if(osceSettings.getReviewMode() != null){
				otherInformation.setReviewmode(osceSettings.getReviewMode().toString());	
			}
			settingsData.setOtherInformation(otherInformation);
			fileName = "Settings.xml"; 

			 getnerateXMLFile(fileName, settingsData, os);
			
		} catch (Exception e) {
		}
		return fileName;
	}
	
	public static String getnerateXMLFile(String fileName,Settings osceSettings, org.apache.commons.io.output.ByteArrayOutputStream os){
		
		try {
				
				JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		 
				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		 
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				jaxbMarshaller.marshal(osceSettings, stream);
				
				
				String data = new String(stream.toByteArray(),"UTF-8");
				
				os.write(data.getBytes("UTF-8"));
				return data;
			}catch (Exception e) {
				e.printStackTrace();
		}
		return "";
	}
}
