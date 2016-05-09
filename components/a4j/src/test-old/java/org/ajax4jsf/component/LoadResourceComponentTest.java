/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.ajax4jsf.renderkit.HeaderResourceProducer2;
import org.ajax4jsf.renderkit.ProducerContext;
import org.ajax4jsf.renderkit.RendererBase;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.resource.InternetResource;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.ResourceNotFoundException;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.richfaces.javascript.AjaxScript;

import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public class LoadResourceComponentTest extends AbstractAjax4JsfTestCase {
    private String componentJs;
    private String componentXcss;
    private String userJs;
    private String userXcss;

    /**
     * @param name
     */
    public LoadResourceComponentTest(String name) {
        super(name);
    }

    private void registerResource(String path) {
        InternetResourceBuilder resourceBuilder = InternetResourceBuilder.getInstance();

        try {
            resourceBuilder.getResource(path);
        } catch (ResourceNotFoundException e) {
            resourceBuilder.createResource(null, path);
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        renderKit.addRenderer(ResourceDependentComponent.COMPONENT_FAMILY, ResourceDependentComponent.COMPONENT_TYPE,
                new ResourceDependentComponentRenderer());

        String resourcePackage = getClass().getPackage().getName().replace('.', '/') + "/";

        componentJs = resourcePackage + "component.js";
        componentXcss = resourcePackage + "component.xcss";
        userJs = resourcePackage + "user.js";
        userXcss = resourcePackage + "user.xcss";
        registerResource(componentJs);
        registerResource(userJs);
        registerResource(componentXcss);
        registerResource(userXcss);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        componentJs = null;
        userJs = null;
        componentXcss = null;
        userXcss = null;
    }

    private UIResource createLoadScriptComponent() {
        return (UIResource) application.createComponent("org.ajax4jsf.LoadScript");
    }

    private UIResource createLoadStyleComponent() {
        return (UIResource) application.createComponent("org.ajax4jsf.LoadStyle");
    }

    private List<HtmlScript> selectScriptsBySrc(HtmlPage page, String src) throws Exception {
        return Collections.checkedList(page.getByXPath("//script[@src[contains(string(.), '" + src + "')]]"),
                HtmlScript.class);
    }

    private List<HtmlLink> selectStylesByHhref(HtmlPage page, String src) throws Exception {
        return Collections.checkedList(
                page.getByXPath(
                        "//link[@type = 'text/css'][@rel = 'stylesheet'][@href[contains(string(.), '" + src
                                + "')]]"), HtmlLink.class);
    }

    protected UIResource createAndAddScriptResource(Object src) {
        UIResource scriptComponent = createLoadScriptComponent();

        scriptComponent.setSrc(src);
        facesContext.getViewRoot().getChildren().add(scriptComponent);

        return scriptComponent;
    }

    protected UIResource createAndAddStyleResource(Object src) {
        UIResource styleComponent = createLoadStyleComponent();

        styleComponent.setSrc(src);
        facesContext.getViewRoot().getChildren().add(styleComponent);

        return styleComponent;
    }

    public void testRenderScript() throws Exception {
        createAndAddScriptResource(RESOURCE_URI_PREFIX + userJs);
        createAndAddScriptResource(RESOURCE_URI_PREFIX + componentJs).setRendered(false);

        HtmlPage page = renderView();
        List<HtmlScript> scripts;

        scripts = selectScriptsBySrc(page, userJs);
        assertEquals(1, scripts.size());
        scripts = selectScriptsBySrc(page, componentJs);
        assertEquals(0, scripts.size());
    }

    public void testRenderScriptResource() throws Exception {
        InternetResourceBuilder resourceBuilder = InternetResourceBuilder.getInstance();

        createAndAddScriptResource(resourceBuilder.getResourceForKey(userJs));
        createAndAddScriptResource(resourceBuilder.getResourceForKey(componentJs)).setRendered(false);

        HtmlPage page = renderView();
        List<HtmlScript> scripts;

        scripts = selectScriptsBySrc(page, userJs);
        assertEquals(1, scripts.size());
        scripts = selectScriptsBySrc(page, componentJs);
        assertEquals(1, scripts.size());
    }

    public void testRenderStyle() throws Exception {
        createAndAddStyleResource(RESOURCE_URI_PREFIX + userXcss);
        createAndAddStyleResource(RESOURCE_URI_PREFIX + componentXcss).setRendered(false);

        HtmlPage page = renderView();
        List<HtmlLink> styles;
        HtmlLink userLink;

        styles = selectStylesByHhref(page, userXcss);
        assertEquals(1, styles.size());
        userLink = styles.get(0);
        assertEquals("user", userLink.getClassAttribute());
        assertEquals("", userLink.getMediaAttribute());
        styles = selectStylesByHhref(page, componentXcss);
        assertEquals(0, styles.size());
    }

    public void testRenderStyleResource() throws Exception {
        InternetResourceBuilder resourceBuilder = InternetResourceBuilder.getInstance();

        createAndAddStyleResource(resourceBuilder.getResourceForKey(userXcss));
        createAndAddStyleResource(resourceBuilder.getResourceForKey(componentXcss)).setRendered(false);

        UIResource styleComponent = createLoadStyleComponent();

        styleComponent.setSrc(InternetResourceBuilder.getInstance().getResourceForKey(userXcss));
        facesContext.getViewRoot().getChildren().add(styleComponent);

        HtmlPage page = renderView();
        List<HtmlLink> styles;
        HtmlLink userLink;

        styles = selectStylesByHhref(page, userXcss);
        assertEquals(1, styles.size());
        userLink = styles.get(0);
        assertEquals("user", userLink.getClassAttribute());
        assertEquals("", userLink.getMediaAttribute());
        styles = selectStylesByHhref(page, componentXcss);
        assertEquals(1, styles.size());
        userLink = styles.get(0);
        assertEquals("user", userLink.getClassAttribute());
        assertEquals("", userLink.getMediaAttribute());
    }

    public void testRenderMedia() throws Exception {
        createAndAddStyleResource(RESOURCE_URI_PREFIX + userXcss).getAttributes().put(HTML.media_ATTRIBUTE,
                "screen print");

        HtmlPage page = renderView();
        List<HtmlLink> styles = selectStylesByHhref(page, userXcss);

        assertEquals(1, styles.size());

        HtmlLink userLink = styles.get(0);

        assertEquals("user", userLink.getClassAttribute());
        assertEquals("screen print", userLink.getMediaAttribute());
    }

    public void testRenderOverridenScript() throws Exception {
        List<UIComponent> childrenList = facesContext.getViewRoot().getChildren();
        UIResource userJsComponent = createLoadScriptComponent();

        userJsComponent.setSrc(RESOURCE_URI_PREFIX + userJs);
        childrenList.add(userJsComponent);

        InternetResourceBuilder resourceBuilder = InternetResourceBuilder.getInstance();

        childrenList.add(new ResourceDependentComponent(true, resourceBuilder.getResourceForKey(componentJs),
                resourceBuilder.getResourceForKey(userJs)));

        UIResource componentJsComponent = createLoadScriptComponent();

        componentJsComponent.setSrc(RESOURCE_URI_PREFIX + componentJs);
        childrenList.add(componentJsComponent);

        HtmlPage page = renderView();
        List<HtmlScript> userScripts = selectScriptsBySrc(page, userJs);

        assertEquals(1, userScripts.size());

        List<HtmlScript> componentScripts = selectScriptsBySrc(page, componentJs);

        assertEquals(1, componentScripts.size());
    }

    public void testRenderOverridenStyle() throws Exception {
        List<UIComponent> childrenList = facesContext.getViewRoot().getChildren();
        UIResource userXcssComponent = createLoadStyleComponent();

        userXcssComponent.setSrc(RESOURCE_URI_PREFIX + userXcss);
        childrenList.add(userXcssComponent);

        InternetResourceBuilder resourceBuilder = InternetResourceBuilder.getInstance();

        childrenList.add(new ResourceDependentComponent(false, resourceBuilder.getResourceForKey(componentXcss),
                resourceBuilder.getResourceForKey(userXcss)));

        UIResource componentXcssComponent = createLoadStyleComponent();

        componentXcssComponent.setSrc(RESOURCE_URI_PREFIX + componentXcss);
        childrenList.add(componentXcssComponent);

        HtmlPage page = renderView();
        List<HtmlLink> styles = selectStylesByHhref(page, userXcss);

        assertEquals(2, styles.size());
        assertEquals("component", styles.get(0).getClassAttribute());
        assertEquals("user", styles.get(1).getClassAttribute());
        styles = selectStylesByHhref(page, componentXcss);
        assertEquals(2, styles.size());
        assertEquals("component", styles.get(0).getClassAttribute());
        assertEquals("user", styles.get(1).getClassAttribute());
    }

    public void testRenderOverridenAjaxScript() throws Exception {
        UIResource userComponent = createLoadScriptComponent();

        userComponent.setSrc(RESOURCE_URI_PREFIX + "/org/ajax4jsf/javascript/scripts/AJAX.js");

        List<UIComponent> childrenList = facesContext.getViewRoot().getChildren();

        childrenList.add(userComponent);
        childrenList.add(new ResourceDependentComponent(true, new AjaxScript()));

        HtmlPage page = renderView();
        List<HtmlScript> scripts = selectScriptsBySrc(page, "AjaxScript");

        assertEquals(1, scripts.size());
        scripts = selectScriptsBySrc(page, "AJAX.js");

        // TODO change to 0 after related issue resolution
        assertEquals(1, scripts.size());
    }
}

class ResourceDependentComponent extends UIComponentBase {
    static String COMPONENT_TYPE = "org.ajax4jsf.component.Test$ResourceDependentComponent";
    static String COMPONENT_FAMILY = COMPONENT_TYPE;
    private InternetResource[] resources;
    private boolean useScripts;

    ResourceDependentComponent(boolean useScripts, InternetResource... resources) {
        this.useScripts = useScripts;
        this.resources = resources;
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return COMPONENT_TYPE;
    }

    public InternetResource[] getScripts() {
        return useScripts ? resources : null;
    }

    public InternetResource[] getStyles() {
        return useScripts ? null : resources;
    }
}

class ResourceDependentComponentRenderer extends RendererBase implements HeaderResourceProducer2 {
    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return ResourceDependentComponent.class;
    }

    public void encodeToHead(FacesContext context, UIComponent component, ProducerContext pc) throws IOException {
        ResourceDependentComponent resourceDependentComponent = (ResourceDependentComponent) component;

        if (pc.isProcessScripts()) {
            InternetResource[] scripts = resourceDependentComponent.getScripts();

            if (scripts != null) {
                for (InternetResource script : scripts) {
                    script.encode(context, null);
                }
            }
        }

        if (pc.isProcessStyles()) {
            InternetResource[] styles = resourceDependentComponent.getStyles();

            if (styles != null) {
                for (InternetResource script : styles) {
                    script.encode(context, null);
                }
            }
        }
    }
}
