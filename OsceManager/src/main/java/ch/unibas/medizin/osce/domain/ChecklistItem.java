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
}
