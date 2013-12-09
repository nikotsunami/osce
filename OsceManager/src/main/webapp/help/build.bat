@echo on

set template=template.html
set stylesheet=github.css
set infile=manual.md
set outfile=index.html
set options=--toc ^
 --standalone ^
 --chapters ^
 --number-sections ^
 --section-divs 
REM --email-obfuscation=javascript 

REM type %infile% >> test.txt
REM type %infile% >> test.txt

pandoc %options% --css=%stylesheet% --template=%template% --output=%outfile% %infile%

pause