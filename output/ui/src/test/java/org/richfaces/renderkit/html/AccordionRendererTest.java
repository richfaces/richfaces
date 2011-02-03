package org.richfaces.renderkit.html;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

public class AccordionRendererTest extends RendererTestBase {

    @Test
    public void testAccordion() throws IOException, SAXException {
        doTest("accordion", "f:accordion");
    }

    @Test
    public void testAccordionItem() throws IOException, SAXException {
        doTest("accordionItem", "f:item");
        doTest("accordionItem", "accordionItemDisabled", "f:item2");
    }
}
