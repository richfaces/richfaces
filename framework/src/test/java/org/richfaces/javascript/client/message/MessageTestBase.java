/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.javascript.client.message;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.test.qunit.Qunit;
import org.jboss.test.qunit.Qunit.Builder;
import org.junit.Rule;
import org.richfaces.javascript.JSFunction;
import org.richfaces.validator.Message;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MessageTestBase {
    public static final String COMPONENT = "form:component";
    public static final String MY_MESSAGE = "form:uiMessage";
    @Rule
    public final Qunit qunit;

    public MessageTestBase() {
        this.qunit = createQunitPage().build();
    }

    public void setUpMessage() {
        setUpMessage("");
    }

    public void setUpMessage(String messageOptions) {
        qunit.runScript(getMessageInit(messageOptions));
    }

    protected Builder createQunitPage() {
        return Qunit
            .builder()
            .emulate(BrowserVersion.FIREFOX_3_6)
            .loadJsfResource("jquery.js")
            .loadJsfResource("richfaces.js")
            .loadJsfResource("richfaces-event.js")
            .loadJsfResource("richfaces-base-component.js", "org.richfaces/common")
            .loadJsfResource("richfaces-csv.js", "org.richfaces")
            .loadJsfResource("message/message/message.js", "org.richfaces")
            .content(
                "<form id=\"form\" name=\"form\" method=\"post\" action=\"/client-test.jsf\" enctype=\"application/x-www-form-urlencoded\">\n"
                    + "      <input type=\"hidden\" name=\"form\" value=\"form\"/>\n"
                    + "      <input id=\"form:text\" type=\"text\" name=\"form:text\" value=\"fooValue\" onblur=\"form_3Atext_3Av(&quot;form:text&quot;,this,event)\"/>\n"
                    + "      <span id=\"form:out\">\n"
                    + "        fooValue\n"
                    + "      </span><span id=\""
                    + MY_MESSAGE
                    + "\">"
                    + getMessageContent()
                    + "</span>"
                    + "      <input type=\"hidden\" name=\"javax.faces.ViewState\" id=\"javax.faces.ViewState\" value=\"4262028796446907996:-2607792463910755035\" autocomplete=\"off\"/>\n"
                    + "    </form>");
    }

    protected String getMessageContent() {
        return "";
    }

    protected Object sendMessage() {
        JSFunction clientSideFunction = new JSFunction("RichFaces.csv." + getJavaScriptFunctionName(), COMPONENT,
            getErrorMessage());
        return qunit.runScript(clientSideFunction.toScript());
    }

    protected String getJavaScriptFunctionName() {
        return "sendMessage";
    }

    protected Message getErrorMessage() {
        return new Message(2, "error summary", "error description");
    }

    protected HtmlElement getMessageContentElement() {
        HtmlPage page = qunit.getPage();
        HtmlElement htmlElement = (HtmlElement) page.getElementById(MY_MESSAGE);
        assertNotNull(htmlElement);
        return htmlElement;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param messageOptions TODO
     * @return the messageInit
     */
    public String getMessageInit(String messageOptions) {
        return "new RichFaces.ui.Message(\"" + MY_MESSAGE + "\", {forComponentId:\"" + COMPONENT + "\"" + messageOptions + "})";
    }

    protected void checkMessageContent(String summary) {
        String text = getMessageAsText();
        assertTrue(text.contains(summary));
    }

    protected String getMessageAsText() {
        HtmlElement htmlElement = getMessageContentElement();
        String text = htmlElement.asText();
        return text;
    }
}
