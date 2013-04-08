How To Run Framework Tests
==========================

Framework tests allows you to run set of Arquillian-based tests using
Graphene and Warp extensions on supported browsers and containers.

The supported container matrix:

* JBoss AS 7.1
* TomEE 1.5
* GlassFish 3.1
* Tomcat 6
* Tomcat 7

Note: for specific supported versions, consult pom.xml

The supported browser matrix:

* PhantomJS (default)
* Chrome
* Firefox
* others (TBD)

TL;DR
=====

Running a full build including smoke tests:

    mvn install -Dintegration=jbossas71 -Dsmoke

Running particular framework test (on Chrome) from console:

    // console 1: start JBoss AS 7 
    ./jboss-as-7.1.1.Final//bin/standalone.sh

    // console 2: start Selenium Server
    java -jar selenium-server-standalone-${VERSION}.jar -Dwebdriver.chrome.driver=/opt/google/chrome/chromedriver
    
    // console 3: run a test
    cd richfaces5/framework/
    mvn verify -Pintegration-tests -Pjbossas-remote-7-1 -Dbrowser=chrome -Dreusable -DskipTests=true -Dtest=IT_RF12765


Framework Tests Overview
========================

The framework tests are designed to fully leverage Arquillian in order to achieve extensive integration coverage.

The are:

* reusable across modules
* runnable from build system (Maven) and IDE (Eclipse)

Naming conventions
------------------

In order to distinguish integration tests from unit tests, we adopted convention of using `IT` prefix (stands for integration test).
Thuse each test class' name needs to start with `IT*`, e.g.:

* `ITAutocompleteEvents`
* `IT_RF12765`

Test run modes
--------------

There are two methods how to run tests:

* *managed*
  * suitable one-off test execution (without prior development environment setup)
  * suitable for continuous integration
  * complete container and browser lifecycle management
    * downloads and starts container
    * manages browser session
* *remote*
  * best for development and fast turnaround
  * container and browser adapters reuses running instances
  * not support for Tomcat7 (missing remote adapter)

Changing RichFaces version
--------------------------

You can change the version of RichFaces the test will run against by passing following property
to Maven execution:

    -Darquillian.richfaces.version=5.0.0.Alpha1

or modifying pom.xml:

    <arquillian.richfaces.version>5.0.0.Alpha1</arquillian.richfaces.version>


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


Choosing a browser
------------------

To switch a browser, you can use following Maven property:

    -Dbrowser=chrome

By default, tests will use `phantomjs`.


Using reusable Selenium session
-------------------------------

In order to reuse running Selenium Server and connect to instantiated browser, use:

    -Dreusable

Selecting tests to run
----------------------

You can skip the unit tests using following Maven option - in this case only Framework tests will be executed:

    -DskipTests=true

You can select particular Framework test to be executed by passing following Maven option:

    -Dtest=TestName


Test Categories
===============

Framework tests are categorized in several categories by purpose which determines, when and how often they will be run.

To categorize a test, you can either:

Annotate a test case:

    @RunWith(Arquillian.class)
    @Category(...)
    public class ITMyFeatureTest() {
        @Test
        public void testXYZ() {
            ...
        }
    }

Annotate a test method:

    @RunWith(Arquillian.class)
    public class ITMyFeatureTest() {
    
        @Test
        @Category(...)
        public void testXYZ() {
            ...
        }
    }

The categories are Java interfaces stored in `build-resources/` maven module under package `category`.

The execution of certain categories is described in top-level `richfaces-parent` module (`pom.xml`).

Smoke Tests
-----------

`category.Smoke`

The basic set of tests which should be run with each commit.

The number of smoke tests needs to be kept in reasonable numbers, because those tests should be run as often as possible
but still they should affect the core framework and components' features.

To run these tests from Maven, use:

    -Dsmoke

Failing Tests
-------------

`category.Failing`

These tests are currently generally failing, but they should pass once the referenced issue is fixed.

Other categories
----------------

The intention is to use more categories as required to:

* exclude some tests targetting specific environment (e.g. Java EE vs. non Java EE)
* exclude some tests which are not running on given browser

Test inclusion / exclusion intends to provide as extensive test coverage for all supported environments,
but still avoid the known failures to affect test results.
The categories should be designed for two purposes, as evident from samples:

* `JavaEEOnly` - these tests will be run *only* on Java EE capable browsers
* `NoPhantomJS` - these tests *won't* be executed on PhantomJS
* `FailingOnMyFaces`, `FailingOnFirefox`, `FailingOnTomcat` - these tests are *currently failing*, but they should pass once the references issue is fixed

Note that those categories use keywords `*Only`, `No*` and `FailingOn*` in order to be descriptive.

    
Managed Containers 
==================

### JBoss AS 7.1 - Managed

    mvn verify -Dintegration=jbossas71

### TomEE 1.5 - Managed

    mvn verify -Dintegration=tomee15

### GlassFish 3.1 - Managed

    mvn verify -Dintegration=glassfish31

### Tomcat 6 - Managed

    mvn verify -Dintegration=tomcat6

### Tomcat 7 - Managed

    mvn verify -Dintegration=tomcat7


Remote Containers
=================

For quick turnaround, you should use IDE.

In order to execute the tests against running container and browser instances, you should activate following profiles:

* `integration-tests`
* `browser-firefox`
* `browser-remote-reusable`

and activate one container specific profile as listed bellow.

At first, start the Selenium Server:

    java -jar selenium-server-standalone-${VERSION}.jar -Dwebdriver.chrome.driver=/opt/google/chrome/chromedriver

then run the test from IDE (Eclipse: `Run As > JUnit Test`).

### JBoss AS 7.1 - Remote

Start: `[jboss-as-7.1.1.Final]$ ./bin/standalone.sh`
Profile: `jbossas-remote-7-1`

### GlassFish 3.1 - Remote

Start: `[glassfish3]$ ./glassfish/bin/startserv`
Profile: `glassfish-remote-3-1`

### TomEE 1.5 - Remote

Start: `[apache-tomee-webprofile-1.5.1]$ ./bin/tomee.sh start`
Profile: `tomee-1-5`

### Tomcat 6 - Remote

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


Start: `[apache-tomcat-6.0.33]$ ./bin/catalina.run.sh`
Profile: `tomcat-remote-6`


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