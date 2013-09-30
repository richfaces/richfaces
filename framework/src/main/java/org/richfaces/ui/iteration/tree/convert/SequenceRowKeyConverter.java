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

package org.richfaces.ui.iteration.tree.convert;

import static org.richfaces.model.TreeDataModel.SEPARATOR_CHAR;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.richfaces.model.SequenceRowKey;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

/**
 * @author Nick Belaevski
 * @since 3.3.1
 */
public class SequenceRowKeyConverter<T> implements Converter {
    static final Splitter SEPARATOR_SPLITTER = Splitter.on(SEPARATOR_CHAR);
    private Class<T> clazz;
    private Converter delegateConverter;

    public SequenceRowKeyConverter(Class<T> clazz, Converter delegateConverter) {
        super();
        this.clazz = clazz;
        this.delegateConverter = delegateConverter;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }

        Iterable<String> split = SEPARATOR_SPLITTER.split(value);
        List<T> keysList = Lists.<T>newArrayList();

        for (String s : split) {
            T convertedKey = clazz.cast(delegateConverter.getAsObject(context, component, s));
            keysList.add(convertedKey);
        }

        return new SequenceRowKey(keysList.toArray(ObjectArrays.newArray(clazz, keysList.size())));
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        SequenceRowKey sequenceRowKey = (SequenceRowKey) value;

        StringBuilder result = new StringBuilder();

        for (Object simpleKey : sequenceRowKey.getSimpleKeys()) {
            String convertedKey = delegateConverter.getAsString(context, component, simpleKey);

            if (result.length() > 0) {
                result.append(SEPARATOR_CHAR);
            }

            result.append(convertedKey);
        }

        return result.toString();
    }
}
