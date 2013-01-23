package org.richfaces.ui.input;

import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.richfaces.ui.common.RendererTestBase;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public abstract class InplaceRendererTestBase extends RendererTestBase {
    public static String EDIT = "Edit";
    public static String WITH_CONTROLS = "WithControls";
    public static String DEFAULT = "Default";

    @Override
    public void setUp() throws URISyntaxException {
        environment = new HtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/resource/ui/input/faces-config.xml");
        environment.start();
    }

    public void doTestDefaultEncode(String pageName, String baseId) throws IOException, SAXException {
        doTest(pageName, (pageName + DEFAULT), (baseId + DEFAULT));
    }

    public void doTestDefaultWithControlsEncode(String pageName, String baseId) throws IOException, SAXException {
        doTest(pageName, (pageName + WITH_CONTROLS), (baseId + WITH_CONTROLS));
    }

    public void doTestEditEncode(String pageName, String baseId) throws IOException, SAXException {
        doTest(pageName, (pageName + EDIT), (baseId + EDIT));
    }
}
