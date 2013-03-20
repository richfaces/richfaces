package org.richfaces.integration;

import java.io.File;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class IterationDeployment extends Deployment {

    public IterationDeployment(Class<?> testClass) {
        super(testClass);

        JavaArchive iteration = ShrinkWrap.create(JavaArchive.class, "richfaces-framework.jar");
        iteration.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("target/classes/").as(GenericArchive.class),
                "/", Filters.includeAll());
        archive().addAsLibrary(iteration);
        archive().addAsWebInfResource(new File("src/test/resources/beans.xml"));

    }
}
