package ch.unibas.medizin.osce.server.util.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.unibas.medizin.osce.server.i18n.GWTI18N;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;
import ch.unibas.medizin.osce.shared.i18n.OsceConstantsWithLookup;

import org.apache.log4j.Logger;

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
//import com.mattbertolini.hermes.Hermes;

public abstract class PdfUtil {
	protected static final float titleTableSpacing = 0.0f;
	protected static Logger log = Logger.getLogger(PdfUtil.class);

	protected Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	protected Font defaultFont = new Font(Font.FontFamily.HELVETICA, 10);
	protected Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	protected Font italicFont = new Font(Font.FontFamily.HELVETICA, 10,
			Font.ITALIC);
	protected Font paragraphTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
			Font.BOLD);
	protected Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLDITALIC);
	protected Font smallFont = new Font(Font.FontFamily.HELVETICA, 9);
	protected Font smallItalicFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);
	protected Font emailFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.UNDERLINE);

	protected Document document = new Document();
	protected OsceConstants constants;
	protected OsceConstantsWithLookup enumConstants;
	protected String title;
	protected PdfWriter writer;

	public PdfUtil() {
		this(new Locale("de"));
	}

	public PdfUtil(String locale){
		this(new Locale(locale));
	}
	
	public PdfUtil(Locale locale) {
		try {
			// Feature : 154
			log.info("Before constants");
			constants = GWTI18N.create(OsceConstants.class, locale.toString());
			log.info("After constants");
			log.info("Before enumConstants");
			enumConstants = GWTI18N.create(OsceConstantsWithLookup.class, locale.toString());
			log.info("After enumConstants");
			// Feature : 154
		} catch (IOException e) {
			log.error("PdfUtil() -- Error loading translations: " + e.getMessage());
		} catch (Exception e) {
			log.error("PdfUtil() -- Error loading translations: " + e.getMessage());
		}

	}

	protected Image loadImage(String path) {
		if (path == null) {
			throw new IllegalArgumentException(
					"path to image file has to be provided!");
		}

		try {
			Image image = Image.getInstance(path);
			float width = image.getWidth();
			float height = image.getHeight();

			log.debug("PdfUtil - Image width: " + width + ", Image height: "
					+ height);
			image.scaleAbsolute(30f, 30f);
			// 523 * 0.4, ~7 lines of text
			image.scaleToFit(209.2f, 100f);
			image.setAlignment(Element.ALIGN_LEFT);
			return image;
		} catch (Exception e) {
			log.error("In loadImage( \"" + path + "\"): " + e.getMessage());
			return null;
		}
	}

	protected static class TextCellEvent implements PdfPCellEvent {
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

	protected static class CheckBoxCellEvent implements PdfPCellEvent {
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
					group.setFieldFlags(PdfFormField.FF_READ_ONLY);
				} else {
					field = box.getCheckField();
					writer.addAnnotation(field);
				}
			} catch (IOException e) {
				log.error("in PdfUtil.CheckBoxCellEvent.cellLayout() : "
						+ e.getMessage());
			} catch (DocumentException e) {
				log.error("in PdfUtil.CheckBoxCellEvent.cellLayout() : "
						+ e.getMessage());
			}
		}
	}

	protected static class CheckBoxGroupEvent implements PdfPCellEvent {
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

	protected PdfPCell getRadioCell(String question, String[] possibleAnswers,
			Integer selectedAnswer) {
		if (selectedAnswer == null) {
			return getCheckBoxCell(question, possibleAnswers, null, true);
		}
		List<Integer> selectedAnswers = new ArrayList<Integer>();
		selectedAnswers.add(selectedAnswer);
		return getCheckBoxCell(question, possibleAnswers, selectedAnswers, true);
	}

	protected PdfPCell getCheckBoxCell(String question, String[] possibleAnswers,
			List<Integer> selectedAnswers) {
		return getCheckBoxCell(question, possibleAnswers, selectedAnswers,
				false);
	}

	protected PdfPCell getCheckBoxCell(String question, String[] possibleAnswers,
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

		//PdfPTable subTable = new PdfPTable(new float[] { 0.05f, 0.95f });
		PdfPTable subTable = new PdfPTable(new float[] { 0.10f, 0.90f });

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

	protected PdfPCell getPdfCellBold(String text) {
		return getPdfCell(text, boldFont, 0, 0);
	}
	

	protected PdfPCell getPdfCellItalic(String text) {
		return getPdfCell(text, italicFont, 0, 0);
	}

	protected PdfPCell getPdfCell(String text) {
		return getPdfCell(text, defaultFont, 0, 0);
	}

	protected PdfPCell getPdfCell(String text, Font font, int colSpan, int rowSpan) {
		PdfPCell c2 = new PdfPCell(new Phrase(text, font));
		if (colSpan > 0)
			c2.setColspan(colSpan);
		if (rowSpan > 0)
			c2.setRowspan(rowSpan);
		c2.setBorder(Rectangle.NO_BORDER);
		return c2;
	}
	
	protected PdfPCell getPdfCell(Paragraph paragraph, int colSpan, int rowSpan) {
		PdfPCell c2 = new PdfPCell(paragraph);
		if (colSpan > 0)
			c2.setColspan(colSpan);
		if (rowSpan > 0)
			c2.setRowspan(rowSpan);
		c2.setBorder(Rectangle.NO_BORDER);
		return c2;
	}

	protected void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}