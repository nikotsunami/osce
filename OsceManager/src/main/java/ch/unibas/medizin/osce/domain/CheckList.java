package ch.unibas.medizin.osce.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

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
