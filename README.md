OSCEManager
===========

OsceManager is rich-client web application designed for assisting in the 
organisation of [Objective Structured Clinical Examinations][1] (OSCEs) as well as 
documenting the whole process.

Known Issues
------------
- General Issues:
  * Synchronicity: If multiple users work in the software at the same time and
    one of the users is creating a new Semester, then the other user only sees it
    when reloading the whole browser window. (because the navigation never gets
    updated with new data)
- Examinators:
  * SimplePager is missing in Examinator table
- Role Definitions:
  * The search filter does not work (nothing happens upon changing any values
    in the search filter window)
- DefaultSuggestBox:
  * If dropdown menu open, a click on the arrow doesn't close the dropdown menu
- Questionnaire:
  * "previous question" has no influence on the ordering of questions
  * when clicking "save" the parent category closes
  * "Add title" / "Add category" buttons move around when clicking "edit"
  * "Add answer" moves delete buttons around
  * Generally weird behaviour in selection / editing etc.
- OSCE creation:
  * When no semester has been created, the whole *OSCE Administration* submenu
    should be deactivated.
  * There is a null entry in the Semester dropdown

Suggested Features / Improvements
---------------------------------
- General UI:
  * Lots of views are pretty ugly; especially these grey borders.
  * An icon class that helps getting rid of the ugly OsMaConstant-hack.
  * If tables (or other views) are empty because there is no data in the 
    database, there should be no loading gifs displayed.
  * Some kind of BreadcrumbNavigation may have been nice.
- Simulated Patients:
  * Instead of having separate "Delete" and "Anonymize" buttons, a single 
    delete-button would be sufficient, if the software would automatically
    determine wether an SP could be deleted or has to be anonymized.
- Attributes:
  * Possibly exchange TraitTypes enum with a traitType-table in database, so as 
    to allow for more customisation.
- Examiners:
  * The OSCE- and role-participation histories could be more informative.
  * Maybe role- and OSCE should be linked!
- Role definitions:
  * Role filter view: re-think, properly align ui elements
  * Skills could have an additional boolean field that forces a skill to *never*
    be tested with a simpat (and therefore limiting the actual role definition
    as to not allow it to be a simpat-type of role)
  * The dropdown selections of e.g. discipline should not require an ok button, 
    instead the discipline / skill / room is selected upon click and the popup
    gets closed. This would require only a cancel button. Behaviour right now is
    slightly confusing.
  * The abbreviated dicipline / role / skill names in the accordeonView should 
    be abbreviated in a way that takes advantage of the width of the fields they
    are contained in.
- Edit Circuit Details:
  * Rooms that already have a station assigned should be either removed from 
    room selection or marked (and possibly moved to the bottom of the list)
  * The save button behaviour is confusing. There are basically three ways data
    in this view is saved instead of just one.

[1]: http://en.wikipedia.org/wiki/Objective_structured_clinical_examination
