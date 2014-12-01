package ch.unibas.medizin.osce.domain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.apache.commons.math.stat.StatUtils;
import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.server.CalculateCronbachValue;
import ch.unibas.medizin.osce.server.OsMaFilePathConstant;
import ch.unibas.medizin.osce.server.upload.ExportStatisticData;
import ch.unibas.medizin.osce.shared.MapEnvelop;
import ch.unibas.medizin.osce.shared.NoteType;

import com.google.gwt.requestfactory.server.RequestFactoryServlet;


@RooJavaBean
@RooToString
@RooEntity
public class Answer {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	String answer;

	@ManyToOne
	Student student;

	@ManyToOne
	ChecklistQuestion checklistQuestion;

	@ManyToOne
	ChecklistOption checklistOption;

	/*@ManyToOne
	ChecklistCriteria checklistCriteria;*/
	
	@ManyToOne
	ChecklistItem checklistItem;

	@ManyToOne
	Doctor doctor;

	@ManyToOne
	OscePostRoom oscePostRoom;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date answerTimestamp;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistCriteria")
	private List<AnswerCheckListCriteria> itemAnalysis = new ArrayList<AnswerCheckListCriteria>();
	
	
	/*@ManyToMany(cascade = CascadeType.ALL)
	private Set<ChecklistCriteria> checklistCriteria = new HashSet<ChecklistCriteria>();*/
	
	private static Logger log = Logger.getLogger(Answer.class);

	public static List<Answer> retrieveStudent(Long osceDayId, Long courseId) {
		//log.info("retrieveStudent :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.course="
				+ courseId + " ) order by a.student.name asc";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
		List<Answer> assignmentList = query.getResultList();
		//log.info("retrieveStudent query String :" + queryString);
		//log.info("Assignment List Size :" + assignmentList.size());
		return assignmentList;
	}

	public static List<ChecklistQuestion> retrieveDistinctQuestion(Long postId) {
		//log.info("retrieveDistinctQuestion :");
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.checklistQuestion FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.checkListTopic.sort_order, a.checklistQuestion.sequenceNumber asc";

		TypedQuery<ChecklistQuestion> query = em.createQuery(queryString,
				ChecklistQuestion.class);
		List<ChecklistQuestion> questionList = query.getResultList();

		//log.info("retrieveQuestion query String :" + queryString);
		//log.info("Assignment List Size :" + questionList.size());
		return questionList;
	}
	
	public static List<ChecklistItem> retrieveDistinctQuestionItem(Long postId) {
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.checklistItem FROM Answer as a where a.checklistItem.parentItem IS NOT NULL AND a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistItem.parentItem.sequenceNumber, a.checklistItem.sequenceNumber asc";

		TypedQuery<ChecklistItem> query = em.createQuery(queryString,
				ChecklistItem.class);
		List<ChecklistItem> questionList = query.getResultList();

		//log.info("retrieveQuestion query String :" + queryString);
		//log.info("Assignment List Size :" + questionList.size());
		return questionList;
	}

	public static List<Doctor> retrieveDistinctExaminer(Long postId) {
		//log.info("retrieveDistinctExaminer :");
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.doctor FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.sequenceNumber asc";

		TypedQuery<Doctor> query = em.createQuery(queryString, Doctor.class);
		List<Doctor> questionList = query.getResultList();

		//log.info("retrieveQuestion query String :" + queryString);
		//log.info("Assignment List Size :" + questionList.size());
		return questionList;
	}
	
	public static List<Doctor> retrieveDistinctExaminerByItem(Long postId) {
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.doctor FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistItem.sequenceNumber asc";
		TypedQuery<Doctor> query = em.createQuery(queryString, Doctor.class);
		List<Doctor> questionList = query.getResultList();
		return questionList;
	}
	
	public static List<Student> retrieveDistinctStudent(Long postId) {
		//log.info("retrieveDistinctExaminer :");
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.student FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.sequenceNumber asc";

		TypedQuery<Student> query = em.createQuery(queryString, Student.class);
		List<Student> questionList = query.getResultList();

		//log.info("retrieveQuestion query String :" + queryString);
		//log.info("Assignment List Size :" + questionList.size());
		return questionList;
	}
	
	public static List<Student> retrieveDistinctStudentByItem(Long postId) {
		EntityManager em = entityManager();
		String queryString = "SELECT  distinct a.student FROM Answer as a where  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistItem.sequenceNumber asc";
		TypedQuery<Student> query = em.createQuery(queryString, Student.class);
		List<Student> questionList = query.getResultList();
		return questionList;
	}
	
	public static List<Answer> retrieveQuestionPerPostAndItem(Long postId,
			Long itemId) {
		//log.info("retrieveStudent :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where a.checklistQuestion.id="
				+ itemId
				+ " and a.checklistOption!=null and  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistQuestion.sequenceNumber asc";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
		List<Answer> assignmentList = query.getResultList();
		//log.info("retrieveQuestion query String :" + queryString);
		//log.info("Assignment List Size :" + assignmentList.size());
		return assignmentList;
	}
	
	public static List<Answer> retrieveQuestionPerPostAndQuestionItem(Long postId,Long itemId) {
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where a.checklistItem.id="
				+ itemId
				+ " and a.checklistOption!=null and  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) order by a.checklistItem.sequenceNumber asc";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);
		List<Answer> assignmentList = query.getResultList();
		return assignmentList;
	}

	// find now of student given in Answer table for particular post
	public static int countStudent(long dayId) {
		//log.info("countStudent :");
		EntityManager em = entityManager();
		String queryString = "select count(distinct sequenceNumber) from Assignment where type =0 and osceDay="
				+ dayId;

		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		List<Long> assignmentList = query.getResultList();
		//log.info("retrieveQuestion query String :" + queryString);
		//log.info("Assignment List Size :" + assignmentList.size());

		return assignmentList.get(0).intValue();
	}

	public static int countAnswerTableRow(long postId, long itemId) {
		//log.info("countAnswerTableRow :");
		EntityManager em = entityManager();
		String queryString = "SELECT  count(a) FROM Answer as a where a.checklistQuestion="
				+ itemId
				+ "  a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Student> query = em.createQuery(queryString, Student.class);
		List<Student> assignmentList = query.getResultList();
		//log.info("retrieveQuestion query String :" + queryString);
		//log.info("Assignment List Size :" + assignmentList.size());

		return assignmentList.size();
	}

	public static int numOfDistinctStudentExamined(Long examinerId, Long postId) {
		//log.info("numOfDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT  count(a) FROM Answer as a where a.doctor="
				+ examinerId
				+ "  and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Long> query = em.createQuery(queryString, Long.class);

		//log.info("numOfDistinctStudentExamined query String :" + queryString);
		// Log.info("Assignment List Size :" + assignmentList.size());

		return query.getResultList().get(0).intValue();
	}

	public static List<Answer> retrieveDistinctStudentExamined(Long examinerId,
			Long postId) {
		//log.info("retrieveDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT  a FROM Answer as a where a.doctor="
				+ examinerId
				+ " and a.checklistOption!=null and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";

		TypedQuery<Answer> query = em.createQuery(queryString, Answer.class);

		//log.info("retrieveDistinctStudentExamined query String :" + queryString);
		// Log.info("Assignment List Size :" + assignmentList.size());

		return query.getResultList();
	}

	// [spec
	public static Long retrieveNumberOfDistinctStudentExamined(
			Long examinerId, Long postId) {
		//log.info("retrieveDistinctStudentExamined :");
		EntityManager em = entityManager();
		String queryString = "SELECT count(DISTINCT a.student) FROM Answer as a where a.doctor="
				+ examinerId
				+ " and a.checklistOption!=null and a.oscePostRoom in(select opr.id from OscePostRoom as opr where  opr.oscePost="
				+ postId + " ) ";
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		//log.info("numOfDistinctStudentExamined query String :" + queryString);
		return query.getResultList().get(0);
	}

	// spec]

	public static List<MapEnvelop> calculate(Long osceId, int analyticType, Set<Long> missingItemId,List<String> examinerId,List<Integer> addPoints, List<String> postId, List<Long> queId) {
		// List<Map<String,List<String>>> data=new
		// ArrayList<Map<String,List<String>>>();
		List<MapEnvelop> data = new ArrayList<MapEnvelop>();
		if (analyticType == 0)// item analysis
		{
			data = calculateItemAnalysis(osceId, missingItemId);

		} else if (analyticType == 1)// post analysis
		{
			data = calculatePostAnalysis(osceId, examinerId, addPoints, postId, queId);
		}
		
		if ((addPoints != null && addPoints.size() > 0) || (queId != null && queId.size() > 0))
		{
			List<MapEnvelop> tempData = new ArrayList<MapEnvelop>();
			Set<Long>	tempMissingItemId = new HashSet<Long>();
			tempData = calculateItemAnalysis(osceId, tempMissingItemId);
		}

		if (missingItemId != null && missingItemId.size() > 0) // if missing item selected then post analysis data is also calculated
		{
			List<MapEnvelop> tempData = new ArrayList<MapEnvelop>();
			tempData = calculatePostAnalysis(osceId, examinerId, addPoints, postId, queId);
		}
		
		return data;
	}

	private static List<MapEnvelop> calculatePostAnalysis(Long osceId, List<String> examinerId, List<Integer> addPoints, List<String> postId, List<Long> queId) {
		
		List<MapEnvelop> data = new ArrayList<MapEnvelop>();
		
		Osce osce = Osce.findOsce(osceId);
		List<OsceDay> days = osce.getOsce_days();
		CalculateCronbachValue calculateCronbachValue;
		CalculateCronbachValue calculatePostCronbachValue;
		

		for (OsceDay day : days) {
			List<OsceSequence> sequences = day.getOsceSequences();

			// sequence wise calculation
			for (OsceSequence seq : sequences) {
				List<OscePost> posts = seq.getOscePosts();

				// post wise calculation
				for (int l = 0; l < posts.size(); l++) {
					calculatePostCronbachValue = new CalculateCronbachValue();
					
					OscePost post = posts.get(l);
					
					String postKey = "p" + post.getId();
					Long impressionQueId = null;
					if (postId.contains(postKey))
					{
						int index = postId.indexOf(postKey);
						impressionQueId = queId.get(index);
					}
					
					String fileName;
					if (post.getStandardizedRole() != null)
						fileName = post.getStandardizedRole().getShortName() + "_" + seq.getLabel() +".csv";
					else
						fileName = "post" + post.getId() + "_" + seq.getLabel() +".csv";
					
					fileName = fileName.replaceAll("\\\\", "");
					fileName = fileName.replaceAll("\\/", "");
					fileName = fileName.replaceAll(" ", "");
						
					List<String> valueList = ExportStatisticData.createOscePostCSV(RequestFactoryServlet.getThreadLocalRequest(),RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext(), post.getId(), fileName, examinerId, addPoints, impressionQueId);

					if(valueList != null && valueList.size() == 2)
					{
						fileName = valueList.get(0); // return filename
						if(valueList.get(1) != null)
							impressionQueId = Long.parseLong(valueList.get(1)); // return impression question
					}
					
					Map<String, String> postResultMap = calculatePostCronbachValue.calculateOscePostResult(fileName, post.getId());
					
					String postPassingStr = postResultMap.get("passMark") == null ? "0" : postResultMap.get("passMark");
					
					//List<Doctor> doctors = retrieveDistinctExaminer(post.getId());
					
					List<Doctor> doctors = retrieveDistinctExaminerByItem(post.getId());

					List<String> postLevelList = new ArrayList<String>();

					Integer numOfStudentPerPost = 0;
					
					//Integer maxCountValue = OscePost.findMaxValueOfCheckListQuestionByOscePost(post.getId());
					Integer maxCountValue = OscePost.findMaxValueOfCheckListQuestionItemByOscePost(post.getId());

					Double averagePerPost = 0.0;
					int pointsPerPostLength=0;
					double sdPerDoctor[] = new double[doctors.size()];
					double minPerDoctor[] = new double[doctors.size()];
					double maxPerDoctor[] = new double[doctors.size()];
					
					int postPassCtr = 0;
					int postFailCtr = 0;
					
					for (int a = 0; a < doctors.size(); a++) {
						
						calculateCronbachValue = new CalculateCronbachValue();
						
						Doctor doctor = doctors.get(a);
						String filename;
						if (post.getStandardizedRole() != null)
							filename = post.getStandardizedRole().getShortName() + "_" + doctor.getName() + "_" + seq.getLabel() + ".csv";
						else
							filename = "post" + post.getId() + "_" + doctor.getName() + "_" + seq.getLabel() + ".csv";
						
						filename = filename.replaceAll("\\\\", "");
						filename = filename.replaceAll("\\/", "");
						fileName = fileName.replaceAll(" ", "");
							
						Integer addPoint = 0;
						String key="p"+post.getId()+"e"+doctor.getId();
						if (examinerId.contains(key))
						{
							int index=examinerId.indexOf(key);
							addPoint = addPoints.get(index);
						}
						else
						{
							addPoint = PostAnalysis.findAddPointByExaminerAndOscePost(post.getId(), doctor.getId());
						}
						
						filename = ExportStatisticData.createExaminerCSV(RequestFactoryServlet.getThreadLocalRequest(),RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext(), post.getId(), doctor.getId(), filename, addPoint, impressionQueId);
						
						Map<String, String> resultMap = calculateCronbachValue.calculateResult(filename, post.getId(), postPassingStr);
						
						List<String> examinerLevelList = new ArrayList<String>();

						PostAnalysis postAnalysis=PostAnalysis.findExaminerLevelData(osce, post, doctor);
						boolean insert=false;
						if(postAnalysis==null)//insert
						{
							postAnalysis=new PostAnalysis();
							insert=true;
						}
						
						
						//login of save add point in database
						if(examinerId.contains(key))
						{
							int index=examinerId.indexOf(key);
							postAnalysis.setPointsCorrected(addPoints.get(index));
						}
						else
						{
							postAnalysis.setPointsCorrected(0);
						}
						
						// student count
						//List<Answer> answers = retrieveDistinctStudentExamined(doctor.getId(), post.getId());

						Long countOfStudentList = retrieveNumberOfDistinctStudentExamined(doctor.getId(), post.getId());

						numOfStudentPerPost = numOfStudentPerPost + countOfStudentList.intValue();

						examinerLevelList.add(new Integer(countOfStudentList.intValue()).toString());

						postAnalysis.setNumOfStudents(countOfStudentList.intValue());
						
						// pass
						String passCountStr = resultMap.get("passCount") == null ? "0" : resultMap.get("passCount");
						Integer passCount = Integer.parseInt(passCountStr);							
						//examinerLevelList.add("0(0)");
						
						if(insert)
						{
							postAnalysis.setPassOrignal(passCount);
							postAnalysis.setPassCorrected(0);
						}
						else
						{
							if (!postAnalysis.getPassOrignal().equals(passCount))
								postAnalysis.setPassCorrected(passCount);
						}
						
						examinerLevelList.add(postAnalysis.getPassOrignal().toString() + "("+ postAnalysis.getPassCorrected().toString() +")");
						postPassCtr += passCount;

						// fail
						String failCountStr = resultMap.get("failCount") == null ? "0" : resultMap.get("failCount");
						
						//examinerLevelList.add("0(0)");
						if(insert)
						{
							postAnalysis.setFailOrignal(Integer.parseInt(failCountStr));
							postAnalysis.setFailCorrected(0);
						}
						else
						{
							if (!postAnalysis.getFailOrignal().equals(Integer.parseInt(failCountStr)))
								postAnalysis.setFailCorrected(Integer.parseInt(failCountStr));
						}
						
						examinerLevelList.add(postAnalysis.getFailOrignal().toString() + "("+ postAnalysis.getFailCorrected().toString() +")");
						postFailCtr += Integer.parseInt(failCountStr);

						// passing grade
						//String passingGradeStr = resultMap.get("passMark") == null ? "0" : resultMap.get("passMark"); 
						examinerLevelList.add("-");
						postAnalysis.setBoundary(0.0);							

						// average
						/*double points[] = new double[answers.size()];
						for (int i = 0; i < answers.size(); i++) {
							points[i] = new Double(answers.get(i)
									.getChecklistOption().getValue());

						}
						Double average = StatUtils.mean(points);		
						
						averagePerPost = averagePerPost + StatUtils.sum(points);
						pointsPerPostLength=pointsPerPostLength+points.length;
						if (!average.isNaN())
							average = roundTwoDecimals(average);*/
						
						
						
						Double average,averagePer;
						average = resultMap.get("mean") == null ? 0 : Double.parseDouble(resultMap.get("mean"));
						averagePer = average;
						if (maxCountValue > 0)
							averagePer = (average * 100) / maxCountValue;
						
						examinerLevelList.add(String.format("%.2f", averagePer));
						postAnalysis.setMean(average);
						

						// sd
						/*Double sd = Math.sqrt(StatUtils.variance(points));
						sdPerDoctor[a] = sd;
						if (!sd.isNaN())
							sd = roundTwoDecimals(sd);*/
						Double sd,sdPer;
						sd = resultMap.get("sd") == null ? 0 : Double.parseDouble(resultMap.get("sd"));
						sdPer = sd;
						if (maxCountValue > 0)
							sdPer = (sd * 100)/maxCountValue;
						
						examinerLevelList.add(String.format("%.2f", sdPer));
						postAnalysis.setStandardDeviation(sd);

						// minimum
						/*double min = StatUtils.min(points);
						minPerDoctor[a] = min;*/
						int min, minPer;
						min = resultMap.get("min") == null ? 0 : Integer.parseInt(resultMap.get("min"));
						minPer = min;
						if (maxCountValue > 0)
							minPer = (minPer * 100) / maxCountValue;
						examinerLevelList.add(String.valueOf(minPer));
						postAnalysis.setMinOrignal(min);

						// maximum
						/*double max = StatUtils.max(points);
						maxPerDoctor[a] = max;*/
						int max, maxPer;
						max = resultMap.get("max") == null ? 0 : Integer.parseInt(resultMap.get("max"));
						maxPer = max;
						if (maxCountValue > 0)
							maxPer = (maxPer * 100) / maxCountValue;
						examinerLevelList.add(String.valueOf(maxPer));
						
						//add point
						examinerLevelList.add(addPoint.toString());
						
						postAnalysis.setMaxOrignal(max);					
						
						MapEnvelop examinerMap = new MapEnvelop();
						examinerMap.put("e" + post.getId() + doctor.getId(), examinerLevelList);
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
					/*System.out.println("Post : " + post.getSequenceNumber()
							+ " Student count : " + numOfStudentPerPost);*/
					postLevelList.add(numOfStudentPerPost.toString());
					postAnalysis.setNumOfStudents(numOfStudentPerPost);
					

					// pass
					//postLevelList.add(String.valueOf(postPassCtr));
					if(insert)
					{
						postAnalysis.setPassOrignal(postPassCtr);
						postAnalysis.setPassCorrected(0);
					}
					else
					{
						if (postAnalysis.getPassOrignal() != postPassCtr)
							postAnalysis.setPassCorrected(postPassCtr);
					}
					
					postLevelList.add(postAnalysis.getPassOrignal().toString() + "("+ postAnalysis.getPassCorrected().toString() +")");

					// fail
					//postLevelList.add(String.valueOf(postFailCtr));
					//postAnalysis.setFailOrignal(postFailCtr);
					if(insert)
					{
						postAnalysis.setFailOrignal(postFailCtr);
						postAnalysis.setFailCorrected(0);
					}
					else
					{
						if (postAnalysis.getFailOrignal() != postFailCtr)
							postAnalysis.setFailCorrected(postFailCtr);
					}
					
					postLevelList.add(postAnalysis.getFailOrignal().toString() + "(" + postAnalysis.getFailCorrected().toString() + ")");

					// passing grade
					
					postLevelList.add(postPassingStr);
					postAnalysis.setBoundary(Double.parseDouble(postPassingStr));

					// average
					/*averagePerPost = averagePerPost / pointsPerPostLength;

					if (averagePerPost.isNaN()) {
						postLevelList.add("0");
						postAnalysis.setMean(0.0);
					} else {
						averagePerPost = roundTwoDecimals(averagePerPost);
						postLevelList.add(averagePerPost.toString());
						postAnalysis.setMean(averagePerPost);
					}*/
					String meanStr = postResultMap.get("mean") == null ? "0" : postResultMap.get("mean");
					Double postMeanPer = Double.parseDouble(meanStr);
					
					if (maxCountValue > 0)
						postMeanPer = (postMeanPer * 100) / maxCountValue;
					
					postLevelList.add(String.format("%.2f", postMeanPer));
					postAnalysis.setMean(Double.parseDouble(meanStr));

					// sd
					/*Double sdPerPost = Math.sqrt(StatUtils
							.variance(sdPerDoctor));

					if (sdPerPost.isNaN()) {
						postLevelList.add("0");
						postAnalysis.setStandardDeviation(0.0);
					} else {
						sdPerPost = roundTwoDecimals(sdPerPost);
						postLevelList.add(String.valueOf(sdPerPost));
						postAnalysis.setStandardDeviation(sdPerPost);
					}*/
					String sdStr = postResultMap.get("sd") == null ? "0" : postResultMap.get("sd");
					Double postSdPer = Double.parseDouble(sdStr);
					
					if (maxCountValue > 0)
						postSdPer = (postSdPer * 100) / maxCountValue;
						
					postLevelList.add(String.format("%.2f", postSdPer));
					postAnalysis.setStandardDeviation(Double.parseDouble(sdStr));
					
					// min
					/*Double min = StatUtils.min(minPerDoctor);
					if (min.isNaN()) {
						postAnalysis.setMinOrignal(0);
						postLevelList.add("0");
					} else
					{
						postLevelList.add(String.valueOf(min.intValue()));
						postAnalysis.setMinOrignal(min.intValue());
					}*/
					String minStr = postResultMap.get("min") == null ? "0" : postResultMap.get("min");
					int minPer = Integer.parseInt(minStr);
					if (maxCountValue > 0)
						minPer = (minPer * 100) / maxCountValue;
					postLevelList.add(String.valueOf(minPer));
					postAnalysis.setMinOrignal(Integer.parseInt(minStr));
					
					// max
					/*Double max = StatUtils.min(maxPerDoctor);
					if (max.isNaN()) {
						postLevelList.add("0");
						postAnalysis.setMaxOrignal(0);
					} else
					{
						postLevelList.add(String.valueOf(max.intValue()));
						postAnalysis.setMaxOrignal(max.intValue());
					}*/
					String maxStr = postResultMap.get("max") == null ? "0" : postResultMap.get("max");
					int maxPer = Integer.parseInt(maxStr);
					if (maxCountValue > 0)
						maxPer = (maxPer * 100) / maxCountValue;
					postLevelList.add(String.valueOf(maxPer));
					postAnalysis.setMaxOrignal(Integer.parseInt(maxStr));
					
					//impression question
					if(impressionQueId != null)
					{
						//postAnalysis.setChecklistQuestion(ChecklistQuestion.findChecklistQuestion(impressionQueId));
						postAnalysis.setChecklistItem(ChecklistItem.findChecklistItem(impressionQueId));
						postLevelList.add(impressionQueId.toString());
					}
					else
						postLevelList.add("0");
					
					MapEnvelop postMap = new MapEnvelop();
					postMap.put("p" + post.getId(), postLevelList);
					data.add(postMap);
					
					
					postAnalysis.setOsce(osce);
					postAnalysis.setOscePost(post);
					postAnalysis.persist();
				}
			}
		}
		
		return data;
	}

	private static List<MapEnvelop> calculateItemAnalysis(Long osceId, Set<Long> missingItemId) {
		
		List<MapEnvelop> data = new ArrayList<MapEnvelop>();
		
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
						
						String fileName;
						if (post.getStandardizedRole() != null)
							fileName = "Day"+ (dayCtr) + "_" + post.getStandardizedRole().getShortName() + "_" + seq.getLabel() + ".csv";
						else
							fileName = "Day"+ (dayCtr) + "_" + "post" + post.getId() + "_" + seq.getLabel() + ".csv";

						fileName = fileName.replaceAll("\\\\", "");
						fileName = fileName.replaceAll("\\/", "");
						fileName = fileName.replaceAll(" ", "");
						
						fileName = RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getRealPath(OsMaFilePathConstant.assignmentHTML + fileName);
						
						calculateCronbachValue.countValue(fileName, missingItemId);
						
						Map<String, String> cronValMap = calculateCronbachValue.getCronbachValueMap();
						
						Map<String, String> meanMap = calculateCronbachValue.getMeanMap();
						
						Map<String, String> sdMap = calculateCronbachValue.getSdMap();
						
						String overAllCronbachVal = cronValMap.get("overall");
						
						String overAllMean = meanMap.get("overall");
						
						String overAllSd = sdMap.get("overall");
						
						List<String> postLevelList = new ArrayList<String>();

						// retrieve distict item for this post
						//List<ChecklistQuestion> items = retrieveDistinctQuestion(post.getId());
						
						List<ChecklistItem> items = retrieveDistinctQuestionItem(post.getId());

						// total num of student from assignment table
						int totalStudent = countStudent(day.getId());
						//log.info("Total number of student :" + totalStudent);

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
							//ChecklistQuestion item = items.get(k);
							ChecklistItem item = items.get(k);
							// question/item wise calculation
							// Map<String,List<String>> questionMap=new
							// HashMap<String, List<String>>();
							// data.add(questionMap);
							List<String> questionList = new ArrayList<String>();

							// list of answer table data for particular post and
							// item. Number of row should be equal to total num
							// of student.
							//List<Answer> itemAnswers = retrieveQuestionPerPostAndItem(post.getId(), item.getId());
							List<Answer> itemAnswers = retrieveQuestionPerPostAndQuestionItem(post.getId(), item.getId());
							
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
							//log.info("number of student answer :" + countAnswerTableRow);
							int missingAtItemLevel = totalStudent - countAnswerTableRow;
							missingAtPostLevel = missingAtPostLevel + missingAtItemLevel;
							double missingPercentageAtItemLevel = 0;
							if (totalStudent != 0)
								missingPercentageAtItemLevel = percentage(missingAtItemLevel, totalStudent);

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
							
							int[] optionCounts = new int[item.getCheckListOptions().size()];
							List<String> optionValuesList = new ArrayList<String>();
							for (int i = 0; i < item.getCheckListOptions().size(); i++) {
								
								ChecklistOption option = item.getCheckListOptions().get(i);

								if (optionValues.equals(""))
									optionValues = option.getValue();
								else
									optionValues = optionValues + "/" + option.getValue();

								optionValuesList.add(option.getValue());
							}
							
							for (int i = 0; i < itemAnswers.size(); i++) {
								Answer itemAnswer = itemAnswers.get(i);
								// point=point+new
								// Double(itemAnswer.getChecklistOption().getValue());
								//log.info("Point of item:" + itemAnswer.getChecklistOption().getValue());
								points[i] = new Double(itemAnswer.getChecklistOption().getValue());

								for (int j = 0; j < optionValuesList.size(); j++) {

									if (itemAnswer.getChecklistOption().getValue().equals(optionValuesList.get(j))) {
										optionCounts[j]++;
										break;
									}
								}

							}

							// point=point/itemAnswers.size();
							pointAvg = StatUtils.sum(points);
							//log.info("Avg of point at item level :" + pointAvg);
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
									frequency = frequency + Math.round(roundTwoDecimals(percentage(optionCounts[j], itemAnswers.size())));
								else
									frequency = frequency + "/" + Math.round(roundTwoDecimals(percentage(optionCounts[j], itemAnswers.size())));
							}
							
							questionList.add(frequency);
							
							itemAnalysis.setFrequency(frequency);
							// 6. Chronbachs alpha
							//questionList.add("-");
							String val = cronValMap.get(item.getId().toString());
							if (val == null || val.equals("NaN"))
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
							questionMap.put("q" + post.getId() + item.getId(), questionList);
							data.add(questionMap);
							
							//save data at item level
							itemAnalysis.setOsce(osce);
							itemAnalysis.setChecklistItem(item);
							itemAnalysis.setOscePost(post);
							itemAnalysis.setOsceSequence(seq);
							itemAnalysis.persist();
						}
						
						
						//save data at post level
						ItemAnalysis itemAnalysis=new ItemAnalysis();
						
						// 1. Missing at post level
						double missingPercentageAtPostLevel = 0;
						if (totalPerPost != 0)
							missingPercentageAtPostLevel = percentage(missingAtPostLevel, totalPerPost);

						// missingPercentageAtPostLevel=roundTwoDecimals(missingPercentageAtPostLevel);

						String missing = missingAtPostLevel + "/" + totalPerPost;
						
						postLevelList.add(missing);

						//log.info("missing :" + "p" + post.getId() + "  " + missing);

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

					//log.info("Sum of point per sequence :" + StatUtils.sum(totalPointsPerSum));
					
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
								//postLevelList.add(postData.getMissing().toString() +"/"+postData.getMissingPercentage().intValue());
								postLevelList.add(postData.getMissing().toString() +"/"+postData.getMissingPercentage());

								//log.info("missing :" + "p" + post.getId() + "  " + postData.getMissing());
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
							//List<ChecklistQuestion> items = retrieveDistinctQuestion(post.getId());
							
							List<ChecklistItem> items = retrieveDistinctQuestionItem(post.getId());
							
							for(int n=0;n<items.size();n++)
							{
								//ChecklistQuestion item=items.get(n);
								ChecklistItem item=items.get(n);
								
								//2. find post level Data
								List<ItemAnalysis> itemDatas=ItemAnalysis.findQuestionItemLevelData(osce, seq,post,item);
								List<String> questionList = new ArrayList<String>();
								
								for(int p=0;p<itemDatas.size();p++)
								{
									ItemAnalysis itemData=itemDatas.get(p);
									
									questionList.add(itemData.getDeActivate().toString());
									
									//2. create post level list
									questionList.add(itemData.getMissing().toString() +"/"+itemData.getMissingPercentage().intValue()+"%");

									//log.info("missing :" + "p" + post.getId() + "  "+ itemData.getMissing());

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
								questionMap.put("q" + post.getId() + item.getId(), questionList);
								data.add(questionMap);
							}
						}
					}
				}
				
				return data;
			}
			else //no data exist, than calculate with all item enable
			{
				return calculate(osceId, analyticType, new HashSet<Long>(),null,null,null,null);
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
				
					OscePost oscePost=postAnalysis.getOscePost();
					
					//Integer maxCountValue = OscePost.findMaxValueOfCheckListQuestionByOscePost(oscePost.getId());
					Integer maxCountValue = OscePost.findMaxValueOfCheckListQuestionItemByOscePost(oscePost.getId());
					
					postLevelList.add(postAnalysis.getNumOfStudents().toString());
					
					// pass
					postLevelList.add(postAnalysis.getPassOrignal().toString()+"("+postAnalysis.getPassCorrected()+")");		
					
					// fail
					postLevelList.add(postAnalysis.getFailOrignal()+"("+postAnalysis.getFailCorrected()+")");
					
					// passing grade
					postLevelList.add(postAnalysis.getBoundary().toString());
					
					// average
					Double postMeanPer = postAnalysis.getMean();
					if (maxCountValue > 0)
						postMeanPer = (postMeanPer * 100) / maxCountValue;
					postLevelList.add(String.format("%.2f", postMeanPer));
							
					// sd
					Double postSdPer = postAnalysis.getStandardDeviation();
					if (maxCountValue > 0)
						postSdPer = (postSdPer * 100) / maxCountValue;
					postLevelList.add(String.format("%.2f", postSdPer));
					
					// min
					Integer postMinPer = postAnalysis.getMinOrignal();
					if (maxCountValue > 0)
						postMinPer = (postMinPer * 100) / maxCountValue;
					postLevelList.add(String.valueOf(postMinPer));
					
					// max
					Integer postMaxPer = postAnalysis.getMaxOrignal();
					if (maxCountValue > 0)
						postMaxPer = (postMaxPer * 100) / maxCountValue;
					postLevelList.add(String.valueOf(postMaxPer));		
					
					//impression question
					if (postAnalysis.getChecklistQuestion() != null)
						postLevelList.add(String.valueOf(postAnalysis.getChecklistQuestion().getId()));
					else
						postLevelList.add("0");
					
					MapEnvelop postMap = new MapEnvelop();
					String postKey = "p" + oscePost.getId();
					log.info("POST KEY : " + postKey);
					postMap.put(postKey, postLevelList);
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
						if (examinerPostAnalysis.getBoundary() != null)
							examinerLevelList.add( examinerPostAnalysis.getBoundary().equals(Double.valueOf(0)) ? "-" : examinerPostAnalysis.getBoundary().toString());
						else
							examinerLevelList.add("-");
						
						// average
						Double meanPer = examinerPostAnalysis.getMean();
						if (maxCountValue > 0)
							meanPer = (meanPer * 100) / maxCountValue;
						examinerLevelList.add(String.format("%.2f", meanPer));
								
						// sd
						Double sdPer = examinerPostAnalysis.getStandardDeviation();
						if (maxCountValue > 0)
							sdPer = (sdPer * 100) / maxCountValue;
						examinerLevelList.add(String.format("%.2f", sdPer));
					
						
						// min
						Integer minPer = examinerPostAnalysis.getMinOrignal();
						if (maxCountValue > 0)
							minPer = (minPer * 100) / maxCountValue;
						
						examinerLevelList.add(String.valueOf(minPer));
						
						// max
						Integer maxPer = examinerPostAnalysis.getMaxOrignal();
						if (maxCountValue > 0)
							maxPer = (maxPer * 100) / maxCountValue; 
						
						examinerLevelList.add(String.valueOf(maxPer));
						
						//add point
						//add point
						examinerLevelList.add(String.valueOf(examinerPostAnalysis.getPointsCorrected()));
						
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
	public static double percentage(double a, double b) {
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
		/*String queryString = "select ans.checklistOption.id from Answer as ans where ans.student="
				+ studId + " and ans.checklistQuestion=" + questionId;*/
         String queryString = "select ans.checklistOption.id from Answer as ans where ans.student="
				+ studId + " and ans.checklistItem=" + questionId;
				
                log.info("~QUERY String: " + queryString);
		TypedQuery<Long> q = em.createQuery(queryString, Long.class);
		List<Long> result = q.getResultList();
		log.info("~QUERY Result : " + result);
		return result;
	}
	
	public static List<Long> findCheckListOptionsByStudentIdAndQuestionIdMinOption(long studId, Long questionId) {
		EntityManager em = entityManager();
		List<Long> result = new ArrayList<Long>();
		/*String queryString = "select ans.checklistOption.id from Answer as ans where ans.student=" + studId + " and ans.checklistQuestion=" + questionId;*/
		
	    String queryString = "select ans.checklistOption.id from Answer as ans where ans.student=" + studId + " and ans.checklistItem=" + questionId;
        TypedQuery<Long> q = em.createQuery(queryString, Long.class);
		result = q.getResultList();
		String sql = "SELECT co FROM ChecklistOption co WHERE co.checklistItem.id = " + questionId + " ORDER BY co.value DESC";
		
		/*String sql = "SELECT co FROM ChecklistOption co WHERE co.checklistQuestion.id = " + questionId + " ORDER BY co.value DESC";*/
		TypedQuery<ChecklistOption> query = em.createQuery(sql, ChecklistOption.class);
		
		if (query.getResultList() != null && query.getResultList().size() > 0)
		{
			ChecklistOption option = query.getResultList().get(0);
			
			if (result.contains(option.getId()))
				result.clear();
		}
		
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
	
	public static List<Answer> retrieveExportCsvDataByItemAndOscePost(Long osceDayId, Long oscePostId) {
		EntityManager em = entityManager();
		String queryString = "SELECT a FROM Answer a WHERE a.checklistItem.parentItem IS NOT NULL AND a.oscePostRoom.oscePost.id = " + oscePostId + " ORDER BY a.doctor, a.student, a.checklistItem.parentItem.sequenceNumber, a.checklistItem.sequenceNumber, a.checklistItem.id";
		TypedQuery<Answer> q = em.createQuery(queryString, Answer.class);
		return q.getResultList();
	}
	
	public static List<Answer> retrieveExportCsvDataByOscePostAndStudent(Long oscePostId, Long studentId) {
		EntityManager em = entityManager();
		// String
		// queryString="SELECT a FROM Answer a where oscePostRoom in (select distinct (oscePostRoom) from Assignment where osceDay in (select id from OsceDay where osce="+
		// osceId +")) group by doctor,student";
		String queryString = "SELECT a FROM Answer a join a.checklistQuestion c join c.checkListTopic t WHERE a.oscePostRoom.oscePost.id = "
				+ oscePostId + " AND a.student.id = " + studentId 
				+ " ORDER BY a.doctor, a.student, t.sort_order, c.sequenceNumber, c.id";
		TypedQuery<Answer> q = em.createQuery(queryString, Answer.class);
		return q.getResultList();
	}
	
	public static List<Answer> retrieveExportCsvDataByItemOscePostAndStudent(Long oscePostId, Long studentId) {
		EntityManager em = entityManager();
		String queryString = "SELECT a FROM Answer a WHERE a.checklistItem.parentItem IS NOT NULL " +
				"AND a.oscePostRoom.oscePost.id = " + oscePostId + " AND a.student.id = " + studentId + 
				" ORDER BY a.doctor, a.student, a.checklistItem.parentItem.sequenceNumber, a.checklistItem.sequenceNumber, a.checklistItem.id";
		TypedQuery<Answer> q = em.createQuery(queryString, Answer.class);
		return q.getResultList();
	}

	public static List<Answer> retrieveExportCsvDataByOscePostAndExaminer(Long oscePostId, Long examinerId) {
		EntityManager em = entityManager();
		// String
		// queryString="SELECT a FROM Answer a where oscePostRoom in (select distinct (oscePostRoom) from Assignment where osceDay in (select id from OsceDay where osce="+
		// osceId +")) group by doctor,student";
		String queryString = "SELECT a FROM Answer a join a.checklistQuestion c join c.checkListTopic t WHERE a.doctor.id = " + examinerId + " AND a.oscePostRoom.oscePost.id = "
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
	
	public static Answer findItemAnswer(Long studentId, Long questionId, Long osceDayId) {
		EntityManager em = entityManager();
		
		String queryString = "SELECT a FROM Answer a where a.student="
				+ studentId
				+ " and a.checklistItem.id ="
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
	
	public static String createGraph(Long oscePostId)
	{
		OscePost oscePost = OscePost.findOscePost(oscePostId);
		String fileName;
		
		if (oscePost.getStandardizedRole() != null)
			fileName = oscePost.getStandardizedRole().getShortName() + ".csv";
		else
			fileName = "oscePost" + oscePost.getId() + ".csv";		
		
		String fullFileName = ExportStatisticData.createOscePostGraphCSV(RequestFactoryServlet.getThreadLocalRequest(),RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext(), fileName, oscePost);
		fileName = fileName.replace(".csv", ".png");
		fileName = OsMaFilePathConstant.assignmentHTML + fileName;
		return fileName;
	}

	public static List<Student> findDistinctStudentByOscePost(Long oscePostId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT DISTINCT a.student FROM Answer a WHERE a.oscePostRoom.oscePost.id = " + oscePostId;
		TypedQuery<Student> query = em.createQuery(sql, Student.class);
		return query.getResultList();
	}
	
	public static List<Student> findDistinctStudentByExaminerAndOscePost(Long oscePostId, Long examinerId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT DISTINCT a.student FROM Answer a WHERE a.doctor.id = " + examinerId + " AND a.oscePostRoom.oscePost.id = " + oscePostId;
		TypedQuery<Student> query = em.createQuery(sql, Student.class);
		return query.getResultList();
	}
	
	public static Doctor findExaminerByStandardizedRoleOsceAndStudent(Long standardizedRoleId, Long osceId, Long studentId)
	{
		EntityManager em = entityManager();
		String sql = "SELECT DISTINCT a.doctor FROM Answer a WHERE a.student.id = " + studentId + " AND a.oscePostRoom IS NOT NULL AND a.oscePostRoom.oscePost.standardizedRole.id = " + standardizedRoleId + " AND a.oscePostRoom.oscePost.osceSequence.osceDay.osce.id = " + osceId;
		TypedQuery<Doctor> query = em.createQuery(sql, Doctor.class);
		
		if (query.getResultList().size() > 0)
			return query.getResultList().get(0);
		else
			return null;
	}
	
	public static List<Answer> findAnswerByChecklistOption(Long optionId) {
		EntityManager em = entityManager();
		String sql = "SELECT a FROM Answer a WHERE a.checklistOption is not null AND a.checklistOption.id = " + optionId;
		TypedQuery<Answer> query = em.createQuery(sql, Answer.class);
		return query.getResultList();
	}
	
	public static List<Answer> findAnswerByChecklistCriteria(Long criteriaId) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Answer> criteriaQuery = criteriaBuilder.createQuery(Answer.class);
		Root<Answer> from = criteriaQuery.from(Answer.class);
		
		SetJoin<Answer, ChecklistCriteria> joinSet = from.joinSet("checklistCriteria", JoinType.LEFT);
		Predicate predicate = criteriaBuilder.equal(joinSet.get("id"), criteriaId);
		
		criteriaQuery.where(predicate);
		
		TypedQuery<Answer> query = entityManager().createQuery(criteriaQuery);
		
		return query.getResultList();
	}
	
	public static List<Answer> findAnswerByChecklistItem(Long checklistItemId) {
		EntityManager em = entityManager();
		String sql = "SELECT a FROM Answer a WHERE a.checklistItem is not null AND a.checklistItem.id = " + checklistItemId;
		TypedQuery<Answer> query = em.createQuery(sql, Answer.class);
		return query.getResultList();
	}

	@Transactional
	public void deleteAnswerAndCriteria(Long doctorId, Long oprId, Long studentId) {
		try 
    	{
			OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oprId);
			if (oscePostRoom != null && oscePostRoom.getOscePost() != null) {
				String criteriaSql = "delete from answer_check_list_criteria where answer in (select id from answer where osce_post_room = " + oprId + " and doctor = " + doctorId + " and student = " + studentId +")";
	    		int criteriaDeletedCount = entityManager().createNativeQuery(criteriaSql).executeUpdate();
	    		
	    		String answerSql = "delete from answer where osce_post_room = " + oprId + " and doctor = " + doctorId + " and student = " + studentId;
	    		int answerDeletedCount = entityManager().createNativeQuery(answerSql).executeUpdate();
			}
    	} catch(Exception e) {
    		e.printStackTrace();    		
    	}
	}
	
	@Transactional
	public void deleteAudioNote(Long doctorId, Long oprId, Long studentId) {
		try 
    	{
			OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oprId);
			if (oscePostRoom != null && oscePostRoom.getOscePost() != null) {
				String noteSql = "delete from notes where osce_post_room = " + oprId + " and doctor = " + doctorId + " and student = " + studentId + " and note_type != " + NoteType.TEXTUAL.ordinal();
	    		int notesDeletedCount = entityManager().createNativeQuery(noteSql).executeUpdate();
	    	}
    	} catch(Exception e) {
    		e.printStackTrace();    		
    	}
	}
	
	@Transactional
	public void deleteTextualNote(Long doctorId, Long oprId, Long studentId) {
		try 
    	{
			OscePostRoom oscePostRoom = OscePostRoom.findOscePostRoom(oprId);
			if (oscePostRoom != null && oscePostRoom.getOscePost() != null) {
				String noteSql = "delete from notes where osce_post_room = " + oprId + " and doctor = " + doctorId + " and student = " + studentId + " and note_type = " + NoteType.TEXTUAL.ordinal();
	    		int notesDeletedCount = entityManager().createNativeQuery(noteSql).executeUpdate();
			}
    	} catch(Exception e) {
    		e.printStackTrace();    		
    	}
	}
}
