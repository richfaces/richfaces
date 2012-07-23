/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.deployment;

import java.util.Collection;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.shrinkwrap.resolver.api.maven.MavenImporter;

/**
 * Provides base for Core test deployment
 *
 * @author Lukas Fryc
 */
public class CoreDeployment extends Deployment {

    public CoreDeployment(Class<?> testClass) {
        super(testClass);

        Collection<GenericArchive> coreDependencies = DependencyResolvers.use(MavenDependencyResolver.class)
                .loadEffectivePom("pom.xml")
                .artifacts("org.richfaces.core:richfaces-core-api", "com.google.guava:guava", "net.sourceforge.cssparser:cssparser:0.9.5", "org.w3c.css:sac:1.3")
                .resolveAs(GenericArchive.class);

        JavaArchive coreLibrary = ShrinkWrap
                .create(MavenImporter.class, "richfaces-core-impl.jar")
                .loadEffectivePom("pom.xml")
                .importBuildOutput().as(JavaArchive.class);

        archive().addAsLibraries(coreDependencies);
        archive().addAsLibrary(coreLibrary);
    }
}