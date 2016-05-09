/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.view.facelets;

import javax.faces.convert.Converter;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.component.UIDataAdaptor;

/**
 * Apply rowKeyConverter to component
 *
 * @author Maksim Kaszynski
 * @since 3.3.1
 */
public class RowKeyConverterRule extends MetaRule {
    public static final RowKeyConverterRule INSTANCE = new RowKeyConverterRule();

    @Override
    public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
        if (meta.isTargetInstanceOf(UIDataAdaptor.class)) {
            if ("rowKeyConverter".equals(name)) {
                if (attribute.isLiteral()) {
                    return new StaticConverterMetadata(attribute.getValue());
                } else {
                    return new DynamicConverterMetaData(attribute);
                }
            }
        }

        return null;
    }

    static final class DynamicConverterMetaData extends Metadata {
        private final TagAttribute attribute;

        DynamicConverterMetaData(TagAttribute attribute) {
            super();
            this.attribute = attribute;
        }

        @Override
        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIDataAdaptor) instance)
                .setValueExpression("rowKeyConverter", attribute.getValueExpression(ctx, Converter.class));
        }
    }

    static final class StaticConverterMetadata extends Metadata {
        private final String converterId;

        StaticConverterMetadata(String converterId) {
            super();
            this.converterId = converterId;
        }

        @Override
        public void applyMetadata(FaceletContext ctx, Object instance) {
            Converter converter = ctx.getFacesContext().getApplication().createConverter(converterId);

            ((UIDataAdaptor) instance).setRowKeyConverter(converter);
        }
    }
}
