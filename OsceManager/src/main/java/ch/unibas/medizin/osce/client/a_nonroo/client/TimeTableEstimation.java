package ch.unibas.medizin.osce.client.a_nonroo.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.unibas.medizin.osce.client.managed.request.OsceProxy;

public class TimeTableEstimation {
	
	// insert a long in the middle of a rotation if time of rotation (breaks excluded) exceeds this threshold
	private static int LONG_BREAK_MIDDLE_THRESHOLD = 150;
	private static int LUNCH_BREAK_MIDDLE_THRESHOLD = 360;
	private static int NUMBER_OF_LOGICAL_BREAK = 1;
	
	private OsceProxy osce;
	private int numberMinsDayMax;			// needed for optimization purposes (will be overwritten with "osceDay.getTimeEnd() - osceDay.getTimeStart()")
	private int numberSlotsUntilSPChange;	// maximum number of slots that can be placed without a short break in between (defined by "slotsUntilChange" of most difficult role)
	private int numberDays;					// number of required days
	private int numberStudents;				// number of students to base schedule on
	private int numberPosts;				// number of posts defined by the OSCE
	private int numberBreakPosts;			// number of break posts
	private int postLength;					// length of a single post (in minutes)
	private int rotationsPerDay;			// max number of rotations per day
	private int numberParcours;				// number of parallel running parcours
	private List<Integer>[] rotations;		// information on break posts on individual parcours
	private List<Integer> rotationsByDay;	// number of rotations for each day
	private List<Date> lunchBreakByDay;		// lunch-break needs to be stored for each osce-day in order to display it in the CircuitView
	private int timeNeeded;					// total time required to perform the OSCE (without breaks)
	private List<Integer> timeNeededByDay;	// time need per individual day (required to calculate exact end-time of each day)
	
	public List<Integer> breakByRoatation = new ArrayList<Integer>();
	public List<Boolean> lunchBreakRequiredByDay;// = new ArrayList<Boolean>();
	public List<String> breakPerRotationByDay;// = new ArrayList<String>();
	private int noOfOscePost;
	private Date startTime;
	private Date endTime;
	
	/**
	 * Calculate an optimal timetable with respect to the given parameters by trying and comparing
	 * different numbers of courses and break-posts
	 * 
	 * @param osce
	 * @return optimal timetable
	 */
	public static TimeTableEstimation getOptimalSolution(OsceProxy osce, int noOfOscePost, Date startTime, Date endTime) {
		
		if (osce.getLongBreakRequiredTime() != null)
			LONG_BREAK_MIDDLE_THRESHOLD = osce.getLongBreakRequiredTime();
		
		if (osce.getLunchBreakRequiredTime() != null)
			LUNCH_BREAK_MIDDLE_THRESHOLD =  osce.getLunchBreakRequiredTime().intValue();
		
		
		// max number of courses (decrease while looking for optimum)
		int numberParcoursMax = (osce.getNumberCourses() > 0 && osce.getNumberCourses() < osce.getNumberRooms() / noOfOscePost ? osce.getNumberCourses() : osce.getNumberRooms() / noOfOscePost);
		
		TimeTableEstimation ttGen;
		TimeTableEstimation optGen = null;
		double optTimeNeeded = Double.POSITIVE_INFINITY;
		
		for(int nParcours = numberParcoursMax; nParcours >= 1; nParcours--) {
			
			// iterate until (numberPosts - number of posts with post_type=BREAK).
			// NOTE: a number of breaks equal to numberPosts would result in an additional rotation.
			for(int breakPosts = 0; breakPosts <= 0; breakPosts++) {
				ttGen = new TimeTableEstimation(osce, breakPosts, nParcours, noOfOscePost, startTime, endTime);
				ttGen.calcAddBreakPosts();
				ttGen.calcTimeNeeded();
				
				// replace optimal timetable with current timetable if overall time is shorter
				if(ttGen.getTimeNeeded() < optTimeNeeded) {
					optGen = ttGen;
					optTimeNeeded = optGen.getTimeNeeded();
				}
			}
		}
		
		//System.out.println(optGen.toString());
		optGen.shiftLogicalBreak();
		//System.out.println(optGen.toString());
		
		return optGen;
	}
	
	private void shiftLogicalBreak() {
		List<Integer> breakList;
		int lastBreakRotation = -1;		
		int startRot = 0;
		int endRot = 0; 
		
		for (int i=0; i<numberDays; i++)
		{
			String breakStr = breakPerRotationByDay.get(i);
			String[] rotationStr = breakStr.split("-");
			endRot = rotationStr.length;
			
			for (int j=startRot; j<endRot; j++)
			{
				breakList = new ArrayList<Integer>();
				for (int k=0; k<osce.getNumberCourses(); k++)
				 {
					 List<Integer> list = rotations[k];
					 breakList.add(list.get(j));
				 }
				 
				 int count = Collections.frequency(breakList, NUMBER_OF_LOGICAL_BREAK);
				
				 if (count > 0 && count < rotations.length)
				 {
					 lastBreakRotation = j;
					 break;
				 }
			}
			if (lastBreakRotation >= 0)
			 {
				 for (int j=0; j<osce.getNumberCourses(); j++)
				 {
					 int value = rotations[j].get(lastBreakRotation);
					 rotations[j].set(lastBreakRotation, 0);
					 rotations[j].set(endRot-1, value);
				 }
			 }
			
			startRot = endRot;
			lastBreakRotation = -1;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public TimeTableEstimation(OsceProxy osce, int nBreakPosts, int nParcours, int noOfOscePost, Date startTime, Date endTime) {
		
		rotationsByDay = new ArrayList<Integer>();
		timeNeededByDay = new ArrayList<Integer>();
		
		lunchBreakRequiredByDay = new ArrayList<Boolean>();
		lunchBreakByDay = new ArrayList<Date>();
		breakPerRotationByDay = new ArrayList<String>();
				
		this.osce = osce;
		numberStudents = osce.getMaxNumberStudents();
		numberPosts = noOfOscePost;
		postLength = osce.getPostLength();	
		numberSlotsUntilSPChange = 999;
		numberBreakPosts = nBreakPosts;
		numberParcours = nParcours;

		numberMinsDayMax = (int) (endTime.getTime() - startTime.getTime()) / (60 * 1000);
		rotationsPerDay = 1;
		
		rotations = new ArrayList[nParcours];
		for(int i = 0; i < nParcours; i++) {
			rotations[i] = new ArrayList<Integer>();
		}
		
		this.noOfOscePost = noOfOscePost;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	private void calcAddBreakPosts() {
		int nPosts = numberPosts + numberBreakPosts;
		int maxRotations = numberStudents / (nPosts * numberParcours);
		
		rotationsPerDay = (numberMinsDayMax ) / (numberPosts * postLength + (numberPosts - 1) * osce.getShortBreak());
		for(int i = 0; i < numberParcours; i++) {
			for(int j = 0; j < maxRotations; j++) {
				rotations[i].add(numberBreakPosts);
			}
		}
		
		int diff = numberStudents - maxRotations * (nPosts * numberParcours);
		
		if(diff == 0) {
			return;
		}
		int numberPostsNew = numberBreakPosts + 1;
		
		int parcourIndex = 0;
		int rotationIndex = 0;
		for(int i = 0; i < diff; i++) {
			
			if (rotations[parcourIndex].size() > (rotationIndex + 1))
				rotations[parcourIndex].set(rotationIndex, numberPostsNew);
			else
				rotations[parcourIndex].add(rotationIndex, numberPostsNew);
			
			if(parcourIndex >= rotations.length - 1) {
				parcourIndex = 0;

				if(rotationIndex >= maxRotations - 1) {
					rotationIndex = 0;
				} else {
					rotationIndex++;
				}
			} else {
				parcourIndex++;
			}
		}
	}
	
	
	
	public void calcTimeNeeded() {
		numberDays = (int) Math.ceil((double) rotations[0].size() / (double) rotationsPerDay);
		
		int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;		
		
		rotationsByDay.clear();
		timeNeededByDay.clear();
		timeNeeded = 0;
		int i = 0;
		int estimatedRotationPerDay = rotationsMax;
		int rotationRemainsLastDay = rotations[0].size();
		int totalProcessRotation = 0;
		
		while (i < numberDays && totalProcessRotation != rotations[0].size())
		{	
			rotationsByDay.add(i, estimatedRotationPerDay);
			
			calcDayTimeByDayIndex(i, 0);
			
			if(numberMinsDayMax < timeNeededByDay.get(i)) {
				estimatedRotationPerDay--;
			}
			else
			{
				totalProcessRotation += rotationsByDay.get(i);
				rotationRemainsLastDay-=rotationsByDay.get(i);
				
				if (rotationsMax > rotationRemainsLastDay)
					estimatedRotationPerDay = rotationRemainsLastDay;
				else
					estimatedRotationPerDay = rotationsMax;
				
				if (i == (numberDays-1) && rotationRemainsLastDay > 0)
					numberDays++;
				i++;
			}
						
		}
		
		if (numberDays == 1)
			calcDayTimeByDayIndexForSequence(0);
	}

	private void calcDayTimeByDayIndex(int i, int lunchBreakAfterRotation) {
		int rotationsMax;
		String rotationStr = "";
		
		rotationsMax = rotationsByDay.get(i);
		
		
		int slotsSinceLastSimpatChange = 0;
		int timeNeededCurrentDay = 0;
		
		int timeUntilLongORLunchBreak = 0;
		int timeUntilLastLongBreak = 0;
				
		int estimatedTimeDay = 0;
		for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
			int numberBreakPostsThisRotation = 0;
			
			if (rotations[0].size() > j)
				numberBreakPostsThisRotation = rotations[0].get(j);
			else 
				rotations[0].add(j, 0);
			
			int nPostsGeneral = numberPosts + numberBreakPosts;
			int nPostsThisRotation = nPostsGeneral + numberBreakPostsThisRotation;
			
			estimatedTimeDay += (((nPostsThisRotation * osce.getPostLength()) + ((nPostsThisRotation -1) * osce.getShortBreak())));			
		}
		
		estimatedTimeDay += ((rotationsMax -1) * osce.getMiddleBreak());
		
		int numberLongBreak = estimatedTimeDay / LONG_BREAK_MIDDLE_THRESHOLD;
		
		estimatedTimeDay += (numberLongBreak * osce.getLongBreak());  
		
		boolean lunchBreakRequered = false;
			
		lunchBreakRequered = estimatedTimeDay > LUNCH_BREAK_MIDDLE_THRESHOLD;
		
		Integer lunchBreakRotation = 0;
		
		if (lunchBreakRequered)
			lunchBreakRotation = (int) Math.floor((double)rotationsByDay.get(i) / (double)2);
		
		
		int ctr = 0;
		
		// rotations
		for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
			int numberBreakPostsThisRotation = rotations[0].get(j);
			
			// add break posts to regular posts
			int nPostsGeneral = numberPosts + numberBreakPosts;
			int nPostsThisRotation = nPostsGeneral + numberBreakPostsThisRotation;
			
			// index where a SP needs to be changed during the rotation (in the middle if there is
			// only one change, after number of slots of most complicated role otherwise)
			int changeIndex = nPostsThisRotation / numberSlotsUntilSPChange > 1 ? numberSlotsUntilSPChange : nPostsThisRotation / 2 + 1;
			boolean longBreakInRotationHalf = ((nPostsThisRotation * osce.getPostLength()) + ((nPostsThisRotation -1) * osce.getShortBreak())) > LONG_BREAK_MIDDLE_THRESHOLD;

			// posts
			for(int k = 0; k < nPostsThisRotation; k++) {
				boolean halfTimeSlots = k == nPostsThisRotation / 2 - 1;
				
				timeNeededCurrentDay += postLength;
				
				if(longBreakInRotationHalf && halfTimeSlots) {
					slotsSinceLastSimpatChange = 0;
					timeNeededCurrentDay += osce.getLongBreak();
					timeUntilLongORLunchBreak=0;
				} else {
					if(simpatChangeWithinSlots(slotsSinceLastSimpatChange) && k % changeIndex == changeIndex - 1) {
						slotsSinceLastSimpatChange = 0;
						timeNeededCurrentDay += osce.getShortBreakSimpatChange();
					} else {
						if(k < nPostsThisRotation - 1)
							timeNeededCurrentDay += osce.getShortBreak();
					}
				}
				
				slotsSinceLastSimpatChange++;
			}
			
			boolean lastRotation = (j % rotationsMax) == rotationsByDay.get(i) - 1;
			boolean isLongBreakBetweenTwoRotation = false;
			timeUntilLongORLunchBreak = timeNeededCurrentDay - timeUntilLastLongBreak;
			
			if(!lastRotation)
			{
				int nextNumberSlotsTotal = nPostsGeneral + rotations[0].get(j+1);
				int totalTimeForLongBreak = ((nextNumberSlotsTotal * osce.getPostLength()) +  ((nextNumberSlotsTotal - 1) * osce.getShortBreak()) + osce.getMiddleBreak());
				totalTimeForLongBreak += timeUntilLongORLunchBreak;
				isLongBreakBetweenTwoRotation = totalTimeForLongBreak > LONG_BREAK_MIDDLE_THRESHOLD;
				
				if (isLongBreakBetweenTwoRotation)
					timeUntilLastLongBreak = totalTimeForLongBreak;
			}
			
			// additional breaks
			if(!lastRotation) {				
				if(lunchBreakRequered && j == ( ( lunchBreakRotation + (i * rotationsMax) ) ) - 1) {
					timeUntilLongORLunchBreak=0;
					lunchBreakByDay.add(i, dateAddMin(startTime, timeNeededCurrentDay));
					slotsSinceLastSimpatChange = 0;
					timeNeededCurrentDay += osce.getLunchBreak();
					timeUntilLastLongBreak = timeNeededCurrentDay; //Reset time when lunch break added
					breakByRoatation.add(j, (int) osce.getLunchBreak());
				} 
				else 
				{
					if(isLongBreakBetweenTwoRotation)
					{
						timeUntilLongORLunchBreak=0;
						slotsSinceLastSimpatChange = 0;
						timeNeededCurrentDay += osce.getLongBreak();
						timeUntilLastLongBreak = timeNeededCurrentDay; //Reset time when long break added 
						breakByRoatation.add(j, (int) osce.getLongBreak());
					
					} 
					else
					{
						timeNeededCurrentDay += osce.getMiddleBreak();
						breakByRoatation.add(j, (int) osce.getMiddleBreak());
					
					}
				}	
			}
			else
			{
				breakByRoatation.add(j, 0);
			}
			
			
			rotationStr = rotationStr + ctr + ":" + breakByRoatation.get(j) + "-";
			ctr++;
		}
	
		timeNeededByDay.add(i, timeNeededCurrentDay);
		breakPerRotationByDay.add(i, rotationStr);
		
		lunchBreakRequiredByDay.add(i, lunchBreakRequered);
		
		if (!lunchBreakRequered)
			lunchBreakByDay.add(i, startTime);
		
		timeNeeded += timeNeededCurrentDay;
	}
	
	public String checkLongBreakInRotation(Map<Integer, Integer> map, List<Integer> longBreakList, Long totalRotation, Long lunchBreakRotation)
	{
		if (lunchBreakRotation != 0)
		{
			int totalLongBreak = longBreakList.size();
			Long firstLongBreak = 0l, secondLongBreak = 0l;
			
			if(totalLongBreak == 2)
			{
				firstLongBreak = (long) Math.round(lunchBreakRotation.doubleValue() / 2.0);
				secondLongBreak = (long) ((Math.ceil(((totalRotation - lunchBreakRotation) / 2))) + lunchBreakRotation);
				
				if ((firstLongBreak.intValue() - 1) != longBreakList.get(0))
				{	
					map.put((firstLongBreak.intValue() - 1), osce.getLongBreak().intValue());
					map.put(longBreakList.get(0), osce.getMiddleBreak().intValue());
				}
				
				if ((secondLongBreak.intValue() - 1) != longBreakList.get(1))
				{	
					map.put((secondLongBreak.intValue() - 1), osce.getLongBreak().intValue());
					map.put(longBreakList.get(1), osce.getMiddleBreak().intValue());
				}
			}
		}
		else if (lunchBreakRotation == 0)
		{
			int totalLongBreak = longBreakList.size();
			Long firstLongBreak = 0l, secondLongBreak = 0l;
			
			if (totalLongBreak == 1)
			{
				firstLongBreak = (long) Math.floor((totalRotation / 2));
				
				if ((firstLongBreak.intValue() - 1) != longBreakList.get(0))
				{	
					map.put((firstLongBreak.intValue() - 1), osce.getLongBreak().intValue());
					map.put(longBreakList.get(0), osce.getMiddleBreak().intValue());
				}
			}			
		}
		
		Iterator<Integer> itr = map.keySet().iterator();
		int ctr = 0;
		String rotationStr = "";
		while (itr.hasNext())
		{
			Integer key = itr.next();
			rotationStr = rotationStr + ctr + ":" + map.get(key).toString() + "-";
			ctr++;
		}
		
		return rotationStr;
	}
	
	private void calcDayTimeByDayIndexForSequence(int i) {
		int rotationsMax;
		String rotationStr = "";
		
		rotationsMax = rotationsByDay.get(i);
		
		
		int slotsSinceLastSimpatChange = 0;
		int timeNeededCurrentDay = 0;
		
		int timeUntilLongORLunchBreak = 0;
		int timeUntilLastLongBreak = 0;
		
		
		int estimatedTimeDay = 0;
		Map<Integer, Integer> breakForEveryRotation = new HashMap<Integer, Integer>();
		List<Integer> longBreakAfterRotationList = new ArrayList<Integer>();
		
		for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
			int numberBreakPostsThisRotation = 0;
			
			if (rotations[0].size() > j)
				numberBreakPostsThisRotation = rotations[0].get(j);
			else 
				rotations[0].add(j, 0);
			
			// add break posts to regular posts
			int nPostsGeneral = numberPosts + numberBreakPosts;
			int nPostsThisRotation = nPostsGeneral + numberBreakPostsThisRotation;
			
			estimatedTimeDay += (((nPostsThisRotation * osce.getPostLength()) + ((nPostsThisRotation -1) * osce.getShortBreak())));			
		}
		
		estimatedTimeDay += ((rotationsMax -1) * osce.getMiddleBreak());
		
		int numberLongBreak = estimatedTimeDay / LONG_BREAK_MIDDLE_THRESHOLD;
		
		estimatedTimeDay += (numberLongBreak * osce.getLongBreak());  
		
		boolean lunchBreakRequered = true;
		
		Integer lunchBreakRotation = 0;
		
		if (lunchBreakRequered)
			lunchBreakRotation = (int) Math.floor((double)rotationsByDay.get(i) / (double)2);
		
		
		Integer totalRotation = rotationsByDay.get(i);
		int longBreakRotation = (totalRotation - lunchBreakRotation) / 2;
		
		// rotations
		for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
			int numberBreakPostsThisRotation = rotations[0].get(j);
			
			// add break posts to regular posts
			int nPostsGeneral = numberPosts + numberBreakPosts;
			int nPostsThisRotation = nPostsGeneral + numberBreakPostsThisRotation;
			
			// index where a SP needs to be changed during the rotation (in the middle if there is
			// only one change, after number of slots of most complicated role otherwise)
			int changeIndex = nPostsThisRotation / numberSlotsUntilSPChange > 1 ? numberSlotsUntilSPChange : nPostsThisRotation / 2 + 1;
			

			boolean longBreakInRotationHalf = ((nPostsThisRotation * osce.getPostLength()) + ((nPostsThisRotation -1) * osce.getShortBreak())) > LONG_BREAK_MIDDLE_THRESHOLD;
	
			// posts
			for(int k = 0; k < nPostsThisRotation; k++) {
				boolean halfTimeSlots = k == nPostsThisRotation / 2 - 1;
				
				timeNeededCurrentDay += postLength;
				
				if(longBreakInRotationHalf && halfTimeSlots) {
					slotsSinceLastSimpatChange = 0;
					timeNeededCurrentDay += osce.getLongBreak();
					timeUntilLongORLunchBreak=0;
				} else {
					if(simpatChangeWithinSlots(slotsSinceLastSimpatChange) && k % changeIndex == changeIndex - 1) {
						slotsSinceLastSimpatChange = 0;
						timeNeededCurrentDay += osce.getShortBreakSimpatChange();
					} else {
						if(k < nPostsThisRotation - 1)
							timeNeededCurrentDay += osce.getShortBreak();
					}
				}
				
				slotsSinceLastSimpatChange++;
			}
				
			boolean lastRotation = (j % rotationsMax) == rotationsByDay.get(i) - 1;
			boolean isLongBreakBetweenTwoRotation = false;
			timeUntilLongORLunchBreak += timeNeededCurrentDay;
			timeUntilLongORLunchBreak -= timeUntilLastLongBreak;
			if(!lastRotation)
			{
				int nextNumberSlotsTotal = nPostsGeneral + rotations[0].get(j+1);
				int totalTimeForLongBreak = ((nextNumberSlotsTotal * osce.getPostLength()) +  ((nextNumberSlotsTotal - 1) * osce.getShortBreak()) + osce.getMiddleBreak());
				totalTimeForLongBreak += timeUntilLongORLunchBreak;
				if (longBreakRotation == j)
				{
					timeUntilLastLongBreak = totalTimeForLongBreak;
					isLongBreakBetweenTwoRotation = true;
					longBreakRotation += lunchBreakRotation;
				}
			}
			
			// additional breaks
			if(!lastRotation) {				
				if(lunchBreakRequered && j == ( ( lunchBreakRotation + (i * rotationsMax) ) ) - 1) {
					
					timeUntilLongORLunchBreak=0;
					lunchBreakByDay.add(i, dateAddMin(startTime, timeNeededCurrentDay));
					slotsSinceLastSimpatChange = 0;
					timeNeededCurrentDay += osce.getLunchBreak();
					timeUntilLastLongBreak = timeNeededCurrentDay; //Reset time when lunch break added
					breakByRoatation.add(j, (int) osce.getLunchBreak());
				
				} else 
					{
						if(isLongBreakBetweenTwoRotation) {
							timeUntilLongORLunchBreak=0;
						slotsSinceLastSimpatChange = 0;
							timeNeededCurrentDay += osce.getLongBreak();
							timeUntilLastLongBreak = timeNeededCurrentDay; //Reset time when long break added 
							longBreakAfterRotationList.add(j);
							breakByRoatation.add(j, (int) osce.getLongBreak());
						} else {
							timeNeededCurrentDay += osce.getMiddleBreak();
							breakByRoatation.add(j, (int) osce.getMiddleBreak());
						}
					}
					
			}
			else
			{
				breakByRoatation.add(j, 0);
			}
			
			breakForEveryRotation.put(j, breakByRoatation.get(j));
			
		}
	
		rotationStr = checkLongBreakInRotation(breakForEveryRotation, longBreakAfterRotationList, totalRotation.longValue(), lunchBreakRotation.longValue());
	
		timeNeededByDay.add(i, timeNeededCurrentDay);
		breakPerRotationByDay.add(i, rotationStr);
		
		lunchBreakRequiredByDay.add(i, lunchBreakRequered);
		
		if (!lunchBreakRequered)
			lunchBreakByDay.add(i, startTime);
		
		timeNeeded += timeNeededCurrentDay;
	}
	
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("======================================\n");
		sb.append("OSCE -");
		sb.append(" students: " + numberStudents);
		sb.append(" posts: " + numberPosts);
		sb.append(" post length (min): " + postLength + "\n");
		sb.append("max number of minutes per day: " + numberMinsDayMax + "\n");
		sb.append("number of required days: " + numberDays + "\n");
		sb.append("max number of rotations per parcour (per day): " + rotationsPerDay + "\n");
		sb.append("--------------------------------------\n");

		sb.append("number of parcours: " + numberParcours + "\n");
		
		for (int i=0; i<numberDays; i++)
		{
			sb.append("BREAK STR : " + breakPerRotationByDay.get(i) + "\n");
		}
		
		int slotsTotal = 0;
		for(int i = 0; i < rotations.length; i++) {
			ArrayList<Integer> rotationX = (ArrayList<Integer>) rotations[i];
			
			sb.append("    number of rotations for parcour " + (i + 1) + ": " + rotationX.size() + "\n");
			
			if(rotationX.size() > 0) {
				int courseSlotsTotal = 0;
				Iterator<Integer> it = rotationX.iterator();
				sb.append("        ");
				while(it.hasNext()) {
					int slots = it.next();
					sb.append(slots + " ");
					courseSlotsTotal += numberPosts + slots;
				}
				sb.append("\n        total number of slots in parcour: " + courseSlotsTotal +  "\n");
				
				slotsTotal += courseSlotsTotal;
			}
		}
		
		sb.append("total slots: " + slotsTotal + "\n");
		sb.append("timeNeeded: " + timeNeeded + "\n");
		
		sb.append("======================================\n\n");
		
		return sb.toString();
	}
	
	private Date dateAddMin(Date date, long minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}
	
	private boolean simpatChangeWithinSlots(int numberSlots) {
		return (numberSlotsUntilSPChange > 0 && numberSlots > numberSlotsUntilSPChange);
	}
	
	private double getTimeNeeded() {
		return timeNeeded;
	}
	
	public int getNumberDays() {
		return numberDays;
	}
	
	public List<Integer>[] getRotations() {
		return rotations;
	}
	
	public List<String> getBreakPerRotationByDay() {
		return breakPerRotationByDay;
	}
	
}
