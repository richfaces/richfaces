package org.richfaces.ui.drag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.component.UIDragSource;

public class UIDragSourceTest {
    private UIDragSource dragSource;

    @Before
    public void setUp() {
        dragSource = new UIDragSource();
    }

    @Test
    public void testSomething() {
        Assert.assertNotNull(dragSource);
    }
}
