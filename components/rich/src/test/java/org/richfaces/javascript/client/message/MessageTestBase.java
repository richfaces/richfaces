package org.richfaces.javascript.client.message;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ajax4jsf.javascript.JSFunction;
import org.jboss.test.qunit.Qunit;
import org.jboss.test.qunit.Qunit.Builder;
import org.junit.Rule;
import org.richfaces.javascript.Message;

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
            .emulate(BrowserVersion.FIREFOX_24)
            .loadJsfResource("jquery.js", "org.richfaces")
            .loadJsfResource("richfaces.js", "org.richfaces")
            .loadJsfResource("richfaces-event.js", "org.richfaces")
            .loadJsfResource("richfaces-base-component.js", "org.richfaces")
            .loadJsfResource("richfaces-csv.js", "org.richfaces")
            .loadJsfResource("message.js", "org.richfaces")
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
