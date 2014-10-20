package ch.unibas.medizin.osce.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class MediaContentType {

	@PersistenceContext(unitName="persistenceUnit")
    transient EntityManager entityManager;
	
    @NotNull
    @Size(max = 255)
    private String contentType;

    @Size(max = 512)
    private String comment;
}
