// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

@ProxyForName("ch.unibas.medizin.osce.domain.MediaContent")
public interface MediaContentProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getLink();

    abstract void setLink(String link);

    abstract String getComment();

    abstract void setComment(String comment);

    abstract StandardizedPatientProxy getStandardizedPatient();

    abstract void setStandardizedPatient(StandardizedPatientProxy standardizedPatient);

    abstract MediaContentTypeProxy getContentType();

    abstract void setContentType(MediaContentTypeProxy contentType);
}
