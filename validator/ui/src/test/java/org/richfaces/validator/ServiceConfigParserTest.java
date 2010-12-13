package org.richfaces.validator;

import static org.junit.Assert.*;

import java.util.Map;

import javax.faces.FacesException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServiceConfigParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseConfig() {
        Map<Class<?>, LibraryFunction> parseConfig = ClientServiceConfigParser.parseConfig("csv.xml");
        assertEquals(2, parseConfig.size());
        assertTrue(parseConfig.containsKey(String.class));
        LibraryFunction libraryFunction = parseConfig.get(String.class);
        assertEquals("stringConverter", libraryFunction.getName());
        assertEquals("csv.js", libraryFunction.getResource().getResourceName());
        assertEquals("org.richfaces", libraryFunction.getResource().getLibrary());
    }

    @Test(expected=FacesException.class)
    public void testParseBadConfig() {
        Map<Class<?>, LibraryFunction> parseConfig = ClientServiceConfigParser.parseConfig("badcsv.xml");
    }
    @Test()
    public void testParseNoConfig() {
        Map<Class<?>, LibraryFunction> parseConfig = ClientServiceConfigParser.parseConfig("non-exists-csv.xml");
        assertEquals(0, parseConfig.size());
    }
}
