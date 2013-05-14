/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************
 */
package org.richfaces.tests.showcase.panel;

import static org.jboss.arquillian.ajocado.dom.Attribute.STYLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITLookCustomization extends AbstractPanelTest {

    @ArquillianResource
    private Actions actions;
    protected final String outputPanelChangingStyleSyn1 = "fieldset td div.rf-p:eq(0)";
    protected final String outputPanelChangingStyleSyn2 = "fieldset td div.rf-p:eq(1)";
    protected final String outputPanelJavaScript = "fieldset td div.rf-p:eq(2)";
    protected final String outputPanelScrolling = "fieldset td div.rf-p:eq(3)";
    protected final String outputPanelWithoutHeader = "fieldset td div.rf-p:eq(4)";
    // the order of panels is from top left to bottom right corner of sample page with panels
    protected final String PANEL1_HEADER = "Panel #1. Changing Style Synchronously";
    protected final String PANEL1_BODY = "Each component in RichFaces has a pre-defined set "
        + "of CSS classes you can manipulate. If defined, those classes overwrite the " + "ones that come from the skin.";
    protected final String PANEL2_HEADER = "Panel #2. Changing Style Synchronously";
    protected final String PANEL2_BODY = "In this example, we define header color using "
        + "the .rf-panel-header class and all panels located on the same page inherit " + "this color";
    protected final String PANEL3_HEADER = "Panel header";
    protected final String PANEL3_BODY = "Base on the previous layout, but with javascript visual effects added.";
    protected final String PANEL4_HEADER = "Scrolling Text Panel";
    protected final String PANEL4_BODY = "Long Text Long Text Long Text Long Text Long "
        + "Text Long Text Long Text Long Text Long Text Long Text Long Text Long "
        + "Text Long Text Long Text Long Text Long Text Long Text Long Text "
        + "Long Text Long Text Long Text Long Text Long Text Long Text "
        + "Long Text Long Text Long Text Long Text Long Text Long Text "
        + "Long Text Long Text Long Text Long Text Long Text Long Text Long Text Long " + "Text Long Text";
    protected final String PANEL5_BODY = "This is a panel without the header";

    @FindBy(jquery = ".panel:eq(1) > div")
    private WebElement elementWithOnMouseOutHandler;

    /* **********************************************************************************
     * Tests**********************************************************************************
     */
    @Test
    public void testPanelsAreNotEmpty() {
        checkContentOfPanel(outputPanelChangingStyleSyn1 + " > " + HEADER, PANEL1_HEADER);
        checkContentOfPanel(outputPanelChangingStyleSyn1 + " > " + BODY, PANEL1_BODY);

        checkContentOfPanel(outputPanelChangingStyleSyn2 + " > " + HEADER, PANEL2_HEADER);
        checkContentOfPanel(outputPanelChangingStyleSyn2 + " > " + BODY, PANEL2_BODY);

        checkContentOfPanel(outputPanelJavaScript + " > " + HEADER, PANEL3_HEADER);
        checkContentOfPanel(outputPanelJavaScript + " > " + BODY, PANEL3_BODY);

        checkContentOfPanel(outputPanelScrolling + " > " + HEADER, PANEL4_HEADER);
        checkContentOfPanel(outputPanelScrolling + " > " + BODY, PANEL4_BODY);

        assertFalse("There should not be the header!",
            Graphene.element(ByJQuery.jquerySelector(outputPanelWithoutHeader + HEADER)).isPresent().apply(webDriver)); // Won't
                                                                                                                        // work
                                                                                                                        // with
                                                                                                                        // Graphene.wait*()...().isPresent();
        checkContentOfPanel(outputPanelWithoutHeader, PANEL5_BODY);
    }

    @Test
    public void testPanelWithJavaScript() {
        // WebElement outputPanelWithJS = webDriver.findElement(ByJQuery.jquerySelector(outputPanelJavaScript));
        mouseOver();
        mouseOut();

        String styleOfHeaderBefore = getAttribute(outputPanelJavaScript + " > " + HEADER);
        String styleOfBodyBefore = getAttribute(outputPanelJavaScript + " > " + BODY);

        mouseOver();

        String styleOfHeaderAfter = getAttribute(outputPanelJavaScript + " > " + HEADER);
        String styleOfBodyAfter = getAttribute(outputPanelJavaScript + " > " + BODY);

        assertFalse("The style of header should be different" + " after mouseover, the the rgb should be different",
            styleOfHeaderBefore.equals(styleOfHeaderAfter));
        assertFalse("The style of body should be different" + " after mouseover, the the rgb should be different",
            styleOfBodyBefore.equals(styleOfBodyAfter));

        mouseOut();
        styleOfHeaderAfter = getAttribute(outputPanelJavaScript + " > " + HEADER);
        styleOfBodyAfter = getAttribute(outputPanelJavaScript + " > " + BODY);

        assertEquals("The style of header should be returned to the " + "value on the mouseout state", styleOfHeaderAfter,
            styleOfHeaderBefore);
        assertEquals("The style of body should be returned to the " + "value on the mouseout state", styleOfBodyAfter,
            styleOfBodyBefore);
    }

    private String getAttribute(String locator) {
        return webDriver.findElement(ByJQuery.jquerySelector(locator)).getAttribute(STYLE.getAttributeName());
    }

    private void mouseOver() {
        // need to do mouseover in this way, using Actions works, however only till there is not mouseout triggered in this same manner
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("$('.panel:eq(1) > div').mouseover();");
    }

    private void mouseOut() {
        // need to do mouseout in this way, using Actions does not work on Firefox, due to its lack of support for native events
        // by default
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("$('.panel:eq(1) > div').mouseout();");
    }
}