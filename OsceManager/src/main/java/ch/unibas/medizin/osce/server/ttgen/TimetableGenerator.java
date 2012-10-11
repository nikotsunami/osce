package ch.unibas.medizin.osce.server.ttgen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.*;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.ColorPicker;
import ch.unibas.medizin.osce.shared.OsceSequences;
import ch.unibas.medizin.osce.shared.OsceStatus;
import ch.unibas.medizin.osce.shared.PostType;

/**
 * Generates an optimal timetable for an OSCE with respect to minimizing the time required.
 * According to the number of rooms and posts, the most reasonable number of parcours and break
 * posts within a rotation are chosen. As a consequence, the TimetableGenerator provides a
 * correct number of student time slots and therefore makes sure all students can be examined.
 * 
 * @author dk
 *
 */
public class TimetableGenerator {
	
	static Logger log = Logger.getLogger(TimetableGenerator.class);
	
	// insert a long in the middle of a rotation if time of rotation (breaks excluded) exceeds this threshold
	private static final int LONG_BREAK_MIDDLE_THRESHOLD = 200;
	
	private Osce osce;
	private OsceDay osceDayRef;				// reference to first OSCE day (will not be removed if scaffold is re-created)
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
	private Set<Assignment> assignments;	// this set contains all student assignments (after createAssignments() is invoked)
	private long[] simAssLastId;			// SPs are not created and finalized in one step - therefore it is necessary to keep a record of the SP that still needs to be finalized
	
	/**
	 * Calculate an optimal timetable with respect to the given parameters by trying and comparing
	 * different numbers of courses and break-posts
	 * 
	 * @param osce
	 * @return optimal timetable
	 */
	public static TimetableGenerator getOptimalSolution(Osce osce) {
		log.info("calculating optimal solution for osce " + osce.getId());
		
		try {
			OsceVerifier.verifyOsce(osce);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// max number of courses (decrease while looking for optimum)
		int numberParcoursMax = (osce.getNumberCourses() > 0 && osce.getNumberCourses() < osce.getNumberRooms() / osce.numberPostsWithRooms() ? osce.getNumberCourses() : osce.getNumberRooms() / osce.numberPostsWithRooms());
		
		TimetableGenerator ttGen;
		TimetableGenerator optGen = null;
		double optTimeNeeded = Double.POSITIVE_INFINITY;
		
		for(int nParcours = numberParcoursMax; nParcours >= 1; nParcours--) {
			
			// iterate until (numberPosts - number of posts with post_type=BREAK).
			// NOTE: a number of breaks equal to numberPosts would result in an additional rotation.
			for(int breakPosts = 0; breakPosts <= 0; breakPosts++) {
				ttGen = new TimetableGenerator(osce, breakPosts, nParcours);
				ttGen.calcAddBreakPosts();
				ttGen.calcTimeNeeded();
				
				// replace optimal timetable with current timetable if overall time is shorter
				if(ttGen.getTimeNeeded() < optTimeNeeded) {
					optGen = ttGen;
					optTimeNeeded = optGen.getTimeNeeded();
				}
			}
		}
		
		log.info("optimal timetable");
		log.info("===============================");
//		optGen.calcTimeNeeded();
		log.info(optGen.toString());
		
		return optGen;
	}
	
	@SuppressWarnings("unchecked")
	public TimetableGenerator(Osce osce, int nBreakPosts, int nParcours) {
		
		rotationsByDay = new ArrayList<Integer>();
		timeNeededByDay = new ArrayList<Integer>();
		lunchBreakByDay = new ArrayList<Date>();
		
		this.osce = osce;
		numberStudents = osce.getMaxNumberStudents();
		numberPosts = osce.getOscePostBlueprints().size();
		postLength = osce.getPostLength();	
		numberSlotsUntilSPChange = osce.slotsOfMostDifficultRole();
		numberBreakPosts = nBreakPosts;
		numberParcours = nParcours;

		osceDayRef = osce.getOsce_days().iterator().next();
		numberMinsDayMax = (int) (osceDayRef.getTimeEnd().getTime() - osceDayRef.getTimeStart().getTime()) / (60 * 1000);
		rotationsPerDay = 1;
		
		rotations = new ArrayList[nParcours];
		for(int i = 0; i < nParcours; i++) {
			rotations[i] = new ArrayList<Integer>();
		}
	}
	
	/**
	 * Calculate break posts for each rotation at each parcour.
	 */
	private void calcAddBreakPosts() {
		// add break posts to regular posts
		int nPosts = numberPosts + numberBreakPosts;
		
		int maxRotations = numberStudents / (nPosts * numberParcours);
		
		rotationsPerDay = (numberMinsDayMax - osce.getLunchBreak()) / (numberPosts * postLength + (numberPosts - 1) * osce.getShortBreak());
		
		// init all rotations of parcours with number of break posts
		for(int i = 0; i < numberParcours; i++) {
			for(int j = 0; j < maxRotations; j++) {
				rotations[i].add(numberBreakPosts);
			}
		}
		
		// remaining number of students that have no slot yet (slots are created by adding break posts)
		int diff = numberStudents - maxRotations * (nPosts * numberParcours);
		
		// each student has a slot - we are happy
		if(diff == 0) {
			return;
		}
		
		// add additional break post to individual rotations
		int numberPostsNew = numberBreakPosts + 1;
		
		/**
		 * add additional break posts according to the following rules:
		 * 1. add break post to first rotation of each parcour
		 * 2. add break post to second rotation of each parcour
		 * ...
		 * until all additional slots have been created
		 */
		int parcourIndex = 0;
		int rotationIndex = 0;
		for(int i = 0; i < diff; i++) {
			rotations[parcourIndex].set(rotationIndex, numberPostsNew);

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
	
	/**
	 * Calculate the overall time needed for performing the whole OSCE
	 */
	public void calcTimeNeeded() {
		boolean numberDaysVerified = false;
		
		while(numberDaysVerified == false) {
			
			numberDays = (int) Math.ceil((double) rotations[0].size() / (double) rotationsPerDay);
			log.info("calculating time needed (" + numberDays + " day(s)");
			
			int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
			
			rotationsByDay.clear();
			timeNeededByDay.clear();
			timeNeeded = 0;
			
			for(int i = 0; i < numberDays; i++) {
//				// only handle status BLUEPRINT and GENERATED (as structural changes are not possible afterwards)
//				switch(osce.getOsceStatus()) {
//					case OSCE_BLUEPRINT:
//						if(i == numberDays - 1 && numberDays > 1)
//							rotationsByDay.add(i, (rotations[0].size() % rotationsPerDay > 0 ? rotations[0].size() % rotationsPerDay : rotationsPerDay));
//						else
//							rotationsByDay.add(i, rotationsMax);
//						break;
//					case OSCE_GENRATED:
//						rotationsByDay.add(i, osce.getOsce_days().get(i).totalNumberRotations());
//						break;
//				}
				if(i == numberDays - 1 && numberDays > 1)
					rotationsByDay.add(i, (rotations[0].size() % rotationsPerDay > 0 ? rotations[0].size() % rotationsPerDay : rotationsPerDay));
				else
					rotationsByDay.add(i, rotationsMax);
			}
			
			// days
			for(int i = 0; i < numberDays; i++) {
				// TODO: fix issue that lunch break is reset to half of rotations (= 0)
				calcDayTimeByDayIndex(i, 0);
			}
			
			// see whether calculated time fits into the time available of first day - do another round if not
			int numberDays2 = (int) Math.ceil((double) timeNeeded / (double) numberMinsDayMax);
			if(numberMinsDayMax < timeNeededByDay.get(0)) {
				//numberDays = numberDays2;
				rotationsPerDay--;
				log.info("do another round... | numberDays = " + numberDays + ", numberDays2 = " + numberDays2 + ", numberMinsDayMax = " + numberMinsDayMax + ", timeNeededByDay.get(0) = " + timeNeededByDay.get(0) + ", timeNeeded = " + timeNeeded);
			} else {
				numberDaysVerified = true;
			}
		}
	}

	/**
	 * Calculate time for an OSCE day with given index.
	 * 
	 * @param i index of the OSCE day
	 * @param lunchBreakAfterRotation rotation after which the lunch break should be placed (0 for half of all rotations)
	 */
	private void calcDayTimeByDayIndex(int i, int lunchBreakAfterRotation) {
		int rotationsMax;
		
		// take information from DB if OSCE scaffold has already been generated
		if(osce.getOsceStatus().equals(OsceStatus.OSCE_BLUEPRINT))
			rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
		else
			rotationsMax = rotationsByDay.get(i);
				
		
		int slotsSinceLastSimpatChange = 0;
		int timeNeededCurrentDay = 0;
		
		log.info("day " + i + " (rotations: " + rotationsByDay.get(i) + ") / rotationsMax: " + rotationsMax);
		
		// rotations
		for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
			int numberBreakPostsThisRotation = rotations[0].get(j);
			
			// add break posts to regular posts
			int nPostsGeneral = numberPosts + numberBreakPosts;
			int nPostsThisRotation = nPostsGeneral + numberBreakPostsThisRotation;
			
			// index where a SP needs to be changed during the rotation (in the middle if there is
			// only one change, after number of slots of most complicated role otherwise)
			int changeIndex = nPostsThisRotation / numberSlotsUntilSPChange > 1 ? numberSlotsUntilSPChange : nPostsThisRotation / 2 + 1;
			
			boolean longBreakInRotationHalf = nPostsThisRotation * osce.getPostLength() > LONG_BREAK_MIDDLE_THRESHOLD;
			
			log.info("  rotation " + j + " (breakposts: " + numberBreakPostsThisRotation + ") - start: " + timeNeededCurrentDay);
			
			// posts
			for(int k = 0; k < nPostsThisRotation; k++) {
				boolean halfTimeSlots = k == nPostsThisRotation / 2 - 1;
				
				timeNeededCurrentDay += postLength;
				
				if(longBreakInRotationHalf && halfTimeSlots) {
					slotsSinceLastSimpatChange = 0;
					timeNeededCurrentDay += osce.getLongBreak();
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
			
			log.info("  rotation " + j + " end: " + timeNeededCurrentDay);
			
			boolean lastRotation = (j % rotationsMax) == rotationsByDay.get(i) - 1;
			
			// additional breaks
			if(!lastRotation) {
				if((lunchBreakAfterRotation > 0 && (j + 1) % rotationsMax == lunchBreakAfterRotation) || (lunchBreakAfterRotation == 0 && lunchBreakNeeded((j + 1) % rotationsMax))) {
					lunchBreakByDay.add(i, dateAddMin(osceDayRef.getTimeStart(), timeNeededCurrentDay));
					slotsSinceLastSimpatChange = 0;
					timeNeededCurrentDay += osce.getLunchBreak();
					log.info("  lunch break");
				} else if(simpatChangeWithinSlots(slotsSinceLastSimpatChange + nPostsGeneral + rotations[0].get(j + 1)) || longBreakInRotationHalf) {
					slotsSinceLastSimpatChange = 0;
					timeNeededCurrentDay += osce.getLongBreak();
					log.info("  long break");
				} else {
					timeNeededCurrentDay += osce.getMiddleBreak();
					log.info("  middle break");
				}
			}
		}
		
		timeNeededByDay.add(i, timeNeededCurrentDay);
		
		timeNeeded += timeNeededCurrentDay;
	}
	
	/**
	 * Replace lunch break for an OSCE day after a specific rotation number
	 * (used to increase/decrease number of rotations per sequence and still guaranteeing
	 * correct calculation of lunch break start time). Basically, the all times
	 * are calculated again but this assures correct results.
	 * 
	 * @param osceDayId OSCE day to shift lunch break
	 * @param lunchBreakAfterRotation rotation after which should the lunch break be placed
	 */
	public void updateLunchBreakAfterRotation(long osceDayId, int lunchBreakAfterRotation) {
		
		OsceDay thisDay = OsceDay.findOsceDay(osceDayId);
		int thisIndex = getOsceDayIndex(thisDay);
		
		calcDayTimeByDayIndex(thisIndex, lunchBreakAfterRotation);
		
		thisDay.setLunchBreakAfterRotation(lunchBreakAfterRotation);
		thisDay.setLunchBreakStart(lunchBreakByDay.get(thisIndex));
		thisDay.setTimeEnd(dateAddMin(thisDay.getTimeStart(), timeNeededByDay.get(thisIndex)));
		thisDay.flush();
	}

	/**
	 * Find OSCE day index in list
	 * @param osceDay
	 * @return
	 */
	private int getOsceDayIndex(OsceDay osceDay) {
		int index = 0;
		Iterator<OsceDay> allDays = osceDay.getOsce().getOsce_days().iterator();
		while (allDays.hasNext()) {
			OsceDay currDay = (OsceDay) allDays.next();
			if(!currDay.equals(osceDay))
				index++;
			else
				return index;
		}
		return -1;
	}
	
	/**
	 * Update lunch-break-(start-) and end-times. Invoked when shifting rotation.
	 * In a rotation shift, always two days are involved, these days should be updated
	 * (re-calculation of times) after shifting rotations.
	 */
	public void updateTimesAfterRotationShift(long osceDayId1, long osceDayId2) {
		rotationsByDay.clear();
		timeNeededByDay.clear();
		timeNeeded = 0;
		
		OsceDay dayFrom = OsceDay.findOsceDay(osceDayId1);
		OsceDay dayTo = OsceDay.findOsceDay(osceDayId2);
		List<Integer> osceDaysToUpdate = new ArrayList<Integer>();
		osceDaysToUpdate.add(getOsceDayIndex(dayFrom));
		osceDaysToUpdate.add(getOsceDayIndex(dayTo));
		
		Iterator<Integer> it = osceDaysToUpdate.iterator();
		while (it.hasNext()) {
			Integer osceDayIndex = (Integer) it.next();
			OsceDay thisDay = osce.getOsce_days().get(osceDayIndex);
			log.warn(osceDayIndex);
			
			rotationsByDay.add(osceDayIndex, thisDay.totalNumberRotations());
			
			int lunchBreakAfterRotation = 0;
			if(thisDay.getLunchBreakAfterRotation() == null)
				lunchBreakAfterRotation = thisDay.getOsceSequences().get(0).getNumberRotation();
			else
				lunchBreakAfterRotation = thisDay.getLunchBreakAfterRotation();
			
			calcDayTimeByDayIndex(osceDayIndex, lunchBreakAfterRotation);
			
			thisDay.setTimeEnd(dateAddMin(thisDay.getTimeStart(), timeNeededByDay.get(osceDayIndex)));
			thisDay.flush();
		}
	}
	
	/**
	 * Print information on timetable setting
	 */
	public String toString() {
		
		OsceDay osceDay = osce.getOsce_days().iterator().next();
		
		StringBuffer sb = new StringBuffer();
		sb.append("======================================\n");
		sb.append("OSCE -");
		sb.append(" students: " + numberStudents);
		sb.append(" posts: " + numberPosts);
		sb.append(" post length (min): " + postLength + "\n");
		sb.append("max number of minutes per day: " + numberMinsDayMax + "\n");
		sb.append("number of required days: " + numberDays + "\n");
		sb.append("max number of rotations per parcour (per day): " + rotationsPerDay + "\n");
		sb.append("start time first day: "+osceDay.getTimeStart()+"\n");
		sb.append("end time first day: "+osceDay.getTimeEnd()+"\n");
		sb.append("--------------------------------------\n");

		sb.append("number of parcours: " + numberParcours + "\n");
		
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
		
		OsceDay day0 = osce.getOsce_days().iterator().next();
		sb.append(" - from: " + day0.getTimeStart() + " to " + dateAddMin(day0.getTimeStart(), timeNeeded) + "\n");
		sb.append("======================================\n\n");
		
		return sb.toString();
	}
	
	/**
	 * Setup OSCE days and corresponding sequences, parcours and posts.
	 * Sequences are created according to the following scheme:
	 * 1 OSCE day: sequence A for the first half of the rotations, sequence B for the second half
	 * > 1 OSCE day: one sequence for each day
	 * 
	 */
	public void createScaffold() {
		log.info("remove old scaffold");
		// remove old scaffold if existing (and re-create first OSCE day
		removeOldScaffold();
		
		log.info("old scaffold removed");
		
		List<OsceDay> days = insertOsceDays();
		List<OsceSequence> osceSequences = new ArrayList<OsceSequence>();
		
		// only one day --> seq A in the morning, seq B in the afternoon
		if(days.size() == 1) {
			OsceDay osceDay = days.iterator().next();
			
			log.info("rotations for day 0:" + rotationsByDay.get(0));
			
			int[] rotSeq = new int[2];
			rotSeq[0] = rotationsByDay.get(0) / 2;
			rotSeq[1] = rotationsByDay.get(0) - rotSeq[0];
			
			// create sequence for each half day
			for(int i = 0; i < 2; i++) {
				// insert sequence
				OsceSequence seq = new OsceSequence();
				//seq.setLabel(OsceSequences.getConstByIndex(i).toString());
				seq.setLabel(OsceSequences.getOsceSequenceValue(OsceSequences.getConstByIndex(i)));
				seq.setNumberRotation(rotSeq[i]);
				seq.setOsceDay(osceDay);
				
				// insert parcours
				List<Course> parcours = insertParcoursForSequence(seq);
				
				// insert posts
				List<OscePost> posts = insertPostsForSequence(seq);
				
				seq.setCourses(parcours);
				seq.setOscePosts(posts);
				
				seq.persist();
				
				// insert osce_post_rooms
				insertOscePostRoomsForParcoursAndPosts(parcours, posts);
				
				osceSequences.add(seq);
			}
			
			osceDay.setOsceSequences(osceSequences);
		} else { // multiple days --> one sequence for each day
			Iterator<OsceDay> it = days.iterator();
			int i = 0,j=0;
			while (it.hasNext()) {
				OsceDay osceDay = (OsceDay) it.next();
				
				// insert sequence
				OsceSequence seq = new OsceSequence();				
				//seq.setLabel(OsceSequences.getConstByIndex(i).toString());
				
				if(j>4)
				{
					j=0;
				}
				
				seq.setLabel(OsceSequences.getOsceSequenceValue(OsceSequences.getConstByIndex(j)));
				seq.setNumberRotation(rotationsByDay.get(i));
				seq.setOsceDay(osceDay);
				
				// insert parcours
				List<Course> parcours = insertParcoursForSequence(seq);
				
				// insert posts
				List<OscePost> posts = insertPostsForSequence(seq);
				
				seq.setCourses(parcours);
				seq.setOscePosts(posts);
				
				seq.persist();
				
				// insert osce_post_rooms
				insertOscePostRoomsForParcoursAndPosts(parcours, posts);
				
				osceSequences.add(seq);
				
				i++;
				j++;
				
				osceDay.setOsceSequences(osceSequences);
			}
		}
	}

	/**
	 * Remove old calculated scaffold (when OsceStatus is changed from GENERATED to BLUEPRINT again)
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void removeOldScaffold() {
		//SPEC[		
		log.info("removeOldScaffold start");
		Iterator<OsceDay> itDay = osce.getOsce_days().iterator();		
		OsceDay firstDay = itDay.next();							
		OsceDay removeDay = null;
		// Array List for Osce Days which are going to remove
        ArrayList<OsceDay> listRemoveOsceDay = new ArrayList<OsceDay>();
        // Array List for Osce Sequences which are going to remove
		ArrayList<OsceSequence> listRemoveOsceSequence = new ArrayList<OsceSequence>();
		log.info("First Day : " + firstDay.getId() + " : " + firstDay.getOsceDate().toLocaleString());
		while(itDay.hasNext()) {			
			OsceDay nextDay = itDay.next();
			log.info("Next Day : " + nextDay.getId() + " : " + nextDay.getOsceDate().toLocaleString());
			if(firstDay.getOsceDate().after(nextDay.getOsceDate())) {
				log.info("First Day after Next Day");
				if(firstDay.getPatientInSemesters() == null || firstDay.getPatientInSemesters().size() == 0) {
					removeDay = firstDay;					
				}
				
				firstDay = nextDay;
			} else {
				log.info("Next Day after First Day");
				if(nextDay.getPatientInSemesters() == null || nextDay.getPatientInSemesters().size() == 0) {
					log.info("Next Day going to be deleted");
					removeDay = nextDay;
					log.info("Next Day is deleted");					
				}
			}
			
			log.info("remove Day going to be deleted");
			/*osce.getOsce_days().remove(removeDay);
			removeDay.remove();			
			log.info("Next Day is deleted");*/
	        listRemoveOsceDay.add(removeDay);
			
		}		
		    // Removing Osce Days from List
            log.info("removing day from osceDay list");
			osce.getOsce_days().remove(listRemoveOsceDay);
			for(OsceDay rmOsceDay: listRemoveOsceDay)
			{
				log.info("remove Day going to be deleted");
				osce.getOsce_days().remove(rmOsceDay);
				rmOsceDay.remove();
				log.info("OSCEday removed");
			}
			
			log.info("For First Day OSCE Sequences");

		if(firstDay != null) {
			log.info("First Day not null : " + firstDay.getId() + " : " + firstDay.getOsceDate().toLocaleString());						
			List<OsceSequence> setOsceSeq = firstDay.getOsceSequences();
			Iterator<OsceSequence> itOsceSeq = setOsceSeq.iterator();
			//firstDay.getOsceSequences().removeAll(setOsceSeq);			
			/*while(itOsceSeq.hasNext()) {				
				OsceSequence osceSequence = itOsceSeq.next();
				log.info("Removing osce sequence : " + osceSequence.getId() );
				firstDay.getOsceSequences().remove(osceSequence);
				osceSequence.remove();
			}*/
			while(itOsceSeq.hasNext()) 
			{				
				OsceSequence osceSequence = itOsceSeq.next();
				log.info("Removing osce sequence : " + osceSequence.getId() );
				/*firstDay.getOsceSequences().remove(osceSequence);
				osceSequence.remove();*/
				listRemoveOsceSequence.add(osceSequence);
			}
                        // Removing Osce Sequence from List
			log.info("removing sequence from osceSequence list");
			osce.getOsce_days().remove(listRemoveOsceSequence);
			for(OsceSequence rmOsceSequence: listRemoveOsceSequence)
			{
				log.info("remove Day going to be deleted");
				firstDay.getOsceSequences().remove(rmOsceSequence);
				rmOsceSequence.remove();
				log.info("OSCEday removed");
			}
			osceDayRef = firstDay;
		}
		
		//SPEC]
	}

	/**
	 * Create all parcours for a sequence (number of parcours was calculated,
	 * number of sequences is given by number of days).
	 * 
	 * @param seq
	 * @return
	 */
	private List<Course> insertParcoursForSequence(OsceSequence seq) {
		List<Course> parcours = new ArrayList<Course>();
		
		for(int j = 0; j < numberParcours; j++) {
			Course c = new Course();
			c.setColor(ColorPicker.getConstByIndex(j).toString());
			c.setOsce(osce);
			c.setOsceSequence(seq);
			parcours.add(c);
		}
		
		return parcours;
	}

	/**
	 * Create all posts for a sequence (transcribe all OscePostBlueprint into OscePost)
	 * 
	 * @param seq
	 * @return
	 */
	private List<OscePost> insertPostsForSequence(OsceSequence seq) {
		List<OscePost> posts = new ArrayList<OscePost>();
		
		OscePost current = null;
		
		Iterator<OscePostBlueprint> itBP = osce.getOscePostBlueprints().iterator();
		while (itBP.hasNext()) {
			OscePostBlueprint oscePostBlueprint = (OscePostBlueprint) itBP.next();
			
			current = new OscePost();
			current.setOscePostBlueprint(oscePostBlueprint);
			current.setOsceSequence(seq);
			current.setSequenceNumber(oscePostBlueprint.getSequenceNumber());
			
			posts.add(current);
		}
		
		return posts;
	}
	
	/**
	 * Insert the number of needed days for this OSCE into the database.
	 * 
	 * @return
	 */
	private List<OsceDay> insertOsceDays() {
		List<OsceDay> days = new ArrayList<OsceDay>();
		
		OsceDay day0 = osce.getOsce_days().iterator().next();
		days.add(day0);
		
		Calendar dayCal = Calendar.getInstance();
		dayCal.setTime(day0.getTimeStart());
		
		int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
		
		rotationsByDay.set(0, rotationsMax);
		
		// number of rotations we still have to assign to further days
		int rotationsLeft = rotations[0].size() - rotationsMax;
		
		if(numberDays > 1 && rotationsLeft > 0) {
			// create new days and corresponding sequences and parcours
			for(int i = 1; i < numberDays; i++) {
				rotationsByDay.set(i, rotationsLeft);
				dayCal.add(Calendar.DATE, 1);
				
				OsceDay day = new OsceDay();
				day.setOsce(osce);
				day.setOsceDate(dayCal.getTime());
				day.setTimeStart(dayCal.getTime());
				day.setTimeEnd(dateAddMin(dayCal.getTime(), timeNeededByDay.get(i)));
				day.setLunchBreakStart(lunchBreakByDay.get(i));
				day.setLunchBreakAfterRotation(rotationsByDay.get(i) / 2);
				
				day.persist();
				days.add(day);
				
				rotationsLeft -= rotationsPerDay;
			}
		}
		
		if(lunchBreakByDay.size() > 0)
			day0.setLunchBreakStart(lunchBreakByDay.get(0));
		
		day0.setTimeEnd(dateAddMin(day0.getTimeStart(), (long) timeNeededByDay.get(0)));
		day0.flush();
		
		return days;
	}
	
	/**
	 * Insert OscePostRoom assignments for given lists of parcours/courses and posts.
	 * Rooms will be added manually and therefore set to NULL
	 * 
	 * @param parcours
	 * @param posts
	 * @return
	 */
	private Set<OscePostRoom> insertOscePostRoomsForParcoursAndPosts(List<Course> parcours, List<OscePost> posts) {
		Set<OscePostRoom> oscePostRooms = new HashSet<OscePostRoom>();
		
		Iterator<Course> itParcour = parcours.iterator();
		while (itParcour.hasNext()) {
			Course parcour = (Course) itParcour.next();
			
			Iterator<OscePost> itPost = posts.iterator();
			while (itPost.hasNext()) {
				OscePost oscePost = (OscePost) itPost.next();
				
				OscePostRoom opr = new OscePostRoom();
				opr.setCourse(parcour);
				opr.setOscePost(oscePost);
				opr.setRoom(null);
				
				opr.persist();
			}
		}
		
		return oscePostRooms;
	}
	
	/**
	 * Create assignments for students, SimPats and examiners.
	 * @return set containing all assignments
	 */
	public Set<Assignment> createAssignments() {
		assignments = new HashSet<Assignment>();
		
		List<OsceDay> days = new ArrayList<OsceDay>(osce.getOsce_days());
		
		// get posts (sorted by sequenceNumber)
		List<OscePostBlueprint> posts = osce.getOscePostBlueprints();
		
		// total number of rotations is split among sequences, rotationOffset will be incremented
		// by the number of rotations of a sequence after handling it.
		// (e.g. and 1 osce-day has 2 sequences with 4 and 3 rotations, rotationOffset will then
		// first be 0 and 4 after first iteration of sequences)	
		int rotationOffset = 0;
		
		int studentIndexLowerBound = 1;
		
		// iterate over all days
		Iterator<OsceDay> itDays = days.iterator();
		while (itDays.hasNext()) {
			OsceDay osceDay = (OsceDay) itDays.next();
								
			log.info("day " + osceDay.getOsceDate() + " started");
			
			Date time = osceDay.getTimeStart();
			
			//Osce Day Start Time
			Date sequenceStartTime = time;
			
			// iterate over sequences ("A", "B", etc.)
			Iterator<OsceSequence> itSeq = osceDay.getOsceSequences().iterator();
			while (itSeq.hasNext()) {
				OsceSequence osceSequence = (OsceSequence) itSeq.next();
				
				log.info("sequence " + osceSequence.getLabel() + " started");
				
				// number of rotations for current sequence (valid for all parcours in this sequence)
				int numberRotations = osceSequence.getNumberRotation();
				
				// Osce Day Start Time in Sequence Iterator
				Date parcourStartTime = sequenceStartTime;
				
				// iterate over all parcours (red, green, blue, etc.)
				Iterator<Course> itParc = osceSequence.getCourses().iterator();
				int parcourIndex = 0;
				while (itParc.hasNext()) {
					Course course = (Course) itParc.next();
					
					log.info("parcour " + course.getColor() + " started");
					
					int postsSinceSimpatChange = 0;
					
					// Osce Day Start Time in Sequence Iterator
					Date rotationStartTime = parcourStartTime;
					
					simAssLastId = new long[numberPosts];
					
					// flag to define whether the assignments of ANAMNESIS_THERAPY have to be switched in the end (to avoid
					// time overlapping)
					boolean earlyStartFirst = false;
					
					// create assignments for rotations (given by sequence)
					for(int currRotationNumber = rotationOffset; currRotationNumber < (rotationOffset + numberRotations); currRotationNumber++) {
						int numberBreakPosts = rotations[parcourIndex].get(currRotationNumber);
						int numberSlotsTotal = posts.size() + numberBreakPosts;
						
						Set<Assignment> assThisRotation = new HashSet<Assignment>();
						
						log.info("rotation " + currRotationNumber + " started - rotation "+(currRotationNumber - rotationOffset + 1)+"/"+numberRotations+" (total rotation: "+currRotationNumber+"/"+(rotationOffset + numberRotations - 1)+"), numberBreakPosts: "+numberBreakPosts+", numberSlotsTotal: "+numberSlotsTotal);
						
						// get max slots from current rotation (defines when next rotation is about to
						// start since rotations need to start at the same time!)
						int maxPostsCurrentRotation = numberPosts + getMaxBreakPostsCurrentRotation(osceSequence.getCourses(), currRotationNumber);
						
						boolean firstRotation = currRotationNumber == rotationOffset;
						boolean halfRotations = currRotationNumber == (rotationOffset + numberRotations) / 2 - 1;
						boolean lastRotation = currRotationNumber == (rotationOffset + numberRotations - 1);
						boolean changeSimpatDuringRotation = simpatChangeWithinSlots(postsSinceSimpatChange + numberSlotsTotal);
						
						// insert long break in the middle of a rotation if the time of all posts exceeds some threshold
						boolean longBreakInRotationHalf = numberSlotsTotal * osce.getPostLength() > LONG_BREAK_MIDDLE_THRESHOLD;
						
						// index where a SP needs to be changed during the rotation (in the middle if there is
						// only one change, after number of slots of most complicated role otherwise)
						int changeIndex = numberSlotsTotal / numberSlotsUntilSPChange > 1 ? numberSlotsUntilSPChange : numberSlotsTotal / 2 + 1;
						
						// calculate slots that this and next rotation have and check whether a SP change after the rotation is necessary
						int numberSlotsThisAndNextRotation = numberSlotsTotal + numberPosts;
						if(currRotationNumber < (rotationOffset + numberRotations) - 1) {
							numberSlotsThisAndNextRotation += rotations[parcourIndex].get(currRotationNumber + 1);
						}
						boolean changeSimpatAfterRotation = simpatChangeWithinSlots(postsSinceSimpatChange + numberSlotsThisAndNextRotation);
						
						Date nextRotationStartTime = null;
						
						// iterate through all posts as many times as there are posts
						// (in the end, we want to have a n*n matrix where n denotes the number of posts in this OSCE)
						for(int i = 0; i < numberSlotsTotal; i++) {
							OscePostBlueprint postBP = null;
							PostType postType = null;
							OscePost post = null;
							OscePostRoom oscePR = null;
							
							log.info("post " + i + " started");
							
							if(i < posts.size()) {
								postBP = posts.get(i);
								postType = postBP.getPostType();
								post = OscePost.findOscePostsByOscePostBlueprintAndOsceSequence(postBP, osceSequence).getSingleResult();
								
								if(!postType.equals(PostType.BREAK))
									oscePR = OscePostRoom.firstOscePostRoomByCourseAndOscePost(course, post, (i + 1));
							}
							
							// reset to the point in time where the rotation starts (necessary since
							// we increase the time while going through all posts of a rotation)
							time = rotationStartTime;
							
							// post must be a possible start (PAUSE - which is at the end is always a possible start)
							// fill slots for one student in current rotation
							int studentIndexOffset = (i + 1);
							
							for(int j = 0; j < numberSlotsTotal; j++) {
								
								boolean firstTimeSlot = j == 0;
								boolean halfTimeSlots = j == numberSlotsTotal / 2 - 1;
								boolean lastTimeSlot = j == numberSlotsTotal - 1;
								
								// calculate student index for current time slot j in post i
								// for a circuit of 4 posts and one break post, the indexes should be as follows:
								// Posts:		1 2 3 4 break
								// Slot 1:		1 2 3 4 5
								// Slot 2:		5 1 2 3 4
								// Slot 3:		4 5 1 2 3
								// Slot 4:		3 4 5 1 2
								// Slot 5:		2 3 4 5 1
								// NOTE: student indices for first part of PREPARATION are the same as for second part, but with early start
								int studentIndex = studentIndexLowerBound + (numberSlotsTotal + studentIndexOffset - (j + 1)) % numberSlotsTotal;
								if(postBP != null) {
									if(postType.equals(PostType.PREPARATION) && postBP.isFirstPart()) {
										studentIndex = studentIndexLowerBound + (numberSlotsTotal + studentIndexOffset - j) % numberSlotsTotal;
									} else if(postType.equals(PostType.ANAMNESIS_THERAPY) && earlyStartFirst) {
										if(postBP.isFirstPart()) {
											studentIndex = studentIndexLowerBound + (numberSlotsTotal + studentIndexOffset - j) % numberSlotsTotal;
										} else {
											studentIndex = studentIndexLowerBound + (numberSlotsTotal + studentIndexOffset - (j + 2)) % numberSlotsTotal;
										}
									}
								}
								
								Date startTime = time;
								
								if(postBP != null) {
									// early start
									boolean isAnamnesisTherapy = postType.equals(PostType.ANAMNESIS_THERAPY) &&((!earlyStartFirst && !postBP.isFirstPart()) ||(earlyStartFirst && postBP.isFirstPart()));
									boolean isPreparation = postType.equals(PostType.PREPARATION) && postBP.isFirstPart();
									if(firstTimeSlot && (isAnamnesisTherapy || isPreparation)) {
										startTime = dateSubtractMin(startTime, osce.getPostLength());
										
										if(changeSimpatDuringRotation && (j % changeIndex == changeIndex - 1)) {
											startTime = dateSubtractMin(startTime, osce.getShortBreakSimpatChange());
										} else {
											startTime = dateSubtractMin(startTime, osce.getShortBreak());
										}
									}
								}
								
								Date endTime = dateAddMin(startTime, osce.getPostLength());
								
								if(currRotationNumber < 1)
									log.info("\t student index " + studentIndex + " for post " + i);

								assThisRotation.add(Assignment.createStudentAssignment(osceDay, oscePR, studentIndex, startTime, endTime));
								
								// ANAMNESIS_THERAPY --> double length in same room BUT different role for second part!
								if(postBP != null && postType.equals(PostType.ANAMNESIS_THERAPY)) {
									// WARNING! only short break (no shortBreakSimpatChange) is handled here. This is on
									// purpose, since SP change during a double-post might cause big trouble!
									Date startTime2 = dateAddMin(endTime, osce.getShortBreak());
									Date endTime2 = dateAddMin(startTime2, osce.getPostLength());
									
									OscePostRoom oscePRAlt = OscePostRoom.altOscePostRoom(oscePR);
									
									assThisRotation.add(Assignment.createStudentAssignment(osceDay, oscePRAlt, studentIndex, startTime2, endTime2));
									
									endTime = endTime2;
								}
								
								log.info("added assignment for student " + studentIndex);

								if(postBP != null && postType.equals(PostType.ANAMNESIS_THERAPY) ) {
									if(numberSlotsTotal % 2 == 1) {
										if((!earlyStartFirst && !postBP.isFirstPart()) || (earlyStartFirst && postBP.isFirstPart())) {
											lastTimeSlot = j == numberSlotsTotal - 1;
										} else {
											lastTimeSlot = j == numberSlotsTotal - 3;
										}
									} else {
										lastTimeSlot = j == numberSlotsTotal - 2;
									}
								}
								
								log.info("calculated lastTimeSlot");
								
								// handle SP assignments
								if(post != null && post.getStandardizedRole() != null && post.requiresSimpat()) {
									// create first SP slot
									if(firstRotation && firstTimeSlot) {
										createSPAssignment(i, osceDay, startTime, oscePR);
										log.info("create SP assignment for post " + i + " " + debugTime(startTime));
									}
									// finalize last SP slot
									if(lastRotation && lastTimeSlot) {
										finalizeSPAssignment(i, endTime);
										log.info("finalize SP assignment for post " + i + " " + debugTime(endTime));
									}
								}
								
								log.info("handled SP assignments");
								
								if(postBP != null && postType.equals(PostType.ANAMNESIS_THERAPY)) {
									// skip next time-slot
									j++;
								}
								
								if(!lastTimeSlot) {
									// add short break between posts
									// if, for example, there is a maximum of 8 posts and the most difficult role-topic needs a SimPat change after
									// 5 students, there is only one possible SimPat change during a rotation (best placed in the middle)
									if(changeSimpatDuringRotation && (j % changeIndex == changeIndex - 1)) {
										Date endTimeNew;
										if(halfTimeSlots && longBreakInRotationHalf)
											endTimeNew = dateAddMin(endTime, osce.getLongBreak());
										else
											endTimeNew = dateAddMin(endTime, osce.getShortBreakSimpatChange());
										
										if(post != null && post.getStandardizedRole() != null && post.requiresSimpat()) {
											changeSP(i, osceDay, endTime, endTimeNew, oscePR);
											log.info("change SP assignment for post " + i + " " + debugTime(endTime) + " / " + debugTime(endTimeNew) + " (during rotation)");
										}
										
										endTime = endTimeNew;
									} else {
										if(halfTimeSlots && longBreakInRotationHalf)
											endTime = dateAddMin(endTime, osce.getLongBreak());
										else
											endTime = dateAddMin(endTime, osce.getShortBreak());
									}
								} else {
									if((!lastRotation && changeSimpatAfterRotation) || (osceDay.getOsceSequences().size() == 1 && halfRotations)) {
										
										if(post != null && post.getStandardizedRole() != null && post.requiresSimpat()) {
											Date endTimeOld = endTime;
											
											Date startTimeNew = endTimeOld;
											
											if(osceDay.getOsceSequences().size() == 1 && osceDay.getLunchBreakAfterRotation() != null && osceDay.getLunchBreakAfterRotation() > 0 && osceDay.getLunchBreakAfterRotation() == currRotationNumber)
												startTimeNew = dateAddMin(endTimeOld, osce.getLunchBreak());
											else
												startTimeNew = dateAddMin(endTimeOld, osce.getLongBreak());
											
											// fix new start time for SP (next rotation) when lastTimeSlot and switch of assignments will occur
											if(postBP != null && postType.equals(PostType.ANAMNESIS_THERAPY) && numberSlotsTotal % 2 == 1 &&
													(!earlyStartFirst && postBP.isFirstPart() || earlyStartFirst && !postBP.isFirstPart())) {
												startTimeNew = dateAddMin(startTimeNew, osce.getShortBreakSimpatChange() - osce.getShortBreak());
											}
											
											changeSP(i, osceDay, endTimeOld, startTimeNew, oscePR);
											log.info("change SP assignment for post " + i + " " + debugTime(endTime) + " / " + debugTime(startTimeNew) + " (after rotation)");
										}
									}
								}
								
								time = endTime;
								
								// leave loop after last time slot (otherwise we would have too many time slots for double posts)
								if(lastTimeSlot) {
									break;
								}
							}
							// STUDENTS END
							
							// calculate time when next rotation starts
							nextRotationStartTime = dateAddMin(rotationStartTime, maxPostsCurrentRotation * (osce.getPostLength() + osce.getShortBreak()) - osce.getShortBreak());
							
							// if SP was changed during rotation, add the SP change-break
							if(changeSimpatDuringRotation) {
								int numberBreakDuringRotation = numberSlotsTotal / numberSlotsUntilSPChange;
								if(numberSlotsTotal % numberSlotsUntilSPChange == 0)
									numberBreakDuringRotation -= 1;
								
								log.info("numberBreakDuringRotation: " + numberBreakDuringRotation);
								
								nextRotationStartTime = dateAddMin(nextRotationStartTime, numberBreakDuringRotation * (osce.getShortBreakSimpatChange() - osce.getShortBreak()));
								postsSinceSimpatChange = numberSlotsTotal % numberSlotsUntilSPChange;
								
								if(longBreakInRotationHalf)
									nextRotationStartTime = dateSubtractMin(nextRotationStartTime, osce.getShortBreakSimpatChange() - osce.getShortBreak());
							}
							
							if(longBreakInRotationHalf)
								nextRotationStartTime = dateAddMin(nextRotationStartTime, osce.getLongBreak() - osce.getShortBreak());
							
							// add middle break at the end of each rotation (except for last rotation, where either
							// lunch break or nothing is added)
							if(!lastRotation) {
								// add lunch break after half of rotations or after specified number of rotations
//								if(osceDay.getOsceSequences().size() == 1 && (((osceDay.getLunchBreakAfterRotation() == null || osceDay.getLunchBreakAfterRotation() == 0) && halfRotations) ||
//										(osceDay.getLunchBreakAfterRotation() > 0 && osceDay.getLunchBreakAfterRotation() == currRotationNumber))) {
								log.warn(osceDay.getLunchBreakAfterRotation() + " " + currRotationNumber);
								//SPEC[ As rotationOffset is start from 0 and actual rotation is start from 1, need to compare accordingly. Also next day current roation is not start from 1 so we have to add rotationOffSet in lunchBreakAfterRotation.
								//if(osceDay.getOsceSequences().size() == 1 && osceDay.getLunchBreakAfterRotation() != null && osceDay.getLunchBreakAfterRotation() > 0 && osceDay.getLunchBreakAfterRotation() == currRotationNumber) {
								if(osceDay.getOsceSequences().size() == 1 && osceDay.getLunchBreakAfterRotation() != null && osceDay.getLunchBreakAfterRotation() > 0 && (osceDay.getLunchBreakAfterRotation()+rotationOffset) == (currRotationNumber+1)) {
								//SPEC]
									nextRotationStartTime = dateAddMin(nextRotationStartTime, osce.getLunchBreak());
									// trick to make sure postsSinceSimpatChange is 0 after outer loop (is incremented by numberSlotsTotal in outer-loop)
									postsSinceSimpatChange = -1 * numberSlotsTotal;
								} else {
									// also insert long break after rotation if there was a long break in the middle of the rotation (SP change!)
									if(changeSimpatAfterRotation || longBreakInRotationHalf) {
										nextRotationStartTime = dateAddMin(nextRotationStartTime, osce.getLongBreak());
										postsSinceSimpatChange = 0;
									} else {
										nextRotationStartTime = dateAddMin(nextRotationStartTime, osce.getMiddleBreak());
									}
								}
							}
							log.info("next rotation start time: " + nextRotationStartTime);
							
							log.info("students inserted");
							log.info("post " + i + " finished");
						}
						
						rotationStartTime = nextRotationStartTime;
						
						// increase number of already performed posts since SPs were last changed
						if(!changeSimpatAfterRotation && !changeSimpatDuringRotation)
							postsSinceSimpatChange += numberSlotsTotal;
						
						// increase lower bound of student indexes
						studentIndexLowerBound += numberSlotsTotal;
						
						assignments.addAll(assThisRotation);
						
						// switch assignments of next rotation if number of slots is odd
						if(numberSlotsTotal % 2 == 1) {
							earlyStartFirst = !earlyStartFirst;
						}
						
						log.info("rotation " + currRotationNumber + " finished");
					}
					
					log.info("parcour " + course.getColor() + " finished");
					
					parcourIndex++;
				}
				
				log.info("sequence " + osceSequence.getLabel() + " finished");
				
				// add lunch break after sequence one (WARNING: more than two sequences a day is not taken into account!)
				if(osceDay.getOsceSequences().size() > 1)
					sequenceStartTime = dateAddMin(time, osce.getLunchBreak());
				
				rotationOffset += osceSequence.getNumberRotation();
			}
			
			log.info("day " + osceDay.getOsceDate() + " finished");
		}
		
		// persist all student assignments of this rotation
		Iterator<Assignment> itAss = assignments.iterator();
		while (itAss.hasNext()) {
			Assignment assignment = (Assignment) itAss.next();
			assignment.persist();
		}
		
		correctSPSequenceNumbers();
		
		return assignments;
	}
	
	private void correctSPSequenceNumbers() {
		List<Assignment> refAssignments = Assignment.retrieveAssignmentsOfTypeSPUniqueTimes(osce);
		
		int sequenceNumber = 1;
		
		Iterator<Assignment> it = refAssignments.iterator();
		while (it.hasNext()) {
			Assignment assignment = (Assignment) it.next();
			Assignment.updateSequenceNumbersOfTypeSPByTime(sequenceNumber, assignment.getTimeStart(), assignment.getTimeEnd());
			sequenceNumber++;
		}
	}

	/**
	 * Create new SP assignment for post i at specific time
	 * @param i index of the post in "posts"
	 * @param osceDay day on which this assignment is put
	 * @param startTime time when the assignment starts
	 * @param oscePR OscePostRoom-link
	 */
	private void createSPAssignment(int i, OsceDay osceDay, Date startTime, OscePostRoom oscePR) {
		Assignment ass = new Assignment();
		ass.setType(AssignmentTypes.PATIENT);
		ass.setOsceDay(osceDay);
		ass.setTimeStart(startTime);
		ass.setTimeEnd(startTime);
		ass.setOscePostRoom(oscePR);
		ass.persist();
		
		simAssLastId[i] = ass.getId();
	}
	
	/**
	 * Finalize the previously created SP assignment
	 * @param i index of the post in "posts"
	 * @param endTime time when the assignment ends
	 */
	private void finalizeSPAssignment(int i, Date endTime) {
		Assignment ass = Assignment.findAssignment(simAssLastId[i]);
		if(ass != null) {
			ass.setTimeEnd(endTime);
			ass.flush();
		}
	}
	
	/**
	 * Change an SP on a specific post i. This methods invokes appropriate calls
	 * to "finalizeSPAssignment(...)" and "createSPAssignment(...)"
	 * @param i index of the post in "posts"
	 * @param osceDay day on which this assignment is put
	 * @param endTimeOld time when the old assignment ends
	 * @param startTimeNew time when the assignments starts
	 * @param oscePR OscePostRoom-link for new assignment
	 */
	private void changeSP(int i, OsceDay osceDay, Date endTimeOld, Date startTimeNew, OscePostRoom oscePR) {
		finalizeSPAssignment(i, endTimeOld);
		createSPAssignment(i, osceDay, startTimeNew, oscePR);
	}
	
	/**
	 * Add some minutes to a datetime and re-convert it.
	 * @param date
	 * @param minToAdd
	 * @return
	 */
	private Date dateAddMin(Date date, long minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}
	
	/**
	 * Subtract some minutes from a datetime and re-convert it.
	 * @param date
	 * @param minToSubtract
	 * @return
	 */
	private Date dateSubtractMin(Date date, long minToSubtract) {
		return new Date((long) (date.getTime() - minToSubtract * 60 * 1000));
	}

	/**
	 * Detect whether SP has to be changed after a certain number of slots
	 * @param numberSlots
	 * @return
	 */
	private boolean simpatChangeWithinSlots(int numberSlots) {
		return (numberSlotsUntilSPChange > 0 && numberSlots > numberSlotsUntilSPChange);
	}
	
	/**
	 * Check for lunch break after half of the rotations
	 * @param rotationNr
	 * @return
	 */
	private boolean lunchBreakNeeded(int rotationNr) {
		int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
		return rotationNr == rotationsMax / 2 - 1;
	}
	
	/**
	 * Get maximum of posts (of all parallel parcours) for current rotation
	 * 
	 * ex. parcour 1 has 5 posts while parcour 2 has 4 posts. Return value
	 * is then 5. It is needed to make sure parallel parcours always start
	 * at the same time.
	 * 
	 * @param parcours
	 * @param rotationNumber
	 * @return
	 */
	private int getMaxBreakPostsCurrentRotation(List<Course> parcours, int rotationNumber) {
		int maxPosts = 0;
		for(int j = 0; j < parcours.size(); j++) {
			int currPosts = rotations[j].get(rotationNumber);
			if(currPosts > maxPosts) {
				maxPosts = currPosts;
			}
		}
		return maxPosts;
	}
	
	private String debugTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}
	
	private double getTimeNeeded() {
		return timeNeeded;
	}
}
