/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.placeholder;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;
import org.richfaces.integration.MiscDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPlaceholderInplaceSelect extends AbstractPlaceholderTest {

    @FindBy(css = INPUT_SELECTOR)
    private InplaceSelectInput firstInplaceSelect;

    @Deployment
    public static WebArchive createDeployment() {
        MiscDeployment deployment = new MiscDeployment(TestPlaceholderInputText.class);

        deployment.archive().addClasses(PlaceHolderValueConverter.class, PlaceHolderValue.class);

        FaceletAsset p;
        p = deployment.baseFacelet("index.xhtml");
        p.body("<input:inplaceSelect id='input'>");
        p.body("    <f:selectItems value='#{placeHolderValue.items}' />");
        p.body("    <misc:placeholder id='placeholderID' styleClass='#{param.styleClass}' value='Placeholder Text' />");
        p.body("</input:inplaceSelect>");

        p = deployment.baseFacelet("selector.xhtml");
        p.body("<input:inplaceSelect id='input' />");
        p.body("<misc:placeholder id='placeholderID' value='Placeholder Text' selector='[id=input]' />");

        p = deployment.baseFacelet("rendered.xhtml");
        p.body("<input:inplaceSelect id='input' defaultLabel='#{not empty param.defaultLabel ? param.defaultLabel : null}' >");
        p.body("    <misc:placeholder id='placeholderID' value='Placeholder Text' rendered='false' />");
        p.body("</input:inplaceSelect>");

        p = deployment.baseFacelet("converter.xhtml");
        p.body("<input:inplaceSelect id='input' >");
        p.body("    <misc:placeholder id='placeholderID' converter='placeHolderValueConverter' value='#{placeHolderValue}' />");
        p.body("</input:inplaceSelect>");

        p = deployment.baseFacelet("submit.xhtml");
        p.form("<input:inplaceSelect id='input' value='#{placeHolderValue.value2}' >");
        p.form("    <f:selectItems value='#{placeHolderValue.items}' />");
        p.form("    <misc:placeholder id='placeholderID' value='Placeholder Text' />");
        p.form("</input:inplaceSelect>");
        p.form("<br />");
        p.form("<a4j:commandButton id='ajaxSubmit' value='ajax submit' execute='@form' render='output' />");
        p.form("<h:commandButton id='httpSubmit' value='http submit' />");
        p.form("<br />");
        p.form("<h:outputText id='output' value='#{placeHolderValue.value2}' />");

        return deployment.getFinalArchive();
    }

    @Override
    Input input() {
        return firstInplaceSelect;
    }

    @Override
    protected String getTestedValue() {
        return "item1";
    }

    @Override
    protected String getTestedValueResponse() {
        return "item1";
    }

    @Test
    @Override
    public void testAjaxSendsEmptyValue() {
        // given
        browser.get(contextPath.toExternalForm() + "submit.jsf");

        //when
        Graphene.guardXhr(a4jSubmitBtn).click();

        // then
        Graphene.waitAjax().until(Graphene.element(output).not().isVisible());
    }

    @Test
    public void testRendered() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "rendered.jsf");
        // then
        assertEquals("", input().getDefaultText().trim());
    }

    @Test
    public void when_placeholder_is_not_rendered_and_defaultLabel_is_defined_then_is_should_be_used() {
        // having
        browser.navigate().to(contextPath.toExternalForm() + "rendered.jsf?defaultLabel=defaultLabel");
        // then
        assertEquals("defaultLabel", input().getDefaultText());
    }

    @Ignore("RF-12651")
    @Test
    @Override
    public void testDefaultAttributes() {
    }

    @Ignore("RF-12651")
    @Test
    public void testSelector() {
    }

    @Ignore("RF-12651")
    @Test
    public void testStyleClass() {
    }

    @Ignore("RF-12651")
    @Test
    public void when_text_is_changed_then_text_changes_color_to_default_and_removes_placeholder_style_classes() {
    }

    @Ignore("RF-12651")
    @Test
    @Override
    public void when_text_is_cleared_then_input_gets_placeholder_text_and_style_again() {
    }
}
