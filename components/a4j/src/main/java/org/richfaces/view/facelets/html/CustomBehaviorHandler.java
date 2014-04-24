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

import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.CompositeFaceletHandler;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.TagHandler;
import javax.faces.view.facelets.TagHandlerDelegate;

import org.richfaces.view.facelets.tag.BehaviorRule;

/**
 * @author Anton Belevich
 *
 */
public class CustomBehaviorHandler extends BehaviorHandler {
    TagHandlerDelegate helper;

    public CustomBehaviorHandler(BehaviorConfig config) {
        super(config);
    }

    public boolean isWrapping() {
        return ((this.nextHandler instanceof TagHandler) || (this.nextHandler instanceof CompositeFaceletHandler));
    }

    public boolean isWrappingAttachQueue() {
        return (this.nextHandler instanceof AttachQueueHandler);
    }

    public MetaRule[] getMetaRules() {
        return new MetaRule[] { BehaviorRule.INSTANCE };
    }

    @Override
    protected TagHandlerDelegate getTagHandlerDelegate() {
        if (helper == null) {
            helper = new BehaviorTagHandlerDelegate(this, delegateFactory.createBehaviorHandlerDelegate(this));
        }
        return helper;
    }
}
