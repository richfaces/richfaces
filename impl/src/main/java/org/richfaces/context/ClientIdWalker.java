/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.context;

import static org.richfaces.util.Util.NamingContainerDataHolder.SEPARATOR_CHAR_JOINER;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.context.IdParser.Node;

import com.google.common.collect.Lists;

class ClientIdWalker {

    private Node[] parsedId;

    private UIComponent functionTarget;

    private Collection<String> resolvedIds = Lists.newArrayList();

    public ClientIdWalker(Node[] parsedId) {
        super();
        this.parsedId = parsedId;
    }

    private void walk(FacesContext facesContext, UIComponent component, String baseId, int nodeIdx) {

        boolean isLastNode = (nodeIdx == parsedId.length - 1);

        Node node = parsedId[nodeIdx];

        Collection<String> directSubtreeIds;
        UIComponent childComponent;

        if (node.getFunction() == null) {
            directSubtreeIds = Collections.singleton(node.getImage());
            childComponent = component;
        } else {
            directSubtreeIds = evaluateFunction(facesContext, component, baseId, node);
            //functionTarget is set inside evaluateFunction(...) call!
            childComponent = functionTarget;
        }

        for (String directSubtreeId : directSubtreeIds) {
            String clientId = SEPARATOR_CHAR_JOINER.join(baseId, directSubtreeId);

            if (isLastNode) {
                resolvedIds.add(clientId);
            } else {
                walk(facesContext, childComponent, clientId, nodeIdx + 1);
            }
        }
    }

    private Collection<String> evaluateFunction(FacesContext facesContext, UIComponent component, String baseId, Node node) {
        Collection<String> directSubtreeIds;
        String function = node.getFunction();
        String image = node.getImage();

        if (!"rows".equals(function)) {
            throw new IllegalArgumentException(MessageFormat.format("Function {0} is not supported", function));
        }

        RowsFunctionContextCallback rowsFunctionCallback = new RowsFunctionContextCallback(image);

        if (!component.invokeOnComponent(facesContext, baseId, rowsFunctionCallback)) {
            throw new IllegalStateException(MessageFormat.format("Failed to visit {0}", baseId));
        }

        functionTarget = rowsFunctionCallback.getComponent();
        directSubtreeIds = rowsFunctionCallback.getConvertedKeys();
        return directSubtreeIds;
    }

    public void walk(FacesContext facesContext) {
        walk(facesContext, facesContext.getViewRoot(), null, 0);
    }

    public Collection<String> getResolvedIds() {
        return resolvedIds;
    }
}