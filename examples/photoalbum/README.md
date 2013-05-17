# The Photo Album application: short description

## Building and Running the application

### Requirements

 * Maven 3.0.3 or later
 * JBoss AS 7

### Optional Additional Software
	
 * svn client (only if you want to build the application with a full set of images)
 * Eclipse IDE + JBoss Tools (to explore and run the application in IDE)

### Building the application

By default Photoalbum is assembled with a limited set of images (4-5 in each album). In order to build the version of the application with a full set of images you need to use livedemo profile while building Photoalbum (details further in the text).

To build the project you need to navigate to the root folder and run

	mvn clean install

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. 

You can deploy the application on the server by copying the _target/richfaces-photoalbum.war_ file to the _JBOSS_HOME/standalone/deployments_ folder. Then, launch the run.bat file from JBOSS_HOME/bin/ directory to start the server.

To build the project with a full set of images you need to run

	mvn clean install -Plivedemo

To make sure the project is built successfully with this livedemo profile, you need to have a SVN client installed on your local machine, for example Subversion. To launch the application use the instructions given above.

### Predefined users

To use the features available to registered users either create your own account or use one of the predefined ones:

 *	 amarkhel
 *	 Viking
 *	 Noname
 *	 user\_for\_add
 *	 user\_for\_del
 *	 user\_for\_dnd

the password is _12345_ in all cases.