package ch.unibas.medizin.osce.domain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

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

	public static List<Answer> retrieveStudent(Long osceDayId, Long courseId) {
		Log.info("retrieveStudent :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.course="
				+ courseId + " ) order by a.student.name asc";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
		List<Answer> assignmentList = query.getResultList();
		Log.info("retrieveStudent query String :" + queryString);
		Log.info("Assignment List Size :" + assignmentList.size());
		return assignmentList;
	}

	public static List<ChecklistQuestion> retrieveDistinctQuestion(Long postId) {
		Log.info("retrieveDistinctQuestion :");
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.checklistQuestion FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.checkListTopic.sort_order, a.checklistQuestion.sequenceNumber asc";

		TypedQuery<ChecklistQuestion> query = em.createQuery(queryString,
				ChecklistQuestion.class);
		List<ChecklistQuestion> questionList = query.getResultList();

		Log.info("retrieveQuestion query String :" + queryString);
		Log.info("Assignment List Size :" + questionList.size());
		return questionList;
	}

	public static List<Doctor> retrieveDistinctExaminer(Long postId) {
		Log.info("retrieveDistinctExaminer :");
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.doctor FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.sequenceNumber asc";

		TypedQuery<Doctor> query = em.createQuery(queryString, Doctor.class);
		List<Doctor> questionList = query.getResultList();

		Log.info("retrieveQuestion query String :" + queryString);
		Log.info("Assignment List Size :" + questionList.size());
		return questionList;
	}

	public static List<Answer> retrieveQuestionPerPostAndItem(Long postId,
			Long itemId) {
		Log.info("retrieveStudent :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where a.checklistQuestion.id="
				+ itemId
				+ " and a.checklistOption!=null and  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.sequenceNumber asc";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
		List<Answer> assignmentList = query.getResultList();
		Log.info("retrieveQuestion query String :" + queryString);
		Log.info("Assignment List Size :" + assignmentList.size());
		return assignmentList;
	}

	// find now of student given in Answer table for particular post
	public static int countStudent(long dayId) {
		Log.info("countStudent :");
		EntityManager em = entityManager();
		String queryString = "select count(distinct sequenceNumber) from Assignment where type =0 and osceDay="
				+ dayId;

		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		List<Long> assignmentList = query.getResultList();
		Log.info("retrieveQuestion query String :" + queryString);
		Log.info("Assignment List Size :" + assignmentList.size());

		return assignmentList.get(0).intValue();
	}

	public static int countAnswerTableRow(long postId, long itemId) {
		Log.info("countAnswerTableRow :");
		EntityManager em = entityManager();
		String queryString = "SELECT  count(a) FROM Answer as a where a.checklistQuestion="
				+ itemId
				+ "  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Student> query = em.createQuery(queryString, Student.class);
		List<Student> assignmentList = query.getResultList();
		Log.info("retrieveQuestion query String :" + queryString);
		Log.info("Assignment List Size :" + assignmentList.size());

		return assignmentList.size();
	}

	public static int numOfDistinctStudentExamined(Long examinerId, Long postId) {
		Log.info("numOfDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT  count(a) FROM Answer as a where a.doctor="
				+ examinerId
				+ "  and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Long> query = em.createQuery(queryString, Long.class);

		Log.info("numOfDistinctStudentExamined query String :" + queryString);
		// Log.info("Assignment List Size :" + assignmentList.size());

		return query.getResultList().get(0).intValue();
	}

	public static List<Answer> retrieveDistinctStudentExamined(Long examinerId,
			Long postId) {
		Log.info("retrieveDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where a.doctor="
				+ examinerId
				+ " and a.checklistOption!=null and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);

		Log.info("retrieveDistinctStudentExamined query String :" + queryString);
		// Log.info("Assignment List Size :" + assignmentList.size());

		return query.getResultList();
	}

	// [spec
	public static Long retrieveNumberOfDistinctStudentExamined(
			Long examinerId, Long postId) {
		Log.info("retrieveDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT count(DISTINCT a.student) FROM Answer as a where a.doctor="
				+ examinerId
				+ " and a.checklistOption!=null and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		Log.info("numOfDistinctStudentExamined query String :" + queryString);
		return query.getResultList().get(0);
	}

	// spec]

	public static List<MapEnvelop> calculate(Long osceId, int analyticType,
			Set<Long> missingItemId) {
		// List<Map<String,List<String>>> data=new
		// ArrayList<Map<String,List<String>>>();

		List<MapEnvelop> data = new ArrayList<MapEnvelop>();

		if (analyticType == 0)// item analysis
		{
			Osce osce = Osce.findOsce(osceId);
			List<OsceDay> days = osce.getOsce_days();

			for (OsceDay day : days) {
				List<OsceSequence> sequences = day.getOsceSequences();

				// sequence wise calculation
				for (OsceSequence seq : sequences) {

					List<String> seqLevelList = new ArrayList<String>();

					List<OscePost> posts = seq.getOscePosts();

					double totalPointsPerPost[] = new double[posts.size()];

					ArrayList<Double> sumOfPoinstAtSeqLevel = new ArrayList<Double>();

					// post wise calculation
					for (int l = 0; l < posts.size(); l++) {
						OscePost post = posts.get(l);

						List<String> postLevelList = new ArrayList<String>();

						// retrieve distict item for this post
						List<ChecklistQuestion> items = retrieveDistinctQuestion(post
								.getId());

						// total num of student from assignment table
						int totalStudent = countStudent(day.getId());
						Log.info("Total number of student :" + totalStudent);

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

							// 1. calculate missing at item level
							int countAnswerTableRow = itemAnswers.size();
							Log.info("number of student answer :"
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

							// 2. calculate average at item level
							// List<Answer>
							// itemAnswers=retrieveQuestionPerPostAndItem(post.getId(),item.getId());
							Double pointAvg = new Double(0);
							String optionValues = "";
							String frequency = "";

							double[] points = null;
							boolean isMissing = false;
							for (int i = 0; i < missingItemId.size(); i++) {
								if (missingItemId.contains(item.getId())) {
									isMissing = true;
									break;
								}
							}

							if (isMissing)
								points = new double[itemAnswers.size()];
							else
								points = new double[totalStudent];

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
								Log.info("Point of item:"
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
							Log.info("Avg of point at item level :" + pointAvg);
							// sumOfPoinstAtSeqLevel.add(pointAvg);
							// totalPointsPerItem[k]=pointAvg;
							
							average = average + pointAvg;
							pointsPerPostSize=pointsPerPostSize + points.length;
							
							pointAvg = pointAvg / points.length;
							
							pointAvg = roundTwoDecimals(pointAvg);
							
							questionList.add(pointAvg.toString());

							// 3.calculate S.D at item level
							Double pointSD = Math.sqrt(StatUtils
									.variance(points));
							totalPointsPerItem[k] = pointSD;
							pointSD = roundTwoDecimals(pointSD);
							

							questionList.add(pointSD.toString());

							// 4. Point / Option Values
							questionList.add(optionValues);

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

							// 6. Chronbachs alpha
							questionList.add("-");

							MapEnvelop questionMap = new MapEnvelop();
							questionMap.put("q" + post.getId() + item.getId(),
									questionList);
							data.add(questionMap);
						}

						// 1. Missing at post level
						double missingPercentageAtPostLevel = 0;
						if (totalPerPost != 0)
							missingPercentageAtPostLevel = percentage(
									missingAtPostLevel, totalPerPost);

						// missingPercentageAtPostLevel=roundTwoDecimals(missingPercentageAtPostLevel);

						String missing = missingAtPostLevel + "/"
								+ totalPerPost;
						postLevelList.add(missing);

						Log.info("missing :" + "p" + post.getId() + "  "
								+ missing);

						// 2. Average at post level
						average = average / pointsPerPostSize;

						if (average.isNaN())
							postLevelList.add("0.0");
						else {
							average = roundTwoDecimals(average);
							postLevelList.add(average.toString());
						}

						// 3. standard deviation
						if (totalPointsPerItem.length != 0) {
							Double sdPerPost = Math.sqrt(StatUtils
									.variance(totalPointsPerItem));
							sdPerPost = roundTwoDecimals(sdPerPost);
							sumOfPoinstAtSeqLevel.add(sdPerPost);
							postLevelList.add(sdPerPost.toString());
						} else {
							postLevelList.add("0.0");
							sumOfPoinstAtSeqLevel.add(0.0);
						}
						totalPointsPerPost[l] = totalPointsPerPost[l]
								+ StatUtils.sum(totalPointsPerItem);

						MapEnvelop postMap = new MapEnvelop();
						postMap.put("p" + post.getId(), postLevelList);
						data.add(postMap);
					}

					// 1. standard deviation per sequence

					double totalPointsPerSum[] = new double[sumOfPoinstAtSeqLevel
							.size()];
					for (int i = 0; i < sumOfPoinstAtSeqLevel.size(); i++) {
						totalPointsPerSum[i] = sumOfPoinstAtSeqLevel.get(i);
					}

					Log.info("Sum of point per sequence :"
							+ StatUtils.sum(totalPointsPerSum));

					if (totalPointsPerSum.length != 0) {
						Double sdPerSequence = Math.sqrt(StatUtils
								.variance(totalPointsPerSum));
						sdPerSequence = roundTwoDecimals(sdPerSequence);
						seqLevelList.add(sdPerSequence.toString());
					} else {
						seqLevelList.add("0.0");
					}
					MapEnvelop seqMap = new MapEnvelop();

					seqMap.put("s" + seq.getId(), seqLevelList);
					data.add(seqMap);
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

							// student count
							List<Answer> answers = retrieveDistinctStudentExamined(
									doctor.getId(), post.getId());

							Long countOfStudentList = retrieveNumberOfDistinctStudentExamined(
									doctor.getId(), post.getId());

							numOfStudentPerPost = numOfStudentPerPost
									+ countOfStudentList.intValue();

							examinerLevelList.add(new Integer(
									countOfStudentList.intValue()).toString());

							// pass
							examinerLevelList.add("0");

							// fail
							examinerLevelList.add("0");

							// passing grade
							examinerLevelList.add("0");

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

							// sd
							Double sd = Math.sqrt(StatUtils.variance(points));
							sdPerDoctor[a] = sd;
							if (!sd.isNaN())
								sd = roundTwoDecimals(sd);
							
							examinerLevelList.add(String.valueOf(sd));

							// minimum
							double min = StatUtils.min(points);
							minPerDoctor[a] = min;
							examinerLevelList.add(String.valueOf((int) min));

							// maximum
							double max = StatUtils.max(points);
							maxPerDoctor[a] = max;
							examinerLevelList.add(String.valueOf((int) max));

							MapEnvelop examinerMap = new MapEnvelop();
							examinerMap.put(
									"e" + post.getId() + doctor.getId(),
									examinerLevelList);
							data.add(examinerMap);
						}

						// student count
						System.out.println("Post : " + post.getSequenceNumber()
								+ " Student count : " + numOfStudentPerPost);
						postLevelList.add(numOfStudentPerPost.toString());

						// pass
						postLevelList.add("0");

						// fail
						postLevelList.add("0");

						// passing grade
						postLevelList.add("0");

						// average
						averagePerPost = averagePerPost / pointsPerPostLength;

						if (averagePerPost.isNaN()) {
							postLevelList.add("0");
						} else {
							averagePerPost = roundTwoDecimals(averagePerPost);
							postLevelList.add(averagePerPost.toString());
						}

						// sd
						Double sdPerPost = Math.sqrt(StatUtils
								.variance(sdPerDoctor));

						if (sdPerPost.isNaN()) {
							postLevelList.add("0");
						} else {
							sdPerPost = roundTwoDecimals(sdPerPost);
							postLevelList.add(String.valueOf(sdPerPost));
						}
						// min
						Double min = StatUtils.min(minPerDoctor);
						if (min.isNaN()) {
							postLevelList.add("0");
						} else
							postLevelList.add(String.valueOf(min.intValue()));

						// max
						Double max = StatUtils.min(maxPerDoctor);
						if (max.isNaN()) {
							postLevelList.add("0");
						} else
							postLevelList.add(String.valueOf(max.intValue()));

						MapEnvelop postMap = new MapEnvelop();
						postMap.put("p" + post.getId(), postLevelList);
						data.add(postMap);
					}
				}
			}
		}

		return data;
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
		Log.info("~QUERY findCheckListOptionsByStudentIdAndQuestionId()");
		String queryString = "select ans.checklistOption.id from Answer as ans where ans.student="
				+ studId + " and ans.checklistQuestion=" + questionId;
		Log.info("~QUERY String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString, Long.class);
		List<Long> result = q.getResultList();
		Log.info("~QUERY Result : " + result);
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
