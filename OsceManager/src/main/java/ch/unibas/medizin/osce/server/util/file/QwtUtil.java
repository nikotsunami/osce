package ch.unibas.medizin.osce.server.util.file;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.shared.BellAssignmentType;
import ch.unibas.medizin.osce.shared.BellTone;
import ch.unibas.medizin.osce.shared.TimeBell;

/**
 * This class is used to write the qwt file from the ResultSet. It will write
 * the all row from the assignment in specified manner.
 * 
 * @author SPEC-India
 * 
 */

@SuppressWarnings("deprecation")
public class QwtUtil extends FileUtil {

	private String separater = "\n";

	private String EOF = "4B4143424B015840725000000001010C1F0001FFFFFFFFFF0002FFFFFFFFFF0003FFFFFFFFFF0004FFFFFFFFFF0005FFFFFFFFFF0006FFFFFFFFFF0007FFFFFFFFFF0008FFFFFFFFFF0009FFFFFFFFFF00"
			+ separater
			+ "4B4143424B0158407250000AFFFFFFFFFF000BFFFFFFFFFF000CFFFFFFFFFF000DFFFFFFFFFF000EFFFFFFFFFF000FFFFFFFFFFF0010FFFFFFFFFF0011FFFFFFFFFF0012FFFFFFFFFF0013FFFFFFFFFF00"
			+ separater
			+ "4B4143424B01584072500014FFFFFFFFFF0015FFFFFFFFFF0016FFFFFFFFFF0017FFFFFFFFFF0018FFFFFFFFFF0019FFFFFFFFFF001AFFFFFFFFFF001BFFFFFFFFFF001CFFFFFFFFFF001DFFFFFFFFFF00"
			+ separater
			+ "4B4143424B0158407250001EFFFFFFFFFF001FFFFFFFFFFF0020FFFFFFFFFF0021FFFFFFFFFF0022FFFFFFFFFF0023FFFFFFFFFF0024FFFFFFFFFF0025FFFFFFFFFF0026FFFFFFFFFF0027FFFFFFFFFF00"
			+ separater
			+ "4B4143424B01584072500028FFFFFFFFFF0029FFFFFFFFFF002AFFFFFFFFFF002BFFFFFFFFFF002CFFFFFFFFFF002DFFFFFFFFFF002EFFFFFFFFFF002FFFFFFFFFFF0030FFFFFFFFFF0031FFFFFFFFFF00"
			+ separater
			+ "4B4143424B01584072500032FFFFFFFFFF0033FFFFFFFFFF0034FFFFFFFFFF0035FFFFFFFFFF0036FFFFFFFFFF0037FFFFFFFFFF0038FFFFFFFFFF0039FFFFFFFFFF003AFFFFFFFFFF003BFFFFFFFFFF00"
			+ separater
			+ "4B4143424B0158407250003CFFFFFFFFFF003DFFFFFFFFFF003EFFFFFFFFFF003FFFFFFFFFFF0040FFFFFFFFFF0041FFFFFFFFFF0042FFFFFFFFFF0043FFFFFFFFFF0044FFFFFFFFFF0045FFFFFFFFFF00"
			+ separater
			+ "4B4143424B01584072500046FFFFFFFFFF0047FFFFFFFFFF0048FFFFFFFFFF0049FFFFFFFFFF004AFFFFFFFFFF004BFFFFFFFFFF004CFFFFFFFFFF004DFFFFFFFFFF004EFFFFFFFFFF004FFFFFFFFFFF00"
			+ separater
			+ "4B4143424B01584072500050FFFFFFFFFF0051FFFFFFFFFF0052FFFFFFFFFF0053FFFFFFFFFF0054FFFFFFFFFF0055FFFFFFFFFF0056FFFFFFFFFF0057FFFFFFFFFF0058FFFFFFFFFF0059FFFFFFFFFF00"
			+ separater
			+ "4B4143424B0158407250005AFFFFFFFFFF005BFFFFFFFFFF005CFFFFFFFFFF005DFFFFFFFFFF005EFFFFFFFFFF005FFFFFFFFFFF0060FFFFFFFFFF0061FFFFFFFFFF0062FFFFFFFFFF0063010000000000"
			+ separater
			+ "3541434235015840724801000000000000000000000020010000000001080000000000000000010000000000000000000000000000000000000300";

	public void writeQwt(List<BellAssignmentType> bellAssignmentTypes)
			throws SQLException, IOException {

		StringBuffer rowData;
		BellAssignmentType bellAssignmentType;
		int counter = 0;

		for (Iterator<BellAssignmentType> iterator = bellAssignmentTypes
				.iterator(); iterator.hasNext();) {
			bellAssignmentType = (BellAssignmentType) iterator.next();

			rowData = new StringBuffer();
			rowData.append("13414342130158407252"
					// Integer.toHexString(counter)
					+ String.format("%04X", counter)
					+ "00B9000040000"
					+ bellAssignmentType.getBellTone().getQwtTone()
					+ "0C0"
					+ Integer.toHexString(bellAssignmentType.getOsceDate()
							.getMonth() + 1)
					+ String.format("%02X",
							(bellAssignmentType.getOsceDate().getDate()))
					+ getTimeInSeconds(bellAssignmentType.getOsceTime()) + "00"
					+ separater);
//			Log.info("rowData : " + rowData);

			write(rowData.toString());
			counter++;
		}

		write(EOF);

	}

	private String getTimeInSeconds(Date time) {

		return String.format("%04X",
				(((time.getHours() * 3600) + (time.getMinutes() * 60) + (time
						.getSeconds())) / 2));

	}

	public String getSeparater() {
		return separater;
	}

	public void setSeparater(String separater) {
		this.separater = separater;
	}

	private static BellAssignmentType getBellAssignmentType(Date osceDate,
			String osceName) {

		BellAssignmentType bellAssignmentType = new BellAssignmentType();

		bellAssignmentType.setOsceDate(osceDate);
		bellAssignmentType.setOsceName(osceName);

		return bellAssignmentType;

	}

	private static Date getNewDate(Date oldDate, Integer time,
			Integer preparationRing, TimeBell isPlusTime) {

		Date newDate = null;

		switch (isPlusTime) {

		case FALSE: {

			newDate = getCalculatedDate(oldDate, -time, preparationRing);
			break;
		}

		case TRUE: {

			newDate = getCalculatedDate(oldDate, time, preparationRing);
			break;
		}

		default:
		case NONE: {
			newDate = getCalculatedDate(oldDate, 0, preparationRing);
			break;
		}
		}
		return newDate;
	}

	private static Date getCalculatedDate(Date oldDate, Integer time,
			Integer preparationRing) {

		Long timeInMinute = (oldDate.getTime() + time - preparationRing);
		return (new Date(timeInMinute));
	}

	/*public static List<BellAssignmentType> getBellAssignmentType(
			List<Assignment> assignmentProxyList, Integer time,
			TimeBell isPlusTime, Semester semester) {

		List<BellAssignmentType> bellAssignmentTypes = new ArrayList<BellAssignmentType>();

		if (assignmentProxyList != null && assignmentProxyList.size() > 0) {

			Iterator<Assignment> iterator = assignmentProxyList.iterator();
			Assignment assignment = (Assignment) iterator.next();
			Assignment assignmentNext = (Assignment) iterator.next();

			BellAssignmentType bellAssignmentType = null;

			int preparationRing = (semester.getPreparationRing() == null) ? 0
					: semester.getPreparationRing()
							* BellAssignmentType.MILLISECONDTOMINUTE;

			time *= BellAssignmentType.MILLISECONDTOMINUTE;

			if (preparationRing > 0) {
				bellAssignmentType = getBellAssignmentType(assignment
						.getOsceDay().getOsceDate(), assignment.getOsceDay()
						.getOsce().getName());

				System.out.println("assignment.getOsceDay().getTimeStart()"
						+ assignment.getOsceDay().getTimeStart());

				bellAssignmentType.setOsceTime(getNewDate(assignment
						.getOsceDay().getTimeStart(), time, preparationRing,
						isPlusTime));
				bellAssignmentType.setBellTone(BellTone.TONE_8);

				bellAssignmentTypes.add(bellAssignmentType);
			}

			do {

				// System.out.println("PreparationRing "
				// + assignment.getOsceDay().getOsce()
				// .getPreparationRing());
				bellAssignmentType = getBellAssignmentType(assignment
						.getOsceDay().getOsceDate(), assignment.getOsceDay()
						.getOsce().getName());

				bellAssignmentType.setBellTone(BellTone.TONE_2);

				bellAssignmentType.setOsceTime(getNewDate(
						assignment.getTimeStart(), time, 0, isPlusTime));

				bellAssignmentTypes.add(bellAssignmentType);

				bellAssignmentType = getBellAssignmentType(assignment
						.getOsceDay().getOsceDate(), assignment.getOsceDay()
						.getOsce().getName());

				bellAssignmentType.setOsceTime(getNewDate(
						assignment.getTimeEnd(), time, 0, isPlusTime));

				// if (assignment.getTimeEnd().getTime() != assignmentNext
				// .getTimeStart().getTime()) {

				if (assignment.getOsceDay().getOsce().getId().longValue() == assignmentNext
						.getOsceDay().getOsce().getId().longValue()) {

					Long differenceTime = ((assignmentNext.getTimeStart()
							.getTime() - assignment.getTimeEnd().getTime()) / BellAssignmentType.MILLISECONDTOMINUTE);

					// System.out.println("id: " + assignment.getId()
					// + " assignment.getTimeStart() "
					// + assignment.getTimeStart()
					// + "  assignment.getTimeEnd()"
					// + assignment.getTimeEnd());
					// System.out.println("id: " + assignmentNext.getId()
					// + " assignmentNext.getTimeStart() "
					// + assignmentNext.getTimeStart()
					// + "  assignmentNext.getTimeEnd()"
					// + assignmentNext.getTimeEnd());
					//
					// System.out
					// .println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ !@#$ differenceTime : "
					// + differenceTime);

					if ((differenceTime.longValue() > 0)
							&& ((differenceTime.longValue() >= assignment
									.getOsceDay().getOsce().getLongBreak()
									.longValue())
									|| (differenceTime.longValue() >= assignment
											.getOsceDay().getOsce()
											.getLunchBreak().longValue()) || (differenceTime
									.longValue() >= assignment.getOsceDay()
									.getOsce().getMiddleBreak().longValue()))) {

						bellAssignmentType.setBellTone(BellTone.TONE_8);
						bellAssignmentTypes.add(bellAssignmentType);

						if ((differenceTime.longValue() > assignment
								.getOsceDay().getOsce().getMiddleBreak()
								.longValue())
								&& (preparationRing > 0)) {

							bellAssignmentType = getBellAssignmentType(
									assignmentNext.getOsceDay().getOsceDate(),
									assignmentNext.getOsceDay().getOsce()
											.getName());

							bellAssignmentType.setOsceTime(getNewDate(
									assignmentNext.getTimeStart(), time,
									preparationRing, isPlusTime));

							bellAssignmentType.setBellTone(BellTone.TONE_8);
							bellAssignmentTypes.add(bellAssignmentType);
						}

					} else {
						bellAssignmentType.setBellTone(BellTone.TONE_4);
						bellAssignmentTypes.add(bellAssignmentType);
					}

				} else {
					bellAssignmentType.setBellTone(BellTone.TONE_4);
					bellAssignmentTypes.add(bellAssignmentType);

					if (preparationRing > 0) {
						bellAssignmentType = getBellAssignmentType(
								assignmentNext.getOsceDay().getOsceDate(),
								assignmentNext.getOsceDay().getOsce().getName());

//						System.out
//								.println("assignmentNext.getOsceDay().getTimeStart()"
//										+ assignmentNext.getOsceDay()
//												.getTimeStart());

						bellAssignmentType.setOsceTime(getNewDate(
								assignmentNext.getOsceDay().getTimeStart(),
								time, preparationRing, isPlusTime));
						bellAssignmentType.setBellTone(BellTone.TONE_8);

						bellAssignmentTypes.add(bellAssignmentType);
					}
				}

				if (iterator.hasNext()) {
					assignment = assignmentNext;
					assignmentNext = (Assignment) iterator.next();
					// preparationRing = (assignment.getOsceDay().getOsce()
					// .getPreparationRing() == null) ? 0 : assignment
					// .getOsceDay().getOsce().getPreparationRing()
					// * BellAssignmentType.MILLISECONDTOMINUTE;

				} else {
					break;
				}

			} while (iterator.hasNext());

		}
		return bellAssignmentTypes;

		//
		// do {
		//
		// bellAssignmentType = new BellAssignmentType();
		//
		// bellAssignmentType.setBellTone(BellTone.LONG_BREAK);
		// bellAssignmentType.setOsceDate(assignment.getOsceDay()
		// .getOsceDate());
		// bellAssignmentType.setOsceName(assignment.getOsceDay().getOsce()
		// .getName());
		// bellAssignmentType.setOsceTime(assignment.getTimeStart()
		// // getOsceDay().getTimeStart()
		// );
		//
		// bellAssignmentTypes.add(bellAssignmentType);
		//
		// if (assignment.getOsceDay().getOsceDate().getTime() != assignmentNext
		// .getOsceDay().getOsceDate().getTime()) {
		//
		// Long differenceTime = (assignment.getTimeEnd().getTime() -
		// assignmentNext
		// .getTimeStart().getTime()) / 600;
		//
		// Log.info("~~~~~~~~~~~~~~~~~~~~~~~~~ !@#$ differenceTime : "
		// + differenceTime);
		//
		// if ((differenceTime.longValue() == assignment.getOsceDay()
		// .getOsce().getLongBreak().longValue())
		// || (differenceTime.longValue() == assignment
		// .getOsceDay().getOsce().getLunchBreak()
		// .longValue())
		// || (differenceTime.longValue() == assignment
		// .getOsceDay().getOsce().getMiddleBreak()
		// .longValue())) {
		//
		// }
		// }
		//
		// if (iterator.hasNext()) {
		// assignment = assignmentNext;
		// assignmentNext = (Assignment) iterator.next();
		// } else {
		// break;
		// }
		// // assignment.osceDay.get
		// } while (iterator.hasNext());
		//
		// return bellAssignmentTypes;

	}*/
	
	public static List<BellAssignmentType> getBellAssignmentType(
			List<Assignment> assignmentProxyList, Integer time,
			TimeBell isPlusTime, Semester semester) {
		

		List<BellAssignmentType> bellAssignmentTypes = new ArrayList<BellAssignmentType>();
		
		if (assignmentProxyList != null && assignmentProxyList.size() > 0) {
			
			Iterator<Assignment> iterator = assignmentProxyList.iterator();
			int rotationNumber = 0;
			int preparationRing = (semester.getPreparationRing() == null) ? 0
					: semester.getPreparationRing()
							* BellAssignmentType.MILLISECONDTOMINUTE;
			
			BellTone lastTone = null;
			
			Assignment assignment = iterator.next();
			Assignment assignmentNext = null;
			
			if (assignmentProxyList.size() == 1)
			{
				BellAssignmentType startBellAssignmentType = new BellAssignmentType();				
				startBellAssignmentType = getBellAssignmentType(assignment
						.getOsceDay().getOsceDate(), assignment.getOsceDay()
						.getOsce().getName());
				if ((lastTone == null || lastTone == BellTone.TONE_8) && preparationRing != 0)
				{
					startBellAssignmentType.setBellTone(BellTone.TONE_8);
					startBellAssignmentType.setOsceTime(getNewDate(
							assignment.getTimeStart(), time, preparationRing, isPlusTime));
				}
				else
				{
					startBellAssignmentType.setBellTone(BellTone.TONE_2);
					startBellAssignmentType.setOsceTime(getNewDate(
							assignment.getTimeStart(), time, 0, isPlusTime));
				}
				bellAssignmentTypes.add(startBellAssignmentType);
				
				
				BellAssignmentType endBellAssignmentType = new BellAssignmentType();				
				endBellAssignmentType = getBellAssignmentType(assignment
						.getOsceDay().getOsceDate(), assignment.getOsceDay()
						.getOsce().getName());
				endBellAssignmentType.setOsceTime(getNewDate(
						assignment.getTimeEnd(), time, 0, isPlusTime));
				endBellAssignmentType.setBellTone(BellTone.TONE_4);				
				bellAssignmentTypes.add(endBellAssignmentType);
			}
			else
			{
				while (iterator.hasNext())
				{	
					if ((lastTone == null || lastTone == BellTone.TONE_8) && preparationRing > 0)
					{
						BellAssignmentType earlyStartBellAssignmentType = new BellAssignmentType();					
						earlyStartBellAssignmentType = getBellAssignmentType(assignment.getOsceDay().getOsceDate(), assignment.getOsceDay().getOsce().getName());					
						earlyStartBellAssignmentType.setBellTone(BellTone.TONE_8);
						earlyStartBellAssignmentType.setOsceTime(getNewDate(assignment.getTimeStart(), time, preparationRing, isPlusTime));
						bellAssignmentTypes.add(earlyStartBellAssignmentType);
						lastTone = BellTone.TONE_2;
					}
					
					BellAssignmentType startBellAssignmentType = new BellAssignmentType();					
					startBellAssignmentType = getBellAssignmentType(assignment.getOsceDay().getOsceDate(), assignment.getOsceDay().getOsce().getName());					
					startBellAssignmentType.setBellTone(BellTone.TONE_2);
					startBellAssignmentType.setOsceTime(getNewDate(assignment.getTimeStart(), time, 0, isPlusTime));
					bellAssignmentTypes.add(startBellAssignmentType);
					
					BellAssignmentType endBellAssignmentType = new BellAssignmentType();					
					endBellAssignmentType = getBellAssignmentType(assignment.getOsceDay().getOsceDate(), assignment.getOsceDay().getOsce().getName());
					endBellAssignmentType.setOsceTime(getNewDate(assignment.getTimeEnd(), time, 0, isPlusTime));

					if (iterator.hasNext())
					{
						assignmentNext = iterator.next();
						if (rotationNumber != assignmentNext.getRotationNumber())
						{
							endBellAssignmentType.setBellTone(BellTone.TONE_8);
							rotationNumber = assignmentNext.getRotationNumber();
							lastTone = BellTone.TONE_8;
						}
						else
						{
							endBellAssignmentType.setBellTone(BellTone.TONE_4);
							lastTone = BellTone.TONE_4;
						}
						
						assignment = assignmentNext;
						assignmentNext = null;
					}
					else
					{
						endBellAssignmentType.setBellTone(BellTone.TONE_4);
						lastTone = BellTone.TONE_4;
					}
					
					bellAssignmentTypes.add(endBellAssignmentType);
				}
				
				if (assignmentNext != null)
				{
					BellAssignmentType startBellAssignmentType = new BellAssignmentType();				
					startBellAssignmentType = getBellAssignmentType(assignmentNext
							.getOsceDay().getOsceDate(), assignmentNext.getOsceDay()
							.getOsce().getName());
					startBellAssignmentType.setBellTone(BellTone.TONE_2);
					startBellAssignmentType.setOsceTime(getNewDate(
							assignmentNext.getTimeStart(), time, 0, isPlusTime));
					bellAssignmentTypes.add(startBellAssignmentType);
					
					
					BellAssignmentType endBellAssignmentType = new BellAssignmentType();				
					endBellAssignmentType = getBellAssignmentType(assignmentNext
							.getOsceDay().getOsceDate(), assignmentNext.getOsceDay()
							.getOsce().getName());
					endBellAssignmentType.setOsceTime(getNewDate(
							assignmentNext.getTimeEnd(), time, 0, isPlusTime));
					endBellAssignmentType.setBellTone(BellTone.TONE_4);				
					bellAssignmentTypes.add(endBellAssignmentType);
				}
			}
		}	
		return bellAssignmentTypes;
	}
}
