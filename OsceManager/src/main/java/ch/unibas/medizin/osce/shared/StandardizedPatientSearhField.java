package ch.unibas.medizin.osce.shared;

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
     * Getter for static content 
     * @return the part of search query to attach to the search
     */
    public String getQueryPart() {
		return queryPart;
	}
}
