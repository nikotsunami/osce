package ch.unibas.medizin.osce.server.ttgen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.OscePostBlueprint;
import ch.unibas.medizin.osce.shared.AssignmentTypes;
import ch.unibas.medizin.osce.shared.PostType;

/**
 * Methods useful for debugging - can be removed once application is going to be deployed
 * 
 * @author dk
 * @deprecated
 *
 */
public class DebugToolbox {

	/**
	 * Print all student assignments formatted nicely
	 * 
	 * @param assignments
	 */
	public void printAllStudents(Set<Assignment> assignments) {
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
	
	/**
	 * Print all SP assignments formatted nicely
	 * 
	 * @param assignments
	 */
	public void printAllSP(Set<Assignment> assignments) {
		Iterator<Assignment> it = assignments.iterator();
		while(it.hasNext()) {
			Assignment ass = (Assignment) it.next();
			
			if(ass.getType().equals(AssignmentTypes.PATIENT))
				System.out.println(debugSP(ass));
		}
		
	}
	
	/**
	 * Print information of a single student assignment in a readable format
	 * @param ass
	 * @return
	 */
	private String debugStudent(Assignment ass) {
		String room = "none";
		OscePostBlueprint postBP = null;
		if(ass.getOscePostRoom() != null) {
			room = ass.getOscePostRoom().getRoom().getRoomNumber();
			postBP = ass.getOscePostRoom().getOscePost().getOscePostBlueprint();
		}
		
		return "\t student "+ass.getSequenceNumber()+" (room: "+room+", "+debugTimeStartEnd(ass)+") inserted..." + (postBP != null && postBP.equals(PostType.ANAMNESIS_THERAPY) ? "first: " + postBP.getIsFirstPart() : "");
	}
	
	/**
	 * Print information of a single SP assignment in a readable format
	 * @param ass
	 * @return
	 */
	private String debugSP(Assignment ass) {
		String room = "none";
		if(ass.getOscePostRoom() != null) {
			room = ass.getOscePostRoom().getRoom().getRoomNumber();
		}
		return "(room: "+room+", "+debugTimeStartEnd(ass)+") inserted...";
	}
	
	/**
	 * Print debug information on assignment (namely start- and end-time consisting only of HH:mm)
	 * @param ass
	 * @return
	 */
	private String debugTimeStartEnd(Assignment ass) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		String timeString = format.format(ass.getTimeStart()) + " - " + format.format(ass.getTimeEnd());
		return timeString;
	}
}
