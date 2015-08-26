package org.richfaces.integration;

import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.richfaces.arquillian.configuration.FundamentalTestConfiguration;
import org.richfaces.arquillian.configuration.FundamentalTestConfigurationContext;
import org.richfaces.deployment.BaseDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

public class RichDeployment extends BaseDeployment {

    private final FundamentalTestConfiguration configuration = FundamentalTestConfigurationContext.getProxy();

    public RichDeployment(Class<?> testClass) {
        this(testClass == null ? null : testClass.getSimpleName());
    }

    public RichDeployment(String archiveName) {
        super(archiveName);

        if (configuration.isCurrentRichFacesVersion()) {

            addCurrentProjectClasses();

            this.addMavenDependency(
                "org.richfaces:richfaces-core",
                "org.richfaces:richfaces-a4j");

        } else {
            String version = configuration.getRichFacesVersion();
            this.addMavenDependency(
                "org.richfaces:richfaces-core:" + version,
                "org.richfaces:richfaces-a4j:" + version,
                "org.richfaces:richfaces:" + version);
        }

    }

    private void addCurrentProjectClasses() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "richfaces.jar");
        jar.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
            .importDirectory("target/classes/").as(GenericArchive.class),
            "/", Filters.includeAll());
        archive().addAsLibrary(jar);
    }

    public FaceletAsset baseFacelet(String name) {
        FaceletAsset p = new FaceletAsset();

        this.archive().add(p, name);

        return p;
    }
}
