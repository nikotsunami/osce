OSCE Manager
===========

OSCE Manager is rich-client web application designed for assisting in the 
organisation of [Objective Structured Clinical Examinations][1] (OSCEs) as well as 
documenting the whole process. The software has been designed at the [Faculty of
Medicine at the University of Basel][2] and starts to be used productively there (as
of January 2013).

Complementary to OSCE Manager, [OSMA-DMZ][3] and eOSCE are used to assist in 
collecting simpat-data, and for electronic assistance during the OSCEs, 
respectively.

To find out more about the OSCE Manager, have a look at its website, located at
[www.osce-manager.com][4].

Known Issues
------------
###General Issues
  * Synchronicity: If multiple users work in the application at the same time and
    one of the users is creating a new Semester, then the other user only sees it
    when reloading the whole browser window. (because the navigation never gets
    updated with new data)
  * **DefaultSuggestBox**: If dropdown menu open, a click on the arrow doesn't 
  close the dropdown menu
	
###SimPat
  * Uploading images does not work. Also, as soon as you click on the image upload
	icon, the message *Image uploaded successfully* appears, even though an 
	image was not even selected yet.
  * When deleting a simpat that has assigned attributes (Scars), the application 
	tries to delete the scar as well.
  * In the same vein, attributes can not be deleted from an SP, once they have 
	been added.
  * Search criteria don't work precisely. E.g. a search for *gender != male AND 
    gender != female* should return all the SPs that have no gender assigned, which
	it currently doesn't (or of course if you search for *gender != male*, it should
	return all females and NULLs).
  * When anonymizing an SP, nothing is anonymized; some fields just get an "XXX"
	attached to them.
  * When deleting an SP that has an assigned role; a warning pops up, that the 
	SP can not be deleted because uf assigned roles, but then tha status of the
	SP will be changed to ANONYMIZED without actually trying to anonymize 
	anything or warning the user, that it is going to anonymize data.
	
###Questionnaire
I use the word "group" sometimes to refer to the entity AnamnesisCheckTitle!

  * When deleting an entire group that still has  questions, a hibernate exception
	message appears! Show a dialog instead, warning the user that there are still
	questions in the group and if he wants to also delete the questions. If he
	clicks yes, the group as well as the questions should be deleted.
  * When deleting a question, it does not disappear from the list of questions on
	the left without manually "refreshing" the list e.g. by closing and 
	re-opening the group.
  * It would be better if there was a delete button for each question in the table
	on the left instead of in the details view. The details view is not actualyl
	necessary. It'd be quicker if the edit view would come up as soon as you 
	click on a question.
  * When adding a new question:
	- the "type" dropdown contains a null entry.
	- if the question is the first of the current group, the "previous question" 
		field displays the **answers** to the other questions within the same 
		AnamnesisCheckTitle.
	- the choice of "previous question" does not have any influence on anything.
		It doesn't seem to get stored.
	- Questions always get added as the first question in a group, even if moved
		from another group or a "previous question" was selected.
	- **By default a question should be added at the end of a group!**

###Examiners
  * SimplePager is missing in Examiner table

###Role Definitions
  * The search filter does not work (nothing happens upon changing any values
    in the search filter window)
  * When deleting checklist questions, the list numbers are not updated.
  * When deleting a skill (role_topic) that has been assigned to a role 
	(standardized_role), a non-conforming window appears, with a confusing
	sentence.
  * When adding a skill in the *Role definitions* screen, instead of the 
	*Disciplines & Skills* screen, it will not belong to a discipline and 
	therefore can not be deleted anymore.
  * There is an *Export* button  in the Role table view, but nothing happens
	when it is clicked.
	
### Questionnaire
  * "previous question" has no influence on the ordering of questions
  * when clicking "save" the parent category closes
  * "Add title" / "Add category" buttons move around when clicking "edit"
  * "Add answer" moves delete buttons around
  * Generally weird behaviour in selection / editing etc.
  * When adding a new title the SuggestBox gets temporarily colored yellow.
  
### OSCE creation
  * When no semester has been created, the whole *OSCE Administration* submenu
    should be deactivated.
  * Export of SP-tables: The assigned stations are not correct if there was a
	station without SP in the sequence. Seems to be a problem in the HTML-generation:
	If a station does not have SP's, no <td></td> Tag-Pairs are created.
  * The timetable in the Invitations is wrongly ordered; it starts with pm times even
	though it should start with am times. (also customiseable time-format would be nice
	for us Europeans that prefer 24-hour time formats)
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
###General Deployment
  * Folders (e.g. for image upload) should be dynamically configured instead of
    having these statically defined. At least the root directory should be 
    configurable to some extent; currently it is fixed at /usr/oscemanager which
    is problematic when running multiple instances on one server.
	
###General UI
  * Text-entries general
		- If you have e.g. a simple edit pop up where there is e.g. one text value or 
		one dropdown box and one text value or if you have any table where you 
		create a new entry with a single text value, it should be possible to just
		press *Enter* without having to manually click on *Ok* or *Add* etc.
	* Auto-complete box
		- after entering text in the box, if there is only one choice left; it 
		should maybe be automatically selected?
		- After selecting a choice by clicking it, the box should lose focus 
		because otherwise even if you press a function key (F1-F12), it gets 
		interpreted as a regular keystroke, thus expanding the menu.
		- If there are lots of items in an auto-complete box, it may be good to 
		have the filter work in a way that searches for words seperately. E.g. 
		if you search for "children many", instead of just showing any question
		containing this exact sequence of words it should display all the 
		questions containing both words, e.g. "How many children do you have?".
		It should not show hits where only one of the words appears, though!
  * Lots of views are pretty ugly; especially the grey borders.
  * Lots of ValueListBox objects have null/empty values. It would be nice if 
	they didn't. (e.g. Academic year dropdown) Easily doable by setting the 
	default value before defining accepted values.
  * An icon class that helps getting rid of the ugly OsMaConstant-hack.
  * If tables (or other views) are empty because there is no data in the 
    database, there should be no loading gifs displayed.
  * Some kind of BreadcrumbNavigation may have been nice.
  * The loading circle on the darkened screen may be terribly annoying in some
    situations, especially because of the delay until it appears. E.g. when 
    assigning a room to a station in "Define circuit details", it takes several
    seconds between pressing the okay button and the loading circle to appear.
	This defies the point of AJAX entirely as the responsiveness of the 
	application gets reduced to the point where it might just as well load a new
	page entirely.
  * Dates and times in general could be slightly beautified; maybe tied in 
    with i18n.

###Simulated Patients
  * Instead of having separate "Delete" and "Anonymize" buttons, a single 
    delete-button would be sufficient, if the software would automatically
    determine wether an SP could be deleted or has to be anonymized. (Of course
	the user should be informed about what is going to happen and why! - this 
	has been implemented, I think but not thoroughly tested)
  * When filtering the questionnaire using the search box on the bottom left, it 
	would be nice if only those tabs are visible, that contain matching 
	questionnaire items.
	
###Attributes
  * Possibly exchange TraitTypes enum with a traitType-table in database, so as 
    to allow for more customisation.

###Examiners
  * The OSCE- and role-participation histories could be more informative.
  * Maybe role- and OSCE should be linked!
  
###Role definitions
  * Disciplines & Skills: The search box for disciplines should maybe also 
    search skills, so you can search for a discipline containing a certain 
	skill. E.g. if I have the skill "CPR" in the discipline "First Aid", If I 
	would type "CPR" into the search box, the discipline "First Aid" would show
	up in the result.
  * Role filter view (search settings): re-think, properly align ui elements
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
  * Deleting a role does not work.
  * Copying a role does not work as intended. When copying a role, *the 
	checklist will not be copied*, instead the same checklist as the original
	role uses, will be linked. This makes the copy feature basically useless.
	When copying a role, the checklist should be copied as well and the 
	standardized_role should link the copied checklist. The same seems to be the 
	case for role scripts!
	
### Edit Circuit Details
  * Rooms that already have a station assigned should be either removed from 
    room selection or marked (and possibly moved to the bottom of the list)
  * The save button behaviour is confusing. There are basically three ways data
    in this view is saved instead of just one.
  * Nicer fields / assistance when entering time values (e.g. user could enter
    non-numeric values; this is ugly) and date.
  * A possibility to have an OSCE WITHOUT lunch break may be nice?
  
### Edit schedule
  * The right click menu for swapping students and SPs is rather obscure.  
    Suggestion: Use Rollover-Tooltips for the general information (instead of
    forcing the user to click) and have tooltips with possible actions displayed
    on left click.
###Error Handling in general
  * IN 2013, Error popup windows should be a thing of the past! Errors related
    to user data entries should be shown where they are relevant!
###Cleanup
  * MessageConfirmationDialogBox is a widget, but is in 
    ch.unibas.medizin.osce.client.a_nonroo.client.ui.examination
###SP-Portal:
  * SP's should be able to write additional comments when they are surveyed for
    availability.

[1]: http://en.wikipedia.org/wiki/Objective_structured_clinical_examination
[2]: http://medizin.unibas.ch/
[3]: https://github.com/nikotsunami/osma-dmz
[4]: http://www.osce-manager.com
