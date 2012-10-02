Work in progress.

I'm using this branch to port the master branch to Java EE 6. The master branch can be built with maven but probably won't run under current systems.

Sep 6, 2012
Tests written for the org.richfaces.photoalbum.service package run successfuly. To run them under maven use '-Parquillian-jbossas-managed' parameter. Bear in mind that the build will still fail once it reaches the WAR module. 

Oct 2, 2012
Almost all Richfaces components have been updated.
Merged classes and resources into the WAR module (source/web), other packages should no longer be needed (unless I have overlooked something).
----
The project (source/web) can be successfully built and deployed, but so far it doesn't do anything else than look pretty and show errors.
