package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.shared.ChecklistImportPojo;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.OptionType;
import ch.unibas.medizin.osce.shared.OscePostWiseQuestion;
import ch.unibas.medizin.osce.shared.StatisticalEvaluationQuestion;

@RooJavaBean
@RooToString
@RooEntity
public class ChecklistItem {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
	@NotNull
	@Size(max=5000)
	private String name;
	
	@Size(max=5000)
	private String description;
	
	@NotNull
	@Enumerated
	private ItemType itemType;
	
	@Enumerated
    private OptionType optionType;
	
	private Boolean isRegressionItem;
	
	private Integer sequenceNumber;
	
	@ManyToOne
	private CheckList checkList;
	
	@ManyToOne
	private ChecklistItem parentItem;
	
	private Double weight; 
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistItem")
	@OrderBy("sequenceNumber")
    private List<ChecklistOption> checkListOptions = new ArrayList<ChecklistOption>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistItem")
	@OrderBy("sequenceNumber")
    private List<ChecklistCriteria> checkListCriterias = new ArrayList<ChecklistCriteria>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentItem")
	@OrderBy("sequenceNumber")
	private List<ChecklistItem> childChecklistItems = new ArrayList<ChecklistItem>();	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistItem")
	private List<ItemAnalysis> itemAnalysis = new ArrayList<ItemAnalysis>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistItem")
	private List<PostAnalysis> postAnalysis = new ArrayList<PostAnalysis>();
	
	public static List<ChecklistItem> findChecklistTopicByChecklistId(Long checklistId) {
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id IN (SELECT ci FROM ChecklistItem ci WHERE ci.checkList IS NOT NULL AND ci.checkList.id = " + checklistId + ") ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		return query.getResultList();
	}
	
	public static List<ChecklistItem> findChecklistQuestionByChecklistTopicId(Long checklistTopicId) {
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id = " + checklistTopicId + " ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		return query.getResultList();
	}
	public static List<ChecklistItem> findAllChecklistItemsForChecklist(Long checklistId) {
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.checkList IS NOT NULL AND ci.checkList.id = " + checklistId;
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		return query.getResultList();
	}

	public static List<ChecklistItem> findAllChecklistItemsChild(Long parentId) {
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id = " + parentId + " ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		return query.getResultList();
	}
	public static List<ChecklistItem> findAllChecklistItemByChecklistId(Long checklistId) {
		List<ChecklistItem> checklistItemList = new ArrayList<ChecklistItem>();
		
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.checkList IS NOT NULL AND ci.checkList.id = " + checklistId + " ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		checklistItemList.addAll(query.getResultList());
		
		String sql1 = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id IN (SELECT ci FROM ChecklistItem ci WHERE ci.checkList IS NOT NULL AND ci.checkList.id = " + checklistId + ") ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query1 = em.createQuery(sql1, ChecklistItem.class);
		checklistItemList.addAll(query1.getResultList());
		
		String sql2 = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id IN (SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id IN (SELECT ci FROM ChecklistItem ci WHERE ci.checkList IS NOT NULL AND ci.checkList.id = " + checklistId + ")) ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query2 = em.createQuery(sql2, ChecklistItem.class);
		checklistItemList.addAll(query2.getResultList());
				
		return checklistItemList;
	}
	
	public static ChecklistItem saveChecklistTabItem(String name, String description, Long standardizedRoleId) {
		StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(standardizedRoleId);
		CheckList checkList = standardizedRole.getCheckList();
		Integer sequenceNumber = findMaxTabSequenceNumber(checkList.getId());
		
		ChecklistItem checklistItem = new ChecklistItem();
		checklistItem.setName(name);
		checklistItem.setDescription(description);
		checklistItem.setItemType(ItemType.TAB);
		checklistItem.setSequenceNumber(sequenceNumber);
		checklistItem.setCheckList(checkList);
		checklistItem.persist();
		
		return checklistItem;
	}
	
	public static Integer findMaxTabSequenceNumber(Long checklistId) {
		EntityManager em = entityManager();
		String sql = "SELECT MAX(ci.sequenceNumber) FROM ChecklistItem ci WHERE ci.checkList.id = " + checklistId;
		TypedQuery<Integer> query = em.createQuery(sql, Integer.class);
		if (query.getResultList() != null && query.getResultList().size() > 0 && query.getResultList().get(0) != null)
			return (query.getResultList().get(0) + 1);
		else
			return 0;
	}
	
	public static Boolean removeChecklistTabItem(Long checklistItemId) {
		ChecklistItem item = ChecklistItem.findChecklistItem(checklistItemId);
		if (checkCriteriaForRemoveChecklistQuestionItem(item)) {
			return item.removeChecklistTab();
		}
		return false;
		
	}
	
	@Transactional
	private Boolean removeChecklistTab() {
		List<ChecklistItem> topicList = findChecklistItemByParentId(this.getId());
		
		for (ChecklistItem topic : topicList) {
			if (checkCriteriaForRemoveChecklistQuestionItem(topic)) {
				List<ChecklistItem> questionItemList = findChecklistItemByParentId(topic.getId());
				
				for (ChecklistItem question : questionItemList) {
					if (checkCriteriaForRemoveChecklistQuestionItem(question) == false) {
						return false;
					}
				}
			}
			else {
				return false;
			}
		}
		
		for (int i=0; i<topicList.size(); i++) {
			ChecklistItem itemTopic = topicList.get(i);
			
			List<ChecklistItem> questionItemList = findChecklistItemByParentId(itemTopic.getId());
			for (int j=0; j<questionItemList.size(); j++) {
				ChecklistItem questionItem = questionItemList.get(j);
				
				List<ChecklistOption> optionList = questionItem.getCheckListOptions();
				for (int k=0; k<questionItem.getCheckListOptions().size(); k++) {
					ChecklistOption option = optionList.get(k);
					option.remove();
				}
				
				List<ChecklistCriteria> criteriaList = questionItem.getCheckListCriterias();
				for (int k=0; k<questionItem.getCheckListCriterias().size(); k++) {
					ChecklistCriteria criteria = criteriaList.get(k);
					criteria.remove();
				}
				
				questionItem.remove();
			}
			
			itemTopic.remove();
		}
		
		Long checklistId = getCheckList().getId();
		this.remove();
		
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.checkList.id = " + checklistId + " ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		List<ChecklistItem> resultList = query.getResultList();
		int seqNumber = 0;
		for (ChecklistItem checklistItem : resultList) {
			checklistItem.setSequenceNumber(seqNumber);
			checklistItem.persist();
			seqNumber += 1;
		}
		
		return true;
	}
	
	public static ChecklistItem saveChecklistTopicItem(String name, String description, Long parentTabItemId, Double weight) {
		ChecklistItem parentItem = ChecklistItem.findChecklistItem(parentTabItemId);
		Integer seqNumber = findMaxSequenceNumberByParentItem(parentTabItemId);
		
		ChecklistItem checklistItem = new ChecklistItem();
		checklistItem.setName(name);
		checklistItem.setDescription(description);
		checklistItem.setItemType(ItemType.TOPIC);
		checklistItem.setSequenceNumber(seqNumber);
		checklistItem.setWeight(weight==null?0:weight);
		checklistItem.setParentItem(parentItem);
		checklistItem.persist();
		
		return checklistItem;
	}
	
	public static Boolean removeChecklistTopicItem(Long checklistItemId) {
		ChecklistItem checklistItem = ChecklistItem.findChecklistItem(checklistItemId);
		if (checkCriteriaForRemoveChecklistQuestionItem(checklistItem)) {
			return checklistItem.removeChecklistTopic();
		}
		return false;
	}
	
	@Transactional
	private Boolean removeChecklistTopic() {
		List<ChecklistItem> questionItemList = findChecklistItemByParentId(this.getId());
		for (ChecklistItem question : questionItemList) {
			if (checkCriteriaForRemoveChecklistQuestionItem(question) == false) {
				return false;
			}
		}
		
		for (int i=0; i<questionItemList.size(); i++) {
			ChecklistItem questionItem = questionItemList.get(i);
			
			List<ChecklistOption> optionList = questionItem.getCheckListOptions();
			for (int j=0; j<questionItem.getCheckListOptions().size(); j++) {
				ChecklistOption option = optionList.get(j);
				option.remove();
			}
			
			List<ChecklistCriteria> criteriaList = questionItem.getCheckListCriterias();
			for (int j=0; j<questionItem.getCheckListCriterias().size(); j++) {
				ChecklistCriteria criteria = criteriaList.get(j);
				criteria.remove();
			}
			
			questionItem.remove();
		}
		
		Long parentItemId = getParentItem().getId();
		this.remove();
		
		List<ChecklistItem> checklistItemList = findChecklistItemByParentId(parentItemId);
		
		int seqNumber = 0;
		for (ChecklistItem topicItem : checklistItemList) {
			topicItem.setSequenceNumber(seqNumber);
			topicItem.persist();
			seqNumber += 1;
		}
		
		return true;
	}
	
	public static ChecklistItem saveChecklistQuestionItem(String name, String description, Boolean isOverallQue, OptionType optionType, Long parentTopicItemId) {
		ChecklistItem parentItem = ChecklistItem.findChecklistItem(parentTopicItemId);
		Integer seqNumber = findMaxSequenceNumberByParentItem(parentTopicItemId);
		
		ChecklistItem checklistItem = new ChecklistItem();
		checklistItem.setName(name);
		checklistItem.setDescription(description);
		checklistItem.setItemType(ItemType.QUESTION);
		checklistItem.setIsRegressionItem(isOverallQue);
		checklistItem.setOptionType(optionType);
		checklistItem.setSequenceNumber(seqNumber);
		checklistItem.setParentItem(parentItem);
		checklistItem.persist();
		
		return checklistItem;
	}
	
	public static Boolean removeChecklistItemQuestionItem(Long checklistItemId) {
		ChecklistItem checklistItem = ChecklistItem.findChecklistItem(checklistItemId);
		if (checkCriteriaForRemoveChecklistQuestionItem(checklistItem)) {
			checklistItem.removeChecklistQuestion();
			return true;
		}
		return false;	
	}
	
	@Transactional
	private void removeChecklistQuestion() {
		List<ChecklistOption> optionList = getCheckListOptions();
		for (int i=0; i<getCheckListOptions().size(); i++) {
			ChecklistOption option = optionList.get(i);
			option.remove();
		}
		
		List<ChecklistCriteria> criteriaList = getCheckListCriterias();
		for (int i=0; i<getCheckListCriterias().size(); i++) {
			ChecklistCriteria criteria = criteriaList.get(i);
			criteria.remove();
		}
		
		Long parentItemId = getParentItem().getId();
		this.remove();
		
		List<ChecklistItem> checklistItemList = findChecklistItemByParentId(parentItemId);
		
		int seqNumber = 0;
		for (ChecklistItem topicItem : checklistItemList) {
			topicItem.setSequenceNumber(seqNumber);
			topicItem.persist();
			seqNumber += 1;
		}
	}
	
	public static Integer findMaxSequenceNumberByParentItem(Long parentItemId) {
		EntityManager em = entityManager();
		String sql = "SELECT MAX(ci.sequenceNumber) FROM ChecklistItem ci WHERE ci.parentItem.id = " + parentItemId;
		TypedQuery<Integer> query = em.createQuery(sql, Integer.class);
		if (query.getResultList() != null && query.getResultList().size() > 0 && query.getResultList().get(0) != null)
			return (query.getResultList().get(0) + 1);
		else
			return 0;
	}
	
	public static List<ChecklistItem> findChecklistItemByParentId(Long parentId) {
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id = " + parentId;
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		return query.getResultList();
	}
	
	public static List<ChecklistItem> findChecklistItemByChecklistId(Long checklistId) {
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.checkList IS NOT NULL AND ci.checkList.id = " + checklistId;
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		return query.getResultList();
	}
	
	public static List<ChecklistItem> findChecklistTopicByChecklist(Long checklistId) {
		List<ChecklistItem> topicList = new ArrayList<ChecklistItem>();
		
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id IN (SELECT ci FROM ChecklistItem ci WHERE ci.checkList IS NOT NULL AND ci.checkList.id = " + checklistId +  ") ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		topicList.addAll(query.getResultList());
		
		String sql1 = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NULL AND ci.checkList IS NOT NULL AND ci.checkList.id = " + checklistId + " AND ci.itemType = " + ItemType.TOPIC.ordinal() + " ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query1 = em.createQuery(sql1, ChecklistItem.class);
		topicList.addAll(query1.getResultList());
		
		return topicList;
	}
	
	public static List<ChecklistItem> findChecklistQuestionByChecklistTopic(Long checklistTopicId) {
		List<ChecklistItem> questionList = new ArrayList<ChecklistItem>();
		
		List<ChecklistItem> itemList = ChecklistItem.findAllChecklistItemsChild(checklistTopicId);
		exportChecklistItemsChild(itemList, questionList);
		
		return questionList;
	}
	
	private static void exportChecklistItemsChild(List<ChecklistItem> checklistItems, List<ChecklistItem> questionList) {
		for (ChecklistItem checklistItem : checklistItems) {
			
			if (ItemType.QUESTION.equals(checklistItem.getItemType())) {
				questionList.add(checklistItem);				
			}
			else {
				List<ChecklistItem> checklistItemChilds = ChecklistItem.findAllChecklistItemsChild(checklistItem.getId());
				exportChecklistItemsChild(checklistItemChilds, questionList);
			}	
		}
	}
	
	public static List<ChecklistItem> findChecklistQuestionByChecklistId(Long checklistId) {
		List<ChecklistItem> questionList = new ArrayList<ChecklistItem>();
		
		List<ChecklistItem> topicList = findChecklistTopicByChecklist(checklistId);
		for (ChecklistItem checklistTopic : topicList) {
			questionList.addAll(findChecklistQuestionByChecklistTopic(checklistTopic.getId()));
		}
		
		return questionList;
	}
	
	public static List<OscePostWiseQuestion> findChecklistQuestionByOscePost(Long osceSequenceId) {
		List<OscePostWiseQuestion> oscePostQueList = new ArrayList<OscePostWiseQuestion>();
		OsceSequence osceSequence = OsceSequence.findOsceSequence(osceSequenceId);
		List<OscePost> oscePostList = osceSequence.getOscePosts();
		
		for (OscePost oscePost : oscePostList) {
			List<StatisticalEvaluationQuestion> stEvaQueList = new ArrayList<StatisticalEvaluationQuestion>();
			if (oscePost.getStandardizedRole() != null && oscePost.getStandardizedRole().getCheckList() != null) {
				List<ChecklistItem> questionList = findChecklistQuestionByChecklistId(oscePost.getStandardizedRole().getCheckList().getId());
				
				
				for (ChecklistItem checklistItem : questionList) {
					StatisticalEvaluationQuestion stEvaQue = new StatisticalEvaluationQuestion();
					stEvaQue.setQuestionId(checklistItem.getId());
					stEvaQue.setQuestionText(checklistItem.getName());
					stEvaQue.setIsRegressionItem(checklistItem.getIsRegressionItem());
					
					stEvaQueList.add(stEvaQue);
				}
			}
			
			OscePostWiseQuestion postWiseQuestion = new OscePostWiseQuestion();
			postWiseQuestion.setOscePostId(oscePost.getId());
			postWiseQuestion.setQuestionList(stEvaQueList);
			
			oscePostQueList.add(postWiseQuestion);
		}
		
		return oscePostQueList;
	}
	
	public static List<ChecklistItem> findChecklistTopicByChecklistTab(Long checklistTabId) {
		List<ChecklistItem> topicList = new ArrayList<ChecklistItem>();
		
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem  =" + checklistTabId + " and ci.itemType=" +ItemType.TOPIC.ordinal() +  ") ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		topicList.addAll(query.getResultList());
		
		return topicList;
	}
	
	public static List<ChecklistItem> findAllChecklistTabsByChecklistId(Long checkistId){
		List<ChecklistItem> checklistItemList = new ArrayList<ChecklistItem>();
		
		EntityManager em = entityManager();
		String sql = "SELECT ci FROM ChecklistItem ci WHERE  ci.checkList  IS NOT NULL AND ci.checkList.id = " + checkistId + " and ci.itemType="  + ItemType.TAB.ordinal() +" ORDER BY ci.sequenceNumber";
		TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
		checklistItemList.addAll(query.getResultList());
		
				
		return checklistItemList;		
	}
	
	public static boolean checkCriteriaForRemoveChecklistQuestionItem(ChecklistItem checklistItem) {
		List<Answer> answerList = Answer.findAnswerByChecklistItem(checklistItem.getId());
		
		if (answerList != null && answerList.isEmpty() == false) {
			return false;
		}
		
		if (checklistItem.getItemAnalysis() != null && checklistItem.getItemAnalysis().isEmpty() == false) {
			return false;
		}
		
		if (checklistItem.getPostAnalysis() != null && checklistItem.getPostAnalysis().isEmpty() == false) {
			return false;
		}
		
		return true;
	}
	
	public static List<ChecklistImportPojo> findAllChecklistTabsByRoles(Long checklistId){
		
		List<ChecklistImportPojo> checklistImportPojoList = new ArrayList<ChecklistImportPojo>();
		List<ChecklistItem> tabs = findAllChecklistTabsByChecklistId(checklistId);
		
		if(tabs.size() > 0){
			ChecklistImportPojo checklistImportPojo = new ChecklistImportPojo();
			checklistImportPojo.setId(-1l);
			checklistImportPojo.setName("All");
			checklistImportPojoList.add(checklistImportPojo);
			
			for (ChecklistItem checklistItem : tabs) {
				checklistImportPojo = new ChecklistImportPojo();
				checklistImportPojo.setId(checklistItem.getId());
				checklistImportPojo.setName(checklistItem.getName());
				checklistImportPojoList.add(checklistImportPojo);
			}
		}
		return checklistImportPojoList;
	}
	
	public static List<ChecklistItem> importChecklistTabsForTab(Long currentRoleId, Long selectedRoleId, Long checklistTabId){
		
		List<ChecklistItem> newChecklistTabItemList = new ArrayList<ChecklistItem>();
		StandardizedRole currentRole = StandardizedRole.findStandardizedRole(currentRoleId);
		
		if(checklistTabId.equals(-1L)) {
			StandardizedRole selectedRole = StandardizedRole.findStandardizedRole(selectedRoleId);
			
			if (selectedRole != null && selectedRole.getCheckList() != null && selectedRole.getCheckList().getChecklistItems() != null) {
				List<ChecklistItem> oldChecklistItemList = selectedRole.getCheckList().getChecklistItems();
				Integer seqNumber = findMaxTabSequenceNumber(currentRole.getCheckList().getId());
				
				for (ChecklistItem oldChecklistItem : oldChecklistItemList) {
					ChecklistItem newChecklistTabItem = new ChecklistItem().copyChecklistTabItemFromOld(oldChecklistItem, seqNumber, currentRole.getCheckList());
					newChecklistTabItemList.add(newChecklistTabItem);
					new ChecklistItem().copyChecklistItemFromAnotherItem(newChecklistTabItem, oldChecklistItem);
					
					seqNumber += 1;
				}
			}
		}
		else {
			if (currentRole != null && currentRole.getCheckList() != null) {
				ChecklistItem tab = ChecklistItem.findChecklistItem(checklistTabId);
				Integer seqNumber = findMaxTabSequenceNumber(currentRole.getCheckList().getId());
				ChecklistItem newChecklistTabItem = new ChecklistItem().copyChecklistTabItemFromOld(tab, seqNumber, currentRole.getCheckList());
				newChecklistTabItemList.add(newChecklistTabItem);
				new ChecklistItem().copyChecklistItemFromAnotherItem(newChecklistTabItem, tab);
			}
		}
		return newChecklistTabItemList;
	}
	
	public ChecklistItem copyChecklistTabItemFromOld(ChecklistItem oldChecklistItem, Integer seqNumber, CheckList newChecklist) {
		ChecklistItem newChecklistItem = new ChecklistItem();
		newChecklistItem.setName(oldChecklistItem.getName());
		newChecklistItem.setDescription(oldChecklistItem.getDescription());
		newChecklistItem.setItemType(oldChecklistItem.getItemType());
		newChecklistItem.setSequenceNumber(seqNumber);
		newChecklistItem.setCheckList(newChecklist);
		newChecklistItem.persist();
		
		return newChecklistItem;
	}

	@Transactional
	public void copyChecklistItemFromAnotherItem(ChecklistItem newChecklistItem, ChecklistItem oldChecklistItem) {
		
		if (oldChecklistItem.getChildChecklistItems() != null) {
			
			for (ChecklistItem oldItem : oldChecklistItem.getChildChecklistItems()) {
				ChecklistItem newItem = copyChecklistItem(oldItem,newChecklistItem);
				if (newItem != null && oldItem.getCheckListOptions() != null) {
					copyOptions(newItem, oldItem);
				}
				
				if (newItem != null && oldItem.getCheckListCriterias() != null) {
					copyCriterias(newItem, oldItem);
				}
				copyChecklistItemFromAnotherItem(newItem, oldItem);
			}
		}
		
	}
	
	@Transactional
	public static ChecklistItem copyChecklistItem(ChecklistItem oldChecklistItem, ChecklistItem newItem) {
		ChecklistItem newChecklistItem = new ChecklistItem();
		newChecklistItem.setName(oldChecklistItem.getName());
		newChecklistItem.setDescription(oldChecklistItem.getDescription());
		newChecklistItem.setItemType(oldChecklistItem.getItemType());
		newChecklistItem.setSequenceNumber(oldChecklistItem.getSequenceNumber());
		newChecklistItem.setParentItem(newItem);
		newChecklistItem.setOptionType(oldChecklistItem.getOptionType());
		newChecklistItem.setIsRegressionItem(oldChecklistItem.getIsRegressionItem());
		newChecklistItem.persist();
		
		return newChecklistItem;
	}
	
	@Transactional
	public static void copyOptions(ChecklistItem newChecklistItem, ChecklistItem oldChecklistItem) {
		
		for (ChecklistOption oldChecklistOption : oldChecklistItem.getCheckListOptions()) {
			ChecklistOption newChecklistOption = new ChecklistOption();
			newChecklistOption.setOptionName(oldChecklistOption.getOptionName());
			newChecklistOption.setDescription(oldChecklistOption.getDescription());
			newChecklistOption.setChecklistItem(newChecklistItem);
			newChecklistOption.setValue(oldChecklistOption.getValue());
			newChecklistOption.setCriteriaCount(oldChecklistOption.getCriteriaCount());
			newChecklistOption.setSequenceNumber(oldChecklistOption.getSequenceNumber());
		
			newChecklistOption.persist();
		}
	}
	
	@Transactional
	public static void copyCriterias(ChecklistItem newChecklistItem, ChecklistItem oldChecklistItem) {
		
		for (ChecklistCriteria oldChecklistCriteria : oldChecklistItem.getCheckListCriterias()) {
			ChecklistCriteria newChecklistCriteria = new ChecklistCriteria();
			newChecklistCriteria.setCriteria(oldChecklistCriteria.getCriteria());
			newChecklistCriteria.setDescription(oldChecklistCriteria.getDescription());
			newChecklistCriteria.setSequenceNumber(oldChecklistCriteria.getSequenceNumber());
			newChecklistCriteria.setChecklistItem(newChecklistItem);
			newChecklistCriteria.persist();
		}
	}

	public static List<ChecklistItem> importChecklistTopicsForRole(Long currentRoleId, Long selectedRoleId, Long topicId, Long tabId){
		List<ChecklistItem> newChecklistTopicItemList = new ArrayList<ChecklistItem>();
		ChecklistItem tab = ChecklistItem.findChecklistItem(tabId);
		
		if(topicId.equals(-1L)) {
			StandardizedRole selectedRole = StandardizedRole.findStandardizedRole(selectedRoleId);
			
			if (selectedRole != null && selectedRole.getCheckList() != null && selectedRole.getCheckList().getChecklistItems() != null) {
				List<ChecklistItem> oldChecklistItemList = findChecklistTopicByChecklist(selectedRole.getCheckList().getId());
				Integer seqNumber = findMaxSequenceNumberByParentItem(tabId);
				for (ChecklistItem oldChecklistItem : oldChecklistItemList) {
					ChecklistItem newChecklistTopicItem = new ChecklistItem().copyChecklistTopicItemFromOld(oldChecklistItem, seqNumber,tab);
					newChecklistTopicItemList.add(newChecklistTopicItem);
					new ChecklistItem().copyChecklistItemFromAnotherItem(newChecklistTopicItem, oldChecklistItem);
					
					seqNumber += 1;
				}
			}
		}
		else {
			if (tab != null && tab.getId() != null && topicId != null) {
				ChecklistItem topic = ChecklistItem.findChecklistItem(topicId);
				Integer seqNumber = findMaxSequenceNumberByParentItem(tabId);
				ChecklistItem newChecklistTopicItem = new ChecklistItem().copyChecklistTopicItemFromOld(topic, seqNumber,tab);
				newChecklistTopicItemList.add(newChecklistTopicItem);
				new ChecklistItem().copyChecklistItemFromAnotherItem(newChecklistTopicItem, topic);
			}
		}
		return newChecklistTopicItemList;
	}

	private ChecklistItem copyChecklistTopicItemFromOld(ChecklistItem oldChecklistItem, Integer seqNumber, ChecklistItem currentTab) {

		ChecklistItem newChecklistItem = new ChecklistItem();
		newChecklistItem.setName(oldChecklistItem.getName());
		newChecklistItem.setDescription(oldChecklistItem.getDescription());
		newChecklistItem.setItemType(oldChecklistItem.getItemType());
		newChecklistItem.setSequenceNumber(seqNumber);
		newChecklistItem.setParentItem(currentTab);
		newChecklistItem.persist();
		
		return newChecklistItem;
	}

	public static List<ChecklistImportPojo> findAllChecklistTopicsByRoles(Long checklistId) {
		List<ChecklistImportPojo> checklistImportPojoList = new ArrayList<ChecklistImportPojo>();
		List<ChecklistItem> topics = findChecklistTopicByChecklist(checklistId);
		
		if(topics.size() > 0){
			ChecklistImportPojo checklistImportPojo = new ChecklistImportPojo();
			checklistImportPojo.setId(-1l);
			checklistImportPojo.setName("All");
			checklistImportPojoList.add(checklistImportPojo);
			
			for (ChecklistItem checklistItem : topics) {
				checklistImportPojo = new ChecklistImportPojo();
				checklistImportPojo.setId(checklistItem.getId());
				checklistImportPojo.setName(checklistItem.getName());
				checklistImportPojoList.add(checklistImportPojo);
			}
		}
		return checklistImportPojoList;
	}
	
	public static List<ChecklistItem> findAllChecklistTopicsByChecklist(Long checklistId){

		List<ChecklistItem> checklistTopics = findChecklistTopicByChecklist(checklistId);
		return checklistTopics;
	}
	
	public static List<ChecklistItem> importChecklistQuestionsForTopic(Long selectedtopicId, Long selectedRoleId, Long questionId, Long currentTopicId){
		
		List<ChecklistItem> newChecklistQuestionItemList = new ArrayList<ChecklistItem>();
		ChecklistItem topic = ChecklistItem.findChecklistItem(currentTopicId);
		
		if(questionId.equals(-1L)) {
			if (selectedtopicId != null) {

				List<ChecklistItem> oldChecklistItemList = findChecklistQuestionByChecklistTopicId(selectedtopicId);
				Integer seqNumber = findMaxSequenceNumberByParentItem(currentTopicId);
				for (ChecklistItem oldChecklistItem : oldChecklistItemList) {
					ChecklistItem newChecklistQuestionItem=copyChecklistQuestionItemFromOld(oldChecklistItem,seqNumber,topic);
					newChecklistQuestionItemList.add(newChecklistQuestionItem);
					seqNumber += 1;
				}
			}
		}
		else {
			if (questionId != null && topic != null) {
				ChecklistItem questionItem = ChecklistItem.findChecklistItem(questionId);
				Integer seqNumber = findMaxSequenceNumberByParentItem(currentTopicId);
				ChecklistItem newChecklistQuestionItem=copyChecklistQuestionItemFromOld(questionItem,seqNumber,topic);
				newChecklistQuestionItemList.add(newChecklistQuestionItem);
			}
		}
		return newChecklistQuestionItemList;
	}
	
	private static ChecklistItem copyChecklistQuestionItemFromOld(ChecklistItem questionItem, Integer seqNumber, ChecklistItem currentTopic) {

		ChecklistItem newChecklistItem = new ChecklistItem();
		newChecklistItem.setName(questionItem.getName());
		newChecklistItem.setDescription(questionItem.getDescription());
		newChecklistItem.setItemType(questionItem.getItemType());
		newChecklistItem.setSequenceNumber(seqNumber);
		newChecklistItem.setParentItem(currentTopic);
		newChecklistItem.setIsRegressionItem(questionItem.getIsRegressionItem());
		newChecklistItem.setOptionType(questionItem.getOptionType());
		newChecklistItem.persist();
		
		if(questionItem.getCheckListOptions() != null){
			copyOptions(newChecklistItem,questionItem);
		}
		if(questionItem.getCheckListCriterias() != null) {
			copyCriterias(newChecklistItem, questionItem);
		}
		return newChecklistItem;
	}

	public static List<ChecklistImportPojo> findChecklistQuestionByTopicId(Long topicId){
		
		List<ChecklistImportPojo> checklistImportPojoList = new ArrayList<ChecklistImportPojo>();
		List<ChecklistItem> topics = findChecklistQuestionByChecklistTopic(topicId);
		
		if(topics.size() > 0){
			ChecklistImportPojo checklistImportPojo = new ChecklistImportPojo();
			checklistImportPojo.setId(-1l);
			checklistImportPojo.setName("All");
			checklistImportPojoList.add(checklistImportPojo);
			
			for (ChecklistItem checklistItem : topics) {
				checklistImportPojo = new ChecklistImportPojo();
				checklistImportPojo.setId(checklistItem.getId());
				checklistImportPojo.setName(checklistItem.getName());
				checklistImportPojoList.add(checklistImportPojo);
			}
		}
		return checklistImportPojoList;
		
	}
	
	public static Double findMaxTopicWeight(Long checklistId){

		List<ChecklistItem> topics = findChecklistTopicByChecklist(checklistId);
		Double sum=0.0;
		for (ChecklistItem topic : topics) {
			
			if(topic.getWeight() != null){
				sum += topic.getWeight();
			}
		}
		return sum;
	}
}
