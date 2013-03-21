OSCEManager
===========

OsceManager is rich-client web application designed for assisting in the 
organisation of [Objective Structured Clinical Examinations][1] (OSCEs) as well as 
documenting the whole process. The software has been designed at the [Faculty of
Medicine at the University of Basel][2] and starts to be used productively there (as
of January 2013).

Complementary to OSCEManager, [OSMA-DMZ][3] and eOSCE are used to assist in 
collecting simpat-data, and for electronic assistance during the OSCEs, 
respectively.

Known Issues
------------
- General Issues:
  * Synchronicity: If multiple users work in the software at the same time and
    one of the users is creating a new Semester, then the other user only sees it
    when reloading the whole browser window. (because the navigation never gets
    updated with new data)
- SimPat:
  * *When deleting a simpat that has assigned Scars, the application tries to 
    delete the scar as well.*
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
  * When adding a new title the SuggestBox gets temporarily colored yellow.
- OSCE creation:
  * When no semester has been created, the whole *OSCE Administration* submenu
    should be deactivated.
  * There is a null entry in the Semester dropdown
- SP Assignment / Selection (RoleAssignmentPlace):
  * First name and name are in the wrong order
- Schedule:
  * An examiner can be assigned to different stations at the same time!
- DMZ Sync:
  * Error messages don't seem to be displayed properly
- IndividualSchedulesView:
  * Color names are not rendered

Suggested Features / Improvements
---------------------------------
- General Deployment:
  * Folders (e.g. for image upload) should be dynamically configured instead of
    having these statically defined. At least the root directory should be 
    configurable to some extent; currently it is fixed at /usr/oscemanager which
    is problematic when running multiple instances on one server.
- General UI:
  * Lots of views are pretty ugly; especially these grey borders.
  * Lots of ValueListBox objects have null values. It would be nice if they 
    didn't. (e.g. Academic year dropdown)
  * An icon class that helps getting rid of the ugly OsMaConstant-hack.
  * If tables (or other views) are empty because there is no data in the 
    database, there should be no loading gifs displayed.
  * Some kind of BreadcrumbNavigation may have been nice.
  * The loading circle on the darkened screen may be terribly annoying in some
    situations, especially because of the delay until it appears. E.g. when 
    assigning a room to a station in "Define circuit details", it takes several
    seconds between pressing the okay button and the loading circle to appear.
  * Dates and times in general could be slightly beautified; maybe tied in 
    with i18n
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
  * Nicer fields / assistance when entering time values (e.g. user could enter
    non-numeric values; this is ugly) and date.
  * A possibility to have an OSCE WITHOUT lunch break may be nice?
- Edit schedule:
  * The right click menu for swapping students and SPs is rather obscure.
    
    Suggestion: Use Rollover-Tooltips for the general information (instead of
    forcing the user to click) and have tooltips with possible actions displayed
    on left click.
- Error Handling in general:
  * IN 2013, Error popup windows should be a thing of the past! Errors related
    to user data entries should be shown where they are relevant!
- Cleanup:
  * MessageConfirmationDialogBox is a widget, but is in 
    ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination

[1]: http://en.wikipedia.org/wiki/Objective_structured_clinical_examination
[2]: http://medizin.unibas.ch/
[3]: https://github.com/nikotsunami/osma-dmz
