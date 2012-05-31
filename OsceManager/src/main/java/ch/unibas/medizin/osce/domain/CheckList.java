package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooEntity
public class CheckList {

    @NotNull
    private String title;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkList")
    @OrderBy("sort_order")
    private List<ChecklistTopic> checkListTopics = new ArrayList<ChecklistTopic>();
  
}
