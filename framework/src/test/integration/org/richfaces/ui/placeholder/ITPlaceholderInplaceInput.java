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
package org.richfaces.ui.placeholder;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.support.FindBy;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import category.Failing;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ITPlaceholderInplaceInput extends AbstractPlaceholderTest {

    @FindBy(id = INPUT_ID)
    private InplaceInput inplaceInput;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITPlaceholderInputText.class);

        deployment.archive().addClasses(PlaceHolderValueConverter.class, PlaceHolderValue.class);

        FaceletAsset p;
        p = deployment.baseFacelet("index.xhtml");
        p.body("<r:inplaceInput id='input' >");
        p.body("    <r:placeholder id='placeholderID' styleClass='#{param.styleClass}' value='Placeholder Text' />");
        p.body("</r:inplaceInput>");

        p = deployment.baseFacelet("selector.xhtml");
        p.body("<r:inplaceInput id='input' />");
        p.body("<h:inputText id='second-input' />");

        p = deployment.baseFacelet("rendered.xhtml");
        p.body("<r:inplaceInput id='input' defaultLabel='#{not empty param.defaultLabel ? param.defaultLabel : null}' >");
        p.body("    <r:placeholder id='placeholderID' value='Placeholder Text' rendered='false' />");
        p.body("</r:inplaceInput>");

        p = deployment.baseFacelet("converter.xhtml");
        p.body("<r:inplaceInput id='input' >");
        p.body("    <r:placeholder id='placeholderID' converter='placeHolderValueConverter' value='#{placeHolderValue}' />");
        p.body("</r:inplaceInput>");

        p = deployment.baseFacelet("submit.xhtml");
        p.form("<r:inplaceInput id='input' value='#{placeHolderValue.value2}' >");
        p.form("    <r:placeholder id='placeholderID' value='Placeholder Text' />");
        p.form("</r:inplaceInput>");
        p.form("<br />");
        p.form("<r:commandButton id='ajaxSubmit' value='ajax submit' execute='@form' render='output' />");
        p.form("<h:commandButton id='httpSubmit' value='http submit' />");
        p.form("<br />");
        p.form("<h:outputText id='output' value='#{placeHolderValue.value2}' />");

        return deployment.getFinalArchive();
    }

    @Override
    Input input() {
        return inplaceInput;
    }

    @Test
    public void testRendered() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "rendered.jsf");
        // then
        assertEquals("", input().getDefaultText().trim());
    }

    @Test
    public void when_placeholder_is_not_rendered_anddefaultLabel_is_defined_then_is_should_be_used() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "rendered.jsf?defaultLabel=defaultLabel");
        // then
        assertEquals("defaultLabel", input().getDefaultText());
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-12651}
     */
    @Test
    @Override
    @Category(Failing.class)
    public void testDefaultAttributes() {
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-12651}
     */
    @Test
    @Category(Failing.class)
    public void testSelector() {
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-12651}
     */
    @Test
    @Category(Failing.class)
    public void testStyleClass() {
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-12651}
     */
    @Test
    @Category(Failing.class)
    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
    }

    /**
     * {@link https://issues.jboss.org/browse/RF-12651}
     */
    @Test
    @Override
    @Category(Failing.class)
    public void when_text_is_cleared_then_input_gets_placeholder_text_and_style_again() {
    }
}
