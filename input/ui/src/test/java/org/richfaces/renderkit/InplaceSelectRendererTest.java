package org.richfaces.renderkit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author abelevich
 *
 */
public class InplaceSelectRendererTest extends InplaceRendererTestBase {
    public static final String PAGE_NAME = "inplaceSelectTest";
    public static final String BASE_ID = "form:inplaceSelect";

    @Test
    public void testDefaultEncode() throws IOException, SAXException {
        doTestDefaultEncode(PAGE_NAME, BASE_ID);
    }

    @Test
    public void testDefaultWithControlsEncode() throws IOException, SAXException {
        doTestDefaultWithControlsEncode(PAGE_NAME, BASE_ID);
    }

    @Test
    @Ignore
    public void testEditEncode() throws IOException, SAXException {
        doTestEditEncode(PAGE_NAME, BASE_ID);
    }

    private void edit(HtmlPage page, String inplaceSelectId, int selectIndex) throws Exception {
        HtmlElement span = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "']");
        assertNotNull(span);
        span.click();

        HtmlElement edit = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "Edit']");
        assertNotNull(edit);
        assertEquals("rf-is-fld-cntr", edit.getAttribute(HtmlConstants.CLASS_ATTRIBUTE));

        HtmlElement list = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "List']");
        assertNotNull(list);
        assertTrue(list.isDisplayed());

        HtmlElement item = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "Item" + selectIndex + "']");
        assertNotNull(item);
        item.click();

        HtmlElement panel = page.getFirstByXPath("//*[@id = 'form:out']");
        panel.click();

        list = page.getFirstByXPath("//*[@id = '" + inplaceSelectId + "List']");
        assertNotNull(list);
        assertFalse(list.isDisplayed());
    }

    public void TestEditWithControls() {
    }
}
