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
package org.richfaces.context;

import java.util.Collection;

import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.google.common.collect.Lists;

final class RowsFunctionContextCallback implements ContextCallback {
    private final String image;
    private UIComponent component;
    private Collection<String> convertedKeys = Lists.newArrayList();

    RowsFunctionContextCallback(String image) {
        this.image = image;
    }

    public void invokeContextCallback(FacesContext context, UIComponent target) {
        component = target;

        Converter rowKeyConverter = (Converter) target.getAttributes().get("rowKeyConverter");

        Collection<?> keys = (Collection<?>) context.getApplication().evaluateExpressionGet(context, "#{" + image + "}",
            Object.class);

        if (keys == null) {
            return;
        }

        for (Object key : keys) {
            String convertedKey;

            if (rowKeyConverter != null) {
                convertedKey = rowKeyConverter.getAsString(context, target, key);
            } else {
                convertedKey = key.toString();
            }

            convertedKeys.add(convertedKey);
        }
    }

    public UIComponent getComponent() {
        return component;
    }

    public Collection<String> getConvertedKeys() {
        return convertedKeys;
    }
}