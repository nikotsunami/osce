package ch.unibas.medizin.osce.server.util.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.ChecklistTopic;
import ch.unibas.medizin.osce.domain.File;
import ch.unibas.medizin.osce.domain.RoleBaseItem;
import ch.unibas.medizin.osce.domain.RoleItemAccess;
import ch.unibas.medizin.osce.domain.RoleSubItemValue;
import ch.unibas.medizin.osce.domain.RoleTableItem;
import ch.unibas.medizin.osce.domain.RoleTableItemValue;
import ch.unibas.medizin.osce.domain.StandardizedRole;
import ch.unibas.medizin.osce.domain.UsedMaterial;
import ch.unibas.medizin.osce.shared.ItemDefination;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import com.allen_sauer.gwt.log.client.Log;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mattbertolini.hermes.Hermes;

public class RolePrintPdfUtil {
	private static final float titleTableSpacing = 0.0f;

	private Font defaultFont = new Font(Font.FontFamily.HELVETICA, 10);
	private Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	private Font italicFont = new Font(Font.FontFamily.HELVETICA, 10,
			Font.ITALIC);
	private Font paragraphTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
			Font.BOLD);
	private Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
			Font.BOLD);
	private Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private Font smallFont = new Font(Font.FontFamily.HELVETICA, 9);
	private Font emailFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.UNDERLINE);

	private StandardizedRole standardizedRole;

	private Document document = new Document();
	private OsceConstants constants;
	private OsceConstantsWithLookup enumConstants;
	private String title;
	private PdfWriter writer;
	// private List<String> itemsList;
	private Long roleItemAccessId;
	private boolean isValueAvailable[];

	public RolePrintPdfUtil() {
		this(new Locale("de"));
	}

	public RolePrintPdfUtil(Locale locale) {
		try {
			constants = Hermes.get(OsceConstants.class, locale.toString());
			enumConstants = Hermes.get(OsceConstantsWithLookup.class,
					locale.toString());
		} catch (IOException e) {
			Log.error("PdfUtil() -- Error loading translations: "
					+ e.getMessage());
		}

	}

	public void writeFile(String fileName, StandardizedRole standardizedRole,
			List<String> itemsList, Long roleItemAccessId) {
		try {
			this.standardizedRole = standardizedRole;
			// this.itemsList = itemsList;
			this.roleItemAccessId = roleItemAccessId;
			this.isValueAvailable = new boolean[4];

			title = constants.standardizedRole() + " "
					+ standardizedRole.getLongName();
			writer = PdfWriter.getInstance(document, new FileOutputStream(
					fileName));
			document.open();
			addMetaData();
			addHeader();

			for (String items : itemsList) {
				Log.info("items is : " + items);
				Log.info("basicData"
						+ (items.compareToIgnoreCase(constants.basicData()) == 0));
				Log.info("checkList"
						+ (items.compareToIgnoreCase(constants.checkList()) == 0));
				Log.info("roomMaterials"
						+ (items.compareToIgnoreCase(constants.roomMaterials()) == 0));
				Log.info("fileDetail"
						+ (items.compareToIgnoreCase(constants.fileDetail()) == 0));

				if (items.compareToIgnoreCase(constants.basicData()) == 0) {
					Log.info("items is : addDetails");
					addDetails();
				} else if (items.compareTo(constants.checkList()) == 0) {
					Log.info("items is : addCheckList");
					addCheckListDetails();
				} else if (items.compareTo(constants.roomMaterials()) == 0) {
					Log.info("items is : addRoomMaterial");
					addRoomMaterialDetails();
				} else if (items.compareTo(constants.fileDetail()) == 0) {
					Log.info("items is : addFiles");
					addFileDetails();
				}
			}

			// Log.info("@@@@@ roleItemAccessId " + roleItemAccessId);
			if (roleItemAccessId >= 0L) {
				addRoleScriptDetials();
			}

			Paragraph preface = new Paragraph();
			addEmptyLine(preface, 5);
			// addSignature(document);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private void addSignature(Document document) {
	// try {
	// Paragraph signPara = new Paragraph(
	// StandardizedPatientDetailsConstants.signatureDoc, smallFont);
	//
	// emailFont.setColor(BaseColor.BLUE);
	// Anchor anchor = new Anchor(
	// StandardizedPatientDetailsConstants.emailSignDoc, emailFont);
	// anchor.setReference(StandardizedPatientDetailsConstants.emailSignDoc);
	// signPara.add(anchor);
	// signPara.setAlignment(Element.ALIGN_LEFT);
	//
	// Paragraph paragraph = new Paragraph();
	// addEmptyLine(paragraph, 1);
	// document.add(paragraph);
	//
	// document.add(signPara);
	// } catch (DocumentException e) {
	// Log.error("In addSignature of Pdf Creation:" + e.getMessage());
	// }
	//
	// }

	private void addMetaData() {
		document.addTitle(title);
		document.addSubject("Role Details");
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

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private void addDetails() {
		Paragraph details = new Paragraph();
		PdfPTable table = createDetailsTable();
		if (isValueAvailable[0]) {
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
	}

	private void addRoleScriptDetials() {
		Paragraph details = new Paragraph();
		if (standardizedRole.getRoleTemplate() != null) {
			details.add(new Chunk(constants.roleScriptTemplate() + ": "
					+ standardizedRole.getRoleTemplate().getTemplateName(),
					paragraphTitleFont));

			addEmptyLine(details, 1);

			try {
				document.add(details);
			} catch (DocumentException e) {
				Log.error("in PdfUtil.addDetails(): " + e.getMessage());
			}

			createRoleScriptDetails();
		}
	}

	private void createRoleScriptDetails() {

		List<RoleBaseItem> roleBaseItems = standardizedRole.getRoleTemplate()
				.getRoleBaseItem();

		Log.info("roleBaseItems size " + roleBaseItems.size());

		if (roleBaseItems.size() > 0) {
			isValueAvailable[2] = true;
			for (RoleBaseItem roleBaseItem : roleBaseItems) {
				Set<RoleItemAccess> roleItemAccesses = roleBaseItem
						.getRoleItemAccess();

				boolean isAccessGiven = false;
				if (roleItemAccessId == 0L) {
					isAccessGiven = true;
				} else {
					for (Iterator<RoleItemAccess> iterator = roleItemAccesses
							.iterator(); iterator.hasNext();) {
						RoleItemAccess roleItemAccess = (RoleItemAccess) iterator
								.next();
						// Log.info("```````````roleItemAccess.getId()"
						// + roleItemAccess.getId());
						// Log.info("`````````````roleItemAccessId"
						// + roleItemAccessId);
						if (roleItemAccess.getId().longValue() == roleItemAccessId
								.longValue()) {
							isAccessGiven = true;
							break;
						}

					}
				}
				Log.info("isAccessGiven : " + isAccessGiven);
				if (isAccessGiven) {
					String itemName = (roleBaseItem.getItem_name() != null) ? roleBaseItem
							.getItem_name() : "-";
					Paragraph details = new Paragraph();

					details.add(new Chunk(constants.roleBaseItemName() + ": "
							+ itemName, subTitleFont));

					// addEmptyLine(details, 1);

					try {
						document.add(details);
					} catch (DocumentException e) {
						Log.error("in PdfUtil.addDetails(): " + e.getMessage());
					}

					if (roleBaseItem.getItem_defination() == ItemDefination.table_item) {

						PdfPTable roleScriptTable = new PdfPTable(2);

						roleScriptTable.addCell(getPdfCellBold(constants
								.itemName()));
						roleScriptTable.addCell(getPdfCellBold(constants
								.itemValue()));
						// roleBaseItem.getRoleTableItem().get(0).getRoleTableItemValue().
						boolean isRoleScriptAdded = false;

						List<RoleTableItem> roleTableItemList = roleBaseItem
								.getRoleTableItem();

						for (Iterator<RoleTableItem> iterator = roleTableItemList
								.iterator(); iterator.hasNext();) {
							RoleTableItem roleTableItem = (RoleTableItem) iterator
									.next();

							Set<RoleTableItemValue> roleTableItemValueSet = roleTableItem
									.getRoleTableItemValue();

							for (Iterator<RoleTableItemValue> iterator2 = roleTableItemValueSet
									.iterator(); iterator2.hasNext();) {
								RoleTableItemValue roleTableItemValue = (RoleTableItemValue) iterator2
										.next();
								if (roleTableItemValue.getStandardizedRole()
										.getId() == standardizedRole.getId()) {

									isRoleScriptAdded = true;
									roleScriptTable
											.addCell(getPdfCell(roleTableItem
													.getItemName()));
									roleScriptTable
											.addCell(getPdfCell(roleTableItemValue
													.getValue()));
								}

							}

						}

						if (isRoleScriptAdded) {
							roleScriptTable.addCell(getPdfCellBold(" "));
							roleScriptTable.addCell(getPdfCell(" "));

							roleScriptTable.setWidthPercentage(100);
							try {
								document.add(createPara(roleScriptTable, ""));
							} catch (DocumentException e) {
								Log.error("in PdfUtil.addDetails(): "
										+ e.getMessage());
							}
						}
					} else if (roleBaseItem.getItem_defination() == ItemDefination.rich_text_item) {

						List<RoleSubItemValue> roleTableItemList = roleBaseItem
								.getRoleSubItem();

						for (Iterator<RoleSubItemValue> iterator = roleTableItemList
								.iterator(); iterator.hasNext();) {
							RoleSubItemValue roleSubItemValue = (RoleSubItemValue) iterator
									.next();
							if (roleSubItemValue.getStandardizedRole().getId()
									.longValue() == standardizedRole.getId()
									.longValue()) {

								String string = roleSubItemValue.getItemText();
								Log.error("getItemText : " + string);

								HTMLWorker htmlWorker = new HTMLWorker(document);
								try {
									htmlWorker.parse(new StringReader(string));

									details = new Paragraph();
									addEmptyLine(details, 1);
									document.add(details);

								} catch (Exception e) {
									Log.error("in PdfUtil.addDetails(): "
											+ e.getMessage());
								}

							}
						}

					}

				}

			}
		}

	}

	private void addFileDetails() {
		Paragraph details = new Paragraph();
		PdfPTable table = createFileDetailsTable();
		if (isValueAvailable[1]) {
			table.setSpacingBefore(titleTableSpacing);
			details.add(new Chunk(constants.fileDetail() + " "
					+ constants.details(), paragraphTitleFont));
			details.add(table);
			addEmptyLine(details, 1);

			try {
				document.add(details);
			} catch (DocumentException e) {
				Log.error("in PdfUtil.addDetails(): " + e.getMessage());
			}
		}
	}

	private void addRoomMaterialDetails() {
		Paragraph details = new Paragraph();
		PdfPTable table = createRoomMaterialDetailsTable();
		if (isValueAvailable[2]) {
			table.setSpacingBefore(titleTableSpacing);
			details.add(new Chunk(constants.roomMaterials() + " "
					+ constants.details(), paragraphTitleFont));
			details.add(table);
			addEmptyLine(details, 1);

			try {
				document.add(details);
			} catch (DocumentException e) {
				Log.error("in PdfUtil.addDetails(): " + e.getMessage());
			}
		}
	}

	private void addCheckListDetails() {
		if ((standardizedRole.getCheckList() != null)
				&& (standardizedRole.getCheckList().getCheckListTopics() != null)
				&& (standardizedRole.getCheckList().getCheckListTopics().size() > 0)) {
			Paragraph details = new Paragraph();

			details.add(new Chunk(constants.checkList() + " "
					+ constants.details(), paragraphTitleFont));
			// addEmptyLine(details, 1);

			try {
				document.add(details);
			} catch (DocumentException e) {
				Log.error("in PdfUtil.addDetails(): " + e.getMessage());
			}
			createCheckListDetailsTable();
		}

	}

	private Paragraph createPara(PdfPTable pdfPTable, String header) {

		Paragraph details = new Paragraph();
		// pdfPTable.setSpacingBefore(titleTableSpacing);
		// addEmptyLine(details, 1);
		if (header.compareTo("") != 0)
			details.add(new Chunk(header, subTitleFont));
		details.add(pdfPTable);
		return details;
	}

	private void createCheckTopicQuesDetailsTable(
			List<ChecklistQuestion> checklistQuestions) {

		if (checklistQuestions.size() > 0) {
			Paragraph details = new Paragraph();

			details.add(new Chunk(constants.questionName() + " "
					+ constants.details(), subTitleFont));
			// addEmptyLine(details, 1);

			try {
				document.add(details);
			} catch (DocumentException e) {
				Log.error("in PdfUtil.addDetails(): " + e.getMessage());
			}

			Log.info("checklistQuestions size " + checklistQuestions.size());
			for (ChecklistQuestion checklistQuestion : checklistQuestions) {

				String question = (checklistQuestion.getQuestion() != null) ? checklistQuestion
						.getQuestion() : "-";
				String instruction = (checklistQuestion.getInstruction() != null) ? ""
						+ checklistQuestion.getInstruction()
						: "-";

				// Log.info(" ~~ question " + question);
				// Log.info(" ~~~~~~ instruction " + instruction);
				PdfPTable chkListQuesTable = new PdfPTable(4);

				chkListQuesTable.addCell(getPdfCellBold(constants
						.questionName()));
				chkListQuesTable.addCell(getPdfCell(question));
				chkListQuesTable.addCell(getPdfCellBold(constants
						.questionInstruction()));
				chkListQuesTable.addCell(getPdfCell(instruction, italicFont, 0,
						0));

				chkListQuesTable.setWidthPercentage(100);
				// chkListQuesTable.setSpacingBefore(titleTableSpacing);

				try {
					details = new Paragraph();
					addEmptyLine(details, 1);
					document.add(details);

					document.add(createPara(chkListQuesTable, ""));
				} catch (DocumentException e) {
					Log.error("in PdfUtil.addDetails(): " + e.getMessage());
				}

				PdfPTable criteriaTable = new PdfPTable(1);

				Set<ChecklistCriteria> checklistQuestionSet = checklistQuestion
						.getCheckListCriterias();

				Log.info("checklistQuestionSet size : "
						+ checklistQuestionSet.size());
				StringBuffer criteriaBuffer = new StringBuffer("");

				boolean isFirst = false;
				for (ChecklistCriteria checklistCriteria : checklistQuestionSet) {

					String criteria = (checklistCriteria.getCriteria() != null) ? ""
							+ checklistCriteria.getCriteria()
							: "-";
					if (isFirst) {
						criteriaBuffer.append(", ");
					} else {
						isFirst = true;
					}

					Log.info(" ~~~~~~ criteria " + criteria);
					criteriaBuffer.append(criteria);

				}

				criteriaTable.addCell(getPdfCell(criteriaBuffer.toString(),
						italicFont, 0, 0));

				Paragraph paragraph = new Paragraph();
				// pdfPTable.setSpacingBefore(titleTableSpacing);
				// addEmptyLine(details, 1);

				paragraph.add(constants.criterion() + " : "
						+ criteriaBuffer.toString());

				// PdfPCell pdfPCell = new PdfPCell(createPara(criteriaTable,
				// ""));
				// pdfPCell.setBorder(Rectangle.NO_BORDER);
				//
				// chkListQuesTable.addCell(pdfPCell);

				try {
					// document.add(createPara(criteriaTable,
					// constants.criterion()));

					document.add(paragraph);
				} catch (DocumentException e) {
					Log.error("in PdfUtil.addDetails(): " + e.getMessage());
				}

				// PdfPTable optionTable = new PdfPTable(3);
				PdfPTable optionTable = new PdfPTable(2);

				Log.info("getCheckListOptions size : "
						+ checklistQuestion.getCheckListOptions().size());

				optionTable.addCell(getPdfCellBold(constants.optionName()));
				// optionTable.addCell(getPdfCellBold(constants.name()));
				optionTable.addCell(getPdfCellBold(constants.optionValue()));

				Set<ChecklistOption> checklistOptions = checklistQuestion
						.getCheckListOptions();
				for (ChecklistOption checklistOption : checklistOptions) {

					String optionName = (checklistOption.getOptionName() != null) ? ""
							+ checklistOption.getOptionName()
							: "-";
					// String name = (checklistOption.getName() != null) ? ""
					// + checklistOption.getName() : "-";
					String value = (checklistOption.getValue() != null) ? ""
							+ checklistOption.getValue() : "-";

					Log.info("optionName " + optionName);
					// Log.info("name " + name);
					Log.info("value " + value);

					optionTable
							.addCell(getPdfCell(optionName, italicFont, 0, 0));
					// optionTable.addCell(getPdfCell(name, italicFont, 0, 0));
					optionTable.addCell(getPdfCell(value, italicFont, 0, 0));

				}
				// chkListQuesTable.addCell(optionTable);

				// pdfPCell = new PdfPCell(createPara(optionTable, ""));
				// pdfPCell.setBorder(Rectangle.NO_BORDER);
				//
				// chkListQuesTable.addCell(pdfPCell);

				try {
					document.add(createPara(optionTable,
							constants.optionDetail()));
				} catch (DocumentException e) {
					Log.error("in PdfUtil.addDetails(): " + e.getMessage());
				}

			}
		}

	}

	private PdfPTable createRoomMaterialDetailsTable() {

		PdfPTable roomMaterialTable = new PdfPTable(3);

		List<UsedMaterial> usedMaterials = UsedMaterial
				.findUsedMaterialsByStandardizedRoleIDpdf(standardizedRole.getId());
		Log.info("usedMaterials size " + usedMaterials.size());

		roomMaterialTable.addCell(getPdfCellBold(constants.roomMaterialName()));
		roomMaterialTable.addCell(getPdfCellBold(constants.number()));
		roomMaterialTable.addCell(getPdfCellBold(constants.forWho()));
		if (usedMaterials.size() > 0) {
			isValueAvailable[2] = true;
			for (UsedMaterial usedMaterial : usedMaterials) {

				String materialName = (usedMaterial.getMaterialList() != null) ? usedMaterial
						.getMaterialList().getName() : "-";
				String materialCount = (usedMaterial.getMaterialCount() != null) ? ""
						+ usedMaterial.getMaterialCount()
						: "-";
				String usedFor = (usedMaterial.getUsed_from() != null) ? ""
						+ enumConstants.getString(usedMaterial.getUsed_from()
								.toString()) : "-";

				roomMaterialTable.addCell(getPdfCell(materialName));
				roomMaterialTable.addCell(getPdfCell(materialCount, italicFont,
						0, 0));
				roomMaterialTable
						.addCell(getPdfCell(usedFor, italicFont, 0, 0));

			}

			roomMaterialTable.addCell(getPdfCellBold(" "));
			roomMaterialTable.addCell(getPdfCell(" "));
		}
		roomMaterialTable.setWidthPercentage(100);

		return roomMaterialTable;
	}

	private void createCheckListDetailsTable() {

		if ((standardizedRole.getCheckList() != null)
				&& (standardizedRole.getCheckList().getCheckListTopics() != null)) {

			isValueAvailable[3] = true;
			CheckList checkList = standardizedRole.getCheckList();

			List<ChecklistTopic> checklistTopics = checkList
					.getCheckListTopics();

			Log.info("CheckList size " + checklistTopics.size());

			for (ChecklistTopic checklistTopic : checklistTopics) {

				String chkListTitle = (checklistTopic.getTitle() != null) ? checklistTopic
						.getTitle() : "-";
				String chkListDesc = (checklistTopic.getDescription() != null) ? checklistTopic
						.getDescription() : "-";

				PdfPTable checkListTable = new PdfPTable(4);

				checkListTable.addCell(getPdfCellBold(constants.title()));
				checkListTable.addCell(getPdfCell(chkListTitle));
				checkListTable.addCell(getPdfCellBold(constants
						.topicDescription()));
				checkListTable.addCell(getPdfCell(chkListDesc));
				checkListTable.addCell(getPdfCellBold(""));
				checkListTable.addCell(getPdfCellBold(""));

				checkListTable.setSpacingBefore(titleTableSpacing);

				checkListTable.setWidthPercentage(100);

				try {
					Paragraph details = new Paragraph();
					addEmptyLine(details, 1);
					document.add(details);

					document.add(checkListTable);

				} catch (DocumentException e) {
					Log.error("in PdfUtil.addDetails(): " + e.getMessage());
				}
				createCheckTopicQuesDetailsTable(checklistTopic
						.getCheckListQuestions());
			}
		}
	}

	private PdfPTable createFileDetailsTable() {

		PdfPTable fileTable = new PdfPTable(2);

		List<File> files = File.getFilesByStandardizedRoleID(standardizedRole
				.getId());
		Log.info("File size " + files.size());
		if (files.size() > 0) {
			isValueAvailable[1] = true;

			fileTable.addCell(getPdfCellBold(constants.filePath()));
			fileTable.addCell(getPdfCellBold(constants.fileDescription()));

			for (File file : files) {

				String fileDesc = (file.getDescription() != null) ? file
						.getDescription() : "-";
				String filePath = (file.getPath() != null) ? file.getPath()
						: "-";
				fileTable.addCell(getPdfCell(fileDesc));
				fileTable.addCell(getPdfCell(filePath, italicFont, 0, 0));
			}

			fileTable.addCell(getPdfCellBold(" "));
			fileTable.addCell(getPdfCell(" "));

			fileTable.setWidthPercentage(100);
		}

		return fileTable;
	}

	private PdfPTable createDetailsTable() {
		PdfPTable table = new PdfPTable(new float[] { 0.2f, 0.3f, 0.2f, 0.3f });

		String shortName = (standardizedRole.getShortName() != null) ? standardizedRole
				.getShortName().toString() : "-";
		String longName = (standardizedRole.getLongName() != null) ? standardizedRole
				.getLongName() : "-";
		String roleType = (standardizedRole.getRoleType() != null) ? enumConstants
				.getString(standardizedRole.getRoleType().toString()) : "-";

		String studyYear = (standardizedRole.getStudyYear() != null) ? enumConstants
				.getString(standardizedRole.getStudyYear().toString()) : "-";

		table.addCell(getPdfCellBold(constants.shortName() + ":"));
		// TODO format date
		table.addCell(getPdfCell(shortName));

		table.addCell(getPdfCellBold(constants.name() + ":"));
		table.addCell(getPdfCell(longName));

		table.addCell(getPdfCellBold(constants.roleType() + ":"));
		table.addCell(getPdfCell(roleType));

		table.addCell(getPdfCellBold(constants.studyYear() + ":"));
		table.addCell(getPdfCell(studyYear));

		table.addCell(getPdfCellBold(" "));
		table.addCell(getPdfCell(" "));
		isValueAvailable[0] = true;

		table.setWidthPercentage(100);
		return table;
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
}