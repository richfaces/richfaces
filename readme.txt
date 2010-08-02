'richfaces-showcase' is an application intended to show RichFaces components in action. It contains set of small use-cases implemented using RichFaces components.
Except basic samples - use-cases for demo choosen from most popular questions at user forums - so you will be able to find much useful information there and will be able
just to re-use some code to implement the same cases for your applications. 


1 Building and Running the application


1.1. Requirements

- Maven 2.1.0 or later
- Apache Tomcat 6.0
- JDK 1.6

1.2 Optional Additional Software
- Eclipse IDE + JBoss Tools (to explore and run the application in IDE).
- You're free to use any other IDE also but we haven't tested an application in other environments. 


1.3 Building the application

To build the project you need to navigate to the /examples/richfaces-showcase and run

mvn clean package

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. You can deploy it on the server by copying .war file from 'target' folder to 
the TOMCAT_HOME/webapps folder. Then, launch the startup.sh or startup.bat script from TOMCAT_HOME/bin/ directory to start the server.

In order to explore, run and deploy the application in Eclipse IDE you can use one of the following options:

	* Just import as maven project if using m2eclipse plugin.
	
	* without m2eclipse - build it with the

		 mvn clean install eclipse:clean eclipse:eclipse 

	  comand and just import as existent project.

You can find more details in the RichFaces Getting Started Guide (visit documentation page at http://jboss.org/richfaces)



