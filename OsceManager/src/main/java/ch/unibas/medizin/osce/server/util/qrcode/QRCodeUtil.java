package ch.unibas.medizin.osce.server.util.qrcode;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceSettings;
import ch.unibas.medizin.osce.domain.Student;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.server.util.file.PdfUtil;
import ch.unibas.medizin.osce.shared.QRCodeType;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class QRCodeUtil extends PdfUtil  {

	
	private static Logger Log = Logger.getLogger(QRCodeUtil.class);
	
	protected static PdfWriter writer;
	
	public static final String EXAM_START_TAG="<exam>";
	
	public static final String EXAM_END_TAG="</exam>";
	
	public static final String EXAM_ID_START_TAG="<exam-id>";
	
	public static final String EXAM_ID_END_TAG="</exam-id>";
	
	public static final String EXAMINER_ID_START_TAG="<examiner-id>";
	
	public static final String EXAMINER_ID_END_TAG="</examiner-id>";
	
	public static final String STUDENT_START_TAG="<student>";
	
	public static final String STUDENT_END_TAG="</student>";
	
	public static final String ID_START_TAG="<id>";
	
	public static final String ID_END_TAG="</id>";
	
	public static final String STUDENT_ID_START_TAG="<student-id>";
	
	public static final String STUDENT_ID_END_TAG="</student-id>";
	
	protected static Font paraFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	
	/*
	 * This method will do symmetric encryption of url and pass the encrypted url to generate QR code
	 */
	public static String generateQRCodeForChecklist(String  url, Long checklistId, String locale,ByteArrayOutputStream os, HttpSession session) {
	
		CheckList checkList = CheckList.findCheckList(checklistId);
		try {
			//Save the propery list
			QRCodePlist qrCodePlist=new QRCodePlist();
			url = url + OsMaFilePathConstant.EXTRA_SPACE_QR;
			java.io.ByteArrayOutputStream encryptedBytes = Encryptor.encryptFile(OsMaFilePathConstant.getSymmetricKey(),url.getBytes());
			String base64String = Base64.encodeBase64String(encryptedBytes.toByteArray());
			
			qrCodePlist.setQrCodeType(QRCodeType.CHECKLIST_URL_QR_CODE);
			qrCodePlist.setData(base64String);
			
			String plistString = generatePlistFile(qrCodePlist);
			//put data into plist
			if(plistString != null){
				
				Image checklistQRImage = generateQRCode(plistString);
				Document qrCodeChecklist = new Document();
				Paragraph checklistName = new Paragraph();
				
				PdfPTable table = new PdfPTable(1); //1 columns.
				PdfPCell cell1 = new PdfPCell(new Paragraph(new Chunk(checkList.getTitle(),paraFont)));
				cell1.setPaddingLeft(24);
		        PdfPCell cell2 = new PdfPCell(checklistQRImage, true);

		        cell1.setBorder(Rectangle.NO_BORDER);
		        cell2.setBorder(Rectangle.NO_BORDER);
		        table.addCell(cell1);
		        table.addCell(cell2);
	
				PdfWriter.getInstance(qrCodeChecklist, os);
				qrCodeChecklist.open();

				qrCodeChecklist.add(table);
				qrCodeChecklist.close();
			}
	
		} catch (Exception e) {
			Log.error(e.getMessage(),e);
		}
		String fileName = checkList.getTitle() + "_QR.pdf";
		fileName = fileName.replaceAll(" ", "");
		return fileName;
	}
	/*
	 * This method will generate plist for data passed
	 */
	public static String generatePlistFile(QRCodePlist qrCodePlist) {
		try {
			
			//Creating the root object
			NSDictionary root = new NSDictionary();
			root.put("type", qrCodePlist.getQrCodeType().ordinal());
			
			if(qrCodePlist.getNsDictionary() != null){
				root.put("data",qrCodePlist.getNsDictionary());
			}else{
				root.put("data", qrCodePlist.getData());
			}
			//Save the propery list

			File file = File.createTempFile("QRPlist", "plist");
			PropertyListParser.saveAsXML(root, file);
			String plistString = FileUtils.readFileToString(file);
			FileUtils.deleteQuietly(file);
			return plistString;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/*
	 *This method will generate QR code of encrypted URL and will return the image. 
	 */
	public static Image generateQRCode(String symmetricUrl) throws BadElementException {
		int qrCodeWidth = Integer.parseInt(OsMaFilePathConstant.getQRCodeWidth());
		int qrCodeHeight = Integer.parseInt(OsMaFilePathConstant.getQRCodeHeight());
		BarcodeQRCode qrBarCode = new  BarcodeQRCode(symmetricUrl,qrCodeWidth,qrCodeHeight, null);
		Image qr_image = qrBarCode.getImage();
		return qr_image;
	}

	/*
	 *This method will generate QR code for settings xml  
	 */
	public static String generateQRCodeForSettings(String settingsXml,Long settingsId, String locale, ByteArrayOutputStream os, HttpSession session) {
		try {
			QRCodePlist qrCodePlist=new QRCodePlist();
			OsceSettings osceSettings=OsceSettings.findOsceSettings(settingsId);
			Osce osce = osceSettings.getOsce();
			
			qrCodePlist.setQrCodeType(QRCodeType.SETTING_QR_CODE);
			settingsXml = settingsXml + OsMaFilePathConstant.EXTRA_SPACE_QR;
			java.io.ByteArrayOutputStream encryptedBytes = Encryptor.encryptFile(OsMaFilePathConstant.getSymmetricKey(),settingsXml.getBytes());
			String base64String = Base64.encodeBase64String(encryptedBytes.toByteArray());
			qrCodePlist.setData(base64String);
			
			String plistString = generatePlistFile(qrCodePlist);
			
			//put data into plist
			if(plistString != null){

				OsceConstantsWithLookup constants = GWTI18N.create(OsceConstantsWithLookup.class);
				Image settingsQRImage = generateQRCode(plistString);
				Document qrCodeSettings = new Document();
				
				String fileName = osce.getSemester().getSemester().toString() 
						+ osce.getSemester().getCalYear().toString().substring(2, osce.getSemester().getCalYear().toString().length()) 
						+ "-" + (constants.getString(osce.getStudyYear().toString()).replace(".", "")); 
				
				 PdfPTable table = new PdfPTable(1); //1 columns.
				 PdfPCell cell1 = new PdfPCell(new Paragraph(new Chunk(fileName,paraFont)));
				 cell1.setPaddingLeft(15);
		         PdfPCell cell2 = new PdfPCell(settingsQRImage, true);

		         cell1.setBorder(Rectangle.NO_BORDER);
		         cell2.setBorder(Rectangle.NO_BORDER);
		         table.addCell(cell1);
		         table.addCell(cell2);
				 PdfWriter.getInstance(qrCodeSettings, os);

				 qrCodeSettings.open();
				 qrCodeSettings.add(table);
				 qrCodeSettings.close();
			}  
		}catch (Exception e) {
			Log.error(e.getMessage(),e);
		}
		return "SettingsQRCode.pdf";
	}
	public static String generateExaminerQRCode(Long osceId,List<Long> examinersList, String locale, ByteArrayOutputStream os,HttpSession session) {
		try {
			Document qrCodeExaminer = new Document();
			PdfWriter.getInstance(qrCodeExaminer, os);
			QRCodePlist qrCodePlist=new QRCodePlist();
			qrCodePlist.setQrCodeType(QRCodeType.EXAMINER_QR_CODE);
			qrCodeExaminer.open();
			
			for (Long examinerId : examinersList) {
				
				NSDictionary examinerNsDictionary=new NSDictionary();
				examinerNsDictionary.put("examinerId", (examinerId == null ? "" : examinerId.toString()));
				examinerNsDictionary.put("examId",(osceId == null ? "" : osceId.toString()));
				qrCodePlist.setNsDictionary(examinerNsDictionary);

				String plistString = generatePlistFile(qrCodePlist);
				//put data into plist
				if(plistString != null){
					Image examinerQRImage = generateQRCode(plistString);
					qrCodeExaminer.add(examinerQRImage);
					qrCodeExaminer.newPage();
				}  
			}
			qrCodeExaminer.close();
		}catch (Exception e) {
			Log.error(e.getMessage(),e);
		}
		return "ExaminerQR.pdf" ;
	}
	/*private static String generateExaminerXml(Long osceId, Long examinerId) {

		String data=EXAM_START_TAG ;
		data+= EXAM_ID_START_TAG + osceId +EXAM_ID_END_TAG;
		data+= EXAMINER_ID_START_TAG + examinerId + EXAMINER_ID_END_TAG ;
		data+=  EXAM_END_TAG;
		
		return data;
	}*/
	public static String generateStudentQRCode(Long osceId,List<Long> studentList, String locale, ByteArrayOutputStream os,HttpSession session) {
		try {
			Document qrCodeStudent = new Document();
			PdfWriter.getInstance(qrCodeStudent, os);
			QRCodePlist qrCodePlist=new QRCodePlist();
			qrCodePlist.setQrCodeType(QRCodeType.STUDENT_QR_CODE);
			qrCodeStudent.open();
			
			for (Long studId : studentList) {
				Student student = Student.findStudent(studId);
				
				NSDictionary examinerNsDictionary=new NSDictionary();
				examinerNsDictionary.put("studentId", (student.getId() == null ? "" : student.getId().toString()));
				examinerNsDictionary.put("examId",(osceId == null ? "" : osceId.toString()));
				qrCodePlist.setNsDictionary(examinerNsDictionary);
				
				String plistString = generatePlistFile(qrCodePlist);
					
				//put data into plist
				if(plistString != null){
					Image studentQRImage = generateQRCode(plistString);
					qrCodeStudent.add(studentQRImage);
					qrCodeStudent.newPage();
				}  
			}
			qrCodeStudent.close();
		}catch (Exception e) {
			Log.error(e.getMessage(),e);
		}
		return "StudentQR.pdf" ;
	}
	/*private static String generateStudentXml(Long studId, String studentToOsceId) {
		
		String data=STUDENT_START_TAG;
		data+= ID_START_TAG + studentToOsceId +  ID_END_TAG;
		data+= STUDENT_ID_START_TAG + studId + STUDENT_ID_END_TAG;
		data+=  STUDENT_END_TAG;
		return data;
	}*/

		public static BufferedImage toBufferedImage(java.awt.Image img) {
		if (img instanceof BufferedImage) {
	        return (BufferedImage) img;
	    }
		int qrCodeWidth = Integer.parseInt(OsMaFilePathConstant.getQRCodeWidth());
		int qrCodeHeight = Integer.parseInt(OsMaFilePathConstant.getQRCodeHeight());
		 // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_ARGB);
	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0,qrCodeWidth,qrCodeHeight, null);
	    bGr.dispose();
	    // Return the buffered image
	    return bimage;
	}
}