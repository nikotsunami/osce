package ch.unibas.medizin.osce.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import ch.unibas.medizin.osce.shared.PostType;

@RooJavaBean
@RooToString
@RooEntity
public class OscePostBlueprint 
{
	private Boolean isPossibleStart;
		
	@ManyToOne	   
    private RoleTopic roleTopic;
  
	@NotNull
	@ManyToOne	 
	private Osce osce;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "oscePostBlueprint")
	private Set<OscePost> oscePosts = new HashSet<OscePost>();
  
	@NotNull
    @Enumerated
    PostType postType;
  
	
	@ManyToOne	   
	private Specialisation specialisation;

	private Integer sequenceNumber;
}