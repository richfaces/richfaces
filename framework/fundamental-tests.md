How To Run Fundamental Tests
============================

Fundamental tests allows you to run set of Arquillian-based tests using
Graphene and Warp extensions on supported browsers and containers.

The supported container matrix:

* JBoss AS 7.1
* TomEE 1.5
* GlassFish 3.1
* Tomcat 6
* Tomcat 7

Note: for specific supported versions, consult build/parent/pom.xml

The supported browser matrix:

* PhantomJS
* Chrome
* Firefox

Running tests
-------------

There are two methods how to run tests:

* managed
  * suitable one-off test execution (without prior development environment setup)
  * suitable for continuous integration
  * complete container and browser lifecycle management
    * downloads and starts container
    * manages browser session
* remote
  * best for development and fast turnaround
  * container and browser adapters reuses running instances
  * not support for Tomcat7 (missing remote adapter)

Changing RichFaces version
--------------------------

You can change the version of RichFaces the test will run against by passing following property
to Maven execution:

    -Darquillian.richfaces.version=4.2.3.Final

or modifying pom.xml:

    <arquillian.richfaces.version>4.2.3.Final</arquillian.richfaces.version>


This will allow you to run newer tests against previous versions and verify that the issue is regression.


Notes to JSF version
--------------------

Some containers (JBoss AS, GlassFish, TomEE) bundle the JSF version in distribution,

however for Tomcat 6 and Tomcat 7, the JSF implementation needs to be bundled (by default, the version specified in RichFaces BOM will be used).

You can influence the version used during the test on Tomcats using following Maven property:

    -Darquillian.richfaces.jsfImplementation=org.glassfish:javax.faces:2.1.7

or modifying pom.xml:

    <arquillian.richfaces.jsfImplementation>org.glassfish:javax.faces:2.1.7</arquillian.richfaces.jsfImplementation>

Using MyFaces
-------------

In order to test RichFaces using MyFaces as JSF implementation, you need to either use TomEE container or Tomcat 6 or 7 with enforced MyFaces dependency (in first case using version specified in RichFaces BOM)

    -Darquillian.richfaces.jsfImplementation=org.apache.myfaces.core:myfaces-impl

or enforcing specific version:

    -Darquillian.richfaces.jsfImplementation=org.apache.myfaces.core:myfaces-impl:2.1.10

or you can adequately modify POM:

    <arquillian.richfaces.jsfImplementation>org.apache.myfaces.core:myfaces-impl</arquillian.richfaces.jsfImplementation>

or you can use Maven profile selection in IDE to select "myfaces" profile.


Choosing browser
----------------

To switch a browser, you can use following Maven property:

    -Dbrowser=chrome

By default, tests will use phantomjs.


Using reusable Selenium session
----------------------------------------

In order to reuse running Selenium Server and connect to instantiated browser, use:

    -Pbrowser-remote-reusable

Selecting tests to run
----------------------

You can skip the unit tests using following Maven option - in this case only fundamental tests will be executed:

    -DskipTests=true

You can select particular fundamental test to be executed by passing following Maven option:

    -Dtest=TestName

    
Managed Containers 
==================

JBoss AS 7.1 - Managed
----------------------

    mvn verify -Dintegration=jbossas71 -Dbrowser=firefox

TomEE 1.5 - Managed
-------------------

    mvn verify -Dintegration=tomee15 -Dbrowser=firefox

GlassFish 3.1 - Managed
-----------------------

    mvn verify -Dintegration=glassfish31 -Dbrowser=firefox

Tomcat 6 - Managed
------------------

    mvn verify -Dintegration=tomcat6 -Dbrowser=firefox


Tomcat 7 - Managed
------------------

    mvn verify -Dintegration=tomcat7 -Dbrowser=firefox


Remote Containers
=================

For quick turnaround, you should use IDE.

In order to execute the tests against running container and browser instances, you should activate following profiles:

* integration-tests
* browser-firefox
* browser-remote-reusable

and activate one container specific profile as listed bellow.

At first, start the Selenium Server:

    java -jar selenium-server-standalone-${VERSION}.jar -Dwebdriver.chrome.driver=/opt/google/chrome/chromedriver

JBoss AS 7.1 - Remote
---------------------

Start: [jboss-as-7.1.1.Final]$ ./bin/standalone.sh
Profile: jbossas-remote-7-1

GlassFish 3.1 - Remote
----------------------

Start: [glassfish3]$ ./glassfish/bin/startserv
Profile: glassfish-remote-3-1

TomEE 1.5 - Remote
------------------

Start: [apache-tomee-webprofile-1.5.1]$ ./bin/tomee.sh start
Profile: tomee-1-5

Tomcat 6 - Remote
-----------------

You need to modify a `conf/tomcat-users.xml`:

    <tomcat-users>
        <role rolename="manager" />
        <role rolename="manager-jmx" />
        <role rolename="manager-script" />
        <role rolename="manager-gui" />
        <role rolename="manager-status" />
        <role rolename="admin" />
        <user username="admin" password="pass" roles="standard,manager,admin,manager-jmx,manager-gui,manager-script,manager-status" />
    </tomcat-users>

Pass following options to the console prior starting server (or add it to `./bin/catalina.sh`):

    export JAVA_OPTS="-Dcom.sun.management.jmxremote.port=8089 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"

Start the container:

    ./bin/catalina.sh run

Start: [apache-tomcat-6.0.33]$ ./bin/catalina.run.sh 
Profile: tomcat-remote-6


Developing from Maven
=====================

If you are running tests from Maven instead of IDE, following combination of properties might be handy:

    mvn verify -Pintegration-tests -Pjbossas-remote-7-1 -Dbrowser=firefox -DskipTests=true -Dbrowser-remote-reusable -Dtest=RF12765_Test
