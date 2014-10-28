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

import ch.unibas.medizin.osce.shared.ItemType;
import ch.unibas.medizin.osce.shared.OptionType;

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
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistItem")
	@OrderBy("sequenceNumber")
    private List<ChecklistOption> checkListOptions = new ArrayList<ChecklistOption>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistItem")
	@OrderBy("sequenceNumber")
    private List<ChecklistCriteria> checkListCriterias = new ArrayList<ChecklistCriteria>();
	
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
		String sql = "SELECT ci FROM ChecklistItem ci WHERE ci.parentItem IS NOT NULL AND ci.parentItem.id = " + parentId;
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
	
	public static ChecklistItem saveChecklistTopicItem(String name, String description, Long parentTabItemId) {
		ChecklistItem parentItem = ChecklistItem.findChecklistItem(parentTabItemId);
		Integer seqNumber = findMaxSequenceNumberByParentItem(parentTabItemId);
		
		ChecklistItem checklistItem = new ChecklistItem();
		checklistItem.setName(name);
		checklistItem.setDescription(description);
		checklistItem.setItemType(ItemType.TOPIC);
		checklistItem.setSequenceNumber(seqNumber);
		checklistItem.setParentItem(parentItem);
		checklistItem.persist();
		
		return checklistItem;
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
	
}
