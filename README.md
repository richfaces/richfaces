![RichFaces Project Logo][logo]

RichFaces 5 [![Build Status](https://travis-ci.org/richfaces/richfaces.png?branch=master)](https://travis-ci.org/richfaces/richfaces)
===========

<h3>The next-generation JSF component framework by JBoss, Red Hat</h3>

> The RichFaces project is an advanced UI component framework for easily integrating Ajax capabilities into business applications using JSF.


Goal
----

<h3>Version 5</h3>

* New approach to styling based on LESS
* New components based on third-party widgets
* Full JSF 2.2 Compatibility
* Simplified Setup for Users
* Smooth Migration from RF4 to RF5

<h3>Architecture Goals</h3>

* Simplified Framework Build
* Extensive Integration Testing
* Fast Development (CDK) & Test Turnaround

see [Roadmap](https://community.jboss.org/thread/213160) for more information.

Project Info
------------

<table>
	<tr><td>License</td><td>LGPL v2.1</td></tr>
	<tr><td>Build System</td><td>Maven</td></tr>
</table>

* [Documentation](http://docs.jboss.org/richfaces/)
* [News / RSS](http://planet.jboss.org/feed/richfacesall)
  * includes release announcements
* [Issue Tracker](https://issues.jboss.org/browse/RF)
* [Roadmap](https://community.jboss.org/thread/213160)
* [User Forums](https://community.jboss.org/en/richfaces)
* IRC
  * `#richfaces` at `irc.freenode.net`
* [Developer Forums](https://community.jboss.org/en/richfaces/dev)
* [Team Meetings](https://community.jboss.org/en/richfaces/dev/teammtgs)
* [Continuous Integration](https://travis-ci.org/richfaces/richfaces/builds)
  * [![Build Status](https://travis-ci.org/richfaces/richfaces.png?branch=master)](https://travis-ci.org/richfaces/richfaces/builds)

Getting Started
---------------

In order to start with the project, you need just include the RichFaces JARs on the classpath using Maven dependency

    <dependency>
        <groupId>org.richfaces</groupId>
        <artifactId>richfaces</artifactId>
        <type>pom</type>
    </dependency>

or download the project ZIP distribution from [Downloads Page](http://www.jboss.org/richfaces/download.html).

Once you have the RichFaces JARs on the classpath, you can start developing by adding JSF page which includes RichFaces taglib from `http://richfaces.org` namespace:

    <!DOCTYPE html>
    <html lang="en"
		xmlns="http://www.w3.org/1999/xhtml"
		...
		xmlns:r="http://richfaces.org">

	</html>

For more information on how to get started with the project, see [Project Documentation](http://docs.jboss.org/richfaces/).

Cloning the Project
-------------------

    $ git clone git@github.com:richfaces/richfaces.git

Building the Project
--------------------

Prerequsities:

* JDK 1.6 +
* Maven 3.0.4 +
* [JBoss Maven Repository](https://community.jboss.org/wiki/MavenGettingStarted-Developers)

Building the project:

    $ mvn clean install

Maven will build the project and execute unit tests, but it won't build distribution or execute integration tests.

<h3>Building Project Distribution</h3>

    $ mvn clean install -Prelease

the ZIP distribution will be stored in `dist/target/`.

[Contributor Getting Started](https://github.com/richfaces/richfaces5/blob/master/CONTRIBUTING.md)
-----------------------------

Read [How to Contribute](https://github.com/richfaces/richfaces5/blob/master/CONTRIBUTING.md).

Developing the Project
----------------------

<h3>How to Explore the Sources</h3>

All the sources related to RichFaces framework and component library are located in `framework` folder:

* `src/main/java`
  * framework sources
  * JSF components under `org.richfaces.ui` package
* `src/main/resources/META-INF/resources`
  * JavaScript and CSS resources

Examples such as *RichFaces Showcase* and *Components Demo* are stored in `examples` folder.

<h3>Developing the Project in IDE</h3>

Once you have [built the project](#building-the-project) you can import `framework` and one of `examples` to the IDE.

If you are using Eclipse, you should use `Import Existing Maven Project` option.

<h3>How to Test the Project</h3>

RichFaces has extensive framework test suite which validates compatibility with various browsers and application servers.

For more information on how to run and develop this test suite, see [Framework Tests](https://github.com/richfaces/richfaces5/blob/master/TESTS.md) document.

<h3>Running Smoke Tests</h3>

In order to effectively test the project with each code modification, you need to run at least framework's smoke tests:

    $ mvn clean install -Dintegration=wildfly80 -Dsmoke

This will run the project tests against managed instance of WildFly 8.0 and test it on headless PhantomJS browser.

This step is also executed in [the continuous integration job](https://travis-ci.org/richfaces/richfaces/builds).

[logo]: https://raw.github.com/richfaces/docs/4.3.1.20130305-Final/Component_Reference/src/main/docbook/en-US/images/rf_logo.png "RichFaces Project Logo"
