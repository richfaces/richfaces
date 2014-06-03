# The Photo Album application: short description

## Building and Running the application

### Requirements

 * Maven 3.0.3 or later
 * JBoss AS 7

### Optional Additional Software

 * Eclipse IDE + JBoss Tools (to explore and run the application in IDE)

### Building the application

To build the project you need to navigate to the root folder and run

	mvn clean install

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. 

You can deploy the application on the server by copying the _target/richfaces-photoalbum.war_ file to the _JBOSS\_HOME/standalone/deployments_ folder. Then launch the run.bat or run.sh file from _JBOSS\_HOME/bin/_ directory to start the server.

### Testing

The Photoalbum comes with a small set of tests, to run them use

    mvn test -Parquillian-jbossas-remote
   
Or select the _arquillian-jbossas-remote_ profile from the IDE (Right click the project -> _Maven_ -> _Select Maven Profiles_) and then run the tests: right click _src/test/java_ -> _Run As_ -> _JUnit Test_
    
In order for the tests to execute you need to have a server running.

### Predefined users

To use the features available to registered users either create your own account or use one of the predefined ones:

 *	 amarkhel
 *	 Viking
 *	 Noname
 *	 user\_for\_add
 *	 user\_for\_del
 *	 user\_for\_dnd

the password is _12345_ in all cases.

### Social integration

The Photoalbum allows you to connect to your Facebook and Google+ accounts and browse and share your photos. 
Due to limitations put on the apps you need to be running the application on the default `localhost:8080` in order to log in successfully.

## Known issues
### Database error during deployment
There's a number of errors being thrown during deployment

    10:53:27,633 ERROR [org.hibernate.tool.hbm2ddl.SchemaExport] (ServerService Thread Pool -- 48) 
        HHH000389: Unsuccessful: alter table Album drop constraint FK3C68E4FB7F856D
    10:53:27,634 ERROR [org.hibernate.tool.hbm2ddl.SchemaExport] (ServerService Thread Pool -- 48) 
        Table "ALBUM" not found; SQL statement: alter table Album drop constraint FK3C68E4FB7F856D [42102-168]
        …
        
These errors are not serious, they are caused by the database trying to tear down tables that do not yet exist.  