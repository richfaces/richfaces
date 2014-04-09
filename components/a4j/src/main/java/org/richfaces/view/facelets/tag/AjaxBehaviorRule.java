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

import javax.el.MethodExpression;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.ajax4jsf.component.AjaxClientBehavior;
import org.ajax4jsf.component.behavior.AjaxBehavior;
import org.ajax4jsf.component.behavior.MethodExpressionAjaxBehaviorListener;

/**
 * @author Anton Belevich
 *
 */
public class AjaxBehaviorRule extends BehaviorRule {
    public static final Class<?>[] AJAX_BEHAVIOR_LISTENER_SIG = new Class[] { AjaxBehaviorEvent.class };
    private static final Class<?>[] AJAX_BEHAVIOR_ZEROARG_SIG = new Class[] {};
    public static final AjaxBehaviorRule INSTANCE = new AjaxBehaviorRule();
    public static final String EXECUTE = "execute";
    public static final String RENDER = "render";
    public static final String LISTENER = "listener";

    @Override
    public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {

        if (meta.isTargetInstanceOf(AjaxBehavior.class)) {

            if (!attribute.isLiteral()) {

                if (LISTENER.equals(name)) {
                    return new AjaxBehaviorListenerMapper(attribute);
                }

                Class<?> type = meta.getPropertyType(name);

                if (EXECUTE.equals(name) || RENDER.equals(name)) {
                    type = Object.class;
                }

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

    public static final class AjaxBehaviorListenerMapper extends Metadata {
        private final TagAttribute attr;

        public AjaxBehaviorListenerMapper(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            MethodExpression methodExpressionOneArg = this.attr
                .getMethodExpression(ctx, null, AjaxBehaviorRule.AJAX_BEHAVIOR_LISTENER_SIG);
            MethodExpression methodExpressionZeroArg = this.attr
                    .getMethodExpression(ctx, null, AjaxBehaviorRule.AJAX_BEHAVIOR_ZEROARG_SIG);
            ((AjaxClientBehavior) instance).addAjaxBehaviorListener(new MethodExpressionAjaxBehaviorListener(methodExpressionOneArg, methodExpressionZeroArg));
        }
    }
}
