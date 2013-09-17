package org.richfaces.component;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
