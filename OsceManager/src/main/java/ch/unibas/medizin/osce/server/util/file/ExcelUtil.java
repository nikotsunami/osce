package ch.unibas.medizin.osce.server.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.domain.StandardizedPatient;

public class ExcelUtil
{
	
	Map<Long, List<Long>> mainMap = new HashMap<Long, List<Long>>();
	HSSFWorkbook workbook = null;
	DecimalFormat form = null;
	//String semesterName = "";
	
	public ExcelUtil() {
		//this.semesterName = semesterValue;
		workbook = new HSSFWorkbook();
		form = new DecimalFormat("0.00");
	}
	
	public void setHeaderRowCellStyle(HSSFRow row)
	{
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		
		Iterator<HSSFCell> cellItr = row.cellIterator();
		
		while (cellItr.hasNext())
		{
			HSSFCell cell = cellItr.next();
			cell.setCellStyle(cellStyle);
		}		
	}
	
	public void writeSheet(Map<Long, List<Long>> map, String osceName, Long semesterId)
	{
		HSSFSheet sheet = workbook.createSheet(osceName);
		
		sheet.setColumnWidth(0, 5500);
		sheet.setColumnWidth(1, 5500);
		sheet.setColumnWidth(2, 5500);
		sheet.setColumnWidth(3, 5500);
		sheet.setColumnWidth(4, 5500);
		sheet.setColumnWidth(5, 5500);
		
		HSSFRow headerRow = sheet.createRow(0);	
		
		headerRow.createCell(0).setCellValue(new HSSFRichTextString("Standardized Patient"));
		headerRow.createCell(1).setCellValue(new HSSFRichTextString("Simpat Hours"));
		headerRow.createCell(2).setCellValue(new HSSFRichTextString("Statist Hours"));
		headerRow.createCell(3).setCellValue(new HSSFRichTextString("Simpat Hours Cost"));
		headerRow.createCell(4).setCellValue(new HSSFRichTextString("Statist Hours Cost"));
		headerRow.createCell(5).setCellValue(new HSSFRichTextString("Total Cost"));
		
		setHeaderRowCellStyle(headerRow);
		
		Semester semester = Semester.findSemester(semesterId);
		
		int ctr = 1;
		
		Iterator<Long> itr = map.keySet().iterator();
		
		while (itr.hasNext())
		{
			Long key = itr.next();
			
			StandardizedPatient stdPat = StandardizedPatient.findStandardizedPatient(key);
			
			List<Long> list = new ArrayList<Long>();
			list = map.get(key);
			
			Long sphrs = list.get(0);
			Long statistHrs = list.get(1);
			
			float floatSpHrs = sphrs / 60.0f;
			float floatStHrs = statistHrs / 60.0f;

			Double spPrice = 0d;
			Double statistPrice = 0d;
			
			if (semester.getPriceStandardizedPartient() != null)
				spPrice = semester.getPriceStandardizedPartient();
			
			if (semester.getPricestatist() != null)
				statistPrice = semester.getPricestatist();
			
			double dblSpCost = floatSpHrs * spPrice;
			double dblStCost = floatStHrs * statistPrice;
			
			String disSpHrs = "";
			String disStatistHrs = "";
			
			long hr = (long) sphrs / 60;
			long min = (long) sphrs % 60;
			disSpHrs = hr + "." + String.format("%02d" , min);
			
			hr = min = 0;
			
			hr = (long) statistHrs / 60;
			min = (long) statistHrs % 60;
			disStatistHrs = hr + "." + String.format("%02d" , min);
			
			String spname = stdPat.getName() + " " + stdPat.getPreName();
			
			HSSFRow contentRow = sheet.createRow(ctr);			
			contentRow.createCell(0).setCellValue(spname);
			contentRow.createCell(1).setCellValue(disSpHrs);
			contentRow.createCell(2).setCellValue(disStatistHrs);
			contentRow.createCell(3).setCellValue(form.format(dblSpCost));
			contentRow.createCell(4).setCellValue(form.format(dblStCost));
			contentRow.createCell(5).setCellValue(form.format(dblSpCost + dblStCost));
			ctr++;
		}
	}
	
	public void writeExcel(String name) throws IOException
	{
		String path = StandardizedPatient.fetchRealPath() + name;
		FileOutputStream out = new FileOutputStream(new File(path));
		workbook.write(out);
		out.close();
	}
}
