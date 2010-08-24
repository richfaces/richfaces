RichFaces 4.0 Examples
=========================================================
Version 4.0.0.M2

This software is distributed under the terms of the FSF Lesser Gnu Public License (see lgpl.txt). 

Getting up and running quickly
-------------------------------
The core-demo and richfaces-showcase demo applications should be built using Maven 2.1.0

tomcat6/jetty targeted build:

1) execute "mvn clean package" in root application folder(examples/richfaces-showcase or examples/core-demo)
2) Deploy to tomcat 6.x (built .war file is located in the 'target' folder) or just run using maven on jetty using "mvn clean jetty:run-war"

JEE6 (GlassFish 3, JBoss AS 6) server targeted build: 
1) execute "mvn clean package -Pjee6" in root application folder(examples/richfaces-showcase or examples/core-demo)
2) Deploy built application (built .war file is located in the 'target' folder) to application server

Type http://localhost:8080/richfaces-showcase http://localhost:8080/core-demo in your browser address bar after starting server.

More additional information about build profiles for concrete applications and working with them - inside readme's for application itself. 

NEW for M2: richfaces-showcase contains readme.txt which shows how to publish the application to Google Application Engine. 

Using IDE to work with examples
-------------------------------
	*You could use Jboss Tools with m2eclipse plugin and just import the examples as maven-based projects.
	
	*Or if you have not using m2eclipse - execute:
		mvn eclipse:ecllipse 
	in root example folder and import to ecplise just as existent project after build complete. 



Learn more
----------
RichFaces Project - http://www.jboss.org/richfaces
RichFaces 4.0 Wiki - http://www.jboss.org/community/wiki/RichFaces40Planning