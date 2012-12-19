The Photo Album application: short description


1 Building and Running the application

1.1. Requirements

-Maven 3.0.3 or later
-JBoss AS 7

1.2 Optional Additional Software
-svn client (only if you want to build the application with a full set of images)
- Eclipse IDE + JBoss Tools (to explore and run the application in IDE)

1.3 Building the application

By default Photoalbum is assembled with a limited set of images (4-5 in each album). In order to build the version of the application with a full set of images you need to use livedemo profile while building Photoalbum (details further in the text).

To build the project you need to navigate to the root folder and run

mvn clean install

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. 

With the created .war file you also need to deploy a datasource and a database driver for JBOSS. The files are located in src/main/resources:
    - h2-1.3.161-redhat-1.jar
    - photoalbum-ds.xml
In Eclipse you can select those files and choose "Mark as Deployable" from the context menu or you deploy them manually. For further information refer to https://community.jboss.org/en/tools/blog/2012/02/28/excited-about-jboss-as-71-part-i-deployable-datasources

You can deploy the application on the server by copying the target/richfaces-photoalbum.war file to the JBOSS_HOME/standalone/deployments folder. Then, launch the run.bat file from JBOSS_HOME/bin/ directory to start the server.

To build the project with a full set of images you need to run

mvn clean install -Plivedemo

To make sure the project is built successfully you need to have a SVN client installed on your local machine, for example Subversion. To launch the application use the instructions given above.

1.4 Predefined users

To use the features available to registered users either create your own account or use one of the predefined ones:
	- amarkhel
	- Viking
	- Noname
	- user_for_add
	- user_for_del
	- user_for_dnd

the password is 12345 in all cases.