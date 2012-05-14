// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.Bankaccount;
import ch.unibas.medizin.osce.domain.Nationality;
import ch.unibas.medizin.osce.domain.NationalityDataOnDemand;
import java.lang.Integer;
import java.lang.String;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect BankaccountDataOnDemand_Roo_DataOnDemand {
    
    declare @type: BankaccountDataOnDemand: @Component;
    
    private Random BankaccountDataOnDemand.rnd = new SecureRandom();
    
    private List<Bankaccount> BankaccountDataOnDemand.data;
    
    @Autowired
    private NationalityDataOnDemand BankaccountDataOnDemand.nationalityDataOnDemand;
    
    public Bankaccount BankaccountDataOnDemand.getNewTransientBankaccount(int index) {
        Bankaccount obj = new Bankaccount();
        setBIC(obj, index);
        setBankName(obj, index);
        setCity(obj, index);
        setCountry(obj, index);
        setIBAN(obj, index);
        setOwnerName(obj, index);
        setPostalCode(obj, index);
        return obj;
    }
    
    public void BankaccountDataOnDemand.setBIC(Bankaccount obj, int index) {
        String BIC = "BIC_" + index;
        if (BIC.length() > 40) {
            BIC = BIC.substring(0, 40);
        }
        obj.setBIC(BIC);
    }
    
    public void BankaccountDataOnDemand.setBankName(Bankaccount obj, int index) {
        String bankName = "bankName_" + index;
        if (bankName.length() > 40) {
            bankName = bankName.substring(0, 40);
        }
        obj.setBankName(bankName);
    }
    
    public void BankaccountDataOnDemand.setCity(Bankaccount obj, int index) {
        String city = "city_" + index;
        if (city.length() > 30) {
            city = city.substring(0, 30);
        }
        obj.setCity(city);
    }
    
    public void BankaccountDataOnDemand.setCountry(Bankaccount obj, int index) {
        Nationality country = nationalityDataOnDemand.getRandomNationality();
        obj.setCountry(country);
    }
    
    public void BankaccountDataOnDemand.setIBAN(Bankaccount obj, int index) {
        String IBAN = "IBAN_" + index;
        if (IBAN.length() > 40) {
            IBAN = IBAN.substring(0, 40);
        }
        obj.setIBAN(IBAN);
    }
    
    public void BankaccountDataOnDemand.setOwnerName(Bankaccount obj, int index) {
        String ownerName = "ownerName_" + index;
        if (ownerName.length() > 40) {
            ownerName = ownerName.substring(0, 40);
        }
        obj.setOwnerName(ownerName);
    }
    
    public void BankaccountDataOnDemand.setPostalCode(Bankaccount obj, int index) {
        Integer postalCode = new Integer(index);
        obj.setPostalCode(postalCode);
    }
    
    public Bankaccount BankaccountDataOnDemand.getSpecificBankaccount(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        Bankaccount obj = data.get(index);
        return Bankaccount.findBankaccount(obj.getId());
    }
    
    public Bankaccount BankaccountDataOnDemand.getRandomBankaccount() {
        init();
        Bankaccount obj = data.get(rnd.nextInt(data.size()));
        return Bankaccount.findBankaccount(obj.getId());
    }
    
    public boolean BankaccountDataOnDemand.modifyBankaccount(Bankaccount obj) {
        return false;
    }
    
    public void BankaccountDataOnDemand.init() {
        data = Bankaccount.findBankaccountEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'Bankaccount' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ch.unibas.medizin.osce.domain.Bankaccount>();
        for (int i = 0; i < 10; i++) {
            Bankaccount obj = getNewTransientBankaccount(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> it = e.getConstraintViolations().iterator(); it.hasNext();) {
                    ConstraintViolation<?> cv = it.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
