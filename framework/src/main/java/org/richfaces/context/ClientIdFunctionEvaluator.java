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

class ClientIdFunctionEvaluator {
    private FacesContext context;
    private UIComponent functionTarget;
    private Node[] parsedId;
    private Collection<String> resolvedIds = Lists.newArrayListWithCapacity(1);

    public ClientIdFunctionEvaluator(FacesContext context, Node[] parsedId) {
        super();
        this.context = context;
        this.parsedId = parsedId;
    }

    private void walk(UIComponent component, String baseId, int nodeIdx) {

        boolean isLastNode = (nodeIdx == parsedId.length - 1);

        Node node = parsedId[nodeIdx];

        Collection<String> directSubtreeIds;
        UIComponent childComponent;

        if (node.getFunction() == null) {
            directSubtreeIds = Collections.singleton(node.getImage());
            childComponent = component;
        } else {
            directSubtreeIds = evaluateFunction(component, baseId, node);
            // functionTarget is set inside evaluateFunction(...) call!
            childComponent = functionTarget;
        }

        for (String directSubtreeId : directSubtreeIds) {
            String clientId = SEPARATOR_CHAR_JOINER.join(baseId, directSubtreeId);

            if (isLastNode) {
                resolvedIds.add(clientId);
            } else {
                walk(childComponent, clientId, nodeIdx + 1);
            }
        }
    }

    private Collection<String> evaluateFunction(UIComponent component, String baseId, Node node) {
        Collection<String> directSubtreeIds;
        String function = node.getFunction();
        String image = node.getImage();

        if (!"rows".equals(function)) {
            throw new IllegalArgumentException(MessageFormat.format("Function {0} is not supported", function));
        }

        RowsFunctionContextCallback rowsFunctionCallback = new RowsFunctionContextCallback(image);

        if (!component.invokeOnComponent(context, baseId, rowsFunctionCallback)) {
            throw new IllegalStateException(MessageFormat.format("Failed to visit {0}", baseId));
        }

        functionTarget = rowsFunctionCallback.getComponent();
        directSubtreeIds = rowsFunctionCallback.getConvertedKeys();
        return directSubtreeIds;
    }

    public Collection<String> evaluate(UIComponent component) {
        walk(component, null, 0);
        return resolvedIds;
    }
}