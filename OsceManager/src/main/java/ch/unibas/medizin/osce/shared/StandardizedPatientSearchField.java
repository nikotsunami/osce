package ch.unibas.medizin.osce.shared;

//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
/**
 * Universal context search for patients
 */
public enum StandardizedPatientSearchField {
	/**
	 * Fields from StandardizedPatient can be added/deleted here 
	 */
	comment(" stdPat.descriptions.description LIKE :q "),
    name(" stdPat.name LIKE :q "),
    preName(" stdPat.preName LIKE :q "),
    street(" stdPat.street LIKE :q "),
    city(" stdPat.city LIKE :q "),
    // FIXME: postalCode search doesn't work bc. Integer...
    postalCode(" stdPat.city LIKE :q "),
    email(" stdPat.email LIKE :q "),
    BIC(" stdPat.bankAccount.BIC LIKE :q "),
    IBAN(" stdPat.bankAccount.IBAN LIKE :q "),
    bankName(" stdPat.bankAccount.bankName LIKE :q "),
    telephone(" stdPat.telephone LIKE :q "),
    telephone2(" stdPat.telephone2 LIKE :q "),
    mobile(" stdPat.mobile LIKE :q ");
	

    private String queryPart;
    
    private StandardizedPatientSearchField(String queryPart){
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
