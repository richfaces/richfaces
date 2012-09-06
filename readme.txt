Work in progress.

I'm using this branch to port the master branch to Java EE 6. The master branch can be built with maven but probably won't run under current systems.

Sep 6, 2012
Tests written for the org.richfaces.photoalbum.service package run successfuly. To run them under maven use '-Parquillian-jbossas-managed' parameter. Bear in mind that the build will still fail once it reaches the WAR module. 
