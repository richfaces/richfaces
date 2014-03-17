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
package org.richfaces.renderkit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;

import org.jboss.test.faces.FacesEnvironment.FacesRequest;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;
import org.richfaces.component.AbstractExtendedDataTable;
import org.richfaces.model.SortOrder;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Konstantin Mishin
 *
 */
public class ExtendedDataTableRendererTest {
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
        FacesRequest facesRequest = environment.createFacesRequest("http://localhost/extendedDataTableTest.jsf");
        facesRequest.withViewId("/extendedDataTableTest.jsf");
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
        AbstractExtendedDataTable component = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("table");
        ExtendedDataTableRenderer renderer = (ExtendedDataTableRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        assertEquals(AbstractExtendedDataTable.class, renderer.getComponentClass());
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
    public final void testDoEncodeBegin() throws IOException {
        HtmlPage page = environment.getPage("/extendedDataTableTest.jsf");
        HtmlElement table = (HtmlElement) page.getElementById("table");
        assertEquals("rf-edt", table.getAttribute("class"));
        assertEquals("headerFacet", table.getElementById("table:headerFacet").getTextContent());
    }

    /**
     * Test method for
     * {@link org.richfaces.renderkit.ExtendedDataTableRenderer#doEncodeChildren(javax.faces.context.ResponseWriter, javax.faces.context.FacesContext, javax.faces.component.UIComponent)}
     * .
     *
     * @throws IOException
     */
    @Test
    public final void testDoEncodeChildren() throws IOException {
        HtmlPage page = environment.getPage("/extendedDataTableTest.jsf");
        HtmlElement table = (HtmlElement) page.getElementById("table");
        String text = table.getElementsByTagName("style").get(0).getTextContent();
        assertTrue(text.contains(".rf-edt-cnt"));
        assertTrue(text.contains("rf-edt-c"));
        HtmlElement header = table.getFirstByXPath("div[@class='rf-edt-hdr']");
        HtmlElement frozenHeader = header.getElementById("table:frozenHeader");
        HtmlElement normalHeader = header.getElementById("table:header");
        assertTrue(normalHeader.getAttribute("class").contains("rf-edt-cnt"));
        assertNotNull(frozenHeader.getFirstByXPath("descendant::*[@class='rf-edt-rsz']"));
        assertNotNull(normalHeader.getFirstByXPath("descendant::*[@class='rf-edt-rsz']"));
        assertEquals("headerColumnFacet1",
            ((HtmlElement) frozenHeader.getFirstByXPath("//*[@class='rf-edt-hdr-c-cnt']//*[@id='table:headerColumnFacet1']"))
                .getTextContent());
        assertEquals("headerColumnFacet2",
            ((HtmlElement) normalHeader.getFirstByXPath("//*[@class='rf-edt-hdr-c-cnt']//*[@id='table:headerColumnFacet2']"))
                .getTextContent());
        HtmlElement body = table.getElementById("table:b");
        assertEquals("rf-edt-b", body.getAttribute("class"));
        assertNotNull(body.getFirstByXPath("descendant::*[@class='rf-edt-spcr']"));
        assertNotNull(body.getFirstByXPath("descendant::*[@class='rf-edt-cnt']//*[@id='table:tbn']"));
        assertEquals("noDataFacet",
            ((HtmlElement) page.getFirstByXPath("//*[@id='table2']//*[@id='table2:b']//*[@id='table2:noDataFacet']"))
                .getTextContent());
        HtmlElement footer = table.getFirstByXPath("div[@class='rf-edt-ftr']");
        HtmlElement frozenFooter = footer.getFirstByXPath("descendant::*[@class='rf-edt-ftr-fzn']/div");
        HtmlElement normalFooter = footer.getElementById("table:footer");
        assertTrue(normalFooter.getAttribute("class").contains("rf-edt-ftr-cnt"));
        assertEquals("footerColumnFacet1",
            ((HtmlElement) frozenFooter
                .getFirstByXPath("descendant::*[@class='rf-edt-ftr-c-cnt']//*[@id='table:footerColumnFacet1']"))
                .getTextContent());
        assertEquals("footerColumnFacet2",
            ((HtmlElement) normalFooter
                .getFirstByXPath("descendant::*[@class='rf-edt-ftr-c-cnt']//*[@id='table:footerColumnFacet2']"))
                .getTextContent());
    }

    /**
     * Test method for
     * {@link org.richfaces.renderkit.ExtendedDataTableRenderer#doEncodeEnd(javax.faces.context.ResponseWriter, javax.faces.context.FacesContext, javax.faces.component.UIComponent)}
     * .
     *
     * @throws IOException
     */
    @Test
    public final void testDoEncodeEnd() throws IOException {
        HtmlPage page = environment.getPage("/extendedDataTableTest.jsf");
        HtmlElement table = (HtmlElement) page.getElementById("table");
        assertEquals("footerFacet", table.getElementById("table:footerFacet").getTextContent());
        assertEquals("rf-edt-rsz-mkr", table.getElementById("table:d").getAttribute("class"));
        assertEquals("rf-edt-rord rf-edt-tbl", table.getElementById("table:r").getAttribute("class"));
        assertEquals("rf-edt-rord-mkr", table.getElementById("table:rm").getAttribute("class"));
        assertEquals("table:wi", table.getElementById("table:wi").getAttribute("name"));
        assertTrue(table.getElementsByTagName("script").get(0).getTextContent().contains("RichFaces.rf4.ui.ExtendedDataTable"));
    }

    /**
     * Test method for
     * {@link org.richfaces.renderkit.ExtendedDataTableRenderer#encodeRow(org.richfaces.renderkit.RowHolderBase)}.
     *
     * @throws IOException
     */
    @Test
    public final void testEncodeRow() throws IOException {
        HtmlPage page = environment.getPage("/extendedDataTableTest.jsf");
        HtmlElement table = (HtmlElement) page.getElementById("table");
        HtmlElement cell = table.getElementById("table:0:f").getElementsByTagName("div").get(0);
        assertTrue(cell.getAttribute("class").contains("rf-edt-c"));
        HtmlElement cellContent = cell.getElementsByTagName("div").get(0);
        assertEquals("rf-edt-c-cnt", cellContent.getAttribute("class"));
        assertEquals("value", cellContent.getElementById("table:0:outputText").getTextContent());
    }

    /**
     * Test method for
     * {@link org.richfaces.renderkit.ExtendedDataTableRenderer#encodePartially(javax.faces.component.UIComponent, javax.faces.component.visit.VisitContext, javax.faces.component.visit.VisitCallback, java.util.Collection)}
     * .
     */
    @Test
    public final void testEncodePartially() {
        // TODO fail("Not yet implemented");
    }

    @Test
    public final void testFilteringWithoutClean() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable component = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("table");
        ExtendedDataTableRenderer renderer = (ExtendedDataTableRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        Map<String, Object> column1Attributes = component.findComponent("column1").getAttributes();
        Map<String, Object> column2Attributes = component.findComponent("column2").getAttributes();
        String clientId = component.getClientId(facesContext);
        assertNull(column1Attributes.get("filterValue"));
        assertEquals("filterValue2", column2Attributes.get("filterValue"));
        facesRequest.withParameter(clientId, clientId);
        facesRequest.withParameter(clientId + "rich:filtering", "column1:filterValue1:null");
        renderer.doDecode(facesContext, component);
        assertEquals("filterValue1", column1Attributes.get("filterValue"));
        assertEquals("filterValue2", column2Attributes.get("filterValue"));
        assertTrue(facesContext.getPartialViewContext().getRenderIds().contains(clientId));
        facesRequest.release();
    }

    @Test
    public final void testFilteringWithClean() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable component = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("table");
        ExtendedDataTableRenderer renderer = (ExtendedDataTableRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        Map<String, Object> column1Attributes = component.findComponent("column1").getAttributes();
        Map<String, Object> column2Attributes = component.findComponent("column2").getAttributes();
        String clientId = component.getClientId(facesContext);
        assertNull(column1Attributes.get("filterValue"));
        assertEquals("filterValue2", column2Attributes.get("filterValue"));
        facesRequest.withParameter(clientId, clientId);
        facesRequest.withParameter(clientId + "rich:filtering", "column1:filterValue1:true");
        renderer.doDecode(facesContext, component);
        assertEquals("filterValue1", column1Attributes.get("filterValue"));
        assertNull(column2Attributes.get("filterValue"));
        assertTrue(facesContext.getPartialViewContext().getRenderIds().contains(clientId));
        facesRequest.release();
    }

    @Test
    public final void testColumnResizing() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable component = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("table");
        ExtendedDataTableRenderer renderer = (ExtendedDataTableRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        Map<String, Object> column1Attributes = component.findComponent("column1").getAttributes();
        assertNull(column1Attributes.get("width"));
        facesRequest.withParameter(component.getClientId(facesContext) + ":wi", "column1:200px");
        renderer.doDecode(facesContext, component);
        assertEquals("200px", column1Attributes.get("width"));
        facesRequest.release();
    }

    @Test
    public final void testColumnReordering() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable component = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("table");
        ExtendedDataTableRenderer renderer = (ExtendedDataTableRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        Map<String, Object> componentAttributes = component.getAttributes();
        String clientId = component.getClientId(facesContext);
        assertNull(componentAttributes.get("columnsOrder"));
        facesRequest.withParameter(clientId, clientId);
        facesRequest.withParameter("rich:columnsOrder", "column2,column1");
        renderer.doDecode(facesContext, component);
        assertTrue(facesContext.getPartialViewContext().getRenderIds().contains(clientId));
        assertTrue(Arrays.equals(new String[] { "column2", "column1" }, (String[]) componentAttributes.get("columnsOrder")));
        facesRequest.release();
    }

    @Test
    public final void testSortingWithoutClean() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable component = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("table");
        ExtendedDataTableRenderer renderer = (ExtendedDataTableRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        Map<String, Object> column1Attributes = component.findComponent("column1").getAttributes();
        Map<String, Object> column2Attributes = component.findComponent("column2").getAttributes();
        String clientId = component.getClientId(facesContext);
        assertNull(column1Attributes.get("sortOrder"));
        assertEquals(SortOrder.ascending, column2Attributes.get("sortOrder"));
        facesRequest.withParameter(clientId, clientId);
        facesRequest.withParameter(clientId + "rich:sorting", "column1:null:null");
        renderer.doDecode(facesContext, component);
        assertEquals(SortOrder.ascending, column1Attributes.get("sortOrder"));
        assertEquals(SortOrder.ascending, column2Attributes.get("sortOrder"));
        assertTrue(facesContext.getPartialViewContext().getRenderIds().contains(clientId));
        facesRequest.release();
    }

    @Test
    public final void testSortingWithClean() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable component = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("table");
        ExtendedDataTableRenderer renderer = (ExtendedDataTableRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        Map<String, Object> column1Attributes = component.findComponent("column1").getAttributes();
        Map<String, Object> column2Attributes = component.findComponent("column2").getAttributes();
        String clientId = component.getClientId(facesContext);
        assertNull(column1Attributes.get("sortOrder"));
        assertEquals(SortOrder.ascending, column2Attributes.get("sortOrder"));
        facesRequest.withParameter(clientId, clientId);
        facesRequest.withParameter(clientId + "rich:sorting", "column1:descending:true");
        renderer.doDecode(facesContext, component);
        assertEquals(SortOrder.descending, column1Attributes.get("sortOrder"));
        assertEquals(SortOrder.unsorted, column2Attributes.get("sortOrder"));
        assertTrue(facesContext.getPartialViewContext().getRenderIds().contains(clientId));
        facesRequest.release();
    }

    @Test
    public final void testScrolling() throws IOException {
        FacesRequest facesRequest = startFacesRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractExtendedDataTable component = (AbstractExtendedDataTable) facesContext.getViewRoot().findComponent("table");
        ExtendedDataTableRenderer renderer = (ExtendedDataTableRenderer) FacesContext.getCurrentInstance().getRenderKit()
            .getRenderer(component.getFamily(), component.getRendererType());
        Map<String, Object> componentAttributes = component.getAttributes();
        String clientId = component.getClientId(facesContext);
        assertNull(componentAttributes.get(AbstractExtendedDataTable.SUBMITTED_CLIENT_FIRST));
        facesRequest.withParameter(clientId, clientId);
        facesRequest.withParameter("rich:clientFirst", "28");
        renderer.doDecode(facesContext, component);
        assertEquals(28, componentAttributes.get(AbstractExtendedDataTable.SUBMITTED_CLIENT_FIRST));
        assertTrue(facesContext.getPartialViewContext().getRenderIds()
            .contains(clientId + "@" + AbstractExtendedDataTable.SCROLL));
        facesRequest.release();
    }
}
