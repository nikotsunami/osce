package ch.unibas.medizin.osce.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.gwt.requestfactory.shared.Request;

import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.Set;
import ch.unibas.medizin.osce.domain.Semester;
import ch.unibas.medizin.osce.shared.BindType;
import ch.unibas.medizin.osce.shared.Comparison2;
import ch.unibas.medizin.osce.shared.PossibleFields;

import java.util.HashSet;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

@RooJavaBean
@RooToString
@RooEntity
public class AdvancedSearchCriteria {
    
    @NotNull
	private PossibleFields field;
    @NotNull
	private BindType bindType;
    @NotNull
	private Comparison2 comparation;
    @NotNull
    @Size(max = 255)
	private String value;

  
}
