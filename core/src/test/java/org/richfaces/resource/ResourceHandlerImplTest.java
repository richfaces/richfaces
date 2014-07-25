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
package org.richfaces.resource;

import static org.easymock.EasyMock.expect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.easymock.classextension.EasyMock;
import org.jboss.test.faces.AbstractFacesTest;
import org.jboss.test.faces.htmlunit.LocalWebClient;
import org.richfaces.application.DependencyInjector;
import org.richfaces.application.DependencyInjectorImpl;
import org.richfaces.application.Module;
import org.richfaces.application.ServicesFactory;
import org.richfaces.application.ServicesFactoryImpl;
import org.richfaces.application.Uptime;
import org.richfaces.application.configuration.ConfigurationService;
import org.richfaces.application.configuration.ConfigurationServiceImpl;
import org.richfaces.application.ServiceTracker;

import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
public class ResourceHandlerImplTest extends AbstractFacesTest {
    protected static final String ECHO_HEADER = "RichFaces-Echo";
    private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    private static final String RESOURCES_FOLDER_PATH = "resources/";
    private static final String TEST_RESOURCE_NAME = RESOURCES_FOLDER_PATH + "defaultResourceHandlerResource.js";
    protected static final Date currentTime;
    protected static final Date expires;
    protected static final Date lastModified;

    static {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        currentTime = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        lastModified = calendar.getTime();
        calendar.add(Calendar.DATE, 15);
        expires = calendar.getTime();
    }

    private class DisabledCache extends Cache {
        /**
         *
         */
        private static final long serialVersionUID = -1788422188914461469L;

        @Override
        protected boolean isCacheable(WebRequest request, WebResponse response) {
            return false;
        }
    }

    private LocalWebClient webClient;

    private void addClasspathResources() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File testResourceFile = new File(classLoader.getResource(TEST_RESOURCE_NAME).getFile());
        File testResourceFolder = testResourceFile.getParentFile();

        facesServer.addResourcesFromDirectory("/" + RESOURCES_FOLDER_PATH, testResourceFolder);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        addClasspathResources();
        webClient = new LocalWebClient(this.facesServer);

        // quick fix for https://sourceforge.net/tracker/?func=detail&aid=2821888&group_id=47038&atid=448266
        webClient.getOptions().setRedirectEnabled(false);

        // disable built-in cache
        webClient.setCache(new DisabledCache());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        webClient = null;
    }

    public void testReadCacheableResource() throws Exception {
        WebRequest webRequest = new WebRequest(new URL(
            "http://localhost/rfRes/org.richfaces.resource.CacheableResourceImpl.jsf"));

        webRequest.getAdditionalHeaders().put(ECHO_HEADER, "ping?");

        WebResponse webResponse = webClient.loadWebResponse(webRequest);

        assertEquals(HttpServletResponse.SC_OK, webResponse.getStatusCode());
        assertEquals(ResourceUtils.formatHttpDate(lastModified.getTime()), webResponse.getResponseHeaderValue("Last-Modified"));
        assertEquals(ResourceUtils.formatHttpDate(currentTime.getTime()), webResponse.getResponseHeaderValue("Date"));
        assertEquals(ResourceUtils.formatHttpDate(expires.getTime()), webResponse.getResponseHeaderValue("Expires"));
        assertEquals("max-age=1209600", webResponse.getResponseHeaderValue("Cache-Control"));
        assertTrue(webResponse.getResponseHeaderValue("Content-Type").startsWith("text/plain"));
        assertEquals("W/\"" + "ping?".length() + "-" + lastModified.getTime() + "\"",
            webResponse.getResponseHeaderValue("ETag"));
        assertNull(webResponse.getResponseHeaderValue("Pragma"));
        assertEquals("ping?", webResponse.getContentAsString("US-ASCII"));
        webRequest.getAdditionalHeaders().put(ECHO_HEADER, "pong");

        WebResponse cachedWebResponse = webClient.loadWebResponse(webRequest);

        assertEquals(HttpServletResponse.SC_OK, cachedWebResponse.getStatusCode());
        assertEquals("ping?", cachedWebResponse.getContentAsString("US-ASCII"));
        webRequest.getAdditionalHeaders().put(IF_MODIFIED_SINCE, ResourceUtils.formatHttpDate(currentTime));

        WebResponse cachedWebResponse2 = webClient.loadWebResponse(webRequest);

        assertEquals(HttpServletResponse.SC_NOT_MODIFIED, cachedWebResponse2.getStatusCode());
    }

    public void testReadNonCacheableResource() throws Exception {
        WebRequest webRequest = new WebRequest(new URL(
            "http://localhost/rfRes/org.richfaces.resource.NonCacheableResourceImpl.jsf"));

        webRequest.getAdditionalHeaders().put(ECHO_HEADER, "ping?");

        WebResponse webResponse = webClient.loadWebResponse(webRequest);

        assertEquals(HttpServletResponse.SC_OK, webResponse.getStatusCode());
        assertEquals(ResourceUtils.formatHttpDate(lastModified.getTime()), webResponse.getResponseHeaderValue("Last-Modified"));
        assertEquals(ResourceUtils.formatHttpDate(currentTime.getTime()), webResponse.getResponseHeaderValue("Date"));
        assertEquals("0", webResponse.getResponseHeaderValue("Expires"));
        assertEquals("max-age=0, no-store, no-cache", webResponse.getResponseHeaderValue("Cache-Control"));
        assertTrue(webResponse.getResponseHeaderValue("Content-Type").startsWith("text/plain"));
        assertNull(webResponse.getResponseHeaderValue("ETag"));
        assertEquals("no-cache", webResponse.getResponseHeaderValue("Pragma"));
        assertEquals("ping?", webResponse.getContentAsString("US-ASCII"));
        webRequest.getAdditionalHeaders().put(ECHO_HEADER, "pong");

        WebResponse cachedWebResponse = webClient.loadWebResponse(webRequest);

        assertEquals(HttpServletResponse.SC_OK, cachedWebResponse.getStatusCode());
        assertEquals("pong", cachedWebResponse.getContentAsString("US-ASCII"));
        webRequest.getAdditionalHeaders().put(IF_MODIFIED_SINCE, ResourceUtils.formatHttpDate(currentTime));

        WebResponse cachedWebResponse2 = webClient.loadWebResponse(webRequest);

        assertEquals(HttpServletResponse.SC_OK, cachedWebResponse2.getStatusCode());
    }

    public void testNonExistingResource() throws Exception {
        WebRequest emptyResourceNameSettings = new WebRequest(new URL("http://localhost/rfRes/x.jsf"));
        WebResponse emptyResourceNameResponse = webClient.loadWebResponse(emptyResourceNameSettings);

        assertEquals(HttpServletResponse.SC_NOT_FOUND, emptyResourceNameResponse.getStatusCode());
    }

    public void testDefaultMojarraResource() throws Exception {
        WebRequest mojarraWebRequest = new WebRequest(new URL(
            "http://localhost/javax.faces.resource/defaultResourceHandlerResource.js.jsf"));
        WebResponse mojarraResourceNameResponse = webClient.loadWebResponse(mojarraWebRequest);

        assertEquals(HttpServletResponse.SC_OK, mojarraResourceNameResponse.getStatusCode());
    }

    public void testMarkerFile() throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        assertNotNull(Class.forName("org.richfaces.resource.MarkerFileResourceImpl", true, contextClassLoader));
        assertNotNull(contextClassLoader
            .getResource("META-INF/org.richfaces.resource.MarkerFileResourceImpl.resource.properties"));

        WebRequest markerFileWebRequest = new WebRequest(new URL(
            "http://localhost/rfRes/org.richfaces.resource.MarkerFileResourceImpl.jsf"));
        WebResponse markerFileResponse = webClient.loadWebResponse(markerFileWebRequest);

        assertEquals(HttpServletResponse.SC_OK, markerFileResponse.getStatusCode());
        assertNotNull(Class.forName("org.richfaces.resource.NoMarkerFileResourceImpl", true, contextClassLoader));
        assertNull(contextClassLoader
            .getResource("META-INF/org.richfaces.resource.NoMarkerFileResourceImpl.resource.properties"));

        WebRequest noMarkerFileRequestSettings = new WebRequest(new URL(
            "http://localhost/rfRes/org.richfaces.resource.NoMarkerFileResourceImpl.jsf"));
        WebResponse noMarkerResponse = webClient.loadWebResponse(noMarkerFileRequestSettings);

        assertEquals(HttpServletResponse.SC_NOT_FOUND, noMarkerResponse.getStatusCode());
    }

    public void testStateHolder() throws Exception {
        setupFacesRequest();

        final ResourceCodec mockedCodec = EasyMock.createNiceMock(ResourceCodec.class);
        final org.richfaces.cache.Cache mockCache = EasyMock.createNiceMock(org.richfaces.cache.Cache.class);

        ResourceRequestData resourceCodecData = EasyMock.createMock(ResourceRequestData.class);
        expect(resourceCodecData.getResourceName()).andStubReturn("org.richfaces.resource.StateHolderResourceImpl");
        expect(resourceCodecData.getLibraryName()).andStubReturn(null);
        expect(resourceCodecData.getData()).andStubReturn("test text".getBytes());
        expect(resourceCodecData.getVersion()).andStubReturn(null);
        expect(resourceCodecData.getResourceKey()).andStubReturn("StateHolderResource.jsf?db=1");

        EasyMock.expect(mockedCodec.decodeResource(EasyMock.<FacesContext>notNull(), EasyMock.eq("StateHolderResource")))
            .andReturn(resourceCodecData);
        EasyMock.replay(mockedCodec, resourceCodecData, mockCache);

        ServicesFactoryImpl injector = new ServicesFactoryImpl();
        injector.init(Collections.<Module>singletonList(new Module() {
            public void configure(ServicesFactory injector) {
                injector.setInstance(ResourceCodec.class, mockedCodec);
                injector.setInstance(org.richfaces.cache.Cache.class, mockCache);
                injector.setInstance(Uptime.class, new Uptime());
                injector.setInstance(DependencyInjector.class, new DependencyInjectorImpl());
                injector.setInstance(ConfigurationService.class, new ConfigurationServiceImpl());
            }
        }));
        ServiceTracker.setFactory(injector);

        WebRequest webRequest = new WebRequest(new URL("http://localhost/rfRes/StateHolderResource.jsf?db=1"));
        WebResponse resourceResponse = webClient.loadWebResponse(webRequest);

        assertEquals(HttpServletResponse.SC_OK, resourceResponse.getStatusCode());
        assertEquals("test text", resourceResponse.getContentAsString("US-ASCII"));

        EasyMock.verify(mockedCodec, resourceCodecData, mockCache);
    }

    public void testVersionedResource() throws Exception {
        WebRequest webRequest = new WebRequest(new URL(
            "http://localhost/rfRes/org.richfaces.resource.VersionedResourceImpl.jsf"));
        WebResponse resourceResponse = webClient.loadWebResponse(webRequest);

        assertEquals(HttpServletResponse.SC_OK, resourceResponse.getStatusCode());
        webRequest = new WebRequest(new URL(
            "http://localhost/rfRes/org.richfaces.resource.VersionedResourceImpl.jsf?v=1_0_2"));
        resourceResponse = webClient.loadWebResponse(webRequest);
        assertEquals(HttpServletResponse.SC_OK, resourceResponse.getStatusCode());
        webRequest = new WebRequest(new URL(
            "http://localhost/rfRes/org.richfaces.resource.VersionedResourceImpl.jsf?v=1_0_3"));
        resourceResponse = webClient.loadWebResponse(webRequest);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, resourceResponse.getStatusCode());
    }

    public void testCompiledCssResource() throws Exception {
        String baseResourseURL = "http://localhost/rfRes/";
        String endResourceURL = ".jsf";
        String resourceName = null;
        List<String> resources = populateResourcesToCheck();
        for (String resource : resources) {
            resourceName = baseResourseURL + resource + endResourceURL;
            WebRequest webRequest = new WebRequest(new URL(resourceName));
            WebResponse resourceResponse = webClient.loadWebResponse(webRequest);
            assertEquals(resource, HttpServletResponse.SC_OK, resourceResponse.getStatusCode());
            String expected = readFileAsString(getResourceExpectedOutputFileName(resource));

            assertEquals(resource, unifyWhitespace(expected), unifyWhitespace(resourceResponse.getContentAsString()));
        }
    }

    public void testCreateResource() throws Exception {
        setupFacesRequest();

        ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();

        assertNotNull(resourceHandler.createResource("org.richfaces.resource.CacheableResourceImpl"));
        assertNotNull(resourceHandler.createResource("org.richfaces.resource.CacheableResourceImpl", ""));
        assertNotNull(resourceHandler.createResource("org.richfaces.resource.CacheableResourceImpl", null));
        assertNotNull(resourceHandler.createResource("defaultResourceHandlerResource.js"));
        assertNotNull(resourceHandler.createResource("libraryResource.js", "org.richfaces.resource.test"));
    }

    public void testLibraryExists() throws Exception {
        setupFacesRequest();

        ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();

        assertTrue(resourceHandler.libraryExists("org.richfaces.resource.test"));
    }

    private String unifyWhitespace(String source) {
        String result = source.replaceAll("(\n|\r)", "").replaceAll("\\t", " ").replaceAll(" {2,}", " ");

        String[] chars = new String[] { ",", "\\{", "\\}" };
        for (String c : chars) {
            result = result.replaceAll(" ?" + c + " ?", c);
        }

        return result;

    }

    private static String readFileAsString(String filePath) throws java.io.IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File testResourceFile = new File(classLoader.getResource(RESOURCES_FOLDER_PATH + "/" + filePath).getFile());
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(testResourceFile));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    private List<String> populateResourcesToCheck() {
        List<String> resources = new ArrayList<String>();
        resources.add("importedEL.ecss");
        resources.add("media.ecss");
        resources.add("fake.ecss");
        resources.add("fake2.ecss");
        resources.add("fontface.ecss");
        resources.add("full.ecss");
        resources.add("import.ecss");
        resources.add("media.ecss");
        resources.add("page.ecss");
        resources.add("resource.ecss");
        resources.add("selector.ecss");
        resources.add("skin.ecss");
        resources.add("charset.ecss");
        return resources;
    }

    private String getResourceExpectedOutputFileName(String name) {
        return name.replaceAll("ecss", "css");
    }
}