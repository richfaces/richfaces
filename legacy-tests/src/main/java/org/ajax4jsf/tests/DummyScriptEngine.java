
/**
 *
 */
package org.ajax4jsf.tests;

import org.apache.commons.logging.Log;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

/**
 * Dummy implementation of script engine. It really does nuthin.
 * Always returns empty strings.
 * @author Maksim Kaszynski
 *
 */
@SuppressWarnings("serial")
public class DummyScriptEngine extends JavaScriptEngine {
    public DummyScriptEngine(WebClient webClient) {
        super(webClient);
    }

    @Override
    public Object callFunction(HtmlPage htmlPage, Function function, Context context, Scriptable scope,
                               Scriptable thisObject, Object[] args) {
        return false;
    }

    @Override
    public Script compile(HtmlPage htmlPage, String sourceCode, String sourceName, int startLine) {
        return new Script() {
            public Object exec(Context arg0, Scriptable arg1) {
                return false;
            }
        };
    }

    @Override
    public Script getCachedScript(WebResponse webResponse) {
        return null;
    }

    @Override
    protected Log getLog() {
        return null;
    }

    @Override
    public void initialize(WebWindow webWindow) {
        super.initialize(webWindow);
    }

    @Override
    public boolean isScriptRunning() {
        return false;
    }

    @Override
    public String preProcess(HtmlPage htmlPage, String sourceCode, String sourceName, HtmlElement htmlElement) {
        return "";
    }
}
