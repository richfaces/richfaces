RichFaces showcase is an application intended to show RichFaces components in action. It contains set of small use-cases implemented using RichFaces components.  These use-cases for the demo are chosen from most popular questions on the user forums.  You will be able to find a lot of useful information and will be able to re-use some of it to implement the same cases for your applications. 

1 Building and Running the application

1.1. Requirements

- Maven 2.1.0 or later
- Servers: Apache Tomcat 6.0 or any JEE6 application server (e.g. JBoss AS 6)
- JDK 1.6

1.2 Optional Additional Software
- Eclipse IDE + JBoss Tools (to explore and run the application in IDE).
- You're free to use any other IDE also but we haven't tested an application in other environments. 

1.3 Build/Deploy

1.3.1) Tomcat 
To build the project you need to navigate to the /examples/richfaces-showcase and run

mvn clean package

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. You can deploy it on the server by copying .war file from 'target' folder to 
the TOMCAT_HOME/webapps folder. Then, launch the startup.sh or startup.bat script from TOMCAT_HOME/bin/ directory to start the server.

1.3.2) JEE6 server(JBoss AS 6)
To build the project you need to navigate to the /examples/richfaces-showcase and run

mvn clean package -Pjee6

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. You can deploy it on the server by copying .war file from 'target' folder to 
the JBOSS_HOME/server/default/deploy folder(or change "default/" to used profile folder). Then, launch the run.sh or run.bat script from JBOSS_HOME/bin/ directory to start the server.

2) Publishing to Google Application Engine
 
We have completed a resource plugin which generates static resources which allow us to create Google App Engine compatible applications using RichFaces.  This means deploying to and working with GAE is a piece of cake.  Here is how you can deploy your own RichFaces showcase example.

--) Update the GAE application name, and register it with google.  Do this by updating the /webapp-gae/WEB-INF/appengine-web.xml following the GAE standard documentation.

--) Only if using MyFaces: Configure the MyFaces encryption secret values in the /webapp-gae/WEB-INF/web.xml.  Replace the "-- Insert some secret here --" with a base64 encoded password.  This is important because otherwise MyFaces will generate random keys, and this causes issues in a cluster environment like GAE.

See below for more information:
  MyFaces - http://wiki.apache.org/myfaces/Secure_Your_Application
  Mojarra - http://wikis.sun.com/display/GlassFish/JavaServerFacesRI#JavaServerFacesRI-HowcanIsecureviewstatewhenusingclientsidestatesaving%3F

--) Build using: 

mvn clean package -Pgae

--) Use GAE SDK and the appcfg script just as you would for any other GAE application.

There are some additional changes that were made in order to make richfaces-showcase GAE compatible:
	* WebConfiguration class from com.sun.faces were patched removing code which is not compatible with GAE
	* web.xml with additional properties for GAE created and placed at src\main\webapp-gae\WEB-INF\ (it replaces common one during build with GAE profile)
	* check pom.xml GAE profile section in order to check additional dependencies for that configuration.

3) Working with the project in Eclipse IDE:
In order to explore, run and deploy the application in Eclipse IDE you can use one of the following options:

	* Just import as maven project if using m2eclipse plugin.
	
	* without m2eclipse - build it with the

		 mvn clean install eclipse:clean eclipse:eclipse 

	  comand and just import as existent project.

You can find more details in the RichFaces Getting Started Guide (visit documentation page at http://jboss.org/richfaces)

