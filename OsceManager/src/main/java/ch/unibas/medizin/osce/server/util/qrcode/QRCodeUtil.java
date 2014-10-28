package ch.unibas.medizin.osce.server.util.qrcode;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.util.file.PdfUtil;

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
			String symmetricUrl= Base64.encodeBase64String(Encryptor.encryptFile(OsMaFilePathConstant.getSymmetricKey(), url.getBytes()).toByteArray());
			
			Image checklistQRImage = generateQRCode(symmetricUrl);
			
			Document qrCodeChecklist = new Document();
			
			PdfWriter writer = PdfWriter.getInstance(qrCodeChecklist, os);
			qrCodeChecklist.open();
			
			qrCodeChecklist.add(checklistQRImage);
			
			
			qrCodeChecklist.close();
	
		} catch (Exception e) {
			Log.error(e.getMessage(),e);
		}
		return "ChecklistQRCode.pdf";
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
	
}
