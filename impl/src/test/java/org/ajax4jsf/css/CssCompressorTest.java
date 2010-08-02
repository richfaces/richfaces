
/*
* CssCompressorTest.java       Date created: 21.11.2007
* Last modified by: $Author$
* $Revision$   $Date$
 */
package org.ajax4jsf.css;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.ajax4jsf.resource.CountingOutputWriter;

/**
 * Test case for css compressing process
 * @author Andrey Markavtsov
 *
 */
public class CssCompressorTest extends TestCase {

    /** css example to be comressed */
    private static final String cssExample =
        "HTML { \n" + "MARGIN-BOTTOM: 0.01em; HEIGHT: 100%; BACKGROUND-COLOR: #ffffff;\n" + "}\n" + "TD {\n"
        + "FONT-SIZE: 10px; COLOR: #000000; FONT-FAMILY: verdana, arial\n" + "}\n" + "TH {\n"
        + "FONT-WEIGHT: bold; FONT-SIZE: 10px; COLOR: #336699; FONT-FAMILY: verdana, arial; BACKGROUND-COLOR: #ffffff; text-align:left;\n"
        + "}\n" + ".header {\n" + "FONT-WEIGHT: bold; FONT-SIZE: 11px; COLOR: #000000; FONT-FAMILY: verdana, arial\n"
        + "}\n";

    /** Length of correctly compressed css example */
    private static final int lengthCompressed = 305;

    /**
     * Constructor
     * @param name
     */
    public CssCompressorTest(String name) {
        super(name);

        // TODO Auto-generated constructor stub
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        // TODO Auto-generated method stub
        super.setUp();
    }

    /*
     *  (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        // TODO Auto-generated method stub
        super.tearDown();
    }

    /**
     * Test method
     * @throws IOException
     */
    public void testCssCompressor() throws IOException {
        StringBuffer cssBuffer = new StringBuffer(cssExample);
        CssCompressor compressor = new CssCompressor(cssBuffer);
        CountingOutputWriter writer = new CountingOutputWriter();

        compressor.compress(writer, -1);

        // compressed length should equal
        assertEquals(writer.getWritten(), lengthCompressed);
    }

    public void testSequentialComments() throws Exception {
        StringBuffer cssBuffer =
            new StringBuffer(
                "/* copyright */ body { color: red; } /* abc *//* cde */ html { color: red; } /* copyright end */");
        CssCompressor compressor = new CssCompressor(cssBuffer);
        StringWriter stringWriter = new StringWriter();

        compressor.compress(stringWriter, -1);
        stringWriter.close();
        assertEquals("body{color:red;}html{color:red;}", stringWriter.toString());
    }

    public void testFakeComment() throws Exception {

        // this test won't go as our CSS compressor is not aware of possible /**/ in url
        // StringBuffer cssBuffer = new StringBuffer("/* copyright */ body { /* my style */ background-image: url(/*/); color: red; background-image: url(/****/); } /* copyright end */");
        // CssCompressor compressor = new CssCompressor(cssBuffer);
        // StringWriter stringWriter = new StringWriter();
        // compressor.compress(stringWriter, -1);
        // stringWriter.close();
        // assertEquals("body{background-image:url(/*/);color:red;background-image:url(/****/);}", stringWriter.toString());
    }
}
