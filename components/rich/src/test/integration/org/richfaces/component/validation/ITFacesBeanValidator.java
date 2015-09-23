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
package org.richfaces.component.validation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.runner.RunWith;
import org.richfaces.component.GraphBean;
import org.richfaces.component.Group;
import org.richfaces.integration.RichDeployment;

import com.google.common.base.Function;

@RunWith(Arquillian.class)
@RunAsClient
public class ITFacesBeanValidator extends GraphValidationTestBase {

    @Deployment(testable = false)
    public static WebArchive deployment() {
        RichDeployment deployment = new RichDeployment(ITFacesBeanValidator.class);

        deployment.archive().addClasses(GraphBean.class, Group.class);

        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor webXml) {
                webXml.createContextParam()
                    .paramName("javax.faces.validator.DISABLE_DEFAULT_BEAN_VALIDATOR")
                    .paramValue("true");
                return webXml;
            }
        });

        ITGraphValidation.addIndexPage(deployment);

        deployment.addHibernateValidatorWhenUsingServletContainer();

        return deployment.getFinalArchive();
    }
}
