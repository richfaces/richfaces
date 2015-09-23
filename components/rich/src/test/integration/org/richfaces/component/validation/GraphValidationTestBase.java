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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.component.GraphBean;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Smoke;

public abstract class GraphValidationTestBase extends ValidationTestBase {

    @FindBy(id = "form:submit")
    private WebElement element;

    @Override
    protected void submitValue() {
        element.click();
    }

    @Test
    @Category(Smoke.class)
    public void testSubmitTooShortValue() throws Exception {
        submitValueAndCheckMessage("", containsString(GraphBean.SHORT_MSG));
        checkMessage("textMessage", containsString(GraphBean.SHORT_MSG));
        checkMessage("graphMessage", equalTo(""));
    }

    @Test
    public void testBeanLevelConstrain() throws Exception {
        submitValueAndCheckMessage("bar", equalTo(GraphBean.FOO_MSG));
        checkMessage("graphMessage", containsString(GraphBean.FOO_MSG));
        checkMessage("textMessage", equalTo(""));
    }

    @Test
    public void testCorrectValue() throws Exception {
        submitValueAndCheckMessage("foobar", equalTo(""));
    }

    protected static void addIndexPage(org.richfaces.deployment.BaseDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <rich:graphValidator id='validator' value='#{graphBean}' >");
        p.body("    <h:inputText id='text' value='#{graphBean.value}'>");
        p.body("    </h:inputText>");
        p.body("    </rich:graphValidator>");
        p.body("    <h:outputText id='out' value='#{graphBean.value}'></h:outputText>");
        p.body("    <h:commandButton id='submit' value='Submit'/>");
        p.body("</h:form>");
        p.body("<br />");
        p.body("<input id='blurButton' value='blur' type='button' />");
        p.body("<br />");
        p.body("<rich:message id='textMessage' for='text' />");
        p.body("<rich:message id='graphMessage' for='validator' />");
        p.body("<rich:messages id='uiMessage' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
