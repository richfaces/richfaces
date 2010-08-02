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

package org.ajax4jsf.webapp.taglib;

import javax.faces.component.ActionSource;
import javax.faces.component.ActionSource2;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:21 $
 */
public class AjaxActionsRule extends MetaRule {
    public static final Class<?>[] ACTION_SIG = new Class[0];
    public static final Class<?>[] ACTION_LISTENER_SIG = new Class[]{ActionEvent.class};
    public static final AjaxActionsRule INSTANCE = new AjaxActionsRule();

    public AjaxActionsRule() {
        super();
    }

    @Override
    public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
        if (meta.isTargetInstanceOf(ActionSource.class)) {
            if ("action".equals(name)) {
                if (meta.isTargetInstanceOf(ActionSource2.class)) {
                    return new ActionMapper2(attribute);
                } else {
                    return new ActionMapper(attribute);
                }
            }

            if ("actionListener".equals(name)) {
                if (meta.isTargetInstanceOf(ActionSource2.class)) {
                    return new ActionListenerMapper2(attribute);
                } else {
                    return new ActionListenerMapper(attribute);
                }
            }
        }

        return null;
    }

    public static final class ActionListenerMapper extends Metadata {
        private final TagAttribute attr;

        public ActionListenerMapper(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ActionSource) instance).addActionListener(
                new MethodExpressionActionListener(
                    this.attr.getMethodExpression(ctx, null, AjaxActionsRule.ACTION_LISTENER_SIG)));
        }
    }

    static final class ActionListenerMapper2 extends Metadata {
        private final TagAttribute attr;

        public ActionListenerMapper2(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ActionSource2) instance).addActionListener(
                new MethodExpressionActionListener(
                    this.attr.getMethodExpression(ctx, null, AjaxActionsRule.ACTION_LISTENER_SIG)));
        }
    }

    public static final class ActionMapper extends Metadata {
        private final TagAttribute attr;

        public ActionMapper(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ActionSource2) instance).setActionExpression(this.attr.getMethodExpression(ctx, String.class,
                AjaxActionsRule.ACTION_SIG));
        }
    }

    static final class ActionMapper2 extends Metadata {
        private final TagAttribute attr;

        public ActionMapper2(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ActionSource2) instance).setActionExpression(this.attr.getMethodExpression(ctx, String.class,
                AjaxActionsRule.ACTION_SIG));
        }
    }
}
