package ch.unibas.medizin.osce.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ch.unibas.medizin.osce.client.managed.request.AssignmentProxy;
import ch.unibas.medizin.osce.client.managed.request.SemesterProxy;

public class BellAssignmentType implements Serializable {

	public static int MILLISECONDTOMINUTE = 60000;

	private String osceName;
	private Date osceDate;
	private Date osceTime;
	private BellTone bellTone;

	public String getOsceName() {
		return osceName;
	}

	public void setOsceName(String osceName) {
		this.osceName = osceName;
	}

	public Date getOsceDate() {
		return osceDate;
	}

	public void setOsceDate(Date osceDate) {
		this.osceDate = osceDate;
	}

	public Date getOsceTime() {
		return osceTime;
	}

	public void setOsceTime(Date osceTime) {
		this.osceTime = osceTime;
	}

	public BellTone getBellTone() {
		return bellTone;
	}

	public void setBellTone(BellTone bellTone) {
		this.bellTone = bellTone;
	}

	/*public static List<BellAssignmentType> getBellAssignmentProxyType(
			List<AssignmentProxy> assignmentProxyList, Integer time,
			TimeBell isPlusTime, SemesterProxy semesterProxy) {
		List<BellAssignmentType> bellAssignmentTypes = new ArrayList<BellAssignmentType>();

		if (assignmentProxyList != null && assignmentProxyList.size() > 0) {

			Iterator<AssignmentProxy> iterator = assignmentProxyList.iterator();
			AssignmentProxy assignment = (AssignmentProxy) iterator.next();
			AssignmentProxy assignmentNext = (AssignmentProxy) iterator.next();

			BellAssignmentType bellAssignmentType = null;

			int preparationRing = (semesterProxy.getPreparationRing() == null) ? 0
					: semesterProxy.getPreparationRing()
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

						System.out
								.println("assignmentNext.getOsceDay().getTimeStart()"
										+ assignmentNext.getOsceDay()
												.getTimeStart());

						bellAssignmentType.setOsceTime(getNewDate(
								assignmentNext.getOsceDay().getTimeStart(),
								time, preparationRing, isPlusTime));
						bellAssignmentType.setBellTone(BellTone.TONE_8);

						bellAssignmentTypes.add(bellAssignmentType);
					}
				}

				if (iterator.hasNext()) {
					assignment = assignmentNext;
					assignmentNext = (AssignmentProxy) iterator.next();
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

	}*/
	
	public static List<BellAssignmentType> getBellAssignmentProxyType(
			List<AssignmentProxy> assignmentProxyList, Integer time,
			TimeBell isPlusTime, SemesterProxy semesterProxy) {
		List<BellAssignmentType> bellAssignmentTypes = new ArrayList<BellAssignmentType>();
		
		if (assignmentProxyList != null && assignmentProxyList.size() > 0) {
			
			Iterator<AssignmentProxy> iterator = assignmentProxyList.iterator();
			int rotationNumber = 0;
			int preparationRing = (semesterProxy.getPreparationRing() == null) ? 0
					: semesterProxy.getPreparationRing()
							* BellAssignmentType.MILLISECONDTOMINUTE;
			
			BellTone lastTone = null;
			
			AssignmentProxy assignment = iterator.next();
			AssignmentProxy assignmentNext = null;
			
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
				
				if (assignmentNext == null)
				{
					BellAssignmentType startBellAssignmentType = new BellAssignmentType();				
					startBellAssignmentType = getBellAssignmentType(assignment
							.getOsceDay().getOsceDate(), assignment.getOsceDay()
							.getOsce().getName());
					startBellAssignmentType.setBellTone(BellTone.TONE_2);
					startBellAssignmentType.setOsceTime(getNewDate(
							assignment.getTimeStart(), time, 0, isPlusTime));
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
			}
			
			
			/*if(assignmentProxyList.size()>1)
			{
			AssignmentProxy lastAssignment=assignmentProxyList.get(assignmentProxyList.size()-1);
			BellAssignmentType startBellAssignmentType = new BellAssignmentType();				
			startBellAssignmentType = getBellAssignmentType(lastAssignment
					.getOsceDay().getOsceDate(), lastAssignment.getOsceDay()
					.getOsce().getName());
			startBellAssignmentType.setBellTone(BellTone.TONE_2);
			startBellAssignmentType.setOsceTime(getNewDate(
					lastAssignment.getTimeStart(), time, 0, isPlusTime));
			bellAssignmentTypes.add(startBellAssignmentType);
			
			
			BellAssignmentType endBellAssignmentType = new BellAssignmentType();				
			endBellAssignmentType = getBellAssignmentType(lastAssignment
					.getOsceDay().getOsceDate(), lastAssignment.getOsceDay()
					.getOsce().getName());
			endBellAssignmentType.setOsceTime(getNewDate(
					lastAssignment.getTimeEnd(), time, 0, isPlusTime));
			endBellAssignmentType.setBellTone(BellTone.TONE_4);				
			bellAssignmentTypes.add(endBellAssignmentType);
			}*/
		}	
		return bellAssignmentTypes;

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
}
