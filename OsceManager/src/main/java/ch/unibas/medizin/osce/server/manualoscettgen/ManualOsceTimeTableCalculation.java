package ch.unibas.medizin.osce.server.manualoscettgen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ch.unibas.medizin.osce.domain.Assignment;
import ch.unibas.medizin.osce.domain.Course;
import ch.unibas.medizin.osce.domain.Osce;
import ch.unibas.medizin.osce.domain.OsceDay;
import ch.unibas.medizin.osce.domain.OsceDayRotation;
import ch.unibas.medizin.osce.domain.OscePost;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import ch.unibas.medizin.osce.domain.OsceSequence;
import ch.unibas.medizin.osce.shared.BreakType;
import ch.unibas.medizin.osce.shared.PostType;
import ch.unibas.medizin.osce.shared.RoleTypes;


public class ManualOsceTimeTableCalculation {
	
	private static Logger Log = Logger.getLogger(ManualOsceTimeTableCalculation.class);
	private static int LONG_BREAK_MIDDLE_THRESHOLD = 150;
	private static int LUNCH_BREAK_MIDDLE_THRESHOLD = 360;
	private final Osce osce;
	Calendar calendar;
	
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//Manage sequence wise
	Map<Long, Map<Integer, RotationData>> sequenceMap = new HashMap<Long, Map<Integer,RotationData>>();
	
	//Manage Osce day wise
	Map<Integer, Integer> logicalBreakMap = new HashMap<Integer, Integer>();
	
	//Managet OsceDay wise Student Count
	Map<Long, Integer> studentOsceDayMap = new HashMap<Long, Integer>();
	
	public ManualOsceTimeTableCalculation(Osce osce) {
		this.osce = osce;
		
		if (osce.getLongBreakRequiredTime() != null)
			LONG_BREAK_MIDDLE_THRESHOLD = osce.getLongBreakRequiredTime();
		
		if (osce.getLunchBreakRequiredTime() != null)
			LUNCH_BREAK_MIDDLE_THRESHOLD = osce.getLunchBreakRequiredTime();
		
		calendar = Calendar.getInstance();
	}
	
	public void calculateTime2()
	{
		List<OsceDay> osceDayList = osce.getOsce_days();
		for (OsceDay osceDay : osceDayList)
		{
			int totalStudentInDay = 0;
			Date timeStart = osceDay.getTimeStart();			
				
			List<OsceSequence> osceSeqList = osceDay.getOsceSequences();
			
			if (osceSeqList.size() == 1)
			{
				Date startTime = timeStart;
				int breakValue = 0;
				OsceSequence osceSeq = osceSeqList.get(0);
				
				Map<Integer, RotationData> rotationMap = new HashMap<Integer, RotationData>();
				Map<Integer, Integer> longBreakMap = new HashMap<Integer, Integer>();
				
				int noOfLongBreak = 0;
				int numberOfRotation = 0;
				int totalTimeForLongBreak = 0;
				int totalTimeForLunchBreak = 0;
				int totalStudentInOneRotation = 0;
				int lunchBreakRotation = -1;
				
				List<Course> courseList = osceSeq.getCourses();
				
				for (Course course : courseList)
				{
					totalStudentInOneRotation += course.getOscePostRooms().size();
				}
				
				int maxPost = osceSeq.getOscePosts().size();
				int totalTimeForOneRotation = ((maxPost * osce.getPostLength()) + ((maxPost - 1) * osce.getShortBreak()));
				
				for (int i=0; i<osceSeq.getNumberRotation(); i++)
				{
					totalStudentInDay += totalStudentInOneRotation;
					Date rotationEndTime = dateAddMin(startTime, totalTimeForOneRotation);
					
					RotationData rotationData = null;
					totalTimeForLongBreak += totalTimeForOneRotation;
					totalTimeForLunchBreak += totalTimeForOneRotation;
					numberOfRotation += 1;
					
					if (rotationData == null && totalTimeForLunchBreak > LUNCH_BREAK_MIDDLE_THRESHOLD)
					{
						breakValue = osce.getLunchBreak().intValue();
						rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
						totalTimeForLunchBreak = 0;
						totalTimeForLongBreak = 0;
						lunchBreakRotation = i;
					}
					
					if (rotationData == null && totalTimeForLongBreak > LONG_BREAK_MIDDLE_THRESHOLD)
					{
						breakValue = osce.getLongBreak().intValue();
						rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
						noOfLongBreak += 1;
						longBreakMap.put(noOfLongBreak, numberOfRotation);
						totalTimeForLongBreak = 0;
						if (osceSeq.getNumberRotation() == 1)
							totalTimeForLunchBreak = 0;
					}
					
					if (rotationData == null)
					{
						breakValue =  osce.getMiddleBreak().intValue();
						rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
					}
					
					rotationMap.put(numberOfRotation, rotationData);
					startTime = rotationEndTime;
					startTime = dateAddMin(startTime, breakValue);
				}	
				
			
				if (noOfLongBreak > 1)
				{ 
					if (lunchBreakRotation < 1)
						lunchBreakRotation = numberOfRotation;
					
					int index = 1;
					
					while (noOfLongBreak > 0)
					{
						int middleRot = 0;
						if (index == 1)
							middleRot = lunchBreakRotation / 2;
						else
							middleRot += lunchBreakRotation;
						
						Integer longBreakRotation = longBreakMap.get(index);
						
						if (middleRot > 0 && middleRot != longBreakRotation)
						{
							RotationData longBreakRotData = rotationMap.get(longBreakRotation);
							longBreakRotData.setBreakValue(osce.getMiddleBreak().intValue());
							rotationMap.put(longBreakRotation, longBreakRotData);
							int diff = osce.getLongBreak().intValue() - osce.getMiddleBreak().intValue();
							
							for (int i=(longBreakRotation+1); i<=numberOfRotation; i++)
							{
								RotationData rotationData = rotationMap.get(i);
								
								Date stTime = rotationData.getStartTime();
								Date endTime = rotationData.getEndTime();
								
								stTime = dateSubtractMin(stTime, diff);
								endTime = dateSubtractMin(endTime, diff);
								
								rotationData.setStartTime(stTime);
								rotationData.setEndTime(endTime);
								
								rotationMap.put(i, rotationData);
							}
							
							RotationData longBreakData = rotationMap.get(middleRot);
							longBreakData.setBreakValue(osce.getLongBreak().intValue());
							rotationMap.put(middleRot, longBreakData);
							
							for (int i=(middleRot+1); i<=numberOfRotation; i++)
							{
								RotationData rotationData = rotationMap.get(i);
								
								Date stTime = rotationData.getStartTime();
								Date endTime = rotationData.getEndTime();
								
								stTime = dateAddMin(stTime, diff);
								endTime = dateAddMin(endTime, diff);
								
								rotationData.setStartTime(stTime);
								rotationData.setEndTime(endTime);
								
								rotationMap.put(i, rotationData);
							}
						}
						
						index += 1;
						noOfLongBreak -= 1;
					}
				}
				
				sequenceMap.put(osceSeq.getId(), rotationMap);
			}
			else if (osceSeqList.size() == 2)
			{
				Date startTime = timeStart;
				int breakValue = -1;
				
				for (int seqIndex = 0; seqIndex<osceSeqList.size(); seqIndex++)
				{	
					OsceSequence osceSeq = osceSeqList.get(seqIndex);
					Map<Integer, RotationData> rotationMap = new HashMap<Integer, RotationData>();
					Map<Integer, Integer> longBreakMap = new HashMap<Integer, Integer>();
					
					int noOfLongBreak = 0;
					int numberOfRotation = 0;
					int totalTimeForSequence = 0;
					int totalStudentInOneRotation = 0;
					
					List<Course> courseList = osceSeq.getCourses();
					for (Course course : courseList)
					{
						totalStudentInOneRotation += course.getOscePostRooms().size();
					}
					
					int maxPost = osceSeq.getOscePosts().size();
					int totalTimeForOneRotation = ((maxPost * osce.getPostLength()) + ((maxPost - 1) * osce.getShortBreak()));
					
					for (int i=0; i<osceSeq.getNumberRotation(); i++)
					{
						totalStudentInDay += totalStudentInOneRotation;
						Date rotationEndTime = dateAddMin(startTime, totalTimeForOneRotation);
						
						RotationData rotationData = null;
						totalTimeForSequence += totalTimeForOneRotation;
						numberOfRotation += 1;
						
						if (rotationData == null && seqIndex == 0 && i == (osceSeq.getNumberRotation() - 1))
						{	
							breakValue = osce.getLunchBreak().intValue();
							
							if (osceSeq.getNumberRotation() == 1 && totalTimeForSequence > LONG_BREAK_MIDDLE_THRESHOLD)
							{
								rotationEndTime = dateSubtractMin(rotationEndTime, osce.getShortBreak().intValue());
								rotationEndTime = dateAddMin(rotationEndTime, osce.getLongBreak().intValue());
								rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
							}
							else
								rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
						}
						
						if (rotationData == null && rotationData == null && totalTimeForSequence > LONG_BREAK_MIDDLE_THRESHOLD)
						{
							breakValue = osce.getLongBreak().intValue();
							
							if (osceSeq.getNumberRotation() == 1)
							{
								rotationEndTime = dateSubtractMin(rotationEndTime, osce.getShortBreak().intValue());
								rotationEndTime = dateAddMin(rotationEndTime, osce.getLongBreak().intValue());
								rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
							}
							else
								rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
							
							noOfLongBreak += 1;
							longBreakMap.put(noOfLongBreak, numberOfRotation);
							totalTimeForSequence = 0;
						}
						
						if (rotationData == null)
						{
							breakValue =  osce.getMiddleBreak().intValue();
							rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
						}
						
						rotationMap.put(numberOfRotation, rotationData);
						
						startTime = rotationEndTime;
						startTime = dateAddMin(startTime, breakValue);
					}
					
					
					if (noOfLongBreak > 1)
					{
						int breakCount = noOfLongBreak + 1;
						int middleRot = numberOfRotation / breakCount;
						int longBreakIndex = 0;
						int longBreakRot = 0;
						
						while (noOfLongBreak > 0)
						{
							noOfLongBreak -= 1;
							longBreakRot += middleRot;
							longBreakIndex += 1;
							Integer longBreakRotation = longBreakMap.get(longBreakIndex);
						
							if (longBreakRot != longBreakRotation)
							{
								RotationData longBreakRotData = rotationMap.get(longBreakRotation);
								longBreakRotData.setBreakValue(osce.getMiddleBreak().intValue());
								rotationMap.put(longBreakRotation, longBreakRotData);
								int diff = osce.getLongBreak().intValue() - osce.getMiddleBreak().intValue();
								
								for (int i=(longBreakRotation+1); i<=numberOfRotation; i++)
								{
									RotationData rotationData = rotationMap.get(i);
									
									Date stTime = rotationData.getStartTime();
									Date endTime = rotationData.getEndTime();
									
									stTime = dateSubtractMin(stTime, diff);										
									endTime = dateSubtractMin(endTime, diff);
									
									rotationData.setStartTime(stTime);
									rotationData.setEndTime(endTime);
									
									rotationMap.put(i, rotationData);
								}
								
								RotationData longBreakData = rotationMap.get(longBreakRot);
								longBreakData.setBreakValue(osce.getLongBreak().intValue());
								rotationMap.put(longBreakRot, longBreakData);
								
								for (int i=(longBreakRot+1); i<=numberOfRotation; i++)
								{
									RotationData rotationData = rotationMap.get(i);
									
									Date stTime = rotationData.getStartTime();
									Date endTime = rotationData.getEndTime();
									
									stTime = dateAddMin(stTime, diff);
									endTime = dateAddMin(endTime, diff);
									
									rotationData.setStartTime(stTime);
									rotationData.setEndTime(endTime);
									
									rotationMap.put(i, rotationData);
								}
							}
						}
					}
					
					sequenceMap.put(osceSeq.getId(), rotationMap);					
				}
			}
			
			studentOsceDayMap.put(osceDay.getId(), totalStudentInDay);
		}
	}
	
	/*public void calculateTime(){
		List<OsceDay> osceDayList = osce.getOsce_days();
		
		int totalRemainingStudent = totalStudent;
		int assRotNumber = 0;
		
		for (OsceDay osceDay : osceDayList)
		{
			Date timeStart = osceDay.getTimeStart();
			
			calendar.setTime(timeStart);
			calendar.set(Calendar.HOUR_OF_DAY, 12);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date predictStartLunchTime = calendar.getTime();
			
			calendar.setTime(timeStart);
			calendar.set(Calendar.HOUR_OF_DAY, 13);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date predictEndLunchTime = calendar.getTime();
			
			calendar.setTime(timeStart);
			calendar.set(Calendar.HOUR_OF_DAY, 17);
			calendar.set(Calendar.MINUTE, 30);
			calendar.set(Calendar.SECOND, 0);
			Date predicateDayEndTime = calendar.getTime();
			
			List<OsceSequence> osceSeqList = osceDay.getOsceSequences();
			if (osceSeqList.size() == 1)
			{
				Date startTime = timeStart;
				int totalRotation = 0;
				int breakValue = 0;
				OsceSequence osceSeq = osceSeqList.get(0);
				
				Map<Integer, RotationData> rotationMap = new HashMap<Integer, RotationData>();
				Map<Integer, Integer> longBreakMap = new HashMap<Integer, Integer>();
				
				int noOfLongBreak = 0;
				int numberOfRotation = 0;
				int totalTimeForLongBreak = 0;
				int totalTimeForLunchBreak = 0;
				int totalStudentInOneRotation = 0;
				int lunchBreakRotation = -1;
				
				List<Course> courseList = osceSeq.getCourses();
				
				for (Course course : courseList)
				{
					totalStudentInOneRotation += course.getOscePostRooms().size();
				}
				
				int maxPost = osceSeq.getOscePosts().size();
				int totalTimeForOneRotation = ((maxPost * osce.getPostLength()) + ((maxPost - 1) * osce.getShortBreak()));
				
				for (int i=0; i<MAX_ROTATION; i++)
				{
					Date rotationEndTime = dateAddMin(startTime, totalTimeForOneRotation);
					
					RotationData rotationData = null;
					totalTimeForLongBreak += totalTimeForOneRotation;
					totalTimeForLunchBreak += totalTimeForOneRotation;
					numberOfRotation += 1;
					totalRotation += 1;
					
					if (rotationData == null && totalTimeForLunchBreak > LUNCH_BREAK_MIDDLE_THRESHOLD)
					{
						breakValue = osce.getLunchBreak().intValue();
						rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
						totalTimeForLunchBreak = 0;
						totalTimeForLongBreak = 0;
						lunchBreakRotation = i;
					}
					
					if (rotationData == null && totalTimeForLongBreak > LONG_BREAK_MIDDLE_THRESHOLD)
					{
						breakValue = osce.getLongBreak().intValue();
						rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
						noOfLongBreak += 1;
						longBreakMap.put(noOfLongBreak, numberOfRotation);
						totalTimeForLongBreak = 0;
					}
					
					if (rotationData == null)
					{
						breakValue =  osce.getMiddleBreak().intValue();
						rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
					}
					
					rotationMap.put(numberOfRotation, rotationData);
					startTime = rotationEndTime;
					startTime = dateAddMin(startTime, breakValue);
					
					totalRemainingStudent = totalRemainingStudent - totalStudentInOneRotation;
					
					if (totalRemainingStudent < totalStudentInOneRotation)
					{
						int lastRotation = totalRotation;
						while (totalRemainingStudent > 0)
						{
							if (totalRemainingStudent < courseList.size())
							{
								logicalBreakMap.put(assRotNumber, totalRemainingStudent);
								totalRemainingStudent = 0;
							}
							else
							{
								logicalBreakMap.put(assRotNumber, courseList.size());
								totalRemainingStudent = totalRemainingStudent - courseList.size();
							}
							assRotNumber = assRotNumber - 1;
						}
					}
					
					assRotNumber = assRotNumber + 1;
					
					if (totalRemainingStudent <= 0 || isAfter(rotationEndTime, predicateDayEndTime))
					{
						break;
					}
				}	
				
				List<Integer> keySet = new ArrayList<Integer>(new TreeSet<Integer>(logicalBreakMap.keySet()));
				int timeForOnePost = osce.getPostLength().intValue() + osce.getShortBreak().intValue();
				int timeToAdd = 0;
				for (int i=0; i<keySet.size(); i++)
				{
					int key = keySet.get(i);
					Integer noOfLogicalBreak = logicalBreakMap.get(key);
					
					if (rotationMap.containsKey(key))
					{
						if (noOfLogicalBreak == courseList.size())
						{
							RotationData rotationData = rotationMap.get(key);
							
							rotationData = updateRotationTime(rotationData, timeToAdd, timeForOnePost);
							
							rotationMap.put(key, rotationData);
							
							timeToAdd = timeToAdd + timeForOnePost;
						}
						else
						{
							boolean updateTimeFlag = false;
							for (int j=0; j<noOfLogicalBreak; j++)
							{
								if (courseList.size() > j)
								{
									Course course = courseList.get(j);
									int noOfPostInCourse = course.getOscePostRooms().size();
									if (noOfPostInCourse == maxPost)
									{
										updateTimeFlag = true;
										break;
									}
								}
							}
							
							if (updateTimeFlag)
							{
								RotationData rotationData = rotationMap.get(key);
							
								rotationData = updateRotationTime(rotationData, timeToAdd, timeForOnePost);

								rotationMap.put(key, rotationData);
								
								timeToAdd = timeToAdd + timeForOnePost;
							}
						}
						
					}	
				}		
				
				if (noOfLongBreak > 0)
				{ 
					if (lunchBreakRotation < 1)
						lunchBreakRotation = numberOfRotation;
					
					int index = 1;
					
					while (noOfLongBreak > 0)
					{
						int middleRot = 0;
						if (index == 1)
							middleRot = lunchBreakRotation / 2;
						else
							middleRot += lunchBreakRotation;
						
						Integer longBreakRotation = longBreakMap.get(index);
						
						if (middleRot > 0 && middleRot != longBreakRotation)
						{
							RotationData longBreakRotData = rotationMap.get(longBreakRotation);
							longBreakRotData.setBreakValue(osce.getMiddleBreak().intValue());
							rotationMap.put(longBreakRotation, longBreakRotData);
							int diff = osce.getLongBreak().intValue() - osce.getMiddleBreak().intValue();
							
							for (int i=(longBreakRotation+1); i<=numberOfRotation; i++)
							{
								RotationData rotationData = rotationMap.get(i);
								
								Date stTime = rotationData.getStartTime();
								Date endTime = rotationData.getEndTime();
								
								stTime = dateSubtractMin(stTime, diff);
								endTime = dateSubtractMin(endTime, diff);
								
								rotationData.setStartTime(stTime);
								rotationData.setEndTime(endTime);
								
								rotationMap.put(i, rotationData);
							}
							
							RotationData longBreakData = rotationMap.get(middleRot);
							longBreakData.setBreakValue(osce.getLongBreak().intValue());
							rotationMap.put(middleRot, longBreakData);
							
							for (int i=(middleRot+1); i<=numberOfRotation; i++)
							{
								RotationData rotationData = rotationMap.get(i);
								
								Date stTime = rotationData.getStartTime();
								Date endTime = rotationData.getEndTime();
								
								stTime = dateAddMin(stTime, diff);
								endTime = dateAddMin(endTime, diff);
								
								rotationData.setStartTime(stTime);
								rotationData.setEndTime(endTime);
								
								rotationMap.put(i, rotationData);
							}
						}
						
						index += 1;
						noOfLongBreak -= 1;
					}
				}
				
				sequenceMap.put(osceSeq.getId(), rotationMap);
			}
			else if (osceSeqList.size() == 2)
			{
				Date startTime = timeStart;
				int totalRotation = 0;
				int breakValue = 0;
				boolean lunchBreakFlag = false;
				for (OsceSequence osceSeq : osceDay.getOsceSequences())
				{
					if (totalRemainingStudent > 0)
					{
						Map<Integer, RotationData> rotationMap = new HashMap<Integer, RotationData>();
						Map<Integer, Integer> longBreakMap = new HashMap<Integer, Integer>();
						
						int noOfLongBreak = 0;
						int numberOfRotation = 0;
						int totalTimeForSequence = 0;
						int totalStudentInOneRotation = 0;
						
						List<Course> courseList = osceSeq.getCourses();
						
						for (Course course : courseList)
						{
							totalStudentInOneRotation += course.getOscePostRooms().size();
						}
						
						int maxPost = osceSeq.getOscePosts().size();
						int totalTimeForOneRotation = ((maxPost * osce.getPostLength()) + ((maxPost - 1) * osce.getShortBreak()));
						
						for (int i=0; i<MAX_ROTATION; i++)
						{
							Date rotationEndTime = dateAddMin(startTime, totalTimeForOneRotation);
							
							RotationData rotationData = null;
							totalTimeForSequence += totalTimeForOneRotation;
							numberOfRotation += 1;
							totalRotation += 1;
							
							if (rotationData == null && isBetween(rotationEndTime, predictStartLunchTime, predictEndLunchTime))
							{	
								breakValue = osce.getLunchBreak().intValue();
								rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
								lunchBreakFlag = true;								
							}
							
							if (rotationData == null && rotationData == null && totalTimeForSequence > LONG_BREAK_MIDDLE_THRESHOLD)
							{
								breakValue = osce.getLongBreak().intValue();
								rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
								noOfLongBreak += 1;
								longBreakMap.put(noOfLongBreak, numberOfRotation);
								totalTimeForSequence = 0;
							}
							
							if (rotationData == null)
							{
								breakValue =  osce.getMiddleBreak().intValue();
								rotationData = new RotationData(numberOfRotation, startTime, breakValue, rotationEndTime);
							}
							
							rotationMap.put(numberOfRotation, rotationData);
							
							startTime = rotationEndTime;
							startTime = dateAddMin(startTime, breakValue);
							
							totalRemainingStudent = totalRemainingStudent - totalStudentInOneRotation;
							
							if (totalRemainingStudent < totalStudentInOneRotation)
							{
								int lastRotation = totalRotation;
								while (totalRemainingStudent > 0)
								{
									if (totalRemainingStudent < courseList.size())
									{
										logicalBreakMap.put(assRotNumber, totalRemainingStudent);
										totalRemainingStudent = 0;
									}
									else
									{
										logicalBreakMap.put(assRotNumber, courseList.size());
										totalRemainingStudent = totalRemainingStudent - courseList.size();
									}
									assRotNumber = assRotNumber - 1;
								}
							}
							
							assRotNumber = assRotNumber + 1;
							
							if (lunchBreakFlag || totalRemainingStudent <= 0)
							{
								lunchBreakFlag = false;
								break;
							}
						}
						
						List<Integer> keySet = new ArrayList<Integer>(new TreeSet<Integer>(logicalBreakMap.keySet()));
						int timeForOnePost = osce.getPostLength().intValue() + osce.getShortBreak().intValue();
						int timeToAdd = 0;
						for (int i=0; i<keySet.size(); i++)
						{		
							int key = keySet.get(i);
							Integer noOfLogicalBreak = logicalBreakMap.get(key);
							if (numberOfRotation < totalRotation)
							{
								int rot = totalRotation - numberOfRotation;
								int rotationKey = key - rot;
								
								if (rotationMap.containsKey(rotationKey))
								{
									if (noOfLogicalBreak == courseList.size())
									{
										RotationData rotationData = rotationMap.get(rotationKey);
									
										rotationData = updateRotationTime(rotationData, timeToAdd, timeForOnePost);
										
										rotationMap.put(rotationKey, rotationData);
										
										timeToAdd = timeToAdd + timeForOnePost;
									}
									else
									{
										boolean updateTimeFlag = false;
										for (int j=0; j<noOfLogicalBreak; j++)
										{
											if (courseList.size() > j)
											{
												Course course = courseList.get(j);
												int noOfPostInCourse = course.getOscePostRooms().size();
												if (noOfPostInCourse == maxPost)
												{
													updateTimeFlag = true;
													break;
												}
											}
										}
										
										if (updateTimeFlag)
										{
											RotationData rotationData = rotationMap.get(rotationKey);
										
											rotationData = updateRotationTime(rotationData, timeToAdd, timeForOnePost);
											
											rotationMap.put(rotationKey, rotationData);
											
											timeToAdd = timeToAdd + timeForOnePost;
										}
									}
								}	
							}		
							else
							{
								if (rotationMap.containsKey(key))
								{
									if (noOfLogicalBreak == courseList.size())
									{
										RotationData rotationData = rotationMap.get(key);
								
										rotationData = updateRotationTime(rotationData, timeToAdd, timeForOnePost);
										
										rotationMap.put(key, rotationData);
										
										timeToAdd = timeToAdd + timeForOnePost;
									}
									else
									{
										boolean updateTimeFlag = false;
										for (int j=0; j<noOfLogicalBreak; j++)
										{
											if (courseList.size() > j)
											{
												Course course = courseList.get(j);
												int noOfPostInCourse = course.getOscePostRooms().size();
												if (noOfPostInCourse == maxPost)
												{
													updateTimeFlag = true;
													break;
												}
											}
										}
										
										if (updateTimeFlag)
										{
											RotationData rotationData = rotationMap.get(key);
											
											rotationData = updateRotationTime(rotationData, timeToAdd, timeForOnePost);
									
											rotationMap.put(key, rotationData);
											
											timeToAdd = timeToAdd + timeForOnePost;
										}
									}
								}	
							}
						}			
						
						if (noOfLongBreak > 0)
						{
							if (noOfLongBreak == 1)
							{
								int middleRot = numberOfRotation / 2;
								Integer longBreakRotation = longBreakMap.get(noOfLongBreak);
							
								if (middleRot != longBreakRotation)
								{
									RotationData longBreakRotData = rotationMap.get(longBreakRotation);
									longBreakRotData.setBreakValue(osce.getMiddleBreak().intValue());
									rotationMap.put(longBreakRotation, longBreakRotData);
									int diff = osce.getLongBreak().intValue() - osce.getMiddleBreak().intValue();
									
									for (int i=(longBreakRotation+1); i<=numberOfRotation; i++)
									{
										RotationData rotationData = rotationMap.get(i);
										
										Date stTime = rotationData.getStartTime();
										Date endTime = rotationData.getEndTime();
										
										stTime = dateSubtractMin(stTime, diff);										
										endTime = dateSubtractMin(endTime, diff);
										
										rotationData.setStartTime(stTime);
										rotationData.setEndTime(endTime);
										
										rotationMap.put(i, rotationData);
									}
									
									RotationData longBreakData = rotationMap.get(middleRot);
									longBreakData.setBreakValue(osce.getLongBreak().intValue());
									rotationMap.put(middleRot, longBreakData);
									
									for (int i=(middleRot+1); i<=numberOfRotation; i++)
									{
										RotationData rotationData = rotationMap.get(i);
										
										Date stTime = rotationData.getStartTime();
										Date endTime = rotationData.getEndTime();
										
										stTime = dateAddMin(stTime, diff);
										endTime = dateAddMin(endTime, diff);
										
										rotationData.setStartTime(stTime);
										rotationData.setEndTime(endTime);
										
										rotationMap.put(i, rotationData);
									}
								}
							}
						}
						
						sequenceMap.put(osceSeq.getId(), rotationMap);
					}
				}
			}
		}
		
		updateTimeOfLogicalBreak();
	}*/
	
	/*private void updateTimeOfLogicalBreak() {
		Set<Long> keySet = new TreeSet<Long>(sequenceMap.keySet());
		int totalRotation = 0;
		int timeForOnePost = osce.getPostLength().intValue() + osce.getShortBreak().intValue();
		int timeToAdd = 0;
		Long previousSeqOsceDayId = 0l;
		
		for (Long key : keySet)
		{
			OsceSequence osceSequence = OsceSequence.findOsceSequence(key);
			
			if (previousSeqOsceDayId.equals(osceSequence.getOsceDay().getId()) == false)
				timeToAdd = 0;
			
			if (osceSequence != null)
			{
				List<Course> courseList = osceSequence.getCourses();
				int maxPost = osceSequence.getOscePosts().size();
				Map<Integer, RotationData> rotationMap = sequenceMap.get(key);
				Set<Integer> rotationKeySet = new TreeSet<Integer>(rotationMap.keySet());
				
				for (Integer rotationKey : rotationKeySet)
				{
					RotationData rotationData = rotationMap.get(rotationKey);
					
					if (logicalBreakMap.containsKey(totalRotation))
					{
						int noOfLogicalBreak = logicalBreakMap.get(totalRotation);
						if (noOfLogicalBreak == courseList.size())
						{	
							rotationData = updateRotationTime(rotationData, timeToAdd, timeForOnePost);
							
							rotationMap.put(rotationKey, rotationData);
							
							timeToAdd = timeToAdd + timeForOnePost;
						}
						else
						{
							boolean updateTimeFlag = false;
							for (int j=0; j<noOfLogicalBreak; j++)
							{
								if (courseList.size() > j)
								{
									Course course = courseList.get(j);
									int noOfPostInCourse = course.getOscePostRooms().size();
									if (noOfPostInCourse == maxPost)
									{
										updateTimeFlag = true;
										break;
									}
								}
							}
							
							if (updateTimeFlag)
							{
								rotationData = updateRotationTime(rotationData, timeToAdd, timeForOnePost);
								
								rotationMap.put(rotationKey, rotationData);
								
								timeToAdd = timeToAdd + timeForOnePost;
							}
						}
					}
					
					totalRotation = totalRotation + 1;
				}
				
				previousSeqOsceDayId = osceSequence.getOsceDay().getId();
			}
		}
	}*/

	/*private RotationData updateRotationTime(RotationData rotationData, int timeToAdd, int timeForOnePost)
	{
		Date rotStTime = rotationData.getStartTime();
		Date rotEndTime = rotationData.getEndTime();
		rotStTime = dateAddMin(rotStTime, timeToAdd);
		rotEndTime = dateAddMin(rotEndTime, (timeToAdd + timeForOnePost));
		
		rotationData.setStartTime(rotStTime);
		rotationData.setEndTime(rotEndTime);
		
		return rotationData;
	}*/
	
	public void printResult()
	{
		Set<Long> keySet = new TreeSet<Long>(sequenceMap.keySet());
		int totalRotation = 0;
		
		for (Long key : keySet)
		{
			Map<Integer, RotationData> rotationMap = sequenceMap.get(key);
			
			Set<Integer> rotationKeySet = new TreeSet<Integer>(rotationMap.keySet());
			
			for (Integer rotationKey : rotationKeySet)
			{
				totalRotation = totalRotation + 1;
				RotationData rotationData = rotationMap.get(rotationKey);
				System.out.println("ROT NO : " + rotationData.getRotationNumber() + " START TIME : " + rotationData.getStartTime() + " BREAK VAL : " + rotationData.getBreakValue() + " END TIME : " + rotationData.getEndTime());
				
				if (logicalBreakMap.containsKey((totalRotation-1)))
				{
					System.out.println("~~~~~~~~~~~~LOGICAL BREAK : " + logicalBreakMap.get(totalRotation-1));					
				}
			}
		}
	}
	
	public void saveOsceData(){
		
		if (osce != null)
		{
			List<OsceDay> osceDayList = osce.getOsce_days();
			for (OsceDay osceDay : osceDayList)
			{
				Integer totalSp = 0;				
				Integer roomCount = 0;
				Integer studentCount = 0;
				
				new OsceDayRotation().deleteOsceRotationDataByOsceDay(osceDay.getId());
				
				String rotationStr = "";
				int numberOfRotation = 0;
				int lunchBreakRotation = 0;
				Date lunchBreakStartTime = null;
				Date latestEndTime = osceDay.getTimeEnd();
				List<OsceSequence> osceSeqList = osceDay.getOsceSequences();
				
				for (int i=0; i<osceSeqList.size(); i++)
				{
					OsceSequence osceSeq = osceSeqList.get(i);
					if(sequenceMap.containsKey(osceSeq.getId()))
					{
						Map<Integer, RotationData> rotationMap = sequenceMap.get(osceSeq.getId());
						List<Integer> keySet = new ArrayList<Integer>(new TreeSet<Integer>(rotationMap.keySet()));
						
						for (Integer key : keySet)
						{
							RotationData rotationData = rotationMap.get(key);
							
							if (keySet.size() == 1 && (osce.getLongBreak().intValue() == rotationData.getBreakValue() || osce.getLunchBreak().intValue() == rotationData.getBreakValue()))
							{
								int totalPost = osceSeq.getOscePosts().size();
								int halfPost = totalPost / 2;
								int firstHalfTime = (halfPost * osce.getPostLength()) + ((halfPost - 1) * osce.getShortBreak());
								int secondHalfTime = ((totalPost - halfPost) * osce.getPostLength()) + (((totalPost - halfPost)) * osce.getShortBreak());
								Date longBreakStart = dateAddMin(rotationData.getStartTime(), firstHalfTime);
								Date longBreakEndTime = dateAddMin(longBreakStart, osce.getLongBreak().intValue());
								Date rotationEndTime = dateAddMin(longBreakEndTime, secondHalfTime);
								rotationEndTime = dateSubtractMin(rotationEndTime, osce.getShortBreak().intValue());
								
								OsceDayRotation firstOsceDayRotation = new OsceDayRotation();
								firstOsceDayRotation.setRotationNumber(rotationData.getRotationNumber());
								firstOsceDayRotation.setTimeStart(rotationData.getStartTime());
								firstOsceDayRotation.setTimeEnd(longBreakStart);
								firstOsceDayRotation.setOsceDay(osceDay);
								firstOsceDayRotation.setOsceSequence(osceSeq);
								firstOsceDayRotation.setBreakDuration(rotationData.getBreakValue());
								firstOsceDayRotation.setBreakType(BreakType.LONG_BREAK);
								firstOsceDayRotation.persist();
								
								OsceDayRotation secondOsceDayRotation = new OsceDayRotation();
								secondOsceDayRotation.setRotationNumber(rotationData.getRotationNumber());
								secondOsceDayRotation.setTimeStart(longBreakEndTime);
								secondOsceDayRotation.setTimeEnd(rotationEndTime);
								secondOsceDayRotation.setOsceDay(osceDay);
								secondOsceDayRotation.setOsceSequence(osceSeq);
								secondOsceDayRotation.setBreakDuration(rotationData.getBreakValue());
								if (i == 0)
								{
									secondOsceDayRotation.setBreakType(BreakType.LUNCH_BREAK);
									lunchBreakRotation = rotationData.getRotationNumber();
									lunchBreakStartTime = rotationEndTime;
								}
								else
									secondOsceDayRotation.setBreakType(BreakType.MIDDLE_BREAK);
								secondOsceDayRotation.persist();
							}
							else
							{
								OsceDayRotation osceDayRotation = new OsceDayRotation();
								osceDayRotation.setRotationNumber(rotationData.getRotationNumber());
								osceDayRotation.setTimeStart(rotationData.getStartTime());
								osceDayRotation.setTimeEnd(rotationData.getEndTime());
								osceDayRotation.setOsceDay(osceDay);
								osceDayRotation.setOsceSequence(osceSeq);
								osceDayRotation.setBreakDuration(rotationData.getBreakValue());
								
								if (osce.getMiddleBreak().intValue() == rotationData.getBreakValue())
									osceDayRotation.setBreakType(BreakType.MIDDLE_BREAK);
								else if (osce.getLongBreak().intValue() == rotationData.getBreakValue())
									osceDayRotation.setBreakType(BreakType.LONG_BREAK);
								else if (osce.getLunchBreak().intValue() == rotationData.getBreakValue())
								{
									lunchBreakRotation = rotationData.getRotationNumber();
									lunchBreakStartTime = rotationData.getEndTime();
									osceDayRotation.setBreakType(BreakType.LUNCH_BREAK);
								}
								
								osceDayRotation.persist();
							}
							
							rotationStr = rotationStr + numberOfRotation + ":" + rotationData.getBreakValue() + "-"; 
							numberOfRotation += 1;
							
							latestEndTime = rotationData.getEndTime();						
						}
					}
					
					Integer spCount = OscePost.findSpByOsceSeqId(osceSeq.getId());
					totalSp += spCount;
				}
				
				roomCount = OscePostRoom.findRoomByOsceDayId(osceDay.getId());
				if (studentOsceDayMap.containsKey(osceDay.getId()))
				{
					studentCount = studentOsceDayMap.get(osceDay.getId());					
				}
				
				osceDay.setStudentCount(studentCount);
				osceDay.setRoomCount(roomCount);
				osceDay.setSpCount(totalSp);
				osceDay.setLunchBreakAfterRotation(lunchBreakRotation);
				osceDay.setLunchBreakStart(lunchBreakStartTime);
				osceDay.setBreakByRotation(rotationStr);
				osceDay.setTimeEnd(latestEndTime);
				osceDay.persist();							
			}
		}
	}
	
	public void createAssignment()
	{
		if (osce != null)
		{
			List<StudentAssignment> studAssList = new ArrayList<ManualOsceTimeTableCalculation.StudentAssignment>();
			List<SpAssignment> spAssList = new ArrayList<ManualOsceTimeTableCalculation.SpAssignment>();
			
			int postLength = osce.getPostLength();
			int shortBreak = osce.getShortBreak();
			List<OsceDay> osceDayList = osce.getOsce_days();
			int sequenceNumber = 1;
			int noOfSeqRot = 0;
			int assRotNumber;
			int spIndex = 1;
			for (OsceDay osceDay : osceDayList)
			{
				Long osceDayID = osceDay.getId();
				int numberOfRotation = 0;
				
				List<OsceSequence> osceSeqList = osceDay.getOsceSequences();
				
				for (OsceSequence osceSequence : osceSeqList)
				{
					List<OsceDayRotation> osceDayRotationList = osceSequence.getOsceDayRotations();
					List<Course> courseList = osceSequence.getCourses();
					
					if (osceSequence.getNumberRotation() == 1 && osceDayRotationList.size() == 2)
					{
						sequenceNumber = createAssignemntForSequenceContainsOneRotation(postLength, shortBreak, osceDayID, courseList, osceDayRotationList, studAssList, spAssList, sequenceNumber, spIndex, numberOfRotation);
						spIndex = spIndex + 2;
					}
					else
					{
						for (Course course : courseList)
						{
							assRotNumber = noOfSeqRot;
							
							for (OsceDayRotation osceDayRotation : osceDayRotationList)
							{
								Date rotationEndTime = osceDayRotation.getTimeEnd();
								int rotationNumber = osceDayRotation.getRotationNumber();
								List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseIdOrderByOscePostSeqNo(course.getId());
								int noOfSlot = oscePostRoomList.size();
								
								if (logicalBreakMap.containsKey(assRotNumber))
								{
									Integer logicalBreak = logicalBreakMap.get(assRotNumber);
									if (logicalBreak > 0)
									{
										noOfSlot = noOfSlot + 1;
										logicalBreak = logicalBreak - 1;
										logicalBreakMap.put(assRotNumber, logicalBreak);
										oscePostRoomList.add(null);
									}
								}
								
								List<Integer> seqNumList = new ArrayList<Integer>();
								for (int i=0; i<noOfSlot; i++)
								{
									seqNumList.add(sequenceNumber);
									sequenceNumber = sequenceNumber + 1;
								}
								
								int seqIndex = 0;
								Date stTimeStart = osceDayRotation.getTimeStart();
								
								for (int i=0; i<noOfSlot; i++)
								{	
									Date stTimeEnd = dateAddMin(stTimeStart, postLength);
									
									for (int j=0; j<noOfSlot; j++)
									{
										Long oprId = null;
										if (oscePostRoomList.size() > j && oscePostRoomList.get(j) != null)
											oprId = oscePostRoomList.get(j).getId();
																			
										if (seqIndex > (seqNumList.size() - 1 ))
											seqIndex = 0;
										
										Integer stSeqNo = seqNumList.get(seqIndex);
										
										if (j < (noOfSlot - 1))
											seqIndex = seqIndex + 1;				
										
										
										StudentAssignment studentAssignment = new StudentAssignment();
										studentAssignment.setTimeStart(stTimeStart);
										studentAssignment.setTimeEnd(stTimeEnd);
										studentAssignment.setOscePostRoomId(oprId);
										studentAssignment.setRotationNumber(assRotNumber);
										studentAssignment.setSequenceNumber(stSeqNo);
										studentAssignment.setOsceDayId(osceDayID);
										
										studAssList.add(studentAssignment);									
										
										rotationEndTime = stTimeEnd;
									}
									stTimeStart = dateAddMin(stTimeEnd, shortBreak);
								}
								
								for (OscePostRoom oscePostRoom : oscePostRoomList)
								{
									PostType postType = oscePostRoom.getOscePost().getOscePostBlueprint().getPostType();
									
									if (oscePostRoom != null && (PostType.NORMAL.equals(postType) || PostType.DUALSP.equals(postType)))
									{
										if (oscePostRoom.getOscePost().getStandardizedRole() != null && RoleTypes.Material.equals(oscePostRoom.getOscePost().getStandardizedRole().getRoleType()) == false)
										{
											SpAssignment spAssignment = new SpAssignment();
											spAssignment.setTimeStart(osceDayRotation.getTimeStart());
											spAssignment.setTimeEnd(rotationEndTime);
											spAssignment.setSequenceNumber((numberOfRotation + rotationNumber));
											spAssignment.setOscePostRoomId(oscePostRoom.getId());
											spAssignment.setOsceDayId(osceDayID);
											
											spAssList.add(spAssignment);
											
											if (PostType.DUALSP.equals(postType))
											{
												SpAssignment spAssignment1 = new SpAssignment();
												spAssignment1.setTimeStart(osceDayRotation.getTimeStart());
												spAssignment1.setTimeEnd(rotationEndTime);
												spAssignment1.setSequenceNumber((numberOfRotation + rotationNumber));
												spAssignment1.setOscePostRoomId(oscePostRoom.getId());
												spAssignment1.setOsceDayId(osceDayID);
												
												spAssList.add(spAssignment1);
											}
										}
									}
								}
								
								assRotNumber = assRotNumber + 1;
							}
						}
					}								
					numberOfRotation += osceSequence.getNumberRotation();
					noOfSeqRot += osceSequence.getNumberRotation();
				}
			}
			
			storeStudentAssignment(studAssList);
			
			storeSPAssignment(spAssList);
		}
	}
	
	private int createAssignemntForSequenceContainsOneRotation(int postLength, int shortBreak, Long osceDayID, List<Course> courseList, List<OsceDayRotation> osceDayRotationList, List<StudentAssignment> studAssList, List<SpAssignment> spAssList, int sequenceNumber, int spIndex, int numberOfRotation) 
	{
		if (osceDayRotationList.size() == 2)
		{
			OsceDayRotation firstOsceDayRotation = osceDayRotationList.get(0);
			OsceDayRotation secondOsceDayRotation = osceDayRotationList.get(1);
			
			for (Course course : courseList)
			{
				Date rotationEndTime = firstOsceDayRotation.getTimeEnd();
				int rotationNumber = firstOsceDayRotation.getRotationNumber();
				List<OscePostRoom> oscePostRoomList = OscePostRoom.findOscePostRoomByCourseIdOrderByOscePostSeqNo(course.getId());
				
				int noOfSlot = oscePostRoomList.size();
				
				List<Integer> seqNumList = new ArrayList<Integer>();
				for (int i=0; i<noOfSlot; i++)
				{
					seqNumList.add(sequenceNumber);
					sequenceNumber = sequenceNumber + 1;
				}
				
				int seqIndex = 0;
				Date stTimeStart = firstOsceDayRotation.getTimeStart();
				
				for (int i=0; i<noOfSlot; i++)
				{	
					Date stTimeEnd = dateAddMin(stTimeStart, postLength);
					
					for (int j=0; j<noOfSlot; j++)
					{
						Long oprId = null;
						if (oscePostRoomList.size() > j && oscePostRoomList.get(j) != null)
							oprId = oscePostRoomList.get(j).getId();
															
						if (seqIndex > (seqNumList.size() - 1 ))
							seqIndex = 0;
						
						Integer stSeqNo = seqNumList.get(seqIndex);
						
						if (j < (noOfSlot - 1))
							seqIndex = seqIndex + 1;				
						
						
						StudentAssignment studentAssignment = new StudentAssignment();
						studentAssignment.setTimeStart(stTimeStart);
						studentAssignment.setTimeEnd(stTimeEnd);
						studentAssignment.setOscePostRoomId(oprId);
						studentAssignment.setRotationNumber(numberOfRotation);
						studentAssignment.setSequenceNumber(stSeqNo);
						studentAssignment.setOsceDayId(osceDayID);
						
						studAssList.add(studentAssignment);									
						
						rotationEndTime = stTimeEnd;
					}
				
					if (stTimeEnd.equals(firstOsceDayRotation.getTimeEnd()))
					{
						stTimeStart = dateAddMin(stTimeEnd, osce.getLongBreak());
					}
					else
					{
						stTimeStart = dateAddMin(stTimeEnd, shortBreak);
					}
				}
				
				for (OscePostRoom oscePostRoom : oscePostRoomList)
				{
					int spSequence = spIndex;
					PostType postType = oscePostRoom.getOscePost().getOscePostBlueprint().getPostType();
					
					if (oscePostRoom != null && (PostType.NORMAL.equals(postType) || PostType.DUALSP.equals(postType)))
					{
						if (oscePostRoom.getOscePost().getStandardizedRole() != null && RoleTypes.Material.equals(oscePostRoom.getOscePost().getStandardizedRole().getRoleType()) == false)
						{
							Date timeStart = firstOsceDayRotation.getTimeStart();
							Date timeEnd = firstOsceDayRotation.getTimeEnd();
							SpAssignment spAssignment = new SpAssignment();
							spAssignment.setTimeStart(timeStart);
							spAssignment.setTimeEnd(timeEnd);
							spAssignment.setSequenceNumber(spSequence);
							spAssignment.setOscePostRoomId(oscePostRoom.getId());
							spAssignment.setOsceDayId(osceDayID);
							spAssList.add(spAssignment);
							
							if (PostType.DUALSP.equals(postType))
							{
								SpAssignment spAssignment1 = new SpAssignment();
								spAssignment1.setTimeStart(timeStart);
								spAssignment1.setTimeEnd(timeEnd);
								spAssignment1.setSequenceNumber(spSequence);
								spAssignment1.setOscePostRoomId(oscePostRoom.getId());
								spAssignment1.setOsceDayId(osceDayID);
								
								spAssList.add(spAssignment1);
							}
							
							spSequence += 1;
							
							Date timeStart1 = secondOsceDayRotation.getTimeStart();
							Date timeEnd1 = secondOsceDayRotation.getTimeEnd();
							SpAssignment spAssignment2 = new SpAssignment();
							spAssignment2.setTimeStart(timeStart1);
							spAssignment2.setTimeEnd(timeEnd1);
							spAssignment2.setSequenceNumber(spSequence);
							spAssignment2.setOscePostRoomId(oscePostRoom.getId());
							spAssignment2.setOsceDayId(osceDayID);
							spAssList.add(spAssignment2);
							
							if (PostType.DUALSP.equals(postType))
							{
								SpAssignment spAssignment1 = new SpAssignment();
								spAssignment1.setTimeStart(timeStart1);
								spAssignment1.setTimeEnd(timeEnd1);
								spAssignment1.setSequenceNumber(spSequence);
								spAssignment1.setOscePostRoomId(oscePostRoom.getId());
								spAssignment1.setOsceDayId(osceDayID);
								
								spAssList.add(spAssignment1);
							}
						}
					}
				}
			}
		}
		
		return sequenceNumber;
	}

	private void storeSPAssignment(List<SpAssignment> spAssList) {
		
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO `assignment` (`sequence_number`, `time_end`, `time_start`, `type`, `version`, `osce_day`, `osce_post_room`) VALUES");
		
		for (SpAssignment spAssignment : spAssList)
		{
			sqlBuilder.append(" (")
					  .append(spAssignment.getSequenceNumber())
					  .append(", ")
					  .append(getTimeStamp(spAssignment.getTimeEnd()))
					  .append(", ")
					  .append(getTimeStamp(spAssignment.getTimeStart()))
					  .append(", 1")
					  .append(", 0, ")
					  .append(spAssignment.getOsceDayId())
					  .append(", ")
					  .append(spAssignment.getOscePostRoomId())
					  .append(")");
			
			sqlBuilder.append(",");
		}
		
		if (spAssList.isEmpty() == false)
		{
			String sql = StringUtils.removeEnd(sqlBuilder.toString(), ",");
			new Assignment().insertAssignment(sql);	
		}
	}

	private void storeStudentAssignment(List<StudentAssignment> studAssList) {

		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO `assignment` (`sequence_number`, `time_end`, `time_start`, `type`, `version`, `osce_day`, `osce_post_room`, `rotation_number`) VALUES");
		
		for (StudentAssignment studentAssignment : studAssList)
		{
			sqlBuilder.append(" (")
					  .append(studentAssignment.getSequenceNumber())
					  .append(", ")
					  .append(getTimeStamp(studentAssignment.getTimeEnd()))
					  .append(", ")
					  .append(getTimeStamp(studentAssignment.getTimeStart()))
					  .append(", 0")
					  .append(", 0, ")
					  .append(studentAssignment.getOsceDayId())
					  .append(", ")
					  .append(studentAssignment.getOscePostRoomId())
					  .append(", ")
					  .append(studentAssignment.getRotationNumber())
					  .append(")");
			
			sqlBuilder.append(",");
		}
		
		if (studAssList.isEmpty() == false)
		{
			String sql = StringUtils.removeEnd(sqlBuilder.toString(), ",");
			new Assignment().insertAssignment(sql);	
		}
	}
	
	private String getTimeStamp(Date lastTimeStamp) {
		if (lastTimeStamp != null)
		{
			return ("'" + format.format(lastTimeStamp) + "'");
		}
		return null;
	}

	/*private void printSPRecord(List<SpAssignment> spAssList) {
		System.out.println("SP DATA");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("SEQ NO	TIMESTART		TIMEEND		OPRID");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for (SpAssignment spAssignment : spAssList)
		{
			System.out.println(spAssignment.getSequenceNumber() + "	" + format.format(spAssignment.getTimeStart()) + "	" + format.format(spAssignment.getTimeEnd()) + "	" + spAssignment.getOscePostRoomId());
		}
		System.out.println("----------------------------------------------------------------------------------------");
	}

	private void printStudentRecord(List<StudentAssignment> studAssList) {
		System.out.println("STUDENT DATA");
		System.out.println("----------------------------------------------------------------------------------------");
		System.out.println("SEQ NO	TIMESTART		TIMEEND			ROTNO		OPRID");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for (StudentAssignment studentAssignment : studAssList)
		{
			System.out.println(studentAssignment.getSequenceNumber() + "	" + format.format(studentAssignment.getTimeStart()) + "	" + format.format(studentAssignment.getTimeEnd()) + "	" + studentAssignment.getRotationNumber() + "	" + studentAssignment.getOscePostRoomId());
		}
		System.out.println("----------------------------------------------------------------------------------------");
	}*/

	private Date dateAddMin(Date date, int minToAdd) {
		return new Date((long) (date.getTime() + minToAdd * 60 * 1000));
	}
	
	/*private boolean isBetween(Date date, Date minDate, Date maxDate)
	{
		if (date.after(minDate) && date.before(maxDate))
			return true;
		
		return false;
	}
	
	private boolean isAfter(Date date1, Date date2)
	{
		if (date1.after(date2))
			return true;
		
		return false;
	}*/
	
	private Date dateSubtractMin(Date date, int minToSubtract) {
		return new Date((long) (date.getTime() - minToSubtract * 60 * 1000));
	}
	
	public class RotationData{
		private int rotationNumber;
		private Date startTime;
		private Date endTime;
		private int breakValue;		
		
		public RotationData(int rotationNumber,	Date startTime,	int breakValue, Date endTime) {
			this.rotationNumber = rotationNumber;
			this.startTime = startTime;
			this.breakValue = breakValue;
			this.endTime = endTime;
		}
		
		public int getRotationNumber() {
			return rotationNumber;
		}
		
		public Date getStartTime() {
			return startTime;
		}
		
		public int getBreakValue() {
			return breakValue;
		}
		
		public void setRotationNumber(int rotationNumber) {
			this.rotationNumber = rotationNumber;
		}
		
		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}
		
		public void setBreakValue(int breakValue) {
			this.breakValue = breakValue;
		}
		
		public Date getEndTime() {
			return endTime;
		}
		
		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
	}
	
	public class StudentAssignment{
		private int sequenceNumber;
		private Date timeStart;
		private Date timeEnd;
		private int rotationNumber;
		private Long oscePostRoomId;
		private Long osceDayId;
		
		public int getSequenceNumber() {
			return sequenceNumber;
		}
		
		public void setSequenceNumber(int sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}
		
		public Date getTimeStart() {
			return timeStart;
		}
		
		public void setTimeStart(Date timeStart) {
			this.timeStart = timeStart;
		}
		
		public Date getTimeEnd() {
			return timeEnd;
		}
		
		public void setTimeEnd(Date timeEnd) {
			this.timeEnd = timeEnd;
		}
		
		public int getRotationNumber() {
			return rotationNumber;
		}
		
		public void setRotationNumber(int rotationNumber) {
			this.rotationNumber = rotationNumber;
		}
		
		public Long getOscePostRoomId() {
			return oscePostRoomId;
		}
		
		public void setOscePostRoomId(Long oscePostRoomId) {
			this.oscePostRoomId = oscePostRoomId;
		}	
		
		public Long getOsceDayId() {
			return osceDayId;
		}
		
		public void setOsceDayId(Long osceDayId) {
			this.osceDayId = osceDayId;
		}
	}
	
	public class SpAssignment{
		private int sequenceNumber;
		private Date timeStart;
		private Date timeEnd;
		private Long oscePostRoomId;
		private Long osceDayId;
	
		public int getSequenceNumber() {
			return sequenceNumber;
		}
		
		public void setSequenceNumber(int sequenceNumber) {
			this.sequenceNumber = sequenceNumber;
		}
		
		public Date getTimeStart() {
			return timeStart;
		}
		
		public void setTimeStart(Date timeStart) {
			this.timeStart = timeStart;
		}
		
		public Date getTimeEnd() {
			return timeEnd;
		}
		
		public void setTimeEnd(Date timeEnd) {
			this.timeEnd = timeEnd;
		}
		
		public Long getOscePostRoomId() {
			return oscePostRoomId;
		}
		
		public void setOscePostRoomId(Long oscePostRoomId) {
			this.oscePostRoomId = oscePostRoomId;
		}
		
		public Long getOsceDayId() {
			return osceDayId;
		}
		
		public void setOsceDayId(Long osceDayId) {
			this.osceDayId = osceDayId;
		}
	}
}
