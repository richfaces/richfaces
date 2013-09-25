/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;
import org.richfaces.component.AbstractPanel;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Andrey Markhel
 *
 */
public class PanelRendererTest {
    private HtmlUnitEnvironment environment;

    @Before
    public void setUp() {
        environment = new CustomizedHtmlUnitEnvironment();
        environment.withWebRoot(new File("src/test/resources"));
        environment.start();
    }

    @After
    public void tearDown() {
        environment.release();
        environment = null;
    }

    private FacesRequest startFacesRequest() throws IOException {
        FacesRequest facesRequest = environment.createFacesRequest("http://localhost/panelTest.jsf");
        facesRequest.withViewId("/panelTest.jsf");
        facesRequest.start();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ViewHandler vh = facesContext.getApplication().getViewHandler();
        ViewDeclarationLanguage vdl = vh.getViewDeclarationLanguage(facesContext, facesContext.getViewRoot().getViewId());
        vdl.buildView(facesContext, facesContext.getViewRoot());
        return facesRequest;
    }

    /**
     * Test method for {@link org.richfaces.renderkit.ExtendedDataTableRenderer#getComponentClass()}.
     *
     * @throws IOException
     */
    @Test
    public final void testGetComponentClass() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractPanel component = (AbstractPanel) facesContext.getViewRoot().findComponent("panelWithFacet");
        PanelRenderer renderer = (PanelRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        assertEquals(AbstractPanel.class, renderer.getComponentClass());
        facesRequest.release();
    }

    /**
     * Test method for
     * {@link org.richfaces.renderkit.ExtendedDataTableRenderer#doEncodeBegin(javax.faces.context.ResponseWriter, javax.faces.context.FacesContext, javax.faces.component.UIComponent)}
     * .
     *
     * @throws IOException
     */
    @Test
    public final void testDoEncode() throws IOException {
        HtmlPage page = environment.getPage("/panelTest.jsf");
        HtmlElement panelWithFacet = page.getElementById("panelWithFacet");
        assertEquals("rf-p panel", panelWithFacet.getAttribute("class"));
        assertEquals("Write your own custom rich components with built-in AJAX support",
            panelWithFacet.getElementById("panelWithFacet_header").getTextContent().trim());
        assertEquals("The CDK includes", panelWithFacet.getElementById("panelWithFacet_body").getTextContent().trim()
            .substring(0, 16));
        HtmlElement simplePanel = page.getElementById("simplePanel");
        assertEquals("rf-p ", simplePanel.getAttribute("class"));
        try {
            simplePanel.getElementById("simplePanel_header");
        } catch (Exception e) {
            assertTrue(true);
        }
        assertEquals("RichFaces is a l", simplePanel.getElementById("simplePanel_body").getTextContent().trim()
            .substring(0, 16));
        HtmlElement simplePanelBody = page.getElementById("simplePanel_body");
        assertEquals("rf-p-b rich-laguna-panel-no-header", simplePanelBody.getAttribute("class"));
        HtmlElement simplePanel2 = page.getElementById("simplePanelWithTextHeader");
        assertEquals("rf-p ", simplePanel2.getAttribute("class"));
        assertNotNull(simplePanel2.getElementById("simplePanelWithTextHeader_header"));
        assertEquals("rich-laguna-panel-no-header", simplePanel2.getElementById("simplePanelWithTextHeader_header")
            .getTextContent().trim());
        assertEquals("RichFaces is a l", simplePanel2.getElementById("simplePanelWithTextHeader_body").getTextContent().trim()
            .substring(0, 16));

        HtmlElement nestedPanelContainer = page.getElementById("nestedPanelContainer");
        assertEquals("rf-p ", nestedPanelContainer.getAttribute("class"));
        assertNotNull(nestedPanelContainer.getElementById("nestedPanelContainer_header"));
        assertEquals("||||", nestedPanelContainer.getElementById("nestedPanelContainer_header").getTextContent().trim());
        HtmlElement nestedPanelContainerHeader = page.getElementById("nestedPanelContainer_header");
        assertEquals("rf-p-hdr outpanelHeader", nestedPanelContainerHeader.getAttribute("class"));
        assertEquals("Benefits of Usin", nestedPanelContainer.getElementById("nestedPanelContainer_body").getTextContent()
            .trim().substring(0, 16));
        HtmlElement nestedPanel1 = nestedPanelContainer.getElementById("nestedPanel1");
        assertEquals("rf-p ", nestedPanel1.getAttribute("class"));
        HtmlElement nestedPanel1Body = page.getElementById("nestedPanel1_body");
        assertEquals("rf-p-b inpanelBody", nestedPanel1Body.getAttribute("class"));
        assertNotNull(nestedPanel1.getElementById("nestedPanel1_header"));
        assertEquals("For Application Developers", nestedPanel1.getElementById("nestedPanel1_header").getTextContent().trim());
        assertEquals("Production quali",
            nestedPanel1.getElementById("nestedPanel1_body").getTextContent().trim().substring(0, 16));
        HtmlElement nestedPanel2 = nestedPanelContainer.getElementById("nestedPanel2");
        assertEquals("rf-p ", nestedPanel2.getAttribute("class"));
        HtmlElement nestedPanel2Body = page.getElementById("nestedPanel2_body");
        assertEquals("rf-p-b inpanelBody", nestedPanel2Body.getAttribute("class"));
        assertNotNull(nestedPanel2.getElementById("nestedPanel2_header"));
        assertEquals("For Component Developers", nestedPanel2.getElementById("nestedPanel2_header").getTextContent().trim());
        assertEquals("Ajax4jsf is Open",
            nestedPanel2.getElementById("nestedPanel2_body").getTextContent().trim().substring(0, 16));
    }
}
