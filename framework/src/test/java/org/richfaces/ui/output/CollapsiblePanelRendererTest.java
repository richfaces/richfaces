package org.richfaces.ui.output;

import org.junit.Test;
import org.richfaces.ui.common.RendererTestBase;
import org.xml.sax.SAXException;

import java.io.IOException;

public class CollapsiblePanelRendererTest extends RendererTestBase {
    @Test
    public void testEmpty() throws IOException, SAXException {
        doTest("collapsiblePanel", "f:panel");
    }
}
