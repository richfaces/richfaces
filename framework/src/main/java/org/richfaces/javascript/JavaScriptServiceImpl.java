/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.javascript;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.richfaces.component.UIScripts;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class JavaScriptServiceImpl implements JavaScriptService {
    /**
     * <p class="changed_added_4_0">
     * 'Special Case' object to return if no Component with scripts added to View Root.
     * </p>
     *
     */
    private static final ScriptsHolder EMPTY_SCRIPTS_HOLDER = new ScriptsHolder() {
        public Collection<Object> getScripts() {
            return Collections.emptySet();
        }

        public Collection<Object> getPageReadyScripts() {
            return Collections.emptySet();
        }
    };

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.JavaScriptService#addScript(org.ajax4jsf.javascript.ScriptString)
     */
    public <S> S addScript(FacesContext facesContext, S script) {
        UIScripts scriptResource = getOrCreateScriptResource(facesContext);

        return addOrFind(scriptResource.getScripts(), script);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.javascript.JavaScriptService#addPageReadyScript(org.ajax4jsf.javascript.ScriptString)
     */
    public <S> S addPageReadyScript(FacesContext facesContext, S script) {
        UIScripts scriptResource = getOrCreateScriptResource(facesContext);

        return addOrFind(scriptResource.getPageReadyScripts(), script);
    }

    @SuppressWarnings("unchecked")
    private <S> S addOrFind(Collection<Object> collection, S script) {
        for (Object object : collection) {
            if (script.equals(object)) {
                return (S) object;
            }
        }
        collection.add(script);
        return script;
    }

    /**
     * <p class="changed_added_4_0">
     * This method looks for {@link UIScripts} component in view resource. If such resource not found, it creates a new instance
     * and stores it in {@link UIViewRoot} view resource with default target.
     * </p>
     *
     * @param facesContext
     * @return
     */
    UIScripts getOrCreateScriptResource(FacesContext facesContext) {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (null == viewRoot) {
            throw new FacesException("View is not created");
        }
        UIScripts scriptResource = findScriptResource(facesContext, viewRoot);
        if (null == scriptResource) {
            scriptResource = (UIScripts) facesContext.getApplication().createComponent(UIScripts.COMPONENT_TYPE);
            viewRoot.addComponentResource(facesContext, scriptResource);
        }
        return scriptResource;
    }

    private UIScripts findScriptResource(FacesContext facesContext, UIViewRoot viewRoot) {
        List<UIComponent> componentResources = viewRoot.getComponentResources(facesContext, "form");
        UIScripts scriptResource = findScriptComponent(componentResources);
        if (null == scriptResource) {
            componentResources = viewRoot.getComponentResources(facesContext, "body");
            scriptResource = findScriptComponent(componentResources);
        }
        return scriptResource;
    }

    private UIScripts findScriptComponent(List<UIComponent> componentResources) {
        for (UIComponent uiComponent : componentResources) {
            if (uiComponent instanceof UIScripts) {
                UIScripts script = (UIScripts) uiComponent;
                return script;
            }
        }
        return null;
    }

    public ScriptsHolder getScriptsHolder(FacesContext context) {
        UIViewRoot viewRoot = context.getViewRoot();
        ScriptsHolder result = null;
        if (null != viewRoot) {
            result = findScriptResource(context, viewRoot);
        }
        if (null == result) {
            result = EMPTY_SCRIPTS_HOLDER;
        }
        return result;
    }
}
