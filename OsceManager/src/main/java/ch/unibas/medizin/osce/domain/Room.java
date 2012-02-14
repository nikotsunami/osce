package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import ch.unibas.medizin.osce.domain.OscePostRoom;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class Room {

    @NotNull
    @Size(min = 1, max = 20)
    private String roomNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private Set<OscePostRoom> oscePostRooms = new HashSet<OscePostRoom>();

    private Double length;

    private Double width;
}
