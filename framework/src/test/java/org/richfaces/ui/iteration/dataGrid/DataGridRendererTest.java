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

package org.richfaces.ui.iteration.dataGrid;

import static org.junit.Assert.assertEquals;

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

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DataGridRendererTest {
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
        FacesRequest facesRequest = environment.createFacesRequest("http://localhost/dataGridTest.jsf");
        facesRequest.withViewId("/dataGridTest.jsf");
        facesRequest.start();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ViewHandler vh = facesContext.getApplication().getViewHandler();
        ViewDeclarationLanguage vdl = vh.getViewDeclarationLanguage(facesContext, facesContext.getViewRoot().getViewId());
        vdl.buildView(facesContext, facesContext.getViewRoot());
        return facesRequest;
    }

    @Test
    public final void testGetComponentClass() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractDataGrid component = (AbstractDataGrid) facesContext.getViewRoot().findComponent("dataGrid");
        DataGridRenderer renderer = (DataGridRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        assertEquals(AbstractDataGrid.class, renderer.getComponentClass());
        facesRequest.release();
    }

    @Test
    public final void testDoEncodeChildren() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = page.getElementById("dataGrid2");
        assertEquals("rf-dg", dataGrid.getAttribute("class"));
        assertEquals("noDataFacet",
            ((HtmlElement) dataGrid
                .getFirstByXPath("tbody/tr[@class='rf-dg-nd']/td[@class='rf-dg-nd-c']/*[@id='dataGrid2:noDataFacet']"))
                .getTextContent());
    }

    @Test
    public final void testEncodeRow() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = page.getElementById("dataGrid");
        assertEquals("value",
            ((HtmlElement) dataGrid
                .getFirstByXPath("tbody/tr[@class='rf-dg-r']/td[@class='rf-dg-c']/*[@id='dataGrid:0:outputText']"))
                .getTextContent());
    }

    @Test
    public final void testEncodeHeader() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = page.getElementById("dataGrid");
        HtmlElement th = dataGrid.getFirstByXPath("thead[@class='rf-dg-thead']/tr[@class='rf-dg-h']/th[@class='rf-dg-h-c']");
        assertEquals("1", th.getAttribute("colspan"));
        assertEquals("headerFacet", th.getElementById("dataGrid:headerFacet").getTextContent());
    }

    @Test
    public final void testEncodeFooter() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = page.getElementById("dataGrid");
        HtmlElement td = dataGrid.getFirstByXPath("tfoot[@class='rf-dg-tfoot']/tr[@class='rf-dg-f']/td[@class='rf-dg-f-c']");
        assertEquals("1", td.getAttribute("colspan"));
        assertEquals("footerFacet", td.getElementById("dataGrid:footerFacet").getTextContent());
    }

    @Test
    public final void testEncodeCaption() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = page.getElementById("dataGrid");
        assertEquals("captionFacet",
            ((HtmlElement) dataGrid.getFirstByXPath("caption[@class='rf-dg-cap']//*[@id='dataGrid:captionFacet']"))
                .getTextContent());
    }

    @Test
    public final void testEncodeTBody() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = page.getElementById("dataGrid2");
        assertEquals("rf-dg-body", ((HtmlElement) dataGrid.getFirstByXPath("tbody[@id='dataGrid2:dgb']")).getAttribute("class"));
    }
}
