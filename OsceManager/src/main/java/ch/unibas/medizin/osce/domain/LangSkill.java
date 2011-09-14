package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.SpokenLanguage;

@RooJavaBean
@RooToString
@RooEntity
public class LangSkill {

    @Size(max = 40)
    private String skill;

    @ManyToOne
    private StandardizedPatient standardizedpatient;

    @ManyToOne
    private SpokenLanguage spokenlanguage;
}
