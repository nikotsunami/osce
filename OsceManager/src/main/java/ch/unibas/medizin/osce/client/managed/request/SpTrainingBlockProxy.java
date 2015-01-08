// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Date;
import java.util.Set;

@ProxyForName("ch.unibas.medizin.osce.domain.spportal.SpTrainingBlock")
public interface SpTrainingBlockProxy extends EntityProxy {

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract Long getId();

    abstract void setId(Long id);

    abstract Date getStartDate();

    abstract void setStartDate(Date startDate);

    abstract SpSemesterProxy getSemester();

    abstract void setSemester(SpSemesterProxy semester);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.SpTrainingDateProxy> getTrainingDates();

    abstract void setTrainingDates(Set<SpTrainingDateProxy> trainingDates);
}