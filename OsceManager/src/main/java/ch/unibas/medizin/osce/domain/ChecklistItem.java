package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import ch.unibas.medizin.osce.shared.ChecklistImportPojo;
import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.OptionType;
import ch.unibas.medizin.osce.shared.OscePostWiseQuestion;
import ch.unibas.medizin.osce.shared.StatisticalEvaluationQuestion;

@Entity
@Configurable
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
		
		//findAllChecklistItemsChild
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
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id = " + parentId + " ORDER BY ci.sequenceNumber";
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
		newChecklistItem.setWeight(0.0);		
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
		//newChecklistItem.setWeight(oldChecklistItem.getWeight() == null? null: oldChecklistItem.getWeight());
		newChecklistItem.setWeight(0.0);
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
	
	public static void importChecklistQuestionsForTopic(Long selectedtopicId, Long selectedRoleId, Long questionId, Long currentTopicId){
		
		ChecklistItem topic = ChecklistItem.findChecklistItem(currentTopicId);
		
		if(questionId.equals(-1L)) {
			if (selectedtopicId != null) {

				List<ChecklistItem> oldChecklistItemList = findChecklistQuestionByChecklistTopicId(selectedtopicId);
				Integer seqNumber = findMaxSequenceNumberByParentItem(currentTopicId);
				for (ChecklistItem oldChecklistItem : oldChecklistItemList) {
					new ChecklistItem().copyChecklistQuestionItemFromOld(oldChecklistItem,seqNumber,topic);
					seqNumber += 1;
				}
			}
		}
		else {
			if (questionId != null && topic != null) {
				ChecklistItem questionItem = ChecklistItem.findChecklistItem(questionId);
				Integer seqNumber = findMaxSequenceNumberByParentItem(currentTopicId);
				new ChecklistItem().copyChecklistQuestionItemFromOld(questionItem,seqNumber,topic);
			}
		}
		
	}
	
	public static List<ChecklistItem> findChecklistItemQuestionById(List<Long> itemIdList) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<ChecklistItem> criteriaQuery = criteriaBuilder.createQuery(ChecklistItem.class);
		Root<ChecklistItem> from = criteriaQuery.from(ChecklistItem.class);
		Predicate predicate = criteriaBuilder.in(from.get("id")).value(itemIdList);
		criteriaQuery.where(predicate);
		TypedQuery<ChecklistItem> query = entityManager().createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	@Transactional
	private void copyChecklistQuestionItemFromOld(ChecklistItem questionItem, Integer seqNumber, ChecklistItem currentTopic) {

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
	
	public static CheckList copyOldChecklist(Long oldChecklist, String title){
		
		CheckList oldProxy= CheckList.findCheckList(oldChecklist);
		CheckList newChecklistToReturn = StandardizedRole.copyChecklistItemFromOldRole(oldProxy);
		
		 newChecklistToReturn.setTitle(title);
		 newChecklistToReturn.persist();
			
		 return newChecklistToReturn;
	}
	

	public static ChecklistItem moveChecklistItemUp(ChecklistItem checklistItemToMoveUp, int seqNumToSet){
		
		if(seqNumToSet >= 0){
			ChecklistItem checklistItem = new ChecklistItem().moveItemUp(checklistItemToMoveUp,seqNumToSet);
			return checklistItem;
		}
		return null;
	}
	
	public static ChecklistItem moveChecklistItemDown(ChecklistItem checklistItemToMoveDown, int seqNumToSet){
		
		Integer maxSeqNum = findMaxSequenceNumberByParentItem(checklistItemToMoveDown.getParentItem().getId());

		if(seqNumToSet >= 0 &&  seqNumToSet < (maxSeqNum)){
			ChecklistItem checklistItem = new ChecklistItem().moveItemDown(checklistItemToMoveDown,seqNumToSet);
			return checklistItem;
		}
		return null;
	}

	@Transactional
	public ChecklistItem moveItemDown(ChecklistItem checklistItemToMoveDown, int seqNumToSet) {
	
		if(checklistItemToMoveDown.getParentItem()  != null){

			EntityManager em = entityManager();
			String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem = " + checklistItemToMoveDown.getParentItem().getId() + " and ci.sequenceNumber = "  + seqNumToSet + "";
			TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
			if (query != null && query.getResultList().isEmpty() == false) {
				ChecklistItem item = query.getResultList().get(0);
				
				if(item != null){
					item.setSequenceNumber(seqNumToSet - 1);
					item.persist();
				}
				checklistItemToMoveDown.setSequenceNumber(seqNumToSet);
				checklistItemToMoveDown.persist();
			}
		}
		return checklistItemToMoveDown;
	}

	@Transactional
	public ChecklistItem moveItemUp(ChecklistItem checklistItemToMoveUp, int seqNumToSet) {
		
		if(checklistItemToMoveUp.getParentItem()  != null){

			EntityManager em = entityManager();
			String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem = " + checklistItemToMoveUp.getParentItem().getId() + " and ci.sequenceNumber = "  + seqNumToSet + "";
			TypedQuery<ChecklistItem> query = em.createQuery(sql, ChecklistItem.class);
			if (query != null && query.getResultList().isEmpty() == false) {
				ChecklistItem item = query.getResultList().get(0);				
				if(item != null){
					item.setSequenceNumber(seqNumToSet + 1);
					item.persist();
				}
				checklistItemToMoveUp.setSequenceNumber(seqNumToSet);
				checklistItemToMoveUp.persist();
			}				
		}
		return checklistItemToMoveUp;
	}
	
	public static List<ChecklistItem> updateChecklistItemQuestionSequenceNumber(List<Long> checklistItemIdList) {
		new ChecklistItem().updateChecklistItemQueSequenceNumber(checklistItemIdList);
		
		return findChecklistItemQuestionById(checklistItemIdList);
	}
	
	@Transactional
	public void updateChecklistItemQueSequenceNumber(List<Long> checklistItemIdList) {
		for (int i=0; i<checklistItemIdList.size(); i++) {
			Long itemId = checklistItemIdList.get(i);
			ChecklistItem checklistItem = ChecklistItem.findChecklistItem(itemId);
			if (checklistItem != null) {
				checklistItem.setSequenceNumber(i);
				checklistItem.persist();
			}
		}
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ChecklistItem attached = ChecklistItem.findChecklistItem(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public ChecklistItem merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ChecklistItem merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new ChecklistItem().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countChecklistItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ChecklistItem o", Long.class).getSingleResult();
    }

	public static List<ChecklistItem> findAllChecklistItems() {
        return entityManager().createQuery("SELECT o FROM ChecklistItem o", ChecklistItem.class).getResultList();
    }

	public static ChecklistItem findChecklistItem(Long id) {
        if (id == null) return null;
        return entityManager().find(ChecklistItem.class, id);
    }

	public static List<ChecklistItem> findChecklistItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ChecklistItem o", ChecklistItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CheckList: ").append(getCheckList()).append(", ");
        sb.append("CheckListCriterias: ").append(getCheckListCriterias() == null ? "null" : getCheckListCriterias().size()).append(", ");
        sb.append("CheckListOptions: ").append(getCheckListOptions() == null ? "null" : getCheckListOptions().size()).append(", ");
        sb.append("ChildChecklistItems: ").append(getChildChecklistItems() == null ? "null" : getChildChecklistItems().size()).append(", ");
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("IsRegressionItem: ").append(getIsRegressionItem()).append(", ");
        sb.append("ItemAnalysis: ").append(getItemAnalysis() == null ? "null" : getItemAnalysis().size()).append(", ");
        sb.append("ItemType: ").append(getItemType()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("OptionType: ").append(getOptionType()).append(", ");
        sb.append("PostAnalysis: ").append(getPostAnalysis() == null ? "null" : getPostAnalysis().size()).append(", ");
        sb.append("SequenceNumber: ").append(getSequenceNumber()).append(", ");
        sb.append("Version: ").append(getVersion()).append(", ");
        sb.append("Weight: ").append(getWeight());
        return sb.toString();
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public ItemType getItemType() {
        return this.itemType;
    }

	public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

	public OptionType getOptionType() {
        return this.optionType;
    }

	public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

	public Boolean getIsRegressionItem() {
        return this.isRegressionItem;
    }

	public void setIsRegressionItem(Boolean isRegressionItem) {
        this.isRegressionItem = isRegressionItem;
    }

	public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

	public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

	public CheckList getCheckList() {
        return this.checkList;
    }

	public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }

	public ChecklistItem getParentItem() {
        return this.parentItem;
    }

	public void setParentItem(ChecklistItem parentItem) {
        this.parentItem = parentItem;
    }

	public Double getWeight() {
        return this.weight;
    }

	public void setWeight(Double weight) {
        this.weight = weight;
    }

	public List<ChecklistOption> getCheckListOptions() {
        return this.checkListOptions;
    }

	public void setCheckListOptions(List<ChecklistOption> checkListOptions) {
        this.checkListOptions = checkListOptions;
    }

	public List<ChecklistCriteria> getCheckListCriterias() {
        return this.checkListCriterias;
    }

	public void setCheckListCriterias(List<ChecklistCriteria> checkListCriterias) {
        this.checkListCriterias = checkListCriterias;
    }

	public List<ChecklistItem> getChildChecklistItems() {
        return this.childChecklistItems;
    }

	public void setChildChecklistItems(List<ChecklistItem> childChecklistItems) {
        this.childChecklistItems = childChecklistItems;
    }

	public List<ItemAnalysis> getItemAnalysis() {
        return this.itemAnalysis;
    }

	public void setItemAnalysis(List<ItemAnalysis> itemAnalysis) {
        this.itemAnalysis = itemAnalysis;
    }

	public List<PostAnalysis> getPostAnalysis() {
        return this.postAnalysis;
    }

	public void setPostAnalysis(List<PostAnalysis> postAnalysis) {
        this.postAnalysis = postAnalysis;
    }
}
