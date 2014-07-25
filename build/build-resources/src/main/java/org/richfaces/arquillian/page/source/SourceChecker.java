package org.richfaces.arquillian.page.source;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestClass;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByTagName;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SourceChecker {

    @Inject
    private Instance<TestClass> testClass;

    public void checkComponentSource(URL pageName, String xmlunitPage, By pageElementToTest) throws IOException, SAXException {
        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);

        HtmlPage page = client.getPage(pageName);
        DomElement element;

        String locator = pageElementToTest.toString();
        locator = locator.substring(locator.indexOf(':') + 1).trim();

        if (pageElementToTest instanceof ById) {
            element = page.getElementById(locator);
        } else if (pageElementToTest instanceof ByTagName) {
            element = page.getElementsByTagName(locator).get(0);
        } else {
            throw new IllegalArgumentException("Only id and name are supported");
        }

        String pageCode = element.asXml();

        checkXmlStructure(xmlunitPage, pageCode);
    }

    protected void checkXmlStructure(String xmlunitPage, String pageCode) throws SAXException, IOException {
        InputStream expectedPageCode = testClass.get().getJavaClass().getResourceAsStream(xmlunitPage);

        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);

        Diff xmlDiff = new Diff(new InputStreamReader(expectedPageCode), new StringReader(pageCode));
        xmlDiff.overrideDifferenceListener(getDifferenceListener());

        if (!xmlDiff.similar()) {
            System.out.println("=== ACTUAL PAGE CODE ===");
            System.out.println(pageCode);
            System.out.println("======== ERROR =========");
            System.out.println(xmlDiff.toString());
            System.out.println("========================");
            fail("XML was not similar:" + xmlDiff.toString());
        }
    }

    protected DifferenceListener getDifferenceListener() {
        return new IgnoreScriptsContent();
    }
}
