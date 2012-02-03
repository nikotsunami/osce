// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.LangSkill;
import ch.unibas.medizin.osce.domain.SpokenLanguage;
import ch.unibas.medizin.osce.domain.SpokenLanguageDataOnDemand;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.domain.StandardizedPatientDataOnDemand;
import ch.unibas.medizin.osce.shared.LangSkillLevel;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect LangSkillDataOnDemand_Roo_DataOnDemand {
    
    declare @type: LangSkillDataOnDemand: @Component;
    
    private Random LangSkillDataOnDemand.rnd = new SecureRandom();
    
    private List<LangSkill> LangSkillDataOnDemand.data;
    
    @Autowired
    private SpokenLanguageDataOnDemand LangSkillDataOnDemand.spokenLanguageDataOnDemand;
    
    @Autowired
    private StandardizedPatientDataOnDemand LangSkillDataOnDemand.standardizedPatientDataOnDemand;
    
    public LangSkill LangSkillDataOnDemand.getNewTransientLangSkill(int index) {
        LangSkill obj = new LangSkill();
        setSkill(obj, index);
        //setSpokenlanguage(obj, index);
        setStandardizedpatient(obj, index);
        return obj;
    }
    
    public void LangSkillDataOnDemand.setSkill(LangSkill obj, int index) {
        LangSkillLevel skill = LangSkillLevel.class.getEnumConstants()[0];
        obj.setSkill(skill);
    }
    
//    public void LangSkillDataOnDemand.setSpokenlanguage(LangSkill obj, int index) {
//        SpokenLanguage spokenlanguage = spokenLanguageDataOnDemand.getRandomSpokenLanguage();
//        obj.setSpokenlanguage(spokenlanguage);
//    }
    
    public void LangSkillDataOnDemand.setStandardizedpatient(LangSkill obj, int index) {
        StandardizedPatient standardizedpatient = standardizedPatientDataOnDemand.getRandomStandardizedPatient();
        obj.setStandardizedpatient(standardizedpatient);
    }
    
    public LangSkill LangSkillDataOnDemand.getSpecificLangSkill(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        LangSkill obj = data.get(index);
        return LangSkill.findLangSkill(obj.getId());
    }
    
    public LangSkill LangSkillDataOnDemand.getRandomLangSkill() {
        init();
        LangSkill obj = data.get(rnd.nextInt(data.size()));
        return LangSkill.findLangSkill(obj.getId());
    }
    
    public boolean LangSkillDataOnDemand.modifyLangSkill(LangSkill obj) {
        return false;
    }
    
    public void LangSkillDataOnDemand.init() {
        data = LangSkill.findLangSkillEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'LangSkill' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ch.unibas.medizin.osce.domain.LangSkill>();
        for (int i = 0; i < 10; i++) {
            LangSkill obj = getNewTransientLangSkill(i);
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
