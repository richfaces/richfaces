package org.richfaces.ui.output;

import org.junit.Test;
import org.richfaces.ui.common.RendererTestBase;
import org.xml.sax.SAXException;

import java.io.IOException;

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
