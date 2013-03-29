package org.richfaces.integration;

import java.io.File;

import org.richfaces.deployment.FrameworkDeployment;

public class IterationDeployment extends FrameworkDeployment {

    public IterationDeployment(Class<?> testClass) {
        super(testClass);

        archive().addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }
}
