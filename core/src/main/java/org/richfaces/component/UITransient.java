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

package org.richfaces.component;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UniqueIdVendor;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.Renderer;

import org.richfaces.renderkit.html.ScriptsRenderer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public abstract class UITransient extends UIComponentBase {
    private String id;
    private UIComponent parent;
    private final Map<String, Object> attributesMap = new AttributesMap();
    private String clientId;

    public UITransient() {
        super();
    }

    @Override
    public Object saveState(FacesContext context) {
        // This is transient component
        return null;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        // Do nothing, this component never saved

    }

    @Override
    public boolean isTransient() {
        return true;
    }

    @Override
    public void setTransient(boolean newTransientValue) {

    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributesMap;
    }

    @SuppressWarnings("deprecation")
    @Override
    public javax.faces.el.ValueBinding getValueBinding(String name) {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setValueBinding(String name, javax.faces.el.ValueBinding binding) {
        // do nothing

    }

    @Override
    public ValueExpression getValueExpression(String name) {
        return null;
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        // do nothing
    }

    @Override
    public String getClientId(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // if the clientId is not yet set
        if (this.clientId == null) {
            UIComponent namingContainerAncestor = this.getNamingContainer();
            String parentId = null;

            // give the parent the opportunity to first
            // grab a unique clientId
            if (namingContainerAncestor != null) {
                parentId = namingContainerAncestor.getContainerClientId(context);
            }

            // now resolve our own client id
            String clientId = getId();
            if (clientId == null) {
                if (null != namingContainerAncestor && namingContainerAncestor instanceof UniqueIdVendor) {
                    clientId = ((UniqueIdVendor) namingContainerAncestor).createUniqueId(context, null);
                } else {
                    clientId = context.getViewRoot().createUniqueId();
                }
                setId(clientId);
            }
            if (parentId != null) {
                StringBuilder idBuilder = new StringBuilder(parentId.length() + 1 + clientId.length());
                clientId = idBuilder.append(parentId).append(UINamingContainer.getSeparatorChar(context)).append(clientId)
                        .toString();
            }

            // allow the renderer to convert the clientId
            Renderer renderer = this.getRenderer(context);
            if (renderer != null) {
                this.clientId = renderer.convertClientId(context, clientId);
            } else {
                this.clientId = clientId;
            }
        }
        return this.clientId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
        this.clientId = null;
    }

    @Override
    public UIComponent getParent() {
        return this.parent;
    }

    @Override
    public void setParent(UIComponent parent) {
        this.parent = parent;
    }

    @Override
    public boolean isRendered() {
        return true;
    }

    @Override
    public void setRendered(boolean rendered) {
        // always rendered
    }

    @Override
    public String getRendererType() {
        return ScriptsRenderer.RENDERER_TYPE;
    }

    @Override
    public void setRendererType(String rendererType) {
        // do nothing

    }

    @Override
    public boolean getRendersChildren() {
        return false;
    }

    @Override
    public List<UIComponent> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public UIComponent findComponent(String expr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, UIComponent> getFacets() {
        return Collections.emptyMap();
    }

    @Override
    public UIComponent getFacet(String name) {
        return null;
    }

    @Override
    public Iterator<UIComponent> getFacetsAndChildren() {
        return ImmutableSet.<UIComponent>of().iterator();
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        // Do nothing
    }

    @Override
    public void decode(FacesContext context) {
        // Do nothing
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        Renderer renderer = getRenderer(context);
        if (null != renderer) {
            renderer.encodeBegin(context, this);
        }
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        Renderer renderer = getRenderer(context);
        if (null != renderer) {
            renderer.encodeChildren(context, this);
        }
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        Renderer renderer = getRenderer(context);
        if (null != renderer) {
            renderer.encodeEnd(context, this);
        }
    }

    @Override
    protected void addFacesListener(FacesListener listener) {
    }

    @Override
    protected FacesListener[] getFacesListeners(Class clazz) {
        return new FacesListener[0];
    }

    @Override
    protected void removeFacesListener(FacesListener listener) {
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if (null != getParent()) {
            getParent().queueEvent(event);
        }
    }

    @Override
    public void processRestoreState(FacesContext context, Object state) {
        // Do nothing, this component does not have state.

    }

    @Override
    public void processDecodes(FacesContext context) {
        // Do nothing

    }

    @Override
    public void processValidators(FacesContext context) {
        // Do nothing

    }

    @Override
    public void processUpdates(FacesContext context) {
        // Do nothing
    }

    @Override
    public Object processSaveState(FacesContext context) {
        // Should never be used
        return null;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected Renderer getRenderer(FacesContext context) {
        String rendererType = getRendererType();
        Renderer result = null;
        if (rendererType != null) {
            result = context.getRenderKit().getRenderer(getFamily(), rendererType);
        }
        return result;
    }

    final class AttributesMap implements Map<String, Object> {
        @Override
        public void clear() {
            // do nothing

        }

        @Override
        public boolean containsKey(Object key) {

            return "target".equals(key) || "id".equals(key) || "clientId".equals(key) || hasAttribute(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Set<java.util.Map.Entry<String, Object>> entrySet() {
            return Collections.emptySet();
        }

        @Override
        public Object get(Object key) {
            if ("id".equals(key)) {
                return getId();
            } else if ("clientId".equals(key)) {
                return getClientId();
            } else {
                return getAttribute(key);
            }
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Set<String> keySet() {
            return ImmutableSet.of("target", "id", "clientId");
        }

        @Override
        public Object put(String key, Object value) {
            if ("id".equals(key)) {
                String id = getId();
                setId((String) value);
                return id;
            } else {
                return setAttribute(key, value);
            }
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> m) {

        }

        @Override
        public Object remove(Object key) {
            return null;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public Collection<Object> values() {
            return ImmutableList.<Object>of(getId(), getClientId());
        }
    }

    @Override
    public List<SystemEventListener> getListenersForEventClass(Class<? extends SystemEvent> eventClass) {
        return Collections.EMPTY_LIST;
    }

    protected abstract boolean hasAttribute(Object key);

    protected abstract Object setAttribute(String key, Object value);

    protected abstract Object getAttribute(Object key);
}
