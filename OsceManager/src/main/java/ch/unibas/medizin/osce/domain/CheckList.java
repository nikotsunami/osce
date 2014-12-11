package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class CheckList {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @NotNull
    private String title;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkList")
    @OrderBy("sort_order")
    private List<ChecklistTopic> checkListTopics = new ArrayList<ChecklistTopic>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkList")
    @OrderBy("sequenceNumber")
    private List<ChecklistItem> checklistItems = new ArrayList<ChecklistItem>();

	public static List<CheckList> findAllCheckListforOsce(Long osceId) {
		EntityManager em = entityManager();
    	String query = "SELECT distinct o.oscePost.standardizedRole.checkList FROM OscePostRoom o WHERE o.course.osce.id = " +osceId+ " ORDER BY o.oscePost.standardizedRole.checkList";
    	TypedQuery<CheckList> q = em.createQuery(query, CheckList.class);
    	return q.getResultList();
	}

	public static CheckList copyOldChecklist(Long oldChecklist, String title){
		
		CheckList oldProxy= CheckList.findCheckList(oldChecklist);
			
		 CheckList newChecklistToReturn = StandardizedRole.copyChecklistItemFromOldRole(oldProxy);
		
		 newChecklistToReturn.setTitle(title);
		 newChecklistToReturn.persist();
			
		 return newChecklistToReturn;
	}
}
