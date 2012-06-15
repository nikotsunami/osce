package ch.unibas.medizin.osce.server.util.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.unibas.medizin.osce.client.i18n.OsceConstants;
import ch.unibas.medizin.osce.client.i18n.OsceConstantsWithLookup;
import ch.unibas.medizin.osce.domain.AnamnesisCheck;
import ch.unibas.medizin.osce.domain.AnamnesisCheckTitle;
import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.LangSkill;
import ch.unibas.medizin.osce.domain.Scar;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.server.StandardizedPatientDetailsConstants;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;

import com.allen_sauer.gwt.log.client.Log;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;
import com.itextpdf.text.pdf.TextField;
import com.mattbertolini.hermes.Hermes;

public class PdfUtil {
	private static final float titleTableSpacing = 0.0f;

	private Font defaultFont = new Font(Font.FontFamily.HELVETICA, 10);
	private Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	private Font italicFont = new Font(Font.FontFamily.HELVETICA, 10,
			Font.ITALIC);
	private Font paragraphTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
			Font.BOLD);
	private Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private Font smallFont = new Font(Font.FontFamily.HELVETICA, 9);
	private Font emailFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.UNDERLINE);

	private StandardizedPatient stdPat;
	private Long anamnesisFormId;
	private Document document = new Document();
	private OsceConstants constants;
	private OsceConstantsWithLookup enumConstants;
	private String title;
	private PdfWriter writer;

	public PdfUtil() {
		this(new Locale("de"));
	}

	public PdfUtil(Locale locale) {
		try {
			constants = Hermes.get(OsceConstants.class, locale.toString());
			enumConstants = Hermes.get(OsceConstantsWithLookup.class, locale.toString());
		} catch (IOException e) {
			Log.error("PdfUtil() -- Error loading translations: " + e.getMessage());
		}
		
	}

	public void writeFile(String fileName,
			StandardizedPatient standardizedPatient) {
		try {
			stdPat = standardizedPatient;
			anamnesisFormId = stdPat.getAnamnesisForm().getId();
			title = constants.standardizedPatient() + " "
					+ standardizedPatient.getName() + " "
					+ standardizedPatient.getPreName();
			writer = PdfWriter.getInstance(document, new FileOutputStream(
					fileName));
			document.open();
			addMetaData();
			addHeader();
			addContactDetails();
			addDetails();
			addBankAccount();
			addTraits();
			addAnamnesis();

			// createOtherDetailsTable(document);
			// addEmptyLine(preface, 5);
			// addSignature(document);

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

	private void addMetaData() {
		document.addTitle(title);
		document.addSubject("Simulation Patientendaten");
		document.addAuthor(System.getProperty("user.name"));
		document.addCreator(System.getProperty("user.name"));
		document.addCreationDate();
	}

	private void addHeader() {
		Paragraph preface = new Paragraph();
		addEmptyLine(preface, 2);
		Paragraph headerParagraph = new Paragraph(title, catFont);
		headerParagraph.setAlignment(Element.ALIGN_CENTER
				| Element.ALIGN_MIDDLE);
		preface.add(headerParagraph);
		Paragraph confidentialityParagraph = new Paragraph(
				constants.handledConfidentially(), smallFont);
		preface.add(confidentialityParagraph);
		addEmptyLine(preface, 1);

		try {
			document.add(preface);
		} catch (Exception e) {
			Log.error("In addStatndadizedPatientImage: " + e.getMessage());
		}
	}

	private Image loadImage(String path) {
		if (path == null) {
			throw new IllegalArgumentException(
					"path to image file has to be provided!");
		}

		try {
			Image image = Image.getInstance(path);
			float width = image.getWidth();
			float height = image.getHeight();

			Log.debug("PdfUtil - Image width: " + width + ", Image height: "
					+ height);
			image.scaleAbsolute(30f, 30f);
			// 523 * 0.4, ~7 lines of text
			image.scaleToFit(209.2f, 100f);
			image.setAlignment(Element.ALIGN_LEFT);
			return image;
		} catch (Exception e) {
			Log.error("In loadImage( \"" + path + "\"): " + e.getMessage());
			return null;
		}
	}

	private void addContactDetails() {
		Paragraph contactDetails = new Paragraph();
		PdfPTable table = createContactDetailsTable();
		table.setSpacingBefore(titleTableSpacing);
		Chunk bla = new Chunk(constants.contactInfo(), paragraphTitleFont);
		contactDetails.add(bla);
		contactDetails.add(table);
		addEmptyLine(contactDetails, 1);

		try {
			document.add(contactDetails);
		} catch (DocumentException e) {
			Log.error("in PdfUtil.addContactDetails(): " + e.getMessage());
		}
	}

	private void addDetails() {
		Paragraph details = new Paragraph();
		PdfPTable table = createDetailsTable();
		table.setSpacingBefore(titleTableSpacing);
		details.add(new Chunk(constants.details(), paragraphTitleFont));
		details.add(table);
		addEmptyLine(details, 1);

		try {
			document.add(details);
		} catch (DocumentException e) {
			Log.error("in PdfUtil.addDetails(): " + e.getMessage());
		}
	}

	private void addBankAccount() {
		Paragraph bankAccount = new Paragraph();
		PdfPTable table = createBankAccountTable();
		table.setSpacingBefore(titleTableSpacing);
		bankAccount.add(new Chunk(constants.bankAccount(), paragraphTitleFont));
		bankAccount.add(table);
		addEmptyLine(bankAccount, 1);

		try {
			document.add(bankAccount);
		} catch (DocumentException e) {
			Log.error("in PdfUtil.addBankAccount(): " + e.getMessage());
		}
	}

	private void addTraits() {
		Paragraph traits = new Paragraph();
		PdfPTable table = createTraitsTable();
		table.setSpacingBefore(titleTableSpacing);
		traits.add(new Chunk(constants.traits(), paragraphTitleFont));
		traits.add(table);
		addEmptyLine(traits, 1);

		try {
			document.add(traits);
		} catch (DocumentException e) {
			Log.error("in PdfUtil.addTraits(): " + e.getMessage());
		}
	}

	private void addAnamnesis() {
		Paragraph anamnesis = new Paragraph();
		anamnesis.add(new Chunk(constants.anamnesisValues(), paragraphTitleFont));
		
		AnamnesisChecksValue.fillAnamnesisChecksValues(anamnesisFormId);
		List<AnamnesisCheckTitle> titles = AnamnesisCheckTitle.findAllAnamnesisCheckTitles();
		
		for (AnamnesisCheckTitle title : titles) {
			String titleText = "";
			if (title.getText() != null) {
				titleText = title.getText();
			}
			anamnesis.add(new Chunk(titleText, paragraphTitleFont));
			PdfPTable table = createAnamnesisTableForTitle(title.getId());
			table.setSpacingBefore(titleTableSpacing);
			anamnesis.add(table);
		}
		
		
		addEmptyLine(anamnesis, 1);

		// TODO: addLegend (for answer types)

		try {
			document.add(anamnesis);
		} catch (DocumentException e) {
			Log.error("in PdfUtil.addAnamnesis(): " + e.getMessage());
		}
	}

	private PdfPTable createTraitsTable() {
		PdfPTable table = new PdfPTable(new float[] { 0.2f, 0.8f });
		List<Scar> scars = Scar.findScarEntriesByAnamnesisForm(anamnesisFormId,
				0, 1024);

		for (Scar scar : scars) {
			if (scar.getBodypart() != null && scar.getTraitType() != null) {
				String location = scar.getBodypart() + ":";
				String type = enumConstants.getString(scar.getTraitType()
						.toString());

				table.addCell(getPdfCellBold(location));
				table.addCell(getPdfCell(type));
			}
		}

		table.setWidthPercentage(100.0f);
		return table;
	}

	private PdfPTable createAnamnesisTableForTitle(Long titleId) {
		PdfPTable table = new PdfPTable(new float[] { 0.5f, 0.5f });
		
		List<AnamnesisChecksValue> values = AnamnesisChecksValue.
				findAnamnesisChecksValuesByAnamnesisFormAndTitle(anamnesisFormId, titleId, "", 0, 1024);
		for (AnamnesisChecksValue value : values) {
			AnamnesisCheck check = value.getAnamnesischeck();
			if (check != null) {
				table.addCell(getPdfCellBold(check.getText()));
				table.addCell(getAnswerCell(value));
			}
		}

		table.setWidthPercentage(100.0f);
		return table;
	}

	private PdfPCell getAnswerCell(AnamnesisChecksValue value) {
		String[] possibleAnswers;
		AnamnesisCheck check = value.getAnamnesischeck();
		String question = check.getText();

		boolean questionAnswered = !(value.getTruth() == null && value
				.getAnamnesisChecksValue() == null);
		AnamnesisCheckTypes type = check.getType();

		if (type == AnamnesisCheckTypes.QUESTION_YES_NO) {
			possibleAnswers = new String[] { constants.yes(), constants.no() };
			if (questionAnswered && value.getTruth() == true) {
				return getRadioCell(question, possibleAnswers, 0);
			} else if (questionAnswered) {
				return getRadioCell(question, possibleAnswers, 1);
			} else {
				return getRadioCell(question, possibleAnswers, null);
			}
		} else if (type == AnamnesisCheckTypes.QUESTION_MULT_M) {
			possibleAnswers = value.getAnamnesischeck().getValue().split("\\|");
			List<Integer> selectedAnswers = new ArrayList<Integer>();

			if (questionAnswered) {
				int i = 0;
				for (String selected : value.getAnamnesisChecksValue().split(
						"-")) {
					try {
						boolean isSelected = (Integer.parseInt(selected) > 0) ? true
								: false;
						if (isSelected) {
							selectedAnswers.add(new Integer(i));
						}
					} catch (NumberFormatException ex) {
					}
					i++;
				}
			}
			return getCheckBoxCell(question, possibleAnswers, selectedAnswers);
		} else if (type == AnamnesisCheckTypes.QUESTION_MULT_S) {
			possibleAnswers = value.getAnamnesischeck().getValue().split("\\|");
			Integer selectedAnswer = null;

			if (questionAnswered) {
				int i = 0;
				for (String selected : value.getAnamnesisChecksValue().split(
						"-")) {
					try {
						boolean isSelected = (Integer.parseInt(selected) > 0) ? true
								: false;
						if (isSelected) {
							selectedAnswer = i;
							break;
						}
					} catch (NumberFormatException ex) {
					}
					i++;
				}
			}
			return getRadioCell(question, possibleAnswers, selectedAnswer);
		} else /* QUESTION_OPEN */{
			if (questionAnswered) {
				return getPdfCell(value.getAnamnesisChecksValue());
			} else {
				return getPdfCell("");
			}
		}
	}

	private static class TextCellEvent implements PdfPCellEvent {
		private PdfWriter writer;
		private String fieldName;
		private Font font;

		public TextCellEvent(PdfWriter writer, String fieldName, Font font) {
			this.writer = writer;
			this.fieldName = fieldName;
		}

		@Override
		public void cellLayout(PdfPCell cell, Rectangle bounds,
				PdfContentByte[] canvas) {

			TextField tf = new TextField(writer, bounds, fieldName);
			tf.setFontSize(12);
			try {
				PdfFormField field = tf.getTextField();
				writer.addAnnotation(field);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	private static class CheckBoxCellEvent implements PdfPCellEvent {
		private PdfWriter writer;
		private boolean isSelected;
		private PdfFormField group;
		private String answer;
		boolean isRadio;

		public CheckBoxCellEvent(PdfWriter writer, String answer,
				boolean isSelected) {
			this.writer = writer;
			this.isSelected = isSelected;
			this.answer = answer.toLowerCase().replaceAll("[^a-z0-9]+", "_");
			this.isRadio = false;
		}

		public CheckBoxCellEvent(PdfWriter writer, PdfFormField group,
				String answer, boolean isSelected) {
			this.writer = writer;
			this.isSelected = isSelected;
			this.group = group;
			this.answer = answer.toLowerCase().replaceAll("[^a-z0-9]+", "_");
			this.isRadio = true;
		}

		@Override
		public void cellLayout(PdfPCell cell, Rectangle bounds,
				PdfContentByte[] canvas) {
			float llx = bounds.getLeft() + 2.0f;
			float lly = bounds.getBottom() + 0.5f;
			float urx = bounds.getRight() - 2.0f;
			float ury = bounds.getTop() - 3.0f;

			try {
				Rectangle rect = new Rectangle(llx, lly, urx, ury);
				RadioCheckField box;
				if (isRadio) {
					box = new RadioCheckField(writer, rect, null, answer);
					box.setCheckType(RadioCheckField.TYPE_CIRCLE);
				} else {
					box = new RadioCheckField(writer, rect, answer, "on");
					box.setCheckType(RadioCheckField.TYPE_CROSS);
				}

				box.setBorderColor(GrayColor.BLACK);
				box.setBackgroundColor(GrayColor.WHITE);
				box.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
				box.setChecked(isSelected);
				PdfFormField field;
				if (isRadio) {
					field = box.getRadioField();
					group.addKid(field);
				} else {
					field = box.getCheckField();
					writer.addAnnotation(field);
				}
			} catch (IOException e) {
				Log.error("in PdfUtil.CheckBoxCellEvent.cellLayout() : "
						+ e.getMessage());
			} catch (DocumentException e) {
				Log.error("in PdfUtil.CheckBoxCellEvent.cellLayout() : "
						+ e.getMessage());
			}
		}
	}

	private static class CheckBoxGroupEvent implements PdfPCellEvent {
		private PdfWriter writer;
		private PdfFormField group;

		public CheckBoxGroupEvent(PdfWriter writer, PdfFormField group) {
			this.writer = writer;
			this.group = group;
		}

		@Override
		public void cellLayout(PdfPCell cell, Rectangle bounds,
				PdfContentByte[] canvas) {
			writer.addAnnotation(group);
		}
	}

	private PdfPCell getRadioCell(String question, String[] possibleAnswers,
			Integer selectedAnswer) {
		if (selectedAnswer == null) {
			return getCheckBoxCell(question, possibleAnswers, null, true);
		}
		List<Integer> selectedAnswers = new ArrayList<Integer>();
		selectedAnswers.add(selectedAnswer);
		return getCheckBoxCell(question, possibleAnswers, selectedAnswers, true);
	}

	private PdfPCell getCheckBoxCell(String question, String[] possibleAnswers,
			List<Integer> selectedAnswers) {
		return getCheckBoxCell(question, possibleAnswers, selectedAnswers,
				false);
	}

	private PdfPCell getCheckBoxCell(String question, String[] possibleAnswers,
			List<Integer> selectedAnswers, boolean isRadio) {
		PdfPCell cell = new PdfPCell();
		PdfFormField group = null;
		cell.setPadding(0);
		cell.setBorder(Rectangle.NO_BORDER);
		if (isRadio) {
			group = PdfFormField.createRadioButton(writer, true);
			String fieldName = question.toLowerCase().replaceAll("[^a-z0-9]+",
					"_");
			group.setFieldName(fieldName);
			cell.setCellEvent(new CheckBoxGroupEvent(writer, group));
		}

		PdfPTable subTable = new PdfPTable(new float[] { 0.05f, 0.95f });

		for (int i = 0; i < possibleAnswers.length; i++) {
			boolean isSelected = false;
			if (selectedAnswers != null) {
				for (Integer nAnswer : selectedAnswers) {
					if (nAnswer != null && nAnswer.intValue() == i) {
						isSelected = true;
					}
				}
			}
			String answer = possibleAnswers[i];
			PdfPCell subCell = new PdfPCell();
			CheckBoxCellEvent event;
			if (isRadio) {
				event = new CheckBoxCellEvent(writer, group,
						possibleAnswers[i], isSelected);
			} else {
				event = new CheckBoxCellEvent(writer, possibleAnswers[i],
						isSelected);
			}
			subCell.setCellEvent(event);
			subCell.setBorder(Rectangle.NO_BORDER);
			subTable.addCell(subCell);
			subTable.addCell(getPdfCell(answer));
		}

		subTable.setWidthPercentage(100.0f);

		cell.addElement(subTable);
		return cell;
	}

	private PdfPTable createBankAccountTable() {
		PdfPTable table = new PdfPTable(new float[] { 0.2f, 0.3f, 0.2f, 0.3f });
		Bankaccount bankAccount = stdPat.getBankAccount();
		if (bankAccount == null) {
			// TODO: how to handle?
			return null;
		}
		String iban = (bankAccount.getIBAN() != null) ? bankAccount.getIBAN()
				: "-";
		String owner = (bankAccount.getOwnerName() != null) ? bankAccount
				.getOwnerName() : "-";
		String bankName = (bankAccount.getBankName() != null) ? bankAccount
				.getBankName() : "-";
		String bic = (bankAccount.getBIC() != null) ? bankAccount.getBIC()
				: "-";
		String plz = (bankAccount.getPostalCode() != null) ? bankAccount
				.getPostalCode().toString() : "-";
		String city = (bankAccount.getCity() != null) ? bankAccount.getCity()
				: "-";
		String country = (bankAccount.getCountry() != null) ? bankAccount
				.getCountry().getNationality() : "-";

		table.addCell(getPdfCellBold(constants.iban() + ":"));
		table.addCell(getPdfCell(iban));

		table.addCell(getPdfCellBold(constants.ownerName() + ":"));
		table.addCell(getPdfCell(owner));

		table.addCell(getPdfCellBold(constants.bic() + ":"));
		table.addCell(getPdfCell(bic));

		table.addCell(getPdfCellBold(constants.bank() + ":"));
		table.addCell(getPdfCell(bankName));

		table.addCell(getPdfCellBold(constants.plzCity() + ":"));
		table.addCell(getPdfCell(plz + " " + city));

		table.addCell(getPdfCellBold(constants.country() + ":"));
		table.addCell(getPdfCell(country));

		table.setWidthPercentage(100.0f);
		return table;
	}

	private PdfPTable createDetailsTable() {
		PdfPTable table = new PdfPTable(new float[] { 0.2f, 0.3f, 0.2f, 0.3f });

		String birthday = (stdPat.getBirthday() != null) ? stdPat.getBirthday()
				.toString() : "-";
		String gender = (stdPat.getGender() != null) ? enumConstants
				.getString(stdPat.getGender().toString()) : "-";
		String weight = (stdPat.getWeight() != null) ? stdPat.getWeight()
				.toString() : "-";
		String height = (stdPat.getHeight() != null) ? stdPat.getHeight()
				.toString() : "-";
		String nationality = (stdPat.getNationality() != null) ? stdPat
				.getNationality().getNationality() : "-";
		String profession = (stdPat.getProfession() != null) ? stdPat
				.getProfession().getProfession() : "-";
		String socialInsurance = (stdPat.getSocialInsuranceNo() != null) ? stdPat
				.getSocialInsuranceNo() : "-";
		String workPermission = (stdPat.getWorkPermission() != null) ? enumConstants
				.getString(stdPat.getWorkPermission().toString()) : "-";
		String maritalStatus = (stdPat.getMaritalStatus() != null) ? enumConstants
				.getString(stdPat.getMaritalStatus().toString()) : "-";

		table.addCell(getPdfCellBold(constants.birthday() + ":"));
		// TODO format date
		table.addCell(getPdfCell(birthday));

		table.addCell(getPdfCellBold(constants.gender() + ":"));
		table.addCell(getPdfCell(gender));

		table.addCell(getPdfCellBold(constants.weight() + ":"));
		table.addCell(getPdfCell(weight));

		table.addCell(getPdfCellBold(constants.height() + ":"));
		table.addCell(getPdfCell(height));

		table.addCell(getPdfCellBold(constants.nationality() + ":"));
		table.addCell(getPdfCell(nationality));

		table.addCell(getPdfCellBold(constants.profession() + ":"));
		table.addCell(getPdfCell(profession));

		table.addCell(getPdfCellBold(constants.socialInsuranceNo() + ":"));
		table.addCell(getPdfCell(socialInsurance));

		table.addCell(getPdfCellBold(constants.workPermission() + ":"));
		table.addCell(getPdfCell(workPermission));

		table.addCell(getPdfCellBold(constants.maritalStatus() + ":"));
		table.addCell(getPdfCell(maritalStatus));
		table.addCell(getPdfCellBold(" "));
		table.addCell(getPdfCell(" "));

		table.addCell(getPdfCellBold(constants.languageSkills() + ":"));
		PdfPTable langTable = new PdfPTable(2);

		Object[] langSkills = stdPat.getLangskills().toArray();
		for (Object skillObj : langSkills) {
			LangSkill skill = (LangSkill) skillObj;
			String skillLevel = (skill.getSkill() != null) ? enumConstants
					.getString(skill.getSkill().toString()) : "-";
			String language = (skill.getSpokenlanguage() != null) ? skill
					.getSpokenlanguage().getLanguageName() : "-";
			langTable.addCell(getPdfCell(language));
			langTable.addCell(getPdfCell(skillLevel, italicFont, 0, 0));
		}
		PdfPCell innerCell = new PdfPCell(langTable);
		innerCell.setPadding(0);
		innerCell.setBorder(Rectangle.NO_BORDER);

		table.addCell(innerCell);
		table.addCell(getPdfCellBold(" "));
		table.addCell(getPdfCell(" "));

		table.setWidthPercentage(100);
		return table;
	}

	private PdfPTable createContactDetailsTable() {
		try {
			boolean hasImage = (stdPat.getImmagePath() != null);
			PdfPTable table;

			if (hasImage) {
				table = new PdfPTable(new float[] { 0.2f, 0.4f, 0.4f });
			} else {
				table = new PdfPTable(new float[] { 0.2f, 0.8f });
			}

			table.addCell(getPdfCellBold(constants.name() + ":"));
			table.addCell(getPdfCell(stdPat.getName()));

			if (hasImage) {
				Image image = loadImage(stdPat.getImmagePath());
				PdfPCell cell = new PdfPCell(image);
				cell.setRowspan(7);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
			}

			table.addCell(getPdfCellBold(constants.preName() + ":"));
			table.addCell(getPdfCell(stdPat.getPreName()));

			table.addCell(getPdfCellBold(constants.street() + ":"));
			table.addCell(getPdfCell(stdPat.getStreet()));

			table.addCell(getPdfCellBold(constants.plzCity() + ":"));
			table.addCell(getPdfCell(stdPat.getPostalCode() + " "
					+ stdPat.getCity()));

			table.addCell(getPdfCellBold(constants.telephone() + ":"));
			table.addCell(getPdfCell(stdPat.getTelephone()));

			table.addCell(getPdfCellBold(constants.email() + ":"));
			PdfPCell emailCell = new PdfPCell(new Phrase((new Chunk(
					stdPat.getEmail(), defaultFont)).setAnchor("mailto:"
					+ stdPat.getEmail().trim())));
			emailCell.setBorder(Rectangle.NO_BORDER);
			table.addCell(emailCell);

			table.addCell(getPdfCellBold(constants.mobile() + ":"));
			table.addCell(getPdfCell(stdPat.getMobile()));
			table.setWidthPercentage(100);
			return table;

		} catch (Exception e) {
			Log.error("In createTable for PDf standardized patient : "
					+ e.getMessage());
			return null;
		}
	}

	private PdfPCell getPdfCellBold(String text) {
		return getPdfCell(text, boldFont, 0, 0);
	}

	private PdfPCell getPdfCell(String text) {
		return getPdfCell(text, defaultFont, 0, 0);
	}

	private PdfPCell getPdfCell(String text, Font font, int colSpan, int rowSpan) {
		PdfPCell c2 = new PdfPCell(new Phrase(text, font));
		if (colSpan > 0)
			c2.setColspan(colSpan);
		if (rowSpan > 0)
			c2.setRowspan(rowSpan);
		c2.setBorder(Rectangle.NO_BORDER);
		return c2;
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}