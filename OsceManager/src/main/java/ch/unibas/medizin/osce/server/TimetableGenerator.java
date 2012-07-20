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
import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;

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
		
		int numberPostsWithRoom = osce.numberPostsWithRooms();
		
		// max number of courses (decrease while looking for optimum)
		int numberParcoursMax = osce.getNumberRooms() / numberPostsWithRoom;
		
		TimetableGenerator ttGen;
		TimetableGenerator optGen = null;
		double optTimeNeeded = Double.POSITIVE_INFINITY;
		
		for(int nParcours = numberParcoursMax; nParcours >= 1; nParcours--) {
			
			// iterate until (numberPosts - number of posts with post_type=BREAK).
			// NOTE: a number of breaks equal to numberPosts would result in an additional rotation.
			for(int breakPosts = 0; breakPosts <= 0; breakPosts++) {
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
		
		log.info("optimal timetable");
		log.info("===============================");
		optGen.calcTimeNeeded();
		
		return optGen;
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
		
//		log.info(numberPosts + " " + numberBreakPosts);
//		log.info("nPosts: "+nPosts);
//		log.info("maxRotations: "+maxRotations);
		
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
		
		rotationsPerDay = (numberMinsDayMax - osce.getLunchBreak()) /
							(numberPosts * postLength + (numberPosts - 1) * osce.getShortBreak() +
							osce.getLongBreak() / numberSlotsUntilSPChange +
							osce.getMiddleBreak());
//		log.info("rotations per day: " + rotationsPerDay);
	}
	
	public void calcTimeNeeded() {
		numberDays = (int) Math.ceil((double) rotations[0].size() / (double) rotationsPerDay);
//		log.info(numberDays);
		
		boolean numberDaysVerified = false;
		
		while(numberDaysVerified == false) {
			
			int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
			
			rotationsByDay.clear();
			
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
				log.info("day " + i + " (rotations: " + rotationsByDay.get(i) + ") / rotationsMax: " + rotationsMax);
				
				// rotations
				for(int j = (i * rotationsMax); j < (i * rotationsMax + rotationsByDay.get(i)); j++) {
					int numberBreakPostsThisRotation = rotations[0].get(j);
					
					// add break posts to regular posts
					int nPostsGeneral = numberPosts + numberBreakPosts;
					int nPostsThisRotation = nPostsGeneral + numberBreakPostsThisRotation;
					
					log.info("  rotation " + j + " (breakposts: " + numberBreakPostsThisRotation + ")");
					
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
					}
				}
				
				timeNeededByDay.add(i, timeNeededCurrentDay);
				
				timeNeeded += timeNeededCurrentDay;
			}
			
			// see whether calculated time fits into the time available of first day - do another round if not
			int numberDays2 = (int) Math.ceil((double) timeNeeded / (double) numberMinsDayMax);
			if(numberDays == 1 && numberDays < numberDays2) {
				numberDays = numberDays2;
				rotationsPerDay--;
			} else {
				numberDaysVerified = true;
			}
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
		
		// remove old scaffold if existing (and re-create first OSCE day
		removeOldScaffold();
		
		Set<OsceDay> days = insertOsceDays();
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
			List<OsceSequence> setOsceSeq = firstDay.getOsceSequences();
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
		
		int rotationsMax = rotationsPerDay > rotations[0].size() ? rotations[0].size() : rotationsPerDay;
		
		rotationsByDay.set(0, rotationsMax);
		
		// number of rotations we still have to assign to further days
		int rotationsLeft = rotations[0].size() - rotationsMax;
		
		if(numberDays > 1 && rotationsLeft > 0) {
			// create new days and corresponding sequences and parcours
			for(int i = 1; i < numberDays; i++) {
				rotationsByDay.set(i, rotationsLeft);
				dayCal.add(Calendar.DATE, i);
				
				OsceDay day = new OsceDay();
				day.setOsce(osce);
				day.setOsceDate(dayCal.getTime());
				day.setTimeStart(dayCal.getTime());
				day.setTimeEnd(dateAddMin(dayCal.getTime(), timeNeededByDay.get(i)));
				
				day.persist();
				days.add(day);
				
				rotationsLeft -= rotationsPerDay;
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
		while (itDays.hasNext()) {
			OsceDay osceDay = (OsceDay) itDays.next();
			
			Date time = osceDay.getTimeStart();
			
			Date sequenceStartTime = time;
			
			// iterate over sequences ("A", "B", etc.)
			Iterator<OsceSequence> itSeq = osceDay.getOsceSequences().iterator();
			while (itSeq.hasNext()) {
				OsceSequence osceSequence = (OsceSequence) itSeq.next();
				
//				System.out.println("===============================");
//				System.out.println("sequence " + osceSequence.getLabel());
//				System.out.println("===============================");
				
				// number of rotations for current sequence (valid for all parcours in this sequence)
				int numberRotations = osceSequence.getNumberRotation();
				
				// arrays only need to be 1 dim since each course is isolated from each other
				Assignment[] simAss = new Assignment[numberPosts];
				
				Date parcourStartTime = sequenceStartTime;
				
				// iterate over all parcours (red, green, blue, etc.)
				Iterator<Course> itParc = osceSequence.getCourses().iterator();
				int parcourIndex = 0;
				while (itParc.hasNext()) {
					Course course = (Course) itParc.next();
					
//					System.out.println("-------------------------------");
//					System.out.println("parcour " + course.getColor());
//					System.out.println("-------------------------------");
					
					postsSinceSimpatChange = 0;
					
					// used for automatic simpat assignment (see IFS constraints for details)
					int simpatSequenceNumber = 1;
					
					Date rotationStartTime = parcourStartTime;
					
					// flag to define which room-assignment to take for double posts (this flag is
					// switched whenever a room-assignment for a double-post is inserted in order to avoid overlapping)
					boolean useFirstPartOfDoublePost = false;
					
					boolean earlyStartFirst = false;
					
					// create assignments for rotations (given by sequence)
					for(int currRotationNumber = rotationOffset; currRotationNumber < (rotationOffset + numberRotations); currRotationNumber++) {
						int numberBreakPosts = rotations[parcourIndex].get(currRotationNumber);
						int numberSlotsTotal = posts.size() + numberBreakPosts;
						
						Set<Assignment> assThisRotation = new HashSet<Assignment>();
						
//						System.out.println("rotation "+(currRotationNumber - rotationOffset + 1)+"/"+numberRotations+" (total rotation: "+currRotationNumber+"/"+(rotationOffset + numberRotations - 1)+"), numberBreakPosts: "+numberBreakPosts+", numberSlotsTotal: "+numberSlotsTotal);
						
						// reset to the point in time where the rotation starts (necessary since
						// we increase the time while going through all posts of a rotation)
						time = rotationStartTime;
						
						// get max slots from current rotation (defines when next rotation is about to
						// start since rotations need to start at the same time!)
						int maxPostsCurrentRotation = numberPosts + getMaxBreakPostsCurrentRotation(osceSequence.getCourses(), currRotationNumber);
						
						boolean firstRotation = currRotationNumber == rotationOffset;
						boolean lastRotation = currRotationNumber == (rotationOffset + numberRotations - 1);
						boolean changeSimpatDuringRotation = simpatChangeWithinSlots(postsSinceSimpatChange + numberSlotsTotal);

						// calculate slots that this and next rotation have - used to check whether a SP change after the rotation is necessary
						int numberSlotsThisAndNextRotation = numberSlotsTotal + numberPosts;
						if(currRotationNumber < (rotationOffset + numberRotations) - 1) {
							numberSlotsThisAndNextRotation += rotations[parcourIndex].get(currRotationNumber + 1);
						}
						boolean changeSimpatAfterRotation = simpatChangeWithinSlots(postsSinceSimpatChange + numberSlotsThisAndNextRotation);
						
						// check whether new simpat has been added (and increment sequenceNumber if so)
						boolean simpatAdded = false;
						
						// iterate through all posts as many times as there are posts
						// (in the end, we want to have a n*n matrix where n denotes the number of posts in this OSCE)
						for(int i = 0; i < numberSlotsTotal; i++) {
							
							// flag to determine whether the student started the rotation early - if so,
							// the last assignment (which would be the second part of the double post)
							// will not be created
							boolean hadEarlyStart = false;
							
							// post must be a possible start (PAUSE - which is at the end is always a possible start)
							// fill slots for one student in current rotation
							int postIndex = i;
							for(int j = i; j < (numberSlotsTotal + i); j++) {

								// STUDENTS START
								Date endTime = dateAddMin(time, osce.getPostLength());
								Assignment ass = new Assignment();
								ass.setType(AssignmentTypes.STUDENT);
								ass.setOsceDay(osceDay);
								ass.setSequenceNumber(studentIndex);
								ass.setTimeStart(time);

								boolean createAssignment = true;

								// the last assignment of a student with early start will not be created
								// (this would be the second part of a double post)
								if(hadEarlyStart && j == (numberSlotsTotal + i) - 1) {
									createAssignment = false;
								}

								// insert post or break post (no OscePostRoom assignment)
								if(numberBreakPosts == 0 || j != numberSlotsTotal - 1) {
									OscePost post = OscePost.findOscePostsByOscePostBlueprintAndOsceSequence(posts.get(postIndex % posts.size()), osceSequence).getSingleResult();
									OscePostBlueprint postBP = post.getOscePostBlueprint();
									OscePostRoom opr = OscePostRoom.findOscePostRoomsByCourseAndOscePost(course, post).getSingleResult();
									PostType postType = postBP.getPostType();

									// ANAMNESIS_THERAPY has double post in same room an therefore needs alteration of room assignments
									// all other room assignments are straightforward
									if(postType.equals(PostType.ANAMNESIS_THERAPY)) {

										// determine which part of the double-post to use - skip the other part by not creating an assignment
										if((!useFirstPartOfDoublePost && !postBP.isFirstPartOfDoublePost()) || 
												(useFirstPartOfDoublePost && postBP.isFirstPartOfDoublePost())) {

											ass.setOscePostRoom(opr);

											// add another post-time since this is a double post
											endTime = dateAddMin(endTime, osce.getPostLength() + osce.getShortBreak());
										} else {
											createAssignment = false;
										}

										// switch alternation flag (= make sure that in the next iteration, the other part of the double post is used)
										if(!postBP.isFirstPartOfDoublePost()) {
											useFirstPartOfDoublePost = !useFirstPartOfDoublePost;
										}
									} else {
										ass.setOscePostRoom(opr);
									}
									
									// handle early start (only 1 student per rotation - this student will automatically finish the rotation early)
									if(i == (postIndex % posts.size())) {
										if((postType.equals(PostType.PREPARATION) && postBP.isFirstPartOfDoublePost()) ||
												postType.equals(PostType.ANAMNESIS_THERAPY) &&!postBP.isFirstPartOfDoublePost()) {
											
											ass.setTimeStart(dateSubtractMin(time, osce.getPostLength() + osce.getShortBreak()));
											endTime = dateSubtractMin(endTime, osce.getPostLength() + osce.getShortBreak());

											hadEarlyStart = true;

											// stay at same part of double post if number of posts is odd (= no free slot at the other part of the double-post)
											if(numberSlotsTotal % 2 == 1) {
												useFirstPartOfDoublePost = !useFirstPartOfDoublePost;
											}
										}
									}

									postIndex++;
								}

								if(createAssignment) {
									ass.setTimeEnd(endTime);
									assThisRotation.add(ass);

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
							}

							studentIndex++;
							postsSinceSimpatChange++;
							// STUDENTS END
							
							if(i < numberPosts) {
								// not all types of posts need an SP
								if(posts.get(i).requiresSimpat()) {
									// SIMPATS START
									OscePost post = OscePost.findOscePostsByOscePostBlueprintAndOsceSequence(posts.get(i), osceSequence).getSingleResult();
									// TODO re-activate this after testing
//									if(!post.getStandardizedRole().getRoleType().equals(RoleTypes.Material)) {
										
										// finalize SP assignment - after last rotation of a parcour as well as if change during or after rotation is needed
										// (assuming that assignment has been initialized already)
										if((lastRotation || changeSimpatAfterRotation || changeSimpatDuringRotation) && simAss[i] instanceof Assignment) {
											Date endTime = time;
											
											// adapt time for early start
											// TODO: fix incorrect calculation caused by OscePostRoom switching
											if(posts.get(i).getPostType().equals(PostType.ANAMNESIS_THERAPY)) {
												if((earlyStartFirst && posts.get(i).isFirstPartOfDoublePost()) ||
														(!earlyStartFirst && !posts.get(i).isFirstPartOfDoublePost())) {
													endTime = dateSubtractMin(endTime, osce.getPostLength() + osce.getShortBreak());
												}
											}
											
											simAss[i].setTimeEnd(endTime);
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
											
											// adapt time for early start
											// TODO: fix incorrect calculation caused by OscePostRoom switching
											if(posts.get(i).getPostType().equals(PostType.ANAMNESIS_THERAPY)) {
												if((earlyStartFirst && posts.get(i).isFirstPartOfDoublePost()) ||
														(!earlyStartFirst && !posts.get(i).isFirstPartOfDoublePost())) {
													startTime = dateSubtractMin(startTime, osce.getPostLength() + osce.getShortBreak());
												}
											}
											
//											endTime = dateSubtractMin(endTime, osce.getPostLength() + osce.getShortBreak());

											simAss[i] = new Assignment();
											simAss[i].setType(AssignmentTypes.PATIENT);
											simAss[i].setOsceDay(osceDay);
											simAss[i].setSequenceNumber(simpatSequenceNumber);
											simAss[i].setTimeStart(startTime);
											simAss[i].setOscePostRoom(opr);
											assThisRotation.add(simAss[i]);
											
											simpatAdded = true;
										}
//									}
									// SIMPATS END
								}

								// reset time to start time of next course
								time = new Date((long) (rotationStartTime.getTime()));
							}
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
						
						// switch assignments if earlyStartFirst
						if(earlyStartFirst == true) {
							assThisRotation = switchOscePostRoom(assThisRotation);
						}
						
						assignments.addAll(assThisRotation);
						
						if(numberSlotsTotal % 2 == 1) {
							earlyStartFirst = !earlyStartFirst;
						}
					}
					
					parcourIndex++;
				}
				
				// add lunch break after sequence one (WARNING: more than two sequences a day is not taken into account!)
				sequenceStartTime = dateAddMin(time, osce.getLunchBreak());
				
				rotationOffset += osceSequence.getNumberRotation();
			}
		}
		
		printAllStudents(assignments);
		printAllSP(assignments);
		
		// studentsLeft should be 0 here - all students meant to be assigned
		if((studentIndex - 1) != osce.getMaxNumberStudents()) {
			log.error("number of created slots does not equal the number of students!");
		} else {
			log.info("[SUCCESS] hooray! number of created slots is enough to examine all student!");
		}
		
		return assignments;
	}
	
	private void printAllStudents(Set<Assignment> assThisRotation) {
		List<Assignment> assList = new ArrayList<Assignment>(assThisRotation);
		Collections.sort(assList, new Comparator<Assignment>() {
			public int compare(Assignment ass1, Assignment ass2) {
				if(ass1.getSequenceNumber() == ass2.getSequenceNumber()) {
					return (int) (ass1.getTimeStart().getTime() - ass2.getTimeStart().getTime());
				} else {
					return (int) (ass1.getSequenceNumber() - ass2.getSequenceNumber());
				}
			}
			
		});
		
		Iterator<Assignment> it = assList.iterator();
		int oldSeq = 0;
		while(it.hasNext()) {
			Assignment ass = (Assignment) it.next();
			
			if(ass.getSequenceNumber() > oldSeq) {
				System.out.println("\t ---");
				oldSeq = ass.getSequenceNumber();
			}
			
			if(ass.getType().equals(AssignmentTypes.STUDENT))
				System.out.println(debugStudent(ass));
		}
		
	}
	
	private void printAllSP(Set<Assignment> assignments) {
		List<Assignment> assList = new ArrayList<Assignment>(assignments);
		Collections.sort(assList, new Comparator<Assignment>() {
			public int compare(Assignment ass1, Assignment ass2) {
				if(ass1.getSequenceNumber() == ass2.getSequenceNumber()) {
					return (int) (ass1.getTimeStart().getTime() - ass2.getTimeStart().getTime());
				} else {
					return (int) (ass1.getSequenceNumber() - ass2.getSequenceNumber());
				}
			}
			
		});
		
		Iterator<Assignment> it = assList.iterator();
		while(it.hasNext()) {
			Assignment ass = (Assignment) it.next();
			
			if(ass.getType().equals(AssignmentTypes.PATIENT))
				System.out.println(debugSP(ass));
		}
		
	}

	private Set<Assignment> switchOscePostRoom(Set<Assignment> assThisRotation) {
		Iterator<Assignment> it = assThisRotation.iterator();
		OscePostRoom opr1 = null;
		OscePostRoom opr2 = null;
		while (it.hasNext() && (opr1 == null || opr2 == null)) {
			Assignment ass = (Assignment) it.next();
			
			if(ass.getOscePostRoom() != null) {
				OscePostBlueprint postBP = ass.getOscePostRoom().getOscePost().getOscePostBlueprint();
				if(postBP.getPostType().equals(PostType.ANAMNESIS_THERAPY)) {
					if(postBP.isFirstPartOfDoublePost()) {
						opr1 = ass.getOscePostRoom();
					} else {
						opr2 = ass.getOscePostRoom();
					}
				}
			}
		}
		
		it = assThisRotation.iterator();
		while(it.hasNext()) {
			Assignment ass = (Assignment) it.next();
			
			if(ass.getOscePostRoom() != null) {
				OscePostBlueprint postBP = ass.getOscePostRoom().getOscePost().getOscePostBlueprint();
				if(postBP.getPostType().equals(PostType.ANAMNESIS_THERAPY)) {
					if(postBP.isFirstPartOfDoublePost()) {
						ass.setOscePostRoom(opr2);
					} else {
						ass.setOscePostRoom(opr1);
					}
				}
			}
		}
		
		return assThisRotation;
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
	 * Check OSCE for valid information set needed to optimize
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
	
	private String debugTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}
	
	private String debugStudent(Assignment ass) {
		String room = "none";
		OscePostBlueprint postBP = null;
		if(ass.getOscePostRoom() != null) {
			room = ass.getOscePostRoom().getRoom().getRoomNumber();
			postBP = ass.getOscePostRoom().getOscePost().getOscePostBlueprint();
		}
		
		return "\t student "+ass.getSequenceNumber()+" (room: "+room+", "+debugTimeStartEnd(ass)+") inserted..." + (postBP != null && postBP.getPostType().equals(PostType.ANAMNESIS_THERAPY) ? "first: " + postBP.isFirstPartOfDoublePost() : "");
	}
	
	private String debugSP(Assignment ass) {
		String room = "none";
		if(ass.getOscePostRoom() != null) {
			room = ass.getOscePostRoom().getRoom().getRoomNumber();
		}
		return "(room: "+room+", "+debugTimeStartEnd(ass)+") inserted...";
	}
}
