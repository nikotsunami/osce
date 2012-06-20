package ch.unibas.medizin.osce.domain;

import java.util.List;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.allen_sauer.gwt.log.client.SystemLogger;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooEntity
public class AnamnesisCheckTitle {

    @NotNull
    @Size(max = 255)
    private String text;

    private Integer sort_order;

    
    
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sort_order == null) ? 0 : sort_order.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnamnesisCheckTitle other = (AnamnesisCheckTitle) obj;
		if (sort_order == null) {
			if (other.sort_order != null)
				return false;
		} else if (!sort_order.equals(other.sort_order))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
    
    
    public void insertNewSortOder(Integer previousSortOder){
    	//TODO 
    	System.out.println("!!!!!!!!!!!!!!!!this is insertNewSortOder ");
    	if(previousSortOder == 0){
    	List<AnamnesisCheckTitle> anamnesisCheckTitles = findAllAnamnesisCheckTitles();
    	for(AnamnesisCheckTitle anamnesisCheckTitleBlow : anamnesisCheckTitles){
    		if(anamnesisCheckTitleBlow.sort_order != null){
    			anamnesisCheckTitleBlow.sort_order = anamnesisCheckTitleBlow.sort_order + 1;
    			anamnesisCheckTitleBlow.persist();
    		}
    	}
    	this.sort_order = 1;
    	this.persist();
    	}
    }
    

}
