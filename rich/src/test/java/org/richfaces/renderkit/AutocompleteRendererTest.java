package org.richfaces.renderkit;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.IgnoreTextAndAttributeValuesDifferenceListener;
import org.junit.Assert;
import org.junit.Test;
import org.richfaces.CustomizedHtmlUnitEnvironment;
import org.richfaces.renderkit.html.RendererTestBase;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AutocompleteRendererTest extends RendererTestBase {
    @Override
    public void setUp() throws URISyntaxException {
        environment = new CustomizedHtmlUnitEnvironment();
        environment.withWebRoot(new File(this.getClass().getResource(".").toURI()));
        environment.withResource("/WEB-INF/faces-config.xml", "org/richfaces/renderkit/faces-config.xml");
        environment.start();
    }

    @Test
    public void testListLayoutRender() throws Exception {
        environment.getWebClient().getOptions().setJavaScriptEnabled(false);
        doTest("autocompleteListLayoutTest", "autocompleteListLayoutClientMode", "form:myAutocomplete");
    }

    @Test
    public void testDivLayoutRender() throws Exception {
        environment.getWebClient().getOptions().setJavaScriptEnabled(false);
        doTest("autocompleteDivLayoutTest", "autocompleteDivLayoutClientMode", "form:myAutocomplete");
    }

    @Test
    public void testSetValueDivLayout() throws Exception {
        environment.getWebClient().getOptions().setJavaScriptEnabled(true);
        HtmlPage page = environment.getPage("/autocompleteDivLayoutTest.jsf");
        HtmlElement autocompleteList = (HtmlElement) page.getElementById("form:myAutocompleteList");
        Assert.assertNotNull(autocompleteList);

        List<?> countryList = autocompleteList.getByXPath("div/div/div/div/div");
        Assert.assertEquals(30, countryList.size());

        HtmlInput input = (HtmlInput) page.getElementById("form:myAutocompleteInput");
        Assert.assertNotNull(input);
        input.type("al");

        for (int i = 0; i < 20; i++) {
            synchronized (page) {
                autocompleteList = (HtmlElement) page.getElementById("form:myAutocompleteList");
                Assert.assertNotNull(autocompleteList);

                countryList = autocompleteList.getByXPath("div/div/div/div/div");

                if (countryList.size() == 2) {
                    break;
                } else {
                    page.wait(500);
                }
            }
        }

        Assert.assertEquals(2, countryList.size());
    }

    @Test
    public void testSetValueListLayout() throws Exception {
        environment.getWebClient().getOptions().setJavaScriptEnabled(true);
        HtmlPage page = environment.getPage("/autocompleteListLayoutTest.jsf");
        HtmlElement autocompleteList = (HtmlElement) page.getElementById("form:myAutocompleteList");
        Assert.assertNotNull(autocompleteList);

        List<?> countryList = autocompleteList.getByXPath("div/div/div/ul/li");
        Assert.assertEquals(30, countryList.size());

        HtmlInput input = (HtmlInput) page.getElementById("form:myAutocompleteInput");
        Assert.assertNotNull(input);
        input.type("be");

        for (int i = 0; i < 20; i++) {
            synchronized (page) {
                autocompleteList = (HtmlElement) page.getElementById("form:myAutocompleteList");
                Assert.assertNotNull(autocompleteList);

                countryList = autocompleteList.getByXPath("div/div/div/ul/li");

                if (countryList.size() == 5) {
                    break;
                } else {
                    page.wait(500);
                }
            }
        }

        Assert.assertEquals(5, countryList.size());

    }

    @Override
    protected DifferenceListener getDifferenceListener() {
        final String skipAttribute = "(jquery|sizcache)\\d*";

        return new IgnoreTextAndAttributeValuesDifferenceListener() {
            @Override
            public int differenceFound(Difference difference) {
                String controlNameAttribute = difference.getControlNodeDetail().getValue();
                String testNameAttribute = difference.getTestNodeDetail().getValue();
                if (controlNameAttribute.matches(skipAttribute) || testNameAttribute.matches(skipAttribute)) {
                    return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
                } else {
                    return super.differenceFound(difference);
                }
            }
        };
    }
}
