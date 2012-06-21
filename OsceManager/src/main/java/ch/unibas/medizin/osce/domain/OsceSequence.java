package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class OsceSequence {
	
	 
	 private Integer numberRotation;
	 
	 private String label;
	
	 @ManyToOne
	 private OsceDay osceDay;
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceSequence")
	 private Set<Course> courses = new HashSet<Course>();
	 
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "osceSequence")
	 @OrderBy("sequenceNumber")
	 private List<OscePost> oscePosts = new ArrayList<OscePost>();
	 
	
}
