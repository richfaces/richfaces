# The Photo Album application: short description

## Building and Running the application

### Requirements

 * Maven 3.0.3 or later
 * Wildfly 8.1  or later

### Optional Additional Software

 * Eclipse IDE + JBoss Tools (to explore and run the application in IDE)

### Building the application

To build the project you need to navigate to the root folder and run

	mvn clean install

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. 

You can deploy the application on the server by copying the _target/richfaces-photoalbum.war_ file to the _JBOSS\_HOME/standalone/deployments_ folder. Then launch the run.bat or run.sh file from _JBOSS\_HOME/bin/_ directory to start the server.

#### Running on WildFly

In order to run the demo on WildFly the server has to be started with `-Dorg.jboss.weld.nonPortableMode=true`. Alternatively, put this in the standalone.xml:

    <system-properties>
        <property name="org.jboss.weld.nonPortableMode" value="true"/>
    </system-properties>
    
     

### Integration Testing

The Photoalbum comes with a small set of integration tests, to run them use

    mvn clean verify -Dintegration=<nameOfServer> -Dtest=<nameOfTest>
   
Tests are runnable on wildfly81, wildfly81-remote, wildfly82, wildfly82-remote, jbosseap63 and jbosseap63-remote.
To choose a server, write its name into -Dintegration. To run tests on remote server, you need start the server before running tests.
In order to execute single test, use -Dtest=TestSomething, to run all the tests, simply omit this parameter.

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
As for Facebook integration, we are using Graph API(version > 2) and there is a test user profile with following credentials:
E-mail address -> vocfryc_wongwitz_1429527192@tfbnw.net 
Password -> 12345

n order to successfully run integration tests which include Google+ login, you need to provide your own credentials. This can be
achieved by adding following parameters when executing maven: -DgooglePlus.username=yourName -DgooglePlus.password=yourPswd
If not provided, these tests will end up with IllegalArgumentException.

## Known issues
### Database error during deployment
There's a number of errors being thrown during deployment

    10:53:27,633 ERROR [org.hibernate.tool.hbm2ddl.SchemaExport] (ServerService Thread Pool -- 48) 
        HHH000389: Unsuccessful: alter table Album drop constraint FK3C68E4FB7F856D
    10:53:27,634 ERROR [org.hibernate.tool.hbm2ddl.SchemaExport] (ServerService Thread Pool -- 48) 
        Table "ALBUM" not found; SQL statement: alter table Album drop constraint FK3C68E4FB7F856D [42102-168]
        …
        
These errors are not serious, they are caused by the database trying to tear down tables that do not yet exist.