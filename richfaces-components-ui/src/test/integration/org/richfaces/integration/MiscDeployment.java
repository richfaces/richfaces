package org.richfaces.integration;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.richfaces.arquillian.configuration.FundamentalTestConfiguration;
import org.richfaces.arquillian.configuration.FundamentalTestConfigurationContext;
import org.richfaces.deployment.Deployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

public class MiscDeployment extends Deployment {

    private final FundamentalTestConfiguration configuration = FundamentalTestConfigurationContext.getProxy();

    public MiscDeployment(Class<?> testClass) {
        super(testClass);

        if (configuration.isCurrentRichFacesVersion()) {

            addCurrentProjectClasses();

            this.addMavenDependency(
                    "org.richfaces.core:richfaces-core-api",
                    "org.richfaces.core:richfaces-core-impl",
                    "org.richfaces.ui.common:richfaces-ui-common-api",
                    "org.richfaces.ui.common:richfaces-ui-common-ui",
                    "org.richfaces.ui.core:richfaces-ui-core-api",
                    "org.richfaces.ui.core:richfaces-ui-core-ui",
                    "org.richfaces.ui.input:richfaces-ui-input-api",
                    "org.richfaces.ui.input:richfaces-ui-input-ui");

        } else {

            String version = configuration.getRichFacesVersion();
            this.addMavenDependency(
                    "org.richfaces.core:richfaces-core-api:" + version,
                    "org.richfaces.core:richfaces-core-impl:" + version,
                    "org.richfaces.ui.common:richfaces-ui-common-api:" + version,
                    "org.richfaces.ui.common:richfaces-ui-common-ui:" + version,
                    "org.richfaces.ui.core:richfaces-ui-core-api:" + version,
                    "org.richfaces.ui.core:richfaces-ui-core-ui:" + version,
                    "org.richfaces.ui.input:richfaces-ui-input-api:" + version,
                    "org.richfaces.ui.input:richfaces-ui-input-ui:" + version,
                    "org.richfaces.ui.misc:richfaces-ui-misc-ui:" + version);
        }
    }

    private void addCurrentProjectClasses() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "richfaces-ui-misc-ui.jar");
        jar.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory("target/classes/").as(GenericArchive.class),
                "/", Filters.includeAll());
        archive().addAsLibrary(jar);
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
