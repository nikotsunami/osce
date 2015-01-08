// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.ChecklistCriteria")
public interface ChecklistCriteriaProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getCriteria();

    abstract void setCriteria(String criteria);

    abstract String getDescription();

    abstract void setDescription(String description);

    abstract ChecklistQuestionProxy getChecklistQuestion();

    abstract void setChecklistQuestion(ChecklistQuestionProxy checklistQuestion);

    abstract Integer getSequenceNumber();

    abstract void setSequenceNumber(Integer sequenceNumber);

    abstract ChecklistItemProxy getChecklistItem();

    abstract void setChecklistItem(ChecklistItemProxy checklistItem);
}