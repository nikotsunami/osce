package ch.unibas.medizin.osce.server.util.file;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.shared.Semesters;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class StandardizedPatientPaymentUtil {

	private static final Logger log = Logger
			.getLogger(StandardizedPatientPaymentUtil.class);

	

	private final Locale locale;
	private final OsceConstants constants;
	private final List<StandardizedPatient> spList;
	private final ByteArrayOutputStream os;
	private final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	private final String fileName;
	private final Semester semester;
	public StandardizedPatientPaymentUtil(List<StandardizedPatient> list,
			ByteArrayOutputStream os,HttpSession session,Long semesterId) throws IOException {
		this.spList = list;
		this.os = os;
		this.locale = new Locale("de");
		this.constants = GWTI18N.create(OsceConstants.class, locale.toString());
		this.fileName = session.getServletContext().getRealPath(OsMaFilePathConstant.appStandardizedPatientPaymentPDF);
		//Added for OMS-152.
		//changed for OMS-160.
		if(semesterId!=null){
			this.semester=Semester.findSemester(semesterId);
		}else{
			this.semester=null;
		}
	}

	public String createPDF() {

		// create main pdf
		final Document document = new Document();
		final List<PdfReader> readers = new ArrayList<PdfReader>();
		int totalPages = 0;

		for (StandardizedPatient sp : spList) {
			try {
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				final PdfReader reader = new PdfReader(fileName);
				final PdfStamper stamp = new PdfStamper(reader, out);
				final AcroFields form = stamp.getAcroFields();
				final Map<String, String> valueMap = createValueMap(sp);

				setFields(form, valueMap);
				resetPDF(form);
				genratePDF(stamp, out);
				
				final PdfReader reader2 = new PdfReader(out.toByteArray());
				readers.add(reader2);
				totalPages += reader2.getNumberOfPages();
			} catch (DocumentException e) {
				log.error(e.getMessage(), e);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}

		}
		createFinalPDF(readers, totalPages, document);

		return PDF_NAME;
	}

	private void createFinalPDF(List<PdfReader> readers, int totalPages,
			Document document) {
		try {
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			//int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					//currentPageNumber++;
					page = writer.getImportedPage(pdfReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
//					boolean paginate = true;
//					if (paginate) {
//						cb.beginText();
//						cb.setFontAndSize(bf, 9);
//						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
//								+ currentPageNumber + " of " + totalPages, 520,
//								5, 0);
//						cb.endText();
//					}
				}
				pageOfCurrentReaderPDF = 0;
			}
			os.flush();
			document.close();
			os.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (os != null)
					os.close();
			} catch (IOException ioe) {
				log.error(ioe.getMessage(), ioe);
			}
		}
	}

	private Map<String, String> createValueMap(StandardizedPatient sp) {
		final Map<String, String> valueMap = new HashMap<String, String>();

		valueMap.put(INSTITUTE, constants.paymentInstitute());
		valueMap.put(NAME, constants.paymentName());

		String fullName = "";
		if (sp.getGender() != null) {
			switch (sp.getGender()) {
			case MALE: {
				valueMap.put(MR, CHECKED);
				fullName += "Mr. ";
				break;
			}
			case FEMALE: {
				valueMap.put(MRS, CHECKED);
				fullName += "Mrs. ";
				break;
			}
			default: {
				log.error("Not a valid gender");
				break;
			}
			}
		}

		if (isNotBlank(sp.getPreName())) {
			fullName += sp.getPreName() + " ";
		}

		if (isNotBlank(sp.getName())) {
			fullName += sp.getName();
		}

		if (isNotBlank(fullName)) {
			valueMap.put(PAYEE_NAME, fullName);
		}

		if (isNotBlank(sp.getStreet())) {
			valueMap.put(PAYEE_ADDRESS, sp.getStreet());
		}

		// place
		String place = "";
		/*if (sp.getNationality() != null && isNotBlank(sp.getNationality().getNationality())) {
			place += sp.getNationality().getNationality() + ", ";
		}*/
		if (isNotBlank(sp.getPostalCode())) {
			place += sp.getPostalCode() + ", ";
		}
		if (isNotBlank(sp.getCity())) {
			place += sp.getCity();
		}
		if (isNotBlank(place)) {
			valueMap.put(PAYEE_PLACE, place);
		}
		// phone number
		String phoneNumber = "";
		if (isNotBlank(sp.getTelephone())) {
			phoneNumber += sp.getTelephone() + ", ";
		}
		if (isNotBlank(sp.getTelephone2())) {
			phoneNumber += sp.getTelephone2() + ", ";
		}
		if (isNotBlank(sp.getMobile())) {
			phoneNumber += sp.getMobile();
		}
		if (isNotBlank(phoneNumber)) {
			valueMap.put(PAYEE_PHONE, phoneNumber);
		}
		if (sp.getBirthday() != null) {
			valueMap.put(PAYEE_DOB, format.format(sp.getBirthday()));
		}

		if (sp.getNationality() != null) {
			valueMap.put(PAYEE_NATIONALITY, sp.getNationality()
					.getNationality());
		}

		if (isNotBlank(sp.getEmail())) {
			valueMap.put(PAYEE_EMAIL, sp.getEmail());
		}

		if (sp.getMaritalStatus() != null) {

			switch (sp.getMaritalStatus()) {
			case UNMARRIED:
				valueMap.put(SINGLE, CHECKED);
				break;
			case MARRIED:
				valueMap.put(MARRIED, CHECKED);
				break;

			case CIVIL_UNION:
				valueMap.put(REG_PARTNERSHIP, CHECKED);
				break;

			case DIVORCED:
				valueMap.put(DIVORCED, CHECKED);
				break;

			case SEPARATED:
				valueMap.put(SEPARATED, CHECKED);
				break;

			case WIDOWED:
				valueMap.put(WIDOWED, CHECKED);
				break;

			default:
				log.error("Not a valid status");
				break;
			}
		}

		//status
//		valueMap.put(SCHOLAR, CHECKED);
//		valueMap.put(STUDENT, CHECKED);

		if (isNotBlank(sp.getSocialInsuranceNo())) {
			valueMap.put(SOCIAL_SECURITY_NUMBER, sp.getSocialInsuranceNo());
		}

		if (sp.getWorkPermission() != null) {
			switch (sp.getWorkPermission()) {
			case B:
				valueMap.put(B_PERMIT, CHECKED);
				break;
			case L:
				valueMap.put(L_PERMIT, CHECKED);
				break;
			case C:
				valueMap.put(C_PERMIT, CHECKED);
				break;
			default:
				log.error("not a valid work permission");
				break;
			}
		}

		// valueMap.put(SELF_EMPLOYED_IN_SWITERLAND, CHECKED);

		// valueMap.put(DATES_WORKED_FROM, "27.11.2011");
		// valueMap.put(DATES_WORKED_TO, "26.11.2012");
		//Added code for OMS-152.
		//changed for OMS-160.
		if(semester!=null){
			if(semester.getSemester().equals(Semesters.FED)){
				valueMap.put(SERVICE_CLAIMED, "Simulationspatient Eidgenössische Schlussprüfung Medizin " + semester.getCalYear());	
			}else if(semester.getSemester().equals(Semesters.HS) || semester.getSemester().equals(Semesters.FS)){
				valueMap.put(SERVICE_CLAIMED, "Simulationspatient OSCE " + semester.getSemester() + " " + semester.getCalYear());
			}
		}
		 //Added code for OMS-152.
		 valueMap.put(AMOUNT_IN, "CHF");
		// valueMap.put(DATE_1, "26.11.2012");
		// valueMap.put(AMOUNT_1, "100");
		// valueMap.put(COST_CENTRE_1, "center 1");
		// valueMap.put(DATE_2, "26.11.2012");
		// valueMap.put(AMOUNT_2, "200");
		//Added code for OMS-152.
		 //changed for OMS-160.
		 if(semester!=null){
			 if(semester.getSemester().equals(Semesters.FED)){
				 valueMap.put(COST_CENTRE_1, "3MX 1191");
				}else if(semester.getSemester().equals(Semesters.HS) || semester.getSemester().equals(Semesters.FS)){
					valueMap.put(COST_CENTRE_1, "MX 1102");
				}
		 }

		// valueMap.put(COST_CENTRE_2, "center 2");
		// valueMap.put(TOTAL, "unknown");

		if (sp.getBankAccount() != null) {
			Bankaccount bankAccount = sp.getBankAccount();

			if (isNotBlank(bankAccount.getBankName())) {
				valueMap.put(NAME_OF_BANK, bankAccount.getBankName());
			}

			String bankAddress = "";
			if (bankAccount.getCountry() != null
					&& isNotBlank(bankAccount.getCountry()
							.getNationality())) {
				bankAddress += bankAccount.getCountry().getNationality() + ", ";
			}

			if (isNotBlank(bankAccount.getPostalCode())) {
				bankAddress += bankAccount.getPostalCode() + ", ";
			}

			if (isNotBlank(bankAccount.getCity())) {
				bankAddress += bankAccount.getCity();
			}

			if (isNotBlank(bankAddress)) {
				valueMap.put(BANK_ADDRESS, bankAddress);
			}

			if (isNotBlank(bankAccount.getOwnerName())) {
				valueMap.put(ACCOUNT_HOLDER, bankAccount.getOwnerName());
			}

			if (isNotBlank(bankAccount.getIBAN())) {
				valueMap.put(IBAN, bankAccount.getIBAN());
			}

			if (isNotBlank(bankAccount.getBIC())) {
				valueMap.put(BIC, bankAccount.getBIC());
			}

		}

		// valueMap.put(OVERSEAS, CHECKED);
		// valueMap.put(OTHER_COUNTRY, CHECKED);

		return valueMap;
	}

	
	private void setFields(AcroFields form,
			Map<String, String> valueMap) throws IOException, DocumentException {

		for (Entry<String, String> element : valueMap.entrySet()) {
			form.setField(element.getKey(), element.getValue());
		}
		//Added code for OMS-152. When semester is Staats  than value is not get set in SERVICE_CLAIMED so changing its text size. Manish
		if(semester!=null){
			if(semester.getSemester().equals(Semesters.FED)){
				form.setFieldProperty(SERVICE_CLAIMED,"textsize", new Float(8),null);
				form.regenerateField(SERVICE_CLAIMED);
			}
		}
	}

	private void resetPDF(AcroFields form) {

		// PdfDictionary dictionary= .getMerged(0);

		// AcroFields.Item item = form.getFieldItem(INSTITUT);
		// PdfDictionary dictionary = item.getWidget(0);
		// dictionary.getKeys();
		// System.out.println(item);
		// // dictionary.getAsDict(PdfName.BM);
		// dictionary.remove(PdfName.AP);
		//
		// hide form print button
		form.removeField(FORM_PRINT);
	}

	private void genratePDF(PdfStamper stamp, ByteArrayOutputStream out)
			throws DocumentException, IOException {

		stamp.setFormFlattening(true);
		stamp.close();

		//return out.toByteArray();
	}


	private static final String PDF_NAME = "payment.pdf";
	private static final String CHECKED = "0";
	
	private static final String FORM_PRINT = "TopmostSubform[0].Page1[0].DruckenSchaltfläche1[0]";
	private static final String INSTITUTE = "TopmostSubform[0].Page1[0].Textfeld1[0]";
	private static final String NAME = "TopmostSubform[0].Page1[0].Textfeld1[1]";

	// PAYEE
	private static final String MR = "TopmostSubform[0].Page1[0].Table[0].CheckBox[0]";
	private static final String MRS = "TopmostSubform[0].Page1[0].Table[0].CheckBox[1]";
	private static final String PAYEE_NAME = "TopmostSubform[0].Page1[0].Table[0].Name[0]";
	private static final String PAYEE_ADDRESS = "TopmostSubform[0].Page1[0].Table[0].Strasse[0]";
	private static final String PAYEE_PLACE = "TopmostSubform[0].Page1[0].Table[0].Ort[0]";
	private static final String PAYEE_PHONE = "TopmostSubform[0].Page1[0].Table[0].Telefon[0]";
	private static final String PAYEE_DOB = "TopmostSubform[0].Page1[0].Table[0].GebDatum[0]";
	private static final String PAYEE_NATIONALITY = "TopmostSubform[0].Page1[0].Table[0].Nationalität[0]";
	private static final String PAYEE_EMAIL = "TopmostSubform[0].Page1[0].Table[0].eMail[0]";

	// marital_status
	private static final String SINGLE = "TopmostSubform[0].Page1[0].CheckBox[8]";
	private static final String MARRIED = "TopmostSubform[0].Page1[0].CheckBox[6]";
	private static final String REG_PARTNERSHIP = "TopmostSubform[0].Page1[0].CheckBox[9]";
	private static final String DIVORCED = "TopmostSubform[0].Page1[0].CheckBox[4]";
	private static final String SEPARATED = "TopmostSubform[0].Page1[0].CheckBox[7]";
	private static final String WIDOWED = "TopmostSubform[0].Page1[0].CheckBox[5]";

	// status
	private static final String SCHOLAR = "TopmostSubform[0].Page1[0].CheckBox[11]";
	private static final String STUDENT = "TopmostSubform[0].Page1[0].CheckBox[10]";

	private static final String SOCIAL_SECURITY_NUMBER = "TopmostSubform[0].Page1[0].SVNr[0]";

	// work permit
	private static final String B_PERMIT = "TopmostSubform[0].Page1[0].CheckBox[0]";
	private static final String L_PERMIT = "TopmostSubform[0].Page1[0].CheckBox[1]";
	private static final String C_PERMIT = "TopmostSubform[0].Page1[0].CheckBox[2]";

	private static final String SELF_EMPLOYED_IN_SWITERLAND = "TopmostSubform[0].Page1[0].CheckBox[3]";

	private static final String DATES_WORKED_FROM = "TopmostSubform[0].Page1[0].DatumVon[0]";
	private static final String DATES_WORKED_TO = "TopmostSubform[0].Page1[0].DatumBis[0]";
	private static final String SERVICE_CLAIMED = "TopmostSubform[0].Page1[0].Leistung[0]";
	private static final String DATE_1 = "TopmostSubform[0].Page1[0].DatumHonorar[0]";
	private static final String DATE_2 = "TopmostSubform[0].Page1[0].DatumSpesen[0]";
	private static final String AMOUNT_IN = "TopmostSubform[0].Page1[0].Waehrung[0]";
	private static final String AMOUNT_1 = "TopmostSubform[0].Page1[0].Betrag[0]";
	private static final String AMOUNT_2 = "TopmostSubform[0].Page1[0].Betrag[1]";
	private static final String COST_CENTRE_1 = "TopmostSubform[0].Page1[0].KST1[0]";
	private static final String COST_CENTRE_2 = "TopmostSubform[0].Page1[0].KST2[0]";
	private static final String TOTAL = "TopmostSubform[0].Page1[0].Zwischensumme[0]";

	// payable to
	private static final String NAME_OF_BANK = "TopmostSubform[0].Page1[0].BankName[0]";
	private static final String BANK_ADDRESS = "TopmostSubform[0].Page1[0].BankAdresse[0]";
	private static final String ACCOUNT_HOLDER = "TopmostSubform[0].Page1[0].BankKontoinhaber[0]";
	private static final String IBAN = "TopmostSubform[0].Page1[0].BankIBAN[0]";
	private static final String BIC = "TopmostSubform[0].Page1[0].BankIBIC[0]";
	
	private static final String OVERSEAS = "TopmostSubform[0].Page1[0].Paragraph[0].CheckBox[0]";
	private static final String OTHER_COUNTRY = "TopmostSubform[0].Page1[0].Paragraph[1].CheckBox[0]";

}
