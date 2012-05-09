package ch.unibas.medizin.osce.client.a_nonroo.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class CalendarUtil {
	private static final String D_M_YYYY = "d-M-yyyy";
	private static final String DATE_SEPARATOR = "-";
	
	private Date date;
	private int day;
	private int month;
	private int year;
	
	public CalendarUtil() {
		this(new Date());
	}
	
	public CalendarUtil(Date date) {
		this.date = date;
		extractDayMonthYear();
	}
	
	private void extractDayMonthYear() {
		String parts[] = DateTimeFormat.getFormat(D_M_YYYY).format(date).split(DATE_SEPARATOR);
		day = Integer.parseInt(parts[0]);
		month = Integer.parseInt(parts[1]);
		year = Integer.parseInt(parts[2]);
	}
	
	public Date getDate() {
		return date;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setDay(int day) {
		if (day > getDaysInMonth() || day < 1)throw new IndexOutOfBoundsException();
		Date date = createDateFromInteger(day, month, year);
		if (date == null) throw new IndexOutOfBoundsException();
		this.date = date;
		this.day = day;
	}
	
	public void setMonth(int month) {
		if (month < 1 || month > 12) throw new IndexOutOfBoundsException();
		Date date = createDateFromInteger(day, month, year);
		if (date == null) throw new IndexOutOfBoundsException();
		this.date = date;
		this.month = month;
	}
	
	public void setYear(int year) {
		Date date = createDateFromInteger(day, month, year);
		if (date == null) throw new IndexOutOfBoundsException();
		this.date = date;
		this.year = year;
	}
	
	public int getDaysInMonth() {
		if (month == 2) {
			if (year % 4 == 0) {
				if ( (!(year % 100 == 0)) || (year % 400 == 0)) {
					return 29;
				}
			}
			return 28;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		return 31;
	}
	
	public void setDate(Date date) {
		this.date = date;
		extractDayMonthYear();
	}
	
	private static Date createDateFromInteger(Integer dd, Integer mm, Integer yyyy) {
		if (dd == null || mm == null || yyyy == null)
			return null;

		Date retVal = null;
		try {
			retVal = DateTimeFormat.getFormat(D_M_YYYY).parse(
					dd + DATE_SEPARATOR + mm + DATE_SEPARATOR + yyyy);
		} catch (Exception e) {
			retVal = null;
		}

		return retVal;
	}

//	public static boolean isValidDate(Integer dd, Integer mm, Integer yyyy) {
//		boolean isvalidDate = true;
//
//		try {
//			String transformedInput = DateTimeFormat.getFormat(D_M_YYYY)
//					.format(getDate(dd, mm, yyyy));
//			String originalInput = dd + DATE_SEPARATOR + mm + DATE_SEPARATOR
//					+ yyyy;
//
//			isvalidDate = transformedInput.equals(originalInput);
//		} catch (Exception e) {
//			isvalidDate = false;
//		}
//
//		return isvalidDate;
//	}
}
