package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import ch.unibas.medizin.osce.domain.StandardizedPatient;

import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import ch.unibas.medizin.osce.domain.SpokenLanguage;
import ch.unibas.medizin.osce.shared.LangSkillLevel;

@RooJavaBean
@RooToString
@RooEntity
public class LangSkill {

	@Enumerated
    @NotNull
    private LangSkillLevel skill;

    @ManyToOne
    private StandardizedPatient standardizedpatient;

    @ManyToOne
    private SpokenLanguage spokenlanguage;
}
