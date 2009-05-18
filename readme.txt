The Photo Album application: short description



1 Building and Running the application


1.1. Requirements

-Maven 2.0.9 or later
-JBoss Application server 4.2.3 or 5.0

1.2 Optional Additional Software
-svn client( only if you want to build the application with a full set of images)
- Eclipse IDE + JBoss Tools (to explore and run the application in IDE)



1.3 Building the application

By default Photo Album is assembled with a limited set of images (4-5 in each album). In order to build the version of the application with a full set of images you need to use livedemo profile while building Photo Album(details further in the text).

To build the project you need to navigate to the examples/photoalbum/source/ and run

mvn clean install

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. You can deploy it on the server by copying the photoalbum/sources/ear/target/photoalbum-ear-1.0-SNAPSHOT.ear file to the JBOSS_HOME/server/default/deploy folder. Then, launch the run.bat file from JBOSS_HOME/bin/ directory to start the server.

To build the project with a full set of images you need to run

mvn clean install -Plivedemo

To make sure the project is built successfully you need to have a SVN client installed on your local machine, for example Subversion. To launch the application use the instructions given above.

In order to explore, run and deploy the application in Eclipse IDE build it with the
 mvn clean install eclipse:clean eclipse:eclipse eclipse:eclipse 

command and import the project to the IDE.  More details you can find in the JBoss Server Manager Reference Guide (http://download.jboss.org/jbosstools/nightly-docs/en/as/html/index.html)



