RichFaces Framework
===================

TBD


Integration Tests
-----------------

Executing single test for debugging purposes
--------------------------------------------

    mvn verify -Pintegration-tests -Pjbossas-remote-7-1 -DskipTests=true -Dcheckstyle.skip=true -Dgeneration.skip=true -Darquillian.debug=true -Dreusable -Dbrowser=chrome -Dtest=IT_RF12765


