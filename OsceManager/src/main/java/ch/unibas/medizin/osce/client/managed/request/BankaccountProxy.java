// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.

package ch.unibas.medizin.osce.client.managed.request;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("ch.unibas.medizin.osce.domain.Bankaccount")
@ProxyForName("ch.unibas.medizin.osce.domain.Bankaccount")
public interface BankaccountProxy extends EntityProxy {

    abstract Long getId();

    abstract void setId(Long id);

    abstract Integer getVersion();

    abstract void setVersion(Integer version);

    abstract String getBankName();

    abstract void setBankName(String bankName);

    abstract String getIBAN();

    abstract void setIBAN(String iBAN);

    abstract String getBIC();

    abstract void setBIC(String bIC);

    abstract String getOwnerName();

    abstract void setOwnerName(String ownerName);

    abstract Integer getPostalCode();

    abstract void setPostalCode(Integer postalCode);

    abstract String getCity();

    abstract void setCity(String city);

    abstract NationalityProxy getCountry();

    abstract void setCountry(NationalityProxy country);
}
