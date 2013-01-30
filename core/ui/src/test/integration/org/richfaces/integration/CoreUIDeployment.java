package org.richfaces.integration;

import com.google.common.collect.Sets;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.richfaces.deployment.Deployment;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

public class CoreUIDeployment extends Deployment {

    public CoreUIDeployment(Class<?> testClass) {
        super(testClass);

        this.addMavenDependency(
                "org.richfaces.core:richfaces-core-api",
                "org.richfaces.core:richfaces-core-impl",
                "org.richfaces.ui.common:richfaces-ui-common-api",
                "org.richfaces.ui.common:richfaces-ui-common-ui",
                "org.richfaces.ui.core:richfaces-ui-core-api");

        JavaArchive miscArchive = ShrinkWrap.create(JavaArchive.class, "richfaces-ui-core-ui.jar");
        miscArchive.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
            .importDirectory("target/classes/").as(GenericArchive.class),
            "/", Filters.includeAll());
        archive().addAsLibrary(miscArchive);
    }

    public CoreUIDeployment(Class<?> testClass, String version) {
        super(testClass);

        Set<File> jarFiles = Sets.newHashSet();
        String[] deps = {
                "org.richfaces.core:richfaces-core-api:" + version,
                "org.richfaces.core:richfaces-core-impl:" + version,
                "org.richfaces.ui:richfaces-components-api:" + version,
                "org.richfaces.ui:richfaces-components-ui:" + version};
        for (String dep : deps) {
            jarFiles.addAll(Arrays.asList(Maven.resolver().resolve(dep)
                    .withClassPathResolution(false).withTransitivity().asFile()));
        }
        archive().addAsLibraries(jarFiles.toArray(new File[jarFiles.size()]));
    }

}
