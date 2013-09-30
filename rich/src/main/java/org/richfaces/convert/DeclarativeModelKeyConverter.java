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

import static org.richfaces.component.util.Strings.NamingContainerDataHolder.SEPARATOR_CHAR_JOINER;
import static org.richfaces.component.util.Strings.NamingContainerDataHolder.SEPARATOR_CHAR_SPLITTER;
import static org.richfaces.convert.TreeConverterUtil.escape;
import static org.richfaces.convert.TreeConverterUtil.unescape;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.richfaces.model.DeclarativeModelKey;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 *
 */
public class DeclarativeModelKeyConverter implements Converter {
    private Converter delegateConverter;

    public DeclarativeModelKeyConverter(Converter delegateConverter) {
        super();
        this.delegateConverter = delegateConverter;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }

        String s = unescape(value);

        Iterator<String> split = SEPARATOR_CHAR_SPLITTER.split(s).iterator();

        String modelId = (String) split.next();
        Object modelKey = delegateConverter.getAsObject(context, component, split.next());

        if (split.hasNext()) {
            throw new ConverterException(value);
        }

        return new DeclarativeModelKey(modelId, modelKey);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        DeclarativeModelKey declarativeModelKey = (DeclarativeModelKey) value;

        String convertedModelKey = delegateConverter.getAsString(context, component, declarativeModelKey.getModelKey());
        String keyString = SEPARATOR_CHAR_JOINER.join(declarativeModelKey.getModelId(), convertedModelKey);

        return escape(keyString);
    }
}