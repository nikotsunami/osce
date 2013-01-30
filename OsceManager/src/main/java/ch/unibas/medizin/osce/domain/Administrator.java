package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findAdministratorsByNameNotEquals", "findAdministratorsByNameNotEqualsAndPreNameLikeOrSemesters" })
public class Administrator {

    @NotNull
    @Column(unique = true)
    @Size(min = 6, max = 40)
    private String email;

    @NotNull
    @Size(max = 40)
    private String name;

    @NotNull
    @Size(max = 40)
    private String preName;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Semester> semesters = new HashSet<Semester>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administrator")
    private Set<Task> tasks = new HashSet<Task>();

    public static Long countAdministratorsByName(String name) {
        return null;
    }

    public static List<Administrator> findAdministratorsByNameNotEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Administrator.entityManager();
        TypedQuery<Administrator> q = em.createQuery("SELECT o FROM Administrator AS o WHERE o.name != :name", Administrator.class);
        q.setParameter("name", name);
        return q.getResultList();
    }
}
