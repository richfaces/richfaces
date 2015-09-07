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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.richfaces.component.Bean;
import org.richfaces.integration.RichDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

@RunWith(Arquillian.class)
@RunAsClient
public class ITClientValidation extends ValidationTestBase {

    @Deployment(testable = false)
    public static WebArchive deployment() {
        RichDeployment deployment = new RichDeployment(ITClientValidation.class);

        deployment.archive().addClasses(Bean.class);

        addIndexPage(deployment);

        deployment.addHibernateValidatorWhenUsingServletContainer();

        return deployment.getFinalArchive();
    }

    @Test
    @Category(Smoke.class)
    public void testSubmitTooShortValue() throws Exception {
        submitValueAndCheckMessage("", not(equalTo("")));
    }

    @Test
    public void testSubmitTooLongValue() throws Exception {
        submitValueAndCheckMessage("123456", not(equalTo("")));
    }

    @Test
    public void testSubmitProperValue() throws Exception {
        submitValueAndCheckMessage("ab", equalTo(""));
    }

    private static void addIndexPage(org.richfaces.deployment.BaseDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <h:inputText id='text' value='#{test.value}'>");
        p.body("        <f:validateLength minimum='1' maximum='3' />");
        p.body("        <rich:validator event='blur' />");
        p.body("    </h:inputText>");
        p.body("    <h:outputText id='out' value='#{test.value}'></h:outputText>");
        p.body("</h:form>");
        p.body("<br />");
        p.body("<input id='blurButton' value='blur' type='button' />");
        p.body("<br />");
        p.body("<rich:message id='uiMessage' for='text' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
