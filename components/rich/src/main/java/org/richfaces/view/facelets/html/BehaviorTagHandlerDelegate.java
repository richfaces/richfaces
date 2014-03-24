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
package org.richfaces.view.facelets.html;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagHandlerDelegate;

import org.richfaces.ui.behavior.ClientBehavior;

/**
 * @author Anton Belevich
 *
 */
public class BehaviorTagHandlerDelegate extends TagHandlerDelegate implements AttachedObjectHandler {
    TagHandlerDelegate wrappedHandlerDelegate;
    CustomBehaviorHandler owner;
    private String behaviorId;
    private String eventName;
    private MetaRule[] metaRules;

    public BehaviorTagHandlerDelegate(CustomBehaviorHandler owner, TagHandlerDelegate wrappedHandlerDelegate) {
        this.owner = owner;
        this.wrappedHandlerDelegate = wrappedHandlerDelegate;
        this.behaviorId = owner.getBehaviorId();
        this.eventName = owner.getEventName();
        this.metaRules = owner.getMetaRules();
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {

        if (owner.isWrapping()) {

            Application application = ctx.getFacesContext().getApplication();
            Behavior behavior = application.createBehavior(this.behaviorId);

            if (behavior instanceof ClientBehavior) {
                ClientBehavior clientBehavior = (ClientBehavior) behavior;
                owner.setAttributes(ctx, clientBehavior);

                FacesContext context = ctx.getFacesContext();
                BehaviorStack behaviorStack = BehaviorStack.getBehaviorStack(context, true);

                behaviorStack.pushBehavior(context, clientBehavior, this.behaviorId, this.eventName);

                owner.applyNextHandler(ctx, parent);

                behaviorStack.popBehavior();
            }
        } else {
            wrappedHandlerDelegate.apply(ctx, parent);
        }
    }

    public MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = wrappedHandlerDelegate.createMetaRuleset(type);
        for (MetaRule metaRule : metaRules) {
            metaRuleset.addRule(metaRule);
        }
        return metaRuleset;
    }

    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        if (wrappedHandlerDelegate instanceof AttachedObjectHandler) {
            ((AttachedObjectHandler) wrappedHandlerDelegate).applyAttachedObject(context, parent);
        }
    }

    public String getFor() {
        if (wrappedHandlerDelegate instanceof AttachedObjectHandler) {
            return ((AttachedObjectHandler) wrappedHandlerDelegate).getFor();
        }
        return null;
    }
}
