package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooEntity
public class Specialisation {

    @NotNull
    @Size(min = 2, max = 255)
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialisation")
    private Set<RoleTopic> roleTopics = new HashSet<RoleTopic>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "specialisation")
    private Set<Doctor> doctors = new HashSet<Doctor>();
}
