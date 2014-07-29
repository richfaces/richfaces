package org.richfaces.renderkit;

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
import org.richfaces.component.AbstractDataGrid;

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
        HtmlElement dataGrid = (HtmlElement) page.getElementById("dataGrid2");
        assertEquals("rf-dg", dataGrid.getAttribute("class"));
        assertEquals("noDataFacet",
            ((HtmlElement) dataGrid
                .getFirstByXPath("tbody/tr[@class='rf-dg-nd']/td[@class='rf-dg-nd-c']/*[@id='dataGrid2:noDataFacet']"))
                .getTextContent());
    }

    @Test
    public final void testEncodeRow() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = (HtmlElement) page.getElementById("dataGrid");
        assertEquals("value",
            ((HtmlElement) dataGrid
                .getFirstByXPath("tbody/tr[@class='rf-dg-r']/td[@class='rf-dg-c']/*[@id='dataGrid:0:outputText']"))
                .getTextContent());
    }

    @Test
    public final void testEncodeHeader() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = (HtmlElement) page.getElementById("dataGrid");
        HtmlElement th = dataGrid.getFirstByXPath("thead[@class='rf-dg-thead']/tr[@class='rf-dg-h']/th[@class='rf-dg-h-c']");
        assertEquals("1", th.getAttribute("colspan"));
        assertEquals("headerFacet", page.getElementById("dataGrid:headerFacet").getTextContent());
    }

    @Test
    public final void testEncodeFooter() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = (HtmlElement) page.getElementById("dataGrid");
        HtmlElement td = dataGrid.getFirstByXPath("tfoot[@class='rf-dg-tfoot']/tr[@class='rf-dg-f']/td[@class='rf-dg-f-c']");
        assertEquals("1", td.getAttribute("colspan"));
        assertEquals("footerFacet", page.getElementById("dataGrid:footerFacet").getTextContent());
    }

    @Test
    public final void testEncodeCaption() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGridCaption = (HtmlElement) page.getElementById("dataGrid:captionFacet");
        assertEquals("captionFacet", dataGridCaption.getTextContent());
    }

    @Test
    public final void testEncodeTBody() throws IOException {
        HtmlPage page = environment.getPage("/dataGridTest.jsf");
        HtmlElement dataGrid = (HtmlElement) page.getElementById("dataGrid2");
        assertEquals("rf-dg-body", ((HtmlElement) dataGrid.getFirstByXPath("tbody[@id='dataGrid2:dgb']")).getAttribute("class"));
    }
}
