package org.richfaces.renderkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.List;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.IgnoreTextAndAttributeValuesDifferenceListener;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.Assert;
import org.junit.Test;
import org.richfaces.renderkit.html.RendererTestBase;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AutocompleteRendererTest extends RendererTestBase {
    
    @Override
    public void setUp() throws URISyntaxException {
        environment = new HtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/renderkit/faces-config.xml");
        environment.start();
    }
    
    @Test
    public void testListLayoutRender() throws Exception {
        doTest("autocompleteListLayoutTest", "autocompleteListLayoutClientMode", "form:myAutocomplete");
    }
    
    @Test
    public void testDivLayoutRender() throws Exception {
        doTest("autocompleteDivLayoutTest", "autocompleteDivLayoutClientMode", "form:myAutocomplete");
    }
    
    @Test
    public void testSetValueDivLayout() throws Exception {
        HtmlPage page =  environment.getPage("/autocompleteDivLayoutTest.jsf");
        HtmlElement autocompleteList = page.getElementById("form:myAutocompleteList");
        Assert.assertNotNull(autocompleteList);
        
        List<?> coutnryList = autocompleteList.getByXPath("div/div/div/div/div");
        Assert.assertEquals(30, coutnryList.size());
        
        HtmlInput input = (HtmlInput) page.getElementById("form:myAutocompleteInput");
        Assert.assertNotNull(input);
        input.type("al");

        //try 5 times to wait .5 second each for filling the page.
        for (int i = 0; i < 5; i++) {
            synchronized (page) {
                page.wait(500);
            }
        }
        
        autocompleteList = page.getElementById("form:myAutocompleteList");
        Assert.assertNotNull(autocompleteList);
        
        coutnryList = autocompleteList.getByXPath("div/div/div/div/div");
        Assert.assertEquals(2, coutnryList.size());
    }    
    
    @Test
    public void testSetValueListLayout() throws Exception {
        HtmlPage page =  environment.getPage("/autocompleteListLayoutTest.jsf");
        HtmlElement autocompleteList = page.getElementById("form:myAutocompleteList");
        Assert.assertNotNull(autocompleteList);
        
        List<?> coutnryList = autocompleteList.getByXPath("div/div/div/ul/li");
        Assert.assertEquals(30, coutnryList.size());
        
        HtmlInput input = (HtmlInput) page.getElementById("form:myAutocompleteInput");
        Assert.assertNotNull(input);
        input.type("be");

        //try 5 times to wait .5 second each for filling the page.
        for (int i = 0; i < 5; i++) {
            synchronized (page) {
                page.wait(500);
            }
        }
        
        autocompleteList = page.getElementById("form:myAutocompleteList");
        Assert.assertNotNull(autocompleteList);
        
        coutnryList = autocompleteList.getByXPath("div/div/div/ul/li");
        Assert.assertEquals(5, coutnryList.size());
    }
    
    @Override
    protected void checkXmlStructure(String pageName, String xmlunitPageName, String pageCode) throws SAXException,
        IOException {
        if(xmlunitPageName == null) {
            xmlunitPageName = pageName + ".xmlunit.xml";
        }
        InputStream expectedPageCode = this.getClass().getResourceAsStream(xmlunitPageName + ".xmlunit.xml");
        if (expectedPageCode == null) {
            return;
        }
         
        Diff xmlDiff = new Diff(new StringReader(pageCode), new InputStreamReader(expectedPageCode));
        xmlDiff.overrideDifferenceListener(getDifferenceListener("jquery\\d*"));
        Assert.assertTrue("XML was not similar:" + xmlDiff.toString(), xmlDiff.similar());
    }
    
    private DifferenceListener getDifferenceListener(final String skipAttribute) {
        return new IgnoreTextAndAttributeValuesDifferenceListener() {
            @Override
            public int differenceFound(Difference difference) {
                String controlNameAttribute = difference.getControlNodeDetail().getValue();
                String testNameAttribute = difference.getTestNodeDetail().getValue();
                if (controlNameAttribute.matches(skipAttribute) ||
                    testNameAttribute.matches(skipAttribute)) {
                    return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
                }
                else {
                    return super.differenceFound(difference);
                }
            }
        };
    }
}
