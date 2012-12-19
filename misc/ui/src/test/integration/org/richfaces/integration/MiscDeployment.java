package org.richfaces.integration;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.richfaces.deployment.Deployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

public class MiscDeployment extends Deployment {

    public MiscDeployment(Class<?> testClass) {
        super(testClass);

        this.addMavenDependency(
                "org.richfaces.core:richfaces-core-api",
                "org.richfaces.core:richfaces-core-impl",
                "org.richfaces.ui.common:richfaces-ui-common-api",
                "org.richfaces.ui.common:richfaces-ui-common-ui",
                "org.richfaces.ui.input:richfaces-ui-input-api",
                "org.richfaces.ui.input:richfaces-ui-input-ui",
                "org.richfaces.ui.core:richfaces-ui-core-api",
                "org.richfaces.ui.core:richfaces-ui-core-ui");

        JavaArchive miscArchive = ShrinkWrap.create(JavaArchive.class, "richfaces-ui-misc-ui.jar");
        miscArchive.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("target/classes/").as(GenericArchive.class),
                "/", Filters.includeAll());
        archive().addAsLibrary(miscArchive);
    }



    public FaceletAsset baseFacelet(String name) {
        FaceletAsset p = new FaceletAsset();

        p.xmlns("misc", "http://richfaces.org/misc");
        p.xmlns("a4j", "http://richfaces.org/a4j");
        p.xmlns("input", "http://richfaces.org/input");

        this.archive().add(p, name);

        return p;
    }
}
