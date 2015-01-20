
# SETUP									

1. For GWT 2.7.0, STS -3.6.3 or higher version is required. So download STS with this version.
		
2. Please verify attributes of "sts.ini" file against the attributes given below and add/update accordingly. In place of "C:/Program Files/Java/jdk1.8.0_05/bin/javaw.exe" 
	 paste your java installation directory path. Also set initial memory setting as per available memory.

	----------------------------------------
		-vm
		C:/Program Files/Java/jdk1.8.0_05/bin/javaw.exe
		-startup
		plugins/org.eclipse.equinox.launcher_1.3.0.v20140415-2008.jar
		--launcher.library
		plugins/org.eclipse.equinox.launcher.win32.win32.x86_64_1.1.200.v20140603-1326
		-product
		org.springsource.sts.ide
		--launcher.defaultAction
		openFile
		-server
		--launcher.XXMaxPermSize
		384M
		-vmargs
		-Dosgi.requiredJavaVersion=1.6
		-Xmn1024m
		-Xms2048m
		-Xmx4096m
		-Xss1m
		-XX:PermSize=512m
		-XX:MaxPermSize=1024m
		-Dorg.eclipse.swt.browser.IEVersion=10001
	--------------------------------------------------------------------

3. After starting STS, click on Help --> Install new software and follow the steps as given below:
	4. In the "works with" field paste the url "https://dl.google.com/eclipse/plugin/4.4".
	5. Click Add button.
	6. A list of tools will be shown. Select "SDKS" and "Google plugin for eclipse".
	7. Click next, then accept the licence agreement and install the SDK. 

4. Restart the STS when asked after installation.

5. Click Window --> Preferences --> Google --> Web Toolkit. Select GWT SDK version 2.7.0. If required GWT SDK version is not shown, for example, 2.7.0 (in our case) is not shown, then download GWT SDK 2.7.0 from http://www.gwtproject.org/versions.html into a local directory. After that, follow the below mentioned steps:
	6. Click on "Add" button.
	7. Paste the GWT 2.7.0 SDK directory path.
	8. Give the name, for example, GWT 2.7.0.
	9. Apply the changes.
			
6. Click on Project --> Properties --> Java Compiler --> Annotation processing
	7. Enable the checkbox "Enable project specific settings" and "Enable annotation processing".
	8. Add new variable with name 'verbose' and value 'false'.
	9. Click "Apply".
		
7. Click on Project --> Properties --> Java Compiler --> Annotation processing --> Factory path
	8. Enable the checkbox "Enable project specific settings".
	9. Click on "Add external jars".
	10. Add the jar file "requestfactory-apt.jar" from the installation directory of gwt 2.7.0.
	11. Select the checkbox named with this jar and deselect other.
	12. Click "Apply" and rebuild the project.
			
8. Click Project --> Properties --> Project Facets --> Java. If the java version is not 1.7, change the java version to 1.7.

9. Right click on OsceManager in project explorer.
	10. Go to OsceManager --> Properties --> Google --> Web Toolkit. 
	11. Remove the OsMaEntry.
	12. Click on "Add" button.
	13. Add new entry OsMaEntry-ch.unibas.medizin.osce.
		
10. Update maven depencies. Right click on OsceManager(Project Explorer) --> Maven --> Update Project. Select the checkbox "Force update snapshots".
					
11. Clean and build the project.

13. Change the run configuration.
	14. Click Run --> Run configuration.
	15. Click GWT.
	16. Remove the current available OsMaEntry.
	17. Click Add button.
	18. Add OsMaEntry-ch.unibas.medizin.osce.
	19. Click Arguments 
	20. In program arguments, paste following path:
			
			-war E:\OSCE\backup\src_backup\OsceManager\target\OsceManager-0.1.0.BUILD-SNAPSHOT
			-remoteUI "${gwt_remote_ui_server_port}:${unique_id}" -startupUrl OsMaEntry.html
			-logLevel INFO -codeServerPort 9997 -port 8888
			
	Change your directory path in aformentioned path and apply the changes.
			
14. Click Window --> Preferences --> Validation. A list of validators will appear. From the list in the "Build" column, deselect all except "Application client validator", "Classpath dependency validator" and "Connector validator". This process is done to fasten the Build process of project.
				
15. Click OsceManager (Package Explorer)--> Run As --> GWT Web Application (Classic mode).
				
16. System may ask for target path. Give target path as E:\OSCE\backup\src_backup\OsceManager\target\OsceManager-0.1.0.BUILD-SNAPSHOT
17. Here in place of E:\OSCE\backup\src_backup\OsceManager\target\OsceManager-0.1.0.BUILD-SNAPSHOT paste your directory path till OsceManager-0.1.0.BUILD-SNAPSHOT
18. Apply the changes.

