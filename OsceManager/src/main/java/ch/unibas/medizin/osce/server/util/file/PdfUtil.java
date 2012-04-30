package ch.unibas.medizin.osce.server.util.file;

import java.io.FileOutputStream;
import java.net.URL;

import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.LangSkill;
import ch.unibas.medizin.osce.domain.Scar;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.server.StandardizedPatientDetailsConstants;

import com.allen_sauer.gwt.log.client.Log;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil {

	private Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 9);
	private Font emailFont = new Font(Font.FontFamily.TIMES_ROMAN, 9,
			Font.UNDERLINE);

	StandardizedPatient standardizedPatient;

	public void writeFile(String fileName,
			StandardizedPatient standardizedPatient) {
		try {
			this.standardizedPatient = standardizedPatient;
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();
			addMetaData(document);
			addTitlePage(document);

			createBasicDetailsTable(document);

			Paragraph preface = new Paragraph();
			document.add(preface);
			addEmptyLine(preface, 2);

			createOtherDetailsTable(document);
			addEmptyLine(preface, 5);
			addSignature(document);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addSignature(Document document) {
		try {
			Paragraph signPara = new Paragraph(
					StandardizedPatientDetailsConstants.signatureDoc, smallFont);
			emailFont.setColor(BaseColor.BLUE);
			Anchor anchor = new Anchor(
					StandardizedPatientDetailsConstants.emailSignDoc, emailFont);
			anchor.setReference(StandardizedPatientDetailsConstants.emailSignDoc);
			signPara.add(anchor);
			signPara.setAlignment(Element.ALIGN_LEFT);
			document.add(signPara);
		} catch (DocumentException e) {
			Log.error("In addSignature of Pdf Creation:" + e.getMessage());
		}

	}

	private void addMetaData(Document document) {
		document.addTitle(this.standardizedPatient.getName());
		document.addSubject("Simulation Patientendaten");
		document.addAuthor(System.getProperty("user.name"));
		document.addCreator(System.getProperty("user.name"));
	}

	private void addTitlePage(Document document) throws DocumentException {
		try {
			Paragraph preface = new Paragraph();

			addEmptyLine(preface, 1);

			Paragraph headerParagraph = new Paragraph(
					"Simulation Patientendaten "
							+ this.standardizedPatient.getName(), catFont);
			headerParagraph.setAlignment(Element.ALIGN_CENTER
					| Element.ALIGN_MIDDLE);

			Paragraph paragraph = new Paragraph(
					StandardizedPatientDetailsConstants.docDesc);

			String imageUrl = (standardizedPatient.getImmagePath() != null) ? standardizedPatient
					.getImmagePath()
					: StandardizedPatientDetailsConstants.ImageNotFoundPath;

			try {
				Image image2 = Image.getInstance(new URL(imageUrl));
				image2.scalePercent(50f);
				image2.setAlignment(Element.ALIGN_RIGHT);
				paragraph.add(image2);
			} catch (Exception e) {
				Log.error("In addStatndadizedPatientImage: " + e.getMessage());
			}

			addEmptyLine(preface, 1);
			preface.add(headerParagraph);
			preface.add(paragraph);

			// addEmptyLine(preface, 2);
			document.add(preface);
		} catch (Exception e) {
			Log.error("In addStatndadizedPatientImage: " + e.getMessage());
		}

	}

	private void createBasicDetailsTable(Document document) {
		try {

			PdfPTable table = new PdfPTable(4);

			table.addCell(StandardizedPatientDetailsConstants.name);
			table.addCell(getPdfCell(this.standardizedPatient.getName(), 3, 0));

			table.addCell(StandardizedPatientDetailsConstants.preName);
			table.addCell(getPdfCell(this.standardizedPatient.getPreName(), 3,
					0));

			table.addCell(StandardizedPatientDetailsConstants.street);
			table.addCell(getPdfCell(this.standardizedPatient.getStreet(), 3, 0));

			table.addCell(StandardizedPatientDetailsConstants.plzCity);
			table.addCell(getPdfCell(this.standardizedPatient.getCity(), 3, 0));

			table.addCell(StandardizedPatientDetailsConstants.telephone);
			table.addCell(getPdfCell(this.standardizedPatient.getTelephone(),
					0, 0));

			table.addCell(StandardizedPatientDetailsConstants.email);
			table.addCell(getPdfCell(this.standardizedPatient.getEmail(), 0, 0));

			table.addCell(StandardizedPatientDetailsConstants.mobile);
			table.addCell(getPdfCell(this.standardizedPatient.getMobile(), 0, 0));

			table.addCell(StandardizedPatientDetailsConstants.birthday);
			table.addCell(getPdfCell(this.standardizedPatient.getBirthday()
					.toString(), 0, 0));

			table.addCell(StandardizedPatientDetailsConstants.nationality);
			table.addCell(getPdfCell(this.standardizedPatient.getNationality()
					.getNationality(), 0, 0));

			table.addCell(StandardizedPatientDetailsConstants.gender);
			table.addCell(getPdfCell(this.standardizedPatient.getGender()
					.name(), 0, 0));

			table.addCell(StandardizedPatientDetailsConstants.weight);
			table.addCell(getPdfCell(this.standardizedPatient.getWeight()
					.toString(), 0, 0));

			table.addCell(StandardizedPatientDetailsConstants.height);
			table.addCell(getPdfCell(this.standardizedPatient.getHeight()
					.toString(), 0, 0));

			table.addCell(StandardizedPatientDetailsConstants.profession);
			table.addCell(getPdfCell(this.standardizedPatient.getProfession()
					.getProfession(), 0, 0));

			table.addCell(StandardizedPatientDetailsConstants.languageSkills);
			Object langSkills[] = this.standardizedPatient.getLangskills()
					.toArray();

			StringBuffer lang = new StringBuffer("");
			for (int i = 0; i < langSkills.length; i++) {
				lang.append(((LangSkill) langSkills[i]).getSpokenlanguage()
						.getLanguageName() + "\n");
			}

			table.addCell(getPdfCell(lang.toString(), 0, 0));

			table.addCell(getPdfCell(
					StandardizedPatientDetailsConstants.bankAccount, 0, 0));
			table.addCell(getPdfCell(this.standardizedPatient.getBankAccount()
					.getBankName()
					+ "\n"
					+ this.standardizedPatient.getBankAccount().getBIC()
					+ "\n"
					+ this.standardizedPatient.getBankAccount().getBIC()
					+ "extra", 3, 0));
			table.setTotalWidth(500);
			document.add(table);

		} catch (Exception e) {
			Log.error("In createTable for PDf standardized patient : "
					+ e.getMessage());

		}

	}

	private PdfPCell getPdfCell(String text, int colSpan, int rowSpan) {
		PdfPCell c2 = new PdfPCell(new Phrase(text));
		if (colSpan > 0)
			c2.setColspan(colSpan);
		if (rowSpan > 0)
			c2.setRowspan(rowSpan);
		return c2;
	}

	private void createOtherDetailsTable(Document document) {
		try {
			PdfPTable table = new PdfPTable(2);

			Object anamnesisChecksValue[] = this.standardizedPatient
					.getAnamnesisForm().getAnamnesischecksvalues().toArray();

			int iAnamnesisChecksValue = anamnesisChecksValue.length;

			for (int i = 0; i < iAnamnesisChecksValue; i++) {

				table.addCell(((AnamnesisChecksValue) anamnesisChecksValue[i])
						.getAnamnesischeck().getText());
				switch (((AnamnesisChecksValue) anamnesisChecksValue[i])
						.getAnamnesischeck().getType()) {

				case QUESTION_OPEN: {
					table.addCell(((AnamnesisChecksValue) anamnesisChecksValue[i])
							.getAnamnesisChecksValue());

					break;
				}
				case QUESTION_YES_NO: {

					table.addCell(((AnamnesisChecksValue) anamnesisChecksValue[i])
							.getAnamnesisChecksValue());
					break;
				}
				case QUESTION_MULT_S:
				case QUESTION_MULT_M: {
					try {
						String anamnesIsCheck[] = new String(
								""
										+ ((AnamnesisChecksValue) anamnesisChecksValue[i])
												.getAnamnesischeck().getValue())
								.split("/");
						String anamnesisCheckvalues[] = new String(
								""
										+ ((AnamnesisChecksValue) anamnesisChecksValue[i])
												.getAnamnesisChecksValue())
								.split("-");

						StringBuffer cellValue = new StringBuffer();
						for (int counter = 0; counter < anamnesIsCheck.length
								&& counter < anamnesisCheckvalues.length; counter++) {

							if ((anamnesisCheckvalues[counter]
									.compareToIgnoreCase("1") == 0)) {
								cellValue.append(anamnesIsCheck[counter]);
							}
						}

						table.addCell(cellValue.toString());
					} catch (Exception e) {
						Log.error("Mapper : QUESTION_MULT_S QUESTION_MULT_M:"
								+ e.getMessage());
					}
					break;
				}

				case QUESTION_TITLE: {
					table.addCell(((AnamnesisChecksValue) anamnesisChecksValue[i])
							.getAnamnesisChecksValue());
					break;
				}
				}

				table.completeRow();
			}
			Log.info("anamnesisChecksValue complete : " + iAnamnesisChecksValue);

			Object anamnesisScars[] = standardizedPatient.getAnamnesisForm()
					.getScars().toArray();
			for (int j = 0; j < anamnesisScars.length; j++) {
				table.addCell(((Scar) anamnesisScars[j]).getBodypart());
				table.addCell(((Scar) anamnesisScars[j]).getTraitType().name());
				table.completeRow();

			}

			document.add(table);
		} catch (DocumentException e) {
			Log.error("In createTable for PDf standardized patient : "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}