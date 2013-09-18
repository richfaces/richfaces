/*
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
package org.richfaces.component;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.DeclarativeModelKey;
import org.richfaces.model.SequenceRowKey;
import org.richfaces.ui.common.ComponentPredicates;

import com.google.common.collect.Iterables;

/**
 * @author Nick Belaevski
 *
 */
public class DeclarativeTreeDataModelWalker {
    private static final Logger LOGGER = RichfacesLogger.COMPONENTS.getLogger();
    private String var;
    private FacesContext facesContext;
    private UIComponent rootComponent;
    private UIComponent currentComponent;
    private Map<String, Object> contextMap;
    private Object modelData;
    private Object data;

    public DeclarativeTreeDataModelWalker(FacesContext facesContext, AbstractTree rootComponent) {
        super();

        this.rootComponent = rootComponent;
        this.facesContext = facesContext;
        this.contextMap = rootComponent.getVariablesMap(facesContext);
        this.var = rootComponent.getVar();
        this.currentComponent = rootComponent;
    }

    private void setupModelComponentContext(String modelId) {
        if (currentComponent instanceof TreeModelRecursiveAdaptor && modelId.equals(currentComponent.getId())) {
            // currentComponent already set
            modelData = ((TreeModelRecursiveAdaptor) currentComponent).getNodes();
        } else {
            currentComponent = Iterables.find(currentComponent.getChildren(), ComponentPredicates.withId(modelId));
            if (currentComponent instanceof TreeModelRecursiveAdaptor) {
                modelData = ((TreeModelRecursiveAdaptor) currentComponent).getRoots();
            } else {
                modelData = ((TreeModelAdaptor) currentComponent).getNodes();
            }
        }
    }

    private void setupDataModelContext(Object key) {
        if (modelData instanceof Iterable<?>) {
            Iterable<?> iterable = (Iterable<?>) modelData;
            Integer index = (Integer) key;

            if (index < Iterables.size(iterable)) {
                data = Iterables.get(iterable, index);
            } else {
                data = null;
            }
        } else {
            data = ((Map<?, ?>) modelData).get(key);
        }
    }

    private void resetForDataNotAvailable() {
        data = null;
        currentComponent = rootComponent;
    }

    protected FacesContext getFacesContext() {
        return facesContext;
    }

    protected UIComponent getRootComponent() {
        return rootComponent;
    }

    public UIComponent getCurrentComponent() {
        return currentComponent;
    }

    public Object getData() {
        return data;
    }

    public void walk(SequenceRowKey key) {
        Object initialContextValue = null;

        if (var != null) {
            initialContextValue = contextMap.remove(var);
        }

        try {
            for (Object simpleKey : key.getSimpleKeys()) {
                DeclarativeModelKey declarativeKey = (DeclarativeModelKey) simpleKey;
                if (var != null) {
                    contextMap.put(var, data);
                }

                setupModelComponentContext(declarativeKey.getModelId());

                if (modelData == null) {
                    resetForDataNotAvailable();
                    break;
                }

                setupDataModelContext(declarativeKey.getModelKey());

                if (data == null) {
                    resetForDataNotAvailable();
                    break;
                }
            }
        } finally {
            if (var != null) {
                try {
                    contextMap.put(var, initialContextValue);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
}
