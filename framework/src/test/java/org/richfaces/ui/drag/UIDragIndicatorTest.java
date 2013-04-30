package org.richfaces.ui.drag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.ui.drag.dragIndicator.UIDragIndicator;

public class UIDragIndicatorTest {
    private UIDragIndicator indicator;

    @Before
    public void setUp() {
        indicator = new UIDragIndicator();
    }

    @Test
    public void testSomething() {
        Assert.assertNotNull(indicator);
    }
}
