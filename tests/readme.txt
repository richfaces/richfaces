How to run Selenium tests

Requirements
– Maven 2.0.9 or later
– Firefox 
– JBoss Application Server (4.2.3.GA, 5.0.x.GA)


The Photo Album application is designed to be deployed and run on JBoss Application server,
so please make sure that the <jboss.installer.url> property of the project pom.xml (examples/photoalbum/) 
file contains the correct link to your copy of the sever on your local machine.

Now you need to build the Photo Album project with mvn clean install command.
You need to run the 

mvn clean install 

command in source folder of the project (examples/photoalbum/source/). 
When the project is built it will be also placed in your local repository. 
By default it is located in (home/user/.m2 for Linux and c:\Documents and Settings\msorokin\.m2\repository\ for Windows XP). 
You can also define your custom location of the repository in %M2_HOME%/conf/settings.xml. 
Then, you need to go to the test folder of the project (examples/photoalbum/test/) and run the

mvn clean integration-test

in there.  By default Selenium tests are executed in the Firefox browser, hence you need to have it installed. 
If you configured everything like it is said above you will see tests being executed in the Firefox browser.  
When the tests are finished you can read  test reports  in the examples/photoalbum/tests/target/surefire-reports / folder.

