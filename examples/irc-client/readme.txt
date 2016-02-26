=======================================================================

                      RichFaces - Ajax enabled JSF 2.0 component library

                              RichFaces 4.5.14.Final irc-client sample
					      	    http://richfaces.org
                                                              March 2011
                     This software is distributed under the terms of the 
                            FSF Lesser Gnu Public License (see lgpl.txt)

========================================================================

IRC Client Application is a sample which shows RichFaces Push in action.

Targetted to JBoss AS 6
 * Run mvn install

 * Place war file into "JBoss AS 6/server/default/deploy" folder

 * Run JBoss AS

JMS Configuration should be done prior to running application. 

 * Start Admin console going to http://localhost:8080/admin-console

 * Setup new JMS Topic with the folowing properties:
   Name: chat
   JNDI name: /topic/chat
   All the others: by default.

 * Add role to that topic
   Name: guest
   Send: yes
   Consume: yes
   create subscriber: yes
   delete subscriber: yes
   create durable subscriber: yes
   delete durable subscriber: yes

Running application
 * Enjoy playing with the client at http://localhost:8080/irc-client 
