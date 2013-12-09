#!/bin/bash

TEMPLATE="template.html"
STYLESHEET="github.css"
INFILE="manual.md"
OUTFILE="index.html"
OPTIONS="--toc \
 --standalone \
 --chapters \
 --number-sections \
 --section-divs"
# --email-obfuscation=javascript
# type %infile% >> test.txt
# type %infile% >> test.txt

pandoc $OPTIONS --css=$STYLESHEET --template=$TEMPLATE --output=$OUTFILE $INFILE