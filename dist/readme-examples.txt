========================================================================

                      RichFaces - Ajax enabled JSF 2.0 component library

                                          RichFaces 4.0.0.Final EXAMPLES
                                                    http://richfaces.org
                                                              March 2011
                     This software is distributed under the terms of the 
                            FSF Lesser Gnu Public License (see lgpl.txt)

========================================================================

RichFaces 4 is a component library for JSF2 and an advanced framework for
easily integrating AJAX capabilities into enterprise applications.

This file explains how to set up the RichFaces examples bundled with the
distribution package. If you find any issue with the project, please report 
the issue on the RichFaces user forum (http://community.jboss.org/en/richfaces) 
or jira (https://issues.jboss.org/browse/RF).


SETTING UP THE EXAMPLES
-----------------------

    1. Requirements
    
    In order to build the Examples applications you will need:
       - Maven 2.1.0 or later
       - Servers: Apache Tomcat 6.0 or any JEE6 application server 
       	 (e.g. Jboss AS 6/7)
       - JDK 1.6

    Also optional Additional Software would make it easy for you to work 
    with the sources of the application:

       - Eclipse IDE + JBoss Tools (to explore and run the application 
	 in IDE). You're free to use any other IDE also but we haven't 
	 tested the application in other environments. 

    2. Build/Deploy

       - Deploy on Tomcat/Jetty

	 To build the project for Tomcat you need to navigate to the 
      	 /examples/richfaces-showcase or /examples/core-demo directories 
	 and run:

	         mvn clean package

	 When you see the BUILD SUCCESSFUL message you can deploy the 
	 application on the server. To deploy it on Tomcat, copy the .war 
	 file from 'target' folder to TOMCAT_HOME/webapps folder. Then, 
	 launch the startup.sh or startup.bat script from TOMCAT_HOME/bin/ 
	 directory to start the server.

       - Deploy on JEE6 server (JBoss AS 6)

	 To build the project for a JEE6 server you need to navigate to the 
	 /examples/richfaces-showcase or /examples/core-demo directory 
	 and run:

	         mvn clean package -Pjee6
         
	 When you see the BUILD SUCCESSFUL message you can deploy the 
	 application on the server. To deoploy it on the application server 
	 copy the .war file from 'target' folder to the JBOSS_HOME/server/default/deploy 
	 folder (or change "default/" to used profile folder). Then, launch 
	 the run.sh or run.bat script from JBOSS_HOME/bin/ directory to start 
	 the server.

After deploying the examples to your server open a browser and type 
http://localhost:8080/richfaces-showcase or http://localhost:8080/core-demo 
to view the examples.

Additional information about build profiles for these applications 
and working with them can be found inside the readme files for application 
itself. 

NOTE: richfaces-showcase contains readme.txt which shows how to 
publish the application to Google Application Engine. 


USING AN IDE TO WORK WITH EXAMPLES
----------------------------------

In order to explore, run and deploy the examples using Eclipse IDE you use 
one of the following options:
    
    - JBoss Tools with m2eclipse plugin and just import 
      the examples as maven-based projects.
      	  * Open Eclipse and go to File > Import
	  * Expand the project type named Maven and select Import Maven 
	    Project
	  * Browse the directory in which you placed the examples, select 
	    it and press OK.
	  * Press next on the import wizard to finish importing the 
	    examples.

    - Or if you are not using m2eclipse execute use Maven to convert the 
      project to an Eclipse project:
      	 * Open a terminal inside the RichFaces examples directory
	 * execute mvn eclipse:ecllipse
	 * Open Eclipse and select File > Import
	 * Select Import as existing project into workspace after build 
	   is complete.
	 * Follow the instructions on the import wizard.

You are now able to work with the examples within Eclipse.

PUSH COMPONENT EXAMPLE
------------------------------

We've completed the Push feature sample "irc-client" prior to release but there were not enough
time in order to include into the distribution. So currently you could check out 
it from svn at http://anonsvn.jboss.org/repos/richfaces/branches/4.0.X/examples/irc-client .
It contains separate readme file which describes build and additional settings required.
We will add it to distribution in the next releases.

MORE INFORMATION AND RESOURCES
------------------------------

For more information on RichFaces 4, please visit RichFaces 
website at:

    - http://richfaces.org

Or check the Wiki for more information on build instructions for the examples at:

    - http://community.jboss.org/wiki/HowtoworkwithRichFaces4xexamplessources

To report or check the status of issues related to RichFaces Examples 
use the project's Jira at:
    
    - https://issues.jboss.org/browse/RF

Community support and help can be obtained from the RichFaces forums at:

    - http://community.jboss.org/en/richfaces?view=discussions



###
