package ch.unibas.medizin.osce.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.*;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.RoleTypes;

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
	private int numberSlotsUntilSPChange;		// maximum number of slots that can be placed without a short break in between (defined by "slotsUntilChange" of most difficult role)
	private int numberDays;					// number of required days
	private int numberStudents;				// number of students to base schedule on
	private int numberPosts;				// number of posts defined by the OSCE
	private int numberBreakPosts;			// number of break posts
	private int postLength;					// length of a single post (in minutes)
	private int rotationsPerDay;			// max number of rotations per day
	private int numberParcours;				// number of parallel running parcours
	private List<Integer>[] rotations;		// information on break posts on individual parcours
	private List<Integer> rotationsByDay = new ArrayList<Integer>();	// number of rotations for each day
	private int timeNeeded;					// total time required to perform the OSCE (without breaks)
	private List<Integer> timeNeededByDay = new ArrayList<Integer>();	// time need per individual day (required to calculate exact end-time of each day)
	
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
	public TimetableGenerator(Osce osce, int nBreakPosts, int nParcours) {
		
		this.osce = osce;
		numberStudents = osce.getMaxNumberStudents();
		numberPosts = osce.getOscePostBlueprints().size();
		postLength = osce.getPostLength();
		numberSlotsUntilSPChange = osce.slotsOfMostDifficultRole();
		numberBreakPosts = nBreakPosts;
		numberParcours = nParcours;

		osceDayRef = osce.getOsce_days().iterator().next();
		numberMinsDayMax = (int) (osceDayRef.getTimeEnd().getTime() - osceDayRef.getTimeStart().getTime()) / (60 * 1000);
		rotationsPerDay = (numberMinsDayMax - osce.getLunchBreak()) /
							(numberPosts * postLength + (numberPosts - 1) * osce.getShortBreak() +
									osce.getLongBreak() / numberSlotsUntilSPChange +
									osce.getMiddleBreak());
		
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
		int nPosts = numberPosts + numberBreakPosts - osce.numberPostsNotPossibleStart();
		
		int maxRotations = numberStudents / (nPosts * numberParcours);
		
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
	
	public void calcTimeNeeded() {
		numberDays = (int) Math.ceil((double) rotations[0].size() / (double) rotationsPerDay);
//		System.out.println(numberDays);
		
		int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
//		System.out.println("max rotations: " + rotationsMax);
		
		for(int i = 0; i < numberDays; i++) {
			if(i == numberDays - 1 && numberDays > 1)
				rotationsByDay.add(i, (rotations[0].size() % rotationsPerDay));
			else
				rotationsByDay.add(i, rotationsMax);
		}
		
		timeNeeded = 0;
		int timeNeededCurrentDay = 0;
		int slotsSinceLastSimpatChange = 0;
		
		// days
		for(int i = 0; i < numberDays; i++) {
			slotsSinceLastSimpatChange = 0;
//			System.out.println("day " + i + " (rotations: " + rotationsByDay.get(i) + ")");
			
			// rotations
			for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
				int numberBreakPostsThisRotation = rotations[0].get(j);
				
				// add break posts to regular posts
				int nPostsGeneral = numberPosts + numberBreakPosts - osce.numberPostsNotPossibleStart();
				int nPostsThisRotation = nPostsGeneral + numberBreakPostsThisRotation;
				
//				System.out.println("  rotation " + j + " (breakposts: " + numberBreakPostsThisRotation + ")");
				
				// posts
				for(int k = 0; k < nPostsThisRotation; k++) {
					timeNeededCurrentDay += postLength;
					
					if(simpatChangeWithinSlots(slotsSinceLastSimpatChange)) {
						slotsSinceLastSimpatChange = 0;
						timeNeededCurrentDay += osce.getShortBreakSimpatChange();
//						System.out.print("  short break for SP change");
					} else {
						if(k < nPostsThisRotation - 1)
							timeNeededCurrentDay += osce.getShortBreak();
					}
					
					slotsSinceLastSimpatChange++;
				}
				
				// additional breaks
				if(j < rotations[0].size() - 1) {
					if(lunchBreakNeeded((j + 1) % rotationsMax)) {
						slotsSinceLastSimpatChange = 0;
						timeNeededCurrentDay += osce.getLunchBreak();
//						System.out.print("  lunch break");
					} else if(simpatChangeWithinSlots(slotsSinceLastSimpatChange + nPostsGeneral + rotations[0].get(j + 1))) {
						slotsSinceLastSimpatChange = 0;
						timeNeededCurrentDay += osce.getLongBreak();
//						System.out.print("  long break");
					} else {
						timeNeededCurrentDay += osce.getMiddleBreak();
//						System.out.print("  middle break");
					}
					System.out.println();
				}
			}
			
			timeNeededByDay.add(i, timeNeededCurrentDay);
			
			timeNeeded += timeNeededCurrentDay;
		}
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
		
		//SPEC[		
		log.info("removeOldScaffold start");
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
				day.setOsceDate(dayCal.getTime());
				day.setTimeStart(dayCal.getTime());
				day.setTimeEnd(dateAddMin(dayCal.getTime(), timeNeededByDay.get(i)));
				
				day.persist();
				days.add(day);
				
				rotationsTotal -= rotationsPerDay;
			}
		}
		
		day0.setTimeEnd(dateAddMin(day0.getTimeStart(), (long) timeNeededByDay.get(0)));
		day0.flush();
		
		return days;
	}
	
	
	/**
	 * Create assignments for students, SimPats and examiners.
	 * @return set containing all assignments
	 */
	public Set<Assignment> createAssignments() {
		Set<Assignment> assignments = new HashSet<Assignment>();
		
		List<OsceDay> days = new ArrayList<OsceDay>(osce.getOsce_days());
		
		// sort days
		Collections.sort(days, new Comparator<OsceDay>() {
			public int compare(OsceDay day1, OsceDay day2) {
				return (int) (day1.getTimeStart().getTime() - day2.getTimeStart().getTime());
			}

		});
		
		// get posts (sorted by sequenceNumber)
		List<OscePostBlueprint> posts = osce.getOscePostBlueprints();
		
		// number of student left to assign (will be decremented while running)
		int studentIndex = 1;
		
		int postsSinceSimpatChange = 0;	// SimPat change
		
		// total number of rotations is split among sequences, rotationOffset will be incremented
		// by the number of rotations of a sequence after handling it.
		// (e.g. and 1 osce-day has 2 sequences with 4 and 3 rotations, rotationOffset will then
		// first be 0 and 4 after first iteration of sequences)
		int rotationOffset = 0;
		
		// iterate over all days
		Iterator<OsceDay> itDays = days.iterator();
//		OsceDay prevDay = null;
		while (itDays.hasNext()) {
			OsceDay osceDay = (OsceDay) itDays.next();
			
			Date time = osceDay.getTimeStart();
			
			Date sequenceStartTime = time;
			
			// iterate over sequences ("A", "B", etc.)
			Iterator<OsceSequence> itSeq = osceDay.getOsceSequences().iterator();
//			OsceSequence prevSequence = null;
			while (itSeq.hasNext()) {
				OsceSequence osceSequence = (OsceSequence) itSeq.next();
				
				System.out.println("===============================");
				System.out.println("sequence " + osceSequence.getLabel());
				System.out.println("===============================");
				
				// number of rotations for current sequence (valid for all parcours in this sequence)
				int numberRotations = osceSequence.getNumberRotation();
				
				// arrays only need to be 1 dim since each course is isolated from each other
//				Assignment[] exaAss = new Assignment[numberPosts];
				Assignment[] simAss = new Assignment[numberPosts];
				
				Date parcourStartTime = sequenceStartTime;
				
				// iterate over all parcours (red, green, blue, etc.)
				Iterator<Course> itParc = osceSequence.getCourses().iterator();
				int parcourIndex = 0;
				while (itParc.hasNext()) {
					Course course = (Course) itParc.next();
					
					System.out.println("-------------------------------");
					System.out.println("parcour " + course.getColor());
					System.out.println("-------------------------------");
					
					postsSinceSimpatChange = 0;
					
					// flag to define which room-assignment to take for double posts
					boolean useAltOscePostRoom = false;
					
					// used for automatic simpat assignment (see IFS constraints for details)
					int simpatSequenceNumber = 1;
					
					Date rotationStartTime = parcourStartTime;
					
					// create assignments for rotations (given by sequence)
					for(int currRotationNumber = rotationOffset; currRotationNumber < (rotationOffset + numberRotations); currRotationNumber++) {
						int numberBreakPosts = rotations[parcourIndex].get(currRotationNumber);
						int numberSlotsTotal = posts.size() + numberBreakPosts;
						
						System.out.println("rotation "+(currRotationNumber - rotationOffset + 1)+"/"+numberRotations+" (total rotation: "+currRotationNumber+"/"+(rotationOffset + numberRotations - 1)+"), numberBreakPosts: "+numberBreakPosts+", numberSlotsTotal: "+numberSlotsTotal);
						
						time = rotationStartTime;
						
						// get max slots from current rotation (defines when next rotation is about to
						// start since rotations need to start at the same time!)
						int maxPostsCurrentRotation = numberPosts + getMaxBreakPostsCurrentRotation(osceSequence.getCourses(), currRotationNumber);
						
						boolean changeSimpatDuringRotation = simpatChangeWithinSlots(numberSlotsTotal);
						boolean changeSimpatAfterRotation = simpatChangeWithinSlots(postsSinceSimpatChange + 2*numberPosts);
						
//						boolean newDay = (prevDay != null && prevDay != osceDay);
//						boolean newSequence = (prevSequence != null && prevSequence != osceSequence);
						boolean firstRotation = currRotationNumber == rotationOffset;
						boolean lastRotation = currRotationNumber == (rotationOffset + numberRotations - 1);
						
						// check whether new simpat has been added (and increment sequenceNumber if so)
						boolean simpatAdded = false;
						
						// iterate through all posts as many times as there are posts
						// (in the end, we want to have a n*n matrix where n denotes the number of posts in this OSCE)
						for(int i = 0; i < numberSlotsTotal; i++) {
							
							// STUDENTS START
//							System.out.println("STUDENT ASSIGNMENTS");
							// post must be a possible start (PAUSE - which is at the end is always a possible start)
							if(i == (numberSlotsTotal - 1) || posts.get(i).isPossibleStart()) {

								// fill slots for one student in current rotation
								int index = i;
								for(int j = i; j < (numberSlotsTotal + i); j++) {

									// STUDENTS START
									Date endTime = dateAddMin(time, osce.getPostLength());
									Assignment ass = new Assignment();
									ass.setType(AssignmentTypes.STUDENT);
									ass.setOsceDay(osceDay);
									ass.setSequenceNumber(studentIndex);
									// TODO: evaluate if second break post (in the middle of the course) is needed
									
									// insert post or break post (no OscePostRoom assignment)
									if(numberBreakPosts == 0 || j != numberSlotsTotal - 1) {
										OscePost post = OscePost.findOscePostsByOscePostBlueprintAndOsceSequence(posts.get(index % posts.size()), osceSequence).getSingleResult();
										OscePostRoom opr = OscePostRoom.findOscePostRoomsByCourseAndOscePost(course, post).getSingleResult();
										
										// alternate between room assignments. Alternative room assignment is in subsequent post
										// (this is for "double-posts" but at the moment only for post_type = ANAMNESIS_THERAPY, since
										// this is the only "double-post" which is located in the same room and therefore needs an alternative
										// assignment) 
										// TODO: verify this if-clause!
										if(post.getOscePostBlueprint().getPostType().equals(PostType.ANAMNESIS_THERAPY)) {
											System.out.println("inside...");
											if(useAltOscePostRoom) {
												System.out.println("use alt oscepostroom");
												OscePostBlueprint otherPartOfDoublePostBlueprint = post.getOscePostBlueprint().otherPartOfDoublePost();
												System.out.println(otherPartOfDoublePostBlueprint.getSequenceNumber());
												OscePost otherPartOfDoublePost = OscePost.findOscePostsByOscePostBlueprintAndOsceSequence(otherPartOfDoublePostBlueprint, osceSequence).getSingleResult();
												System.out.println(otherPartOfDoublePost.getId());
												OscePostRoom oprAlt = OscePostRoom.findOscePostRoomsByCourseAndOscePost(course, otherPartOfDoublePost).getSingleResult();
												System.out.println(oprAlt.getRoom().getRoomNumber());
												if(oprAlt != null) {
													ass.setOscePostRoom(oprAlt);
												}
											} else {
												ass.setOscePostRoom(opr);
											}
											
											// alternate between two states
											useAltOscePostRoom = !useAltOscePostRoom;
											
											// add another post-time since this is a double post
											endTime = dateAddMin(endTime, osce.getPostLength() + osce.getShortBreak());
											
											// skip next post as it is just used to get second room assignment for double post
											j++;
										} else {
											ass.setOscePostRoom(opr);
										}
										
										index++;
									}
									
									ass.setTimeStart(time);
									ass.setTimeEnd(endTime);
									assignments.add(ass);

//									if(i == 1)
									String room = "none";
									if(ass.getOscePostRoom() != null) {
										room = ass.getOscePostRoom().getRoom().getRoomNumber();
									}
//									System.out.println("parcour "+course.getColor()+", rotation "+currRotationNumber+", student "+studentIndex+" (room: "+room+", "+debugTimeStartEnd(ass)+") inserted...");
									System.out.println("\t student "+studentIndex+" (room: "+room+", "+debugTimeStartEnd(ass)+") inserted...");

									if(j == (numberSlotsTotal + i) - 1)
										System.out.println("\t ---");
									
									time = dateAddMin(time, osce.getPostLength());
									
									if(j < (numberSlotsTotal + i) - 1) {
										// add short break between posts
										// if, for example, there is a maximum of 8 posts and the most difficult role-topic needs a SimPat change after
										// 5 students, there is only one possible SimPat change during a rotation (best placed in the middle)
										if(changeSimpatDuringRotation && j == course.getOscePostRooms().size() / 2) {
											time = dateAddMin(endTime, osce.getShortBreakSimpatChange());
										} else {
											time = dateAddMin(endTime, osce.getShortBreak());
										}
									}
								}
								
								studentIndex++;
								postsSinceSimpatChange++;
							}
							// STUDENTS END
							
							if(i < numberPosts) {
//								// EXAMINERS START
//								System.out.println("EXAMINER ASSIGNMENTS");
//								
//								// finalize examiner assignment - after lunch break or when starting a new day
//								// (assuming that assignment has been initialized already)
//								if((newDay || newSequence || lastRotation) && exaAss[i] instanceof Assignment) {
//									// subtract time at examiner change
//									Date end = null;
//									if(newSequence) {
//										end = dateSubtractMin(rotationStartTime, osce.getLunchBreak());
//									} else if(lastRotation) {
//										end = dateAddMin(rotationStartTime, maxPostsCurrentRotation * (postLength + osce.getShortBreak()) - osce.getShortBreak());
//									} else if(newDay) {
//										end = timeBeforeDayChange;
//										//end = dateAddMin(timeBeforeDayChange, maxPostsCurrentRotation * (postLength + osce.getShortBreak()) - osce.getShortBreak());
//									} else {
//										end = dateSubtractMin(rotationStartTime, osce.getMiddleBreak());
//									}
//									exaAss[i].setTimeEnd(end);
//									
////									System.out.println("parcour "+course.getColor()+", rotation "+rotationIndex+", ("+exaAss[i].debugExaminator()+") inserted...");
//								}
//								
//								// initialize examiner assignment
//								if(firstRotation || newDay || newSequence) {
//									OscePost post = OscePost.findOscePostsByOscePostBlueprintAndOsceSequence(posts.get(i), osceSequence).getSingleResult();
//									OscePostRoom opr = OscePostRoom.findOscePostRoomsByCourseAndOscePost(course, post).getSingleResult();
//									
//									exaAss[i] = new Assignment();
//									exaAss[i].setType(AssignmentTypes.EXAMINER);
//									exaAss[i].setOsceDay(osceDay);
//									exaAss[i].setTimeStart(rotationStartTime);
//									exaAss[i].setOscePostRoom(opr);
//									//exaAss[i].setSlotNumber((courseRunNr == 0 || dayChanged ? 1 : 2)); // for examiners, there is only slot number 1 or 2
//									assignments.add(exaAss[i]);
//								}
//								// EXAMINERS END
								
								
								// SIMPATS START
								OscePost post = OscePost.findOscePostsByOscePostBlueprintAndOsceSequence(posts.get(i), osceSequence).getSingleResult();
								if(!post.getStandardizedRole().getRoleType().equals(RoleTypes.Material)) {
									// finalize SP assignment - after last rotation of a parcour as well as if change during or after rotation is needed
									// (assuming that assignment has been initialized already)
									if((lastRotation || changeSimpatAfterRotation || changeSimpatDuringRotation) && simAss[i] instanceof Assignment) {
										simAss[i].setTimeEnd(time);

//										String room = "none";
//										if(simAss[i].getOscePostRoom() != null) {
//											room = simAss[i].getOscePostRoom().getRoom().getRoomNumber();
//										}
//										System.out.println("(room: "+room+", "+debugTimeStartEnd(simAss[i])+") inserted...");
									}

									// initialize SP assignment at the beginning of the first rotation of a parcour or whenever a SP change is necessary
									if(firstRotation || changeSimpatAfterRotation || changeSimpatDuringRotation) {
										OscePostRoom opr = OscePostRoom.findOscePostRoomsByCourseAndOscePost(course, post).getSingleResult();

										Date startTime = rotationStartTime;
										if(!firstRotation) {
											startTime = dateAddMin(rotationStartTime, maxPostsCurrentRotation * (osce.getPostLength() + osce.getShortBreak()));

											if(changeSimpatDuringRotation) {
												startTime = dateSubtractMin(startTime, osce.getShortBreakSimpatChange());
											} else {
												startTime = dateSubtractMin(startTime, osce.getShortBreak());
											}

											if(changeSimpatAfterRotation) {
												startTime = dateAddMin(startTime, osce.getLongBreak());
											} else {
												startTime = dateAddMin(startTime, osce.getMiddleBreak());
											}
										}

										simAss[i] = new Assignment();
										simAss[i].setType(AssignmentTypes.PATIENT);
										simAss[i].setOsceDay(osceDay);
										simAss[i].setSequenceNumber(simpatSequenceNumber);
										simAss[i].setTimeStart(startTime);
										simAss[i].setOscePostRoom(opr);
										assignments.add(simAss[i]);
										
										simpatAdded = true;
									}
								}
								// SIMPATS END
							}
							
							// reset time to start time of next course
							time = new Date((long) (rotationStartTime.getTime()));
						}
						
						if(simpatAdded)
							simpatSequenceNumber++;
						
						// calculate time when next rotation starts
						time = dateAddMin(rotationStartTime, maxPostsCurrentRotation * (osce.getPostLength() + osce.getShortBreak()));
						
						// if simpat was changed during rotation, subtract the simpat-change-break
						if(changeSimpatDuringRotation) {
							time = dateSubtractMin(time, osce.getShortBreakSimpatChange());
							postsSinceSimpatChange = 0;
						} else {
							time = dateSubtractMin(time, osce.getShortBreak());
						}
						
						// add middle break at the end of each rotation (except for last rotation, where either
						// lunch break or nothing is added)
						if(changeSimpatAfterRotation) {
							rotationStartTime = dateAddMin(time, osce.getLongBreak());
							postsSinceSimpatChange = 0;
						} else {
							rotationStartTime = dateAddMin(time, osce.getMiddleBreak());
						}
						
					}
					
					parcourIndex++;
				}
				
				// add lunch break after sequence one (WARNING: more than two sequences a day is not taken into account!)
				sequenceStartTime = dateAddMin(time, osce.getLunchBreak());
				
				rotationOffset += osceSequence.getNumberRotation();
			}
		}
		
		// studentsLeft should be 0 here - all students meant to be assigned
		if((studentIndex - 1) != osce.getMaxNumberStudents()) {
			System.out.println("number of created slots does not equal the number of students - PROBLEM!");
		} else {
			System.out.println("hooray! number of created slots is enough to examine all student!");
		}
		
		return assignments;
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
		return (numberSlotsUntilSPChange > 0 && numberSlots >= numberSlotsUntilSPChange);
	}

//	/**
//	 * Check for the lunch break around mid-day (not used at the moment)
//	 * @param courseRunStart
//	 * @return
//	 */
//	private boolean lunchBreakNeeded(Date courseRunStart) {
//		GregorianCalendar currTimeCal = new GregorianCalendar(2011, Calendar.JUNE, 1, 13, 30, 0);
//		Date currTime = currTimeCal.getTime();
//		
//		return (courseRunStart.getTime() + 60 * 60 * 1000) > currTime.getTime() && courseRunStart.getTime() < currTime.getTime();
//	}
	
	/**
	 * Check for lunch break after half of the rotations
	 * @param rotationNr
	 * @return
	 */
	private boolean lunchBreakNeeded(int rotationNr) {
		return rotationNr == rotationsPerDay / 2;
	}

	/** Check for a long break (after a certain number of student slots)
	 * 
	 * @param postsSinceBreak
	 * @return
	 */
	private boolean longBreakNeeded(int postsSinceBreak) {
		return (postsSinceBreak >= numberSlotsUntilSPChange);
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
	private int getMaxBreakPostsCurrentRotation(Set<Course> parcours, int rotationNumber) {
		int maxPosts = 0;
		for(int j = 0; j < parcours.size(); j++) {
			int currPosts = rotations[j].get(rotationNumber);
			if(currPosts > maxPosts) {
				maxPosts = currPosts;
			}
		}
		return maxPosts;
	}
	
	/**
	 * Print debug information on assignment (namely start- and end-time consiting only of HH:mm)
	 * @param ass
	 * @return
	 */
	private String debugTimeStartEnd(Assignment ass) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String timeString = format.format(ass.getTimeStart()) + " - " + format.format(ass.getTimeEnd());
		return timeString;
	}
}
