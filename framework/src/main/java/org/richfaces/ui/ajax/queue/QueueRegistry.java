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
package org.richfaces.ui.ajax.queue;

import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Nick Belaevski
 * @since 3.3.0
 */
public final class QueueRegistry {
    public static final String QUEUE_SCRIPT_ID = "_org_richfaces_queue";
    private static final Logger LOGGER = RichfacesLogger.COMPONENTS.getLogger();
    private static final String REGISTRY_ATTRIBUTE_NAME = QueueRegistry.class.getName();
    private Map<String, UIComponent> queuesData = new LinkedHashMap<String, UIComponent>();

    private QueueRegistry() {
    }

    public static QueueRegistry getInstance(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        QueueRegistry registry = (QueueRegistry) requestMap.get(REGISTRY_ATTRIBUTE_NAME);

        if (registry == null) {
            registry = new QueueRegistry();
            requestMap.put(REGISTRY_ATTRIBUTE_NAME, registry);
        }

        return registry;
    }

    public void addQueue(String clientName, UIComponent component) {
        if (!containsQueue(clientName)) {
            queuesData.put(clientName, component);
        } else {
            LOGGER.warn("Queue with name '" + clientName + "' has already been registered");
        }
    }

    public UIComponent removeQueue(String clientName) {
        return queuesData.remove(clientName);
    }

    public boolean containsQueue(String name) {
        return queuesData.containsKey(name);
    }

    public Map<String, UIComponent> getRegisteredQueues() {
        return queuesData;
    }

    public boolean hasQueuesToEncode() {
        if (queuesData.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, UIComponent> queue : queuesData.entrySet()) {
            if (queue.getValue().isRendered()) {
                return true;
            }
        }
        return false;
    }
}