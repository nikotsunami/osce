OSCEManager
===========

What is the OSCEManager?
------------------------

OsceManager is rich-client web application designed for assisting in the 
organisation of Objective Structured Clinical Examinations (OSCEs) as well as 
documenting the whole process.

Known Issues
------------
- Examinators:

  SimplePager is missing in Examinator table  

- DefaultSuggestBox:
  
  if dropdown menu open, a click on the arrow doesn't close the dropdown menu

- Questionnaire:
  
  * "previous question" has no influence on the ordering of questions
  * when clicking "save" the parent category closes
  * "Add title" / "Add category" buttons move around when clicking "edit"
  * "Add answer" moves delete buttons around
  * Generally weird behaviour in selection / editing etc.

Suggested Features / Improvements
---------------------------------
- Simulated Patients:

  Instead of having separate "Delete" and "Anonymize" buttons, a single 
  delete-button would be sufficient, if the software would automatically
  determine wether an SP could be deleted or has to be anonymized.

- Attributes:

  Possibly exchange TraitTypes enum with a traitType-table in database, so as 
  to allow for more customisation.

- Examiners:

  The OSCE- and role-participation histories could be more informative.

  Maybe role- and OSCE should be linked!
