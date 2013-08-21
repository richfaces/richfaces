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
package org.ajax4jsf.component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.richfaces.javascript.JSFunction;
import org.richfaces.javascript.JSFunctionDefinition;
import org.richfaces.javascript.JSReference;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.UserResourceRenderer2;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.ResourceNotFoundException;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.sun.facelets.Facelet;
import com.sun.facelets.FaceletFactory;
import com.sun.facelets.compiler.Compiler;
import com.sun.facelets.compiler.SAXCompiler;
import com.sun.facelets.impl.DefaultFaceletFactory;
import com.sun.facelets.impl.ResourceResolver;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public abstract class AbstractQueueComponentTest extends AbstractAjax4JsfTestCase {
    private static final String AJAX_SUBMIT = "ajaxSubmit";
    private static final String COMPONENT_TYPE = AjaxSubmitFunctionComponent.class.getName();
    public static final int DEFAULT_REQUEST_TIME = 1000;
    private static final String SIMULATION_SCRIPT_NAME = "org/ajax4jsf/component/simulation.js";
    private static final Compiler compiler = new SAXCompiler();
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
            System.out.println(s);
        }
    };

    static {
        try {
            systemOut.defineProperty("println",
                    new FunctionObject(null, systemOut.getClass().getMethod("println", String.class),
                            systemOut), ScriptableObject.READONLY);
        } catch (SecurityException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    protected HtmlPage page;

    public AbstractQueueComponentTest(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        InternetResourceBuilder resourceBuilder = InternetResourceBuilder.getInstance();

        try {
            resourceBuilder.getResource(SIMULATION_SCRIPT_NAME);
        } catch (ResourceNotFoundException e) {
            resourceBuilder.createResource(null, SIMULATION_SCRIPT_NAME);
        }

        UIViewRoot viewRoot = facesContext.getViewRoot();
        UIResource resource;
        UIComponent form = application.createComponent(UIForm.COMPONENT_TYPE);

        viewRoot.getChildren().add(form);

        final UIComponent commandButton = application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);

        form.getChildren().add(commandButton);
        facesContext.getRenderKit().addRenderer(COMPONENT_TYPE, COMPONENT_TYPE,
                new AjaxSubmitFunctionResourceRenderer());
        form.getChildren().add(new AjaxSubmitFunctionComponent());
        resource = (UIResource) application.createComponent("org.ajax4jsf.LoadScript");
        resource.setSrc("resource:///" + SIMULATION_SCRIPT_NAME);
        viewRoot.getChildren().add(resource);
    }

    @Override
    public void tearDown() throws Exception {
        this.page = null;
        super.tearDown();
    }

    protected ParametersBuilder createAjaxParameters() {
        return new ParametersBuilder();
    }

    protected void checkRequestData(RequestData requestData, String data, double startTime, double endTime,
            boolean aborted) {
        assertEquals("Data check failed for " + requestData, data, requestData.getData());
        assertEquals("Start time check failed for " + requestData, startTime, requestData.getStartTime());
        assertEquals("End time check failed for " + requestData, endTime, requestData.getEndTime());
        assertEquals("Aborted check failed for " + requestData, aborted, requestData.isAborted());
    }

    /**
     * Execute simulated ajax request starting on given time and having data and paramaters passed as arguments.
     * For simulated requests defaut value of data is id of the element firing request and default request time
     * is 1000
     *
     * @param time
     * @param data
     * @param builder
     */
    protected void ajax(int time, String data, ParametersBuilder builder) {
        JSFunction function = new JSFunction("simulationContext.ajax", time, data, builder.getParameters());

        page.executeJavaScript(function.toScript());
    }

    protected void executeOnTime(int time, String expression) {
        JSFunction function = new JSFunction("simulationContext.executeOnTime", time,
                new JSFunctionDefinition().addToBody(expression));

        page.executeJavaScript(function.toScript());
    }

    private String buildClickExpression(String id) {
        return "document.getElementById('" + id + "').click()";
    }

    protected void clickOnTime(int time, String id) {
        JSFunction function = new JSFunction("simulationContext.executeOnTime", time,
                new JSFunctionDefinition().addToBody(buildClickExpression(id)));

        page.executeJavaScript(function.toScript());
    }

    protected String getRootContextPath() {
        return this.getClass().getPackage().getName().replace('.', '/');
    }

    protected ResourceResolver createResourceResolver() {
        return new ResourceResolver() {
            public URL resolveUrl(String path) {
                return Thread.currentThread().getContextClassLoader().getResource(getRootContextPath() + path);
            }
        };
    }

    protected void buildView(String viewId) throws IOException {
        FaceletFactory factory = new DefaultFaceletFactory(compiler, createResourceResolver());

        FaceletFactory.setInstance(factory);

        FaceletFactory f = FaceletFactory.getInstance();
        Facelet at = f.getFacelet(viewId);
        UIViewRoot root = facesContext.getViewRoot();

        root.setViewId(viewId);
        at.apply(facesContext, root);
    }

    protected void preRenderView() throws Exception {
        StringBuilder builder = new StringBuilder("<script type='text/javascript'>");

        builder.append("DEFAULT_REQUEST_TIME = " + DEFAULT_REQUEST_TIME + ";");
        builder.append("window.simulationContext = new SimulationContext(");
        builder.append(AJAX_SUBMIT);
        builder.append(");</script>");

        HtmlOutputText text = new HtmlOutputText();

        text.setEscape(false);
        text.setValue(builder.toString());

        List<UIComponent> childList = facesContext.getViewRoot().getChildren();

        childList.add(childList.size(), text);
    }

    protected void postRenderView() throws Exception {
        ScriptableObject scriptableObject =
                (ScriptableObject) this.page.executeJavaScript("window.LOG").getJavaScriptResult();

        scriptableObject.defineProperty("out", systemOut, ScriptableObject.READONLY);
    }

    protected HtmlPage renderView(String viewId) throws Exception {
        buildView(viewId);
        preRenderView();
        this.page = super.renderView();
        postRenderView();

        return this.page;
    }

    @Override
    protected HtmlPage renderView() throws Exception {
        preRenderView();
        this.page = super.renderView();
        postRenderView();

        return this.page;
    }

    protected void click(String id) {
        executeJavaScript(buildClickExpression(id));
    }

    protected Object executeJavaScript(String expression) {
        return page.executeJavaScript(expression).getJavaScriptResult();
    }

    protected void executeTimer() {
        page.executeJavaScript("Timer.execute();");
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

            Double startTime = (Double) object.get("startTime", object);
            Double endTime = (Double) object.get("endTime", object);
            Object aborted = object.get("aborted", object);
            boolean abortedBoolean = aborted instanceof Boolean && (Boolean) aborted;

            result.addData(new RequestData(data, startTime, endTime, abortedBoolean));
        }

        scriptResult = page.executeJavaScript("Timer.currentTime");
        result.setCurrentTime((Double) scriptResult.getJavaScriptResult());

        return result;
    }

    @Override
    protected void setupWebClient() {
        super.setupWebClient();
        webClient.setJavaScriptEngine(new JavaScriptEngine(webClient));
        webClient.setScriptPreProcessor(new UnescapingScriptPreprocessor());
        webClient.setThrowExceptionOnScriptError(true);
        webClient.setAlertHandler(new AlertHandler() {
            public void handleAlert(Page page, String message) {
                fail(message);
            }
        });
    }

    public final static class AjaxSubmitFunctionComponent extends UIComponentBase {
        @Override
        public String getRendererType() {
            return COMPONENT_TYPE;
        }

        @Override
        public String getFamily() {
            return COMPONENT_TYPE;
        }
    }

    private final static class AjaxSubmitFunctionResourceRenderer extends Renderer implements UserResourceRenderer2 {
        public void encodeToHead(FacesContext facesContext, UIComponent component) throws IOException {
            JSFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(component, facesContext);
            Map<String, Object> options = AjaxRendererUtils.buildEventOptions(facesContext, component, true);

            options.put("requestDelay", new JSReference("options.requestDelay"));
            options.put("similarityGroupingId",
                    new JSReference("options.similarityGroupingId || '" + component.getClientId(facesContext)
                            + "'"));
            options.put("data", new JSReference("data"));
            options.put("requestTime", new JSReference("options.requestTime"));
            options.put("timeout", new JSReference("options.timeout"));
            options.put("eventsQueue", new JSReference("options.eventsQueue"));
            options.put("implicitEventsQueue", new JSReference("options.implicitEventsQueue"));
            options.put("ignoreDupResponses", new JSReference("options.ignoreDupResponses"));
            ajaxFunction.addParameter(options);

            ResponseWriter responseWriter = facesContext.getResponseWriter();

            responseWriter.startElement(HTML.SCRIPT_ELEM, component);
            responseWriter.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
            responseWriter.writeText("var " + AJAX_SUBMIT + " = function(data, options) {" + ajaxFunction.toScript()
                    + "};", null);
            responseWriter.endElement(HTML.SCRIPT_ELEM);
        }
    }

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
         * @param value
         * @return
         */
        public ParametersBuilder requestDelay(double value) {
            return new ParametersBuilder(this.parameters).put("requestDelay", value);
        }

        /**
         * Sets value of similarityGroupingId parameter
         * @param id
         * @return
         */
        public ParametersBuilder similarityGroupingId(Object id) {
            return new ParametersBuilder(this.parameters).put("similarityGroupingId", id);
        }

        /**
         * Defines how long this request will be executed on server
         * @param value
         * @return
         */
        public ParametersBuilder requestTime(double value) {
            return new ParametersBuilder(this.parameters).put("requestTime", value);
        }

        /**
         * Sets value of timeout parameter
         * @param value
         * @return
         */
        public ParametersBuilder timeout(double value) {
            return new ParametersBuilder(this.parameters).put("timeout", value);
        }

        /**
         * Sets value of eventsQueue parameter
         * @param name
         * @return
         */
        public ParametersBuilder eventsQueue(String name) {
            return new ParametersBuilder(this.parameters).put("eventsQueue", name);
        }

        /**
         * Sets value of implicitEventsQueue parameter
         * @param name
         * @return
         */
        public ParametersBuilder implicitEventsQueue(String name) {
            return new ParametersBuilder(this.parameters).put("implicitEventsQueue", name);
        }

        /**
         * Sets value of ignoreDupResponses parameter
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

    public String preProcess(HtmlPage htmlPage, String sourceCode, String sourceName, HtmlElement htmlElement) {
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
