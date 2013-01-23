package org.richfaces.ui.drag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.component.UIDropTarget;

public class UIDropTargetTest {
    private UIDropTarget dragTarget;

    @Before
    public void setUp() {
        dragTarget = new UIDropTarget();
    }

    @Test
    public void testSomething() {
        Assert.assertNotNull(dragTarget);
    }
}
