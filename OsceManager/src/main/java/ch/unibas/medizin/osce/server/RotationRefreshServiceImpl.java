/**
 * 
 */
package ch.unibas.medizin.osce.server;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.client.RotationRefreshService;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.server.ttgen.TimetableGenerator;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author rahul
 *
 */
@SuppressWarnings("serial")
public class RotationRefreshServiceImpl extends RemoteServiceServlet implements RotationRefreshService
{	
	private static Logger Log = Logger.getLogger(RotationRefreshServiceImpl.class);
	
	public RotationRefreshServiceImpl() 
	{
		super();
		Log.info("RoomRefreshServiceImpl Call");
	}

	@Override
	public Map<String, String> schedulePostpone(Long osceDayId) 
	{
		Log.info("~~SchedulePostPone.");			
		Log.info("~~Osce Day Id: " + osceDayId);
		
		OsceDay osceDayProxy=OsceDay.findOsceDay(osceDayId);
		
		//Map<String, String> mapForSchedulePostpone=new HashMap<String, String>();
		
		Long osceId=OsceDay.findOsceIdByOsceDayId(osceDayProxy.getId());
		//Log.info("Osce Id: " + osceId);

		// Get Last OsceDay to Check weather add New Day or Update rotation in Existing Days
		OsceDay lastOsceDay=OsceDay.findLastOsceDayByOsce(osceId);
		if(osceDayProxy.equals(lastOsceDay))
		{
			Log.info("~~~~Last Day Clicked................Add New Osce Day");
			// If Schedule Postpone for Last Day then Add New Osce Day [Mean it will Scheduled to Next Day.]
			Map<String, String> mapForSchedulePostpone=createNewOsceDayOsceSequenceAndCourse(osceDayProxy);
			return mapForSchedulePostpone;
			
		}
		else
		{
			Log.info("~~~~ Update Rotation................");
			Log.info("Osce Day Proxy: " + osceDayProxy.getId());
			Map<String, String> mapForSchedulePostpone=updateNextRotation(osceDayProxy);
			return mapForSchedulePostpone;
		}		
	}
	
	static Map<String, String> mapUpdateNextRotation=new HashMap<String, String>();
	private static Map<String, String> updateNextRotation(OsceDay osceDayProxy) 
	{
		Long osceId=OsceDay.findOsceIdByOsceDayId(osceDayProxy.getId());
		List<OsceDay> osceDays=OsceDay.findOsceDayByOsce(osceId);
		Log.info("~~Total "+ osceDays.size()+" OsceDay for Osce " + osceId);
		Iterator<OsceDay> osceDayIterator=osceDays.iterator();
		OsceDay nextOsceDay = null;
		boolean flagUpdateExistingNumberOfRotation=false;
		while(osceDayIterator.hasNext())
		{
			OsceDay osceDay=osceDayIterator.next();
			if(osceDay.equals(osceDayProxy))
			{
				Log.info("Update Rotation for OsceDay" + osceDay.getId());
				List<OsceSequence> osceSequences=OsceSequence.findOsceSequenceByOsceDay(osceDay.getId());
				if(osceSequences.size()>0)
				{				
					// Sequence Iterate only if the Existing Sequence Number of Rotation is not Updated (Minus 1)	
					if(flagUpdateExistingNumberOfRotation==false)
					{							
						Iterator<OsceSequence> osceSequenceIterator=osceSequences.iterator();
						OsceSequence osceSequence=null;		
						while(osceSequenceIterator.hasNext())
						{							
							osceSequence=osceSequenceIterator.next();																		
						}
						if(osceSequence.getNumberRotation()==1)
						{
							Log.info("No Postpone is Allowed");											
							mapUpdateNextRotation.put("Message","Rotation1");
							return mapUpdateNextRotation;
						}
						else
						{
							osceSequence.setNumberRotation((osceSequence.getNumberRotation()-1));
							osceSequence.persist();
							mapUpdateNextRotation.put("CurrentSequenceId", ""+osceSequence.getId());
							mapUpdateNextRotation.put("CurrentSequenceRotation",""+(osceSequence.getNumberRotation()));
							flagUpdateExistingNumberOfRotation=true;
						}
					}
				}
			}
			else if(flagUpdateExistingNumberOfRotation==true)
			{
				osceDay.getOsceSequences().get(0).setNumberRotation((osceDay.getOsceSequences().get(0).getNumberRotation()+1));
				osceDay.getOsceSequences().get(0).persist();
				mapUpdateNextRotation.put("NextSequenceId", ""+osceDay.getOsceSequences().get(0).getId());
				mapUpdateNextRotation.put("NextSequenceRotation",""+(osceDay.getOsceSequences().get(0).getNumberRotation()));
				nextOsceDay = osceDay;
				break;
			}
		}
		try{			
			if(nextOsceDay !=null )
			{
				mapUpdateNextRotation.put("CurrentDayId", ""+osceDayProxy.getId());
				mapUpdateNextRotation.put("PreviousDayId", ""+nextOsceDay.getId());
				updateTimesAfterRotationShift(osceDayProxy.getId(),nextOsceDay.getId());
			}
				
				
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		mapUpdateNextRotation.put("Message",  "UpdateSuccessful");
		return mapUpdateNextRotation;
	}
	
	static HashMap<String, String> createNewOsceDaySequenceAndCourseMap=new HashMap<String, String>();
	private static HashMap<String, String> createNewOsceDayOsceSequenceAndCourse(OsceDay osceDayProxy) 
	{
		//List<OsceDay> osceDays=OsceDay.findOsceDayByOsce(osceId);
		//Log.info("~~Total "+ osceDays.size()+" OsceDay for Osce " + osceId);
		//if(osceDays.size()>0)
		//{						
			//Iterator<OsceDay> osceDayIterator=osceDays.iterator();
			//while(osceDayIterator.hasNext())
			//{
				//OsceDay osceDay=osceDayIterator.next();
				
				List<OsceSequence> osceSequences=OsceSequence.findOsceSequenceByOsceDay(osceDayProxy.getId());
				Log.info("~~Total "+ osceSequences.size() + " Osce Sequence for Osce Day" + osceDayProxy.getId());
				OsceSequence newOsceSequence;
				OscePost newOscePost;
				Course newCourse;
				if(osceSequences.size()>0)
				{				
						Log.info("New Osce Day Add");						
						Iterator<OsceSequence> osceSequenceIterator=osceSequences.iterator();
						OsceSequence osceSequence=null;
						while(osceSequenceIterator.hasNext())
						{
							osceSequence=osceSequenceIterator.next();
						}
						if(osceSequence.getNumberRotation()==1)
						{
							Log.info("No Postpone is Allowed");							
							//return "Rotation1";
							createNewOsceDaySequenceAndCourseMap.put("Message","Rotation1");
							return createNewOsceDaySequenceAndCourseMap;
						}
						osceSequence.setNumberRotation((osceSequence.getNumberRotation()-1));
						
						OsceDay newOsceday=new OsceDay();
						newOsceday.setLunchBreakStart(osceDayProxy.getLunchBreakStart());
						Calendar cal = Calendar.getInstance();
						cal.setTime(osceDayProxy.getOsceDate());
						cal.add(Calendar.DAY_OF_MONTH, 1);
						newOsceday.setOsceDate(cal.getTime());
						newOsceday.setTimeEnd(osceDayProxy.getTimeEnd());
						newOsceday.setTimeStart(osceDayProxy.getTimeStart());
						newOsceday.setValue(osceDayProxy.getValue());
						newOsceday.setOsce(osceDayProxy.getOsce());
						newOsceday.setLunchBreakAfterRotation(osceDayProxy.getLunchBreakAfterRotation());
						newOsceday.persist();
						
						newOsceSequence=new OsceSequence();
						newOsceSequence.setLabel(osceSequence.getLabel());
						newOsceSequence.setNumberRotation(1);
						newOsceSequence.setOsceDay(newOsceday);
						newOsceSequence.persist();
						
						List<OscePost> oscePosts=OscePost.findOscePostByOsceSequence(osceSequence.getId());
						if(oscePosts.size()>0)
						{
							Iterator<OscePost> oscePostIterator=oscePosts.iterator();
							while(oscePostIterator.hasNext())
							{
								OscePost oscePost=oscePostIterator.next();
								newOscePost=new OscePost();
								newOscePost.setSequenceNumber(oscePost.getSequenceNumber());
								newOscePost.setOscePostBlueprint(oscePost.getOscePostBlueprint());
								newOscePost.setOsceSequence(newOsceSequence);
								//newOscePost.setStandardizedRole(oscePost.getStandardizedRole());
								newOscePost.persist();								
							}
						}
						
						List<Course> courses=Course.findCourseByOsceSequence(osceSequence.getId());
						if(courses.size()>0)
						{
							Iterator<Course> courseIterator=courses.iterator();
							while(courseIterator.hasNext())
							{
								Course course=courseIterator.next();
								newCourse=new Course();
								newCourse.setColor(course.getColor());
								newCourse.setOsce(course.getOsce());
								newCourse.setOsceSequence(newOsceSequence);
								newCourse.persist();								
							}
						}		
						
						// Add OscePostRoom
						List<OscePost> oldOscePosts=OscePost.findOscePostByOsceSequence(osceSequence.getId());
						List<Course> oldCourses=Course.findCourseByOsceSequence(osceSequence.getId());
						
						List<OscePost> oscePostsListforOscePostRoom=OscePost.findOscePostByOsceSequence(newOsceSequence.getId());
						List<Course> coursesforOscePostRoom=Course.findCourseByOsceSequence(newOsceSequence.getId());
						
						if(oscePostsListforOscePostRoom.size()>0 && coursesforOscePostRoom.size()>0)							
						{
							Iterator<Course> courseIterator=coursesforOscePostRoom.iterator();
							Iterator<Course> oldCourseIterator=oldCourses.iterator();
							
							while(courseIterator.hasNext() && oldCourseIterator.hasNext())
							{
								Iterator<OscePost> oscePostIterator=oscePostsListforOscePostRoom.iterator();
								Iterator<OscePost> oldOscePostIterator=oldOscePosts.iterator();
								
								Course course=courseIterator.next();
								Course oldCourse=oldCourseIterator.next();
								
								while(oscePostIterator.hasNext() && oldOscePostIterator.hasNext())
								{
									OscePost oscePost=oscePostIterator.next();
									OscePost oldOscePost=oldOscePostIterator.next();
									
									OscePostRoom opr=OscePostRoom.findOscePostRoomByOscePostAndCourse(oldCourse, oldOscePost);
																		
									System.out.println("Course: " + course + "Post: " + oscePost);
									OscePostRoom newOscePostRoom=new OscePostRoom();
									newOscePostRoom.setCourse(course);
									newOscePostRoom.setOscePost(oscePost);
									if(opr!=null)
									{
										newOscePostRoom.setRoom(opr.getRoom());
									}
									newOscePostRoom.persist();
								}
							}
						}
						
						try{					
							createNewOsceDaySequenceAndCourseMap.put("CurrentDayId", ""+osceDayProxy.getId());
							createNewOsceDaySequenceAndCourseMap.put("PreviousDayId", ""+newOsceday.getId());							
							updateTimesAfterRotationShift(osceDayProxy.getId(),newOsceday.getId());
							
						}catch(Exception e)
						{
							e.printStackTrace();
						}					
				}
				
				
			//}
		//}
				createNewOsceDaySequenceAndCourseMap.put("Message","CreateSuccessful");
				return createNewOsceDaySequenceAndCourseMap;
				//return "CreateSuccessful";
	}
	public static Boolean updateTimesAfterRotationShift(Long osceDayIdFrom, Long osceDayIdTo) {
    	try {    		
    		OsceDay osceDay = OsceDay.findOsceDay(osceDayIdFrom);
    		TimetableGenerator optGen = TimetableGenerator.getOptimalSolution(osceDay.getOsce());
    	
    		//by spec[
    		if (osceDayIdTo == null)
    			optGen.updateTimesAfterRotationShiftByRemoveDay(osceDayIdFrom);
    		else
        	optGen.updateTimesAfterRotationShift(osceDayIdFrom, osceDayIdTo);
    		//by spec]
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
	
	public Map<String, String> scheduleEarlier(Long osceDayId) 
	{
		Log.info("~~scheduleEarlier.");							
		Log.info("~~Osce Day Id: " + osceDayId);
		
		OsceDay osceDayProxy=OsceDay.findOsceDay(osceDayId);
		
		Long osceId=OsceDay.findOsceIdByOsceDayId(osceDayProxy.getId());
		OsceDay firstOsceDay=OsceDay.findFirstOsceDayByOsce(osceId);
		// Get Last OsceDay to Check weather add New Day or Update rotation in Existing Days
		
		if(osceDayProxy.equals(firstOsceDay))
		{
			Log.info("~~~~First Day Clicked................Add New Osce Day");
			Log.info("No Schedule Earlier Possible");
			Map<String, String> firstDayWarningMap=new HashMap<String, String>();
			firstDayWarningMap.put("Message","FirstDay");
			return firstDayWarningMap;			
		}
		else
		{
			Log.info("~~~~ Update Rotation................");
			Log.info("Osce Day Proxy: " + osceDayProxy.getId());
			Map<String, String> updatePreviousRotationMap=new HashMap<String, String>();
			updatePreviousRotationMap=updatePreviousRotation(osceDayProxy);			
			return updatePreviousRotationMap;
		}
	}
	
	static Map<String, String> finalSuccessPreponeMap=new HashMap<String, String>();
	private static Map<String, String> updatePreviousRotation(OsceDay osceDayProxy) 
	{	
		Long osceId=OsceDay.findOsceIdByOsceDayId(osceDayProxy.getId());
		List<OsceDay> osceDays=OsceDay.findDescOsceDayByOsce(osceId);
		OsceDay currentOsceDay=new OsceDay();
		OsceDay previousOsceDay=new OsceDay();
		
		Log.info("~~Total "+ osceDays.size()+" OsceDay for Osce " + osceId);
		boolean flagUpdateExistingNumberOfRotation=false;
		for(int i=0;i<osceDays.size();i++)
		{
			OsceDay osceDay=osceDays.get(i);	
			if(osceDay.equals(osceDayProxy))
			{
				currentOsceDay=osceDay;
				List<OsceSequence> osceSequences=OsceSequence.findOsceSequenceByOsceDay(osceDay.getId());
				if(osceSequences.size()>0)
				{	
					if(osceSequences.get(0).getNumberRotation()==1)
					{
						if(osceSequences.size()>1)
						{
							// More Than one Osce So No Need To Remove  Day
							Log.info("More Than Two Sequence");
							//Map<String, String> updatePreviousRotationMap=new HashMap<String, String>();
							finalSuccessPreponeMap.put("Message", "Rotation1");
							return finalSuccessPreponeMap;
						}
						else
						{
							// Only One Sequence So Need to Remove Corse Osce_Post and Day
							Log.info("Only One Sequence");
							OsceDay tempPreviousOsceDay=osceDays.get(i+1);
							previousOsceDay=tempPreviousOsceDay;
							
							List<OsceSequence> previousDaySequences=OsceSequence.findOsceSequenceByOsceDay(tempPreviousOsceDay.getId());							
							if(previousDaySequences.size()>0)
							{	
								Iterator<OsceSequence> osceSeqIterator=previousDaySequences.iterator();
								OsceSequence updateOsceSequence=null;
								while(osceSeqIterator.hasNext())
								{
									updateOsceSequence=osceSeqIterator.next();
								}
								updateOsceSequence.setNumberRotation((updateOsceSequence.getNumberRotation()+1));
								finalSuccessPreponeMap.put("PreviousDaySequenceId", ""+updateOsceSequence.getId());
								finalSuccessPreponeMap.put("PreviousDaySequenceRotation", ""+(updateOsceSequence.getNumberRotation()+1));
								finalSuccessPreponeMap=removeOsceSequenceCourseDay(osceSequences.get(0),osceDay,updateOsceSequence);															
								
								if(finalSuccessPreponeMap.get("Message").compareToIgnoreCase("SuccessfullyPreponeWithDelete")==0)
								{
									Log.info("Update Osce Sequence Rotation Successfully.");
									updateOsceSequence.persist();
									/*try{					
										Log.info("IF TTG Current Osce Day: " + currentOsceDay.getId());
										Log.info("IF TTG Previous Osce Day: " + previousOsceDay.getId());
										updateTimesAfterRotationShift(currentOsceDay.getId(),previousOsceDay.getId());
									}catch(Exception e)
									{
										e.printStackTrace();
									}	*/
									
									//by spec[
									System.out.println("~~~ID : " + previousOsceDay.getId());
									updateTimesAfterRotationShift(previousOsceDay.getId(), null);
									//by spec]
									
									return finalSuccessPreponeMap;
								}
								//Map<String, String> rotationOneMap=new HashMap<String, String>();
								finalSuccessPreponeMap.put("Message","Rotation1");
								return finalSuccessPreponeMap;
								//break;
							}							
						}
					}
					else
					{
						Log.info("Go To Else.");
						//More than One Sequence So Need to Update Sequence
						osceSequences.get(0).setNumberRotation((osceSequences.get(0).getNumberRotation()-1));										
						osceSequences.get(0).persist();
						finalSuccessPreponeMap.put("CurrentDaySequenceId", ""+osceSequences.get(0).getId());
						finalSuccessPreponeMap.put("CurrentDaySequenceRotation", ""+(osceSequences.get(0).getNumberRotation()));
						flagUpdateExistingNumberOfRotation=true;						
					}
				}
			}
			//If Current Day Sequence Rotation is Updated Successfully then flag is TRUE and go to update previous day sequence
			else if(flagUpdateExistingNumberOfRotation==true)
			{
				List<OsceSequence> osceSequences=OsceSequence.findOsceSequenceByOsceDay(osceDay.getId());
				previousOsceDay=osceDay;
				if(osceSequences.size()>0)
				{	
					Iterator<OsceSequence> osceSequenceIterator=osceSequences.iterator();
					OsceSequence osceSequence=null;
					while(osceSequenceIterator.hasNext())
					{						
						osceSequence=osceSequenceIterator.next();						
					}
					finalSuccessPreponeMap.put("PreviousDaySequenceId", ""+osceSequence.getId());
					finalSuccessPreponeMap.put("PreviousDaySequenceRotation", ""+(osceSequence.getNumberRotation()+1));
					
					osceSequence.setNumberRotation((osceSequence.getNumberRotation()+1));
					osceSequence.persist();					
					//Map<String, String> successPreponeMap=new HashMap<String, String>();
					finalSuccessPreponeMap.put("Message", "SuccessfulPrepond");
					try
					{					
						Log.info("ELSE TTG Current Osce Day: " + currentOsceDay.getId());
						Log.info("ELSE TTG Previous Osce Day: " + previousOsceDay.getId());
						finalSuccessPreponeMap.put("CurrentDayId", ""+currentOsceDay.getId());
						finalSuccessPreponeMap.put("PreviousDayId", ""+previousOsceDay.getId());
						
						updateTimesAfterRotationShift(currentOsceDay.getId(),previousOsceDay.getId());
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}	
					return finalSuccessPreponeMap;
					//break;
				}
			}
		}	
		
		/*try{					
			Log.info("1TTG Current Osce Day: " + currentOsceDay.getId());
			Log.info("1TTG Previous Osce Day: " + previousOsceDay.getId());
			updateTimesAfterRotationShift(currentOsceDay.getId(),previousOsceDay.getId());
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
		*/
		finalSuccessPreponeMap.put("Message", "SuccessfulPrepond");
		return finalSuccessPreponeMap;
		
	}
	private static Map<String, String> removeOsceSequenceCourseDay(OsceSequence osceSequence,OsceDay osceDay,OsceSequence updateOsceSequence) 
	{
		Log.info("Call removeOsceSequenceCourseDay for Sequence" +osceSequence.getId());			
		try{
			osceDay.remove();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> removeSequenceMap=new HashMap<String, String>();
		removeSequenceMap.put("Message", "SuccessfullyPreponeWithDelete");
		return removeSequenceMap;
	}		
}
