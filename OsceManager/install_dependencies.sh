#!/bin/bash
mvn deploy:deploy-file -DgroupId=com.google.code.gwt-log -DartifactId=gwt-log -Dversion=3.1.3 -Dpackaging=jar -Dfile=lib/gwt-log-3.1.3.jar -Durl=file://lib -DrepositoryId=local-project-libraries
mvn deploy:deploy-file -DgroupId=com.google.code.gwt-dnd -DartifactId=gwt-dnd -Dversion=3.1.1 -Dpackaging=jar -Dfile=lib/gwt-dnd-3.1.1.jar -Durl=file://lib -DrepositoryId=local-project-libraries
mvn deploy:deploy-file -DgroupId=com.itextpdf -DartifactId=itextpdf -Dversion=5.2.1 -Dpackaging=jar -Dfile=lib/itextpdf-5.2.1.jar -Durl=file://lib -DrepositoryId=local-project-libraries
mvn deploy:deploy-file -DgroupId=com.itextpdf.text -DartifactId=itext-xtra -Dversion=5.2.1 -Dpackaging=jar -Dfile=lib/itext-xtra-5.2.1.jar -Durl=file://lib -DrepositoryId=local-project-libraries
mvn deploy:deploy-file -DgroupId=fr.hd3d.html5.video -DartifactId=video -Dversion=1.0 -Dpackaging=jar -Dfile=lib/fr.hd3d.html5.video.1.0.jar -Durl=file://lib -DrepositoryId=local-project-libraries
mvn deploy:deploy-file -DgroupId=com.mattbertolini.hermes -DartifactId=hermes -Dversion=1.2.0 -Dpackaging=jar -Dfile=lib/com.mattbertolini.hermes-1.2.0.jar -Durl=file://lib -DrepositoryId=local-project-librarise

mvn install:install-file -DgroupId=com.google.code.gwt-log -DartifactId=gwt-log -Dversion=3.1.3 -Dpackaging=jar -Dfile=lib/gwt-log-3.1.3.jar 
mvn install:install-file -DgroupId=com.google.code.gwt-dnd -DartifactId=gwt-dnd -Dversion=3.1.1 -Dpackaging=jar -Dfile=lib/gwt-dnd-3.1.1.jar 
mvn install:install-file -DgroupId=com.itextpdf -DartifactId=itextpdf -Dversion=5.2.1 -Dpackaging=jar -Dfile=lib/itextpdf-5.2.1.jar 
mvn install:install-file -DgroupId=com.itextpdf.text -DartifactId=itext-xtra -Dversion=5.2.1 -Dpackaging=jar -Dfile=lib/itext-xtra-5.2.1.jar 
mvn install:install-file -DgroupId=fr.hd3d.html5.video -DartifactId=video -Dversion=1.0 -Dpackaging=jar -Dfile=lib/fr.hd3d.html5.video.1.0.jar 
mvn install:install-file -DgroupId=com.mattbertolini.hermes -DartifactId=hermes -Dversion=1.2.0 -Dpackaging=jar -Dfile=lib/com.mattbertolini.hermes-1.2.0.jar
