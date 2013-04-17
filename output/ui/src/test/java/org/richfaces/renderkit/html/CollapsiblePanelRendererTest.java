package org.richfaces.renderkit.html;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class CollapsiblePanelRendererTest extends RendererTestBase {
    @Test
    public void testEmpty() throws IOException, SAXException {
        doTest("collapsiblePanel", "f:panel");
    }
}
