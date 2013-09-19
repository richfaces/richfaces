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
package org.richfaces.view.facelets.html;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeSource;
import org.richfaces.event.MethodExpressionItemChangeListener;

/**
 * @author akolonitsky
 * @since Dec 2, 2010
 */
public class PanelMenuTagHandler extends ComponentHandler {
    private static final MetaRule META_RULE = new PanelMenuMetaRule();

    public PanelMenuTagHandler(ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(META_RULE);
        return metaRuleset;
    }

    private static class PanelMenuMetaRule extends MetaRule {
        @Override
        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(ItemChangeSource.class)) {
                if ("itemChangeListener".equals(name)) {
                    return new ItemChangeExpressionMetadata(attribute);
                }
            }

            return null;
        }
    }

    private static final class ItemChangeExpressionMetadata extends Metadata {
        private static final Class<?>[] ITEM_CHANGE_SIG = new Class[] { ItemChangeEvent.class };
        private final TagAttribute attr;

        ItemChangeExpressionMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        @Override
        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ItemChangeSource) instance).addItemChangeListener(new MethodExpressionItemChangeListener(this.attr
                .getMethodExpression(ctx, null, ITEM_CHANGE_SIG)));
        }
    }
}
