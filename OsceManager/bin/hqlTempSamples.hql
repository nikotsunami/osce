SELECT COUNT(StandardizedPatient) FROM StandardizedPatient stdPat  
where  
stdPat.name LIKE '%:q%'  OR  
stdPat.preName LIKE '%:q%'  OR  
stdPat.descriptions.description LIKE '%:q%'  OR  
stdPat.bankAccount.BIC LIKE '%:q%'  OR  
stdPat.bankAccount.IBAN LIKE '%:q%'  OR  
stdPat.bankAccount.bankName LIKE '%:q%'  ORDER BY name ASC