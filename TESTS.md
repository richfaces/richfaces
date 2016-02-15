How To Run Framework Tests
==========================

Framework tests allow you to run a set of Arquillian-based tests using the Graphene and Warp extensions on supported browsers and containers.

The supported container matrix is:

* WildFly 8.1
* WildFly 8.2
* WildFly 9.0
* WildFly 10.0
* JBoss EAP 6.2
* JBoss EAP 6.3
* JBoss EAP 6.4
* Tomcat 7
* Tomcat 8

Note: for specific supported versions consult the pom.xml

The supported browser matrix is:

* PhantomJS (default)
* Chrome
* Firefox
* others (TBD)

TL;DR
=====

Running a full build including smoke tests:

    mvn install -Dintegration=wildfly82 -Dsmoke

Running particular framework test (on Chrome) from console:

    // console 1: start WildFly 8.2 
    ./wildfly-8.2.0.Final/bin/standalone.sh

    // console 2: start Selenium Server
    java -jar selenium-server-standalone-${VERSION}.jar -Dwebdriver.chrome.driver=/opt/google/chrome/chromedriver
    
    // console 3: run a test
    cd richfaces/core/
    mvn verify -Dintegration=wildfly82-remote -Dbrowser=chrome -Dreusable -DskipTests=true -Dtest=IT_RF12765

You can also add following parameters to skip CDK build and/or Resource Optimization and/or disable unit tests:

    -Dgeneration.skip -Doptimization.skip -DskipTests -Dcheckstyle.skip
    
It is oftentimes useful to deploy the test archive manually to debug a test without continually rebuilidng and deploying it.  To retain a deployable war in the target folder, add the following parameter:

    -Darquillian.deploymentExportPath=target
    
Conversely, to run _just_ the CDK generation invoke the plugin directly as in:

    mvn richfaces-cdk:generate
    // or
    mvn richfaces-cdk:generate -Dcdk.recompile=true

Framework Tests Overview
========================

Framework tests are designed to fully leverage Arquillian in order to achieve extensive integration test coverage.

They are:

* reusable across modules
* runnable from build system (Maven) and IDE (Eclipse)

Naming conventions
------------------

In order to distinguish integration tests from unit tests, we adopted a convention of using an `IT` prefix (stands for integration test).
Thus each test class' name needs to start with `IT*`, e.g.:

* `ITAutocompleteEvents`
* `IT_RF12765`

Test run modes
--------------

There are two ways to run tests:

* *managed*
  * suitable one-off test execution (without prior development environment setup)
  * suitable for continuous integration
  * complete container and browser lifecycle management
    * downloads and starts container
    * manages browser session
* *remote*
  * best for development and fast turnaround
  * container and browser adapters reuse running instances

Changing RichFaces version
--------------------------

You can change the version of RichFaces the test will run against by passing the following property to the Maven execution:

    -Darquillian.richfaces.version=4.5.0.Alpha2

or modifying the pom.xml:

    <arquillian.richfaces.version>4.5.0.Alpha2</arquillian.richfaces.version>


This will allow you to run newer tests against previous versions and verify that the issue is regression.


JSF version Notes
-----------------

Some containers (JBoss AS, GlassFish, TomEE) bundle the JSF version in their distribution, however for Tomcat 6, 7, and 8, the JSF implementation needs to be bundled (by default, the version specified in RichFaces BOM will be used).

You can influence the version used during the test on Tomcats using the following Maven property:

    -Darquillian.richfaces.jsfImplementation=org.glassfish:javax.faces:2.1.7

or modifying the pom.xml:

    <arquillian.richfaces.jsfImplementation>org.glassfish:javax.faces:2.1.7</arquillian.richfaces.jsfImplementation>

Using MyFaces
-------------

In order to test RichFaces using MyFaces as JSF implementation, you need to either use the TomEE container or Tomcat 6/7/8 with enforced MyFaces dependency (in the first case using version specified in RichFaces BOM)

    -Darquillian.richfaces.jsfImplementation=org.apache.myfaces.core:myfaces-impl

or enforcing a specific version:

    -Darquillian.richfaces.jsfImplementation=org.apache.myfaces.core:myfaces-impl:2.1.10

or you can modify the POM:

    <arquillian.richfaces.jsfImplementation>org.apache.myfaces.core:myfaces-impl</arquillian.richfaces.jsfImplementation>

or you can use Maven profile selection in IDE to select the "myfaces" profile.


Choosing a browser
------------------

To switch the browser used in test execution, you can use the following Maven property:

    -Dbrowser=chrome

By default, tests will use headless browser `phantomjs`.


Using reusable Selenium session
-------------------------------

In order to reuse a running Selenium Server and connect to an instantiated browser, use:

    -Dreusable

Selecting tests to run
----------------------

You can skip the unit tests using following Maven option - in this case only the Framework tests will be executed:

    -DskipTests=true

You can select particular Framework test to be executed by passing the following Maven option:

    -Dtest=TestName


Test Categories
===============

Framework tests are categorized in several categories by purpose, which determines when and how often they will be run.

To categorize a test, you can either:

Annotate a test case with @Category:

    @RunWith(Arquillian.class)
    @Category(...)
    public class ITMyFeatureTest() {
        @Test
        public void testXYZ() {
            ...
        }
    }

Annotate a test method with @Category:

    @RunWith(Arquillian.class)
    public class ITMyFeatureTest() {
    
        @Test
        @Category(...)
        public void testXYZ() {
            ...
        }
    }

The categories are Java interfaces stored in `build-resources/` maven module under the package `category`.

The execution of certain categories is described in the top-level `richfaces-parent` module (`pom.xml`).

Smoke Tests
-----------

`category.Smoke`

The basic set of tests which should be run with each commit.

The number of smoke tests needs to be kept in reasonable numbers, because those tests should be run as often as possible but still they should affect the core framework and components' features.

To run these tests from Maven, use:

    -Dsmoke

Failing Tests
-------------

`category.Failing`

These tests are currently known to fail, but they should pass once the referenced issue is fixed.

Other categories
----------------

The intention is to use more categories as required to:

* exclude tests targeting a specific environment (e.g. Java EE vs. non Java EE)
* exclude tests which are not running on given browser

Test inclusion / exclusion intends to provide as extensive test coverage for all supported environments, but still avoid the known failures to affect test results.  The categories should be designed for two purposes, as evidenced from the samples:

* `JavaEEOnly` - these tests will be run *only* on Java EE capable containers
* `FailingOnPhantomJS` - these tests *won't* be executed on PhantomJS
* `FailingOnMyFaces`, `FailingOnFirefox`, `FailingOnTomcat` - these tests are *currently failing*, but they should pass once the referenced issue is fixed

Note that those categories use keywords `*Only`, `No*` and `FailingOn*` in order to be sufficiently descriptive.

Screenshots during testing
==========================

There is a possibility to take screenshots during executing tests. Its done via Graphene Screenshooter extension. Basic configuration is set in `arquillian.xml` under `screenshooter` qualifier (by default it takes screenshots when the test fails). For more configuration options see [Graphene screenshooter docs](https://github.com/arquillian/arquillian-graphene/tree/master/extension/screenshooter).

To take screenshots one has to run the build with `take-screenshots` profile.

    mvn verify -Dintegration=wildfly82 -Ptake-screenshots

    
Managed Containers 
==================

### WildFly 8.1 - Managed

    mvn verify -Dintegration=wildfly81

### WildFly 8.2 - Managed

    mvn verify -Dintegration=wildfly82

### WildFly 9.0 - Managed

    mvn verify -Dintegration=wildfly90

### WildFly 10.0 - Managed

    mvn verify -Dintegration=wildfly100

### JBoss EAP 6.2 - Managed

    mvn verify -Dintegration=jbosseap62

### JBoss EAP 6.3 - Managed

    mvn verify -Dintegration=jbosseap63

### JBoss EAP 6.4 - Managed

    mvn verify -Dintegration=jbosseap64

### Tomcat 7 - Managed

    mvn verify -Dintegration=tomcat7

### Tomcat 8 - Managed

    mvn verify -Dintegration=tomcat8


Providing container distribution
--------------------------------

By default, all managed container are configured to obtain a distribution from specified Maven artifact using `arquillian.container.distribution` property.

You can specify an URL that a container distribution should be downloaded from using the same property:

    -Darquillian.container.distribution=file:///tmp/wildfly-dist-8.2.0.Final.zip

or

    -Darquillian.container.distribution=http://some.repository/wildfly-dist-8.2.0.Final.zip


Remote Containers
=================

For quick turnaround, you should use an IDE.

In order to execute the tests against running container and browser instances, you should activate the following profiles:

* `integration-tests`
* `browser-firefox`
* `browser-remote-reusable`

and activate one container specific profile as listed bellow.

First, start the Selenium Server:

    java -jar selenium-server-standalone-${VERSION}.jar -Dwebdriver.chrome.driver=/opt/google/chrome/chromedriver

then run the test from the IDE (eg. in Eclipse: `Run As > JUnit Test`).


### WildFly 8.1 - Remote

Start: `[wildfly-8.1]$ ./bin/standalone.sh`

Profile: `wildfly-remote-8-1`

    mvn verify -Dintegration=wildfly81-remote

### WildFly 8.2 - Remote

Start: `[wildfly-8.2]$ ./bin/standalone.sh`

Profile: `wildfly-remote-8-2`

    mvn verify -Dintegration=wildfly82-remote

### WildFly 9.0 - Remote

Start: `[wildfly-9.0]$ ./bin/standalone.sh`

Profile: `wildfly-remote-9-0`

    mvn verify -Dintegration=wildfly90-remote

### WildFly 10.0 - Remote

Start: `[wildfly-10.0]$ ./bin/standalone.sh`

Profile: `wildfly-remote-10-0`

    mvn verify -Dintegration=wildfly100-remote

### JBoss EAP 6.2 - Remote

Start: `[jboss-eap-6.2]$ ./bin/standalone.sh`

Profile: `jbosseap-remote-6-2`

    mvn verify -Dintegration=jbosseap62-remote

### JBoss EAP 6.3 - Remote

Start: `[jboss-eap-6.3]$ ./bin/standalone.sh`

Profile: `jbosseap-remote-6-3`

    mvn verify -Dintegration=jbosseap63-remote

### JBoss EAP 6.4 - Remote

Start: `[jboss-eap-6.4]$ ./bin/standalone.sh`

Profile: `jbosseap-remote-6-4`

    mvn verify -Dintegration=jbosseap64-remote

### Tomcat 7/8 - Remote

You need to modify the `conf/tomcat-users.xml` file:

    <tomcat-users>
        <role rolename="manager" />
        <role rolename="manager-jmx" />
        <role rolename="manager-script" />
        <role rolename="manager-gui" />
        <role rolename="manager-status" />
        <role rolename="admin" />
        <user username="admin" password="pass" roles="standard,manager,admin,manager-jmx,manager-gui,manager-script,manager-status" />
    </tomcat-users>

Pass the following options to the console prior starting the server (or add it to `./bin/catalina.sh`):

    export JAVA_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8089 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Djava.rmi.server.hostname=127.0.0.1"

Start the container:

    ./bin/catalina.sh run


Start: `[apache-tomcat-8.0.9]$ ./bin/catalina.run.sh`

Profile: `tomcat-remote-8`

    mvn verify -Dintegration=tomcat8-remote


Reusing Test Infrastructure Setup
=================================

The framework test infrastructure is designed to be reusable across framework modules:

* all Maven setup is in `richfaces-parent` (top-level `pom.xml`)
* `arquillian.xml` is in `build-resources` and is copied over placeholder `src/test/arquillian-settings/arquillian.xml`
* all shared Java classes for test setup and configuration are in `build-resources` (`src/main/java`)

In order to use those tests, one needs to:

* inherit the maven module from `richfaces-parent`
* active `integration-tests` profile
* add dependency `build-resources` on test classpath
* follow test naming convention (*IT**) & use appropriate categories
