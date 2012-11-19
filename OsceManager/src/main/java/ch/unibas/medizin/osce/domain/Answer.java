package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math.stat.StatUtils;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.MapEnvelop;

import com.allen_sauer.gwt.log.client.Log;

@RooJavaBean
@RooToString
@RooEntity
public class Answer {
	
	String answer;
	
	@ManyToOne
	Student student;
	
	@ManyToOne
	ChecklistQuestion checklistQuestion;
	
	@ManyToOne
	ChecklistOption checklistOption;
	
	@ManyToOne
	ChecklistCriteria checklistCriteria;
	
	@ManyToOne
	Doctor doctor;
	
	@ManyToOne
	OscePostRoom oscePostRoom;
	
	public static List<Answer> retrieveStudent(Long osceDayId,Long courseId)
	{
		 	Log.info("retrieveStudent :");
	        EntityManager em = entityManager();
	        String queryString = "SELECT  a FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.course=" + courseId + " ) order by a.student.name asc";
	        
	        TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
	        List<Answer> assignmentList = query.getResultList();
	        Log.info("retrieveStudent query String :" + queryString);
	        Log.info("Assignment List Size :" + assignmentList.size());
	        return assignmentList;
	}
	
	public static List<ChecklistQuestion> retrieveDistinctQuestion(Long postId)
	{
		 	Log.info("retrieveStudent :");
	        EntityManager em = entityManager();
	        String queryString = "SELECT  distinct a.checklistQuestion FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost=" + postId + " ) order by a.checklistQuestion.sequenceNumber asc";
	        
	        TypedQuery<ChecklistQuestion> query = em.createQuery(queryString, ChecklistQuestion.class);
	        List<ChecklistQuestion> questionList = query.getResultList();
	        
	       
	        Log.info("retrieveQuestion query String :" + queryString);
	        Log.info("Assignment List Size :" + questionList.size());
	        return questionList;
	}
	
	public static List<Answer> retrieveQuestionPerPostAndItem(Long postId,Long itemId)
	{
		 	Log.info("retrieveStudent :");
	        EntityManager em = entityManager();
	        String queryString = "SELECT  a FROM Answer as a where a.checklistQuestion.id="+itemId+" and checklistOption!=null and  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost=" + postId + " ) order by a.checklistQuestion.sequenceNumber asc";
	        
	        TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
	        List<Answer> assignmentList = query.getResultList();
	        Log.info("retrieveQuestion query String :" + queryString);
	        Log.info("Assignment List Size :" + assignmentList.size());
	        return assignmentList;
	}
	
	
	//find now of student given in Answer table for particular post
	public static int countStudent(long dayId)
	{
		Log.info("countStudent :");
        EntityManager em = entityManager();
        String queryString = "select count(distinct sequenceNumber) from Assignment where type =0 and osceDay="+dayId;
        
        TypedQuery<Long> query = em.createQuery(queryString, Long.class);
        List<Long> assignmentList = query.getResultList();
        Log.info("retrieveQuestion query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList.size());
       
		
		return assignmentList.get(0).intValue();
	}
	
	
	
	public static int countAnswerTableRow(long postId,long itemId)
	{
		Log.info("countStudent :");
        EntityManager em = entityManager();
        String queryString = "SELECT  count(a) FROM Answer as a where a.checklistQuestion"+itemId+"  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost=" + postId + " ) ";
        
        TypedQuery<Student> query = em.createQuery(queryString, Student.class);
        List<Student> assignmentList = query.getResultList();
        Log.info("retrieveQuestion query String :" + queryString);
        Log.info("Assignment List Size :" + assignmentList.size());
       
		
		return assignmentList.size();
	}
	
	
	public static List<MapEnvelop> calculate(Long osceId,int analyticType)
	{
		//List<Map<String,List<String>>> data=new ArrayList<Map<String,List<String>>>();
		
		List<MapEnvelop> data=new ArrayList<MapEnvelop>();
		
		Osce osce=Osce.findOsce(osceId);
		List<OsceDay> days=osce.getOsce_days();
		
		for(OsceDay day:days)
		{
			List<OsceSequence> sequences=day.getOsceSequences();
			
			
			//sequence wise calculation
			for(OsceSequence seq:sequences)
			{
				//Map<String,List<String>> seqMap=new HashMap<String, List<String>>();
				
				
			//	data.add(seqMap);
				List<String> seqLevelList=new ArrayList<String>();
				
				List<OscePost> posts=seq.getOscePosts();
				
				double totalPointsPerPost[]=new double[posts.size()];
				
				ArrayList<Double> sumOfPoinstAtSeqLevel=new ArrayList<Double>();
				
				for(int l=0;l<posts.size();l++)
				{
					OscePost post=posts.get(l);
					
					//post wise calculation
				//	Map<String,List<String>> postMap=new HashMap<String, List<String>>();
				//	data.add(postMap);
					List<String> postLevelList=new ArrayList<String>();
					
					//retrieve distict item for this post
					List<ChecklistQuestion> items=retrieveDistinctQuestion(post.getId());
					
					//total num of student from assignment table
					int totalStudent=countStudent(day.getId());
					Log.info("Total number of student :" + totalStudent);
					//total number (expected num of rows in answer table if all student are present)= num of student * num of distinct question for particular post
					int totalPerPost=(totalStudent*items.size());
					
					//total number of points per item(sum of points/marks per item)
					double totalPointsPerItem[]=new double[items.size()];
					
					int missingAtPostLevel=0;
					Double average=new Double(0);
					
					//loop through distinct item/question for particular post
					for(int k=0;k<items.size();k++)
					{
						ChecklistQuestion item=items.get(k);
						// question/item wise calculation
						//Map<String,List<String>> questionMap=new HashMap<String, List<String>>();
						//data.add(questionMap);
						List<String> questionList=new ArrayList<String>();
						
						//list of answer table data for particular post and item. Number of row should be equal to total num of student. 
						List<Answer> itemAnswers=retrieveQuestionPerPostAndItem(post.getId(),item.getId());
						
						//1. calculate missing at item level
						int countAnswerTableRow= itemAnswers.size();
						Log.info("number of student answer :" +countAnswerTableRow);
						int missingAtItemLevel=totalStudent-countAnswerTableRow;
						missingAtPostLevel=missingAtPostLevel+missingAtItemLevel;
						double missingPercentageAtItemLevel=0;
						if(totalStudent != 0)
							missingPercentageAtItemLevel= percentage(missingAtPostLevel,totalStudent);
						
						String missingItem=missingAtItemLevel+"/"+missingPercentageAtItemLevel;
						questionList.add(missingItem);
						
						//2. calculate average at item level
						//List<Answer> itemAnswers=retrieveQuestionPerPostAndItem(post.getId(),item.getId());
						Double pointAvg=new Double(0);
						String optionValues="";
						String frequency="";
						 double [] points = new double[itemAnswers.size()];
						int [] optionCounts=new int[item.getCheckListOptions().size()]; 
						List<String> optionValuesList=new ArrayList<String>();
						for(int i=0;i<item.getCheckListOptions().size();i++)
						{
							ChecklistOption option=item.getCheckListOptions().get(i);
							
							if(optionValues.equals(""))
								optionValues=option.getValue();
							else
								optionValues=optionValues+"/"+option.getValue();
							
							optionValuesList.add(option.getValue());
						}
						for(int i=0;i<itemAnswers.size();i++)
						{
							Answer itemAnswer=itemAnswers.get(i);
						//	point=point+new Double(itemAnswer.getChecklistOption().getValue());
							Log.info("Point of item:" + itemAnswer.getChecklistOption().getValue());
							points[i]=new Double(itemAnswer.getChecklistOption().getValue());
							
							for(int j=0;j<optionValuesList.size();j++)
							{
								
								
								if(itemAnswer.getChecklistOption().getValue().equals(optionValuesList.get(j)))
								{
									optionCounts[j]++;
									break;
								}
							}
							
						}
						
						
				//		point=point/itemAnswers.size();
						pointAvg=StatUtils.sum(points);
						Log.info("Avg of point at item level :" + pointAvg);
						sumOfPoinstAtSeqLevel.add(pointAvg);
						totalPointsPerItem[k]=pointAvg;
						pointAvg=pointAvg/points.length;
						average=average+pointAvg;
						questionList.add(pointAvg.toString());
						
						
						//3.calculate S.D at item level
						Double pointSD=Math.sqrt(StatUtils.variance(points));
						questionList.add(pointSD.toString());
						
						//4. Point / Option Values
						questionList.add(optionValues);
						
						//5. frequency
						for(int j=0;j<optionValuesList.size();j++)
						{
							if(frequency.equals(""))
								frequency=frequency+percentage(optionCounts[j],itemAnswers.size());
							else
								frequency=frequency+"/"+percentage(optionCounts[j],itemAnswers.size());
							
						}
						questionList.add(frequency);
						
						
						//6. Chronbachs alpha
						questionList.add("-");
						
						MapEnvelop questionMap=new MapEnvelop();
						questionMap.put("q"+post.getId()+item.getId(), questionList);
						data.add(questionMap);
					}
					
					//1. Missing at post level
					double missingPercentageAtPostLevel=0;
					if(totalPerPost!=0)
						missingPercentageAtPostLevel= percentage(missingAtPostLevel,totalPerPost);
					String missing=missingAtPostLevel+"/"+missingPercentageAtPostLevel;
					postLevelList.add(missing);
					
					Log.info("missing :" + "p"+post.getId() + "  " +missing);
					
					// 2. Average at post level
					average=average/items.size();
					
					
					if(average.isNaN())
						postLevelList.add("0");
					else
						postLevelList.add(average.toString());
						
					
					// 3. standard deviation
					if(totalPointsPerItem.length != 0)
					{
						Double sdPerPost=Math.sqrt(StatUtils.variance(totalPointsPerItem));
						postLevelList.add(sdPerPost.toString());
					}
					else
					{
						postLevelList.add("0");
					}
					totalPointsPerPost[l]=totalPointsPerPost[l]+StatUtils.sum(totalPointsPerItem);
					
					MapEnvelop postMap=new MapEnvelop();
					postMap.put("p"+post.getId(), postLevelList);
					data.add(postMap);
				}
				
				// 1. standard deviation per sequence
				
				
				
				double totalPointsPerSum[]=new double[sumOfPoinstAtSeqLevel.size()] ;
				for(int i=0;i<sumOfPoinstAtSeqLevel.size();i++)
				{
					totalPointsPerSum[i]=sumOfPoinstAtSeqLevel.get(i);
				}
				
				Log.info("Sum of point per sequence :" +StatUtils.sum(totalPointsPerSum));
				
				if(totalPointsPerSum.length !=0)
				{
					Double sdPerSequence=Math.sqrt(StatUtils.variance(totalPointsPerSum));
					seqLevelList.add(sdPerSequence.toString());
				}
				else
				{
					seqLevelList.add("0");
				}
				MapEnvelop seqMap=new MapEnvelop();
			
				seqMap.put("s"+seq.getId(), seqLevelList);
				data.add(seqMap);
			}
			
		}
		
		
		
		return data;
	}
	public static double percentage(int a,int b)
	{
		if(b==0)
			return 0;
		else
			return ((a/b)*100);
	}
}
