package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;
import java.util.Set;
import ch.unibas.medizin.osce.domain.LangSkill;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class SpokenLanguage {

    @Size(max = 40)
    private String languageName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spokenlanguage")
    private Set<LangSkill> langskills = new HashSet<LangSkill>();
}
