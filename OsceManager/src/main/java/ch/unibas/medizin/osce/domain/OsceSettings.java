package ch.unibas.medizin.osce.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

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
	
	private Integer screenSaverTime;
	
	@Value("false")
	@Column(columnDefinition="BIT", length = 1)
	private Boolean autoSelection;  
	
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
}
