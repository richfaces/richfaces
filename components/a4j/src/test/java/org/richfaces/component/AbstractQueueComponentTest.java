/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.component;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.UniqueTag;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.jboss.test.faces.ApplicationServer;
import org.jboss.test.faces.htmlunit.HtmlUnitEnvironment;
import org.junit.After;
import org.junit.Before;
import org.richfaces.CustomizedHtmlUnitEnvironment;

import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.WindowProxy;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public abstract class AbstractQueueComponentTest {
    private static final String AJAX_SUBMIT = "ajaxSubmit";
    public static final int DEFAULT_REQUEST_TIME = 1000;
    private static final String SIMULATION_SCRIPT_NAME = "simulation.js";
    private static final String QUEUEAJAX_SCRIPT_NAME = "queue-ajax.js";
    private static final ScriptableObject systemOut = new ScriptableObject() {
        /**
         *
         */
        private static final long serialVersionUID = -8574162538513136625L;

        @Override
        public String getClassName() {
            return "systemOut";
        }

        @SuppressWarnings("unused")
        public void println(String s) {
            System.out.println("Client log: " + s);
        }
    };

    static {
        try {
            systemOut.defineProperty("println", new FunctionObject(null, systemOut.getClass()
                .getMethod("println", String.class), systemOut), ScriptableObject.READONLY);
        } catch (SecurityException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    protected HtmlPage page;
    protected HtmlUnitEnvironment facesEnvironment;
    private StringBuilder loggedJavaScript = new StringBuilder();

    @Before
    public void setUp() throws Exception {
        facesEnvironment = new CustomizedHtmlUnitEnvironment();

        ApplicationServer facesServer = facesEnvironment.getServer();
        facesServer.addResource("/resources/" + SIMULATION_SCRIPT_NAME, "org/ajax4jsf/component/" + SIMULATION_SCRIPT_NAME);
        facesServer.addResource("/resources/" + QUEUEAJAX_SCRIPT_NAME, "org/ajax4jsf/component/" + QUEUEAJAX_SCRIPT_NAME);
        facesServer.addResource("/test.xhtml", "org/ajax4jsf/component/test.xhtml");

        facesEnvironment.start();
    }

    @After
    public void tearDown() throws Exception {
        this.page = null;
        this.facesEnvironment.release();
        this.facesEnvironment = null;
    }

    protected ParametersBuilder createAjaxParameters() {
        return new ParametersBuilder();
    }

    protected void checkRequestData(RequestData requestData, String data, double startTime, double endTime, boolean aborted) {
        assertEquals("Data check failed for " + requestData, data, requestData.getData());
        assertEquals("Start time check failed for " + requestData, startTime, requestData.getStartTime(), 0.01);
        assertEquals("End time check failed for " + requestData, endTime, requestData.getEndTime(), 0.01);
        assertEquals("Aborted check failed for " + requestData, aborted, requestData.isAborted());
    }

    protected HtmlPage renderView(String url) throws Exception {
        page = facesEnvironment.getPage(url);
        postRenderView();
        return page;
    }

    /**
     * Execute simulated ajax request starting on given time and having data and paramaters passed as arguments. For simulated
     * requests defaut value of data is id of the element firing request and default request time is 1000
     *
     * @param time
     * @param data
     * @param builder
     */
    protected void ajax(int time, String data, ParametersBuilder builder) {
        JSFunction function = new JSFunction("simulationContext.ajax", time, data, builder.getParameters());
        executeJavaScriptLogged(function.toScript());
    }

    protected void executeOnTime(int time, String expression) {
        JSFunction function = new JSFunction("simulationContext.executeOnTime", time,
            new JSFunctionDefinition().addToBody(expression));

        executeJavaScriptLogged(function.toScript());
    }

    private String buildClickExpression(String id) {
        return "document.getElementById('" + id + "').click()";
    }

    protected void clickOnTime(int time, String id) {
        JSFunction function = new JSFunction("simulationContext.executeOnTime", time,
            new JSFunctionDefinition().addToBody(buildClickExpression(id)));

        executeJavaScriptLogged(function.toScript());
    }

    protected String getRootContextPath() {
        return this.getClass().getPackage().getName().replace('.', '/');
    }

    @SuppressWarnings("deprecation") // deprecated in JSF 2.2
    protected javax.faces.view.facelets.ResourceResolver createResourceResolver() {
        return new javax.faces.view.facelets.ResourceResolver() {
            public URL resolveUrl(String path) {
                return Thread.currentThread().getContextClassLoader().getResource(getRootContextPath() + path);
            }
        };
    }

    protected void postRenderView() throws Exception {
        WindowProxy scriptableObject = (WindowProxy) page.executeJavaScript("window").getJavaScriptResult();
        scriptableObject.getDelegee().defineProperty("sysOut", systemOut, ScriptableObject.READONLY);
    }

    protected void click(String id) {
        executeJavaScriptLogged(buildClickExpression(id));
    }

    protected Object executeJavaScriptLogged(String expression) {
        if (loggedJavaScript.length() != 0) {
            loggedJavaScript.append(System.getProperty("line.separator"));
        }
        loggedJavaScript.append(expression);
        if (!expression.endsWith(";")) {
            loggedJavaScript.append(";");
        }

        return page.executeJavaScript(expression).getJavaScriptResult();
    }

    protected Object executeJavaScript(String expression) {
        return page.executeJavaScript(expression).getJavaScriptResult();
    }

    protected void executeTimer() {
        executeJavaScriptLogged("Timer.execute();");
    }

    protected TestsResult getTestsResult() {
        TestsResult result = new TestsResult();

        executeTimer();

        ScriptResult scriptResult = page.executeJavaScript("window.simulationContext.results");
        NativeArray array = (NativeArray) scriptResult.getJavaScriptResult();

        for (int i = 0; i < array.getLength(); i++) {
            NativeObject object = (NativeObject) array.get(i, array);
            String data = null;
            Object dataObject = object.get("data", object);

            if (!(dataObject instanceof Undefined)) {
                data = (String) dataObject;
            }

            Object startTimeObject = object.get("startTime", object);
            Double startTime =  startTimeObject instanceof Double ? (Double) startTimeObject : Double.NaN;
            Object endTimeObject = object.get("endTime", object);
            Double endTime =  endTimeObject instanceof Double ? (Double) endTimeObject : Double.NaN;
            Object aborted = object.get("aborted", object);
            boolean abortedBoolean = aborted instanceof Boolean && (Boolean) aborted;

            result.addData(new RequestData(data, startTime, endTime, abortedBoolean));
        }

        scriptResult = page.executeJavaScript("Timer.currentTime");
        result.setCurrentTime((Double) scriptResult.getJavaScriptResult());

        System.out.println();
        System.out.println("Logged Javascript statements:");
        System.out.println("*****************************");
        System.out.println(loggedJavaScript.toString());
        System.out.println("*****************************");
        loggedJavaScript.setLength(0);

        System.out.println("Test execution result:");
        System.out.println(result);
        System.out.println();

        return result;
    }

    // @Override
    // protected void setupWebClient() {
    // super.setupWebClient();
    // webClient.setJavaScriptEngine(new JavaScriptEngine(webClient));
    // webClient.setScriptPreProcessor(new UnescapingScriptPreprocessor());
    // webClient.setThrowExceptionOnScriptError(true);
    // webClient.setAlertHandler(new AlertHandler() {
    // public void handleAlert(Page page, String message) {
    // fail(message);
    // }
    // });
    // }

    protected static final class ParametersBuilder {
        private Map<String, Object> parameters;

        private ParametersBuilder() {
            this.parameters = new HashMap<String, Object>();
        }

        private ParametersBuilder(Map<String, Object> parameters) {
            this.parameters = new HashMap<String, Object>(parameters);
        }

        private ParametersBuilder put(String key, Object value) {
            this.parameters.put(key, value);

            return this;
        }

        protected Map<String, Object> getParameters() {
            return parameters;
        }

        /**
         * Sets value of requestDelay parameter
         *
         * @param value
         * @return
         */
        public ParametersBuilder requestDelay(double value) {
            return new ParametersBuilder(this.parameters).put("requestDelay", value);
        }

        /**
         * Sets value of similarityGroupingId parameter
         *
         * @param id
         * @return
         */
        public ParametersBuilder similarityGroupingId(Object id) {
            return new ParametersBuilder(this.parameters).put("similarityGroupingId", id);
        }

        /**
         * Defines how long this request will be executed on server
         *
         * @param value
         * @return
         */
        public ParametersBuilder requestTime(double value) {
            return new ParametersBuilder(this.parameters).put("requestTime", value);
        }

        /**
         * Sets value of timeout parameter
         *
         * @param value
         * @return
         */
        public ParametersBuilder timeout(double value) {
            return new ParametersBuilder(this.parameters).put("timeout", value);
        }

        /**
         * Sets value of eventsQueue parameter
         *
         * @param name
         * @return
         */
        public ParametersBuilder eventsQueue(String name) {
            return new ParametersBuilder(this.parameters).put("eventsQueue", name);
        }

        /**
         * Sets value of implicitEventsQueue parameter
         *
         * @param name
         * @return
         */
        public ParametersBuilder implicitEventsQueue(String name) {
            return new ParametersBuilder(this.parameters).put("implicitEventsQueue", name);
        }

        /**
         * Sets value of ignoreDupResponses parameter
         *
         * @param value
         * @return
         */
        public ParametersBuilder ignoreDupResponses(boolean value) {
            return new ParametersBuilder(this.parameters).put("ignoreDupResponses", value);
        }
    }

    protected static final class RequestData {
        private boolean aborted;
        private String data;
        private double endTime;
        private double startTime;

        public RequestData(String data, double startTime, double endTime, boolean aborted) {
            super();
            this.data = data;
            this.startTime = startTime;
            this.endTime = endTime;
            this.aborted = aborted;
        }

        public boolean isAborted() {
            return aborted;
        }

        public double getStartTime() {
            return startTime;
        }

        public double getEndTime() {
            return endTime;
        }

        public String getData() {
            return data;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            builder.append("data: ");
            builder.append(data);
            builder.append(", ");
            builder.append("startTime: ");
            builder.append(startTime);
            builder.append(", ");
            builder.append("endTime: ");
            builder.append(endTime);

            if (isAborted()) {
                builder.append(", aborted: ");
                builder.append(true);
            }

            return builder.toString();
        }
    }

    protected static final class TestsResult {
        private List<RequestData> dataList = new ArrayList<RequestData>();
        private double currentTime;

        public void addData(RequestData data) {
            this.dataList.add(data);
        }

        public List<RequestData> getDataList() {
            return dataList;
        }

        public void setCurrentTime(double number) {
            this.currentTime = number;
        }

        public double getCurrentTime() {
            return currentTime;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            builder.append("[\n");

            for (RequestData data : dataList) {
                builder.append("   ");
                builder.append(data);
                builder.append("\n");
            }

            builder.append("]\n");
            builder.append("Current time: " + this.currentTime);

            return builder.toString();
        }
    }

    ;
}

class UnescapingScriptPreprocessor implements ScriptPreProcessor {
    private static final Map<String, String> ENTITIES_MAP = new HashMap<String, String>();
    private static final Pattern ENTITIES_PATTERN;

    static {
        ENTITIES_MAP.put("&apos;", "\"");
        ENTITIES_MAP.put("&quot;", "'");
        ENTITIES_MAP.put("&amp;", "&");
        ENTITIES_MAP.put("&lt;", "<");
        ENTITIES_MAP.put("&gt;", ">");
    }

    static {
        StringBuilder sb = new StringBuilder();

        for (String entity : ENTITIES_MAP.keySet()) {
            if (sb.length() != 0) {
                sb.append('|');
            }

            sb.append(Pattern.quote(entity));
        }

        ENTITIES_PATTERN = Pattern.compile("(" + sb.toString() + ")");
    }

    public String preProcess(HtmlPage htmlPage, String sourceCode, String sourceName, int lineNumber, HtmlElement htmlElement) {
        if (sourceName != null && !sourceName.startsWith("http:/")) {
            Matcher m = ENTITIES_PATTERN.matcher(sourceCode);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String entity = m.group(1);

                m.appendReplacement(sb, ENTITIES_MAP.get(entity));
            }

            m.appendTail(sb);

            return sb.toString();
        } else {
            return sourceCode;
        }
    }
}
