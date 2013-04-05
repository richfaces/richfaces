package org.richfaces.resource;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ResourceLibraryFactoryTest {

    @Test
    public void testCreatingStaticResourceLibrary() {
        ResourceLibraryFactory factory = new ResourceLibraryFactoryImpl();
        ResourceLibrary resourceLibrary = factory.getResourceLibrary("static", "org.richfaces");
        assertNotNull(resourceLibrary);
    }
}
