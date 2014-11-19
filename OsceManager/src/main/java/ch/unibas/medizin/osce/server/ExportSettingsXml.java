package ch.unibas.medizin.osce.server;

import static org.apache.commons.lang.StringUtils.defaultString;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.OsceSettings;
import ch.unibas.medizin.osce.server.util.settingsbean.Settings;
import ch.unibas.medizin.osce.server.util.settingsbean.Settings.Credentials;
import ch.unibas.medizin.osce.server.util.settingsbean.Settings.OtherInformation;
import ch.unibas.medizin.osce.server.util.settingsbean.Settings.OtherInformation.BackupPeriod;
import ch.unibas.medizin.osce.shared.BucketInfoType;

public class ExportSettingsXml {

	private static Logger Log = Logger.getLogger(ExportSettingsXml.class);
	
	public ExportSettingsXml() {
	}


	public static String createSettingsXmlFile(ByteArrayOutputStream os, Long osceSettingsId) {
		String fileName = "";
		try {
			OsceSettings oscesettings = OsceSettings.findOsceSttingsForId(osceSettingsId);
			byte[] bytes = new ExportSettingsXml().generateXmlFileByOsceSettingsId(osceSettingsId, oscesettings);
			os.write(bytes);
			fileName = "Settings.xml";
			
			return fileName;
		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
			return fileName;
		}
	}
	
	public byte[] generateXmlFileByOsceSettingsId(Long osceId, OsceSettings osceSettings) {
		try {
			ch.unibas.medizin.osce.server.util.settingsbean.ObjectFactory objectFactory=new ch.unibas.medizin.osce.server.util.settingsbean.ObjectFactory();
			Settings settingsData=objectFactory.createSettings();
			settingsData.setVersion(1.1f);
			
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
			otherInformation.setScreenSaverText(osceSettings.getScreenSaverText() == null ? "" : defaultString(osceSettings.getScreenSaverText()));
			if(osceSettings.getScreenSaverTime() != null){
				otherInformation.setScreenSaverTime(osceSettings.getScreenSaverTime());
			}
			if(osceSettings.getAutoSelection() != null){
				otherInformation.setAutoSelection(osceSettings.getAutoSelection().toString());
			}
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
			
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
	 
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			jaxbMarshaller.marshal(settingsData, stream);
			
			String data = new String(stream.toByteArray(),"UTF-8");
			
			return data.getBytes();

		}
		catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
	}


}

