package ch.unibas.medizin.osce.shared;

//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
/**
 * Universal context search for patients
 */
public enum RoleTopicSearchField {
	/**
	 * Fields from StandardizedPatient can be added/deleted here 
	 */

	
    name(" rt.name LIKE :q "),
    keyword(" k.name LIKE :q "),
    itemName(" rtiv.roleTableItem.itemName LIKE :q "),
    checkListItem(" sr.checkList.title LIKE :q "),
    roles(" sr.shortName LIKE :q "),
    itemValue("rtiv.value LIKE :q ");
	
   
    
    
    private String queryPart;
    
    private RoleTopicSearchField(String queryPart){
    	this.queryPart=queryPart;
    }

    /**
     * Getter for static content 
     * @return the part of search query to attach to the search
     */
    public String getQueryPart() {
		return queryPart;
	}
    
    public String getQueryPart(int tokenNo) {
    	return queryPart.replace(":q", ":q" + tokenNo);
    }
}
