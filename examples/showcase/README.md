RichFaces Showcase
==================

**RichFaces: Ajax enabled JSF 2.0 component library**

**Online demo <http://showcase.richfaces.org>**

* RichFaces 4.5
* <http://richfaces.org>
* This software is distributed under the terms of the FSF Lesser Gnu Public License (see lgpl.txt)
    
RichFaces showcase is an application created to show **RichFaces components
in action.** It contains a set of small use-cases implemented using RichFaces components.
    
The examples shown in the application were chosen from the most popular
questions at the user forum. You will find a lot of useful information on 
how to implement RichFaces components and **re-use** the code to implement the 
components in your own application.
    
This file explains how to build and execute the showcase application on
your own server.

Building and running the application
====================================
    
Requirements
------------
    
In order to build the Showcase application you will need:
    
* Maven 3.0.3 or later
* Servers: Apache Tomcat 6.0.x/7.0.x or any Java 
EE 6 application server (e.g. JBoss AS 7)
* JDK 1.6 or better
    
Also optional Additional Software would make it easy for you to work with the sources of the application:
    
* *Eclipse IDE* + *JBoss Tools* (to explore and run the application in IDE). You're obviously free to use any other IDE you wish, but JBoss Tools is recommended. Check <http://www.jboss.org/tools/download> out for tools downloading.
* Another option is to use *JBoss Developer Studio*, where you can find all required plugins pre installed for better convenience. Check <https://devstudio.jboss.com/download/6.x.html> out for developer studio downloading. 
    
Build / Deploy / Test
---------------------

### Deploying on Tomcat

To build the project for Tomcat you need to navigate to the ``/examples/richfaces-showcase`` and run:
    
    mvn clean package
    
When you see the `BUILD SUCCESSFUL` message you can deploy the 
application on the server. To deploy it on Tomcat, copy the *.war* 
file from `target` folder to ``TOMCAT_HOME/webapps`` folder. Then, launch the *startup.sh* or *startup.bat* script from ``TOMCAT_HOME/bin/`` directory to start the server.
    
### Deploying on EAP 6 / WildFly 8, 9, 10
    
To build the project for a JEE6 server you need to navigate to the ``/examples/richfaces-showcase`` and run
    
for for EAP 6 / WildFly 8, 9, 10
    
    mvn clean package -Pjee6
    
When you see the `BUILD SUCCESSFUL` message you can deploy the application on the server.
    
First, make sure the application server is running.  To start the server:
launch the `standalone.sh` or `standalone.bat` script from ``JBOSS_HOME/bin/`` together with parameter ``--server-config=standalone-full.xml`` for both latest JBoss AS 7.1.x and for latest JBoss AS 7.0.x.
    
To **deploy** it on the application server, use either:
    
1. use the server's management console, which is bound by default at <http://localhost:9990>

2. or copy the `.war` file from `target` folder to the folder: ``JBOSS_HOME/standalone/deployments``
    
After deploying the examples to your server open a browser and type 
<http://localhost:8080/showcase> to view the examples. Note that the URL depends on the context on which your application server deployed the showcase application.

### How to Test the Project

The tests work quite the same as framework test. For more information see [Framework Tests](https://github.com/richfaces/richfaces5/blob/master/TESTS.md) document.

#### Some examples

For testing on managed WildFly 8.2 on Firefox use:

   ``mvn clean verify -Dintegration=wildfly82 -Dbrowser=firefox``

For testing on managed Tomcat 7 using Mojarra:

   ``mvn clean verify -Dintegration=tomcat7``

For testing on managed Tomcat 8 on Firefox using MyFaces use:

   ``mvn clean verify -Dintegration=tomcat8 -Dbrowser=firefox -Dmyfaces``


Setting up Eclipse to work with the showcase
--------------------------------------------
    
In order to explore, run and deploy the Showcase using Eclipse IDE you can use one of the following options:
    
1. You could use **JBoss Tools** with *m2eclipse plugin* and just import 
the examples as maven-based projects.
    * Open Eclipse and go to `File > Import`
    * Expand the project type named Maven and select Import Maven Project
    * Browse the directory in which you placed the examples, select it and press OK.
    * Press next on the import wizard to finish importing the examples.
    
**You are now able to work with the Showcase within Eclipse.** Note that by using JBoss Developer Studio you can skip installing all of the required plugins and you can import the project right away.
    
* In order to deploy the Showcase on WildFly 8.2 from Eclipse one needs to:
    * Select the right maven profile: ``jee6``
        * Either by pressing hot key `CTRL + ALT + P`, while the showcase project is selected
        * Or right click on the showcase project in the `project explorer --> Properties --> Maven` and fill in the input: `jee6`
    * Alter the deployment assembly
        * Right click on the showcase project --> Properties --> Deployment assembly. By default there should be: src/main/java, src/main/resources, src/main/resources-jee6/, src/main/webapp
        * One needs to add src/main/webapp-jee6: hit the add button and select the folder option, find the webapp-jee6 and add it
    * Now the showcase can be **deployed**, be sure that you are loading the showcase application on the correct context root and also that there was not added a default `persistence.xml` in ``src/main/resources/META-INF`` (delete it). The URL one should access looks like: <http://localhost:8080/showcase>
    
    
More information and resources
------------------------------

The Showcase is accessible online as well:

* http://showcase.richfaces.org/

For more information on RichFaces 4, please visit RichFaces website at:
    
* <http://richfaces.org>

To report or check the status of issues related to RichFaces Examples use the project's Jira at:
        
* <https://issues.jboss.org/browse/RF>
    
Community support and help can be obtained from the RichFaces forums at:
    
* <http://community.jboss.org/en/richfaces?view=discussions>


Mobile compatibility
====================
    
This showcase is also mobile compatible with *WebKit* based browsers; this
includes *iOS*, *Android*, and Others. After you deploy the showcase, just
browse to the default context path (for example, <http://localhost:8080/showcase>) with a mobile iOS or Android based device.
    
You will see the showcase has been modified to fit and **dynamically** adjust to mobile screen widths and orientations. 
    
We removed some components from the RichFaces Mobile Showcase which were not mobile ready. For example, ``rich:tooltip``, ``rich:extendedDataTable``, ``rich:jquery``, and ``rich:popupPanel`` either did not make sense in a mobile environment or needed a heavy rewrite for touch interfaces.
    
``rich:dragDrop`` however, does work on iOS Mobile Safari, but not on Android. To use ``rich:dragDrop`` in Mobile Safari browsers, you can include this snippet of [JavaScript](https://github.com/richfaces/components/blob/develop/mobile-compatibility/rf-dnd.js) at the bottom of your JSF template.

