/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.view.facelets.html;

import javax.faces.event.ActionEvent;
import javax.faces.view.facelets.*;
import org.richfaces.component.AbstractPush;
import org.richfaces.view.facelets.MethodMetadata;

import java.util.EventListener;

public class AjaxPushHandler extends ComponentHandler {

    private static final AjaxPushHandlerMetaRule META_RULE = new AjaxPushHandlerMetaRule();


    public AjaxPushHandler(ComponentConfig config) {
        super(config);
        
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset m = super.createMetaRuleset(type);
        m.addRule(META_RULE);
        return m;
    }

    static class AjaxPushHandlerMetaRule extends MetaRule{

        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AbstractPush.class)) {
                if ("action".equals(name)) {
                    return new MethodMetadata(attribute) {
                        public void applyMetadata(FaceletContext ctx, Object instance) {
                            ((AbstractPush) instance).setAction(getMethodBinding(ctx));
                        }
                    };
                }
                if ("actionExpression".equals(name)) {
                    return new MethodMetadata(attribute) {
                        public void applyMetadata(FaceletContext ctx, Object instance) {
                            ((AbstractPush) instance).setActionExpression(getMethodExpression(ctx));
                        }
                    };
                }
                if ("actionListener".equals(name)) {
                    return new MethodMetadata(attribute, ActionEvent.class) {
                        public void applyMetadata(FaceletContext ctx, Object instance) {
                            ((AbstractPush) instance).setActionListener(getMethodBinding(ctx));
                        }
                    };
                }
                if ("eventProducer".equals(name)) {
                    return new MethodMetadata(attribute, EventListener.class) {
                        public void applyMetadata(FaceletContext ctx, Object instance) {
                            ((AbstractPush) instance).setEventProducer(getMethodExpression(ctx));
                        }
                    };
                }
            }
            return null;
        }
    }
}
