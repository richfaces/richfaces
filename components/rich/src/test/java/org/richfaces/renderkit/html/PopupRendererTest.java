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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Andrey Markhel
 *
 */
public class PopupRendererTest {
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
        FacesRequest facesRequest = environment.createFacesRequest("http://localhost/popupPanelTest.jsf");
        facesRequest.withViewId("/panelTest.jsf");
        facesRequest.start();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ViewHandler vh = facesContext.getApplication().getViewHandler();
        ViewDeclarationLanguage vdl = vh.getViewDeclarationLanguage(facesContext, facesContext.getViewRoot().getViewId());
        vdl.buildView(facesContext, facesContext.getViewRoot());
        return facesRequest;
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
        HtmlPage page = environment.getPage("/popupPanelTest.jsf");
        HtmlElement panelWithFacet = page.getHtmlElementById("panel");
        assertNotNull(panelWithFacet);
        assertEquals("visibility: hidden; display: inline-block;", panelWithFacet.getAttribute("style"));
        HtmlElement panelShade = page.getHtmlElementById("panel_shade");
        assertEquals("rf-pp-shade", panelShade.getAttribute("class"));
        assertNotNull(panelShade);
        HtmlElement panelShadow = page.getHtmlElementById("panel_shadow");
        assertEquals("rf-pp-shdw", panelShadow.getAttribute("class"));
        assertNotNull(panelShadow);
        HtmlElement panelContainer = page.getHtmlElementById("panel_container");
        assertNotNull(panelContainer);
        assertEquals("rf-pp-cntr panelStyle", panelContainer.getAttribute("class"));
        HtmlElement panelScroller = page.getHtmlElementById("panel_content_scroller");
        assertNotNull(panelScroller);
        assertEquals("rf-pp-cnt-scrlr", panelScroller.getAttribute("class"));
        HtmlElement panelContent = page.getHtmlElementById("panel_content");
        assertNotNull(panelContent);
        assertEquals("rf-pp-cnt", panelContent.getAttribute("class"));
        assertEquals("The CDK includes", panelContent.getTextContent().trim().substring(0, 16));
        HtmlElement panelHeader = page.getHtmlElementById("panel_header");
        assertNotNull(panelHeader);
        assertEquals("rf-pp-hdr header", panelHeader.getAttribute("class"));
        // assertEquals("cursor: move;", panelHeader.getAttribute("style"));
        assertEquals("Write your own custom rich components with built-in AJAX", panelHeader.getTextContent().trim());
        HtmlElement panelResizer = page.getHtmlElementById("panelResizerN");
        assertNotNull(panelResizer);
        assertEquals("rf-pp-hndlr rf-pp-hndlr-t", panelResizer.getAttribute("class"));
        assertEquals("cursor: N-resize;", panelResizer.getAttribute("style"));
        List<DomElement> result = page.getElementsByName("script");
        HtmlElement button = page.getHtmlElementById("button");
        button.click();
        HtmlElement sizeButton = page.getHtmlElementById("size");
        sizeButton.click();
        String width = panelContainer.getAttribute("width");
    }
}