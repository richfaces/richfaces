package org.richfaces.javascript.client.message;

import static org.junit.Assert.*;

import org.ajax4jsf.javascript.JSFunction;
import org.jboss.test.qunit.Qunit;
import org.jboss.test.qunit.Qunit.Builder;
import org.junit.Rule;
import org.richfaces.javascript.Message;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MessageTestBase {

    @Rule
    public final Qunit qunit;

    public static final String COMPONENT = "form:component";

    public static final String MY_MESSAGE = "form:uiMessage";

    public static final String MESSAGE_INIT = "new RichFaces.ui.Message(\"" + MY_MESSAGE + "\", {forComponentId:\"" + COMPONENT + "\"})";

    public MessageTestBase() {
        this.qunit = createQunitPage().build();
    }

    public void setUpMessage(){
        qunit.runScript(MESSAGE_INIT);
    }

    protected Builder createQunitPage() {
        return Qunit.builder().emulate(BrowserVersion.FIREFOX_3_6).loadJsfResource("jquery.js").loadJsfResource("richfaces.js")
        .loadJsfResource("richfaces-event.js").loadJsfResource("richfaces-base-component.js").
        loadJsfResource("csv.js", "org.richfaces").loadJsfResource("message.js", "org.richfaces").content("<form id=\"form\" name=\"form\" method=\"post\" action=\"/client-test.jsf\" enctype=\"application/x-www-form-urlencoded\">\n" + 
        		"      <input type=\"hidden\" name=\"form\" value=\"form\"/>\n" + 
        		"      <input id=\"form:text\" type=\"text\" name=\"form:text\" value=\"fooValue\" onblur=\"form_3Atext_3Av(&quot;form:text&quot;,this,event)\"/>\n" + 
        		"      <span id=\"form:out\">\n" + 
        		"        fooValue\n" + 
        		"      </span><div id=\"foo\" ><ul id=\"" + MY_MESSAGE + "\">"+getMessageContent()+"</ul></div><input type=\"hidden\" name=\"javax.faces.ViewState\" id=\"javax.faces.ViewState\" value=\"4262028796446907996:-2607792463910755035\" autocomplete=\"off\"/>\n" 
        				+ "    </form>");
    }

    
    protected String getMessageContent() {        
        return "";
    }

    protected Object sendMessage() {
        JSFunction clientSideFunction = new JSFunction("RichFaces.csv." + getJavaScriptFunctionName(),COMPONENT,getErrorMessage());
        return qunit.runScript(clientSideFunction.toScript());

    }
    protected String getJavaScriptFunctionName() {
        return "sendMessage";
    }
    
    protected Message getErrorMessage() {
        return new Message(2,"error","script error");
    }

    protected HtmlElement getMessageContentElement() {
        HtmlPage page = qunit.getPage();
        HtmlElement htmlElement = page.getElementById(MY_MESSAGE);
        assertNotNull(htmlElement);
        return htmlElement;
    }

}
