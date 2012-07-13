package ch.unibas.medizin.osce.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.*;

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
	
	// automatically assign colors to parcours
	private static final String[] parcourColors = {"blue", "green", "red", "yellow", "purple", "orange", "turquise", "brown"};
	
	// automatically assign sequence labels
	private static final String[] sequences = {"A", "B", "C", "D", "E"};
	
	private Osce osce;
	private OsceDay osceDayRef;				// reference to first OSCE day (will not be removed if scaffold is re-created)
	private int numberMinsDayMax;			// needed for optimization purposes (will be overwritten with "osceDay.getTimeEnd() - osceDay.getTimeStart()")
	private int numberSlotsUntilChange;		// maximum number of slots that can be placed without a short break in between (defined by "slotsUntilChange" of most difficult role)
	private int numberDays;					// number of required days
	private int numberStudents;				// number of students to base schedule on
	private int numberPosts;				// number of posts defined by the OSCE
//	private int numberRooms;				// number of rooms available defined by the OSCE
	private int numberBreakPosts;			// number of break posts
	private int postLength;					// length of a single post (in minutes)
	private int rotationsPerDay;			// max number of rotations per day
	private int numberParcours;				// number of parallel running parcours
	private List<Integer>[] rotations;		// information on break posts on individual parcours
	private List<Integer> rotationsByDay = new ArrayList<Integer>();	// number of rotations for each day
	private int timeNeeded;					// total time required to perform the OSCE (without breaks)
	
	/**
	 * Calculate an optimal timetable with respect to the given parameters by trying and comparing
	 * different numbers of courses and break-posts
	 * 
	 * @param osce
	 * @return optimal timetable
	 */
	public static TimetableGenerator getOptimalSolution(Osce osce) {
//		log.info("calculating optimal solution for osce " + osce.getId());
		
		try {
			checkOsce(osce);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int numberPosts = osce.getOscePostBlueprints().size();
//		log.info("oscepostblueprint.size() = " + numberPosts);
		
		// max number of courses (decrease while looking for optimum)
		int numberParcoursMax = osce.getNumberRooms() / numberPosts;
		
		TimetableGenerator ttGen;
		TimetableGenerator optGen = null;
		double optTimeNeeded = Double.POSITIVE_INFINITY;
		
		for(int nParcours = numberParcoursMax; nParcours >= 1; nParcours--) {
			
			// iterate until (numberPosts - 1) since a number of breaks equal to
			// numberPosts would result in an additional rotation.
			for(int breakPosts = 0; breakPosts <= (numberPosts - 1); breakPosts++) {
//				System.out.println("optimize for breakPosts = " + breakPosts + ", nParcours = " + nParcours);
//				log.info("optimize for breakPosts = " + breakPosts + ", nParcours = " + nParcours);
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
		
		return optGen;
	}
	
	/**
	 * Check OSCE for valid information set needed to omptimize
	 * 
	 * @param osce
	 */
	private static void checkOsce(Osce osce) throws Exception {
		String errString = "missing information: %s";
		
		if(osce.getMaxNumberStudents() == null || osce.getMaxNumberStudents() <= 0)
			throw new Exception(String.format(errString, "maximum number of students"));
		
		if(osce.getNumberRooms() == null || osce.getNumberRooms() <= 0)
			throw new Exception(String.format(errString, "number of rooms available"));
		
		if(osce.getPostLength() == null || osce.getPostLength() <= 0)
			throw new Exception(String.format(errString, "post length"));
		
		if(osce.getShortBreak() == null || osce.getShortBreak() <= 0)
			throw new Exception(String.format(errString, "duration of short break (after a post)"));
		
		if(osce.getShortBreakSimpatChange() == null || osce.getShortBreakSimpatChange() <= 0)
			throw new Exception(String.format(errString, "duration of simpat change break (when a change of simpat is needed WITHIN rotation)"));
		
		if(osce.getMiddleBreak() == null || osce.getMiddleBreak() <= 0)
			throw new Exception(String.format(errString, "duration of middle break (after a rotation)"));
		
		if(osce.getLongBreak() == null || osce.getLongBreak() <= 0)
			throw new Exception(String.format(errString, "duration of long break (when a change of simpat is needed AFTER rotation)"));
		
		if(osce.getLunchBreak() == null || osce.getLunchBreak() <= 0)
			throw new Exception(String.format(errString, "duration of lunch break"));
		
		if(!(osce.getOsce_days().size() > 0))
			throw new Exception(String.format(errString, "no OsceDay for this Osce defined"));
	}

	@SuppressWarnings("unchecked")
	public TimetableGenerator(Osce osce, int numberBreakPosts, int numberParcours) {
		
		this.osce = osce;
		numberStudents = osce.getMaxNumberStudents();
		numberPosts = osce.getOscePostBlueprints().size();
//		numberRooms = osce.getNumberRooms();
		
		numberSlotsUntilChange = osce.slotsOfMostDifficultRole();
		
		this.numberBreakPosts = numberBreakPosts;

		// calculate number of possible parcours only if no specific number of courses is requested
//		if(numberParcours != 0) {
//			if(numberParcours > numberRooms / numberPosts) {
//				this.numberParcours = osce.getNumberCourses();
//			} else {
//				this.numberParcours = numberParcours;
//			}
//		} else {
//			if(osce.getNumberCourses() == null || osce.getNumberCourses() == 0) {
//				this.numberParcours = numberRooms / numberPosts;
//			} else {
//				this.numberParcours = osce.getNumberCourses();
//			}
//		}
		this.numberParcours = numberParcours;

		postLength = osce.getPostLength();
		
		// TODO get earliest day (which has to be day 1 by definition)
		osceDayRef = osce.getOsce_days().iterator().next();
		numberMinsDayMax = (int) (osceDayRef.getTimeEnd().getTime() - osceDayRef.getTimeStart().getTime()) / (60 * 1000);
		rotationsPerDay = (numberMinsDayMax - osce.getLunchBreak()) /
							(numberPosts * postLength + (numberPosts - 1) * osce.getShortBreak() +
									osce.getLongBreak() / numberSlotsUntilChange +
									osce.getMiddleBreak());
		
		rotations = new ArrayList[numberParcours];
		for(int i = 0; i < numberParcours; i++) {
			rotations[i] = new ArrayList<Integer>();
		}
	}
	
	public void calcTimeNeeded() {
		int slotsSinceLastBreak = 0;
		
		timeNeeded = 0;
		
		numberDays = (int) Math.ceil((double) rotations[0].size() / (double) rotationsPerDay);
//		System.out.println(numberDays);
//		System.out.println(rotationsPerDay);
//		System.out.println(rotations[0].size());
		
		int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
//		System.out.println(rotationsMax);
		
		for(int i = 0; i < numberDays; i++) {
			if(i + 1 == numberDays && numberDays > 1)
				rotationsByDay.add(i, (rotations[0].size() % rotationsPerDay));
			else
				rotationsByDay.add(i, rotationsMax);
//			System.out.println(rotationsByDay.get(i));
		}
		
		// days
		for(int i = 0; i < numberDays; i++) {
			slotsSinceLastBreak = 0;
//			System.out.println("day " + i + " (rotations: " + rotationsByDay[i] + ")");
			
			// rotations
			for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
				int breakPosts = rotations[0].get(j);
				
				// add break posts to regular posts
				int nPosts = numberPosts + numberBreakPosts - osce.numberPostsNotPossibleStart() + breakPosts;
//				System.out.println(nPosts);
				
				if(j > (i * rotationsMax)) {
					if(longBreakNeeded(j)) {
						slotsSinceLastBreak = 0;
						timeNeeded += osce.getLunchBreak();
//						System.out.print("  lunch break");
					} else if(shortBreakNeeded(slotsSinceLastBreak)) {
						slotsSinceLastBreak = 0;
						timeNeeded += osce.getLongBreak();
//						System.out.print("  long break");
					} else {
						timeNeeded += osce.getMiddleBreak();
//						System.out.print("  middle break");
					}
//					System.out.println();
				}
				
//				System.out.println("  rotation " + j + " (breakposts: " + breakPosts + ")");
				
				// posts
				for(int k = 1; k <= nPosts; k++) {
					timeNeeded += postLength;
					
					if(simpatChangeDuringCourseRun(slotsSinceLastBreak)) {
						slotsSinceLastBreak = 0;
						timeNeeded += osce.getShortBreakSimpatChange();
//						System.out.print("  short break for SP change");
						
					} else {
						slotsSinceLastBreak++;
						
						if(k != nPosts)
							timeNeeded += osce.getShortBreak();
					}
				}
			}
		}
	}
	
	/**
	 * Calculate break posts for each rotation at each parcour.
	 */
	private void calcAddBreakPosts() {
		
		// add break posts to regular posts
		int nPosts = numberPosts + numberBreakPosts - osce.numberPostsNotPossibleStart();
		
		int maxRotations = numberStudents / (nPosts * numberParcours);
		
		// init all rotations of parcours with number of posts
		for(int i = 0; i < numberParcours; i++) {
			for(int j = 0; j < maxRotations; j++) {
				rotations[i].add(numberBreakPosts);
			}
		}
		
		// remaining number of students that have no slot yet
		int diff = numberStudents - maxRotations * (nPosts * numberParcours);
		
		// each student has a slot - we are happy
		if(diff == 0) {
			return;
		}
		
		// add additional break post to individual rotations
		int numberPostsNew = numberBreakPosts + 1;
		
		int parcourIndex = 0;
		int rotationIndex = 0;
		for(int i = 0; i < diff; i++) {
			rotations[parcourIndex].set(rotationIndex, numberPostsNew);
//			System.out.println(parcourIndex + " - " + rotations[parcourIndex].get(rotationIndex));

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
		
//		numberDays = (int) Math.ceil((double) timeNeeded / (double) NUMBER_MINS_DAY_MAX);
//		numberDays = (int) Math.ceil((double) rotations[0].size() / (double) rotationsPerDay);
	}
	
	private double getTimeNeeded() {
		return timeNeeded;
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
		sb.append("number of required days: " + numberDays + "\n");
		sb.append("max number of rotations per parcour (per day): " + rotationsPerDay + "\n");
		sb.append("start time: "+osceDay.getTimeStart()+"\n");
		sb.append("end time: "+osceDay.getTimeEnd()+"\n");
		sb.append("diff in minutes: "+numberMinsDayMax+"\n");
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
					courseSlotsTotal += numberPosts + slots - osce.numberPostsNotPossibleStart();
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
		
		// remove old scaffold if existing (and re-create first OSCE day
		removeOldScaffold();
		if(true)
			return;
		// TODO: remove old scaffold (all OSCE days and re-create first day again)
		
		Set<OsceDay> days = insertOsceDays();
		Set<OsceSequence> osceSequences = new HashSet<OsceSequence>();
		
		// only one day --> seq A in the morning, seq B in the afternoon
		if(days.size() == 1) {
			OsceDay osceDay = days.iterator().next();
			
			int[] rotSeq = new int[2];
			rotSeq[0] = rotationsPerDay / 2;
			rotSeq[1] = rotationsPerDay - rotSeq[0];
			
			// create sequence for each half day
			for(int i = 0; i < 2; i++) {
				// insert sequence
				OsceSequence seq = new OsceSequence();
				seq.setLabel(sequences[i]);
				seq.setNumberRotation(rotSeq[i]);
				seq.setOsceDay(osceDay);
				
				Set<Course> parcours = insertParcoursForSequence(seq);
				
				// insert posts
				List<OscePost> posts = insertPostsForSequence(seq);
				
				seq.setCourses(parcours);
				seq.setOscePosts(posts);
				
				seq.persist();
				
				osceSequences.add(seq);
			}
			
			osceDay.setOsceSequences(osceSequences);
		} else { // multiple days --> one sequence for each day
			Iterator<OsceDay> it = days.iterator();
			int i = 0;
			while (it.hasNext()) {
				OsceDay osceDay = (OsceDay) it.next();
				
				// insert sequence
				OsceSequence seq = new OsceSequence();
				seq.setLabel(sequences[i]);
				seq.setNumberRotation(rotationsByDay.get(i));
				seq.setOsceDay(osceDay);
				
				// insert parcours
				Set<Course> parcours = insertParcoursForSequence(seq);
				
				// insert posts
				List<OscePost> posts = insertPostsForSequence(seq);
				
				seq.setCourses(parcours);
				seq.setOscePosts(posts);
				
				seq.persist();
				
				osceSequences.add(seq);
				
				i++;
				
				osceDay.setOsceSequences(osceSequences);
			}
		}
	}

	/**
	 * Remove old calculated scaffold (when OsceStatus is changed from GENERATED to BLUEPRINT again)
	 * 
	 */
	private void removeOldScaffold() {
		// temp variables for first OSCE day (used to re-create OSCE day after deleting whole scaffold)
		Date dayRefTimeStart = osceDayRef.getTimeStart();
		Date dayRefTimeEnd = osceDayRef.getTimeEnd();
		
//		Iterator<OsceDay> itDay = osce.getOsce_days().iterator();
//		log.info("removing old scaffold...if necessary");
//		while (itDay.hasNext()) {
//			OsceDay osceDay = (OsceDay) itDay.next();
//			log.info("remove osce day " + osceDay.getId());
//			osceDay.remove();
//			osceDay.flush();
//		}
//		osce.setOsce_days(null);
//		osce.flush();
		
		//SPEC[		
		log.info("removeOldScaffold start");
		Set<OsceDay> setOsceDays = new HashSet<OsceDay>();		
		Iterator<OsceDay> itDay = osce.getOsce_days().iterator();		
		OsceDay firstDay = itDay.next();							
		OsceDay removeDay = null;
		log.info("First Day : " + firstDay.getId() + " : " + firstDay.getOsceDate().toLocaleString());
		while(itDay.hasNext())
		{			
			OsceDay nextDay = itDay.next();
			log.info("Next Day : " + nextDay.getId() + " : " + nextDay.getOsceDate().toLocaleString());
			if(firstDay.getOsceDate().after(nextDay.getOsceDate()))
			{
				log.info("First Day after Next Day");
				if(firstDay.getPatientInSemesters() == null || firstDay.getPatientInSemesters().size()==0)
				{
					removeDay = firstDay;					
				}
				
				firstDay = nextDay;
			}
			else
			{
				log.info("Next Day after First Day");
				if(nextDay.getPatientInSemesters() == null || nextDay.getPatientInSemesters().size()==0)
				{
					log.info("Next Day going to be deleted");
					removeDay = nextDay;
					log.info("Next Day is deleted");					
				}
			}
			
			log.info("remove Day going to be deleted");
			osce.getOsce_days().remove(removeDay);
			removeDay.remove();			
			log.info("Next Day is deleted");
			
			
		}
		
		
		if(firstDay != null)
		{
			log.info("First Day not null : " + firstDay.getId() + " : " + firstDay.getOsceDate().toLocaleString());						
			Set<OsceSequence> setOsceSeq = firstDay.getOsceSequences();
			Iterator<OsceSequence> itOsceSeq = setOsceSeq.iterator();
			//firstDay.getOsceSequences().removeAll(setOsceSeq);			
			while(itOsceSeq.hasNext())
			{				
				OsceSequence osceSequence = itOsceSeq.next();
				log.info("Removing osce sequence : " + osceSequence.getId() );
				firstDay.getOsceSequences().remove(osceSequence);
				osceSequence.remove();
			}
			
			osceDayRef = firstDay;
		}
		else
		{
			// re-create new OSCE day
			OsceDay newDay = new OsceDay();
			newDay.setOsce(osce);
			newDay.setOsceDate(dayRefTimeStart);
			newDay.setTimeStart(dayRefTimeStart);
			newDay.setTimeEnd(dayRefTimeEnd);
			newDay.persist();
			osceDayRef = newDay;
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
	private Set<Course> insertParcoursForSequence(OsceSequence seq) {
		Set<Course> parcours = new HashSet<Course>();
		
		for(int j = 0; j < numberParcours; j++) {
			Course c = new Course();
			c.setColor(parcourColors[j]);
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
			current.setIsPossibleStart(oscePostBlueprint.getIsPossibleStart());
			
			posts.add(current);
		}
		
		return posts;
	}
	
	/**
	 * Insert the number of needed days for this OSCE into the database.
	 * 
	 * @return
	 */
	private Set<OsceDay> insertOsceDays() {
		Set<OsceDay> days = new HashSet<OsceDay>();
		
		OsceDay day0 = osce.getOsce_days().iterator().next();
//		OsceDay day0 = osceDayRef;
		
		days.add(day0);
		
		Calendar dayCal = Calendar.getInstance();
		dayCal.setTime(day0.getTimeStart());
		
		rotationsByDay.set(0, rotationsPerDay);
		
		// number of rotations we still have to assign to further days
		int rotationsTotal = rotations[0].size() - rotationsPerDay;
		
		if(numberDays > 1) {
			// create new days and corresponding sequences and parcours
			for(int i = 1; i < numberDays; i++) {
				rotationsByDay.set(i, rotationsTotal);
				dayCal.add(Calendar.DATE, i);
				
				OsceDay day = new OsceDay();
				day.setOsce(osce);
				day.setTimeStart(dayCal.getTime());
				day.setOsceDate(dayCal.getTime());
				// TODO: calculate end time!
				
				day.persist();
				
				days.add(day);
				
				rotationsTotal -= rotationsPerDay;
			}
		}
		
		day0.setTimeEnd(dateAddMin(day0.getTimeStart(), (long) timeNeeded));
		day0.flush();
		
		return days;
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
	@SuppressWarnings("unused")
	private Date dateSubtractMin(Date date, long minToSubtract) {
		return new Date((long) (date.getTime() - minToSubtract * 60 * 1000));
	}

	private boolean simpatChangeDuringCourseRun(int numberSlotsTotal) {
		int minSlotsUntilChange = osce.slotsOfMostDifficultRole();
		
		return (minSlotsUntilChange > 0 && numberSlotsTotal >= minSlotsUntilChange);
	}

//	/**
//	 * Check for the long break around mid-day (not used at the moment)
//	 * @param courseRunStart
//	 * @return
//	 */
//	private boolean longBreakNeeded(Date courseRunStart) {
//		GregorianCalendar currTimeCal = new GregorianCalendar(2011, Calendar.JUNE, 1, 13, 30, 0);
//		Date currTime = currTimeCal.getTime();
//		
//		return (courseRunStart.getTime() + 60 * 60 * 1000) > currTime.getTime() && courseRunStart.getTime() < currTime.getTime();
//	}
	
	/**
	 * Check for long break after half of the rotations
	 * @param rotationNr
	 * @return
	 */
	private boolean longBreakNeeded(int rotationNr) {
		return rotationNr == rotationsPerDay / 2;
	}

	/** Check for a short break (after a certain number of student slots)
	 * 
	 * @param postsSinceBreak
	 * @return
	 */
	private boolean shortBreakNeeded(int postsSinceBreak) {
		return (postsSinceBreak >= numberSlotsUntilChange);
	}
}