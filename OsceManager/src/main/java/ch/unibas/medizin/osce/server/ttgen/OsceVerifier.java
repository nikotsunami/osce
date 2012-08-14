package ch.unibas.medizin.osce.server.ttgen;

import ch.unibas.medizin.osce.domain.Osce;

/**
 * Check OSCE for valid information set needed to optimize
 * 
 * @author dk
 *
 */
public class OsceVerifier {

	public static void verifyOsce(Osce osce) throws Exception {
		if(osce.getMaxNumberStudents() == null || osce.getMaxNumberStudents() <= 0)
			throw new OsceParamException("maximum number of students");
		
		if(osce.getNumberRooms() == null || osce.getNumberRooms() <= 0)
			throw new OsceParamException("number of rooms available");
		
		if(osce.getPostLength() == null || osce.getPostLength() <= 0)
			throw new OsceParamException("post length");
		
		if(osce.getShortBreak() == null || osce.getShortBreak() <= 0)
			throw new OsceParamException("duration of short break (after a post)");
		
		if(osce.getShortBreakSimpatChange() == null || osce.getShortBreakSimpatChange() <= 0)
			throw new OsceParamException("duration of simpat change break (when a change of simpat is needed WITHIN rotation)");
		
		if(osce.getMiddleBreak() == null || osce.getMiddleBreak() <= 0)
			throw new OsceParamException("duration of middle break (after a rotation)");
		
		if(osce.getLongBreak() == null || osce.getLongBreak() <= 0)
			throw new OsceParamException("duration of long break (when a change of simpat is needed AFTER rotation)");
		
		if(osce.getLunchBreak() == null || osce.getLunchBreak() <= 0)
			throw new OsceParamException("duration of lunch break");
		
		if(!(osce.getOsce_days().size() > 0))
			throw new OsceParamException("no OsceDay for this Osce defined");
	}
}
