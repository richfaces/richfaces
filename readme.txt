Work in progress.

I'm using this branch to port the master branch to Java EE 6. The master branch can be built with maven but probably won't run under current systems.

Sep 6, 2012
Tests written for the org.richfaces.photoalbum.service package run successfuly. To run them under maven use '-Parquillian-jbossas-managed' parameter. Bear in mind that the build will still fail once it reaches the WAR module. 

Oct 2, 2012
Almost all Richfaces components have been updated.
Merged classes and resources into the WAR module (source/web), other packages should no longer be needed (unless I have overlooked something).
----
The project (source/web) can be successfully built and deployed, but so far it doesn't do anything else than look pretty and show errors.

======

Dec 10, 2012
Apart from several cosmetic features and things I might've overlooked the application is in working order.

Before you deploy:
A datasource needs to be added to your JBoss AS configuration.
In your JBoss home go to runtimes/jboss-eap/standalone/configuration/ and open standalone.xml (or one of the other files depending on how your server is run)
In the standalone.xml file find <datatasources> and add new datasouce:
                <datasource jndi-name="java:jboss/datasources/photoalbumDatasource" pool-name="photoalbum" enabled="true" use-java-context="true">
                    <connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1</connection-url>
                    <driver>h2</driver>
                    <security>
                        <user-name>sa</user-name>
                        <password>sa</password>
                    </security>
                </datasource>
                
Now you should be able to deploy the application.

If you want to try features restricted to registered users you can log in as "amarkhel" or "Viking", the password is 12345 for both. Thare are predefined photo collections for those two.
