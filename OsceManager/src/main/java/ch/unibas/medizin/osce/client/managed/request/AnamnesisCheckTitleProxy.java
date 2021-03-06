// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Set;

@ProxyForName("ch.unibas.medizin.osce.domain.AnamnesisCheckTitle")
public interface AnamnesisCheckTitleProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getText();

    abstract void setText(String text);

    abstract Integer getSort_order();

    abstract void setSort_order(Integer sort_order);

    abstract Set<ch.unibas.medizin.osce.client.managed.request.AnamnesisCheckProxy> getAnamnesisChecks();

    abstract void setAnamnesisChecks(Set<AnamnesisCheckProxy> anamnesisChecks);
}
