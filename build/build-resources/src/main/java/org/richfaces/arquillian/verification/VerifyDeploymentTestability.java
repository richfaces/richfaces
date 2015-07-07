/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.richfaces.arquillian.verification;

import java.lang.reflect.Method;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.warp.WarpTest;

/**
 * <p>
 * We use two kinds of tests in RichFaces test suite:
 * </p>
 *
 * <ul>
 * <li>Warp tests (@Warp @RunAsClient @Deployment(testable = true))
 * <li>client-side only, such as Graphene tests (@RunAsClient @Deployment(testable = false))
 * </ul>
 *
 *
 * @author Lukas Fryc
 */
public class VerifyDeploymentTestability {

    /**
     * Checks if WarpTest.class is on classpath
     */
    private boolean isWarpDependencyInProject() {
        try {
            WarpTest.class.getName();
            return true;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    /**
     * Verifies that all non-Warp, client-side only tests does not have @Deployment that is marked testable.
     *
     * @param event
     */
    public void verifyThatRunAsClientClassIsNotTestable(@Observes BeforeClass event) {

        TestClass testClass = event.getTestClass();

        // run-as-client class
        if (testClass.getAnnotation(RunAsClient.class) != null) {

            // project does not contain Warp dependency or it is a non-warp class
            if (!isWarpDependencyInProject() || testClass.getAnnotation(WarpTest.class) == null) {
                Method method = testClass.getMethod(Deployment.class);
                Deployment deployment = method.getAnnotation(Deployment.class);

                // deployment is testable
                if (deployment.testable()) {
                    throw new IllegalArgumentException("Non-Warp test that is marked as @RunAsClient should not be testable: "
                        + testClass.getJavaClass().getName());
                }
            }
        }
    }
}
