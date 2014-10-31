package ch.unibas.medizin.osce.server.util.qrcode;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.util.file.PdfUtil;
import ch.unibas.medizin.osce.shared.QRCodeType;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;


public class QRCodeUtil extends PdfUtil  {

	
	private static Logger Log = Logger.getLogger(QRCodeUtil.class);
	
	protected static PdfWriter writer;
	
	/*
	 * This method will do symmetric encryption of url and pass the encrypted url to generate QR code
	 */
	public static String generateQRCodeForChecklist(String  url, String locale,ByteArrayOutputStream os, HttpSession session) {
	
		try {
			
			//Save the propery list
			QRCodePlist qrCodePlist=new QRCodePlist();
			qrCodePlist.setData(url);
			qrCodePlist.setQrCodeType(QRCodeType.CHECKLIST_URL_QR_CODE);
			
			byte[] plistBytes = generatePlistFile(qrCodePlist);
			//put data into plist
			if(plistBytes != null){
				java.io.ByteArrayOutputStream encryptedBytes = Encryptor.encryptFile(OsMaFilePathConstant.getSymmetricKey(),plistBytes);
				
				String base64String = Base64.encodeBase64String(encryptedBytes.toByteArray());
				
				Image checklistQRImage = generateQRCode(base64String);
				Document qrCodeChecklist = new Document();
				
				PdfWriter writer = PdfWriter.getInstance(qrCodeChecklist, os);
				
				qrCodeChecklist.open();
				qrCodeChecklist.add(checklistQRImage);
				qrCodeChecklist.close();
			}
	
		} catch (Exception e) {
			Log.error(e.getMessage(),e);
		}
		return "ChecklistQRCode.pdf";
	}
	/*
	 * This method will generate plist for data passed
	 */
	public static byte[] generatePlistFile(QRCodePlist qrCodePlist) {
		try {
			
			//Creating the root object
			NSDictionary root = new NSDictionary();

			root.put("type", qrCodePlist.getQrCodeType().ordinal());
			root.put("data", qrCodePlist.getData());
			//Save the propery list

			File file = File.createTempFile("QRPlist", "plist");
			PropertyListParser.saveAsXML(root, file);
			String plistString = FileUtils.readFileToString(file);
			plistString= plistString + OsMaFilePathConstant.EXTRA_SPACE_QR;
			byte[] bytes = plistString.getBytes();
			
			FileUtils.deleteQuietly(file);
			return bytes;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/*
	 *This method will generate QR code of encrypted URL and will return the image. 
	 */
	private static Image generateQRCode(String symmetricUrl) throws BadElementException {
		int qrCodeWidth = Integer.parseInt(OsMaFilePathConstant.getQRCodeWidth());
		int qrCodeHeight = Integer.parseInt(OsMaFilePathConstant.getQRCodeHeight());
		BarcodeQRCode qrBarCode = new  BarcodeQRCode(symmetricUrl,qrCodeWidth,qrCodeHeight, null);
		
		Image qr_image = qrBarCode.getImage();
		return qr_image;
	}

	/*
	 *This method will generate QR code for settings xml  
	 */
	public static String generateQRCodeForSettings(String settingsXml,String locale, ByteArrayOutputStream os, HttpSession session) {
		try {
			QRCodePlist qrCodePlist=new QRCodePlist();
			qrCodePlist.setData(settingsXml);
			qrCodePlist.setQrCodeType(QRCodeType.SETTING_QR_CODE);
			byte[] plistBytes = generatePlistFile(qrCodePlist);
			
			//put data into plist
			if(plistBytes != null){
				java.io.ByteArrayOutputStream encryptedBytes = Encryptor.encryptFile(OsMaFilePathConstant.getSymmetricKey(),plistBytes);
				
				String base64String = Base64.encodeBase64String(encryptedBytes.toByteArray());
				Image settingsQRImage = generateQRCode(base64String);
				Document qrCodeSettings = new Document();
				
				PdfWriter writer = PdfWriter.getInstance(qrCodeSettings, os);
				qrCodeSettings.open();
				
				qrCodeSettings.add(settingsQRImage);
				qrCodeSettings.close();
			}  
		}catch (Exception e) {
			Log.error(e.getMessage(),e);
		}
		return "SettingsQRCode.pdf";
	}
}