// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ch.unibas.medizin.osce.domain;

import ch.unibas.medizin.osce.domain.AnamnesisChecksValue;
import ch.unibas.medizin.osce.shared.AnamnesisCheckTypes;
import java.lang.Integer;
import java.lang.String;
import java.util.Set;

privileged aspect AnamnesisCheck_Roo_JavaBean {
    
    public String AnamnesisCheck.getText() {
        return this.text;
    }
    
    public void AnamnesisCheck.setText(String text) {
        this.text = text;
    }
    
    public String AnamnesisCheck.getValue() {
        return this.value;
    }
    
    public void AnamnesisCheck.setValue(String value) {
        this.value = value;
    }
    
    public Integer AnamnesisCheck.getSort_order() {
        return this.sort_order;
    }
    
    public void AnamnesisCheck.setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }
    
    public AnamnesisCheckTypes AnamnesisCheck.getType() {
        return this.type;
    }
    
    public void AnamnesisCheck.setType(AnamnesisCheckTypes type) {
        this.type = type;
    }
    
    public Set<AnamnesisChecksValue> AnamnesisCheck.getAnamnesischecksvalues() {
        return this.anamnesischecksvalues;
    }
    
    public void AnamnesisCheck.setAnamnesischecksvalues(Set<AnamnesisChecksValue> anamnesischecksvalues) {
        this.anamnesischecksvalues = anamnesischecksvalues;
    }

    public AnamnesisCheck AnamnesisCheck.getTitle() {
        return this.title;
    }
    
    public void AnamnesisCheck.setTitle(AnamnesisCheck title) {
        this.title = title;
    }
    
}
