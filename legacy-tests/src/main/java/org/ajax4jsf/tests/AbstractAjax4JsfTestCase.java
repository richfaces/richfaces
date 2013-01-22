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



package org.ajax4jsf.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.lang.reflect.Field;

import java.net.URL;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.context.AjaxContextImpl;
import org.ajax4jsf.context.ViewResources;
import org.ajax4jsf.io.parser.FastHtmlParser;
import org.ajax4jsf.renderkit.AjaxViewRootRenderer;
import org.ajax4jsf.renderkit.ChameleonRenderKitImpl;
import org.ajax4jsf.resource.InternetResource;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.ResourceBuilderImpl;
import org.ajax4jsf.resource.image.ImageInfo;
import org.ajax4jsf.tests.org.apache.shale.test.config.ConfigParser;
import org.ajax4jsf.webapp.BaseXMLFilter;
import org.ajax4jsf.webapp.HtmlParser;
import org.ajax4jsf.webapp.WebXml;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.MockApplication;
import org.apache.shale.test.mock.MockPrintWriter;
import org.apache.shale.test.mock.MockResponseWriter;
import org.apache.shale.test.mock.MockServletOutputStream;

import org.richfaces.VersionBean;
import org.richfaces.skin.SkinBean;
import org.richfaces.skin.SkinFactory;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebResponseImpl;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Base class for testing components and renderers in a4j framework
 * Simulate a4j environment for perform requests.
 * For concrete tests MUST be overriden setUp/tearDown methods, even if no actions
 * performed - it need for junit initializations.
 * All output html collected and can be checked by htmlunit
 * @author shura (latest modification by $Author: ishabalov $)
 * @version $Revision: 1.1.2.5 $ $Date: 2007/02/20 20:58:08 $
 *
 */
public class AbstractAjax4JsfTestCase extends AbstractJsfTestCase {
    private static final String HTTP_PREFIX = "http:";
    private static final String IMAGE_NOT_FOUND_OR_UNAVAILABLE_MESSAGE = "Image not found or unavailable";
    private static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource not found";
    @Deprecated
    protected final static String SLASHED_RESOURCE_URI_PREFIX = "/" + WebXml.RESOURCE_URI_PREFIX;
    protected AjaxContext ajaxContext = null;

    // Thread context class loader saved and restored after each test
    private ClassLoader threadContextClassLoader = null;
    private File tmpRoot = null;

    /**
     * Initialised instance of VCP render kit.
     */
    protected ChameleonRenderKitImpl vcpRenderKit = null;

    /**
     * <p>The htmlunit web client for this test case. </p>
     *
     */
    protected WebClient webClient = null;

    /**
     * Mock web connection for accept stored content of JSF encoding. For testing JavaScript code, all URL's for scripts must be
     * rregistered by {@link MockWebConnection#setResponse(java.net.URL, byte[], int, java.lang.String, java.lang.String, java.util.List)} method
     * By default, for unregistered pages return 404 - not found.
     */
    protected MockWebConnection webConnection = null;
    protected ELContext elContext;
    protected ExpressionFactory expressionFactory;
    protected WebXml webXml;

    /**
     * Instance of Mock {@link javax.faces.context.ResponseWriter} , created by setupResponseWriter
     */
    protected MockResponseWriter writer;

    /**
     * @param name
     */
    public AbstractAjax4JsfTestCase(String name) {
        super(name);

        // TODO Auto-generated constructor stub
    }

    public static final void evaluate(Condition condition) {
        String message = condition.getMessage();

        assertTrue(message, condition.isConditionTrue());
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.shale.test.base.AbstractJsfTestCase#setUp()
     */
    public void setUp() throws Exception {

        // This method MUST BE OVERRIDEN in any subclasses - since Junit see for it in class for call
        super.setUp();

        // Try to override default expression factory with our wrapper that supports enums (like Sun's)
        try {
            ExpressionFactory exprFact = new EnumSupportExpressionFactoryWrapper(application.getExpressionFactory());
            Class<? extends MockApplication> applicationClass = application.getClass();
            Field field = applicationClass.getDeclaredField("expressionFactory");

            field.setAccessible(true);
            field.set(application, exprFact);
            expressionFactory = application.getExpressionFactory();
        } catch (Exception e) {

            // e.printStackTrace();
            // We cannot set private field
        }

        // Setup FacesContext with necessary init parameters.
        this.servletContext.addInitParameter(SkinFactory.SKIN_PARAMETER, getSkinName());
        this.servletContext.addInitParameter("com.sun.faces.externalizeJavaScript", "true");

        // setup VCP renderKit, create renderers.
        RenderKitFactory vcpRenderKitFactory =
            (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

        vcpRenderKit = new ChameleonRenderKitImpl();
        vcpRenderKitFactory.addRenderKit("HTML_CHAMELEON", vcpRenderKit);
        renderKit.addRenderer(UIViewRoot.COMPONENT_FAMILY, UIViewRoot.COMPONENT_TYPE, new AjaxViewRootRenderer());

        // setup nessesary components.
        application.addComponent("javax.faces.ViewRoot", MockViewRoot.class.getName());

        // setup AjaxContext.
        ajaxContext = new AjaxContextImpl();
        request.setAttribute(AjaxContext.AJAX_CONTEXT_KEY, ajaxContext);

        // Setup ViewHandler / ViewRoot.
        application.setViewHandler(new MockViewHandler(application.getViewHandler()));

        UIViewRoot root = new MockViewRoot();

        root.setViewId("/viewId");
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        facesContext.setViewRoot(root);

        // Setup Skin avd version variables.
        servletContext.setAttribute("a4j", new VersionBean());
        servletContext.setAttribute("a4jSkin", new SkinBean());

        // Setup servlet context and testing web.xml
        request.setPathElements("/testContext", "/faces", "/vievId.jsf", "");

        File webRoot = new File(getClass().getResource("/WEB-INF/web.xml").getFile()).getParentFile().getParentFile();

        if (webRoot.exists()) {
            servletContext.setDocumentRoot(webRoot);
        } else {

            // Prepare WEB-ROOT in temp folder
            tmpRoot = File.createTempFile("TmpTestRoot" + new Random().nextInt(), null);
            tmpRoot.delete();
            tmpRoot.mkdir();
            servletContext.setDocumentRoot(tmpRoot);

            URL jarUrl = AbstractAjax4JsfTestCase.class.getProtectionDomain().getCodeSource().getLocation();
            ZipInputStream zis = new ZipInputStream(jarUrl.openStream());

            try {
                ZipEntry entry;
                byte[] buffer = new byte[8192];

                while ((entry = zis.getNextEntry()) != null) {
                    String name = entry.getName();

                    if (name.startsWith("WEB-INF/")) {
                        File out = new File(tmpRoot, name);

                        if (entry.isDirectory()) {
                            out.mkdirs();
                        } else {
                            out.getParentFile().mkdirs();

                            OutputStream os = new FileOutputStream(out);

                            try {
                                int count;

                                while ((count = zis.read(buffer)) > 0) {
                                    os.write(buffer, 0, count);
                                }
                            } finally {
                                os.close();
                            }

                            zis.closeEntry();
                        }
                    }
                }
            } catch (IOException e) {
                deleteRecursively(tmpRoot);

                throw e;
            }

            servletContext.setDocumentRoot(tmpRoot);
        }

        try {
            InternetResourceBuilder.getInstance().init();
        } catch (FacesException e) {
            InternetResourceBuilder.setInstance(null);
        }

        webXml = new WebXml();
        webXml.init(servletContext, "A4J");

        ConfigParser parser = new ConfigParser();

        parser.parse(parser.getPlatformURLs());

        Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/faces-config.xml");

        while (resources.hasMoreElements()) {
            parser.parse((URL) resources.nextElement());
        }

        externalContext = new MockExternalContext(externalContext);
        facesContext.setExternalContext(externalContext);
        elContext = facesContext.getELContext();
        expressionFactory = application.getExpressionFactory();
    }

    protected WebClient createWebClient() {
        WebClient client = new WebClient();

        // disable javascript for now as HtmlUnit doesn't treat prototype.js
        // well
        client.setJavaScriptEnabled(true);

        // Set dummy script engine to prevent NPEs from default HtmlUnit engine
        client.setJavaScriptEngine(new DummyScriptEngine(client));

        return client;
    }

    /**
     * This function checks is resource present. If so, resource returned in the form of InputStream, otherwise IOException throwed.
     *
     * @param path - path to specified resource
     *
     * @return InputStream of the resource, if resource present
     *
     * @throws IOException - if resource not available
     */
    protected InputStream getResourceIfPresent(String path) throws IOException {
        InternetResourceBuilder builder = ResourceBuilderImpl.getInstance();
        InternetResource resource = builder.getResource(path);

        if (resource != null) {
            String uri = HTTP_PREFIX + resource.getUri(facesContext, null);
            Page page = webClient.getPage(uri);

            if (page.getWebResponse().getStatusCode() == HttpServletResponse.SC_OK) {
                return page.getWebResponse().getContentAsStream();
            }
        }

        throw new IOException(RESOURCE_NOT_FOUND_MESSAGE + path);
    }

    /**
     * This function checks is image resource present. If so, image returned, otherwise IOException throwed.
     *
     * @param path - path to specified image resource
     *
     * @return ImageInfo of the image, if image resource present or null if method ImageInfo.check() return false;
     *
     * @throws IOException - if image not available or not exist
     */
    protected ImageInfo getImageResource(String path) throws IOException {
        ImageInfo info = new ImageInfo();

        try {
            info.setInput(getResourceIfPresent(path));
        } catch (IOException e) {
            throw new IOException(IMAGE_NOT_FOUND_OR_UNAVAILABLE_MESSAGE + path);
        }

        return info.check() ? info : null;
    }

    /**
     * This function return count of <script> elements on page and validate - if this script contains in Set of predefined attribute for this component
     *
     * @param view - page to validate
     * @param predefinedScripts - set of predefined scripts, should be presented on component
     *
     * @return count of valid <script> element
     * @throws Exception if script not available on page
     */
    protected Integer getCountValidScripts(HtmlPage view, Set<String> predefinedScripts,
            boolean isPageAvailabilityCheck)
            throws Exception {

        /*
         * List<HtmlScript> scripts = view.getDocumentHtmlElement()
         *       .getHtmlElementsByTagName(HTML.SCRIPT_ELEM);
         * int foundCount = 0;
         * for (Iterator<HtmlScript> it = scripts.iterator(); it.hasNext();) {
         *   HtmlScript item = it.next();
         *   String srcAttr = item.getSrcAttribute();
         *
         *   if (StringUtils.isNotBlank(srcAttr)) {
         *       boolean found = false;
         *       for (Iterator<String> srcIt = predefinedScripts.iterator(); srcIt
         *               .hasNext();) {
         *           String src = (String) srcIt.next();
         *
         *           found = srcAttr.contains(src);
         *           if (found) {
         *               foundCount++;
         *
         *               String uri = "http:" + srcAttr;
         *               Page page = webClient.getPage(uri);
         *               if (!(page != null && page.getWebResponse()
         *                       .getStatusCode() == HttpServletResponse.SC_OK)) {
         *                   throw new Exception("Page is not available " + uri);
         *
         *               }
         *               break;
         *           }
         *       }
         *   }
         * }
         * return foundCount;
         */
        int foundCount = 0;
        List<String> scriptSources = HtmlTestUtils.extractScriptSources(view);

        for (String javascript : predefinedScripts) {
            for (String script : scriptSources) {
                if (script.indexOf(javascript) >= 0) {
                    foundCount++;

                    String uri = HTTP_PREFIX + script;
                    Page page = webClient.getPage(uri);

                    if (isPageAvailabilityCheck) {
                        if (!(page != null && page.getWebResponse().getStatusCode() == HttpServletResponse.SC_OK)) {
                            throw new Exception("Component script " + javascript + " is not found in the response");
                        }
                    }

                    break;
                }
            }
        }

        return foundCount;
    }

    /**
     *
     */
    protected void setupWebClient() {
        ajaxContext = null;

        // setup webClient for got response content.
        webClient = createWebClient();
        webConnection = new MockWebConnection(webClient) {
            public WebResponse getResponse(WebRequestSettings settings) throws IOException {
                String resourceKey = WebXml.getInstance(facesContext).getFacesResourceKey(
                                         settings.getURL().getFile().substring(request.getContextPath().length()));

                if (resourceKey != null) {
                    InternetResourceBuilder resourceBuilder = InternetResourceBuilder.getInstance();
                    InternetResource resource = resourceBuilder.getResourceForKey(resourceKey);
                    Object resourceData = resourceBuilder.getResourceDataForKey(resourceKey);
                    final MockFacesResourceContext resourceContext = new MockFacesResourceContext(facesContext);

                    resourceContext.setResourceData(resourceData);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    response.setOutputStream(new MockServletOutputStream(baos));
                    resource.sendHeaders(resourceContext);
                    resource.send(resourceContext);

                    return new WebResponseImpl(new WebResponseData(new ByteArrayInputStream(baos.toByteArray()),
                            HttpServletResponse.SC_OK, "OK", resourceContext.getHeaders()), settings.getURL(),
                                settings.getSubmitMethod(), 0) {
                        public String getContentType() {
                            return resourceContext.getContentType();
                        }
                    };
                } else {
                    return super.getResponse(settings);
                }
            }
        };
        webClient.setWebConnection(webConnection);
        webConnection.setDefaultResponse("<html><body>not found</body></html>", 404, "NOT FOUND", "text/html");
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setThrowExceptionOnScriptError(false);
    }

    private void deleteRecursively(File file) {
        if (file != null) {
            String[] list = file.list();

            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    String name = list[i];
                    File f = new File(file, name);

                    if (f.isDirectory()) {
                        deleteRecursively(f);
                    } else {
                        f.delete();
                    }
                }
            }

            file.delete();
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.shale.test.base.AbstractJsfTestCase#tearDown()
     */
    public void tearDown() throws Exception {

        // This method MUST BE OVERRIDEN in any subclasses - since Junit see for it in class for call
        expressionFactory = null;
        elContext = null;
        super.tearDown();
        vcpRenderKit = null;

//      Thread.currentThread().setContextClassLoader(threadContextClassLoader);
//      threadContextClassLoader = null;
        webClient = null;
        webConnection = null;
        writer = null;
        SkinFactory.reset();
        InternetResourceBuilder.setInstance(null);
        deleteRecursively(tmpRoot);
    }

    // Protected configurations URL's

    /**
     * Hook method for setup current VCP skin name for test. default use "classic"
     * @return
     */
    protected String getSkinName() {
        return "DEFAULT";
    }

    /**
     * Hook method for load diffrernt frameworks for this test.
     * Can return URL's for jars with tested implementation.
     * @return
     */
    protected URL[] getImplementationUrls() {
        return new URL[0];
    }

    /**
     * Create component with given render kit and unique Id from ViewRoot.
     * @param type - component type.
     * @param clazz - class of component.
     * @param rendererType - name for renderer type ( can be null ).
     * @param rendererClazz - class of renderer in HTMP_BASIC renderkit. If not null, renderer will created and registered.
     * @param skinRendererClazz - class of renderer in Skin render-kit.
     * @return component instance.
     * @throws Exception
     */
    protected UIComponent createComponent(String type, String className, String rendererType, Class rendererClazz,
            Class skinRendererClazz) {
        UIComponent comp;

        try {
            comp = application.createComponent(type);
        } catch (FacesException e) {
            application.addComponent(type, className);
            comp = application.createComponent(type);
        }

        comp.setRendererType(rendererType);

        String family = comp.getFamily();

        if (null != rendererClazz) {
            Renderer renderer = renderKit.getRenderer(family, rendererType);

            if (null == renderer) {
                try {
                    renderer = (Renderer) rendererClazz.newInstance();
                } catch (InstantiationException e) {
                    assertTrue("Instantiation exception for create renderer " + rendererClazz.getName(), false);
                } catch (IllegalAccessException e) {
                    assertTrue("ILEGAL access exception for create renderer " + rendererClazz.getName(), false);
                }

                renderKit.addRenderer(family, rendererType, renderer);
            }
        }

        if (null != skinRendererClazz) {
            Renderer renderer = renderKit.getRenderer(family, rendererType);

            if (null == renderer) {
                try {
                    renderer = (Renderer) skinRendererClazz.newInstance();
                } catch (InstantiationException e) {
                    assertTrue("Instantiation exception for create renderer " + skinRendererClazz.getName(), false);
                } catch (IllegalAccessException e) {
                    assertTrue("ILEGAL access exception for create renderer " + skinRendererClazz.getName(), false);
                }

                vcpRenderKit.addRenderer(family, rendererType, renderer);
            }
        }

        comp.setId(facesContext.getViewRoot().createUniqueId());

        return comp;
    }

    /**
     * Render all children for given component.
     * @param component
     * @throws IOException
     */
    protected void renderChildren(FacesContext context, UIComponent component) throws IOException {
        if (component.getChildCount() > 0) {
            for (Iterator it = component.getChildren().iterator(); it.hasNext(); ) {
                UIComponent child = (UIComponent) it.next();

                renderChild(context, child);
            }
        }
    }

    /**
     * Render one component and it childrens
     * @param child
     * @throws IOException
     */
    protected void renderChild(FacesContext context, UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }

        child.encodeBegin(context);

        if (child.getRendersChildren()) {
            child.encodeChildren(context);
        } else {
            renderChildren(context, child);
        }

        child.encodeEnd(context);
    }

    /**
     * Render test view and parse to htmlunit page structure.
     * @return
     * @throws Exception
     */
    protected HtmlPage renderView() throws Exception {
        setupResponseWriter();

        // Emulate A4J web filter chaining like in real web application.
        TestXMLFilter filter = new TestXMLFilter();

        try {
            filter.processFilter(new FilterChain() {
                public void doFilter(ServletRequest request, ServletResponse response)
                        throws IOException, ServletException {
                    response.setContentType("text/html");
                    facesContext.setResponseWriter(
                        (MockResponseWriter) renderKit.createResponseWriter(response.getWriter(), "text/html", null));

                    ViewResources viewResources = new ViewResources();

                    viewResources.initialize(facesContext);

                    // viewResources.setStyleStrategy(InternetResourceBuilder.LOAD_ALL);
                    // viewResources.setScriptStrategy(InternetResourceBuilder.LOAD_ALL);
                    viewResources.setUseStdControlsSkinning(false);
                    viewResources.setUseStdControlsSkinningClasses(false);
                    renderChild(facesContext, facesContext.getViewRoot());
                    viewResources.processHeadResources(facesContext);

                    Object[] headEvents = (Object[]) viewResources.getHeadEvents();

                    request.setAttribute(AjaxContext.HEAD_EVENTS_PARAMETER, headEvents);
                    facesContext.setResponseWriter(writer);
                }
            }, request, response);
        } catch (Exception e) {
            throw e;
        }

        return processResponseWriter();
    }

    /**
     * Finish response processing by call {@link javax.faces.context.ResponseWriter#endDocument()} and parse
     * result to htmlunit {@link HtmlPage}
     * @return
     * @throws IOException
     * @throws Exception
     */
    protected HtmlPage processResponseWriter() throws IOException, Exception {
        writer.endDocument();

        return (HtmlPage) processResponse();
    }

    /**
     * Setup Faces {@link javax.faces.context.ResponseWriter} and prepare rendering ( startDocument ).
     * after this method, any jsf rendering methods can be performed, and results can be checked by {@link #processResponseWriter()}
     * @throws IOException
     */
    protected void setupResponseWriter() throws IOException {
        writer = (MockResponseWriter) renderKit.createResponseWriter(response.getWriter(), "text/html", null);
        facesContext.setResponseWriter(writer);
        writer.startDocument();
    }

    /**
     * Parse collected content of mock  response to Page instance, used for check rendered html.
     * @return
     * @throws Exception
     */
    protected Page processResponse() throws Exception {
        setupWebClient();

        URL page = new URL(HTTP_PREFIX + facesContext.getExternalContext().getRequestContextPath()
                           + facesContext.getViewRoot().getViewId());

        try {
            char[] content = ((MockPrintWriter) response.getWriter()).content();

            webConnection.setResponse(page, String.valueOf(content), response.getStatus(), "OK",
                                      response.getContentType(), Collections.EMPTY_LIST);
        } catch (IllegalStateException e) {
            byte[] content = ((MockServletOutputStream) response.getOutputStream()).content();

            webConnection.setResponse(page, content, response.getStatus(), "OK", response.getContentType(),
                                      Collections.EMPTY_LIST);
        }

        return webClient.getPage(page);
    }

    /**
     * Implements default XML filter to emulate A4J filter that invoked while
     * http request in web application.
     *
     * Needed to make sure that all processed (web page generation) like in real
     * web application. Additionally now all RF components styles and scripts generated
     * by A4J filter only, so we need the same in out unit tests.
     * Fast filter used now by default.
     * TODO: we can make it configurable like org.ajax4jsf.webapp.ConfigurableXMLFilter
     *
     * @author dmorozov
     */
    private class TestXMLFilter extends BaseXMLFilter {
        private HtmlParser parser = null;

        public TestXMLFilter() {}

        public void processFilter(FilterChain chain, HttpServletRequest request, final HttpServletResponse response)
                throws IOException, ServletException {
            super.doXmlFilter(chain, request, response);
        }

        @Override
        protected HtmlParser getParser(String mimetype, boolean isAjax, String viewId) {
            HtmlParser parser = this.parser;

            if (parser == null) {
                parser = new FastHtmlParser();
            }

            return parser;
        }

        /**
         * Peturn parser to pool
         * @param parser
         */
        @Override
        protected void reuseParser(HtmlParser parser) {
            this.parser = parser;
        }
    }
}
