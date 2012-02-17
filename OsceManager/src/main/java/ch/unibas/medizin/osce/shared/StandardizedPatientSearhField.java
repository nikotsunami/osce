package ch.unibas.medizin.osce.shared;

//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
/**
 * Universal context search for patients
 */
public enum StandardizedPatientSearhField {
	/**
	 * Fields from StandardizedPatient can be added/deleted here 
	 */
	comment(" stdPat.descriptions.description LIKE :q "),
    name(" stdPat.name LIKE :q "),
    preName(" stdPat.preName LIKE :q "),
    street(" stdPat.street LIKE :q "),
    city(" stdPat.city LIKE :q "),
    postalCode(" stdPat.preName LIKE :q "),
    email(" stdPat.email LIKE :q "),
    BIC(" stdPat.bankAccount.BIC LIKE :q "),
    IBAN(" stdPat.bankAccount.IBAN LIKE :q "),
    bankName(" stdPat.bankAccount.bankName LIKE :q "),
    telephone(" stdPat.telephone LIKE :q ");

    String queryPart;
    
    StandardizedPatientSearhField(String queryPart){
    	this.queryPart=queryPart;
    }
    /**
     * Count record Query search
     * @param em HQL entity manager
     * @param searchTerm target term to compare with
     * @return query to launch
     */
    
   //TODO: ###siebers### The Shared Package is compiled into gwt, the shouldn'd be spring classes lie Entity Manager!!!
 /*   public static TypedQuery<Long> createSearchQuery(EntityManager em,
    		String searchTerm){
    	StringBuilder result = new StringBuilder("SELECT COUNT(o) FROM StandardizedPatient o " +
    			"WHERE ");
    	if(values().length == 0)
    		throw new RuntimeException("Cannot have empty search term set");
    	for(StandardizedPatientSearhField value: values()){
    		result.append("o.");
    		result.append(value);
    		result.append(" LIKE :q OR ");
    	}
    	TypedQuery<Long> query = em.createQuery(result.substring(0, result.length()-3), Long.class);
    	query.setParameter("q", "%" + searchTerm + "%");
    	return query;
    }*/

    /**
     * Getter for static content 
     * @return the part of search query to attach to the search
     */
    public String getQueryPart() {
		return queryPart;
	}
}
