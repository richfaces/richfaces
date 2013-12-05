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
public class ITPlaceholderSelect extends AbstractPlaceholderTest {

    @FindBy(css = INPUT_SELECTOR + " input[id$=Input]")
    private SelectInput firstInput;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ITPlaceholderTextarea.class);

        deployment.archive().addClasses(PlaceHolderValueConverter.class, PlaceHolderValue.class);

        FaceletAsset p;
        p = deployment.baseFacelet("index.xhtml");
        p.body("<r:select id='input' enableManualInput='true'>");
        p.form("    <f:selectItems value='#{placeHolderValue.items}' />");
        p.body("    <r:placeholder id='placeholderID' styleClass='#{param.styleClass}' value='Placeholder Text' />");
        p.body("</r:select>");

        p = deployment.baseFacelet("selector.xhtml");
        p.body("<r:select id='input' />");
        p.body("<r:placeholder id='placeholderID' value='Placeholder Text' selector='[id=input]' />");

        p = deployment.baseFacelet("rendered.xhtml");
        p.body("<r:select id='input'>");
        p.body("    <r:placeholder id='placeholderID' value='Placeholder Text' rendered='false' />");
        p.body("</r:select>");

        p = deployment.baseFacelet("converter.xhtml");
        p.body("<r:select id='input' >");
        p.body("    <r:placeholder id='placeholderID' converter='placeHolderValueConverter' value='#{placeHolderValue}' />");
        p.body("</r:select>");

        p = deployment.baseFacelet("submit.xhtml");
        p.form("<r:select id='input' value='#{placeHolderValue.value2}' enableManualInput='true' >");
        p.form("    <f:selectItems value='#{placeHolderValue.items}' />");
        p.form("    <r:placeholder id='placeholderID' value='Placeholder Text' />");
        p.form("</r:select>");
        p.form("<br />");
        p.form("<r:commandButton id='ajaxSubmit' value='ajax submit' execute='@form' render='output' />");
        p.form("<h:commandButton id='httpSubmit' value='http submit' />");
        p.form("<br />");
        p.form("<h:outputText id='output' value='#{placeHolderValue.value2}' />");

        return deployment.getFinalArchive();
    }
    @Override
    Input input() {
        return firstInput;
    }

    @Override
    protected String getTestedValue() {
        return "item1";
    }

    @Override
    protected String getTestedValueResponse() {
        return "item1";
    }

    /**
     * The select component does behave differently - delegates to defaultLabel implementation
     */
    @Test
    @Override
    @Category(Failing.class)
    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
    }

    /**
     * The select component can't send invalid value by AJAX
     */
    @Test
    @Override
    @Category(Failing.class)
    public void testAjaxSendsEmptyValue() {
    }
}
