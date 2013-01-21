package org.richfaces.renderkit.html;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class TabPanelRendererTest extends RendererTestBase {
    @Test
    public void testTabPanel() throws IOException, SAXException {
        doTest("tabPanel", "f:tabPanel");
    }

    @Test
    public void testTab() throws IOException, SAXException {
        doTest("tab", "f:tabPanel");
    }
}
