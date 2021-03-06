// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Set;

@ProxyForName("ch.unibas.medizin.osce.domain.spportal.SpPatientInSemester")
public interface SpPatientInSemesterProxy extends EntityProxy {

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract Long getId();

    abstract void setId(Long id);

    abstract SpSemesterProxy getSemester();

    abstract void setSemester(SpSemesterProxy semester);

    abstract SpStandardizedPatientProxy getStandardizedPatient();

    abstract void setStandardizedPatient(SpStandardizedPatientProxy standardizedPatient);

    abstract Boolean getAccepted();

    abstract void setAccepted(Boolean accepted);

    abstract Integer getValue();

    abstract void setValue(Integer value);

    abstract SPPortalPersonProxy getPerson();

    abstract void setPerson(SPPortalPersonProxy person);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.SpTrainingDateProxy> getTrainingDates();

    abstract void setTrainingDates(Set<SpTrainingDateProxy> trainingDates);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.SpOsceDateProxy> getOsceDates();

    abstract void setOsceDates(Set<SpOsceDateProxy> osceDates);
}
