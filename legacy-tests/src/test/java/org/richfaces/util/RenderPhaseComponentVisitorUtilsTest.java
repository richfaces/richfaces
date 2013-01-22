
/**
 *
 */
package org.richfaces.util;

import org.apache.shale.test.base.AbstractJsfTestCase;

import org.richfaces.event.RenderPhaseComponentVisitor;

public class RenderPhaseComponentVisitorUtilsTest extends AbstractJsfTestCase {
    public RenderPhaseComponentVisitorUtilsTest(String name) {
        super(name);
    }

    /**
     * Test method for {@link org.richfaces.util.ComponentPhaseEventHandlerUtils#getHandlers()}.
     */
    public void testGetHandlers() {
        RenderPhaseComponentVisitor[] handlers = RenderPhaseComponentVisitorUtils.getVisitors(facesContext);

        assertNotNull(handlers);
        assertTrue(handlers.length > 0);
    }
}
