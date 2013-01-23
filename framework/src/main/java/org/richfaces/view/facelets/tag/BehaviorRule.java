/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.view.facelets.tag;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.ui.behavior.ClientBehavior;

/**
 * @author Anton Belevich
 *
 */
public class BehaviorRule extends MetaRule {
    public static final BehaviorRule INSTANCE = new BehaviorRule();

    static final class LiteralAttributeMetadata extends Metadata {
        private final String name;
        private final String value;

        public LiteralAttributeMetadata(String name, String value) {
            this.value = value;
            this.name = name;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ClientBehavior) instance).setLiteralAttribute(this.name, this.value);
        }
    }

    static final class ValueExpressionMetadata extends Metadata {
        private final String name;
        private final TagAttribute attr;
        private final Class<?> type;

        public ValueExpressionMetadata(String name, Class<?> type, TagAttribute attr) {
            this.name = name;
            this.attr = attr;
            this.type = type;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ClientBehavior) instance).setValueExpression(this.name, this.attr.getValueExpression(ctx, this.type));
        }
    }

    @Override
    public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
        if (meta.isTargetInstanceOf(ClientBehavior.class)) {
            if (!attribute.isLiteral()) {
                Class<?> type = meta.getPropertyType(name);
                if (type == null) {
                    type = Object.class;
                }
                return new ValueExpressionMetadata(name, type, attribute);
            } else if (meta != null && meta.getWriteMethod(name) != null) {
                return new LiteralAttributeMetadata(name, attribute.getValue());
            }
        }

        return null;
    }
}
