package ch.unibas.medizin.osce.domain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.commons.math.stat.StatUtils;
import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.server.CalculateCronbachValue;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.upload.ExportStatisticData;
import ch.unibas.medizin.osce.shared.MapEnvelop;

import com.google.gwt.requestfactory.server.RequestFactoryServlet;


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
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date answerTimestamp;
	
	private static Logger log = Logger.getLogger(Answer.class);

	public static List<Answer> retrieveStudent(Long osceDayId, Long courseId) {
		log.info("retrieveStudent :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.course="
				+ courseId + " ) order by a.student.name asc";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
		List<Answer> assignmentList = query.getResultList();
		log.info("retrieveStudent query String :" + queryString);
		log.info("Assignment List Size :" + assignmentList.size());
		return assignmentList;
	}

	public static List<ChecklistQuestion> retrieveDistinctQuestion(Long postId) {
		log.info("retrieveDistinctQuestion :");
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.checklistQuestion FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.checkListTopic.sort_order, a.checklistQuestion.sequenceNumber asc";

		TypedQuery<ChecklistQuestion> query = em.createQuery(queryString,
				ChecklistQuestion.class);
		List<ChecklistQuestion> questionList = query.getResultList();

		log.info("retrieveQuestion query String :" + queryString);
		log.info("Assignment List Size :" + questionList.size());
		return questionList;
	}

	public static List<Doctor> retrieveDistinctExaminer(Long postId) {
		log.info("retrieveDistinctExaminer :");
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.doctor FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.sequenceNumber asc";

		TypedQuery<Doctor> query = em.createQuery(queryString, Doctor.class);
		List<Doctor> questionList = query.getResultList();

		log.info("retrieveQuestion query String :" + queryString);
		log.info("Assignment List Size :" + questionList.size());
		return questionList;
	}

	public static List<Answer> retrieveQuestionPerPostAndItem(Long postId,
			Long itemId) {
		log.info("retrieveStudent :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where a.checklistQuestion.id="
				+ itemId
				+ " and a.checklistOption!=null and  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.sequenceNumber asc";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
		List<Answer> assignmentList = query.getResultList();
		log.info("retrieveQuestion query String :" + queryString);
		log.info("Assignment List Size :" + assignmentList.size());
		return assignmentList;
	}

	// find now of student given in Answer table for particular post
	public static int countStudent(long dayId) {
		log.info("countStudent :");
		EntityManager em = entityManager();
		String queryString = "select count(distinct sequenceNumber) from Assignment where type =0 and osceDay="
				+ dayId;

		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		List<Long> assignmentList = query.getResultList();
		log.info("retrieveQuestion query String :" + queryString);
		log.info("Assignment List Size :" + assignmentList.size());

		return assignmentList.get(0).intValue();
	}

	public static int countAnswerTableRow(long postId, long itemId) {
		log.info("countAnswerTableRow :");
		EntityManager em = entityManager();
		String queryString = "SELECT  count(a) FROM Answer as a where a.checklistQuestion="
				+ itemId
				+ "  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Student> query = em.createQuery(queryString, Student.class);
		List<Student> assignmentList = query.getResultList();
		log.info("retrieveQuestion query String :" + queryString);
		log.info("Assignment List Size :" + assignmentList.size());

		return assignmentList.size();
	}

	public static int numOfDistinctStudentExamined(Long examinerId, Long postId) {
		log.info("numOfDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT  count(a) FROM Answer as a where a.doctor="
				+ examinerId
				+ "  and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Long> query = em.createQuery(queryString, Long.class);

		log.info("numOfDistinctStudentExamined query String :" + queryString);
		// Log.info("Assignment List Size :" + assignmentList.size());

		return query.getResultList().get(0).intValue();
	}

	public static List<Answer> retrieveDistinctStudentExamined(Long examinerId,
			Long postId) {
		log.info("retrieveDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where a.doctor="
				+ examinerId
				+ " and a.checklistOption!=null and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);

		log.info("retrieveDistinctStudentExamined query String :" + queryString);
		// Log.info("Assignment List Size :" + assignmentList.size());

		return query.getResultList();
	}

	// [spec
	public static Long retrieveNumberOfDistinctStudentExamined(
			Long examinerId, Long postId) {
		log.info("retrieveDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT count(DISTINCT a.student) FROM Answer as a where a.doctor="
				+ examinerId
				+ " and a.checklistOption!=null and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		log.info("numOfDistinctStudentExamined query String :" + queryString);
		return query.getResultList().get(0);
	}

	// spec]

	public static List<MapEnvelop> calculate(Long osceId, int analyticType,
			Set<Long> missingItemId,List<Long> examinerId,List<Integer> addPoints) {
		// List<Map<String,List<String>>> data=new
		// ArrayList<Map<String,List<String>>>();

		List<MapEnvelop> data = new ArrayList<MapEnvelop>();

		if (analyticType == 0)// item analysis
		{
			Osce osce = Osce.findOsce(osceId);
			List<OsceDay> days = osce.getOsce_days();
			
			//clear calculated data 
			ItemAnalysis.clearData(osce);
			
			ExportStatisticData.createCSV(osceId, RequestFactoryServlet.getThreadLocalRequest(),RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext(), false,new ArrayList<String>());

			int dayCtr = 0;
			
			for (OsceDay day : days) {
				
				try{
					dayCtr++;
					
					List<OsceSequence> sequences = day.getOsceSequences();

					// sequence wise calculation
					for (OsceSequence seq : sequences) {

						List<String> seqLevelList = new ArrayList<String>();

						List<OscePost> posts = seq.getOscePosts();

						double totalPointsPerPost[] = new double[posts.size()];

						ArrayList<Double> sumOfPoinstAtSeqLevel = new ArrayList<Double>();

						CalculateCronbachValue calculateCronbachValue;
						
						// post wise calculation
						for (int l = 0; l < posts.size(); l++) {
							
							calculateCronbachValue = new CalculateCronbachValue();
							
							OscePost post = posts.get(l);
							
							String fileName = "Day"+ (dayCtr) + "_" + post.getStandardizedRole().getShortName() + "_" + seq.getLabel() + ".csv";

							fileName = RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getRealPath(OsMaFilePathConstant.assignmentHTML) + fileName;
							
							calculateCronbachValue.countValue(fileName, missingItemId);
							
							Map<String, String> cronValMap = calculateCronbachValue.getCronbachValueMap();
							
							Map<String, String> meanMap = calculateCronbachValue.getMeanMap();
							
							Map<String, String> sdMap = calculateCronbachValue.getSdMap();
							
							String overAllCronbachVal = cronValMap.get("overall");
							
							String overAllMean = meanMap.get("overall");
							
							String overAllSd = sdMap.get("overall");
							
							List<String> postLevelList = new ArrayList<String>();

							// retrieve distict item for this post
							List<ChecklistQuestion> items = retrieveDistinctQuestion(post
									.getId());

							// total num of student from assignment table
							int totalStudent = countStudent(day.getId());
							log.info("Total number of student :" + totalStudent);

							// total number of student per post(expected num of rows
							// in answer table if all student are present)= num of
							// student * num of distinct question for particular
							// post
							int totalPerPost = (totalStudent * items.size());

							// total number of points per item(sum of points/marks
							// per item)
							double totalPointsPerItem[] = new double[items.size()];

							int missingAtPostLevel = 0;
							Double average = new Double(0);
							int pointsPerPostSize=0;
							// loop through distinct item/question for particular
							// post
							for (int k = 0; k < items.size(); k++) {
								ChecklistQuestion item = items.get(k);
								// question/item wise calculation
								// Map<String,List<String>> questionMap=new
								// HashMap<String, List<String>>();
								// data.add(questionMap);
								List<String> questionList = new ArrayList<String>();

								// list of answer table data for particular post and
								// item. Number of row should be equal to total num
								// of student.
								List<Answer> itemAnswers = retrieveQuestionPerPostAndItem(
										post.getId(), item.getId());
								
								
								//save data at question level
								ItemAnalysis itemAnalysis=new ItemAnalysis();
								
								
								double[] points = null;
								boolean isMissing = false;
								for (int i = 0; i < missingItemId.size(); i++) {
									if (missingItemId.contains(item.getId())) {
										isMissing = true;
										break;
									}
								}
								itemAnalysis.setDeActivate(isMissing);
								
								questionList.add(new Boolean(isMissing).toString());
								if (isMissing)
								{
									
									points = new double[itemAnswers.size()];
								}
								else
								{
									points = new double[totalStudent];
								}
								
								
								// 1. calculate missing at item level
								int countAnswerTableRow = itemAnswers.size();
								log.info("number of student answer :"
										+ countAnswerTableRow);
								int missingAtItemLevel = totalStudent
										- countAnswerTableRow;
								missingAtPostLevel = missingAtPostLevel
										+ missingAtItemLevel;
								double missingPercentageAtItemLevel = 0;
								if (totalStudent != 0)
									missingPercentageAtItemLevel = percentage(
											missingAtItemLevel, totalStudent);

								missingPercentageAtItemLevel = roundTwoDecimals(missingPercentageAtItemLevel);

								String missingItem = missingAtItemLevel + "/"
										+ missingPercentageAtItemLevel + "%";
								questionList.add(missingItem);
								
								itemAnalysis.setMissing(missingAtItemLevel);
								itemAnalysis.setMissingPercentage(new Double(missingPercentageAtItemLevel));
								
								// 2. calculate average at item level
								// List<Answer>
								// itemAnswers=retrieveQuestionPerPostAndItem(post.getId(),item.getId());
								Double pointAvg = new Double(0);
								String optionValues = "";
								String frequency = "";
								
								int[] optionCounts = new int[item
										.getCheckListOptions().size()];
								List<String> optionValuesList = new ArrayList<String>();
								for (int i = 0; i < item.getCheckListOptions()
										.size(); i++) {
									ChecklistOption option = item
											.getCheckListOptions().get(i);

									if (optionValues.equals(""))
										optionValues = option.getValue();
									else
										optionValues = optionValues + "/"
												+ option.getValue();

									optionValuesList.add(option.getValue());
								}
								for (int i = 0; i < itemAnswers.size(); i++) {
									Answer itemAnswer = itemAnswers.get(i);
									// point=point+new
									// Double(itemAnswer.getChecklistOption().getValue());
									log.info("Point of item:"
											+ itemAnswer.getChecklistOption()
													.getValue());
									points[i] = new Double(itemAnswer
											.getChecklistOption().getValue());

									for (int j = 0; j < optionValuesList.size(); j++) {

										if (itemAnswer.getChecklistOption()
												.getValue()
												.equals(optionValuesList.get(j))) {
											optionCounts[j]++;
											break;
										}
									}

								}

								// point=point/itemAnswers.size();
								pointAvg = StatUtils.sum(points);
								log.info("Avg of point at item level :" + pointAvg);
								// sumOfPoinstAtSeqLevel.add(pointAvg);
								// totalPointsPerItem[k]=pointAvg;
								
								average = average + pointAvg;
								pointsPerPostSize=pointsPerPostSize + points.length;
								
								/*pointAvg = pointAvg / points.length;
								
								pointAvg = roundTwoDecimals(pointAvg);*/
								
								String tempPointAvg = meanMap.get(String.valueOf(item.getId())) == null ? "0.0" : meanMap.get(String.valueOf(item.getId()));
								pointAvg = Double.parseDouble(tempPointAvg);
								
								questionList.add(pointAvg.toString());
								
								itemAnalysis.setMean(pointAvg);
								
								// 3.calculate S.D at item level
								Double pointSD = Math.sqrt(StatUtils.variance(points));
								
								totalPointsPerItem[k] = pointSD;
								pointSD = roundTwoDecimals(pointSD);
								
								//pointSD = roundTwoDecimals(pointSD);
								String tempPointSd = sdMap.get(String.valueOf(item.getId())) == null ? "0.0" : sdMap.get(String.valueOf(item.getId())); 
								pointSD = Double.parseDouble(tempPointSd);
								
								questionList.add(pointSD.toString());
								
								itemAnalysis.setStandardDeviation(pointSD);
								
								// 4. Point / Option Values
								questionList.add(optionValues);
								
								itemAnalysis.setPoints(optionValues);

								// 5. frequency
								for (int j = 0; j < optionValuesList.size(); j++) {
									if (frequency.equals(""))
										frequency = frequency
												+ Math.round(roundTwoDecimals(percentage(
														optionCounts[j],
														itemAnswers.size())));
									else
										frequency = frequency
												+ "/"
												+ Math.round(roundTwoDecimals(percentage(
														optionCounts[j],
														itemAnswers.size())));

								}
								questionList.add(frequency);
								
								itemAnalysis.setFrequency(frequency);
								// 6. Chronbachs alpha
								//questionList.add("-");
								String val = cronValMap.get(item.getId().toString());
								if (val == null)
								{
									questionList.add("-");
									itemAnalysis.setCronbach(0.0);
								}
								else	
								{
									questionList.add(cronValMap.get(item.getId().toString()));
									itemAnalysis.setCronbach(new Double(val));
								}

								MapEnvelop questionMap = new MapEnvelop();
								questionMap.put("q" + post.getId() + item.getId(),
										questionList);
								data.add(questionMap);
								
								//save data at item level
								itemAnalysis.setOsce(osce);
								itemAnalysis.setQuestion(item);
								itemAnalysis.setOscePost(post);
								itemAnalysis.setOsceSequence(seq);
								itemAnalysis.persist();
							}
							
							
							//save data at post level
							ItemAnalysis itemAnalysis=new ItemAnalysis();
							
							// 1. Missing at post level
							double missingPercentageAtPostLevel = 0;
							if (totalPerPost != 0)
								missingPercentageAtPostLevel = percentage(
										missingAtPostLevel, totalPerPost);

							// missingPercentageAtPostLevel=roundTwoDecimals(missingPercentageAtPostLevel);

							String missing = missingAtPostLevel + "/"
									+ totalPerPost;
							
							postLevelList.add(missing);

							log.info("missing :" + "p" + post.getId() + "  "
									+ missing);

							// 2. Average at post level
							/*if (pointsPerPostSize > 0)
							average = average / pointsPerPostSize;
							else
								average = Double.NaN;	*/						

							if (overAllMean == null || overAllMean.equals("NA"))
								average = Double.NaN;
							else
								average = Double.parseDouble(overAllMean);

							if (average.isNaN())
							{
								postLevelList.add("0.0");
								itemAnalysis.setMean(0.0);
							}
							else {
								average = roundTwoDecimals(average);
								postLevelList.add(average.toString());
								itemAnalysis.setMean(average);
							}

							// 3. standard deviation
							
							/*if (totalPointsPerItem.length != 0) {
								Double sdPerPost = Math.sqrt(StatUtils
										.variance(totalPointsPerItem));
								sdPerPost = roundTwoDecimals(sdPerPost);
								sumOfPoinstAtSeqLevel.add(sdPerPost);
								postLevelList.add(sdPerPost.toString());
								
								itemAnalysis.setStandardDeviation(sdPerPost);
							} else {
								postLevelList.add("0.0");
								sumOfPoinstAtSeqLevel.add(0.0);
								itemAnalysis.setStandardDeviation(0.0);
							}*/
							
							Double sdPerPost;
							
							if (overAllSd == null || overAllSd.equals("NA"))
								sdPerPost = 0.0;
							else
								sdPerPost = Double.parseDouble(overAllSd);
							
							sumOfPoinstAtSeqLevel.add(sdPerPost);
							postLevelList.add(sdPerPost.toString());						
							itemAnalysis.setStandardDeviation(sdPerPost);						
							
							totalPointsPerPost[l] = totalPointsPerPost[l] + StatUtils.sum(totalPointsPerItem);
							
							//4.points at post level
							postLevelList.add("-");
							
							//5.frequency at post level
							postLevelList.add("-");
							
							//6. Crohbach's alpha at post level
							
							if (overAllCronbachVal != null && !overAllCronbachVal.equals("NA"))
							{
								postLevelList.add(overAllCronbachVal);
								itemAnalysis.setCronbach(new Double(overAllCronbachVal));
							}
							else
							{
								postLevelList.add("0.0");
								itemAnalysis.setCronbach(0.0);
							}
							
							MapEnvelop postMap = new MapEnvelop();
							postMap.put("p" + post.getId(), postLevelList);
							data.add(postMap);
							
							//save data at post level
							itemAnalysis.setMissing(missingAtPostLevel);
							itemAnalysis.setMissingPercentage(new Double(totalPerPost));
							itemAnalysis.setOsce(osce);
							itemAnalysis.setOscePost(post);
							itemAnalysis.setOsceSequence(seq);
							
							
							itemAnalysis.persist();
							
							
						}

						// 1. standard deviation per sequence

						double totalPointsPerSum[] = new double[sumOfPoinstAtSeqLevel
								.size()];
						for (int i = 0; i < sumOfPoinstAtSeqLevel.size(); i++) {
							totalPointsPerSum[i] = sumOfPoinstAtSeqLevel.get(i);
						}

						log.info("Sum of point per sequence :"
								+ StatUtils.sum(totalPointsPerSum));
						
						//save data at sequence level
						ItemAnalysis itemAnalysis=new ItemAnalysis();
						
						if (totalPointsPerSum.length != 0) {
							Double sdPerSequence = Math.sqrt(StatUtils
									.variance(totalPointsPerSum));
							sdPerSequence = roundTwoDecimals(sdPerSequence);
							seqLevelList.add(sdPerSequence.toString());
							itemAnalysis.setStandardDeviation(sdPerSequence);
						} else {
							seqLevelList.add("0.0");
							itemAnalysis.setStandardDeviation(0.0);
						}
						MapEnvelop seqMap = new MapEnvelop();

						seqMap.put("s" + seq.getId(), seqLevelList);
						data.add(seqMap);
						
						//save data at sequence level
						itemAnalysis.setOsce(osce);
						itemAnalysis.setOsceSequence(seq);
						itemAnalysis.persist();
						
					}

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				
			}

		} else if (analyticType == 1)// post analysis
		{
			Osce osce = Osce.findOsce(osceId);
			List<OsceDay> days = osce.getOsce_days();

			for (OsceDay day : days) {
				List<OsceSequence> sequences = day.getOsceSequences();

				// sequence wise calculation
				for (OsceSequence seq : sequences) {
					List<OscePost> posts = seq.getOscePosts();

					// post wise calculation
					for (int l = 0; l < posts.size(); l++) {
						OscePost post = posts.get(l);

						List<Doctor> doctors = retrieveDistinctExaminer(post
								.getId());

						List<String> postLevelList = new ArrayList<String>();

						Integer numOfStudentPerPost = 0;

						Double averagePerPost = 0.0;
						int pointsPerPostLength=0;
						double sdPerDoctor[] = new double[doctors.size()];
						double minPerDoctor[] = new double[doctors.size()];
						double maxPerDoctor[] = new double[doctors.size()];
						for (int a = 0; a < doctors.size(); a++) {
							Doctor doctor = doctors.get(a);
							List<String> examinerLevelList = new ArrayList<String>();

							PostAnalysis postAnalysis=PostAnalysis.findExaminerLevelData(osce, post, doctor);
							boolean insert=false;
							if(postAnalysis==null)//insert
							{
								postAnalysis=new PostAnalysis();
								insert=true;
							}
							
							
							//login of save add point in database
 							if(examinerId.contains(doctor.getId()))
 							{
 								int index=examinerId.indexOf(doctor.getId());
 								postAnalysis.setPointsCorrected(addPoints.get(index));
 							}
 							else
 							{
 								postAnalysis.setPointsCorrected(0);
 							}
							
							// student count
							List<Answer> answers = retrieveDistinctStudentExamined(
									doctor.getId(), post.getId());

							Long countOfStudentList = retrieveNumberOfDistinctStudentExamined(
									doctor.getId(), post.getId());

							numOfStudentPerPost = numOfStudentPerPost
									+ countOfStudentList.intValue();

							examinerLevelList.add(new Integer(
									countOfStudentList.intValue()).toString());

							postAnalysis.setNumOfStudents(countOfStudentList.intValue());
							
							// pass
							examinerLevelList.add("0(0)");
							if(insert)
							{
								postAnalysis.setPassOrignal(0);
								postAnalysis.setPassCorrected(0);
							}
							else
								postAnalysis.setPassCorrected(0);

							// fail
							examinerLevelList.add("0(0)");
							if(insert)
							{
							postAnalysis.setFailOrignal(0);
							postAnalysis.setFailCorrected(0);
							}
							else
								postAnalysis.setFailCorrected(0);

							// passing grade
							examinerLevelList.add("0");
							postAnalysis.setBoundary(0);
							

							// average
							double points[] = new double[answers.size()];
							for (int i = 0; i < answers.size(); i++) {
								points[i] = new Double(answers.get(i)
										.getChecklistOption().getValue());

							}
							Double average = StatUtils.mean(points);
							
							
							averagePerPost = averagePerPost + StatUtils.sum(points);
							pointsPerPostLength=pointsPerPostLength+points.length;
							if (!average.isNaN())
								average = roundTwoDecimals(average);
							
							examinerLevelList.add(String.valueOf(average));
							postAnalysis.setMean(average);
							

							// sd
							Double sd = Math.sqrt(StatUtils.variance(points));
							sdPerDoctor[a] = sd;
							if (!sd.isNaN())
								sd = roundTwoDecimals(sd);
							
							examinerLevelList.add(String.valueOf(sd));
							postAnalysis.setStandardDeviation(sd);

							// minimum
							double min = StatUtils.min(points);
							minPerDoctor[a] = min;
							examinerLevelList.add(String.valueOf((int) min));
							postAnalysis.setMinOrignal((int) min);

							// maximum
							double max = StatUtils.max(points);
							maxPerDoctor[a] = max;
							examinerLevelList.add(String.valueOf((int) max));
							postAnalysis.setMaxOrignal((int) max);

							MapEnvelop examinerMap = new MapEnvelop();
							examinerMap.put(
									"e" + post.getId() + doctor.getId(),
									examinerLevelList);
							data.add(examinerMap);
							
							postAnalysis.setOsce(osce);
							postAnalysis.setOscePost(post);
							postAnalysis.setExaminer(doctor);
							postAnalysis.persist();
						}
						
						
						//save post data
						PostAnalysis postAnalysis=PostAnalysis.findPostLevelData(osce, post);
						boolean insert=false;
						if(postAnalysis==null)//insert
						{
							postAnalysis=new PostAnalysis();
							insert=true;
						}

						// student count
						System.out.println("Post : " + post.getSequenceNumber()
								+ " Student count : " + numOfStudentPerPost);
						postLevelList.add(numOfStudentPerPost.toString());
						postAnalysis.setNumOfStudents(numOfStudentPerPost);
						

						// pass
						postLevelList.add("0");
						if(insert)
							postAnalysis.setPassOrignal(0);
						else
							postAnalysis.setPassCorrected(0);
						

						// fail
						postLevelList.add("0");
						postAnalysis.setFailOrignal(0);
						if(insert)
							postAnalysis.setFailOrignal(0);
							else
								postAnalysis.setFailCorrected(0);

						// passing grade
						postLevelList.add("0");
						postAnalysis.setBoundary(0);

						// average
						averagePerPost = averagePerPost / pointsPerPostLength;

						if (averagePerPost.isNaN()) {
							postLevelList.add("0");
							postAnalysis.setMean(0.0);
						} else {
							averagePerPost = roundTwoDecimals(averagePerPost);
							postLevelList.add(averagePerPost.toString());
							postAnalysis.setMean(averagePerPost);
						}

						// sd
						Double sdPerPost = Math.sqrt(StatUtils
								.variance(sdPerDoctor));

						if (sdPerPost.isNaN()) {
							postLevelList.add("0");
							postAnalysis.setStandardDeviation(0.0);
						} else {
							sdPerPost = roundTwoDecimals(sdPerPost);
							postLevelList.add(String.valueOf(sdPerPost));
							postAnalysis.setStandardDeviation(sdPerPost);
						}
						// min
						Double min = StatUtils.min(minPerDoctor);
						if (min.isNaN()) {
							postAnalysis.setMinOrignal(0);
							postLevelList.add("0");
						} else
						{
							postLevelList.add(String.valueOf(min.intValue()));
							postAnalysis.setMinOrignal(min.intValue());
						}
						// max
						Double max = StatUtils.min(maxPerDoctor);
						if (max.isNaN()) {
							postLevelList.add("0");
							postAnalysis.setMaxOrignal(0);
						} else
						{
							postLevelList.add(String.valueOf(max.intValue()));
							postAnalysis.setMaxOrignal(max.intValue());
						}
						MapEnvelop postMap = new MapEnvelop();
						postMap.put("p" + post.getId(), postLevelList);
						data.add(postMap);
						
						
						postAnalysis.setOsce(osce);
						postAnalysis.setOscePost(post);
						postAnalysis.persist();
					}
				}
			}
		}

		return data;
	}
	
	public static List<MapEnvelop> retrieveCalulatedData(Long osceId,int analyticType)
	{
		
		List<MapEnvelop> data = new ArrayList<MapEnvelop>();

		if (analyticType == 0)// item analysis
		{
			Osce osce = Osce.findOsce(osceId);
			
			if(ItemAnalysis.countItemAnalysesByOsce(osce) > 0)
			{
			
				
				List<OsceDay> days = osce.getOsce_days();
				
				for(int i=0;i<days.size();i++)
				{
					OsceDay day=days.get(i);
					List<OsceSequence> seqs=day.getOsceSequences();
					
					for(int j=0;j<seqs.size();j++)
					{
						
						OsceSequence seq=seqs.get(j);
						
						//1. find seq level Data
						List<ItemAnalysis> seqDatas=ItemAnalysis.findSeqLevelData(osce, seq);
						
						List<String> seqLevelList = new ArrayList<String>();
						
						
						for(int k=0;k<seqDatas.size();k++)
						{
							ItemAnalysis seqData=seqDatas.get(k);
							
							//create seq level list
							seqLevelList.add(seqData.getStandardDeviation().toString());
				
						}
						
						//add seq level list to MAP
						MapEnvelop seqMap = new MapEnvelop();
						seqMap.put("s" + seq.getId(), seqLevelList);
						data.add(seqMap);
						
						List<OscePost> posts=seq.getOscePosts();
						
						for(int l=0;l<posts.size();l++)
						{
							
							
							OscePost post=posts.get(l);
							//2. find post level Data
							List<ItemAnalysis> postDatas=ItemAnalysis.findPostLevelData(osce, seq,post);
							List<String> postLevelList = new ArrayList<String>();
							
							for(int m=0;m<postDatas.size();m++)
							{
								ItemAnalysis postData=postDatas.get(m);
								
								//2. create post level list
								postLevelList.add(postData.getMissing().toString() +"/"+postData.getMissingPercentage().intValue());

								log.info("missing :" + "p" + post.getId() + "  "
										+ postData.getMissing());

							

									postLevelList.add(postData.getMean().toString());
									postLevelList.add(postData.getStandardDeviation().toString());
							
							

								
								//4.points at post level
								postLevelList.add("-");
								
								//5.frequency at post level
								postLevelList.add("-");
								
								//6. Crohbach's alpha at post level
								
								postLevelList.add(postData.getCronbach().toString());
								
								
								
								
					
							}
							
							
							
							//add post level list to MAP
							MapEnvelop postMap = new MapEnvelop();
							postMap.put("p" + post.getId(), postLevelList);
							data.add(postMap);
							
							// retrieve distict item for this post
							List<ChecklistQuestion> items = retrieveDistinctQuestion(post
									.getId());
							
							for(int n=0;n<items.size();n++)
							{
								ChecklistQuestion item=items.get(n);
								
								//2. find post level Data
								List<ItemAnalysis> itemDatas=ItemAnalysis.findItemLevelData(osce, seq,post,item);
								List<String> questionList = new ArrayList<String>();
								
								for(int p=0;p<itemDatas.size();p++)
								{
									ItemAnalysis itemData=itemDatas.get(p);
									
									questionList.add(itemData.getDeActivate().toString());
									
									//2. create post level list
									questionList.add(itemData.getMissing().toString() +"/"+itemData.getMissingPercentage().intValue()+"%");

									log.info("missing :" + "p" + post.getId() + "  "
											+ itemData.getMissing());

								

									questionList.add(itemData.getMean().toString());
									questionList.add(itemData.getStandardDeviation().toString());
								
								

									
									//4.points at post level
									questionList.add(itemData.getPoints());
									
									//5.frequency at post level
									questionList.add(itemData.getFrequency());
									
									//6. Crohbach's alpha at post level
									
									questionList.add(itemData.getCronbach().toString());
									
									
									
									
						
								}
								
								MapEnvelop questionMap = new MapEnvelop();
								questionMap.put("q" + post.getId() + item.getId(),
										questionList);
								data.add(questionMap);
							}
							
							
							
						}
						
						
						
					}
				}
				
				
				return data;
			}
			else //no data exist, than calculate with all item enable
			{
				
				return calculate(osceId, analyticType, new HashSet<Long>(),null,null);
			}
		}
		else if(analyticType==1)
		{
			Osce osce = Osce.findOsce(osceId);
			
			if(ItemAnalysis.countItemAnalysesByOsce(osce) > 0)
			{
				
				List<PostAnalysis> postLevelDatas=PostAnalysis.findPostLevelDatas(osce);
				
				for(int i=0;i<postLevelDatas.size();i++)
				{
					List<String> postLevelList = new ArrayList<String>();
					PostAnalysis postAnalysis=postLevelDatas.get(i);
					
					postLevelList.add(postAnalysis.getNumOfStudents().toString());
					
					
					
					// pass
					postLevelList.add(postAnalysis.getPassOrignal().toString()+"("+postAnalysis.getPassCorrected()+")");		
				
					
					
					// fail
					postLevelList.add(postAnalysis.getFailOrignal()+"("+postAnalysis.getFailCorrected()+")");
					
					
					// passing grade
					postLevelList.add(postAnalysis.getBoundary().toString());
				
					
					// average
					postLevelList.add(postAnalysis.getMean().toString());
							
					// sd
					postLevelList.add(postAnalysis.getStandardDeviation().toString());
					
					// min
					postLevelList.add(postAnalysis.getMinOrignal().toString());
					
					// max
					postLevelList.add(postAnalysis.getMaxOrignal().toString());
					
					
					OscePost oscePost=postAnalysis.getOscePost();
					
					MapEnvelop postMap = new MapEnvelop();
					postMap.put("p" + oscePost.getId(), postLevelList);
					data.add(postMap);
					
					
					
					List<PostAnalysis> examinerLevelDatas=PostAnalysis.findExaminerLevelDatas(osce,oscePost);
					
					for(int j=0;j<examinerLevelDatas.size();j++)
					{
						List<String> examinerLevelList = new ArrayList<String>();
						PostAnalysis examinerPostAnalysis=examinerLevelDatas.get(j);
						
						//num of student
						examinerLevelList.add(examinerPostAnalysis.getNumOfStudents().toString());
						
						
						
						// pass
						examinerLevelList.add(examinerPostAnalysis.getPassOrignal().toString()+"("+examinerPostAnalysis.getPassCorrected()+")");		
					
						
						
						// fail
						examinerLevelList.add(examinerPostAnalysis.getFailOrignal()+"("+examinerPostAnalysis.getFailCorrected()+")");
						
						
						// passing grade
						examinerLevelList.add(examinerPostAnalysis.getBoundary().toString());
					
						
						// average
						examinerLevelList.add(examinerPostAnalysis.getMean().toString());
								
						// sd
						examinerLevelList.add(examinerPostAnalysis.getStandardDeviation().toString());
						
						// min
						examinerLevelList.add(examinerPostAnalysis.getMinOrignal().toString());
						
						// max
						examinerLevelList.add(examinerPostAnalysis.getMaxOrignal().toString());
						
						MapEnvelop examinerMap = new MapEnvelop();
						examinerMap.put(
								"e" + oscePost.getId() + examinerPostAnalysis.getExaminer().getId(),
								examinerLevelList);
						data.add(examinerMap);
						
					}
					
				}
				

				return data;
				
			}
			else //no data exist return null
			{
				return null;
			}
		}
		
		
		
		
		return null;
	}
	
	public static double percentage(int a, int b) {
		double c = a;
		double d = b;
		if (b == 0)
			return 0;
		else
			return ((c / d) * 100);
	}

	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}

	public static List<Long> findCheckListOptionsByStudentIdAndQuestionId(
			long studId, Long questionId) {
		EntityManager em = entityManager();
		log.info("~QUERY findCheckListOptionsByStudentIdAndQuestionId()");
		String queryString = "select ans.checklistOption.id from Answer as ans where ans.student="
				+ studId + " and ans.checklistQuestion=" + questionId;
		log.info("~QUERY String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString, Long.class);
		List<Long> result = q.getResultList();
		log.info("~QUERY Result : " + result);
		return result;
	}

	public static List<ChecklistQuestion> retrieveDistinctItems(Long osceId) {
		EntityManager em = entityManager();

		String queryString = "select distinct ans.checklistQuestion from Answer as ans where ans.oscePostRoom in (select distinct (oscePostRoom) from Assignment as ass where ass.osceDay in (select id from OsceDay where osce="
				+ osceId + ")) order by ans.checklistQuestion.id";
		TypedQuery<ChecklistQuestion> q = em.createQuery(queryString,
				ChecklistQuestion.class);

		return q.getResultList();
	}

	public static List<Answer> retrieveExportCsvData(Long osceId) {
		EntityManager em = entityManager();
		// String
		// queryString="SELECT a FROM Answer a where oscePostRoom in (select distinct (oscePostRoom) from Assignment where osceDay in (select id from OsceDay where osce="+
		// osceId +")) group by doctor,student";
		String queryString = "SELECT a FROM Answer a where oscePostRoom in (select distinct (oscePostRoom) from Assignment where osceDay in (select id from OsceDay where osce="
				+ osceId + ")) order by a.doctor, a.student";
		TypedQuery<Answer> q = em.createQuery(queryString, Answer.class);

		return q.getResultList();
	}

	public static List<Answer> retrieveExportCsvDataByOscePost(Long osceDayId,
			Long oscePostId) {
		EntityManager em = entityManager();
		// String
		// queryString="SELECT a FROM Answer a where oscePostRoom in (select distinct (oscePostRoom) from Assignment where osceDay in (select id from OsceDay where osce="+
		// osceId +")) group by doctor,student";
		String queryString = "SELECT a FROM Answer a join a.checklistQuestion c join c.checkListTopic t WHERE a.oscePostRoom.oscePost.id = "
				+ oscePostId
				+ " ORDER BY a.doctor, a.student, t.sort_order, c.sequenceNumber, c.id";
		TypedQuery<Answer> q = em.createQuery(queryString, Answer.class);
		return q.getResultList();
	}

	public static Answer findAnswer(Long studentId, Long questionId,
			Long osceDayId) {
		EntityManager em = entityManager();
		String queryString = "SELECT a FROM Answer a where a.student="
				+ studentId
				+ " and a.checklistQuestion="
				+ questionId
				+ " and a.oscePostRoom in (select distinct (oscePostRoom) from Assignment where osceDay.id = "
				+ osceDayId + ") order by a.doctor,a.student";
		TypedQuery<Answer> q = em.createQuery(queryString, Answer.class);
		List<Answer> a = q.getResultList();
		if (a.size() > 0)
			return q.getResultList().get(0);
		else
			return null;

	}
	
	
	
}
