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
package org.richfaces.convert;

import static org.richfaces.convert.SequenceRowKeyConverter.SEPARATOR_SPLITTER;
import static org.richfaces.convert.TreeConverterUtil.escape;
import static org.richfaces.convert.TreeConverterUtil.unescape;
import static org.richfaces.model.TreeDataModel.SEPARATOR_CHAR;

import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.richfaces.component.AbstractTree;
import org.richfaces.model.DeclarativeModelKey;
import org.richfaces.model.SequenceRowKey;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 *
 */
public class DeclarativeModelSequenceKeyConverter implements Converter {
    public static final String CONVERTER_ID = "org.richfaces.DeclarativeModelSequenceKeyConverter";

    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }

        Iterator<String> split = SEPARATOR_SPLITTER.split(value).iterator();

        AbstractTree tree = (AbstractTree) component;

        List<DeclarativeModelKey> declarativeKeys = Lists.newArrayList();

        while (split.hasNext()) {
            String modelId = unescape(split.next());
            String modelKeyAsString = unescape(split.next());

            DeclarativeModelKey declarativeKey = tree.convertDeclarativeKeyFromString(context, modelId, modelKeyAsString);

            declarativeKeys.add(declarativeKey);
        }

        return new SequenceRowKey((Object[]) declarativeKeys.toArray(new DeclarativeModelKey[declarativeKeys.size()]));
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        SequenceRowKey sequenceRowKey = (SequenceRowKey) value;

        Object[] declarativeKeys = sequenceRowKey.getSimpleKeys();
        AbstractTree tree = (AbstractTree) component;

        StringBuilder result = new StringBuilder();

        for (Object declarativeKeyObject : declarativeKeys) {
            DeclarativeModelKey declarativeKey = (DeclarativeModelKey) declarativeKeyObject;
            String modelId = escape(declarativeKey.getModelId());

            String modelKeyAsString = escape(tree.convertDeclarativeKeyToString(context, declarativeKey));

            if (result.length() != 0) {
                result.append(SEPARATOR_CHAR);
            }

            result.append(modelId);

            result.append(SEPARATOR_CHAR);
            result.append(modelKeyAsString);
        }

        return result.toString();
    }
}
