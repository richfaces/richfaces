package org.richfaces.integration.partialResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This utility helps to parse a last partial-response done by jsf.js by intercepting the JSF JS API and writing the response to
 * the object for inspection.
 */
public class PartialResponseTestingHelper {

    public static void addPartialResponseInterceptorToPage(FaceletAsset p) {
        p.body("<h:outputScript>");
        p.body("    var __backup = jsf.ajax.response;");
        p.body("    var __response;");
        p.body("    jsf.ajax.response = function(request, context) {");
        p.body("        __response = request.responseText;");
        p.body("        __backup(request, context);");
        p.body("    };");
        p.body("</h:outputScript>");
    }

    public static Document getDocument(WebDriver browser) throws SAXException, IOException, ParserConfigurationException {
        final JavascriptExecutor executor = (JavascriptExecutor) browser;

        String responseText = (String) executor.executeScript("return __response");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(new ByteArrayInputStream(responseText.getBytes()));
    }

}
