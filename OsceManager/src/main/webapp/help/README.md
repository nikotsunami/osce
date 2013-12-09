% How to assemble the manual

The OSCE Manager Manual was written in [Markdown][markdown], which allows for easy conversion 
to hypertext without the fuss of actually working with a full-blown hypertext format like 
HTML. Markdown documents are usually equally readable in plain-text form as they are when
compiled to HTML.

To compile the manual, I used [PanDoc][pandoc]. Use the build scripts supplied (build.bat
for Windows and build.sh for Unix-based systems) to easily compile the manual. The manual
will be created from the template *_template.html* and from the markdown document 
*_manual.md*. The result is an HTML file *index.html* using the CSS stylesheet 
*github.css* as well as some jQuery-based javascript you can find in the *js* subdirectory.
All the images used are in the *img* subdirectory.

Just for the sake of completeness, here's a list of what's in this directory. Files 
in boldface are required to be uploaded to the server you want to display the manual on.

- *build.bat*: Batch-file used to compile the manual from the source on Windows
	(requires PanDoc to be in the path).
- *build.sh*: Shell script used to compile the manual from the source on Unix based 
	systems (requires PanDoc to be in the path).
- **favicon.ico**: A favicon that can be used when placing the manual on a webserver.
- **github.css**: A modified version of githubs default markdown styling to suit the
	styling of the manual.
- **img**: Folder containing all the images used in the manual.
- **index.html**: The actual manual that resulted from compiling the markdown.
- **js**: Contains some JavaScript helpers to make the manual a bit more (or less) user
	friendly. 
- *manual.md*: The actual document containing the markdown source for the manual.
- *README.md*: what you are currently reading.
- *template.html*: The template from which the *index.html* is created. You can modify
	this to your liking, just don't do anything stupid with the variables.

[markdown]:
[pandoc]: 