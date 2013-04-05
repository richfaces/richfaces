package org.richfaces.ui.input;

import java.io.File;
import java.net.URISyntaxException;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.Test;
import org.richfaces.ui.common.RendererTestBase;

public class AutocompleteRendererTest extends RendererTestBase {

    @Override
    public void setUp() throws URISyntaxException {
        environment = new HtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/ui/input/faces-config.xml");
        environment.start();
        environment.getWebClient().setJavaScriptEnabled(false);
    }

    @Test
    public void testListLayoutRender() throws Exception {
        doTest("autocompleteListLayoutTest", "autocompleteListLayoutClientMode", "form:myAutocomplete");
    }

    @Test
    public void testDivLayoutRender() throws Exception {
        doTest("autocompleteDivLayoutTest", "autocompleteDivLayoutClientMode", "form:myAutocomplete");
    }
}
