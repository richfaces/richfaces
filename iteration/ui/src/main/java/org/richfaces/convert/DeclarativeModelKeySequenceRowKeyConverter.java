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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.IntegerConverter;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.DeclarativeTreeDataModelWalker;
import org.richfaces.model.DeclarativeModelKey;
import org.richfaces.model.SequenceRowKey;
/**
 * @author Nick Belaevski
 * 
 */
public class DeclarativeModelKeySequenceRowKeyConverter extends SequenceRowKeyConverter<DeclarativeModelKey> {

    private final class KeyConvertingWalker extends DeclarativeTreeDataModelWalker {

        private final FacesContext context;

        private DeclarativeModelKey[] convertedSimpleKeys;

        private int keysIdx;
        
        private KeyConvertingWalker(AbstractTree rootComponent, FacesContext context) {
            super(rootComponent);
            this.context = context;
        }

        @Override
        protected DeclarativeModelKey convertKey(Object nodes, DeclarativeModelKey declarativeModelKey) {
            DeclarativeModelKey convertedKey;
            
            if (nodes instanceof Iterable<?>) {
                String modelKeyAsString = (String) declarativeModelKey.getModelKey();
                Object modelKey = INTEGER_CONVERTER.getAsObject(context, getRootComponent(), modelKeyAsString);
                convertedKey = new DeclarativeModelKey(declarativeModelKey.getModelId(), modelKey);
            } else {
                convertedKey = declarativeModelKey;
            }

            convertedSimpleKeys[keysIdx++] = convertedKey;
            
            return super.convertKey(nodes, convertedKey);
        }
        
        @Override
        public void walk(SequenceRowKey key) {
            convertedSimpleKeys = new DeclarativeModelKey[key.getSimpleKeys().length];
            keysIdx = 0;
            super.walk(key);
        }

        public DeclarativeModelKey[] getConvertedSimpleKeys() {
            return convertedSimpleKeys;
        }
    }

    private static final Converter INTEGER_CONVERTER = new IntegerConverter();
    
    public DeclarativeModelKeySequenceRowKeyConverter() {
        super(DeclarativeModelKey.class, new DeclarativeModelKeyConverter(ConverterUtil.stringConverter()));
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        SequenceRowKey key = (SequenceRowKey) super.getAsObject(context, component, value);
        
        if (key != null) {
            
            KeyConvertingWalker walker = new KeyConvertingWalker((AbstractTree) component, context);
            walker.walk(key);
            
            key = new SequenceRowKey((Object[]) walker.getConvertedSimpleKeys());
        }
        
        return key;
    }

}
