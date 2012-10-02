Work in progress.

I'm using this branch to port the master branch to Java EE 6. The master branch can be built with maven but probably won't run under current systems.

Sep 6, 2012
Tests written for the org.richfaces.photoalbum.service package run successfuly. To run them under maven use '-Parquillian-jbossas-managed' parameter. Bear in mind that the build will still fail once it reaches the WAR module. 

Oct 2, 2012
Almost all Richfaces components have been updated.
Merged classes and resources into the WAR module (source/web), other packages should no longer be needed (unless I have overlooked something) and you can build the project just from the WAR module (it still won't deploy successfully though).
