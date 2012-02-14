package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooEntity
public class Keyword {

    @NotNull
    @Size(min = 2, max = 255)
    private String name;
    
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "keywords")
    private Set<StandardizedRole> standardizedRoles = new HashSet<StandardizedRole>();
}
